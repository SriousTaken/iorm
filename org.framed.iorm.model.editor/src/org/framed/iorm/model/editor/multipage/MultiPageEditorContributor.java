package org.framed.iorm.model.editor.multipage;

import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.framed.iorm.model.editor.literals.IdentifierLiterals;
import org.framed.iorm.model.editor.subeditors.DiagramEditorWithID;
import org.framed.iorm.model.editor.subeditors.TextViewerWithID;

public class MultiPageEditorContributor extends MultiPageEditorActionBarContributor {
	
	//id literals
	public final String PAGE_ID_BEHAVIOR = IdentifierLiterals.PAGE_ID_BEHAVIOR,
 			  			PAGE_ID_DATA = IdentifierLiterals.PAGE_ID_DATA,
 			  			PAGE_ID_IORM_TEXT = IdentifierLiterals.PAGE_ID_IORM_TEXT,
 			  			PAGE_ID_CROM_TEXT = IdentifierLiterals.PAGE_ID_CROM_TEXT,
 			  			PAGE_ID_FEATURE = IdentifierLiterals.PAGE_ID_FEATURE;
	
	private IEditorPart activeEditorPart;
	
	public MultiPageEditorContributor() {
		super();
	}
	
	protected IAction getAction(ITextEditor editor, String actionID) {
		if(editor == null) return null;
		return editor.getAction(actionID);
	}
	

	public void setActivePage(IEditorPart part) {
		if (activeEditorPart == part) return;
		activeEditorPart = part;

		IActionBars actionBars = getActionBars();
		if (actionBars != null) {

			//page is DiagramEditor
			if((part instanceof DiagramEditorWithID)) {
				DiagramEditorWithID editor = (DiagramEditorWithID) part;
				if(editor.getId()==PAGE_ID_BEHAVIOR || editor.getId()==PAGE_ID_DATA) {
					String undoActionID = new UndoAction(editor).getId(),
						   redoActionID = new RedoAction(editor).getId(),
						   deleteActionID = new DeleteAction((IWorkbenchPart) editor).getId();
				
					actionBars.setGlobalActionHandler( ActionFactory.UNDO.getId(), editor.getActionRegistry().getAction(undoActionID));
					actionBars.setGlobalActionHandler( ActionFactory.REDO.getId(), editor.getActionRegistry().getAction(redoActionID));
					actionBars.setGlobalActionHandler( ActionFactory.DELETE.getId(), editor.getActionRegistry().getAction(deleteActionID));
					
					//TODO copy & paste
					//actionBars.setGlobalActionHandler( ActionFactory.COPY.getId(), getAction(editor, ITextEditorActionConstants.COPY));
					//actionBars.setGlobalActionHandler( ActionFactory.PASTE.getId(), getAction(editor, ITextEditorActionConstants.PASTE));
				}	
			}
			//page is TextViewer		
			if((part instanceof TextViewerWithID)) {
				TextViewerWithID editor = (TextViewerWithID) part;
				if(editor.getId()==PAGE_ID_IORM_TEXT || editor.getId()==PAGE_ID_CROM_TEXT) {
					actionBars.setGlobalActionHandler( ActionFactory.COPY.getId(), getAction(editor, ITextEditorActionConstants.COPY));
					actionBars.setGlobalActionHandler( ActionFactory.SELECT_ALL.getId(), getAction(editor, ITextEditorActionConstants.SELECT_ALL));
					actionBars.setGlobalActionHandler( ActionFactory.FIND.getId(), getAction(editor, ITextEditorActionConstants.FIND));
					actionBars.updateActionBars();
				}
			}
		}
	}
}