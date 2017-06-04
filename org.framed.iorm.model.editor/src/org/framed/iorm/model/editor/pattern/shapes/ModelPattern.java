package org.framed.iorm.model.editor.pattern.shapes;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.pattern.IPattern;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.OrmFactory;
import org.framed.iorm.model.editor.literals.IdentifierLiterals;
import org.framed.iorm.model.editor.literals.NameLiterals;

public class ModelPattern extends AbstractPattern implements IPattern {
	//TODO: singleton für model
	
	//ID literals
	public final String SHAPE_ID_MODEL_CONTAINER = IdentifierLiterals.SHAPE_ID_MODEL_CONTAINER,
			 			SHAPE_ID_MODEL_NAME = IdentifierLiterals.SHAPE_ID_MODEL_NAME;
	
	//name literals
	private final String MODEL_FEATURE_NAME = NameLiterals.MODEL_FEATURE_NAME;
		
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
		addGraphicalRepresentation(context, newModel);
		return new Object[] { newModel };
	}
	
}