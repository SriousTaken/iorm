package org.framed.iorm.model.editor.subeditors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.ui.IEditorInput;
import org.framed.iorm.model.editor.multipage.MultipageEditor;

public class DiagramEditorWithID extends DiagramEditor  {

	//identifier for the editor
	private String id;
			
	public DiagramEditorWithID(String id, MultipageEditor multipageEditor, IEditorInput editorInput) {
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
		System.out.println(id);	
		super.doSave(monitor);
	}
}
