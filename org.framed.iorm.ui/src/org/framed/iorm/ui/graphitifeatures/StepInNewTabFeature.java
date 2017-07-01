package org.framed.iorm.ui.graphitifeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.literals.TextLiterals;
import org.framed.iorm.ui.multipage.MultipageEditor;
import org.framed.iorm.ui.util.GeneralUtil;
import org.framed.iorm.ui.util.PropertyUtil;

/**
 * This graphiti custom feature is used to step in groups and compartment types by opening a new tab.
 * @author Kevin Kassin
 */
public class StepInNewTabFeature extends AbstractCustomFeature {
	
	/**
	 * the name of the feature gathered from {@link NameLiterals} 
	 */
	private final String STEP_IN_FEATURE_NAME = NameLiterals.STEP_IN_FEATURE_NAME;
	
	/**
	 * the shape identifiers of the shapes this feature can be used on gathered from {@link IdentifierLiterals}
	 * <p>
	 * can be:<br>
	 * (1) the shape identifier of type body rectangle of a group or<br>
	 * (2) the shape identifier of type body rectangle of a compartment type or<br>
	 */
	private final String SHAPE_ID_GROUP_TYPEBODY = IdentifierLiterals.SHAPE_ID_GROUP_TYPEBODY;

	/**
	 * identifiers used to open a new editor for the groups or compartment types diagram gathered from {@link IdentifierLiterals}
	 * <p>
	 * can be:<br>
	 * (1) the identifier for the diagram provider that is used to create an IDiagramEditorInput
	 *     in the operation {@link #execute} or<br>
	 * (2) the identifier for the multipage editor that is used to open a new multipage editor in 
	 *     the operation {@link #execute} 
	 */
	private final String DIAGRAM_PROVIDER_ID = IdentifierLiterals.DIAGRAM_PROVIDER_ID,
						 EDITOR_ID = IdentifierLiterals.EDITOR_ID;
	
	/**
	 * the prefix of the multipage editor name if an groups diagram is opened in the multipage editor gathered
	 * from {@link TextLiterals} 
	 */
	private final String MULTIPAGE_EDITOR_NAME_GROUP_DIAGRAM = TextLiterals.MULTIPAGE_EDITOR_NAME_GROUP_DIAGRAM;
	
	/**
	 * Class constructor
	 * @param featureProvider the feature provider the feature belongs to 
	 */
	public StepInNewTabFeature(IFeatureProvider featureProvider) {
		super(featureProvider);
	}
	 
	/**
	 * get method for the features name
	 * @return the name of the feature 
	 */
	@Override
	public String getName() {
		return STEP_IN_FEATURE_NAME;
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
	 * This operation opens a new multipage editor with the groups or compartment types diagram and sets the multipage editors name.
	 * <p>
	 * In the first line there are no checks for types and the size of the list of selected pictograms needed since this checks are
	 * already done in {@link #canExecute}.  
	 */
	@Override
	public void execute(ICustomContext context) {
		ContainerShape typeBodyShape = (ContainerShape) context.getPictogramElements()[0];
		Diagram groupDiagram = GeneralUtil.getGroupDiagramFromGroupShape(typeBodyShape);
		IEditorInput diagramEditorInput = DiagramEditorInput.createEditorInput(groupDiagram, DIAGRAM_PROVIDER_ID);
		try {
			MultipageEditor multipageEditor = (MultipageEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(diagramEditorInput, EDITOR_ID);
			multipageEditor.setPartName(MULTIPAGE_EDITOR_NAME_GROUP_DIAGRAM + " " + groupDiagram.getName());
		} catch (PartInitException e) { e.printStackTrace(); }
	}	
}	
