package org.framed.iorm.ui.literals;

/**
 * This class saves severals static Strings used as names.
 * @author Kevin Kassin
 */
public class NameLiterals {
	
	/**
	 * name literals used in the role model wizard or role model project wizard
	 * <p>
	 * can be:<br>
	 * (1) the name of the wizard or<br>
	 * (2) the name of the role model wizard page or<br>
	 * (3) the default name of a new diagram
	 */
	public static final String WIZARD_WINDOW_NAME = "Role Model Wizard", 
							   WIZARD_PAGE_NAME = "Role Model",
							   STANDARD_DIAGRAM_NAME = "newDiagram";
							  
	/**
	 * standard names for graphiti shapes
	 * <p>
	 * can be:<br>
	 * (1) the standard name for attributes or<br>
	 * (2) the standard name for operations or<br>
	 */
	public static final String STANDARD_ATTRIBUTE_NAME = "attribute:type",
							   STANDARD_OPERATION_NAME = "operation():type";

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
	 * (8) the name of the change configuration custom feature
	 */
	public static final String ATTRIBUTE_FEATURE_NAME = "Attribute",
							   OPERATION_FEATURE_NAME = "Operation",
							   ATTRIBUTE_OPERATION_COMMON_FEATURE_NAME = "AttributeOperationCommonPattern",
							   MODEL_FEATURE_NAME = "Model",
							   NATURALTYPE_FEATURE_NAME = "Natural Type",
							   DATATYPE_FEATURE_NAME = "Data Type",
							   INHERITANCE_FEATURE_NAME = "Inheritance",
							   CHANGECONFIGURATION_FEATURE_NAME = "Change Feature Model";
	
	/**
	 * name literals for editor pages
	 * <p>
	 * can be:<br>
	 * (1) the name for the behavior diagram page or<br>
	 * (2) the name for the data diagram page or<br>
	 * (3) the name for the iorm textviewer page or<br>
	 * (4) the name for the crom textviewer page or<br>
	 * (5) the name for the feature configuration editor page
	 */
	public static final String BEHAVIOR_PAGE_NAME = "Behavior Diagram",
							   DATA_PAGE_NAME = "Data Diagram",	
							   TEXT_IORM_PAGE_NAME = "IORM Text",
							   TEXT_CROM_PAGE_NAME = "CROM Text",
							   FEATURE_PAGE_NAME = "Feature Configuration";
}
