package miscellaneous;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

//mix von graphit classes AbstractInstancesOfTypeContainerNode, AbstractContainerNode
public abstract class AbstractInstancesOfTypeContainerNode {

	private Object parent;
	IProject project;
	
	protected abstract String getContainerName();
	
	public AbstractInstancesOfTypeContainerNode(Object parent, IProject project) {
		this.parent = parent;
		this.project = project;
	}
	
	public String getText() {
		String ret = getContainerName();
		return ret;
	}
	
	public Image getImage() {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
	
	
	public Object getParent() {
		return parent;
	}
	
	public boolean hasChildren() {
		return true;
	}

	public IProject getProject() {
		return project;
	}
}
