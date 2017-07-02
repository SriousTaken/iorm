package org.framed.iorm.ui.subeditors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.framed.iorm.featuremodel.FRaMEDFeature;
import org.framed.iorm.model.Model;
import org.framed.iorm.ui.util.GeneralUtil;
import org.framed.iorm.ui.multipage.MultipageEditor; //*import for javadoc link
import org.framed.iorm.ui.subeditors.FRaMEDFeatureEditor; //*import for javadoc link

/**
 * the diagram editor used by {@link MultipageEditor}
 * <p>
 * Its extends {@link DiagramEditor} by the variable {@link #selectedFeatures} and its get and set methods.
 * Its also makes the editors CommandStack public.
 * @author Kevin Kassin
 */
public class FRaMEDDiagramEditor extends DiagramEditor  {
	
	/**
	 * the list of selected features in the diagram
	 * <p>
	 * Its refreshed each time the features are changed by the {@link FRaMEDFeatureEditor}.
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
	 * updates the value of the class variable {@link #selectedFeatures}
	 */
	public void updateSelectedFeatures() {
		Model rootModel = GeneralUtil.getDiagramRootModel(this.getDiagramTypeProvider().getDiagram());
		selectedFeatures = rootModel.getFramedConfiguration().getFeatures();
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