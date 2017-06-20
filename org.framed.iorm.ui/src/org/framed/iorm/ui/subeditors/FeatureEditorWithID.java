package org.framed.iorm.ui.subeditors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.graphiti.mm.pictograms.Diagram;
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
import org.framed.iorm.featuremodel.FeaturemodelFactory;
import org.framed.iorm.model.Model;
import org.framed.iorm.ui.commands.ConfigurationEditorChangeCommand;
import org.framed.iorm.ui.literals.IdentifierLiterals;
import org.framed.iorm.ui.literals.LayoutLiterals;
import org.framed.iorm.ui.literals.URLLiterals;
import org.framed.iorm.ui.multipage.MultipageEditor;
import org.framed.iorm.ui.util.MethodUtil;

import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.SelectableFeature;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.configuration.TreeElement;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelReader;

@SuppressWarnings("deprecation")
public class FeatureEditorWithID extends EditorPart {
	
	//id literals
	private final String FEATUREMODEL_ID = IdentifierLiterals.FEATUREMODEL_ID;
	
	//layout literals
	private final Color COLOR_VALID_CONFIGURATION = LayoutLiterals.COLOR_VALID_CONFIGURATION,
						COLOR_INVALID_CONFIGURATION = LayoutLiterals.COLOR_INVALID_CONFIGURATION;
	
	//identifier of editor
	private String id;
	
	//multipage editor that uses the feature editor
	private MultipageEditor multipageEditor;
	
	//file, resource, diagram and models of the editors input
	private IFile file;
	private Resource resource;
	private Model rootModel;
	private FeatureModel featureModel;
	
	//configuration of the editor 
	private Configuration configuration;
	
	//path to feature model and standart configuration
	private final URL URL_TO_FEATUREMODEL = URLLiterals.URL_TO_FEATUREMODEL,
					  URL_TO_STANDARD_CONFIGURATION = URLLiterals.URL_TO_STANDARD_CONFIGURATION;
	
	//graphical representation and its items
	private Tree tree;
	private final Map<SelectableFeature, TreeItem> itemMap = new HashMap<SelectableFeature, TreeItem>();
	
	//status of the configuration
	private Label infoLabel;
	
	public FeatureEditorWithID(String id, IEditorInput editorInput, MultipageEditor multipageEditor) throws FileNotFoundException, UnsupportedModelException {
		super();
		this.id = id;
		this.multipageEditor = multipageEditor;
		getResourceFromEditorInput(editorInput);
		readRootModel();
		readFeatureModel();
		loadConfiguration();
		try {
			createStandardFramedConfiguration();
		} catch (URISyntaxException e) { e.printStackTrace(); } 
		  catch (IOException e) { e.printStackTrace(); }
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
	    setInput(input);
	}
	
	public String getId() {
		return id;
	}
	
	public void getResourceFromEditorInput(IEditorInput editorInput) {
		ResourceSet resourceSet = new ResourceSetImpl();
	 	if (editorInput instanceof IFileEditorInput) {
	    	IFileEditorInput fileInput = (IFileEditorInput) editorInput;
	    	file = fileInput.getFile();
	    
	    	resource = resourceSet.createResource(URI.createURI(file.getLocationURI().toString()));
	    	try {
	    		resource.load(null);
	    	} catch (IOException e) { 
	    		e.printStackTrace();
	    		resource = null;
	    	}
	    }
	}
	
	public void readRootModel() {
		if (resource != null) {
			Diagram diagram = (Diagram) resource.getContents().get(0);
			rootModel = MethodUtil.getDiagramRootModel(diagram);
		} else
			throw new NullPointerException("The resource could not be loaded.");
	}
		
	private void readFeatureModel() throws FileNotFoundException, UnsupportedModelException {
		FeatureModel featureModel = new FeatureModel(FEATUREMODEL_ID);
		File featureModelFile = null;
	  	try {
	    	featureModelFile = new File(FileLocator.resolve(URL_TO_FEATUREMODEL).toURI());
	    } catch (URISyntaxException e) { e.printStackTrace(); }
	      catch (IOException e) { e.printStackTrace(); };
	    if(featureModelFile != null) {
	    	new XmlFeatureModelReader(featureModel).readFromFile(featureModelFile);
	    } else 
	    	throw new NullPointerException("The feature model file could not be loaded.");
	    this.featureModel = featureModel;
	}
	
	private void loadConfiguration() {
	    FRaMEDConfiguration framedConfiguration = rootModel.getFramedConfiguration();
	    configuration = new Configuration(featureModel);
	    configuration.getPropagator().update();
	    // if a feature is noted in the .diagram file its set as selected
	    if (framedConfiguration != null) {
	    	for (FRaMEDFeature f : framedConfiguration.getFeatures())
	    		configuration.setManual(f.getName().getLiteral(), Selection.SELECTED);
	    }
	}
	
	private void createStandardFramedConfiguration() throws URISyntaxException, IOException {
	    FRaMEDConfiguration framedConfiguration = rootModel.getFramedConfiguration();
	    if (framedConfiguration == null || 
	    	framedConfiguration.getFeatures() == null || 
	        framedConfiguration.getFeatures().size() < 1) {
	    	//load standard configuration for framed
	    	ResourceSet resourceSet = new ResourceSetImpl();
	    	Resource resourceStandartConfiguration =
	          resourceSet.createResource(URI.createURI(FileLocator.resolve(URL_TO_STANDARD_CONFIGURATION).toURI().toString()));
	    	try {
	    		resourceStandartConfiguration.load(null);
	    	} catch (IOException e) { e.printStackTrace();
	        	resourceStandartConfiguration = null;
	    	}
	    	Model standardConfigurationModel = (Model) resourceStandartConfiguration.getContents().get(0);
	    	rootModel.setFramedConfiguration(FeaturemodelFactory.eINSTANCE.createFRaMEDConfiguration());

	    	//apply each feature in the standard configuration to the FeatureIDE Configuration
	    	for (FRaMEDFeature framedFeature : standardConfigurationModel.getFramedConfiguration().getFeatures()) {
	    		if (framedFeature.isManuallySelected()) {
	    			configuration.setManual(framedFeature.getName().getLiteral(), Selection.SELECTED);
	    		} else {
	    			configuration.setManual(framedFeature.getName().getLiteral(), Selection.UNDEFINED);
	    		}
	      }
	    }
	}
	
	//visualisation related operation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@Override
	public void createPartControl(Composite parent) {
		 // parent composite
		 GridLayout gridLayout = new GridLayout(1, false);
		 gridLayout.verticalSpacing = 4;
		 gridLayout.marginHeight = 2;
		 gridLayout.marginWidth = 0;
		 parent.setLayout(gridLayout);

		 // 1. sub composite
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

		 // info label
		 gridData = new GridData();
		 gridData.horizontalAlignment = SWT.FILL;
		 gridData.grabExcessHorizontalSpace = true;
		 gridData.verticalAlignment = SWT.CENTER;
		 infoLabel = new Label(compositeTop, SWT.NONE);
		 infoLabel.setLayoutData(gridData);
		 updateInfoLabel();	

		 // 2. sub composite
		 gridData = new GridData();
		 gridData.horizontalAlignment = SWT.FILL;
		 gridData.verticalAlignment = SWT.FILL;
		 gridData.grabExcessHorizontalSpace = true;
		 gridData.grabExcessVerticalSpace = true;
		 final Composite compositeBottom = new Composite(parent, SWT.BORDER);
		 compositeBottom.setLayout(new FillLayout());
		 compositeBottom.setLayoutData(gridData);
		 
		 tree = new Tree(compositeBottom, SWT.CHECK);
		 createSelectionListener();
		 updateTree();
		 updateInfoLabel();
	 }
	 
	 private void updateInfoLabel() {
		    Boolean valid = configuration.isValid();
		    infoLabel.setText(valid ? "VALID Configuration" : "INVALID Configuration");
		    infoLabel.setForeground(valid ? COLOR_VALID_CONFIGURATION : COLOR_INVALID_CONFIGURATION);
		  }

	//tree related operations
	//~~~~~~~~~~~~~~~~~~~~~~~ 
	public void createSelectionListener() {
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
		
	public void updateTree() {
	    tree.removeAll();
	    final TreeItem root = new TreeItem(tree, 0);
	    final SelectableFeature rootFeature = configuration.getRoot();
		root.setText(configuration.getRoot().getName());
	    root.setData(configuration.getRoot());
		refreshItem(root, rootFeature);
	    itemMap.put(configuration.getRoot(), root);
	    buildTree(root, configuration.getRoot().getChildren());
		}
		
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
	    }
	}
	
	public void changeSelection(final TreeItem item, final boolean select) {
		ConfigurationEditorChangeCommand cmd = new ConfigurationEditorChangeCommand();
		cmd.setFeatureEditor(this);
		cmd.setBehaviorDiagramEditor(multipageEditor.getBehaviorEditor());
		//cmd.setBehaviorDiagramEditor(multipageEditor.getDataEditor());
		cmd.setLastUsedDiagramEditor(multipageEditor.getLastUsedDiagramEditor());
		cmd.setItem(item);
		cmd.setSelect(select);
		multipageEditor.getBehaviorEditor().getCommandStack().execute(cmd);
	}
	
	public void setSelection(final TreeItem item, final boolean select) {
		SelectableFeature feature = (SelectableFeature) item.getData();
	    if (feature.getAutomatic() == Selection.UNDEFINED) {
	    	switch (feature.getManual()) {
	        	case SELECTED:
	        		set(feature, (select) ? Selection.UNDEFINED : Selection.UNSELECTED);
	        		break;
	        	case UNSELECTED:
	        		set(feature, (select) ? Selection.SELECTED : Selection.UNDEFINED);
	        		break;
	        	case UNDEFINED:
	        		set(feature, (select) ? Selection.SELECTED : Selection.UNSELECTED);
	        		break;
	        	default:
	        		set(feature, Selection.UNDEFINED);
	        }
	    	TreeElement configRootFeature = configuration.getRoot();
	    	updateSelections(itemMap.get(configRootFeature), configRootFeature.getChildren());
	    }
	    updateInfoLabel();
	}
	    
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
	    
	protected void set(SelectableFeature feature, Selection selection) {
		configuration.setManual(feature, selection);
	}
	
	public void updateItemCheckedStatusOnRedo(TreeItem item) {
		SelectableFeature selectableFeature = null;
		//get SelectableFeature for item
		for(Map.Entry<SelectableFeature, TreeItem> entry : itemMap.entrySet()){
		    if(entry.getValue().equals(item)) {
		    	selectableFeature = entry.getKey();
		}	}
		//undo the change of the item checked status
		selectableFeature.setManual(item.getChecked() ? Selection.SELECTED : Selection.UNSELECTED);
		updateTree();
	}
	 
	//unused methods
	//~~~~~~~~~~~~~~	
	@Override
	public void setFocus() {}

	@Override
	public boolean isDirty() {
	    return false;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	    
	@Override
	public void doSaveAs() {}
}

