package winkhouse.wizard.colloqui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.dialogs.custom.FileDialogCellEditor;
import winkhouse.orm.Allegaticolloquio;
import winkhouse.orm.Colloquicriteriricerca;
//import winkhouse.vo.Allegaticolloquio;
//import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.wizard.ColloquiWizard;



public class AllegatiColloquio extends WizardPage {

	private Composite container = null;
	private ArrayList<Allegaticolloquio> allegati = null;
	private TableViewer tvAllegati = null;
	
	public AllegatiColloquio(String pageName) {
		super(pageName);
	}

	public AllegatiColloquio(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		setTitle(getName());
		allegati = new ArrayList<Allegaticolloquio>();
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
		
		Composite toolbar = new Composite(container,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = new ImageHyperlink(toolbar, SWT.WRAP);	
		ihNew.setToolTipText("nuovo allegato");
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Allegaticolloquio acVO = new Allegaticolloquio();
				allegati.add(acVO);
				tvAllegati.refresh();
				
				TableItem ti = tvAllegati.getTable().getItem(tvAllegati.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvAllegati.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvAllegati.getTable();
				tvAllegati.getTable().notifyListeners(SWT.Selection, ev);

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				StructuredSelection ss = (StructuredSelection)tvAllegati.getSelection();
				if ((ss != null) && (ss.getFirstElement() != null)){
					allegati.remove((Colloquicriteriricerca)ss.getFirstElement());
					tvAllegati.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		

		tvAllegati = new TableViewer(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvAllegati.getTable().setLayoutData(gdFillHV);
		tvAllegati.getTable().setHeaderVisible(true);
		tvAllegati.getTable().setLinesVisible(true);
		tvAllegati.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return allegati.toArray();
			}

			@Override
			public void dispose() {
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				
			}
			
		});
		tvAllegati.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
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
		
		TableColumn tcDescrizione = new TableColumn(tvAllegati.getTable(),SWT.CENTER,0);
		tcDescrizione.setWidth(100);
		tcDescrizione.setText("Descrizione");
		
		TableViewerColumn tvcDescrizione = new TableViewerColumn(tvAllegati,tcDescrizione);
		tvcDescrizione.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if (((Allegaticolloquio)cell.getElement()).getNome() != null){
					cell.setText(((Allegaticolloquio)cell.getElement()).getNome());
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcDescrizione.setEditingSupport(new EditingSupport(tvAllegati){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAllegati.getTable());
			}

			@Override
			protected Object getValue(Object element) {
				if (((Allegaticolloquio)element).getNome() != null){
					return ((Allegaticolloquio)element).getNome();
				}else{
					return "";
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
			//	((Allegaticolloquio)element).setDescrizione(String.valueOf(value));
				tvAllegati.refresh();
			}
			
		});
		
		TableColumn tcPathFrom = new TableColumn(tvAllegati.getTable(),SWT.CENTER,1);
		tcPathFrom.setWidth(200);
		tcPathFrom.setText("Documento");
		
		TableViewerColumn tvcPathFrom = new TableViewerColumn(tvAllegati,tcPathFrom);
		tvcPathFrom.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((Allegaticolloquio)cell.getElement()).getFromPath() != null){
					cell.setText(((Allegaticolloquio)cell.getElement()).getFromPath());
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcPathFrom.setEditingSupport(new EditingSupport(tvAllegati){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				FileDialogCellEditor fdce = new FileDialogCellEditor(tvAllegati.getTable());
				fdce.setButtonImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
				fdce.setTootTipButton("Seleziona documento");
				return fdce;
			}

			@Override
			protected Object getValue(Object element) {				
				if (((Allegaticolloquio)element).getFromPath() != null){
					return ((Allegaticolloquio)element).getFromPath();
				}else{
					return "";
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Allegaticolloquio)element).setFromPath(String.valueOf(value));
				((Allegaticolloquio)element).setNome(((Allegaticolloquio)element).getFromPath()
																				   .substring(((Allegaticolloquio)element).getFromPath()
																						   								   .lastIndexOf(File.separator)+1)
												      );
				tvAllegati.refresh();
			}
			
		});
				
		tvAllegati.setInput(allegati);
		for (Iterator iterator = allegati.iterator(); iterator.hasNext();) {
			((ColloquiWizard)getWizard()).getColloquio().addToAllegaticolloquios((Allegaticolloquio) iterator.next());			
		}
		
		
		setControl(container);
	}

}
