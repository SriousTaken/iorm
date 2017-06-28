package org.framed.iorm.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.examples.common.FileService;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.literals.TextLiterals;

import miscellaneous.AbstractInstancesOfTypeContainerNode;

/**
 * This class creates an Eclipse wizard to create a role model.
 * <p>
 * The code in this class is mostly taken and customized from the class "CreateDiagramWizard.java" by Graphiti. 
 * You can find this class in the github repository of Graphiti. Also look there for reference too.
 */
public class RoleModelWizard extends BasicNewResourceWizard {

	/**
	 * identifier literals for the diagram type and the multipage editor id gathered from {@link IdentifierLiterals}
	 */
	private final String DIAGRAM_TYPE = IdentifierLiterals.DIAGRAM_TYPE,
						 EDITOR_ID = IdentifierLiterals.EDITOR_ID;
	
	/**
	 * name literals for the file extension of the new diagram, the wizard page and window gathered from {@link NameLiterals}
	 */
	private final String FILE_EXTENSION_FOR_DIAGRAMS = NameLiterals.FILE_EXTENSION_FOR_DIAGRAMS,
						 WIZARD_PAGE_NAME = NameLiterals.WIZARD_PAGE_NAME,
						 WIZARD_WINDOW_NAME = NameLiterals.WIZARD_WINDOW_NAME;
	
	/**
	 * the text literals for the wizard error if there is no project selected
	 */
	private final String WIZARD_ERROR_NO_PROJECT_TITLE = TextLiterals.WIZARD_ERROR_NO_PROJECT_TITLE,
						 WIZARD_ERROR_NO_PROJECT_TEXT = TextLiterals.WIZARD_ERROR_NO_PROJECT_TEXT;
	
	/**
	 * the wizard page used by this wizard
	 */
	private RoleModelWizardPage roleModelWizardPage;
	
	/**
	 * creates and adds and {@link RoleModelWizardPage} to the wizard
	 */
	@Override
	public void addPages() {
		super.addPages();
		roleModelWizardPage = new RoleModelWizardPage(WIZARD_PAGE_NAME); 
		addPage(roleModelWizardPage);
	}

	/**
	 * initalize method
	 * <p>
	 * sets the windows title
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setWindowTitle(WIZARD_WINDOW_NAME);
	}

	/**
	 * performs the creation of a role model using the following steps:
	 * <p>
	 * Step 1: ask for the diagrams name via the wizard page
	 * Step 2: checks if project is selected into which the diagram can be created
	 * Step 3: create a diagram, write it to file and open it
	 */
	@Override
	public boolean performFinish() {
		//Step 1
		final String diagramName = roleModelWizardPage.getText();
		//Step 2
		IProject project = null;
		Object element = getSelection().getFirstElement();
		if (element instanceof IProject) {
			project = (IProject) element;
		} else if (element instanceof AbstractInstancesOfTypeContainerNode) {
			AbstractInstancesOfTypeContainerNode aiocn = (AbstractInstancesOfTypeContainerNode) element;
			project = aiocn.getProject();
		} 
		if (project == null || !project.isAccessible()) {
			MessageDialog.openError(getShell(), WIZARD_ERROR_NO_PROJECT_TITLE, WIZARD_ERROR_NO_PROJECT_TEXT);
			return false;
		}
		//Step 3
		Diagram diagram = Graphiti.getPeCreateService().createDiagram(DIAGRAM_TYPE, diagramName, true);
		IFile diagramFile = project.getFile(diagramName + FILE_EXTENSION_FOR_DIAGRAMS);
		URI uri = URI.createPlatformResourceURI(diagramFile.getFullPath().toString(), true);
		FileService.createEmfFileForDiagram(uri, diagram);
		IFileEditorInput iFileEditorInput = new FileEditorInput(diagramFile);
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(iFileEditorInput, EDITOR_ID);
		} catch (PartInitException e) { e.printStackTrace(); return false; }
		return true;
	}
}	