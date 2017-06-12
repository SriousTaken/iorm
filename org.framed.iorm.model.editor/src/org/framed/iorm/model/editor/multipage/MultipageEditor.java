package org.framed.iorm.model.editor.multipage;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.framed.iorm.featuremodel.FRaMEDConfiguration;
import org.framed.iorm.featuremodel.FRaMEDFeature;
import org.framed.iorm.featuremodel.FeaturemodelFactory;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.Shape;
import org.framed.iorm.model.editor.literals.IdentifierLiterals;
import org.framed.iorm.model.editor.literals.NameLiterals;
import org.framed.iorm.model.editor.subeditors.DiagramEditorWithID;
import org.framed.iorm.model.editor.subeditors.FeatureEditorWithID;
import org.framed.iorm.model.editor.subeditors.TextViewerWithID;
import org.framed.iorm.model.editor.util.MethodUtil;

import de.ovgu.featureide.fm.core.io.UnsupportedModelException;

public class MultipageEditor extends FormEditor implements IResourceChangeListener, ISelectionListener {
	
	boolean a=false, b=false, c=false, d=false;
	
	//name literals
	private final String BEHAVIOR_PAGE_NAME = NameLiterals.BEHAVIOR_PAGE_NAME,
						 DATA_PAGE_NAME = NameLiterals.DATA_PAGE_NAME,
						 TEXT_IORM_PAGE_NAME = NameLiterals.TEXT_IORM_PAGE_NAME,
						 TEXT_CROM_PAGE_NAME = NameLiterals.TEXT_CROM_PAGE_NAME,
						 FEATURE_PAGE_NAME = NameLiterals.FEATURE_PAGE_NAME,
						 MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
	
	//id literals
	private final String  PAGE_ID_BEHAVIOR = IdentifierLiterals.PAGE_ID_BEHAVIOR,
			   			  PAGE_ID_DATA = IdentifierLiterals.PAGE_ID_DATA,
			   			  PAGE_ID_IORM_TEXT = IdentifierLiterals.PAGE_ID_IORM_TEXT,
			   			  PAGE_ID_CROM_TEXT = IdentifierLiterals.PAGE_ID_CROM_TEXT,
			   			  PAGE_ID_FEATURE = IdentifierLiterals.PAGE_ID_FEATURE;
	
	//editors
	private DiagramEditorWithID editorBehaviorDiagram,
								editorDataDiagram;
	private TextViewerWithID textViewerIORM,
							 textViewerCROM;
	private FeatureEditorWithID editorFeatures;
	
	//editors indexes
	private int editorBehaviorDiagramIndex,
				editorDataDiagramIndex,
				textViewerIORMIndex,
				textViewerCROMIndex,
				editorFeaturesIndex;
	
	public MultipageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
		
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}
	
	public DiagramEditorWithID getBehaviorEditor() {
	    return editorBehaviorDiagram;
	}
	
	public DiagramEditorWithID getDataEditor() {
	    return editorDataDiagram;
	}
	
	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
		
	@Override
	protected void addPages() {
		//create editors (except feature editor)
		editorBehaviorDiagram = new DiagramEditorWithID(PAGE_ID_BEHAVIOR, this, getEditorInput());
		editorDataDiagram = new DiagramEditorWithID(PAGE_ID_DATA, this, getEditorInput());
		textViewerIORM = new TextViewerWithID(PAGE_ID_IORM_TEXT);
		textViewerCROM = new TextViewerWithID(PAGE_ID_CROM_TEXT);
		//add pages (except feature page)
		try { 
			editorBehaviorDiagramIndex = addPage(editorBehaviorDiagram, getEditorInput());
			editorDataDiagramIndex = addPage(editorDataDiagram, getEditorInput());
			textViewerIORMIndex = addPage(textViewerIORM, getEditorInput());
			textViewerCROMIndex = addPage(textViewerCROM, getEditorInput());
		} catch (PartInitException e) { e.printStackTrace(); }
		//set data page as active to create root model and the feature model
		setActivePage(editorDataDiagramIndex);
		//create root model in graphiti business model in
		ICreateFeature createModelFeature = null;
			ICreateFeature[] createFeatures = editorDataDiagram.getDiagramTypeProvider().getFeatureProvider().getCreateFeatures();
			for(int i = 0; i<createFeatures.length; i++) {
				if(createFeatures[i].getCreateName().equals(MODEL_FEATURE_NAME)) 
					createModelFeature = createFeatures[i];
			}
		ICreateContext createModelFeatureContext = new CreateContext();
		createModelFeature.create(createModelFeatureContext);
		//save after creation of root model
		doSave(null);
		
		//create feature editor and add feature page
		try {
			editorFeatures = new FeatureEditorWithID(PAGE_ID_FEATURE, getEditorInput(), this);
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		  catch (UnsupportedModelException e) { e.printStackTrace(); }	
		try {
			editorFeaturesIndex = addPage(editorFeatures, getEditorInput());
		} catch (PartInitException e) { e.printStackTrace(); }
		//set page names
		setPageText(editorBehaviorDiagramIndex, BEHAVIOR_PAGE_NAME);
		setPageText(editorDataDiagramIndex, DATA_PAGE_NAME);
		setPageText(textViewerIORMIndex, TEXT_IORM_PAGE_NAME);
		setPageText(textViewerCROMIndex, TEXT_CROM_PAGE_NAME);	
		setPageText(editorFeaturesIndex, FEATURE_PAGE_NAME);
		//set behavior page as is active for the user to see first
		setActivePage(editorBehaviorDiagramIndex);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		//save feature editor
		if(getEditor(editorFeaturesIndex).isDirty()) getEditor(editorFeaturesIndex).doSave(monitor);
		//save active page last, so it has the priority
		//if text pages or feature page are active prioritize the behavior page
		if(getActivePage()==editorDataDiagramIndex) {
			if(editorBehaviorDiagram.isDirty()) editorBehaviorDiagram.doSave(monitor);
			if(editorDataDiagram.isDirty()) editorDataDiagram.doSave(monitor);
		} else {
			if(editorDataDiagram.isDirty()) editorDataDiagram.doSave(monitor);
			if(editorBehaviorDiagram.isDirty()) editorBehaviorDiagram.doSave(monitor);
		}	
		
	}
		
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void doSaveAs() {}
	
	protected void pageChange(int newPageIndex) {
		doSave(null);
		super.pageChange(newPageIndex);
	}
		
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
						for (int i = 0; i<pages.length; i++){
							if(((FileEditorInput)editorBehaviorDiagram.getEditorInput()).getFile().getProject().equals(event.getResource())){
								IEditorPart editorPart = pages[i].findEditor(editorBehaviorDiagram.getEditorInput());
								pages[i].closeEditor(editorPart,true);
				}	}	}	}	
			);	
	}	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		//System.out.println(editorBehaviorDiagram);
		if (editorBehaviorDiagram.equals(getActiveEditor())) 
			editorBehaviorDiagram.selectionChanged(getActiveEditor(), selection);
	    if (editorDataDiagram.equals(getActiveEditor()))
	    	editorDataDiagram.selectionChanged(getActiveEditor(), selection);
	}
}
