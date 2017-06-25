package org.framed.iorm.ui.wizards;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * This class creates an Eclipse wizard is used to create a role model project
 * <p>
 * The code in this class is mostly taken and customized from the class "CreateSampleProjectWizard.java" by Graphiti. 
 * You can find this class in the github repository of Graphiti. Also look there for reference too.
 */
public class RoleModelProjectWizard extends BasicNewProjectResourceWizard {

	/**
	 * evaluates if the wizard can be finished and the project can be created
	 * <p>
	 * also creates the project and an empty text file in it
	 */
	@Override
	public boolean performFinish() {
		if (!super.performFinish()) return false;
		IProject newProject = getNewProject();
		IProjectDescription description;
		try {
			description = newProject.getDescription();
			newProject.setDescription(description, null);
		} catch (CoreException e) { e.printStackTrace();}
		createEmptyTextFile(newProject);
		return true;
	}
	
	/**
	 * creates an empty text file in the created project
 	 * <p>
 	 * The empty text file is used for the status page that shows the status of the multipage editor.
	 * @param newProject the created project
	 */
	private void createEmptyTextFile(IProject newProject) {
		ResourceSet set = new ResourceSetImpl();
		URI uri = URI.createPlatformResourceURI(newProject.getFolder("text").getFile("empty.txt").getFullPath().toString(), true);
		Resource resource = set.createResource(uri);
		try {
			resource.save(null);
		} catch (IOException e) { e.printStackTrace(); }
	}
}
