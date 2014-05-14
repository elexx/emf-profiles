/**
 * Copyright (c) ${year} modelversioning.org
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 */
package org.modelversioning.emfprofile.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.modelversioning.emfprofile.Action;
import org.modelversioning.emfprofile.EMFProfilePackage;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.modelversioning.emfprofile.impl.ProfileImpl#getActions <em>Actions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfileImpl extends EPackageImpl implements Profile {
	
	/**
	 * The cached value of the '{@link #getActions() <em>Actions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActions()
	 * @generated
	 * @ordered
	 */
	protected EList<Action> actions;
	protected static final String NAME_EDEFAULT = "MyProfile"; //$NON-NLS-1$
	protected static final String NS_URI_EDEFAULT = "http://www.modelversioning.org/myprofile"; //$NON-NLS-1$
	protected static final String NS_PREFIX_EDEFAULT = "myprofile"; //$NON-NLS-1$
	
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected ProfileImpl() {
		super();
		setDefaultValues();
	}
	
	private void setDefaultValues() {
		this.setName(NAME_EDEFAULT);
		this.setNsURI(NS_URI_EDEFAULT);
		this.setNsPrefix(NS_PREFIX_EDEFAULT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EMFProfilePackage.Literals.PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Action> getActions() {
		if (actions == null) {
			actions = new EObjectContainmentEList<Action>(Action.class, this, EMFProfilePackage.PROFILE__ACTIONS);
		}
		return actions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Stereotype> getApplicableStereotypes(EClass eClass) {
		EList<Stereotype> applicableStereotypes = new BasicEList<Stereotype>();
		for (Stereotype stereotype : this.getStereotypes()) {
			if (stereotype.isApplicable(eClass)) {
				applicableStereotypes.add(stereotype);
			}
		}
		return ECollections.unmodifiableEList(applicableStereotypes);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Returns all contained {@link Stereotype}s.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Stereotype> getStereotypes() {
		EList<Stereotype> stereotypes = new BasicEList<Stereotype>();
		for (EClassifier eClass : this.eClassifiers) {
			if (eClass instanceof Stereotype) {
				stereotypes.add((Stereotype)eClass);
			}
		}
		return ECollections.unmodifiableEList(stereotypes);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Returns the {@link Stereotype} with the specified <code>name</code>.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Stereotype getStereotype(String stereotypeName) {
		for (Stereotype stereotype : getStereotypes()) {
			if (stereotypeName.equals(stereotype.getName())) {
				return stereotype;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EMFProfilePackage.PROFILE__ACTIONS:
				return ((InternalEList<?>)getActions()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EMFProfilePackage.PROFILE__ACTIONS:
				return getActions();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EMFProfilePackage.PROFILE__ACTIONS:
				getActions().clear();
				getActions().addAll((Collection<? extends Action>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case EMFProfilePackage.PROFILE__ACTIONS:
				getActions().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case EMFProfilePackage.PROFILE__ACTIONS:
				return actions != null && !actions.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ProfileImpl
