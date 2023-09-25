package winkhouse.xmldeser.utils;

import org.eclipse.swt.graphics.Image;

import winkhouse.xmldeser.models.xml.AbbinamentiXMLModel;
import winkhouse.xmldeser.models.xml.AffittiAllegatiXMLModel;
import winkhouse.xmldeser.models.xml.AffittiAnagraficheXMLModel;
import winkhouse.xmldeser.models.xml.AffittiRateXMLModel;
import winkhouse.xmldeser.models.xml.AffittiSpeseXMLModel;
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
import winkhouse.xmldeser.models.xml.ColloquiAnagraficheXMLModel;
import winkhouse.xmldeser.models.xml.ColloquiCriteriRicercaXMLModel;
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
import winkhouse.xmldeser.models.xml.TipologiaContattiXMLModel;
import winkhouse.xmldeser.models.xml.TipologiaStanzeXMLModel;
import winkhouse.xmldeser.models.xml.TipologieImmobiliXMLModel;
import winkhouse.xmldeser.wizard.importer.vo.ImporterVO;

public class ElementsDescriptors {
	
	private ImporterVO importerVO = null;
	
	public ElementsDescriptors(ImporterVO importerVO) {
		this.importerVO = importerVO; 
	}

	public String getCodObject(Object element){
		
		String returnValue = "";
		
		if (element instanceof ImmobiliXMLModel){
			return ((ImmobiliXMLModel)element).getRif();
		}
		if (element instanceof AnagraficheXMLModel){
			return ((AnagraficheXMLModel)element).getCodAnagrafica().toString();
		}
		if (element instanceof AffittiXMLModel){
			return ((AffittiXMLModel)element).getCodAffitti().toString();
		}
		if (element instanceof AgentiXMLModel){
			return ((AgentiXMLModel)element).getCodAgente().toString();
		}
		if (element instanceof ColloquiXMLModel){
			return ((ColloquiXMLModel)element).getCodColloquio().toString();
		}
		if (element instanceof ClasseEnergeticaXMLModel){
			return ((ClasseEnergeticaXMLModel)element).getCodClasseEnergetica().toString();
		}
		if (element instanceof StatoConservativoXMLModel){
			return ((StatoConservativoXMLModel)element).getCodStatoConservativo().toString();
		}
		if (element instanceof AllegatiColloquiXMLModel){
			return ((AllegatiColloquiXMLModel)element).getCodAllegatiColloquio().toString();
		}  
		if (element instanceof AllegatiImmobiliXMLModel){
			return ((AllegatiImmobiliXMLModel)element).getCodAllegatiImmobili().toString();
		}
		if (element instanceof AffittiAllegatiXMLModel){
			return ((AffittiAllegatiXMLModel)element).getCodAffittiAllegati().toString();
		}
		if (element instanceof DatiCatastaliXMLModel){
			return ((DatiCatastaliXMLModel)element).getCodDatiCatastali().toString();
		}
		if (element instanceof StanzeImmobiliXMLModel){
			return ((StanzeImmobiliXMLModel)element).getCodStanzeImmobili().toString();
		}
		if (element instanceof TipologieImmobiliXMLModel){
			return ((TipologieImmobiliXMLModel)element).getCodTipologiaImmobile().toString();
		}
		if (element instanceof ImmagineXMLModel){
			return ((ImmagineXMLModel)element).getCodImmagine().toString();
		}
		if (element instanceof ClassiClientiXMLModel){
			return ((ClassiClientiXMLModel)element).getCodClasseCliente().toString();
		}
		if (element instanceof ContattiXMLModel){
			return ((ContattiXMLModel)element).getCodContatto().toString();
		}
		if (element instanceof AbbinamentiXMLModel){
			return ((AbbinamentiXMLModel)element).getCodAbbinamento().toString();
		}
		if (element instanceof ColloquiCriteriRicercaXMLModel){
			return ((ColloquiCriteriRicercaXMLModel)element).getCodCriterioRicerca().toString();
		}
		if (element instanceof CriteriRicercaXMLModel){
			return ((CriteriRicercaXMLModel)element).getCodCriterioRicerca().toString();
		}
		if (element instanceof TipologiaStanzeXMLModel){
			return ((TipologiaStanzeXMLModel)element).getCodTipologiaStanza().toString();
		}
		if (element instanceof TipologiaContattiXMLModel){
			return ((TipologiaContattiXMLModel)element).getCodTipologiaContatto().toString();
		}
		if (element instanceof ColloquiAgentiXMLModel){
			return ((ColloquiAgentiXMLModel)element).getCodColloquioAgenti().toString();
		}
		if (element instanceof RiscaldamentiXMLModel){
			return ((RiscaldamentiXMLModel)element).getCodRiscaldamento().toString();
		}
		if (element instanceof AffittiAnagraficheXMLModel){
			return ((AffittiAnagraficheXMLModel)element).getCodAffittiAnagrafiche().toString();
		}
		if (element instanceof AffittiRateXMLModel){
			return ((AffittiRateXMLModel)element).getCodAffittiRate().toString();
		}
		if (element instanceof AffittiSpeseXMLModel){
			return ((AffittiSpeseXMLModel)element).getCodAffittiSpese().toString();
		}
		if (element instanceof ColloquiAnagraficheXMLModel){
			return ((ColloquiAnagraficheXMLModel)element).getCodColloquioAnagrafiche().toString();
		}
		if (element instanceof EntityXMLModel){
			return ((EntityXMLModel)element).getIdClassEntity().toString();
		}
		if (element instanceof AttributeXMLModel){
			return ((AttributeXMLModel)element).getIdAttribute().toString();
		}
		if (element instanceof AttributeValueXMLModel){
			return ((AttributeValueXMLModel)element).getIdValue().toString();
		}
		
		
		return returnValue;
	}

	public String getDesObject(Object element){
		
		String returnValue = "";
		
		if (element instanceof ImmobiliXMLModel){
			return ((ImmobiliXMLModel)element).toString();
		}
		if (element instanceof AnagraficheXMLModel){
			return ((AnagraficheXMLModel)element).toString();
		}
		if (element instanceof AffittiXMLModel){
			return ((AffittiXMLModel)element).toString();
		}
		if (element instanceof AgentiXMLModel){
			return ((AgentiXMLModel)element).toString();
		}
		if (element instanceof ColloquiXMLModel){
			return ((ColloquiXMLModel)element).toString();
		}
		if (element instanceof ClasseEnergeticaXMLModel){
			return ((ClasseEnergeticaXMLModel)element).getDescrizione();
		}
		if (element instanceof StatoConservativoXMLModel){
			return ((StatoConservativoXMLModel)element).getDescrizione();
		}
		if (element instanceof AllegatiColloquiXMLModel){
			return "codice colloquio : " + ((AllegatiColloquiXMLModel)element).getCodColloquio() + 
					" nome file : " + ((AllegatiColloquiXMLModel)element).getNome();
		} 
		if (element instanceof AllegatiImmobiliXMLModel){
			return "codice immobile : " + ((AllegatiImmobiliXMLModel)element).getCodImmobile() + 
					" nome file : " + ((AllegatiImmobiliXMLModel)element).getNome();
			
		}
		if (element instanceof AffittiAllegatiXMLModel){
			return "codice affitto : " + ((AffittiAllegatiXMLModel)element).getCodAffitto() + 
					" nome file : " + ((AffittiAllegatiXMLModel)element).getNome();
		}
		if (element instanceof DatiCatastaliXMLModel){
			return ((DatiCatastaliXMLModel)element).toString();
		}
		if (element instanceof StanzeImmobiliXMLModel){
			return "codice immobile : " + ((StanzeImmobiliXMLModel)element).getCodImmobile().toString() + 
				   " -> " + ((StanzeImmobiliXMLModel)element).getDescrizioneTipologia();
		}
		if (element instanceof TipologieImmobiliXMLModel){
			return ((TipologieImmobiliXMLModel)element).getDescrizione();
		}
		if (element instanceof ImmagineXMLModel){
			return ((ImmagineXMLModel)element).getPathImmagine();
		}
		if (element instanceof ClassiClientiXMLModel){
			return ((ClassiClientiXMLModel)element).getDescrizione();
		}
		if (element instanceof ContattiXMLModel){
			return ((ContattiXMLModel)element).getContatto();
		}
		if (element instanceof AbbinamentiXMLModel){
			ImmobiliXMLModel imXmlModel = (ImmobiliXMLModel)importerVO.getHmImmobili().get(((AbbinamentiXMLModel)element).getCodImmobile());
			AnagraficheXMLModel aXmlModel = (AnagraficheXMLModel)importerVO.getHmAnagrafiche().get(((AbbinamentiXMLModel)element).getCodImmobile());
			String des_abbimanento = "";
			if (imXmlModel != null){
				des_abbimanento += "rif immobile : " + imXmlModel.getRif() + " - indirizzo : " + imXmlModel.getIndirizzo();
			}
			if (aXmlModel != null){
				des_abbimanento += "codice anagrafica : " + aXmlModel.getCodAnagrafica().toString() + 
								   " cognome, nome : " + aXmlModel.getCognome() + ", " + aXmlModel.getNome();
			}
			
			return des_abbimanento;
		}
		if (element instanceof ColloquiCriteriRicercaXMLModel){
			return ((ColloquiCriteriRicercaXMLModel)element).toString();
		}
		if (element instanceof CriteriRicercaXMLModel){
			return ((CriteriRicercaXMLModel)element).toString();
		}
		if (element instanceof TipologiaStanzeXMLModel){
			return ((TipologiaStanzeXMLModel)element).getDescrizione();
		}
		if (element instanceof TipologiaContattiXMLModel){
			return ((TipologiaContattiXMLModel)element).getDescrizione();
		}
		if (element instanceof ColloquiAgentiXMLModel){
			return "codice agente : " + ((ColloquiAgentiXMLModel)element).getCodAgente() + 
				   " codice colloquio : " + ((ColloquiAgentiXMLModel)element).getCodColloquio() +
				   " commento : " + ((ColloquiAgentiXMLModel)element).getCommento();
		}
		if (element instanceof RiscaldamentiXMLModel){
			return ((RiscaldamentiXMLModel)element).getDescrizione();
		}
		if (element instanceof AffittiAnagraficheXMLModel){
			return "codice affitto : " + ((AffittiAnagraficheXMLModel)element).getCodAffitto() + 
					" codice anagrafica : " + ((AffittiAnagraficheXMLModel)element).getCodAnagrafica();
		}
		if (element instanceof AffittiRateXMLModel){
			return ((AffittiRateXMLModel)element).toString();
		}
		if (element instanceof AffittiSpeseXMLModel){
			return ((AffittiSpeseXMLModel)element).toString();
		}
		if (element instanceof ColloquiAnagraficheXMLModel){
			return ((ColloquiAnagraficheXMLModel)element).toString();
		}
		if (element instanceof EntityXMLModel){
			return ((EntityXMLModel)element).getDescription();
		}
		if (element instanceof AttributeXMLModel){
			return ((AttributeXMLModel)element).getAttributeName();
		}
		if (element instanceof AttributeValueXMLModel){
			return ((AttributeValueXMLModel)element).getFieldValue();
		}

		
		return returnValue;
	}

	public Image getItemImage(Object element){
		
		Image return_value = null;
		
		if (element instanceof ImmobiliXMLModel){
			return ImageCache.getInstance().immobile;
		}
		if (element instanceof AnagraficheXMLModel){
			return ImageCache.getInstance().anagrafica;
		}
		if (element instanceof AffittiXMLModel){
			return ImageCache.getInstance().affitto;
		}
		if (element instanceof AgentiXMLModel){
			return ImageCache.getInstance().agente;
		}
		if (element instanceof ColloquiXMLModel){
			return ImageCache.getInstance().colloqui;
		}
		if (element instanceof ClasseEnergeticaXMLModel){
			return ImageCache.getInstance().classe_energetica;
		}
		if (element instanceof StatoConservativoXMLModel){
			return ImageCache.getInstance().statoconservativo;
		}
		if ((element instanceof AllegatiColloquiXMLModel) || 
			(element instanceof AllegatiImmobiliXMLModel) ||
			(element instanceof AffittiAllegatiXMLModel)){
			return ImageCache.getInstance().allegati;
		}
		if (element instanceof DatiCatastaliXMLModel){
			return ImageCache.getInstance().daticatastali;
		}
		if (element instanceof StanzeImmobiliXMLModel){
			return ImageCache.getInstance().stanza;
		}
		if (element instanceof TipologieImmobiliXMLModel){
			return ImageCache.getInstance().tipologia_immobile;
		}
		if (element instanceof ImmagineXMLModel){
			return ImageCache.getInstance().immagine;
		}
		if (element instanceof ClassiClientiXMLModel){
			return ImageCache.getInstance().classe_cliente;
		}
		if (element instanceof ContattiXMLModel){
			return ImageCache.getInstance().recapiti;
		}
		if (element instanceof AbbinamentiXMLModel){
			return ImageCache.getInstance().abbinamenti;
		}
		if ((element instanceof ColloquiCriteriRicercaXMLModel) ||
			(element instanceof CriteriRicercaXMLModel)){
			return ImageCache.getInstance().criteriricerca;
		}
		if (element instanceof TipologiaStanzeXMLModel){
			return ImageCache.getInstance().tipo_stanza;
		}
		if (element instanceof TipologiaContattiXMLModel){
			return ImageCache.getInstance().tipo_recapiti;
		}
		if (element instanceof ColloquiAgentiXMLModel){
			return ImageCache.getInstance().colloquiagenti;
		}
		if (element instanceof RiscaldamentiXMLModel){
			return ImageCache.getInstance().riscaldamento;
		}
		if (element instanceof AffittiAnagraficheXMLModel){
			return ImageCache.getInstance().affittianagrafiche;
		}					
		if (element instanceof AffittiRateXMLModel){
			return ImageCache.getInstance().affittirate;
		}
		if (element instanceof AffittiSpeseXMLModel){
			return ImageCache.getInstance().affittispese;
		}
		if (element instanceof ColloquiAnagraficheXMLModel){
			return ImageCache.getInstance().colloquioanagrafica;
		}
		if (element instanceof EntityXMLModel){
			return ImageCache.getInstance().entity;
		}
		if (element instanceof AttributeXMLModel){
			return ImageCache.getInstance().attribute;
		}
		if (element instanceof AttributeValueXMLModel){
			return ImageCache.getInstance().attributeValue;
		}
		
		return return_value;
	}

	public String getItemTooltip(Object element){
		
		String return_value = null;
		
		if (element instanceof ImmobiliXMLModel){
			return "immobile";
		}
		if (element instanceof AnagraficheXMLModel){
			return "anagrafica";
		}
		if (element instanceof AffittiXMLModel){
			return "affitto";
		}
		if (element instanceof AgentiXMLModel){
			return "agente";
		}
		if (element instanceof ColloquiXMLModel){
			return "colloqui";
		}
		if (element instanceof ClasseEnergeticaXMLModel){
			return "classe energetica";
		}
		if (element instanceof StatoConservativoXMLModel){
			return "stato conservativo";
		}
		if ((element instanceof AllegatiColloquiXMLModel) || 
			(element instanceof AllegatiImmobiliXMLModel) ||
			(element instanceof AffittiAllegatiXMLModel)){
			return "allegati";
		}
		if (element instanceof DatiCatastaliXMLModel){
			return "dati catastali";
		}
		if (element instanceof StanzeImmobiliXMLModel){
			return "stanza";
		}
		if (element instanceof TipologieImmobiliXMLModel){
			return "tipologia immobile";
		}
		if (element instanceof ImmagineXMLModel){
			return "immagine";
		}
		if (element instanceof ClassiClientiXMLModel){
			return "classe cliente";
		}
		if (element instanceof ContattiXMLModel){
			return "recapiti";
		}
		if (element instanceof AbbinamentiXMLModel){
			return "abbinamenti";
		}
		if ((element instanceof ColloquiCriteriRicercaXMLModel) ||
			(element instanceof CriteriRicercaXMLModel)){
			return "criteri ricerca";
		}
		if (element instanceof TipologiaStanzeXMLModel){
			return "tipologia stanza";
		}
		if (element instanceof TipologiaContattiXMLModel){
			return "tipo recapiti";
		}
		if (element instanceof ColloquiAgentiXMLModel){
			return "colloqui agenti";
		}
		if (element instanceof RiscaldamentiXMLModel){
			return "riscaldamento";
		}
		if (element instanceof EntityXMLModel){
			return "entit�";
		}
		if (element instanceof AttributeXMLModel){
			return "campo personale";
		}
		if (element instanceof AttributeValueXMLModel){
			return "valore campo personale";
		}					
		
		return return_value;
	}

}
