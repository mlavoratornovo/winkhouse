package winkhouse.xmlser.wizard.exporter.pages;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;

import winkhouse.export.helpers.UtilsHelper;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.Activator;
import winkhouse.xmlser.helper.XMLExportHelper;
import winkhouse.xmlser.models.DatiBaseModel;
import winkhouse.xmlser.models.xml.AgentiXMLModel;
import winkhouse.xmlser.models.xml.ClasseEnergeticaXMLModel;
import winkhouse.xmlser.models.xml.ClassiClientiXMLModel;
import winkhouse.xmlser.models.xml.RiscaldamentiXMLModel;
import winkhouse.xmlser.models.xml.StatoConservativoXMLModel;
import winkhouse.xmlser.models.xml.TipologiaContattiXMLModel;
import winkhouse.xmlser.models.xml.TipologiaStanzeXMLModel;
import winkhouse.xmlser.models.xml.TipologieImmobiliXMLModel;
import winkhouse.xmlser.wizard.exporter.ExporterWizard;

public class SelettoreDatiBase extends WizardPage {

	private Image agente = Activator.getImageDescriptor("icons/wizardexport/looknfeel.png").createImage();
	private Image classe_energetica = Activator.getImageDescriptor("icons/wizardexport/ce.jpeg").createImage();
	private Image riscaldamento = Activator.getImageDescriptor("icons/wizardexport/riscaldamento.jpeg").createImage();
	private Image statoconservativo = Activator.getImageDescriptor("icons/wizardexport/statoconservativo.png").createImage();
	private Image tipologia_immobile = Activator.getImageDescriptor("icons/wizardexport/tipiimmobili.png").createImage();
	private Image classe_cliente = Activator.getImageDescriptor("icons/wizardexport/classianagrafiche.png").createImage();
	private Image tipo_stanza = Activator.getImageDescriptor("icons/wizardexport/tipologiastanza.png").createImage();
	private Image tipo_recapiti = Activator.getImageDescriptor("icons/wizardexport/tiporecapiti.png").createImage();
	
	private CheckboxTableViewer tvDatiBase = null;
	
	public SelettoreDatiBase(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	public SelettoreDatiBase(String pageName, String title,ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	class SelezionaTuttoAction extends Action{

		CheckboxTableViewer table = null;
		
		public SelezionaTuttoAction(CheckboxTableViewer table,int type){
			super("Seleziona tutto",type);			
			setImageDescriptor(Activator.getImageDescriptor("/icons/chkall.png"));
			this.table = table;
		}
		
		@Override
		public void run() {
			
			if (isChecked()){
				
				setDescription("Deseleziona tutto");
				setImageDescriptor(Activator.getImageDescriptor("/icons/chknone.png"));
				TableItem[] items = table.getTable().getItems();
				
				for (int i = 0; i < items.length; i++) {					
					items[i].setChecked(true);
					checkItems(true, ((DatiBaseModel)items[i].getData()).getCodDatiBase());
				}
								
			}else{
				
				setDescription("Seleziona tutto");
				setImageDescriptor(Activator.getImageDescriptor("/icons/chkall.png"));
				TableItem[] items = table.getTable().getItems();
				
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(false);					
					checkItems(false, ((DatiBaseModel)items[i].getData()).getCodDatiBase());
				}
								
			}
			
			getWizard().getContainer().updateButtons();
			
		}
		
	}
	
	private void checkItems(boolean checked,Integer codType){

		if (checked){
			
			if (codType == XMLExportHelper.AGENTI_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmAgenti(new HashMap());
				ArrayList<AgentiVO> al_agenti = UtilsHelper.getInstance().getAllAgenti();
				for (AgentiVO agentiVO : al_agenti) {
					((ExporterWizard)getWizard()).getExporterVO().getHmAgenti().put(agentiVO.getCodAgente(), new AgentiXMLModel(agentiVO));
				} 
				
			}
			if (codType == XMLExportHelper.CLASSE_CLIENTE_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmCategorieClienti(new HashMap());
				ArrayList<ClassiClientiVO> al_classiclienti = UtilsHelper.getInstance().getAllClassiClienti();
				for (ClassiClientiVO classiClientiVO : al_classiclienti) {
					((ExporterWizard)getWizard()).getExporterVO().getHmCategorieClienti().put(classiClientiVO.getCodClasseCliente(), new ClassiClientiXMLModel(classiClientiVO));
				}
				
			}
			if (codType == XMLExportHelper.CLASSE_ENERGETICA_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmClassiEnergetiche(new HashMap());
				ArrayList<ClasseEnergeticaVO> al_classeenergetica = UtilsHelper.getInstance().getAllClasseEnergetica();
				for (ClasseEnergeticaVO classeEnergeticaVO : al_classeenergetica) {
					((ExporterWizard)getWizard()).getExporterVO().getHmClassiEnergetiche().put(classeEnergeticaVO.getCodClasseEnergetica(), new ClasseEnergeticaXMLModel(classeEnergeticaVO));
				}
				
			}
			if (codType == XMLExportHelper.RISCALDAMENTO_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmRiscaldamento(new HashMap());
				ArrayList<RiscaldamentiVO> al_riscaldamenti = UtilsHelper.getInstance().getAllRiscaldamenti();
				for (RiscaldamentiVO riscaldamentiVO : al_riscaldamenti) {
					((ExporterWizard)getWizard()).getExporterVO().getHmRiscaldamento().put(riscaldamentiVO.getCodRiscaldamento(), new RiscaldamentiXMLModel(riscaldamentiVO));
				}
										
			}
			if (codType == XMLExportHelper.STATO_CONSERVATIVO_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmStatiConservativi(new HashMap());
				ArrayList<StatoConservativoVO> al_statoconservativo = UtilsHelper.getInstance().getAllStatoConservativo();
				for (StatoConservativoVO statoConservativoVO : al_statoconservativo) {
					((ExporterWizard)getWizard()).getExporterVO().getHmStatiConservativi().put(statoConservativoVO.getCodStatoConservativo(), new StatoConservativoXMLModel(statoConservativoVO));
				}						
				
			}
			if (codType == XMLExportHelper.TIPOLOGIA_CONTATTI_COD){

				((ExporterWizard)getWizard()).getExporterVO().setHmTipiContatti(new HashMap());
				ArrayList<TipologiaContattiVO> al_tipologiacontatti = UtilsHelper.getInstance().getAllTipologiaContatti();
				for (TipologiaContattiVO tipologiaContattiVO : al_tipologiacontatti) {
					((ExporterWizard)getWizard()).getExporterVO().getHmTipiContatti().put(tipologiaContattiVO.getCodTipologiaContatto(), new TipologiaContattiXMLModel(tipologiaContattiVO));
				}												
				
			}
			if (codType == XMLExportHelper.TIPOLOGIA_IMMOBILE_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmTipiImmobili(new HashMap());
				ArrayList<TipologieImmobiliVO> al_tipologieimmobili = UtilsHelper.getInstance().getAllTipologieImmobili();
				for (TipologieImmobiliVO tipologieImmobiliVO : al_tipologieimmobili) {
					((ExporterWizard)getWizard()).getExporterVO().getHmTipiImmobili().put(tipologieImmobiliVO.getCodTipologiaImmobile(), new TipologieImmobiliXMLModel(tipologieImmobiliVO));
				}												
				
			}
			if (codType == XMLExportHelper.TIPOLOGIA_STANZE_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmTipiStanze(new HashMap());
				ArrayList<TipologiaStanzeVO> al_tipologiestanze = UtilsHelper.getInstance().getAllTipologieStanze();
				for (TipologiaStanzeVO tipologiaStanzeVO : al_tipologiestanze) {
					((ExporterWizard)getWizard()).getExporterVO().getHmTipiStanze().put(tipologiaStanzeVO.getCodTipologiaStanza(), new TipologiaStanzeXMLModel(tipologiaStanzeVO));
				}						
										
			}					
			
		}else{

			if (codType == XMLExportHelper.AGENTI_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmAgenti(new HashMap());
				
			}
			if (codType == XMLExportHelper.CLASSE_CLIENTE_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmCategorieClienti(new HashMap());						
				
			}
			if (codType == XMLExportHelper.CLASSE_ENERGETICA_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmClassiEnergetiche(new HashMap());
				
			}
			if (codType == XMLExportHelper.RISCALDAMENTO_COD){

				((ExporterWizard)getWizard()).getExporterVO().setHmRiscaldamento(new HashMap());
										
			}
			if (codType == XMLExportHelper.STATO_CONSERVATIVO_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmStatiConservativi(new HashMap());
				
			}
			if (codType == XMLExportHelper.TIPOLOGIA_CONTATTI_COD){

				((ExporterWizard)getWizard()).getExporterVO().setHmTipiContatti(new HashMap());
				
			}
			if (codType == XMLExportHelper.TIPOLOGIA_IMMOBILE_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmTipiImmobili(new HashMap());
				
			}
			if (codType == XMLExportHelper.TIPOLOGIA_STANZE_COD){
				
				((ExporterWizard)getWizard()).getExporterVO().setHmTipiStanze(new HashMap());
										
			}					
			
		}
				
	}

	@Override
	public void createControl(Composite arg0) {
		
		setTitle(((ExporterWizard)getWizard()).getVersion());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		Composite container = new Composite(arg0,SWT.NONE);		
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
		
		ToolBar tb = new ToolBar(container, SWT.HORIZONTAL|SWT.FLAT|SWT.RIGHT);
		ToolBarManager tbm = new ToolBarManager(tb);

		
		Table t = new Table(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.CHECK);
		tvDatiBase = new CheckboxTableViewer(t);
		tvDatiBase.getTable().setLayoutData(gdFillHV);
		tvDatiBase.getTable().setHeaderVisible(true);
		tvDatiBase.getTable().setLinesVisible(true);
		
		tvDatiBase.addCheckStateListener(new ICheckStateListener() {
			
			@Override
			public void checkStateChanged(CheckStateChangedEvent arg0) {
				
				DatiBaseModel dbm = (DatiBaseModel)arg0.getElement();
				checkItems(arg0.getChecked(),dbm.getCodDatiBase());
				
			}
		});
		
		tvDatiBase.setContentProvider(new IStructuredContentProvider(){

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
		
		tvDatiBase.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				if (element instanceof DatiBaseModel){
					DatiBaseModel dbm = (DatiBaseModel)element;
					if (dbm.getCodDatiBase() == XMLExportHelper.AGENTI_COD){
						return agente;
					}
					if (dbm.getCodDatiBase() == XMLExportHelper.CLASSE_CLIENTE_COD){
						return classe_cliente;
					}
					if (dbm.getCodDatiBase() == XMLExportHelper.CLASSE_ENERGETICA_COD){
						return classe_energetica;
					}
					if (dbm.getCodDatiBase() == XMLExportHelper.RISCALDAMENTO_COD){
						return riscaldamento;
					}
					if (dbm.getCodDatiBase() == XMLExportHelper.STATO_CONSERVATIVO_COD){
						return statoconservativo;
					}
					if (dbm.getCodDatiBase() == XMLExportHelper.TIPOLOGIA_CONTATTI_COD){
						return tipo_recapiti;
					}
					if (dbm.getCodDatiBase() == XMLExportHelper.TIPOLOGIA_IMMOBILE_COD){
						return tipologia_immobile;
					}
					if (dbm.getCodDatiBase() == XMLExportHelper.TIPOLOGIA_STANZE_COD){
						return tipo_stanza;
					}
					
				}
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				
				if (element instanceof DatiBaseModel){
					switch(columnIndex){
					case 1 : return ((DatiBaseModel)element).getDescrizione();
					default : return "";
					}
				}
				return "";
				
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
		
		TableColumn tcchk = new TableColumn(tvDatiBase.getTable(),SWT.LEFT,0);
		tcchk.setWidth(20);
		
		TableColumn tcDescrizione = new TableColumn(tvDatiBase.getTable(),SWT.LEFT,1);
		tcDescrizione.setWidth(200);
		tcDescrizione.setText("Descrizione");

		tbm.add(new SelezionaTuttoAction(tvDatiBase,Action.AS_CHECK_BOX));
		tbm.update(true);

		
		setControl(container);


	}
	
	public void setDatiBaseModels(ArrayList<DatiBaseModel> datibase){
		
		tvDatiBase.setInput(datibase);
		
	}

}