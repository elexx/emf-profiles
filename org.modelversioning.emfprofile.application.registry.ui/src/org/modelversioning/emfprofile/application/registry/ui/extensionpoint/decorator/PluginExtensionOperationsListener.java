/**
 * Copyright (c) 2012 modelversioning.org
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 */
package org.modelversioning.emfprofile.application.registry.ui.extensionpoint.decorator;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:becirb@gmail.com">Becir Basic</a>
 *
 */
public interface PluginExtensionOperationsListener {
	
	/**
	 * Executes the operation of applying stereotypes on 
	 * given {@link EObject}
	 * @param eObject
	 */
	void applyStereotype(EObject eObject);
	
	/**
	 * Executes an action on given {@link EObject}
	 * @param eObject
	 * @param actionHandlers a map of actionHandlerIds to a collection of handlers registered for given id
	 */
	void executeAction(EObject eObject, Map<String, Collection<IConfigurationElement>> actionIdToHandlerMap);

	/**
	 * Notifies the extended plug-in that the 
	 * new selection on an instance of {@link EObject} 
	 * was registered. 
	 * @param eObject
	 */
	void eObjectSelected(EObject eObject);
}
