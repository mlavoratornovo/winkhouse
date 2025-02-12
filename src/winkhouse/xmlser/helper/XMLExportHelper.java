package winkhouse.xmlser.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javolution.util.FastList;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.export.helpers.SystemPreferenceHelper;
import winkhouse.export.helpers.UtilsHelper;
import winkhouse.orm.Classienergetiche;
import winkhouse.orm.Riscaldamenti;
import winkhouse.orm.Statoconservativo;
import winkhouse.orm.Tipologiastanze;
import winkhouse.orm.Tipologiecontatti;
import winkhouse.orm.Tipologieimmobili;
import winkhouse.util.ZipUtils;
import winkhouse.vo.AffittiAllegatiVO;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ImmagineVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipiAppuntamentiVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.Activator;
import winkhouse.xmlser.models.DatiBaseModel;
import winkhouse.xmlser.models.xml.AbbinamentiXMLModel;
import winkhouse.xmlser.models.xml.AffittiAllegatiXMLModel;
import winkhouse.xmlser.models.xml.AffittiAnagraficheXMLModel;
import winkhouse.xmlser.models.xml.AffittiRateXMLModel;
import winkhouse.xmlser.models.xml.AffittiSpeseXMLModel;
import winkhouse.xmlser.models.xml.AffittiXMLModel;
import winkhouse.xmlser.models.xml.AgentiAppuntamentiXMLModel;
import winkhouse.xmlser.models.xml.AgentiXMLModel;
import winkhouse.xmlser.models.xml.AllegatiColloquiXMLModel;
import winkhouse.xmlser.models.xml.AllegatiImmobiliXMLModel;
import winkhouse.xmlser.models.xml.AnagraficheAppuntamentiXMLModel;
import winkhouse.xmlser.models.xml.AnagraficheXMLModel;
import winkhouse.xmlser.models.xml.AppuntamentiXMLModel;
import winkhouse.xmlser.models.xml.AttributeValueXMLModel;
import winkhouse.xmlser.models.xml.AttributeXMLModel;
import winkhouse.xmlser.models.xml.ClasseEnergeticaXMLModel;
import winkhouse.xmlser.models.xml.ClassiClientiXMLModel;
import winkhouse.xmlser.models.xml.ColloquiAgentiXMLModel;
import winkhouse.xmlser.models.xml.ColloquiAnagraficheXMLModel;
import winkhouse.xmlser.models.xml.ColloquiCriteriRicercaXMLModel;
import winkhouse.xmlser.models.xml.ColloquiXMLModel;
import winkhouse.xmlser.models.xml.ContattiXMLModel;
import winkhouse.xmlser.models.xml.CriteriRicercaXMLModel;
import winkhouse.xmlser.models.xml.DatiCatastaliXMLModel;
import winkhouse.xmlser.models.xml.EntityXMLModel;
import winkhouse.xmlser.models.xml.GCalendarXMLModel;
import winkhouse.xmlser.models.xml.ImmagineXMLModel;
import winkhouse.xmlser.models.xml.ImmobiliPropietariXMLModel;
import winkhouse.xmlser.models.xml.ImmobiliXMLModel;
import winkhouse.xmlser.models.xml.RicercheXMLModel;
import winkhouse.xmlser.models.xml.RiscaldamentiXMLModel;
import winkhouse.xmlser.models.xml.StanzeImmobiliXMLModel;
import winkhouse.xmlser.models.xml.StatoConservativoXMLModel;
import winkhouse.xmlser.models.xml.TipiAppuntamentiXMLModel;
import winkhouse.xmlser.models.xml.TipologiaContattiXMLModel;
import winkhouse.xmlser.models.xml.TipologiaStanzeXMLModel;
import winkhouse.xmlser.models.xml.TipologieImmobiliXMLModel;
import winkhouse.xmlser.wizard.exporter.ExporterWizard;

public class XMLExportHelper {

	public final static int AGENTI_COD = 1;
	public final static int CLASSE_ENERGETICA_COD = 2;
	public final static int RISCALDAMENTO_COD = 3;
	public final static int STATO_CONSERVATIVO_COD = 4;
	public final static int TIPOLOGIA_IMMOBILE_COD = 5;
	public final static int CLASSE_CLIENTE_COD = 6;
	public final static int TIPOLOGIA_STANZE_COD = 7;
	public final static int TIPOLOGIA_CONTATTI_COD = 8;
	
	private String pathExportDir = null;
	private String path = null;
	
 	public XMLExportHelper() {

	}
	
	public boolean exportToPath(String pathToExport, ExporterWizard.ExporterVO objsToExport, boolean zipped) {
		
		
		path = buildexportFolderName();
		
		try {
			pathExportDir = pathToExport + File.separator + path;
			File f = new File(pathToExport + File.separator + path);
			f.mkdirs();			
		} catch (Exception e) {
				MessageDialog.openError(PlatformUI.getWorkbench()
												 .getActiveWorkbenchWindow()
												 .getShell(), 
										"Errore salvataggio esportazione", 
										"Impossibile individuare o creare la cartella di destinazione");
				pathExportDir = null;
				return false;
			
		}
		String base_path = pathToExport + File.separator + path + File.separator;
		
		exportSelection(objsToExport.getHmAbbinamenti(),base_path + "abbinamenti.xml");
		if (exportSelection(objsToExport.getHmAffittiAllegati(),base_path + "allegati_affitti.xml")){
			
			Iterator it = objsToExport.getHmAffittiAllegati().values().iterator();
			while (it.hasNext()) {
				AffittiAllegatiVO aavo = (AffittiAllegatiVO) it.next();
				saveFileAllegatiAffitti(aavo, base_path);
			}
			
		}	
		
		exportSelection(objsToExport.getHmAffittiAnagrafiche(),base_path + "affitti_anagrafiche.xml");		
		exportSelection(objsToExport.getHmAffittiRate(),base_path + "affitti_rate.xml");
		exportSelection(objsToExport.getHmAffittiSpese(),base_path + "affitti_spese.xml");
		exportSelection(objsToExport.getHmAffitti(),base_path + "affitti.xml");
		exportSelection(objsToExport.getHmAgentiAppuntamenti(),base_path + "agenti_appuntamenti.xml");
		
		if (objsToExport.isFlagDatiBaseCompleti()){
			
			HashMap hm = new HashMap();
			
			ArrayList<AgentiVO> al_agenti = UtilsHelper.getInstance().getAllAgenti();
			for (AgentiVO agentiVO : al_agenti) {
				hm.put(agentiVO.getCodAgente(),new AgentiXMLModel(agentiVO));
			}
			exportSelection(hm,base_path + "agenti.xml");
		}else{
			exportSelection(objsToExport.getHmAgenti(),base_path + "agenti.xml");
		}
		
		if (exportSelection(objsToExport.getHmAllegatiColloquio(),base_path + "allegati_colloqui.xml")){
			
			Iterator it = objsToExport.getHmAllegatiColloquio().values().iterator();
			while (it.hasNext()) {
				AllegatiColloquiVO acvo = (AllegatiColloquiVO) it.next();
				saveFileAllegatiColloquio(acvo, base_path);
			}			
			
		}
		
		if (exportSelection(objsToExport.getHmAllegatiImmobili(),base_path + "allegati_immobili.xml")){
			
			Iterator it = objsToExport.getHmAllegatiImmobili().values().iterator();
			while (it.hasNext()) {
				AllegatiImmobiliVO aivo = (AllegatiImmobiliVO) it.next();
				saveFileAllegatiImmobili(aivo, base_path);
			}						
			
		}
		
		exportSelection(objsToExport.getHmAnagrafiche(),base_path + "anagrafiche.xml");
		exportSelection(objsToExport.getHmAppuntamenti(),base_path + "appuntamenti.xml");
		
		if (objsToExport.isFlagDatiBaseCompleti()){
			
			HashMap hm = new HashMap();
			
			ArrayList<Classienergetiche> al_ce = UtilsHelper.getInstance().getAllClasseEnergetica();
			for (Classienergetiche classeEnergeticaVO : al_ce) {
				hm.put(classeEnergeticaVO.getCodClasseEnergetica(),new ClasseEnergeticaXMLModel(classeEnergeticaVO));
			}
			exportSelection(hm,base_path + "classeenergetica.xml");
		}else{
			exportSelection(objsToExport.getHmClassiEnergetiche(),base_path + "classeenergetica.xml");
		}
		
		if (objsToExport.isFlagDatiBaseCompleti()){
			
			HashMap hm = new HashMap();
			
			ArrayList<ClassiClientiVO> al_cc = UtilsHelper.getInstance().getAllClassiClienti();
			for (ClassiClientiVO classiClientiVO : al_cc) {
				hm.put(classiClientiVO.getCodClasseCliente(),new ClassiClientiXMLModel(classiClientiVO));
			}
			exportSelection(hm,base_path + "classiclienti.xml");
		}else{		
			exportSelection(objsToExport.getHmCategorieClienti(),base_path + "classiclienti.xml");
		}
		
		exportSelection(objsToExport.getHmColloquiAgenti(),base_path + "colloqui_agenti.xml");
		exportSelection(objsToExport.getHmColloquiAnagrafica(),base_path + "colloqui_anagrafiche.xml");
		exportSelection(objsToExport.getHmColloquiCriteriRicerca(),base_path + "colloqui_criteriricerca.xml");
		exportSelection(objsToExport.getHmColloqui(),base_path + "colloqui.xml");
		exportSelection(objsToExport.getHmCriteriRicerca(),base_path + "criteriricerca.xml");
		exportSelection(objsToExport.getHmDatiCatastali(),base_path + "daticatastali.xml");
		exportSelection(objsToExport.getHmGCalendar(),base_path + "gcalendar.xml");
		
		if (exportSelection(objsToExport.getHmImmagini(),base_path + "immagine.xml")){
			
			Iterator it = objsToExport.getHmImmagini().values().iterator();
			while (it.hasNext()) {
				ImmagineVO ivo = (ImmagineVO) it.next();
				saveFileImmagini(ivo, base_path);
			}
			
		}
		
		exportSelection(objsToExport.getHmImmobili(),base_path + "immobili.xml");
		exportSelection(objsToExport.getHmRicerche(),base_path + "ricerche.xml");

		if (objsToExport.isFlagDatiBaseCompleti()){
			
			HashMap hm = new HashMap();
			
			ArrayList<Riscaldamenti> al_r = UtilsHelper.getInstance().getAllRiscaldamenti();
			for (Riscaldamenti riscaldamentiVO : al_r) {
				hm.put(riscaldamentiVO.getCodRiscaldamento(),new RiscaldamentiXMLModel(riscaldamentiVO));
			}
			exportSelection(hm,base_path + "riscaldamenti.xml");
		}else{		
			exportSelection(objsToExport.getHmRiscaldamento(),base_path + "riscaldamenti.xml");
		}

		exportSelection(objsToExport.getHmStanzeImmobili(),base_path + "stanzeimmobili.xml");

		if (objsToExport.isFlagDatiBaseCompleti()){
			
			HashMap hm = new HashMap();
			
			ArrayList<Statoconservativo> al_sc = UtilsHelper.getInstance().getAllStatoConservativo();
			for (Statoconservativo statoConservativoVO : al_sc) {
// TO DO				hm.put(statoConservativoVO.getCodStatoConservativo(),new StatoConservativoXMLModel(statoConservativoVO));
			}
			exportSelection(hm,base_path + "statoconservativo.xml");
		}else{		
			exportSelection(objsToExport.getHmStatiConservativi(),base_path + "statoconservativo.xml");
		}		

		if (objsToExport.isFlagDatiBaseCompleti()){
			
			HashMap hm = new HashMap();
			
			ArrayList<TipiAppuntamentiVO> al_ta = UtilsHelper.getInstance().getAllTipiAppuntamenti();
			for (TipiAppuntamentiVO tipiAppuntamentiVO : al_ta) {
				hm.put(tipiAppuntamentiVO.getCodTipoAppuntamento(),new TipiAppuntamentiXMLModel(tipiAppuntamentiVO));
			}
			exportSelection(hm,base_path + "tipologiaappuntamenti.xml");
		}else{		
			exportSelection(objsToExport.getHmTipiAppuntamento(),base_path + "tipologiaappuntamenti.xml");
		}		

		if (objsToExport.isFlagDatiBaseCompleti()){
			
			HashMap hm = new HashMap();
			
			ArrayList<Tipologiecontatti> al_tc = UtilsHelper.getInstance().getAllTipologiaContatti();
			for (Tipologiecontatti tipologiaContattiVO : al_tc) {
// TO DO				hm.put(tipologiaContattiVO.getCodTipologiaContatto(),new TipologiaContattiXMLModel(tipologiaContattiVO));
			}
			exportSelection(hm,base_path + "tipologiacontatti.xml");
		}else{		
			exportSelection(objsToExport.getHmTipiContatti(),base_path + "tipologiacontatti.xml");
		}		
		
		exportSelection(objsToExport.getHmContatti(),base_path + "contatti.xml");

		if (objsToExport.isFlagDatiBaseCompleti()){
			
			HashMap hm = new HashMap();
			
			ArrayList<Tipologiastanze> al_ts = UtilsHelper.getInstance().getAllTipologieStanze();
			for (Tipologiastanze tipologiaStanzeVO : al_ts) {
// TO DO				hm.put(tipologiaStanzeVO.getCodTipologiaStanza(),new TipologiaStanzeXMLModel(tipologiaStanzeVO));
			}
			exportSelection(hm,base_path + "tipologiastanze.xml");
		}else{		
			exportSelection(objsToExport.getHmTipiStanze(),base_path + "tipologiastanze.xml");
		}				

		if (objsToExport.isFlagDatiBaseCompleti()){
			
			HashMap hm = new HashMap();
			
			ArrayList<Tipologieimmobili> al_ti = UtilsHelper.getInstance().getAllTipologieImmobili();
			for (Tipologieimmobili tipologieImmobiliVO : al_ti) {
// TODO				hm.put(tipologieImmobiliVO.getCodTipologiaImmobile(),new TipologieImmobiliXMLModel(tipologieImmobiliVO));
			}
			exportSelection(hm,base_path + "tipologiaimmobili.xml");
		}else{		
			exportSelection(objsToExport.getHmTipiImmobili(),base_path + "tipologiaimmobili.xml");
		}		

		
		
		exportSelection(objsToExport.getHmEntity(),base_path + "entita.xml");
		exportSelection(objsToExport.getHmAttribute(),base_path + "attributi.xml");
		exportSelection(objsToExport.getHmAttributeValue(),base_path + "valoriattributi.xml");
		exportSelection(objsToExport.getHmImmobiliPropietari(),base_path + "immobilipropietari.xml");
		
		if (zipped){
			ZipUtils zu = new ZipUtils();
			zu.zip4jArchivio(base_path, pathToExport + File.separator + path + ".zip");
		}
		
		MessageDialog.openInformation(Activator.getDefault()
				 							   .getWorkbench()
				 							   .getActiveWorkbenchWindow()
				 							   .getShell(), 
				 					  "salvataggio esportazione", 
									  "Salvataggio esportazione completato");
		
		
		return true;
		
	}
	
	public boolean exportSelection(HashMap selezione,String exportFolderPath){
		
		boolean returnValue = true;
		
		if (selezione.size() > 0){
			
			FileOutputStream fos = null;
			XMLObjectWriter xmlow = null;
					
			Iterator<Map.Entry> it = selezione.entrySet().iterator();
						
			File f = new File(exportFolderPath);
			try {
				f.createNewFile();
			    fos = new FileOutputStream(f);
			} catch (FileNotFoundException e1) {
				returnValue = false;
			} catch (IOException e2) {
				returnValue = false;
			}

			try {
				
				xmlow = XMLObjectWriter.newInstance(fos);
				xmlow.setBinding(getBindingsObj());
				
				while (it.hasNext()){
					xmlow.write(it.next().getValue());					
				}
				
				xmlow.close();
				
			} catch (XMLStreamException e1) {
				returnValue = false;
			}
						
		}
		
		return returnValue;
		
	}
	
	protected boolean saveFileAllegatiAffitti(AffittiAllegatiVO aavo,String pathDestinazione){
		
		boolean returnValue = true;
		
		SystemPreferenceHelper sph = new SystemPreferenceHelper();
		String pathAllegati = sph.getArchivioAllegatiPath();
		
		String pathOrigine = pathAllegati + File.separator + "affitti" + File.separator + 
							 aavo.getCodAffitto() + File.separator + aavo.getNome();
		
		String pathDestinazioneAllegati = pathDestinazione + File.separator + 
										  "allegati" + File.separator + "affitti" + File.separator +  
										  aavo.getCodAffitto() + File.separator + aavo.getNome();
		
		copiaFile(pathOrigine , pathDestinazioneAllegati);				
								 		
		return returnValue;
	}

	protected boolean saveFileAllegatiColloquio(AllegatiColloquiVO acvo,String pathDestinazione){
		
		boolean returnValue = true;
		
		SystemPreferenceHelper sph = new SystemPreferenceHelper();
		String pathAllegati = sph.getArchivioAllegatiPath();
		
		String pathOrigine = pathAllegati + File.separator + "colloqui" + File.separator + 
				 			 acvo.getCodColloquio() + File.separator + acvo.getNome();
		
		String pathDestinazioneAllegati = pathDestinazione + File.separator + 
										  "allegati" + File.separator + "colloqui" + File.separator +  
										  acvo.getCodColloquio() + File.separator + acvo.getNome();
		
		copiaFile(pathOrigine , pathDestinazioneAllegati);				
								 		
		return returnValue;
	}

	protected boolean saveFileAllegatiImmobili(AllegatiImmobiliVO aivo,String pathDestinazione){
		
		boolean returnValue = true;
		
		SystemPreferenceHelper sph = new SystemPreferenceHelper();
		String pathAllegati = sph.getArchivioAllegatiPath();
		
		String pathOrigine = pathAllegati + File.separator + "immobili" + File.separator + 
				 			 aivo.getCodImmobile() + File.separator + aivo.getNome();
		
		String pathDestinazioneAllegati = pathDestinazione + File.separator + 
										  "allegati" + File.separator + "immobili" + File.separator +  
										  aivo.getCodImmobile() + File.separator + aivo.getNome();
		
		copiaFile(pathOrigine , pathDestinazioneAllegati);				
								 		
		return returnValue;
	}

	public boolean saveFileImmagini(ImmagineVO ivo,String pathDestinazione){
		
		boolean returnValue = true;
		
		SystemPreferenceHelper sph = new SystemPreferenceHelper();
		String pathImmagini = sph.getArchivioImmaginiPath();
		
		String pathOrigine = pathImmagini + File.separator + 
							 ivo.getCodImmobile() + File.separator + ivo.getPathImmagine();
		
		String pathDestinazioneImmagini = pathDestinazione + File.separator + 
										  "immagini" + File.separator + 
										  ivo.getCodImmobile() + File.separator + ivo.getPathImmagine();
		
		copiaFile(pathOrigine , pathDestinazioneImmagini);				
								 		
		return returnValue;
	}
	
	protected String buildexportFolderName(){
		
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		String folderName = "winkxmlexport" + 
							c.get(Calendar.YEAR) +  
							c.get(Calendar.MONTH) + 
							c.get(Calendar.DAY_OF_MONTH) +
							c.get(Calendar.HOUR_OF_DAY) +
							c.get(Calendar.MINUTE) +
							c.get(Calendar.SECOND);
		
		return folderName;
		
	}
	
	protected XMLBinding getBindingsObj(){
		
		XMLBinding winkhouseBinding = new XMLBinding();
		
		winkhouseBinding.setAlias(AbbinamentiXMLModel.class, "abbinamenti");
		winkhouseBinding.setAlias(AffittiAllegatiXMLModel.class, "allegati_affitti");
		winkhouseBinding.setAlias(AffittiAnagraficheXMLModel.class, "affitti_anagrafiche");
		winkhouseBinding.setAlias(AffittiRateXMLModel.class, "affitti_rate");
		winkhouseBinding.setAlias(AffittiSpeseXMLModel.class, "affitti_spese");
		winkhouseBinding.setAlias(AffittiXMLModel.class, "affitti");
		winkhouseBinding.setAlias(AgentiAppuntamentiXMLModel.class, "agenti_appuntamenti");
		winkhouseBinding.setAlias(AgentiXMLModel.class, "agenti");
		winkhouseBinding.setAlias(AllegatiColloquiXMLModel.class, "allegati_colloqui");
		winkhouseBinding.setAlias(AllegatiImmobiliXMLModel.class, "allegati_immobili");
		winkhouseBinding.setAlias(AnagraficheAppuntamentiXMLModel.class, "anagrafiche_appuntamenti");
		winkhouseBinding.setAlias(AnagraficheXMLModel.class, "anagrafiche");
		winkhouseBinding.setAlias(AppuntamentiXMLModel.class, "appuntamenti");
		winkhouseBinding.setAlias(ClasseEnergeticaXMLModel.class, "classeenergetica");
		winkhouseBinding.setAlias(ClassiClientiXMLModel.class, "classiclienti");
		winkhouseBinding.setAlias(ColloquiAgentiXMLModel.class, "colloqui_agenti");
		winkhouseBinding.setAlias(ColloquiAnagraficheXMLModel.class, "colloqui_anagrafiche");
		winkhouseBinding.setAlias(ColloquiCriteriRicercaXMLModel.class, "colloqui_criteriricerca");
		winkhouseBinding.setAlias(ColloquiXMLModel.class, "colloqui");
		winkhouseBinding.setAlias(ContattiXMLModel.class, "contatti");
		winkhouseBinding.setAlias(CriteriRicercaXMLModel.class, "criteriricerca");
		winkhouseBinding.setAlias(DatiCatastaliXMLModel.class, "daticatastali");
		winkhouseBinding.setAlias(GCalendarXMLModel.class, "gcalendar");
		winkhouseBinding.setAlias(ImmagineXMLModel.class, "immagine");
		winkhouseBinding.setAlias(ImmobiliXMLModel.class, "immobili");
		winkhouseBinding.setAlias(RicercheXMLModel.class, "ricerche");
		winkhouseBinding.setAlias(RiscaldamentiXMLModel.class, "riscaldamenti");
		winkhouseBinding.setAlias(StanzeImmobiliXMLModel.class, "stanzeimmobili");
		winkhouseBinding.setAlias(StatoConservativoXMLModel.class, "statoconservativo");
		winkhouseBinding.setAlias(TipiAppuntamentiXMLModel.class, "tipologiaappuntamenti");
		winkhouseBinding.setAlias(TipologiaContattiXMLModel.class, "tipologiacontatti");
		winkhouseBinding.setAlias(TipologiaStanzeXMLModel.class, "tipologiastanze");
		winkhouseBinding.setAlias(TipologieImmobiliXMLModel.class, "tipologiaimmobili");
		winkhouseBinding.setAlias(EntityXMLModel.class, "entita");
		winkhouseBinding.setAlias(AttributeXMLModel.class, "attributo");
		winkhouseBinding.setAlias(AttributeValueXMLModel.class, "valoreattributo");
		winkhouseBinding.setAlias(FastList.class, "lista");
		winkhouseBinding.setAlias(EntityXMLModel.class, "entita");
		winkhouseBinding.setAlias(AttributeXMLModel.class, "attributo");
		winkhouseBinding.setAlias(AttributeValueXMLModel.class, "valoreattributo");
		winkhouseBinding.setAlias(ImmobiliPropietariXMLModel.class, "immobilipropietari");		
				
		return winkhouseBinding;
		
	}

	protected boolean copiaFile(String pathOrigine, String pathDestinazione) {
		  
	    boolean returnValue = true;
		try {
			File destinazione = new File(pathDestinazione);
			
			if (!destinazione.exists()){				
				returnValue = doCopy(pathOrigine, pathDestinazione);
			}else{
				if (MessageDialog.openConfirm(Activator.getDefault()
													   .getWorkbench().getActiveWorkbenchWindow().getShell(), 
											  "File esistente", 
											  "Negli archivi � presente un file con lo stesso nome \n" + 
										 	  "sovrascrivo il file in archivio con quello attuale ?")){
					
					returnValue = doCopy(pathOrigine, pathDestinazione);
					
				}else{
					returnValue = false;
				}				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
    
    }
  
	protected boolean doCopy(String pathOrigine, String pathDestinazione) {
	  
	  	boolean returnValue = true;
	  	
		try {
			File origine = new File(pathOrigine);
			File destinazione = new File(pathDestinazione);

			FileInputStream fis = new FileInputStream(origine);
			String pathdestinazione = pathDestinazione.substring(0, pathDestinazione.lastIndexOf("\\")); 
			File f = new File(pathdestinazione);
			System.out.println(f.mkdirs());
			
			  FileOutputStream fos = new FileOutputStream(destinazione);

			  byte [] dati = new byte[fis.available()];
			  fis.read(dati);
			  fos.write(dati);
			  
			  fos.flush();
			  fis.close();
			  fos.close();
		} catch (FileNotFoundException e) {
			returnValue = false;
			e.printStackTrace();
		} catch (IOException e) {
			returnValue = false;
			e.printStackTrace();
		}
		return returnValue;

    }

	public ArrayList<DatiBaseModel> getDatiBaseModel(){
		
		ArrayList<DatiBaseModel> returnValue = new ArrayList<DatiBaseModel>();
		
		returnValue.add(new DatiBaseModel(AGENTI_COD, "Agenti"));
		returnValue.add(new DatiBaseModel(CLASSE_ENERGETICA_COD, "Classe energetica"));
		returnValue.add(new DatiBaseModel(RISCALDAMENTO_COD, "Riscaldamento"));
		returnValue.add(new DatiBaseModel(STATO_CONSERVATIVO_COD, "Stato conservativo"));
		returnValue.add(new DatiBaseModel(TIPOLOGIA_IMMOBILE_COD, "Tipologia immobile"));
		returnValue.add(new DatiBaseModel(CLASSE_CLIENTE_COD, "Classe cliente"));
		returnValue.add(new DatiBaseModel(TIPOLOGIA_STANZE_COD, "Tipologia stanze"));
		returnValue.add(new DatiBaseModel(TIPOLOGIA_CONTATTI_COD, "Tipologia contatti"));
		
		return returnValue;
		
	}

	public String getPathExportDir() {
		return pathExportDir;
	}

	public String getPath() {
		return path;
	}
	
}