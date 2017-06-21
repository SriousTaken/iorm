package org.framed.iorm.ui.multipage;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.framed.iorm.ui.contexts.CreateModelContext;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.subeditors.DiagramEditorWithID;
import org.framed.iorm.ui.subeditors.FeatureEditorWithID;
import org.framed.iorm.ui.subeditors.TextViewerWithID;

import de.ovgu.featureide.fm.core.io.UnsupportedModelException;

/**
 * This class is used creates the overall editor to edit the role model. 
 * <p>
 * It uses subeditors by the types {@link DiagramEditorWithID}, {@link FeatureEditorWithID} and 
 * {@link TextViewerWithID}. It call the constructors and handles the management of saving these subeditors.
 * @see DiagramEditorWithID
 * @see FeatureEditorWithID
 * @see TextViewerWithID
 * @author Kevin Kassin
 */
public class MultipageEditor extends FormEditor implements IResourceChangeListener, ISelectionListener {
	
	/**
	 * name literals for the pages of the multipage editor and the model feature
	 * <p>
	 * for reference check the Strings in {@link NameLiterals}
	 * @see NameLiterals
	 */
	private final String DIAGRAM_PAGE_NAME = NameLiterals.DIAGRAM_PAGE_NAME,
						 TEXT_IORM_PAGE_NAME = NameLiterals.TEXT_IORM_PAGE_NAME,
						 TEXT_CROM_PAGE_NAME = NameLiterals.TEXT_CROM_PAGE_NAME,
						 FEATURE_PAGE_NAME = NameLiterals.FEATURE_PAGE_NAME,
						 MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
	
	/**
	 * identifier literals for the pages of the multipage editor
	 * <p>
	 * for reference check the Strings in {@link IdentifierLiterals}
	 * @see IdentifierLiterals
	 */
	private final String  PAGE_ID_DIAGRAM = IdentifierLiterals.PAGE_ID_DIAGRAM,
			   			  PAGE_ID_IORM_TEXT = IdentifierLiterals.PAGE_ID_IORM_TEXT,
			   			  PAGE_ID_CROM_TEXT = IdentifierLiterals.PAGE_ID_CROM_TEXT,
			   			  PAGE_ID_FEATURE = IdentifierLiterals.PAGE_ID_FEATURE;
	
	/**
	 * the subeditors of the multipage editor of type {@link DiagramEditorWithID}
	 * <p>
	 * the editor with the diagram
	 */
	private DiagramEditorWithID editorDiagram;
	
	/**
	 * the subeditors of the multipage editor of type {@link TextViewerWithID}
	 * <p>
	 * (1) the textviewer for the iorm diagram
	 * (2) the textviewer for the transformed diagram in the crom model
	 */
	private TextViewerWithID textViewerIORM,
							 textViewerCROM;
	/**
	 * the subeditors of the multipage editor of type {@link FeatureEditorWithID}
	 * <p>
	 * the editor that is usd to change the configuration of the role model
	 */
	private FeatureEditorWithID editorFeatures;
	
	/**
	 * the indices if the subeditor of the multipage editor
	 */
	private int editorDiagramIndex,
				textViewerIORMIndex,
				textViewerCROMIndex,
				editorFeaturesIndex;
	
	/**
	 * Class constructor
	 */
	public MultipageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	
	/**
	 * dispose the editor on close or deletion of opened file
	 */
	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * initialize method
	 * @param site
	 * @param editorInput the file opened with the editor
	 * @throws PartInitException if editor input is not of type {@link IFileEditorInput}	
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}
	
	/**
	 * get method for the editor with the diagram
	 * @return
	 */
	public DiagramEditorWithID getDiagramEditor() {
	    return editorDiagram;
	}
		
	/**
	 * This method add pages to the multipage editor using the following steps:
	 * <p>
	 * Step 1: It creates the subeditors, except the feature editor.<br>
	 * Step 2: It add the diagram page, the pages uses the subeditors and the editor input.<br>
	 * Step 3: It creates a new root model using the create Model feature in the opened diagram if there is no.<br>
	 * Step 4: It save after the creation of the root model.<br>
	 * Step 5: It creates the feature editor and adds the page. To do that the created root model is needed.
	 * 		   It also add the pages of the iorm and crom text viewers.<br> 
	 * Step 6: Its set the names of the pages.
	 * @exception PartInitException
	 * @exception FileNotFoundException
	 * @exception UnsupportedModelException
	 */
	@Override
	protected void addPages() {
		//Step 1
		editorDiagram = new DiagramEditorWithID(PAGE_ID_DIAGRAM, getEditorInput());
		textViewerIORM = new TextViewerWithID(PAGE_ID_IORM_TEXT);
		textViewerCROM = new TextViewerWithID(PAGE_ID_CROM_TEXT);
		//Step 2
		try { 
			editorDiagramIndex = addPage(editorDiagram, getEditorInput());		
		} catch (PartInitException e) { e.printStackTrace(); }
		//Step 3
		ICreateFeature createModelFeature = null;
		ICreateFeature[] createFeatures = editorDiagram.getDiagramTypeProvider().getFeatureProvider().getCreateFeatures();
		for(int i = 0; i<createFeatures.length; i++) {
			if(createFeatures[i].getCreateName().equals(MODEL_FEATURE_NAME)) 
				createModelFeature = createFeatures[i];
		}
		CreateModelContext createModelFeatureContext = new CreateModelContext();
		createModelFeatureContext.setDiagramEditor(editorDiagram);
		createModelFeature.create(createModelFeatureContext);
		//Step 4
		doSave(null);
		//Step 5
		try {
			editorFeatures = new FeatureEditorWithID(PAGE_ID_FEATURE, getEditorInput(), this);
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		  catch (UnsupportedModelException e) { e.printStackTrace(); }	
		try {
			editorFeaturesIndex = addPage(editorFeatures, getEditorInput());
			textViewerIORMIndex = addPage(textViewerIORM, getEditorInput());
			textViewerCROMIndex = addPage(textViewerCROM, getEditorInput());
		} catch (PartInitException e) { e.printStackTrace(); }
		//Step 6
		setPageText(editorDiagramIndex, DIAGRAM_PAGE_NAME);
		setPageText(textViewerIORMIndex, TEXT_IORM_PAGE_NAME);
		setPageText(textViewerCROMIndex, TEXT_CROM_PAGE_NAME);	
		setPageText(editorFeaturesIndex, FEATURE_PAGE_NAME);
	}
	
	/**
	 * save method
	 * <p>
	 * This operation calls the save methods of the subeditors and synchronizes the feature configuration
	 * in the feature editor with the one in the role model. This is needed because there can be inconsistencies
	 * after undoing and redoing an feature configuration change. 
	 * @param monitor the monitor used for the save activity
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		if(editorDiagram.isDirty()) editorDiagram.doSave(monitor);
		//at the first save of a new diagram there is no editor feature
		if(editorFeatures != null)
			editorFeatures.synchronizeConfigurationEditorAndModelConfiguration();
	}
		
	/**
	 * sets the selected page as active page
	 * <p>
	 * This operation calls the save method to avoid save competition between subeditors
	 * @param newPageIndex the index of the page to change to
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
	}
	
	/**
	 * disables the save as function
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
		
	/**
	 * operation not used
	 */
	@Override
	public void doSaveAs() {}
	
	/**
	 * operation not used
	 */
	public void resourceChanged(final IResourceChangeEvent event) {}
	
	/**
	 * operation not used
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {}
}
