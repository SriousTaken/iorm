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

import de.ovgu.featureide.fm.core.base.IConstraint;

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
	
	private class ParsedRule {
		public ParsedRule(String firstFeature, String secondFeature, String ruleSign) {
			this.firstFeature = firstFeature;
			this.secondFeature = secondFeature;
			this.ruleSign = ruleSign;
		}
		public String firstFeature,
					  secondFeature,
					  ruleSign;
	}

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
	 * Step 1: Its gets the root model of the diagram.<br>
	 * Step 2a: It gets the by the user selected feature from the diagrams configuration.<br>
	 * Step 3a: It uses the operation {@link resolveConstraintsDelete} get other features that needs
	 * 		   to be deleted too, since there is a feature model constraint resolve. Delete all
	 * 		   found features for the diagrams configuration.<br>
	 * Step 2b: Its check if the feature to add is already existing in the configuration.<br>
	 * Step 3b: Its uses the operation {@link resolveConstraintsAdd} to get other feature names of
	 * 			features to add to configuration. Add features with all found feature names.
	 * Step 4: updates the public list of the features of the role model in the {@link FRaMEDDiagramEditor}
	 * @throws ConfigurationInconsistentException
	 */
	@Override
	public void execute(ICustomContext context) throws ConfigurationInconsistentException {
		List<FRaMEDFeature> featuresFound = new ArrayList<FRaMEDFeature>();
		ChangeConfigurationContext cfmc = (ChangeConfigurationContext) context;
		
		//Step 1
		Model rootModel = MethodUtil.getDiagramRootModel(getDiagram());
		EList<FRaMEDFeature> ConfigurationFeatures = rootModel.getFramedConfiguration().getFeatures();
		//Step 2a
		if(!(cfmc.getTreeItem().getChecked())) {
			for(FRaMEDFeature feature : ConfigurationFeatures) {
				if(feature.getName().equals(FeatureName.getByName(cfmc.getTreeItem().getText()))) {
					featuresFound.add(feature);
			}	}
			if(featuresFound.size() == 0) throw new ConfigurationInconsistentException();
			//Step 3a
			featuresFound.addAll( resolveConstraintsDelete(cfmc, ConfigurationFeatures) );
			for(FRaMEDFeature feature : featuresFound) {
				ConfigurationFeatures.remove(feature);
		}	}
		//Step 2b
		if(cfmc.getTreeItem().getChecked()) {
			for(FRaMEDFeature feature : ConfigurationFeatures) {
				if(feature.getName().equals(FeatureName.getByName(cfmc.getTreeItem().getText()))) {
						featuresFound.add(feature);
			}	}
			if(featuresFound.size() != 0) throw new ConfigurationInconsistentException();
			//Step 3b
			List<String> featuresToAdd = resolveConstraintsAdd(cfmc);
			featuresToAdd.add(cfmc.getTreeItem().getText());
			for(String featureName : featuresToAdd) {
				FRaMEDFeature framedFeature = FeaturemodelFactory.eINSTANCE.createFRaMEDFeature();
				framedFeature.setName(FeatureName.getByName(featureName));
				if(getFeatureFromEListByName(cfmc.getStandardFeatures(), featureName).isManuallySelected())
					framedFeature.setManuallySelected(true);
				ConfigurationFeatures.add(framedFeature);		
		}	}
		//Step 4
		cfmc.getBehaviorEditor().setSelectedFeatures(ConfigurationFeatures);
	}
	
	private FRaMEDFeature getFeatureFromEListByName(EList<FRaMEDFeature> featureList , String name) {
		for(FRaMEDFeature feature : featureList) {
			if(name.equals(feature.getName().getLiteral().toString()))
				return feature;
		}
		return null;
	}
	
	private List<FRaMEDFeature> resolveConstraintsDelete(ChangeConfigurationContext cfmc, EList<FRaMEDFeature> ConfigurationFeatures) {
		String equivalence = "<=>",
			   implication = "=>";
		List<FRaMEDFeature> additionalFeaturesToDelete = new ArrayList<FRaMEDFeature>();
		for(IConstraint constraint : cfmc.getFeatureModel().getConstraints()) {
			ParsedRule parsedRule = parseRule(constraint);
			if(parsedRule.ruleSign.equals(equivalence)) {
				if(parsedRule.firstFeature.equals(cfmc.getTreeItem().getText())) {
					for(FRaMEDFeature feature : ConfigurationFeatures) {
						if(parsedRule.secondFeature.equals(feature.getName().getLiteral().toString())) {
							additionalFeaturesToDelete.add(feature);
				}	}	}	
				if(parsedRule.secondFeature.equals(cfmc.getTreeItem().getText())) {
					for(FRaMEDFeature feature : ConfigurationFeatures) {
						if(parsedRule.firstFeature.equals(feature.getName().getLiteral().toString())) {
							additionalFeaturesToDelete.add(feature);
			}	}	}	}	
			if(parsedRule.ruleSign.equals(implication)) {
				if(parsedRule.firstFeature.equals(cfmc.getTreeItem().getText())) {
					for(FRaMEDFeature feature : ConfigurationFeatures) {
						if(parsedRule.secondFeature.equals(feature.getName().getLiteral().toString())
						   && !(feature.isManuallySelected())) {
							additionalFeaturesToDelete.add(feature);
				}	} 	}	
				if(parsedRule.secondFeature.equals(cfmc.getTreeItem().getText())) {
					for(FRaMEDFeature feature : ConfigurationFeatures) {
						if(parsedRule.firstFeature.equals(feature.getName().getLiteral().toString())) {
							additionalFeaturesToDelete.add(feature);
		}	}	}	}	}	
		return additionalFeaturesToDelete;
	}
	
	private List<String> resolveConstraintsAdd(ChangeConfigurationContext cfmc) {
		String equivalence = "<=>",
			   implication = "=>";
		List<String> additionalFeaturesToAdd = new ArrayList<String>();
		for(IConstraint constraint : cfmc.getFeatureModel().getConstraints()) {
			ParsedRule parsedRule = parseRule(constraint);
			if(parsedRule.ruleSign.equals(equivalence)) {
				if(parsedRule.firstFeature.equals(cfmc.getTreeItem().getText())) {
					additionalFeaturesToAdd.add(parsedRule.secondFeature);
				}	
				if(parsedRule.secondFeature.equals(cfmc.getTreeItem().getText())) {
					additionalFeaturesToAdd.add(parsedRule.firstFeature);
			}	}	
			if(parsedRule.ruleSign.equals(implication)) {
				if(parsedRule.firstFeature.equals(cfmc.getTreeItem().getText())) {
					additionalFeaturesToAdd.add(parsedRule.secondFeature);
		}	}	}
		return additionalFeaturesToAdd;
	}
	
	private ParsedRule parseRule(IConstraint constraint) {
		String ruleSign = constraint.getDisplayName().substring(
				constraint.getDisplayName().indexOf("  "),
				constraint.getDisplayName().indexOf("  ", constraint.getDisplayName().indexOf("  ")+1));
		ParsedRule parsedRule = new ParsedRule(constraint.getContainedFeatures().toArray()[0].toString(),
											   constraint.getContainedFeatures().toArray()[1].toString(),
											   ruleSign.trim());
		return parsedRule;		
	}
}
