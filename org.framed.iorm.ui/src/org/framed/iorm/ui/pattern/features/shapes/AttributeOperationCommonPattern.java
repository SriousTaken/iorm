package org.framed.iorm.ui.pattern.features.shapes;

import org.eclipse.graphiti.features.IDirectEditingInfo;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.framed.iorm.model.ModelElement;
import org.framed.iorm.model.NamedElement;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.LayoutLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.literals.TextLiterals;
import org.framed.iorm.ui.util.DirectEditingUtil;
import org.framed.iorm.ui.util.MethodUtil;
import org.framed.iorm.ui.util.PropertyUtil;

public class AttributeOperationCommonPattern extends AbstractPattern {
	
	//name literals
	private final String ATTRIBUTE_STANDART_NAME = NameLiterals.STANDART_ATTRIBUTE_NAME,
						 STANDART_OPERATION_NAME = NameLiterals.STANDART_OPERATION_NAME,
						 ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME = NameLiterals.ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME;
	
	//text literals
	private final String DIRECTEDITING_ATTRIBUTES = TextLiterals.DIRECTEDITING_ATTRIBUTES,
						 DIRECTEDITING_OPERATIONS = TextLiterals.DIRECTEDITING_OPERATIONS;
	
	//ID literals
	private final String SHAPE_ID_ATTRIBUTE_TEXT = IdentifierLiterals.SHAPE_ID_ATTRIBUTE_TEXT,
						 SHAPE_ID_OPERATION_TEXT = IdentifierLiterals.SHAPE_ID_OPERATION_TEXT;
	
	//layout literals
	private final int HEIGHT_NAME_SHAPE = LayoutLiterals.HEIGHT_NAME_SHAPE,
			          PUFFER_BETWEEN_ELEMENTS = LayoutLiterals.PUFFER_BETWEEN_ELEMENTS,
			          HEIGHT_ATTRIBUTE_SHAPE = LayoutLiterals.HEIGHT_ATTRITBUTE_SHAPE,
			          HEIGHT_OPERATION_SHAPE = LayoutLiterals.HEIGHT_OPERATION_SHAPE;
	private final IColorConstant COLOR_TEXT = LayoutLiterals.COLOR_TEXT;
	
	//services
	private final IPeCreateService peCreateService = Graphiti.getPeCreateService();
	private final IGaService gaService = Graphiti.getGaService();
	
	public AttributeOperationCommonPattern() {
		super(null);
	}
	
	@Override
	public String getCreateName() {
		return ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME;
	}
	
	@Override
	public boolean isMainBusinessObjectApplicable(Object businessObject) {
		return (businessObject instanceof NamedElement &&
				!(businessObject instanceof ModelElement));
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
	
	//add feature
	//~~~~~~~~~~~
	@Override
	public boolean canAdd(IAddContext addContext) {
		//new Object is a named element
		if(addContext.getNewObject() instanceof NamedElement) {
			NamedElement namedElement = (NamedElement) addContext.getNewObject();
			if(namedElement.getName().equals(ATTRIBUTE_STANDART_NAME) || namedElement.getName().equals(STANDART_OPERATION_NAME)) {
				PictogramElement pictogramElement = addContext.getTargetContainer();
				Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
				if(businessObject instanceof org.framed.iorm.model.Shape) {
					org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
					if(shape.getType() == Type.NATURAL_TYPE) return true;
					if(shape.getType() == Type.DATA_TYPE) return true;
					
		}	}	}	
		
		return false;
	}

	@Override
	public PictogramElement add(IAddContext addContext) {
		//get new object and containers
		Shape attributeOrOperationShape = null;
		Text text = null;
		NamedElement addedAttributeOrOperation = (NamedElement) addContext.getNewObject();
		ContainerShape classOrRoleContainer = (ContainerShape) addContext.getTargetContainer();
		org.framed.iorm.model.Shape businessObjectOfClassOrRole = (org.framed.iorm.model.Shape) getBusinessObjectForPictogramElement(classOrRoleContainer);
		ContainerShape attributeContainer = (ContainerShape) addContext.getTargetContainer().getChildren().get(2),
					   operationContainer = (ContainerShape) addContext.getTargetContainer().getChildren().get(4);
		int attributeContainerSize = attributeContainer.getChildren().size(),
		    operationContainerSize = operationContainer.getChildren().size(); 	
		int horizontalCenter;
			horizontalCenter = MethodUtil.calculateHorizontalCenter(businessObjectOfClassOrRole.getType(), 
																		 classOrRoleContainer.getGraphicsAlgorithm().getHeight());
		//create shape and text for attribute/operation, set location and property
		 if(addedAttributeOrOperation.getName().equals(ATTRIBUTE_STANDART_NAME)) {
	    	attributeOrOperationShape = peCreateService.createShape(attributeContainer, true);
	    	text = gaService.createText(attributeOrOperationShape, addedAttributeOrOperation.getName());
	    	text.setForeground(manageColor(COLOR_TEXT));
	    	gaService.setLocationAndSize(text, PUFFER_BETWEEN_ELEMENTS, HEIGHT_NAME_SHAPE+PUFFER_BETWEEN_ELEMENTS+HEIGHT_ATTRIBUTE_SHAPE*attributeContainerSize, 
	    								 classOrRoleContainer.getGraphicsAlgorithm().getWidth()-2*PUFFER_BETWEEN_ELEMENTS, HEIGHT_ATTRIBUTE_SHAPE);
	    	PropertyUtil.setShape_IdValue(text, SHAPE_ID_ATTRIBUTE_TEXT);
	    } else {
	    	attributeOrOperationShape = peCreateService.createShape(operationContainer, true);
	    	text = gaService.createText(attributeOrOperationShape, addedAttributeOrOperation.getName());
	    	text.setForeground(manageColor(COLOR_TEXT));
	    	gaService.setLocationAndSize(text, PUFFER_BETWEEN_ELEMENTS, PUFFER_BETWEEN_ELEMENTS+horizontalCenter+HEIGHT_OPERATION_SHAPE*operationContainerSize, 
					 classOrRoleContainer.getGraphicsAlgorithm().getWidth()-2*PUFFER_BETWEEN_ELEMENTS, HEIGHT_OPERATION_SHAPE);
	    	PropertyUtil.setShape_IdValue(text, SHAPE_ID_OPERATION_TEXT);	      	
	    }	    
		
	    //set direct editing information
	    IDirectEditingInfo directEditingInfo = getFeatureProvider().getDirectEditingInfo();
		directEditingInfo.setMainPictogramElement(classOrRoleContainer);
		directEditingInfo.setPictogramElement(attributeOrOperationShape);
		directEditingInfo.setGraphicsAlgorithm(text);
	    //set link
	    link(attributeOrOperationShape, addedAttributeOrOperation);
		return attributeOrOperationShape;
	}
	
	//direct editing feature
	//~~~~~~~~~~~~~~~~~~~~~~
	private Object getBusinessObject(IDirectEditingContext editingContext) {
		PictogramElement pictogramElement = editingContext.getPictogramElement();
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return businessObject;
	}
		
	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}
		
	public boolean canDirectEdit(IDirectEditingContext editingContext) {
		Object businessObject = getBusinessObject(editingContext);
		GraphicsAlgorithm graphicsAlgorithm = editingContext.getGraphicsAlgorithm();
		if(businessObject instanceof NamedElement && graphicsAlgorithm instanceof Text)
			return true;
		return false;
	}

	@Override
	public String getInitialValue(IDirectEditingContext editingContext) {
		NamedElement operation = (NamedElement) getBusinessObject(editingContext);
		return operation.getName();
	}
		
	public String checkValueValid(String newName, IDirectEditingContext editingContext) {
		Shape shape = (Shape) editingContext.getPictogramElement();
		ContainerShape classOrRoleShape = shape.getContainer().getContainer();
		ContainerShape attributeContainer = (ContainerShape) classOrRoleShape.getChildren().get(2);
		ContainerShape operationContainer = (ContainerShape) classOrRoleShape.getChildren().get(4);
		//attributes
		if(attributeContainer.getChildren().contains(shape))	{
			if(!(DirectEditingUtil.matchesAttribute(newName))) return DIRECTEDITING_ATTRIBUTES;
		}
		//operation
		if(operationContainer.getChildren().contains(shape))	{
			if(!(DirectEditingUtil.matchesOperation(newName))) return DIRECTEDITING_OPERATIONS;
		}
		return null;
	}	
			
	public void setValue(String newName, IDirectEditingContext editingContext) {	   
		NamedElement attributeOrOperation = (NamedElement) getBusinessObject(editingContext);
		attributeOrOperation.setName(newName);
		updatePictogramElement(((Shape) editingContext.getPictogramElement()).getContainer().getContainer());
	}
	
	//delete feature
	//~~~~~~~~~~~~~~
	@Override
	public boolean canDelete(IDeleteContext deleteContext) {
		return true;
	}
	
	@Override
	public void delete(IDeleteContext deleteContext) {
		Shape ClassOrRole = ((Shape) deleteContext.getPictogramElement()).getContainer().getContainer();
		super.delete(deleteContext);
		updatePictogramElement(ClassOrRole);
	}
		
	//move feature
	//~~~~~~~~~~~~
	@Override
	public boolean canMoveShape(IMoveShapeContext moveContext) {
		return false;
	}
		
	//resize feature
	//~~~~~~~~~~~~~~
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		return false; 
	}
}
