package org.framed.iorm.ui.literals;

import org.framed.iorm.ui.commands.ConfigurationEditorChangeCommand; //*import for javadoc link
import org.framed.iorm.ui.wizards.RoleModelWizard;

/**
 * This class saves severals static Strings used as names.
 * @author Kevin Kassin
 */
public class NameLiterals {
	
	/**
	 * the file extension of the diagram type used in this editor 
	 */
	public static final String FILE_EXTENSION_FOR_DIAGRAMS = ".crom_diagram";
	
	/**
	 * name literals used in the role model wizard or role model project wizard
	 * <p>
	 * can be:<br>
	 * (1) the name of the wizard or<br>
	 * (2) the name of the role model wizard page or<br>
	 * (3) the default name of a new diagram or<br>
	 * (4) the name of of the container diagram
	 * <p>
	 *  If its not clear what <em>container diagram</em> means, see {@link RoleModelWizard#createEmfFileForDiagram} for reference.
	 */
	public static final String WIZARD_WINDOW_NAME = "Role Model Wizard", 
							   WIZARD_PAGE_NAME = "Role Model",
							   STANDARD_DIAGRAM_NAME = "newDiagram",
							   CONTAINER_DIAGRAM_NAME = "Container";
							  
	/**
	 * standard names for graphiti shapes
	 * <p>
	 * can be:<br>
	 * (1) the standard name for attributes or<br>
	 * (2) the standard name for operations or<br>
	 * (3) the standard name for naturals types or<br>
	 * (4) the standard name for data types or<b>
	 * (5) the standard name for groups
	 */
	public static final String STANDARD_ATTRIBUTE_NAME = "attribute:type",
							   STANDARD_OPERATION_NAME = "operation():type",
							   STANDARD_NATURALTYPE_NAME = "naturalType",
							   STANDARD_DATATYPE_NAME = "dataType",
							   STANDART_GROUP_NAME = "group";

	/**
	 * name literals for commands
	 * <p>
	 * the name for the {@link ConfigurationEditorChangeCommand}
	 */
	public static final String CONFIGURATION_CHANGE_COMMAND_NAME = "Configuration Change";
	
	/**
	 * name literals used in the create features
	 * <p>
	 * can be:<br>
	 * (1) the name of the attribute create feature or<br>
	 * (2) the name of the operation create feature or<br>
	 * (3) the name of the common pattern for attributes and operations or<br>
	 * (4) the name of the model create feature or<br>
	 * (5) the name of the natural type create feature or<br>
	 * (6) the name of the data type create feature or<br>
	 * (7) the name of the inheritance create feature or<br>
	 * (8) the name of the change configuration custom feature or<br>
	 * (9) the name of the group create feature or<br>
	 * (10) the name of the step in custom feature
	 */
	public static final String ATTRIBUTE_FEATURE_NAME = "Attribute",
							   OPERATION_FEATURE_NAME = "Operation",
							   ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME = "AttributeOperationCommonPattern",
							   MODEL_FEATURE_NAME = "Model",
							   NATURALTYPE_FEATURE_NAME = "Natural Type",
							   DATATYPE_FEATURE_NAME = "Data Type",
							   INHERITANCE_FEATURE_NAME = "Inheritance",
							   CHANGECONFIGURATION_FEATURE_NAME = "Change Feature Model",
							   GROUP_FEATURE_NAME = "Group",
							   STEP_IN_FEATURE_NAME = "Step In",
							   STEP_IN_NEW_TAB_FEATURE_NAME = "Step In New Tab",
							   STEP_OUT_FEATURE_NAME = "Step out";
	
	/**
	 * name literals for editors/ pages
	 * <p>
	 * can be:<br>
	 * (1) the name for the diagram page or<br>
	 * (2) the name for the iorm textviewer page or<br>
	 * (3) the name for the crom textviewer page or<br>
	 * (4) the name for the feature configuration editor page
	 */
	public static final String DIAGRAM_PAGE_NAME = "CROM Diagram",
							   TEXT_IORM_PAGE_NAME = "IORM",
							   TEXT_CROM_PAGE_NAME = "CROM",
							   FEATURE_PAGE_NAME = "Configuration";

	/**
	 * the prefix for the multipage editor names if a groups diagram is opened in it
	 */
	public static final String MULTIPAGE_EDITOR_NAME_GROUP_DIAGRAM = "Group";
}

