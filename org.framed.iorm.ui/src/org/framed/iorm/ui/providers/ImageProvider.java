package org.framed.iorm.ui.providers;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.URLLiterals;

public class ImageProvider extends AbstractImageProvider {
 
    //image IDs for features
    private final String IMG_ID_FEATURE_NATURALTYPE = IdentifierLiterals.IMG_ID_FEATURE_NATURALTYPE,
    				    IMG_ID_FEATURE_DATATYPE = IdentifierLiterals.IMG_ID_FEATURE_DATATYPE;
    
    //file path to icons
    private final String IMG_FILEPATH_FEATURE_NATURALTYPE = URLLiterals.IMG_FILEPATH_FEATURE_NATURALTYPE,
    					 IMG_FILEPATH_FEATURE_DATATYPE = URLLiterals.IMG_FILEPATH_FEATURE_DATATYPE;
    
    @Override
    protected void addAvailableImages() {
        // register path for feature images
        addImageFilePath(IMG_ID_FEATURE_NATURALTYPE, IMG_FILEPATH_FEATURE_NATURALTYPE);
        addImageFilePath(IMG_ID_FEATURE_DATATYPE, IMG_FILEPATH_FEATURE_DATATYPE);
    }

}
