package winkhouse.view.anagrafica;

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
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.immobili.ApriDettaglioImmobileAction;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Immobili;


public class ImmobiliPropietaView extends ViewPart {

	public final static String ID = "winkhouse.immobilipropietaview";
	private TableViewer tvImmobiliProprieta = null;
	private Anagrafiche anagrafica = null;
	
	public ImmobiliPropietaView() {

	}

	@Override
	public void createPartControl(Composite parent) {
		
		FormToolkit ft = new FormToolkit(getViewSite().getShell().getDisplay());
		Form f = ft.createForm(parent);
		f.setImage(Activator.getImageDescriptor("icons/immobile.png").createImage());
		f.setText("Immobili Proprietà");
		f.getBody().setLayout(new GridLayout());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
				
		Table t = new Table(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvImmobiliProprieta = new CheckboxTableViewer(t);
		tvImmobiliProprieta.getTable().setLayoutData(gdFillHV);
		tvImmobiliProprieta.getTable().setHeaderVisible(true);
		tvImmobiliProprieta.getTable().setLinesVisible(true);
		tvImmobiliProprieta.getTable().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ImmobiliModel im = (ImmobiliModel)((StructuredSelection)tvImmobiliProprieta.getSelection()).getFirstElement();
				ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction(im, null);
				adia.run();
			}
		});	
		tvImmobiliProprieta.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return ((ArrayList<Immobili>)inputElement).toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
			
		});
		tvImmobiliProprieta.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
		
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				
				switch(columnIndex){
				case 0 : return ((Immobili)element).getCitta();
				case 1 : return ((Immobili)element).getProvincia();
				case 2 : return ((Immobili)element).getIndirizzo();
				case 3 : return ((Immobili)element).getDescrizione();
				case 4 : return String.valueOf(((Immobili)element).getPrezzo());
				case 5 : return String.valueOf(((Immobili)element).getMq());
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
		

		TableColumn tcCitta = new TableColumn(tvImmobiliProprieta.getTable(),SWT.LEFT,0);
		tcCitta.setWidth(100);
		tcCitta.setText("Citta");

		TableColumn tcProvincia = new TableColumn(tvImmobiliProprieta.getTable(),SWT.CENTER,1);
		tcProvincia.setWidth(100);
		tcProvincia.setText("Provincia");
		
		TableColumn tcIndirizzo = new TableColumn(tvImmobiliProprieta.getTable(),SWT.CENTER,2);
		tcIndirizzo.setWidth(150);
		tcIndirizzo.setText("Indirizzo");

		TableColumn tcDescrizione = new TableColumn(tvImmobiliProprieta.getTable(),SWT.CENTER,3);
		tcDescrizione.setWidth(150);
		tcDescrizione.setText("Descrizione");

		TableColumn tcPrezzo = new TableColumn(tvImmobiliProprieta.getTable(),SWT.CENTER,4);
		tcPrezzo.setWidth(50);
		tcPrezzo.setText("Prezzo");

		TableColumn tcMq = new TableColumn(tvImmobiliProprieta.getTable(),SWT.CENTER,5);
		tcMq.setWidth(50);
		tcMq.setText("MQ");
		

	}

	@Override
	public void setFocus() {


	}

	public TableViewer getTvImmobiliPropieta() {
		return tvImmobiliProprieta;
	}

	public Anagrafiche getAnagrafica() {
		return anagrafica;
	}

	public void setAnagrafica(Anagrafiche anangrafica) {
		this.anagrafica = anangrafica;
		if ((this.anagrafica != null) && 
			(this.anagrafica.getImmobilis() != null)){
			tvImmobiliProprieta.setInput(new ArrayList<Immobili>(this.anagrafica.getImmobilis()));
		}else{
			tvImmobiliProprieta.setInput(new ArrayList<Immobili>());
		}
	}

}
