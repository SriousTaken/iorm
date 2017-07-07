package org.framed.iorm.ui.pattern.shapes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IDirectEditingInfo;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.features.context.impl.MultiDeleteInfo;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.pattern.IPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.NamedElement;
import org.framed.iorm.model.OrmFactory;
import org.framed.iorm.model.Segment;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.LayoutLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.literals.TextLiterals;
import org.framed.iorm.ui.util.DirectEditingUtil;
import org.framed.iorm.ui.util.GeneralUtil;
import org.framed.iorm.ui.util.PropertyUtil;

public class DataTypePattern extends FRaMEDShapePattern implements IPattern {
	
	//name literals
	private final String DATATYPE_FEATURE_NAME = NameLiterals.DATATYPE_FEATURE_NAME,
						 STANDARD_DATATYPE_NAME = NameLiterals.STANDARD_DATATYPE_NAME;
	
	//text literals
	private final String DIRECTEDITING_DATATYPE = TextLiterals.DIRECTEDITING_DATATYPE;
	
	//ID literals
	private final String SHAPE_ID_DATATYPE_TYPEBODY = IdentifierLiterals.SHAPE_ID_DATATYPE_TYPEBODY,
						 SHAPE_ID_DATATYPE_SHADOW = IdentifierLiterals.SHAPE_ID_DATATYPE_SHADOW,
						 SHAPE_ID_DATATYPE_NAME = IdentifierLiterals.SHAPE_ID_DATATYPE_NAME,
					     SHAPE_ID_DATATYPE_FIRSTLINE = IdentifierLiterals.SHAPE_ID_DATATYPE_FIRSTLINE,
					 	 SHAPE_ID_DATATYPE_SECONDLINE = IdentifierLiterals.SHAPE_ID_DATATYPE_SECONDLINE,
						 SHAPE_ID_DATATYPE_ATTRIBUTECONTAINER = IdentifierLiterals.SHAPE_ID_DATATYPE_ATTRIBUTECONTAINER, 
						 SHAPE_ID_DATATYPE_OPERATIONCONTAINER = IdentifierLiterals.SHAPE_ID_DATATYPE_OPERATIONCONTAINER,
					     IMG_ID_FEATURE_DATATYPE = IdentifierLiterals.IMG_ID_FEATURE_DATATYPE;
	
	//layout literals
	private final int MIN_WIDTH = LayoutLiterals.MIN_WIDTH_FOR_CLASS_OR_ROLE, 
					  MIN_HEIGHT = LayoutLiterals.MIN_HEIGHT_FOR_CLASS_OR_ROLE,
					  HEIGHT_NAME_SHAPE = LayoutLiterals.HEIGHT_NAME_SHAPE,
					  HEIGHT_ATTRITBUTE_SHAPE = LayoutLiterals.HEIGHT_ATTRITBUTE_SHAPE,
					  HEIGHT_OPERATION_SHAPE = LayoutLiterals.HEIGHT_OPERATION_SHAPE,
					  DATATYPE_CORNER_SIZE = LayoutLiterals.DATATYPE_CORNER_SIZE,
					  PUFFER_BETWEEN_ELEMENTS = LayoutLiterals.PUFFER_BETWEEN_ELEMENTS,
					  SHADOW_SIZE = LayoutLiterals.SHADOW_SIZE;
	private final IColorConstant COLOR_TEXT = LayoutLiterals.COLOR_TEXT,
								 COLOR_LINES = LayoutLiterals.COLOR_LINES,
								 COLOR_BACKGROUND = LayoutLiterals.COLOR_BACKGROUND,
								 COLOR_SHADOW = LayoutLiterals.COLOR_SHADOW;
	
	/**
	 * Class constructor
	 */
	public DataTypePattern() {
		super();
	}
	
	/**
	 * get method for the features name
	 * @return the name of the feature
	 */
	@Override
	public String getCreateName() {
		return DATATYPE_FEATURE_NAME;
	}
	
	/**
	 * enables the icon for the create feature in this pattern
	 * @return the image identifier for the icon of the create feature in this pattern
	 */
	@Override
	public String getCreateImageId() {
		return IMG_ID_FEATURE_DATATYPE;
	}
	
	/**
	 * checks if pattern is applicable for a given business object
	 * @return true, if business object is a {@link org.framed.iorm.model.Shape} of type {@link Type#DATA_TYPE}
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object businessObject) {
		if(businessObject instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
			if(shape.getType() == Type.DATA_TYPE) return true;
		}
		return false;
	}

	/**
	 * checks if pattern is applicable for a given pictogram element
	 * @return true, if business object of the pictogram element is a {@link org.framed.iorm.model.Shape} of type {@link Type#DATA_TYPE}
	 */
	@Override
	protected boolean isPatternControlled(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}

	/**
	 * checks if the pictogram element to edit with the pattern is its root
	 * @return true, if business object of the pictogram element is a {@link org.framed.iorm.model.Shape} of type {@link Type#DATA_TYPE}
	 */
	@Override
	protected boolean isPatternRoot(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}
	
	// add features
	//~~~~~~~~~~~~~
	@Override
	public boolean canAdd(IAddContext addContext) {
		//new Object is a data type
		if(addContext.getNewObject() instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) addContext.getNewObject();
			if(shape.getType()==Type.DATA_TYPE) {
				//target container is diagram with root model
				ContainerShape containerShape = getDiagram();
				if(containerShape instanceof Diagram) {
					if(GeneralUtil.getRootModelForDiagram((Diagram) containerShape) != null)
						return true;
		}	}	}
		return false;
	}

	@Override
	public PictogramElement add(IAddContext addContext) {
		//get container and new object
		org.framed.iorm.model.Shape addedDataType = (org.framed.iorm.model.Shape) addContext.getNewObject();
		ContainerShape targetDiagram = getDiagram();
		
		//get width, height and polygon points
		int width = addContext.getWidth(), height = addContext.getHeight();
		if(addContext.getWidth() < MIN_WIDTH) width = MIN_WIDTH;
		if(addContext.getHeight() < MIN_HEIGHT) height = MIN_HEIGHT;
		int points[] = new int[] { 0, 0+DATATYPE_CORNER_SIZE,			//Point 1  
								   0+DATATYPE_CORNER_SIZE, 0, 			//P2
								   width-DATATYPE_CORNER_SIZE, 0, 		//P3
								   width, DATATYPE_CORNER_SIZE,  		//P4
								   width, height-DATATYPE_CORNER_SIZE,	//P5 
								   width-DATATYPE_CORNER_SIZE, height,	//P6
								   DATATYPE_CORNER_SIZE, height,	    //P7
								   0, height-DATATYPE_CORNER_SIZE };	//P8
		
		//container for body shape and shadow
		ContainerShape containerShape = pictogramElementCreateService.createContainerShape(targetDiagram, false);
		
		//drop shadow
		ContainerShape dropShadowShape = pictogramElementCreateService.createContainerShape(containerShape, true);
		Polygon dropShadowPolygon = graphicAlgorithmService.createPolygon(dropShadowShape, points);
		dropShadowPolygon.setForeground(manageColor(COLOR_SHADOW));
		dropShadowPolygon.setBackground(manageColor(COLOR_SHADOW));
		graphicAlgorithmService.setLocationAndSize(dropShadowPolygon, addContext.getX()+SHADOW_SIZE, addContext.getY()+SHADOW_SIZE, width, height);
		
		//body shape of type
		ContainerShape typeBodyShape = pictogramElementCreateService.createContainerShape(containerShape, true);
		Polygon typeBodyPolygon = graphicAlgorithmService.createPolygon(typeBodyShape, points);
		typeBodyPolygon.setForeground(manageColor(COLOR_LINES));
		typeBodyPolygon.setBackground(manageColor(COLOR_BACKGROUND));
		graphicAlgorithmService.setLocationAndSize(typeBodyPolygon, addContext.getX(), addContext.getY(), width, height);
			
		//name
		Shape nameShape = pictogramElementCreateService.createShape(typeBodyShape, false);
		Text text = graphicAlgorithmService.createText(nameShape, addedDataType.getName());	
		text.setForeground(manageColor(COLOR_TEXT));	
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);	
		graphicAlgorithmService.setLocationAndSize(text, DATATYPE_CORNER_SIZE, 0, width-2*DATATYPE_CORNER_SIZE, HEIGHT_NAME_SHAPE);
		
		//first line
		Shape firstLineShape = pictogramElementCreateService.createShape(typeBodyShape, false);
		Polyline firstPolyline = graphicAlgorithmService.createPolyline(firstLineShape, new int[] {PUFFER_BETWEEN_ELEMENTS, HEIGHT_NAME_SHAPE, width-2*PUFFER_BETWEEN_ELEMENTS, HEIGHT_NAME_SHAPE});
		firstPolyline.setForeground(manageColor(COLOR_LINES));
		
		//attribute container
		ContainerShape attributeContainer = pictogramElementCreateService.createContainerShape(typeBodyShape, false);
		Rectangle attributeRectangle = graphicAlgorithmService.createRectangle(attributeContainer);
		attributeRectangle.setLineVisible(false);
		attributeRectangle.setBackground(manageColor(COLOR_BACKGROUND));
		int horizontalCenter = GeneralUtil.calculateHorizontalCenter(Type.DATA_TYPE, height);
		graphicAlgorithmService.setLocationAndSize(attributeRectangle, PUFFER_BETWEEN_ELEMENTS, HEIGHT_NAME_SHAPE+PUFFER_BETWEEN_ELEMENTS, 
												   width-2*PUFFER_BETWEEN_ELEMENTS, horizontalCenter-HEIGHT_NAME_SHAPE-2*PUFFER_BETWEEN_ELEMENTS);
		
		//second line
		Shape secondLineShape = pictogramElementCreateService.createShape(typeBodyShape, false);	
		Polyline secondPolyline = graphicAlgorithmService.createPolyline(secondLineShape, new int[] {0, horizontalCenter, width, horizontalCenter});
		secondPolyline.setForeground(manageColor(COLOR_LINES));
		
		//operation container
		ContainerShape operationContainer = pictogramElementCreateService.createContainerShape(typeBodyShape, false);
		Rectangle operationRectangle = graphicAlgorithmService.createRectangle(operationContainer);
		operationRectangle.setLineVisible(false);
		operationRectangle.setBackground(manageColor(COLOR_BACKGROUND));
		graphicAlgorithmService.setLocationAndSize(operationRectangle, PUFFER_BETWEEN_ELEMENTS, horizontalCenter+PUFFER_BETWEEN_ELEMENTS, 
												   width-2*PUFFER_BETWEEN_ELEMENTS, horizontalCenter-HEIGHT_NAME_SHAPE-2*PUFFER_BETWEEN_ELEMENTS);
		
		//setProperties
		PropertyUtil.setShape_IdValue(typeBodyPolygon, SHAPE_ID_DATATYPE_TYPEBODY);
		PropertyUtil.setShape_IdValue(dropShadowPolygon, SHAPE_ID_DATATYPE_SHADOW);
		PropertyUtil.setShape_IdValue(text, SHAPE_ID_DATATYPE_NAME);
		PropertyUtil.setShape_IdValue(firstPolyline, SHAPE_ID_DATATYPE_FIRSTLINE);
		PropertyUtil.setShape_IdValue(attributeRectangle, SHAPE_ID_DATATYPE_ATTRIBUTECONTAINER);
		PropertyUtil.setShape_IdValue(secondPolyline, SHAPE_ID_DATATYPE_SECONDLINE);
		PropertyUtil.setShape_IdValue(operationRectangle, SHAPE_ID_DATATYPE_OPERATIONCONTAINER);
		//set links
		link(containerShape, addedDataType);
		link(dropShadowShape, addedDataType);
		link(typeBodyShape, addedDataType);
		link(nameShape, addedDataType);
		link(firstLineShape, addedDataType);	
		link(attributeContainer, addedDataType);
		link(secondLineShape, addedDataType);
		link(operationContainer, addedDataType);
		//set directEditing informations
		IDirectEditingInfo directEditingInfo = getFeatureProvider().getDirectEditingInfo();
		directEditingInfo.setMainPictogramElement(typeBodyShape);
		directEditingInfo.setPictogramElement(nameShape);
		directEditingInfo.setGraphicsAlgorithm(text);
		//add anchors to the container
		pictogramElementCreateService.createChopboxAnchor(typeBodyShape);
		//set container as layout target
		layoutPictogramElement(containerShape);
		return containerShape;
	}		
	
	//create feature
	//~~~~~~~~~~~~~~
	@Override
	public boolean canCreate(ICreateContext createContext) {
		//target container is either diagram with model or a group
		ContainerShape containerShape = getDiagram();
		if(containerShape instanceof Diagram) {
			if(GeneralUtil.getRootModelForDiagram((Diagram) containerShape) != null)
				return true;
		}
		return false;
	}

	@Override
	public Object[] create(ICreateContext createContext) {
		//create new data type
		org.framed.iorm.model.Shape newDataType = OrmFactory.eINSTANCE.createShape();
		newDataType.setType(Type.DATA_TYPE);
		newDataType.setName(STANDARD_DATATYPE_NAME);
		//add new natural type to the elements of the model
		Model model = GeneralUtil.getRootModelForDiagram(getDiagram());
		if(newDataType.eResource() != null) getDiagram().eResource().getContents().add(newDataType);
		model.getElements().add(newDataType);
		newDataType.setContainer(model);
		//create segments
		Segment attributeSegment = OrmFactory.eINSTANCE.createSegment(),
				operationSegment = OrmFactory.eINSTANCE.createSegment();
		getDiagram().eResource().getContents().add(attributeSegment);
		getDiagram().eResource().getContents().add(operationSegment);
		newDataType.setFirstSegment(attributeSegment);
		newDataType.setSecondSegment(operationSegment);
		//enable direct editing
		getFeatureProvider().getDirectEditingInfo().setActive(true);
		//add to graphiti representation
		addGraphicalRepresentation(createContext, newDataType);
		return new Object[] { newDataType };
	}
	
	//direct editing feature
	//~~~~~~~~~~~~~~~~~~~~~~~	
	/**
	 * sets the editing type as a text field for the direct editing of the attributes or operations name
	 */
	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}
		
	@Override
	public boolean canDirectEdit(IDirectEditingContext editingContext) {
		Object businessObject = getBusinessObject(editingContext);
		GraphicsAlgorithm graphicsAlgorithm = editingContext.getGraphicsAlgorithm();
		if(businessObject instanceof org.framed.iorm.model.Shape && graphicsAlgorithm instanceof Text) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
			if(shape.getType() == Type.DATA_TYPE) {
				return true;
		}	}
		return false;
	}

	@Override
	public String getInitialValue(IDirectEditingContext editingContext) {
		org.framed.iorm.model.Shape dateType = (org.framed.iorm.model.Shape) getBusinessObject(editingContext);
		return dateType.getName();
	}
		
	@Override
	public String checkValueValid(String newName, IDirectEditingContext editingContext) {
		if(!(DirectEditingUtil.matchesIdentifier(newName))) return DIRECTEDITING_DATATYPE;
	    return null;
	}
		
	@Override
	public void setValue(String value, IDirectEditingContext editingContext) {	     
		org.framed.iorm.model.Shape naturalType = (org.framed.iorm.model.Shape) getBusinessObject(editingContext);
		naturalType.setName(value);
	    updatePictogramElement(((Shape) editingContext.getPictogramElement()).getContainer());
	}
	
	//layout feature
	//~~~~~~~~~~~~~~
	@Override
	public boolean canLayout(ILayoutContext layoutContext) {
		PictogramElement element = layoutContext.getPictogramElement();
		if(element instanceof ContainerShape) {
			EList<EObject> businessObjects = element.getLink().getBusinessObjects();
			if(businessObjects.size()==1) {
				if(businessObjects.get(0) instanceof org.framed.iorm.model.Shape) {
					org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObjects.get(0);
					if(shape.getType() == Type.DATA_TYPE) return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean layout(ILayoutContext layoutContext) {
		boolean layoutChanged = false;
		ContainerShape container = (ContainerShape) layoutContext.getPictogramElement();
		Polygon typeBodyPolygon = null;
		//return false is container is overall container that has typeBodyShape and dropShadowShape as children
		if(container.getGraphicsAlgorithm() == null)  return false; 
		//container is typeBodyShape, else return false
		if(PropertyUtil.isShape_IdValue(container.getGraphicsAlgorithm(), SHAPE_ID_DATATYPE_TYPEBODY))
			typeBodyPolygon = (Polygon) container.getGraphicsAlgorithm(); 
		else return false;
		//get the drop shadow polygon to the type body polygon
		Polygon dropShadowPolygon = (Polygon) container.getContainer().getChildren().get(0).getGraphicsAlgorithm();
		
		//ensure the minimal width and height
		if(typeBodyPolygon.getWidth() < MIN_WIDTH) typeBodyPolygon.setWidth(MIN_WIDTH);
		if(typeBodyPolygon.getHeight() < MIN_HEIGHT) typeBodyPolygon.setHeight(MIN_HEIGHT);
		int containerWidth = typeBodyPolygon.getWidth();
	    int containerHeight = typeBodyPolygon.getHeight();
	    //set the size of the drop shadow to the new size of the type body
	    dropShadowPolygon.setWidth(containerWidth);
	    dropShadowPolygon.setHeight(containerHeight);
        //set the x and y value of the drop shadow to the values of the type body
	    dropShadowPolygon.setX(typeBodyPolygon.getX()+SHADOW_SIZE);
	    dropShadowPolygon.setY(typeBodyPolygon.getY()+SHADOW_SIZE);
	    
	    for (Shape shape : container.getChildren()){
	    	GraphicsAlgorithm graphicsAlgorithm = shape.getGraphicsAlgorithm();
	        //name
	        if (graphicsAlgorithm instanceof Text) {
	        	Text text = (Text) graphicsAlgorithm;	
	            if(PropertyUtil.isShape_IdValue(text, SHAPE_ID_DATATYPE_NAME)) {
	            	graphicAlgorithmService.setLocationAndSize(text, DATATYPE_CORNER_SIZE, 0, containerWidth-2*DATATYPE_CORNER_SIZE, HEIGHT_NAME_SHAPE);
	            	layoutChanged=true;
	            }	
	       }
	       //first and second line
	       if (graphicsAlgorithm instanceof Polyline) {	   
	    	   Polyline polyline = (Polyline) graphicsAlgorithm;  
		       if(PropertyUtil.isShape_IdValue(polyline, SHAPE_ID_DATATYPE_SECONDLINE)) {   
		            polyline.getPoints().set(0, graphicAlgorithmService.createPoint(0, (((containerHeight)-HEIGHT_NAME_SHAPE-DATATYPE_CORNER_SIZE)/2)+HEIGHT_NAME_SHAPE));
		            polyline.getPoints().set(1, graphicAlgorithmService.createPoint(containerWidth, (((containerHeight-HEIGHT_NAME_SHAPE-DATATYPE_CORNER_SIZE))/2)+HEIGHT_NAME_SHAPE));
		            layoutChanged=true;
		      }
		      if(PropertyUtil.isShape_IdValue(polyline, SHAPE_ID_DATATYPE_FIRSTLINE)) {
		            polyline.getPoints().set(1, graphicAlgorithmService.createPoint(containerWidth-PUFFER_BETWEEN_ELEMENTS, polyline.getPoints().get(1).getY()));
		            layoutChanged=true;
		      }	
	       }
	       //attribute and operation container
	       if (graphicsAlgorithm instanceof Rectangle) {
	    	   Rectangle rectangle = (Rectangle) graphicsAlgorithm;  
		       if(PropertyUtil.isShape_IdValue(rectangle, SHAPE_ID_DATATYPE_ATTRIBUTECONTAINER)) {
		    	   int newHeight = (((containerHeight)-HEIGHT_NAME_SHAPE-DATATYPE_CORNER_SIZE)/2)-PUFFER_BETWEEN_ELEMENTS,
		               newWidth = (typeBodyPolygon.getWidth()-2*PUFFER_BETWEEN_ELEMENTS);            				
		           rectangle.setHeight(newHeight);
		           rectangle.setWidth(newWidth);
		           ContainerShape attributeContainerShape = (ContainerShape) shape;	       
		           EList<Shape> attributes = attributeContainerShape.getChildren();
		            			            	
		           //set all attributes visible
		           for(int j = 0; j<attributes.size(); j++) attributes.get(j).setVisible(true);	   	
		           //check if not all attributes fit in the attribute field
		           if(attributeContainerShape.getChildren().size()*HEIGHT_ATTRITBUTE_SHAPE>newHeight) {	            		
		        	   int fittingAttributes = (newHeight-HEIGHT_ATTRITBUTE_SHAPE)/HEIGHT_ATTRITBUTE_SHAPE;	   
		        	   //set not fitting attributes to invisible
		        	   for(int k = fittingAttributes; k<attributes.size(); k++) {
		        	   attributeContainerShape.getChildren().get(k).setVisible(false);
		        	   }	            			
		            }	            			
		            layoutChanged=true;
		       }
		       if(PropertyUtil.isShape_IdValue(rectangle, SHAPE_ID_DATATYPE_OPERATIONCONTAINER)) {
		    	   int horizontalCenter = GeneralUtil.calculateHorizontalCenter(Type.DATA_TYPE, containerHeight);
		           int newHeight = horizontalCenter-HEIGHT_NAME_SHAPE-2*PUFFER_BETWEEN_ELEMENTS;
		           int newWidth = typeBodyPolygon.getWidth()-2*PUFFER_BETWEEN_ELEMENTS;		
		           int newY = horizontalCenter+PUFFER_BETWEEN_ELEMENTS;	            	
		           rectangle.setHeight(newHeight);
		           rectangle.setWidth(newWidth);
		           rectangle.setY(newY);
		           ContainerShape operationContainerShape = (ContainerShape) shape;
		           EList<Shape> operations = operationContainerShape.getChildren();
		           
			       //set place of operations
			       for(int m = 0; m<operations.size(); m++) {
			    	   operationContainerShape.getChildren().get(m).getGraphicsAlgorithm().setY(newY+m*HEIGHT_OPERATION_SHAPE);
			       }
			       
			       //set all operations visible
			       for(int n = 0; n<operations.size(); n++) operations.get(n).setVisible(true);
			       		//check if not all operations fit in the operations field
			       		if(operations.size()*HEIGHT_OPERATION_SHAPE>newHeight) {	            		
			       			int fittingOperations = (newHeight-HEIGHT_OPERATION_SHAPE)/HEIGHT_OPERATION_SHAPE;	   
			       			//set not fitting operations to invisible
			       			for(int o = fittingOperations; o<operations.size(); o++) {
			       				operationContainerShape.getChildren().get(o).setVisible(false);            				
			       }   	}  	}    
		           layoutChanged=true;
		}	}
	    return layoutChanged;
	}
	
	
	
	//update feature
	//~~~~~~~~~~~~~~
	@Override
	public boolean canUpdate(IUpdateContext updateContext) {
		//check if object to update is a Data Type
		PictogramElement pictogramElement = updateContext.getPictogramElement();
		Object businessObject =  getBusinessObjectForPictogramElement(pictogramElement);
		if(businessObject instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
			if(shape.getType() == Type.DATA_TYPE) {
				return true;
		}	}
		return false;
	}
	
	@Override
	public IReason updateNeeded(IUpdateContext updateContext) {
		//check for changed names 
		PictogramElement pictogramElement = updateContext.getPictogramElement();
		if( pictogramElement.getGraphicsAlgorithm() != null &&
			PropertyUtil.isShape_IdValue(pictogramElement.getGraphicsAlgorithm(), SHAPE_ID_DATATYPE_TYPEBODY)) {
			//pictogram name of data type, attributes and operations
			String pictogramTypeName = GeneralUtil.getNameOfPictogramElement(pictogramElement, SHAPE_ID_DATATYPE_NAME);
			List<String> pictogramAttributeNames = GeneralUtil.getpictogramAttributeNames(pictogramElement, SHAPE_ID_DATATYPE_ATTRIBUTECONTAINER);
			List<String> pictogramOperationNames = GeneralUtil.getpictogramOperationNames(pictogramElement, SHAPE_ID_DATATYPE_OPERATIONCONTAINER);
			//business name and attributes
			String businessTypeName = GeneralUtil.getNameOfBusinessObject(getBusinessObjectForPictogramElement(pictogramElement));
			List<String> businessAttributeNames = getBusinessAttributeNames(pictogramElement);
			List<String> businessOperationNames = getBusinessOperationNames(pictogramElement);
								
			//check for update: different names, different amount of attibutes/ operations
			if(pictogramTypeName==null || businessTypeName==null) return Reason.createTrueReason("Name is null.");
			if(!(pictogramTypeName.equals(businessTypeName))) return Reason.createTrueReason("Name is out of date.");  
			if(pictogramAttributeNames.size() != businessAttributeNames.size()) return Reason.createTrueReason("Different amount of Attributes.");
			if(pictogramOperationNames.size() != businessOperationNames.size()) return Reason.createTrueReason("Different amount of Operations.");
			for(int i=0; i<pictogramAttributeNames.size(); i++) {
				if(!(pictogramAttributeNames.get(i).equals(businessAttributeNames.get(i)))) return Reason.createTrueReason("Different names of Attributes.");
			}	
			for(int i=0; i<pictogramOperationNames.size(); i++) {
				if(!(pictogramOperationNames.get(i).equals(businessOperationNames.get(i)))) return Reason.createTrueReason("Different names of Operations.");
		}	}
		return Reason.createFalseReason();
	}
	
	private List<String> getBusinessAttributeNames(PictogramElement pictogramElement) {
		List<String> businessAttributeNames = new ArrayList<String>();
		if (pictogramElement instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) pictogramElement;
			for (Shape shape : containerShape.getChildren()) {
				if(shape instanceof ContainerShape) {
					ContainerShape innerContainerShape = (ContainerShape) shape;
					if(innerContainerShape.getGraphicsAlgorithm() instanceof Rectangle) {
						Rectangle rectangle = (Rectangle) innerContainerShape.getGraphicsAlgorithm();
						if(PropertyUtil.isShape_IdValue(rectangle, SHAPE_ID_DATATYPE_ATTRIBUTECONTAINER)) {
							for(Shape attributeShape : innerContainerShape.getChildren()) {
								NamedElement attribute = (NamedElement) getBusinessObjectForPictogramElement(attributeShape);
								businessAttributeNames.add(attribute.getName());
		}	}	}	}	}	}	
		return businessAttributeNames;
	}
	
	private List<String> getBusinessOperationNames(PictogramElement pictogramElement) {
		List<String> businessOperationNames = new ArrayList<String>();
		if (pictogramElement instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) pictogramElement;
			for (Shape shape : containerShape.getChildren()) {
				if(shape instanceof ContainerShape) {
					ContainerShape innerContainerShape = (ContainerShape) shape;
					if(innerContainerShape.getGraphicsAlgorithm() instanceof Rectangle) {
						Rectangle rectangle = (Rectangle) innerContainerShape.getGraphicsAlgorithm();
						if(PropertyUtil.isShape_IdValue(rectangle, SHAPE_ID_DATATYPE_OPERATIONCONTAINER)) {
							for(Shape operationShape : innerContainerShape.getChildren()) {
								NamedElement operation = (NamedElement) getBusinessObjectForPictogramElement(operationShape);
								businessOperationNames.add(operation.getName());
		}	}	}	}	}	}	
		return businessOperationNames;
	}
	
	@Override
	public boolean update(IUpdateContext updateContext) {
		int counter, newY;
		boolean returnValue = false;
         
		PictogramElement pictogramElement = updateContext.getPictogramElement();
		//business names of natural type, attributes and operations
		String businessTypeName = GeneralUtil.getNameOfBusinessObject(getBusinessObjectForPictogramElement(pictogramElement));
		List<String> businessAttributeNames = getBusinessAttributeNames(pictogramElement);
		List<String> businessOperationNames = getBusinessOperationNames(pictogramElement);
		
		//set type name in pictogram model
        if (pictogramElement instanceof ContainerShape) {     
            ContainerShape containerShape = (ContainerShape) pictogramElement;
            int horizontalCenter = GeneralUtil.calculateHorizontalCenter(Type.DATA_TYPE, containerShape.getGraphicsAlgorithm().getHeight());
            for (Shape shape : containerShape.getChildren()) {
                if (shape.getGraphicsAlgorithm() instanceof Text) {
                    Text text = (Text) shape.getGraphicsAlgorithm();
                    if(PropertyUtil.isShape_IdValue(text, SHAPE_ID_DATATYPE_NAME)) {
                    	text.setValue(businessTypeName);
                    	returnValue = true;
                    }           
                }
                //set attribute and operation names and their places in pictogram model
                if(shape instanceof ContainerShape) {
                	ContainerShape innerContainerShape = (ContainerShape) shape;
					if(innerContainerShape.getGraphicsAlgorithm() instanceof Rectangle) {
						Rectangle rectangle = (Rectangle) innerContainerShape.getGraphicsAlgorithm();
						//Attributes
						counter = 0;
						newY = HEIGHT_NAME_SHAPE+PUFFER_BETWEEN_ELEMENTS;
						if(PropertyUtil.isShape_IdValue(rectangle, SHAPE_ID_DATATYPE_ATTRIBUTECONTAINER)) {
							for(Shape attributeShape : innerContainerShape.getChildren()) {
								Text text = (Text) attributeShape.getGraphicsAlgorithm();
								text.setValue(businessAttributeNames.get(counter));
								attributeShape.getGraphicsAlgorithm().setY(newY+counter*HEIGHT_ATTRITBUTE_SHAPE);
								returnValue = true;
								counter++;
							}	
						}
						//Operations
						counter = 0;
						newY = horizontalCenter+PUFFER_BETWEEN_ELEMENTS;
						if(PropertyUtil.isShape_IdValue(rectangle, SHAPE_ID_DATATYPE_OPERATIONCONTAINER)) {
							for(Shape operationShape : innerContainerShape.getChildren()) {
								Text text = (Text) operationShape.getGraphicsAlgorithm();
								text.setValue(businessOperationNames.get(counter));									
								operationShape.getGraphicsAlgorithm().setY(newY+counter*HEIGHT_OPERATION_SHAPE);
								returnValue = true;
								counter++;
		}	}	}	}	}	}
        return returnValue;
	}
	
	//move feature
	//~~~~~~~~~~~~
	//disable that the user can move the drop shadow manually
	@Override
	public boolean canMoveShape(IMoveShapeContext moveContext) {
		if(PropertyUtil.isShape_IdValue(moveContext.getPictogramElement().getGraphicsAlgorithm(), SHAPE_ID_DATATYPE_SHADOW)) {
			return false;
		}
		ContainerShape typeBodyShape = (ContainerShape) moveContext.getPictogramElement();
		ContainerShape dropShadowShape = (ContainerShape) ((ContainerShape) typeBodyShape).getContainer().getChildren().get(0);
		//copied and expanded from super.canMoveShape(IMoveShapeContext moveContext)
		return moveContext.getSourceContainer() != null && 
			   (moveContext.getSourceContainer().equals(moveContext.getTargetContainer()) ||
			    moveContext.getTargetContainer().equals(dropShadowShape)) && 
			   isPatternRoot(moveContext.getPictogramElement());
	}
		
	//move the type body and the drop shadow 
	@Override
	public void moveShape(IMoveShapeContext moveContext) {
		ContainerShape typeBodyShape = (ContainerShape) moveContext.getPictogramElement();
		Polygon typeBodyPolygon = (Polygon) typeBodyShape.getGraphicsAlgorithm();
		ContainerShape dropShadowShape = (ContainerShape) ((ContainerShape) typeBodyShape).getContainer().getChildren().get(0);
		Polygon dropShadowPolygon = (Polygon) dropShadowShape.getGraphicsAlgorithm();
			
		if(moveContext.getSourceContainer().equals(moveContext.getTargetContainer())) {
			dropShadowPolygon.setX(moveContext.getX()+SHADOW_SIZE);
			dropShadowPolygon.setY(moveContext.getY()+SHADOW_SIZE);
			super.moveShape(moveContext);
		} else {
			//targetContainer of moveContext is dropShadowShape
			//set targetContainer to diagram and use special calculation for the new position of type body and drop shadow 
			dropShadowPolygon.setX(typeBodyPolygon.getX()+moveContext.getX()+2*SHADOW_SIZE);
			dropShadowPolygon.setY(typeBodyPolygon.getY()+moveContext.getY()+2*SHADOW_SIZE);
			MoveShapeContext changedMoveContextForTypeBody = new MoveShapeContext(moveContext.getShape());
			changedMoveContextForTypeBody.setTargetContainer(dropShadowShape.getContainer());
			changedMoveContextForTypeBody.setX(typeBodyPolygon.getX()+moveContext.getX()+SHADOW_SIZE);
			changedMoveContextForTypeBody.setY(typeBodyPolygon.getY()+moveContext.getY()+SHADOW_SIZE);
			super.moveShape(changedMoveContextForTypeBody);
		}
	}
		
	//resize feature
	//~~~~~~~~~~~~~~
	//disable that the user can resize the drop shadow manually
	@Override
	public boolean canResizeShape(IResizeShapeContext resizeContext) {
		if(PropertyUtil.isShape_IdValue(resizeContext.getPictogramElement().getGraphicsAlgorithm(), SHAPE_ID_DATATYPE_SHADOW)) {
			return false;
		}
		return super.canResizeShape(resizeContext);
	}
		
	@Override
	public void resizeShape(IResizeShapeContext resizeContext) {
		ContainerShape typeBodyShape = (ContainerShape) resizeContext.getPictogramElement();
		Polygon typeBodyPolygon = (Polygon) typeBodyShape.getGraphicsAlgorithm();
		ContainerShape dropShadowShape = (ContainerShape) ((ContainerShape) typeBodyShape).getContainer().getChildren().get(0);
		Polygon dropShadowPolygon = (Polygon) dropShadowShape.getGraphicsAlgorithm();
		int X = resizeContext.getX();
		int Y = resizeContext.getY();
		int height = MIN_HEIGHT, width = MIN_WIDTH;
		if(resizeContext.getHeight() > MIN_HEIGHT) height = resizeContext.getHeight();
		if(resizeContext.getWidth() > MIN_WIDTH) width = resizeContext.getWidth();
			
		//set size of polygon
		graphicAlgorithmService.setLocationAndSize(typeBodyPolygon, X, Y, width, height);
			
		//resize the shape polygon
		//Point 1 stays the same
		//P2 stays the same		
		//P3 x=width-DATATYPE_CORNER_SIZE, y=0		
		typeBodyPolygon.getPoints().set(2, graphicAlgorithmService.createPoint(width-DATATYPE_CORNER_SIZE, 0));
		dropShadowPolygon.getPoints().set(2, graphicAlgorithmService.createPoint(width-DATATYPE_CORNER_SIZE, 0));
		//P4 x= width, y=DATATYPE_CORNER_SIZE
		typeBodyPolygon.getPoints().set(3, graphicAlgorithmService.createPoint(width, DATATYPE_CORNER_SIZE));
		dropShadowPolygon.getPoints().set(3, graphicAlgorithmService.createPoint(width, DATATYPE_CORNER_SIZE));
		//P5 x=width, y=height-DATATYPE_CORNER_SIZE
		typeBodyPolygon.getPoints().set(4, graphicAlgorithmService.createPoint(width, height-DATATYPE_CORNER_SIZE));
		dropShadowPolygon.getPoints().set(4, graphicAlgorithmService.createPoint(width, height-DATATYPE_CORNER_SIZE));
		//P6 x=width-DATATYPE_CORNER_SIZE y=height
		typeBodyPolygon.getPoints().set(5, graphicAlgorithmService.createPoint(width-DATATYPE_CORNER_SIZE, height));
		dropShadowPolygon.getPoints().set(5, graphicAlgorithmService.createPoint(width-DATATYPE_CORNER_SIZE, height));
		//P7 x=DATATYPE_CORNER_SIZE, x=height
		typeBodyPolygon.getPoints().set(6, graphicAlgorithmService.createPoint(DATATYPE_CORNER_SIZE, height));
		dropShadowPolygon.getPoints().set(6, graphicAlgorithmService.createPoint(DATATYPE_CORNER_SIZE, height));
		//P8 x=0, y=height-DATATYPE_CORNER_SIZE 
		typeBodyPolygon.getPoints().set(7, graphicAlgorithmService.createPoint(0, height-DATATYPE_CORNER_SIZE));
		dropShadowPolygon.getPoints().set(7, graphicAlgorithmService.createPoint(0, height-DATATYPE_CORNER_SIZE));
		layoutPictogramElement(resizeContext.getShape());
	}
		
	//delete feature
	//~~~~~~~~~~~~~~
	//disable that the user can delete the drop shadow manually
	@Override
	public boolean canDelete(IDeleteContext deleteContext) {
		if(PropertyUtil.isShape_IdValue(deleteContext.getPictogramElement().getGraphicsAlgorithm(), SHAPE_ID_DATATYPE_SHADOW)) {
			return false;
		}
		return super.canDelete(deleteContext);
	}
			
	//delete parent container (the one that contains drop shadow shape and type body shape)
	@Override
	public void delete(IDeleteContext deleteContext) {
		ContainerShape containerShape = (ContainerShape) ((ContainerShape) deleteContext.getPictogramElement()).getContainer();
		DeleteContext deleteContextForAllShapes = new DeleteContext(containerShape);
		deleteContextForAllShapes.setMultiDeleteInfo(new MultiDeleteInfo(false, false, 0));
		super.delete(deleteContextForAllShapes);
	}
}
