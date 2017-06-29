package org.framed.iorm.ui.graphitifeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.providers.DiagramProvider;
import org.framed.iorm.ui.util.PropertyUtil;

public class StepInFeature extends AbstractCustomFeature {
	
	/**
	 * the name of the feature gathered from {@link NameLiterals}
	 */
	private String STEP_IN_FEATURE_NAME = NameLiterals.STEP_IN_FEATURE_NAME;
	
	/**
	 * the shape identifiers of the shapes this feature can be used on gathered from {@link IdentifierLiterals}
	 * <p>
	 * can be:<br>
	 * (1) the shape identifier of type body rectangle of a group or<br>
	 * (2) the shape identifier of type body rectangle of a compartment type or<br>
	 * (3) the shape identifier of type body rectangle of a role group 
	 */
	private String SHAPE_ID_GROUP_TYPEBODY = IdentifierLiterals.SHAPE_ID_GROUP_TYPEBODY;

	/**
	 * Class constructor
	 * @param featureProvider the feature provider the feature belongs to
	 */
	public StepInFeature(IFeatureProvider featureProvider) {
		super(featureProvider);
	}
	 
	/**
	 * get method for the features name
	 * @return the name of the feature
	 */
	@Override
	public String getName() {
		return STEP_IN_FEATURE_NAME;
	}
	
	/**
	 * This methods checks if the feature can be executed.
	 * <p>
	 * It return true if<br>
	 * (1) exactly one pictogram element is selected and<br>
	 * (2) this pictogram element has a graphics algorithm that is the type body of a 
	 * group, compartment type or role group.
	 * @return if the feature can be executed
	 */
	@Override
	public boolean canExecute(ICustomContext customContext) {
		if(customContext.getPictogramElements().length == 1) {
			if(customContext.getPictogramElements()[0].getGraphicsAlgorithm() != null) {
				GraphicsAlgorithm graphicAlgorithm =  customContext.getPictogramElements()[0].getGraphicsAlgorithm();
				if(PropertyUtil.isShape_IdValue(graphicAlgorithm, SHAPE_ID_GROUP_TYPEBODY)) 
					return true;
		}	}
		return false;
	}
	
	@Override
	public void execute(ICustomContext context) {
		ContainerShape typeBodyShape = (ContainerShape) context.getPictogramElements()[0];
		//MethodUtil
		
		//((DiagramProvider) getFeatureProvider().getDiagramTypeProvider()).setDiagram(diagram);
	}
	
	
}	
