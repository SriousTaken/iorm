package org.framed.iorm.model.editor.subeditors;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

public class DiagramEditorWithID extends DiagramEditor  {

	//identifier for the editor
	private String id;
		
	public DiagramEditorWithID(String id) {
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
}
