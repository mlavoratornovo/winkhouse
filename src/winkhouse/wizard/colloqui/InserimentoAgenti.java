package winkhouse.wizard.colloqui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.orm.Agenti;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.ColloquiAgentiVO;
import winkhouse.wizard.ColloquiWizard;

// Pagina da eliminare

public class InserimentoAgenti extends WizardPage {

	private Composite container = null;
	private TableViewer tvAgenti = null;
	private ArrayList<AgentiVO> agenti = null;
	private ArrayList<AgentiVO> agentiCollection = null;
	private ArrayList<ColloquiAgentiModel_Age> colloquiAgenti = null;
	private ArrayList<String> desAgentiCollections = null;
	private ComboBoxViewerCellEditor cbvce = null;
	private TextCellEditor tce = null;
	private Button inagenda = null;
	
	private String[] desAgenti = null;
	private Integer[] codAgenti = null;
	
	public InserimentoAgenti(String pageName) {
		super(pageName);
	}

	public InserimentoAgenti(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	Comparator c = new Comparator<String>(){

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
		
	};
	
	@Override
	public void createControl(Composite parent) {
		setTitle(getName());		
		colloquiAgenti = new ArrayList<ColloquiAgentiModel_Age>();
		
		fillDescrizioniAgenti();
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
		
		Label lagenda = new Label(container,SWT.NONE);
		lagenda.setText("aggiungi appuntamento in agenda");
		inagenda = new Button(container,SWT.CHECK);
		inagenda.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				((ColloquiWizard)getWizard()).getColloquio()
											 .setScadenziere(((Button)e.getSource()).getSelection());
				System.out.println(((ColloquiWizard)getWizard()).getColloquio()
						 										.isScadenziere());
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});
		
		Composite toolbar = new Composite(container,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = new ImageHyperlink(toolbar, SWT.WRAP);	
		ihNew.setToolTipText("nuovo allegato");
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				ColloquiAgentiModel_Age aM_A = new ColloquiAgentiModel_Age();
				colloquiAgenti.add(aM_A);
				tvAgenti.refresh();
				
				TableItem ti = tvAgenti.getTable().getItem(tvAgenti.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvAgenti.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvAgenti.getTable();
				tvAgenti.getTable().notifyListeners(SWT.Selection, ev);
				
				
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
				StructuredSelection ss = (StructuredSelection)tvAgenti.getSelection();
				if ((ss != null) && (ss.getFirstElement() != null)){
					colloquiAgenti.remove((ColloquiAgentiVO)ss.getFirstElement());
					tvAgenti.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		

		tvAgenti = new TableViewer(container,SWT.MULTI|SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvAgenti.getTable().setLayoutData(gdFillHV);
		tvAgenti.getTable().setHeaderVisible(true);
		tvAgenti.getTable().setLinesVisible(true);
		tvAgenti.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return colloquiAgenti.toArray();
			}

			@Override
			public void dispose() {
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				
			}
			
		});
		TableColumn tcAgenti = new TableColumn(tvAgenti.getTable(),SWT.CENTER,0);
		tcAgenti.setWidth(200);
		tcAgenti.setText("Agente");
		
		TableViewerColumn tvcAgenti = new TableViewerColumn(tvAgenti,tcAgenti);
		tvcAgenti.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if (((ColloquiAgentiModel_Age)cell.getElement()).getAgente() != null){
					cell.setText(
							((ColloquiAgentiModel_Age)cell.getElement()).getAgente().getCognome() + " " +
							((ColloquiAgentiModel_Age)cell.getElement()).getAgente().getNome()
							    );
				}else{
					cell.setText("");
				}
			}
			
		});

		tvcAgenti.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				//return new ComboBoxCellEditor(tvAgenti.getTable(),desAgentiCollections.toArray(new String[desAgentiCollections.size()]));
				
				return new ComboBoxCellEditor(tvAgenti.getTable(),
						 					  desAgenti,
						 					  SWT.READ_ONLY|SWT.DROP_DOWN);
			}

			@Override
			protected Object getValue(Object element) {
				Integer cod = ((ColloquiAgentiModel_Age)element).getCodAgente();
				if ((cod != null) &&
					(cod.intValue() != 0)){
					AgentiVO aVO = MobiliaDatiBaseCache.getInstance().findAgentiByCod(cod);
				//Arrays.sort(desAgenti, c);
					if (aVO != null){
						return Arrays.binarySearch(desAgenti, aVO.getCognome() + " " + aVO.getNome(), c);
					}else{
						return -1;
					}
				}else{
					return -1;
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (((Integer)value).intValue() > -1){					
					((ColloquiAgentiModel_Age)element).setCodAgente(codAgenti[((Integer)value).intValue()]);
					((ColloquiAgentiModel_Age)element).setAgente(null);
				}		
				tvAgenti.refresh();
				((ColloquiWizard)getWizard()).getContainer().updateButtons();
			}
			
		});
		
		/*
		cbvce = new ComboBoxViewerCellEditor(tvAgenti.getTable());
		cbvce.setContenProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return agenti.toArray();						
			}

			@Override
			public void dispose() {
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
									 Object newInput) {
				if (newInput instanceof AgentiVO){
					Collections.sort(agenti, c);
					int index = Collections.binarySearch(agenti, newInput, c);
					if (index < 0){
						agenti.add((AgentiVO)newInput);
					}else{
						agenti.set(index, (AgentiVO)newInput);
					}
				}
				
			}
			
		});
		
		cbvce.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				return ((AgentiVO)element).getCognome() + " " + ((AgentiVO)element).getNome();
			}
			
		});
		cbvce.setInput(new Object());
		
		tvcAgenti.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				//return new ComboBoxCellEditor(tvAgenti.getTable(),desAgentiCollections.toArray(new String[desAgentiCollections.size()]));
				
				return cbvce;
			}

			@Override
			protected Object getValue(Object element) {
				int index = -1;
				if (((ColloquiAgentiModel_Age)element).getAgente() != null){
					Collections.sort(agenti,c);
					index = Collections.binarySearch(agenti,((ColloquiAgentiModel_Age)element).getAgente(),c); 
				}
				return index;
			}

			@Override
			protected void setValue(Object element, Object value) {

				((ColloquiAgentiModel_Age)element).setAgente((AgentiVO)value);
				tvAgenti.refresh();
				
			}
			
		});
		*/
		TableColumn tcCommentiAgenti = new TableColumn(tvAgenti.getTable(),SWT.CENTER,1);
		tcCommentiAgenti.setWidth(200);
		tcCommentiAgenti.setText("Commento");
		
		TableViewerColumn tvcCommentiAgenti = new TableViewerColumn(tvAgenti,tcCommentiAgenti);
		tvcCommentiAgenti.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if (((ColloquiAgentiVO)cell.getElement()).getCommento() != null){
					cell.setText(((ColloquiAgentiVO)cell.getElement()).getCommento());
				}else{
					cell.setText("");
				}
			}
			
		});
		
		tce = new TextCellEditor(tvAgenti.getTable(),SWT.MULTI|SWT.V_SCROLL);
		
		tvcCommentiAgenti.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				
				return tce;
			}

			@Override
			protected Object getValue(Object element) {
				return (((ColloquiAgentiVO)element).getCommento() == null)
					   ? ""
					   : ((ColloquiAgentiVO)element).getCommento();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((ColloquiAgentiVO)element).setCommento((String)value);
				tvAgenti.refresh();
			}
			
			
		});
		
		tvAgenti.setInput(colloquiAgenti);
		//((ColloquiWizard)getWizard()).getColloquio().setAgenti(colloquiAgenti);
		setControl(container);
		
	}

	public void fillDescrizioniAgenti(){						
		desAgenti = new String[MobiliaDatiBaseCache.getInstance().getAgenti().size()];
		codAgenti = new Integer[MobiliaDatiBaseCache.getInstance().getAgenti().size()];
		Iterator<Agenti> it = MobiliaDatiBaseCache.getInstance().getAgenti().iterator();
		int count = 0;
		while(it.hasNext()){
			Agenti aVO = it.next();
			desAgenti[count] = aVO.getCognome() + " " + aVO.getNome();
			codAgenti[count] = aVO.getCodAgente();
			count++;
		}		
	}

}
