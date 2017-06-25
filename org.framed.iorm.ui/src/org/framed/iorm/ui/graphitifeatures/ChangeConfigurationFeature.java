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
import org.framed.iorm.ui.exceptions.ConfigurationInconsistentException;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.util.MethodUtil;

/**
 * This graphiti custom feature is used change the role models configuration.
 * <p>
 * It is called by {@link org.framed.iorm.ui.commands.ConfigurationEditorChangeCommand} and uses the {@link ChangeConfigurationContext}.
 * @see org.framed.iorm.ui.commands.ConfigurationEditorChangeCommand
 * @see ChangeConfigurationContext
 * @author Kevin Kassin
 */
public class ChangeConfigurationFeature extends AbstractCustomFeature  {
	
	/**
	 * the name of the feature gathered from {@link NameLiterals}
	 */
	private String CHANGECONFIGURATION_FEATURE_NAME = NameLiterals.CHANGECONFIGURATION_FEATURE_NAME; 
	
	/**
	 * Class constructor
	 * @param featureProvider the feature provider the feature belogs to
	 */
	public ChangeConfigurationFeature(IFeatureProvider featureProvider) {
		super(featureProvider);
	}
	 
	/**
	 * get method for the features name
	 * @return the name of the feature
	 */
	@Override
	public String getName() {
		return CHANGECONFIGURATION_FEATURE_NAME;
	}
	
	/**
	 * This methods checks if the feature can be executed.
	 * <p>
	 * It return true if<br>
	 * (1) all needed informations in the context are set and<br>
	 * (2) the diagram to work has a root model
	 * @return if feature can be executed
	 */
	@Override
	public boolean canExecute(ICustomContext context) {
		ChangeConfigurationContext cfmc = (ChangeConfigurationContext) context;
		return (cfmc.getTreeItem() != null &&
				cfmc.getBehaviorEditor() != null &&
				MethodUtil.getDiagramRootModel(getDiagram()) != null);
	}
	 
	/**
	 * This method changes the role models configuration using the following steps:<br>
	 * Step 1: gets the root model of the diagram<br>
	 * Step 2: deletes or add a feature in the root model<br>
	 * Step 3: updates the public list of the features of the role model in the {@link FRaMEDDiagramEditor}
	 * @throws ConfigurationInconsistentException
	 */
	@Override
	public void execute(ICustomContext context) throws ConfigurationInconsistentException {
		List<FRaMEDFeature> featuresFound = new ArrayList<FRaMEDFeature>();
		ChangeConfigurationContext cfmc = (ChangeConfigurationContext) context;
		
		//Step 1
		Model rootModel = MethodUtil.getDiagramRootModel(getDiagram());
		EList<FRaMEDFeature> ConfigurationFeatures = rootModel.getFramedConfiguration().getFeatures();
		//Step 2
		if(!(cfmc.getTreeItem().getChecked())) {
			for(FRaMEDFeature feature : ConfigurationFeatures) {
				if(feature.getName().equals(FeatureName.getByName(cfmc.getTreeItem().getText()))) {
					featuresFound.add(feature);
			}	}
			if(featuresFound.size() == 0) throw new ConfigurationInconsistentException();
			for(FRaMEDFeature feature : featuresFound) {
				ConfigurationFeatures.remove(feature);
		}	}
		if(cfmc.getTreeItem().getChecked()) {
			for(FRaMEDFeature feature : ConfigurationFeatures) {
				if(feature.getName().equals(FeatureName.getByName(cfmc.getTreeItem().getText()))) {
						featuresFound.add(feature);
			}	}
			if(featuresFound.size() != 0) throw new ConfigurationInconsistentException();
			FRaMEDFeature framedFeature = FeaturemodelFactory.eINSTANCE.createFRaMEDFeature();
			framedFeature.setName(FeatureName.getByName(cfmc.getTreeItem().getText()));
			framedFeature.setManuallySelected(true);
			ConfigurationFeatures.add(framedFeature);		
		}
		//Step 3
		cfmc.getBehaviorEditor().setSelectedFeatures(ConfigurationFeatures);
	}
}
