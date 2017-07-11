package org.framed.iorm.ui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
import org.framed.iorm.ui.wizards.RoleModelWizard; //*import for javadoc link
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.ModelElement;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.exceptions.NoModelFoundException;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.wizards.RoleModelProjectWizard; //*import for javadoc link

/**
 * This class is used to check names when direct editing shapes and connection for their validity. 
 * {@link RoleModelWizard} and {@link RoleModelProjectWizard} also use this class to check the names 
 * of project and file names.
 * @author Kevin Kassin
 */
public class DirectEditingUtil {

	/**
	 * the identifier of the <em>main diagram</em> using the property diagram kind
	 * <p>
	 * If its not clear what <em>main diagram</em> means, see {@link RoleModelWizard#createEmfFileForDiagram} for reference.
	 */
	private static final String DIAGRAM_KIND_MAIN_DIAGRAM = IdentifierLiterals.DIAGRAM_KIND_MAIN_DIAGRAM;
	
	//TODO
	private final static int STANDART_NAMES_COUNTER_LIMIT = 10;
	
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
	
	/**
	 * calculates if another model element of a specific {@link org.framed.iorm.model.Type} already has a name equivalent 
	 * to the new given name when direct editing names using the following steps:
	 * <p>
	 * Step 1: It gets the <em>main diagram</em> of the role model that the given diagram belongs to.<br>
	 * Step 2: It fetches a list of the model element names for the given type and checks if this list contains
	 * 		   the new name.
	 * <p>
	 * If its not clear what <em>main diagram</em> means, see {@link RoleModelWizard#createEmfFileForDiagram} for reference.
	 * @param diagram the diagram the that is direct edited
	 * @param type the type to the check for if a model element of that type already has the same name
	 * @param newName the name to check against
	 * @return boolean if another model element of a given type already has the same name when direct editing
	 */
	public static boolean nameAlreadyUsedForClassOrRole(Diagram diagram, Type type, String newName) {
		List<String> modelElements = new ArrayList<String>();
		//Step 1
		Model rootModel = null;
		Diagram containerDiagram = DiagramUtil.getContainerDiagramForAnyDiagram(diagram);
		for(Shape shape : containerDiagram.getChildren()) {
			if(shape instanceof Diagram &&
			   PropertyUtil.isDiagram_KindValue((Diagram) shape, DIAGRAM_KIND_MAIN_DIAGRAM)) {
				if(shape.getLink().getBusinessObjects().size() == 1) {
					rootModel = (Model) shape.getLink().getBusinessObjects().get(0);
		}	}	}
		if(rootModel == null) throw new NoModelFoundException();
		else {
			//Step 2
			getModelElementsNamesRecursive(rootModel, type, modelElements);
			return modelElements.contains(newName);
	}	}
		
	/**
	 * fetches all names of model elements of a given type in a recursive manner
	 * @param model the model to fetch the model elements names from
	 * @param type the type of the model elements to get the names from
	 * @param modelElementNames the list of model element names to fill while using recursion
	 */
	private static void getModelElementsNamesRecursive(Model model, Type type, List<String> modelElementNames) {
		for(ModelElement modelElement : model.getElements()) {
			if(modelElement instanceof org.framed.iorm.model.Shape) {
				if(modelElement.getType() == Type.NATURAL_TYPE &&
				   type == Type.NATURAL_TYPE) 
					modelElementNames.add(modelElement.getName());
				if(modelElement.getType() == Type.DATA_TYPE &&
				   type == Type.DATA_TYPE)
					modelElementNames.add(modelElement.getName());
				if(modelElement.getType() == Type.GROUP) {
					if(type == Type.GROUP) modelElementNames.add(modelElement.getName());
					getModelElementsNamesRecursive(((org.framed.iorm.model.Shape) modelElement).getModel(), type, modelElementNames);
	}	}	}	}
	
	/**
	 * calculates if another attribute or operation in the same class or role already have the same name when 
	 * direct editing names of attributes or operations 
	 * @param attributeOrOperationsContainer the container to search the other attributes or operations to check against
	 * @param newName the name to check against
	 * @return boolean if another attribute or operation in the same class or role already has the same name when 
	 * 		   direct editing
	 */
	public static boolean nameAlreadyUsedForAttributeOrOperation(ContainerShape attributeOrOperationsContainer, String newName) {
		List<String> attributeOrOperationNames = new ArrayList<String>();
		for(Shape shape : attributeOrOperationsContainer.getChildren()) {
			attributeOrOperationNames.add(((Text) shape.getGraphicsAlgorithm()).getValue());
		}
		return attributeOrOperationNames.contains(newName);
	}
	
	//TODO
	public static String calculateStandardNameForAttributeOrOperation(ContainerShape attributeOrOperationsContainer, String standardName) {
		List<String> attributeOrOperationNames = new ArrayList<String>();
		for(Shape shape : attributeOrOperationsContainer.getChildren()) {
			attributeOrOperationNames.add(((Text) shape.getGraphicsAlgorithm()).getValue());
		}
		if(!(attributeOrOperationNames.contains(standardName))) return standardName;
		for(int i=1; i<=STANDART_NAMES_COUNTER_LIMIT; i++) {
			if(!(attributeOrOperationNames.contains(standardName + Integer.toString(i))))
				return standardName + Integer.toString(i);
		}
		return standardName;
	}
}
