package org.framed.iorm.ui.graphitifeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.ui.PlatformUI;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.multipage.MultipageEditor;
import org.framed.iorm.ui.util.PropertyUtil;

//TODO
public abstract class AbstractStepInFeature extends AbstractCustomFeature {

	/**
	 * the shape identifiers of the shapes this feature can be used on gathered from {@link IdentifierLiterals}
	 * <p>
	 * can be:<br>
	 * (1) the shape identifier of type body rectangle of a group or<br>
	 * (2) the shape identifier of type body rectangle of a compartment type or<br>
	 */
	protected final String SHAPE_ID_GROUP_TYPEBODY = IdentifierLiterals.SHAPE_ID_GROUP_TYPEBODY;
	
	/**
	 * identifiers used to open a new editor for the groups or compartment types diagram gathered from {@link IdentifierLiterals}
	 * <p>
	 * can be:<br>
	 * (1) the identifier for the diagram provider that is used to create an IDiagramEditorInput
	 *     in the operation {@link #execute} or<br>
	 * (2) the identifier for the multipage editor that is used to open a new multipage editor in 
	 *     the operation {@link #execute} 
	 */
	protected final String DIAGRAM_PROVIDER_ID = IdentifierLiterals.DIAGRAM_PROVIDER_ID,
						   EDITOR_ID = IdentifierLiterals.EDITOR_ID;
	
	/**
	 * Class constructor
	 * @param featureProvider the feature provider the feature belongs to
	 */
	public AbstractStepInFeature(IFeatureProvider featureProvider) {
		super(featureProvider);
	}
	
	/**
	 * This methods checks if the feature can be executed.
	 * <p>
	 * It return true if<br>
	 * (1) exactly one pictogram element is selected and<br>
	 * (2) this pictogram element has a graphics algorithm that is the type body of a 
	 * group, compartment type or role group and<br>
	 * (3) there are no unsaved changes of the diagram editor in which the pictogram element is shown
	 * @return if the feature can be executed
	 */
	@Override
	public boolean canExecute(ICustomContext customContext) {
		if(customContext.getPictogramElements().length == 1) {
			if(customContext.getPictogramElements()[0].getGraphicsAlgorithm() != null) {
				GraphicsAlgorithm graphicAlgorithm =  customContext.getPictogramElements()[0].getGraphicsAlgorithm();
				if(PropertyUtil.isShape_IdValue(graphicAlgorithm, SHAPE_ID_GROUP_TYPEBODY)) { 
					MultipageEditor multipageEditor = (MultipageEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
					if(!(multipageEditor.getDiagramEditor().isDirty()))
						return true;
		}	}	}
		return false;
	}

	/**
	 * to be overriden in subclasses
	 */
	@Override
	public abstract void execute(ICustomContext context);

}
