package org.framed.iorm.ui.subeditors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.framed.iorm.featuremodel.FRaMEDFeature;

public class FRaMEDDiagramEditor extends DiagramEditor  {
	
	//public list of selected features, refreshed each time the features are changed
	private EList<FRaMEDFeature> selectedFeatures = null;
			
	public FRaMEDDiagramEditor() {
		super();
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