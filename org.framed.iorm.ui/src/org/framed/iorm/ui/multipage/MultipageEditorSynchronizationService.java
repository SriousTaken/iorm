package org.framed.iorm.ui.multipage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.framed.iorm.ui.exceptions.InvalidTypeOfEditorInputException;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.util.GeneralUtil;

//TODO
public class MultipageEditorSynchronizationService {
	
	static private final String EDITOR_ID = IdentifierLiterals.EDITOR_ID,
								DIAGRAM_PROVIDER_ID = IdentifierLiterals.DIAGRAM_PROVIDER_ID;
	
	static private List<MultipageEditor> registeredEditors = new ArrayList<MultipageEditor>();
	static private List<Boolean> dirtyStatesOfEditors = new ArrayList<Boolean>();
	
	public static void registerEditor(MultipageEditor multipageEditor) {
		registeredEditors.add(multipageEditor);
	}
	
	public static void deregisterEditor(MultipageEditor multipageEditor) {
		registeredEditors.remove(multipageEditor);
	}
	
	public static void getDirtyStates() {
		dirtyStatesOfEditors.clear();
		for(MultipageEditor multipageEditor : registeredEditors) {
			dirtyStatesOfEditors.add(multipageEditor.isDirty());
		}
	}
	
	public static void synchronize() {
		//get first multipage editor that is dirty
		MultipageEditor synchronizationBase = null;
		for(int i = 0; i<dirtyStatesOfEditors.size(); i++) {
			if(dirtyStatesOfEditors.get(i)) {
				synchronizationBase = registeredEditors.get(i);
				break;
			}
		}
		if(synchronizationBase == null) System.err.println("failed");
		else {
			System.out.println(synchronizationBase.getPartName());
			List<MultipageEditor> copyOfRegisteredEditors = createCopyOfRegisteredEditors();
			for(MultipageEditor multipageEditor : copyOfRegisteredEditors) {
				if(multipageEditor.getPartName() != synchronizationBase.getPartName()) {
					System.out.println(multipageEditor.getPartName());
					IEditorInput newEditorInput = 
							getEquivalentEditorInput(synchronizationBase.getEditorInput(), multipageEditor.getEditorInput());
					
					
					multipageEditor.setInput(newEditorInput);
					multipageEditor.addPages();
				}	
			}
		}
	}
	
	private static List<MultipageEditor> createCopyOfRegisteredEditors() {
		List<MultipageEditor> copyOfRegisteredEditors = new ArrayList<MultipageEditor>();
		copyOfRegisteredEditors.addAll(registeredEditors);
		return copyOfRegisteredEditors;
	}
	
	//TODO
	private static IEditorInput getEquivalentEditorInput(IEditorInput baseEditorInput, IEditorInput changedEditorInput) {
		Resource baseResource = null;
		if(baseEditorInput instanceof IFileEditorInput || baseEditorInput instanceof DiagramEditorInput)
			baseResource = GeneralUtil.getResourceFromEditorInput(baseEditorInput);
		else throw new InvalidTypeOfEditorInputException();
		
		if(changedEditorInput instanceof IFileEditorInput)
			return GeneralUtil.getIFileEditorInputForResource(baseResource);
		if(changedEditorInput instanceof DiagramEditorInput) {
			Resource changedEditorResource = GeneralUtil.getResourceFromEditorInput(changedEditorInput);
			Diagram changedEditorDiagram = GeneralUtil.getDiagramForResourceOfDiagramEditorInput(changedEditorResource);
			Diagram equivalentDiagramInBaseResource =
					GeneralUtil.getDiagramFromResourceByName(baseResource, changedEditorDiagram.getName());
			return DiagramEditorInput.createEditorInput(equivalentDiagramInBaseResource, DIAGRAM_PROVIDER_ID);
		} 
		throw new InvalidTypeOfEditorInputException();		
	}
}
