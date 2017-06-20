package org.framed.iorm.ui.literals;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class URLLiterals {
	
	//urls related to the feature model
	private static final Bundle BUNDLE_FEATUREMODEL = Platform.getBundle("org.framed.iorm.featuremodel");
	public static final URL URL_TO_FEATUREMODEL = BUNDLE_FEATUREMODEL.getEntry("model.xml"),
						  	URL_TO_STANDARD_CONFIGURATION = BUNDLE_FEATUREMODEL.getEntry("/standardframedconfiguration/standardFramedConfiguration.diagram");

	//file paths
	public static final String IMG_FILEPATH_FEATURE_NATURALTYPE = "icons/features/icon_naturaltype.PNG",
							   IMG_FILEPATH_FEATURE_DATATYPE = "icons/features/icon_datatype.PNG";
	
}
