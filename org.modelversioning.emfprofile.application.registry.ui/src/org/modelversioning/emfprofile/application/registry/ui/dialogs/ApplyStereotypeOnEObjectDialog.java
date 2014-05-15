/**
 * Copyright (c) 2012 modelversioning.org
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 */
package org.modelversioning.emfprofile.application.registry.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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
import org.modelversioning.emfprofile.application.registry.ProfileApplicationDecorator;
import org.modelversioning.emfprofile.application.registry.ui.EMFProfileApplicationRegistryUIPlugin;
import org.modelversioning.emfprofile.application.registry.ui.dialogs.utils.AutoExpandedTreeSelectionDialog;
import org.modelversioning.emfprofile.application.registry.ui.dialogs.utils.TreeObject;
import org.modelversioning.emfprofile.application.registry.ui.dialogs.utils.ViewContentProvider;
import org.modelversioning.emfprofile.application.registry.ui.observer.ActiveEditorObserver;
import org.modelversioning.emfprofile.application.registry.ui.providers.ProfileProviderLabelAdapter;
import org.modelversioning.emfprofile.application.registry.ui.views.EMFProfileApplicationsView;
import org.modelversioning.emfprofileapplication.StereotypeApplicability;

/**
 * @author <a href="mailto:becirb@gmail.com">Becir Basic</a>
 *
 */
public class ApplyStereotypeOnEObjectDialog {

	private ProfileProviderLabelAdapter labelAdapter = new ProfileProviderLabelAdapter(EMFProfileApplicationsView.getAdapterFactory());
	
	private final Map<ProfileApplicationDecorator, Collection<StereotypeApplicability>> profileToStereotypeApplicabilityForEObjectMap;
	public ApplyStereotypeOnEObjectDialog(Map<ProfileApplicationDecorator, Collection<StereotypeApplicability>> profileToStereotypeApplicabilityForEObjectMap) {
		this.profileToStereotypeApplicabilityForEObjectMap = profileToStereotypeApplicabilityForEObjectMap;
	}
	
	/**
	 * Opens this dialog, in which the stereotypes that can be applied
	 * on the given {@link EObject} can be selected.
	 * @param eObject in question.
	 */
	public void openApplyStereotypeDialog(EObject eObject) {
		Collection<TreeObject> parents = new ArrayList<>();
		for(ProfileApplicationDecorator profileApplication : profileToStereotypeApplicabilityForEObjectMap.keySet()){
			TreeObject parent = new TreeObject(profileApplication);
			for(StereotypeApplicability stereotypeApplicability : profileToStereotypeApplicabilityForEObjectMap.get(profileApplication)){
				parent.addChild(new TreeObject(stereotypeApplicability, parent));
			}
			parents.add(parent);
		}
		
		AutoExpandedTreeSelectionDialog dialog = new AutoExpandedTreeSelectionDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				new ViewLabelProvider(), new ViewContentProvider());
		dialog.setTitle("Stereotype Selection");
		dialog.setMessage("Select one or more Stereotypes to apply");
		
		dialog.setInput(parents);
		dialog.setDoubleClickSelects(true);
		dialog.setValidator(new ISelectionStatusValidator() {
			@Override
			public IStatus validate(Object[] selection) {
				if(selection.length==0)
					return new Status(IStatus.ERROR, EMFProfileApplicationRegistryUIPlugin.PLUGIN_ID, "No Stereotype selected yet.");
				for (Object object : selection) {
					TreeObject treeObject = (TreeObject) object;
					if(treeObject.hasParent() && !treeObject.hasChildren())
						return new Status(IStatus.OK, EMFProfileApplicationRegistryUIPlugin.PLUGIN_ID, "");
				}
				return new Status(IStatus.ERROR, EMFProfileApplicationRegistryUIPlugin.PLUGIN_ID, "No Stereotype selected yet.");
			}
		});
		int result = dialog.open();
		if (Dialog.OK == result) {
			Object[] treeObjects = dialog.getResult();
			StringBuilder strBuilder = new StringBuilder();
			boolean hasNotApplicableStereotypes = false;
			Collection<ProfileApplicationDecorator> profileApplicationDecoratorToBeRefreshedInView = new ArrayList<>();
			for (Object object : treeObjects) {
				TreeObject child = (TreeObject) object;
				StereotypeApplicability stereotypeApplicability = ((StereotypeApplicability)child.getElement());
				ProfileApplicationDecorator profileApplicationDecorator = (ProfileApplicationDecorator)child.getParent().getElement();
				try {
					profileApplicationDecorator.applyStereotype(stereotypeApplicability, eObject);
					profileApplicationDecoratorToBeRefreshedInView.add(profileApplicationDecorator);
				} catch (IllegalArgumentException e) {
					hasNotApplicableStereotypes = true;
					strBuilder.append(stereotypeApplicability.getStereotype().getName() + ", from profile: " + profileApplicationDecorator.getProfileName() + "\n");
				}
			}
			if( ! profileApplicationDecoratorToBeRefreshedInView.isEmpty()){
				ActiveEditorObserver.INSTANCE.refreshViewer(profileApplicationDecoratorToBeRefreshedInView);
				ActiveEditorObserver.INSTANCE.refreshDecoration(eObject);
			}
			if(hasNotApplicableStereotypes){
				strBuilder.insert(0, "Not applicable stereotype(s) to object: "+ (eObject == null ? "" : eObject.toString() +"\n"));
				MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
				messageBox.setText("Could not be applied!");
				messageBox.setMessage(strBuilder.toString());
				messageBox.open();
			}
		}
		
	}
	final class ViewLabelProvider extends LabelProvider {
		@Override
		public String getText(Object obj) {
			Object element = ((TreeObject) obj).getElement();
			if(element instanceof ProfileApplicationDecorator) {
				ProfileApplicationDecorator profileApplicationDecorator = (ProfileApplicationDecorator) element;
				return profileApplicationDecorator.getName();
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
