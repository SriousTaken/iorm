package org.framed.iorm.model.editor.pattern.connections;

import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractConnectionPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.framed.iorm.model.OrmFactory;
import org.framed.iorm.model.Relation;
import org.framed.iorm.model.Type;
import org.framed.iorm.model.editor.literals.IdentifierLiterals;
import org.framed.iorm.model.editor.literals.LayoutLiterals;
import org.framed.iorm.model.editor.literals.NameLiterals;
import org.framed.iorm.model.editor.util.PropertyUtil;

public class InheritancePattern extends AbstractConnectionPattern {
	
	//name literals
	private static final String INHERITANCE_FEATURE_NAME = NameLiterals.INHERITANCE_FEATURE_NAME;
	
	//ID literals
	private static final String SHAPE_ID_INHERITANCE_CONNECTION = IdentifierLiterals.SHAPE_ID_INHERITANCE_CONNECTION;
	
	//layout literals
	private static final int INHERITANCE_ARROWHEAD_LENGTH = LayoutLiterals.INHERITANCE_ARROWHEAD_LENGTH,
							 INHERITANCE_ARROWHEAD_HEIGHT = LayoutLiterals.INHERITANCE_ARROWHEAD_HEIGHT;
							 
	
	private static final IColorConstant COLOR_CONNECTIONS = LayoutLiterals.COLOR_CONNECTIONS,
										COLOR_INHERITANCE_ARROWHEAD = LayoutLiterals.COLOR_INHERITANCE_ARROWHEAD;		

	//services
	private static final IPeCreateService pictogramElementCreateSerive = Graphiti.getPeCreateService();
	private static final IGaService graphicAlgorithmService = Graphiti.getGaService();
		
	public InheritancePattern() {
		super();
	}
	
	public String getCreateName() {
		return INHERITANCE_FEATURE_NAME;
	}
	
	//add feature
	//~~~~~~~~~~~
	public boolean canAdd(IAddContext addContext) {
		if (addContext instanceof IAddConnectionContext &&
		   addContext.getNewObject() instanceof Relation) {
		   Relation relation = (Relation) addContext.getNewObject();
		   if(relation.getType() == Type.INHERITANCE) {
			   return true;
		   }
		  
	   }
	   return false;
	}
	
	public PictogramElement add(IAddContext addContext) {
		IAddConnectionContext addConnectionContext = (IAddConnectionContext) addContext;
	    Relation addedInheritance = (Relation) addContext.getNewObject();
	    Anchor sourceAnchor = addConnectionContext.getSourceAnchor();
	    Anchor targetAnchor = addConnectionContext.getTargetAnchor();
	    
	    //create connection
	    Connection connection = pictogramElementCreateSerive.createFreeFormConnection(getDiagram());
	    connection.setStart(sourceAnchor);
	    connection.setEnd(targetAnchor);
	    
	    //set graphic algorithms
	    Polyline polyline = graphicAlgorithmService.createPolyline(connection);
	    polyline.setForeground(manageColor(COLOR_CONNECTIONS));
	    
	    //add arrowhead as decorator
	    ConnectionDecorator connectionDecorator;
	    connectionDecorator = pictogramElementCreateSerive.createConnectionDecorator(connection, false, 1.0, true);
	    int points[] = new int[] { -1*INHERITANCE_ARROWHEAD_LENGTH, INHERITANCE_ARROWHEAD_HEIGHT, 		//Point 1
	    						   0, 0, 																//P2
	    						   -1*INHERITANCE_ARROWHEAD_LENGTH, -1*INHERITANCE_ARROWHEAD_HEIGHT };	//P3						 
	    Polygon arrowhead = graphicAlgorithmService.createPolygon(connectionDecorator, points);
	    arrowhead.setForeground(manageColor(COLOR_CONNECTIONS));
	    arrowhead.setBackground(manageColor(COLOR_INHERITANCE_ARROWHEAD));
	    
	    //set property
	    PropertyUtil.setShape_IdValue(polyline, SHAPE_ID_INHERITANCE_CONNECTION);
	    
	    //links
	    link(connection, addedInheritance);
	
	    return connection;
	}
	 
	//create feature
	//~~~~~~~~~~~~~~
	public boolean canCreate(ICreateConnectionContext createContext) {
		// return true if both anchors belong to an natural or data type, both anchors belong to the same kind of type   
		Anchor sourceAnchor = createContext.getSourceAnchor();
	    Anchor targetAnchor = createContext.getTargetAnchor();
	    org.framed.iorm.model.Shape sourceShape = getShapeForAnchor(sourceAnchor);
	    org.framed.iorm.model.Shape targetShape = getShapeForAnchor(targetAnchor);
	    if(sourceShape != null && targetShape != null) {
	    	if(sourceShape.getType() == Type.NATURAL_TYPE) 
	    		if(targetShape.getType() == Type.NATURAL_TYPE) return true;
	    	if(sourceShape.getType() == Type.DATA_TYPE)	
	    		if(targetShape.getType() == Type.DATA_TYPE) return true;
	    }	
	    return false;
	}
	 
	public boolean canStartConnection(ICreateConnectionContext createContext) {
		// return true if start anchor belongs to a natural or data type
		Anchor sourceAnchor = createContext.getSourceAnchor();
		org.framed.iorm.model.Shape sourceShape = getShapeForAnchor(sourceAnchor);
		if(sourceShape != null){	
			if(sourceShape.getType() == Type.NATURAL_TYPE || 
			   sourceShape.getType() == Type.DATA_TYPE) 
			return true;
		}	
		return false;
	}
	  
	public Connection create(ICreateConnectionContext createContext) {
		Anchor sourceAnchor = createContext.getSourceAnchor();
	    Anchor targetAnchor = createContext.getTargetAnchor();
	    org.framed.iorm.model.Shape sourceShape = getShapeForAnchor(sourceAnchor);
	    org.framed.iorm.model.Shape targetShape = getShapeForAnchor(targetAnchor);
		Connection newConnection = null;
	 
	    if (sourceShape != null && targetShape != null) {
	    	// create a new inheritance	 
	    	Relation newInheritance = OrmFactory.eINSTANCE.createRelation();
	    	newInheritance.setType(Type.INHERITANCE); 
	    	if(newInheritance.eResource() != null) getDiagram().eResource().getContents().add(newInheritance);
	    	if(sourceShape.getContainer() == targetShape.getContainer()) {
	    		newInheritance.setContainer(sourceShape.getContainer());
	    		sourceShape.getContainer().getElements().add(newInheritance);
	    	}	
	    	newInheritance.setSource(sourceShape);
	    	newInheritance.setTarget(targetShape);
	    	// add connection for business object
	        AddConnectionContext addContext = new AddConnectionContext(sourceAnchor, targetAnchor);
	        addContext.setNewObject(newInheritance);
	        if(canAdd(addContext)) add(addContext); 	        
	    }
	    return newConnection;
	}

	private org.framed.iorm.model.Shape getShapeForAnchor(Anchor anchor) {
		Object object = null;
		if (anchor != null) { object = getBusinessObjectForPictogramElement(anchor.getParent()); }
		if (object != null) {
			if (object instanceof org.framed.iorm.model.Shape)
				return (org.framed.iorm.model.Shape) object;
		}
		return null;
	}	
}
