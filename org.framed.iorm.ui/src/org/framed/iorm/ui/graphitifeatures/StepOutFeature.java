package org.framed.iorm.ui.graphitifeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.framed.iorm.ui.literals.NameLiterals;

public class StepOutFeature extends AbstractCustomFeature {
	
	/**
	 * the name of the feature gathered from {@link NameLiterals}
	 */
	private String STEP_OUT_FEATURE_NAME = NameLiterals.STEP_OUT_FEATURE_NAME;
	
	/**
	 * Class constructor
	 * @param featureProvider the feature provider the feature belongs to
	 */
	public StepOutFeature(IFeatureProvider featureProvider) {
		super(featureProvider);
	}
	 
	/**
	 * get method for the features name
	 * @return the name of the feature
	 */
	@Override
	public String getName() {
		return STEP_OUT_FEATURE_NAME;
	}
	
	@Override
	public boolean canExecute(ICustomContext customContext) {
		return true;
	}

	@Override
	public void execute(ICustomContext context) {
		// TODO Auto-generated method stub
		
	}
}
