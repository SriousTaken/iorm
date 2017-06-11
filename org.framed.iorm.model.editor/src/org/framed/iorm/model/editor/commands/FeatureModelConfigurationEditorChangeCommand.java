package org.framed.iorm.model.editor.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.TreeItem;
import org.framed.iorm.model.editor.subeditors.FeatureEditorWithID;

public class FeatureModelConfigurationEditorChangeCommand extends Command {

	//feature editor that uses the command
	private FeatureEditorWithID featureEditor;
	
	//new value of feature selection
	private boolean select;
	
	//tree item that presents the feature 
	private TreeItem item;

	public FeatureModelConfigurationEditorChangeCommand() {
		super.setLabel("Configuration Change");
	}

	@Override
	public void execute() {	  
		featureEditor.setSelection(item, select);
	}

	@Override
	public void undo() {	  
		featureEditor.setSelection(item, !select);
	}

	public void setEditor(FeatureEditorWithID editor) {
		this.featureEditor = editor;
	}
	  
	public void setItem(TreeItem item) {
		this.item = item;
	}
	  
	public void setSelect(boolean select) {
		this.select = select;
	}
}	  
