package org.framed.iorm.model.editor.literals;

public class IdentifierLiterals {

	//diagram ID
	//~~~~~~~~~~
	public static final String DIAGRAM_TYPE = "Role Model";
	
	//editor ID
	//~~~~~~~~~
	public static final String EDITOR_ID = "IORM_MultipageEditor";
	
	//page IDs
	//~~~~~~~~
	public static final String PAGE_ID_BEHAVIOR = "page_behavior",
							   PAGE_ID_DATA = "page_data",
							   PAGE_ID_IORM_TEXT = "page_iorm_text",
							   PAGE_ID_CROM_TEXT = "page_crom_text",
							   PAGE_ID_FEATURE = "page_feature_configuration";
	//image IDs
	//~~~~~~~~~
	public static final String IMG_ID_PREFIX = "org.framed.iorm.model.editor.",
							   IMG_ID_FEATURE_NATURALTYPE = IMG_ID_PREFIX + "img_naturaltype",
							   IMG_ID_FEATURE_DATATYPE = IMG_ID_PREFIX + "img_datatype";
	
	//feature model ID
	//~~~~~~~~~~~~~~~~
	public static final String FEATUREMODEL_ID = "org.framed.iorm.featuremodel";
	
	//shape IDs
	//~~~~~~~~~
	//Model
	public static final String SHAPE_ID_MODEL_CONTAINER = "shape_model_container",
						 	   SHAPE_ID_MODEL_NAME = "shape_model_name";
	
	//Natural Type
	public static final String SHAPE_ID_NATURALTYPE_CONTAINER = "shape_nt_container",
						 	   SHAPE_ID_NATURALTYPE_SHADOW = "shape_nt_shadow",
						 	   SHAPE_ID_NATURALTYPE_NAME = "shape_nt_name", 
						 	   SHAPE_ID_NATURALTYPE_FIRSTLINE = "shape_nt_firstline",
						 	   SHAPE_ID_NATURALTYPE_SECONDLINE = "shape_nt_secondline", 
						 	   SHAPE_ID_NATURALTYPE_ATTRIBUTECONTAINER = "shape_nt_attcontainer",
						 	   SHAPE_ID_NATURALTYPE_OPERATIONCONTAINER = "shape_nt_opcontainer";
	
	//Data Type
	public static final String SHAPE_ID_DATATYPE_CONTAINER = "shape_dt_container",
						 	   SHAPE_ID_DATATYPE_SHADOW = "shape_dt_shadow",
						 	   SHAPE_ID_DATATYPE_NAME = "shape_dt_name", 
						 	   SHAPE_ID_DATATYPE_FIRSTLINE = "shape_dt_firstline",
						 	   SHAPE_ID_DATATYPE_SECONDLINE = "shape_dt_secondline", 
						 	   SHAPE_ID_DATATYPE_ATTRIBUTECONTAINER = "shape_dt_attcontainer",
						 	   SHAPE_ID_DATATYPE_OPERATIONCONTAINER = "shape_dt_opcontainer";
	
	//Attributes
	public static final String SHAPE_ID_ATTRIBUTE_TEXT = "shape_att_text";
	
	//Operations
	public static final String SHAPE_ID_OPERATION_TEXT = "shape_op_text";
	
	//Inheritance
	public static final String SHAPE_ID_INHERITANCE_CONNECTION = "shape_inheritance_con";
	
}
