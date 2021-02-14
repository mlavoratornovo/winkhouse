package winkhouse.xmlser.wizard.exporter.pages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.TreeItem;

import winkhouse.Activator;
import winkhouse.export.helpers.AffittiHelper;
import winkhouse.model.AbbinamentiModel;
import winkhouse.model.AffittiAnagraficheModel;
import winkhouse.model.AffittiModel;
import winkhouse.model.AgentiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AttributeModel;
import winkhouse.model.AttributeValueModel;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ContattiModel;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.model.EntityModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.StanzeImmobiliModel;
import winkhouse.vo.AbbinamentiVO;
import winkhouse.vo.AffittiAllegatiVO;
import winkhouse.vo.AffittiRateVO;
import winkhouse.vo.AffittiSpeseVO;
import winkhouse.vo.AffittiVO;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.DatiCatastaliVO;
import winkhouse.vo.EntityVO;
import winkhouse.vo.ImmagineVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StanzeImmobiliVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.xmlser.models.xml.AttributeXMLModel;
import winkhouse.xmlser.models.xml.EntityXMLModel;
import winkhouse.xmlser.models.xml.ImmobiliPropietariXMLModel;
import winkhouse.xmlser.wizard.exporter.ExporterWizard;

public class SelettoreEntitaImmobili extends WizardPage {

	private TreeViewer tree = null;
	private ViewLabelProvider label_provider = new ViewLabelProvider();
	
	private Image immobile = Activator.getImageDescriptor("icons/wizardexport/immobile.png").createImage();
	private Image anagrafica = Activator.getImageDescriptor("icons/wizardexport/anagrafica.png").createImage();
	private Image anagrafica_immobile = Activator.getImageDescriptor("icons/wizardexport/anagraficaImmobile.png").createImage();
	private Image affitto = Activator.getImageDescriptor("icons/wizardexport/affitti.png").createImage();
	private Image agente = Activator.getImageDescriptor("icons/wizardexport/looknfeel.png").createImage();
	private Image colloqui = Activator.getImageDescriptor("icons/wizardexport/colloqui16.png").createImage();
	private Image classe_energetica = Activator.getImageDescriptor("icons/wizardexport/ce.jpeg").createImage();
	private Image riscaldamento = Activator.getImageDescriptor("icons/wizardexport/riscaldamento.jpeg").createImage();
	private Image statoconservativo = Activator.getImageDescriptor("icons/wizardexport/statoconservativo.png").createImage();
	private Image allegati = Activator.getImageDescriptor("icons/wizardexport/attach.png").createImage();
	private Image daticatastali = Activator.getImageDescriptor("icons/wizardexport/daticatastali.png").createImage();
	private Image stanza = Activator.getImageDescriptor("icons/wizardexport/stanza.png").createImage();
	private Image tipologia_immobile = Activator.getImageDescriptor("icons/wizardexport/tipiimmobili.png").createImage();
	private Image immagine = Activator.getImageDescriptor("icons/wizardexport/immagini.png").createImage();
	private Image classe_cliente = Activator.getImageDescriptor("icons/wizardexport/classianagrafiche.png").createImage();
	private Image recapiti = Activator.getImageDescriptor("icons/wizardexport/recapiti.png").createImage();
	private Image abbinamenti = Activator.getImageDescriptor("icons/wizardexport/abbinamenti.png").createImage();
	private Image criteriricerca = Activator.getImageDescriptor("icons/wizardexport/criteriricerca.png").createImage();
	private Image tipo_stanza = Activator.getImageDescriptor("icons/wizardexport/tipologiastanza.png").createImage();
	private Image tipo_recapiti = Activator.getImageDescriptor("icons/wizardexport/tiporecapiti.png").createImage();
	private Image colloquiagenti = Activator.getImageDescriptor("icons/wizardexport/colloquiagenti.png").createImage();
	private Image rataaffitto = Activator.getImageDescriptor("icons/wizardexport/rataaffitto.png").createImage();
	private Image speseaffitto = Activator.getImageDescriptor("icons/wizardexport/affittispese.png").createImage();
	private Image affittianagrafiche = Activator.getImageDescriptor("icons/wizardexport/affittianagrafiche.png").createImage();
	private Image campi_personali = Activator.getImageDescriptor("icons/wizardexport/campi_personali16.png").createImage();
	private Image campi_personali_value = Activator.getImageDescriptor("icons/wizardexport/campi_personali_value16.png").createImage();
	private Image entity = Activator.getImageDescriptor("icons/wizardexport/entity.png").createImage();
	
	private Button chk_dati_di_base = null;
	
	
	private AffittiHelper ah = new AffittiHelper();
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
	
	class SelezionaTuttoAction extends Action{

		TreeViewer tree = null;
		
		public SelezionaTuttoAction(TreeViewer tree,int type){
			super("Seleziona tutto",type);			
			setImageDescriptor(Activator.getImageDescriptor("/icons/wizardexport/chkall.png"));
			this.tree = tree;
		}
		
		@Override
		public void run() {
			
			if (isChecked()){
				
				setDescription("Deseleziona tutto");
				setToolTipText("Deseleziona tutto");
				setImageDescriptor(Activator.getImageDescriptor("/icons/wizardexport/chknone.png"));
				TreeItem[] items = tree.getTree().getItems();
				
				for (int i = 0; i < items.length; i++) {
					checkItems(items[i], true);
				}
				
				
			}else{
				setToolTipText("Seleziona tutto");
				setDescription("Seleziona tutto");
				setImageDescriptor(Activator.getImageDescriptor("/icons/wizardexport/chkall.png"));
				TreeItem[] items = tree.getTree().getItems();
				
				for (int i = 0; i < items.length; i++) {
					checkItems(items[i], false);
				}
				
			}
			
			getWizard().getContainer().updateButtons();
			
		}
		
	}

	
	public SelettoreEntitaImmobili(String pageName) {
		super(pageName);
	}

	public SelettoreEntitaImmobili(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	class ViewLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			if (element instanceof ImmobiliVO){
				return immobile;
			}else if ((element instanceof AnagraficheVO) || (element instanceof ColloquiAnagraficheModel_Ang)){
				return anagrafica;
			}else if (element instanceof ImmobiliPropietariXMLModel){
				return anagrafica_immobile;
			}else if (element instanceof AgentiVO){
				return agente;
			}else if (element instanceof RiscaldamentiVO){
				return riscaldamento;
			}else if (element instanceof ClasseEnergeticaVO){
				return classe_energetica;
			}else if (element instanceof StatoConservativoVO){
				return statoconservativo;
			}else if (element instanceof TipologieImmobiliVO){
				return tipologia_immobile;
			}else if (element instanceof ColloquiVO){
				return colloqui;
			}else if (element instanceof ImmagineVO){
				return immagine;
			}else if ((element instanceof AllegatiImmobiliVO) || (element instanceof AllegatiColloquiVO) || (element instanceof AffittiAllegatiVO)){
				return allegati;
			}else if (element instanceof DatiCatastaliVO){
				return daticatastali;
			}else if (element instanceof StanzeImmobiliVO){
				return stanza;
			}else if (element instanceof ClassiClientiVO){
				return classe_cliente;
			}else if (element instanceof ContattiVO){
				return recapiti;
			}else if (element instanceof AbbinamentiVO){
				return abbinamenti;
			}else if ((element instanceof CriteriRicercaModel) || (element instanceof ColloquiCriteriRicercaVO)){
				return criteriricerca;
			}else if (element instanceof TipologiaStanzeVO){
				return tipo_stanza;
			}else if (element instanceof TipologiaContattiVO){
				return tipo_recapiti;
			}else if (element instanceof ColloquiAgentiModel_Age){
				return colloquiagenti;
			}else if (element instanceof AffittiModel){
				return affitto;
			}else if (element instanceof AffittiRateVO){
				return rataaffitto;
			}else if (element instanceof AffittiSpeseVO){
				return speseaffitto;
			}else if (element instanceof AffittiAnagraficheModel){
				return affittianagrafiche;
			}else if (element instanceof EntityModel){
				return entity;
			}else if (element instanceof AttributeModel){
				return campi_personali;
			}else if (element instanceof AttributeValueModel){
				return campi_personali_value;
			}else{
				return null;
			}
		}

		@Override
		public String getText(Object element) {
			if (element instanceof ImmobiliVO){
				return ((ImmobiliVO)element).toString();
			}else if (element instanceof AnagraficheVO){
				return ((AnagraficheVO)element).toString();
			}else if (element instanceof ImmobiliPropietariXMLModel){
				return ((ImmobiliPropietariXMLModel)element).getAnagrafica().toString() + " - " + ((ImmobiliPropietariXMLModel)element).getImmobile().toString();			
			}else if (element instanceof AgentiVO){
				return ((AgentiVO)element).toString();
			}else if (element instanceof RiscaldamentiVO){
				return ((RiscaldamentiVO)element).toString();
			}else if (element instanceof ClasseEnergeticaVO){
				return ((ClasseEnergeticaVO)element).toString();
			}else if (element instanceof StatoConservativoVO){
				return ((StatoConservativoVO)element).toString();
			}else if (element instanceof TipologieImmobiliVO){
				return ((TipologieImmobiliVO)element).toString();
			}else if (element instanceof ColloquiVO){
				return ((ColloquiModel)element).toString();
			}else if (element instanceof ImmagineVO){
				return ((ImmagineVO)element).toString();
			}else if (element instanceof AllegatiImmobiliVO){
				return ((AllegatiImmobiliVO)element).toString();
			}else if (element instanceof AllegatiColloquiVO){
				return ((AllegatiColloquiVO)element).toString();
			}else if (element instanceof DatiCatastaliVO){
				return "Foglio : " + ((DatiCatastaliVO)element).getFoglio() + 
					   " Particella : " + ((DatiCatastaliVO)element).getParticella() +
					   " Subalterno : " + ((DatiCatastaliVO)element).getSubalterno() +
					   " Categoria : " + ((DatiCatastaliVO)element).getCategoria();
			}else if (element instanceof StanzeImmobiliVO){
				return ((StanzeImmobiliVO)element).toString();
			}else if (element instanceof ClassiClientiVO){
				return ((ClassiClientiVO)element).toString();
			}else if (element instanceof ContattiVO){
				return ((ContattiVO)element).toString();
			}else if (element instanceof AbbinamentiModel){
				return "Immobile : " + ((AbbinamentiModel)element).getImmobile().toString() + 
					  "  Anagrafica : " + ((AbbinamentiModel)element).getAnagrafica().toString();
			}else if (element instanceof CriteriRicercaModel){
				return ((CriteriRicercaModel)element).toString();
			}else if (element instanceof TipologiaStanzeVO){
				return ((TipologiaStanzeVO)element).toString();
			}else if (element instanceof TipologiaContattiVO){
				return ((TipologiaContattiVO)element).toString();
			}else if (element instanceof ColloquiAgentiModel_Age){
				return "Agente : " + ((((ColloquiAgentiModel_Age)element).getAgente() != null)?((ColloquiAgentiModel_Age)element).getAgente().toString():"") + 
					   " Commneto : " + ((ColloquiAgentiModel_Age)element).getCommento();
			}else if (element instanceof AffittiVO){
				return ((AffittiVO)element).toString();
			}else if (element instanceof AffittiRateVO){
				return ((AffittiRateVO)element).toString();
			}else if (element instanceof AffittiSpeseVO){
				return ((AffittiSpeseVO)element).toString();
			}else if (element instanceof AffittiAllegatiVO){
				return ((AffittiAllegatiVO)element).getNome();
			}else if (element instanceof ColloquiAnagraficheModel_Ang){
				return ((ColloquiAnagraficheModel_Ang)element).toString();
			}else if (element instanceof ColloquiCriteriRicercaVO){
				return ((ColloquiCriteriRicercaVO)element).toString();
			}else if (element instanceof AffittiAnagraficheModel){
				return ((AffittiAnagraficheModel)element).toString();									
			}else if (element instanceof EntityModel){
				return ((EntityModel)element).getDescription();									
			}else if (element instanceof AttributeModel){
				return ((AttributeModel)element).getAttributeName();									
			}else if (element instanceof AttributeValueModel){
				return ((AttributeValueModel)element).getFieldValue();									
			}{
				return "";
			}
		}
		
	}
	
	@Override
	public void createControl(Composite parent) {

		setDescription("Selezione dei dati collegati agli immobili selezionati");		
		setTitle(((ExporterWizard)getWizard()).getVersion());
		

		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;		
		
		Composite cb = new Composite(parent, SWT.NONE);
		cb.setLayoutData(gdFillHV);
		cb.setLayout(new GridLayout());	

		ToolBar tb = new ToolBar(cb, SWT.HORIZONTAL|SWT.FLAT|SWT.RIGHT);
		ToolBarManager tbm = new ToolBarManager(tb);

		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;		
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		
		Composite c = new Composite(cb, SWT.NONE);
		c.setLayoutData(gdFillH);
		c.setLayout(gl);
		
		chk_dati_di_base = new Button(c,SWT.CHECK);
		chk_dati_di_base.setText("Dati di base completi");
		chk_dati_di_base.setToolTipText("Esporta tutti gli elementi dei dati di base oltre a quelli selezionati");
		chk_dati_di_base.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {

				((ExporterWizard)getWizard()).getExporterVO().setFlagDatiBaseCompleti(chk_dati_di_base.getSelection());
				
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		tree = new TreeViewer(cb, SWT.HORIZONTAL|SWT.VERTICAL|SWT.BORDER|SWT.CHECK);
		tree.getTree().setLayoutData(gdFillHV);
		
		tree.getTree().addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	            if (event.detail == SWT.CHECK) {
	                TreeItem item = (TreeItem) event.item;
	                boolean checked = item.getChecked();
	                checkItems(item, checked);
	                checkPath(item.getParentItem(), checked, false);
	            }
	        }
	    });
		
		tree.setContentProvider(new ITreeContentProvider() {
			
			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
				
			}
			
			@Override
			public void dispose() {

				
			}
			
			@Override
			public boolean hasChildren(Object arg0) {
				
				if ((arg0 instanceof ImmobiliModel) || (arg0 instanceof AnagraficheModel) ||
					(arg0 instanceof AgentiVO) || (arg0 instanceof ColloquiVO) || 
					(arg0 instanceof StanzeImmobiliVO) || (arg0 instanceof ContattiVO) || 
					(arg0 instanceof AffittiVO) || (arg0 instanceof ColloquiAnagraficheModel_Ang) ||
					(arg0 instanceof AffittiAnagraficheModel) || (arg0 instanceof AbbinamentiModel) ||
					(arg0 instanceof ColloquiAgentiModel_Age) || (arg0 instanceof EntityModel) ||
					(arg0 instanceof AttributeModel) || (arg0 instanceof AttributeValueModel)){
					return true;
				}else{
					return false;
				}
			}
			
			@Override
			public Object getParent(Object arg0) {
				return null;
			}
			
			@Override
			public Object[] getElements(Object arg0) {
				if (arg0 instanceof ArrayList){
					
					return ((ArrayList) arg0).toArray();
					
				}else {
					
					return null;
				}
			}
			
			@Override
			public Object[] getChildren(Object arg0) {
				
				ArrayList returnValue = new ArrayList();
				
				if (arg0 instanceof ImmobiliModel){
					ArrayList<AttributeModel> al = ((ImmobiliModel)arg0).getEntity().getAttributes();
					for (AttributeModel attributeModel : al) {
						attributeModel.getValueForReport(((ImmobiliModel)arg0).getCodImmobile());						
					}
					if (((ImmobiliModel)arg0).getAnagrafichePropietarie() != null){
						
						for (AnagraficheModel anagrafica : ((ImmobiliModel)arg0).getAnagrafichePropietarie()){
							
							ImmobiliPropietariXMLModel ipXMLModel = new ImmobiliPropietariXMLModel();
							
							ipXMLModel.setCodImmobile(((ImmobiliModel)arg0).getCodImmobile());
							ipXMLModel.setCodAnagrafica(anagrafica.getCodAnagrafica());
							
							returnValue.add(ipXMLModel);
						}
						
					}
					if (((ImmobiliModel)arg0).getAgenteInseritore() != null){
						returnValue.add(((ImmobiliModel)arg0).getAgenteInseritore());
					}
					if (((ImmobiliModel)arg0).getClasseEnergetica() != null){
						returnValue.add(((ImmobiliModel)arg0).getClasseEnergetica());
					}
					if (((ImmobiliModel)arg0).getRiscaldamento() != null){
						returnValue.add(((ImmobiliModel)arg0).getRiscaldamento());
					}
					if (((ImmobiliModel)arg0).getStatoConservativo() != null){
						returnValue.add(((ImmobiliModel)arg0).getStatoConservativo());
					}
					if (((ImmobiliModel)arg0).getTipologiaImmobile() != null){
						returnValue.add(((ImmobiliModel)arg0).getTipologiaImmobile());
					}
					if (((ImmobiliModel)arg0).getColloquiVisiteReport() != null){
						returnValue.addAll(((ImmobiliModel)arg0).getColloquiVisiteReport());
					}
					if (((ImmobiliModel)arg0).getImmagini() != null){
						returnValue.addAll(((ImmobiliModel)arg0).getImmagini());
					}
					if (((ImmobiliModel)arg0).getAllegati() != null){
						returnValue.addAll(((ImmobiliModel)arg0).getAllegati());
					}
					if (((ImmobiliModel)arg0).getAnagraficheAbbinate() != null){
						returnValue.addAll(((ImmobiliModel)arg0).getAnagraficheAbbinate());
					}
					if (((ImmobiliModel)arg0).getDatiCatastali() != null){
						returnValue.addAll(((ImmobiliModel)arg0).getDatiCatastali());
					}
					if (((ImmobiliModel)arg0).getStanze() != null){
						returnValue.addAll(((ImmobiliModel)arg0).getStanze());
					}
					ArrayList<AffittiModel> affitti = ah.getAffittiByImmobile(((ImmobiliModel)arg0).getCodImmobile());
					if (affitti != null){
						returnValue.addAll(affitti);
					}
					if (((ImmobiliModel)arg0).getEntity() != null){
						EntityXMLModel exmlmodel = new EntityXMLModel((EntityVO)((ImmobiliModel)arg0).getEntity());
						exmlmodel.setIdObject(((ImmobiliModel)arg0).getCodImmobile());
						returnValue.add(exmlmodel);
					}
					
				}
				if (arg0 instanceof AnagraficheModel){
					ArrayList<AttributeModel> al = ((AnagraficheModel)arg0).getEntity().getAttributes();
					for (AttributeModel attributeModel : al) {
						attributeModel.getValueForReport(((AnagraficheModel)arg0).getCodAnagrafica());
					}

					if (((AnagraficheModel)arg0).getClasseCliente() != null){
						returnValue.add(((AnagraficheModel)arg0).getClasseCliente());
					}
					if (((AnagraficheModel)arg0).getContatti() != null){
						returnValue.addAll(((AnagraficheModel)arg0).getContatti());
					}	
					if (((AnagraficheModel)arg0).getEntity() != null){
						EntityXMLModel exmlmodel = new EntityXMLModel((EntityVO)((AnagraficheModel)arg0).getEntity());
						exmlmodel.setIdObject(((AnagraficheModel)arg0).getCodAnagrafica());
						returnValue.add(exmlmodel);
					}
				}
				if (arg0 instanceof AgentiVO){
					AgentiModel am = new AgentiModel((AgentiVO)arg0);
					if (am.getContatti()!= null){
						returnValue.addAll(am.getContatti());
					}					
				}
				if (arg0 instanceof ColloquiModel){
//					if (((ColloquiModel)arg0).getImmobileAbbinato()!= null){
//						returnValue.add(((ColloquiModel)arg0).getImmobileAbbinato());
//					}
					
					ArrayList<AttributeModel> al = ((ColloquiModel)arg0).getEntity().getAttributes();
					for (AttributeModel attributeModel : al) {
						attributeModel.getValueForReport(((ColloquiModel)arg0).getCodColloquio());
					}					
					
					if (((ColloquiModel)arg0).getAnagrafiche() != null){
						returnValue.addAll(((ColloquiModel)arg0).getAnagrafiche());
					}										
					if (((ColloquiModel)arg0).getCriteriRicerca() != null){
						returnValue.addAll(((ColloquiModel)arg0).getCriteriRicerca());
					}										
					if (((ColloquiModel)arg0).getAgenteInseritore()!= null){
						returnValue.add(((ColloquiModel)arg0).getAgenteInseritore());
					}					
					if (((ColloquiModel)arg0).getAllegati()!= null){
						returnValue.addAll(((ColloquiModel)arg0).getAllegati());
					}	
					if (((ColloquiModel)arg0).getAgenti()!= null){
						returnValue.addAll(((ColloquiModel)arg0).getAgenti());
/*						Iterator it = ((ColloquiModel)arg0).getAgenti().iterator();
						while(it.hasNext()){
							ColloquiAgentiModel_Age cam_age= (ColloquiAgentiModel_Age)it.next();
							if (cam_age.getAgente() != null){
								returnValue.add(cam_age);
							}
						}
*/						
					}	
					if (((ColloquiModel)arg0).getEntity() != null){
						EntityXMLModel exmlmodel = new EntityXMLModel((EntityVO)((ColloquiModel)arg0).getEntity());
						exmlmodel.setIdObject(((ColloquiModel)arg0).getCodColloquio());
						returnValue.add(exmlmodel);
					}
					
//					if (((ColloquiModel)arg0).getCriteriRicerca()!= null){
//						returnValue.addAll(((ColloquiModel)arg0).getCriteriRicerca());
//					}										
				}
				if (arg0 instanceof ColloquiAnagraficheModel_Ang){
					if ((((ColloquiAnagraficheModel_Ang)arg0).getAnagrafica() != null) && 
						(((ColloquiAnagraficheModel_Ang)arg0).getAnagrafica().getClasseCliente() != null)){
						returnValue.add(((ColloquiAnagraficheModel_Ang)arg0).getAnagrafica().getClasseCliente());
					}
					if ((((ColloquiAnagraficheModel_Ang)arg0).getAnagrafica() != null) && 
						(((ColloquiAnagraficheModel_Ang)arg0).getAnagrafica().getContatti() != null)){
						returnValue.addAll(((ColloquiAnagraficheModel_Ang)arg0).getAnagrafica().getContatti());
					}					
				}
				if (arg0 instanceof ColloquiAgentiModel_Age){
					if (((ColloquiAgentiModel_Age)arg0).getAgente() != null){
						returnValue.add(((ColloquiAgentiModel_Age)arg0).getAgente());
					}
				}				
				if (arg0 instanceof StanzeImmobiliModel){
					if (((StanzeImmobiliModel)arg0).getTipologia()!= null){
						returnValue.add(((StanzeImmobiliModel)arg0).getTipologia());
					}					
				}
				if (arg0 instanceof ContattiModel){
					if (((ContattiModel)arg0).getTipologia()!= null){
						returnValue.add(((ContattiModel)arg0).getTipologia());
					}					
				}
				if (arg0 instanceof AffittiModel){
					
					ArrayList<AttributeModel> al = ((AffittiModel)arg0).getEntity().getAttributes();
					for (AttributeModel attributeModel : al) {
						attributeModel.getValueForReport(((AffittiModel)arg0).getCodAffitti());
					}										
					
					if (((AffittiModel)arg0).getRate()!= null){
						returnValue.addAll(((AffittiModel)arg0).getRate());
					}
					if (((AffittiModel)arg0).getSpese()!= null){
						returnValue.addAll(((AffittiModel)arg0).getSpese());
					}					
					if (((AffittiModel)arg0).getAllegati()!= null){
						returnValue.addAll(((AffittiModel)arg0).getAllegati());
					}					
					if (((AffittiModel)arg0).getAnagrafiche()!= null){
						returnValue.addAll(((AffittiModel)arg0).getAnagrafiche());
					}
					if (((AffittiModel)arg0).getEntity() != null){
						EntityXMLModel exmlmodel = new EntityXMLModel((EntityVO)((AffittiModel)arg0).getEntity());
						exmlmodel.setIdObject(((AffittiModel)arg0).getCodAffitti());
						returnValue.add(exmlmodel);
					}
					
				}
				if (arg0 instanceof AffittiAnagraficheModel){
					if (((AffittiAnagraficheModel)arg0).getAnagrafica()!= null){
						returnValue.add(((AffittiAnagraficheModel)arg0).getAnagrafica());
					}
				}
				if (arg0 instanceof AbbinamentiModel){
					if (((AbbinamentiModel)arg0).getAnagrafica()!= null){
						returnValue.add(((AbbinamentiModel)arg0).getAnagrafica());
					}
				}
				if (arg0 instanceof EntityXMLModel){
					if (((EntityXMLModel)arg0).getAttributes()!= null){
						for (Iterator iterator = ((EntityXMLModel)arg0).getAttributes().iterator(); iterator.hasNext();) {
							AttributeXMLModel axmlModel = new AttributeXMLModel((AttributeModel)iterator.next());
							axmlModel.setIdObject(((EntityXMLModel)arg0).getIdObject());
							returnValue.add(axmlModel);
						}
					}
				}
				if (arg0 instanceof AttributeXMLModel){
					if (((AttributeXMLModel)arg0).getValue(((AttributeXMLModel)arg0).getIdObject())!= null){						
						returnValue.add(((AttributeXMLModel)arg0).getValue(((AttributeXMLModel)arg0).getIdObject()));
					}
				}
				
//				if (arg0 instanceof AbbinamentiModel){
//					if (((AbbinamentiModel)arg0).getAnagrafica()!= null){
//						returnValue.add(((AbbinamentiModel)arg0).getAnagrafica());
//					}					
//				}
//				if (arg0 instanceof AbbinamentiModel){
//					if (((AbbinamentiModel)arg0).getImmobile()!= null){
//						returnValue.add(((AbbinamentiModel)arg0).getImmobile());
//					}					
//				}
								
				return returnValue.toArray();
			}
		});
		
		tree.setLabelProvider(label_provider);
		tbm.add(new SelezionaTuttoAction(tree,Action.AS_CHECK_BOX));
		tbm.update(true);

		setControl(cb);

		
	}
	
	public void setRisultati_Selected(ArrayList risultatiSelected){
		tree.setInput(risultatiSelected);
		tree.expandAll();
	}
	
	private void checkPath(TreeItem item, boolean checked, boolean grayed) {
	    if (item == null) return;
	    if (grayed) {
	        checked = true;
	    } else {
	        int index = 0;
	        TreeItem[] items = item.getItems();
	        while (index < items.length) {
	            TreeItem child = items[index];
	            if (child.getGrayed() || checked != child.getChecked()) {
	                checked = grayed = true;
	                break;
	            }
	            index++;
	        }
	    }
	    item.setChecked(checked);
	    item.setGrayed(grayed);
	    
	    ((ExporterWizard)getWizard()).getExporterVO()
	    							 .hashMappersSetter(((ExporterWizard)getWizard()), item.getData(), checked);
	    checkPath(item.getParentItem(), checked, grayed);
	}
	
	private void checkItems(TreeItem item, boolean checked) {
	    item.setGrayed(false);
	    item.setChecked(checked);
	    ((ExporterWizard)getWizard()).getExporterVO()
		 							 .hashMappersSetter(((ExporterWizard)getWizard()), item.getData(), checked);	    
	    TreeItem[] items = item.getItems();
	    for (int i = 0; i < items.length; i++) {
	        checkItems(items[i], checked);
		    ((ExporterWizard)getWizard()).getExporterVO()
			 							 .hashMappersSetter(((ExporterWizard)getWizard()), items[i].getData(), checked);
	        
	    }
	}	

}
