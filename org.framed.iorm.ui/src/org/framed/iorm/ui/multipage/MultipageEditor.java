package org.framed.iorm.ui.multipage;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.framed.iorm.ui.exceptions.InvalidTypeOfEditorInputException;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.literals.TextLiterals;
import org.framed.iorm.ui.subeditors.FRaMEDDiagramEditor;
import org.framed.iorm.ui.subeditors.FRaMEDFeatureEditor;
import org.framed.iorm.ui.subeditors.FRaMEDTextViewer;
import org.framed.iorm.ui.util.GeneralUtil;
import org.framed.iorm.ui.providers.DiagramTypeProvider; //*import for javadoc link
import org.framed.iorm.ui.wizards.RoleModelWizard; //*import for javadoc link

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
	 * the identifier of the {@link DiagramTypeProvider} which is needed to instantiate an {@link DiagramEditorInput}
	 */
	private final String DIAGRAM_PROVIDER_ID = IdentifierLiterals.DIAGRAM_PROVIDER_ID;
	
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
	 * the prefix of the multipage editor name if an groups diagram is opened in the multipage editor gathered
	 * from {@link NameLiterals} 
	 */
	private final String MULTIPAGE_EDITOR_NAME_GROUP_DIAGRAM = NameLiterals.MULTIPAGE_EDITOR_NAME_GROUP_DIAGRAM;
	
	/**
	 * text literas used for messages
	 * <p>
	 * can be:<br>
	 * (1) the message used for the workbench status line if there are unsaved changes gathered from {@link TextLiterals} or<br>
	 * (2) the message used for a null pointer exceptions on a file editor input
	 */
	private final String STATUS_MESSAGE_UNSAVED_CHANGES = TextLiterals.STATUS_MESSAGE_UNSAVED_CHANGES,
						 MUTLIPAGE_EDITOR_ERROR_NULLPOINTER_ON_FILE_EDITOR_INPUT = TextLiterals.MUTLIPAGE_EDITOR_ERROR_NULLPOINTER_ON_FILE_EDITOR_INPUT;
			
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
	 * delegates the task to add pages to the multipage editor depending on the type of
	 * editor input
	 * <p>
	 * (1) If the editor input is of type {@link IFileEditorInput} it calls {@link #addPageWithIFileEditorInput}
	 * 	   and sets the multipage editors name.<br>
	 * (2) If the editor input is of type {@link DiagramEditorInput} it calls {@link #addPagesWithDiagramEditorInput},
	 *     gets the referenced diagram of the editor input to set the multipage editors name.<br>
	 * (3) else throw {@link InvalidTypeOfEditorInputException}
	 * @throws InvalidTypeOfEditorInputException
	 */
	@Override
	protected void addPages() {	
		if(getEditorInput() instanceof IFileEditorInput) {
			addPageWithIFileEditorInput();
			setPartName(getEditorInput().getName());
		} else if(getEditorInput() instanceof DiagramEditorInput) {
			DiagramEditorInput diagramEditorInput = (DiagramEditorInput) getEditorInput();
			addPagesWithDiagramEditorInput(diagramEditorInput, null);
			Resource resource = GeneralUtil.getResourceFromEditorInput(diagramEditorInput);	
			Diagram diagram = GeneralUtil.getDiagramForResourceOfDiagramEditorInput(resource);
			setPartName(MULTIPAGE_EDITOR_NAME_GROUP_DIAGRAM + " " + diagram.getName());
		} else {
			throw new InvalidTypeOfEditorInputException();
		}
	}
	
	/**
	 * This method add pages to the multipage editor if the editor input is of type {@link IFileEditorInput} 
	 * by creating an {@link DiagramEditorInput} with the <em>main diagram</em> of the CROM Diagram. Then it call the
	 * operation {@link #addPagesWithDiagramEditorInput} to add the pages using the created {@link DiagramEditorInput}.
	 * <p>
	 * If its not clear what <em>main diagram</em> means, see {@link RoleModelWizard#createEmfFileForDiagram} for reference.
	 * <p>
	 * If the <em>main Diagram</em> is null in this operation there is no exception thrown, since this already happens in 
	 * {@link GeneralUtil#getMainDiagramForIEditorInput(IEditorInput)}.
	 * @see RoleModelWizard#createEmfFileForDiagram
	 */
	private void addPageWithIFileEditorInput() {
		Diagram mainDiagram = GeneralUtil.getMainDiagramForIEditorInput(getEditorInput());
		if(mainDiagram != null) {
			DiagramEditorInput diagramEditorInput = DiagramEditorInput.createEditorInput(mainDiagram, DIAGRAM_PROVIDER_ID);
			addPagesWithDiagramEditorInput(diagramEditorInput, (IFileEditorInput) getEditorInput());
	}	}
			
	/**
	 * This method add pages to the multipage editor if the editor input is of type {@link DiagramEditor} 
	 * using the following steps:
	 * <p>
	 * Step 1: It gets the resource of the editor input and uses this resource to get the groups or compartment
	 * 		   types diagram. It uses the operation {@link getDiagramForResourceOfDiagramEditorInput} for the second
	 * 		   part of this step.<br>
	 * Step 2: It creates an IFileEditorInput for the text viewers of the multipage editor using the resource and
	 * 		   the operation {@link #getIFileEditorInputForResource}.<br>
	 * Step 3: It creates the diagram subeditor and adds the diagram page. The pages use the subeditors and the editor 
	 * 		   input.<br>
	 * Step 4 : It creates a linked root model for the <em>main diagram</em> if it does not already have on linked. If so it
	 * 			also saves the role model file.<br>
	 * Step 5: It creates the feature editor and adds the page. To do that the created root model is needed.
	 * 		   It also creates the editores and add the pages for the iorm and crom text viewers.<br>  
	 * Step 6: Its set the names of the pages.
	 * <p>
	 * If its not clear what <em>main diagram</em> means, see {@link RoleModelWizard#createEmfFileForDiagram} for reference.<br>
	 * @see RoleModelWizard#createEmfFileForDiagram
	 */
	private void addPagesWithDiagramEditorInput(DiagramEditorInput diagramEditorInput, IFileEditorInput fileEditorInput) {
		//Step 1
		Resource resource = GeneralUtil.getResourceFromEditorInput(diagramEditorInput);	
		//Step 2
		if(fileEditorInput == null) 
			fileEditorInput = getIFileEditorInputForResource(resource);
		//Step 3
		editorDiagram = new FRaMEDDiagramEditor();
		try { 
			editorDiagramIndex = addPage(editorDiagram, diagramEditorInput);		
		} catch (PartInitException e) { e.printStackTrace(); }
		//Step 4 
		ICreateFeature createModelFeature = null;
		ICreateFeature[] createFeatures = editorDiagram.getDiagramTypeProvider().getFeatureProvider().getCreateFeatures();
		for(int i = 0; i<createFeatures.length; i++) {
			if(createFeatures[i].getCreateName().equals(MODEL_FEATURE_NAME)) 
				createModelFeature = createFeatures[i];
		}
		if(createModelFeature != null) {
			CreateContext createContext = new CreateContext();
			if(createModelFeature.canCreate(createContext)) createModelFeature.create(createContext);
			doSave(new NullProgressMonitor());
		}	
		//Step 4
		editorFeatures = new FRaMEDFeatureEditor(diagramEditorInput, this);
		textViewerIORM = new FRaMEDTextViewer();
		textViewerCROM = new FRaMEDTextViewer();
		try {
			editorFeaturesIndex = addPage(editorFeatures, diagramEditorInput);
			textViewerIORMIndex = addPage(textViewerIORM, fileEditorInput);
			textViewerCROMIndex = addPage(textViewerCROM, fileEditorInput);
		} catch (PartInitException e) { e.printStackTrace(); }
		//Step 5
		setPageText(editorDiagramIndex, DIAGRAM_PAGE_NAME);
		setPageText(textViewerIORMIndex, TEXT_IORM_PAGE_NAME);
		setPageText(textViewerCROMIndex, TEXT_CROM_PAGE_NAME);	
		setPageText(editorFeaturesIndex, FEATURE_PAGE_NAME);
	}
	
	/**
	 * generates an {@link IFileEditorInput} for a given resource
	 * @param resource the resource to create the editor input for
	 * @return the generated editor input
	 */
	private IFileEditorInput getIFileEditorInputForResource(Resource resource) {
		if (resource.getURI().isPlatformResource()) {
			String platformString = resource.getURI().toPlatformString(true);
			IFile iFile = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
			return new FileEditorInput(iFile);
		}
		return null;
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
			refreshFile();
		}
	}
	
	/**
	 * refreshes the file which is edited by the multipage editor
	 * <p>
	 * This is needed since the text files use different editor inputs from diagram and the feature editor.
	 * Therefore Graphiti does not synchronize them automatically.
	 * @throws NullPointerException if there cant be fetched a {@link IFileEditorInput}
	 */
	public void refreshFile() throws NullPointerException {
		IFileEditorInput fileEditorInput = null;
		if(getEditorInput() instanceof DiagramEditorInput) {
			Resource resource = GeneralUtil.getResourceFromEditorInput(getEditorInput());	
			fileEditorInput = getIFileEditorInputForResource(resource);
		}
		if(getEditorInput() instanceof IFileEditorInput) { 
			fileEditorInput = (IFileEditorInput) getEditorInput();
		}
		if(fileEditorInput != null) {
			try {
				fileEditorInput.getFile().refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
			} catch (CoreException e) { e.printStackTrace(); }
		} else throw new NullPointerException(MUTLIPAGE_EDITOR_ERROR_NULLPOINTER_ON_FILE_EDITOR_INPUT);
	}
		
	/**
	 * sets the selected page as active page
	 * <p>
	 * changes the page to the selected one by the user as long the user don't chooses the status page which 
	 * is not intended to be opened. 
	 * @see #statusPage
	 * @param newPageIndex the index of the page to change to
	 */
	@Override
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
