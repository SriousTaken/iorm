package org.framed.iorm.ui.actions;

import java.text.MessageFormat;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.framed.iorm.ui.literals.NameLiterals;

public class UndoActionForRoleModel extends UndoAction {

	//name literals
	private String CONFIGURATION_CHANGE_COMMAND_NAME = NameLiterals.CONFIGURATION_CHANGE_COMMAND_NAME;
	
	public UndoActionForRoleModel(IEditorPart editor) {
		super(editor);
	}
	
	public UndoActionForRoleModel(IWorkbenchPart part) {
		super(part);
	}
		
	//call specificly ...getUndoCommand().undo() and not the standart undo function of the diagram editors
	public void run() {
		if(getCommandStack().getUndoCommand().getLabel().equals(CONFIGURATION_CHANGE_COMMAND_NAME)) 
			
			
			
			getCommandStack().getUndoCommand().undo();
		else getCommandStack().undo();
	}
}
