package org.framed.iorm.model.editor.pattern.shapes;

import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.pattern.IPattern;
import org.framed.iorm.model.NamedElement;
import org.framed.iorm.model.OrmFactory;
import org.framed.iorm.model.Type;
import org.framed.iorm.model.editor.literals.NameLiterals;

public class OperationPattern extends AbstractPattern implements IPattern {
	
	//name literals
	private final String STANDART_OPERATION_NAME = NameLiterals.STANDART_OPERATION_NAME,
						 OPERATION_FEATURE_NAME = NameLiterals.OPERATION_FEATURE_NAME;
	
	public OperationPattern() {
		super(null);
	}
	
	@Override
	public String getCreateName() {
		return OPERATION_FEATURE_NAME;
	}
	
	@Override
	public boolean isMainBusinessObjectApplicable(Object businessObject) {
		return (businessObject instanceof NamedElement);
	}

	@Override
	protected boolean isPatternControlled(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}

	@Override
	protected boolean isPatternRoot(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}
	
	//create feature
	//~~~~~~~~~~~~~~
	@Override
	public boolean canCreate(ICreateContext createContext) {
		PictogramElement pictogramElement = createContext.getTargetContainer();
		Object businessObject =  getBusinessObjectForPictogramElement(pictogramElement);
		if(businessObject instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject; 
			if(shape.getType() == Type.NATURAL_TYPE) return true;
			if(shape.getType() == Type.DATA_TYPE) return true;	
		}
		return false;
	}

	@Override
	public Object[] create(ICreateContext createContext) {
		//create new operation
		NamedElement newOperation = OrmFactory.eINSTANCE.createNamedElement();
		newOperation.setName(STANDART_OPERATION_NAME);
		//add new operation to the operation segment of the class or role
		org.framed.iorm.model.Shape classOrRole = 
				(org.framed.iorm.model.Shape) getBusinessObjectForPictogramElement(createContext.getTargetContainer());
		if(newOperation.eResource() != null) getDiagram().eResource().getContents().add(newOperation);
		classOrRole.getSecondSegment().getElements().add(newOperation);
		//enable direct editing
		getFeatureProvider().getDirectEditingInfo().setActive(true);
		//add to graphiti representation
		addGraphicalRepresentation(createContext, newOperation);
		return new Object[] { newOperation };
	}
}
