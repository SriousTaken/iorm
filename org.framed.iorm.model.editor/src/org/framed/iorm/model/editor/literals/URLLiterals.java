package org.framed.iorm.model.editor.literals;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class URLLiterals {
	
	//featuremodel related urls
	private static final Bundle bundleFeatureModel = Platform.getBundle("org.framed.iorm.featuremodel");
	public static final URL URL_TO_FEATUREMODEL = bundleFeatureModel.getEntry("model.xml"),
						  	URL_TO_STANDARD_CONFIGURATION = bundleFeatureModel.getEntry("/standardframedconfiguration/standardFramedConfiguration.diagram");

}
