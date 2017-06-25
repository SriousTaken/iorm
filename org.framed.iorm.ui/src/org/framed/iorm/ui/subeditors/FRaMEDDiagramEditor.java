package org.framed.iorm.ui.subeditors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.framed.iorm.featuremodel.FRaMEDFeature;

/**
 * the diagram editor used by {@link org.framed.iorm.ui.multipage.MultipageEditor}
 * <p>
 * Its extends {@link DiagramEditor} by the variable {@link #selectedFeatures} and its get and set methods.
 * Its also makes the editors CommandStack public.
 * @author Kevin Kassin
 */
public class FRaMEDDiagramEditor extends DiagramEditor  {
	
	/**
	 * the list of selected features in the diagram
	 * <p>
	 * Its refreshed each time the features are changed by the {@link org.framed.iorm.ui.subeditors.FRaMEDFeatureEditor}.
	 * On saving the feature editor uses this to synchronize its configuration with the diagrams configuration. This is
	 * needed to implement undo and redo for configuration changes.
	 */
	private EList<FRaMEDFeature> selectedFeatures = null;
			
	/**
	 * Class constructor
	 */
	public FRaMEDDiagramEditor() {
		super();
	}
	
	/**
	 * the set method for the variable {@link #selectedFeatures}
	 * @param selectedFeatures the new value of selectedFeatures to set
	 */
	public void setSelectedFeatures(EList<FRaMEDFeature> selectedFeatures) {
		this.selectedFeatures = selectedFeatures;
	}
	
	/**
	 * the get method for the variable {@link #selectedFeatures}
	 * @return the selected features of the diagram 
	 */
	public EList<FRaMEDFeature> getSelectedFeatures() {
		return selectedFeatures;
	}

	/**
	 * publishes the editors command stack
	 */
	@Override
	public CommandStack getCommandStack() {
		return super.getCommandStack();
	}
}