package winkhouse.view.immobili;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.ObjectContext;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.anagrafiche.ApriDettaglioAnagraficaAction;
import winkhouse.action.anagrafiche.CancellaAnagraficaPropietariaAction;
import winkhouse.action.anagrafiche.OpenPopUpAnagrafiche;
import winkhouse.action.anagrafiche.RefreshAnagrafichePropietarieAction;
import winkhouse.action.anagrafiche.SalvaAnagrafichePropietarieAction;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Agenti;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Immobili;
import winkhouse.orm.Immobilipropietari;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.common.RecapitiView;

public class AnagrafichePropietarieView extends ViewPart {

	public final static String ID = "winkhouse.anagrafichepropietarieview";
	private TableViewer tvAnagrafichePropietarie = null;
	private Immobili immobile = null;
	private OpenPopUpAnagrafiche openPopUpAnagrafiche = null;
	private CancellaAnagraficaPropietariaAction cancellaAnagraficaPropietariaAction = null; 
	private RefreshAnagrafichePropietarieAction refreshAnagrafichePropietarieAction = null;
	private SalvaAnagrafichePropietarieAction salvaAnagrafichePropietarieAction = null;
	
	public AnagrafichePropietarieView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		
		FormToolkit ft = new FormToolkit(getViewSite().getShell().getDisplay());
		Form f = ft.createForm(parent);
		f.setImage(Activator.getImageDescriptor("icons/anagraficaImmobile.png").createImage());
		f.setText("Anagrafiche Proprietarie");
		f.getBody().setLayout(new GridLayout());

		openPopUpAnagrafiche = new OpenPopUpAnagrafiche();
		
		cancellaAnagraficaPropietariaAction = new CancellaAnagraficaPropietariaAction("elimina proprietario da immobile",
																					  Activator.getImageDescriptor("icons/edittrash.png"));
		
		refreshAnagrafichePropietarieAction = new RefreshAnagrafichePropietarieAction("rileggi dati da archivio",
																					  Activator.getImageDescriptor("icons/adept_reinstall.png"));
		
		salvaAnagrafichePropietarieAction = new SalvaAnagrafichePropietarieAction("salva i dati nell'archivio",
				  																  Activator.getImageDescriptor("icons/document-save.png"));
		
		getViewSite().getActionBars().getToolBarManager().add(salvaAnagrafichePropietarieAction);
		getViewSite().getActionBars().getToolBarManager().add(openPopUpAnagrafiche);
		getViewSite().getActionBars().getToolBarManager().add(cancellaAnagraficaPropietariaAction);
		getViewSite().getActionBars().getToolBarManager().add(refreshAnagrafichePropietarieAction);
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
				
		Table t = new Table(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvAnagrafichePropietarie = new CheckboxTableViewer(t);
		tvAnagrafichePropietarie.getTable().setLayoutData(gdFillHV);
		tvAnagrafichePropietarie.getTable().setHeaderVisible(true);
		tvAnagrafichePropietarie.getTable().setLinesVisible(true);
		tvAnagrafichePropietarie.getTable().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				AnagraficheModel am = (AnagraficheModel)((StructuredSelection)tvAnagrafichePropietarie.getSelection()).getFirstElement();
//				ApriDettaglioAnagraficaAction adia = new ApriDettaglioAnagraficaAction(am, null);
//				adia.run();
			}
		});	
		tvAnagrafichePropietarie.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return ((List)inputElement).toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
			
		});
		tvAnagrafichePropietarie.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
		
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				
				switch(columnIndex){
				case 0 : return ((Immobilipropietari)element).getAnagrafiche().getNome();
				case 1 : return ((Immobilipropietari)element).getAnagrafiche().getCognome();
				case 2 : return ((Immobilipropietari)element).getAnagrafiche().getRagsoc();
				case 3 : return ((Immobilipropietari)element).getAnagrafiche().getCitta();
				case 4 : return String.valueOf(((Immobilipropietari)element).getAnagrafiche().getIndirizzo());
				
				default : return "";
				}
				
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}
			
		});
		

		TableColumn tcCitta = new TableColumn(tvAnagrafichePropietarie.getTable(),SWT.LEFT,0);
		tcCitta.setWidth(100);
		tcCitta.setText("Nome");

		TableColumn tcProvincia = new TableColumn(tvAnagrafichePropietarie.getTable(),SWT.CENTER,1);
		tcProvincia.setWidth(100);
		tcProvincia.setText("Cognome");
		
		TableColumn tcIndirizzo = new TableColumn(tvAnagrafichePropietarie.getTable(),SWT.CENTER,2);
		tcIndirizzo.setWidth(150);
		tcIndirizzo.setText("Ragione sociale");

		TableColumn tcDescrizione = new TableColumn(tvAnagrafichePropietarie.getTable(),SWT.CENTER,3);
		tcDescrizione.setWidth(150);
		tcDescrizione.setText("Città");

		TableColumn tcPrezzo = new TableColumn(tvAnagrafichePropietarie.getTable(),SWT.CENTER,4);
		tcPrezzo.setWidth(50);
		tcPrezzo.setText("Indirizzo");
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public TableViewer getTvAnagrafichePropietarie() {
		return tvAnagrafichePropietarie;
	}

	public Immobili getImmobile() {
		return immobile;
	}

	public void setAnagrafica(Immobili immobile) {
		this.immobile = immobile;
		if ((this.immobile != null) && 
			(this.immobile.getImmobilipropietaris() != null)){
			tvAnagrafichePropietarie.setInput(this.immobile.getImmobilipropietaris());
		}else{
			tvAnagrafichePropietarie.setInput(new ArrayList());
		}
	}
	
	public void addAnagrafica(Anagrafiche anagrafica) {
		
		if (this.immobile != null){
			
//			if (this.immobile.getImmobilipropietaris() == null){
//				this.immobile.setAnagrafichePropietarie(new ArrayList<AnagraficheModel>());
//			}
			ObjectContext oc = WinkhouseUtils.getInstance().getCayenneObjectContext();
			Immobilipropietari ip = oc.newObject(Immobilipropietari.class);
			ip.setAnagrafiche(ip.getObjectContext().localObject(anagrafica));
			if (this.immobile.getCodImmobile() != 0) {
				ip.setImmobili(ip.getObjectContext().localObject(this.immobile));
			}else {
				ip.setImmobili(this.immobile);
			}
			
			oc.commitChanges();
			this.immobile.addToImmobilipropietaris(this.immobile.getObjectContext().localObject(ip));
			setAnagrafica(this.immobile);
			RecapitiView riv = (RecapitiView)PlatformUI.getWorkbench()
			   										   .getActiveWorkbenchWindow()
			   										   .getActivePage()
			   										   .findView(RecapitiView.ID);
			
			ArrayList<Anagrafiche> anagrafiche = new ArrayList<Anagrafiche>();
			if (immobile.getImmobilipropietaris() == null || immobile.getImmobilipropietaris().size() > 0) {
				for (Immobilipropietari ipr : immobile.getImmobilipropietaris()) {
					anagrafiche.add(ipr.getAnagrafiche());
				}
			}				  				  

			riv.setAnagrafiche(anagrafiche);

		}
				
	}

	public void setCompareView(boolean enabled){
		
		tvAnagrafichePropietarie.getTable().setEnabled(!enabled);
		openPopUpAnagrafiche.setEnabled(!enabled);
		cancellaAnagraficaPropietariaAction.setEnabled(!enabled);
		refreshAnagrafichePropietarieAction.setEnabled(!enabled);
		salvaAnagrafichePropietarieAction.setEnabled(!enabled);		

	}

}