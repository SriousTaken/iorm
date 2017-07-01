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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.editor.IDiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.LayoutLiterals;

public class GeneralUtil {
	
	//TODO
	private static final String SHAPE_ID_GROUP_TYPEBODY = IdentifierLiterals.SHAPE_ID_GROUP_TYPEBODY,
								SHAPE_ID_GROUP_NAME = IdentifierLiterals.SHAPE_ID_GROUP_NAME;
								
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
	 * This operation creates an {@link IFile} of an file in the role model project for a given {@link IEditorInput} using two steps:
	 * <p>
	 * Step 1: It gets the project the editor input is located in.<br>
	 * Step 2: The method searches for the file in the project the editor input is located in. With the found file
	 * 		   it creates an IFile.
	 * @param editorInput the editor input to get the IFile for
	 * @return the created IFile
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static final IFile getIFileForFile(IEditorInput editorInput, IPath PathToFile) throws IOException, URISyntaxException {
		//Step 1
		IFileEditorInput fileInput = (IFileEditorInput) editorInput;
    	IFile file = fileInput.getFile();
		String projectNameOfDiagram = file.getParent().getParent().getName();
		//Step 2		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot(); 
		IPath pathToEmptyTectFile = new Path(projectNameOfDiagram + PathToFile);
		return root.getFile(pathToEmptyTectFile);		
	}
	
	/**
	 * fetches the {@link Resource} for a given {@link IEditorInput}
	 * @param editorInput the editor input to get the resource for
	 * @return the resource if edtior input is of type {@link IFileEditorInput} and the resource and be loaded 
	 * and returns null else
	 */
	public static Resource getResourceFromEditorInput(IEditorInput editorInput) {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = null;
	 	if (editorInput instanceof IFileEditorInput) {
	    	IFileEditorInput fileInput = (IFileEditorInput) editorInput;
	    	IFile file = fileInput.getFile();
	    	resource = resourceSet.createResource(URI.createURI(file.getLocationURI().toString()));
	 	} 
	 	if(editorInput instanceof IDiagramEditorInput) {
	 		IDiagramEditorInput diagramInput = (IDiagramEditorInput) editorInput;
	 		resource = resourceSet.createResource(diagramInput.getUri());
	 	}	
	 	if(resource != null) {
	    	try {
	    		resource.load(null);
	    		return resource;
	    	} catch (IOException e) { e.printStackTrace(); }
	    }
	 	return null;
	}
	
	/**
	 * sets the values of a given {@link AddContext} using a given {@link CreateContext}
	 * <p>
	 * This operation only deals with add and create contexts for graphiti shapes since graphiti connections use
	 * a special type of create contexts.
	 * @param addContext the {@link AddContext} to set the values in
	 * @param createContext the {@link CreateContext} to get the values of
	 * @return the given {@link AddContext} with set values
	 */
	public static AddContext getAddContextForCreateShapeContext(AddContext addContext, ICreateContext createContext) {
		addContext.setHeight(createContext.getHeight());
		addContext.setWidth(createContext.getWidth());
		addContext.setX(createContext.getX());
		addContext.setY(createContext.getY());
		addContext.setLocation(createContext.getX(), createContext.getY());
		addContext.setSize(createContext.getWidth(), createContext.getHeight());
		return addContext;
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
	 * This operation fetches the groups diagram for a shape that is a part of a groups pictogram 
	 * representation using the following steps:
	 * <p>
	 * Step 1: If the given shape has no graphics Algorithm it returns null.<br>
	 * Step 2: It calculates the group container shape depending on the given shape.<br>
	 * Step 3: It searches for the diagram container in the list of children of the group container shape
	 * 		   and returns the found groups diagram.
	 * <p>
	 * If its not clear what the different shapes are look for the pictogram structure of a group here: 
	 * {@link org.framed.iorm.ui.pattern.shapes.GroupPattern#add}.
	 * @param shapeToStartFrom the shape to start the search for the groups diagram 
	 * @return the groups diagram, if the given shape was a name shape or the type body shape of a group
	 */
	public static Diagram getGroupDiagramFromGroupShape(Shape shapeToStartFrom) {
		//Step 1
		if(shapeToStartFrom.getGraphicsAlgorithm() == null) return null;
		else {
			//Step 2
			ContainerShape groupContainerShape = null;
			if(PropertyUtil.isShape_IdValue(shapeToStartFrom.getGraphicsAlgorithm(), SHAPE_ID_GROUP_TYPEBODY))
				groupContainerShape = shapeToStartFrom.getContainer();
			if(PropertyUtil.isShape_IdValue(shapeToStartFrom.getGraphicsAlgorithm(), SHAPE_ID_GROUP_NAME))
				groupContainerShape = shapeToStartFrom.getContainer().getContainer();
			if(groupContainerShape != null) {
			//Step 3
				for(Shape shape : groupContainerShape.getChildren()) {
					if(shape instanceof ContainerShape) { 
						ContainerShape containerShape = (ContainerShape) shape; 
						if(containerShape.getChildren().size() == 1 &&
						   containerShape.getChildren().get(0) instanceof Diagram) {
							Diagram diagram = (Diagram) containerShape.getChildren().get(0);
							return diagram;
		}	}	}	}	}
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
	 * @return the name of the business object if it is an {@link org.framed.iorm.model.Shape}
	 */
	public static String getBusinessObjectName(Object businessObject) {
		if (businessObject instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
			return shape.getName();
		}
		return null;
	}
}
