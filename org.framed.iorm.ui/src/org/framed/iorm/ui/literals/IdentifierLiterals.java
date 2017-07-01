package org.framed.iorm.ui.literals;

/**
 * This class saves severals static Strings used as identifiers.
 * @author Kevin Kassin
 */
public class IdentifierLiterals {

	/**
	 * the identifier for the {@link org.framed.iorm.ui.providers.DiagramTypeProvider} as defined in the extensions of this package
	 * <p>
	 * This value should always be the same as the value of the field <em>id</em> of the extension point <em>org.eclipse.graphiti.ui.diagramTypeProviders</em>.
	 */
	public static final String DIAGRAM_PROVIDER_ID = "IORM_Diagram_Provider";
	
	/**
	 * the diagram identifier of the diagram type for compartment role object models as defined in the extensions of this package
	 * <p>
	 * This value should always be the same as the value of the field <em>id</em> of the extension point <em>org.eclipse.graphiti.ui.diagramTypes</em>.
	 */
	public static final String DIAGRAM_TYPE_ID = "Role Model";
	
	/**
	 * editor ID
	 * <p>
	 * the editor id of the {@link org.framed.iorm.ui.multipage.MultipageEditor}
	 */
	public static final String EDITOR_ID = "IORM_MultipageEditor";

	/**
	 * string used as prefix for image identifiers for icon of create features
	 */
	private static final String IMG_ID_PREFIX = "org.framed.iorm.ui.";
	
	/**
	 * image IDs
	 * <p>
	 * the identifier of images used in the {@link org.framed.iorm.ui.subeditors.FRaMEDDiagramEditor} for features
	 */
	public static final String IMG_ID_FEATURE_NATURALTYPE = IMG_ID_PREFIX + "img_naturaltype",
							   IMG_ID_FEATURE_DATATYPE = IMG_ID_PREFIX + "img_datatype",
							   IMG_ID_FEATURE_ATTRIBUTE = IMG_ID_PREFIX + "img_attribute",
							   IMG_ID_FEATURE_OPERATION = IMG_ID_PREFIX + "img_operation",
							   IMG_ID_FEATURE_INHERITANCE = IMG_ID_PREFIX + "img_inheritance",
							   IMG_ID_FEATURE_GROUP = IMG_ID_PREFIX + "img_group";
	
	/**
	 * feature model ID
	 * <p>
	 * the idenfier of the feature model the {@link org.framed.iorm.ui.subeditors.FRaMEDFeatureEditor} uses
	 */
	public static final String FEATUREMODEL_ID = "org.framed.iorm.featuremodel";
	
	/**
	 * identifers used for the natural type
	 * <p>
	 * can be:<br>
	 * (1) typebody rectangle or<br>
	 * (2) shadow rectangle or<br>
	 * (3) name rectange or<br>
	 * (4) first line or<br>
	 * (5) second line or<br>
	 * (6) attribute container rectangle or<br>
	 * (7) operation container rectangle
	 */
	public static final String SHAPE_ID_NATURALTYPE_TYPEBODY = "shape_nt_typebody",
						 	   SHAPE_ID_NATURALTYPE_SHADOW = "shape_nt_shadow",
						 	   SHAPE_ID_NATURALTYPE_NAME = "shape_nt_name", 
						 	   SHAPE_ID_NATURALTYPE_FIRSTLINE = "shape_nt_firstline",
						 	   SHAPE_ID_NATURALTYPE_SECONDLINE = "shape_nt_secondline", 
						 	   SHAPE_ID_NATURALTYPE_ATTRIBUTECONTAINER = "shape_nt_attcontainer",
						 	   SHAPE_ID_NATURALTYPE_OPERATIONCONTAINER = "shape_nt_opcontainer";
	
	/**
	 * identifers used for the data type
	 * <p>
	 * can be:<br>
	 * (1) typebody polygon or<br>
	 * (2) shadow polygon or<br>
	 * (3) name rectange or<br>
	 * (4) first line or<br>
	 * (5) second line or<br>
	 * (6) attribute container rectangle or<br>
	 * (7) operation container rectangle 
	 */
	public static final String SHAPE_ID_DATATYPE_TYPEBODY = "shape_dt_typebody",
						 	   SHAPE_ID_DATATYPE_SHADOW = "shape_dt_shadow",
						 	   SHAPE_ID_DATATYPE_NAME = "shape_dt_name", 
						 	   SHAPE_ID_DATATYPE_FIRSTLINE = "shape_dt_firstline",
						 	   SHAPE_ID_DATATYPE_SECONDLINE = "shape_dt_secondline", 
						 	   SHAPE_ID_DATATYPE_ATTRIBUTECONTAINER = "shape_dt_attcontainer",
						 	   SHAPE_ID_DATATYPE_OPERATIONCONTAINER = "shape_dt_opcontainer";
	
	/**
	 * identifers used for the group
	 * <p>
	 * can be:<br>
	 * (1) typebody rectangle or<br>
	 * (2) shadow rectangle or<br>
	 * (3) name rectange or<br>
	 * (4) line or<br>
	 * (5) model container rectangle 
	 */
	public static final String SHAPE_ID_GROUP_TYPEBODY = "shape_group_typebody",
						 	   SHAPE_ID_GROUP_SHADOW = "shape_group_shadow",
						 	   SHAPE_ID_GROUP_NAME = "shape_group_name", 
						 	   SHAPE_ID_GROUP_LINE = "shape_group_line",
						 	   SHAPE_ID_GROUP_MODEL = "shape_group_model";
	
	/**
	 * identifers used for attributes
	 * <p>
	 * the identifier for the attribute rectangle
	 */
	public static final String SHAPE_ID_ATTRIBUTE_TEXT = "shape_att_text";
	
	/**
	 * identifers used for operation
	 * <p>
	 * the identifier for the operation rectangle
	 */
	public static final String SHAPE_ID_OPERATION_TEXT = "shape_op_text";
	
	/**
	 * identifers used for the inheritance connection
	 * <p>
	 * can be:<br>
	 * (1) the identifier for the inheritance polyline or<br>
	 * (2) the identifier for the inheritance decorator polygon
	 */
	public static final String SHAPE_ID_INHERITANCE_LINE = "shape_inheritance_con",
							   SHAPE_ID_INHERITANCE_DECORATOR = "shape_inheritance_dec";

	/**
	 * key values used to identify properties in {@link org.framed.iorm.ui.util.PropertyUtil}
	 * <p>
	 * the key value for the property shape id
	 */
	public static final String KEY_SHAPE_ID = "shape-id";
}
