package winkhouse.xmldeser.wizard.importer.pages;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.swt.widgets.ToolBar;

import winkhouse.Activator;
import winkhouse.xmldeser.helpers.ImporterHelper;
import winkhouse.xmldeser.utils.ElementsDescriptors;
import winkhouse.xmldeser.utils.ObjectTypeCompare;
import winkhouse.xmldeser.wizard.importer.ImporterWizard;

public class ResultsPage extends WizardPage {

	private TableViewer tvImporter = null;
	private Image inserimentoImg = Activator.getImageDescriptor("icons/wizardimport/ins.png").createImage();
	private Image updateImg = Activator.getImageDescriptor("icons/wizardimport/up.png").createImage();
	
	private CellLabelProvider labelProvider = new CellLabelProvider(){

		
		@Override
		public void update(ViewerCell arg0) {
			
			switch (arg0.getColumnIndex()) {
			
				case 1: arg0.setImage(descriptor.getItemImage(arg0.getElement()));
						break;
						
				case 2: arg0.setImage(
								(((ObjectTypeCompare)arg0.getElement()).getImportOperation() == ObjectTypeCompare.AGGIORNAMENTO)
								? updateImg
								: inserimentoImg
									);
						break;

				case 3: arg0.setText(descriptor.getCodObject(arg0.getElement()));
						break;
						
				case 4: arg0.setText(descriptor.getDesObject(arg0.getElement()));
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
	
	public ResultsPage(String pageName) {
		
		super(pageName);

	}

	public ResultsPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		
	}

	@Override
	public void createControl(Composite arg0) {

		setDescription("Avvio importazione e Risultati dell'importazione");
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
		tbm.add(new ScriviDatiAction(Action.AS_PUSH_BUTTON));
		tbm.update(true);

		
		Table t = new Table(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.CHECK);
		tvImporter = new CheckboxTableViewer(t);
		tvImporter.getTable().setLayoutData(gdFillHV);
		tvImporter.getTable().setHeaderVisible(true);
		tvImporter.getTable().setLinesVisible(true);
		ColumnViewerToolTipSupport.enableFor(tvImporter,ToolTip.NO_RECREATE);
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

		TableColumn tcimgResult = new TableColumn(tvImporter.getTable(),SWT.LEFT,2);
		tcimgResult.setWidth(20);
		
		TableViewerColumn tvcImgResult = new TableViewerColumn(tvImporter, tcimgResult);
		tvcImgResult.setLabelProvider(labelProvider);		
		
		TableColumn tcCodice = new TableColumn(tvImporter.getTable(),SWT.LEFT,3);
		tcCodice.setWidth(50);
		tcCodice.setText("Codice");

		TableViewerColumn tvcCodice = new TableViewerColumn(tvImporter, tcCodice);
		tvcCodice.setLabelProvider(labelProvider);
		
		TableColumn tcDescrizione = new TableColumn(tvImporter.getTable(),SWT.LEFT,4);
		tcDescrizione.setWidth(400);
		tcDescrizione.setText("Descrizione");
		
		TableViewerColumn tvcDescrizione = new TableViewerColumn(tvImporter, tcDescrizione);
		tvcDescrizione.setLabelProvider(labelProvider);
		
		setControl(container);
	
	} 
	
	class ScriviDatiAction extends Action{
		
		public ScriviDatiAction(int type){
			
			super("Avvia scrittura",type);			
			setImageDescriptor(Activator.getImageDescriptor("/icons/wizardimport/avvia.png"));
			setToolTipText("Avvia la scrittura dei dati");
		}
		
		@Override
		public void run() {
			
			ImporterHelper ih = new ImporterHelper();
			ih.writeData(((ImporterWizard)getWizard()).getImporterVO());
			tvImporter.setInput(((ImporterWizard)getWizard()).getImporterVO().getRisultati_selected());
			getWizard().getContainer().updateButtons();
			
		}
		
	}
		
	public void setRisultati(ArrayList risultati){
				
		tvImporter.setInput(risultati);
		
	}
		
}
