package winkhouse.xmldeser.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import winkhouse.model.EntityModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Colloqui;
import winkhouse.orm.Immobili;
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
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.ImmagineVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.RiscaldamentiVO;
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

public class CheckExists implements IRunnableWithProgress {

	private ImporterVO importerVO = null;
	private ImmobiliHelper immobiliHelper = null;
	private AnagraficheHelper anagraficheHelper = null;
	private AffittiHelper affittiHelper = null;
	private ColloquiHelper colloquiHelper = null;
	private CampiPersonaliHelper campiPersonaliHelper = null; 
	  

	public CheckExists(ImporterVO importerVO) {
		this.importerVO = importerVO;
		this.immobiliHelper = new ImmobiliHelper();
		this.anagraficheHelper = new AnagraficheHelper();
		this.affittiHelper = new AffittiHelper();
		this.colloquiHelper = new ColloquiHelper();
		this.campiPersonaliHelper = new CampiPersonaliHelper();
	}
	
	@Override
	public void run(IProgressMonitor arg0) throws InvocationTargetException,
												  InterruptedException {
		
		importerVO.setRisultati_merge(new ArrayList());
		arg0.beginTask("Confronto dati ...", importerVO.getRisultati_selected().size());
		
		Collections.sort(importerVO.getRisultati_selected(), new ObjectTypeComparator());
		
		Iterator it = importerVO.getRisultati_selected().iterator();
		
		while (it.hasNext()){
			
			Object o = it.next();
			
			if (o instanceof ImmobiliXMLModel){
				checkImmobile((ImmobiliXMLModel)o);
			}
			if (o instanceof AnagraficheXMLModel){
				checkAnagrafiche((AnagraficheXMLModel)o);
			}
			if (o instanceof AffittiXMLModel){
				checkAffitti((AffittiXMLModel)o);
			}
			if (o instanceof AffittiSpeseXMLModel){
				checkAffittiSpese((AffittiSpeseXMLModel)o);
			}
			if (o instanceof AffittiRateXMLModel){
				checkAffittiRate((AffittiRateXMLModel)o);
			}			
			if (o instanceof TipologieImmobiliXMLModel){
				checkTipologieImmobili((TipologieImmobiliXMLModel)o);
			}
			if (o instanceof RiscaldamentiXMLModel){
				checkRiscaldamenti((RiscaldamentiXMLModel)o);
			}
			if (o instanceof StatoConservativoXMLModel){
				checkStatoConservativo((StatoConservativoXMLModel)o);
			}
			if (o instanceof ClasseEnergeticaXMLModel){
				checkClasseEnergetica((ClasseEnergeticaXMLModel)o);
			}
			if (o instanceof TipologiaContattiXMLModel){
				checkTipologiaContatti((TipologiaContattiXMLModel)o);
			}
			if (o instanceof ContattiXMLModel){
				checkContatti((ContattiXMLModel)o);
			}
			if (o instanceof AgentiXMLModel){
				checkAgenti((AgentiXMLModel)o);
			}
			if (o instanceof AppuntamentiXMLModel){
				checkAppuntamenti((AppuntamentiXMLModel)o);
			}
			if (o instanceof ColloquiXMLModel){
				checkColloqui((ColloquiXMLModel)o);
			}
			if (o instanceof TipologiaStanzeXMLModel){
				checkTipologieStanze((TipologiaStanzeXMLModel)o);
			}
			if (o instanceof ColloquiAgentiXMLModel){
				checkColloquiAgenti((ColloquiAgentiXMLModel)o);
			}
			if (o instanceof ImmagineXMLModel){
				checkImmagine((ImmagineXMLModel)o);
			}
			if (o instanceof StanzeImmobiliXMLModel){
				checkStanzeImmobili((StanzeImmobiliXMLModel)o);
			}
			if (o instanceof DatiCatastaliXMLModel){
				checkDatiCatastali((DatiCatastaliXMLModel)o);
			}
			if (o instanceof AllegatiImmobiliXMLModel){
				checkAllegatiImmobili((AllegatiImmobiliXMLModel)o);
			}
			if (o instanceof AbbinamentiXMLModel){
				checkAbbinamenti((AbbinamentiXMLModel)o);
			}
			if (o instanceof ColloquiAnagraficheXMLModel){
				checkColloquiAnagrafiche((ColloquiAnagraficheXMLModel)o);
			}
			if (o instanceof AffittiAllegatiXMLModel){
				checkAffittiAllegati((AffittiAllegatiXMLModel)o);
			}
			if (o instanceof AffittiAnagraficheXMLModel){
				checkAffittiAnagrafiche((AffittiAnagraficheXMLModel)o);
			}
			if (o instanceof ClassiClientiXMLModel){
				checkClassiClienti((ClassiClientiXMLModel)o);
			}
			if (o instanceof ColloquiCriteriRicercaXMLModel){
				checkCriteriRicerca((ColloquiCriteriRicercaXMLModel)o);
			}
			if (o instanceof AllegatiColloquiXMLModel){
				checkColloquiAllegati((AllegatiColloquiXMLModel)o);
			}
			if (o instanceof EntityXMLModel){
				checkEntita((EntityXMLModel)o);
			}
			if (o instanceof AttributeXMLModel){
				checkAttributo((AttributeXMLModel)o);
			}
			if (o instanceof AttributeValueXMLModel){
				checkValoreAttributo((AttributeValueXMLModel)o);
			}
						
			arg0.worked(1);
		}
		
	}

	protected void checkColloqui(ColloquiXMLModel colloqui){
		
		HashMap<Integer,AnagraficheXMLModel> anagraficheSelected = importerVO.getAnagraficheSelected();
		
		Iterator<Entry<Integer, AnagraficheXMLModel>> it = anagraficheSelected.entrySet().iterator();
		boolean find = false;
		while (it.hasNext()) {
			
			Entry<Integer, AnagraficheXMLModel> anagrafica = it.next();
			//ArrayList<Anagrafiche> al_anagrafiche = this.anagraficheHelper.getAnagraficheExist(anagrafica.getValue());
			// TO DO 
			ArrayList<Anagrafiche> al_anagrafiche = new ArrayList<Anagrafiche>();
			for (Anagrafiche am : al_anagrafiche) {
				ArrayList<ColloquiModel> al = colloquiHelper.getColloquiExist(colloqui, am);
				
				if (al.size() > 0){
									
					importerVO.getRisultati_merge().add(colloqui);
					find = true;
					break;
					
				}
				
			}
			if (find){
				break;
			}
			
		}
				
	}	
	
	protected void checkAppuntamenti(AppuntamentiXMLModel appuntamento){
/*		
		ArrayList<AgentiVO> al = UtilsHelper.getInstance()
											.find(appuntamento);

		if (al.size() > 0){
			importerVO.getRisultati_merge().add(agente);
		}
	*/	
	}
	
	protected void checkAgenti(AgentiXMLModel agente){
		
		ArrayList<AgentiVO> al = UtilsHelper.getInstance()
											.findAgentiByDescription(agente);
		
		if (al.size() > 0){
			importerVO.getRisultati_merge().add(agente);
		}
		
	}

	protected void checkContatti(ContattiXMLModel contatto){
		
		ArrayList<ContattiVO> al = UtilsHelper.getInstance()
				   							  .findContattiByDescription(contatto);

		if (al.size() > 0){
			importerVO.getRisultati_merge().add(contatto);
		}
				
	}

	protected void checkTipologiaContatti(TipologiaContattiXMLModel tipologiaContatti){
		
		ArrayList<TipologiaContattiVO> al = UtilsHelper.getInstance()
													   .findTipologiaContattiByDescription(tipologiaContatti);
		
		if (al.size() > 0){
			importerVO.getRisultati_merge().add(tipologiaContatti);
		}
		
	}
	
	protected void checkClasseEnergetica(ClasseEnergeticaXMLModel classeEnergetica){
		
		ArrayList<ClasseEnergeticaVO> al = UtilsHelper.getInstance()
													  .findClasseEnergeticaByDescription(classeEnergetica);
		
		if (al.size() > 0){
			importerVO.getRisultati_merge().add(classeEnergetica);
		}
		
	}

	protected void checkStatoConservativo(StatoConservativoXMLModel statoConservativo){
		
		ArrayList<StatoConservativoVO> al = UtilsHelper.getInstance()
															 .findStatoConservativoByDescription(statoConservativo);
		
		if (al.size() > 0){
			importerVO.getRisultati_merge().add(statoConservativo);
		}
		
	}
	
	protected void checkRiscaldamenti(RiscaldamentiXMLModel riscaldamento){
		
		ArrayList<RiscaldamentiVO> al = UtilsHelper.getInstance().findRiscaldamentiByDescription(riscaldamento);
		
		if (al.size() > 0){
			importerVO.getRisultati_merge().add(riscaldamento);
		}
		
	}
	
	protected void checkTipologieImmobili(TipologieImmobiliXMLModel tipologiaImmobile){
		
		ArrayList<TipologieImmobiliVO> al = UtilsHelper.getInstance().findTipologieImmobiliByDescription(tipologiaImmobile);
		
		if (al.size() > 0){
			importerVO.getRisultati_merge().add(tipologiaImmobile);
		}
		
	}
	
	protected void checkImmobile(ImmobiliXMLModel immobile){
			
		ImmobiliModel im = immobiliHelper.getImmobileByRif(immobile.getRif());
		
		if (im != null){
			importerVO.getRisultati_merge().add(immobile);
		}
		
	}
	
	protected void checkAnagrafiche(AnagraficheXMLModel anagrafica){
		
		ArrayList<Anagrafiche> amlist = anagraficheHelper.getAnagraficheExist(anagrafica);
		
		if (amlist.size() > 0){
			importerVO.getRisultati_merge().add(anagrafica);
		}
		
	}
	
	protected void checkAffitti(AffittiXMLModel affitto){
		
		ImmobiliXMLModel ixmlModel = this.importerVO.getImmobiliSelected().get(affitto.getCodImmobile());
		
		if (ixmlModel != null){
			ArrayList<AffittiModel> amlist = affittiHelper.getAffittiByRifDate(ixmlModel.getRif(), 
									  									       affitto.getDataInizio(), 
											  								   affitto.getDataFine());
			
			if (amlist.size() > 0){
				importerVO.getRisultati_merge().add(affitto);
			}
		}
	}

	protected void checkTipologieStanze(TipologiaStanzeXMLModel tipologiaStanza){
		
		ArrayList<TipologiaStanzeVO> al = UtilsHelper.getInstance()
													 .findTipologieStanzeByDescription(tipologiaStanza);
		
		if (al.size() > 0){
			importerVO.getRisultati_merge().add(tipologiaStanza);
		}
				
	}	

	protected void checkImmagine(ImmagineXMLModel immagine){
			
		ImmobiliXMLModel ixmlModel = this.importerVO.getImmobiliSelected().get(immagine.getCodImmobile());
		if (ixmlModel != null){
			
			ImmobiliModel im = this.immobiliHelper.getImmobileByRif(ixmlModel.getRif());
			
			if (im != null){
				ArrayList<ImmagineVO> alim = UtilsHelper.getInstance().findImmagineByDescription(immagine, im);
				if (alim.size() > 0){
					importerVO.getRisultati_merge().add(immagine);
				}

			}
			
		}
				
	}
	
	protected void checkColloquiAgenti(ColloquiAgentiXMLModel colloquiAgenti){
		
		ColloquiXMLModel cxmlModel = this.importerVO.getColloquiSelected().get(colloquiAgenti.getCodColloquio());
		AgentiXMLModel axmlModel = this.importerVO.getAgentiSelected().get(colloquiAgenti.getCodAgente());
		if (axmlModel != null && cxmlModel != null){
			ArrayList al_agentiVO = UtilsHelper.getInstance().findAgentiByDescription(axmlModel);
				
			Iterator it = al_agentiVO.iterator();
				
			while (it.hasNext()){
					
				AgentiVO agente = (AgentiVO)it.next();
				ArrayList al = this.colloquiHelper.getColloquioAgenteExist(cxmlModel, agente);
				if (al.size() > 0){
					this.importerVO.getRisultati_merge().add(colloquiAgenti);
					break;
				}
			}
		}
	}

	protected void checkColloquiAnagrafiche(ColloquiAnagraficheXMLModel colloquiAnagrafiche){
		
		ColloquiXMLModel cxmlModel = this.importerVO.getColloquiSelected().get(colloquiAnagrafiche.getCodColloquio());
		AnagraficheXMLModel axmlModel = this.importerVO.getAnagraficheSelected().get(colloquiAnagrafiche.getCodAnagrafica());
		if (axmlModel != null && cxmlModel != null){
			ArrayList<Anagrafiche> al_anagraficheModel = this.anagraficheHelper.getAnagraficheExist(axmlModel);
				
			Iterator it = al_anagraficheModel.iterator();
				
			while (it.hasNext()){
					
				AnagraficheVO anagrafica = (AnagraficheVO)it.next();
				ArrayList al = this.colloquiHelper.getColloquioAnagraficheExist(cxmlModel, anagrafica);
				if (al.size() > 0){
					this.importerVO.getRisultati_merge().add(colloquiAnagrafiche);
					break;
				}
			}
		}
	}

	protected void checkStanzeImmobili(StanzeImmobiliXMLModel stanzeImmobili){
		
		ImmobiliXMLModel ixmlModel = this.importerVO.getImmobiliSelected().get(stanzeImmobili.getCodImmobile());
		TipologiaStanzeXMLModel tsxmlModel = this.importerVO.getTipologieStanzeSelected().get(stanzeImmobili.getCodTipologiaStanza());
		
		if (ixmlModel != null && tsxmlModel != null){
			ImmobiliModel im = this.immobiliHelper.getImmobileByRif(ixmlModel.getRif());
			ArrayList al_ts = UtilsHelper.getInstance().findTipologieStanzeByDescription(tsxmlModel);
			
			if (im != null && al_ts.size() > 0){
	
				for (Object object : al_ts) {
					ArrayList al = this.immobiliHelper.getStanzaImmobileByCodImmobileCodTipologia(im.getCodImmobile(),
																				   				 ((TipologiaStanzeVO)object).getCodTipologiaStanza());
					if (al.size() > 0){
						
						this.importerVO.getRisultati_merge().add(stanzeImmobili);
						break;
											
					}
					
				}
				
			}
		}
		
	}

	protected void checkDatiCatastali(DatiCatastaliXMLModel datiCatastali){
		
		ImmobiliXMLModel ixmlModel = this.importerVO.getImmobiliSelected().get(datiCatastali.getCodImmobile());
		if (ixmlModel != null){
			
			ImmobiliModel im = this.immobiliHelper.getImmobileByRif(ixmlModel.getRif());
			
			if (im != null){
				
				if (this.immobiliHelper.getDatiCatastli(im.getCodImmobile(), datiCatastali.getFoglio(), datiCatastali.getParticella(), 
														datiCatastali.getSubalterno(), datiCatastali.getCategoria(), datiCatastali.getRendita(), 
														datiCatastali.getRedditoDomenicale(), datiCatastali.getRedditoAgricolo(), datiCatastali.getDimensione()) != null){
					this.importerVO.getRisultati_merge().add(datiCatastali);
					
				}				
				
			}
			
		}
		
	}
	
	protected void checkAllegatiImmobili(AllegatiImmobiliXMLModel allegatiImmobili){
	
		ImmobiliXMLModel ixmlModel = this.importerVO.getImmobiliSelected().get(allegatiImmobili.getCodImmobile());
		if (ixmlModel != null){
			
			ImmobiliModel im = this.immobiliHelper.getImmobileByRif(ixmlModel.getRif());
			
			if (im != null){
				ArrayList al = this.immobiliHelper.getAllegatiImmobileByCodImmobile(im.getCodImmobile());
				if (al.size() > 0){
					Iterator it = al.iterator();
					while (it.hasNext()) {
						AllegatiImmobiliVO object = (AllegatiImmobiliVO) it.next();
						if (object.getCodImmobile().intValue() == allegatiImmobili.getCodImmobile().intValue()){
							this.importerVO.getRisultati_merge().add(allegatiImmobili);
							break;
						}
					}
					
					
				}				
				
			}
			
		}
		
	}
	
	protected void checkAbbinamenti(AbbinamentiXMLModel abbinamenti){
		
		ImmobiliXMLModel ixmlModel = this.importerVO.getImmobiliSelected().get(abbinamenti.getCodImmobile());
		AnagraficheXMLModel axmlModel = this.importerVO.getAnagraficheSelected().get(abbinamenti.getCodAnagrafica());
		
		if (ixmlModel != null && axmlModel != null){
			
			ArrayList<Immobili> al_im = this.immobiliHelper.getImmobiliExist(ixmlModel);
			ArrayList<Anagrafiche> al_anagrafiche = this.anagraficheHelper.getAnagraficheExist(axmlModel);
			
			if (al_im.size() > 0 && al_anagrafiche.size() > 0){
				this.importerVO.getRisultati_merge().add(abbinamenti);
			}
			
		}
		
	}
	
	protected void checkAffittiAllegati(AffittiAllegatiXMLModel affittiAllegati){
	
		AffittiXMLModel aXMLModel = this.importerVO.getAffittiSelected().get(affittiAllegati.getCodAffitto());
		
		if (aXMLModel != null){

			ImmobiliXMLModel iselected = this.importerVO.getImmobiliSelected().get(aXMLModel.getCodImmobile());
			
			if (iselected != null ){
				ArrayList <AffittiModel> al = this.affittiHelper.getAffittiByRifDate(iselected.getRif(), aXMLModel.getDataInizio(), aXMLModel.getDataFine());
				if (al.size() > 0){
					AffittiAllegatiVO aaVO = this.affittiHelper.getAffittiAllegatiExist(al.get(0).getCodAffitti(), 
																						affittiAllegati.getNome());
				
					if (aaVO != null){
						this.importerVO.getRisultati_merge().add(affittiAllegati);
					}
					
				}
			}
			
		}
		
	}

	protected void checkColloquiAllegati(AllegatiColloquiXMLModel allegatiColloquio){
		ColloquiXMLModel colloquio = importerVO.getColloquiSelected().get(allegatiColloquio.getCodColloquio());
		if (colloquio != null){
			
			ArrayList<ColloquiModel> cm = colloquiHelper.getColloquiExist(colloquio);
			if (cm.size() > 0){
								
				ArrayList<AllegatiColloquiVO> al_allegati = colloquiHelper.getColloquioAllegatiByCodColloquio(cm.get(0).getCodColloquio());
					Iterator<AllegatiColloquiVO> it_allegati = al_allegati.iterator();
					while (it_allegati.hasNext()) {
						AllegatiColloquiVO allegatoColloqui_db = it_allegati.next();
						if (allegatoColloqui_db.getNome().equalsIgnoreCase(allegatiColloquio.getNome())){
							this.importerVO.getRisultati_merge().add(allegatiColloquio);
							break;
						}						
					}
			}
			
		}

	}
	
	protected void checkAffittiAnagrafiche(AffittiAnagraficheXMLModel affittiAnagrafiche){
		
		AffittiXMLModel aXMLModel = this.importerVO.getAffittiSelected().get(affittiAnagrafiche.getCodAffitto());
		
		if (aXMLModel != null){

			AnagraficheXMLModel aselected = this.importerVO.getAnagraficheSelected().get(affittiAnagrafiche.getCodAnagrafica());
			ImmobiliXMLModel iselected = this.importerVO.getImmobiliSelected().get(aXMLModel.getCodImmobile());
			
			if (aselected != null && iselected != null){
				
				ArrayList <AffittiModel> al_affitti = this.affittiHelper.getAffittiByRifDate(iselected.getRif(), aXMLModel.getDataInizio(), aXMLModel.getDataFine());
				ArrayList <Anagrafiche> al_anagrafiche = this.anagraficheHelper.getAnagraficheExist(aselected);
				
				if (al_affitti.size() > 0 && al_anagrafiche.size() > 0){
					AffittiAnagraficheVO aaVO = this.affittiHelper
												 	.getAffittiAnagraficheExist(al_affitti.get(0).getCodAffitti(), 
														 					 	al_anagrafiche.get(0).getCodAnagrafica());
				
					if (aaVO != null){
						this.importerVO.getRisultati_merge().add(affittiAnagrafiche);
					}
					
				}
			}
			
		}
		
	}

	protected void checkAffittiRate(AffittiRateXMLModel affittirate){
		
		AffittiXMLModel aXMLModel = this.importerVO.getAffittiSelected().get(affittirate.getCodAffitto());
		
		if (aXMLModel != null){

			AnagraficheXMLModel aselected = this.importerVO.getAnagraficheSelected().get(affittirate.getCodAnagrafica());
			ImmobiliXMLModel iselected = this.importerVO.getImmobiliSelected().get(aXMLModel.getCodImmobile());
			
			if (aselected != null && iselected != null){
				
				ArrayList <AffittiModel> al_affitti = this.affittiHelper.getAffittiByRifDate(iselected.getRif(), aXMLModel.getDataInizio(), aXMLModel.getDataFine());
				ArrayList <Anagrafiche> al_anagrafiche = this.anagraficheHelper.getAnagraficheExist(aselected);				
				
				if (al_affitti.size() > 0 && al_anagrafiche.size() > 0){
					AffittiRateVO arVO = new AffittiRateVO(affittirate);
					arVO.setCodAffitto(al_affitti.get(0).getCodAffitti());
					arVO.setCodAnagrafica(al_anagrafiche.get(0).getCodAnagrafica());
					ArrayList<AffittiRateVO> al_arVO = this.affittiHelper.getAffittiRateExist(arVO);
				
					if (al_arVO.size() > 0){
						this.importerVO.getRisultati_merge().add(affittirate);
					}
					
				}
			}
			
		}
		
	}

	protected void checkAffittiSpese(AffittiSpeseXMLModel affittiSpese){
		
		AffittiXMLModel aXMLModel = this.importerVO.getAffittiSelected().get(affittiSpese.getCodAffitto());
		
		if (aXMLModel != null){

			AnagraficheXMLModel aselected = this.importerVO.getAnagraficheSelected().get(affittiSpese.getCodAnagrafica());
			ImmobiliXMLModel iselected = this.importerVO.getImmobiliSelected().get(aXMLModel.getCodImmobile());
			
			if (aselected != null && iselected != null){
				
				ArrayList <AffittiModel> al_affitti = this.affittiHelper.getAffittiByRifDate(iselected.getRif(), aXMLModel.getDataInizio(), aXMLModel.getDataFine());
				ArrayList <Anagrafiche> al_anagrafiche = this.anagraficheHelper.getAnagraficheExist(aselected);				
				
				if (al_affitti.size() > 0 && al_anagrafiche.size() > 0){
					AffittiSpeseVO arVO = new AffittiSpeseVO(affittiSpese);
					arVO.setCodAffitto(al_affitti.get(0).getCodAffitti());
					arVO.setCodAnagrafica(al_anagrafiche.get(0).getCodAnagrafica());
					ArrayList<AffittiSpeseVO> al_asVO = this.affittiHelper.getAffittiSpeseExist(arVO);
				
					if (al_asVO.size() > 0){
						this.importerVO.getRisultati_merge().add(affittiSpese);
					}
					
				}
			}
			
		}
		
	}
	
	protected void checkClassiClienti(ClassiClientiXMLModel classiClienti){
		
		ArrayList al = UtilsHelper.getInstance().findClassiClientiByDescription(classiClienti);
		
		if (al.size() > 0){
			this.importerVO.getRisultati_merge().add(classiClienti);
		}
		
	}

	protected void checkCriteriRicerca(ColloquiCriteriRicercaXMLModel criterioRicerca){
		
		ColloquiXMLModel c = this.importerVO.getColloquiSelected().get(criterioRicerca.getCodColloquio());
		
		if (c != null){
			CriteriRicercaModel crm = new CriteriRicercaModel(criterioRicerca);
			ArrayList al_cr = UtilsHelper.getInstance().findCriteriRicercaByDescription(crm, c);
			
			if (al_cr.size() > 0){
				this.importerVO.getRisultati_merge().add(criterioRicerca);
			}
		}
		
	}
	
	protected void checkEntita(EntityXMLModel entity){
		
		CampiPersonaliHelper cph = new CampiPersonaliHelper();
		
		EntityModel em = cph.getEntityByClassName(entity.getClassName());
		
		if (em != null){
			this.importerVO.getRisultati_merge().add(entity);
		}
		
	}
	
	protected void checkAttributo(AttributeXMLModel attribute){
		
		EntityXMLModel em = this.importerVO.getEntitaSelected().get(attribute.getIdClassEntity());
		
		if (em != null){

			AttributeModel am = campiPersonaliHelper.getAttributeByClassNameAttributeNameAttributeType(em.getClassName(), 
					  																  				   attribute.getAttributeName(), 
					  																  				   attribute.getFieldType());

			if (am != null){
				this.importerVO.getRisultati_merge().add(new AttributeXMLModel(am));
			}
			
		}
		
	}
	
	protected void checkValoreAttributo(AttributeValueXMLModel attributeValue){
			
		AttributeXMLModel am = this.importerVO.getAttributiSelected().get(attributeValue.getIdField());
							
		if (am != null){
			EntityXMLModel em = this.importerVO.getEntitaSelected().get(am.getIdClassEntity());
			AttributeModel dbam = campiPersonaliHelper.getAttributeByClassNameAttributeNameAttributeType(em.getClassName(), 
																  										 am.getAttributeName(),
																  										 am.getFieldType());
						
			if (dbam != null){
				
				Object idObject = null;
				if (em.getClassName().equalsIgnoreCase(ImmobiliVO.class.getName())){
					ImmobiliXMLModel immobile = this.importerVO.getImmobiliSelected().get(attributeValue.getIdObject());
					ImmobiliModel im = immobiliHelper.getImmobileByRif(immobile.getRif());
					if (im != null){
						idObject = im.getCodImmobile();
					}
				}
				
				if (em.getClassName().equalsIgnoreCase(AnagraficheVO.class.getName())){
					AnagraficheXMLModel anagrafica = this.importerVO.getAnagraficheSelected().get(attributeValue.getIdObject());
					idObject = anagraficheHelper.getAnagraficheExist(anagrafica);
				}
				
				if (em.getClassName().equalsIgnoreCase(ColloquiVO.class.getName())){
					
					ArrayList<Colloqui> al = new ArrayList<Colloqui>();
					
					ColloquiXMLModel colloqui = importerVO.getColloquiSelected().get(attributeValue.getIdObject());
					HashMap<Integer,AnagraficheXMLModel> anagraficheSelected = importerVO.getAnagraficheSelected();
					Iterator<Entry<Integer, AnagraficheXMLModel>> it = anagraficheSelected.entrySet().iterator();
					
					boolean find = false;
					while (it.hasNext()) {
						
						Entry<Integer, AnagraficheXMLModel> anagrafica = it.next();
						ArrayList<Anagrafiche> al_anagrafiche = this.anagraficheHelper.getAnagraficheExist(anagrafica.getValue());
						
						for (Anagrafiche anagraficaModel : al_anagrafiche) {
							al.addAll(colloquiHelper.getColloquiExist(colloqui, anagraficaModel));
						}
							
					}
						
					idObject = al;
							
				}

				if (em.getClassName().equalsIgnoreCase(AffittiVO.class.getName())){
					
					ArrayList<ColloquiModel> al = new ArrayList<ColloquiModel>();
					
					AffittiXMLModel affitti = importerVO.getAffittiSelected().get(attributeValue.getIdObject());
					ImmobiliXMLModel ixmlModel = this.importerVO.getImmobiliSelected().get(affitti.getCodImmobile());
					
					if (ixmlModel != null){
						idObject = affittiHelper.getAffittiByRifDate(ixmlModel.getRif(), 
												  					 affitti.getDataInizio(), 
														  			 affitti.getDataFine());
					}
							
				}

				if (idObject != null){
					
					AttributeValueModel avm = null;
					if (idObject instanceof Integer){
						avm = campiPersonaliHelper.getAttributeValueByIdFieldIdObject(dbam.getIdAttribute(), 
																										  (Integer)idObject);
					}
					if (idObject instanceof ArrayList){
						
						for (Object item : (ArrayList)idObject) {
							if (item instanceof AnagraficheModel){
								avm = campiPersonaliHelper.getAttributeValueByIdFieldIdObject(dbam.getIdAttribute(), 
										  													  ((AnagraficheModel)item).getCodAnagrafica());
								if (avm != null){
									break;
								}
							}else if (item instanceof ColloquiModel){
								avm = campiPersonaliHelper.getAttributeValueByIdFieldIdObject(dbam.getIdAttribute(), 
										  													  ((ColloquiModel)item).getCodColloquio());
								if (avm != null){
									break;
								}
							}else{
								avm = campiPersonaliHelper.getAttributeValueByIdFieldIdObject(dbam.getIdAttribute(), 
										  													  ((AffittiModel)item).getCodAffitti());
								if (avm != null){
									break;
								}																
							}
						}						
					}
					
					if (avm != null){				
						this.importerVO.getRisultati_merge().add(attributeValue);
					}
					
				}
			}
		}	
	}
	
}
