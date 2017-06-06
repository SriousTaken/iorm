package org.framed.iorm.model.editor.subeditors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class FeatureEditorWithID extends EditorPart {

	//identifier of editor
	private String id;
	
	//resource of the editors input
	private final Resource resource;
	
	public FeatureEditorWithID(String id, Resource resource) {
		super();
		this.resource = resource;
		this.id = id;
	}
	
	public String getId() {
		return id;
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
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
	    setInput(input);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
	
	 /*
	  private MultiPageEditor multiPageEditor;
	  private Configuration configuration;
	  private final Resource cdResource;
	  private Model rootmodel;

	  
	   * The FramedConfiguration Model {@link Model}, which represents the root of the model tree. This does only
	   * contain the standard (most complete) Configuration.
	   
	  private Model standardConfigurationModel;

	  /**
	   * The actual feature model used for the configuration
	   private FeatureModel featureModel = new FeatureModel();

	  /**
	   * The file of the corresponding feature model.
	   
	  File featureModelFile = null;

	  /**
	   * The info label which represents the current status of the configuration
	   
	  private Label infoLabel;

	  /**
	   * The tree which represents the feature model visually
	   
	  private Tree tree;

	  /**
	   * A mapping from a Feature to the respective item of the tree (internal to visual mapping).
	   *	  protected final HashMap<SelectableFeature, TreeItem> itemMap =
	      new HashMap<SelectableFeature, TreeItem>();

	  /**
	   * Current change status of this editor. if there are unsaved changes, the dirty-flag is set to true.
	   
	  protected boolean dirty = false;

	  /**
	   * Creates this editor and performs the following:
	   * <ul>
	   *    <li>Reads the included FeatureModel of FRaMED</li>
	   *    <li>Loads the {@link org.framed.orm.featuremodel.FRaMEDConfiguration <em>FRaMEDConfiguration</em>} from the
	   *    {@link org.eclipse.emf.ecore.resource.Resource Resource} and creates the {@link de.ovgu.featureide.fm.core.configuration.Configuration
	   * <em>Configuration</em>} out of it </li>
	   *    <li> Creates a standard {@link org.framed.orm.featuremodel.impl.FRaMEDConfiguration <em>FRaMEDConfiguration</em>} if the previous step did not successfully load a usable configuration </li>
	   * </ul> 
	   * 
	   * 
	   * @param ormMultiPageEditor
	   * @param resource
	   
	  public FeatureEditorWithID(MultiPageEditor multiPageEditor, Resource resource) {
	    this.multiPageEditor = multiPageEditor;
	    this.cdResource = resource;
	    // Assign rootmodel
	    if (cdResource != null) {
	      rootmodel = (Model) cdResource.getContents().get(0);
	    } else
	      throw new NullPointerException("The resource could not be loaded.");

	    try {
	      readFeatureModel();
	    } catch (FileNotFoundException e) { e.printStackTrace(); }
	      catch (UnsupportedModelException e) { e.printStackTrace(); }
	    loadConfiguration();

	    try {
	      createStandardFramedConfiguration();
	    } catch (URISyntaxException e) { e.printStackTrace(); }
	      catch (IOException e) { e.printStackTrace(); }
	  }

	  /**
	   * Saves the changes made to this editor and creates a standard {@link org.framed.orm.featuremodel.impl.FRaMEDConfiguration <em>FRaMEDConfiguration</em>} if necessary.
	   
	  @Override
	  public void doSave(IProgressMonitor monitor) {
		  // if the internal representation (framed configuration) does not exist or contain features,
		  // create standard config
		  try {
			  createStandardFramedConfiguration();
		  } catch (URISyntaxException e) { e.printStackTrace(); }
	      	catch (IOException e) { e.printStackTrace(); }
		  boolean resourceSaved = saveGraphicalResource();
		  if (!resourceSaved) {
			  return;
		  }
		  dirty = false;
		  firePropertyChange(IEditorPart.PROP_DIRTY);
	  }

	  /**
	   * Saves graphical resource (.crom_dia) handled by the dataEditor.
	   * Edits the FRaMED Configuration according to the changes in this editor. 
	   * 
	   * @return True if saving of model succeeds, otherwise false.
	   
	  private boolean saveGraphicalResource() {
	    Resource currentResource = getCdResource();
	    if (currentResource == null) {
	      return false;
	    }

	    try {
	      currentResource.save(null);
	      return true;
	    } catch (IOException e) { e.printStackTrace(); }
	    return false;
	  }

	  @Override
	  public void init(IEditorSite site, IEditorInput input) throws PartInitException {
	    setSite(site);
	    setInput(input);
	  }

	  /**
	   * Retrieves the standard {@link org.framed.orm.featuremodel.impl.FRaMEDConfiguration <em>FRaMEDConfiguration</em>} and applies the features to
	   * the runtime-{@link org.framed.orm.featuremodel.impl.FRaMEDConfiguration <em>FRaMEDConfiguration</em>}. 
	   * <br>
	   * The standard {@link org.framed.orm.featuremodel.impl.FRaMEDConfiguration <em>FRaMEDConfiguration</em>}
	   * is only created, if at least one of the following is true:
	   * <ul>
	   *    <li> The loaded {@link org.framed.orm.featuremodel.impl.FRaMEDConfiguration <em>FRaMEDConfiguration</em>} is null </li>
	   *    <li> The {@link org.eclipse.emf.common.util.EList <em>EList</em>} of {@link org.framed.orm.featuremodel.FRaMEDFeature <em>FRaMEDFeatures</em>} is null </li>
	   *    <li> The {@link org.eclipse.emf.common.util.EList <em>EList</em>} of {@link org.framed.orm.featuremodel.FRaMEDFeature <em>FRaMEDFeatures</em>} has less than one element</li>
	   * </ul>
	   * 
	   * @throws URISyntaxException
	   * @throws IOException
	   
	  private void createStandardFramedConfiguration() throws URISyntaxException, IOException {
	    FRaMEDConfiguration framedConfiguration = getRootmodel().getFramedConfiguration();
	    if (framedConfiguration == null || framedConfiguration.getFeatures() == null
	        || framedConfiguration.getFeatures().size() < 1) {
	      // Load standard configuration for framed
	      Bundle bundle = Platform.getBundle("org.framed.orm.featuremodel");
	      URL fileURL =
	          bundle.getEntry("/standardframedconfiguration/standardFramedConfiguration.crom_dia");
	      ResourceSet resourceSet = new ResourceSetImpl();
	      Resource resource =
	          resourceSet
	              .createResource(URI.createURI(FileLocator.resolve(fileURL).toURI().toString()));
	      try {
	        resource.load(null);
	      } catch (IOException e) {
	        e.printStackTrace();
	        resource = null;
	      }
	      standardConfigurationModel = (Model) resource.getContents().get(0);
	      rootmodel.setFramedConfiguration(FeaturemodelFactory.eINSTANCE.createFRaMEDConfiguration());

	      // Apply each feature in the standard configuration to the FeatureIDE Configuration
	      for (FRaMEDFeature framedFeature : standardConfigurationModel.getFramedConfiguration()
	          .getFeatures()) {
	        if (framedFeature.isManuallySelected()) {
	          getConfiguration().setManual(framedFeature.getName().getLiteral(), Selection.SELECTED);
	        } else {
	          getConfiguration().setManual(framedFeature.getName().getLiteral(), Selection.UNDEFINED);
	        }
	      }
	      writeConfigurationToModel();
	    }
	  }

	  @Override
	  public boolean isDirty() {
	    return dirty;
	  }

	  @Override
	  public boolean isSaveAsAllowed() {
	    return false;
	  }



	  @Override
	  public void setFocus() {}

	  /**
	   * Sets the state of the editor to dirty. This results in the eclipse workspace displaying that a saveable change has been made.
	   
	  public void setDirty() { 
	    dirty = true;
	    firePropertyChange(PROP_DIRTY);
	  }

	  /**
	   * Sets the parent of this editor.
	   
	  public void setOrmMultiPageEditor(MultiPageEditor multiPageEditor) {
	    this.multiPageEditor = multiPageEditor;
	  }

	  /**
	   * Gets the parent of this editor.
	   * @return The {@link ORMMultiPageEditor} parent of this editor.
	   
	  public MultiPageEditor getMultiPageEditor() {
	    return multiPageEditor;
	  }

	  /**
	   * Creates the visual representation of the Feature Model and the respective {@link org.framed.orm.featuremodel.impl.FRaMEDConfiguration <em>FRaMEDConfiguration</em>}.
	   * Attaches a selection-listener to the Tree.
	   * 
	   * @param parent
	   
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
	  
	  /**
	   * Performs the adjustments necessary if the selection changes.
	   * @param item
	   * @param select
	   */
	  /*
	  public void changeSelection(final TreeItem item, final boolean select) {
		  FeatureModelConfigurationEditorChangeCommand cmd = new FeatureModelConfigurationEditorChangeCommand();
		  
		  cmd.setEditor(this);
		  cmd.setItem(item);
		  cmd.setSelect(select);
		  this.ormMultiPageEditor.getBehaviorEditor().getCommandStack().execute(cmd);   
	  }
	  
	public void  setSelection(final TreeItem item, final boolean select) {
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
	      if (!dirty) {
	        setDirty();
	      }
	      // if (ormMultiPageEditor.isAutoSelectFeatures()) {
	      TreeElement configRootFeature = getConfiguration().getRoot();
	      updateSelections(itemMap.get(configRootFeature), configRootFeature.getChildren());
	      // } else {
	      // refreshItem(item, feature);
	      // }
	    }
	    updateInfoLabel();


	  }


	  /**
	   * Sets the visible state of the {@link TreeItem} which represents a feature from the configuration.
	   * @param item
	   * @param feature
	   
	  protected void refreshItem(TreeItem item, SelectableFeature feature) {
	    item.setBackground(null);
	    item.setFont(treeItemStandardFont);
	    switch (feature.getAutomatic()) {
	      case SELECTED:
	        item.setGrayed(true);
	        item.setForeground(null);
	        item.setChecked(true);
	        break;
	      case UNSELECTED:
	        item.setGrayed(true);
	        item.setForeground(gray);
	        item.setChecked(false);
	        break;
	      case UNDEFINED:
	        item.setGrayed(false);
	        item.setForeground(null);
	        item.setChecked(feature.getManual() == Selection.SELECTED);
	        break;
	    }
	  }

	  /**
	   * Sets the selected feature in the {@link de.ovgu.featureide.fm.core.configuration.Configuration Configuration} 
	   * (provided by FeatureIDE), which then performs the necessary changes (constraints like inclusions). Furthermore, this method writes
	   * the current configuration to the model.
	   * @param feature
	   * @param selection
	   
	  protected void set(SelectableFeature feature, Selection selection) {
		getConfiguration().setManual(feature, selection);
	    writeConfigurationToModel();
	  }


	  /**
	   * Getter for the currently used {@link Configuration}.
	   * 
	   * @return The currently used {@link Configuration}.
	     public Configuration getConfiguration() {
	    return configuration;
	  }

	  /**
	   * Getter for the resource used in this editor.
	   * @return
	   
	  public Resource getCdResource() {
	    return cdResource;
	  }

	  /**
	   * Getter for the model used in this editor.
	   * @return
	   
	  public Model getRootmodel() {
	    return rootmodel;
	  }

	  /**
	   * Removes all existing {@link org.framed.orm.featuremodel.FRaMEDFeature FRaMEDFeature}s in the current 
	   * {@link org.framed.orm.featuremodel.impl.FRaMEDConfiguration FRaMEDConfiguration} and writes the currently selected ones to the
	   * {@link org.framed.orm.featuremodel.impl.FRaMEDConfiguration FRaMEDConfiguration} (and therefore to the model).
	   * <br>
	   * <em> Note that the graphical model is NOT saved in this step</em>
	   
	  private void writeConfigurationToModel() {
	    Configuration configuration = getConfiguration();
	    FRaMEDConfiguration framedConfiguration = getRootmodel().getFramedConfiguration();
	    // Remove all existing Features
	    framedConfiguration.getFeatures().clear();
	    List<String> manualFeatureNames = new ArrayList<String>();
	    for (SelectableFeature s : configuration.getManualFeatures()) {
	      manualFeatureNames.add(s.getName());
	    }
	    // Add each selected feature to the FramedConfiguration
	    for (Feature f : configuration.getSelectedFeatures()) {
	      FRaMEDFeature myFeature = FeaturemodelFactory.eINSTANCE.createFRaMEDFeature();
	      myFeature.setName(FeatureName.getByName(f.getName()));
	      myFeature.setManuallySelected(manualFeatureNames.contains(FeatureName.getByName(f.getName())
	          .getLiteral()));
	      framedConfiguration.getFeatures().add(myFeature);
	    }
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

	  /**
	   * Updates the info label according to the status of the current {@link de.ovgu.featureide.fm.core.configuration.Configuration Configuration}
	   
	  private void updateInfoLabel() {
	    Boolean valid = configuration.isValid();
	    infoLabel.setText(valid ? "VALID Configuration" : "INVALID Configuration");
	    infoLabel.setForeground(valid ? blue : red);
	  }

	  /**
	   * Updates and builds the tree.
	   
	  public void updateTree() {
	    final Configuration configuration = getConfiguration();
	    tree.removeAll();
	    final TreeItem root = new TreeItem(tree, 0);
	    final SelectableFeature rootFeature = configuration.getRoot();
	    root.setText(configuration.getRoot().getName());
	    root.setData(configuration.getRoot());
	    refreshItem(root, rootFeature);
	    itemMap.put(configuration.getRoot(), root);
	    buildTree(root, configuration.getRoot().getChildren());
	    updateInfoLabel();
	  }

	  /**
	   * Builds the tree starting from the root element of the {@link de.ovgu.featureide.fm.core.configuration.Configuration Configuration}.
	   * @param parent
	   * @param children
	   
	  private void buildTree(final TreeItem parent, final TreeElement[] children) {
	    for (int i = 0; i < children.length; i++) {
	      final TreeElement child = children[i];
	      if (child instanceof SelectableFeature) {
	        final SelectableFeature currentFeature = (SelectableFeature) child;
	        if (!currentFeature.getFeature().isHidden()) {
	          TreeItem childNode = null;
	          // This try for the case that the parent item is already disposed.
	          try {
	            childNode = new TreeItem(parent, 0);
	          } catch (Exception e) {

	            return;
	          }
	          childNode.setText(currentFeature.getFeature().getDisplayName());
	          childNode.setData(currentFeature);
	          refreshItem(childNode, currentFeature);
	          itemMap.put(currentFeature, childNode);
	          if (currentFeature.hasChildren()) {
	            buildTree(childNode, currentFeature.getChildren());
	          }
	        }
	      }
	    }
	    parent.setExpanded(true);
	  }

	  /**
	   * Updates the whole tree by checking each {@link TreeItem} if it is still in the current {@link de.ovgu.featureide.fm.core.configuration.Configuration Configuration}
	   * and updating its status respectively.
	   * @param parent
	   * @param children
	   
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
	          }
	        }
	      }
	    }
	  }


	  /**
	   * Reads the included Feature Model from the bundle org.framed.orm.featuremodel
	   * 
	   * @throws FileNotFoundException
	   * @throws UnsupportedModelException
	   
	  private void readFeatureModel() throws FileNotFoundException, UnsupportedModelException {
	    final FeatureModel featureModel = new FeatureModel();

	    Bundle bundle = Platform.getBundle("org.framed.orm.featuremodel");
	    URL fileURL = bundle.getEntry("model.xml");
	    try {
	      featureModelFile = new File(FileLocator.resolve(fileURL).toURI());
	    } catch (URISyntaxException e1) {
	      e1.printStackTrace();
	    } catch (IOException e1) {
	      e1.printStackTrace();
	    };

	    new XmlFeatureModelReader(featureModel).readFromFile(featureModelFile);
	    this.featureModel = featureModel;
	  }

	  /**
	   * Loads the {@link org.framed.orm.featuremodel.FRaMEDConfiguration <em>FRaMEDConfiguration</em>} from the currently loaded 
	   * {@link org.framed.orm.model.Model <em>Model</em>} and creates the {@link de.ovgu.featureide.fm.core.configuration.Configuration
	   * <em>Configuration</em>} (from FeatureIDE) out of it.
	   
	  private void loadConfiguration() {
	    FRaMEDConfiguration framedConfiguration = getRootmodel().getFramedConfiguration();
	    configuration = new Configuration(featureModel);
	    configuration.getPropagator().update(false, null, new WorkMonitor());
	    EList<FRaMEDFeature> featuresToRemove = new BasicEList<FRaMEDFeature>();
	    // Check if the FeatureName used in the .crom_dia file
	    // corresponds with an actually existing feature in the feature model
	    if (framedConfiguration != null) {
	      for (FRaMEDFeature f : framedConfiguration.getFeatures()) {
	        if (featureModel.getFeature(f.getName().getLiteral()) != null) {
	          configuration.setManual(f.getName().getLiteral(), Selection.SELECTED);
	        } else {
	          featuresToRemove.add(f);
	        }
	      }
	      for (FRaMEDFeature toRemove : featuresToRemove) {
	        framedConfiguration.getFeatures().remove(toRemove);
	      }
	    }
	  }

	  /**
	   * Getter for the used {@link FeatureModel}.
	   * @return The used {@link FeatureModel}
	   
	  public FeatureModel getFeatureModel() {
	    return featureModel;
	  }
	  */
}

