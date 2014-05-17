package org.modelversioning.emfprofile.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;
import org.modelversioning.emfprofile.Action;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

public class ActionHandlerExecutor {

	public static final String PLUGIN_ID = "org.modelversioning.emfprofile.actions";

	private static final String EXTENSION_POINT_ID = "org.modelversioning.emfprofile.actions.extensionpoint.actionhandlers";
	private static final String ATTRIBUTE_CLASS = "class";
	private static final String ATTRIBUTE_ACTION_ID = "actionId";

	private static final ActionHandlerExecutor INSTANCE = new ActionHandlerExecutor();

	private ActionHandlerExecutor() {
	}

	public static ActionHandlerExecutor getInstance() {
		return INSTANCE;
	}

	public MultiStatus executeActions(Action action, List<StereotypeApplication> appliedStereotypes) {
		MultiStatus actionHandlerstatuses = new MultiStatus(PLUGIN_ID, IStatus.INFO, "Handler results for: " + action.getName(), null);

		Map<String, Collection<IConfigurationElement>> actionIdToHandlerMap = new HashMap<>();
		for (IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID)) {
			String actionId = element.getAttribute(ATTRIBUTE_ACTION_ID);
			if (!actionIdToHandlerMap.containsKey(actionId)) {
				actionIdToHandlerMap.put(actionId, new LinkedList<IConfigurationElement>());
			}
			actionIdToHandlerMap.get(actionId).add(element);
		}

		if (actionIdToHandlerMap.containsKey(action.getId())) {
			for (IConfigurationElement element : actionIdToHandlerMap.get(action.getId())) {
				try {
					Object o = element.createExecutableExtension(ATTRIBUTE_CLASS);
					IStatus outcome = ((ActionHandler) o).doAction(action, appliedStereotypes);
					actionHandlerstatuses.add(outcome);
				} catch (CoreException e) {
					StatusManager.getManager().handle(new Status(IStatus.ERROR, element.getAttribute(ATTRIBUTE_CLASS), "Instanziation of actionHandler '" + element.getAttribute("class") + "' failed", e));
				}
			}
		} else {
			actionHandlerstatuses.add(new Status(IStatus.INFO, PLUGIN_ID, "No ActionHandler registered for ActionID (" + action.getId() + ")"));
		}

		return actionHandlerstatuses;
	}
}
