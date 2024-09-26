package winkhouse.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javolution.util.FastList;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.stream.XMLStreamException;
import winkhouse.Activator;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.dao.ImmobiliPropietariDAO;
import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.dao.StatoConservativoDAO;
import winkhouse.dao.TipologiaContattiDAO;
import winkhouse.dao.TipologiaStanzeDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.engine.search.SearchEngineAnagrafiche;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ColloquiModelAnagraficaCollection;
import winkhouse.model.ColloquiModelRicercaCollection;
import winkhouse.model.ContattiModel;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.model.ImmagineModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.StanzeImmobiliModel;
import winkhouse.model.winkcloud.CloudQueryTypes;
import winkhouse.model.winkcloud.RicercaModel;
import winkhouse.orm.Classicliente;
import winkhouse.orm.Classienergetiche;
import winkhouse.orm.Colloquicriteriricerca;
import winkhouse.util.ZipUtils;
import winkhouse.vo.ImmagineVO;
import winkhouse.vo.ImmobiliPropietariVO;
import winkhouse.xmlser.helper.XMLExportHelper;
import winkhouse.xmlser.models.xml.AnagraficheXMLModel;
import winkhouse.xmlser.models.xml.ClasseEnergeticaXMLModel;
import winkhouse.xmlser.models.xml.ClassiClientiXMLModel;
import winkhouse.xmlser.models.xml.ColloquiAgentiXMLModel;
import winkhouse.xmlser.models.xml.ColloquiAnagraficheXMLModel;
import winkhouse.xmlser.models.xml.ColloquiXMLModel;
import winkhouse.xmlser.models.xml.ContattiXMLModel;
import winkhouse.xmlser.models.xml.ImmagineXMLModel;
import winkhouse.xmlser.models.xml.ImmobiliPropietariXMLModel;
import winkhouse.xmlser.models.xml.ImmobiliXMLModel;
import winkhouse.xmlser.models.xml.RiscaldamentiXMLModel;
import winkhouse.xmlser.models.xml.StanzeImmobiliXMLModel;
import winkhouse.xmlser.models.xml.StatoConservativoXMLModel;
import winkhouse.xmlser.models.xml.TipologiaContattiXMLModel;
import winkhouse.xmlser.models.xml.TipologiaStanzeXMLModel;
import winkhouse.xmlser.models.xml.TipologieImmobiliXMLModel;

public class WinkCloudHelper {
	
	public final static String XML_TAG_MOBILE ="ricerca";
	private ArrayList<RicercaModel> retval = null;
	private ArrayList<Colloquicriteriricerca> al = null;
	
	public WinkCloudHelper() {
		
	}
	
	public void fillDatiBase(HashMap tipologieImmobili,
							 HashMap tipologieContatti,
							 HashMap tipologieStanze,
							 HashMap riscaldamenti,
							 HashMap statoConservativo,
							 HashMap classiClienti,
							 HashMap classiEnergetiche){
		
		tipologieImmobili.clear();
		TipologieImmobiliDAO tiDAO = new TipologieImmobiliDAO();
		ArrayList<TipologieImmobiliXMLModel> al = tiDAO.list(TipologieImmobiliXMLModel.class.getName());
		for (TipologieImmobiliXMLModel tipologieImmobiliXMLModel : al) {
			tipologieImmobili.put(tipologieImmobiliXMLModel.getCodTipologiaImmobile(), tipologieImmobiliXMLModel);
		}
		
		tipologieContatti.clear();
		TipologiaContattiDAO tcDAO = new TipologiaContattiDAO();
		ArrayList<TipologiaContattiXMLModel> al_tc = tcDAO.list(TipologiaContattiXMLModel.class.getName());
		for (TipologiaContattiXMLModel tipologieContattiXMLModel : al_tc) {
			tipologieContatti.put(tipologieContattiXMLModel.getCodTipologiaContatto(), tipologieContattiXMLModel);
		}

		tipologieStanze.clear();
		TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();
		ArrayList<TipologiaStanzeXMLModel> al_ts = tsDAO.list(TipologiaStanzeXMLModel.class.getName());
		for (TipologiaStanzeXMLModel tipologieStanzeXMLModel : al_ts) {
			tipologieStanze.put(tipologieStanzeXMLModel.getCodTipologiaStanza(), tipologieStanzeXMLModel);
		}

		riscaldamenti.clear();
		RiscaldamentiDAO rDAO = new RiscaldamentiDAO();
		ArrayList<RiscaldamentiXMLModel> al_r = rDAO.list(RiscaldamentiXMLModel.class.getName());
		for (RiscaldamentiXMLModel riscaldamentiXMLModel : al_r) {
			riscaldamenti.put(riscaldamentiXMLModel.getCodRiscaldamento(), riscaldamentiXMLModel);
		}

		statoConservativo.clear();
		StatoConservativoDAO scDAO = new StatoConservativoDAO();
		ArrayList<StatoConservativoXMLModel> al_sc = scDAO.list(StatoConservativoXMLModel.class.getName());
		for (StatoConservativoXMLModel statoConservativoXMLModel : al_sc) {
			statoConservativo.put(statoConservativoXMLModel.getCodStatoConservativo(), statoConservativoXMLModel);
		}

		classiClienti.clear();
		ClassiClientiDAO ccDAO = new ClassiClientiDAO();
		ArrayList<Classicliente> al_cc = ccDAO.list(ClassiClientiXMLModel.class.getName());
		
		for (Classicliente classiClientiXMLModel : al_cc) {
			classiClienti.put(classiClientiXMLModel.getCodClasseCliente(), classiClientiXMLModel);
		}

		classiEnergetiche.clear();
		ClassiEnergeticheDAO ceDAO = new ClassiEnergeticheDAO();
		ArrayList<Classienergetiche> al_ce = ceDAO.listClassiEnergetiche(ClasseEnergeticaXMLModel.class.getName());
		for (Classienergetiche classeEnergeticaXMLModel : al_ce) {
			classiEnergetiche.put(classeEnergeticaXMLModel.getCodClasseEnergetica(), classeEnergeticaXMLModel);
		}

	}
	
	public boolean exportImmobili(ArrayList immobili,String zipdir){
		
		HashMap hm_immobili = new HashMap() ;
		HashMap hm_stanze = new HashMap();
		//HashMap hm_colloqui_immobili = new HashMap();
//		HashMap hm_colloqui_anagrafiche = new HashMap();
		HashMap hm_colloqui_agenti = new HashMap();
		HashMap hm_contatti_anagrafiche = new HashMap();
		HashMap hm_immagini = new HashMap();
		HashMap hm_anagrafiche_propietarie = new HashMap();
		HashMap hm_anagrafiche = new HashMap();
		HashMap hm_colloqui = new HashMap();
		HashMap hm_contatti = new HashMap();
		HashMap hm_anagrafiche_colloqui = new HashMap();
		HashMap hm_agenti_colloqui = new HashMap();
		
		HashMap tipologieImmobili = new HashMap();
		HashMap tipologieContatti = new HashMap();
		HashMap tipologieStanze = new HashMap();
		HashMap riscaldamenti = new HashMap();
		HashMap statoConservativo = new HashMap();
		HashMap classiClienti = new HashMap();
		HashMap classiEnergetiche = new HashMap();
		
		ImmobiliPropietariDAO ip = new ImmobiliPropietariDAO();		
		
		XMLExportHelper xml_eh = new XMLExportHelper();				
		
		try{
			
			fillDatiBase(tipologieImmobili, tipologieContatti, tipologieStanze, riscaldamenti, statoConservativo, classiClienti, classiEnergetiche);
			
			File zipdirfile = new File(zipdir);
			
			zipdirfile.mkdirs();
	
			if (immobili.size() > 0){
				
				Iterator it = immobili.iterator();	
				while (it.hasNext()) {
					
					ImmobiliModel object = (ImmobiliModel) it.next();						
					hm_immobili.put(object.getCodImmobile(),new ImmobiliXMLModel(object));
					
					ArrayList<StanzeImmobiliModel> stanzeModels = object.getStanze();
					for (StanzeImmobiliModel stanzeImmobiliModel : stanzeModels) {
						hm_stanze.put(stanzeImmobiliModel.getCodStanzeImmobili(), new StanzeImmobiliXMLModel(stanzeImmobiliModel));
					}
					
					ArrayList<ColloquiModel> al_colloqui_immobile = object.getColloquiVisiteReport();
					for (ColloquiModel colloquiModel : al_colloqui_immobile) {
						
						hm_colloqui.put(colloquiModel.getCodColloquio(), new ColloquiXMLModel(colloquiModel));
						
						ArrayList<ColloquiAgentiModel_Age> al_colloqui_agenti = colloquiModel.getAgenti();
						for (ColloquiAgentiModel_Age colloquiAgentiModel_Age : al_colloqui_agenti) {
							
							ColloquiAgentiXMLModel caXML = new ColloquiAgentiXMLModel(colloquiAgentiModel_Age);
							hm_colloqui_agenti.put(caXML.getCodColloquioAgenti(), caXML);
							
						}
						
						ArrayList<ColloquiAnagraficheModel_Ang> al_colloqui_anagrafiche = colloquiModel.getAnagrafiche();
						for (ColloquiAnagraficheModel_Ang colloquiAnagraficheModel_Age : al_colloqui_anagrafiche) {
							
							ColloquiAnagraficheXMLModel caXML = new ColloquiAnagraficheXMLModel(colloquiAnagraficheModel_Age);
							hm_anagrafiche_colloqui.put(caXML.getCodColloquioAnagrafiche(), caXML);
							
						}
						
					}				
					
					ArrayList<AnagraficheModel> anagrafiche_propietarie = object.getAnagrafichePropietarie();
					for (AnagraficheModel object2 : anagrafiche_propietarie) {
						fillAnagraficaExportMaps(object2, hm_anagrafiche, hm_colloqui, hm_contatti, hm_anagrafiche_colloqui, hm_agenti_colloqui);	
					}
					
					ArrayList<ImmobiliPropietariVO> al_immobili_propietari = ip.getImmobiliPropietariByCodImmobile(object.getCodImmobile());
					for (ImmobiliPropietariVO immobiliPropietariVO : al_immobili_propietari) {
						ImmobiliPropietariXMLModel ipXmlModel = new ImmobiliPropietariXMLModel(immobiliPropietariVO);
						hm_anagrafiche_propietarie.put(String.valueOf(ipXmlModel.getCodAnagrafica()) + String.valueOf(ipXmlModel.getCodImmobile()), ipXmlModel);
					}
					
					ArrayList immagini = object.getImmagini();
					for (Object object2 : immagini) {
						hm_immagini.put(((ImmagineModel)object2).getCodImmagine(), new ImmagineXMLModel((ImmagineModel)object2)); 
					}
				}
				
				xml_eh.exportSelection(hm_anagrafiche_colloqui, zipdir + File.separator + "colloqui_anagrafiche.xml");
				xml_eh.exportSelection(hm_anagrafiche, zipdir + File.separator + "anagrafiche.xml");
				xml_eh.exportSelection(hm_contatti, zipdir + File.separator + "contatti.xml");
	//			xml_eh.exportSelection(hm_colloqui_anagrafiche, zipdir + File.separator + "colloqui_anagrafiche.xml");
				xml_eh.exportSelection(hm_colloqui_agenti, zipdir + File.separator + "colloqui_agenti.xml");
				xml_eh.exportSelection(hm_colloqui, zipdir + File.separator + "colloqui.xml");
				xml_eh.exportSelection(hm_immobili, zipdir + File.separator+"immobili.xml");
				xml_eh.exportSelection(hm_stanze, zipdir + File.separator+ "stanzeimmobili.xml");
				xml_eh.exportSelection(hm_anagrafiche_propietarie,zipdir + File.separator + "immobilipropietari.xml");
				
				if (xml_eh.exportSelection(hm_immagini,zipdir + File.separator + "immagine.xml")){
					
					Iterator it_immagini = hm_immagini.values().iterator();
					while (it_immagini.hasNext()) {
						ImmagineVO ivo = (ImmagineVO) it_immagini.next();
						xml_eh.saveFileImmagini(ivo, zipdir + File.separator);
					}
					
				}
				
				xml_eh.exportSelection(tipologieImmobili,zipdir + File.separator +  "tipologiaimmobili.xml");
				xml_eh.exportSelection(tipologieContatti,zipdir + File.separator +  "tipologiacontatti.xml");
				xml_eh.exportSelection(tipologieStanze,zipdir + File.separator +  "tipologiastanze.xml");
				xml_eh.exportSelection(riscaldamenti,zipdir + File.separator +  "riscaldamenti.xml");
				xml_eh.exportSelection(statoConservativo,zipdir + File.separator +  "statoconservativo.xml");
				xml_eh.exportSelection(classiClienti,zipdir + File.separator +  "classiclienti.xml");
				xml_eh.exportSelection(classiEnergetiche,zipdir + File.separator +  "classeenergetica.xml");
				
			}else{
				return false;
			}
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	public void fillAnagraficaExportMaps(AnagraficheModel anagrafica,
										 HashMap hm_anagrafiche,
										 HashMap hm_colloqui,
										 HashMap hm_contatti,
										 HashMap hm_anagrafiche_colloqui,
										 HashMap hm_agenti_colloqui) throws NullPointerException{
		
		if (hm_anagrafiche == null){
			throw new NullPointerException("HashMap hm_anagrafiche � null");
		}

		if (hm_colloqui == null){
			throw new NullPointerException("HashMap hm_colloqui � null");
		}
		
		if (hm_contatti == null){
			throw new NullPointerException("HashMap hm_contatti � null");
		}
						 
		if (hm_anagrafiche_colloqui == null){
			throw new NullPointerException("HashMap hm_anagrafiche_colloqui � null");
		}

		if (hm_agenti_colloqui == null){
			throw new NullPointerException("HashMap hm_agenti_colloqui � null");
		}

		AnagraficheXMLModel axmModel = new AnagraficheXMLModel(anagrafica);
		hm_anagrafiche.put(axmModel.getCodAnagrafica(), axmModel);
		
		ArrayList<ContattiModel> al_contatti = anagrafica.getContatti();
		for (ContattiModel cm : al_contatti) {
			ContattiXMLModel cXmlModel = new ContattiXMLModel(cm);
			hm_contatti.put(cXmlModel.getCodContatto(), cXmlModel);
		}
		
		ArrayList al_colloqui = anagrafica.getColloqui();
		for (Object object : al_colloqui) {			
			
			if (object instanceof ColloquiModelAnagraficaCollection) {
				
				ColloquiModelAnagraficaCollection cmac = (ColloquiModelAnagraficaCollection) object;
				ArrayList<ColloquiModel> al_cm = cmac.getColloqui();
				
				for (ColloquiModel colloquiModel : al_cm) {
					
					ColloquiXMLModel colloquiXmlModel = new ColloquiXMLModel(colloquiModel);
					hm_colloqui.put(colloquiXmlModel.getCodColloquio(), colloquiXmlModel);
					
					ArrayList<ColloquiAgentiModel_Age> al_agenti =  colloquiModel.getAgenti();
					for (ColloquiAgentiModel_Age colloquiAgentiModel_Age : al_agenti) {
						ColloquiAgentiXMLModel caXMLModel = new ColloquiAgentiXMLModel(colloquiAgentiModel_Age);
						hm_agenti_colloqui.put(caXMLModel.getCodColloquioAgenti(), caXMLModel);						
					}
					
					ArrayList<ColloquiAnagraficheModel_Ang> al_colloqui_agenti = colloquiModel.getAnagrafiche();
					for (ColloquiAnagraficheModel_Ang colloquiAgentiModel_Age : al_colloqui_agenti) {
						ColloquiAnagraficheXMLModel caXmlModel = new ColloquiAnagraficheXMLModel(colloquiAgentiModel_Age);
						hm_anagrafiche_colloqui.put(caXmlModel.getCodColloquioAnagrafiche(), caXmlModel);
					}
					
				}
								
			}
			
			if (object instanceof ColloquiModelRicercaCollection){
				
				ColloquiModelRicercaCollection cmrc = (ColloquiModelRicercaCollection) object;
				ArrayList<ColloquiModel> al_cm = cmrc.getColloquiRicerca();
				
				for (ColloquiModel colloquiModel : al_cm) {
					
					ColloquiXMLModel colloquiXmlModel = new ColloquiXMLModel(colloquiModel);
					hm_colloqui.put(colloquiXmlModel.getCodColloquio(), colloquiXmlModel);
					
					ArrayList<ColloquiAgentiModel_Age> al_agenti =  colloquiModel.getAgenti();
					for (ColloquiAgentiModel_Age colloquiAgentiModel_Age : al_agenti) {
						ColloquiAgentiXMLModel caXMLModel = new ColloquiAgentiXMLModel(colloquiAgentiModel_Age);
						hm_agenti_colloqui.put(caXMLModel.getCodColloquioAgenti(), caXMLModel);						
					}
					
					ArrayList<ColloquiAnagraficheModel_Ang> al_colloqui_agenti = colloquiModel.getAnagrafiche();
					for (ColloquiAnagraficheModel_Ang colloquiAgentiModel_Age : al_colloqui_agenti) {
						ColloquiAnagraficheXMLModel caXmlModel = new ColloquiAnagraficheXMLModel(colloquiAgentiModel_Age);
						hm_anagrafiche_colloqui.put(caXmlModel.getCodColloquioAnagrafiche(), caXmlModel);
					}
					
				}
				
			}
			
		}
				 	  
	}

	public boolean exportAnagrafiche(){
		return true;
	}

	public ArrayList parseMobileRequest(String filename,String unzipdir){
		
		ArrayList returnvalue = new ArrayList();
		
		ZipUtils zu = new ZipUtils();
		
		if (zu.unZip4jArchivio(filename, Activator.getDefault()
												  .getStateLocation()
												  .toFile().toString() + File.separator+"cloudsearch" + File.separator + unzipdir)){
			File f = new File(Activator.getDefault()
					 				   .getStateLocation()
					 				   .toFile().toString() + File.separator+"cloudsearch" + File.separator + unzipdir);
			File[]fs = f.listFiles();
			al = new ArrayList<Colloquicriteriricerca>();
			
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].getName().endsWith(".xml")){
					retval = loadFromFile(fs[i], XML_TAG_MOBILE, RicercaModel.class);
					Iterator<RicercaModel> it = retval.iterator();
					
					while (it.hasNext()) {
						RicercaModel type = it.next();
						al.add(type.toCriteriRicercaModel());
					}
					if (al.size()>0){
						al.get(al.size()-1).setLogicaloperator("");
					}
					if (retval.size() > 0 && retval.get(0).getSearchType().equalsIgnoreCase("immobili")){
						SearchEngineImmobili sei = new SearchEngineImmobili(al);
						returnvalue.addAll(sei.find());
					}
					if (retval.size() > 0 && retval.get(0).getSearchType().equalsIgnoreCase("anagrafiche")){
						SearchEngineAnagrafiche sea = new SearchEngineAnagrafiche(al);
						returnvalue.addAll(sea.find());
					}
				}
				fs[i].delete();
			}
			f.delete();
			f = null;
		}
		return returnvalue;
		
	}
	
	public CloudQueryTypes getType() throws Exception{
		CloudQueryTypes returnValue = null;
		if (retval == null){
			Exception e = new Exception("parse non chiamato");
			throw e;
		}
		if (retval.get(0).getSearchType().equalsIgnoreCase("immobili")){
			returnValue = CloudQueryTypes.IMMOBILI;
		}
		if (retval.get(0).getSearchType().equalsIgnoreCase("anagrafiche")){
			returnValue = CloudQueryTypes.ANAGRAFICHE;
		} 		
		return returnValue;
	}
	
	public ArrayList<Colloquicriteriricerca> getCriteri()throws Exception{
		if (al == null){
			Exception e = new Exception("parse non chiamato");
			throw e;			
		}
		return al;
	}
	
	public ArrayList loadFromFile(File exportFile,String xmlTag,Class classToDeserialize){
		
		ArrayList returnValue = null;
		XMLObjectReader xmlor = null;
		try {
			
			xmlor = XMLObjectReader.newInstance(new FileInputStream(exportFile));
			xmlor.setBinding(getBinding());
			returnValue = new ArrayList();
			while (xmlor.hasNext()){
				
				Object o = xmlor.read(xmlTag, classToDeserialize);
				returnValue.add(o);
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (xmlor != null){
				try {
					xmlor.close();
				} catch (XMLStreamException e) {
					xmlor = null;
				}
			}			
		}
		
		return returnValue;
	}
	
	private XMLBinding getBinding(){
		
		XMLBinding returnvalue = new XMLBinding();
		
		returnvalue.setAlias(RicercaModel.class, "ricerca");
		returnvalue.setAlias(FastList.class, "lista");
		
		return returnvalue;
		
	}
}