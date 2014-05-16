package org.modelversioning.emfprofile.application.registry.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.modelversioning.emfprofile.Action;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofile.action.ActionHandler;
import org.modelversioning.emfprofile.application.registry.ui.EMFProfileApplicationRegistryUIPlugin;
import org.modelversioning.emfprofile.application.registry.ui.dialogs.utils.AutoExpandedTreeSelectionDialog;
import org.modelversioning.emfprofile.application.registry.ui.dialogs.utils.TreeObject;
import org.modelversioning.emfprofile.application.registry.ui.dialogs.utils.ViewContentProvider;
import org.modelversioning.emfprofile.application.registry.ui.providers.ProfileProviderLabelAdapter;
import org.modelversioning.emfprofile.application.registry.ui.views.EMFProfileApplicationsView;

public class ExecuteActionDialog {

	private final ProfileProviderLabelAdapter labelAdapter = new ProfileProviderLabelAdapter(EMFProfileApplicationsView.getAdapterFactory());
	private final Map<Stereotype, Collection<Action>> executableActions;
	private final Map<String, Collection<IConfigurationElement>> actionIdToHandlerMap;

	public ExecuteActionDialog(Map<Stereotype, Collection<Action>> executableActions, Map<String, Collection<IConfigurationElement>> actionIdToHandlerMap) {
		this.executableActions = executableActions;
		this.actionIdToHandlerMap = actionIdToHandlerMap;
	}

	public void openExecuteActionDialog(EObject eObject) {
		Collection<TreeObject> parents = new ArrayList<>();
		for (Stereotype stereotype : executableActions.keySet()) {
			TreeObject parent = new TreeObject(stereotype);
			for (Action action : executableActions.get(stereotype)) {
				parent.addChild(new TreeObject(action, parent));
			}
			parents.add(parent);
		}

		AutoExpandedTreeSelectionDialog dialog = new AutoExpandedTreeSelectionDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), new ViewLabelProvider(), new ViewContentProvider());
		dialog.setTitle("Action Selection");
		dialog.setMessage("Select one or more Actions to execute");

		dialog.setInput(parents);
		dialog.setDoubleClickSelects(true);
		dialog.setValidator(new ISelectionStatusValidator() {
			@Override
			public IStatus validate(Object[] selection) {
				if (selection.length == 0)
					return new Status(IStatus.ERROR, EMFProfileApplicationRegistryUIPlugin.PLUGIN_ID, "No Action selected yet.");
				for (Object object : selection) {
					TreeObject treeObject = (TreeObject) object;
					if (treeObject.hasParent() && !treeObject.hasChildren())
						return new Status(IStatus.OK, EMFProfileApplicationRegistryUIPlugin.PLUGIN_ID, "");
				}
				return new Status(IStatus.ERROR, EMFProfileApplicationRegistryUIPlugin.PLUGIN_ID, "No Action selected yet.");
			}
		});
		int result = dialog.open();
		if (Dialog.OK == result) {
			Object[] selection = dialog.getResult();

			StringBuilder strBuilderSuccess = new StringBuilder();
			StringBuilder strBuilderFailed = new StringBuilder();
			for (Object selectedObject : selection) {
				TreeObject selectedTreeObject = (TreeObject) selectedObject;
				Action action = (Action) selectedTreeObject.getElement();
				if (actionIdToHandlerMap.containsKey(action.getId())) {
					for (IConfigurationElement element : actionIdToHandlerMap.get(action.getId())) {
						try {
							Object o = element.createExecutableExtension("class");
							((ActionHandler) o).doAction();
							strBuilderSuccess.append("Execution of ActionHandler (" + element.getAttribute("class") + ") successful.\n");
						} catch (CoreException e) {
							System.err.println(e.getMessage());
							strBuilderFailed.append("Execution of ActionHandler (" + element.getAttribute("class") + ") with ActionID (" + action.getId() + ") failed.\n");
						}
					}
				} else {
					strBuilderFailed.append("No ActionHandler registered for ActionID (" + action.getId() + ")\n");
				}
			}
			if (strBuilderSuccess.length() > 0) {
				MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_INFORMATION | SWT.OK);
				messageBox.setText("Successful ActionHandlers!");
				messageBox.setMessage(strBuilderSuccess.toString());
				messageBox.open();
			}
			if (strBuilderFailed.length() > 0) {
				MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
				messageBox.setText("Failed ActionHandlers!");
				messageBox.setMessage(strBuilderFailed.toString());
				messageBox.open();
			}
		}
	}

	final class ViewLabelProvider extends LabelProvider {
		@Override
		public String getText(Object obj) {
			Object element = ((TreeObject) obj).getElement();
			if (element instanceof Action) {
				Action action = (Action) element;
				return action.getName();
			} else {
				return labelAdapter.getText(element);
			}
		}

		@Override
		public Image getImage(Object obj) {
			Object element = ((TreeObject) obj).getElement();
			return labelAdapter.getImage(element);
		}
	}
}
