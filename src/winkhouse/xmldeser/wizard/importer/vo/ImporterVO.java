package winkhouse.xmldeser.wizard.importer.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import winkhouse.model.AttributeModel;
import winkhouse.model.AttributeValueModel;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.vo.AbbinamentiVO;
import winkhouse.vo.AffittiVO;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.AttributeVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.DatiCatastaliVO;
import winkhouse.vo.EntityVO;
import winkhouse.vo.ImmagineVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StanzeImmobiliVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipiAppuntamentiVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.xmldeser.models.xml.AbbinamentiXMLModel;
import winkhouse.xmldeser.models.xml.AffittiXMLModel;
import winkhouse.xmldeser.models.xml.AgentiXMLModel;
import winkhouse.xmldeser.models.xml.AllegatiColloquiXMLModel;
import winkhouse.xmldeser.models.xml.AllegatiImmobiliXMLModel;
import winkhouse.xmldeser.models.xml.AnagraficheXMLModel;
import winkhouse.xmldeser.models.xml.AttributeValueXMLModel;
import winkhouse.xmldeser.models.xml.AttributeXMLModel;
import winkhouse.xmldeser.models.xml.ClasseEnergeticaXMLModel;
import winkhouse.xmldeser.models.xml.ClassiClientiXMLModel;
import winkhouse.xmldeser.models.xml.ColloquiAgentiXMLModel;
import winkhouse.xmldeser.models.xml.ColloquiXMLModel;
import winkhouse.xmldeser.models.xml.ContattiXMLModel;
import winkhouse.xmldeser.models.xml.CriteriRicercaXMLModel;
import winkhouse.xmldeser.models.xml.DatiCatastaliXMLModel;
import winkhouse.xmldeser.models.xml.EntityXMLModel;
import winkhouse.xmldeser.models.xml.ImmagineXMLModel;
import winkhouse.xmldeser.models.xml.ImmobiliXMLModel;
import winkhouse.xmldeser.models.xml.RiscaldamentiXMLModel;
import winkhouse.xmldeser.models.xml.StanzeImmobiliXMLModel;
import winkhouse.xmldeser.models.xml.StatoConservativoXMLModel;
import winkhouse.xmldeser.models.xml.TipiAppuntamentiXMLModel;
import winkhouse.xmldeser.models.xml.TipologiaContattiXMLModel;
import winkhouse.xmldeser.models.xml.TipologiaStanzeXMLModel;
import winkhouse.xmldeser.models.xml.TipologieImmobiliXMLModel;
import winkhouse.xmldeser.wizard.importer.ImporterWizard;

public class ImporterVO {

	//private ArrayList<CriteriRicercaModel> criteriRicerca = null;
	private ArrayList risultati = null;
	private ArrayList risultati_selected = null;
	private ArrayList risultati_merge = null;
	
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
	private HashMap hmEntita = null;
	private HashMap hmAttributi = null;
	private HashMap hmValoriAttributi = null;
	private ArrayList alImmobiliPropieta = null;
	
	private HashMap<Integer,AgentiXMLModel> agentiSelected = null;
	private HashMap<Integer,AnagraficheXMLModel> anagraficheSelected = null;
	private HashMap<Integer,ColloquiXMLModel> colloquiSelected = null;
	private HashMap<Integer,ImmobiliXMLModel> immobiliSelected = null;
	private HashMap<Integer,TipologiaStanzeXMLModel> tipologieStanzeSelected = null;
	private HashMap<Integer,TipologieImmobiliXMLModel> tipologieImmobiliSelected = null;
	private HashMap<Integer,TipologiaContattiXMLModel> tipologieContattiSelected = null;
	private HashMap<Integer,TipiAppuntamentiXMLModel> tipologiaAppuntamentiSelected = null;
	private HashMap<Integer,ClasseEnergeticaXMLModel> classeEnergeticheSelected = null;
	private HashMap<Integer,StatoConservativoXMLModel> statoConservativoSelected = null;
	private HashMap<Integer,RiscaldamentiXMLModel> riscaldamentiSelected = null;
	private HashMap<Integer,ClassiClientiXMLModel> classiClientiSelected = null;
	private HashMap<Integer,AffittiXMLModel> affittiSelected = null;
	private HashMap<Integer,EntityXMLModel> entitaSelected = null;
	private HashMap<Integer,AttributeXMLModel> attributiSelected = null;
	private HashMap<Integer,AttributeValueXMLModel> valoriAttributiSelected = null;
	
	private boolean flagCollegati = false;
	private String pathExportFile = null;
	private String winkCloudPathFile = null;
	private Date winkCloudPathFile_receiveDate = null;
	private boolean isPathUpdated = false;
	private boolean isZipped = false;
	
	public ImporterVO(){
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
		 alImmobiliPropieta = new ArrayList();
	}

	/*
	public boolean almostOneSelected(){
		if (getHmAbbinamenti().size() > 0 || getHmAffitti().size() > 0 || getHmAffittiAllegati().size() > 0 ||
			getHmAffittiAnagrafiche().size() > 0 || getHmAffittiRate().size() > 0 || getHmAffittiSpese().size() > 0 ||
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
	*/
	public void hashMappersSetter(ImporterWizard wizard, Object objectSelected,boolean checked){
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
	    		}
	    		
	    		wizard.getContainer().updateButtons();
	    		    									  
	    	}else{
	    		
	    		getHmColloquiAgenti().remove(((ColloquiAgentiModel_Age)objectSelected).getCodColloquioAgenti());
				
	    		wizard.getContainer().updateButtons();
	    	}
	    		
	    }
	    if(objectSelected instanceof EntityVO) {
	    	
	    	if (checked){
	    		
	    		if (!getHmEntita().containsKey(((EntityVO)objectSelected).getIdClassEntity())){
	
	    			getHmEntita().put(((EntityVO)objectSelected).getIdClassEntity(),
						  			  new EntityXMLModel((EntityVO)objectSelected));
	    		}
	    		
	    		wizard.getContainer().updateButtons();
	    		    									  
	    	}else{
	    		
	    		getHmEntita().remove(((EntityVO)objectSelected).getIdClassEntity());
				
	    		wizard.getContainer().updateButtons();
	    	}
	    		
	    }
	    if(objectSelected instanceof AttributeModel) {
	    	
	    	if (checked){
	    		
	    		if (!getHmAttributi().containsKey(((AttributeVO)objectSelected).getIdAttribute())){
	
	    			getHmAttributi().put(((AttributeModel)objectSelected).getIdAttribute(),
						  					  new AttributeXMLModel((AttributeModel)objectSelected));
	    		}
	    		
	    		wizard.getContainer().updateButtons();
	    		    									  
	    	}else{
	    		
	    		getHmAttributi().remove(((AttributeModel)objectSelected).getIdAttribute());
				
	    		wizard.getContainer().updateButtons();
	    	}
	    		
	    }
	    if(objectSelected instanceof AttributeValueModel) {
	    	
	    	if (checked){
	    		
	    		if (!getHmValoriAttributi().containsKey(((AttributeValueModel)objectSelected).getIdValue())){
	
	    			getHmValoriAttributi().put(((AttributeValueModel)objectSelected).getIdValue(),
						  					  new AttributeValueXMLModel((AttributeValueModel)objectSelected));
	    		}
	    		
	    		wizard.getContainer().updateButtons();
	    		    									  
	    	}else{
	    		
	    		getHmValoriAttributi().remove(((AttributeValueModel)objectSelected).getIdValue());
				
	    		wizard.getContainer().updateButtons();
	    	}
	    		
	    }

	}
/*
	public ArrayList<CriteriRicercaModel> getCriteriRicerca() {
		return criteriRicerca;
	}

	public void setCriteriRicerca(ArrayList<CriteriRicercaModel> criteriRicerca) {
		this.criteriRicerca = criteriRicerca;
	}
*/
	public ArrayList getRisultati() {
		
		if (risultati == null){
			
			risultati = new ArrayList();
			
			if (getHmImmobili() != null){
				risultati.addAll(getHmImmobili().values());
			}
			if (getHmAnagrafiche() != null){
				risultati.addAll(getHmAnagrafiche().values());
			}
			if (getHmContatti() != null){
				risultati.addAll(getHmContatti().values());
			}
			if (getHmColloqui() != null){
				risultati.addAll(getHmColloqui().values());
			}
			if (getHmAbbinamenti() != null){
				risultati.addAll(getHmAbbinamenti().values());
			}
			if (getHmAffitti() != null){
				risultati.addAll(getHmAffitti().values());
			}
			if (getHmAffittiAllegati() != null){
				risultati.addAll(getHmAffittiAllegati().values());
			}
			if (getHmAffittiAnagrafiche() != null){
				risultati.addAll(getHmAffittiAnagrafiche().values());
			}
			if (getHmAffittiRate() != null){
				risultati.addAll(getHmAffittiRate().values());
			}
			if (getHmAffittiSpese() != null){
				risultati.addAll(getHmAffittiSpese().values());
			}
			if (getHmAgenti() != null){
				risultati.addAll(getHmAgenti().values());
			}
			if (getHmAgentiAppuntamenti() != null){
				risultati.addAll(getHmAgentiAppuntamenti().values());
			}
			if (getHmAllegatiColloquio() != null){
				risultati.addAll(getHmAllegatiColloquio().values());
			}
			if (getHmAllegatiImmobili() != null){
				risultati.addAll(getHmAllegatiImmobili().values());
			}
			if (getHmAnagraficheAppuntamenti() != null){
				risultati.addAll(getHmAnagraficheAppuntamenti().values());
			}
			if (getHmAppuntamenti() != null){
				risultati.addAll(getHmAppuntamenti().values());
			}
			if (getHmCategorieClienti() != null){
				risultati.addAll(getHmCategorieClienti().values());
			}
			if (getHmClassiEnergetiche() != null){
				risultati.addAll(getHmClassiEnergetiche().values());
			}
			if (getHmColloquiAgenti() != null){
				risultati.addAll(getHmColloquiAgenti().values());
			}
			if (getHmColloquiAnagrafica() != null){
				risultati.addAll(getHmColloquiAnagrafica().values());
			}
			if (getHmColloquiCriteriRicerca() != null){
				risultati.addAll(getHmColloquiCriteriRicerca().values());
			}
			if (getHmCriteriRicerca() != null){
				risultati.addAll(getHmCriteriRicerca().values());
			}
			if (getHmDatiCatastali() != null){
				risultati.addAll(getHmDatiCatastali().values());
			}
			if (getHmGCalendar() != null){
				risultati.addAll(getHmGCalendar().values());
			}
			if (getHmImmagini() != null){
				risultati.addAll(getHmImmagini().values());
			}
			if (getHmRicerche() != null){
				risultati.addAll(getHmRicerche().values());
			}
			if (getHmRiscaldamento() != null){
				risultati.addAll(getHmRiscaldamento().values());
			}
			if (getHmStanzeImmobili() != null){
				risultati.addAll(getHmStanzeImmobili().values());
			}
			if (getHmStatiConservativi() != null){
				risultati.addAll(getHmStatiConservativi().values());
			}
			if (getHmTipiAppuntamento() != null){
				risultati.addAll(getHmTipiAppuntamento().values());
			}
			if (getHmTipiContatti() != null){
				risultati.addAll(getHmTipiContatti().values());
			}
			if (getHmTipiImmobili() != null){
				risultati.addAll(getHmTipiImmobili().values());
			}
			if (getHmTipiStanze() != null){
				risultati.addAll(getHmTipiStanze().values());
			}
			if (getHmEntita() != null){
				risultati.addAll(getHmEntita().values());
			}
			if (getHmAttributi() != null){
				risultati.addAll(getHmAttributi().values());
			}
			if (getHmValoriAttributi() != null){
				risultati.addAll(getHmValoriAttributi().values());
			}
			
		}
		
		return risultati;
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

	public void setHmImmobili(HashMap hmImmobili) {
		this.hmImmobili = hmImmobili;
	}

	public void setHmAnagrafiche(HashMap hmAnagrafiche) {
		this.hmAnagrafiche = hmAnagrafiche;
	}

	public void setHmColloqui(HashMap hmColloqui) {
		this.hmColloqui = hmColloqui;
	}

	public void setHmAgenti(HashMap hmAgenti) {
		this.hmAgenti = hmAgenti;
	}

	public void setHmContatti(HashMap hmContatti) {
		this.hmContatti = hmContatti;
	}

	public void setHmAffitti(HashMap hmAffitti) {
		this.hmAffitti = hmAffitti;
	}

	public void setHmAppuntamenti(HashMap hmAppuntamenti) {
		this.hmAppuntamenti = hmAppuntamenti;
	}

	public void setHmImmagini(HashMap hmImmagini) {
		this.hmImmagini = hmImmagini;
	}

	public void setHmCriteriRicerca(HashMap hmCriteriRicerca) {
		this.hmCriteriRicerca = hmCriteriRicerca;
	}

	public void setHmDatiCatastali(HashMap hmDatiCatastali) {
		this.hmDatiCatastali = hmDatiCatastali;
	}

	public void setHmAffittiAllegati(HashMap hmAffittiAllegati) {
		this.hmAffittiAllegati = hmAffittiAllegati;
	}

	public void setHmAffittiAnagrafiche(HashMap hmAffittiAnagrafiche) {
		this.hmAffittiAnagrafiche = hmAffittiAnagrafiche;
	}

	public void setHmAffittiRate(HashMap hmAffittiRate) {
		this.hmAffittiRate = hmAffittiRate;
	}

	public void setHmAffittiSpese(HashMap hmAffittiSpese) {
		this.hmAffittiSpese = hmAffittiSpese;
	}

	public void setHmAgentiAppuntamenti(HashMap hmAgentiAppuntamenti) {
		this.hmAgentiAppuntamenti = hmAgentiAppuntamenti;
	}

	public void setHmAllegatiColloquio(HashMap hmAllegatiColloquio) {
		this.hmAllegatiColloquio = hmAllegatiColloquio;
	}

	public void setHmAllegatiImmobili(HashMap hmAllegatiImmobili) {
		this.hmAllegatiImmobili = hmAllegatiImmobili;
	}

	public void setHmAnagraficheAppuntamenti(HashMap hmAnagraficheAppuntamenti) {
		this.hmAnagraficheAppuntamenti = hmAnagraficheAppuntamenti;
	}

	public void setHmColloquiAgenti(HashMap hmColloquiAgenti) {
		this.hmColloquiAgenti = hmColloquiAgenti;
	}

	public void setHmColloquiAnagrafica(HashMap hmColloquiAnagrafica) {
		this.hmColloquiAnagrafica = hmColloquiAnagrafica;
	}

	public void setHmColloquiCriteriRicerca(HashMap hmColloquiCriteriRicerca) {
		this.hmColloquiCriteriRicerca = hmColloquiCriteriRicerca;
	}

	public void setHmGCalendar(HashMap hmGCalendar) {
		this.hmGCalendar = hmGCalendar;
	}

	public void setHmStanzeImmobili(HashMap hmStanzeImmobili) {
		this.hmStanzeImmobili = hmStanzeImmobili;
	}

	public void setHmRicerche(HashMap hmRicerche) {
		this.hmRicerche = hmRicerche;
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

	public void setHmAbbinamenti(HashMap hmAbbinamenti) {
		this.hmAbbinamenti = hmAbbinamenti;
	}

	public void setHmTipiContatti(HashMap hmTipiContatti) {
		this.hmTipiContatti = hmTipiContatti;
	}
	
	public ArrayList getRisultati_merge() {
		if (risultati_merge == null){
			risultati_merge = new ArrayList();
		}
		return risultati_merge;
	}
			
	public HashMap<Integer,ImmobiliXMLModel>getImmobiliSelected(){
		
		if (this.immobiliSelected == null){
			this.immobiliSelected = new HashMap<Integer,ImmobiliXMLModel>();
			
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof ImmobiliXMLModel){
					ImmobiliXMLModel copy = new ImmobiliXMLModel((ImmobiliVO)o);
					this.immobiliSelected.put(copy.getCodImmobile(),copy);
				}
			}
		}
		return this.immobiliSelected;
	}
			
	public HashMap<Integer,AnagraficheXMLModel>getAnagraficheSelected(){
		if (this.anagraficheSelected == null){
			this.anagraficheSelected = new HashMap<Integer,AnagraficheXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();					
				if (o instanceof AnagraficheVO){
					AnagraficheXMLModel copy = new AnagraficheXMLModel((AnagraficheVO)o);
					this.anagraficheSelected.put(copy.getCodAnagrafica(),copy);
				}
			}
		}
		
		return this.anagraficheSelected;
	}

	public HashMap<Integer,ColloquiXMLModel>getColloquiSelected(){
		if (this.colloquiSelected == null){
			this.colloquiSelected = new HashMap<Integer,ColloquiXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof ColloquiXMLModel){
					ColloquiXMLModel copy = new ColloquiXMLModel((ColloquiVO)o);
					this.colloquiSelected.put(copy.getCodColloquio(),copy);
				}
			}
		}
		
		return this.colloquiSelected;
	}

	public HashMap<Integer,AgentiXMLModel>getAgentiSelected(){
		if (this.agentiSelected == null){
			this.agentiSelected = new HashMap<Integer,AgentiXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof AgentiXMLModel){
					AgentiXMLModel copy = new AgentiXMLModel((AgentiVO)o);
					this.agentiSelected.put(copy.getCodAgente(),copy);
				}
			}
		}
		
		return this.agentiSelected;
	}

	public void setRisultati_merge(ArrayList risultati_merge) {
		this.risultati_merge = risultati_merge;
	}

	public void setAgentiSelected(HashMap<Integer, AgentiXMLModel> agentiSelected) {
		this.agentiSelected = agentiSelected;
	}

	public void setAnagraficheSelected(
			HashMap<Integer, AnagraficheXMLModel> anagraficheSelected) {
		this.anagraficheSelected = anagraficheSelected;
	}

	public void setColloquiSelected(
			HashMap<Integer, ColloquiXMLModel> colloquiSelected) {
		this.colloquiSelected = colloquiSelected;
	}

	public void setImmobiliSelected(
			HashMap<Integer, ImmobiliXMLModel> immobiliSelected) {
		this.immobiliSelected = immobiliSelected;
	}
	
	public void resetSelectedHashMaps(){
		
		setAgentiSelected(null);
		setAnagraficheSelected(null);
		setColloquiSelected(null);
		setImmobiliSelected(null);
		setTipologieStanzeSelected(null);
		setStatoConservativoSelected(null);
		setClasseEnergeticheSelected(null);
		setRiscaldamentiSelected(null);
		setTipologieImmobiliSelected(null);
		setTipologieContattiSelected(null);
		setEntitaSelected(null);
		setAttributiSelected(null);
		setValoriAttributiSelected(null);

	}

	public void initSelectedHashMaps(){
		
		getAgentiSelected();
		getAnagraficheSelected();
		getColloquiSelected();
		getImmobiliSelected();
		getTipologieStanzeSelected();
		getStatoConservativoSelected();
		getClasseEnergeticheSelected();
		getRiscaldamentiSelected();
		getTipologieImmobiliSelected();
		getTipologieContattiSelected();
		getEntitaSelected();
		getAttributiSelected();
		getValoriAttributiSelected();

	}
	
	public HashMap<Integer, TipologiaStanzeXMLModel> getTipologieStanzeSelected() {
		
		if (this.tipologieStanzeSelected == null){
			this.tipologieStanzeSelected = new HashMap<Integer,TipologiaStanzeXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof TipologiaStanzeXMLModel){
					TipologiaStanzeXMLModel copy = new TipologiaStanzeXMLModel((TipologiaStanzeVO)o);
					this.tipologieStanzeSelected.put(copy.getCodTipologiaStanza(),copy);
				}
			}
		}
		
		return tipologieStanzeSelected;
	}

	public void setTipologieStanzeSelected(
			HashMap<Integer, TipologiaStanzeXMLModel> tipologieStanzeSelected) {
					
		this.tipologieStanzeSelected = tipologieStanzeSelected;
	}

	public boolean isPathUpdated() {
		return isPathUpdated;
	}

	public void setPathUpdated(boolean isPathUpdated) {
		this.isPathUpdated = isPathUpdated;
	}

	public HashMap<Integer, TipologieImmobiliXMLModel> getTipologieImmobiliSelected() {
		
		if (this.tipologieImmobiliSelected == null){
			this.tipologieImmobiliSelected = new HashMap<Integer,TipologieImmobiliXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof TipologieImmobiliXMLModel){
					TipologieImmobiliXMLModel copy = new TipologieImmobiliXMLModel((TipologieImmobiliVO)o);
					this.tipologieImmobiliSelected.put(copy.getCodTipologiaImmobile(),copy);
				}
			}
		}
					
		return tipologieImmobiliSelected;
	}
	
	public void setTipologieImmobiliSelected(
			HashMap<Integer, TipologieImmobiliXMLModel> tipologieImmobiliSelected) {
		this.tipologieImmobiliSelected = tipologieImmobiliSelected;
	}
	
	public HashMap<Integer, StatoConservativoXMLModel> getStatoConservativoSelected() {

		if (this.statoConservativoSelected == null){
			this.statoConservativoSelected = new HashMap<Integer,StatoConservativoXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof StatoConservativoXMLModel){
					StatoConservativoXMLModel copy = new StatoConservativoXMLModel((StatoConservativoVO)o);
					this.statoConservativoSelected.put(copy.getCodStatoConservativo(),copy);
				}
			}
		}			
		
		return statoConservativoSelected;
	}
	
	public void setStatoConservativoSelected(
			HashMap<Integer, StatoConservativoXMLModel> statoConservativoSelected) {
		this.statoConservativoSelected = statoConservativoSelected;
	}

	public HashMap<Integer, RiscaldamentiXMLModel> getRiscaldamentiSelected() {
		
		if (this.riscaldamentiSelected == null){
			this.riscaldamentiSelected = new HashMap<Integer,RiscaldamentiXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof RiscaldamentiXMLModel){
					RiscaldamentiXMLModel copy = new RiscaldamentiXMLModel((RiscaldamentiVO)o);
					this.riscaldamentiSelected.put(copy.getCodRiscaldamento(),copy);
				}
			}
		}						
		
		return riscaldamentiSelected;
	}

	public void setRiscaldamentiSelected(
			HashMap<Integer, RiscaldamentiXMLModel> riscaldamentiSelected) {
		this.riscaldamentiSelected = riscaldamentiSelected;
	}

	public HashMap<Integer, ClasseEnergeticaXMLModel> getClasseEnergeticheSelected() {
		
		if (this.classeEnergeticheSelected == null){
			this.classeEnergeticheSelected = new HashMap<Integer,ClasseEnergeticaXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof ClasseEnergeticaXMLModel){
					ClasseEnergeticaXMLModel copy = new ClasseEnergeticaXMLModel((ClasseEnergeticaVO)o);
					this.classeEnergeticheSelected.put(copy.getCodClasseEnergetica(),copy);
				}
			}
		}									
		
		return classeEnergeticheSelected;
	}

	public void setClasseEnergeticheSelected(
			HashMap<Integer, ClasseEnergeticaXMLModel> classeEnergeticheSelected) {
		this.classeEnergeticheSelected = classeEnergeticheSelected;
	}

	public HashMap<Integer, ClassiClientiXMLModel> getClassiClientiSelected() {
		
		if (this.classiClientiSelected == null){
			this.classiClientiSelected = new HashMap<Integer,ClassiClientiXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof ClassiClientiXMLModel){
					ClassiClientiXMLModel copy = new ClassiClientiXMLModel((ClassiClientiVO)o);
					this.classiClientiSelected.put(copy.getCodClasseCliente(),copy);
				}
			}
		}												
		
		return classiClientiSelected;
	}

	public HashMap<Integer, AffittiXMLModel> getAffittiSelected() {
		
		if (this.affittiSelected == null){
			this.affittiSelected = new HashMap<Integer,AffittiXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof AffittiXMLModel){
					AffittiXMLModel copy = new AffittiXMLModel((AffittiVO)o);
					this.affittiSelected.put(copy.getCodAffitti(),copy);
				}
			}
		}												
		
		return affittiSelected;
	}
	
	public void setClassiClientiSelected(
			HashMap<Integer, ClassiClientiXMLModel> classiClientiSelected) {
		this.classiClientiSelected = classiClientiSelected;
	}

	public HashMap<Integer, TipologiaContattiXMLModel> getTipologieContattiSelected() {
		
		if (this.tipologieContattiSelected == null){
			this.tipologieContattiSelected = new HashMap<Integer,TipologiaContattiXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof TipologiaContattiXMLModel){
					TipologiaContattiXMLModel copy = new TipologiaContattiXMLModel((TipologiaContattiVO)o);
					this.tipologieContattiSelected.put(copy.getCodTipologiaContatto(),copy);
				}
			}
		}									
		
		return tipologieContattiSelected;
	}
	
	public void setTipologieContattiSelected(
			HashMap<Integer, TipologiaContattiXMLModel> tipologieContattiSelected) {
		this.tipologieContattiSelected = tipologieContattiSelected;
	}

	public HashMap<Integer, TipiAppuntamentiXMLModel> getTipologiaAppuntamentiSelected() {
		
		if (this.tipologiaAppuntamentiSelected == null){
			this.tipologiaAppuntamentiSelected = new HashMap<Integer,TipiAppuntamentiXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof TipiAppuntamentiXMLModel){
					TipiAppuntamentiXMLModel copy = new TipiAppuntamentiXMLModel((TipiAppuntamentiVO)o);
					this.tipologiaAppuntamentiSelected.put(copy.getCodTipoAppuntamento(),copy);
				}
			}
		}									
		
		
		return tipologiaAppuntamentiSelected;
	}
	
	public HashMap<Integer, EntityXMLModel> getEntitaSelected() {
		
		if (this.entitaSelected == null){
			this.entitaSelected = new HashMap<Integer,EntityXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof EntityXMLModel){
					EntityXMLModel copy = new EntityXMLModel((EntityVO)o);
					this.entitaSelected.put(copy.getIdClassEntity(),copy);
				}
			}
		}									
		
		
		return entitaSelected;
	}

	public HashMap<Integer, AttributeXMLModel> getAttributiSelected() {
		
		if (this.attributiSelected == null){
			this.attributiSelected = new HashMap<Integer,AttributeXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof AttributeXMLModel){
					AttributeXMLModel copy = new AttributeXMLModel((AttributeModel)o);
					this.attributiSelected.put(copy.getIdAttribute(),copy);
				}
			}
		}									
		
		
		return attributiSelected;
	}

	public HashMap<Integer, AttributeValueXMLModel> getValoriAttributiSelected() {
		
		if (this.valoriAttributiSelected == null){
			this.valoriAttributiSelected = new HashMap<Integer,AttributeValueXMLModel>();
		
			Iterator it = getRisultati_selected().iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof AttributeValueXMLModel){
					AttributeValueXMLModel copy = new AttributeValueXMLModel((AttributeValueModel)o);
					this.valoriAttributiSelected.put(copy.getIdValue(),copy);
				}
			}
		}									
		
		
		return valoriAttributiSelected;
	}
	
	
	public void setTipologiaAppuntamentiSelected(
			HashMap<Integer, TipiAppuntamentiXMLModel> tipologiaAppuntamentiSelected) {
		this.tipologiaAppuntamentiSelected = tipologiaAppuntamentiSelected;
	}

	public void initializeSelected(){
		getAffittiSelected();
		getAgentiSelected();
		getAnagraficheSelected();
		getClasseEnergeticheSelected();
		getClassiClientiSelected();
		getColloquiSelected();
		getImmobiliSelected();
		getRiscaldamentiSelected();
		getStatoConservativoSelected();
		getTipologiaAppuntamentiSelected();
		getTipologieContattiSelected();
		getTipologieImmobiliSelected();
		getTipologieStanzeSelected();
		getEntitaSelected();
		getAttributiSelected();
		getValoriAttributiSelected();
	}


	public HashMap getHmEntita() {
		return hmEntita;
	}

	public void setHmEntita(HashMap hmEntita) {
		this.hmEntita = hmEntita;
	}

	public HashMap getHmAttributi() {
		return hmAttributi;
	}

	public void setHmAttributi(HashMap hmAttributi) {
		this.hmAttributi = hmAttributi;
	}

	public HashMap getHmValoriAttributi() {
		return hmValoriAttributi;
	}

	public void setHmValoriAttributi(HashMap hmValoriAttributi) {
		this.hmValoriAttributi = hmValoriAttributi;
	}

	public void setEntitaSelected(HashMap<Integer, EntityXMLModel> entitaSelected) {
		this.entitaSelected = entitaSelected;
	}

	public void setAttributiSelected(
			HashMap<Integer, AttributeXMLModel> attributiSelected) {
		this.attributiSelected = attributiSelected;
	}

	public void setValoriAttributiSelected(
			HashMap<Integer, AttributeValueXMLModel> valoriAttributiSelected) {
		this.valoriAttributiSelected = valoriAttributiSelected;
	}

	
	public ArrayList getAlImmobiliPropieta() {
		return alImmobiliPropieta;
	}

	
	public void setAlImmobiliPropieta(ArrayList alImmobiliPropieta) {
		this.alImmobiliPropieta = alImmobiliPropieta;
	}

	@Override
	public String toString() {
		return "Immobili trovati : " + getHmImmobili().size();
	}

	public String getWinkCloudPathFile() {
		return winkCloudPathFile;
	}

	public void setWinkCloudPathFile(String winkCloudPathFile) {
		this.winkCloudPathFile = winkCloudPathFile;
	}

	public Date getWinkCloudPathFile_receiveDate() {
		return winkCloudPathFile_receiveDate;
	}

	public void setWinkCloudPathFile_receiveDate(Date winkCloudPathFile_receiveDate) {
		this.winkCloudPathFile_receiveDate = winkCloudPathFile_receiveDate;
	}

	public boolean isZipped() {
		return isZipped;
	}

	public void setZipped(boolean isZipped) {
		this.isZipped = isZipped;
	}
	

}
