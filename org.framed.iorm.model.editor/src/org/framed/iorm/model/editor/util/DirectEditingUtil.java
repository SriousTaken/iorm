package org.framed.iorm.model.editor.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectEditingUtil {

	//regular Expression
	//~~~~~~~~~~~~~~~~~~
	// string of letters and digits, first is no digit (for names)
	private static final String identifier = "[a-zA-Z_$][a-zA-Z\\d_$]*"; 
	// full classes: (N.)*N (for types)
	private static final String qualifiedIdentifier = "(" + identifier + "\\.)*" + identifier; 
	// (<integer>|"*")[".."(<integer>|"*")]
	private static final String cardinality = "(\\d+|\\*)(\\.\\.(\\d+|\\*))?"; 
	// <name>:<type>
	private static final String attribute = identifier + ":" + qualifiedIdentifier; 
	// <name>:<type>
	private static final String parameter = attribute; 
	// <name>"("[Parameter(","Parameter)*]"):"<type>, return type is optional
	private static final String operation = identifier + "\\((" + parameter + "(," + parameter + ")*)?\\)(:" + qualifiedIdentifier + ")?";
	// name [ "(" cardinality ")" ]
	private static final String rolegroup = identifier +"([ ]*[(]"+ cardinality +"[)])?"; 

	//Pattern
	//~~~~~~~
	private static final Pattern identifierPattern = Pattern.compile(identifier);
	private static final Pattern attributePattern = Pattern.compile(attribute);
	private static final Pattern operationPattern = Pattern.compile(operation);
	
	//Matcher operations
	//~~~~~~~
	public static final boolean matchesIdentifier(String identifier) {
		Matcher identifierMatcher = identifierPattern.matcher(identifier);
		return identifierMatcher.matches();
	}
	
	public static final boolean matchesAttribute(String attributeName) {
		Matcher attributeMatcher = attributePattern.matcher(attributeName);
		return attributeMatcher.matches();
	}
	
	public static final boolean matchesOperation(String operationName) {
		Matcher operationMatcher = operationPattern.matcher(operationName);
		return operationMatcher.matches();
	}
}
