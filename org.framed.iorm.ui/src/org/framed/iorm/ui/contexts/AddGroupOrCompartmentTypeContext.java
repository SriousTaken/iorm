package org.framed.iorm.ui.contexts;

import org.eclipse.graphiti.features.context.impl.AddContext;
import org.framed.iorm.model.Model;

public class AddGroupOrCompartmentTypeContext extends AddContext {

	private Model modelToLink;
	
	public void setModelToLink(Model modelToLink) {
		this.modelToLink = modelToLink;
	}
	
	public Model getModelToLink() {
		return modelToLink;
	}
}
