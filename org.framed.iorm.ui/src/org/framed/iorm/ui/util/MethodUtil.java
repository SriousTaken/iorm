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
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.literals.LayoutLiterals;
import org.framed.iorm.ui.literals.URLLiterals;

public class MethodUtil {
	
	private static final int HEIGHT_NAME_SHAPE = LayoutLiterals.HEIGHT_NAME_SHAPE,
			 		  DATATYPE_CORNER_SIZE = LayoutLiterals.DATATYPE_CORNER_SIZE;
	
	private static final IPath PATH_TO_EMPTY_TEXTFILE = URLLiterals.PATH_TO_EMPTY_TEXTFILE;
	
	//TODO:  wo called?
	public static final int calculateHorizontalCenter(Type type, int heightOfClassOrRole) {
		if(type == Type.NATURAL_TYPE) 
			return ((heightOfClassOrRole-HEIGHT_NAME_SHAPE)/2)+HEIGHT_NAME_SHAPE;
		if(type == Type.DATA_TYPE)	
			return ((heightOfClassOrRole-HEIGHT_NAME_SHAPE-DATATYPE_CORNER_SIZE)/2)+HEIGHT_NAME_SHAPE;
		return 0;
	}
	
	//TODO: wo called
	public static final Model getDiagramRootModel(Diagram diagram) {
		List<Model> models=  new ArrayList<Model>();
		for(EObject eObject : diagram.eResource().getContents()) {
			if(eObject instanceof Model) {
				models.add((Model) eObject);
		}	}
		if(models.size()==1) return models.get(0);
		return null;
	}
	
	public static final IFile getEmptyTextFileForDiagram(IEditorInput editorInput) throws IOException, URISyntaxException {
		//Step 1
		IFileEditorInput fileInput = (IFileEditorInput) editorInput;
    	IFile file = fileInput.getFile();
		String projectNameOfDiagram = file.getParent().getParent().getParent().getName();
		//Step 2		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot(); 
		IPath pathToEmptyTectFile = new Path(projectNameOfDiagram + PATH_TO_EMPTY_TEXTFILE);
		return root.getFile(pathToEmptyTectFile);		
	}
}
