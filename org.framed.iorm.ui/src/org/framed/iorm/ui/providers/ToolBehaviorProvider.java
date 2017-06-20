package org.framed.iorm.ui.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;
import org.framed.iorm.ui.literals.NameLiterals;

public class ToolBehaviorProvider extends DefaultToolBehaviorProvider{
	
	//name literals
	private static final String ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME = NameLiterals.ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME,
								MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
	
	public ToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}
	
	@Override
	public IContextButtonPadData getContextButtonPad(IPictogramElementContext pictogramElementContext) {
		IContextButtonPadData contextButtonPadData = super.getContextButtonPad(pictogramElementContext);
	    PictogramElement pictogramElement = pictogramElementContext.getPictogramElement();
	    setGenericContextButtons(contextButtonPadData, pictogramElement, CONTEXT_BUTTON_DELETE | CONTEXT_BUTTON_UPDATE);
	    return contextButtonPadData;
	}    
	
	@Override
	public IPaletteCompartmentEntry[] getPalette() {
		List<IPaletteCompartmentEntry> paletteCompartmentEntry = new ArrayList<IPaletteCompartmentEntry>();
	
		//compartments from superclass
	    IPaletteCompartmentEntry[] superCompartments = super.getPalette();
	    //remove features from palette in create features compartment
	    for(int i = 0; i < superCompartments[1].getToolEntries().size(); i++) {
	    	if(superCompartments[1].getToolEntries().get(i).getLabel().equals(ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME) ||
	    	   superCompartments[1].getToolEntries().get(i).getLabel().equals(MODEL_FEATURE_NAME))
	    		superCompartments[1].getToolEntries().remove(i);
	    }
	    //add compartments from superclass
	    for (int j = 0; j < superCompartments.length; j++) {
	    	paletteCompartmentEntry.add(superCompartments[j]);
	    }
	    return paletteCompartmentEntry.toArray(new IPaletteCompartmentEntry[paletteCompartmentEntry.size()]);
	}	    
}
