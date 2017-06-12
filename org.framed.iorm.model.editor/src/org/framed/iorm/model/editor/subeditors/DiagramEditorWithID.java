package org.framed.iorm.model.editor.subeditors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.framed.iorm.featuremodel.FRaMEDConfiguration;
import org.framed.iorm.featuremodel.FRaMEDFeature;
import org.framed.iorm.featuremodel.FeatureName;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.editor.multipage.MultipageEditor;
import org.framed.iorm.model.editor.util.MethodUtil;

import de.ovgu.featureide.fm.core.configuration.SelectableFeature;
import de.ovgu.featureide.fm.core.configuration.Selection;

public class DiagramEditorWithID extends DiagramEditor  {

	//identifier for the editor
	private String id;
	
	private MultipageEditor multipageEditor;
	
	private Model rootModel;
	
	private Resource resource;
	
	private IEditorInput editorInput;
		
	public DiagramEditorWithID(String id, MultipageEditor multipageEditor, IEditorInput editorInput) {
		super();
		this.id = id;
		this.multipageEditor = multipageEditor;
		this.editorInput = editorInput;
	}
	
	public String getId() {
		return id;
	}
	
	//make method getCommandStack public
	@Override
	public CommandStack getCommandStack() {
		return super.getCommandStack();
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		System.out.println(id);	
		super.doSave(monitor);
	}
}
