package org.framed.iorm.model.editor.providers;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;
import org.framed.iorm.model.editor.literals.IdentifierLiterals;

public class ImageProvider extends AbstractImageProvider {
 
    //image IDs for features
    public final String IMG_ID_FEATURE_NATURALTYPE = IdentifierLiterals.IMG_ID_FEATURE_NATURALTYPE;
    public final String IMG_ID_FEATURE_DATATYPE = IdentifierLiterals.IMG_ID_FEATURE_DATATYPE;
 
    @Override
    protected void addAvailableImages() {
        // register path for feature images
        addImageFilePath(IMG_ID_FEATURE_NATURALTYPE, "icons/features/icon_naturaltype.PNG");
        addImageFilePath(IMG_ID_FEATURE_DATATYPE, "icons/features/icon_datatype.PNG");
    }

}
