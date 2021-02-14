package winkhouse.view.preference;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.dao.EntityDAO;
import winkhouse.helper.EntityHelper;
import winkhouse.model.AttributeModel;
import winkhouse.model.EntityModel;
import winkhouse.vo.AffittiVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.EntityVO;
import winkhouse.vo.ImmobiliVO;

public class DynamicFieldsSettingsPage extends PreferencePage {

	private Form form = null;
	private Button rImmobili = null;
	private Button rAnagrafiche = null;
	private Button rColloqui = null;
	private Button rAffitti = null;
	private TableViewer tvCampiPersonali = null;
	private ComboBoxCellEditor tceContatto = null;
	private ArrayList<EntityModel> entita = null; 
	private int selectedEntityIndex = 0;
	private String[] fieldTypeNames = {"Testo","Numerico intero","Numerico con virgola","Data","Si/No","A valori fissi"};
	private TextCellEditor texteditorFixedValues = null;
	
	public DynamicFieldsSettingsPage() {
	
	}

	public DynamicFieldsSettingsPage(String title) {
		super(title);

	}

	public DynamicFieldsSettingsPage(String title, ImageDescriptor image) {
		super(title, image);

	}

	private void fillEntities(){
		
		if (entita == null){
			
			EntityDAO eDAO = new EntityDAO();
			entita = new ArrayList<EntityModel>();
			entita.add(eDAO.getEntityByClassName(ImmobiliVO.class.getName()));
			entita.add(eDAO.getEntityByClassName(AnagraficheVO.class.getName()));
			entita.add(eDAO.getEntityByClassName(ColloquiVO.class.getName()));
			entita.add(eDAO.getEntityByClassName(AffittiVO.class.getName()));
		}
		
	}
	
	private void setSelectedEntityIndex(String className){
		
		int count = 0;
		
		for (Iterator<EntityModel> iterator = entita.iterator(); iterator.hasNext();) {
			if (iterator.next().getClassName().equalsIgnoreCase(className)){
				selectedEntityIndex = count;
				break;
			}else{
				count ++;
			}						
		}
		
	}
	
	@Override
	protected Control createContents(Composite parent) {
		
		fillEntities();
		
		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;	

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		GridLayout gridLayout = new GridLayout();
		
		IToolBarManager mgr = form.getToolBarManager();
		
		form.getBody().setLayout(gridLayout);
		
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		
		toolkit.paintBordersFor(form.getBody());
		
		Group entitySelector = new Group(form.getBody(), SWT.SHADOW_ETCHED_IN);
		entitySelector.setLayout(new FillLayout(SWT.HORIZONTAL));
		entitySelector.setLayoutData(gridData);
		entitySelector.setText("Selezione tipo dati");
		rImmobili = new Button(entitySelector, SWT.RADIO);
		rImmobili.setSelection(true);
		rImmobili.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				setSelectedEntityIndex(ImmobiliVO.class.getName());
				tvCampiPersonali.setInput(entita.get(selectedEntityIndex).getAttributes());
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		rImmobili.setText("Immobili");
		
		rAnagrafiche = new Button(entitySelector, SWT.RADIO);
		rAnagrafiche.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				setSelectedEntityIndex(AnagraficheVO.class.getName());
				tvCampiPersonali.setInput(entita.get(selectedEntityIndex).getAttributes());
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		rAnagrafiche.setText("Anagrafiche");
		
		rColloqui = new Button(entitySelector, SWT.RADIO);
		rColloqui.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				setSelectedEntityIndex(ColloquiVO.class.getName());
				tvCampiPersonali.setInput(entita.get(selectedEntityIndex).getAttributes());
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		rColloqui.setText("Colloqui");

		rAffitti = new Button(entitySelector, SWT.RADIO);
		rAffitti.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				setSelectedEntityIndex(AffittiVO.class.getName());
				tvCampiPersonali.setInput(entita.get(selectedEntityIndex).getAttributes());
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		rAffitti.setText("Affitti");

		
		Composite toolbar = toolkit.createComposite(form.getBody(),SWT.NONE);
		toolbar.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = toolkit.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				AttributeModel am = new AttributeModel();
				am.setIdClassEntity(entita.get(selectedEntityIndex).getIdClassEntity());
				entita.get(selectedEntityIndex).getAttributes().add(am);
				tvCampiPersonali.setInput(entita.get(selectedEntityIndex).getAttributes());
				TableItem ti = tvCampiPersonali.getTable().getItem(tvCampiPersonali.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvCampiPersonali.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvCampiPersonali.getTable();
				tvCampiPersonali.getTable().notifyListeners(SWT.Selection, ev);

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		

		ImageHyperlink ihCancella = toolkit.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvCampiPersonali.getSelection() != null){
					Iterator it = ((StructuredSelection)tvCampiPersonali.getSelection()).iterator();
					while (it.hasNext()) {
						AttributeModel cModel = (AttributeModel)it.next();
						entita.get(selectedEntityIndex).getAttributes().remove(cModel);
					}
					tvCampiPersonali.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		


		tvCampiPersonali = new TableViewer(form.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvCampiPersonali.getTable().setLayoutData(gdExpVH);
		tvCampiPersonali.getTable().setHeaderVisible(true);
		tvCampiPersonali.getTable().setLinesVisible(true);
		tvCampiPersonali.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (inputElement instanceof ArrayList)
					   ? ((ArrayList)inputElement).toArray()
					   : null;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
		});
		tvCampiPersonali.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				
				AttributeModel cModel = (AttributeModel)element;
				switch (columnIndex){
				case 0: return (cModel.getAttributeName() == null)
							   ? ""
							   : cModel.getAttributeName();					        
				
				case 1: return (cModel.getFieldType() == null)
								? ""
				   			    : (cModel.getFieldType().equalsIgnoreCase(String.class.getName()))
				   			       ? "Testo"
				   			       : (cModel.getFieldType().equalsIgnoreCase(Integer.class.getName()))
				   			    	  ? "Numerico intero"
				   			    	  : (cModel.getFieldType().equalsIgnoreCase(Double.class.getName()))
				   			    		? "Numerico con virgola"
				   			    		: (cModel.getFieldType().equalsIgnoreCase(Date.class.getName()))
				   			    		  ? "Data"
				   			    		  : (cModel.getFieldType().equalsIgnoreCase(Boolean.class.getName()))
				   			    		  	? "Si/No"
				   			    		  	: (cModel.getFieldType().equalsIgnoreCase(Enum.class.getName()))
					   			    		  ? "Valori fissi"
							   			      : "";
				case 2: return (cModel.getFieldType().equalsIgnoreCase(Enum.class.getName()))
 			    		  		? cModel.getEnumFieldValues()
 			    		  		: "";
 			    		  		
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
		
		TableColumn tcNome = new TableColumn(tvCampiPersonali.getTable(),SWT.CENTER,0);
		tcNome.setWidth(150);
		tcNome.setText("Nome campo");
				
		TableViewerColumn tcvNome = new TableViewerColumn(tvCampiPersonali,tcNome);
		tcvNome.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				AttributeModel aModel = ((AttributeModel)cell.getElement());
				cell.setText((aModel.getAttributeName() == null)
							 ? ""
							 : aModel.getAttributeName());
				
			}
			
		});
		tcvNome.setEditingSupport(new EditingSupport(tvCampiPersonali){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {

				TextCellEditor tce = new TextCellEditor(tvCampiPersonali.getTable());
				return tce;
			}

			@Override
			protected Object getValue(Object element) {
				AttributeModel cModel = ((AttributeModel)element);
				return (cModel.getAttributeName() == null)
						? ""
						: cModel.getAttributeName();
			}

			@Override
			protected void setValue(Object element, Object value) {
				AttributeModel cModel = ((AttributeModel)element);
				cModel.setAttributeName((String) value);
				tvCampiPersonali.refresh();
			}
			
		});
			
		TableColumn tcTipo = new TableColumn(tvCampiPersonali.getTable(),SWT.CENTER,1);
		tcTipo.setWidth(150);
		tcTipo.setText("Tipo dati");

		TableViewerColumn tcvTipo = new TableViewerColumn(tvCampiPersonali,tcTipo);
		
		tceContatto = new ComboBoxCellEditor(tvCampiPersonali.getTable(),fieldTypeNames);
		
		tcvTipo.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				cell.setText(
						(((AttributeModel)cell.getElement()).getFieldType()==null)
						? "Selezionare tipo campo"
						: ((AttributeModel)cell.getElement()).getFieldType().equalsIgnoreCase(String.class.getName())
						  ? "Testo"
						  : ((AttributeModel)cell.getElement()).getFieldType().equalsIgnoreCase(Integer.class.getName())
						  	? "Numerico intero"
						  	: ((AttributeModel)cell.getElement()).getFieldType().equalsIgnoreCase(Double.class.getName())
						  	  ? "Numerico con virgola"
						  	  : ((AttributeModel)cell.getElement()).getFieldType().equalsIgnoreCase(Date.class.getName())
						  	    ? "Data"
						  	    : ((AttributeModel)cell.getElement()).getFieldType().equalsIgnoreCase(Boolean.class.getName())
						  	    	? "Si/No"
						  	    	: ((AttributeModel)cell.getElement()).getFieldType().equalsIgnoreCase(Enum.class.getName())
						  	    		? "A valori fissi"
						  	    		: "Selezionare tipo campo" 
							);
				
			}
			
		});
		
		tcvTipo.setEditingSupport(new EditingSupport(tvCampiPersonali){

			@Override
			protected boolean canEdit(Object element) {
				return (((AttributeModel)element).getFieldType() != null)
						? false
						: true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return tceContatto;
			}

			@Override
			protected Object getValue(Object element) {
				
				return (((AttributeModel)element).getFieldType() == null)
	   			   	   ? 0
			   		   : (((AttributeModel)element).getFieldType().equalsIgnoreCase(String.class.getName()))
			   		   	 ? 0
			   		   	 : (((AttributeModel)element).getFieldType().equalsIgnoreCase(Integer.class.getName()))
			   		   	   ? 1
			   		   	   : (((AttributeModel)element).getFieldType().equalsIgnoreCase(Double.class.getName()))
			   		   	     ? 2
			   		   	     : (((AttributeModel)element).getFieldType().equalsIgnoreCase(Date.class.getName()))
			   		   	       ? 3
			   		   	       : (((AttributeModel)element).getFieldType().equalsIgnoreCase(Boolean.class.getName()))
			   		   	       	 ? 4
			   		   	    	 : (((AttributeModel)element).getFieldType().equalsIgnoreCase(Enum.class.getName()))
			   		   	       	 	? 5
			   		   	       	 	: 0; 
				
				
			}

			@Override
			protected void setValue(Object element, Object value) {
				((AttributeModel)element).setFieldType(EntityVO.fieldTypes[(Integer)value]);
				tvCampiPersonali.refresh();
			}
			
		});
		
		TableColumn tcValoriFissi = new TableColumn(tvCampiPersonali.getTable(),SWT.CENTER,2);
		tcValoriFissi.setWidth(150);		
		
		TableViewerColumn tcvValoriFissi = new TableViewerColumn(tvCampiPersonali,tcValoriFissi);
		
		tcvValoriFissi.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				cell.setText((((AttributeModel)cell.getElement()).getFieldType() != null) && 
							 ((AttributeModel)cell.getElement()).getFieldType().equalsIgnoreCase(Enum.class.getName())
						  	 ? ((AttributeModel)cell.getElement()).getEnumFieldValues()
						  	 : "");
				
			}
			
		});
		
		texteditorFixedValues = new TextCellEditor(tvCampiPersonali.getTable());
		texteditorFixedValues.getControl().setToolTipText("Separare i valori con il simobolo | (shift + \\)");

		tcvValoriFissi.setEditingSupport(new EditingSupport(tvCampiPersonali){

			@Override
			protected boolean canEdit(Object element) {
				return (((AttributeModel)element).getFieldType() != null) && (((AttributeModel)element).getFieldType().equalsIgnoreCase(Enum.class.getName()))
						? true
						: false;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return texteditorFixedValues;
			}

			@Override
			protected Object getValue(Object element) {
				
				return (((AttributeModel)element).getEnumFieldValues() == null)
	   			   	   ? ""
			   		   : ((AttributeModel)element).getEnumFieldValues();

			}

			@Override
			protected void setValue(Object element, Object value) {
				((AttributeModel)element).setEnumFieldValues(((String)value));
				tvCampiPersonali.refresh();
			}
			
		});

		tvCampiPersonali.setInput(entita.get(selectedEntityIndex).getAttributes());
		
		return form;
	}

	
	@Override
	protected void performApply() {
		
		EntityHelper eh = new EntityHelper();
		
		
		if (eh.updateEntitiesAttributes(entita)){
							
			MessageDialog.openInformation(getShell(), "Esito salvataggio", "Salvataggio avvenuto con successo");
			entita = null;
			fillEntities();
			tvCampiPersonali.setInput(entita.get(selectedEntityIndex).getAttributes());
			
		}else{
							
			MessageDialog.openError(getShell(), "Esito salvataggio", "Salvataggio non avvenuto con successo");
			
		}
					
	}

	@Override
	public boolean performOk() {
		return true;
	}

}
