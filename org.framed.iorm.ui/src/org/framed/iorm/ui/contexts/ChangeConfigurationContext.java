package org.framed.iorm.ui.contexts;

import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.swt.widgets.TreeItem;
import org.framed.iorm.ui.subeditors.FRaMEDDiagramEditor;

/**
 * This context is used to save needed information for the Graphiti custom feature {@link
 * org.framed.iorm.ui.graphitifeatures.ChangeConfigurationFeature}.
 * <p>
 * It extends the {@link CustomContext} by two variables and their get and set methods.
 * @see org.framed.iorm.ui.graphitifeatures.ChangeConfigurationFeature
 * @see CustomContext
 * @author Kevin Kassin
 */
public class ChangeConfigurationContext extends CustomContext implements ICustomContext {

	/**
	 * the tree item the custom feature works on
	 */
	private TreeItem treeItem;
	
	/**
	 * the diagram editor the custom feature works 
	 */
	private FRaMEDDiagramEditor behaviorEditor;
	
	/**
	 * sets the class variable treeItem
	 * @param TreeItem the tree item to set
	 */
	public void setTreeItem(TreeItem TreeItem) {
		this.treeItem = TreeItem;
	}
	
	/**
	 * sets the class variable behaviorEditor
	 * @param behaviorEditor the diagram editor to set
	 */
	public void setBehaviorEditor(FRaMEDDiagramEditor behaviorEditor) {
		this.behaviorEditor = behaviorEditor;
	}
	
	/**
	 * get method for the tree item
	 * @return the class variable treeItem
	 */
	public TreeItem getTreeItem() {
		return treeItem;
	}
	
	/**
	 * get method for the diagram editor
	 * @return the class variable behaviorEditor
	 */
	public FRaMEDDiagramEditor getBehaviorEditor() {
		return behaviorEditor;
	}
}
