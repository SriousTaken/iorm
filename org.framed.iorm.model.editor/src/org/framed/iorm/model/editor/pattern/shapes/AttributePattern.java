package org.framed.iorm.model.editor.pattern.shapes;

import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.pattern.IPattern;
import org.framed.iorm.model.NamedElement;
import org.framed.iorm.model.OrmFactory;
import org.framed.iorm.model.Type;
import org.framed.iorm.model.editor.literals.NameLiterals;

public class AttributePattern extends AbstractPattern implements IPattern {

	//name literals
	private final String ATTRIBUTE_STANDART_NAME = NameLiterals.STANDART_ATTRIBUTE_NAME,
						 ATTRIBUTE_FEATURE_NAME = NameLiterals.ATTRIBUTE_FEATURE_NAME;
		
	public AttributePattern() {
		super(null);
	}
	
	@Override
	public String getCreateName() {
		return ATTRIBUTE_FEATURE_NAME;
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
		newAttribute.setName(ATTRIBUTE_STANDART_NAME);
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
