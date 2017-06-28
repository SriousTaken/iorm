package org.framed.iorm.ui.contexts;

import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.framed.iorm.ui.subeditors.FRaMEDDiagramEditor;

import de.ovgu.featureide.fm.core.configuration.Configuration;

/**
 * This context is used to save needed information for the Graphiti custom feature {@link
 * org.framed.iorm.ui.graphitifeatures.ChangeConfigurationFeature}.
 * <p>
 * It extends the {@link CustomContext} by some variables and their get and set methods.
 * @see org.framed.iorm.ui.graphitifeatures.ChangeConfigurationFeature
 * @see CustomContext
 * @author Kevin Kassin
 */
public class ChangeConfigurationContext extends CustomContext implements ICustomContext {

	/**
	 * the diagram editor the custom feature works 
	 */
	private FRaMEDDiagramEditor behaviorEditor;
	
	/**
	 * the configuration of the feature editor that uses the custom feature with this context
	 */
	private Configuration configuration;
	
	/**
	 * sets the class variable behaviorEditor
	 * @param behaviorEditor the diagram editor to set
	 */
	public void setBehaviorEditor(FRaMEDDiagramEditor behaviorEditor) {
		this.behaviorEditor = behaviorEditor;
	}
	
	/**
	 * sets the class variable configuration
	 * @param configuration the configuration of the feature editor that uses the custom feature with this context
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * get method for the diagram editor
	 * @return the class variable behaviorEditor
	 */
	public FRaMEDDiagramEditor getBehaviorEditor() {
		return behaviorEditor;
	}
	
	/**
	 * get method for the configuration of the feature editor that uses the custom feature with this context
	 * @return the class variable behaviorEditor
	 */
	public Configuration getConfiguration() {
		return configuration;
	}
}
