package winkhouse.xmldeser.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import winkhouse.export.helpers.AffittiHelper;
import winkhouse.export.helpers.AnagraficheHelper;
import winkhouse.export.helpers.CampiPersonaliHelper;
import winkhouse.export.helpers.ColloquiHelper;
import winkhouse.export.helpers.ImmobiliHelper;
import winkhouse.export.helpers.UtilsHelper;
import winkhouse.model.AffittiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AttributeModel;
import winkhouse.model.AttributeValueModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Immobili;
import winkhouse.vo.AbbinamentiVO;
import winkhouse.vo.AffittiAllegatiVO;
import winkhouse.vo.AffittiAnagraficheVO;
import winkhouse.vo.AffittiRateVO;
import winkhouse.vo.AffittiSpeseVO;
import winkhouse.vo.AffittiVO;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ColloquiAgentiVO;
import winkhouse.vo.ColloquiAnagraficheVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.DatiCatastaliVO;
import winkhouse.vo.ImmobiliPropietariVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StanzeImmobiliVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.vo.TipologieImmobiliVO;
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
import winkhouse.xmldeser.models.xml.AppuntamentiXMLModel;
import winkhouse.xmldeser.models.xml.AttributeValueXMLModel;
import winkhouse.xmldeser.models.xml.AttributeXMLModel;
import winkhouse.xmldeser.models.xml.ClasseEnergeticaXMLModel;
import winkhouse.xmldeser.models.xml.ClassiClientiXMLModel;
import winkhouse.xmldeser.models.xml.ColloquiAgentiXMLModel;
import winkhouse.xmldeser.models.xml.ColloquiAnagraficheXMLModel;
import winkhouse.xmldeser.models.xml.ColloquiCriteriRicercaXMLModel;
import winkhouse.xmldeser.models.xml.ColloquiXMLModel;
import winkhouse.xmldeser.models.xml.ContattiXMLModel;
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

public class DataWriter implements IRunnableWithProgress {

	private ImporterVO importer = null;
	private HashMap mergedItems = null;
	private ImmobiliHelper immobili_helper = null;
	private AnagraficheHelper anagrafiche_helper = null;
	private AffittiHelper affitti_helper = null;
	private ColloquiHelper colloqui_helper = null;
	private CampiPersonaliHelper campiPersonali_helper = null;
	
	//private HashMap itemsSelected = null;
	
	public DataWriter(ImporterVO importer){
		this.importer = importer;
		this.mergedItems = new RisultatiList2MapAdapter(this.importer.getRisultati_merge()).adapt();
		this.immobili_helper = new ImmobiliHelper();
		this.anagrafiche_helper = new AnagraficheHelper();
		this.affitti_helper = new AffittiHelper();
		this.colloqui_helper = new ColloquiHelper();
		this.campiPersonali_helper = new CampiPersonaliHelper();
	}
	
	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
													 InterruptedException {

		importer.setRisultati_merge(new ArrayList());
		monitor.beginTask("Scrittura dati, " + String.valueOf((importer.getRisultati_selected().size() + 1)) + " elementi :", 
						  importer.getRisultati_selected().size() + 1);
		
		//itemsSelected = new RisultatiList2MapAdapter(this.importer.getRisultati_selected()).adapt();
		//ArrayList alselected = (ArrayList) importer.getRisultati_selected().clone();
		importer.initializeSelected();
		Collections.sort(importer.getRisultati_selected(), new ObjectTypeComparator());
		
		Iterator it = importer.getRisultati_selected().iterator();
		
		while (it.hasNext()){
			
			Object o = it.next();
			if (o instanceof ClassiClientiXMLModel){
				monitor.setTaskName("scrittura classi cliente : " + ((ClassiClientiXMLModel)o).toString());
				writeClassiClienti((ClassiClientiXMLModel)o);
			}			
			if (o instanceof ImmobiliXMLModel){
				monitor.setTaskName("scrittura immobile : " + ((ImmobiliXMLModel)o).toString());
				writeImmobile((ImmobiliXMLModel)o);
				
			}
			if (o instanceof DatiCatastaliXMLModel){
				monitor.setTaskName("scrittura dati catastali : " + ((DatiCatastaliXMLModel)o).toString());
				writeDatiCatastali((DatiCatastaliXMLModel)o);				
			}			
			if (o instanceof AnagraficheXMLModel){
				monitor.setTaskName("scrittura anagrafica : " + ((AnagraficheXMLModel)o).toString());
				writeAnagrafica((AnagraficheXMLModel)o);				
			}			
			
			if (o instanceof AffittiXMLModel){
				monitor.setTaskName("scrittura affitto : " + ((AffittiXMLModel)o).toString());
				writeAffitto((AffittiXMLModel)o);				
			}
			if (o instanceof TipologieImmobiliXMLModel){
				monitor.setTaskName("scrittura tipologia immobile : " + ((TipologieImmobiliXMLModel)o).toString());
				writeTipologiaImmobile((TipologieImmobiliXMLModel)o);
			}
			if (o instanceof RiscaldamentiXMLModel){
				monitor.setTaskName("scrittura riscaldamento : " + ((RiscaldamentiXMLModel)o).toString());			
				writeRiscaldamento((RiscaldamentiXMLModel)o);
			}
			if (o instanceof StatoConservativoXMLModel){
				monitor.setTaskName("scrittura stato conservativo : " + ((StatoConservativoXMLModel)o).toString());			
				writeStatoConservativo((StatoConservativoXMLModel)o);				
			}
			if (o instanceof ClasseEnergeticaXMLModel){
				monitor.setTaskName("scrittura classe energetica : " + ((ClasseEnergeticaXMLModel)o).toString());			
				writeClasseEnergetica((ClasseEnergeticaXMLModel)o);					
			}
			if (o instanceof TipologiaContattiXMLModel){
				monitor.setTaskName("scrittura tipologia contatti : " + ((TipologiaContattiXMLModel)o).toString());			
				writeTipologiaContatti((TipologiaContattiXMLModel)o);									
			}
			if (o instanceof ContattiXMLModel){
				monitor.setTaskName("scrittura contatto : " + ((ContattiXMLModel)o).toString());			
				writeContatti((ContattiXMLModel)o);																	
			}
			if (o instanceof AgentiXMLModel){
				monitor.setTaskName("scrittura tipologia agente : " + ((AgentiXMLModel)o).toString());			
				writeAgenti((AgentiXMLModel)o);													
			}
			if (o instanceof AbbinamentiXMLModel){
				monitor.setTaskName("scrittura abbinamento : " + ((AbbinamentiXMLModel)o).toString());			
				writeAbbinamenti((AbbinamentiXMLModel)o);													
			}
			
			/*
			if (o instanceof AppuntamentiXMLModel){
				monitor.setTaskName("scrittura appuntamento : " + ((AppuntamentiXMLModel)o).toString());			
				writeAppuntamenti((AppuntamentiXMLModel)o);																	
			}
			*/
			if (o instanceof ColloquiXMLModel){
				monitor.setTaskName("scrittura colloquio : " + ((ColloquiXMLModel)o).toString());			
				writeColloqui((ColloquiXMLModel)o);																	
			}
			if (o instanceof TipologiaStanzeXMLModel){
				monitor.setTaskName("scrittura tipologia stanze : " + ((TipologiaStanzeXMLModel)o).toString());			
				writeTipologiaStanze((TipologiaStanzeXMLModel)o);													
			}
			if (o instanceof ColloquiAgentiXMLModel){
				monitor.setTaskName("scrittura colloquio agente : " + ((ColloquiAgentiXMLModel)o).toString());			
				writeColloquiAgenti((ColloquiAgentiXMLModel)o);															
			}
			if (o instanceof ColloquiAnagraficheXMLModel){
				monitor.setTaskName("scrittura colloquio anagrafica : " + ((ColloquiAnagraficheXMLModel)o).toString());			
				writeColloquiAnagrafiche((ColloquiAnagraficheXMLModel)o);															
			}
			if (o instanceof ColloquiCriteriRicercaXMLModel){
				monitor.setTaskName("scrittura colloquio criterio ricerca : " + ((ColloquiCriteriRicercaXMLModel)o).toString());			
				writeColloquiCriteriRicerca((ColloquiCriteriRicercaXMLModel)o);															
			}
			if (o instanceof AllegatiColloquiXMLModel){
				monitor.setTaskName("scrittura allegato colloquio : " + ((AllegatiColloquiXMLModel)o).toString());			
				writeAllegatiColloquio((AllegatiColloquiXMLModel)o);															
			}												
			if (o instanceof ImmagineXMLModel){
				monitor.setTaskName("scrittura immagine : " + ((ImmagineXMLModel)o).toString());			
				writeImmagine((ImmagineXMLModel)o);																	
			}
			if (o instanceof StanzeImmobiliXMLModel){
				monitor.setTaskName("scrittura stanza immobile : " + ((StanzeImmobiliXMLModel)o).toString());			
				writeStanzeImmobili((StanzeImmobiliXMLModel)o);																	
			}
			if (o instanceof AllegatiImmobiliXMLModel){
				monitor.setTaskName("scrittura allegato immobile : " + ((AllegatiImmobiliXMLModel)o).toString());			
				writeAllegatiImmobile((AllegatiImmobiliXMLModel)o);																	
			}
			if (o instanceof AffittiAllegatiXMLModel){
				monitor.setTaskName("scrittura allegato affitto : " + ((AffittiAllegatiXMLModel)o).toString());			
				writeAllegatiAffitti((AffittiAllegatiXMLModel)o);																	
			}
			if (o instanceof AffittiAnagraficheXMLModel){
				monitor.setTaskName("scrittura anagrafica affitto : " + ((AffittiAnagraficheXMLModel)o).toString());			
				writeAffittiAnagrafiche((AffittiAnagraficheXMLModel)o);																	
			}
			if (o instanceof AffittiRateXMLModel){
				monitor.setTaskName("scrittura rata affitto : " + ((AffittiRateXMLModel)o).toString());			
				writeAffittiRate((AffittiRateXMLModel)o);																	
			}
			if (o instanceof AffittiSpeseXMLModel){
				monitor.setTaskName("scrittura spesa affitto : " + ((AffittiSpeseXMLModel)o).toString());			
				writeAffittiSpese((AffittiSpeseXMLModel)o);																	
			}
			if (o instanceof EntityXMLModel){
				monitor.setTaskName("scrittura entit� : " + ((EntityXMLModel)o).getDescription());			
				writeEntity((EntityXMLModel)o);																	
			}
			if (o instanceof AttributeXMLModel){
				monitor.setTaskName("scrittura attributo : " + ((AttributeXMLModel)o).getAttributeName());			
				writeAttribute((AttributeXMLModel)o);																	
			}
			if (o instanceof AttributeValueXMLModel){
				monitor.setTaskName("scrittura valore attributo : " + ((AttributeValueXMLModel)o).getFieldValue());			
				writeAttributeValue((AttributeValueXMLModel)o);																	
			}
			
			monitor.worked(1);
			
		}
		monitor.setTaskName("scrittura associazioni immobili anagrafiche");
		writeImmobiliPropieta();
		monitor.worked(1);
		
	}
	
	protected boolean isInMergeList(Object obj){
		
		if(this.mergedItems.get(obj.getClass().getName()) == null){
			return false;
		}else{
			
			Object o = this.mergedItems.get(obj.getClass().getName());
			
			if (o instanceof ArrayList){
				
				Iterator it = ((ArrayList)o).iterator();
				while (it.hasNext()) {
					
					ObjectTypeCompare otc = (ObjectTypeCompare) it.next();
					if (otc.getUniqueKey() == ((ObjectTypeCompare)obj).getUniqueKey()){
						return true;
					}
					
				}
				return false;
			}else{
				if (((ObjectTypeCompare)o).getUniqueKey() == ((ObjectTypeCompare)obj).getUniqueKey()){
					return true;
				}
				return false;
			}
		}
		
	}
	
	protected void writeAbbinamenti(AbbinamentiXMLModel abbinamento){
		
		AnagraficheXMLModel anagrafica = importer.getAnagraficheSelected().get(abbinamento.getCodAnagrafica());
		ImmobiliXMLModel immobile = importer.getImmobiliSelected().get(abbinamento.getCodImmobile());
		
		if ((anagrafica != null) && (immobile != null)){
			
			ArrayList<Anagrafiche> al_anagrafiche = anagrafiche_helper.getAnagraficheExist(anagrafica);
			ArrayList<Immobili> al_immobili = immobili_helper.getImmobiliExist(immobile);
			
			if ((al_anagrafiche.size() > 0) && (al_immobili.size() > 0)){
				
				abbinamento.setCodAnagrafica(al_anagrafiche.get(0).getCodAnagrafica());
				abbinamento.setCodImmobile(al_immobili.get(0).getCodImmobile());
				
				if (!this.isInMergeList(abbinamento)){
					
					abbinamento.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
					abbinamento.setCodAbbinamento(0);
					
				}else{
					
					abbinamento.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
					AbbinamentiVO aVO = immobili_helper.getAbbinamenti(abbinamento.getCodImmobile(), abbinamento.getCodAnagrafica());
					abbinamento.setCodAbbinamento(aVO.getCodAbbinamento());
					
				}
				
				try {
					immobili_helper.saveUpdateAbbinamenti(abbinamento);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	protected void writeImmobile(ImmobiliXMLModel immobile){
		
		Object tipologia = importer.getTipologieImmobiliSelected().get(immobile.getCodTipologia());
		if (tipologia == null){
		}else{
			ArrayList<TipologieImmobiliVO> al = UtilsHelper.getInstance().findTipologieImmobiliByDescription((TipologieImmobiliVO)tipologia);
			if (al.size() > 0){
				immobile.setCodTipologia(((TipologieImmobiliVO)al.get(0)).getCodTipologiaImmobile());
			}
		}
				
		Object agenteinseritore = importer.getAgentiSelected().get(immobile.getCodAgenteInseritore());
		if (agenteinseritore == null){
			immobile.setCodAgenteInseritore(null);
		}else{
			ArrayList<AgentiVO> al_agenti = UtilsHelper.getInstance().findAgentiByDescription((AgentiVO)agenteinseritore);
			if (al_agenti.size() > 0){
				
			}
		}
						
		Object anagrafica = importer.getAnagraficheSelected().get(immobile.getCodAnagrafica());
		if (anagrafica == null){
			immobile.setCodAnagrafica(null);
		}else{
			ArrayList<Anagrafiche> al_anagrafica = anagrafiche_helper.getAnagraficheExist((AnagraficheXMLModel)anagrafica);
			if (al_anagrafica.size() > 0){
				immobile.setCodAnagrafica((al_anagrafica.get(0)).getCodAnagrafica());
			}
		}
								
		Object classeenergetica = importer.getClasseEnergeticheSelected().get(immobile.getCodClasseEnergetica());
		if (classeenergetica == null){
			immobile.setCodClasseEnergetica(null);
		}else{
			ArrayList<ClasseEnergeticaVO> al_classe = UtilsHelper.getInstance().findClasseEnergeticaByDescription((ClasseEnergeticaVO)classeenergetica);
			if (al_classe.size() > 0){
				immobile.setCodClasseEnergetica(((ClasseEnergeticaVO)al_classe.get(0)).getCodClasseEnergetica());
			}
		}
										
		Object riscaldamento = importer.getRiscaldamentiSelected().get(immobile.getCodRiscaldamento());
		if (riscaldamento == null){
			immobile.setCodRiscaldamento(null);
		}else{
			ArrayList<RiscaldamentiVO> al_riscaldamento = UtilsHelper.getInstance().findRiscaldamentiByDescription((RiscaldamentiVO)riscaldamento);
			if (al_riscaldamento.size() > 0){
				immobile.setCodRiscaldamento(((RiscaldamentiVO)al_riscaldamento.get(0)).getCodRiscaldamento());
			}
		}
												
		Object statoconservativo = importer.getStatoConservativoSelected().get(immobile.getCodStato());
		if (statoconservativo == null){
			immobile.setCodStato(null);
		}else{
			ArrayList<StatoConservativoVO> al_stato = UtilsHelper.getInstance().findStatoConservativoByDescription((StatoConservativoVO)statoconservativo);
			if (al_stato.size() > 0){
				immobile.setCodStato(((StatoConservativoVO)al_stato.get(0)).getCodStatoConservativo());
			}
		}
				
		if (!this.isInMergeList(immobile)){			
			immobile.setCodImmobile(null);
			immobile.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
		}else{
			immobile.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
			ImmobiliModel im = immobili_helper.getImmobileByRif(immobile.getRif());
			immobile.setCodImmobile(im.getCodImmobile());
		}
		
		try {
			immobili_helper.saveUpdateImmobile(immobile);
		} catch (SQLException e) {
			e.printStackTrace();
		}
													
	}

	protected void writeDatiCatastali(DatiCatastaliXMLModel datiCatastali){
	
		ImmobiliXMLModel im = importer.getImmobiliSelected().get(datiCatastali.getCodImmobile());
		if (im != null){
			ImmobiliModel imodel = immobili_helper.getImmobileByRif(im.getRif());
			if (imodel != null){
				datiCatastali.setCodImmobile(imodel.getCodImmobile());
				if (!this.isInMergeList(datiCatastali)){
					datiCatastali.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
					datiCatastali.setCodDatiCatastali(null);					
				}else{
					datiCatastali.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
					DatiCatastaliVO dcVO = immobili_helper.getDatiCatastli(datiCatastali.getCodImmobile(),
																			datiCatastali.getFoglio(),
																			datiCatastali.getParticella(),
																			datiCatastali.getSubalterno(),
																			datiCatastali.getCategoria(),
																			datiCatastali.getRendita(), 
																			datiCatastali.getRedditoDomenicale(),
																			datiCatastali.getRedditoAgricolo(),
																			datiCatastali.getDimensione());
					if (dcVO != null){
						datiCatastali.setCodDatiCatastali(dcVO.getCodDatiCatastali());
					}
				}
				try {
					immobili_helper.saveUpdateDatiCatastali(datiCatastali);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	protected void writeAnagrafica(AnagraficheXMLModel anagrafica){
		
		Object agenteinseritore = importer.getAgentiSelected().get(anagrafica.getCodAgenteInseritore());
		if (agenteinseritore == null){
			anagrafica.setCodAgenteInseritore(null);
		}else{
			ArrayList<AgentiVO> al = UtilsHelper.getInstance().findAgentiByDescription((AgentiVO)agenteinseritore);
			if (al.size() > 0){
				anagrafica.setCodAgenteInseritore(((AgentiVO)al.get(0)).getCodAgente());
			}
		}
		ClassiClientiXMLModel classeCliente = importer.getClassiClientiSelected().get(anagrafica.getCodClasseCliente());
		if (classeCliente == null){
		}else{
			ArrayList<ClassiClientiVO> al_cc = UtilsHelper.getInstance().findClassiClientiByDescription((ClassiClientiVO)classeCliente);
			if (al_cc.size() > 0){
				anagrafica.setCodClasseCliente(((ClassiClientiVO)al_cc.get(0)).getCodClasseCliente());
			}
		}
		if (!this.isInMergeList(anagrafica)){
			
			anagrafica.setCodAnagrafica(null);
			anagrafica.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
		}else{
			anagrafica.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
			ArrayList<Anagrafiche> amlist = anagrafiche_helper.getAnagraficheExist(anagrafica);
			if (amlist.size() > 0){
				anagrafica.setCodAnagrafica(amlist.get(0).getCodAnagrafica());
			}
			
		}
		
		try {
			anagrafiche_helper.saveUpdateAnagrafica(anagrafica);
		} catch (SQLException e) {
			e.printStackTrace();
		}						
										
	}
	
	protected void writeAffitto(AffittiXMLModel affitto){
		
		Object agenteinseritore = importer.getAgentiSelected().get(affitto.getCodAgenteIns());
		if (agenteinseritore == null){
			affitto.setCodAgenteIns(null);
		}else{
			ArrayList<AgentiVO> al = UtilsHelper.getInstance().findAgentiByDescription((AgentiVO)agenteinseritore);
			if (al.size() > 0){
				affitto.setCodAgenteIns(((AgentiVO)al.get(0)).getCodAgente());
			}
		}
		Object immobile = importer.getImmobiliSelected().get(affitto.getCodImmobile());
		if (immobile != null){
			ImmobiliModel im = immobili_helper.getImmobileByRif(((ImmobiliVO)immobile).getRif());
			if (im != null){
				affitto.setCodImmobile(im.getCodImmobile());
				
				if (!this.isInMergeList(affitto)){
					
					affitto.setCodAffitti(null);
					affitto.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
				}else{
					
					affitto.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
					if (affitto.getCodImmobile() != 0){
						ArrayList<AffittiModel> amlist = affitti_helper.getAffittiByRifDate(((ImmobiliXMLModel)immobile).getRif(), 
																							affitto.getDataInizio(), 
																							affitto.getDataFine());
						if (amlist.size() > 0){
							affitto.setCodAffitti(amlist.get(0).getCodAffitti());
						}
					}
					
				}
				
				try {
					affitti_helper.saveUpdateAffitti(affitto);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				
			}
		}
		
	}

	protected void writeAffittiRate(AffittiRateXMLModel affittiRate){
		
		AffittiXMLModel affitto = importer.getAffittiSelected().get(affittiRate.getCodAffitto());
		if (affitto != null){
			ImmobiliXMLModel immobile = importer.getImmobiliSelected().get(affitto.getCodImmobile());
			if (immobile != null){
				ArrayList<AffittiModel> al_affitti =affitti_helper.getAffittiByRifDate(immobile.getRif(), affitto.getDataInizio(), affitto.getDataFine());
				if (al_affitti.size() > 0){
					affittiRate.setCodAffitto(((AffittiModel)al_affitti.get(0)).getCodAffitti());
					
					AnagraficheXMLModel anagrafica = importer.getAnagraficheSelected().get(affittiRate.getCodAnagrafica());
					if (anagrafica != null){
						ArrayList<Anagrafiche> ala_model = anagrafiche_helper.getAnagraficheExist(anagrafica);
						if (ala_model.size() > 0){
							affittiRate.setCodAnagrafica(ala_model.get(0).getCodAnagrafica());
						}
					}
					
					if (!this.isInMergeList(affittiRate)){
						affittiRate.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
						affittiRate.setCodAffittiRate(null);
					}else{
						affittiRate.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
						ArrayList<AffittiRateVO> al_ar = affitti_helper.getAffittiRateExist(affittiRate);
						if (al_ar.size() > 0){
							affittiRate.setCodAffittiRate(al_ar.get(0).getCodAffittiRate());	
						}						

					}
					
					try {
						affitti_helper.saveUpdateAffittiRate(affittiRate);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}

	protected void writeAffittiSpese(AffittiSpeseXMLModel affittiSpese){
		
		AffittiXMLModel affitto = importer.getAffittiSelected().get(affittiSpese.getCodAffitto());
		if (affitto != null){
			ImmobiliXMLModel immobile = importer.getImmobiliSelected().get(affitto.getCodImmobile());
			if (immobile != null){
				ArrayList<AffittiModel> al_affitti =affitti_helper.getAffittiByRifDate(immobile.getRif(), affitto.getDataInizio(), affitto.getDataFine());
				if (al_affitti.size() > 0){
					affittiSpese.setCodAffitto(((AffittiModel)al_affitti.get(0)).getCodAffitti());
					
					AnagraficheXMLModel anagrafica = importer.getAnagraficheSelected().get(affittiSpese.getCodAnagrafica());
					if (anagrafica != null){
						ArrayList<Anagrafiche> ala_model = anagrafiche_helper.getAnagraficheExist(anagrafica);
						if (ala_model.size() > 0){
							affittiSpese.setCodAnagrafica(ala_model.get(0).getCodAnagrafica());
						}
					}
					
					if (!this.isInMergeList(affittiSpese)){
						affittiSpese.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
						affittiSpese.setCodAffittiSpese(null);
					}else{
						affittiSpese.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
						ArrayList<AffittiSpeseVO> al_as = affitti_helper.getAffittiSpeseExist(affittiSpese);
						if (al_as.size() > 0){
							affittiSpese.setCodAffittiSpese(al_as.get(0).getCodAffittiSpese());	
						}						

					}
					
					try {
						affitti_helper.saveUpdateAffittiSpese(affittiSpese);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}
	
	protected void writeTipologiaImmobile(TipologieImmobiliXMLModel tipologiaImmobile){
		
		if (!this.isInMergeList(tipologiaImmobile)){
			
			tipologiaImmobile.setCodTipologiaImmobile(null);
			tipologiaImmobile.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);

		}else{
			ArrayList<TipologieImmobiliVO> al_tiVO = UtilsHelper.getInstance().findTipologieImmobiliByDescription(tipologiaImmobile);
			if (al_tiVO.size() > 0){
				tipologiaImmobile.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
				tipologiaImmobile.setCodTipologiaImmobile(al_tiVO.get(0).getCodTipologiaImmobile());
								
			}
			
		}
		
		try {
			UtilsHelper.getInstance().saveUpdateTipologiaImmobile(tipologiaImmobile);
		} catch (SQLException e) {
			e.printStackTrace();
		}

				
	}

	protected void writeRiscaldamento(RiscaldamentiXMLModel riscaldamento){
		
		if (!this.isInMergeList(riscaldamento)){
			
			riscaldamento.setCodRiscaldamento(null);
			riscaldamento.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);

		}else{
			ArrayList<RiscaldamentiVO> al_r = UtilsHelper.getInstance().findRiscaldamentiByDescription(riscaldamento);
			if (al_r.size() > 0){
				riscaldamento.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
				riscaldamento.setCodRiscaldamento(al_r.get(0).getCodRiscaldamento());
				
			}
			
		}
		
		try {
			UtilsHelper.getInstance().saveUpdateRiscaldamento(riscaldamento);
		} catch (SQLException e) {
			e.printStackTrace();
		}

			
	}
		
	protected void writeStatoConservativo(StatoConservativoXMLModel statoConservativo){
		
		if (!this.isInMergeList(statoConservativo)){
			
			statoConservativo.setCodStatoConservativo(null);
			statoConservativo.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
			try {
				UtilsHelper.getInstance().saveUpdateStatoConservativo(statoConservativo);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}else{
			ArrayList<StatoConservativoVO> al_sc = UtilsHelper.getInstance().findStatoConservativoByDescription(statoConservativo);
			if (al_sc.size() > 0){
				statoConservativo.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
				statoConservativo.setCodStatoConservativo(al_sc.get(0).getCodStatoConservativo());
				
				try {
					UtilsHelper.getInstance().saveUpdateStatoConservativo(statoConservativo);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
				
	}

	protected void writeClasseEnergetica(ClasseEnergeticaXMLModel classeEnergetica){
		
		if (!this.isInMergeList(classeEnergetica)){
			
			classeEnergetica.setCodClasseEnergetica(null);
			classeEnergetica.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
			
		}else{
			ArrayList<ClasseEnergeticaVO> al_ce = UtilsHelper.getInstance().findClasseEnergeticaByDescription(classeEnergetica);
			if (al_ce.size() > 0){
				classeEnergetica.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
				classeEnergetica.setCodClasseEnergetica(al_ce.get(0).getCodClasseEnergetica());
				
			}
			
		}
		
		try {
			UtilsHelper.getInstance().saveUpdateClasseEnergetica(classeEnergetica);
		} catch (SQLException e) {
			e.printStackTrace();
		}

				
	}
		
	protected void writeTipologiaContatti(TipologiaContattiXMLModel tipologiaContatti){
		
		if (!this.isInMergeList(tipologiaContatti)){
			
			tipologiaContatti.setCodTipologiaContatto(null);
			tipologiaContatti.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);

		}else{
			ArrayList<TipologiaContattiVO> al_tc = UtilsHelper.getInstance().findTipologiaContattiByDescription(tipologiaContatti);
			if (al_tc.size() > 0){
				tipologiaContatti.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
				tipologiaContatti.setCodTipologiaContatto(al_tc.get(0).getCodTipologiaContatto());

			}
		}

		try {
			UtilsHelper.getInstance().saveUpdateTipologiaContatti(tipologiaContatti);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void writeTipologiaStanze(TipologiaStanzeXMLModel tipologiaStanze){
		
		if (!this.isInMergeList(tipologiaStanze)){
			
			tipologiaStanze.setCodTipologiaStanza(null);
			tipologiaStanze.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
			
		}else{
			ArrayList<TipologiaStanzeVO> al_tp = UtilsHelper.getInstance().findTipologieStanzeByDescription(tipologiaStanze);
			if (al_tp.size() > 0){
				tipologiaStanze.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
				tipologiaStanze.setCodTipologiaStanza(al_tp.get(0).getCodTipologiaStanza());
								
			}
		}
		
		try {
			UtilsHelper.getInstance().saveUpdateTipologiaStanze(tipologiaStanze);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	protected void writeClassiClienti(ClassiClientiXMLModel classiClienti){
		
		if (!this.isInMergeList(classiClienti)){
			
			classiClienti.setCodClasseCliente(null);
			classiClienti.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
						
		}else{
			ArrayList<ClassiClientiVO> alcc = UtilsHelper.getInstance().findClassiClientiByDescription(classiClienti);
			if (alcc.size() > 0){
				classiClienti.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
				classiClienti.setCodClasseCliente(alcc.get(0).getCodClasseCliente());
				
			}
			
		}
		
		try {
			UtilsHelper.getInstance().saveUpdateClassiClienti(classiClienti);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		
	}

	protected void writeAgenti(AgentiXMLModel agente){
		
		if (!this.isInMergeList(agente)){
			
			agente.setCodAgente(null);
			agente.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
			
		}else{
			agente.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
			ArrayList<AgentiVO> al_agenti = UtilsHelper.getInstance().findAgentiByDescription(agente);
			if (al_agenti.size() > 0){
				agente.setCodAgente(al_agenti.get(0).getCodAgente());
				
			}
		}

		try {
			UtilsHelper.getInstance().saveUpdateAgente(agente);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	protected void writeContatti(ContattiXMLModel contatto){
		
		AnagraficheXMLModel anagrafica = importer.getAnagraficheSelected().get(contatto.getCodAnagrafica());
		if (anagrafica != null){
			ArrayList<Anagrafiche> al = anagrafiche_helper.getAnagraficheExist((AnagraficheXMLModel)anagrafica);
			if (al.size() > 0){
				contatto.setCodAnagrafica((al.get(0)).getCodAnagrafica());
			}
		}
		AgentiXMLModel agente = importer.getAgentiSelected().get(contatto.getCodAgente());
		if (agente != null){
			ArrayList<AgentiVO> al = UtilsHelper.getInstance().findAgentiByDescription(agente);
			if (al.size() > 0){
				contatto.setCodAgente(((AgentiVO)al.get(0)).getCodAgente());
			}
		}
		
		if (anagrafica != null || agente != null){	
			Object tipologiacontatti = importer.getTipologieContattiSelected().get(contatto.getCodTipologiaContatto());
			if (contatto.getCodTipologiaContatto() == 0 || tipologiacontatti != null){
				if (tipologiacontatti != null){
					ArrayList<TipologiaContattiVO> al_tipo_contatto = UtilsHelper.getInstance().findTipologiaContattiByDescription((TipologiaContattiVO)tipologiacontatti);
					if (al_tipo_contatto.size() > 0){
						contatto.setCodTipologiaContatto(((TipologiaContattiVO)al_tipo_contatto.get(0)).getCodTipologiaContatto());
					}
				}
				
				if (!this.isInMergeList(contatto)){					
					contatto.setCodContatto(null);
					contatto.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);

				}else{
					contatto.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
					ArrayList<ContattiVO> al_contatti = UtilsHelper.getInstance().findContattiByDescription(contatto);
					if (al_contatti.size() > 0){
						contatto.setCodContatto(((ContattiVO)al_contatti.get(0)).getCodContatto());

					}

				}
				
				try {
					UtilsHelper.getInstance().saveUpdateContatto(contatto);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				
			}
			
		}
				
	}
		
	protected void writeColloqui(ColloquiXMLModel colloquio){
		
		Object agenteinseritore = importer.getAgentiSelected().get(colloquio.getCodAgenteInseritore());
		if (agenteinseritore == null){
			colloquio.setCodAgenteInseritore(null);
		}else{
			ArrayList<AgentiVO> al = UtilsHelper.getInstance().findAgentiByDescription((AgentiVO)agenteinseritore);
			if (al.size() > 0){
				colloquio.setCodAgenteInseritore(((AgentiVO)al.get(0)).getCodAgente());
			}			
		}
		ImmobiliXMLModel immobile = importer.getImmobiliSelected().get(colloquio.getCodImmobileAbbinato());
		if (immobile == null){
			colloquio.setCodImmobileAbbinato(null);
		}else{
			ImmobiliModel im = immobili_helper.getImmobileByRif(immobile.getRif());
			colloquio.setCodImmobileAbbinato(im.getCodImmobile());
		}
		if (!this.isInMergeList(colloquio)){
			
			colloquio.setCodColloquio(null);
			colloquio.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
			try {
				colloqui_helper.saveUpdateColloquio(colloquio);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}else{
			colloquio.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
			
				Collection<AnagraficheXMLModel> c_anag = importer.getAnagraficheSelected().values();
				Iterator<AnagraficheXMLModel> it = c_anag.iterator();
				
				while (it.hasNext()) {
					
					AnagraficheXMLModel anagrafica = it.next();
					ArrayList<Anagrafiche> al_anag_db = anagrafiche_helper.getAnagraficheExist(anagrafica);
					
					boolean find_it = false;
					
					Iterator<Anagrafiche>it_anag_db = al_anag_db.iterator();
					while (it_anag_db.hasNext()) {
						Anagrafiche anag_db = it_anag_db.next();
// TODO cayenne						ArrayList<ColloquiModel> al_colloqui = colloqui_helper.getColloquiExist(colloquio, anag_db);
						
//						if (al_colloqui.size() > 0){
//							colloquio.setCodColloquio(((ColloquiModel)al_colloqui.get(0)).getCodColloquio());
//							find_it = true;
//							break;
//						}
						
					}
					
					if (find_it){
						break;
					}
					
				}
			
		}
		try {
			colloqui_helper.saveUpdateColloquio(colloquio);
		} catch (SQLException e) {
			e.printStackTrace();
		}

				
	}

	protected void writeColloquiAgenti(ColloquiAgentiXMLModel colloquioagenti){
		
		AgentiXMLModel axml = importer.getAgentiSelected().get(colloquioagenti.getCodAgente());
		AgentiVO agente = null;
		if (axml != null){
			ArrayList<AgentiVO> al = UtilsHelper.getInstance().findAgentiByDescription(axml);
			if (al.size() > 0){
				agente = ((AgentiVO)al.get(0));
				colloquioagenti.setCodAgente(agente.getCodAgente());
				
				if (agente != null){
					
					ColloquiXMLModel colloquio = importer.getColloquiSelected().get(colloquioagenti.getCodColloquio());
					ArrayList al_colloqui = colloqui_helper.getColloquiExist(colloquio);
					ColloquiModel colloquiodb = null;
					
					if (al_colloqui.size() > 0){
						colloquiodb = ((ColloquiModel)al_colloqui.get(0));
						colloquioagenti.setCodColloquio(colloquiodb.getCodColloquio());
						
					}						
					
					if (colloquiodb != null){			
					
						if (!this.isInMergeList(colloquioagenti)){
							colloquioagenti.setCodColloquioAgenti(null);
							colloquioagenti.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);

						}else{
							colloquioagenti.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
							if (axml != null){
								ArrayList<ColloquiAgentiVO> alca = this.colloqui_helper.getColloquioAgenteExist(colloquio, axml);
								if (alca.size() > 0){
									colloquioagenti.setCodColloquioAgenti(((ColloquiAgentiVO)alca.get(0)).getCodColloquioAgenti());
								}
							}					
						}
						
						try {
							colloqui_helper.saveUpdateColloquiAgenti(colloquioagenti);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
					}
						
				}				
				
			}	
			
		}		
		
	}

	protected void writeColloquiAnagrafiche(ColloquiAnagraficheXMLModel colloquioAnagrafiche){
		
		AnagraficheXMLModel axml = importer.getAnagraficheSelected().get(colloquioAnagrafiche.getCodAnagrafica());
		
		if (axml != null){
// TODO cayenne			
//			ArrayList<AnagraficheModel> al = anagrafiche_helper.getAnagraficheExist(axml);
//			if (al.size() > 0){
//				AnagraficheModel anagrafica = ((AnagraficheModel)al.get(0));
//				colloquioAnagrafiche.setCodAnagrafica(anagrafica.getCodAnagrafica());
//				
//				if (anagrafica != null){ 
//					ColloquiXMLModel colloquio = importer.getColloquiSelected().get(colloquioAnagrafiche.getCodColloquio());
//					ArrayList al_colloqui = colloqui_helper.getColloquiExist(colloquio);
//						
//					if (al_colloqui.size() > 0){
//						ColloquiModel colloquiodb = ((ColloquiModel)al_colloqui.get(0));
//						colloquioAnagrafiche.setCodColloquio(colloquiodb.getCodColloquio());
//						if (!this.isInMergeList(colloquioAnagrafiche)){
//							colloquioAnagrafiche.setCodColloquioAnagrafiche(null);
//							colloquioAnagrafiche.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
//
//						}else{
//							colloquioAnagrafiche.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
//							if (axml != null){
//								ArrayList<ColloquiAnagraficheVO> alca = this.colloqui_helper.getColloquioAnagraficheExist(colloquio, axml);
//								if (alca.size() > 0){
//									colloquioAnagrafiche.setCodColloquioAnagrafiche(((ColloquiAnagraficheVO)alca.get(0)).getCodColloquioAnagrafiche());
//								}
//							}					
//						}
//						
//						try {
//							colloqui_helper.saveUpdateColloquiAnagrafiche(colloquioAnagrafiche);
//						} catch (SQLException e) {
//							e.printStackTrace();
//						}
//
//					}						
//						
//				}				
//				
//			}	
			
		}		
		
	}

	protected void writeColloquiCriteriRicerca(ColloquiCriteriRicercaXMLModel criterioRicerca){
		
		ColloquiXMLModel c = this.importer.getColloquiSelected().get(criterioRicerca.getCodColloquio());
		
		if (c != null){
// TODO cayenne			
//			Collection<AnagraficheXMLModel> c_anag = importer.getAnagraficheSelected().values();
//			Iterator<AnagraficheXMLModel> it = c_anag.iterator();
//			
//			while (it.hasNext()) {
//				
//				AnagraficheModel anagrafica = it.next();
//				ArrayList<AnagraficheModel> al_anag_db = anagrafiche_helper.getAnagraficheExist(anagrafica);
//				
//				boolean find_it = false;
//				
//				Iterator<AnagraficheModel>it_anag_db = al_anag_db.iterator();
//				while (it_anag_db.hasNext()) {
//					AnagraficheModel anag_db = it_anag_db.next();
//					ArrayList<ColloquiModel> al_colloqui = colloqui_helper.getColloquiExist((ColloquiVO)c, (AnagraficheVO)anag_db);
//					
//					if (al_colloqui.size() > 0){
//						criterioRicerca.setCodColloquio(al_colloqui.get(0).getCodColloquio());
//						find_it = true;
//						break;
//					}
//				}
//				
//				if (find_it){
//					break;
//				}
//				
//			}			
//			
//			if (!this.isInMergeList(criterioRicerca)){
//				criterioRicerca.setCodCriterioRicerca(null);
//				criterioRicerca.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
//
//			}else{
//				criterioRicerca.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
//				CriteriRicercaModel crm = new CriteriRicercaModel(criterioRicerca);
//				ArrayList<CriteriRicercaModel> al_cr = UtilsHelper.getInstance().findCriteriRicercaByDescription(crm, c);				
//				if (al_cr.size() > 0){					
//					criterioRicerca.setCodCriterioRicerca(((CriteriRicercaModel)al_cr.get(0)).getCodCriterioRicerca());					
//				}					
//			}
//			
//			try {
//				colloqui_helper.saveUpdateColloquiCriteriRicerca(criterioRicerca);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}			
			
		}
		
	}
	
	protected void writeStanzeImmobili(StanzeImmobiliXMLModel stanzaimmobile){
		
		TipologiaStanzeVO stVO = importer.getTipologieStanzeSelected().get(stanzaimmobile.getCodTipologiaStanza());
		if (stVO != null){
			ArrayList<TipologiaStanzeVO> alts = UtilsHelper.getInstance().findTipologieStanzeByDescription(stVO);
			if (alts.size() > 0){
				stanzaimmobile.setCodTipologiaStanza(((TipologiaStanzeVO)alts.get(0)).getCodTipologiaStanza());
				
				ImmobiliXMLModel immobile = importer.getImmobiliSelected().get(stanzaimmobile.getCodImmobile());
				if (immobile != null){
					ImmobiliModel im = immobili_helper.getImmobileByRif(immobile.getRif());
					if (im != null){
						stanzaimmobile.setCodImmobile(im.getCodImmobile());
						
						if (!this.isInMergeList(stanzaimmobile)){
							stanzaimmobile.setCodStanzeImmobili(null); 
							stanzaimmobile.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
						}else{
							stanzaimmobile.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
							ArrayList al =  immobili_helper.getStanzaImmobileByCodImmobileCodTipologia(stanzaimmobile.getCodImmobile(), 
																									   stanzaimmobile.getCodTipologiaStanza());
							if (al.size() > 0){
								stanzaimmobile.setCodStanzeImmobili(((StanzeImmobiliVO)al.get(0)).getCodStanzeImmobili());
							}
						}						
						
						try {
							immobili_helper.saveUpdateStanzaImmobile(stanzaimmobile);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
					}
				}
				
			}
		}
		
	}

	protected void writeImmobiliPropieta(){
		
		ArrayList al = importer.getAlImmobiliPropieta();
				
		for (Object object : al) {
			
			ImmobiliXMLModel imm = this.importer.getImmobiliSelected().get(((ImmobiliPropietariVO)object).getCodImmobile());
			AnagraficheXMLModel anag = this.importer.getAnagraficheSelected().get(((ImmobiliPropietariVO)object).getCodAnagrafica());
			if ((imm != null) && (anag != null)){
				ImmobiliModel im = immobili_helper.getImmobileByRif(imm.getRif());
				ArrayList<Anagrafiche> al_am = anagrafiche_helper.getAnagraficheExist(anag);
				if ((im != null) && (al_am.size() > 0)){
					((ImmobiliPropietariVO)object).setCodImmobile(im.getCodImmobile());
					((ImmobiliPropietariVO)object).setCodAnagrafica(al_am.get(0).getCodAnagrafica());
					try {
						immobili_helper.saveUpdateImmobiliPropieta((ImmobiliPropietariVO)object);
					} catch (SQLException e) {
						e.printStackTrace();
					}	
				}else{
					((ImmobiliPropietariVO)object).setCodImmobile(null);
					((ImmobiliPropietariVO)object).setCodAnagrafica(null);
					
				}
			}
		}
		
	}
	
	protected void writeAppuntamenti(AppuntamentiXMLModel appuntamento){
		/*
		Object tipoappuntamento = importer.getTipologiaAppuntamentiSelected().get(appuntamento.getCodTipoAppuntamento()); 
		if (tipoappuntamento == null){
			appuntamento.setCodTipoAppuntamento(0);
		}else{
			ArrayList<TipiAppuntamentiVO> al = UtilsHelper.getInstance().findTipiAppuntamentiByDescription(((TipiAppuntamentiVO)tipoappuntamento));
			if (al.size() > 0){
				appuntamento.setCodTipoAppuntamento(((TipiAppuntamentiVO)al.get(0)).getCodTipoAppuntamento());
			}
			
		}

		if (!this.isInMergeList(appuntamento)){
			
			appuntamento.setCodTipoAppuntamento(0);
			
		}else{
			
			ArrayList<ContattiVO> al = UtilsHelper.getInstance().findContattiByDescription(contatto);
			if (al.size() > 0){
				contatto.setCodContatto(((ContattiVO)al.get(0)).getCodContatto());
			}

		}
		
		try {
			UtilsHelper.getInstance().saveUpdateContatto(contatto);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		*/
	}

	protected void writeImmagine(ImmagineXMLModel immagine){
	
		ImmobiliXMLModel immobile = importer.getImmobiliSelected().get(immagine.getCodImmobile());
		if (immobile != null){
			
			ImmobiliModel imm = immobili_helper.getImmobileByRif(immobile.getRif());
			if (imm != null){
				immagine.setCodImmobile(imm.getCodImmobile());
				
				if (!this.isInMergeList(immagine)){
					immagine.setCodImmagine(null);
					immagine.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
				}else{
					immagine.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
				}
				try {
					
					if (immobili_helper.saveUpdateImmagine(immagine)){
						UtilsHelper.getInstance().copyFile(importer.getPathExportFile()+File.separator+"immagini"+File.separator + immobile.getCodImmobile() + File.separator+immagine.getPathImmagine(),
														   UtilsHelper.getInstance().getPathImmagini()+File.separator+immagine.getCodImmobile()+File.separator+immagine.getPathImmagine());
		
					
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}			
		}		
	}

	protected void writeAllegatiImmobile(AllegatiImmobiliXMLModel allegatiImmobili){
		
		Integer old_cod_immobile = allegatiImmobili.getCodImmobile();
		
		ImmobiliXMLModel immobile = importer.getImmobiliSelected().get(allegatiImmobili.getCodImmobile());
		if (immobile != null){
			
			ImmobiliModel imm = immobili_helper.getImmobileByRif(immobile.getRif());
			if (imm != null){
				
				allegatiImmobili.setCodImmobile(imm.getCodImmobile());
				
				if (!this.isInMergeList(allegatiImmobili)){
					allegatiImmobili.setCodAllegatiImmobili(null);
					allegatiImmobili.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
				}else{
					allegatiImmobili.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
					ArrayList al_allegati = immobili_helper.getAllegatiImmobileByCodImmobile(imm.getCodImmobile());
					Iterator it_allegati = al_allegati.iterator();
					while (it_allegati.hasNext()) {
						AllegatiImmobiliVO allegatoImmobili_db = (AllegatiImmobiliVO) it_allegati.next();
						if (allegatoImmobili_db.getNome().equalsIgnoreCase(allegatiImmobili.getNome())){
						
							allegatiImmobili.setCodAllegatiImmobili(allegatoImmobili_db.getCodAllegatiImmobili());
							break;
						}						
					}
										
				}
				
				try {
					immobili_helper.saveUpdateAllegatiImmobili(allegatiImmobili);
					String pathOrigine = importer.getPathExportFile() + File.separator + "allegati" + File.separator  + "immobili" + 
							 			 File.separator + old_cod_immobile + File.separator + allegatiImmobili.getNome();

					String pathDestinazione = UtilsHelper.getInstance().getPathAllegati() + File.separator + "immobili" + 
				                  			  File.separator + allegatiImmobili.getCodImmobile() + File.separator + allegatiImmobili.getNome();

					UtilsHelper.getInstance().copyFile(pathOrigine,pathDestinazione);
				} catch (SQLException e) {
					e.printStackTrace();
				}				
										
			}
			
		}
				
	}

	protected void writeAllegatiColloquio(AllegatiColloquiXMLModel allegatiColloquio){
		
		Integer old_cod_colloquio = allegatiColloquio.getCodColloquio();
		
		ColloquiXMLModel colloquio = importer.getColloquiSelected().get(allegatiColloquio.getCodColloquio());
		if (colloquio != null){
			
			ArrayList<ColloquiModel> cm = colloqui_helper.getColloquiExist(colloquio);
			if (cm.size() > 0){
				
				allegatiColloquio.setCodColloquio(cm.get(0).getCodColloquio());
				
				if (!this.isInMergeList(allegatiColloquio)){
					allegatiColloquio.setCodAllegatiColloquio(null);
					allegatiColloquio.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
				}else{
					allegatiColloquio.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
					ArrayList<AllegatiColloquiVO> al_allegati = colloqui_helper.getColloquioAllegatiByCodColloquio(cm.get(0).getCodColloquio());
					Iterator<AllegatiColloquiVO> it_allegati = al_allegati.iterator();
					while (it_allegati.hasNext()) {
						AllegatiColloquiVO allegatoColloqui_db = it_allegati.next();
						if (allegatoColloqui_db.getNome().equalsIgnoreCase(allegatiColloquio.getNome())){
							allegatiColloquio.setCodAllegatiColloquio(allegatoColloqui_db.getCodAllegatiColloquio());
							break;
						}						
					}
										
				}
				
				try {
					
					colloqui_helper.saveUpdateColloquiAllegati(allegatiColloquio);
					
					String pathOrigine = importer.getPathExportFile() + File.separator + "allegati" + File.separator  + "colloqui" + 
							 			 File.separator + old_cod_colloquio + File.separator + allegatiColloquio.getNome();

					String pathDestinazione = UtilsHelper.getInstance().getPathAllegati() + File.separator + "colloqui" + 
				                  			  File.separator + allegatiColloquio.getCodColloquio() + File.separator + allegatiColloquio.getNome();

					UtilsHelper.getInstance().copyFile(pathOrigine,pathDestinazione);
				} catch (SQLException e) {
					e.printStackTrace();
				}				

										
			}
			
		}
				
	}

	protected void writeAllegatiAffitti(AffittiAllegatiXMLModel allegatiAffitti){
		
		Integer old_cod_affitto = allegatiAffitti.getCodAffitto();
		
		AffittiXMLModel affitto = importer.getAffittiSelected().get(allegatiAffitti.getCodAffitto());
		if (affitto != null){
			ImmobiliXMLModel immobile = importer.getImmobiliSelected().get(affitto.getCodImmobile());
			ImmobiliModel imm = immobili_helper.getImmobileByRif(immobile.getRif());			
			if (imm != null){							
				ArrayList<AffittiModel> al_aff = affitti_helper.getAffittiByRifDate(imm.getRif(), affitto.getDataInizio(), affitto.getDataFine());
				if (al_aff.size() > 0){
					allegatiAffitti.setCodAffitto(((AffittiModel)al_aff.get(0)).getCodAffitti());
					
					if (!this.isInMergeList(allegatiAffitti)){
						allegatiAffitti.setCodAffittiAllegati(null);
						allegatiAffitti.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
					}else{
						allegatiAffitti.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
						AffittiAllegatiVO aaVO = affitti_helper.getAffittiAllegatiExist(allegatiAffitti.getCodAffitto(), allegatiAffitti.getNome());
						if (aaVO != null){
							allegatiAffitti.setCodAffittiAllegati(aaVO.getCodAffittiAllegati());
						}
					}
					

					try {
						
						affitti_helper.saveUpdateAllegatiAffitti(allegatiAffitti);
						
						String pathOrigine = importer.getPathExportFile() + File.separator + "allegati" + File.separator  + "affitti" + 
								 			 File.separator + old_cod_affitto + File.separator + allegatiAffitti.getNome();

						String pathDestinazione = UtilsHelper.getInstance().getPathAllegati() + File.separator + "affitti" + 
					                  			  File.separator + allegatiAffitti.getCodAffitto() + File.separator + allegatiAffitti.getNome();
						
						UtilsHelper.getInstance().copyFile(pathOrigine,pathDestinazione);
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
				
			}			
			
		}
				
	}

	protected void writeAffittiAnagrafiche(AffittiAnagraficheXMLModel affittiAnagrafiche){
		
		Integer old_cod_affitto = affittiAnagrafiche.getCodAffitto();
		
		AffittiXMLModel affitto = importer.getAffittiSelected().get(affittiAnagrafiche.getCodAffitto());
		if (affitto != null){
			ImmobiliXMLModel immobile = importer.getImmobiliSelected().get(affitto.getCodImmobile());
			ImmobiliModel imm = immobili_helper.getImmobileByRif(immobile.getRif());			
			if (imm != null){							
				ArrayList<AffittiModel> al_aff = affitti_helper.getAffittiByRifDate(imm.getRif(), affitto.getDataInizio(), affitto.getDataFine());
				if (al_aff.size() > 0){
					affittiAnagrafiche.setCodAffitto(((AffittiModel)al_aff.get(0)).getCodAffitti());
					
					Integer old_cod_anagrafica = affittiAnagrafiche.getCodAnagrafica();
					
					AnagraficheXMLModel anagrafica = importer.getAnagraficheSelected().get(affittiAnagrafiche.getCodAnagrafica());
					if (anagrafica != null){
						
						ArrayList<Anagrafiche> al_am = anagrafiche_helper.getAnagraficheExist(anagrafica);			
						if (al_am.size() > 0){							
							affittiAnagrafiche.setCodAnagrafica(al_am.get(0).getCodAnagrafica());
							
							if (!this.isInMergeList(affittiAnagrafiche)){
								affittiAnagrafiche.setCodAffitto(null);
								affittiAnagrafiche.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
							}else{
								affittiAnagrafiche.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
								AffittiAnagraficheVO aaVO = affitti_helper.getAffittiAnagraficheExist(affittiAnagrafiche.getCodAffitto(), affittiAnagrafiche.getCodAnagrafica());
								if (aaVO != null){
									affittiAnagrafiche.setCodAffittiAnagrafiche(aaVO.getCodAffittiAnagrafiche());
									try {
										affitti_helper.saveUpdateAffittiAnagrafiche(affittiAnagrafiche);
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
								
							}
														
						}										
						
					}					
					
				}
				
			}
			
		}
		
	}
	
	protected void writeEntity(EntityXMLModel entity){}

	protected void writeAttribute(AttributeXMLModel attribute){
		
		if (this.isInMergeList(attribute)){
			AttributeModel attribute_local = campiPersonali_helper.getAttributeByIdClassFieldName(attribute.getIdClassEntity(), attribute.getAttributeName());
			if (attribute_local != null){
				attribute.setIdAttribute(attribute_local.getIdAttribute());
				attribute.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
			}
		}else{
			attribute.setIdAttribute(null);
			attribute.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
		}
		
		campiPersonali_helper.saveUpdateAttribute(attribute);
		
	}

	protected void writeAttributeValue(AttributeValueXMLModel attributeValue){
		
		AttributeXMLModel aXMLModel = importer.getAttributiSelected().get(attributeValue.getIdField());
		
		if (aXMLModel != null){
			
			EntityXMLModel em = importer.getEntitaSelected().get(aXMLModel.getIdClassEntity());
			AttributeModel db_aXMLModel = campiPersonali_helper.getAttributeByIdClassFieldName(aXMLModel.getIdClassEntity(), 
																						       aXMLModel.getAttributeName());
			if (db_aXMLModel != null){
				attributeValue.setIdField(db_aXMLModel.getIdAttribute());			
			
				
				Object idObject = null;
				if (em.getClassName().equalsIgnoreCase(ImmobiliVO.class.getName())){
					ImmobiliXMLModel immobile = this.importer.getImmobiliSelected().get(attributeValue.getIdObject());
					ImmobiliModel im = immobili_helper.getImmobileByRif(immobile.getRif());
					if (im != null){
						idObject = im.getCodImmobile();
					}
				}
				
				if (em.getClassName().equalsIgnoreCase(AnagraficheVO.class.getName())){
					AnagraficheXMLModel anagrafica = this.importer.getAnagraficheSelected().get(attributeValue.getIdObject());
					ArrayList<Anagrafiche> al = anagrafiche_helper.getAnagraficheExist(anagrafica);
					if (al.size() > 0){
						idObject = al.get(0).getCodAnagrafica();
					}
				}
				
				if (em.getClassName().equalsIgnoreCase(ColloquiVO.class.getName())){
					
					ArrayList<ColloquiModel> al = new ArrayList<ColloquiModel>();
					
					ColloquiXMLModel colloqui = importer.getColloquiSelected().get(attributeValue.getIdObject());
					HashMap<Integer,AnagraficheXMLModel> anagraficheSelected = importer.getAnagraficheSelected();
					Iterator<Entry<Integer, AnagraficheXMLModel>> it = anagraficheSelected.entrySet().iterator();
					
					boolean find = false;
					while (it.hasNext()) {
						
						Entry<Integer, AnagraficheXMLModel> anagrafica = it.next();
						ArrayList<Anagrafiche> al_anagrafiche = this.anagrafiche_helper.getAnagraficheExist(anagrafica.getValue());
						
						for (Anagrafiche anagraficaModel : al_anagrafiche) {
// TODO cayenne							al.addAll(colloqui_helper.getColloquiExist(colloqui, anagraficaModel));
						}
							
					}
					if (al.size() > 0){
						idObject = al.get(0).getCodColloquio();	
					}
					
							
				}
	
				if (em.getClassName().equalsIgnoreCase(AffittiVO.class.getName())){
					
					ArrayList<AffittiModel> al = new ArrayList<AffittiModel>();
					
					AffittiXMLModel affitti = importer.getAffittiSelected().get(attributeValue.getIdObject());
					ImmobiliXMLModel ixmlModel = this.importer.getImmobiliSelected().get(affitti.getCodImmobile());
					
					if (ixmlModel != null){
						al = affitti_helper.getAffittiByRifDate(ixmlModel.getRif(), 
												  				affitti.getDataInizio(), 
														  		affitti.getDataFine());
					}
					if (al.size() > 0){
						idObject = al.get(0).getCodAffitti();	
					}
							
				}
	
				if (idObject != null){
					
					AttributeValueModel avm = null;
					avm = campiPersonali_helper.getAttributeValueByIdFieldIdObject(db_aXMLModel.getIdAttribute(), 
																				   (Integer)idObject);
					Integer oldidvalue = attributeValue.getIdValue();
					
					if (idObject != null){
						if (isInMergeList(attributeValue)){
							avm = campiPersonali_helper.getAttributeValueByIdFieldIdObject(db_aXMLModel.getIdAttribute(), 
																						   (Integer)idObject);
							if (avm != null){
								attributeValue.setIdValue(avm.getIdValue());						
								attributeValue.setImportOperation(ObjectTypeCompare.AGGIORNAMENTO);
							}
						}else{
							attributeValue.setImportOperation(ObjectTypeCompare.NUOVO_INSERIMENTO);
							attributeValue.setIdValue(null);
						}
						
						attributeValue.setIdObject((Integer)idObject);
						System.out.println("av : " + String.valueOf(campiPersonali_helper.saveUpdateAttributeValue(attributeValue)));
						attributeValue.setIdValue(oldidvalue);							
						
						
					}					
					
				}
							
			}
		
		}
		
	}

}
