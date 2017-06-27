package org.framed.iorm.ui.providers;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.URLLiterals;

public class ImageProvider extends AbstractImageProvider {
 
    //image IDs for features
    private final String IMG_ID_FEATURE_NATURALTYPE = IdentifierLiterals.IMG_ID_FEATURE_NATURALTYPE,
    				     IMG_ID_FEATURE_DATATYPE = IdentifierLiterals.IMG_ID_FEATURE_DATATYPE,
    				     IMG_ID_FEATURE_ATTRIBUTE = IdentifierLiterals.IMG_ID_FEATURE_ATTRIBUTE,
 					     IMG_ID_FEATURE_OPERATION = IdentifierLiterals.IMG_ID_FEATURE_OPERATION,
    				     IMG_ID_FEATURE_INHERITANCE = IdentifierLiterals.IMG_ID_FEATURE_INHERITANCE,
    				     IMG_ID_FEATURE_GROUP = IdentifierLiterals.IMG_ID_FEATURE_GROUP;
    
    //file path to icons
    private final String IMG_FILEPATH_FEATURE_NATURALTYPE = URLLiterals.IMG_FILEPATH_FEATURE_NATURALTYPE,
    					 IMG_FILEPATH_FEATURE_DATATYPE = URLLiterals.IMG_FILEPATH_FEATURE_DATATYPE,
    				     IMG_FILEPATH_FEATURE_ATTRIBUTE = URLLiterals.IMG_FILEPATH_FEATURE_ATTRIBUTE,
  					     IMG_FILEPATH_FEATURE_OPERATION = URLLiterals.IMG_FILEPATH_FEATURE_OPERATION,
  					     IMG_FILEPATH_FEATURE_INHERITANCE = URLLiterals.IMG_FILEPATH_FEATURE_INHERITANCE,
  					     IMG_FILEPATH_FEATURE_GROUP = URLLiterals.IMG_FILEPATH_FEATURE_GROUP;
    
    @Override
    protected void addAvailableImages() {
        // register path for feature images
        addImageFilePath(IMG_ID_FEATURE_NATURALTYPE, IMG_FILEPATH_FEATURE_NATURALTYPE);
        addImageFilePath(IMG_ID_FEATURE_DATATYPE, IMG_FILEPATH_FEATURE_DATATYPE);
        addImageFilePath(IMG_ID_FEATURE_ATTRIBUTE, IMG_FILEPATH_FEATURE_ATTRIBUTE);
        addImageFilePath(IMG_ID_FEATURE_OPERATION, IMG_FILEPATH_FEATURE_OPERATION);
        addImageFilePath(IMG_ID_FEATURE_INHERITANCE, IMG_FILEPATH_FEATURE_INHERITANCE);
        addImageFilePath(IMG_ID_FEATURE_GROUP, IMG_FILEPATH_FEATURE_GROUP);
    }
}
