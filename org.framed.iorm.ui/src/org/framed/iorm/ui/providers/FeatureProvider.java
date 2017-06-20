package org.framed.iorm.ui.providers;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.features.impl.DefaultRemoveFeature;
import org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns;
import org.framed.iorm.ui.graphitifeatures.ChangeConfigurationFeature;
import org.framed.iorm.ui.pattern.features.connections.InheritancePattern;
import org.framed.iorm.ui.pattern.features.shapes.AttributeOperationCommonPattern;
import org.framed.iorm.ui.pattern.features.shapes.AttributePattern;
import org.framed.iorm.ui.pattern.features.shapes.DataTypePattern;
import org.framed.iorm.ui.pattern.features.shapes.ModelPattern;
import org.framed.iorm.ui.pattern.features.shapes.NaturalTypePattern;
import org.framed.iorm.ui.pattern.features.shapes.OperationPattern;

public class FeatureProvider extends DefaultFeatureProviderWithPatterns {
	
	public FeatureProvider(IDiagramTypeProvider diagramTypeProvider) {
      super(diagramTypeProvider);
      
      //add patterns for shapes
      addPattern(new ModelPattern());
      addPattern(new NaturalTypePattern());
      addPattern(new DataTypePattern());
      addPattern(new AttributeOperationCommonPattern());
      addPattern(new AttributePattern());
      addPattern(new OperationPattern());
      
      //add patterns for connections
      addConnectionPattern(new InheritancePattern());
	}	
	
	//disable the remove feature
	@Override
	public IRemoveFeature getRemoveFeature(IRemoveContext context) {
		return new DefaultRemoveFeature(this) {
			@Override
			public boolean isAvailable(IContext context) {
				return false;
			}
		};
	}
	
	//add custom feature to change the feature model
	@Override
	public ICustomFeature[] getCustomFeatures(ICustomContext context) {
	    return new ICustomFeature[] { new ChangeConfigurationFeature(this) };
	} 
}

