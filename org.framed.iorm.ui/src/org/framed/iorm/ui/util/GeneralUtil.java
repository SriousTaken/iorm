package org.framed.iorm.ui.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.editor.IDiagramEditorInput;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.exceptions.NoDiagramFoundException;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.LayoutLiterals;
import org.framed.iorm.ui.multipage.MultipageEditor;
import org.framed.iorm.ui.wizards.RoleModelWizard;

public class GeneralUtil {
	
	/**
	 * the identifiers for graphics algorithms of group pictograms needed for the operation 
	 * {@link #getGroupDiagramFromGroupShape} gathered from {@link IdentifierLiterals}
	 */
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
	 * @return the root model of the given diagram if there is exactly one model linked and returns null else
	 */
	public static final Model getRootModelForDiagram(Diagram diagram) {
		if(diagram.getLink() != null) {
			if(diagram.getLink().getBusinessObjects().size() == 1 &&
			   diagram.getLink().getBusinessObjects().get(0) instanceof Model) {
				return (Model) diagram.getLink().getBusinessObjects().get(0);
			}
		}
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
	 * This operation fetches the groups diagram for a shape that is a part of a groups pictogram 
	 * representation using the following steps:
	 * <p>
	 * Step 1: If the given shape has no graphics Algorithm it returns null.<br>
	 * Step 2: It gets the name of the group depending on the given shape.<br>
	 * Step 3: It searches in the list of children of the container diagram for a diagram with the name
	 * 		   found in step 2. If no such diagram can be found, throw a {@link NoDiagramFoundException}
	 * <p>
	 * If its not clear what the different shapes are look for the pictogram structure of a group here: 
	 * {@link org.framed.iorm.ui.pattern.shapes.GroupPattern#add}.<br>
	 * If its not clear what <em>container diagram</em> means, see {@link RoleModelWizard#createEmfFileForDiagram} for reference.
	 * @param groupShape the shape to start the search for the groups diagram 
	 * @return the groups diagram, if the given shape was a name shape or the type body shape of a group
	 * @throws NoDiagramFoundException
	 */
	public static Diagram getGroupDiagramForGroupShape(Shape groupShape, Diagram diagram) {
		//Step 1
		if(groupShape.getGraphicsAlgorithm() == null) return null;
		else {
			//Step 2
			String groupName = null;
			if(PropertyUtil.isShape_IdValue(groupShape.getGraphicsAlgorithm(), SHAPE_ID_GROUP_TYPEBODY)) {
				Shape groupNameShape = ((ContainerShape) groupShape).getChildren().get(0);
				if(PropertyUtil.isShape_IdValue(groupNameShape.getGraphicsAlgorithm(), SHAPE_ID_GROUP_NAME))
					groupName = ((Text) groupNameShape.getGraphicsAlgorithm()).getValue();
			}	
			if(PropertyUtil.isShape_IdValue(groupShape.getGraphicsAlgorithm(), SHAPE_ID_GROUP_NAME))
				groupName = ((Text) groupShape.getGraphicsAlgorithm()).getValue();	
		   //Step 3
			Diagram containerDiagram = getContainerDiagramForAnyDiagram(diagram);
			if(containerDiagram == null) throw new NoDiagramFoundException();
			for(Shape shape : containerDiagram.getChildren()) {
				if(shape instanceof Diagram) {
					if(((Diagram) shape).getName().equals(groupName))
						return ((Diagram) shape);
				}	
		}	}
		throw new NoDiagramFoundException();	
	}
			
	/**
	 * fetches the diagram in which a given shape in contained
	 * @param shape the shape to get containing diagram for
	 * @return the diagram that contains the given shape
	 */
	public static Diagram getDiagramForContainedShape(Shape shape) {
		if(shape.getContainer() == null) return null;
		if(shape.getContainer() instanceof Diagram) {	 
			return (Diagram) shape.getContainer();
		}
		if(shape.getContainer() instanceof Shape &&
		   !(shape.getContainer() instanceof Diagram)) {
			return getDiagramForContainedShape(shape.getContainer());
		}
		return null;
	}
	
	/**
	 * uses an recursive algorithm to find the <em>container diagram</em> of a role model
	 * <p>
	 * If its not clear what <em>container diagram</em> means, see {@link RoleModelWizard#createEmfFileForDiagram} for reference.
	 * @param diagram the diagram to search the container diagram from
	 * @return the container diagram of a role model
	 */
	private static Diagram getContainerDiagramForAnyDiagram(Diagram diagram) {
		if(diagram.getContainer() == null) return diagram;
		else {
			if(diagram.getContainer() instanceof Diagram)
				return getContainerDiagramForAnyDiagram((Diagram) diagram.getContainer());
			else return null;
		}	
	}
	
	/**
	 * returns the diagram for a resource fetched from a {@link DiagramEditorInput}
	 * @param resource the resource to get the diagram from
	 * @return the fetched diagram
	 * @throws NoDiagramFoundException
	 */
	public static Diagram getDiagramForResourceOfDiagramEditorInput(Resource resource) {
		Diagram diagram = null;
		if(resource.getEObject(resource.getURI().fragment()) instanceof Diagram) {
			diagram = (Diagram) resource.getEObject(resource.getURI().fragment());
			return diagram;
		}	
		throw new NoDiagramFoundException();
	}
	
	/**
	 * fetches the <em>main diagram</em> for a given {@link IEditorInput}.
	 * <p>
	 * If its not clear what <em>main diagram</em> means, see {@link RoleModelWizard#createEmfFileForDiagram} for reference.
	 * @param editorInput the editor input to get the <em>main diagram</em> for
	 * @return the <em>main diagram</em> for an {@link IEditorInput}
	 * @throws NoDiagramFoundException If no diagram can be fetched
	 * @see RoleModelWizard#createEmfFileForDiagram
	 */
	public static Diagram getMainDiagramForIEditorInput(IEditorInput editorInput) {
		Resource resource = GeneralUtil.getResourceFromEditorInput(editorInput);
		if(resource.getContents().get(0) instanceof Diagram) {
			Diagram containerDiagram = (Diagram) resource.getContents().get(0);
			if(containerDiagram.getChildren().get(0) instanceof Diagram) {
				return (Diagram) containerDiagram.getChildren().get(0);
			}	}	
		throw new NoDiagramFoundException();
	}
	
	/**
	 * generates an {@link IFileEditorInput} for a given resource
	 * @param resource the resource to create the editor input for
	 * @return the generated editor input
	 */
	public static IFileEditorInput getIFileEditorInputForResource(Resource resource) {
		IPath path = new Path(resource.getURI().toFileString());
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
		return new FileEditorInput(file);
	}
	
	/**
	 * searches for a diagram with the given name in the given resource
	 * <p>
	 * @param resource the resource to search the diagram in
	 * @param diagramName the name to search the diagram with
	 * @return the diagram with the specific name in the resource
	 * @throws NoDiagramFoundException
	 */
	public static Diagram getDiagramFromResourceByName(Resource resource, String diagramName) {
		if(resource.getContents().get(0) instanceof Diagram) {
			if(((Diagram) resource.getContents().get(0)).getContainer() == null) {
				Diagram containerDiagram = (Diagram) resource.getContents().get(0);
				for(Shape shape : containerDiagram.getChildren()) {
					if(shape instanceof Diagram) {
						Diagram diagram = (Diagram) shape;
						if(diagram.getName().equals(diagramName)) 
							return diagram;
		}	}	}	}
		throw new NoDiagramFoundException();
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
	public static String getNameOfBusinessObject(Object businessObject) {
		if (businessObject instanceof org.framed.iorm.model.Shape) {
			org.framed.iorm.model.Shape shape = (org.framed.iorm.model.Shape) businessObject;
			return shape.getName();
		}
		return null;
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
					if(PropertyUtil.isShape_IdValue(text, SHAPE_ID_NAME)) {
						return text.getValue();
					}
		} 	}	}
		return null;
	}
	
	/**
	 * manages to close a given multipage editor at the next reasonable opportunity usind the operation 
	 * {@link Display#asyncExec}
	 * <p>
	 * It also saves the multipage editor before closing it to clean up the dirty state of the whole workbench.
	 * @param multipageEditorToClose
	 */
	public static void closeMultipageEditorWhenPossible(MultipageEditor multipageEditorToClose) {
		Display display = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				multipageEditorToClose.getDiagramEditor().doSave(new NullProgressMonitor());
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(multipageEditorToClose, false);
			}
		});
	}
}
