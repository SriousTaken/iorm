package org.framed.iorm.ui.exceptions;

import org.framed.iorm.ui.literals.TextLiterals;

/**
 * This exceptions is thrown if a inconsistent state between the role models configuration and
 * configuration in the {@link org.framed.iorm.ui.subeditors.FRaMEDFeatureEditor}.
 * @author Kevin Kassin
 */
public class ConfigurationInconsistentException extends RuntimeException {

	/**
	 * serial 
	 */
	private static final long serialVersionUID = 6740010032531900708L;
	
	/**
	 * the exceptions message gathered from {@link TextLiterals}
	 */
	private static final String CONFIGURATION_INCONSISTENT_MESSAGE = TextLiterals.CONFIGURATION_INCONSISTENT_MESSAGE;
	
	/**
	 * Class constructor
	 */
	public ConfigurationInconsistentException() {
		super(CONFIGURATION_INCONSISTENT_MESSAGE);
	}
}
