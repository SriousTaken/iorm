package org.framed.iorm.ui.contexts;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.swt.widgets.TreeItem;
import org.framed.iorm.featuremodel.FRaMEDFeature;
import org.framed.iorm.ui.subeditors.FRaMEDDiagramEditor;

import de.ovgu.featureide.fm.core.base.IFeatureModel;

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
	 * the feature model used to get it constraints
	 */
	private IFeatureModel featureModel;
	
	/**
	 * the features of the standard configuration
	 * <p>
	 * This is used to get the information if a feature has to be added with the property manuallySelected or not.
	 */
	private EList<FRaMEDFeature> standardFeatures;
	
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
	 * sets the class variable featureModel
	 * @param featureModel the feature model to set
	 */
	public void setFeatureModel(IFeatureModel featureModel) {
		this.featureModel = featureModel;
	}
	
	/**
	 * sets the class variable standardFeatures
	 * @param standartFeature the standard features to set
	 */
	public void setStandardFeatures(EList<FRaMEDFeature> standardFeatures) {
		this.standardFeatures = standardFeatures;
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
	
	/**
	 * get method for the feature model
	 * @return the class variable featureModel
	 */
	public IFeatureModel getFeatureModel() {
		return featureModel;
	}
	
	/**
	 * get method for the feature model
	 * @return the class variable featureModel
	 */
	public EList<FRaMEDFeature> getStandardFeatures() {
		return standardFeatures;
	}
}
