package org.framed.iorm.ui.exceptions;

import org.framed.iorm.ui.literals.TextLiterals;

/**
 * This exception is thrown if the feature model cant be read when the 
 * {@link org.framed.iorm.ui.subeditors.FRaMEDFeatureEditor} is created.
 * @author Kevin Kassin
 */
public class FeatureModelNotReadableException extends RuntimeException {
	
	/**
	 * serial
	 */
	private static final long serialVersionUID = 8396630214250470131L;
	
	/**
	 * the exceptions message gathered from {@link TextLiterals}
	 */
	private static final String FEATUREMODEL_NOT_READABLE_MESSAGE = TextLiterals.FEATUREMODEL_NOT_READABLE_MESSAGE;
	
	/**
	 * Class constructor
	 */
	public FeatureModelNotReadableException() {
		super(FEATUREMODEL_NOT_READABLE_MESSAGE);
	}
}
