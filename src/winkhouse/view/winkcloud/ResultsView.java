package winkhouse.view.winkcloud;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;

public class ResultsView extends ViewPart {

	public final static String ID = "winkhouse.queryresults";
	
	private FormToolkit ft = null;
	private Form f = null;	
	private TableViewer tvResults = null;
	
	private Image immobile = Activator.getImageDescriptor("icons/gohome.png").createImage();
	private Image anagrafica = Activator.getImageDescriptor("icons/anagrafica_16.png").createImage();
	private Image viewicon = Activator.getImageDescriptor("icons/view_text.png").createImage();
	
	public ResultsView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {

		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createForm(parent);
		f.setImage(viewicon);
		f.setText("Richieste ricevute");
		f.getBody().setLayout(new GridLayout());

		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;	

		tvResults = new TableViewer(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvResults.getTable().setLayoutData(gdExpVH);
		tvResults.getTable().setHeaderVisible(true);
		tvResults.getTable().setLinesVisible(true);
		tvResults.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				
				if (inputElement instanceof ArrayList){
					return ((ArrayList)inputElement).toArray();
				}
				return null;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
		});
		tvResults.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				if (columnIndex == 0){
					if (element instanceof ImmobiliModel){
						return immobile;
					}
					if (element instanceof AnagraficheModel){
						return anagrafica;
					}
				}
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				if (columnIndex == 1){
					return element.toString();
				}
				if (columnIndex == 2){
					if (element instanceof ImmobiliModel){
						return ((ImmobiliModel)element).getDescrizioneAnagrafichePropietarie();
					}
					return "";
				}
				
				return null;
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
		
		TableColumn tcimg = new TableColumn(tvResults.getTable(),SWT.CENTER,0);
		tcimg.setWidth(20);

		TableColumn tcDescrizione = new TableColumn(tvResults.getTable(),SWT.CENTER,1);
		tcDescrizione.setWidth(400);
		tcDescrizione.setText("Descrizione");

		TableColumn tcPropietario = new TableColumn(tvResults.getTable(),SWT.CENTER,2);
		tcPropietario.setWidth(400);
		tcPropietario.setText("Propietrio");

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setResults(ArrayList results){
		tvResults.setInput(results);
	}
}
