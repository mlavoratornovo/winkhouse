package winkhouse.xmldeser.wizard.importer.pages;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;

import winkhouse.Activator;
import winkhouse.xmldeser.helpers.ImporterHelper;
import winkhouse.xmldeser.models.xml.AbbinamentiXMLModel;
import winkhouse.xmldeser.models.xml.AffittiAllegatiXMLModel;
import winkhouse.xmldeser.models.xml.AffittiXMLModel;
import winkhouse.xmldeser.models.xml.AgentiXMLModel;
import winkhouse.xmldeser.models.xml.AllegatiColloquiXMLModel;
import winkhouse.xmldeser.models.xml.AllegatiImmobiliXMLModel;
import winkhouse.xmldeser.models.xml.AnagraficheXMLModel;
import winkhouse.xmldeser.models.xml.ClasseEnergeticaXMLModel;
import winkhouse.xmldeser.models.xml.ClassiClientiXMLModel;
import winkhouse.xmldeser.models.xml.ColloquiAgentiXMLModel;
import winkhouse.xmldeser.models.xml.ColloquiCriteriRicercaXMLModel;
import winkhouse.xmldeser.models.xml.ColloquiXMLModel;
import winkhouse.xmldeser.models.xml.ContattiXMLModel;
import winkhouse.xmldeser.models.xml.CriteriRicercaXMLModel;
import winkhouse.xmldeser.models.xml.DatiCatastaliXMLModel;
import winkhouse.xmldeser.models.xml.ImmagineXMLModel;
import winkhouse.xmldeser.models.xml.ImmobiliXMLModel;
import winkhouse.xmldeser.models.xml.RiscaldamentiXMLModel;
import winkhouse.xmldeser.models.xml.StanzeImmobiliXMLModel;
import winkhouse.xmldeser.models.xml.StatoConservativoXMLModel;
import winkhouse.xmldeser.models.xml.TipologiaContattiXMLModel;
import winkhouse.xmldeser.models.xml.TipologiaStanzeXMLModel;
import winkhouse.xmldeser.models.xml.TipologieImmobiliXMLModel;
import winkhouse.xmldeser.utils.ElementsDescriptors;
import winkhouse.xmldeser.wizard.importer.ImporterWizard;

public class ImportPreviewPage extends WizardPage {

	private CheckboxTableViewer tvImporter = null;
	
	private CellLabelProvider labelProvider = new CellLabelProvider(){

		
		@Override
		public void update(ViewerCell arg0) {
			
			switch (arg0.getColumnIndex()) {
			
				case 1: arg0.setImage(descriptor.getItemImage(arg0.getElement()));
						break;
						
				case 2: arg0.setText(descriptor.getCodObject(arg0.getElement()));
						break;

				case 3: arg0.setText(descriptor.getDesObject(arg0.getElement()));
						break;

			default:
				break;
			} 

			
		}

		@Override
		public String getToolTipText(Object element) {
			return descriptor.getItemTooltip(element);
		}

		public Point getToolTipShift(Object object) {
				return new Point(10, -5);
		}
			 	
		public int getToolTipDisplayDelayTime(Object object) {
			return 200;
		}
			 	
		public int getToolTipTimeDisplayed(Object object) {
			return 3000;
		}		
	};

	private ElementsDescriptors descriptor = null;
	
	public ImportPreviewPage(String pageName) {
		
		super(pageName);
	}

	public ImportPreviewPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);

	}

	@Override
	public void createControl(Composite arg0) {
		
		setDescription("Oggetti da importare");		
		setTitle(((ImporterWizard)getWizard()).getVersion());
		
		descriptor = new ElementsDescriptors(((ImporterWizard)getWizard()).getImporterVO());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;		
		
		Composite container = new Composite(arg0, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
		
		ToolBar tb = new ToolBar(container, SWT.HORIZONTAL|SWT.FLAT|SWT.RIGHT);
		ToolBarManager tbm = new ToolBarManager(tb);
		
		Table t = new Table(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.CHECK);
		tvImporter = new CheckboxTableViewer(t);
		tvImporter.getTable().setLayoutData(gdFillHV);
		tvImporter.getTable().setHeaderVisible(true);
		tvImporter.getTable().setLinesVisible(true);
		ColumnViewerToolTipSupport.enableFor(tvImporter,ToolTip.NO_RECREATE);
		tvImporter.addCheckStateListener(new ICheckStateListener() {
			
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (event.getChecked()){
					if (!((ImporterWizard)getWizard()).getImporterVO()
		 						  					  .getRisultati_selected()
		 						  					  .contains(event.getElement())){
	
							((ImporterWizard)getWizard()).getImporterVO()
													 	 .getRisultati_selected()
													 	 .add(event.getElement());
					}
				}else{
					((ImporterWizard)getWizard()).getImporterVO()
				 	 							 .getRisultati_selected()
				 	 							 .remove(event.getElement());					
				}
				
				getWizard().getContainer().updateButtons();
				
			}
		});
		tvImporter.setContentProvider(new IStructuredContentProvider(){

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

		
		TableColumn tcchk = new TableColumn(tvImporter.getTable(),SWT.LEFT,0);
		tcchk.setWidth(20);

		TableColumn tcimg = new TableColumn(tvImporter.getTable(),SWT.LEFT,1);
		tcimg.setWidth(20);
		
		TableViewerColumn tvcImg = new TableViewerColumn(tvImporter, tcimg);
		tvcImg.setLabelProvider(labelProvider);
		
		
		
		TableColumn tcCodice = new TableColumn(tvImporter.getTable(),SWT.LEFT,2);
		tcCodice.setWidth(50);
		tcCodice.setText("Codice");

		TableViewerColumn tvcCodice = new TableViewerColumn(tvImporter, tcCodice);
		tvcCodice.setLabelProvider(labelProvider);
		
		
		TableColumn tcDescrizione = new TableColumn(tvImporter.getTable(),SWT.LEFT,3);
		tcDescrizione.setWidth(500);
		tcDescrizione.setText("Descrizione");

		TableViewerColumn tvcDescrizione = new TableViewerColumn(tvImporter, tcDescrizione);
		tvcDescrizione.setLabelProvider(labelProvider);
		
		tbm.add(new ReadExportedFiles(this, Action.AS_PUSH_BUTTON));
		tbm.add(new SelezionaTuttoAction(tvImporter,Action.AS_CHECK_BOX));
		tbm.update(true);
		
		setControl(container);
	
	
	} 
		
	public void setRisultati(ArrayList risultati){
		((ImporterWizard)getWizard()).getImporterVO().resetSelectedHashMaps();
		tvImporter.setInput(risultati);
		
	}

	class ReadExportedFiles extends Action{
		
		private ImportPreviewPage ipp = null;
		
		public ReadExportedFiles(ImportPreviewPage ipp,int type){
			super("Avvia lettura files esportazione",type);
			setImageDescriptor(Activator.getImageDescriptor("/icons/wizardimport/avvia.png"));
			setToolTipText("Avvia lettura files esportazione");			
			this.ipp = ipp; 
		}
		
		public void run(){
			
			ImporterHelper ih = new ImporterHelper();
			ih.importData(((ImporterWizard)getWizard()).getImporterVO());
			ipp.setRisultati(((ImporterWizard)getWizard()).getImporterVO().getRisultati());
			((ImporterWizard)getWizard()).getImporterVO().setRisultati_selected(new ArrayList());

			
		}
		
	}
	
	class SelezionaTuttoAction extends Action{

		CheckboxTableViewer table = null;
		
		public SelezionaTuttoAction(CheckboxTableViewer table,int type){
			super("Seleziona tutto",type);			
			setImageDescriptor(Activator.getImageDescriptor("/icons/wizardimport/chkall.png"));
			this.table = table;
		}
		
		@Override
		public void run() {
			
			if (isChecked()){
				
				setDescription("Deseleziona tutto");
				setImageDescriptor(Activator.getImageDescriptor("/icons/wizardimport/chknone.png"));
				TableItem[] items = table.getTable().getItems();
				
				((ImporterWizard)getWizard()).getImporterVO().resetSelectedHashMaps();
				
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(true);
					if (!((ImporterWizard)getWizard()).getImporterVO()
							 						  .getRisultati_selected()
							 						  .contains(items[i].getData())){
					
						((ImporterWizard)getWizard()).getImporterVO()
					 							 	 .getRisultati_selected()
					 							 	 .add(items[i].getData());
					}

				}
				
				
				
				
			}else{
				
				setDescription("Seleziona tutto");
				setImageDescriptor(Activator.getImageDescriptor("/icons/wizardimport/chkall.png"));
				TableItem[] items = table.getTable().getItems();
				Object[] checked = tvImporter.getCheckedElements();
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(false);
					if (((ImporterWizard)getWizard()).getImporterVO()
					 								 .getRisultati_selected()
					 								 .contains(items[i].getData())){

						((ImporterWizard)getWizard()).getImporterVO()
					 							 	 .getRisultati_selected()
					 							 	 .remove(items[i].getData());
					}
					
				}
				((ImporterWizard)getWizard()).getImporterVO().resetSelectedHashMaps();
			}
			
			getWizard().getContainer().updateButtons();
			
		}
		
	}
	
	
}
