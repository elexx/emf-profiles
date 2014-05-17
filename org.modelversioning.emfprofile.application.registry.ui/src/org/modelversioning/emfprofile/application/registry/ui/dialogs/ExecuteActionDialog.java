package org.modelversioning.emfprofile.application.registry.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.statushandlers.StatusManager;
import org.modelversioning.emfprofile.Action;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofile.action.ActionHandlerExecutor;
import org.modelversioning.emfprofile.application.registry.ui.EMFProfileApplicationRegistryUIPlugin;
import org.modelversioning.emfprofile.application.registry.ui.dialogs.utils.AutoExpandedTreeSelectionDialog;
import org.modelversioning.emfprofile.application.registry.ui.dialogs.utils.TreeObject;
import org.modelversioning.emfprofile.application.registry.ui.dialogs.utils.ViewContentProvider;
import org.modelversioning.emfprofile.application.registry.ui.providers.ProfileProviderLabelAdapter;
import org.modelversioning.emfprofile.application.registry.ui.views.EMFProfileApplicationsView;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

public class ExecuteActionDialog {

	private final ProfileProviderLabelAdapter labelAdapter = new ProfileProviderLabelAdapter(EMFProfileApplicationsView.getAdapterFactory());
	private final List<StereotypeApplication> stereotypeApplications;

	public ExecuteActionDialog(List<StereotypeApplication> stereotypeApplications) {
		this.stereotypeApplications = stereotypeApplications;
	}

	public void openExecuteActionDialog(EObject eObject) {
		Collection<TreeObject> parents = new ArrayList<>();
		Set<Stereotype> alreadyAddedStereotypes = new HashSet<>();

		for (StereotypeApplication stereotypeApplication : stereotypeApplications) {
			Stereotype stereotype = stereotypeApplication.getStereotype();

			if (!alreadyAddedStereotypes.contains(stereotype)) {
				TreeObject parent = new TreeObject(stereotype);
				for (Action action : stereotype.getActions()) {
					parent.addChild(new TreeObject(action, parent));
				}
				parents.add(parent);

				alreadyAddedStereotypes.add(stereotype);
			}
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

			MultiStatus allHandlerStatuses = new MultiStatus(EMFProfileApplicationRegistryUIPlugin.PLUGIN_ID, IStatus.INFO, "ExecuteAction results", null);
			for (Object selectedObject : selection) {
				TreeObject selectedTreeObject = (TreeObject) selectedObject;
				Action action = (Action) selectedTreeObject.getElement();

				List<StereotypeApplication> appliedStereotypes = getStereotypeApplications(eObject, (Stereotype) selectedTreeObject.getParent().getElement());

				IStatus handlerStatuses = ActionHandlerExecutor.getInstance().executeActions(action, appliedStereotypes);
				allHandlerStatuses.add(handlerStatuses);
			}
			if (allHandlerStatuses.isOK()) {
				StatusManager.getManager().handle(allHandlerStatuses, StatusManager.LOG);
			} else {
				StatusManager.getManager().handle(allHandlerStatuses, StatusManager.LOG | StatusManager.SHOW);
			}
		}
	}

	private List<StereotypeApplication> getStereotypeApplications(EObject eObject, Stereotype stereotype) {
		List<StereotypeApplication> appliedStereotypes = new LinkedList<>();
		for (StereotypeApplication stereotypeApplication : stereotypeApplications) {
			if (stereotypeApplication.getStereotype().equals(stereotype) && stereotypeApplication.getAppliedTo().equals(eObject)) {
				appliedStereotypes.add(stereotypeApplication);
			}
		}
		return appliedStereotypes;
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
