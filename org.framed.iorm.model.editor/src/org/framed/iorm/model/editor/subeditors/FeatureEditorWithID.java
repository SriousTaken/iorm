package org.framed.iorm.model.editor.subeditors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.framed.iorm.featuremodel.FeatureName;
import org.framed.iorm.featuremodel.FeaturemodelFactory;
import org.framed.iorm.model.Model;
import org.framed.iorm.model.editor.literals.IdentifierLiterals;
import org.framed.iorm.model.editor.util.MethodUtil;
import org.osgi.framework.Bundle;

import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.SelectableFeature;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelReader;
import de.ovgu.featureide.fm.core.job.WorkMonitor;

public class FeatureEditorWithID extends EditorPart {

	//id literals
	private final String FEATUREMODEL_ID = IdentifierLiterals.FEATUREMODEL_ID;
	
	//identifier of editor
	private String id;
	
	//resource, diagram and models of the editors input
	private Resource resource;
	private Diagram diagram;
	private Model rootModel;
	private FeatureModel featureModel;
	
	//configuration of the editor 
	private Configuration configuration;
	
	//path to feature model and standart configuration
	private final Bundle bundleFeatureModel = Platform.getBundle("org.framed.iorm.featuremodel");
	private final URL fileURLToFeatureModel = bundleFeatureModel.getEntry("model.xml");
	private final URL fileURLToStandartConfiguration = bundleFeatureModel.getEntry("/standardframedconfiguration/standardFramedConfiguration.diagram");
	
	//graphical representation
	private Tree tree;
	
	//status of the configuration
	private Label infoLabel;
	
	public FeatureEditorWithID(String id, IEditorInput editorInput) throws FileNotFoundException, UnsupportedModelException {
		super();
		this.id = id;
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
	
	private void getResourceFromEditorInput(IEditorInput editorInput) {
		ResourceSet resourceSet = new ResourceSetImpl();
	 	if (editorInput instanceof IFileEditorInput) {
	    	IFileEditorInput fileInput = (IFileEditorInput) editorInput;
	    	IFile file = fileInput.getFile();
	    	//String inputFilename = file.getName();
	    	resource = resourceSet.createResource(URI.createURI(file.getLocationURI().toString()));
	    	try {
	    		resource.load(null);
	    	} catch (IOException e) { 
	    		e.printStackTrace();
	    		resource = null;
	    	}
	    }
	}
	
	private void readRootModel() {
		if (resource != null) {
			diagram = (Diagram) resource.getContents().get(0);
			rootModel = MethodUtil.getDiagramRootModel(diagram);
		} else
			throw new NullPointerException("The resource could not be loaded.");
	}
	
	private void readFeatureModel() throws FileNotFoundException, UnsupportedModelException {
		FeatureModel featureModel = new FeatureModel(FEATUREMODEL_ID);
		File featureModelFile = null;
	  	try {
	    	featureModelFile = new File(FileLocator.resolve(fileURLToFeatureModel).toURI());
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
	    configuration.getPropagator().update(); //TODO vorher:update(false, null, new WorkMonitor());
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
	      // Load standard configuration for framed
	    	ResourceSet resourceSet = new ResourceSetImpl();
	    	Resource resourceStandartConfiguration =
	          resourceSet.createResource(URI.createURI(FileLocator.resolve(fileURLToStandartConfiguration).toURI().toString()));
	    	try {
	    		resourceStandartConfiguration.load(null);
	    	} catch (IOException e) { e.printStackTrace();
	        	resourceStandartConfiguration = null;
	    	}
	    	Model standardConfigurationModel = (Model) resourceStandartConfiguration.getContents().get(0);
	    	rootModel.setFramedConfiguration(FeaturemodelFactory.eINSTANCE.createFRaMEDConfiguration());

	    	// Apply each feature in the standard configuration to the FeatureIDE Configuration
	    	for (FRaMEDFeature framedFeature : standardConfigurationModel.getFramedConfiguration().getFeatures()) {
	    		if (framedFeature.isManuallySelected()) {
	    			configuration.setManual(framedFeature.getName().getLiteral(), Selection.SELECTED);
	    		} else {
	    			configuration.setManual(framedFeature.getName().getLiteral(), Selection.UNDEFINED);
	    		}
	      }
	      writeConfigurationToModel();
	    }
	}
	
	//TODO: rename myfeature...
	private void writeConfigurationToModel() {
	    FRaMEDConfiguration framedConfiguration = rootModel.getFramedConfiguration();
	    // Remove all existing Features
	    framedConfiguration.getFeatures().clear();
	    List<String> manualFeatureNames = new ArrayList<String>();
	    for (SelectableFeature s : configuration.getManualFeatures()) {
	      manualFeatureNames.add(s.getName());
	    }
	    // Add each selected feature to the FramedConfiguration
	    for (IFeature f : configuration.getSelectedFeatures()) {
	      FRaMEDFeature myFeature = FeaturemodelFactory.eINSTANCE.createFRaMEDFeature();
	      myFeature.setName(FeatureName.getByName(f.getName()));
	      myFeature.setManuallySelected(manualFeatureNames.contains(FeatureName.getByName(f.getName())
	          .getLiteral()));
	      framedConfiguration.getFeatures().add(myFeature);
	    }
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

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
		 
		 

	    // TODO: This block was commented out in FeatureIDE v2.7.5. I did not check if there was a
	    // necissity for this, so this might be used
	    // in the future.

	    // autoselect button
	    // gridData = new GridData();
	    // gridData.horizontalAlignment = SWT.RIGHT;
	    // gridData.verticalAlignment = SWT.CENTER;
	    // autoSelectButton = new Button(compositeTop, SWT.TOGGLE);
	    // autoSelectButton.setText(AUTOSELECT_FEATURES);
	    // autoSelectButton.setLayoutData(gridData);
	    // autoSelectButton.setSelection(false);
	    // autoSelectButton.setEnabled(false);
	    //
	    // autoSelectButton.addSelectionListener(new SelectionListener() {
	    // @Override
	    // public void widgetSelected(SelectionEvent e) {
	    // final Configuration config = configurationEditor.getConfiguration();
	    // if (configurationEditor.isAutoSelectFeatures()) {
	    // invalidFeatures.clear();
	    // configurationEditor.setAutoSelectFeatures(false);
	    // configurationEditor.getConfigJobManager().cancelAllJobs();
	    // config.makeManual(!canDeselectFeatures());
	    // walkTree(new IBinaryFunction<TreeItem, SelectableFeature, Void>() {
	    // @Override
	    // public Void invoke(TreeItem item, SelectableFeature feature) {
	    // refreshItem(item, feature);
	    // return null;
	    // }
	    // }, new FunctionalInterfaces.NullFunction<Void, Void>());
	    // updateInfoLabel(Display.getCurrent());
	    // } else {
	    // if (invalidFeatures.isEmpty()) {
	    // configurationEditor.setAutoSelectFeatures(true);
	    // // updateInfoLabel();
	    // computeTree(true);
	    // } else {
	    // autoSelectButton.setSelection(false);
	    // }
	    // }
	    // }
	    //
	    // @Override
	    // public void widgetDefaultSelected(SelectionEvent e) {}
	    // });

		 // 2. sub composite
		 gridData = new GridData();
		 gridData.horizontalAlignment = SWT.FILL;
		 gridData.verticalAlignment = SWT.FILL;
		 gridData.grabExcessHorizontalSpace = true;
		 gridData.grabExcessVerticalSpace = true;
		 final Composite compositeBottom = new Composite(parent, SWT.BORDER);
		 compositeBottom.setLayout(new FillLayout());
		 compositeBottom.setLayoutData(gridData);

		 createUITree(compositeBottom);
	  }
	 
	 protected void createUITree(Composite parent) {
		 tree = new Tree(parent, SWT.CHECK);
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
		                //changeSelection(item, true);
		                break;
		            }
		          }
		        }
		      }
		    });
		  }
	 
	 private void updateInfoLabel() {
		    Boolean valid = configuration.isValid();
		    infoLabel.setText(valid ? "VALID Configuration" : "INVALID Configuration");
		    //infoLabel.setForeground(valid ? blue : red);
		  }

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
}

