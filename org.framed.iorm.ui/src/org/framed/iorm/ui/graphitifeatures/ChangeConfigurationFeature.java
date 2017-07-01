package org.framed.iorm.ui.graphitifeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.framed.iorm.featuremodel.FRaMEDConfiguration;
import org.framed.iorm.featuremodel.FRaMEDFeature;
import org.framed.iorm.featuremodel.FeatureName;
import org.framed.iorm.featuremodel.FeaturemodelFactory;
import org.framed.iorm.ui.contexts.ChangeConfigurationContext;
import org.framed.iorm.ui.exceptions.ConfigurationInconsistentException;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.util.GeneralUtil;

import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.configuration.Configuration;

/**
 * This graphiti custom feature is used to change the role models configuration.
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
	 * return true if<p>
	 * (1) all needed context informations are set and<br>
	 * (2) the root model of the diagram to work is not null
	 * @return if the feature can be executed
	 */
	@Override
	public boolean canExecute(ICustomContext context) {
		ChangeConfigurationContext cfmc = (ChangeConfigurationContext) context;
		return (cfmc.getBehaviorEditor() != null &&
				cfmc.getConfiguration() != null &&
				GeneralUtil.getDiagramRootModel(getDiagram()) != null);
	}
	 
	/**
	 * This method changes the role models configuration using the following steps:<br>
	 * TODO
	 * @throws ConfigurationInconsistentException
	 */
	@Override
	public void execute(ICustomContext context) throws ConfigurationInconsistentException {
		ChangeConfigurationContext cfmc = (ChangeConfigurationContext) context;
		FRaMEDConfiguration framedFeatureConfiguration = FeaturemodelFactory.eINSTANCE.createFRaMEDConfiguration();
		Configuration editorFeatureConfiguration = cfmc.getConfiguration();
		for(IFeature editorFeature : editorFeatureConfiguration.getSelectedFeatures()) {
			FRaMEDFeature framedFeature = FeaturemodelFactory.eINSTANCE.createFRaMEDFeature();
			framedFeature.setName(FeatureName.getByName(editorFeature.getName()));
			if(editorFeatureConfiguration.getManualFeatures().contains(editorFeature))
				framedFeature.setManuallySelected(true);
			framedFeatureConfiguration.getFeatures().add(framedFeature);	
		}
		GeneralUtil.getDiagramRootModel(getDiagram()).setFramedConfiguration(framedFeatureConfiguration);
	}
}
