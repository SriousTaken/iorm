package org.framed.iorm.ui.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.literals.LayoutLiterals;
import org.framed.iorm.ui.literals.URLLiterals;

public class MethodUtil {
	
	/**
	 * the layout integers this class need to perform the operation {@link #calculateHorizontalCenter}
	 * gathered from {@link LayoutLiterals}
	 */
	private static final int HEIGHT_NAME_SHAPE = LayoutLiterals.HEIGHT_NAME_SHAPE,
			 		  	 	 DATATYPE_CORNER_SIZE = LayoutLiterals.DATATYPE_CORNER_SIZE;
	
	/**
	 * This operation calculates where the horizontal center of a class or role is.
	 * <p>
	 * Depending on the the type of the class or role and its height the horizontal center position is returned.
	 * @param type the type of the class or role
	 * @param heightOfClassOrRole the height of the class or role
	 * @return the horizontal center position
	 */
	public static final int calculateHorizontalCenter(Type type, int heightOfClassOrRole) {
		if(type == Type.NATURAL_TYPE) 
			return ((heightOfClassOrRole-HEIGHT_NAME_SHAPE)/2)+HEIGHT_NAME_SHAPE;
		if(type == Type.DATA_TYPE)	
			return ((heightOfClassOrRole-HEIGHT_NAME_SHAPE-DATATYPE_CORNER_SIZE)/2)+HEIGHT_NAME_SHAPE;
		return 0;
	}
	
	/**
	 * This operation gets the root {@link Model} for a given {@link Diagram}.
	 * @param diagram The diagram to get the model from
	 * @return the root model of the given diagram if there is exactly one model found and returns null else
	 */
	public static final Model getDiagramRootModel(Diagram diagram) {
		List<Model> models=  new ArrayList<Model>();
		for(EObject eObject : diagram.eResource().getContents()) {
			if(eObject instanceof Model) {
				models.add((Model) eObject);
		}	}
		if(models.size()==1) return models.get(0);
		return null;
	}
	
	/**
	 * This operation gets the name of a pictogram element with text shape as children.
	 * @param pictogramElement the pictogram element to get the name of
	 * @param SHAPE_ID_NAME the identifier of the textshape
	 * @return the name of a pictogram element if it has a text shape as children and return null else 
	 */
	public static String getPictogramTypeName(PictogramElement pictogramElement, String SHAPE_ID_NAME) {
		if (pictogramElement instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) pictogramElement;
			for (Shape shape : containerShape.getChildren()) {
				if (shape.getGraphicsAlgorithm() instanceof Text) {
					Text text = (Text) shape.getGraphicsAlgorithm();
					if(PropertyUtil.isShape_IdValue(text, SHAPE_ID_NAME)) {
						return text.getValue();
					}
		} 	}	}
		return null;
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
					if(innerContainerShape.getGraphicsAlgorithm() instanceof Rectangle) {
						Rectangle rectangle = (Rectangle) innerContainerShape.getGraphicsAlgorithm();
						if(PropertyUtil.isShape_IdValue(rectangle, SHAPE_ID_ATTRIBUTECONTAINER)) {
							for(Shape attributeShape : innerContainerShape.getChildren()) {
								Text text = (Text) attributeShape.getGraphicsAlgorithm();
								pictogrammAttributeNames.add(text.getValue());
		}	}	}	}	}	}
		return pictogrammAttributeNames;
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
					if(innerContainerShape.getGraphicsAlgorithm() instanceof Rectangle) {
						Rectangle rectangle = (Rectangle) innerContainerShape.getGraphicsAlgorithm();
						if(PropertyUtil.isShape_IdValue(rectangle, SHAPE_ID_OPERATIONCONTAINER)) {
									for(Shape operationShape : innerContainerShape.getChildren()) {
										Text text = (Text) operationShape.getGraphicsAlgorithm();
										pictogramOperationNames.add(text.getValue());
		}	}	}	}	}	}
		return pictogramOperationNames;
	}
	
	/**
	 * This operation gets the name of a business object that is an {@link org.framed.iorm.model.Shape}
	 * @param businessObject the business object to get the name of
	 * @return the name of the business object if it is an {@link org.framed.iorm.model.Shape} and return null else
	 */
	public static String getBusinessObjectName(Object businessObject) {
		if (businessObject instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
			return shape.getName();
		}
		return null;
	}
}
