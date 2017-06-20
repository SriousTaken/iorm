package org.framed.iorm.ui.subeditors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.ui.IEditorInput;
import org.framed.iorm.featuremodel.FRaMEDFeature;

public class DiagramEditorWithID extends DiagramEditor  {

	//identifier for the editor
	private String id;
	
	//public list of selected features, refreshed each time the features are changed
	private EList<FRaMEDFeature> selectedFeatures = null;
			
	public DiagramEditorWithID(String id, IEditorInput editorInput) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setSelectedFeatures(EList<FRaMEDFeature> selectedFeatures) {
		this.selectedFeatures = selectedFeatures;
	}
	
	public EList<FRaMEDFeature> getSelectedFeatures() {
		return selectedFeatures;
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