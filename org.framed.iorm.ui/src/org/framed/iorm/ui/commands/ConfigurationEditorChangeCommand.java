package org.framed.iorm.ui.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.swt.widgets.TreeItem;
import org.framed.iorm.ui.contexts.ChangeConfigurationContext;
import org.framed.iorm.ui.exceptions.FeatureModelInconsistentException;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.subeditors.DiagramEditorWithID;
import org.framed.iorm.ui.subeditors.FeatureEditorWithID;

public class ConfigurationEditorChangeCommand extends Command {

	//name literals
	private final String CHANGECONFIGURATION_FEATURE_NAME = NameLiterals.CHANGECONFIGURATION_FEATURE_NAME,
				   CONFIGURATION_CHANGE_COMMAND_NAME = NameLiterals.CONFIGURATION_CHANGE_COMMAND_NAME;
	
	//feature editor that uses the command
	private FeatureEditorWithID featureEditor;
	
	//diagram editors that used by the command
	private DiagramEditorWithID behaviorDiagramEditor,
								dataDiagramEditor,
								lastUsedDiagramEditor;
	
	//new value of feature selection
	private boolean select;
	
	//tree item that presents the feature 
	private TreeItem item;

	public ConfigurationEditorChangeCommand() {
		super.setLabel(CONFIGURATION_CHANGE_COMMAND_NAME);
	}

	@Override
	public void execute() {	 
		featureEditor.setSelection(item, select);
		ICustomFeature changeConfigurationFeature = null;
		ICustomFeature[] customFeatures = behaviorDiagramEditor.getDiagramTypeProvider().getFeatureProvider().getCustomFeatures(new ChangeConfigurationContext());
		for(int i = 0; i<customFeatures.length; i++) {
			if(customFeatures[i].getName().equals(CHANGECONFIGURATION_FEATURE_NAME)) 
				changeConfigurationFeature = customFeatures[i];
		}
		if(changeConfigurationFeature != null) {
			ChangeConfigurationContext changeConfigurationContext = new ChangeConfigurationContext();
			changeConfigurationContext.setTreeItem(item);
			if(changeConfigurationFeature.canExecute(changeConfigurationContext)) {
				try {
					changeConfigurationFeature.execute(changeConfigurationContext);
				} catch(FeatureModelInconsistentException e) { e.printStackTrace(); }
			}	
		}
	}
	
	@Override
	public boolean canUndo() {
		return true;
	}
	
	@Override
	public void undo() {
		System.out.println("asd");
		//do standart undo
		String undoActionID = new UndoAction(behaviorDiagramEditor).getId();
		behaviorDiagramEditor.getActionRegistry().getAction(undoActionID).run();
		//update check status of item in feature editor additionally
		item.setChecked(!(item.getChecked())); this.
		featureEditor.updateItemCheckedStatusOnRedo(item);
	}
		
	public void setFeatureEditor(FeatureEditorWithID featureEditor) {
		this.featureEditor = featureEditor;
	}
	  
	public void setBehaviorDiagramEditor(DiagramEditorWithID behaviorDiagramEditor) {
		this.behaviorDiagramEditor = behaviorDiagramEditor;
	}
	
	public void setDiagramEditor(DiagramEditorWithID dataDiagramEditor) {
		this.dataDiagramEditor = dataDiagramEditor;
	}
	
	public void setLastUsedDiagramEditor(DiagramEditorWithID lastUsedDiagramEditor) {
		this.lastUsedDiagramEditor = lastUsedDiagramEditor;
	}
	
	public void setItem(TreeItem item) {
		this.item = item;
	}
	  
	public void setSelect(boolean select) {
		this.select = select;
	}
}	  
