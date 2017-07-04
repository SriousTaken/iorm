package org.framed.iorm.ui.pattern.shapes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.pattern.IPattern;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.OrmFactory;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.literals.URLLiterals;
import org.framed.iorm.ui.util.GeneralUtil;

/**
 * This graphiti pattern class is used to work with {@link org.framed.iorm.model.Model} in the editor. 
 * <p>
 * This pattern especially creates the root model of a diagram in the editor. The root model is special 
 * since it has to keep a framed configuration. It deals with the following aspects of the root model:<br>
 * (1) creating the root model, especially its business object<br>
 * (2) setting the standard configuration of the root model at creation<br>
 * (3) adding the root model to the diagram, if is not already has a root model attached
 * @author Kevin Kassin
 */
public class ModelPattern extends AbstractPattern implements IPattern {
	
	/**
	 * the features name gathered from {@link NameLiterals}
	 */
	private final String MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
	
	/**
	 * the URL leading to the standard configuration gathered from {@link URLLiterals}
	 */
	private final URL fileURLToStandartConfiguration = URLLiterals.URL_TO_STANDARD_CONFIGURATION;
		
	/**
	 * Class constructor
	 */
	public ModelPattern() {
		super(null);
	}
	
	/**
	 * get method for the features name
	 * @return the name of the feature
	 */
	@Override
	public String getCreateName() {
		return MODEL_FEATURE_NAME;
	}
	
	/**
	 * checks if pattern is applicable for a given business object
	 * @return true, if business object is of type {@link org.framed.iorm.model.Model}
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object businessObject) {
		return (businessObject instanceof Model);
	}

	/**
	 * checks if pattern is applicable for a given pictogram element
	 * @return true, if business object of the pictogram element is of type {@link org.framed.iorm.model.Model}
	 */
	@Override
	protected boolean isPatternControlled(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}

	/**
	 * checks if the pictogram element to edit with the pattern is its root
	 * @return true, if business object of the pictogram element is of type {@link org.framed.iorm.model.Model}
	 */
	@Override
	protected boolean isPatternRoot(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}
	
	// add feature
	//~~~~~~~~~~~~~
	/**
	 * calculates if a root model can be added to the diagram
	 * <p>
	 * returns true if<br>
	 * (1) the added business object is a {@link org.framed.iorm.model.Model} and<br>
	 * (2) the diagram does not already have a root model linked
	 * @return if the root model can be added
	 */
	@Override
	public boolean canAdd(IAddContext addContext) {
		if(addContext.getNewObject() instanceof Model) {
			return (GeneralUtil.getRootModelForDiagram(getDiagram()) == null);
		}  
		return false;
	}

	/**
	 * adds and links the root model to the diagram
	 * @return null, since the model has no pictogram element 
	 */
	@Override
	public PictogramElement add(IAddContext addContext) {
		Model addedModel = (Model) addContext.getNewObject();
		getDiagram().eResource().getContents().add(addedModel);
		link(getDiagram(), addedModel);
		return null;
	}
	
	//create feature
	//~~~~~~~~~~~~~~
	/**
	 * calculates if a root model can be created
	 * <p>
	 * returns true if the diagram does not already have a root model linked
	 * @return if the root model can be added
	 */
	@Override
	public boolean canCreate(ICreateContext createContext) {
		return (GeneralUtil.getRootModelForDiagram(getDiagram()) == null);
	}
	
	/**
	 * creates the business object of the root model, set its standard configuration and
	 * calls the add function for the root model
	 * @return the created busines object of the root model
	 */
	@Override
	public Object[] create(ICreateContext createContext) {
		Model newModel = OrmFactory.eINSTANCE.createModel();
		try {
			setStandartConfiguration(newModel);
		} catch (URISyntaxException | IOException e) { e.printStackTrace(); }
		addGraphicalRepresentation(createContext, newModel);
		return new Object[] { newModel };
	}
	
	/**
	 * sets the standard configuration for a given {@link org.framed.iorm.model.Model}
	 * @param model the model to set the standard configuration for
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private void setStandartConfiguration(Model model) throws URISyntaxException, IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resourceStandartConfiguration =
			resourceSet.createResource(URI.createURI(FileLocator.resolve(fileURLToStandartConfiguration).toURI().toString()));
		try {
			resourceStandartConfiguration.load(null);
		} catch (IOException e) { 
			e.printStackTrace();
			resourceStandartConfiguration = null;
		}
		Model standardConfigurationModel = (Model) resourceStandartConfiguration.getContents().get(0);
		model.setFramedConfiguration(standardConfigurationModel.getFramedConfiguration());
	}
}