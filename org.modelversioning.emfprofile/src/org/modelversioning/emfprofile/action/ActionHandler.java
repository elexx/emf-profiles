package org.modelversioning.emfprofile.action;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.modelversioning.emfprofile.Action;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

public interface ActionHandler {
	
	/**
	 * @param action The action for which this handler is executed.
	 * @param stereotypeApplication All stereotype applications for this action's stereotype on the element.
	 * @return
	 */
	IStatus doAction(Action action, Collection<StereotypeApplication> stereotypeApplications);
}
