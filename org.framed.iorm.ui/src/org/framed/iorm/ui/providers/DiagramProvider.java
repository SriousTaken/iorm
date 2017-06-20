package org.framed.iorm.ui.providers;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

public class DiagramProvider extends AbstractDiagramTypeProvider {

	private IToolBehaviorProvider[] toolBehaviorProviders;
	
	public DiagramProvider() { 
		super(); 
		setFeatureProvider(new FeatureProvider(this));
	}
	
	@Override
	public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
		if (toolBehaviorProviders == null)
			toolBehaviorProviders = new IToolBehaviorProvider[] { new ToolBehaviorProvider(this) };
		return toolBehaviorProviders;
	}
}
