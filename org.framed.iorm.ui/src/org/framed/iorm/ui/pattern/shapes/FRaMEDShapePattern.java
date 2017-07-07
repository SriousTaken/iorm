package org.framed.iorm.ui.pattern.shapes;

import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;

public class FRaMEDShapePattern extends AbstractPattern {
	
	public FRaMEDShapePattern() {
		super(null);
	}
	
	protected Object getBusinessObject(IDirectEditingContext editingContext) {
		PictogramElement pictogramElement = editingContext.getPictogramElement();
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return businessObject;
	}
	
	/**
	 * the pictogram elements service used to creates pictogram elements in the subclasses
	 */
	protected final IPeCreateService pictogramElementCreateService = Graphiti.getPeCreateService();
	
	/**
	 * the graphics algorithm service used to create graphics algorithms in the subclasses
	 */
	protected final IGaService graphicAlgorithmService = Graphiti.getGaService();

	/**
	 * to be overriden in it subclasses
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject) {
		return false;
	}

	/**
	 * to be overriden in it subclasses
	 */
	@Override
	protected boolean isPatternControlled(PictogramElement pictogramElement) {
		return false;
	}

	/**
	 * to be overriden in it subclasses
	 */
	@Override
	protected boolean isPatternRoot(PictogramElement pictogramElement) {
		return false;
	}
}
