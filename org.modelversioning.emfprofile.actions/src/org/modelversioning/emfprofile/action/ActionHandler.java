package org.modelversioning.emfprofile.action;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.modelversioning.emfprofile.Action;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

public interface ActionHandler {

	/**
	 * @param action The action for which this handler is executed.
	 * @param stereotypeApplication All stereotype applications for this action's stereotype on the element.
	 * @return should return {@link IStatus#OK} if executed successful.
	 */
	IStatus doAction(Action action, List<StereotypeApplication> stereotypeApplications);
}
