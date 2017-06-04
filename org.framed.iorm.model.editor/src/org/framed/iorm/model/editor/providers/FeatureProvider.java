package org.framed.iorm.model.editor.providers;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.impl.DefaultRemoveFeature;
import org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns;
import org.framed.iorm.model.editor.pattern.connections.InheritancePattern;
import org.framed.iorm.model.editor.pattern.shapes.AttributeOperationCommonPattern;
import org.framed.iorm.model.editor.pattern.shapes.AttributePattern;
import org.framed.iorm.model.editor.pattern.shapes.DataTypePattern;
import org.framed.iorm.model.editor.pattern.shapes.ModelPattern;
import org.framed.iorm.model.editor.pattern.shapes.NaturalTypePattern;
import org.framed.iorm.model.editor.pattern.shapes.OperationPattern;

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
	
	@Override
	public IRemoveFeature getRemoveFeature(IRemoveContext context) {
		return new DefaultRemoveFeature(this) {
			@Override
			public boolean isAvailable(IContext context) {
				return false;
			}
		};
	}
}

