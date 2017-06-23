package org.framed.iorm.ui.pattern.shapes;

import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.pattern.IPattern;
import org.framed.iorm.model.NamedElement;
import org.framed.iorm.model.OrmFactory;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.NameLiterals;

public class AttributePattern extends AbstractPattern implements IPattern {

	//name literals
	private final String ATTRIBUTE_STANDARD_NAME = NameLiterals.STANDARD_ATTRIBUTE_NAME,
						 ATTRIBUTE_FEATURE_NAME = NameLiterals.ATTRIBUTE_FEATURE_NAME;
	
	//id literals
	private final String IMG_ID_FEATURE_ATTRIBUTE = IdentifierLiterals.IMG_ID_FEATURE_ATTRIBUTE;
		
	public AttributePattern() {
		super(null);
	}
	
	@Override
	public String getCreateName() {
		return ATTRIBUTE_FEATURE_NAME;
	}
	
	@Override
	public String getCreateImageId() {
		return IMG_ID_FEATURE_ATTRIBUTE;
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
		//create new attribute
		NamedElement newAttribute = OrmFactory.eINSTANCE.createNamedElement();
		newAttribute.setName(ATTRIBUTE_STANDARD_NAME);
		//add new attribute to the attribute segment of the class or role
		org.framed.iorm.model.Shape classOrRole = 
				(org.framed.iorm.model.Shape) getBusinessObjectForPictogramElement(createContext.getTargetContainer());
		if(newAttribute.eResource() != null) getDiagram().eResource().getContents().add(newAttribute);
		classOrRole.getFirstSegment().getElements().add(newAttribute);
		//enable direct editing
		getFeatureProvider().getDirectEditingInfo().setActive(true);
		//add to graphiti representation
		addGraphicalRepresentation(createContext, newAttribute);
		return new Object[] { newAttribute };
	}
}
