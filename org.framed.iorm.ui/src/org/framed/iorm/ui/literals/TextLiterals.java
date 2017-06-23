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
	 * (1) the direct editing message for attributes or<br>
	 * (2) the direct editing message for operations or<br>
	 * (3) the direct editing message for data types or<br>
	 * (4) the direct editing message for natural types or<br>
	 */
	public static final String DIRECTEDITING_ATTRIBUTES = "An attributes name has the form <name>:<type>",
							   DIRECTEDITING_OPERATIONS = "An operations name has the form <name>(<parameters>):<type>",
							   DIRECTEDITING_DATATYPE = "A datatypes name cant be empty and cant contains spaces. Numbers are allowed but as first symbol.",
							   DIRECTEDITING_NATURALTYPE = "A natural types name cant be empty and cant contains spaces. Numbers are allowed but as first symbol.";
	
	/**
	 * messages used in exceptions
	 * <p>
	 * the message for the {@link FeatureModelInconsistentException} 
	 */
	public static final String FEATUREMODEL_INCONSISTENT_MESSAGE = "The feature models used in edited file and the editor are inconsistent!";
	
	/**
	 * messages used in the Eclipse wizards
	 * <p>
	 * can be:<br>
	 * (1) the message for the {@link RoleModelWizardPage} or<br>
	 * (2) the message for the user if his input for the diagrams name is invalid
	 */
	public static final String WIZARD_PAGE_DESC = "Enter the name of the Role Model",
							   WIZARD_MESSAGE_INVALID_INPUT = "A diagrams name cant be empty and cant contains spaces. Numbers are allowed but as first symbol.";
}