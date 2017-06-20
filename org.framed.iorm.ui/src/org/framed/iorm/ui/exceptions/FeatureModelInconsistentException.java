package org.framed.iorm.ui.exceptions;

import org.framed.iorm.ui.literals.TextLiterals;

public class FeatureModelInconsistentException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String FEATUREMODEL_INCONSISTENT_MESSAGE = TextLiterals.FEATUREMODEL_INCONSISTENT_MESSAGE;
	
	public FeatureModelInconsistentException() {
		super(FEATUREMODEL_INCONSISTENT_MESSAGE);
	}
}
