package org.framed.iorm.ui.literals;

import org.framed.iorm.ui.exceptions.FeatureModelNotReadableException; //*import for javadoc link
import org.framed.iorm.ui.wizards.RoleModelWizardPage; //*import for javadoc link
import org.framed.iorm.ui.multipage.MultipageEditor; //*import for javadoc link

/**
 * This class saves severals static Strings used as messages for the user.
 * @author Kevin Kassin
 */
public class TextLiterals {

	/**
	 * messages used as direct editing tips
	 * <p>
	 * can be:<br>
	 * (1) the direct editing message for attribute names or<br>
	 * (2) the direct editing message for operation names or<br>
	 * (3) the direct editing message for data type names or<br>
	 * (4) the direct editing message for natural type names or<br>
	 * (5) the direct editing message for group names
	 */
	public static final String DIRECTEDITING_ATTRIBUTES = "An attributes name has the form <name>:<type>",
							   DIRECTEDITING_OPERATIONS = "An operations name has the form <name>(<parameters>):<type>",
							   DIRECTEDITING_DATATYPE = "A datatypes name cant be empty and cant contains spaces. Numbers are allowed but as first symbol.",
							   DIRECTEDITING_NATURALTYPE = "A natural types name cant be empty and cant contains spaces. Numbers are allowed but as first symbol.",
							   DIRECTEDITING_GROUP = "A groups name cant be empty and cant contains spaces. Numbers are allowed but as first symbol.";
	
	/**
	 * messages used in the feature sub editor
	 * <p>
	 * the message for the {@link FeatureModelNotReadableException}
	 */
	public static final String FEATUREMODEL_NOT_READABLE_MESSAGE = "The feature model could not be read!";
	
	/**
	 * messages used in the Eclipse wizards
	 * <p>
	 * can be:<br>
	 * (1) the message for the {@link RoleModelWizardPage} or<br>
	 * (2) the message for the user if his input for the diagrams name is invalid or<br>
	 * (3) the title of the error message if no project is selected at role model creation or<br>
	 * (4) the error message if no project is selected at role model creation
	 */
	public static final String WIZARD_PAGE_DESC = "Enter the name of the Role Model",
							   WIZARD_MESSAGE_INVALID_INPUT = "A diagrams name cant be empty and cant contains spaces. Numbers are allowed but as first symbol.",
							   WIZARD_ERROR_NO_PROJECT_TITLE = "No Project Selected",
							   WIZARD_ERROR_NO_PROJECT_TEXT = "Please choose a CROM project to create the role model in!",
							   WIZARD_ERROR_DEADLOCK_DANGER = "Saving called from within a command (likely produces a deadlock)",
							   WIZARD_ERROR_SAVING_FAILED = "Saving failed";
	
	/**
	 * messages used in the multipage editor
	 * <p>
	 * can be:<br>
	 * (1) the error message if the editor input can not be used for the {@link MultipageEditor} or<br>
	 * (2) the error message if the {@link MultipageEditor} can not find the diagram for a DiagramEditorInput or<br>
	 * (3) the error message if the {@link MultipageEditor} could not gather a the file imput to refresh a file or<br>
	 * (4) the message of the workbench status line if there are unsaved changes
	 */
	public static final String MULTIPAGE_EDITOR_ERROR_NO_VALID_EDITOR_INPUT = "The editor input of the multipage editor is not of a valid type!",
							   MULTIPAGE_EDITOR_ERROR_NO_DIAGRAM_FOUND = "There was no diagram found for the the diagram editor input!",
							   MUTLIPAGE_EDITOR_ERROR_NULLPOINTER_ON_FILE_EDITOR_INPUT = "The file editor input used to refresh the file is null!",
							   STATUS_MESSAGE_UNSAVED_CHANGES = "Unsaved changes - the pages are out of sync!";
}
