package org.framed.iorm.ui.exceptions;

import org.framed.iorm.ui.literals.TextLiterals;

/**
 * This exception is thrown if there is no linked shape or connection can be found for a business object
 * of the type {@link org.framed.iorm.model.ModelElement} when using the {@link StepInFeature} or
 * the {@link StepOutFeature}.
 * @author Kevin Kassin
 */
public class NoShapeOrConnectionForModelElementFoundException extends RuntimeException {
	
	/**
	 * serial
	 */
	private static final long serialVersionUID = 1612012153621347816L;
	
	/**
	 * the exceptions message gathered from {@link TextLiterals}
	 */
	private static final String NO_SHAPE_OR_CONNECTION_FOR_MODELELEMENT_FOUND_MESSAGE = TextLiterals.NO_SHAPE_OR_CONNECTION_FOR_MODELELEMENT_FOUND_MESSAGE;
	
	/**
	 * Class constructor
	 */
	public NoShapeOrConnectionForModelElementFoundException() {
		super(NO_SHAPE_OR_CONNECTION_FOR_MODELELEMENT_FOUND_MESSAGE);
	}
}
