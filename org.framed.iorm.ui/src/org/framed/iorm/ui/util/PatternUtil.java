package org.framed.iorm.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.ModelElement;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.pattern.shapes.GroupPattern; //*import for javadoc link

/**
 * This class offers several utility operations used by the graphiti patterns.
 * @author Kevin Kassin
 */
public class PatternUtil {
	
	/**
	 * the identifiers for graphics algorithms of group pictograms gathered from {@link IdentifierLiterals}
	 */
	static final String SHAPE_ID_GROUP_TYPEBODY = IdentifierLiterals.SHAPE_ID_GROUP_TYPEBODY,
				  	    SHAPE_ID_GROUP_NAME = IdentifierLiterals.SHAPE_ID_GROUP_NAME,
				  	    SHAPE_ID_GROUP_MODEL = IdentifierLiterals.SHAPE_ID_GROUP_MODEL;

	/**
	 * fetches all the names of the groups content that are shown in <em>model container</em> of the group
	 * <p>
	 * If its not clear what <em>model container</em> and <em>type body shape</em> means, see 
	 * {@link GroupPattern#add} for reference. 
	 * @param pictogramElement the type body shape of of a group
	 * @return a list of the shown names of child elements of a group
	 */
	public static List<String> getModelContainerElementsNames(PictogramElement pictogramElement) {
		List<String> modelContainerElementsNames = new ArrayList<String>();
		if(pictogramElement instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) pictogramElement;
			for(Shape shape : containerShape.getChildren()) {
				if(PropertyUtil.isShape_IdValue(shape, SHAPE_ID_GROUP_MODEL)) {
					ContainerShape modelContainer = (ContainerShape) shape; 
					for(Shape modelContainterElement : modelContainer.getChildren()) {
						Text text = (Text) modelContainterElement.getGraphicsAlgorithm();
						modelContainerElementsNames.add(text.getValue());
		}	}	}	}	
		return modelContainerElementsNames;
	}

	/**
	 * fetches all the names of the actual direct child elements in a groups model
	 * @param pictogramElement the pictogram/ shape element of the group
	 * @param diagram the diagram the pattern that called this operation works in
	 * @return a list of names of all direct child elements in a groups model
	 */
	public static List<String> getModelElementsNames(PictogramElement pictogramElement, Diagram diagram) {
		List<String> modelElementsNames = new ArrayList<String>();
		Diagram groupDiagram = DiagramUtil.getGroupDiagramForGroupShape((ContainerShape) pictogramElement, diagram);
		Model groupModel = DiagramUtil.getLinkedModelForDiagram(groupDiagram);
		for(ModelElement modelElement : groupModel.getElements()) {
			modelElementsNames.add(modelElement.getName());
		}
		return modelElementsNames;
	}

	/**
	 * This operation gets the name of a pictogram element with text shape as children.
	 * @param pictogramElement the pictogram element to get the name of
	 * @param SHAPE_ID_NAME the identifier of the textshape
	 * @return the name of a pictogram element if it has a text shape as children and return null else 
	 */
	public static String getNameOfPictogramElement(PictogramElement pictogramElement, String SHAPE_ID_NAME) {
		if (pictogramElement instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) pictogramElement;
			for (Shape shape : containerShape.getChildren()) {
				if (shape.getGraphicsAlgorithm() instanceof Text) {
					Text text = (Text) shape.getGraphicsAlgorithm();
					if(PropertyUtil.isShape_IdValue(shape, SHAPE_ID_NAME)) {
						return text.getValue();
					}
		} 	}	}
		return null;
	}

	/**
	 * This operation gets the name of a business object that is an {@link org.framed.iorm.model.Shape}
	 * @param businessObject the business object to get the name of
	 * @return the name of the business object if it is an {@link org.framed.iorm.model.Shape}
	 */
	public static String getNameOfBusinessObject(Object businessObject) {
		if (businessObject instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
			return shape.getName();
		}
		return null;
	}

	/**
	 * This method gets the names of the operations of a pictogram element that has an operation container shape.
	 * @param pictogramElement the pictogram element to get the operation names of
	 * @param SHAPE_ID_OPERATIONCONTAINER the identifier of the operation container shape
	 * @return the operation names of the pictogram element if it has an operation container shape and returns null else
	 */
	public static List<String> getpictogramOperationNames(PictogramElement pictogramElement, String SHAPE_ID_OPERATIONCONTAINER) {
		List<String> pictogramOperationNames = new ArrayList<String>();
		if (pictogramElement instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) pictogramElement;
			for (Shape shape : containerShape.getChildren()) {
				if(shape instanceof ContainerShape) {
					ContainerShape innerContainerShape = (ContainerShape) shape;
					if(PropertyUtil.isShape_IdValue(innerContainerShape, SHAPE_ID_OPERATIONCONTAINER)) {
						for(Shape operationShape : innerContainerShape.getChildren()) {
							Text text = (Text) operationShape.getGraphicsAlgorithm();
							pictogramOperationNames.add(text.getValue());
		}	}	}	}	}	
		return pictogramOperationNames;
	}

	/**
	 * This operation gets the names of the attributes of a pictogram element that has an attribute container shape.
	 * @param pictogramElement the pictogram element to get the attribute names of
	 * @param SHAPE_ID_ATTRIBUTECONTAINER the identifier of the attribute container shape
	 * @return the attribute names of the pictogram element if it has an attribute container shape and returns null else
	 */
	public static List<String> getpictogramAttributeNames(PictogramElement pictogramElement, String SHAPE_ID_ATTRIBUTECONTAINER) {
		List<String> pictogrammAttributeNames = new ArrayList<String>();
		if (pictogramElement instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) pictogramElement;
			for (Shape shape : containerShape.getChildren()) {
				if(shape instanceof ContainerShape) {
					ContainerShape innerContainerShape = (ContainerShape) shape;
					if(PropertyUtil.isShape_IdValue(innerContainerShape, SHAPE_ID_ATTRIBUTECONTAINER)) {
						for(Shape attributeShape : innerContainerShape.getChildren()) {
							Text text = (Text) attributeShape.getGraphicsAlgorithm();
							pictogrammAttributeNames.add(text.getValue());
		}	}	}	}	}	
		return pictogrammAttributeNames;
	}

	/**
	 * fetches the <em>type body shape</em> of group that has the given diagram attached to
	 * <p>
	 * If its not clear what <em>type body shape</em> means, see {@link GroupPattern#add} for reference. 
	 * @param diagram the diagram to find the groups type body shape for
	 * @return the type body shape of the group that has the given diagram attached
	 */
	public static ContainerShape getGroupTypeBodyForGroupsDiagram(Diagram diagram) {
		String groupName = null,
			   diagramName = diagram.getName();
		Diagram containerDiagram = DiagramUtil.getContainerDiagramForAnyDiagram(diagram);
		List<ContainerShape> groupTypeBodies = getAllGroupTypeBodiesForContainerDiagram(containerDiagram);
		for(ContainerShape groupTypeBody : groupTypeBodies) {
			for(Shape innerShape : groupTypeBody.getChildren()) {
				if(PropertyUtil.isShape_IdValue(innerShape, SHAPE_ID_GROUP_NAME))
					groupName = ((Text) innerShape.getGraphicsAlgorithm()).getValue();
			}
			if(groupName != null &&
			   groupName.equals(diagramName))
				return groupTypeBody;
		}
		return null;
	}
	
	/**
	 * FOR ALL TYPES...
	 * verallgemeinern
	 */
	private static List<ContainerShape> getAllGroupTypeBodiesForContainerDiagram(Diagram containerDiagram) {
		List<ContainerShape> groupTypeBodies = new ArrayList<ContainerShape>();
		Diagram mainDiagram = (Diagram) containerDiagram.getChildren().get(0);//TODO better
		for(Shape shape : mainDiagram.getChildren()) {
			if(shape instanceof ContainerShape &&
			   ((ContainerShape) shape).getGraphicsAlgorithm() == null) {
				ContainerShape containershape = (ContainerShape) shape;
				for(Shape innerShape : containershape.getChildren()) {
					if(innerShape.getGraphicsAlgorithm() != null &&
					   PropertyUtil.isShape_IdValue(innerShape, SHAPE_ID_GROUP_TYPEBODY)) {
						groupTypeBodies.add((ContainerShape) innerShape);
		}	}	}	}
		return groupTypeBodies;
	}
	
	public static List<String> getListOfModelElementNamesOfType(Diagram diagram, Type type) {
		List<ModelElement> modelElements = new ArrayList<ModelElement>();
		getListOfModelElementsOfType(diagram, type, modelElements);
		return null;
	}
	
	private static List<ModelElement> getListOfModelElementsOfType(Diagram diagram, Type type, List<ModelElement> modelElements) {
		for(Shape shape : diagram.getChildren()) {
			//diagram
			if(shape instanceof Diagram) {
				getListOfModelElementsOfType((Diagram) shape, type, modelElements);
			}
			if(shape instanceof ContainerShape &&
			   !(shape instanceof Diagram)) {
				ContainerShape containerShape = (ContainerShape) shape;
				getListOfModelElementsOfType((Diagram) shape, type, modelElements);
			}
		}		
		return null;
	}
	
	/**
	 * calculate the string for a group or compartment type element that is shown in the group as
	 * preview of its content
	 * @param modelElement the model elemen in the group or compartment type to calculate the shown string for
	 * @return the value of preview string of group or compartment type content for the given modelElement
	 */
	public static String getGroupOrCompartmentTypeElementText(ModelElement modelElement) {
		return modelElement.getType().toString().toUpperCase() + ": " + modelElement.getName();
	}

}
