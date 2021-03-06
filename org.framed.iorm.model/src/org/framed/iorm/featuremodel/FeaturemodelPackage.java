/**
 */
package org.framed.iorm.featuremodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.framed.iorm.featuremodel.FeaturemodelFactory
 * @model kind="package"
 * @generated
 */
public interface FeaturemodelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "featuremodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://iorm.featuremodel/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.framed.iorm.model.featuremodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FeaturemodelPackage eINSTANCE = org.framed.iorm.featuremodel.impl.FeaturemodelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.framed.iorm.featuremodel.impl.FRaMEDFeatureImpl <em>FRa MED Feature</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.framed.iorm.featuremodel.impl.FRaMEDFeatureImpl
	 * @see org.framed.iorm.featuremodel.impl.FeaturemodelPackageImpl#getFRaMEDFeature()
	 * @generated
	 */
	int FRA_MED_FEATURE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRA_MED_FEATURE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Manually Selected</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRA_MED_FEATURE__MANUALLY_SELECTED = 1;

	/**
	 * The number of structural features of the '<em>FRa MED Feature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRA_MED_FEATURE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>FRa MED Feature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRA_MED_FEATURE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.framed.iorm.featuremodel.impl.FRaMEDConfigurationImpl <em>FRa MED Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.framed.iorm.featuremodel.impl.FRaMEDConfigurationImpl
	 * @see org.framed.iorm.featuremodel.impl.FeaturemodelPackageImpl#getFRaMEDConfiguration()
	 * @generated
	 */
	int FRA_MED_CONFIGURATION = 1;

	/**
	 * The feature id for the '<em><b>Features</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRA_MED_CONFIGURATION__FEATURES = 0;

	/**
	 * The number of structural features of the '<em>FRa MED Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRA_MED_CONFIGURATION_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>FRa MED Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRA_MED_CONFIGURATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.framed.iorm.featuremodel.FeatureName <em>Feature Name</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.framed.iorm.featuremodel.FeatureName
	 * @see org.framed.iorm.featuremodel.impl.FeaturemodelPackageImpl#getFeatureName()
	 * @generated
	 */
	int FEATURE_NAME = 2;

	/**
	 * Returns the meta object for class '{@link org.framed.iorm.featuremodel.FRaMEDFeature <em>FRa MED Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FRa MED Feature</em>'.
	 * @see org.framed.iorm.featuremodel.FRaMEDFeature
	 * @generated
	 */
	EClass getFRaMEDFeature();

	/**
	 * Returns the meta object for the attribute '{@link org.framed.iorm.featuremodel.FRaMEDFeature#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.framed.iorm.featuremodel.FRaMEDFeature#getName()
	 * @see #getFRaMEDFeature()
	 * @generated
	 */
	EAttribute getFRaMEDFeature_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.framed.iorm.featuremodel.FRaMEDFeature#isManuallySelected <em>Manually Selected</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Manually Selected</em>'.
	 * @see org.framed.iorm.featuremodel.FRaMEDFeature#isManuallySelected()
	 * @see #getFRaMEDFeature()
	 * @generated
	 */
	EAttribute getFRaMEDFeature_ManuallySelected();

	/**
	 * Returns the meta object for class '{@link org.framed.iorm.featuremodel.FRaMEDConfiguration <em>FRa MED Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FRa MED Configuration</em>'.
	 * @see org.framed.iorm.featuremodel.FRaMEDConfiguration
	 * @generated
	 */
	EClass getFRaMEDConfiguration();

	/**
	 * Returns the meta object for the containment reference list '{@link org.framed.iorm.featuremodel.FRaMEDConfiguration#getFeatures <em>Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Features</em>'.
	 * @see org.framed.iorm.featuremodel.FRaMEDConfiguration#getFeatures()
	 * @see #getFRaMEDConfiguration()
	 * @generated
	 */
	EReference getFRaMEDConfiguration_Features();

	/**
	 * Returns the meta object for enum '{@link org.framed.iorm.featuremodel.FeatureName <em>Feature Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Feature Name</em>'.
	 * @see org.framed.iorm.featuremodel.FeatureName
	 * @generated
	 */
	EEnum getFeatureName();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	FeaturemodelFactory getFeaturemodelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.framed.iorm.featuremodel.impl.FRaMEDFeatureImpl <em>FRa MED Feature</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.framed.iorm.featuremodel.impl.FRaMEDFeatureImpl
		 * @see org.framed.iorm.featuremodel.impl.FeaturemodelPackageImpl#getFRaMEDFeature()
		 * @generated
		 */
		EClass FRA_MED_FEATURE = eINSTANCE.getFRaMEDFeature();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FRA_MED_FEATURE__NAME = eINSTANCE.getFRaMEDFeature_Name();

		/**
		 * The meta object literal for the '<em><b>Manually Selected</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FRA_MED_FEATURE__MANUALLY_SELECTED = eINSTANCE.getFRaMEDFeature_ManuallySelected();

		/**
		 * The meta object literal for the '{@link org.framed.iorm.featuremodel.impl.FRaMEDConfigurationImpl <em>FRa MED Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.framed.iorm.featuremodel.impl.FRaMEDConfigurationImpl
		 * @see org.framed.iorm.featuremodel.impl.FeaturemodelPackageImpl#getFRaMEDConfiguration()
		 * @generated
		 */
		EClass FRA_MED_CONFIGURATION = eINSTANCE.getFRaMEDConfiguration();

		/**
		 * The meta object literal for the '<em><b>Features</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FRA_MED_CONFIGURATION__FEATURES = eINSTANCE.getFRaMEDConfiguration_Features();

		/**
		 * The meta object literal for the '{@link org.framed.iorm.featuremodel.FeatureName <em>Feature Name</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.framed.iorm.featuremodel.FeatureName
		 * @see org.framed.iorm.featuremodel.impl.FeaturemodelPackageImpl#getFeatureName()
		 * @generated
		 */
		EEnum FEATURE_NAME = eINSTANCE.getFeatureName();

	}

} //FeaturemodelPackage
