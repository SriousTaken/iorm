package org.framed.iorm.model.editor.wizards;

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
import org.framed.iorm.model.editor.literals.IdentifierLiterals;
import org.framed.iorm.model.editor.literals.NameLiterals;

//mostly taken and customized from the class "CreateDiagramWizard.java" in the github repository of Graphiti.
//look there for reference
public class RoleModelWizard extends BasicNewResourceWizard {

	//id literals
	private final String DIAGRAM_TYPE = IdentifierLiterals.DIAGRAM_TYPE,
						 EDITOR_ID = IdentifierLiterals.EDITOR_ID;
	
	//name literals
	private final String WIZARD_PAGE_NAME = NameLiterals.WIZARD_PAGE_NAME,
						 WIZARD_WINDOW_NAME = NameLiterals.WIZARD_WINDOW_NAME;
	
	private RoleModelWizardPage roleModelWizardPage;
	private Diagram diagram;

	@Override
	public void addPages() {
		super.addPages();
		roleModelWizardPage = new RoleModelWizardPage(WIZARD_PAGE_NAME); 
		addPage(roleModelWizardPage);
	}

	@Override
	public boolean canFinish() {
		return super.canFinish();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setWindowTitle(WIZARD_WINDOW_NAME);
	}

	@Override
	public boolean performFinish() {
		final String diagramName = roleModelWizardPage.getText();
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

	public Diagram getDiagram() {
		return diagram;
	}
}	