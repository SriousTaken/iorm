package org.framed.iorm.ui.contexts;

import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.framed.iorm.ui.subeditors.DiagramEditorWithID;;

/**
 * This context is used to save needed information for the create feature of the model
 * in the {@link ModelPattern}.
 * <p>
 * It extends {@link CreateContext} by adding one variable and its get- and set methods.
 * @author Kevin Kassin
 */
public class CreateModelContext extends CreateContext {
	
	/**
	 * the diagram editor the create feature works on
	 */
	private DiagramEditorWithID diagramEditor;
	
	/**
	 * set the class variable diagramEditor
	 * @param diagramEditor the diagram editor to set
	 */
	public void setDiagramEditor(DiagramEditorWithID diagramEditor) {
		this.diagramEditor = diagramEditor;
	}
	
	/**
	 * get method for the diagram editor
	 * @return the class variable diagram editor
	 */
	public DiagramEditorWithID getDiagramEditor() {
		return diagramEditor;
	}

}
