package org.framed.iorm.model.editor.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.Type;
import org.framed.iorm.model.editor.literals.LayoutLiterals;

public class MethodUtil {
	
	public static final int HEIGHT_NAME_SHAPE = LayoutLiterals.HEIGHT_NAME_SHAPE,
			 				DATATYPE_CORNER_SIZE = LayoutLiterals.DATATYPE_CORNER_SIZE;
	
	//TODO:  wo called?
	public static final int calculateHorizontalCenter(Type type, int heightOfClassOrRole) {
		if(type == Type.NATURAL_TYPE) 
			return ((heightOfClassOrRole-HEIGHT_NAME_SHAPE)/2)+HEIGHT_NAME_SHAPE;
		if(type == Type.DATA_TYPE)	
			return ((heightOfClassOrRole-HEIGHT_NAME_SHAPE-DATATYPE_CORNER_SIZE)/2)+HEIGHT_NAME_SHAPE;
		return 0;
	}
	
	//TODO: wo called
	public static final Model getDiagramRootModel(Diagram diagram) {
		List<Model> models=  new ArrayList<Model>();
		for(EObject eObject : diagram.eResource().getContents()) {
			if(eObject instanceof Model) models.add((Model) eObject);
		}
		if(models.size()==1) return models.get(0);
		return null;
	}
}
