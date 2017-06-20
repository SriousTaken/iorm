package org.framed.iorm.ui.literals;

/**
 * This class saves severals static Strings used as identifiers.
 * @author Kevin Kassin
 */
public class IdentifierLiterals {

	/**
	 * diagram ID
	 * <p>
	 * the diagram identifier of the diagram type for role models
	 */
	public static final String DIAGRAM_TYPE = "Role Model";
	
	/**
	 * editor ID
	 * <p>
	 * the editor id of the {@link MultipageEditor}
	 */
	public static final String EDITOR_ID = "IORM_MultipageEditor";
	
	/**
	 * subeditor IDs
	 * <p>
	 * the identifiers of the subeditors of the {@link MultipageEditor}
	 */
	public static final String PAGE_ID_BEHAVIOR = "page_behavior",
							   PAGE_ID_DATA = "page_data",
							   PAGE_ID_IORM_TEXT = "page_iorm_text",
							   PAGE_ID_CROM_TEXT = "page_crom_text",
							   PAGE_ID_FEATURE = "page_feature_configuration";
	
	/**
	 * string used as prefix for image identifiers for icon of create features
	 */
	private static final String IMG_ID_PREFIX = "org.framed.iorm.ui.";
	
	/**
	 * image IDs
	 * <p>
	 * the identifier of images used in the {@link DiagramEditorWithID} for features
	 */
	public static final String IMG_ID_FEATURE_NATURALTYPE = IMG_ID_PREFIX + "img_naturaltype",
							   IMG_ID_FEATURE_DATATYPE = IMG_ID_PREFIX + "img_datatype",
							   IMG_ID_FEATURE_ATTRIBUTE = IMG_ID_PREFIX + "img_attribute",
							   IMG_ID_FEATURE_OPERATION = IMG_ID_PREFIX + "img_operation",
							   IMG_ID_FEATURE_INHERITANCE = IMG_ID_PREFIX + "img_inheritance";
	
	/**
	 * feature model ID
	 * <p>
	 * the idenfier of the feature model the {@link FeatureEditorWithID} uses
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
	 * (7) operation container rectangleor<br>
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
	 * (1) typebody polyon or<br>
	 * (2) shadow polyon or<br>
	 * (3) name rectange or<br>
	 * (4) first line or<br>
	 * (5) second line or<br>
	 * (6) attribute container rectangle or<br>
	 * (7) operation container rectangleor<br>
	 */
	public static final String SHAPE_ID_DATATYPE_TYPEBODY = "shape_dt_typebody",
						 	   SHAPE_ID_DATATYPE_SHADOW = "shape_dt_shadow",
						 	   SHAPE_ID_DATATYPE_NAME = "shape_dt_name", 
						 	   SHAPE_ID_DATATYPE_FIRSTLINE = "shape_dt_firstline",
						 	   SHAPE_ID_DATATYPE_SECONDLINE = "shape_dt_secondline", 
						 	   SHAPE_ID_DATATYPE_ATTRIBUTECONTAINER = "shape_dt_attcontainer",
						 	   SHAPE_ID_DATATYPE_OPERATIONCONTAINER = "shape_dt_opcontainer";
	
	/**
	 * identifers used for attributes
	 * <p>
	 * the identifier for the attribute rectangle<br>
	 */
	public static final String SHAPE_ID_ATTRIBUTE_TEXT = "shape_att_text";
	
	/**
	 * identifers used for operation
	 * <p>
	 * the identifier for the operation rectangle<br>
	 */
	public static final String SHAPE_ID_OPERATION_TEXT = "shape_op_text";
	
	/**
	 * identifers used for the inheritance connection
	 * <p>
	 * the identifier for the decorated inheritance polyline<br>
	 */
	public static final String SHAPE_ID_INHERITANCE_CONNECTION = "shape_inheritance_con";
	
}
