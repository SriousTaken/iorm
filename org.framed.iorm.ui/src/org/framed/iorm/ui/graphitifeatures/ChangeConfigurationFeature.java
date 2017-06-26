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
	
	/**
	 * the internal class to save the parsed informations of a feature model constraint
	 * @author Kevin Kassin
	 */
	private class ParsedRule {
		public ParsedRule(String leftFeature, String rightFeature, String ruleSign) {
			this.leftFeature = leftFeature;
			this.rightFeature = rightFeature;
			this.ruleSign = ruleSign;
		}
		public String leftFeature, rightFeature, ruleSign;
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
	 * 		    to be deleted too, since there is a feature model constraint resolve. Delete all
	 * 		    found features for the diagrams configuration.<br>
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
	
	/**
	 * Gets the first feature in a list of features with a specific name.
	 * @param featureList the list to search in
	 * @param name the name to search a feature with in the list
	 * @return the first feature in the list that has the given name
	 */
	private FRaMEDFeature getFeatureFromEListByName(EList<FRaMEDFeature> featureList , String name) {
		for(FRaMEDFeature feature : featureList) {
			if(name.equals(feature.getName().getLiteral().toString()))
				return feature;
		}
		return null;
	}
	
	/**
	 * calculates features to delete from the configuration according to the feature model constraints
	 * <p>
	 * If the user unchecks a feature there can be more features to be deleted than the on the user chosed to be removed.
	 * The other features to be deleted can be found by analyzing the feature model constraints. A feature model constraint
	 * looks like this: <em>leftFeature ruleSign rightFeature<em>. This operation uses the following steps
	 * to do that for every constraint:<br>
	 * Step 1: It parses the constraint using the method {@link #parseRule}.<br>
	 * Step 2: If the rule sign of the parsed rule is an equivalence, check if the user chosen feature to delete
	 * 		    is part of that rule. If yes, add the other feature in this rule to the features to delete.<br>
	 * Step 3: If the rule sign is an implication, check if the user chosen feature to delete is part of that rule.
	 * Step 4: If the user chosen feature is the left feature of the constraint, add the right feature to list of features to  
	 * 		   delete, only if if the feature is not chosen manually.<br>
	 * Step 5: If the user chosen feature is the right feature of the constraint, add the left feature to the list of features to
	 * 		   delete.
	 * @param cfmc the context of this custom feature
	 * @param ConfigurationFeatures the feature configuration of the edited diagram
	 * @return a list of features to delete
	 */
	private List<FRaMEDFeature> resolveConstraintsDelete(ChangeConfigurationContext cfmc, EList<FRaMEDFeature> ConfigurationFeatures) {
		String equivalence = "<=>",
			   implication = "=>";
		List<FRaMEDFeature> additionalFeaturesToDelete = new ArrayList<FRaMEDFeature>();
		for(IConstraint constraint : cfmc.getFeatureModel().getConstraints()) {
			//Step 1
			ParsedRule parsedRule = parseRule(constraint);
			//Step 2
			if(parsedRule.ruleSign.equals(equivalence)) {
				if(parsedRule.leftFeature.equals(cfmc.getTreeItem().getText())) {
					for(FRaMEDFeature feature : ConfigurationFeatures) {
						if(parsedRule.rightFeature.equals(feature.getName().getLiteral().toString())) {
							additionalFeaturesToDelete.add(feature);
				}	}	}	
				if(parsedRule.rightFeature.equals(cfmc.getTreeItem().getText())) {
					for(FRaMEDFeature feature : ConfigurationFeatures) {
						if(parsedRule.leftFeature.equals(feature.getName().getLiteral().toString())) {
							additionalFeaturesToDelete.add(feature);
			}	}	}	}	
			//Step 3
			if(parsedRule.ruleSign.equals(implication)) {
				if(parsedRule.leftFeature.equals(cfmc.getTreeItem().getText())) {
					for(FRaMEDFeature feature : ConfigurationFeatures) {
						//Step 4
						if(parsedRule.rightFeature.equals(feature.getName().getLiteral().toString())
						   && !(feature.isManuallySelected())) {
							additionalFeaturesToDelete.add(feature);
				}	} 	}	
				if(parsedRule.rightFeature.equals(cfmc.getTreeItem().getText())) {
					for(FRaMEDFeature feature : ConfigurationFeatures) {
						//Step 5
						if(parsedRule.leftFeature.equals(feature.getName().getLiteral().toString())) {
							additionalFeaturesToDelete.add(feature);
		}	}	}	}	}	
		return additionalFeaturesToDelete;
	}
	
	/**
	 * calculates features to add to the configuration according to the feature model constraints
	 * <p>
	 * If the user chooses a feature to add there can be more features to be added than the on the user chosed to add.
	 * The other features to be add can be found by analyzing the feature model constraints. A feature model constraint
	 * looks like this: <em>leftFeature ruleSign rightFeature<em>. This operation uses the following steps
	 * to do that for every constraint:<br>
	 * Step 1: It parses the constraint using the method {@link #parseRule}.<br>
	 * Step 2: If the rule sign of the parsed rule is an equivalence, check if the user chosen feature to add
	 * 		    is part of that rule. If yes, add the other features name in this rule to the features to be added.<br>
	 * Step 3: If the rule sign is an implication, check if the user chosen feature to add is part of that rule.<br>
	 * Step 4: If the user chosen feature is the left feature of the constraint, add the right feature to list of feature to  
	 * 		   add.
	 * @param cfmc the context of the custom feature
	 * @return a list of feature names to add features with
	 */
	private List<String> resolveConstraintsAdd(ChangeConfigurationContext cfmc) {
		String equivalence = "<=>",
			   implication = "=>";
		List<String> additionalFeaturesToAdd = new ArrayList<String>();
		for(IConstraint constraint : cfmc.getFeatureModel().getConstraints()) {
			//Step 1
			ParsedRule parsedRule = parseRule(constraint);
			//Step 2
			if(parsedRule.ruleSign.equals(equivalence)) {
				if(parsedRule.leftFeature.equals(cfmc.getTreeItem().getText())) {
					additionalFeaturesToAdd.add(parsedRule.rightFeature);
				}	
				if(parsedRule.rightFeature.equals(cfmc.getTreeItem().getText())) {
					additionalFeaturesToAdd.add(parsedRule.leftFeature);
			}	}	
			//Step 3
			if(parsedRule.ruleSign.equals(implication)) {
				//Step 4
				if(parsedRule.leftFeature.equals(cfmc.getTreeItem().getText())) {
					additionalFeaturesToAdd.add(parsedRule.rightFeature);
		}	}	}
		return additionalFeaturesToAdd;
	}
	
	/**
	 * parses a feature model constraint to make the access to its features and rule sign possible in an easy way
	 * <p>
	 * A feature model constraint looks like this: <em>leftFeature ruleSign rightFeature<em>. This method uses the internal
	 * class {@link ParsedRule} to save the features and the rule sign.
	 * @param constraint the constraint to be parsed
	 * @return a {@link ParsedRule} with the parsed informations
	 */
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
