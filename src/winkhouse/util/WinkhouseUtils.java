package winkhouse.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.cayenne.ObjectContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.action.WinkPreferenceDialog;
import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.AttributeDAO;
import winkhouse.dao.EntityDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.WinkSysDAO;
import winkhouse.db.orm.CayenneContextManager;
import winkhouse.dialogs.custom.GoogleConfDialog;
import winkhouse.dialogs.custom.ImageViewer;
import winkhouse.engine.report.ReportEngine;
import winkhouse.model.AbbinamentiModel;
import winkhouse.model.AffittiAnagraficheModel;
import winkhouse.model.AffittiModel;
import winkhouse.model.AffittiRateModel;
import winkhouse.model.AffittiSpeseModel;
import winkhouse.model.AgentiAppuntamentiModel;
import winkhouse.model.AgentiModel;
import winkhouse.model.AllegatiColloquiModel;
import winkhouse.model.AnagraficheAppuntamentiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.AttributeModel;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ColloquiModelVisiteCollection;
import winkhouse.model.ContattiModel;
import winkhouse.model.EntityModel;
import winkhouse.model.ImmagineModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.Immobili_AffittiModel;
import winkhouse.model.PromemoriaModel;
import winkhouse.model.ReportModel;
import winkhouse.model.StanzeImmobiliModel;
import winkhouse.orm.Agenti;
import winkhouse.orm.Immobili;
import winkhouse.perspective.AffittiPerspective;
import winkhouse.perspective.AgendaPerspective;
import winkhouse.perspective.AnagrafichePerspective;
import winkhouse.perspective.DatiBasePerspective;
import winkhouse.perspective.DesktopPerspective;
import winkhouse.perspective.ImmobiliPerspective;
import winkhouse.perspective.PermessiPerspective;
import winkhouse.perspective.ReportPerspective;
import winkhouse.view.affitti.DettaglioAffittiView;
import winkhouse.view.affitti.ListaAffittiView;
import winkhouse.view.agenda.CalendarioView;
import winkhouse.view.agenda.DettaglioAppuntamentoView;
import winkhouse.view.agenda.ListaAppuntamentiView;
import winkhouse.view.agenti.PopUpRicercaAgenti;
import winkhouse.view.anagrafica.AnagraficaTreeView;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.view.anagrafica.ImmobiliPropietaView;
import winkhouse.view.anagrafica.PopUpRicercaAnagrafica;
import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.common.ColloquiView;
import winkhouse.view.common.EAVView;
import winkhouse.view.common.MapView;
import winkhouse.view.common.RecapitiView;
import winkhouse.view.datibase.DatiBaseView;
import winkhouse.view.immobili.AnagrafichePropietarieView;
import winkhouse.view.immobili.DettaglioImmobileView;
import winkhouse.view.immobili.ImmaginiImmobiliView;
import winkhouse.view.immobili.ImmobiliTreeView;
import winkhouse.view.permessi.AgentiView;
import winkhouse.view.permessi.DettaglioPermessiAgenteView;
import winkhouse.view.report.DettaglioReportView;
import winkhouse.view.report.ReportTreeView;
import winkhouse.vo.AffittiAllegatiVO;
import winkhouse.vo.AffittiVO;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.DatiCatastaliVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.TipiAppuntamentiVO;
import winkhouse.vo.WinkSysVO;
import winkhouse.widgets.data.IPropertiesFieldDescriptor;
import winkhouse.wizard.ColloquiWizard;
import winkhouse.wizard.GCalendarSyncWizard;
import winkhouse.wizard.RicercaWizard;


public class WinkhouseUtils {

	public final static Image UNCHECK = Activator.getImageDescriptor("icons/chknone.png").createImage();
	public final static Image CHECK = Activator.getImageDescriptor("icons/chkall.png").createImage();
	
	public final static String USERDBPWD = "userdbpwd";
	public final static String DATABASE_PREFERENCE_FILE = "database.preference"; 
	public final static String POSIZIONEDB = "posizionedb";
	public final static String USERNAME = "username";
	public final static String PASSWORD = "password";
	public final static String IMAGEPATH = "imagepath";
	public final static String BUNDLEDB = "bundledb";
	
	public final static String ALLEGATIPATH = "allegatipath";
	public final static String REPORTTEMPLATEPATH = "reporttemplatepath";
	public final static String PLUGINSPATH = "pluginspath";
	public final static String STARTPERSPECTIVE = "startperspective";
	public final static String GOOGLETEXTINPUT = "googletextinput";
	public final static String GOOGLEBUTTON = "googlebutton";
	public final static String MAXCLOUDRESULT = "maxcloudresult";
	public final static boolean ARCHIVIO_CORRENTE = false;
	public final static boolean ARCHIVIO_STORICO = true;
	public final static String GCAL_COLOR_CACHE_FILE = "gcalcolors.cache";
	private final static String PATHKEYVIEW = "0023dsjietnnvpo98zzajfwe8c3r";
	
	public final static String IMMOBILI = ImmobiliModel.class.getName();
	public final static String ANAGRAFICHE = AnagraficheModel.class.getName();
	public final static String ANAGRAFICHEAPPUNTAMENTI = AnagraficheAppuntamentiModel.class.getName();
	public final static String COLLOQUI = ColloquiModel.class.getName();
	public final static String COLLOQUIVISITE = ColloquiModelVisiteCollection.class.getName();
	public final static String AGENTI = AgentiModel.class.getName();
	public final static String ALLEGATICOLLOQUIO = AllegatiColloquiModel.class.getName();
	public final static String ALLEGATIIMMOBILE = AllegatiImmobiliVO.class.getName();
	public final static String AGENTICOLLOQUIO = ColloquiAgentiModel_Age.class.getName();
	public final static String AGENTIAPPUNTAMENTI = AgentiAppuntamentiModel.class.getName();
	public final static String ANAGRAFICHECOLLOQUIO = ColloquiAnagraficheModel_Ang.class.getName();
	public final static String CONTATTI = ContattiModel.class.getName();
	public final static String IMMAGINI = ImmagineModel.class.getName();
	public final static String STANZEIMMOBILI = StanzeImmobiliModel.class.getName();
	public final static String CRITERIRICERCA = ColloquiCriteriRicercaVO.class.getName();
	public final static String ABBINAMENTI = AbbinamentiModel.class.getName();
	public final static String APPUNTAMENTI = AppuntamentiModel.class.getName();
	public final static String TIPIAPPUNTAMENTI = TipiAppuntamentiVO.class.getName();
	public final static String AFFITTI = AffittiModel.class.getName();
	public final static String AFFITTIALLEGATI = AffittiAllegatiVO.class.getName();
	public final static String AFFITTIANAGRAFICHE = AffittiAnagraficheModel.class.getName();
	public final static String AFFITTIIMMOBILI = Immobili_AffittiModel.class.getName();
	public final static String AFFITTIRATE = AffittiRateModel.class.getName();
	public final static String AFFITTISPESE = AffittiSpeseModel.class.getName();
	public final static String DATICATASTALI = DatiCatastaliVO.class.getName();
	public final static String CLASSIENERGETICHE = ClasseEnergeticaVO.class.getName();
	public final static String CAMPIPERSONALI = AttributeModel.class.getName();
	public final static String PROMEMORIA = PromemoriaModel.class.getName();
	
	public final static String FUNCTION_SEARCH = "search";
	public final static String FUNCTION_REPORT = "report";
	
	public final static short PROSPETTIVA = 0;
	public final static short VISTA = 1;
	public final static short DIALOG = 2;
	
	private PreferenceStore preferenceStore = null;
	private String storeFileName = Activator.getDefault().getStateLocation().toFile() + File.separator + DATABASE_PREFERENCE_FILE;
	private static WinkhouseUtils instance = null;
	
	private ArrayList<ObjectSearchGetters> immobileSearchGetters = null;
	private ArrayList<ObjectSearchGetters> immobileReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> anagraficheSearchGetters = null;
	private ArrayList<ObjectSearchGetters> anagraficheReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> colloquiSearchGetters = null;
	private ArrayList<ObjectSearchGetters> colloquiReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> contattiSearchGetters = null;
	private ArrayList<ObjectSearchGetters> contattiReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> agentiColloquioSearchGetters = null;
	private ArrayList<ObjectSearchGetters> agentiColloquioReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> allegatiColloquioSearchGetters = null;
	private ArrayList<ObjectSearchGetters> allegatiColloquioReportSearchGetters = null;	
	private ArrayList<ObjectSearchGetters> anagraficheColloquioSearchGetters = null;
	private ArrayList<ObjectSearchGetters> anagraficheColloquioReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> criteriRicercaSearchGetters = null;
	private ArrayList<ObjectSearchGetters> criteriRicercaReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> immagineSearchGetters = null;
	private ArrayList<ObjectSearchGetters> immagineReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> stanzeImmobileSearchGetters = null;
	private ArrayList<ObjectSearchGetters> stanzeImmobileReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> allegatiImmobileSearchGetters = null;
	private ArrayList<ObjectSearchGetters> allegatiImmobileReportSearchGetters = null;	
	private ArrayList<ObjectSearchGetters> abbinamentiSearchGetters = null;
	private ArrayList<ObjectSearchGetters> abbinamentiReportSearchGetters = null;	
	private ArrayList<ObjectSearchGetters> affittiSearchGetters = null;
	private ArrayList<ObjectSearchGetters> affittiReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> affittiAllegatiSearchGetters = null;
	private ArrayList<ObjectSearchGetters> affittiAllegatiReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> affittiRateReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> affittiSpeseReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> affittiAnagraficheReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> appuntamentiSearchGetters = null;
	private ArrayList<ObjectSearchGetters> appuntamentiReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> tipiappuntamentiReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> classienergeticheReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> immobileAffittiSearchGetters = null;
	private ArrayList<ObjectSearchGetters> datiCatastaliImmobileSearchGetters = null;
	private ArrayList<ObjectSearchGetters> datiCatastaliImmobileReportSearchGetters = null;
	private ArrayList<ObjectSearchGetters> campiPersonaliSearchGetters = null;
	private ArrayList<ObjectSearchGetters> campiPersonaliReportSearchGetters = null;

	private ArrayList<MarkerSpecialParam> makersSpecialParams = null;
	private Integer lastCodImmobileSelected = null;
	private Integer lastCodAnagraficaSelected = null;
	private String lastPerspectiveSelected = "";
	private String lastEntityTypeFocused = null;
	
	private Boolean tipoArchivio = ARCHIVIO_CORRENTE; 
	
	private ArrayList<Integer> codiciImmobili = null;
	private ArrayList<Integer> codiciAnagrafiche = null;
	
	private RicercaWizard ricercaWiz = null;
	
	private boolean isAnagraficheFiltered = false;	
	
	public static Integer PROPIETARI = 0;
	public static Integer RICHIEDENTI = 1;
	private Integer angraficheFilterType = RICHIEDENTI;
	
	private ArrayList<Mese> mesi = null;
	
	public boolean startConnectionErrorShow = false;
	
	private ArrayList<PerspectiveInfo> perspectiveInfo =null;
	private ArrayList<ViewInfo> viewInfo = null;
	private ArrayList<DialogInfo> dialogInfo = null;
	private Agenti loggedAgent = null;
	
	private HashMap<String,String> hm_winkSys = null; 
	
	private boolean isBundleDBRunning = false; 
	private ObjectContext cayenneContext = null;
	
	public static WinkhouseUtils getInstance(){
		if (instance == null){
			instance = new WinkhouseUtils();
		}
		return instance;
	}
	
	private WinkhouseUtils() {
		createPreferenceStore();
	}
	
	public ObjectContext getCayenneObjectContext() {
		if (this.cayenneContext == null) {
			this.cayenneContext = CayenneContextManager.getInstance().getContext();
		}
		return this.cayenneContext;
	}

	public void setCayenneObjectContext(ObjectContext oc) {
		this.cayenneContext = oc;
	}

	public ObjectContext getNewCayenneObjectContext() {
		
		return CayenneContextManager.getInstance().getContext();
				
	}

	public class Mese{
		
		private Integer key = null;
		private String nome = null;
		
		public Mese(Integer key,String nome){
			this.key = key;
			this.nome = nome;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}
		
	}
	
	public class ReportTypes{
		
		private String className = null;
		private String reportTypeName = null;
		
		public ReportTypes(String className,String reportTypeName){
			this.className = className;
			this.reportTypeName = reportTypeName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getReportTypeName() {
			return reportTypeName;
		}

		public void setReportTypeName(String reportTypeName) {
			this.reportTypeName = reportTypeName;
		} 
		
	}

	public class MarkerSpecialParam{
		
		private Integer codint = null;
		private String codstr = null;
		private String descrizione = null;
		
		public MarkerSpecialParam(Integer codint, String codstr, String descrizione){
			setCodint(codint);
			setCodstr(codstr);
			setDescrizione(descrizione);
		}

		public Integer getCodint() {
			return codint;
		}

		public void setCodint(Integer codint) {
			this.codint = codint;
		}

		public String getCodstr() {
			return codstr;
		}

		public void setCodstr(String codstr) {
			this.codstr = codstr;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}
		
	}
	
	public class ObjectSearchGetters implements IPropertiesFieldDescriptor{
		
		private Integer key = null;
		private String methodName = null;
		private String descrizione = null;
		private String columnName = null;
		private String parametrizedTypeName = null;
		private Boolean isPersonal = null;

		public ObjectSearchGetters(){
		}		
		
		public ObjectSearchGetters(Integer key,String methodName,String descrizione,String columnName,String parametrizedTypeName){
			setMethodName(methodName);
			setDescrizione(descrizione);
			setKey(key);
			setColumnName(columnName);
			setParametrizedTypeName(parametrizedTypeName);
			setIsPersonal(false);
		}

		public ObjectSearchGetters(Integer key,String methodName,String descrizione,String columnName,String parametrizedTypeName,Boolean isPersonal){
			this(key,methodName,descrizione,columnName,parametrizedTypeName);
			this.setIsPersonal(isPersonal);
		}
		
		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public String getParametrizedTypeName() {
			return parametrizedTypeName;
		}

		public void setParametrizedTypeName(String parametrizedTypeName) {
			this.parametrizedTypeName = parametrizedTypeName;
		}

		
		public Boolean getIsPersonal() {
			return isPersonal;
		}
		

		public void setIsPersonal(Boolean isPersonal) {
			this.isPersonal = isPersonal;
		}
		
	}
	
	public class PerspectiveInfo{
		
		private Image immagine = null;
		private String descrizione = null;
		private String id = null;
		
		public PerspectiveInfo(Image immagine, String descrizione, String id){
			this.immagine = immagine;
			this.descrizione = descrizione;
			this.id = id;
		}

		public Image getImmagine() {
			return immagine;
		}

		public void setImmagine(Image immagine) {
			this.immagine = immagine;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
				
	}
	
	public class ViewInfo{
		
		private Image immagine = null;
		private String descrizione = null;
		private String id = null;

		public ViewInfo(Image immagine, String descrizione, String id){
			this.immagine = immagine;
			this.descrizione = descrizione;
			this.id = id;
		}

		public Image getImmagine() {
			return immagine;
		}

		public void setImmagine(Image immagine) {
			this.immagine = immagine;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
		

	} 
	
	public class DialogInfo{
		
		private Image immagine = null;
		private String descrizione = null;
		private String id = null;

		public DialogInfo(Image immagine, String descrizione, String id){
			this.immagine = immagine;
			this.descrizione = descrizione;
			this.id = id;
		}

		public Image getImmagine() {
			return immagine;
		}

		public void setImmagine(Image immagine) {
			this.immagine = immagine;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
		

	} 
	
	private Comparator<ObjectSearchGetters> getSetComparator = new Comparator<ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters o1, ObjectSearchGetters o2) {
			if ((o1 != null) && (o2 != null)){
				if ((o1.getMethodName() != null) && (o2.getMethodName() != null)){
					return o1.methodName.compareTo(o2.getMethodName());
				}else if ((o1.getMethodName() == null) && (o2.getMethodName() != null)){
					return -1;
				}else if((o1.getMethodName() == null) && (o2.getMethodName() != null)){
					return 1;
				}else{return 0;} 				
			}else if ((o1 == null) && (o2 != null)){
				return -1;
			}else if((o1 == null) && (o2 != null)){
				return 1;
			}else{return 0;}
		}
		
	};

	private Comparator<ObjectSearchGetters> getSetComparatorDesc = new Comparator<ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters o1, ObjectSearchGetters o2) {
			return o1.getDescrizione().compareTo(o2.getDescrizione());
		}
		
	};
	
	private Comparator<ObjectSearchGetters> getSetComparatorId = new Comparator<ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters o1, ObjectSearchGetters o2) {
			if (o1.getKey().intValue() == o2.getKey().intValue()){
				return 0;
			}else if (o1.getKey().intValue() < o2.getKey().intValue()){
				return -1;
			}else{
				return 1;
			}
		}
		
	};

	
	private void createPreferenceStore(){

		preferenceStore = new PreferenceStore(storeFileName);
		
		try {
			preferenceStore.load();
			
			InetAddress addr = null;
			try {
				addr = InetAddress.getLocalHost();							
			} catch (UnknownHostException e) {				
				e.printStackTrace();
			}
			
			preferenceStore.setDefault(WinkhouseUtils.POSIZIONEDB, 
									   (addr != null)?addr.getHostAddress():"");

			preferenceStore.setDefault(WinkhouseUtils.USERNAME ,"SA");
			preferenceStore.setDefault(WinkhouseUtils.PASSWORD ,"");
			preferenceStore.setDefault(WinkhouseUtils.IMAGEPATH ,
									   Activator.getDefault()
									   			.getStateLocation()
									   			.toFile()
									   			.toString()+File.separator+"immagini");
			
			preferenceStore.setDefault(WinkhouseUtils.ALLEGATIPATH ,
									   Activator.getDefault()
									   			.getStateLocation()
									   			.toFile()
									   			.toString()+File.separator+"allegati");
			
			preferenceStore.setDefault(WinkhouseUtils.REPORTTEMPLATEPATH ,
									   Activator.getDefault()
									   			.getStateLocation()
									   			.toFile()
									   			.toString()+File.separator+"template");
			
//			preferenceStore.setDefault(WinkhouseUtils.CRIPTKEY,"winkhouse");
			
			preferenceStore.setDefault(WinkhouseUtils.PLUGINSPATH ,
					   				   Activator.getDefault()
					   				   			.getStateLocation()
					   				   			.toFile()
					   				   			.toString()+File.separator+"estensioni");
			
			preferenceStore.setDefault(WinkhouseUtils.STARTPERSPECTIVE,DesktopPerspective.ID);
			preferenceStore.setDefault(WinkhouseUtils.BUNDLEDB,true);
			
			preferenceStore.setDefault(WinkhouseUtils.USERDBPWD,"");
			preferenceStore.setDefault(WinkhouseUtils.MAXCLOUDRESULT, 20);
			preferenceStore.setDefault("winkcloudurls", "http://www.winkcloudquery.org");
			
		} catch (IOException e) {
			File f = new File(storeFileName);
			try {
				f.createNewFile();
				createPreferenceStore();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}		
	}

	public PreferenceStore getPreferenceStore() {
		if (preferenceStore == null){
			createPreferenceStore();
		}
		return preferenceStore;
	}

	public void setPreferenceStore(PreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
	}
	
	public void savePreference(){
		
		try {
			getPreferenceStore().save(new FileOutputStream(new File(storeFileName)),"");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public ArrayList<ObjectSearchGetters> getImmobileSearchGetters() {
		if (immobileSearchGetters == null){
			immobileSearchGetters = new ArrayList<ObjectSearchGetters>();
			immobileSearchGetters.add(new ObjectSearchGetters(-1,"MMqStanza","Mq stanza","SI.MQ",""));
			immobileSearchGetters.add(new ObjectSearchGetters(0,"MTipologiaStanza","tipologia stanza","SI.CODTIPOLOGIASTANZA",""));
			immobileSearchGetters.add(new ObjectSearchGetters(1,"getMq","metri quadrati","MQ",""));
			immobileSearchGetters.add(new ObjectSearchGetters(2,"getNumeroStanze","numero stanze","NS.NUMSTANZE",""));
			immobileSearchGetters.add(new ObjectSearchGetters(3,"getPrezzo","prezzo","PREZZO",""));
			immobileSearchGetters.add(new ObjectSearchGetters(4,"getMutuo","mutuo","MUTUO",""));
			immobileSearchGetters.add(new ObjectSearchGetters(5,"getSpese","spese","SPESE",""));
			immobileSearchGetters.add(new ObjectSearchGetters(6,"getZona","zona","ZONA",""));
			immobileSearchGetters.add(new ObjectSearchGetters(7,"getCitta","citta","CITTA",""));
			immobileSearchGetters.add(new ObjectSearchGetters(8,"getProvincia","provincia","PROVINCIA",""));
			immobileSearchGetters.add(new ObjectSearchGetters(9,"getCap","cap","CAP",""));
			immobileSearchGetters.add(new ObjectSearchGetters(10,"getAnnoCostruzione","anno costruzione","ANNOCOSTRUZIONE",""));
			immobileSearchGetters.add(new ObjectSearchGetters(11,"getDataLibero","data libero","DATALIBERO",""));
			immobileSearchGetters.add(new ObjectSearchGetters(12,"getCodTipologia","tipologia immobile","CODTIPOLOGIA",""));
			immobileSearchGetters.add(new ObjectSearchGetters(13,"getCodStato","stato conservativo","CODSTATO",""));
			immobileSearchGetters.add(new ObjectSearchGetters(14,"getCodRiscaldamento","riscaldamenti","CODRISCALDAMENTO",""));
			immobileSearchGetters.add(new ObjectSearchGetters(15,"getCodAgenteInseritore","agente inseritore","CODAGENTEINSERITORE",""));
			immobileSearchGetters.add(new ObjectSearchGetters(16,"getRif","codice riferimento","RIF",""));
			immobileSearchGetters.add(new ObjectSearchGetters(17,"(","(","",""));
			immobileSearchGetters.add(new ObjectSearchGetters(18,")",")","",""));
			immobileSearchGetters.add(new ObjectSearchGetters(19,"getCodClasseEnergetica","classe energetica","CODCLASSEENERGETICA",""));
			immobileSearchGetters.add(new ObjectSearchGetters(20,"getIndirizzo","indirizzo","INDIRIZZO",""));
			immobileSearchGetters.add(new ObjectSearchGetters(21,"getCodImmobile","Codice immobile","CODIMMOBILE",""));
			addCampiPersonalizzatiOSG(immobileSearchGetters, ImmobiliVO.class.getName(), 21);
		}
		return immobileSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getImmobileReportSearchGetters() {
		if (immobileReportSearchGetters == null){
			immobileReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			immobileReportSearchGetters.add(new ObjectSearchGetters(1,"getMq","metri quadrati","MQ",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(2,"getNumeroStanze","numero stanze","NS.NUMSTANZE",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(3,"getPrezzo","prezzo","PREZZO",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(4,"getMutuo","mutuo","MUTUO",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(5,"getSpese","spese","SPESE",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(6,"getZona","zona","ZONA",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(7,"getCitta","citta","CITTA",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(8,"getProvincia","provincia","PROVINCIA",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(9,"getCap","cap","CAP",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(10,"getAnnoCostruzione","anno costruzione","ANNOCOSTRUZIONE",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(11,"getDataLibero","data libero","DATALIBERO",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(12,"getCodTipologia","codice tipologia immobile","CODTIPOLOGIA",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(13,"getCodStato","codice stato conservativo","CODSTATO",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(14,"getCodRiscaldamento","codice riscaldamenti","CODRISCALDAMENTO",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(15,"getCodAgenteInseritore","codice agente inseritore","CODAGENTEINSERITORE",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(16,"getDescrizione","descrizione","DESCRIZIONE",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(17,"getRif","codice riferimento","RIF",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(18,"getImmagini","Lista immagini","",WinkhouseUtils.IMMAGINI));
			immobileReportSearchGetters.add(new ObjectSearchGetters(19,"getStanze","Lista stanze","",WinkhouseUtils.STANZEIMMOBILI));
			immobileReportSearchGetters.add(new ObjectSearchGetters(20,"getTipologiaImmobile","tipologia immobile","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(21,"getStatoConservativo","stato conservativo","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(22,"getRiscaldamento","riscaldamento","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(23,"getAgenteInseritore","agente inseritore","",WinkhouseUtils.AGENTI));
			immobileReportSearchGetters.add(new ObjectSearchGetters(24,"getAnagrafica","anagrafica","",WinkhouseUtils.ANAGRAFICHE));
			immobileReportSearchGetters.add(new ObjectSearchGetters(25,"getCodImmobile","codice immobile db","CODIMMOBILE",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(26,"getIndirizzo","indirizzo immobile","INDIRIZZO",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(27,"getDataInserimento","data inserimento immobile","DATAINSERIMENTO",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(28,"getMutuoDescrizione","descrizione mutuo","MUTUODESCRIZIONE",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(29,"getVisione","visionabile","VISIONE",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(30,"getVarie","varie","VARIE",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(31,"getAllegati","Lista allegati","",WinkhouseUtils.ALLEGATIIMMOBILE));
			immobileReportSearchGetters.add(new ObjectSearchGetters(32,"getColloquiVisiteReport","Lista colloqui","",WinkhouseUtils.COLLOQUI));
			immobileReportSearchGetters.add(new ObjectSearchGetters(33,"getAnagraficheAbbinate","Lista anagrafiche abbinate","",WinkhouseUtils.ABBINAMENTI));
			immobileReportSearchGetters.add(new ObjectSearchGetters(34,"getNomeCognomeAnagrafica","nome cognome anagrafica proprietario","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(35,"getCittaAnagrafica","citta anagrafica proprietario","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(36,"getIndirizzoAnagrafica","indirizzo anagrafica proprietario","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(37,"getPrimoRecapitoAnagrafica","primo recapito anagrafica proprietario","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(38,"getSecondoRecapitoAnagrafica","secondo recapito anagrafica proprietario","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(39,"getCodiceFiscaleAnagrafica","codice fiscale anagrafica proprietario","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(40,"getPartitaIvaAnagrafica","partita iva anagrafica proprietario","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(41,"getCapAnagrafica","cap anagrafica proprietario","",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(42,"getProvinciaAnagrafica","provincia anagrafica proprietario","",""));			
			immobileReportSearchGetters.add(new ObjectSearchGetters(43,"getDatiCatastali","dati catastali","",WinkhouseUtils.DATICATASTALI));
			immobileReportSearchGetters.add(new ObjectSearchGetters(44,"getCodClasseEnergetica","codice classe energetica","CODCLASSEENERGETICA",""));
			immobileReportSearchGetters.add(new ObjectSearchGetters(45,"getClasseEnergetica","classe energetica","",WinkhouseUtils.CLASSIENERGETICHE));
			addCampiPersonalizzatiOSG(immobileReportSearchGetters, ImmobiliVO.class.getName(), 46);
			
		}
		return immobileReportSearchGetters;
	}

	protected void addCampiPersonalizzatiOSG(ArrayList<ObjectSearchGetters> arrayToFill, String entityName, int firstIdValue){
		
		AttributeDAO aDAO = new AttributeDAO();
		EntityDAO eDAO = new EntityDAO();
//		EntityModel em = eDAO.getEntityByClassName(entityName);
//		ArrayList<AttributeModel> al_attribute = aDAO.getAttributeByIdEntity(em.getIdClassEntity());
		ArrayList<AttributeModel> al_attribute = new ArrayList<AttributeModel>(); 
		int count = firstIdValue;
		for (AttributeModel attribute : al_attribute){
			arrayToFill.add(new ObjectSearchGetters(count,
													attribute.getIdAttribute().toString(),
													attribute.getAttributeName(),
													"",
													"",
													true));
		}
		
		
	} 
	
	public ObjectSearchGetters findObjectSearchGettersByMethodName(String methodName, String entity, String type){
		ArrayList<ObjectSearchGetters> al = null;
		if (entity.equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
			if (type.equalsIgnoreCase(WinkhouseUtils.FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getImmobileSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(WinkhouseUtils.FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getImmobileReportSearchGetters().clone();
			}			
		}
		if (entity.equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
			if (type.equalsIgnoreCase(WinkhouseUtils.FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(WinkhouseUtils.FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheReportSearchGetters().clone();
			}			
		}
		if (entity.equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
			if (type.equalsIgnoreCase(WinkhouseUtils.FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getColloquiSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(WinkhouseUtils.FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getColloquiReportSearchGetters().clone();
			}			
		}
		if (entity.equalsIgnoreCase(CONTATTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getContattiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getContattiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(AGENTICOLLOQUIO)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAgentiColloquioReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAgentiColloquioSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(ALLEGATICOLLOQUIO)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAllegatiColloquioReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAllegatiColloquioSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(ANAGRAFICHECOLLOQUIO)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheColloquioReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheColloquioSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(CRITERIRICERCA)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getCriteriRicercaReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getCriteriRicercaSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(IMMAGINI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getImmagineReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getImmagineSearchGetters().clone();
			}
		}		
		if (entity.equalsIgnoreCase(STANZEIMMOBILI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getStanzeImmobileReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getStanzeImmobileSearchGetters().clone();
			}
		}		
		if (entity.equalsIgnoreCase(APPUNTAMENTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAppuntamentiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAppuntamentiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(AFFITTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiSearchGetters().clone();
			}
		}		
		if (entity.equalsIgnoreCase(AFFITTIALLEGATI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAllegatiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAllegatiSearchGetters().clone();
			}
		}		
		if (entity.equalsIgnoreCase(AFFITTIIMMOBILI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getImmobileAffittiSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getImmobileAffittiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(AFFITTIANAGRAFICHE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAnagraficheReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAnagraficheReportSearchGetters().clone();
			}
		}		
		if (entity.equalsIgnoreCase(ABBINAMENTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiReportSearchGetters().clone();
			}
		}		
		if (entity.equalsIgnoreCase(DATICATASTALI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getDatiCatastaliImmobileReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getDatiCatastaliImmobileSearchGetters().clone();
			}
		}		
		if (entity.equalsIgnoreCase(TIPIAPPUNTAMENTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getTipiAppuntamentiReportSearchGetters().clone();
			}
		}		
		if (entity.equalsIgnoreCase(CLASSIENERGETICHE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getClassiEnergeticheReportSearchGetters().clone();
			}
		}		
		if (entity.equalsIgnoreCase(CAMPIPERSONALI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getCampiPersonaliReportSearchGetters().clone();
			}
		}		
		
		
		if (al != null){
			Collections.sort(al, getSetComparator);
			ObjectSearchGetters test = new ObjectSearchGetters(0,methodName,"","","");
			int index = Collections.binarySearch(al, test, getSetComparator);
			if (index > -1){
				return al.get(index);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public Class getReturnTypeGetterMethod(Class classe,String methodName){
		
		Class c = null;
		if (!methodName.equalsIgnoreCase("")){
			try {
				Method m = classe.getMethod(methodName, null);
				c = m.getReturnType();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return c;
	}
	
	public ArrayList<ObjectSearchGetters> getAnagraficheReportSearchGetters() {
		if (anagraficheReportSearchGetters == null){
			anagraficheReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(1,"getNome","Nome","NOME",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(2,"getCognome","Cognome","COGNOME",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(3,"getIndirizzo","indirizzo","INDIRIZZO",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(4,"getProvincia","provincia","PROVINCIA",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(5,"getCap","cap","CAP",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(6,"getCitta","citta","CITTA",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(7,"getDataInserimento","Data inserimento","DATAINSERIMENTO",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(8,"getCommento","commento","COMMENTO",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(9,"getCodAnagrafica","codice anagrafica","CODANAGRAFICA",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(10,"getCodClasseCliente","Codice categoria cliente","CODCLASSECLIENTE",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(11,"getCodAgenteInseritore","Codice agente inseritore","CODAGENTEINSERITORE",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(12,"getClasseCliente","categoria cliente","",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(13,"getAgenteInseritore","agente inseritore","",WinkhouseUtils.AGENTI));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(14,"getContatti","Lista contatti","",WinkhouseUtils.CONTATTI));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(15,"getRagioneSociale","ragione sociale","RAGSOC",""));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(16,"getColloquiReport","Lista colloqui","",WinkhouseUtils.COLLOQUI));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(17,"getImmobili","Lista immobili proprieta","",WinkhouseUtils.IMMOBILI));
			anagraficheReportSearchGetters.add(new ObjectSearchGetters(18,"getAbbinamenti","Lista immobili abbinati","",WinkhouseUtils.ABBINAMENTI));
			
			addCampiPersonalizzatiOSG(anagraficheReportSearchGetters, AnagraficheVO.class.getName(), 19);
		}
		return anagraficheReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAnagraficheSearchGetters() {
		if (anagraficheSearchGetters == null){
			anagraficheSearchGetters = new ArrayList<ObjectSearchGetters>();
			anagraficheSearchGetters.add(new ObjectSearchGetters(1,"getNome","Nome","NOME",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(2,"getCognome","Cognome","COGNOME",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(3,"getIndirizzo","indirizzo","INDIRIZZO",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(4,"getProvincia","provincia","PROVINCIA",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(5,"getCap","cap","CAP",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(6,"getCitta","citta","CITTA",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(7,"getDataInserimento","dataInserimento","DATAINSERIMENTO",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(8,"getCommento","commento","COMMENTO",""));
//			anagraficheSearchGetters.add(new ObjectSearchGetters(9,"getStorico","storico","STORICO",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(10,"getCodClasseCliente","categoria cliente","CODCLASSECLIENTE",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(11,"getCodAgenteInseritore","Agente inseritore","CODAGENTEINSERITORE",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(12,"getDescrizioneClasseCliente","Descrizione categoria cliente","CC.DESCRIZIONE",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(13,"getNomeAgenteInseritore","Nome agente inseritore","AG.NOME",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(14,"getCognomeAgenteInseritore","Cognome agente inseritore","AG.COGNOME",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(15,"getContatti","Lista contatti","C.CONTATTO",WinkhouseUtils.CONTATTI));
			anagraficheSearchGetters.add(new ObjectSearchGetters(16,"(","(","",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(17,")",")","",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(18,"getCodiceFiscale","Codice fiscale","CODICEFISCALE",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(19,"getPartitaIva","Partita iva","PIVA",""));
			anagraficheSearchGetters.add(new ObjectSearchGetters(21,"getCodAnagrafica","Codice anagrafica","CODANAGRAFICA",""));
			addCampiPersonalizzatiOSG(anagraficheSearchGetters, AnagraficheVO.class.getName(), 20);
		}
		return anagraficheSearchGetters;
	}
	
	public ArrayList<ObjectSearchGetters> getColloquiReportSearchGetters() {
		if (colloquiReportSearchGetters == null){
			colloquiReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			colloquiReportSearchGetters.add(new ObjectSearchGetters(1,"getCodColloquio","Codice colloquio","CODCOLLOQUIO",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(2,"getDescrizione","Descrizione","DESCRIZIONE",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(3,"getCodAgenteInseritore","Codice agente inseritore","CODAGENTEINSERITORE",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(4,"getCodImmobileAbbinato","Codice immobile abbinato","CODIMMOBILEABBINATO",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(5,"getCodTipologiaColloquio","Codice tipologia colloquio","CODTIPOLOGIACOLLOQUIO",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(6,"getDataInserimento","Data inserimento","DATAINSERIMENTO",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(7,"getDataColloquio","Data colloquio","DATACOLLOQUIO",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(8,"getLuogoIncontro","Luogo","LUOGO",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(9,"getScadenziere","In agenda","SCADENZIERE",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(10,"getCommentoAgenzia","Commento agenzia","COMMENTOAGENZIA",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(11,"getCommentoCliente","Commento cliente","COMMENTOCLIENTE",""));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(12,"getAgenteInseritore","agente inseritore","",WinkhouseUtils.AGENTI));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(13,"getAgenti","Lista agenti","",WinkhouseUtils.AGENTICOLLOQUIO));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(14,"getAllegati","Lista allegati","",WinkhouseUtils.ALLEGATICOLLOQUIO));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(15,"getAnagrafiche","Lista anagrafiche","",WinkhouseUtils.ANAGRAFICHECOLLOQUIO));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(16,"getCriteriRicerca","Lista criteri ricerca","",WinkhouseUtils.CRITERIRICERCA));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(17,"getImmobileAbbinato","immobile visita","",WinkhouseUtils.IMMOBILI));
			colloquiReportSearchGetters.add(new ObjectSearchGetters(18,"getTipologia","tipologia colloquio","",""));
			addCampiPersonalizzatiOSG(colloquiReportSearchGetters, ColloquiVO.class.getName(), 19);
		}
		return colloquiReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getColloquiSearchGetters() {
		if (colloquiSearchGetters == null){
			colloquiSearchGetters = new ArrayList<ObjectSearchGetters>();
			colloquiSearchGetters.add(new ObjectSearchGetters(1,"getCodColloquio","Codice colloquio","CODCOLLOQUIO",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(2,"getDescrizione","Descrizione","DESCRIZIONE",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(3,"getCodAgenteInseritore","Codice agente inseritore","CODAGENTEINSERITORE",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(4,"getCodImmobileAbbinato","Codice immobile abbinato","CODIMMOBILEABBINATO",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(5,"getCodTipologiaColloquio","Codice tipologia colloquio","CODTIPOLOGIACOLLOQUIO",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(6,"getDataInserimento","Data inserimento","DATAINSERIMENTO",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(7,"getDataColloquio","Data colloquio","DATACOLLOQUIO",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(8,"getLuogoIncontro","Luogo","LUOGO",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(9,"getScadenziere","In agenda","SCADENZIERE",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(10,"getCommentoAgenzia","Commento agenzia","COMMENTOAGENZIA",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(11,"getCommentoCliente","Commento cliente","COMMENTOCLIENTE",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(12,"getNomeCognomeAgenteInseritore","Nome Cognome agente inseritore","",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(12,"getAgenteInseritore","agente inseritore","",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(13,"getAgenti","Lista agenti","",WinkhouseUtils.AGENTICOLLOQUIO));
			colloquiSearchGetters.add(new ObjectSearchGetters(14,"getAllegati","Lista allegati","",WinkhouseUtils.ALLEGATICOLLOQUIO));
			colloquiSearchGetters.add(new ObjectSearchGetters(15,"getAnagrafiche","Lista anagrafiche","",WinkhouseUtils.ANAGRAFICHECOLLOQUIO));			
			colloquiSearchGetters.add(new ObjectSearchGetters(16,"getCriteriRicerca","Lista criteri ricerca","",WinkhouseUtils.CRITERIRICERCA));
			colloquiSearchGetters.add(new ObjectSearchGetters(17,"(","(","",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(18,")",")","",""));
			colloquiSearchGetters.add(new ObjectSearchGetters(19,"getImmobileAbbinato","immobile","",WinkhouseUtils.IMMOBILI));
			addCampiPersonalizzatiOSG(colloquiSearchGetters, ColloquiVO.class.getName(), 18);
		}
		return colloquiSearchGetters;
	}
	
	public ArrayList<ObjectSearchGetters> getContattiReportSearchGetters() {
		if (contattiReportSearchGetters == null){
			contattiReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			contattiReportSearchGetters.add(new ObjectSearchGetters(1,"getContatto","Contatto","",""));
			contattiReportSearchGetters.add(new ObjectSearchGetters(2,"getDesTipologiaContatto","Tipologia contatto","",""));
			contattiReportSearchGetters.add(new ObjectSearchGetters(3,"getAnagrafica","Anagrafica contatto","",WinkhouseUtils.ANAGRAFICHE));
			contattiReportSearchGetters.add(new ObjectSearchGetters(4,"getAgente","Agente contatto","",WinkhouseUtils.AGENTI));
		}
		return contattiReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getContattiSearchGetters() {
		if (contattiSearchGetters == null){
			contattiSearchGetters = new ArrayList<ObjectSearchGetters>();
			contattiSearchGetters.add(new ObjectSearchGetters(1,"getContatto","Contatto","",""));
			contattiSearchGetters.add(new ObjectSearchGetters(2,"getDesTipologiaContatto","Tipologia contatto","",""));
			contattiSearchGetters.add(new ObjectSearchGetters(3,"getDescrizioneproprietario","Proprietario contatto","",""));
			contattiSearchGetters.add(new ObjectSearchGetters(4,"(","(","",""));
			contattiSearchGetters.add(new ObjectSearchGetters(5,")",")","",""));
		}
		return contattiSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAgentiColloquioReportSearchGetters() {
		if (agentiColloquioReportSearchGetters == null){
			agentiColloquioReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			agentiColloquioReportSearchGetters.add(new ObjectSearchGetters(1,"getCodColloquioAgenti","codice colloquio agenti","CODCOLLOQUIOAGENTE",""));
			agentiColloquioReportSearchGetters.add(new ObjectSearchGetters(2,"getCodColloquio","codice colloquio","CODCOLLOQUIO",""));
			agentiColloquioReportSearchGetters.add(new ObjectSearchGetters(3,"getCodAgente","codice agente","CODAGENTE",""));
			agentiColloquioReportSearchGetters.add(new ObjectSearchGetters(4,"getCommento","commento","COMMENTO",""));
			agentiColloquioReportSearchGetters.add(new ObjectSearchGetters(5,"getAgente","agente","",AGENTI));
			agentiColloquioReportSearchGetters.add(new ObjectSearchGetters(6,"getColloquio","colloquio","",COLLOQUI));
		}
		return agentiColloquioReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAgentiColloquioSearchGetters() {
		if (agentiColloquioSearchGetters == null){
			agentiColloquioSearchGetters = new ArrayList<ObjectSearchGetters>();
			agentiColloquioSearchGetters.add(new ObjectSearchGetters(1,"getCodColloquioAgenti","codice colloquio agenti","CODCOLLOQUIOAGENTE",""));
			agentiColloquioSearchGetters.add(new ObjectSearchGetters(2,"getCodColloquio","codice colloquio","CODCOLLOQUIO",""));
			agentiColloquioSearchGetters.add(new ObjectSearchGetters(3,"getCodAgente","codice agente","CODAGENTE",""));
			agentiColloquioSearchGetters.add(new ObjectSearchGetters(4,"getCommento","commento","COMMENTO",""));
			agentiColloquioSearchGetters.add(new ObjectSearchGetters(5,"getAgente","agente","",AGENTI));
			agentiColloquioSearchGetters.add(new ObjectSearchGetters(6,"getColloquio","colloquio","",COLLOQUI));
			agentiColloquioSearchGetters.add(new ObjectSearchGetters(7,"(","(","",""));
			agentiColloquioSearchGetters.add(new ObjectSearchGetters(8,")",")","",""));
		}
		return agentiColloquioSearchGetters;
	}
	
	public ArrayList<ObjectSearchGetters> getAllegatiColloquioReportSearchGetters() {
		if (allegatiColloquioReportSearchGetters == null){
			allegatiColloquioReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			allegatiColloquioReportSearchGetters.add(new ObjectSearchGetters(1,"getDescrizione","Descrizione","",""));
			allegatiColloquioReportSearchGetters.add(new ObjectSearchGetters(2,"getCodColloquio","Codice colloquio","",""));
			allegatiColloquioReportSearchGetters.add(new ObjectSearchGetters(3,"getDesColloquio","Descrizione colloquio","",""));
			allegatiColloquioReportSearchGetters.add(new ObjectSearchGetters(4,"getNome","Nome","",""));
		}
		return allegatiColloquioReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAllegatiColloquioSearchGetters() {
		if (allegatiColloquioSearchGetters == null){
			allegatiColloquioSearchGetters = new ArrayList<ObjectSearchGetters>();
			allegatiColloquioSearchGetters.add(new ObjectSearchGetters(1,"getDescrizione","Descrizione","",""));
			allegatiColloquioSearchGetters.add(new ObjectSearchGetters(2,"getCodColloquio","Codice colloquio","",""));
			allegatiColloquioSearchGetters.add(new ObjectSearchGetters(3,"getDesColloquio","Descrizione colloquio","",""));
			allegatiColloquioSearchGetters.add(new ObjectSearchGetters(4,"getNome","Nome","",""));
			allegatiColloquioSearchGetters.add(new ObjectSearchGetters(5,"(","(","",""));
			allegatiColloquioSearchGetters.add(new ObjectSearchGetters(6,")",")","",""));
		}
		return allegatiColloquioSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAnagraficheColloquioReportSearchGetters() {
		if (anagraficheColloquioReportSearchGetters == null){
			anagraficheColloquioReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			anagraficheColloquioReportSearchGetters.add(new ObjectSearchGetters(1,"getCodColloquioAnagrafiche","codice colloquio anagrafica","CODCOLLOQUIANAGRAFICHE",""));
			anagraficheColloquioReportSearchGetters.add(new ObjectSearchGetters(2,"getCodColloquio","Codice colloquio","CODCOLLOQUIO",""));
			anagraficheColloquioReportSearchGetters.add(new ObjectSearchGetters(3,"getCodAnagrafica","Codice anagrafica","CODANAGRAFICA",""));
			anagraficheColloquioReportSearchGetters.add(new ObjectSearchGetters(4,"getCommento","commento","COMMENTO",""));
			anagraficheColloquioReportSearchGetters.add(new ObjectSearchGetters(5,"getAnagrafica","anagrafica","",WinkhouseUtils.ANAGRAFICHE));
			anagraficheColloquioReportSearchGetters.add(new ObjectSearchGetters(6,"getColloquio","colloquio","",WinkhouseUtils.COLLOQUI));
		}
		return anagraficheColloquioReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAnagraficheColloquioSearchGetters() {
		if (anagraficheColloquioSearchGetters == null){
			anagraficheColloquioSearchGetters = new ArrayList<ObjectSearchGetters>();
			anagraficheColloquioSearchGetters.add(new ObjectSearchGetters(1,"getCodColloquioAnagrafiche","codice colloquio anagrafica","CODCOLLOQUIANAGRAFICHE",""));
			anagraficheColloquioSearchGetters.add(new ObjectSearchGetters(2,"getCodColloquio","Codice colloquio","CODCOLLOQUIO",""));
			anagraficheColloquioSearchGetters.add(new ObjectSearchGetters(3,"getCodAnagrafica","Codice anagrafica","CODANAGRAFICA",""));
			anagraficheColloquioSearchGetters.add(new ObjectSearchGetters(4,"getAnagrafica","anagrafica","",WinkhouseUtils.ANAGRAFICHE));
			anagraficheColloquioSearchGetters.add(new ObjectSearchGetters(5,"getColloquio","colloquio","",WinkhouseUtils.COLLOQUI));
			anagraficheColloquioSearchGetters.add(new ObjectSearchGetters(6,"(","(","",""));
			anagraficheColloquioSearchGetters.add(new ObjectSearchGetters(7,")",")","",""));
		}
		return anagraficheColloquioSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getCriteriRicercaReportSearchGetters() {
		if (criteriRicercaReportSearchGetters == null){
			criteriRicercaReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			criteriRicercaReportSearchGetters.add(new ObjectSearchGetters(1,"getGetterMethodName","Nome metodo","",""));
			criteriRicercaReportSearchGetters.add(new ObjectSearchGetters(2,"getCodColloquio","Codice colloquio","",""));
			criteriRicercaReportSearchGetters.add(new ObjectSearchGetters(3,"getFromValue","valore da","",""));
			criteriRicercaReportSearchGetters.add(new ObjectSearchGetters(4,"getToValue","valore a","",""));
			criteriRicercaReportSearchGetters.add(new ObjectSearchGetters(5,"getLogicalOperator","operatore logico","",""));
			criteriRicercaReportSearchGetters.add(new ObjectSearchGetters(6,"getMethodDescription","descrizione","",""));
		}
		return criteriRicercaReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getCriteriRicercaSearchGetters() {
		if (criteriRicercaSearchGetters == null){
			criteriRicercaSearchGetters = new ArrayList<ObjectSearchGetters>();
			criteriRicercaSearchGetters.add(new ObjectSearchGetters(1,"getGetterMethodName","Nome metodo","",""));
			criteriRicercaSearchGetters.add(new ObjectSearchGetters(2,"getCodColloquio","Codice colloquio","",""));
			criteriRicercaSearchGetters.add(new ObjectSearchGetters(3,"getFromValue","valore da","",""));
			criteriRicercaSearchGetters.add(new ObjectSearchGetters(4,"getToValue","valore a","",""));
			criteriRicercaSearchGetters.add(new ObjectSearchGetters(5,"getLogicalOperator","operatore logico","",""));
			criteriRicercaSearchGetters.add(new ObjectSearchGetters(6,"getMethodDescription","descrizione","",""));
			criteriRicercaSearchGetters.add(new ObjectSearchGetters(7,"(","(","",""));
			criteriRicercaSearchGetters.add(new ObjectSearchGetters(8,")",")","",""));
		}
		return criteriRicercaSearchGetters;
	}

    public boolean copiaFile(String pathOrigine, String pathDestinazione) {
		  
		    boolean returnValue = true;
			try {
				File destinazione = new File(pathDestinazione);
				
				if (!destinazione.exists()){				
					returnValue = doCopy(pathOrigine, pathDestinazione);
				}else{
					if (MessageDialog.openConfirm(Activator.getDefault()
														   .getWorkbench().getActiveWorkbenchWindow().getShell(), 
												  "File esistente", 
												  "Negli archivi � presente un file con lo stesso nome : \n" +
												  pathDestinazione + "\n" +
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
	  
	private boolean doCopy(String pathOrigine, String pathDestinazione) {
		  
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

	public Integer getLastCodImmobileSelected() {
		return lastCodImmobileSelected;
	}

	public void setLastCodImmobileSelected(Integer lastCodImmobileSelected) {
		this.lastCodImmobileSelected = lastCodImmobileSelected;
	}

	public Boolean getTipoArchivio() {
		return tipoArchivio;
	}

	public void setTipoArchivio(Boolean tipoArchivio) {
		this.tipoArchivio = tipoArchivio;
	}

	public ArrayList<MarkerSpecialParam> getMakersSpecialParams() {
		if (makersSpecialParams == null){
			makersSpecialParams = new ArrayList<MarkerSpecialParam>();
			makersSpecialParams.add(new MarkerSpecialParam(1,"LOC","Locandina"));
		}
		return makersSpecialParams;
	}
	
	public String getMethodsDescription(String methodNames,Class c){
		
		String returnValue = "";
		
		if (c.getName().equalsIgnoreCase(AnagraficheModel.class.getName())){
			
			Collections.sort(anagraficheReportSearchGetters,getSetComparator);
			String[] metodi = methodNames.split(",");
			ObjectSearchGetters osg = new ObjectSearchGetters();
			
			for (int i = 0; i < metodi.length; i++) {
				osg.setMethodName(metodi[i]);
				int index = Collections.binarySearch(anagraficheReportSearchGetters, osg, getSetComparator);
				if (index > -1){
					returnValue += (i < metodi.length -1)
								    ? ((ObjectSearchGetters)anagraficheReportSearchGetters.get(index)).getDescrizione() + ","
								    : ((ObjectSearchGetters)anagraficheReportSearchGetters.get(index)).getDescrizione();
				}
			}
			
		}
		if (c.getName().equalsIgnoreCase(ImmobiliModel.class.getName())){
			
			Collections.sort(immobileReportSearchGetters,getSetComparator);
			String[] metodi = methodNames.split(",");
			ObjectSearchGetters osg = new ObjectSearchGetters();
			
			for (int i = 0; i < metodi.length; i++) {
				osg.setMethodName(metodi[i]);
				int index = Collections.binarySearch(immobileReportSearchGetters, osg, getSetComparator);
				if (index > -1){
					returnValue += (i < metodi.length -1)
								    ? ((ObjectSearchGetters)immobileReportSearchGetters.get(index)).getDescrizione() + ","
								    : ((ObjectSearchGetters)immobileReportSearchGetters.get(index)).getDescrizione();
				}
			}

		}
		if (c.getName().equalsIgnoreCase(ColloquiModel.class.getName())){
			
			Collections.sort(colloquiReportSearchGetters,getSetComparator);
			String[] metodi = methodNames.split(",");
			ObjectSearchGetters osg = new ObjectSearchGetters();
			
			for (int i = 0; i < metodi.length; i++) {
				osg.setMethodName(metodi[i]);
				int index = Collections.binarySearch(colloquiReportSearchGetters, osg, getSetComparator);
				if (index > -1){
					returnValue += (i < metodi.length -1)
								    ? ((ObjectSearchGetters)colloquiReportSearchGetters.get(index)).getDescrizione() + ","
								    : ((ObjectSearchGetters)colloquiReportSearchGetters.get(index)).getDescrizione();
				}
			}

		}
		
		return returnValue;
	}
	
	public String translateNameToMethodNames(String names,String entity,String type){
		String returnValue = "";
		String[] namedesc = names.split(",");
		ArrayList<ObjectSearchGetters> al = null;
		
		if (entity.equalsIgnoreCase(IMMOBILI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getImmobileReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getImmobileSearchGetters().clone();				
			}
		}
		if (entity.equalsIgnoreCase(ANAGRAFICHE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheSearchGetters().clone();
			}			
		}
		if (entity.equalsIgnoreCase(COLLOQUI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getColloquiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getColloquiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(CONTATTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getContattiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getContattiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(AGENTICOLLOQUIO)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAgentiColloquioReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAgentiColloquioSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(ALLEGATICOLLOQUIO)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAllegatiColloquioReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAllegatiColloquioSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(ANAGRAFICHECOLLOQUIO)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheColloquioReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheColloquioSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(CRITERIRICERCA)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getCriteriRicercaReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getCriteriRicercaSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(IMMAGINI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getImmagineReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getImmagineSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(STANZEIMMOBILI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getStanzeImmobileReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getStanzeImmobileSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(ALLEGATIIMMOBILE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAllegatiImmobileReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAllegatiImmobileSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(ABBINAMENTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(AFFITTIRATE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiRateReportSearchGetters().clone();
			}
/*			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiRateSearchGetters().clone();
			}*/
		}
		if (entity.equalsIgnoreCase(AFFITTISPESE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiSpeseReportSearchGetters().clone();
			}
/*			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiSearchGetters().clone();
			}*/
		}
		if (entity.equalsIgnoreCase(AFFITTIANAGRAFICHE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAnagraficheReportSearchGetters().clone();
			}
/*			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiSearchGetters().clone();
			}*/
		}
		if (entity.equalsIgnoreCase(DATICATASTALI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getDatiCatastaliImmobileReportSearchGetters().clone();
			}
/*			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiSearchGetters().clone();
			}*/
		}
		if (entity.equalsIgnoreCase(TIPIAPPUNTAMENTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getTipiAppuntamentiReportSearchGetters().clone();
			}
/*			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiSearchGetters().clone();
			}*/
		}
		if (entity.equalsIgnoreCase(CLASSIENERGETICHE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getClassiEnergeticheReportSearchGetters().clone();
			}
/*			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiSearchGetters().clone();
			}*/
		}
		
		
		if (al != null){
			Collections.sort(al, getSetComparatorDesc);
			for (int i = 0; i < namedesc.length; i++) {
				ObjectSearchGetters osg = new ObjectSearchGetters();
				osg.setDescrizione(namedesc[i]);
				int index = Collections.binarySearch(al, osg, getSetComparatorDesc);
				if (index > -1){
					if (i<namedesc.length-1){
						returnValue += al.get(index).getMethodName() + ",";
					}else{
						returnValue += al.get(index).getMethodName();
					}
				}
			}
		}

		return returnValue;
	}

	public String translateMethodNamesToNames(String methodNames,String entity,String type){
		String returnValue = "";
		String[] namedesc = methodNames.split(",");
		ArrayList<ObjectSearchGetters> al = null;
		
		if (entity.equalsIgnoreCase(IMMOBILI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getImmobileReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getImmobileSearchGetters().clone();				
			}
		}
		if (entity.equalsIgnoreCase(ANAGRAFICHE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheSearchGetters().clone();
			}			
		}
		if (entity.equalsIgnoreCase(COLLOQUI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getColloquiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getColloquiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(CONTATTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getContattiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getContattiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(AGENTICOLLOQUIO)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAgentiColloquioReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAgentiColloquioSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(ALLEGATICOLLOQUIO)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAllegatiColloquioReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAllegatiColloquioSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(ANAGRAFICHECOLLOQUIO)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheColloquioReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAnagraficheColloquioSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(CRITERIRICERCA)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getCriteriRicercaReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getCriteriRicercaSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(IMMAGINI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getImmagineReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getImmagineSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(STANZEIMMOBILI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getStanzeImmobileReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getStanzeImmobileSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(ALLEGATIIMMOBILE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAllegatiImmobileReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAllegatiImmobileSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(ABBINAMENTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAbbinamentiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(APPUNTAMENTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAppuntamentiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAppuntamentiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(AFFITTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiSearchGetters().clone();
			}
		}		
		if (entity.equalsIgnoreCase(AFFITTIALLEGATI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAllegatiReportSearchGetters().clone();
			}
			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAllegatiSearchGetters().clone();
			}
		}
		if (entity.equalsIgnoreCase(AFFITTIRATE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiRateReportSearchGetters().clone();
			}
/*			if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAllegatiSearchGetters().clone();
			}*/
		}
		if (entity.equalsIgnoreCase(AFFITTISPESE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiSpeseReportSearchGetters().clone();
			}
			/*if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAllegatiSearchGetters().clone();
			}*/
		}
		if (entity.equalsIgnoreCase(AFFITTIANAGRAFICHE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAnagraficheReportSearchGetters().clone();
			}
			/*if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAllegatiSearchGetters().clone();
			}*/
		}
		if (entity.equalsIgnoreCase(DATICATASTALI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getDatiCatastaliImmobileReportSearchGetters().clone();
			}
			/*if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAllegatiSearchGetters().clone();
			}*/
		}
		if (entity.equalsIgnoreCase(TIPIAPPUNTAMENTI)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getTipiAppuntamentiReportSearchGetters().clone();
			}
			/*if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAllegatiSearchGetters().clone();
			}*/
		}
		if (entity.equalsIgnoreCase(CLASSIENERGETICHE)){
			if (type.equalsIgnoreCase(FUNCTION_REPORT)){
				al = (ArrayList<ObjectSearchGetters>)getTipiAppuntamentiReportSearchGetters().clone();
			}
			/*if (type.equalsIgnoreCase(FUNCTION_SEARCH)){
				al = (ArrayList<ObjectSearchGetters>)getAffittiAllegatiSearchGetters().clone();
			}*/
		}
		
		
		if (al != null){
			Collections.sort(al, getSetComparator);
			for (int i = 0; i < namedesc.length; i++) {
				ObjectSearchGetters osg = new ObjectSearchGetters();
				osg.setMethodName(namedesc[i]);
				int index = Collections.binarySearch(al, osg, getSetComparator);
				if (index > -1){
					if (i<namedesc.length-1){
						returnValue += al.get(index).getDescrizione() + ",";
					}else{
						returnValue += al.get(index).getDescrizione();
					}
				}
			}
		}

		return returnValue;
	}
	
	public ArrayList<ObjectSearchGetters> getObjFromStringList(String stringList, String entity,String type){
		
		ArrayList<ObjectSearchGetters> returnValue = new ArrayList<ObjectSearchGetters>();
		String[] arr = stringList.split(",");
		for (int i = 0; i < arr.length; i++) {
			ObjectSearchGetters osg = findObjectSearchGettersByMethodName(arr[i], entity, type);
			if (osg != null){
				returnValue.add(osg);
			}
		}		
		
		return returnValue;
	}

	public Integer getLastCodAnagraficaSelected() {
		return lastCodAnagraficaSelected;
	}

	public void setLastCodAnagraficaSelected(Integer lastCodAnagraficaSelected) {
		this.lastCodAnagraficaSelected = lastCodAnagraficaSelected;
	}

	public ArrayList<Integer> getCodiciImmobili() {
		if (codiciImmobili == null){
			codiciImmobili = new ArrayList<Integer>();
			ImmobiliDAO iDAO = new ImmobiliDAO();
			ArrayList<Immobili> al = iDAO.list(ImmobiliVO.class.getName());
//			al.stream().max((v1, v2) -> Double.compare(v1.getCodImmobile(), v2.getCodImmobile()))
//		    .orElse(null);
			Iterator it = al.iterator();
			while (it.hasNext()) {
				ImmobiliVO type = (ImmobiliVO) it.next();
				codiciImmobili.add(type.getCodImmobile());
			}
		}
		return codiciImmobili;
	}

	public void setCodiciImmobili(ArrayList<Integer> codiciImmobili) {
		this.codiciImmobili = codiciImmobili;
	}

	public ArrayList<Integer> getCodiciAnagrafiche() {
		if (codiciAnagrafiche == null){
			codiciAnagrafiche = new ArrayList<Integer>();
			AnagraficheDAO aDAO = new AnagraficheDAO();
			ArrayList al = aDAO.list(AnagraficheVO.class.getName());
			Iterator it = al.iterator();
			while (it.hasNext()) {
				AnagraficheVO type = (AnagraficheVO) it.next();
				codiciAnagrafiche.add(type.getCodAnagrafica());
			}			
		}
		return codiciAnagrafiche;
	}

	public void setCodiciAnagrafiche(ArrayList<Integer> codiciAnagrafiche) {
		this.codiciAnagrafiche = codiciAnagrafiche;
	}

	public Integer getNextCodImmobile(){
		Integer returnValue = null;
		try {
			returnValue = (getLastCodImmobileSelected() != null)
							?(getCodiciImmobili().indexOf(getCodiciImmobili().get(getLastCodImmobileSelected())) == getCodiciImmobili().indexOf(getCodiciImmobili().size()-1))
									? 0 
								    : getCodiciImmobili().indexOf(getLastCodImmobileSelected()) + 1
							: 0;
			
		} catch (Exception e) {
			returnValue = 0;
		}
		
		return returnValue;
	}

	public Integer getNextCodAnagrafiche(){
		Integer returnValue = null;
		try {
			returnValue = (getLastCodAnagraficaSelected() != null)
							?(getCodiciAnagrafiche().indexOf(getCodiciAnagrafiche().get(getLastCodAnagraficaSelected())) == getCodiciAnagrafiche().indexOf(getCodiciAnagrafiche().size()-1))
							  ? 0 
							  : getCodiciAnagrafiche().indexOf(getLastCodAnagraficaSelected()) + 1
							: 0;
		} catch  (Exception e) {
			returnValue = 0;
		}
		
		return returnValue;
	}

	public Integer getPreviousCodImmobile(){
		Integer returnValue = null;
		try {
			returnValue = (getLastCodImmobileSelected() != null)
						  ?  (getCodiciImmobili().indexOf(getCodiciImmobili().get(getLastCodImmobileSelected())) == 0)
								? getCodiciImmobili().size() - 1
								: getCodiciImmobili().indexOf(getCodiciImmobili().get(getLastCodImmobileSelected()) - 1)
						  : 0;
			
		} catch (Exception e) {
			returnValue = 0;
		}
		
		return returnValue;
	}

	public Integer getPreviousCodAnagrafiche(){
		Integer returnValue = null;
		try {
			returnValue = (getLastCodAnagraficaSelected() != null)
							?  (getCodiciAnagrafiche().indexOf(getCodiciAnagrafiche().get(getLastCodAnagraficaSelected())) == 0)
							   ? getCodiciAnagrafiche().size() - 1
							   : getCodiciAnagrafiche().indexOf(getCodiciAnagrafiche().get(getLastCodAnagraficaSelected()) - 1)
							: 0;
			
		} catch  (Exception e) {
			returnValue = 0;
		}
		
		return returnValue;
	}

	public String getLastPerspectiveSelected() {
		return lastPerspectiveSelected;
	}

	public void setLastPerspectiveSelected(String lastPerspectiveSelected) {
		this.lastPerspectiveSelected = lastPerspectiveSelected;
	}
	
	public class ReportEngineRunner implements IRunnableWithProgress{

		public final static int STAMPA_UNICO_FILE = 0;
		public final static int STAMPA_PIU_FILE = 1;
		
		private ArrayList objsToPrint = null;
		private ReportModel report = null;
		private String destinationPath = null;
		private int returnFileType = 1;
		
		public ReportEngineRunner(ArrayList objsToPrint, ReportModel report, String destinationPath,int resultFileType){
			this.objsToPrint = objsToPrint;
			this.report = report;
			this.destinationPath = destinationPath;
			this.returnFileType = resultFileType;
		}
		@Override
		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
				ReportEngine re = new ReportEngine();
				if (re.doReport(report,objsToPrint,destinationPath,returnFileType)){
					MessageDialog.openInformation(PlatformUI.getWorkbench()
	  						  								.getActiveWorkbenchWindow()
	  						  								.getShell(), 
												  "Stampa", 
												  "Stampa completata con successo");	
					
				}else{
					MessageDialog.openError(PlatformUI.getWorkbench()
							  						  .getActiveWorkbenchWindow()
							  						  .getShell(), 
							  				"Stampa", 
											"Stampa non completata si � verificato un errore");						
				}		
		}
		
	}

	public ArrayList<ObjectSearchGetters> getImmagineSearchGetters() {
		if (immagineSearchGetters == null){
			immagineSearchGetters = new ArrayList<ObjectSearchGetters>();
			immagineSearchGetters.add(new ObjectSearchGetters(1,"getCodImmagine","Codice immagine","",""));
			immagineSearchGetters.add(new ObjectSearchGetters(2,"getCodImmobile","Codice immobile","",""));
			immagineSearchGetters.add(new ObjectSearchGetters(3,"getImgPropsStr","Proprieta immagine","",""));
			immagineSearchGetters.add(new ObjectSearchGetters(4,"getOrdine","ordine","",""));
			immagineSearchGetters.add(new ObjectSearchGetters(5,"getPathImmagine","Nome immagine","",""));
		}
		return immagineSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getImmagineReportSearchGetters() {
		if (immagineReportSearchGetters == null){
			immagineReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			immagineReportSearchGetters.add(new ObjectSearchGetters(1,"getCodImmagine","Codice immagine","",""));
			immagineReportSearchGetters.add(new ObjectSearchGetters(2,"getCodImmobile","Codice immobile","",""));
			immagineReportSearchGetters.add(new ObjectSearchGetters(3,"getImgPropsStr","Proprieta immagine","",""));
			immagineReportSearchGetters.add(new ObjectSearchGetters(4,"getOrdine","ordine","",""));
			immagineReportSearchGetters.add(new ObjectSearchGetters(5,"getPathImmagine","Nome immagine","",""));
		}		
		return immagineReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getStanzeImmobileSearchGetters() {
		if (stanzeImmobileSearchGetters == null){
			stanzeImmobileSearchGetters = new ArrayList<ObjectSearchGetters>();
			stanzeImmobileSearchGetters.add(new ObjectSearchGetters(1,"getCodStanzeImmobili","codice stanza","",""));
			stanzeImmobileSearchGetters.add(new ObjectSearchGetters(2,"getCodImmobile","codice immobile","",""));
			stanzeImmobileSearchGetters.add(new ObjectSearchGetters(3,"getCodTipologiaStanza","codice tipologia stanza","",""));
			stanzeImmobileSearchGetters.add(new ObjectSearchGetters(4,"getMq","metri quadrati","",""));
			stanzeImmobileSearchGetters.add(new ObjectSearchGetters(5,"getDescrizioneTipologia","descrizione tipologia stanza","",""));
		}
		return stanzeImmobileSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getStanzeImmobileReportSearchGetters() {
		if (stanzeImmobileReportSearchGetters == null){
			stanzeImmobileReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			stanzeImmobileReportSearchGetters.add(new ObjectSearchGetters(1,"getCodStanzeImmobili","codice stanza","",""));
			stanzeImmobileReportSearchGetters.add(new ObjectSearchGetters(2,"getCodImmobile","codice immobile","",""));
			stanzeImmobileReportSearchGetters.add(new ObjectSearchGetters(3,"getCodTipologiaStanza","codice tipologia stanza","",""));
			stanzeImmobileReportSearchGetters.add(new ObjectSearchGetters(4,"getMq","metri quadrati","",""));
			stanzeImmobileReportSearchGetters.add(new ObjectSearchGetters(5,"getDescrizioneTipologia","descrizione tipologia stanza","",""));
		}
		
		return stanzeImmobileReportSearchGetters;
	}

	public RicercaWizard getRicercaWiz() {
		return ricercaWiz;
	}

	public void setRicercaWiz(RicercaWizard ricercaWiz) {
		this.ricercaWiz = ricercaWiz;
	}

	public ArrayList<ObjectSearchGetters> getAllegatiImmobileSearchGetters() {
		if (allegatiImmobileSearchGetters == null){
			allegatiImmobileSearchGetters = new ArrayList<ObjectSearchGetters>();
			allegatiImmobileSearchGetters.add(new ObjectSearchGetters(1,"getCodAllegatiImmobili","codice allegato","CODALLEGATIIMMOBILI",""));
			allegatiImmobileSearchGetters.add(new ObjectSearchGetters(2,"getCodImmobile","codice allegato","CODIMMOBILE",""));
			allegatiImmobileSearchGetters.add(new ObjectSearchGetters(3,"getCommento","commento","COMMENTO",""));
			allegatiImmobileSearchGetters.add(new ObjectSearchGetters(4,"getNome","nome","NOME",""));
		}
		return allegatiImmobileSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAllegatiImmobileReportSearchGetters() {
		if (allegatiImmobileReportSearchGetters == null){
			allegatiImmobileReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			allegatiImmobileReportSearchGetters.add(new ObjectSearchGetters(1,"getCodAllegatiImmobili","codice allegato","CODALLEGATIIMMOBILI",""));
			allegatiImmobileReportSearchGetters.add(new ObjectSearchGetters(2,"getCodImmobile","codice allegato","CODIMMOBILE",""));
			allegatiImmobileReportSearchGetters.add(new ObjectSearchGetters(3,"getCommento","commento","COMMENTO",""));
			allegatiImmobileReportSearchGetters.add(new ObjectSearchGetters(4,"getNome","nome","NOME",""));
		}
		return allegatiImmobileReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAbbinamentiSearchGetters() {
		if (abbinamentiSearchGetters == null){
			abbinamentiSearchGetters = new ArrayList<ObjectSearchGetters>();
			abbinamentiSearchGetters.add(new ObjectSearchGetters(1,"getCodAbbinamento","codice abbinamento","CODABBINAMENTO",""));
			abbinamentiSearchGetters.add(new ObjectSearchGetters(2,"getCodImmobile","codice immobile","CODIMMOBILE",""));
			abbinamentiSearchGetters.add(new ObjectSearchGetters(3,"getCodAnagrafica","codice anagrafica","CODANAGRAFICA",""));
			abbinamentiSearchGetters.add(new ObjectSearchGetters(4,"getImmobile","immobile","",WinkhouseUtils.IMMOBILI));
			abbinamentiSearchGetters.add(new ObjectSearchGetters(5,"getAnagrafica","anagrafica","",WinkhouseUtils.ANAGRAFICHE));
		}
		return abbinamentiSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAbbinamentiReportSearchGetters() {
		if (abbinamentiReportSearchGetters == null){
			abbinamentiReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(1,"getCodAbbinamento","codice abbinamento","CODABBINAMENTO",""));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(2,"getCodImmobile","codice immobile","CODIMMOBILE",""));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(3,"getCodAnagrafica","codice anagrafica","CODANAGRAFICA",""));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(4,"getImmobile","immobile","",WinkhouseUtils.IMMOBILI));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(5,"getAnagrafica","anagrafica","",WinkhouseUtils.ANAGRAFICHE));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(6,"getRifImmobile","riferimento immobile","",""));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(7,"getCittaImmobile","citta immobile","",""));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(8,"getIndirizzoImmobile","indirizzo immobile","",""));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(9,"getNomeCognomeproprietarioImmobile","nome e cognome proprietario immobile","",""));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(10,"getNomeCognomeAnagrafica","nome e cognome anagrafica","",""));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(11,"getRagioneSocialeAnagrafica","ragione sociale anagrafica","",""));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(12,"getIndirizzoAnagrafica","indirizzo anagrafica","",""));
			abbinamentiReportSearchGetters.add(new ObjectSearchGetters(13,"getCittaAnagrafica","citta anagrafica","",""));
		}
		return abbinamentiReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAffittiSearchGetters() {
		if (affittiSearchGetters == null){
			affittiSearchGetters = new ArrayList<ObjectSearchGetters>();
			affittiSearchGetters.add(new ObjectSearchGetters(1,"getCodAffitti","codice affitto","CODAFFITTI",""));
			affittiSearchGetters.add(new ObjectSearchGetters(2,"getCauzione","cauzione","CAUZIONE",""));
			affittiSearchGetters.add(new ObjectSearchGetters(3,"getCodAgenteIns","codice agente inseritore","CODAGENTEINS",""));
			affittiSearchGetters.add(new ObjectSearchGetters(4,"getCodImmobile","codice immobile","CODIMMOBILE",""));
			affittiSearchGetters.add(new ObjectSearchGetters(5,"getDataFine","data fine","DATAFINE",""));
			affittiSearchGetters.add(new ObjectSearchGetters(6,"getDataInizio","data inizio","DATAINIZIO",""));
			affittiSearchGetters.add(new ObjectSearchGetters(7,"getDescrizione","descrizione","DESCRIZIONE",""));
			affittiSearchGetters.add(new ObjectSearchGetters(8,"getRata","rata","RATA",""));
			affittiSearchGetters.add(new ObjectSearchGetters(9,"getAgenteInseritore","agente inseritore","",AGENTI));
			affittiSearchGetters.add(new ObjectSearchGetters(10,"getImmobile","immobile","",IMMOBILI));
			affittiSearchGetters.add(new ObjectSearchGetters(11,"getAllegati","lista allegati","",AFFITTIALLEGATI));
			affittiSearchGetters.add(new ObjectSearchGetters(12,"getAnagrafiche","lista anagrafiche","",ANAGRAFICHE));			
		}
		return affittiSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAffittiReportSearchGetters() {
		if (affittiReportSearchGetters == null){
			affittiReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			affittiReportSearchGetters.add(new ObjectSearchGetters(1,"getCodAffitti","codice affitto","CODAFFITTI",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(2,"getCauzione","cauzione","CAUZIONE",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(3,"getCodAgenteIns","codice agente inseritore","CODAGENTEINS",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(4,"getCodImmobile","codice immobile","CODIMMOBILE",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(5,"getDataFine","data fine","DATAFINE",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(6,"getDataInizio","data inizio","DATAINIZIO",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(7,"getDescrizione","descrizione","DESCRIZIONE",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(8,"getRata","rata","RATA",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(9,"getAgenteInseritore","agente inseritore","",AGENTI));
			affittiReportSearchGetters.add(new ObjectSearchGetters(10,"getImmobile","immobile","",IMMOBILI));
			affittiReportSearchGetters.add(new ObjectSearchGetters(11,"getAllegati","lista allegati","",AFFITTIALLEGATI));
			affittiReportSearchGetters.add(new ObjectSearchGetters(12,"getAnagrafiche","lista anagrafiche","",AFFITTIANAGRAFICHE));
			affittiReportSearchGetters.add(new ObjectSearchGetters(13,"getRate","lista rate","",AFFITTIRATE));
			affittiReportSearchGetters.add(new ObjectSearchGetters(14,"getSpese","lista spese","",AFFITTISPESE));
			affittiReportSearchGetters.add(new ObjectSearchGetters(15,"getNumeroAnagrafiche","numero anagrafiche","",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(16,"getRataMenoCauzione","differenza tra rata e cauzione","",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(17,"getCauzioneMenoRata","differenza tra cauzione e rata","",""));
			affittiReportSearchGetters.add(new ObjectSearchGetters(18,"getStanzeimmobile","lista stanze immobili","",STANZEIMMOBILI));
			
		}
		return affittiReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAppuntamentiSearchGetters() {
		if (appuntamentiSearchGetters == null){
			appuntamentiSearchGetters = new ArrayList<ObjectSearchGetters>();
			appuntamentiSearchGetters.add(new ObjectSearchGetters(1,"getCodAppuntamento","codice appuntamento","CODAPPUNTAMENTO",""));
			appuntamentiSearchGetters.add(new ObjectSearchGetters(2,"getDataInserimento","data inserimento","DATAINSERIMENTO",""));
			appuntamentiSearchGetters.add(new ObjectSearchGetters(3,"getDataAppuntamento","data appuntamento","DATAAPPUNTAMENTO",""));
			appuntamentiSearchGetters.add(new ObjectSearchGetters(4,"getDescrizione","descrizione","DESCRIZIONE",""));
			appuntamentiSearchGetters.add(new ObjectSearchGetters(5,"getLuogo","luogo","LUOGO",""));
			appuntamentiSearchGetters.add(new ObjectSearchGetters(6,"getAgenti","agenti","",AGENTIAPPUNTAMENTI));
			appuntamentiSearchGetters.add(new ObjectSearchGetters(7,"getAnagrafiche","anagrafiche","",ANAGRAFICHEAPPUNTAMENTI));
		}
		return appuntamentiSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAppuntamentiReportSearchGetters() {
		if (appuntamentiReportSearchGetters == null){
			appuntamentiReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(1,"getCodAppuntamento","codice appuntamento","CODAPPUNTAMENTO",""));
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(2,"getDataInserimento","data inserimento","DATAINSERIMENTO",""));
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(3,"getDataAppuntamento","data/ora appuntamento","DATAAPPUNTAMENTO",""));
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(4,"getDescrizione","descrizione","DESCRIZIONE",""));
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(5,"getLuogo","luogo","LUOGO",""));
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(6,"getAgenti","agenti","",AGENTIAPPUNTAMENTI));
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(7,"getAnagrafiche","anagrafiche","",ANAGRAFICHEAPPUNTAMENTI));
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(8,"getDataStrAppuntamento","data appuntamento","",""));
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(9,"getOraStrAppuntamento","ora appuntamento","DATAAPPUNTAMENTO",""));			
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(11,"getiCalUID","Codice Google Calendar","ICALUID",""));
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(12,"getTipoAppuntamento","Tipo appuntamento","",TIPIAPPUNTAMENTI));			
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(10,"getDataFineStrAppuntamento","data fine appuntamento","",""));
			appuntamentiReportSearchGetters.add(new ObjectSearchGetters(13,"getOraFineStrAppuntamento","ora fine appuntamento","",""));			
		}
		return appuntamentiReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getTipiAppuntamentiReportSearchGetters() {
		if (tipiappuntamentiReportSearchGetters == null){
			tipiappuntamentiReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			tipiappuntamentiReportSearchGetters.add(new ObjectSearchGetters(1,"getCodTipoAppuntamento","codice tipo appuntamento","CODTIPOAPPUNTAMENTO",""));
			tipiappuntamentiReportSearchGetters.add(new ObjectSearchGetters(2,"getDescrizione","descrizione tipo appuntamento","DESCRIZIONE",""));
		}
		return tipiappuntamentiReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getClassiEnergeticheReportSearchGetters() {
		if (classienergeticheReportSearchGetters == null){
			classienergeticheReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			classienergeticheReportSearchGetters.add(new ObjectSearchGetters(1,"getCodClasseEnergetica","codice classe energetica","CODCLASSEENERGETICA",""));
			classienergeticheReportSearchGetters.add(new ObjectSearchGetters(2,"getNome","nome classe energetica","NOME",""));
			classienergeticheReportSearchGetters.add(new ObjectSearchGetters(3,"getDescrizione","descrizione classe energetica","DESCRIZIONE",""));
			classienergeticheReportSearchGetters.add(new ObjectSearchGetters(4,"getOrdine","ordine","ORDINE",""));
		}
		return classienergeticheReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAffittiAllegatiSearchGetters() {
		if (affittiAllegatiSearchGetters == null){
			affittiAllegatiSearchGetters = new ArrayList<ObjectSearchGetters>();
			affittiAllegatiSearchGetters.add(new ObjectSearchGetters(1,"getCodAffittiAllegati","codice allegato affitto","CODAFFITTIALLEGATI",""));
			affittiAllegatiSearchGetters.add(new ObjectSearchGetters(2,"getCodAffitto","codice affitto","CODAFFITTO",""));
			affittiAllegatiSearchGetters.add(new ObjectSearchGetters(3,"getNome","nome","NOME",""));
			affittiAllegatiSearchGetters.add(new ObjectSearchGetters(4,"getDescrizione","descrizione","DESCRIZIONE",""));
		}
		return affittiAllegatiSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAffittiAllegatiReportSearchGetters() {
		if (affittiAllegatiReportSearchGetters == null){
			affittiAllegatiReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			affittiAllegatiReportSearchGetters.add(new ObjectSearchGetters(1,"getCodAffittiAllegati","codice allegato affitto","CODAFFITTIALLEGATI",""));
			affittiAllegatiReportSearchGetters.add(new ObjectSearchGetters(2,"getCodAffitto","codice affitto","CODAFFITTO",""));
			affittiAllegatiReportSearchGetters.add(new ObjectSearchGetters(3,"getNome","nome","NOME",""));
			affittiAllegatiReportSearchGetters.add(new ObjectSearchGetters(4,"getDescrizione","descrizione","DESCRIZIONE",""));
		}
		return affittiAllegatiReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAffittiRateReportSearchGetters() {
		if (affittiRateReportSearchGetters == null){
			affittiRateReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			affittiRateReportSearchGetters.add(new ObjectSearchGetters(1,"getCodAffittiRate","codice rata affitto","CODAFFITTIRATE",""));
			affittiRateReportSearchGetters.add(new ObjectSearchGetters(2,"getCodAffitto","codice affitto","CODAFFITTO",""));
			affittiRateReportSearchGetters.add(new ObjectSearchGetters(3,"getCodAnagrafica","codice anagrafica","CODANAGRAFICA",""));
			affittiRateReportSearchGetters.add(new ObjectSearchGetters(4,"getDataPagato","data pagamento rata","DATAPAGATO",""));
			affittiRateReportSearchGetters.add(new ObjectSearchGetters(5,"getScadenza","scadenza rata","SCADENZA",""));
			affittiRateReportSearchGetters.add(new ObjectSearchGetters(6,"getImporto","importo rata","IMPORTO",""));
			affittiRateReportSearchGetters.add(new ObjectSearchGetters(7,"getNota","nota rata","NOTA",""));
			affittiRateReportSearchGetters.add(new ObjectSearchGetters(8,"getNomeMese","nome mese rata","",""));
			affittiRateReportSearchGetters.add(new ObjectSearchGetters(9,"getAnagrafica","anagrafica","",ANAGRAFICHE));
		}
		return affittiRateReportSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getAffittiSpeseReportSearchGetters() {
		if (affittiSpeseReportSearchGetters == null){
			affittiSpeseReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(1,"getCodAffittiSpese","codice spesa affitto","CODAFFITTISPESE",""));
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(2,"getCodAffitto","codice affitto","CODAFFITTO",""));
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(3,"getCodAnagrafica","codice anagrafica","CODANAGRAFICA",""));
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(4,"getDescrizione","descrizione spesa","DESCRIZIONE",""));
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(5,"getDataInizio","data inizio periodo","DATAINIZIO",""));
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(6,"getDataFine","data fine periodo","DATAFINE",""));
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(7,"getDataPagato","data pagamento spesa","DATAPAGATO",""));
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(8,"getScadenza","data scadenza spesa","SCADENZA",""));
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(9,"getImporto","importo spesa","IMPORTO",""));
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(10,"getVersato","importo versato spesa","VERSATO",""));
			affittiSpeseReportSearchGetters.add(new ObjectSearchGetters(11,"getAnagrafica","anagrafica","",ANAGRAFICHE));
		}
		return affittiSpeseReportSearchGetters;
	}
	
	public ArrayList<ObjectSearchGetters> getAffittiAnagraficheReportSearchGetters() {
		if (affittiAnagraficheReportSearchGetters == null){
			affittiAnagraficheReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(1,"getNota","nota","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(2,"getAnagrafica","anagrafica","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(3,"getIndirizzoAnagrafica","indirizzo anagrafica","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(4,"getCittaAnagrafica","citta anagrafica","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(5,"getCodicefiscaleAnagrafica","codice fiscale anagrafica","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(6,"getPrimoRecapitoAnagrafica","primo recapito anagrafica","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(7,"getSecondoRecapitoAnagrafica","secondo  recapito anagrafica","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(8,"getNomeCognomeAnagrafica","nome e cognome anagrafica","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(9,"getCapAnagrafica","cap anagrafica","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(10,"getProvinciaAnagrafica","provincia anagrafica","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(9,"getRagioneSocialeAnagrafica","ragione sociale anagrafica","",""));
			affittiAnagraficheReportSearchGetters.add(new ObjectSearchGetters(10,"getPivaAnagrafica","partita iva anagrafica","",""));						
		}
		return affittiAnagraficheReportSearchGetters;
	}	

	public ArrayList<ObjectSearchGetters> getImmobileAffittiSearchGetters() {
		if (immobileAffittiSearchGetters == null){
			immobileAffittiSearchGetters = new ArrayList<ObjectSearchGetters>();
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(1,"getMq","metri quadrati","MQ",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(2,"getNumeroStanze","numero stanze","NS.NUMSTANZE",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(3,"getPrezzo","prezzo","PREZZO",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(4,"getMutuo","mutuo","MUTUO",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(5,"getSpese","spese","SPESE",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(6,"getZona","zona","ZONA",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(7,"getCitta","citta","CITTA",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(8,"getProvincia","provincia","PROVINCIA",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(9,"getCap","cap","CAP",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(10,"getAnnoCostruzione","anno costruzione","ANNOCOSTRUZIONE",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(11,"getDataLibero","data libero","DATALIBERO",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(12,"getCodTipologia","tipologia immobile","CODTIPOLOGIA",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(13,"getCodStato","stato conservativo","CODSTATO",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(14,"getCodRiscaldamento","riscaldamenti","CODRISCALDAMENTO",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(15,"getCodAgenteInseritore","agente inseritore","CODAGENTEINSERITORE",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(16,"getRif","codice riferimento","RIF",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(17,"getPeriodoAffitto","periodo affitto","",""));						
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(18,"(","(","",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(19,")",")","",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(20,"getRata","rata affitto","A.RATA",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(21,"getCauzione","cauzione affitto","A.CAUZIONE",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(22,"getScadenzaSpesa","scadenza spese affitto","AFS.SCADENZA",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(23,"getDataPagatoSpesa","data pagamento spesa","AFS.DATAPAGATO",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(24,"getScadenzaRata","scadenza rata","AR.SCADENZA",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(25,"getDataPagatoRata","data pagamento rata","AR.DATAPAGATO",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(26,"getNomeAnagrafica","nome anagrafica affittuaria","AN.NOME",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(27,"getCognomeAnagrafica","cognome anagrafica affittuaria","AN.COGNOME",""));
			immobileAffittiSearchGetters.add(new ObjectSearchGetters(28,"getRagioneSocialeAnagrafica","ragione sociale anagrafica affittuaria","AN.RAGSOC",""));			
			addCampiPersonalizzatiOSG(immobileAffittiSearchGetters, AffittiVO.class.getName(), 26);
		}
		return immobileAffittiSearchGetters;
	}
	
	public ArrayList<ObjectSearchGetters> getDatiCatastaliImmobileSearchGetters() {
		if (datiCatastaliImmobileSearchGetters == null){
			datiCatastaliImmobileSearchGetters = new ArrayList<ObjectSearchGetters>();
			datiCatastaliImmobileSearchGetters.add(new ObjectSearchGetters(1,"getFoglio","foglio","FOGLIO",""));
			datiCatastaliImmobileSearchGetters.add(new ObjectSearchGetters(2,"getParticella","particella","PARTICELLA",""));
			datiCatastaliImmobileSearchGetters.add(new ObjectSearchGetters(3,"getSubalterno","subalterno","SUBALTERNO",""));
			datiCatastaliImmobileSearchGetters.add(new ObjectSearchGetters(4,"getCategoria","categoria","CATEGORIA",""));
			datiCatastaliImmobileSearchGetters.add(new ObjectSearchGetters(5,"getRendita","rendita","RENDITA",""));
			datiCatastaliImmobileSearchGetters.add(new ObjectSearchGetters(6,"getRedditoDomenicale","reddito domenicale","REDDITODOMENICALE",""));
			datiCatastaliImmobileSearchGetters.add(new ObjectSearchGetters(7,"getRedditoAgricolo","reddito agricolo","REDDITOAGRICOLO",""));
			datiCatastaliImmobileSearchGetters.add(new ObjectSearchGetters(8,"getDimensione","dimensione","DIMENSIONE",""));
		}
		return datiCatastaliImmobileSearchGetters;
	}

	public ArrayList<ObjectSearchGetters> getDatiCatastaliImmobileReportSearchGetters() {
		if (datiCatastaliImmobileReportSearchGetters == null){
			datiCatastaliImmobileReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			datiCatastaliImmobileReportSearchGetters.add(new ObjectSearchGetters(1,"getFoglio","foglio","FOGLIO",""));
			datiCatastaliImmobileReportSearchGetters.add(new ObjectSearchGetters(2,"getParticella","particella","PARTICELLA",""));
			datiCatastaliImmobileReportSearchGetters.add(new ObjectSearchGetters(3,"getSubalterno","subalterno","SUBALTERNO",""));
			datiCatastaliImmobileReportSearchGetters.add(new ObjectSearchGetters(4,"getCategoria","categoria","CATEGORIA",""));
			datiCatastaliImmobileReportSearchGetters.add(new ObjectSearchGetters(5,"getRendita","rendita","RENDITA",""));
			datiCatastaliImmobileReportSearchGetters.add(new ObjectSearchGetters(6,"getRedditoDomenicale","reddito domenicale","REDDITODOMENICALE",""));
			datiCatastaliImmobileReportSearchGetters.add(new ObjectSearchGetters(7,"getRedditoAgricolo","reddito agricolo","REDDITOAGRICOLO",""));
			datiCatastaliImmobileReportSearchGetters.add(new ObjectSearchGetters(8,"getDimensione","dimensione","DIMENSIONE",""));
		}		
		return datiCatastaliImmobileReportSearchGetters;
	}
	
	public void tmpDirectoryDeleter(String parentPath){
		
		File fparent = new File(parentPath);
		if (fparent.exists()){
			File[] parentContent = fparent.listFiles();
			for (int i = 0; i < parentContent.length; i++) {
				if (parentContent[i].isDirectory()){
					tmpDirectoryDeleter(parentContent[i].getAbsolutePath());
					parentContent[i].delete();
				}
				if (parentContent[i].getName()
					    			.indexOf('.') > -1){
					if (!parentContent[i].getName()
										 .substring(parentContent[i].getName()
																    .indexOf('.')).equalsIgnoreCase(".odt")){
						parentContent[i].delete();	
					}
				}
			}
		}
		
	}
	
	public void standardDirectoryDeleter(String parentPath){
		
		File fparent = new File(parentPath);
		if (fparent.exists()){
			File[] parentContent = fparent.listFiles();
			for (int i = 0; i < parentContent.length; i++) {
				if (parentContent[i].isDirectory()){
					tmpDirectoryDeleter(parentContent[i].getAbsolutePath());
					parentContent[i].delete();
				}else{
					parentContent[i].delete();	
				}
			}
		}
		
	}
	

	public boolean isAnagraficheFiltered() {
		return isAnagraficheFiltered;
	}

	public void setAnagraficheFiltered(boolean isAnagraficheFiltered) {
		this.isAnagraficheFiltered = isAnagraficheFiltered;
	}

	public Integer getAngraficheFilterType() {
		return angraficheFilterType;
	}

	public void setAngraficheFilterType(Integer angraficheFilterType) {
		this.angraficheFilterType = angraficheFilterType;
	}
	
	public String getOpenOfficeInstalledPath(){
		WindowsReqistry wr = new WindowsReqistry();
        String value = wr.readRegistry("HKEY_LOCAL_MACHINE\\Software\\OpenOffice\\Layers\\OpenOffice\\4", "OFFICEINSTALLLOCATION");
        if (value == null){
        	value = wr.readRegistry("HKEY_LOCAL_MACHINE\\Software\\OpenOffice.org\\Layers_\\OpenOffice.org\\3", "OFFICEINSTALLLOCATION");
        }
        value = value.substring(0, value.lastIndexOf('\\'));
   	    return value;
	}

	public String getOpenOfficeWriterPath(){
		String returnValue = null;
		String installPath = getOpenOfficeInstalledPath();
		if (installPath != null){
			File f = new File(installPath + File.separator + "program" + File.separator + "swriter.exe"); 
			if (f.exists()){
				returnValue = installPath + File.separator + "program" + File.separator + "swriter.exe";
			}
		}
		return returnValue;
	}

	public class WindowsReqistry {

		public WindowsReqistry(){
			
		}

		public String readRegistry(String location, String key){
			String returnValue = null;
	        try {
	           
	            Process process = Runtime.getRuntime().exec("reg query " + 
	                    '"'+ location + "\" /v " + key);

	            StreamReader reader = new StreamReader(process.getInputStream());
	            reader.start();
	            process.waitFor();
	            reader.join();
	            String output = reader.getResult();
	            
	            if( ! output.contains("\t")){
	            	if (output.split("\r\n").length == 3){
	            		returnValue = output.split("\r\n")[2];
	            		returnValue = returnValue.split("    ")[3];
	            	}
	            }else{
		            String[] parsed = output.split("\t");
		            returnValue = parsed[parsed.length-1];	            	
	            }
	            
	            return returnValue;

	        }
	        catch (Exception e) {
	            return null;
	        }

	    }

	    class StreamReader extends Thread {
	        private InputStream is;
	        private StringWriter sw= new StringWriter();;

	        public StreamReader(InputStream is) {
	            this.is = is;
	        }

	        public void run() {
	            try {
	                int c;
	                while ((c = is.read()) != -1)
	                    sw.write(c);
	            }
	            catch (IOException e) { 
	        }
	        }

	        public String getResult() {
	            return sw.toString();
	        }
	    }
	    
	}
	
	public Integer getMesiLocaleKey(String mese){
		
		for (int i = 1;i< 13; i++){
			if (EnvSettingsFactory.getInstance()
			  				      .getDefaultMesiDescription()
			  				      .getString(String.valueOf(i))
			  				      .equalsIgnoreCase(mese)){
				return i;
			}			  				  
		}
		return 0;
	}

    private Comparator<WinkhouseUtils.Mese> comparatorMese = new Comparator<WinkhouseUtils.Mese>(){

		@Override
		public int compare(WinkhouseUtils.Mese arg0,WinkhouseUtils.Mese arg1) {
			if (arg0.getKey().intValue() == arg1.getKey().intValue()){
				return 0;
			}else if (arg0.getKey().intValue() > arg1.getKey().intValue()){
				return 1;
			}else{
				return -1;
			}				
		}
		
	};

	public ArrayList<Mese> getMesi() {
		if (mesi == null){
			mesi = new ArrayList<Mese>();
			ResourceBundle rb = EnvSettingsFactory.getInstance()
												  .getDefaultMesiDescription();
			Enumeration<String> e  = rb.getKeys();

			Object o = null;
			while (e.hasMoreElements()){
				o = e.nextElement();
				mesi.add(new Mese(Integer.valueOf(String.valueOf(o)),
								  rb.getString(String.valueOf(o)))
						);
			}
		}
		Collections.sort(mesi, comparatorMese);

		return mesi;
	}

	public String getLastEntityTypeFocused() {
		return lastEntityTypeFocused;
	}

	public void setLastEntityTypeFocused(String lastEntityTypeFocused) {
		this.lastEntityTypeFocused = lastEntityTypeFocused;
	}
	
	private class StringEncrypter {

	    Cipher ecipher;
	    Cipher dcipher;


	    /**
	     * Constructor used to create this object.  Responsible for setting
	     * and initializing this object's encrypter and decrypter Chipher instances
	     * given a Secret Key and algorithm.
	     * @param key        Secret Key used to initialize both the encrypter and
	     *                   decrypter instances.
	     * @param algorithm  Which algorithm to use for creating the encrypter and
	     *                   decrypter instances.
	     */
	    public StringEncrypter(SecretKey key, String algorithm) {
	        try {
	            ecipher = Cipher.getInstance(algorithm);
	            dcipher = Cipher.getInstance(algorithm);
	            ecipher.init(Cipher.ENCRYPT_MODE, key);
	            dcipher.init(Cipher.DECRYPT_MODE, key);
	        } catch (NoSuchPaddingException e) {
	            System.out.println("EXCEPTION: NoSuchPaddingException");
	        } catch (NoSuchAlgorithmException e) {
	            System.out.println("EXCEPTION: NoSuchAlgorithmException");
	        } catch (InvalidKeyException e) {
	            System.out.println("EXCEPTION: InvalidKeyException");
	        }
	    }


	    /**
	     * Constructor used to create this object.  Responsible for setting
	     * and initializing this object's encrypter and decrypter Chipher instances
	     * given a Pass Phrase and algorithm.
	     * @param passPhrase Pass Phrase used to initialize both the encrypter and
	     *                   decrypter instances.
	     */
	    public StringEncrypter(String passPhrase) {

	        // 8-bytes Salt
	        byte[] salt = {
	            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
	            (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
	        };

	        // Iteration count
	        int iterationCount = 19;

	        try {

	            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
	            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

	            ecipher = Cipher.getInstance(key.getAlgorithm());
	            dcipher = Cipher.getInstance(key.getAlgorithm());

	            // Prepare the parameters to the cipthers
	            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

	            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
	            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

	        } catch (InvalidAlgorithmParameterException e) {
	            System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
	        } catch (InvalidKeySpecException e) {
	            System.out.println("EXCEPTION: InvalidKeySpecException");
	        } catch (NoSuchPaddingException e) {
	            System.out.println("EXCEPTION: NoSuchPaddingException");
	        } catch (NoSuchAlgorithmException e) {
	            System.out.println("EXCEPTION: NoSuchAlgorithmException");
	        } catch (InvalidKeyException e) {
	            System.out.println("EXCEPTION: InvalidKeyException");
	        }
	    }


	    /**
	     * Takes a single String as an argument and returns an Encrypted version
	     * of that String.
	     * @param str String to be encrypted
	     * @return <code>String</code> Encrypted version of the provided String
	     */
	    private String encrypt(String str) {
	        try {
	            // Encode the string into bytes using utf-8
	            byte[] utf8 = str.getBytes("UTF8");

	            // Encrypt
	            byte[] enc = ecipher.doFinal(utf8);

	            // Encode bytes to base64 to get a string
	            return "";//new sun.misc.BASE64Encoder().encode(enc);

	        } catch (BadPaddingException e) {
	        } catch (IllegalBlockSizeException e) {
	        } catch (UnsupportedEncodingException e) {
	        } catch (IOException e) {
	        }
	        return null;
	    }


	    /**
	     * Takes a encrypted String as an argument, decrypts and returns the
	     * decrypted String.
	     * @param str Encrypted String to be decrypted
	     * @return <code>String</code> Decrypted version of the provided String
	     */
	    private String decrypt(String str) {

	        try {

	            // Decode base64 to get bytes
	            byte[] dec = null;//new sun.misc.BASE64Decoder().decodeBuffer(str);

	            // Decrypt
	            byte[] utf8 = dcipher.doFinal(dec);

	            // Decode using utf-8
	            return new String(utf8, "UTF8");

	        } catch (BadPaddingException e) {
	        } catch (IllegalBlockSizeException e) {
	        	e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	        } catch (IOException e) {
	        }
	        return null;
	    }

	}
	
    public String getSecret(){
    	
    	String cryptKey = null;    	
    		
		if (WinkhouseUtils.getInstance()
				          .getHm_winkSys()
						  .get(IWinkSysProperties.CRIPTKEY) != null){ 
			
			cryptKey = WinkhouseUtils.getInstance()
					                 .getHm_winkSys()
					                 .get(IWinkSysProperties.CRIPTKEY);				
					
		}else{
			
			cryptKey = "winkhouse";				
						
		}
		if (!cryptKey.equalsIgnoreCase("winkhouse")){
			StringEncrypter wse = new StringEncrypter(WinkhouseUtils.PATHKEYVIEW);
			cryptKey = wse.decrypt(cryptKey);
		}
    	return cryptKey;
    }
    
    public String setSecret(String encryptString){
		StringEncrypter wse = new StringEncrypter(WinkhouseUtils.PATHKEYVIEW);
		return wse.encrypt(encryptString);
    }
    
//    public String DecryptString(String encrypString){
//    	
//		String decryptResult = null;
//		
//		StringEncrypter se = new StringEncrypter(getSecret());
//		decryptResult = se.decrypt(encrypString);						
//		
//		return decryptResult;
//    	
//    }
    
    public String DecryptStringStandard(String encrypString){
    	
		String decryptResult = null;
		
		StringEncrypter se = new StringEncrypter(PATHKEYVIEW);
		decryptResult = se.decrypt(encrypString);						
		
		return decryptResult;
    	
    }
    
//    public String EncryptString(String dencrypString){
//    	
//		String encryptResult = null;
//		
//		StringEncrypter se = new StringEncrypter(getSecret());
//		encryptResult = se.encrypt(dencrypString);						
//		
//		return encryptResult;
//    	
//    }

    public String EncryptStringStandard(String dencrypString){
    	
		String encryptResult = null;
		
		StringEncrypter se = new StringEncrypter(PATHKEYVIEW);
		encryptResult = se.encrypt(dencrypString);						
		
		return encryptResult;
    	
    }
    
    
    public boolean isGMailAccount(String mail){
    	
    	return mail.endsWith("@gmail.com");
    	
    }

    public Object getObjectMethodValue(Object o, 
    								   String methodName,
    								   Class<?>[] parameterTypes, 
    								   Array parameters) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    	
    	Method m = o.getClass().getMethod(methodName, parameterTypes);
    	Object return_Value = m.invoke(o, parameters);
    	
    	return return_Value;
    	
    	
    }

    public ArrayList<ObjectSearchGetters> getCampiPersonaliSearchGetters() {
		return campiPersonaliSearchGetters;
	}
	
    public ArrayList<ObjectSearchGetters> getCampiPersonaliReportSearchGetters() {
    	
		if (campiPersonaliReportSearchGetters == null){
			campiPersonaliReportSearchGetters = new ArrayList<ObjectSearchGetters>();
			campiPersonaliReportSearchGetters.add(new ObjectSearchGetters(1,"getIdAttribute","codice campo","DATAAPPUNTAMENTO",""));
			campiPersonaliReportSearchGetters.add(new ObjectSearchGetters(2,"getAttributeName","nome campo","ATTRIBUTENAME",""));
			campiPersonaliReportSearchGetters.add(new ObjectSearchGetters(3,"getFieldTypeForReport","tipo campo","FIELDTYPE",""));
			campiPersonaliReportSearchGetters.add(new ObjectSearchGetters(4,"getValueForReport","valore","",""));
		}
		return campiPersonaliReportSearchGetters;
	}
	
    public void setImmobileSearchGetters(
			ArrayList<ObjectSearchGetters> immobileSearchGetters) {
		this.immobileSearchGetters = immobileSearchGetters;
	}
	
    public void setImmobileReportSearchGetters(
			ArrayList<ObjectSearchGetters> immobileReportSearchGetters) {
		this.immobileReportSearchGetters = immobileReportSearchGetters;
	}
	
    public void setAnagraficheSearchGetters(
			ArrayList<ObjectSearchGetters> anagraficheSearchGetters) {
		this.anagraficheSearchGetters = anagraficheSearchGetters;
	}
	
    public void setAnagraficheReportSearchGetters(
			ArrayList<ObjectSearchGetters> anagraficheReportSearchGetters) {
		this.anagraficheReportSearchGetters = anagraficheReportSearchGetters;
	}
	
    public void setColloquiSearchGetters(
			ArrayList<ObjectSearchGetters> colloquiSearchGetters) {
		this.colloquiSearchGetters = colloquiSearchGetters;
	}
	
    public void setColloquiReportSearchGetters(
			ArrayList<ObjectSearchGetters> colloquiReportSearchGetters) {
		this.colloquiReportSearchGetters = colloquiReportSearchGetters;
	}
    
    public void resetObjectSearchGetters_Personal(){
    	setImmobileSearchGetters(null);
    	setImmobileReportSearchGetters(null);
    	setAnagraficheSearchGetters(null);
    	setAnagraficheReportSearchGetters(null);
    	setColloquiReportSearchGetters(null);
    	setColloquiSearchGetters(null);
    }

	public ArrayList<PerspectiveInfo> getPerspectiveInfo() {
		if (perspectiveInfo == null){
			perspectiveInfo = new ArrayList<WinkhouseUtils.PerspectiveInfo>();
			
			perspectiveInfo.add(new PerspectiveInfo(Activator.getImageDescriptor("icons/gohome.png").createImage(), 
							    					"Immobili", 
							    					ImmobiliPerspective.ID));
			
			perspectiveInfo.add(new PerspectiveInfo(Activator.getImageDescriptor("icons/anagrafiche16.png").createImage(), 
													"Anagrafiche", 
													AnagrafichePerspective.ID));
			
			perspectiveInfo.add(new PerspectiveInfo(Activator.getImageDescriptor("icons/affitti.png").createImage(), 
													"Affitti", 
													AffittiPerspective.ID));
			
			perspectiveInfo.add(new PerspectiveInfo(Activator.getImageDescriptor("icons/korgac16.png").createImage(), 
													"Agenda", 
													AgendaPerspective.ID));

			perspectiveInfo.add(new PerspectiveInfo(Activator.getImageDescriptor("icons/kontact_news16.png").createImage(), 
												    "Report", 
												    ReportPerspective.ID));

			perspectiveInfo.add(new PerspectiveInfo(Activator.getImageDescriptor("icons/korgac16.png").createImage(), 
													"Dati di base", 
													DatiBasePerspective.ID));

			perspectiveInfo.add(new PerspectiveInfo(Activator.getImageDescriptor("icons/lock16.png").createImage(), 
													"Permessi", 
													PermessiPerspective.ID));

			perspectiveInfo.add(new PerspectiveInfo(Activator.getImageDescriptor("icons/desktop.png").createImage(), 
								"Scrivania", 
								DesktopPerspective.ID));

		}
		return perspectiveInfo;
	}
	
	public void setPerspectiveInfo(ArrayList<PerspectiveInfo> perspectiveInfo) {
		this.perspectiveInfo = perspectiveInfo;
	}

	public PerspectiveInfo getPerspectiveByID(String perspectiveID){
		
		for (PerspectiveInfo pi : getPerspectiveInfo()) {
			if (pi.getId().equalsIgnoreCase(perspectiveID)){
				return pi;
			}
		}
		return null;
		
	}

	public Agenti getLoggedAgent() {
		return loggedAgent;
	}

	public void setLoggedAgent(Agenti loggedAgent) {
		this.loggedAgent = loggedAgent;
	}

    public ArrayList<ViewInfo> getViewInfo(){
    	
    	if (viewInfo == null){
    		viewInfo = new ArrayList<WinkhouseUtils.ViewInfo>();
    		
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/sample2.gif").createImage(), "Dati di Base", DatiBaseView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/colloqui.png").createImage(), "Colloqui", ColloquiView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/immobile.png").createImage(), "Dettaglio immobile", DettaglioImmobileView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/immagini.png").createImage(), "Immagni immobile", ImmaginiImmobiliView.ID));
    		
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/immobile.png").createImage(), "Lista immobili", ImmobiliTreeView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/recapiti.png").createImage(), "Recapiti", RecapitiView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/anagrafiche.png").createImage(), "Lista anagrafiche", AnagraficaTreeView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/anagrafiche.png").createImage(), "Dettaglio anagrafica", DettaglioAnagraficaView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/colloqui16.png").createImage(), "Dettaglio colloquio", DettaglioColloquioView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/kontact_news.png").createImage(), "Dettaglio report", DettaglioReportView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/kontact_news.png").createImage(), "Lista report", ReportTreeView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/korgac.png").createImage(), "Lista appuntamenti", ListaAppuntamentiView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/calendario.png").createImage(), "Calendario appuntamenti", CalendarioView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/korgac.png").createImage(), "Dettaglio appuntamento", DettaglioAppuntamentoView.ID));
    		
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/anagrafica.png").createImage(), "Anagrafiche proprietarie", AnagrafichePropietarieView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/anagraficheabbinate.png").createImage(), "Abbinamenti", AbbinamentiView.ID));    		
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/immobile.png").createImage(), "Immobili proprieta", ImmobiliPropietaView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/affitti.png").createImage(), "Lista affitti", ListaAffittiView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/affitti20.png").createImage(), "Dettaglio affitto", DettaglioAffittiView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/campi_personali20.png").createImage(), "Campi personali", EAVView.ID));

    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/www.png").createImage(), "Mappa", MapView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/looknfeel.png").createImage(), "Agenti", AgentiView.ID));
    		viewInfo.add(new ViewInfo(Activator.getImageDescriptor("icons/lock16.png").createImage(), "Dettaglio Permessi Agente", DettaglioPermessiAgenteView.ID));

    		
    	}
    	
    	return viewInfo;
    }

	public ViewInfo getViewByID(String viewID){
		
		for (ViewInfo pi : getViewInfo()) {
			if (pi.getId().equalsIgnoreCase(viewID)){
				return pi;
			}
		}
		return null;
		
	}

    public ArrayList<DialogInfo> getDialogInfo(){
    	
    	if (dialogInfo == null){
    		dialogInfo = new ArrayList<WinkhouseUtils.DialogInfo>();
    		
    		dialogInfo.add(new DialogInfo(Activator.getImageDescriptor("icons/kfind.png").createImage(), "Wizard ricerca", RicercaWizard.ID));
    		dialogInfo.add(new DialogInfo(Activator.getImageDescriptor("icons/colloqui.png").createImage(), "Wizard colloqui", ColloquiWizard.ID));
    		dialogInfo.add(new DialogInfo(Activator.getImageDescriptor("icons/google_calendar.png").createImage(), "Wizard sincronizzazione Google Calendar", GCalendarSyncWizard.ID));
    		
    		dialogInfo.add(new DialogInfo(Activator.getImageDescriptor("icons/kfind.png").createImage(), "Finestra creazione ricerca", DettaglioImmobileView.ID));    		
    		dialogInfo.add(new DialogInfo(Activator.getImageDescriptor("icons/kfind.png").createImage(), "Finestra lista ricerche", ImmobiliTreeView.ID));
    		
    		dialogInfo.add(new DialogInfo(Activator.getImageDescriptor("icons/googleconf.png").createImage(), "Finestra modifica dati account Google", GoogleConfDialog.ID));
    		
    		dialogInfo.add(new DialogInfo(Activator.getImageDescriptor("icons/anagrafiche.png").createImage(), "Finestra visualizzazione immagine", ImageViewer.ID));
    		dialogInfo.add(new DialogInfo(Activator.getImageDescriptor("icons/settings16.png").createImage(), "Finestra delle impostazioni programma", WinkPreferenceDialog.ID));

    		dialogInfo.add(new DialogInfo(Activator.getImageDescriptor("icons/immagini.png").createImage(), "Finestra di selezione e ricerca anagrafiche", PopUpRicercaAnagrafica.ID));
    		dialogInfo.add(new DialogInfo(Activator.getImageDescriptor("icons/looknfeel.png").createImage(), "Finestra di selezione e ricerca agenti", PopUpRicercaAgenti.ID));


    	}
    	
    	return dialogInfo;
    }

	public DialogInfo getDialogByID(String dialogID){
		
		for (DialogInfo pi : getDialogInfo()) {
			if (pi.getId().equalsIgnoreCase(dialogID)){
				return pi;
			}
		}
		return null;
		
	}

	
	public HashMap<String, String> getHm_winkSys() {
		
		if (hm_winkSys == null){
			
			hm_winkSys = new HashMap<String, String>();
			
			WinkSysDAO wsDAO = new WinkSysDAO();
			ArrayList<WinkSysVO> al_wsvos = wsDAO.getProperties();
			
			for (WinkSysVO winkSysVO : al_wsvos) {
				hm_winkSys.put(winkSysVO.getPropertyName(), winkSysVO.getPropertyValue());
			}
			
		}
		return hm_winkSys;
	}

	public void setHm_winkSys(HashMap<String, String> hm) {
		hm_winkSys = hm;
	}

	
	public boolean isBundleDBRunning() {
		return isBundleDBRunning;
	}

	public void setBundleDBRunning(boolean isBundleDBRunning) {
		this.isBundleDBRunning = isBundleDBRunning;
	}
	
}
