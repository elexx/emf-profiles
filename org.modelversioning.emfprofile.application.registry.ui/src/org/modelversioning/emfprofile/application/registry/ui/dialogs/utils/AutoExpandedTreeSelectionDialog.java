package org.modelversioning.emfprofile.application.registry.ui.dialogs.utils;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

public class AutoExpandedTreeSelectionDialog extends ElementTreeSelectionDialog {

	public AutoExpandedTreeSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
		super(parent, labelProvider, contentProvider);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);
		getTreeViewer().setAutoExpandLevel(2);
		getTreeViewer().setInput(getTreeViewer().getInput());
		return control;
	}
}