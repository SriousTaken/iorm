package org.framed.iorm.ui.pattern.shapes;

import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.framed.iorm.ui.util.GeneralUtil;

//TODO
public abstract class FRaMEDShapePattern extends AbstractPattern {
	
	public FRaMEDShapePattern() {
		super(null);
	}
	
	protected Object getBusinessObject(IDirectEditingContext editingContext) {
		PictogramElement pictogramElement = editingContext.getPictogramElement();
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return businessObject;
	}
	
	protected void updateContainingGroup() {
		ContainerShape groupTypeBodyToUpdate = GeneralUtil.getGroupTypeBodyForGroupsDiagram(getDiagram());
        updatePictogramElement(groupTypeBodyToUpdate);
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
	public abstract boolean isMainBusinessObjectApplicable(Object mainBusinessObject);

	/**
	 * to be overriden in it subclasses
	 */
	@Override
	protected abstract boolean isPatternControlled(PictogramElement pictogramElement);

	/**
	 * to be overriden in it subclasses
	 */
	@Override
	protected abstract boolean isPatternRoot(PictogramElement pictogramElement);
}
