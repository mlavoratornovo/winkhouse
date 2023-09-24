package winkhouse.xmlser.wizard.exporter;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.osgi.framework.Version;

import winkhouse.export.WrongCriteriaSequenceException;
import winkhouse.export.helpers.AnagraficheHelper;
import winkhouse.export.helpers.ImmobiliHelper;
import winkhouse.export.helpers.UtilsHelper;
import winkhouse.model.AffittiAnagraficheModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AttributeModel;
import winkhouse.model.AttributeValueModel;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.model.EntityModel;
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
import winkhouse.vo.ImmagineVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.RicercaVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StanzeImmobiliVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.Activator;
import winkhouse.xmlser.helper.XMLExportHelper;
import winkhouse.xmlser.models.xml.AbbinamentiXMLModel;
import winkhouse.xmlser.models.xml.AffittiAllegatiXMLModel;
import winkhouse.xmlser.models.xml.AffittiAnagraficheXMLModel;
import winkhouse.xmlser.models.xml.AffittiRateXMLModel;
import winkhouse.xmlser.models.xml.AffittiSpeseXMLModel;
import winkhouse.xmlser.models.xml.AffittiXMLModel;
import winkhouse.xmlser.models.xml.AgentiXMLModel;
import winkhouse.xmlser.models.xml.AllegatiColloquiXMLModel;
import winkhouse.xmlser.models.xml.AllegatiImmobiliXMLModel;
import winkhouse.xmlser.models.xml.AnagraficheXMLModel;
import winkhouse.xmlser.models.xml.AttributeValueXMLModel;
import winkhouse.xmlser.models.xml.AttributeXMLModel;
import winkhouse.xmlser.models.xml.ClasseEnergeticaXMLModel;
import winkhouse.xmlser.models.xml.ClassiClientiXMLModel;
import winkhouse.xmlser.models.xml.ColloquiAgentiXMLModel;
import winkhouse.xmlser.models.xml.ColloquiAnagraficheXMLModel;
import winkhouse.xmlser.models.xml.ColloquiXMLModel;
import winkhouse.xmlser.models.xml.ContattiXMLModel;
import winkhouse.xmlser.models.xml.CriteriRicercaXMLModel;
import winkhouse.xmlser.models.xml.DatiCatastaliXMLModel;
import winkhouse.xmlser.models.xml.EntityXMLModel;
import winkhouse.xmlser.models.xml.ImmagineXMLModel;
import winkhouse.xmlser.models.xml.ImmobiliPropietariXMLModel;
import winkhouse.xmlser.models.xml.ImmobiliXMLModel;
import winkhouse.xmlser.models.xml.RiscaldamentiXMLModel;
import winkhouse.xmlser.models.xml.StanzeImmobiliXMLModel;
import winkhouse.xmlser.models.xml.StatoConservativoXMLModel;
import winkhouse.xmlser.models.xml.TipologiaContattiXMLModel;
import winkhouse.xmlser.models.xml.TipologiaStanzeXMLModel;
import winkhouse.xmlser.models.xml.TipologieImmobiliXMLModel;
import winkhouse.xmlser.wizard.exporter.pages.ListaCriteriAnagrafiche;
import winkhouse.xmlser.wizard.exporter.pages.ListaCriteriImmobili;
import winkhouse.xmlser.wizard.exporter.pages.ListaCriteriImmobiliAffitti;
import winkhouse.xmlser.wizard.exporter.pages.ListaRisultatiAnagrafiche;
import winkhouse.xmlser.wizard.exporter.pages.ListaRisultatiImmobili;
import winkhouse.xmlser.wizard.exporter.pages.SelectTypeExport;
import winkhouse.xmlser.wizard.exporter.pages.SelettoreDatiBase;
import winkhouse.xmlser.wizard.exporter.pages.SelettoreDestinazioneEsportazione;
import winkhouse.xmlser.wizard.exporter.pages.SelettoreEntitaImmobili;

public class ExporterWizard extends Wizard {

	public static int IMMOBILI = 1;
	public static int ANAGRAFICHE = 2;
	public static int AFFITTI = 3;	
	public static int COLLOQUI = 4;
	public static int APPUNTAMENTI = 5;
	public static int DATI_BASE = 6;
	
	private WizardPage currentPage = null;
	private RicercaVO ricercaVO = null;
	private ExporterVO exporterVO = null;
	
	private SelectTypeExport selectTypeExport = null;
	private ListaCriteriImmobili listaCriteriImmobili = null;
	private ListaCriteriAnagrafiche listaCriteriAnagrafiche = null;
	private ListaCriteriImmobiliAffitti listaCriteriImmobiliAffitti = null; 
	private ListaRisultatiImmobili listaRisultatiImmobili = null;
	private ListaRisultatiAnagrafiche listaRisultatiAnagrafiche = null;
	private SelettoreEntitaImmobili selettoreEntitaImmobili = null; 
	private SelettoreDestinazioneEsportazione selettoreDestinazioneEsportazione = null;
	private SelettoreDatiBase selettoreDatiBase = null;
	
	public ExporterWizard() {
		
	}
	
	public class ExporterVO{
		
		private int type = IMMOBILI;
		private ArrayList<ColloquiCriteriRicercaModel> criteriRicerca = null;
		private ArrayList risultati = null;
		private ArrayList risultati_selected = null;
		
		private HashMap hmImmobili = null;
		private HashMap hmAnagrafiche = null;
		private HashMap hmColloqui = null;
		private HashMap hmAgenti = null;
		private HashMap hmContatti = null;
		private HashMap hmAffitti = null;
		private HashMap hmAppuntamenti = null;
		private HashMap hmImmagini = null;
		private HashMap hmCriteriRicerca = null;
		private HashMap hmDatiCatastali = null;
		private HashMap hmAffittiAllegati = null;
		private HashMap hmAffittiAnagrafiche = null;
		private HashMap hmAffittiRate = null;
		private HashMap hmAffittiSpese = null;
		private HashMap hmAgentiAppuntamenti = null;
		private HashMap hmAllegatiColloquio = null;
		private HashMap hmAllegatiImmobili = null;
		private HashMap hmAnagraficheAppuntamenti = null;
		private HashMap hmColloquiAgenti = null;
		private HashMap hmColloquiAnagrafica = null;
		private HashMap hmColloquiCriteriRicerca = null;
		private HashMap hmGCalendar = null;
		private HashMap hmStanzeImmobili = null;
		private HashMap hmRicerche = null;
		private HashMap hmTipiImmobili = null;
		private HashMap hmCategorieClienti = null;
		private HashMap hmRiscaldamento = null;
		private HashMap hmTipiStanze = null;
		private HashMap hmStatiConservativi = null;
		private HashMap hmClassiEnergetiche = null;
		private HashMap hmTipiAppuntamento = null;
		private HashMap hmAbbinamenti = null;
		private HashMap hmTipiContatti = null;
		private HashMap hmEntity = null;
		private HashMap hmAttribute = null;
		private HashMap hmAttributeValue = null;
		private HashMap hmImmobiliPropietari = null;
		
		private boolean flagCollegati = false;
		private boolean flagDatiBaseCompleti = false;
		private String pathExportFile = null;
		
		private boolean isZipped = false;
		
		public ExporterVO(){
			 hmImmobili = new HashMap();
			 hmAnagrafiche = new HashMap();
			 hmColloqui = new HashMap();
			 hmAgenti = new HashMap();
			 hmContatti = new HashMap();
			 hmAffitti = new HashMap();
			 hmAppuntamenti = new HashMap();
			 hmImmagini = new HashMap();
			 hmCriteriRicerca = new HashMap();
			 hmDatiCatastali = new HashMap();
			 hmAffittiAllegati = new HashMap();
			 hmAffittiAnagrafiche = new HashMap();
			 hmAffittiRate = new HashMap();
			 hmAffittiSpese = new HashMap();
			 hmAgentiAppuntamenti = new HashMap();
			 hmAllegatiColloquio = new HashMap();
			 hmAllegatiImmobili = new HashMap();
			 hmAnagraficheAppuntamenti = new HashMap();
			 hmColloquiAgenti = new HashMap();
			 hmColloquiAnagrafica = new HashMap();
			 hmColloquiCriteriRicerca = new HashMap();
			 hmGCalendar = new HashMap();
			 hmStanzeImmobili = new HashMap();
			 hmRicerche = new HashMap();
			 hmTipiImmobili = new HashMap();			 
			 hmCategorieClienti = new HashMap();
			 hmRiscaldamento = new HashMap();
			 hmTipiStanze = new HashMap();
			 hmStatiConservativi = new HashMap();
			 hmClassiEnergetiche = new HashMap();
			 hmTipiAppuntamento = new HashMap();
			 hmAbbinamenti = new HashMap();
			 hmTipiContatti = new HashMap();
			 hmEntity = new HashMap();
			 hmAttribute = new HashMap();
			 hmAttributeValue = new HashMap();
			 hmImmobiliPropietari = new HashMap();
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
		
		public boolean almostOneSelected(){
			if (getHmAbbinamenti().size() > 0 || getHmAffitti().size() > 0 || getHmAffittiAllegati().size() > 0 ||
				getHmAffittiAnagrafiche().size() > 0 || getHmAffittiRate().size() > 0 || getHmAffittiSpese().size() > 0 ||
				getHmAffittiAllegati().size() > 0 ||
				getHmAgenti().size() > 0 || getHmAgentiAppuntamenti().size() > 0 || getHmAllegatiColloquio().size() > 0 ||
				getHmAllegatiImmobili().size() > 0 || getHmAnagrafiche().size() > 0 || getHmAnagraficheAppuntamenti().size() > 0 ||
				getHmAppuntamenti().size() > 0 || getHmCategorieClienti().size() > 0 || getHmClassiEnergetiche().size() > 0 ||
				getHmColloqui().size() > 0 || getHmColloquiAgenti().size() > 0 || getHmColloquiAnagrafica().size() > 0 ||
				getHmColloquiCriteriRicerca().size() > 0 || getHmContatti().size() > 0 || getHmCriteriRicerca().size() > 0 ||
				getHmDatiCatastali().size() > 0 || getHmGCalendar().size() > 0 || getHmImmagini().size() > 0 ||
				getHmImmobili().size() > 0 || getHmRicerche().size() > 0 || getHmRiscaldamento().size() > 0 ||
				getHmStanzeImmobili().size() > 0 || getHmStatiConservativi().size() > 0 || getHmTipiAppuntamento().size() > 0 ||
				getHmTipiContatti().size() > 0 || getHmTipiImmobili().size() > 0 || getHmTipiStanze().size() > 0 ){
				
				return true;
				
			}else{
				
				return false;
			}
		}
		
		public void hashMappersSetter(ExporterWizard wizard, Object objectSelected,boolean checked){
			
		    if(objectSelected instanceof ImmobiliVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmImmobili().containsKey(((ImmobiliVO)objectSelected).getCodImmobile())){

		    			getHmImmobili().put(((ImmobiliVO)objectSelected).getCodImmobile(),
							  				new ImmobiliXMLModel((ImmobiliVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		getHmImmobili().remove(((ImmobiliVO)objectSelected).getCodImmobile());
	    			
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof AffittiVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAffitti().containsKey(((AffittiVO)objectSelected).getCodAffitti())){

		    			getHmAffitti().put(((AffittiVO)objectSelected).getCodAffitti(),
							  				new AffittiXMLModel((AffittiVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		getHmAffitti().remove(((AffittiVO)objectSelected).getCodAffitti());
	    			
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof ImmobiliPropietariXMLModel) {
		    	
	    		String codpropieta = String.valueOf(((ImmobiliPropietariXMLModel)objectSelected).getCodAnagrafica()) + 
			   			 			 "_" +
			   			 			 String.valueOf(((ImmobiliPropietariXMLModel)objectSelected).getCodImmobile());
		    	
		    	if (checked){
		    		if (!getHmImmobiliPropietari().containsKey(codpropieta)){
		    			
		    			getHmAnagrafiche().put(((ImmobiliPropietariXMLModel)objectSelected).getAnagrafica().getCodAnagrafica(),
		    								   new AnagraficheXMLModel(((ImmobiliPropietariXMLModel)objectSelected).getAnagrafica()));
		    			
		    			getHmImmobili().put(((ImmobiliPropietariXMLModel)objectSelected).getImmobile().getCodImmobile(),
		    								new ImmobiliXMLModel(((ImmobiliPropietariXMLModel)objectSelected).getImmobile()));
		    			
		    			getHmImmobiliPropietari().put(codpropieta,objectSelected);

		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		getHmImmobiliPropietari().remove(codpropieta);
	    			
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    
		    if(objectSelected instanceof AffittiRateVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAffittiRate().containsKey(((AffittiRateVO)objectSelected).getCodAffittiRate())){

		    			getHmAffittiRate().put(((AffittiRateVO)objectSelected).getCodAffittiRate(),
							  				new AffittiRateXMLModel((AffittiRateVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		getHmAffittiRate().remove(((AffittiVO)objectSelected).getCodAffitti());
	    			
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }		    
		    if(objectSelected instanceof AffittiSpeseVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAffittiSpese().containsKey(((AffittiSpeseVO)objectSelected).getCodAffittiSpese())){

		    			getHmAffittiSpese().put(((AffittiSpeseVO)objectSelected).getCodAffittiSpese(),
							  				new AffittiSpeseXMLModel((AffittiSpeseVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		getHmAffitti().remove(((AffittiVO)objectSelected).getCodAffitti());
	    			
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }		    		    
		    if(objectSelected instanceof AnagraficheVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAnagrafiche().containsKey(((AnagraficheVO)objectSelected).getCodAnagrafica())){
		
		    			getHmAnagrafiche().put(((AnagraficheVO)objectSelected).getCodAnagrafica(),
							  				   new AnagraficheXMLModel((AnagraficheVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmAnagrafiche().remove(((AnagraficheVO)objectSelected).getCodAnagrafica());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof AgentiVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAgenti().containsKey(((AgentiVO)objectSelected).getCodAgente())){
		
		    			getHmAgenti().put(((AgentiVO)objectSelected).getCodAgente(),
							  			  new AgentiXMLModel((AgentiVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmAgenti().remove(((AgentiVO)objectSelected).getCodAgente());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof RiscaldamentiVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmRiscaldamento().containsKey(((RiscaldamentiVO)objectSelected).getCodRiscaldamento())){
		
		    			getHmRiscaldamento().put(((RiscaldamentiVO)objectSelected).getCodRiscaldamento(),
							  					 new RiscaldamentiXMLModel((RiscaldamentiVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmRiscaldamento().remove(((RiscaldamentiVO)objectSelected).getCodRiscaldamento());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof ClasseEnergeticaVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmClassiEnergetiche().containsKey(((ClasseEnergeticaVO)objectSelected).getCodClasseEnergetica())){
		
		    			getHmClassiEnergetiche().put(((ClasseEnergeticaVO)objectSelected).getCodClasseEnergetica(),
							  					     new ClasseEnergeticaXMLModel((ClasseEnergeticaVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmClassiEnergetiche().remove(((ClasseEnergeticaVO)objectSelected).getCodClasseEnergetica());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof StatoConservativoVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmStatiConservativi().containsKey(((StatoConservativoVO)objectSelected).getCodStatoConservativo())){
		
		    			getHmStatiConservativi().put(((StatoConservativoVO)objectSelected).getCodStatoConservativo(),
							  						 new StatoConservativoXMLModel((StatoConservativoVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmStatiConservativi().remove(((StatoConservativoVO)objectSelected).getCodStatoConservativo());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof TipologieImmobiliVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmTipiImmobili().containsKey(((TipologieImmobiliVO)objectSelected).getCodTipologiaImmobile())){
		
		    			getHmTipiImmobili().put(((TipologieImmobiliVO)objectSelected).getCodTipologiaImmobile(),
							  					new TipologieImmobiliXMLModel((TipologieImmobiliVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmTipiImmobili().remove(((TipologieImmobiliVO)objectSelected).getCodTipologiaImmobile());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof ColloquiVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmColloqui().containsKey(((ColloquiVO)objectSelected).getCodColloquio())){
		
		    			getHmColloqui().put(((ColloquiVO)objectSelected).getCodColloquio(),
							  				new ColloquiXMLModel((ColloquiVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmColloqui().remove(((ColloquiVO)objectSelected).getCodColloquio());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof ImmagineVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmImmagini().containsKey(((ImmagineVO)objectSelected).getCodImmagine())){
		
		    			getHmImmagini().put(((ImmagineVO)objectSelected).getCodImmagine(),
							  			    new ImmagineXMLModel((ImmagineVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmImmagini().remove(((ImmagineVO)objectSelected).getCodImmagine());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof AllegatiImmobiliVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAllegatiImmobili().containsKey(((AllegatiImmobiliVO)objectSelected).getCodAllegatiImmobili())){
		
		    			getHmAllegatiImmobili().put(((AllegatiImmobiliVO)objectSelected).getCodAllegatiImmobili(),
							  					    new AllegatiImmobiliXMLModel((AllegatiImmobiliVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmAllegatiImmobili().remove(((AllegatiImmobiliVO)objectSelected).getCodAllegatiImmobili());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof AllegatiColloquiVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAllegatiColloquio().containsKey(((AllegatiColloquiVO)objectSelected).getCodAllegatiColloquio())){
		
		    			getHmAllegatiColloquio().put(((AllegatiColloquiVO)objectSelected).getCodAllegatiColloquio(),
							  						 new AllegatiColloquiXMLModel((AllegatiColloquiVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmAllegatiColloquio().remove(((AllegatiColloquiVO)objectSelected).getCodAllegatiColloquio());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof AffittiAllegatiVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAffittiAllegati().containsKey(((AffittiAllegatiVO)objectSelected).getCodAffittiAllegati())){
		
		    			getHmAffittiAllegati().put(((AffittiAllegatiVO)objectSelected).getCodAffittiAllegati(),
							  						 new AffittiAllegatiXMLModel((AffittiAllegatiVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmAffittiAllegati().remove(((AffittiAllegatiVO)objectSelected).getCodAffittiAllegati());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof DatiCatastaliVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmDatiCatastali().containsKey(((DatiCatastaliVO)objectSelected).getCodDatiCatastali())){
		
		    			getHmDatiCatastali().put(((DatiCatastaliVO)objectSelected).getCodDatiCatastali(),
							  					 new DatiCatastaliXMLModel((DatiCatastaliVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmDatiCatastali().remove(((DatiCatastaliVO)objectSelected).getCodDatiCatastali());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof StanzeImmobiliVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmStanzeImmobili().containsKey(((StanzeImmobiliVO)objectSelected).getCodStanzeImmobili())){
		
		    			getHmStanzeImmobili().put(((StanzeImmobiliVO)objectSelected).getCodStanzeImmobili(),
							  					  new StanzeImmobiliXMLModel((StanzeImmobiliVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmStanzeImmobili().remove(((StanzeImmobiliVO)objectSelected).getCodStanzeImmobili());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof ClassiClientiVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmCategorieClienti().containsKey(((ClassiClientiVO)objectSelected).getCodClasseCliente())){
		
		    			getHmCategorieClienti().put(((ClassiClientiVO)objectSelected).getCodClasseCliente(),
							  						new ClassiClientiXMLModel((ClassiClientiVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmCategorieClienti().remove(((ClassiClientiVO)objectSelected).getCodClasseCliente());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof ContattiVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmContatti().containsKey(((ContattiVO)objectSelected).getCodContatto())){
		
		    			getHmContatti().put(((ContattiVO)objectSelected).getCodContatto(),
							  				new ContattiXMLModel((ContattiVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmContatti().remove(((ContattiVO)objectSelected).getCodContatto());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof AbbinamentiVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAbbinamenti().containsKey(((AbbinamentiVO)objectSelected).getCodAbbinamento())){
		
		    			getHmAbbinamenti().put(((AbbinamentiVO)objectSelected).getCodAbbinamento(),
							  				   new AbbinamentiXMLModel((AbbinamentiVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmAbbinamenti().remove(((AbbinamentiVO)objectSelected).getCodAbbinamento());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof CriteriRicercaModel) {
		    	
		    	if (checked){
		    		
		    		if (!getHmCriteriRicerca().containsKey(((CriteriRicercaModel)objectSelected).getCodCriterioRicerca())){
		
		    			getHmCriteriRicerca().put(((CriteriRicercaModel)objectSelected).getCodCriterioRicerca(),
							  					  new CriteriRicercaXMLModel((CriteriRicercaModel)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmCriteriRicerca().remove(((CriteriRicercaModel)objectSelected).getCodCriterioRicerca());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof ColloquiCriteriRicercaVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmCriteriRicerca().containsKey(((ColloquiCriteriRicercaVO)objectSelected).getCodCriterioRicerca())){
		
		    			getHmCriteriRicerca().put(((ColloquiCriteriRicercaVO)objectSelected).getCodCriterioRicerca(),
							  					  new CriteriRicercaXMLModel((ColloquiCriteriRicercaVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmCriteriRicerca().remove(((CriteriRicercaModel)objectSelected).getCodCriterioRicerca());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof TipologiaStanzeVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmTipiStanze().containsKey(((TipologiaStanzeVO)objectSelected).getCodTipologiaStanza())){
		
		    			getHmTipiStanze().put(((TipologiaStanzeVO)objectSelected).getCodTipologiaStanza(),
							  				  new TipologiaStanzeXMLModel((TipologiaStanzeVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmTipiStanze().remove(((TipologiaStanzeVO)objectSelected).getCodTipologiaStanza());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof TipologiaContattiVO) {
		    	
		    	if (checked){
		    		
		    		if (!getHmTipiContatti().containsKey(((TipologiaContattiVO)objectSelected).getCodTipologiaContatto())){
		
		    			getHmTipiContatti().put(((TipologiaContattiVO)objectSelected).getCodTipologiaContatto(),
							  				    new TipologiaContattiXMLModel((TipologiaContattiVO)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmTipiContatti().remove(((TipologiaContattiVO)objectSelected).getCodTipologiaContatto());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof ColloquiAgentiModel_Age) {
		    	
		    	if (checked){
		    		
		    		if (!getHmColloquiAgenti().containsKey(((ColloquiAgentiModel_Age)objectSelected).getCodColloquioAgenti())){
		
		    			getHmColloquiAgenti().put(((ColloquiAgentiModel_Age)objectSelected).getCodColloquioAgenti(),
							  					  new ColloquiAgentiXMLModel((ColloquiAgentiModel_Age)objectSelected));
		    			
		    			getHmAgenti().put(((ColloquiAgentiModel_Age)objectSelected).getAgente().getCodAgente(),
		    							  new AgentiXMLModel(((ColloquiAgentiModel_Age)objectSelected).getAgente()));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmColloquiAgenti().remove(((ColloquiAgentiModel_Age)objectSelected).getCodColloquioAgenti());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof ColloquiAnagraficheModel_Ang) {
		    	
		    	if (checked){
		    		
		    		if (!getHmColloquiAnagrafica().containsKey(((ColloquiAnagraficheModel_Ang)objectSelected).getCodColloquioAnagrafiche())){
		
		    			getHmColloquiAnagrafica().put(((ColloquiAnagraficheModel_Ang)objectSelected).getCodColloquioAnagrafiche(),
							  					  new ColloquiAnagraficheXMLModel((ColloquiAnagraficheModel_Ang)objectSelected));
		    			
		    			getHmAnagrafiche().put(((ColloquiAnagraficheModel_Ang)objectSelected).getCodAnagrafica(),
		    								   new AnagraficheXMLModel(((ColloquiAnagraficheModel_Ang)objectSelected).getAnagrafica()));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmColloquiAnagrafica().remove(((ColloquiAnagraficheModel_Ang)objectSelected).getCodColloquioAnagrafiche());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof AffittiAnagraficheModel) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAffittiAnagrafiche().containsKey(((AffittiAnagraficheModel)objectSelected).getCodAffittiAnagrafiche())){
		
		    			getHmAffittiAnagrafiche().put(((AffittiAnagraficheModel)objectSelected).getCodAffittiAnagrafiche(),
							  					  new AffittiAnagraficheXMLModel((AffittiAnagraficheModel)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmAffittiAnagrafiche().remove(((AffittiAnagraficheModel)objectSelected).getCodAffittiAnagrafiche());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof EntityModel) {
		    	
		    	if (checked){
		    		
		    		if (!getHmEntity().containsKey(((EntityModel)objectSelected).getIdClassEntity())){
		
		    			getHmEntity().put(((EntityModel)objectSelected).getIdClassEntity(),
							  					  new EntityXMLModel((EntityModel)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmEntity().remove(((EntityModel)objectSelected).getIdClassEntity());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof AttributeModel) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAttribute().containsKey(((AttributeModel)objectSelected).getIdAttribute())){
		
		    			getHmAttribute().put(((AttributeModel)objectSelected).getIdAttribute(),
							  					  new AttributeXMLModel((AttributeModel)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmAttribute().remove(((AttributeModel)objectSelected).getIdAttribute());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }
		    if(objectSelected instanceof AttributeValueModel) {
		    	
		    	if (checked){
		    		
		    		if (!getHmAttributeValue().containsKey(((AttributeValueModel)objectSelected).getIdValue())){
		
		    			getHmAttributeValue().put(((AttributeValueModel)objectSelected).getIdValue(),
							  					  new AttributeValueXMLModel((AttributeValueModel)objectSelected));
		    		}
		    		
		    		wizard.getContainer().updateButtons();
		    		    									  
		    	}else{
		    		
		    		getHmAttributeValue().remove(((AttributeValueModel)objectSelected).getIdValue());
					
		    		wizard.getContainer().updateButtons();
		    	}
		    		
		    }


		}

		public ArrayList<ColloquiCriteriRicercaModel> getCriteriRicerca() {
			return criteriRicerca;
		}

		public void setCriteriRicerca(ArrayList<ColloquiCriteriRicercaModel> criteriRicerca) {
			this.criteriRicerca = criteriRicerca;
		}

		public ArrayList getRisultati() {
			return risultati;
		}

		public void setRisultati(ArrayList risultati) {
			this.risultati = risultati;
		}

		public boolean isFlagCollegati() {
			return flagCollegati;
		}

		public void setFlagCollegati(boolean flagCollegati) {
			this.flagCollegati = flagCollegati;
		}

		public String getPathExportFile() {
			return pathExportFile;
		}

		public void setPathExportFile(String pathExportFile) {
			this.pathExportFile = pathExportFile;
		}

		public ArrayList getRisultati_selected() {
			if (risultati_selected == null){
				risultati_selected = new ArrayList();
			}
			return risultati_selected;
		}

		public void setRisultati_selected(ArrayList risultati_selected) {
			this.risultati_selected = risultati_selected;
		}

		public HashMap getHmImmobili() {
			return hmImmobili;
		}

		public HashMap getHmAnagrafiche() {
			return hmAnagrafiche;
		}

		public HashMap getHmColloqui() {
			return hmColloqui;
		}

		public HashMap getHmAgenti() {
			return hmAgenti;
		}

		public HashMap getHmContatti() {
			return hmContatti;
		}

		public HashMap getHmAffitti() {
			return hmAffitti;
		}

		public HashMap getHmAppuntamenti() {
			return hmAppuntamenti;
		}

		public HashMap getHmImmagini() {
			return hmImmagini;
		}

		public HashMap getHmCriteriRicerca() {
			return hmCriteriRicerca;
		}

		public HashMap getHmDatiCatastali() {
			return hmDatiCatastali;
		}

		public HashMap getHmAffittiAllegati() {
			return hmAffittiAllegati;
		}

		public HashMap getHmAffittiAnagrafiche() {
			return hmAffittiAnagrafiche;
		}

		public HashMap getHmAffittiRate() {
			return hmAffittiRate;
		}

		public HashMap getHmAffittiSpese() {
			return hmAffittiSpese;
		}

		public HashMap getHmAgentiAppuntamenti() {
			return hmAgentiAppuntamenti;
		}

		public HashMap getHmAllegatiColloquio() {
			return hmAllegatiColloquio;
		}

		public HashMap getHmAllegatiImmobili() {
			return hmAllegatiImmobili;
		}

		public HashMap getHmAnagraficheAppuntamenti() {
			return hmAnagraficheAppuntamenti;
		}

		public HashMap getHmColloquiAgenti() {
			return hmColloquiAgenti;
		}

		public HashMap getHmColloquiAnagrafica() {
			return hmColloquiAnagrafica;
		}

		public HashMap getHmColloquiCriteriRicerca() {
			return hmColloquiCriteriRicerca;
		}

		public HashMap getHmGCalendar() {
			return hmGCalendar;
		}

		public HashMap getHmStanzeImmobili() {
			return hmStanzeImmobili;
		}

		public HashMap getHmRicerche() {
			return hmRicerche;
		}

		public HashMap getHmTipiImmobili() {
			return hmTipiImmobili;
		}

		public HashMap getHmCategorieClienti() {
			return hmCategorieClienti;
		}

		public HashMap getHmRiscaldamento() {
			return hmRiscaldamento;
		}

		public HashMap getHmTipiStanze() {
			return hmTipiStanze;
		}

		public HashMap getHmStatiConservativi() {
			return hmStatiConservativi;
		}

		public HashMap getHmClassiEnergetiche() {
			return hmClassiEnergetiche;
		}

		public HashMap getHmTipiAppuntamento() {
			return hmTipiAppuntamento;
		}

		public HashMap getHmAbbinamenti() {
			return hmAbbinamenti;
		}

		public HashMap getHmTipiContatti() {
			return hmTipiContatti;
		}

		
		public HashMap getHmEntity() {
			return hmEntity;
		}
		

		public HashMap getHmAttribute() {
			return hmAttribute;
		}
		

		public HashMap getHmAttributeValue() {
			return hmAttributeValue;
		}

		
		public boolean isFlagDatiBaseCompleti() {
			return flagDatiBaseCompleti;
		}

		
		public void setFlagDatiBaseCompleti(boolean flagDatiBaseCompleti) {
			this.flagDatiBaseCompleti = flagDatiBaseCompleti;
		}

		public void setHmAgenti(HashMap hmAgenti) {
			this.hmAgenti = hmAgenti;
		}

		public void setHmStanzeImmobili(HashMap hmStanzeImmobili) {
			this.hmStanzeImmobili = hmStanzeImmobili;
		}

		public void setHmTipiImmobili(HashMap hmTipiImmobili) {
			this.hmTipiImmobili = hmTipiImmobili;
		}

		public void setHmCategorieClienti(HashMap hmCategorieClienti) {
			this.hmCategorieClienti = hmCategorieClienti;
		}

		public void setHmRiscaldamento(HashMap hmRiscaldamento) {
			this.hmRiscaldamento = hmRiscaldamento;
		}

		public void setHmTipiStanze(HashMap hmTipiStanze) {
			this.hmTipiStanze = hmTipiStanze;
		}

		public void setHmStatiConservativi(HashMap hmStatiConservativi) {
			this.hmStatiConservativi = hmStatiConservativi;
		}

		public void setHmClassiEnergetiche(HashMap hmClassiEnergetiche) {
			this.hmClassiEnergetiche = hmClassiEnergetiche;
		}

		public void setHmTipiAppuntamento(HashMap hmTipiAppuntamento) {
			this.hmTipiAppuntamento = hmTipiAppuntamento;
		}

		public void setHmTipiContatti(HashMap hmTipiContatti) {
			this.hmTipiContatti = hmTipiContatti;
		}

		
		public HashMap getHmImmobiliPropietari() {
			return hmImmobiliPropietari;
		}

		public boolean isZipped() {
			return isZipped;
		}

		public void setZipped(boolean isZipped) {
			this.isZipped = isZipped;
		}
		
	}

	@Override
	public boolean performFinish() {
		
		return new XMLExportHelper().exportToPath(getExporterVO().pathExportFile, getExporterVO(), getExporterVO().isZipped);
	}

	@Override
	public void addPages() {
		
		selectTypeExport = new SelectTypeExport("Selezione tipologia oggetti da esportare",
				   							    "Selezione tipologia oggetti da esportare",
				   							    Activator.getImageDescriptor("icons/wizardexport/kfind.png"));
		
		selectTypeExport.setDescription("Selezionare la tipologia di oggetti da esportare");
		
		addPage(selectTypeExport);
		/*
		listaCriteriAnagrafiche = new ListaCriteriAnagrafiche("Lista criteri selezione anagrafiche",
				  											  "Lista criteri selezione anagrafiche",
				  											  Activator.getImageDescriptor("icons/wizardricerca/filefind.png"));
		listaCriteriAnagrafiche.setDescription("Inserire i criteri di ricerca per le anagrafiche");
		addPage(listaCriteriAnagrafiche);
		*/
		listaCriteriImmobili = new ListaCriteriImmobili("Lista criteri selezione immobili",
														"Lista criteri selezione immobili",
														Activator.getImageDescriptor("icons/wizardexport/filefind.png"));
		listaCriteriImmobili.setDescription("Inserire i criteri di ricerca per gli immobili");
		addPage(listaCriteriImmobili);

		listaCriteriAnagrafiche = new ListaCriteriAnagrafiche("Lista criteri selezione anagrafiche",
														   	  "Lista criteri selezione anagrafiche",
														   	  Activator.getImageDescriptor("icons/wizardexport/filefind.png"));
		
		listaCriteriAnagrafiche.setDescription("Inserire i criteri di ricerca per le anagrafiche");
		addPage(listaCriteriAnagrafiche);

		listaRisultatiImmobili = new ListaRisultatiImmobili("Lista risultati immobili", 
															"Lista dei risultati ricerca immobili",
															Activator.getImageDescriptor("icons/wizardexport/listaimmobili.png"));
		addPage(listaRisultatiImmobili);

		listaRisultatiAnagrafiche = new ListaRisultatiAnagrafiche("Lista risultati anagrafiche", 
																  "Lista dei risultati ricerca anagrafiche",
																  Activator.getImageDescriptor("icons/wizardexport/ktqueuemanager.png"));
		addPage(listaRisultatiAnagrafiche);

		selettoreEntitaImmobili = new SelettoreEntitaImmobili("Selezione dati collegati",
															  "Selezione dei dati collegati agli immobili selezionati",
															  Activator.getImageDescriptor("icons/wizardexport/selezione.png"));
		addPage(selettoreEntitaImmobili);
		
		selettoreDestinazioneEsportazione = new SelettoreDestinazioneEsportazione("Selezione cartella destinazione",
				  																  "Selezione cartella destinazione",
				  																  Activator.getImageDescriptor("icons/wizardexport/folder_download.png"));
		addPage(selettoreDestinazioneEsportazione);

		selettoreDatiBase = new SelettoreDatiBase("Selezione dati di base",
				  								  "Selezione dati di base",
				  								  Activator.getImageDescriptor("icons/wizardexport/database_48.png"));
		addPage(selettoreDatiBase);

	}
/*
	public RicercaVO getRicercaVO() {
		if (ricercaVO == null){
			ricercaVO = new RicercaVO();
		}
		return ricercaVO;
	}

	public void setRicercaVO(RicercaVO ricercaVO) {
		this.ricercaVO = ricercaVO;
	}
*/
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		
		IWizardPage returnValue = null;
		
		if (page instanceof SelectTypeExport){
			if (getExporterVO().getType() == IMMOBILI){
				returnValue = listaCriteriImmobili;
	        	getExporterVO().setPathExportFile(null);
			}
			if (getExporterVO().getType() == ANAGRAFICHE){
				returnValue = listaCriteriAnagrafiche;
	        	getExporterVO().setPathExportFile(null);
			}
			if (getExporterVO().getType() == DATI_BASE){
				returnValue = selettoreDatiBase;
				selettoreDatiBase.setDatiBaseModels(new XMLExportHelper().getDatiBaseModel());
	        	getExporterVO().setPathExportFile(null);
			}
			
		}
		
		if (page instanceof ListaCriteriImmobili){
			if (getExporterVO().getCriteriRicerca() != null){
				if (getExporterVO().getCriteriRicerca().size() > 0){
					if (getExporterVO().getCriteriRicerca().size() == 1){
						if (((ColloquiCriteriRicercaVO)getExporterVO().getCriteriRicerca()
																	  .get(0)).getGetterMethodName()
																	  .equalsIgnoreCase("(") ||
							((ColloquiCriteriRicercaVO)getExporterVO().getCriteriRicerca()
																	  .get(0)).getGetterMethodName()
																	   .equalsIgnoreCase(")") ||
							((ColloquiCriteriRicercaVO)getExporterVO().getCriteriRicerca()
																	  .get(0)).getGetterMethodName()
																	  .equalsIgnoreCase("")																		
						   ){
							
						}else{
							ImmobiliHelper ih = new ImmobiliHelper();
							ArrayList results = null; 
							try{
								results = ih.getImmobiliByProperties(getExporterVO().getCriteriRicerca());
								listaRisultatiImmobili.setRisultati(results);
								currentPage = listaRisultatiImmobili;
								returnValue = listaRisultatiImmobili;
								getExporterVO().setPathExportFile(null);	
							}catch (WrongCriteriaSequenceException wcse) {
/*								MessageDialog.openError(getShell(), 
														"Errore criteri ricerca",
														"I criteri di ricerca inseriti producono una interrogazione non valida \n" +
														"Modificare i criteri inseriti e riprovare");*/
							}
								
						} 
					}else{
						ImmobiliHelper ih = new ImmobiliHelper();
						ArrayList results = null; 
						try{
							results = ih.getImmobiliByProperties(getExporterVO().getCriteriRicerca());
							listaRisultatiImmobili.setRisultati(results);
							currentPage = listaRisultatiImmobili;
							returnValue = listaRisultatiImmobili;
							getExporterVO().setPathExportFile(null);	
						}catch (WrongCriteriaSequenceException wcse) {
							/*MessageDialog.openError(getShell(), 
													"Errore criteri ricerca",
													"I criteri di ricerca inseriti producono una interrogazione non valida \n" +
													"Modificare i criteri inseriti e riprovare");*/
						}
					}						
				}
			}			
			
		}
		
		if (page instanceof ListaCriteriAnagrafiche){
			if (getExporterVO().getCriteriRicerca() != null){
				if (getExporterVO().getCriteriRicerca().size() > 0){
					if (getExporterVO().getCriteriRicerca().size() == 1){
						if (((ColloquiCriteriRicercaVO)getExporterVO().getCriteriRicerca()
																	  .get(0)).getGetterMethodName()
																	  .equalsIgnoreCase("(") ||
							((ColloquiCriteriRicercaVO)getExporterVO().getCriteriRicerca()
																	  .get(0)).getGetterMethodName()
																	   .equalsIgnoreCase(")") ||
							((ColloquiCriteriRicercaVO)getExporterVO().getCriteriRicerca()
																	  .get(0)).getGetterMethodName()
																	  .equalsIgnoreCase("")																		
						   ){
							
						}else{
							AnagraficheHelper ah = new AnagraficheHelper();
							ArrayList results = null; 
							try{
								results = ah.getAnagraficheByProperties(getExporterVO().getCriteriRicerca());
								listaRisultatiAnagrafiche.setRisultati(results);
								currentPage = listaRisultatiAnagrafiche;
								returnValue = listaRisultatiAnagrafiche;
								getExporterVO().setPathExportFile(null);	
							}catch (WrongCriteriaSequenceException wcse) {
/*								MessageDialog.openError(getShell(), 
														"Errore criteri ricerca",
														"I criteri di ricerca inseriti producono una interrogazione non valida \n" +
														"Modificare i criteri inseriti e riprovare");*/
							}
								
						} 
					}else{
						AnagraficheHelper ah = new AnagraficheHelper();
						ArrayList results = null; 
						try{
							results = ah.getAnagraficheByProperties(getExporterVO().getCriteriRicerca());
							listaRisultatiAnagrafiche.setRisultati(results);
							currentPage = listaRisultatiAnagrafiche;
							returnValue = listaRisultatiAnagrafiche;
							getExporterVO().setPathExportFile(null);	
						}catch (WrongCriteriaSequenceException wcse) {
							/*MessageDialog.openError(getShell(), 
													"Errore criteri ricerca",
													"I criteri di ricerca inseriti producono una interrogazione non valida \n" +
													"Modificare i criteri inseriti e riprovare");*/
						}
					}						
				}
			}			
			
		}
		
		if (page instanceof SelettoreDatiBase){
			
			returnValue = selettoreDestinazioneEsportazione;
			getExporterVO().setPathExportFile(null);
			
		}
		
		if ((page instanceof ListaRisultatiImmobili) || (page instanceof ListaRisultatiAnagrafiche)){
			if (getExporterVO().getRisultati_selected() != null){
				if (getExporterVO().getRisultati_selected().size() > 0){
					
					selettoreEntitaImmobili.setRisultati_Selected(getExporterVO().getRisultati_selected());
					returnValue = selettoreEntitaImmobili; 
					getExporterVO().setPathExportFile(null);
				}
			}
		}
		if (page instanceof SelettoreEntitaImmobili){
			if (getExporterVO() != null){
				if (getExporterVO().almostOneSelected()){	
					returnValue = selettoreDestinazioneEsportazione;
					getExporterVO().setPathExportFile(null);
				}
			}			
		}
		

		return returnValue;
	}
	
	public boolean checkCriteriSyntax(boolean showpopup, String type){
		boolean returnValue = true;
				/*int numOpen = 0;
		int numClose = 0;
		if ((ricerca != null) && 
			(((type.equalsIgnoreCase(WinkhouseUtils.IMMOBILI) || type.equalsIgnoreCase(WinkhouseUtils.AFFITTI))
			 ? ricerca.getCriteriImmobili()
			 : ricerca.getCriteriAnagrafiche())) != null){
			
			Iterator it = (((type.equalsIgnoreCase(WinkhouseUtils.IMMOBILI) || type.equalsIgnoreCase(WinkhouseUtils.AFFITTI))
					 		? ricerca.getCriteriImmobili()
							: ricerca.getCriteriAnagrafiche())).iterator();
			String prev = "";
			while (it.hasNext()){
				ColloquiCriteriRicercaVO ccrVO = (ColloquiCriteriRicercaVO)it.next();
				if (ccrVO.getGetterMethodName().equalsIgnoreCase("(")){
					if (!prev.equalsIgnoreCase(")")){
						numOpen++;					 
					}else{
						returnValue = false;
						break;
					}					
				}
				if (ccrVO.getGetterMethodName().equalsIgnoreCase(")")){
					if (!prev.equalsIgnoreCase("(")){
						numClose++;						 
					}else{
						returnValue = false;
						break;
					}															
				}	
				prev = ccrVO.getGetterMethodName();
			}
		}
		if (returnValue){
			returnValue = (numOpen == numClose)?true:false;
			if (!returnValue){
				if (showpopup){
					MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
					mb.setText("Errore sintassi");
					mb.setMessage("Numero parentesi aperte diverse da numero parentesi chiuse");			
					mb.open();							
				}
			}else{
				boolean verifycheck = true;
				if (type.equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
					SearchEngineImmobili sei = new SearchEngineImmobili(ricerca.getCriteriImmobili());
					verifycheck = sei.verifyQuery();
				}else if (type.equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
					SearchEngineImmobiliAffitti seia = new SearchEngineImmobiliAffitti(ricerca.getCriteriImmobili());
					verifycheck = seia.verifyQuery();					
				}else{
					SearchEngineAnagrafiche sea = new SearchEngineAnagrafiche(ricerca.getCriteriAnagrafiche());
					verifycheck = sea.verifyQuery();					
				}
					
				if (verifycheck){
					if (showpopup){
						MessageBox mb = new MessageBox(this.getShell(),SWT.ICON_INFORMATION);
						mb.setText("Sintassi corretta");
						mb.setMessage("La sintassi delle condizioni inserite risulta corretta");			
						mb.open();
					}
				}else{
					if (showpopup){
						MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
						mb.setText("Errore sintassi");
						mb.setMessage("Richiesta generata non corretta, \n controllare i criteri inseriti");			
						mb.open();
					}
				}
								
			}			
		}else{
			if (showpopup){
				MessageBox mb = new MessageBox(this.getShell(),SWT.ERROR);
				mb.setText("Errore sequenza parentesi");
				mb.setMessage("Sequenza parentesi non corretta");			
				mb.open();							
			}			
		}*/
		return returnValue;
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		return super.getPreviousPage(page);
	}

	@Override
	public boolean canFinish() {
		if (getExporterVO().getPathExportFile() != null){
			return true;
		}else{
			return false;
		}

	}

	public ExporterVO getExporterVO() {
		if (exporterVO == null){
			exporterVO = new ExporterVO();
		}
		return exporterVO;
	}

	public void setExporterVO(ExporterVO exporterVO) {
		this.exporterVO = exporterVO;
	}

	public String getVersion(){
		
		return "";
	}

}
