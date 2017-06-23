package org.framed.iorm.ui.subeditors;

import org.eclipse.ui.editors.text.TextEditor;

public class FRaMEDTextViewer extends TextEditor {
	
	public FRaMEDTextViewer() {
		super();
	}
	
	@Override
	public boolean isEditable() {
	    return false;
	}

	@Override
	public boolean isEditorInputModifiable() {
	    return false;
	}

	@Override
	public boolean isEditorInputReadOnly() {
	    return true;
	}

	@Override
	public boolean isDirty() {
	    return false;
	}
}
