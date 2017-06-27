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

/**
 * This class enables context buttons and can manipulate the palette of the editor.
 * @author Kevin Kassin
 */
public class ToolBehaviorProvider extends DefaultToolBehaviorProvider{
	
	/**
	 * the name literals for features to remove from the editor palette of the diagram type
	 */
	private static final String ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME = NameLiterals.ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME,
								MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
	
	/**
	 * Class constructor
	 * @param diagramTypeProvider the provider of the edited diagram type
	 */
	public ToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}
	
	/**
	 * sets the context buttons for the editor of the diagram type
	 * <p>
	 * This operation explicitly don't sets the context button for the remove function, since this function is disabled.
	 * @see {@link org.framed.iorm.ui.providers.FeatureProvider#getRemoveFeature}
	 */
	@Override
	public IContextButtonPadData getContextButtonPad(IPictogramElementContext pictogramElementContext) {
		IContextButtonPadData contextButtonPadData = super.getContextButtonPad(pictogramElementContext);
	    PictogramElement pictogramElement = pictogramElementContext.getPictogramElement();
	    setGenericContextButtons(contextButtonPadData, pictogramElement, CONTEXT_BUTTON_DELETE | CONTEXT_BUTTON_UPDATE);
	    return contextButtonPadData;
	}    
	
	/**
	 * removes create features implemented by the pattern from the palette
	 * <p>
	 * This is done for patterns which dont have a create features or whichs create features should not be used by the user
	 * manually. Explicitly this patterns are:<br>
	 * (1) {@link org.framed.iorm.ui.pattern.shapes.AttributeOperationCommonPattern} and<br>
	 * (2) {@link org.framed.iorm.ui.pattern.shapes.ModelPattern}.
	 */
	@Override
	public IPaletteCompartmentEntry[] getPalette() {
		List<IPaletteCompartmentEntry> paletteCompartmentEntry = new ArrayList<IPaletteCompartmentEntry>();
	    IPaletteCompartmentEntry[] superCompartments = super.getPalette();
	    for(int i = 0; i < superCompartments[1].getToolEntries().size(); i++) {
	    	if(superCompartments[1].getToolEntries().get(i).getLabel().equals(ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME) ||
	    	   superCompartments[1].getToolEntries().get(i).getLabel().equals(MODEL_FEATURE_NAME))
	    		superCompartments[1].getToolEntries().remove(i);
	    }
	    for (int j = 0; j < superCompartments.length; j++) {
	    	paletteCompartmentEntry.add(superCompartments[j]);
	    }
	    return paletteCompartmentEntry.toArray(new IPaletteCompartmentEntry[paletteCompartmentEntry.size()]);
	}	    
}
