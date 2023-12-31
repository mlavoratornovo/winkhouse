package winkhouse.view.immobili;

import java.util.ArrayList;

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
import winkhouse.view.common.RecapitiView;

public class AnagrafichePropietarieView extends ViewPart {

	public final static String ID = "winkhouse.anagrafichepropietarieview";
	private TableViewer tvAnagrafichePropietarie = null;
	private ImmobiliModel immobile = null;
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
				return ((ArrayList)inputElement).toArray();
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
				case 0 : return ((AnagraficheModel)element).getNome();
				case 1 : return ((AnagraficheModel)element).getCognome();
				case 2 : return ((AnagraficheModel)element).getRagioneSociale();
				case 3 : return ((AnagraficheModel)element).getCitta();
				case 4 : return String.valueOf(((AnagraficheModel)element).getIndirizzo());
				
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

	public ImmobiliModel getImmobile() {
		return immobile;
	}

	public void setAnagrafica(ImmobiliModel immobile) {
		this.immobile = immobile;
		if ((this.immobile != null) && 
			(this.immobile.getAnagrafichePropietarie() != null)){
			tvAnagrafichePropietarie.setInput(this.immobile.getAnagrafichePropietarie());
		}else{
			tvAnagrafichePropietarie.setInput(new ArrayList());
		}
	}
	
	public void addAnagrafica(AnagraficheModel anagrafica) {
		
		if (this.immobile != null){
			
			if (this.immobile.getAnagrafichePropietarie() == null){
				this.immobile.setAnagrafichePropietarie(new ArrayList<AnagraficheModel>());
			}
			
			this.immobile.getAnagrafichePropietarie().add(anagrafica);
			setAnagrafica(this.immobile);
			RecapitiView riv = (RecapitiView)PlatformUI.getWorkbench()
			   										   .getActiveWorkbenchWindow()
			   										   .getActivePage()
			   										   .findView(RecapitiView.ID);
			
//			riv.setAnagrafiche(this.immobile.getAnagrafichePropietarie());

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