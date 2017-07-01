package org.framed.iorm.ui.multipage;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.literals.TextLiterals;
import org.framed.iorm.ui.subeditors.FRaMEDDiagramEditor;
import org.framed.iorm.ui.subeditors.FRaMEDFeatureEditor;
import org.framed.iorm.ui.subeditors.FRaMEDTextViewer;

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
						 MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
	
	/**
	 * the message used for the workbench status line if there are unsaved changes 
	 */
	private final String STATUS_MESSAGE_UNSAVED_CHANGES = TextLiterals.STATUS_MESSAGE_UNSAVED_CHANGES;
			
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
	 * the indices if the subeditor of the multipage editor
	 */
	private int editorDiagramIndex,
				textViewerIORMIndex,
				textViewerCROMIndex,
				editorFeaturesIndex;
							 
	/**
	 * the subeditors of the multipage editor of type {@link FRaMEDFeatureEditor}
	 * <p>
	 * the editor that is usd to change the configuration of the role model
	 */
	private FRaMEDFeatureEditor editorFeatures;
	
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
	 * @throws PartInitException if editor input is not of type {@link IFileEditorInput}	
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
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
	 * 		   It also creates the editores and add the pages for the iorm and crom text viewers.<br>  
	 * Step 6: Its set the names of the pages.
	 * Step 7: If the editor input is a {@link IFileEditorInput} set the file name as multipage editor name. If a groups or compartment
	 * 		   types diagram is opened a {@link org.eclipse.graphiti.ui.editor.DiagramEditorInput} is used and the name of the 
	 * 		   multipage editor is set in the {@link org.framed.iorm.ui.graphitifeatures.StepInNewTabFeature}.
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
		CreateContext createContext = new CreateContext();
		createModelFeature.create(createContext);
		//Step 4
		doSave(null);
		//Step 5
		editorFeatures = new FRaMEDFeatureEditor(getEditorInput(), this);
		textViewerIORM = new FRaMEDTextViewer();
		textViewerCROM = new FRaMEDTextViewer();
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
		//Step 7
		if(getEditorInput() instanceof IFileEditorInput) {
			setPartName(getEditorInput().getName());
		}
	}
	
	/**
	 * This method catches property changes of the dirty state of the multipage editor.
	 * <p>
	 * The property change is caught to update the workbench status line depending on the dirty status after the
	 * property change is triggered.
	 */
	@Override
	protected void handlePropertyChange(int propertyId) {
		super.handlePropertyChange(propertyId);
		IActionBars actionBars = getEditorSite().getActionBars();
		if(isDirty()) actionBars.getStatusLineManager().setErrorMessage(STATUS_MESSAGE_UNSAVED_CHANGES);
		else actionBars.getStatusLineManager().setErrorMessage(null);
	}
	
	/**
	 * save method
	 * <p>
	 * This operation calls the save methods of the subeditors and synchronizes the feature configuration
	 * in the feature editor with the one in the role model. This is needed because there can be inconsistencies
	 * after undoing and redoing an feature configuration change.<br>
	 * The check in this operation is needed because the first save of an multipage editor at its creation is done 
	 * before the feature editor even exists.
	 * @param monitor the monitor used for the save activity
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		if(isDirty()) {
			editorDiagram.doSave(monitor);
			editorDiagram.updateSelectedFeatures();
			//check
			if(editorFeatures != null)
				editorFeatures.synchronizeConfigurationEditorAndModelConfiguration();
		}
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
		super.pageChange(newPageIndex);
	}
	

	/**
	 * publishes the operation {@link org.eclipse.ui.part.EditorPart#setPartName} of {@link org.eclipse.ui.part.EditorPart}
	 */
	@Override
	public void setPartName(String newName) {
		super.setPartName(newName);
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
