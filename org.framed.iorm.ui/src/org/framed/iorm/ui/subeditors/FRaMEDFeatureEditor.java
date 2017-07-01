package org.framed.iorm.ui.subeditors;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.IDiagramEditorInput;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.framed.iorm.featuremodel.FRaMEDConfiguration;
import org.framed.iorm.featuremodel.FRaMEDFeature;
import org.framed.iorm.model.Model;
import org.framed.iorm.ui.commands.ConfigurationEditorChangeCommand;
import org.framed.iorm.ui.exceptions.FeatureModelNotReadableException;
import org.framed.iorm.ui.literals.LayoutLiterals;
import org.framed.iorm.ui.literals.URLLiterals;
import org.framed.iorm.ui.multipage.MultipageEditor;
import org.framed.iorm.ui.util.GeneralUtil;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.SelectableFeature;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.configuration.TreeElement;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

/**
 * the feature editor used by the {@link MultipageEditor}
 * <p>
 * It loads the feature model and its standard configuration. With that informations it can build
 * a tree in which the user can make changes of the configuration. After a change in the tree view
 * a command is used to make the chosen changes to the configuration of the edited diagram.
 * @author Kevin Kassin
 */
public class FRaMEDFeatureEditor extends EditorPart {
		
	/**
	 * URLs to the feature model and its standard configuration gathered from {@link URLLiterals}
	 */
	private final URL URL_TO_FEATUREMODEL = URLLiterals.URL_TO_FEATUREMODEL;
	
	/**
	 * the color values used for the {@link #infoLabel} showing if the chosen configuration is valid or not 
	 * gathered {@link LayoutLiterals}
	 */
	private final Color COLOR_VALID_CONFIGURATION = LayoutLiterals.COLOR_VALID_CONFIGURATION,
						COLOR_INVALID_CONFIGURATION = LayoutLiterals.COLOR_INVALID_CONFIGURATION;
	
	/**
	 * the text label that used to show if the chosen configuration is valid or not
	 */
	private Label infoLabel;
	
	/**
	 * the multipage editor that uses this feature editor
	 * <p>
	 * this variable is used to access the diagram editor of the file edited
	 */
	private MultipageEditor multipageEditor;
	
	/**
	 * the feature model that used to create configuration and use its rules in the {@link ChangeConfigurationsFeature}
	 */
	private IFeatureModel featureModel;
	
	/**
	 * the configuration that used to manage the features in the tree view of the editor 
	 */
	private Configuration configuration;
	
	/**
	 * the tree view of the editor that is used by the user to edit the configuration of the diagram 
	 */
	private Tree tree;
	
	/**
	 * the item of the tree view {@link #tree}
	 */
	private final Map<SelectableFeature, TreeItem> itemMap = new HashMap<SelectableFeature, TreeItem>();
	
	/**
	 * the get method the configuration of the feature editor
	 * @return the class variable {@link #configuration}
	 */
	public Configuration getConfiguration() {
		return configuration;
	}
	
	//constructor and its called function
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Class constructor
	 * <p>
	 * It uses the following operations in order:<br>
	 * (1) {@link #getResourceFromEditorInput}<br>
	 * (2) {@link #readRootModel}<br>
	 * (3) {@link #readFeatureModel}<br>
	 * (4) {@link #getResourceFromEditorInput}<br>
	 * (5) {@link #loadConfiguration}
	 * @param editorInput the opened diagram
	 * @param multipageEditor the multipage editor that uses this editor
	 */
	public FRaMEDFeatureEditor(IEditorInput editorInput, MultipageEditor multipageEditor) {
		super();
		this.multipageEditor = multipageEditor;
		Resource resource = GeneralUtil.getResourceFromEditorInput(editorInput);
		Model rootModel = readRootModel(resource);
		IFeatureModel featureModel = readFeatureModel();
		loadConfiguration(rootModel, featureModel);
	}
	
	/**
	 * initialize method
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
	    setInput(input);
	}
	
	/**
	 * fetches the root model for a resource
	 * @param resource the resource to get the root model from
	 * @return the root model of the resource is not null and the diagram has a root model and return null else
	 */
	private Model readRootModel(Resource resource) {
		if (resource != null) {
			Diagram diagram = (Diagram) resource.getContents().get(0);
			if(GeneralUtil.getDiagramRootModel(diagram) != null)
				return GeneralUtil.getDiagramRootModel(diagram);
		} 
		throw new NullPointerException("The root model could not be loaded.");
	}
		
	/**
	 * fetches the feature model file and gets its feature model
	 * @return the feature model that is used for the {@link MultipageEditor}
	 */
	private IFeatureModel readFeatureModel() {
		File featureModelFile = null;
	  	try {
	    	featureModelFile = new File(FileLocator.resolve(URL_TO_FEATUREMODEL).toURI());
	    } catch (URISyntaxException | IOException e) { e.printStackTrace(); }
	  	FeatureModelManager featureModelManager = FeatureModelManager.getInstance(featureModelFile.toPath());
	  	if(featureModelManager.getLastProblems().containsError()) {
	  		throw new FeatureModelNotReadableException();
	  	}
	  	featureModel = featureModelManager.getObject();
	  	return featureModel;
	}
	
	/**
	 * gets the configuration of the editor using the previously fetched root model and feature model
	 * @param rootModel the root model to get the {@link org.framed.iorm.featuremodel.FRaMEDConfiguration} from
	 * @param featureModel the feature model to instantiate the configuration with
	 */
	private void loadConfiguration(Model rootModel, IFeatureModel featureModel) {
	    FRaMEDConfiguration framedConfiguration = rootModel.getFramedConfiguration();
	    configuration = new Configuration(featureModel);
	    configuration.getPropagator().update();
	    if (framedConfiguration != null) {
	    	for (FRaMEDFeature f : framedConfiguration.getFeatures())
	    		configuration.setManual(f.getName().getLiteral(), Selection.SELECTED);
	    }
	}
	
	//tree related operation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * creates the graphical representation of the tree view creating the following parts:
	 * <p>
	 * Step 1: the parent composite<br>
	 * Step 2: the first composite for the label if configuration is valid or not
	 * Step 3: the label if configuration is valid or not
	 * Step 4: the second composite for the tree
	 * Step 5: the tree view
	 */
	@Override
	public void createPartControl(Composite parent) {
		 //Step 1
		 GridLayout gridLayout = new GridLayout(1, false);
		 gridLayout.verticalSpacing = 4;
		 gridLayout.marginHeight = 2;
		 gridLayout.marginWidth = 0;
		 parent.setLayout(gridLayout);
		 //Step 2
		 GridData gridData = new GridData();
		 gridData.horizontalAlignment = SWT.FILL;
		 gridData.grabExcessHorizontalSpace = true;
		 gridData.grabExcessVerticalSpace = false;
		 gridData.verticalAlignment = SWT.TOP;
		 gridLayout = new GridLayout(1, false);
		 gridLayout.marginHeight = 0;
		 gridLayout.marginWidth = 0;
		 gridLayout.marginLeft = 4;
		 final Composite compositeTop = new Composite(parent, SWT.NONE);
		 compositeTop.setLayout(gridLayout);
		 compositeTop.setLayoutData(gridData);
		 //Step 3
		 gridData = new GridData();
		 gridData.horizontalAlignment = SWT.FILL;
		 gridData.grabExcessHorizontalSpace = true;
		 gridData.verticalAlignment = SWT.CENTER;
		 infoLabel = new Label(compositeTop, SWT.NONE);
		 infoLabel.setLayoutData(gridData);
		 updateInfoLabel();	
		 //Step 4
		 gridData = new GridData();
		 gridData.horizontalAlignment = SWT.FILL;
		 gridData.verticalAlignment = SWT.FILL;
		 gridData.grabExcessHorizontalSpace = true;
		 gridData.grabExcessVerticalSpace = true;
		 final Composite compositeBottom = new Composite(parent, SWT.BORDER);
		 compositeBottom.setLayout(new FillLayout());
		 compositeBottom.setLayoutData(gridData);
		 //Step 5
		 tree = new Tree(compositeBottom, SWT.CHECK);
		 createSelectionListener();
		 updateTree();
		 updateInfoLabel();
	 }
	
	/**
	 * add an selection listener to the tree view
	 * <p>
	 * The added selection listener calls this editors operation {@link #changeSelection} that uses 
	 * the command {@link ConfigurationEditorChangeCommand} to actually change the configuration of the 
	 * diagram.
	 */
	private void createSelectionListener() {
		tree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		
				@Override
				public void widgetSelected(SelectionEvent event) {
					if (event.detail == SWT.CHECK) {
						final TreeItem item = (TreeItem) event.item;
				 		final Object data = item.getData();
				 		if (data instanceof SelectableFeature) {
				 			final SelectableFeature feature = (SelectableFeature) item.getData();
				 			item.setChecked(true);
				 			switch (feature.getAutomatic()) {
				 				case SELECTED: 
				 					item.setChecked(true);
				 					break;
				 				case UNSELECTED:
				 					item.setChecked(false);
				 					break;
				 				case UNDEFINED:
				 					changeSelection(item, true);
				 					break;
	}	}	}	}	}	);	}
	 
	/**
	 * updates the labels text and color if the configuration is valid or not
	 */
	 private void updateInfoLabel() {
		 Boolean valid = configuration.isValid();
		 infoLabel.setText(valid ? "VALID Configuration" : "INVALID Configuration");
		 infoLabel.setForeground(valid ? COLOR_VALID_CONFIGURATION : COLOR_INVALID_CONFIGURATION);
	}

	/**
	 * updates the trees structures depending on the current configuration
	 */
	private void updateTree() {
	    tree.removeAll();
	    final TreeItem root = new TreeItem(tree, 0);
	    final SelectableFeature rootFeature = configuration.getRoot();
		root.setText(configuration.getRoot().getName());
	    root.setData(configuration.getRoot());
		refreshItem(root, rootFeature);
	    itemMap.put(configuration.getRoot(), root);
	    buildTree(root, configuration.getRoot().getChildren());
	}
	
	/**
	 * set the selection of a item in the tree view
	 * @param item the item to set the selection of
	 * @param feature the corresponding feature of the item
	 */
	private void refreshItem(TreeItem item, SelectableFeature feature) {
	    item.setBackground(null);
	    switch (feature.getAutomatic()) {
	      case SELECTED:
	        item.setGrayed(true);
	        item.setForeground(null);
	        item.setChecked(true);
	        break;
	      case UNSELECTED:
	        item.setGrayed(true);
	        item.setChecked(false);
	        break;
	      case UNDEFINED:
	        item.setGrayed(false);
	        item.setForeground(null);
	        item.setChecked(feature.getManual() == Selection.SELECTED);
	        break;
	}	}
		
	/**
	 * builds the tree view from a specific root on
	 * @param parent the root the tree is build from on 
	 * @param children the children of the specific root
	 */
	private void buildTree(final TreeItem parent, final TreeElement[] children) {
		for (int i = 0; i < children.length; i++) {
		 	final TreeElement child = children[i];
		 	if (child instanceof SelectableFeature) {
		 		final SelectableFeature currentFeature = (SelectableFeature) child;
		 		TreeItem childNode = null;
		 		// This try for the case that the parent item is already disposed.
		 		try {
		 			childNode = new TreeItem(parent, 0);
		 		} catch (Exception e) { return; }
		 		childNode.setText(currentFeature.getFeature().getName());
		 		childNode.setData(currentFeature);
		 		refreshItem(childNode, currentFeature);
		 		itemMap.put(currentFeature, childNode);
		 		if (currentFeature.hasChildren()) {
		 			buildTree(childNode, currentFeature.getChildren());
		}	}	}
		parent.setExpanded(true);
	}
	
	/**
	 * updates the selections of the tree view from a specific root on
	 * @param parent the root the tree is build from on 
	 * @param children the children of the specific root
	 */
	private void updateSelections(final TreeItem parent, final TreeElement[] children) {
		for (int i = 0; i < children.length; i++) {
			final TreeElement child = children[i];
	        if (child instanceof SelectableFeature) {
	        	final SelectableFeature currentFeature = (SelectableFeature) child;
	            for (TreeItem t : parent.getItems()) {
	            	if (t.getData().equals(currentFeature)) {
	            		refreshItem(t, currentFeature);
	            		updateSelections(t, currentFeature.getChildren());
	            		break;
	    }	}	}	}
	}
	 
	/**
	 * uses the command {@link ConfigurationEditorChangeCommand} to change the configuration of the 
	 * diagram.
	 * @param item the tree item to change the selection of 
	 * @param select the new selection
	 */
	public void changeSelection(final TreeItem item, final boolean select) {
		ConfigurationEditorChangeCommand command = new ConfigurationEditorChangeCommand();
		command.setFeatureEditor(this);
		command.setBehaviorDiagramEditor(multipageEditor.getDiagramEditor());
		command.setItem(item);
		command.setSelect(select);
		multipageEditor.getDiagramEditor().getCommandStack().execute(command);
	}
	
	/**
	 * sets the selection of a feature in the feature editors configuration
	 * @param item the item to get corresponding feature from
	 * @param select the new selection of the feature
	 */
	public void setSelection(final TreeItem item, final boolean select) {
		SelectableFeature feature = (SelectableFeature) item.getData();
	    if (feature.getAutomatic() == Selection.UNDEFINED) {
	    	switch (feature.getManual()) {
	        	case SELECTED:
	        		configuration.setManual(feature, (select) ? Selection.UNDEFINED : Selection.UNSELECTED);
	        		break;
	        	case UNSELECTED:
	        		configuration.setManual(feature, (select) ? Selection.SELECTED : Selection.UNDEFINED);
	        		break;
	        	case UNDEFINED:
	        		configuration.setManual(feature, (select) ? Selection.SELECTED : Selection.UNSELECTED);
	        		break;
	        	default:
	        		configuration.setManual(feature, Selection.UNDEFINED);
	        }
	    	TreeElement configRootFeature = configuration.getRoot();
	    	updateSelections(itemMap.get(configRootFeature), configRootFeature.getChildren());
	    }
	    updateInfoLabel();
	}
	    
	/**
	 * synchonizes the configuration of the diagram and the feature editor
	 * <p>
	 * This is needed because their can be inconsistent states be between these two configuration
	 * after the use of undo and redo. The method uses the following steps:<br>
	 * Step 1: It searches for features are chosen in the diagram but aren't checked in the 
	 * feature model configuration and checks them in the second configuration<br>
	 * Step 2: If a feature was not chosen in the diagram, but it is checked in the feature model
	 * configuration, uncheck it in the feature editor
	 * Step 3: update tree to show the change to the user
	 */
	public void synchronizeConfigurationEditorAndModelConfiguration() {
		EList<FRaMEDFeature> selectedFeatures = multipageEditor.getDiagramEditor().getSelectedFeatures();
		boolean mapEntryFound = false;
		//Step 1
		for(Map.Entry<SelectableFeature, TreeItem> entry : itemMap.entrySet()){
			for(FRaMEDFeature framedFeature : selectedFeatures) {
				if(entry.getKey().getName().equals(framedFeature.getName().getLiteral())) {
					if (entry.getKey().getAutomatic() == Selection.UNDEFINED) {
						if(!(entry.getValue().getChecked())) {
							setSelection(entry.getValue(), true);
						}
						mapEntryFound = true;
			}	}	}
			//Step 2
			if(!mapEntryFound) {
				if (entry.getKey().getAutomatic() == Selection.UNDEFINED) {
					if((entry.getValue().getChecked())) {
						setSelection(entry.getValue(), false);
			}	}	}
			mapEntryFound = false;
		}
		//Step 3
		updateTree();	
	}
	 
	//unused methods
	//~~~~~~~~~~~~~~	
	
	/**
	 * empty implementation of setFocus
	 */
	@Override
	public void setFocus() {}

	/**
	 * since the resource of this editor is not to save by Graphiti always return false as dirty bit
	 */
	@Override
	public boolean isDirty() {
	    return false;
	}
	
	/**
	 * empty implementation of doSave
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {}
	
	/**
	 * disable the saveAs function
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	    
	/**
	 * empty implementation of doSaveAs
	 */
	@Override
	public void doSaveAs() {}
}

