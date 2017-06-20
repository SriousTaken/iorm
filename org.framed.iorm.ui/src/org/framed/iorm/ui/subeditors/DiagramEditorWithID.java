package org.framed.iorm.ui.subeditors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.ui.IEditorInput;

public class DiagramEditorWithID extends DiagramEditor  {

	//identifier for the editor
	private String id;
	
	//file of editor input
	IFile file; 
	
	//private File file;
			
	public DiagramEditorWithID(String id, IEditorInput editorInput) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	//make method getCommandStack public
	@Override
	public CommandStack getCommandStack() {
		return super.getCommandStack();
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
	}
}