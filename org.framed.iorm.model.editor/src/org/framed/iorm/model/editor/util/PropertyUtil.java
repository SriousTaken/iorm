package org.framed.iorm.model.editor.util;

import org.eclipse.graphiti.mm.PropertyContainer;
import org.eclipse.graphiti.services.Graphiti;

public class PropertyUtil {
	
	//Shape IDs
	public static final String KEY_SHAPE_ID = "shape-id";
	
	public static final void setShape_IdValue(PropertyContainer container, String value) {
		Graphiti.getPeService().setPropertyValue(container, KEY_SHAPE_ID, value);
	}
	
	public static final boolean isShape_IdValue(PropertyContainer container, String value) {
		return (Graphiti.getPeService().getPropertyValue(container, KEY_SHAPE_ID).equals(value));
	}
	
	
	
}
