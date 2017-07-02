package org.framed.iorm.ui.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;
import org.eclipse.graphiti.tb.IContextMenuEntry;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.util.PropertyUtil;
import org.framed.iorm.ui.providers.FeatureProvider; //*import for javadoc link
import org.framed.iorm.ui.pattern.shapes.AttributeOperationCommonPattern; //*import for javadoc link
import org.framed.iorm.ui.pattern.shapes.ModelPattern; //*import for javadoc link

/**
 * This class enables context buttons and can manipulate the palette of the editor.
 * @author Kevin Kassin
 */
public class ToolBehaviorProvider extends DefaultToolBehaviorProvider{
	
	/**
	 * the name literals for features to remove from the editor palette for the diagram type
	 * gathered from {@link NameLiterals}
	 */
	private final String ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME = NameLiterals.ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME,
						 MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
	
	/**
	 * the name literals for features to probaly add to the context menu for the diagram type
	 * gathered from {@link NameLiterals}
	 */
	private final String CHANGECONFIGURATION_FEATURE_NAME = NameLiterals.CHANGECONFIGURATION_FEATURE_NAME,
						 STEP_IN_FEATURE_NAME = NameLiterals.STEP_IN_FEATURE_NAME,
						 STEP_OUT_FEATURE_NAME = NameLiterals.STEP_OUT_FEATURE_NAME;
	
	/**
	 * the shape identifiers of the shapes the step in feature can be used on gathered from {@link IdentifierLiterals}
	 * <p>
	 * can be:<br>
	 * (1) the shape identifier of type body rectangle of a group or<br>
	 * (2) the shape identifier of type body rectangle of a compartment type or<br>
	 * (3) the shape identifier of type body rectangle of a role group 
	 */
	private final String SHAPE_ID_GROUP_TYPEBODY = IdentifierLiterals.SHAPE_ID_GROUP_TYPEBODY;
	
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
	 * @see {@link FeatureProvider#getRemoveFeature}
	 */
	@Override
	public IContextButtonPadData getContextButtonPad(IPictogramElementContext pictogramElementContext) {
		IContextButtonPadData contextButtonPadData = super.getContextButtonPad(pictogramElementContext);
	    PictogramElement pictogramElement = pictogramElementContext.getPictogramElement();
	    setGenericContextButtons(contextButtonPadData, pictogramElement, CONTEXT_BUTTON_DELETE | CONTEXT_BUTTON_UPDATE);
	    return contextButtonPadData;
	}    
	
	/**
	 * set the context menu for a specific context, for example a right clicked pictogram element.
	 * <p>
	 * This operation controls which custom features are shown in the context menu depending on the
	 * right clicked pictogram element. It does this using the following steps:<br>
	 * Step 1: It iterates over all custom feature to probably add to the list of custom feature to show in
	 * 		   the context menu.<br>
	 * Step 2: If its the change configuration custom feature, never add it to this list.<br>
	 * Step 3: If its the step in feature check if the right clicked pictogram element (exactly one!) has a
	 * 		   graphics algorithm that is the type body of a group, compartment type or role group. If yes, add the
	 * 		   corresponding context menu entry to the context menu.
	 */
	@Override
	public IContextMenuEntry[] getContextMenu(ICustomContext customContext) {
		IContextMenuEntry[] superContextEntries = super.getContextMenu(customContext);
		List<IContextMenuEntry> contextMenuEntries = new ArrayList<IContextMenuEntry>();
		//Step 1
		for(int i = 0; i < superContextEntries.length; i++) {
			switch(superContextEntries[i].getText()) {
				//Step 2
				case CHANGECONFIGURATION_FEATURE_NAME: 
					break;
				//Step 3	
				case STEP_IN_FEATURE_NAME: 
					if(customContext.getPictogramElements().length == 1) {
						if(customContext.getPictogramElements()[0].getGraphicsAlgorithm() != null &&
						   !(customContext.getPictogramElements()[0] instanceof Diagram)) {
							GraphicsAlgorithm graphicAlgorithm =  customContext.getPictogramElements()[0].getGraphicsAlgorithm();
							if(PropertyUtil.isShape_IdValue(graphicAlgorithm, SHAPE_ID_GROUP_TYPEBODY)) 
								contextMenuEntries.add(superContextEntries[i]);
					}	}
					break;
				case STEP_OUT_FEATURE_NAME:	
					contextMenuEntries.add(superContextEntries[i]);
					break;
				default: 
					break;	
			}
		}
		return contextMenuEntries.toArray(new IContextMenuEntry[contextMenuEntries.size()]);
	}
	
	/**
	 * removes create features implemented by the pattern from the palette
	 * <p>
	 * This is done for patterns which dont have a create features or whichs create features should not be used by the user
	 * manually. Explicitly this patterns are:<br>
	 * (1) {@link AttributeOperationCommonPattern} and<br>
	 * (2) {@link ModelPattern}.
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
