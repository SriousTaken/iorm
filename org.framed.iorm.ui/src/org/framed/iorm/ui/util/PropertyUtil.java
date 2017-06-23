package org.framed.iorm.ui.util;

import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.services.Graphiti;
import org.framed.iorm.ui.literals.IdentifierLiterals;

/**
 * This class delivers operations to change and access specific properties graphiti model elements.
 * <p>
 * It can change and access the property "shape-id" for {@link org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm}s. 
 * @author Kevin Kassin
 */
public class PropertyUtil {
	
	/**
	 * the key to identify the property shape id gathered from {@link IdentifierLiterals}
	 */
	public static final String KEY_SHAPE_ID = IdentifierLiterals.KEY_SHAPE_ID;
	
	/**
	 * set the property shape id for a given graphic algorithm
	 * @param graphicAlgorithm the graphic algorithm to set the property for
	 * @param value the new value of the property
	 */
	public static final void setShape_IdValue(GraphicsAlgorithm graphicAlgorithm, String value) {
		Graphiti.getPeService().setPropertyValue(graphicAlgorithm, KEY_SHAPE_ID, value);
	}
	
	/**
	 * checks if the property shape id for a given graphic algorithm equals the given value
	 * @param graphicAlgorithm the graphic algorithm to check the property for
	 * @param value the value to check the property against
	 * @return boolean if the property shape equals the given value
	 */
	public static final boolean isShape_IdValue(GraphicsAlgorithm graphicAlgorithm, String value) {
		return (Graphiti.getPeService().getPropertyValue(graphicAlgorithm, KEY_SHAPE_ID).equals(value));
	}
}
