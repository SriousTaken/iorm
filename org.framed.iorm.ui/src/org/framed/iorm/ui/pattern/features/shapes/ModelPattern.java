package org.framed.iorm.ui.pattern.features.shapes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
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
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.NameLiterals;
import org.framed.iorm.ui.literals.URLLiterals;

public class ModelPattern extends AbstractPattern implements IPattern {
	//TODO: singleton für model
	
	//ID literals
	public final String SHAPE_ID_MODEL_CONTAINER = IdentifierLiterals.SHAPE_ID_MODEL_CONTAINER,
			 			SHAPE_ID_MODEL_NAME = IdentifierLiterals.SHAPE_ID_MODEL_NAME;
	
	//name literals
	private final String MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
	
	//url literals
	private final URL fileURLToStandartConfiguration = URLLiterals.URL_TO_STANDARD_CONFIGURATION;
		
	public ModelPattern() {
		super(null);
	}
	
	@Override
	public String getCreateName() {
		return MODEL_FEATURE_NAME;
	}
	
	@Override
	public boolean isMainBusinessObjectApplicable(Object businessObject) {
		return (businessObject instanceof Model);
	}

	@Override
	protected boolean isPatternControlled(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}

	@Override
	protected boolean isPatternRoot(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}
	
	// add features
	//~~~~~~~~~~~~~
	@Override
	public boolean canAdd(IAddContext addContext) {
		//new Object is Model
		if(addContext.getNewObject() instanceof Model) {
			//check if root model already exists
			for(EObject eObject : getDiagram().eResource().getContents()) {
				if(eObject instanceof Model) return false;
			}
			return true;
		}  
		return false;
	}

	@Override
	public PictogramElement add(IAddContext addContext) {
		//get container and new object
		Model addedModel = (Model) addContext.getNewObject();
		getDiagram().eResource().getContents().add(addedModel);
		return null;
	}
	
	//create feature
	//~~~~~~~~~~~~~~
	@Override
	public Object[] create(ICreateContext context) {
		Model newModel = OrmFactory.eINSTANCE.createModel();
		try {
			setStandartConfiguration(newModel);
		} catch (URISyntaxException | IOException e) { e.printStackTrace(); }
		addGraphicalRepresentation(context, newModel);
		return new Object[] { newModel };
	}
	
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