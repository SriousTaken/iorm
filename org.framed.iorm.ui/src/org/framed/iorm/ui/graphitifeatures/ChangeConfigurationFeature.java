package org.framed.iorm.ui.graphitifeatures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.framed.iorm.featuremodel.FRaMEDFeature;
import org.framed.iorm.featuremodel.FeatureName;
import org.framed.iorm.featuremodel.FeaturemodelFactory;
import org.framed.iorm.model.Model;
import org.framed.iorm.ui.contexts.ChangeConfigurationContext;
import org.framed.iorm.ui.exceptions.FeatureModelInconsistentException;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.util.MethodUtil;

public class ChangeConfigurationFeature extends AbstractCustomFeature  {
	
	//name literals
	private String CHANGECONFIGURATION_FEATURE_NAME = NameLiterals.CHANGECONFIGURATION_FEATURE_NAME; 
	
	boolean hasDoneChanges = false;
	
	public ChangeConfigurationFeature(IFeatureProvider featureProvider) {
		super(featureProvider);
	}
	 
	@Override
	public String getName() {
		return CHANGECONFIGURATION_FEATURE_NAME;
	}
	 
	@Override
	public boolean canExecute(ICustomContext context) {
		ChangeConfigurationContext cfmc = (ChangeConfigurationContext) context;
		return (cfmc.getTreeItem() != null &&
				MethodUtil.getDiagramRootModel(getDiagram()) != null);
	}
	 
	@Override
	public void execute(ICustomContext context) throws FeatureModelInconsistentException {
		List<FRaMEDFeature> featuresFound = new ArrayList<FRaMEDFeature>();
		ChangeConfigurationContext cfmc = (ChangeConfigurationContext) context;
		
		Model rootModel = MethodUtil.getDiagramRootModel(getDiagram());
		EList<FRaMEDFeature> ConfigurationFeatures = rootModel.getFramedConfiguration().getFeatures();
		if(!(cfmc.getTreeItem().getChecked())) {
			for(FRaMEDFeature feature : ConfigurationFeatures) {
				if(feature.getName().equals(FeatureName.getByName(cfmc.getTreeItem().getText()))) {
					featuresFound.add(feature);
			}	}
			if(featuresFound.size() == 0) throw new FeatureModelInconsistentException();
			for(FRaMEDFeature feature : featuresFound) {
				ConfigurationFeatures.remove(feature);
			}
		}
		if(cfmc.getTreeItem().getChecked()) {
			for(FRaMEDFeature feature : ConfigurationFeatures) {
				if(feature.getName().equals(FeatureName.getByName(cfmc.getTreeItem().getText()))) {
						featuresFound.add(feature);
			}	}
			if(featuresFound.size() != 0) throw new FeatureModelInconsistentException();
			FRaMEDFeature framedFeature = FeaturemodelFactory.eINSTANCE.createFRaMEDFeature();
			framedFeature.setName(FeatureName.getByName(cfmc.getTreeItem().getText()));
			framedFeature.setManuallySelected(true);
			ConfigurationFeatures.add(framedFeature);		
		}
		//set list of selected features in the editor
		cfmc.getBehaviorEditor().setSelectedFeatures(ConfigurationFeatures);
	}
	 
	@Override
	public boolean hasDoneChanges() {
		return hasDoneChanges; 	
	}
}
