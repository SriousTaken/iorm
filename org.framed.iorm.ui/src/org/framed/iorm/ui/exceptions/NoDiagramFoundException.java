package org.framed.iorm.ui.exceptions;

import org.framed.iorm.ui.literals.TextLiterals;
import org.framed.iorm.ui.util.GeneralUtil; //*import for javadoc link

/**
 * 
 * This exception is thrown if not diagram can be gathered for in a operation that is supposed to find one.
 * <p>
 * This exceptions heavily used for methods in {@link GeneralUtil}.
 * @author Kevin Kassin
 */
public class NoDiagramFoundException extends RuntimeException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -8634710412326399635L;
	
	/**
	 * the exceptions message gathered from {@link TextLiterals}
	 */
	private static final String ERROR_NO_DIAGRAM_FOUND = TextLiterals.ERROR_NO_DIAGRAM_FOUND;

	/**
	 * Class constructor
	 */
	public NoDiagramFoundException() {
		super(ERROR_NO_DIAGRAM_FOUND);
	}
}
