/**
 * Copyright (c) ${year} modelversioning.org
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 */
package org.modelversioning.emfprofile;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.modelversioning.emfprofile.Profile#getActions <em>Actions</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.modelversioning.emfprofile.EMFProfilePackage#getProfile()
 * @model
 * @generated
 */
public interface Profile extends EPackage {
	/**
	 * Returns the value of the '<em><b>Actions</b></em>' containment reference list.
	 * The list contents are of type {@link org.modelversioning.emfprofile.Action}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Actions</em>' containment reference list.
	 * @see org.modelversioning.emfprofile.EMFProfilePackage#getProfile_Actions()
	 * @model containment="true"
	 * @generated
	 */
	EList<Action> getActions();

	/**
	 * <!-- begin-user-doc -->
	 * Returns a list of all applicable stereotypes for the specified
	 * <code>eClass</code>.
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	EList<Stereotype> getApplicableStereotypes(EClass eClass);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<Stereotype> getStereotypes();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model stereotypeNameRequired="true"
	 * @generated
	 */
	Stereotype getStereotype(String stereotypeName);

} // Profile
