package org.modelversioning.emfprofile.application.decorator.reflective.commands.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.modelversioning.emfprofile.application.decorator.reflective.EMFProfileApplicationDecoratorImpl;

public class ExecuteActionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (EMFProfileApplicationDecoratorImpl.getPluginExtensionOperationsListener() != null) {
			ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
			if (currentSelection != null && currentSelection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) currentSelection;
				Object element = structuredSelection.getFirstElement();
				if (element instanceof EObject) {
					EObject selectedEObject = (EObject) element;
					EMFProfileApplicationDecoratorImpl.getPluginExtensionOperationsListener().executeAction(selectedEObject);
				}
			}
		} else {
			MessageDialog.openError(HandlerUtil.getActiveShell(event), "Missing Component", "There is no Plugin Extension Operations Listener registered!");
			System.err.println("There is no Plugin Extension Operations Listener registered!");
		}
		return null;
	}
}
