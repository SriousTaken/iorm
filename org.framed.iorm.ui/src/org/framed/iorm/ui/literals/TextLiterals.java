package org.framed.iorm.ui.literals;

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
	 * messages used in exceptions
	 * <p>
	 * can be:<br>
	 * (1) the message for the {@link org.framed.iorm.ui.exceptions.ConfigurationInconsistentException} or<br>
	 * (2) the message for the {@link org.framed.iorm.ui.exceptions.FeatureModelNotReadableException} or<br>
	 * (3) the message for the {@link org.framed.iorm.ui.exceptions.NoShapeOrConnectionForModelElementFoundException}
	 */
	public static final String CONFIGURATION_INCONSISTENT_MESSAGE = "The configurations used in edited file and the feature editor are inconsistent!",
						       FEATUREMODEL_NOT_READABLE_MESSAGE = "The feature model could not be read!",
						       NO_SHAPE_OR_CONNECTION_FOR_MODELELEMENT_FOUND_MESSAGE = "There is no linked shape for a business object of the diagram.";
	
	/**
	 * messages used in the Eclipse wizards
	 * <p>
	 * can be:<br>
	 * (1) the message for the {@link org.framed.iorm.ui.wizards.RoleModelWizardPage} or<br>
	 * (2) the message for the user if his input for the diagrams name is invalid or<br>
	 * (3) the title of the error message if no project is selected at role model creation or<br>
	 * (4) the error message if no project is selected at role model creation
	 */
	public static final String WIZARD_PAGE_DESC = "Enter the name of the Role Model",
							   WIZARD_MESSAGE_INVALID_INPUT = "A diagrams name cant be empty and cant contains spaces. Numbers are allowed but as first symbol.",
							   WIZARD_ERROR_NO_PROJECT_TITLE = "No Project Selected",
							   WIZARD_ERROR_NO_PROJECT_TEXT = "Please choose a CROM project to create the role model in!";   
	
	/**
	 * messages used for the workbench status line
	 * <p>
	 * the message of the workbench status line if there are unsaved changes
	 */
	public static final String STATUS_MESSAGE_UNSAVED_CHANGES = "Unsaved changes - the pages are out of sync!";
	
	/**
	 * the prefix used for multipage editor names
	 * <p>
	 * can be:<br>
	 * (1) the prefix for multipage editor name of a multipage editor that shows a groups diagram or<br>
	 * (2) the prefix for multipage editor name of a multipage editor that shows a compartment types diagram
	 */
	public static final String MULTIPAGE_EDITOR_NAME_GROUP_DIAGRAM = "Group",
			 				   MULTIPAGE_EDITOR_NAME_COMPARTMENTTYPE_DIAGRAM = "Compartment Type";
}
