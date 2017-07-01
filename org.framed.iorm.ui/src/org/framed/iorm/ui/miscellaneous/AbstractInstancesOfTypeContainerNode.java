package org.framed.iorm.ui.miscellaneous;

import org.eclipse.core.resources.IProject;

/**
 * This class represents a container node in a compartment role object model project. This could be an package for example.
 * <p>
 * The code in this class is mostly taken and customized from the classes "AbstractInstancesOfTypeContainerNode.java"
 * and "AbstractContainerNode" by Graphiti. You can find this classes in the github repository of Graphiti. Also 
 * look there for reference too.
 */
public abstract class AbstractInstancesOfTypeContainerNode {

	/**
	 * the parent object of the container node
	 */
	private Object parent;
	
	/**
	 * the role model project the container node is contained in
	 */
	IProject project;
	
	/**
	 * Class constructor
	 * @param parent the parent object of the container node
	 * @param project the role model project the container node is contained in
	 */
	public AbstractInstancesOfTypeContainerNode(Object parent, IProject project) {
		this.parent = parent;
		this.project = project;
	}
		
	/**
	 * get method for the parent object of the container node
	 * @return the class variable {@link #parent}
	 */
	public Object getParent() {
		return parent;
	}
	
	/**
	 * get method the role model project the container node is contained in
	 * @return the class variable {@link #project}
	 */
	public IProject getProject() {
		return project;
	}
}
