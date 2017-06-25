package org.framed.iorm.ui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to check names when direct editing shapes and connection for their validity. 
 * {@link org.framed.iorm.ui.wizards.RoleModelWizard} and {@link org.framed.iorm.ui.wizards.RoleModelProjectWizard}
 * also use this class to check the names of project and file names.
 * @author Kevin Kassin
 */
public class DirectEditingUtil {

	/**
	 * regular expression for identifiers:
	 * <p>
	 * string of letters and digits, first is no digit (for names)
	 */
	private static final String identifier = "[a-zA-Z_$][a-zA-Z\\d_$]*"; 
	
	/**
	 * regular expression for qualified identifiers
	 * <p>
	 * full classes: (N.)*N (for types)
	 */
	private static final String qualifiedIdentifier = "(" + identifier + "\\.)*" + identifier; 
	
	/**
	 * regular expression for cardinalities
	 * <p>
	 * (integer|"*")[".."(integer|"*")]
	 */
	private static final String cardinality = "(\\d+|\\*)(\\.\\.(\\d+|\\*))?"; 
	
	/**
	 * regular expression for attributes
	 * <p>
	 * name:type
	 */
	private static final String attribute = identifier + ":" + qualifiedIdentifier; 
	
	/**
	 * regular expression for parameters
	 * <p> 
	 * name:type
	 */
	private static final String parameter = attribute; 
	
	/**
	 * regular expression for operations
	 * <p>
	 * name"("[Parameter(","Parameter)*]"):"type, return type is optional
	 */
	private static final String operation = identifier + "\\((" + parameter + "(," + parameter + ")*)?\\)(:" + qualifiedIdentifier + ")?";
	
	/**
	 * regular expression for role groups
	 * <p>
	 * name [ "(" cardinality ")" ]
	 */
	private static final String rolegroup = identifier +"([ ]*[(]"+ cardinality +"[)])?"; 

	/**
	 * patterns to check for the regular expression of this class
	 * <p>
	 * can be:<br>
	 * (1) the pattern for identifiers or<br>
	 * (2) the pattern for attributes or<br>
	 * (3) the pattern for operations
	 */
	private static final Pattern identifierPattern = Pattern.compile(identifier),
								 attributePattern = Pattern.compile(attribute),
								 operationPattern = Pattern.compile(operation);
	
	/**
	 * matching operation for the regular expression of identifiers
	 * @param identifier the string to check against
	 * @return if the given string input matches the regular expression
	 */
	public static final boolean matchesIdentifier(String identifier) {
		Matcher identifierMatcher = identifierPattern.matcher(identifier);
		return identifierMatcher.matches();
	}
	
	/**
	 * matching operation for the regular expression of attributes
	 * @param identifier the string to check against
	 * @return if the given string input matches the regular expression
	 */
	public static final boolean matchesAttribute(String attributeName) {
		Matcher attributeMatcher = attributePattern.matcher(attributeName);
		return attributeMatcher.matches();
	}
	
	/**
	 * matching operation for the regular expression of operations
	 * @param identifier the string to check against
	 * @return if the given string input matches the regular expression
	 */
	public static final boolean matchesOperation(String operationName) {
		Matcher operationMatcher = operationPattern.matcher(operationName);
		return operationMatcher.matches();
	}
}
