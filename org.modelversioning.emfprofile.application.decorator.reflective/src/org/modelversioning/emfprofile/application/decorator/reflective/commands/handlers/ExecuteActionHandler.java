package org.modelversioning.emfprofile.application.decorator.reflective.commands.handlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.modelversioning.emfprofile.application.decorator.reflective.EMFProfileApplicationDecoratorImpl;

public class ExecuteActionHandler extends AbstractHandler {

	private static final String EXTENSION_POINT_ID = "org.modelversioning.emfprofile.actions.extensionpoint.actionhandlers";
	private static final String ATTRIBUTE_CLASS = "class";
	private static final String ATTRIBUTE_ACTION_ID = "actionId";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("execute");

		Map<String, Collection<IConfigurationElement>> actionIdToHandlerMap = new HashMap<>();

		for (IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID)) {
			String actionId = element.getAttribute(ATTRIBUTE_ACTION_ID);
			if (!actionIdToHandlerMap.containsKey(actionId)) {
				actionIdToHandlerMap.put(actionId, new LinkedList<IConfigurationElement>());
			}
			actionIdToHandlerMap.get(actionId).add(element);
		}

		if (EMFProfileApplicationDecoratorImpl.getPluginExtensionOperationsListener() != null) {
			ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
			if (currentSelection != null && currentSelection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) currentSelection;
				Object element = structuredSelection.getFirstElement();
				if (element instanceof EObject) {
					EObject selectedEObject = (EObject) element;
					EMFProfileApplicationDecoratorImpl.getPluginExtensionOperationsListener().executeAction(selectedEObject, actionIdToHandlerMap);
				}
			}
		} else {
			MessageDialog.openError(HandlerUtil.getActiveShell(event), "Missing Component", "There is no Plugin Extension Operations Listener registered!");
			System.err.println("There is no Plugin Extension Operations Listener registered!");
		}
		return null;

	}

	@Execute
	public void execute2(IExtensionRegistry registry) {
		System.out.println("execute2 " + registry);
	}
}
