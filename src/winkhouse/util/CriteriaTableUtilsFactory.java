package winkhouse.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;
import winkhouse.util.criteriatable.AffittiCanEditAValue;
import winkhouse.util.criteriatable.AnagraficheCanEditAValue;
import winkhouse.util.criteriatable.ColloquiCanEditAValue;
import winkhouse.util.criteriatable.ImmobiliCanEditAValue;
import winkhouse.util.criteriatable.celleditors.CampiPersonaliDaACellEditorFactory;
import winkhouse.util.criteriatable.celleditors.CanEditDaValue;
import winkhouse.util.criteriatable.celleditors.da.AgenteInseritoreCellEditor;
import winkhouse.util.criteriatable.celleditors.da.AnagraficheSearchEditor;
import winkhouse.util.criteriatable.celleditors.da.CampiPersonaliBooleanCellEditor;
import winkhouse.util.criteriatable.celleditors.da.CampiPersonaliEnumCellEditor;
import winkhouse.util.criteriatable.celleditors.da.CheckBoxBooleanCellEditor;
import winkhouse.util.criteriatable.celleditors.da.ClasseClienteCellEditor;
import winkhouse.util.criteriatable.celleditors.da.ClasseEnergeticaCellEditor;
import winkhouse.util.criteriatable.celleditors.da.CriteriaSWTCalendarCellEditor;
import winkhouse.util.criteriatable.celleditors.da.CriteriaTextCellEditor;
import winkhouse.util.criteriatable.celleditors.da.ImmobiliSearchCellEditor;
import winkhouse.util.criteriatable.celleditors.da.RiscaldamentoCellEditor;
import winkhouse.util.criteriatable.celleditors.da.StatoConservativoCellEditor;
import winkhouse.util.criteriatable.celleditors.da.TipologiaColloquiCellEditor;
import winkhouse.util.criteriatable.celleditors.da.TipologiaImmobiliCellEditor;
import winkhouse.util.criteriatable.celleditors.da.TipologiaStanzeCellEditor;
import winkhouse.util.criteriatable.valueprovider.a.CalendarAValueProvider;
import winkhouse.util.criteriatable.valueprovider.a.CampiPersonaliAValueProvider;
import winkhouse.util.criteriatable.valueprovider.a.TextAValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.AgenteDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.AnagraficheDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.BooleanCampiPersonaliDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.BooleanDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.CalendarDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.CampiPersonaliDaAValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.ClasseEnergeticaDaAValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.ClassiClientiDaAValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.EnumCampiPersonaliDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.ImmobiliDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.RiscaldamentoDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.StatoConservativoDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.TextDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.TipologiaColloquiDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.TipologiaImmobileDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.TipologiaStanzeDaValueProvider;
import winkhouse.widgets.CriteriaTableViewerFactory;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.IPropertiesFieldDescriptor;
import winkhouse.widgets.data.editing.IDaACellEditor;
import winkhouse.widgets.data.factory.DaACanEditCellHandlerFactory;
import winkhouse.widgets.data.factory.DaACellEditorFactory;
import winkhouse.widgets.data.factory.DaAValueProviderFactory;
import winkhouse.widgets.data.factory.GridDataFactory;
import winkhouse.widgets.data.factory.ObjectPropertiesFactory;

public class CriteriaTableUtilsFactory {

	private ArrayList<IPropertiesFieldDescriptor> ownerProperties = null;
	private ImmobiliMethodsFactory immobiliMethodsFactory = null;
	private AnagraficheMethodsFactory anagraficheMethodsFactory = null;
	private AffittiImmobiliMethodsFactory affittiMethodsFactory = null;
	private ColloquiMethodsFactory colloquiMethodsFactory = null;
	private ImmobiliCanEditAValue immobiliCanEditAValue = null;
	private ColloquiCanEditAValue colloquiCanEditAValue = null;
	private AnagraficheCanEditAValue anagraficheCanEditAValue = null;
	private AffittiCanEditAValue affittiCanEditAValue = null;
	private CanEditDaValue canEditDaValue = null;
	private DaAValueProviderFactory immobiliDaValueProviderFactory = null;
	private DaAValueProviderFactory immobiliAValueProviderFactory = null;
	private DaAValueProviderFactory anagraficheDaValueProviderFactory = null;
	private DaAValueProviderFactory anagraficheAValueProviderFactory = null;
	private DaAValueProviderFactory colloquiDaValueProviderFactory = null;
	private DaAValueProviderFactory colloquiAValueProviderFactory = null;
	
	private IDaACellEditor tipologiaImmobiliCellEditor = null; 
	
	private ArrayList<String> colloquiExcludeMethods = new ArrayList<String>(){{
		add("getCodAgenteInseritore");
		add("getCodImmobileAbbinato");
		add("getCodTipologiaColloquio");
		add("getNomeCognomeAgenteInseritore");
		add("getCodColloquio");
		add("getAllegati");
	}};
			
	public class ImmobiliMethodsFactory extends GridDataFactory{

		@Override
		public ArrayList<IPropertiesFieldDescriptor> getOwnerProperties() {
			if (ownerProperties == null){
				ownerProperties = new ArrayList<IPropertiesFieldDescriptor>();
				Iterator<WinkhouseUtils.ObjectSearchGetters> it = WinkhouseUtils.getInstance().getImmobileSearchGetters().iterator();
				while (it.hasNext()) {
					ownerProperties.add((IPropertiesFieldDescriptor) it.next());
					
				}
			}
			return ownerProperties;
		}
		
	}
	
	public class AnagraficheMethodsFactory extends GridDataFactory{

		@Override
		public ArrayList<IPropertiesFieldDescriptor> getOwnerProperties() {
			if (ownerProperties == null){
				ownerProperties = new ArrayList<IPropertiesFieldDescriptor>();
				Iterator<WinkhouseUtils.ObjectSearchGetters> it = WinkhouseUtils.getInstance().getAnagraficheSearchGetters().iterator();
				while (it.hasNext()) {
					ownerProperties.add((IPropertiesFieldDescriptor) it.next());
					
				}
			}
			return ownerProperties;
		}
		
	} 

	public class AffittiImmobiliMethodsFactory extends GridDataFactory{

		@Override
		public ArrayList<IPropertiesFieldDescriptor> getOwnerProperties() {
			if (ownerProperties == null){
				ownerProperties = new ArrayList<IPropertiesFieldDescriptor>();
				Iterator<WinkhouseUtils.ObjectSearchGetters> it = WinkhouseUtils.getInstance().getImmobileAffittiSearchGetters().iterator();
				while (it.hasNext()) {
					ownerProperties.add((IPropertiesFieldDescriptor) it.next());
					
				}
			}
			return ownerProperties;
		}
		
	}

	public class ColloquiMethodsFactory extends GridDataFactory{

		@Override
		public ArrayList<IPropertiesFieldDescriptor> getOwnerProperties() {
			
			if (ownerProperties == null){
				
				ownerProperties = new ArrayList<IPropertiesFieldDescriptor>();
				Iterator<WinkhouseUtils.ObjectSearchGetters> it = WinkhouseUtils.getInstance().getColloquiSearchGetters().iterator();
				
				while (it.hasNext()) {
					ObjectSearchGetters osg = (ObjectSearchGetters)it.next();
					if (!colloquiExcludeMethods.contains(osg.getMethodName())){
						ownerProperties.add((IPropertiesFieldDescriptor) osg);
					}
					
				}
			}
			return ownerProperties;
		}
		
	}
					
  	public ImmobiliMethodsFactory getImmobiliMethodsFactory() {
		if (immobiliMethodsFactory == null){
			immobiliMethodsFactory = new ImmobiliMethodsFactory();
		}
		return immobiliMethodsFactory;
	}
	

	public void setImmobiliMethodsFactory(
			ImmobiliMethodsFactory immobiliMethodsFactory) {
		this.immobiliMethodsFactory = immobiliMethodsFactory;
	}


	public AnagraficheMethodsFactory getAnagraficheMethodsFactory() {
		if (anagraficheMethodsFactory == null){
			anagraficheMethodsFactory = new AnagraficheMethodsFactory();
		}
		return anagraficheMethodsFactory;
	}


	public void setAnagraficheMethodsFactory(
			AnagraficheMethodsFactory anagraficheMethodsFactory) {
		this.anagraficheMethodsFactory = anagraficheMethodsFactory;
	}


	public AffittiImmobiliMethodsFactory getAffittiMethodsFactory() {
		if (affittiMethodsFactory == null){
			affittiMethodsFactory = new AffittiImmobiliMethodsFactory();
		}
		return affittiMethodsFactory;
	}


	public void setAffittiMethodsFactory(
			AffittiImmobiliMethodsFactory affittiMethodsFactory) {
		this.affittiMethodsFactory = affittiMethodsFactory;
	}


	public ColloquiMethodsFactory getColloquiMethodsFactory() {
		if (colloquiMethodsFactory == null){
			colloquiMethodsFactory = new ColloquiMethodsFactory(); 
		}
		return colloquiMethodsFactory;
	}


	public void setColloquiMethodsFactory(
			ColloquiMethodsFactory colloquiMethodsFactory) {
		this.colloquiMethodsFactory = colloquiMethodsFactory;
	}


	public ImmobiliCanEditAValue getImmobiliCanEditAValue() {
		if (immobiliCanEditAValue == null){
			immobiliCanEditAValue = new ImmobiliCanEditAValue();
		}
		return immobiliCanEditAValue;
	}
	
	public ColloquiCanEditAValue getColloquiCanEditAValue() {
		if (colloquiCanEditAValue == null){
			colloquiCanEditAValue = new ColloquiCanEditAValue();
		}
		return colloquiCanEditAValue;
	}

	public AnagraficheCanEditAValue getAnagraficheCanEditAValue() {
		if (anagraficheCanEditAValue == null){
			anagraficheCanEditAValue = new AnagraficheCanEditAValue();
		}
		return anagraficheCanEditAValue;
	}
	
	public AffittiCanEditAValue getAffittiCanEditAValue() {
		if (affittiCanEditAValue == null){
			affittiCanEditAValue = new AffittiCanEditAValue();
		}
		return affittiCanEditAValue;
	}
	
	public void setCanEditAValue(ImmobiliCanEditAValue canEditAValue) {
		this.immobiliCanEditAValue = canEditAValue;
	}


	public CanEditDaValue getCanEditDaValue() {
		if (canEditDaValue == null){
			canEditDaValue = new CanEditDaValue();
		}
		return canEditDaValue;
	}


	public void setCanEditDaValue(CanEditDaValue canEditDaValue) {
		this.canEditDaValue = canEditDaValue;
	} 
	
	public DaACellEditorFactory getImmobiliDaCellEditorFactory(Composite c){
		
		CampiPersonaliDaACellEditorFactory daACellEditorFactory = new CampiPersonaliDaACellEditorFactory();
		
		daACellEditorFactory.addCellEditor(new TipologiaImmobiliCellEditor(c));
		daACellEditorFactory.addCellEditor(new TipologiaStanzeCellEditor(c));
		daACellEditorFactory.addCellEditor(new StatoConservativoCellEditor(c));
		daACellEditorFactory.addCellEditor(new RiscaldamentoCellEditor(c));
		daACellEditorFactory.addCellEditor(new ClasseEnergeticaCellEditor(c));
		daACellEditorFactory.addCellEditor(new AgenteInseritoreCellEditor(c));
		daACellEditorFactory.addCellEditor(new CriteriaTextCellEditor(c));
		daACellEditorFactory.addCellEditor(new CriteriaSWTCalendarCellEditor(c,null));
		daACellEditorFactory.addCellEditor(new CampiPersonaliBooleanCellEditor(c));
		daACellEditorFactory.addCellEditor(new CampiPersonaliEnumCellEditor(c));		
		
		return daACellEditorFactory;
	}

	public DaACellEditorFactory getColloquiDaCellEditorFactory(Composite c){
		
		CampiPersonaliDaACellEditorFactory daACellEditorFactory = new CampiPersonaliDaACellEditorFactory();
		
		daACellEditorFactory.addCellEditor(new TipologiaColloquiCellEditor(c));		
		daACellEditorFactory.addCellEditor(new AgenteInseritoreCellEditor(c));
		daACellEditorFactory.addCellEditor(new CriteriaTextCellEditor(c));
		daACellEditorFactory.addCellEditor(new CriteriaSWTCalendarCellEditor(c,null));
		daACellEditorFactory.addCellEditor(new AnagraficheSearchEditor(c));
		daACellEditorFactory.addCellEditor(new ImmobiliSearchCellEditor(c));
		daACellEditorFactory.addCellEditor(new CheckBoxBooleanCellEditor(c));	
		daACellEditorFactory.addCellEditor(new CampiPersonaliBooleanCellEditor(c));
		daACellEditorFactory.addCellEditor(new CampiPersonaliEnumCellEditor(c));		

		return daACellEditorFactory;
	}

	public DaACellEditorFactory getAnagraficheDaCellEditorFactory(Composite c){
		
		CampiPersonaliDaACellEditorFactory daACellEditorFactory = new CampiPersonaliDaACellEditorFactory();
		
		daACellEditorFactory.addCellEditor(new ClasseClienteCellEditor(c));
		daACellEditorFactory.addCellEditor(new AgenteInseritoreCellEditor(c));
		daACellEditorFactory.addCellEditor(new CriteriaTextCellEditor(c));
		daACellEditorFactory.addCellEditor(new CriteriaSWTCalendarCellEditor(c,null));
		daACellEditorFactory.addCellEditor(new CampiPersonaliBooleanCellEditor(c));
		daACellEditorFactory.addCellEditor(new CampiPersonaliEnumCellEditor(c));		

		return daACellEditorFactory;
	}

	public DaACellEditorFactory getImmobiliACellEditorFactory(Composite c){
		
		DaACellEditorFactory daACellEditorFactory = new DaACellEditorFactory();
		
		daACellEditorFactory.addCellEditor(new winkhouse.util.criteriatable.celleditors.a.CriteriaSWTCalendarCellEditor(c,null));
		daACellEditorFactory.addCellEditor(new CriteriaTextCellEditor(c));
		return daACellEditorFactory;
	}

	public DaACellEditorFactory getColloquiACellEditorFactory(Composite c){
		
		DaACellEditorFactory daACellEditorFactory = new DaACellEditorFactory();
		
		daACellEditorFactory.addCellEditor(new winkhouse.util.criteriatable.celleditors.a.CriteriaSWTCalendarCellEditor(c,null));
		daACellEditorFactory.addCellEditor(new CriteriaTextCellEditor(c));
		return daACellEditorFactory;
	}

	public DaACellEditorFactory getAnagraficheACellEditorFactory(Composite c){
		
		DaACellEditorFactory daACellEditorFactory = new DaACellEditorFactory();
		
		daACellEditorFactory.addCellEditor(new CriteriaTextCellEditor(c));
		daACellEditorFactory.addCellEditor(new CriteriaSWTCalendarCellEditor(c,null));
		
		return daACellEditorFactory;
	}

	public DaAValueProviderFactory getImmobiliDaValueProviderFactory(){
		
		if (immobiliDaValueProviderFactory == null){
			immobiliDaValueProviderFactory = new DaAValueProviderFactory();
			immobiliDaValueProviderFactory.addContentProvider(new TipologiaImmobileDaValueProvider());
			immobiliDaValueProviderFactory.addContentProvider(new TipologiaStanzeDaValueProvider());
			immobiliDaValueProviderFactory.addContentProvider(new StatoConservativoDaValueProvider());
			immobiliDaValueProviderFactory.addContentProvider(new TextDaValueProvider());
			immobiliDaValueProviderFactory.addContentProvider(new RiscaldamentoDaValueProvider());
			immobiliDaValueProviderFactory.addContentProvider(new ClasseEnergeticaDaAValueProvider());
			immobiliDaValueProviderFactory.addContentProvider(new CampiPersonaliDaAValueProvider());
			immobiliDaValueProviderFactory.addContentProvider(new AgenteDaValueProvider());
			immobiliDaValueProviderFactory.addContentProvider(new CalendarDaValueProvider());
			immobiliDaValueProviderFactory.addContentProvider(new BooleanCampiPersonaliDaValueProvider());
			immobiliDaValueProviderFactory.addContentProvider(new EnumCampiPersonaliDaValueProvider());
		}
		
		return immobiliDaValueProviderFactory;
	}

	public DaAValueProviderFactory getAnagraficheDaValueProviderFactory(){
		
		if (anagraficheDaValueProviderFactory == null){
			anagraficheDaValueProviderFactory = new DaAValueProviderFactory();
			anagraficheDaValueProviderFactory.addContentProvider(new ClassiClientiDaAValueProvider());
			anagraficheDaValueProviderFactory.addContentProvider(new TextDaValueProvider());
			anagraficheDaValueProviderFactory.addContentProvider(new CalendarDaValueProvider());
			anagraficheDaValueProviderFactory.addContentProvider(new CampiPersonaliDaAValueProvider());
			anagraficheDaValueProviderFactory.addContentProvider(new BooleanCampiPersonaliDaValueProvider());
			anagraficheDaValueProviderFactory.addContentProvider(new EnumCampiPersonaliDaValueProvider());
			
		}
		
		return anagraficheDaValueProviderFactory;
	}
	
	public DaAValueProviderFactory getImmobiliAValueProviderFactory(){
		
		if (immobiliAValueProviderFactory == null){
			immobiliAValueProviderFactory = new DaAValueProviderFactory();
			immobiliAValueProviderFactory.addContentProvider(new CampiPersonaliAValueProvider());
			immobiliAValueProviderFactory.addContentProvider(new CalendarAValueProvider());
			immobiliAValueProviderFactory.addContentProvider(new TextAValueProvider());
			
		}
		
		return immobiliAValueProviderFactory;
	}

	public DaAValueProviderFactory getAnagraficheAValueProviderFactory(){
		
		if (anagraficheAValueProviderFactory == null){
			anagraficheAValueProviderFactory = new DaAValueProviderFactory();
			anagraficheAValueProviderFactory.addContentProvider(new CampiPersonaliAValueProvider());
			anagraficheAValueProviderFactory.addContentProvider(new CalendarAValueProvider());
			anagraficheAValueProviderFactory.addContentProvider(new TextAValueProvider());
			
		}
		
		return anagraficheAValueProviderFactory;
	}	

	protected class ImmobiliSearchContentProvider implements IStructuredContentProvider{
		
		private ArrayList immobiliSearchCriteria = null;
		
		public ImmobiliSearchContentProvider(ArrayList itemsSource){
			this.immobiliSearchCriteria = itemsSource;
		}
		
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	
			
		}
		
		@Override
		public void dispose() {
	
			
		}
		
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ArrayList){
				return ((ArrayList)inputElement).toArray();
			}else{
				return null;
			}
		}
		
	}
	
	protected class AnagraficheSearchContentProvider implements IStructuredContentProvider{
		
		private ArrayList anagraficheSearchCriteria = null;
		
		public AnagraficheSearchContentProvider(ArrayList itemsSource){
			this.anagraficheSearchCriteria = itemsSource;
		}
		
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	
			
		}
		
		@Override
		public void dispose() {
	
			
		}
		
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ArrayList){
				return ((ArrayList)inputElement).toArray();
			}else{
				return null;
			}
		}
		
	}
	
    protected class ColloquiSearchContentProvider implements IStructuredContentProvider{
		
		private ArrayList colloquiSearchCriteria = null;
		
		public ColloquiSearchContentProvider(ArrayList itemsSource){
			this.colloquiSearchCriteria = itemsSource;
		}
		
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	
			
		}
		
		@Override
		public void dispose() {
	
			
		}
		
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ArrayList){
				return ((ArrayList)inputElement).toArray();
			}else{
				return null;
			}
		}
		
	}
		
	public TableViewer getSearchImmobiliCriteriaTable(Composite container, ArrayList itemsSource){
				
		CriteriaTableViewerFactory ctvf = new CriteriaTableViewerFactory(ICriteriaOwners.IMMOBILI);
		
		ctvf.setDaCellEditorFactory(getImmobiliDaCellEditorFactory(ctvf.getTableViewer(container,
																					  SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION)
																	   .getTable()));
		
		ctvf.setACellEditorFactory(getImmobiliACellEditorFactory(ctvf.getTableViewer(container,
				  																	 SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION)
				  													  .getTable()));
		
		ObjectPropertiesFactory opf = new ObjectPropertiesFactory();
		opf.addFactory(ICriteriaOwners.IMMOBILI,getImmobiliMethodsFactory());
		
		ctvf.setObjectPropertiesFactory(opf);
		
		DaACanEditCellHandlerFactory daACanEditCellHandlerFactory = new DaACanEditCellHandlerFactory();
		daACanEditCellHandlerFactory.setDaHandler(getCanEditDaValue());
		daACanEditCellHandlerFactory.setaHandler(getImmobiliCanEditAValue());
		
		ctvf.setDaACanEditCellHandlerFactory(daACanEditCellHandlerFactory);
		
		ctvf.setDaValueProviderFactory(getImmobiliDaValueProviderFactory());
		ctvf.setAValueProviderFactory(getImmobiliAValueProviderFactory());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		TableViewer tvCriteri = ctvf.getCriteriaTable(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		
		tvCriteri.getTable().setLayoutData(gdFillHV);
		tvCriteri.getTable().setHeaderVisible(true);
		tvCriteri.getTable().setLinesVisible(true);
		tvCriteri.setContentProvider(new ImmobiliSearchContentProvider(itemsSource));
		
		return tvCriteri;
		
	}

	public TableViewer getSearchColloquiCriteriaTable(Composite container, ArrayList itemsSource){
		
		CriteriaTableViewerFactory ctvf = new CriteriaTableViewerFactory(ICriteriaOwners.COLLOQUI);
		ctvf.setDaCellEditorFactory(getColloquiDaCellEditorFactory(ctvf.getTableViewer(container,
																					  SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION)
																	   .getTable()));
		
		ctvf.setACellEditorFactory(getColloquiACellEditorFactory(ctvf.getTableViewer(container,
					 											 SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION)
					 											 .getTable()));
		
		ObjectPropertiesFactory opf = new ObjectPropertiesFactory();
		opf.addFactory(ICriteriaOwners.COLLOQUI,getColloquiMethodsFactory());
		
		ctvf.setObjectPropertiesFactory(opf);
		
		DaACanEditCellHandlerFactory daACanEditCellHandlerFactory = new DaACanEditCellHandlerFactory();
		daACanEditCellHandlerFactory.setDaHandler(getCanEditDaValue());
		daACanEditCellHandlerFactory.setaHandler(getColloquiCanEditAValue());
		
		ctvf.setDaACanEditCellHandlerFactory(daACanEditCellHandlerFactory);
		
		ctvf.setDaValueProviderFactory(getColloquiDaValueProviderFactory());
		ctvf.setAValueProviderFactory(getColloquiAValueProviderFactory());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		TableViewer tvCriteri = ctvf.getCriteriaTable(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		
		tvCriteri.getTable().setLayoutData(gdFillHV);
		tvCriteri.getTable().setHeaderVisible(true);
		tvCriteri.getTable().setLinesVisible(true);
		tvCriteri.setContentProvider(new ColloquiSearchContentProvider(itemsSource));
		
		return tvCriteri;
		
	}

	public TableViewer getSearchImmobiliAffittiCriteriaTable(Composite container, ArrayList itemsSource){
		
		CriteriaTableViewerFactory ctvf = new CriteriaTableViewerFactory(ICriteriaOwners.AFFITTI);
		
		ctvf.setDaCellEditorFactory(getImmobiliDaCellEditorFactory(ctvf.getTableViewer(container,
																					  SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION)
																	   .getTable()));
		
		ctvf.setACellEditorFactory(getImmobiliACellEditorFactory(ctvf.getTableViewer(container,
				  																	 SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION)
				  													  .getTable()));
		
		ObjectPropertiesFactory opf = new ObjectPropertiesFactory();
		opf.addFactory(ICriteriaOwners.AFFITTI,getAffittiMethodsFactory());
		
		ctvf.setObjectPropertiesFactory(opf);
		
		DaACanEditCellHandlerFactory daACanEditCellHandlerFactory = new DaACanEditCellHandlerFactory();
		daACanEditCellHandlerFactory.setDaHandler(getCanEditDaValue());
		daACanEditCellHandlerFactory.setaHandler(getAffittiCanEditAValue());
		
		ctvf.setDaACanEditCellHandlerFactory(daACanEditCellHandlerFactory);
		
		ctvf.setDaValueProviderFactory(getImmobiliDaValueProviderFactory());
		ctvf.setAValueProviderFactory(getImmobiliAValueProviderFactory());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		TableViewer tvCriteri = ctvf.getCriteriaTable(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		
		tvCriteri.getTable().setLayoutData(gdFillHV);
		tvCriteri.getTable().setHeaderVisible(true);
		tvCriteri.getTable().setLinesVisible(true);
		tvCriteri.setContentProvider(new ImmobiliSearchContentProvider(itemsSource));
		
		return tvCriteri;
		
	}

	public TableViewer getSearchAnagraficheCriteriaTable(Composite container, ArrayList itemsSource){
		
		CriteriaTableViewerFactory ctvf = new CriteriaTableViewerFactory(ICriteriaOwners.ANAGRAFICHE);
		
		ctvf.setDaCellEditorFactory(getAnagraficheDaCellEditorFactory(ctvf.getTableViewer(container,
																					  	  SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION)
																	   .getTable()));
		
		ctvf.setACellEditorFactory(getAnagraficheACellEditorFactory(ctvf.getTableViewer(container,
				  																	 	SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION)
				  													  .getTable()));
		
		ObjectPropertiesFactory opf = new ObjectPropertiesFactory();
		opf.addFactory(ICriteriaOwners.ANAGRAFICHE,getAnagraficheMethodsFactory());
		
		ctvf.setObjectPropertiesFactory(opf);
		
		DaACanEditCellHandlerFactory daACanEditCellHandlerFactory = new DaACanEditCellHandlerFactory();
		daACanEditCellHandlerFactory.setDaHandler(getCanEditDaValue());
		daACanEditCellHandlerFactory.setaHandler(getAnagraficheCanEditAValue());
		
		ctvf.setDaACanEditCellHandlerFactory(daACanEditCellHandlerFactory);
		
		ctvf.setDaValueProviderFactory(getAnagraficheDaValueProviderFactory());
		ctvf.setAValueProviderFactory(getAnagraficheAValueProviderFactory());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		TableViewer tvCriteri = ctvf.getCriteriaTable(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		
		tvCriteri.getTable().setLayoutData(gdFillHV);
		tvCriteri.getTable().setHeaderVisible(true);
		tvCriteri.getTable().setLinesVisible(true);
		tvCriteri.setContentProvider(new AnagraficheSearchContentProvider(itemsSource));
		
		return tvCriteri;

		
	}


	public DaAValueProviderFactory getColloquiDaValueProviderFactory() {
		if (colloquiDaValueProviderFactory == null){
			colloquiDaValueProviderFactory = new DaAValueProviderFactory();
			colloquiDaValueProviderFactory.addContentProvider(new TipologiaColloquiDaValueProvider());
			colloquiDaValueProviderFactory.addContentProvider(new AgenteDaValueProvider());
			colloquiDaValueProviderFactory.addContentProvider(new AnagraficheDaValueProvider());
			colloquiDaValueProviderFactory.addContentProvider(new ImmobiliDaValueProvider());
			colloquiDaValueProviderFactory.addContentProvider(new TextDaValueProvider());
			colloquiDaValueProviderFactory.addContentProvider(new CalendarDaValueProvider());
			colloquiDaValueProviderFactory.addContentProvider(new BooleanDaValueProvider());
			colloquiDaValueProviderFactory.addContentProvider(new BooleanCampiPersonaliDaValueProvider());
			colloquiDaValueProviderFactory.addContentProvider(new EnumCampiPersonaliDaValueProvider());
			

		}
		return colloquiDaValueProviderFactory;
	}


	public void setColloquiDaValueProviderFactory(
			DaAValueProviderFactory colloquiDaValueProviderFactory) {
		this.colloquiDaValueProviderFactory = colloquiDaValueProviderFactory;
	}


	public DaAValueProviderFactory getColloquiAValueProviderFactory() {
		if (colloquiAValueProviderFactory == null){
			colloquiAValueProviderFactory = new DaAValueProviderFactory();
			colloquiAValueProviderFactory.addContentProvider(new CalendarAValueProvider());
			colloquiAValueProviderFactory.addContentProvider(new TextAValueProvider());
		}
		return colloquiAValueProviderFactory;
	}


	public void setColloquiAValueProviderFactory(
			DaAValueProviderFactory colloquiAValueProviderFactory) {
		this.colloquiAValueProviderFactory = colloquiAValueProviderFactory;
	}
	
}
