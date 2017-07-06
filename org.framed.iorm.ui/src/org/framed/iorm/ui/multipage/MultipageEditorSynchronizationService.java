package org.framed.iorm.ui.multipage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.framed.iorm.ui.exceptions.InvalidTypeOfEditorInputException;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.util.GeneralUtil;

/**
 * This operation offers a register and a synchronizing functionality for multipage editors.
 * <p>
 * This is needed if there are multiple multipage editors opened at the same time. Since they all work on different
 * editor inputs and resources a synchronization is needed. For the implementation of this see {@link #synchronize()}.
 * @author Kevin Kassin
 */
public class MultipageEditorSynchronizationService {
	
	/**
	 * the identifiers for the {@link MultipageEditor} and the {@link DiagramTypeProvider}
	 */
	static private final String EDITOR_ID = IdentifierLiterals.EDITOR_ID,
								DIAGRAM_PROVIDER_ID = IdentifierLiterals.DIAGRAM_PROVIDER_ID;
	
	/**
	 * the register for the multipage editors to synchronize
	 */
	static private List<MultipageEditor> registeredEditors = new ArrayList<MultipageEditor>();
	
	/**
	 * a list to save the dirty states of the registered multipage editors
	 */
	static private List<Boolean> dirtyStatesOfEditors = new ArrayList<Boolean>();
	
	/**
	 * operation to add a multipage editor to the register
	 * @param multipageEditor the multipage editor to add
	 */
	public static void registerEditor(MultipageEditor multipageEditor) {
		registeredEditors.add(multipageEditor);
	}
	
	/**
	 * operation to remove a multipage editor from the register
	 * @param multipageEditor the multipage editor to remove
	 */
	public static void deregisterEditor(MultipageEditor multipageEditor) {
		registeredEditors.remove(multipageEditor);
	}
	
	/**
	 * recalculates the values in the list of the dirty states of the registered multipage editors
	 */
	public static void updateDirtyStates() {
		dirtyStatesOfEditors.clear();
		for(MultipageEditor multipageEditor : registeredEditors) {
			dirtyStatesOfEditors.add(multipageEditor.isDirty());
		}
	}
	
	/**
	 * offers a copy of the list of registered editors
	 * <p>
	 * a copy is often needed to avoid ConcurrentModificationExceptions
	 * @return
	 */
	public static List<MultipageEditor> getCopyOfRegisteredEditors() {
		List<MultipageEditor> copyOfRegisteredEditors = new ArrayList<MultipageEditor>();
		for(MultipageEditor multipageEditor : registeredEditors)
			copyOfRegisteredEditors.add(multipageEditor);
		return copyOfRegisteredEditors;
	}
	
	/**
	 * synchronizes mutiple opened multipage editors using the following steps:
	 * <p>
	 * Step 1: It gets the first multipage editor in its register that is dirty to use it as a synchronization base.
			   If there is no dirty multipage editor return instantly since there is no change to synchronize.<br>
	 * Step 2: Its calculates an equivalent editor input for all registered multipage editors except the synchronization base.<br>
	 * Step 3: It closes these multipage editors and uses the equivalent editor inputs to reopen them. 
	 */
	public static void synchronize() {
		//Step 1
		MultipageEditor synchronizationBase = null;
		for(int i = 0; i<dirtyStatesOfEditors.size(); i++) {
			if(dirtyStatesOfEditors.get(i)) {
				synchronizationBase = registeredEditors.get(i);
				break;
				
			}
		}
		if(synchronizationBase == null) return;
		else {
			//Step 2
			List<MultipageEditor> copyOfRegisteredEditors = getCopyOfRegisteredEditors();
			for(MultipageEditor multipageEditor : copyOfRegisteredEditors) {
				if(multipageEditor.getPartName() != synchronizationBase.getPartName()) {
					IEditorInput newEditorInput = 
							getEquivalentEditorInput(synchronizationBase.getEditorInput(), multipageEditor.getEditorInput());
					try {
						//Step 3
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(multipageEditor, false);
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(newEditorInput, EDITOR_ID, false);
					} catch (PartInitException e) { e.printStackTrace(); }	
	}	}	}	}
	
    /**
     * creates an equivalent editor input for another editor input depending on the editor input of the synchronization base
     * using the following steps:
     * <p>
     * Step 1: If the editor input of the synchronization base is of the right type it gets its resource. Otherwise it throws
     * an {@link InvalidTypeOfEditorInputException}.<br>
     * Step 2: If the editor input of the multipage editor that is not the synchronize base is of the type {@link IFileEditorInput}, 
     * 		   return an file editor input for the synchronization base.<br>
     * Step 3: If the editor input of the multipage editor that is not the synchronize base is of the type {@link DiagramEditorInput},
     * 		   get the diagram of the editor input, search with its name the same diagram in the synchronization base and create a
     * 		   diagram editor input with the found diagram.<br> 
     * Step 4: If neither Step 2 or 3 can be executed throw an {@link InvalidTypeOfEditorInputException}.
     * @param baseEditorInput the editor input of the synchronization base
     * @param changedEditorInput the editor input of the multipage editor that is not the synchronization base
     * @return an equivalent editor input for another editor input depending on the editor input of the synchronization base
     * @throws InvalidTypeOfEditorInputException
     */
	private static IEditorInput getEquivalentEditorInput(IEditorInput baseEditorInput, IEditorInput changedEditorInput) {
		//Step 1
		Resource baseResource = null;
		if(baseEditorInput instanceof IFileEditorInput || baseEditorInput instanceof DiagramEditorInput)
			baseResource = GeneralUtil.getResourceFromEditorInput(baseEditorInput);
		else throw new InvalidTypeOfEditorInputException();
		//Step 2
		if(changedEditorInput instanceof IFileEditorInput)
			return GeneralUtil.getIFileEditorInputForResource(baseResource);
		//Step 3
		if(changedEditorInput instanceof DiagramEditorInput) {
			Resource changedEditorResource = GeneralUtil.getResourceFromEditorInput(changedEditorInput);
			Diagram changedEditorDiagram = GeneralUtil.getDiagramForResourceOfDiagramEditorInput(changedEditorResource);
			Diagram equivalentDiagramInBaseResource =
				GeneralUtil.getDiagramFromResourceByName(baseResource, changedEditorDiagram.getName());
			return DiagramEditorInput.createEditorInput(equivalentDiagramInBaseResource, DIAGRAM_PROVIDER_ID);
		} 
		//Step 4
		throw new InvalidTypeOfEditorInputException();		
	}
}
