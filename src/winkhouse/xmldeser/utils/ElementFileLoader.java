package winkhouse.xmldeser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import javolution.xml.XMLObjectReader;
import javolution.xml.stream.XMLStreamException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import winkhouse.util.WinkhouseUtils;
import winkhouse.util.ZipUtils;
import winkhouse.xmldeser.models.xml.AbbinamentiXMLModel;
import winkhouse.xmldeser.models.xml.AffittiAllegatiXMLModel;
import winkhouse.xmldeser.models.xml.AffittiAnagraficheXMLModel;
import winkhouse.xmldeser.models.xml.AffittiRateXMLModel;
import winkhouse.xmldeser.models.xml.AffittiSpeseXMLModel;
import winkhouse.xmldeser.models.xml.AffittiXMLModel;
import winkhouse.xmldeser.models.xml.AgentiAppuntamentiXMLModel;
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
import winkhouse.xmldeser.models.xml.GCalendarXMLModel;
import winkhouse.xmldeser.models.xml.ImmagineXMLModel;
import winkhouse.xmldeser.models.xml.ImmobiliPropietariXMLModel;
import winkhouse.xmldeser.models.xml.ImmobiliXMLModel;
import winkhouse.xmldeser.models.xml.RicercheXMLModel;
import winkhouse.xmldeser.models.xml.RiscaldamentiXMLModel;
import winkhouse.xmldeser.models.xml.StanzeImmobiliXMLModel;
import winkhouse.xmldeser.models.xml.StatoConservativoXMLModel;
import winkhouse.xmldeser.models.xml.TipiAppuntamentiXMLModel;
import winkhouse.xmldeser.models.xml.TipologiaContattiXMLModel;
import winkhouse.xmldeser.models.xml.TipologiaStanzeXMLModel;
import winkhouse.xmldeser.models.xml.TipologieImmobiliXMLModel;
import winkhouse.xmldeser.wizard.importer.ImporterWizard;
import winkhouse.xmldeser.wizard.importer.vo.ImporterVO;

public class ElementFileLoader implements IRunnableWithProgress {
	
	private String importPath = null;
	private ImporterVO importerVO = null;
	
	private String IMMOBILI_TAG = "immobili";
	private String IMMOBILI_FILENAME = "immobili.xml";
	private String IMMOBILI_GETCOD_METHODNAME = "getCodImmobile";
	
	private String ANAGRAFICHE_TAG = "anagrafiche";
	private String ANAGRAFICHE_FILENAME = "anagrafiche.xml";
	private String ANAGRAFICHE_GETCOD_METHODNAME = "getCodAnagrafica";

	private String COLLOQUI_TAG = "colloqui";
	private String COLLOQUI_FILENAME = "colloqui.xml";
	private String COLLOQUI_GETCOD_METHODNAME = "getCodColloquio";

	private String AGENTI_TAG = "agenti";
	private String AGENTI_FILENAME = "agenti.xml";
	private String AGENTI_GETCOD_METHODNAME = "getCodAgente";
	
	private String RISCALDAMENTI_TAG = "riscaldamenti";
	private String RISCALDAMENTI_FILENAME = "riscaldamenti.xml";
	private String RISCALDAMENTI_GETCOD_METHODNAME = "getCodRiscaldamento";

	private String STANZEIMMOBILI_TAG = "stanzeimmobili";
	private String STANZEIMMOBILI_FILENAME = "stanzeimmobili.xml";
	private String STANZEIMMOBILI_GETCOD_METHODNAME = "getCodStanzeImmobili";

	private String STATOCONSERVATIVO_TAG = "statoconservativo";
	private String STATOCONSERVATIVO_FILENAME = "statoconservativo.xml";
	private String STATOCONSERVATIVO_GETCOD_METHODNAME = "getCodStatoConservativo";
	
	private String TIPOLOGIACONTATTO_TAG = "tipologiacontatti";
	private String TIPOLOGIACONTATTO_FILENAME = "tipologiacontatti.xml";
	private String TIPOLOGIACONTATTO_GETCOD_METHODNAME = "getCodTipologiaContatto";

	private String CONTATTI_TAG = "contatti";
	private String CONTATTI_FILENAME = "contatti.xml";
	private String CONTATTI_GETCOD_METHODNAME = "getCodContatto";
	
	private String TIPOLOGIAIMMOBILI_TAG = "tipologiaimmobili";
	private String TIPOLOGIAIMMOBILI_FILENAME = "tipologiaimmobili.xml";
	private String TIPOLOGIAIMMOBILI_GETCOD_METHODNAME = "getCodTipologiaImmobile";

	private String TIPOLOGIASTANZE_TAG = "tipologiastanze";
	private String TIPOLOGIASTANZE_FILENAME = "tipologiastanze.xml";
	private String TIPOLOGIASTANZE_GETCOD_METHODNAME = "getCodTipologiaStanza";

	private String IMMAGINE_TAG = "immagine";
	private String IMMAGINE_FILENAME = "immagine.xml";
	private String IMMAGINE_GETCOD_METHODNAME = "getCodImmagine";

	private String COLLOQUIAGENTI_TAG = "colloqui_agenti";
	private String COLLOQUIAGENTI_FILENAME = "colloqui_agenti.xml";
	private String COLLOQUIAGENTI_GETCOD_METHODNAME = "getCodColloquioAgenti";

	private String ABBINAMENTI_TAG = "abbinamenti";
	private String ABBINAMENTI_FILENAME = "abbinamenti.xml";
	private String ABBINAMENTI_GETCOD_METHODNAME = "getCodAbbinamento";

	private String ALLEGATIAFFITTI_TAG = "allegati_affitti";
	private String ALLEGATIAFFITTI_FILENAME = "allegati_affitti.xml";
	private String ALLEGATIAFFITTI_GETCOD_METHODNAME = "getCodAffittiAllegati";

	private String AFFITTIANAGRAFICHE_TAG = "affitti_anagrafiche";
	private String AFFITTIANAGRAFICHE_FILENAME = "affitti_anagrafiche.xml";
	private String AFFITTIANAGRAFICHE_GETCOD_METHODNAME = "getCodAffittiAnagrafiche";

	private String AFFITTIRATE_TAG = "affitti_rate";
	private String AFFITTIRATE_FILENAME = "affitti_rate.xml";
	private String AFFITTIRATE_GETCOD_METHODNAME = "getCodAffittiRate";

	private String AFFITTISPESE_TAG = "affitti_spese";
	private String AFFITTISPESE_FILENAME = "affitti_spese.xml";
	private String AFFITTISPESE_GETCOD_METHODNAME = "getCodAffittiSpese";

	private String AFFITTI_TAG = "affitti";
	private String AFFITTI_FILENAME = "affitti.xml";
	private String AFFITTI_GETCOD_METHODNAME = "getCodAffitti";

	private String AGENTIAPPUNTAMENTI_TAG = "agenti_appuntamenti";
	private String AGENTIAPPUNTAMENTI_FILENAME = "agenti_appuntamenti.xml";
	private String AGENTIAPPUNTAMENTI_GETCOD_METHODNAME = "getCodAgentiAppuntamenti";

	private String ALLEGATICOLLOQUIO_TAG = "allegati_colloqui";
	private String ALLEGATICOLLOQUIO_FILENAME = "allegati_colloqui.xml";
	private String ALLEGATICOLLOQUIO_GETCOD_METHODNAME = "getCodAllegatiColloquio";
	
	private String ALLEGATIIMMOBILI_TAG = "allegati_immobili";
	private String ALLEGATIIMMOBILI_FILENAME = "allegati_immobili.xml";
	private String ALLEGATIIMMOBILI_GETCOD_METHODNAME = "getCodAllegatiImmobili";

	private String APPUNTAMENTI_TAG = "appuntamenti";
	private String APPUNTAMENTI_FILENAME = "appuntamenti.xml";
	private String APPUNTAMENTI_GETCOD_METHODNAME = "getCodAppuntamento";

	private String CLASSEENERGETICA_TAG = "classeenergetica";
	private String CLASSEENERGETICA_FILENAME = "classeenergetica.xml";
	private String CLASSEENERGETICA_GETCOD_METHODNAME = "getCodClasseEnergetica";

	private String CLASSICLIENTI_TAG = "classiclienti";
	private String CLASSICLIENTI_FILENAME = "classiclienti.xml";
	private String CLASSICLIENTI_GETCOD_METHODNAME = "getCodClasseCliente";

	private String COLLOQUIANAGRAFICHE_TAG = "colloqui_anagrafiche";
	private String COLLOQUIANAGRAFICHE_FILENAME = "colloqui_anagrafiche.xml";
	private String COLLOQUIANAGRAFICHE_GETCOD_METHODNAME = "getCodColloquioAnagrafiche";

	private String COLLOQUICRITERIRICERCA_TAG = "criteriricerca";
	private String COLLOQUICRITERIRICERCA_FILENAME = "criteriricerca.xml";
	private String COLLOQUICRITERIRICERCA_GETCOD_METHODNAME = "getCodCriterioRicerca";

	private String DATICATASTALI_TAG = "daticatastali";
	private String DATICATASTALI_FILENAME = "daticatastali.xml";
	private String DATICATASTALI_GETCOD_METHODNAME = "getCodDatiCatastali";

	private String GCALENDAR_TAG = "gcalendar";
	private String GCALENDAR_FILENAME = "gcalendar.xml";
	private String GCALENDAR_GETCOD_METHODNAME = "getCodGCalendar";

	private String RICERCHE_TAG = "ricerche";
	private String RICERCHE_FILENAME = "ricerche.xml";
	private String RICERCHE_GETCOD_METHODNAME = "getCodGCalendar";

	private String TIPOLOGIAAPPUNTAMENTI_TAG = "tipologiaappuntamenti";
	private String TIPOLOGIAAPPUNTAMENTI_FILENAME = "tipologiaappuntamenti.xml";
	private String TIPOLOGIAAPPUNTAMENTI_GETCOD_METHODNAME = "getCodTipoAppuntamento";

	private String ENTITA_TAG = "entita";
	private String ENTITA_FILENAME = "entita.xml";
	private String ENTITA_GETCOD_METHODNAME = "getIdClassEntity";

	private String ATTRIBUTI_TAG = "attributo";
	private String ATTRIBUTI_FILENAME = "attributi.xml";
	private String ATTRIBUTI_GETCOD_METHODNAME = "getIdAttribute";

	private String VALOREATTRIBUTO_TAG = "valoreattributo";
	private String VALOREATTRIBUTO_FILENAME = "valoriattributi.xml";
	private String VALOREATTRIBUTO_GETCOD_METHODNAME = "getIdValue";

	private String IMMOBILIPROPIETA_TAG = "immobilipropietari";
	private String IMMOBILIPROPIETA_FILENAME = "immobilipropietari.xml";

	
	public ElementFileLoader(ImporterVO importerVO) {
		this.importerVO = importerVO;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,InterruptedException {
		File f_tmp = null;
		String s_tmp = null;
		File exportFolder = new File(this.importerVO.getPathExportFile());
		if (monitor != null){
			monitor.beginTask("Importazione dati ...", 30);
		}
		if (exportFolder.isFile() && exportFolder.getName().endsWith("zip")){
			ZipUtils zu = new ZipUtils();
			s_tmp = exportFolder.getAbsolutePath().replace(".zip", "_tmp");
			f_tmp = new File(s_tmp);
			f_tmp.mkdirs();			
			if (zu.unZip4jArchivio(exportFolder.getAbsolutePath(), s_tmp)){
				exportFolder = new File(s_tmp);
			}else{
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
						"Errore unzip file", 
						"Impossibile aprire il file zip controllare la password se presente.");
			}
		}
		if (exportFolder.isDirectory()){
			
			File[] files = exportFolder.listFiles();
			for (int i = 0; i < files.length; i++) {
				
				if (files[i].isFile()){
					
					if (files[i].getName().equalsIgnoreCase(AGENTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + AGENTI_FILENAME);
						}
						importerVO.setHmAgenti(loadFromFile(files[i], AGENTI_TAG, AgentiXMLModel.class, AGENTI_GETCOD_METHODNAME)); 
					}
										
					if (files[i].getName().equalsIgnoreCase(STANZEIMMOBILI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + STANZEIMMOBILI_FILENAME);
						}
						importerVO.setHmStanzeImmobili(loadFromFile(files[i], STANZEIMMOBILI_TAG, StanzeImmobiliXMLModel.class, STANZEIMMOBILI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(STATOCONSERVATIVO_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + STATOCONSERVATIVO_FILENAME);
						}
						importerVO.setHmStatiConservativi(loadFromFile(files[i], STATOCONSERVATIVO_TAG, StatoConservativoXMLModel.class, STATOCONSERVATIVO_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(TIPOLOGIACONTATTO_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + TIPOLOGIACONTATTO_FILENAME);
						}
						importerVO.setHmTipiContatti(loadFromFile(files[i], TIPOLOGIACONTATTO_TAG, TipologiaContattiXMLModel.class, TIPOLOGIACONTATTO_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(TIPOLOGIAIMMOBILI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + TIPOLOGIAIMMOBILI_FILENAME);
						}
						importerVO.setHmTipiImmobili(loadFromFile(files[i], TIPOLOGIAIMMOBILI_TAG, TipologieImmobiliXMLModel.class, TIPOLOGIAIMMOBILI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(TIPOLOGIASTANZE_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + TIPOLOGIASTANZE_FILENAME);
						}
						importerVO.setHmTipiStanze(loadFromFile(files[i], TIPOLOGIASTANZE_TAG, TipologiaStanzeXMLModel.class, TIPOLOGIASTANZE_GETCOD_METHODNAME)); 
					}

					if (files[i].getName().equalsIgnoreCase(RISCALDAMENTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + RISCALDAMENTI_FILENAME);
						}
						importerVO.setHmRiscaldamento(loadFromFile(files[i], RISCALDAMENTI_TAG, RiscaldamentiXMLModel.class, RISCALDAMENTI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(TIPOLOGIAAPPUNTAMENTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + TIPOLOGIAAPPUNTAMENTI_TAG);
						}
						importerVO.setHmTipiAppuntamento(loadFromFile(files[i], TIPOLOGIAAPPUNTAMENTI_TAG, TipiAppuntamentiXMLModel.class, TIPOLOGIAAPPUNTAMENTI_GETCOD_METHODNAME)); 
					}					

					if (files[i].getName().equalsIgnoreCase(ANAGRAFICHE_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + ANAGRAFICHE_TAG);
						}
						importerVO.setHmAnagrafiche(loadFromFile(files[i], ANAGRAFICHE_TAG, AnagraficheXMLModel.class, ANAGRAFICHE_GETCOD_METHODNAME)); 
					}

					if (files[i].getName().equalsIgnoreCase(CONTATTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + CONTATTI_FILENAME);
						}
						importerVO.setHmContatti(loadFromFile(files[i], CONTATTI_TAG, ContattiXMLModel.class, CONTATTI_GETCOD_METHODNAME)); 
					}

					if (files[i].getName().equalsIgnoreCase(IMMOBILI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + IMMOBILI_FILENAME);
						}
						importerVO.setHmImmobili(loadFromFile(files[i], IMMOBILI_TAG, ImmobiliXMLModel.class, IMMOBILI_GETCOD_METHODNAME)); 
					}
										
					if (files[i].getName().equalsIgnoreCase(COLLOQUI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + COLLOQUI_FILENAME);
						}
						importerVO.setHmColloqui(loadFromFile(files[i], COLLOQUI_TAG, ColloquiXMLModel.class, COLLOQUI_GETCOD_METHODNAME)); 
					}
										
					if (files[i].getName().equalsIgnoreCase(IMMAGINE_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + IMMAGINE_FILENAME);
						}
						importerVO.setHmImmagini(loadFromFile(files[i], IMMAGINE_TAG, ImmagineXMLModel.class, IMMAGINE_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(COLLOQUIAGENTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + COLLOQUIAGENTI_FILENAME);
						}
						importerVO.setHmColloquiAgenti(loadFromFile(files[i], COLLOQUIAGENTI_TAG, ColloquiAgentiXMLModel.class, COLLOQUIAGENTI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(ABBINAMENTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + ABBINAMENTI_FILENAME);
						}
						importerVO.setHmAbbinamenti(loadFromFile(files[i], ABBINAMENTI_TAG, AbbinamentiXMLModel.class, ABBINAMENTI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(AFFITTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + AFFITTI_FILENAME);
						}
						importerVO.setHmAffitti(loadFromFile(files[i], AFFITTI_TAG, AffittiXMLModel.class, AFFITTI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(ALLEGATIAFFITTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + ALLEGATIAFFITTI_FILENAME);
						}
						importerVO.setHmAffittiAllegati(loadFromFile(files[i], ALLEGATIAFFITTI_TAG, AffittiAllegatiXMLModel.class, ALLEGATIAFFITTI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(AFFITTIANAGRAFICHE_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + AFFITTIANAGRAFICHE_FILENAME);
						}
						importerVO.setHmAffittiAnagrafiche(loadFromFile(files[i], AFFITTIANAGRAFICHE_TAG, AffittiAnagraficheXMLModel.class, AFFITTIANAGRAFICHE_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(AFFITTIRATE_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + AFFITTIRATE_FILENAME);
						}
						importerVO.setHmAffittiRate(loadFromFile(files[i], AFFITTIRATE_TAG, AffittiRateXMLModel.class, AFFITTIRATE_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(AFFITTISPESE_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + AFFITTISPESE_FILENAME);
						}
						importerVO.setHmAffittiSpese(loadFromFile(files[i], AFFITTISPESE_TAG, AffittiSpeseXMLModel.class, AFFITTISPESE_GETCOD_METHODNAME)); 
					}
										
					if (files[i].getName().equalsIgnoreCase(AGENTIAPPUNTAMENTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + AGENTIAPPUNTAMENTI_FILENAME);
						}
						importerVO.setHmAgentiAppuntamenti(loadFromFile(files[i], AGENTIAPPUNTAMENTI_TAG, AgentiAppuntamentiXMLModel.class, AGENTIAPPUNTAMENTI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(ALLEGATICOLLOQUIO_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + ALLEGATICOLLOQUIO_FILENAME);
						}
						importerVO.setHmAllegatiColloquio(loadFromFile(files[i], ALLEGATICOLLOQUIO_TAG, AllegatiColloquiXMLModel.class, ALLEGATICOLLOQUIO_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(ALLEGATIIMMOBILI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + ALLEGATIIMMOBILI_FILENAME);
						}
						importerVO.setHmAllegatiImmobili(loadFromFile(files[i], ALLEGATIIMMOBILI_TAG, AllegatiImmobiliXMLModel.class, ALLEGATIIMMOBILI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(APPUNTAMENTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + APPUNTAMENTI_FILENAME);
						}
						importerVO.setHmAppuntamenti(loadFromFile(files[i], APPUNTAMENTI_TAG, AppuntamentiXMLModel.class, APPUNTAMENTI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(CLASSEENERGETICA_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + CLASSEENERGETICA_FILENAME);
						}
						importerVO.setHmClassiEnergetiche(loadFromFile(files[i], CLASSEENERGETICA_TAG, ClasseEnergeticaXMLModel.class, CLASSEENERGETICA_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(CLASSICLIENTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + CLASSICLIENTI_FILENAME);
						}
						importerVO.setHmCategorieClienti(loadFromFile(files[i], CLASSICLIENTI_TAG, ClassiClientiXMLModel.class, CLASSICLIENTI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(COLLOQUIANAGRAFICHE_FILENAME)){
						monitor.setTaskName("lettura file : " + COLLOQUIANAGRAFICHE_FILENAME);
						importerVO.setHmColloquiAnagrafica(loadFromFile(files[i], COLLOQUIANAGRAFICHE_TAG, ColloquiAnagraficheXMLModel.class, COLLOQUIANAGRAFICHE_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(COLLOQUICRITERIRICERCA_FILENAME)){
						monitor.setTaskName("lettura file : " + COLLOQUICRITERIRICERCA_FILENAME);
						importerVO.setHmColloquiCriteriRicerca(loadFromFile(files[i], COLLOQUICRITERIRICERCA_TAG, ColloquiCriteriRicercaXMLModel.class, COLLOQUICRITERIRICERCA_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(DATICATASTALI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + DATICATASTALI_FILENAME);
						}
						importerVO.setHmDatiCatastali(loadFromFile(files[i], DATICATASTALI_TAG, DatiCatastaliXMLModel.class, DATICATASTALI_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(GCALENDAR_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + GCALENDAR_FILENAME);
						}
						importerVO.setHmGCalendar(loadFromFile(files[i], GCALENDAR_TAG, GCalendarXMLModel.class, GCALENDAR_GETCOD_METHODNAME)); 
					}
					
					if (files[i].getName().equalsIgnoreCase(RICERCHE_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + RICERCHE_FILENAME);
						}
						importerVO.setHmRicerche(loadFromFile(files[i], RICERCHE_TAG, RicercheXMLModel.class, RICERCHE_GETCOD_METHODNAME)); 
					}

					if (files[i].getName().equalsIgnoreCase(ENTITA_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + ENTITA_FILENAME);
						}
						importerVO.setHmEntita(loadFromFile(files[i], ENTITA_TAG, EntityXMLModel.class, ENTITA_GETCOD_METHODNAME)); 
					}

					if (files[i].getName().equalsIgnoreCase(ATTRIBUTI_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + ATTRIBUTI_FILENAME);
						}
						importerVO.setHmAttributi(loadFromFile(files[i], ATTRIBUTI_TAG, AttributeXMLModel.class, ATTRIBUTI_GETCOD_METHODNAME)); 
					}

					if (files[i].getName().equalsIgnoreCase(VALOREATTRIBUTO_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + VALOREATTRIBUTO_FILENAME);
						}
						importerVO.setHmValoriAttributi(loadFromFile(files[i], VALOREATTRIBUTO_TAG, AttributeValueXMLModel.class, VALOREATTRIBUTO_GETCOD_METHODNAME)); 
					}

					if (files[i].getName().equalsIgnoreCase(IMMOBILIPROPIETA_FILENAME)){
						if (monitor != null){
							monitor.setTaskName("lettura file : " + IMMOBILIPROPIETA_FILENAME);
						}
						importerVO.setAlImmobiliPropieta(loadFromFile(files[i], IMMOBILIPROPIETA_TAG, ImmobiliPropietariXMLModel.class)); 
					}

				}	
				if (monitor != null){
					monitor.worked(1);
				}
			}
			if (f_tmp != null){				
				if (s_tmp != null){
					WinkhouseUtils.getInstance().standardDirectoryDeleter(s_tmp);
				}
				f_tmp.delete();
				f_tmp = null;
				
			}
			
		}

	}
	
	private HashMap loadFromFile(File exportFile,String xmlTag,Class classToDeserialize,String primaryKeyGetMethodName){
		
		HashMap returnValue = null;
		XMLObjectReader xmlor = null;
		try {
			
			xmlor = XMLObjectReader.newInstance(new FileInputStream(exportFile));
			
			returnValue = new HashMap<Integer, Object>();
			while (xmlor.hasNext()){
				
				Object o = xmlor.read(xmlTag, classToDeserialize);
				try {
					Method m = o.getClass().getMethod(primaryKeyGetMethodName, null);
					try {
						Integer codPrimarykey = (Integer)m.invoke(o, null);
						returnValue.put(codPrimarykey, o);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
			
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				
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
					e.printStackTrace();
				}	
			}
		}
		
		return returnValue;
		
	}

	private ArrayList loadFromFile(File exportFile,String xmlTag,Class classToDeserialize){
		
		ArrayList returnValue = null;
		XMLObjectReader xmlor = null;
		try {
			xmlor = XMLObjectReader.newInstance(new FileInputStream(exportFile));
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
					e.printStackTrace();
				}	
			}
		}

		
		return returnValue;
		
	}

}
