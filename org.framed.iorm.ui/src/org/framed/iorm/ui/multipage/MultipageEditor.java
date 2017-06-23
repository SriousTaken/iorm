package org.framed.iorm.ui.multipage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
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
import org.eclipse.ui.part.FileEditorInput;
import org.framed.iorm.ui.contexts.CreateModelContext;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.subeditors.FRaMEDDiagramEditor;
import org.framed.iorm.ui.subeditors.FRaMEDFeatureEditor;
import org.framed.iorm.ui.subeditors.FRaMEDTextViewer;
import org.framed.iorm.ui.util.MethodUtil;

import de.ovgu.featureide.fm.core.io.UnsupportedModelException;

/**
 * This class is creates the overall editor to edit the role model. 
 * <p>
 * It uses subeditors by the types {@link FRaMEDDiagramEditor}, {@link FRaMEDFeatureEditor} and 
 * {@link FRaMEDTextViewer}. It call the constructors and handles the management of saving these subeditors.
 * @see FRaMEDDiagramEditor
 * @see FRaMEDFeatureEditor
 * @see FRaMEDTextViewer
 * @author Kevin Kassin
 */
public class MultipageEditor extends FormEditor implements ISelectionListener {
	
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
						 STATUS_PAGE_OK = NameLiterals.STATUS_PAGE_OK,
						 STATUS_PAGE_UNSAVED_CHANGES = NameLiterals.STATUS_PAGE_UNSAVED_CHANGES,
						 MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
			
	/**
	 * the subeditors of the multipage editor of type {@link FRaMEDDiagramEditor}
	 * <p>
	 * the editor with the diagram
	 */
	private FRaMEDDiagramEditor editorDiagram;
	
	/**
	 * the subeditors of the multipage editor of type {@link FRaMEDTextViewer}
	 * <p>
	 * (1) the textviewer for the iorm diagram
	 * (2) the textviewer for the transformed diagram in the crom model
	 */
	private FRaMEDTextViewer textViewerIORM,
							 textViewerCROM;
	
	/**
	 * the subeditor status page
	 * <p>
	 * This editor is not intended to be opened. Its function is to show the status of the multipage editor.
	 * The 2 statuses can be that everything is ok (the status pages name is "STATUS: OK") or that that there are
	 * unsaved changes to the file opened in the editor and the pages content are out of sync (the status pages
	 * name is "STATUS: Unsaved changes - the pages are out of sync!").<br>
	 * As editor input is has an empty text file in the role model project associated.
	 */
	private FRaMEDTextViewer statusPage;
							 
	/**
	 * the subeditors of the multipage editor of type {@link FRaMEDFeatureEditor}
	 * <p>
	 * the editor that is usd to change the configuration of the role model
	 */
	private FRaMEDFeatureEditor editorFeatures;
	
	/**
	 * the indices if the subeditor of the multipage editor
	 */
	private int editorDiagramIndex,
				textViewerIORMIndex,
				textViewerCROMIndex,
				editorFeaturesIndex,
				statusPageIndex;
	
	/**
	 * Class constructor
	 */
	public MultipageEditor() {
		super();
	}
	
	/**
	 * dispose the editor on close or deletion of opened file
	 */
	@Override
	public void dispose() {
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
	public FRaMEDDiagramEditor getDiagramEditor() {
	    return editorDiagram;
	}
		
	/**
	 * This method add pages to the multipage editor using the following steps:
	 * <p>
	 * Step 1: It creates the diagram subeditor.<br>
	 * Step 2: It add the diagram page, the pages use the subeditors and the editor input.<br>
	 * Step 3: It creates a new root model using the create Model feature in the opened diagram if there is no.<br>
	 * Step 4: It save after the creation of the root model.<br>
	 * Step 5: It creates the feature editor and adds the page. To do that the created root model is needed.
	 * 		   It also creates the editores and add the pages for the iorm and crom text viewers.
	 * 		   It also creates the text editor for the status page.<br>  
	 * Step 6: It fetches the empty textfile created by the {@link org.framed.iorm.ui.wizards.RoleModelProjectWizard} in the project in which 
	 * 		   the diagram is created and add the the status page with this input.<br>
	 * Step 7: Its set the names of the pages.
	 * @exception PartInitException
	 * @exception FileNotFoundException
	 * @exception UnsupportedModelException
	 * @exception URISyntaxException
	 * @exception IOException
	 */
	@Override
	protected void addPages() {
		//Step 1
		editorDiagram = new FRaMEDDiagramEditor();
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
			editorFeatures = new FRaMEDFeatureEditor(getEditorInput(), this);
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		  catch (UnsupportedModelException e) { e.printStackTrace(); }	
			textViewerIORM = new FRaMEDTextViewer();
			textViewerCROM = new FRaMEDTextViewer();
			statusPage = new FRaMEDTextViewer();
		try {
			editorFeaturesIndex = addPage(editorFeatures, getEditorInput());
			textViewerIORMIndex = addPage(textViewerIORM, getEditorInput());
			textViewerCROMIndex = addPage(textViewerCROM, getEditorInput());
			//Step 6
			IFile emptyTextFile = null;
			try {
				emptyTextFile = MethodUtil.getEmptyTextFileForDiagram(getEditorInput());
			} catch (URISyntaxException | IOException e) { e.printStackTrace(); }
			statusPageIndex = addPage(statusPage, new FileEditorInput(emptyTextFile));
		} catch (PartInitException e) { e.printStackTrace(); }
		//Step 7
		setPageText(editorDiagramIndex, DIAGRAM_PAGE_NAME);
		setPageText(textViewerIORMIndex, TEXT_IORM_PAGE_NAME);
		setPageText(textViewerCROMIndex, TEXT_CROM_PAGE_NAME);	
		setPageText(editorFeaturesIndex, FEATURE_PAGE_NAME);
		setPageText(statusPageIndex, STATUS_PAGE_OK);
	}
	
	/**
	 * This method catches property changes of the dirty state of the multipage editor.
	 * <p>
	 * The property change is caught to update the status pages name depending on the dirty status after the
	 * property change is triggered.
	 */
	@Override
	protected void handlePropertyChange(int propertyId) {
		super.handlePropertyChange(propertyId);
		if(statusPage != null) {
			if(this.isDirty()) setPageText(statusPageIndex, STATUS_PAGE_UNSAVED_CHANGES);
			else setPageText(statusPageIndex, STATUS_PAGE_OK);
		}	
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
	 * changes the page to the selected one by the user as long the user dont chooses the status page which 
	 * is not intended to be opened. 
	 * @see #statusPage
	 * @param newPageIndex the index of the page to change to
	 */
	@Override
	protected void pageChange(int newPageIndex) {
		if(newPageIndex != statusPageIndex) super.pageChange(newPageIndex);
		else setActivePage(getCurrentPage());	
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
