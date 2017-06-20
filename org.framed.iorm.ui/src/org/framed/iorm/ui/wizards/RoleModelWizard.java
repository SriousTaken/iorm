package org.framed.iorm.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.examples.common.ExamplesCommonPlugin;
import org.eclipse.graphiti.examples.common.FileService;
import org.eclipse.graphiti.examples.common.Messages;
import org.eclipse.graphiti.examples.common.navigator.nodes.base.AbstractInstancesOfTypeContainerNode;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.NameLiterals;

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
	 * name literals for the wizard page and window gathered from {@link NameLiterals}
	 */
	private final String WIZARD_PAGE_NAME = NameLiterals.WIZARD_PAGE_NAME,
						 WIZARD_WINDOW_NAME = NameLiterals.WIZARD_WINDOW_NAME;
	
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
	 * performs the creation of a rolemodel using the following steps:
	 * <p>
	 * Step 1: ask for the diagrams name via the wizard page
	 * Step 2: checks if project is selected into which the diagram can be created
	 * Step 3: create a diagram, write it to file and open it
	 * @exception PartInitException
	 */
	@Override
	public boolean performFinish() {
		//Step 1
		final String diagramName = roleModelWizardPage.getText();
		//Step 2
		IProject project = null;
		IFolder diagramFolder = null;
		Object element = getSelection().getFirstElement();
		if (element instanceof IProject) {
			project = (IProject) element;
		} else if (element instanceof AbstractInstancesOfTypeContainerNode) {
			AbstractInstancesOfTypeContainerNode aiocn = (AbstractInstancesOfTypeContainerNode) element;
			project = aiocn.getProject();
		} else if (element instanceof IFolder) {
			diagramFolder = (IFolder) element;
			project = diagramFolder.getProject();
		}
		if (project == null || !project.isAccessible()) {
			String error = Messages.CreateDiagramWizard_NoProjectFoundError;
			IStatus status = new Status(IStatus.ERROR, ExamplesCommonPlugin.getID(), error);
			ErrorDialog.openError(getShell(), Messages.CreateDiagramWizard_NoProjectFoundErrorTitle, null, status);
			return false;
		}
		//Step 3
		Diagram diagram = Graphiti.getPeCreateService().createDiagram(DIAGRAM_TYPE, diagramName, true);
		if (diagramFolder == null) {
			diagramFolder = project.getFolder("src/diagrams/");
		}
		IFile diagramFile = diagramFolder.getFile(diagramName + ".diagram");
		URI uri = URI.createPlatformResourceURI(diagramFile.getFullPath().toString(), true);
		FileService.createEmfFileForDiagram(uri, diagram);
		IFileEditorInput iFileEditorInput = new FileEditorInput(diagramFile);
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(iFileEditorInput, EDITOR_ID);
		} catch (PartInitException e) {
			String error = Messages.CreateDiagramWizard_OpeningEditorError;
			IStatus status = new Status(IStatus.ERROR, ExamplesCommonPlugin.getID(), error, e);
			ErrorDialog.openError(getShell(), Messages.CreateDiagramWizard_ErrorOccurredTitle, null, status);
			return false;
		}
		return true;
	}
}	