package org.framed.iorm.ui.contexts;

import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.swt.widgets.TreeItem;

public class ChangeConfigurationContext extends CustomContext implements ICustomContext {

	private TreeItem treeItem;
	
	public void setTreeItem(TreeItem newTreeItem) {
		treeItem = newTreeItem;
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}
}
