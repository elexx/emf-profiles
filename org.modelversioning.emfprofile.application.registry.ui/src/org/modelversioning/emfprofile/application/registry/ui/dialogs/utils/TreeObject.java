package org.modelversioning.emfprofile.application.registry.ui.dialogs.utils;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

public class TreeObject implements IAdaptable {

	private final Object element;
	private final TreeObject parent;
	private final List<TreeObject> children;

	public TreeObject(Object element) {
		this(element, null);
	}

	public TreeObject(Object element, TreeObject parent) {
		this.element = element;
		this.parent = parent;
		children = new LinkedList<>();
	}

	public final Object getElement() {
		return element;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public TreeObject getParent() {
		return parent;
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

	public TreeObject[] getChildren() {
		return children.toArray(new TreeObject[children.size()]);
	}

	public void addChild(TreeObject child) {
		children.add(child);
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
		return null;
	}
}