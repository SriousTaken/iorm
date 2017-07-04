package org.framed.iorm.ui.exceptions;

import org.framed.iorm.ui.literals.TextLiterals;
import org.framed.iorm.ui.util.GeneralUtil; //*import for javadoc link

/**
 * This exception is thrown if the {@link MultipageEditor} can not gather the diagram for
 * an {@link IEditorInput} performing the operations {@link GeneralUtil#addPagesWithDiagramEditorInput}
 * and {@link GeneralUtil#getMainDiagramForIFileEditorInput}.
 * is of a not supported type.
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
	private static final String MULTIPAGE_EDITOR_ERROR_NO_DIAGRAM_FOUND = TextLiterals.MULTIPAGE_EDITOR_ERROR_NO_DIAGRAM_FOUND;

	/**
	 * Class constructor
	 */
	public NoDiagramFoundException() {
		super(MULTIPAGE_EDITOR_ERROR_NO_DIAGRAM_FOUND);
	}
}
