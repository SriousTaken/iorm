package org.framed.iorm.ui.literals;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * This class saves severals static Strings, bundles, paths and URLs used to access files.
 * @author Kevin Kassin
 */
public class URLLiterals {
	
	/**
	 * bundles to acces packages
	 * <p>
	 * the bundle used to get access to the package "org.framed.iorm.featuremodel"
	 */
	private static final Bundle BUNDLE_FEATUREMODEL = Platform.getBundle("org.framed.iorm.featuremodel");
	
	/**
	 * URLs related to the feature model
	 * <p>
	 * can be:<br>
	 * (1) the URL to the used feature model or<br>
	 * (2) the URL to the standard configuration 
	 */
	public static final URL URL_TO_FEATUREMODEL = BUNDLE_FEATUREMODEL.getEntry("model.xml"),
						  	URL_TO_STANDARD_CONFIGURATION = BUNDLE_FEATUREMODEL.getEntry("/standardframedconfiguration/standardFramedConfiguration.diagram");

	/**
	 * Path to get the empty text file in a role model project for the status page 
	 */
	public static final IPath PATH_TO_EMPTY_TEXTFILE = new Path("/text/empty.txt");
	
	/**
	 * string used as prefix for file pathes for icon of create features
	 */
	private static final String IMG_FILE_PATH_PREFIX = "icons/features/icon_";
	
	/**
	 * file paths to icons used for create features
	 * <p>
	 * can be:<br>
	 * (1) the file path to the icon for the natural type create feature or<br>
	 * (2) the file path to the icon for the data type create feature or<br>
	 * (3) the file path to the icon for the attribute create feature or<br>
	 * (4) the file path to the icon for the operation create feature or<br>
	 * (5) the file path to the icon for the inheritance create feature
	 */
	public static final String IMG_FILEPATH_FEATURE_NATURALTYPE = IMG_FILE_PATH_PREFIX + "naturaltype.png",
							   IMG_FILEPATH_FEATURE_DATATYPE = IMG_FILE_PATH_PREFIX + "datatype.png",
							   IMG_FILEPATH_FEATURE_ATTRIBUTE = IMG_FILE_PATH_PREFIX + "attribute.gif",
							   IMG_FILEPATH_FEATURE_OPERATION = IMG_FILE_PATH_PREFIX + "operation.gif",
							   IMG_FILEPATH_FEATURE_INHERITANCE = IMG_FILE_PATH_PREFIX + "inheritance.png",
							   IMG_FILEPATH_FEATURE_GROUP = IMG_FILE_PATH_PREFIX + "group.png";
	
}
