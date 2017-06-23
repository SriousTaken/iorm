package org.framed.iorm.ui.exceptions;

import org.framed.iorm.ui.literals.TextLiterals;

/**
 * This exceptions is thrown if a inconsistent state between the role models configuration and
 * configuration in the {@link FRaMEDFeatureEditor}.
 * @author Kevin Kassin
 * @serial 
 */
public class FeatureModelInconsistentException extends RuntimeException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the exceptions message gathered from {@link TextLiterals}
	 */
	private static final String FEATUREMODEL_INCONSISTENT_MESSAGE = TextLiterals.FEATUREMODEL_INCONSISTENT_MESSAGE;
	
	/**
	 * Class constructor
	 */
	public FeatureModelInconsistentException() {
		super(FEATUREMODEL_INCONSISTENT_MESSAGE);
	}
}
