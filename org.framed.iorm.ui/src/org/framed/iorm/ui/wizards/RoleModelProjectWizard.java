package org.framed.iorm.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class RoleModelProjectWizard extends BasicNewProjectResourceWizard {

	@Override
	public boolean performFinish() {
		if (!super.performFinish()) return false;
		IProject newProject = getNewProject();
		try {
			IProjectDescription description = newProject.getDescription();
			newProject.setDescription(description, null);
		} catch (CoreException e) { return false; }
		return true;
	}
}
