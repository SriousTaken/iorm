package org.framed.iorm.ui.pattern.shapes;

import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.IPattern;
import org.framed.iorm.model.ModelElement;
import org.framed.iorm.model.NamedElement;
import org.framed.iorm.model.OrmFactory;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.pattern.shapes.AttributeOperationCommonPattern; //*import for javadoc link

/**
 * This graphiti pattern is used to work with {@link NamedElement}s
 * of the type attribute in the editor.
 * <p>
 * It deals with the following aspect of attributes:<br>
 * (1) creating the attribute, especially its business object<br>
 * @see AttributeOperationCommonPattern
 * @author Kevin Kassin
 */
public class AttributePattern extends FRaMEDShapePattern implements IPattern {

	/**
	 * the name of the create feature in this pattern and the standard names of attributes 
	 * gathered from {@link NameLiterals}
	 */
	private final String ATTRIBUTE_STANDARD_NAME = NameLiterals.STANDARD_ATTRIBUTE_NAME,
						 ATTRIBUTE_FEATURE_NAME = NameLiterals.ATTRIBUTE_FEATURE_NAME;
	
	/**
	 * the image identifier for the icon of the create feature in this pattern gathered from
	 * {@link IdentifierLiterals}
	 */
	private final String IMG_ID_FEATURE_ATTRIBUTE = IdentifierLiterals.IMG_ID_FEATURE_ATTRIBUTE;
	
	/**
	 * Class constructor
	 */
	public AttributePattern() {
		super();
	}
	
	/**
	 * get method for the create features name
	 * @return the name of the create feature
	 */
	@Override
	public String getCreateName() {
		return ATTRIBUTE_FEATURE_NAME;
	}
	
	/**
	 * enables the icon for the create feature in this pattern
	 * @return the image identifier for the icon of the create feature in this pattern
	 */
	@Override
	public String getCreateImageId() {
		return IMG_ID_FEATURE_ATTRIBUTE;
	}
	
	/**
	 * checks if pattern is applicable for a given business object
	 * @return true, if business object is of type {@link org.framed.iorm.model.NamedElement} but not
	 * of type {@link org.framed.iorm.model.ModelElement}
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object businessObject) {
		return (businessObject instanceof NamedElement &&
				!(businessObject instanceof ModelElement));
	}

	/**
	 * checks if pattern is applicable for a given pictogram element
	 * @return true, if business object of the pictogram element is of type {@link org.framed.iorm.model.NamedElement} 
	 * but not of type {@link org.framed.iorm.model.ModelElement}
	 */
	@Override
	protected boolean isPatternControlled(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}

	/**
	 * checks if the pictogram element to edit with the pattern is its root
	 * @return true, if business object of the pictogram element is of type {@link org.framed.iorm.model.NamedElement} 
	 * but not of type {@link org.framed.iorm.model.ModelElement}
	 */
	@Override
	protected boolean isPatternRoot(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}
	
	//create feature
	//~~~~~~~~~~~~~~
	/**
	 * calculates if an attribute can be created
	 * <p>
	 * returns true if the attribute is created in a class or role that is a {@link org.framed.iorm.model.Shape} 
	 * of the right type
	 * @return if an attribute can be created
	 */
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

	/**
	 * creates the business object of the attribute, adds it to business object of the class or role in which
	 * the attribute is created in and calls the add function for the attribute in 
	 * {@link AttributeOperationCommonPattern}
	 * @return the created business object of the attribute
	 */
	@Override
	public Object[] create(ICreateContext createContext) {
		NamedElement newAttribute = OrmFactory.eINSTANCE.createNamedElement();
		newAttribute.setName(ATTRIBUTE_STANDARD_NAME);
		org.framed.iorm.model.Shape classOrRole = 
				(org.framed.iorm.model.Shape) getBusinessObjectForPictogramElement(createContext.getTargetContainer());
		if(newAttribute.eResource() != null) getDiagram().eResource().getContents().add(newAttribute);
		classOrRole.getFirstSegment().getElements().add(newAttribute);
		getFeatureProvider().getDirectEditingInfo().setActive(true);
		addGraphicalRepresentation(createContext, newAttribute);
		return new Object[] { newAttribute };
	}
}
