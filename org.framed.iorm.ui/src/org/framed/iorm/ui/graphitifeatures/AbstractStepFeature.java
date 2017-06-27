package org.framed.iorm.ui.graphitifeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.ModelElement;
import org.framed.iorm.model.Type;
import org.framed.iorm.ui.exceptions.NoShapeOrConnectionForModelElementFoundException;
import org.framed.iorm.ui.util.MethodUtil;

public abstract class AbstractStepFeature extends AbstractCustomFeature {
	
	public AbstractStepFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void execute(ICustomContext context) {
		Model rootModel = MethodUtil.getDiagramRootModel(this.getDiagram());
		for(ModelElement modelElement : rootModel.getElements()) {
			if(modelElement.getType() == Type.NATURAL_TYPE ||
			   modelElement.getType() == Type.DATA_TYPE ||
			   modelElement.getType() == Type.GROUP ) {
				ContainerShape containerShape = null;
				try { containerShape = (ContainerShape) getShapeElementForModelElement(modelElement); }
				catch(NoShapeOrConnectionForModelElementFoundException e) { e.printStackTrace(); }
				if(containerShape != null) {
					for(Shape shape : containerShape.getChildren()) {
						if(this instanceof StepInFeature) {
							shape.setVisible(false);
							shape.setActive(false);
						}
						if(this instanceof StepOutFeature) {
							shape.setVisible(true);
							shape.setActive(true);
						}
			}	}	}	
			if(modelElement.getType() == Type.INHERITANCE) {
				Connection connection = null;
				try { connection = getConnectionElementForModelElement(modelElement); }
				catch(NoShapeOrConnectionForModelElementFoundException e) { e.printStackTrace(); }
				if(connection != null) { 
					if(this instanceof StepInFeature) {
						connection.setVisible(false);
						connection.setActive(false);
					}
					if(this instanceof StepOutFeature) {
						connection.setVisible(true);
						connection.setActive(true);
		}	}	}	}	
	}
	
	protected Shape getShapeElementForModelElement(ModelElement modelElement) {
		for(Shape shape : this.getDiagram().getChildren()) {
			if(shape.getLink().getBusinessObjects().size() == 1) {	
				if(shape.getLink().getBusinessObjects().get(0).equals(modelElement)) 
					return shape;
		}	}	
		throw new NoShapeOrConnectionForModelElementFoundException();
	}
	
	protected Connection getConnectionElementForModelElement(ModelElement modelElement) {
		for(Connection connection : this.getDiagram().getConnections()) {
			if(connection.getLink().getBusinessObjects().size() == 1) {	
				if(connection.getLink().getBusinessObjects().get(0).equals(modelElement)) 
					return connection;
		}	}
		throw new NoShapeOrConnectionForModelElementFoundException();
	}
}
