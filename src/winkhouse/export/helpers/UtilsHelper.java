package winkhouse.export.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AllegatiColloquiDAO;
import winkhouse.dao.AllegatiImmobiliDAO;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.dao.ColloquiCriteriRicercaDAO;
import winkhouse.dao.ContattiDAO;
import winkhouse.dao.DatiCatastaliDAO;
import winkhouse.dao.ImmaginiDAO;
import winkhouse.dao.ImmobiliPropietariDAO;
import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.dao.StatoConservativoDAO;
import winkhouse.dao.TipiAppuntamentiDAO;
import winkhouse.dao.TipologiaContattiDAO;
import winkhouse.dao.TipologiaStanzeDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.export.models.ObjectSearchGetters;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.orm.Agenti;
import winkhouse.orm.Classicliente;
import winkhouse.orm.Tipologiecontatti;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.DatiCatastaliVO;
import winkhouse.vo.ImmagineVO;
import winkhouse.vo.ImmobiliPropietariVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipiAppuntamentiVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.vo.TipologieColloquiVO;
import winkhouse.vo.TipologieImmobiliVO;

public class UtilsHelper {

	private static UtilsHelper instance = null;
	private ArrayList<ObjectSearchGetters> immobileSearchGetters = null;
	private ArrayList<ObjectSearchGetters> anagraficheSearchGetters = null;
	private ArrayList<ObjectSearchGetters> immobiliAffittiSearchGetters = null;
	private ArrayList<ObjectSearchGetters> colloquiSearchGetters = null;
	
	public static UtilsHelper getInstance(){
		if (instance == null){
			instance = new UtilsHelper();
		}
		return instance;
	}
	
	private UtilsHelper() {
		
	}
	
	public ArrayList<TipologieImmobiliVO> getTipologieImmobili() {
		return MobiliaDatiBaseCache.getInstance().getTipologieImmobili();
	}
	
	public ArrayList<StatoConservativoVO> getStatiConservativi() {
		return MobiliaDatiBaseCache.getInstance().getStatiConservativi();
	}
	
	public ArrayList<RiscaldamentiVO> getRiscaldamenti() {
		return MobiliaDatiBaseCache.getInstance().getRiscaldamenti();
	}
	
	public ArrayList<TipologiaStanzeVO> getTipologieStanze() {
		return MobiliaDatiBaseCache.getInstance().getTipologieStanze();
	}

	public ArrayList<Tipologiecontatti> getTipologieContatti() {
		return MobiliaDatiBaseCache.getInstance().getTipologieContatti();
	}
	
	public ArrayList<Agenti> getAgenti() {
		return MobiliaDatiBaseCache.getInstance().getAgenti();
	}
	
	public ArrayList<Classicliente> getClassiClienti() {
		return MobiliaDatiBaseCache.getInstance().getClassiClienti();
	}
	
	/**
	 * Ritorna una lista di oggetti ObjectSearchGetters, che rappresentano una mappatura dei metodi relativi alla classe ImmobiliModel.
	 * @return ArrayList<ObjectSearchGetters>
	 */
	public ArrayList<ObjectSearchGetters> getImmobileSearchGetters(){
		
		if (immobileSearchGetters == null){
			
			immobileSearchGetters = new ArrayList<ObjectSearchGetters>();
			
			ArrayList<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> al = WinkhouseUtils.getInstance().getImmobileSearchGetters();
			Iterator<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> it = al.iterator();
			
			while (it.hasNext()){
				winkhouse.util.WinkhouseUtils.ObjectSearchGetters osg = it.next();
				immobileSearchGetters.add(new ObjectSearchGetters(osg.getKey(), 
																  osg.getMethodName(), 
																  osg.getDescrizione(), 
																  osg.getColumnName(), 
																  osg.getParametrizedTypeName(),
																  osg.getIsPersonal()
																  ));
			}
						  
		}
		return immobileSearchGetters;
	}

	/**
	 * Ritorna una lista di oggetti ObjectSearchGetters, che rappresentano una mappatura dei metodi relativi alla classe ImmobiliModel.
	 * A differenza del metodo getImmobileSearchGetters questo metodo ritorna anche la mappatura dei metodi per ottenere gli oggetti 
	 * collegati all'immobile come ad esempio la tipologia, lo stato conservativo, la classe energetica, ecc... 
	 * 
	 * @return ArrayList<ObjectSearchGetters>
	 */
	public ArrayList<ObjectSearchGetters> getImmobileReportSearchGetters(){
		
		if (immobileSearchGetters == null){
			
			immobileSearchGetters = new ArrayList<ObjectSearchGetters>();
			
			ArrayList<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> al = WinkhouseUtils.getInstance().getImmobileReportSearchGetters();
			Iterator<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> it = al.iterator();
			
			while (it.hasNext()){
				winkhouse.util.WinkhouseUtils.ObjectSearchGetters osg = it.next();
				immobileSearchGetters.add(new ObjectSearchGetters(osg.getKey(), 
																  osg.getMethodName(), 
																  osg.getDescrizione(), 
																  osg.getColumnName(), 
																  osg.getParametrizedTypeName(),
																  osg.getIsPersonal()
																  ));
			}
						  
		}
		return immobileSearchGetters;
	}

	
	/**
	 * Ritorna una lista di oggetti ObjectSearchGetters, che rappresentano una mappatura dei metodi relativi 
	 * alla classe ImmobiliModel con l'estensione degli affitti.
	 * @return ArrayList<ObjectSearchGetters>
	 */
	public ArrayList<ObjectSearchGetters> getImmobileAffittiSearchGetters(){
		
		if (immobiliAffittiSearchGetters == null){
			
			immobiliAffittiSearchGetters = new ArrayList<ObjectSearchGetters>();
			
			ArrayList<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> al = WinkhouseUtils.getInstance().getImmobileAffittiSearchGetters();
			Iterator<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> it = al.iterator();
			
			while (it.hasNext()){
				winkhouse.util.WinkhouseUtils.ObjectSearchGetters osg = it.next();
				immobiliAffittiSearchGetters.add(new ObjectSearchGetters(osg.getKey(), 
																  osg.getMethodName(), 
																  osg.getDescrizione(), 
																  osg.getColumnName(), 
																  osg.getParametrizedTypeName()
																  ));
			}
						  
		}
		return immobiliAffittiSearchGetters;
	}

	/**
	 * Ritorna una lista di oggetti ObjectSearchGetters, che rappresentano una mappatura dei metodi relativi alla classe AnagraficheModel.
	 * @return ArrayList<ObjectSearchGetters>
	 */
	public ArrayList<ObjectSearchGetters> getAnagraficheSearchGetters(){
		
		if (anagraficheSearchGetters == null){
			
			anagraficheSearchGetters = new ArrayList<ObjectSearchGetters>();
			
			ArrayList<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> al = WinkhouseUtils.getInstance().getAnagraficheSearchGetters();
			Iterator<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> it = al.iterator();
			
			while (it.hasNext()){
				winkhouse.util.WinkhouseUtils.ObjectSearchGetters osg = it.next();
				anagraficheSearchGetters.add(new ObjectSearchGetters(osg.getKey(), 
																  osg.getMethodName(), 
																  osg.getDescrizione(), 
																  osg.getColumnName(), 
																  osg.getParametrizedTypeName(),
																  osg.getIsPersonal()
																  ));
			}
						  
		}
		return anagraficheSearchGetters;
	}

	/**
	 * Ritorna una lista di oggetti ObjectSearchGetters, che rappresentano una mappatura dei metodi relativi alla classe AnagraficheModel.
	 * A differenza del metodo getAnagraficheSearchGetters questo metodo ritorna anche la mappatura dei metodi per ottenere gli oggetti 
	 * collegati all'anagrafica come ad esempio la classe cliente, i contatti, i colloqui, gli abbinamenti, ecc...
	 * @return ArrayList<ObjectSearchGetters>
	 */
	public ArrayList<ObjectSearchGetters> getAnagraficheReportSearchGetters(){
		
		if (anagraficheSearchGetters == null){
			
			anagraficheSearchGetters = new ArrayList<ObjectSearchGetters>();
			
			ArrayList<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> al = WinkhouseUtils.getInstance().getAnagraficheReportSearchGetters();
			Iterator<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> it = al.iterator();
			
			while (it.hasNext()){
				winkhouse.util.WinkhouseUtils.ObjectSearchGetters osg = it.next();
				anagraficheSearchGetters.add(new ObjectSearchGetters(osg.getKey(), 
																  osg.getMethodName(), 
																  osg.getDescrizione(), 
																  osg.getColumnName(), 
																  osg.getParametrizedTypeName(),
																  osg.getIsPersonal()
																  ));
			}
						  
		}
		return anagraficheSearchGetters;
	}

	/**
	 * Ritorna una lista di oggetti ObjectSearchGetters, che rappresentano una mappatura dei metodi relativi alla classe ColloquiModel.
	 * @return ArrayList<ObjectSearchGetters>
	 */
	public ArrayList<ObjectSearchGetters> getColloquiSearchGetters(){
		
		if (colloquiSearchGetters == null){
			
			colloquiSearchGetters = new ArrayList<ObjectSearchGetters>();
			
			ArrayList<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> al = WinkhouseUtils.getInstance().getColloquiSearchGetters();
			Iterator<winkhouse.util.WinkhouseUtils.ObjectSearchGetters> it = al.iterator();
			
			while (it.hasNext()){
				winkhouse.util.WinkhouseUtils.ObjectSearchGetters osg = it.next();
				colloquiSearchGetters.add(new ObjectSearchGetters(osg.getKey(), 
																  osg.getMethodName(), 
																  osg.getDescrizione(), 
																  osg.getColumnName(), 
																  osg.getParametrizedTypeName(),
																  osg.getIsPersonal()
																  ));
			}
						  
		}
		return colloquiSearchGetters;
	}
	
	public AgentiVO findAgentiByCod(Integer codice){
		return MobiliaDatiBaseCache.getInstance().findAgentiByCod(codice);
	}
	
	/**
	 * Ritorna una lista di oggetti AgentiVO, che hanno le propriet� di nome, cognome, citta e indirizzo uguali 
	 * all'oggetto passato in input.
	 * 
	 * @param agente
	 * @return ArrayList<AgentiVO>
	 */

	public ArrayList<AgentiVO> findAgentiByDescription(AgentiVO agente){
		
		ArrayList<AgentiVO> returnValue = new ArrayList<AgentiVO>();
		AgentiDAO aDAO = new AgentiDAO();
		returnValue = aDAO.findAgentiByNomeCognomeCittaIndirizzo(AgentiVO.class.getName(), agente, null);
		
		return returnValue;
	}

	/**
	 * Ritorna la lista di tutti gli agenti presenti nella base dati. 
	 * 
	 * @return ArrayList<AgentiVO>
	 */
	
	public ArrayList<AgentiVO> getAllAgenti(){
		
		ArrayList<AgentiVO> returnValue = new ArrayList<AgentiVO>();
		AgentiDAO aDAO = new AgentiDAO();
		returnValue = aDAO.list(AgentiVO.class.getName());
		
		return returnValue;
	}
	
	
	public TipiAppuntamentiVO findTipiAppuntamentiByCod(Integer codice){
		return MobiliaDatiBaseCache.getInstance().findTipiAppuntamentiByCod(codice);
	}

	/**
	 * Ritorna una lista di oggetti TipiAppuntamentiVO, con propriet� descrizione uguale a quella  
	 * dell'oggetto passato in input.
	 * 
	 * @param tipoAppuntamento
	 * @return ArrayList<TipiAppuntamentiVO>
	 */

	public ArrayList<TipiAppuntamentiVO> findTipiAppuntamentiByDescription(TipiAppuntamentiVO tipoAppuntamento){
		
		ArrayList<TipiAppuntamentiVO> returnValue = new ArrayList<TipiAppuntamentiVO>();
		TipiAppuntamentiDAO taDAO = new TipiAppuntamentiDAO();
		returnValue = taDAO.getTipiAppuntamentiByName(TipiAppuntamentiVO.class.getName(), 
													  tipoAppuntamento.getDescrizione());

		
		return returnValue;
	}

	/**
	 * Ritorna la lista di tutte le tipologie di appuntamenti presenti nella base dati. 
	 * 
	 * @return ArrayList<TipiAppuntamentiVO>
	 */
	
	public ArrayList<TipiAppuntamentiVO> getAllTipiAppuntamenti(){
		
		ArrayList<TipiAppuntamentiVO> returnValue = new ArrayList<TipiAppuntamentiVO>();
		TipiAppuntamentiDAO taDAO = new TipiAppuntamentiDAO();
		returnValue = taDAO.listTipiAppuntamenti(TipiAppuntamentiVO.class.getName());
		
		return returnValue;
		
	}
	
	public ClassiClientiVO findClassiClientiByCod(Integer codice){
		return MobiliaDatiBaseCache.getInstance().findClassiClientiByCod(codice);
	}

	/**
	 * Ritorna una lista di oggetti ClassiClientiVO, con propriet� descrizione uguale a quella  
	 * dell'oggetto passato in input.
	 * 
	 * @param classeCliente
	 * @return ArrayList<ClassiClientiVO>
	 */

	public ArrayList<ClassiClientiVO> findClassiClientiByDescription(ClassiClientiVO classeCliente){
		
		ArrayList<ClassiClientiVO> returnValue = new ArrayList<ClassiClientiVO>();
		
		ClassiClientiDAO clDAO = new ClassiClientiDAO();
		returnValue = clDAO.getClasseClienteByDescrizione(ClassiClientiVO.class.getName(), 
													  	  classeCliente.getDescrizione());

		
		return returnValue;
	}

	/**
	 * Ritorna la lista di tutte le classi clienti presenti nella base dati. 
	 * 
	 * @return ArrayList<ClassiClientiVO>
	 */
	
	public ArrayList<ClassiClientiVO> getAllClassiClienti(){
		
		ArrayList<ClassiClientiVO> returnValue = new ArrayList<ClassiClientiVO>();
		
		ClassiClientiDAO clDAO = new ClassiClientiDAO();
		returnValue = new ArrayList<ClassiClientiVO>();//clDAO.list(ClassiClientiVO.class.getName());
		
		return returnValue;
	}	
	
	public ClasseEnergeticaVO findClasseEnergeticaByCod(Integer codice){
		return MobiliaDatiBaseCache.getInstance().findClasseEnergeticaByCod(codice);
	}

	/**
	 * Ritorna una lista di oggetti ClasseEnergeticaVO, con propriet� descrizione uguale a quella  
	 * dell'oggetto passato in input.
	 * 
	 * @param classeEnergetica
	 * @return ArrayList<ClasseEnergeticaVO>
	 */

	public ArrayList<ClasseEnergeticaVO> findClasseEnergeticaByDescription(ClasseEnergeticaVO classeEnergetica){
		
		ArrayList<ClasseEnergeticaVO> returnValue = new ArrayList<ClasseEnergeticaVO>();
		
		ClassiEnergeticheDAO ceDAO = new ClassiEnergeticheDAO();
		returnValue = ceDAO.getClassiEnergeticheByName(ClasseEnergeticaVO.class.getName(), 
													   classeEnergetica.getDescrizione());

		
		return returnValue;
	}

	/**
	 * Ritorna la lista di tutte le classi clienti presenti nella base dati. 
	 * 
	 * @return ArrayList<ClasseEnergeticaVO>
	 */
	
	public ArrayList<ClasseEnergeticaVO> getAllClasseEnergetica(){
		
		ArrayList<ClasseEnergeticaVO> returnValue = new ArrayList<ClasseEnergeticaVO>();
		
		ClassiEnergeticheDAO ceDAO = new ClassiEnergeticheDAO();
		returnValue = ceDAO.listClassiEnergetiche(ClasseEnergeticaVO.class.getName());
		
		return returnValue;
	}	
	
	public RiscaldamentiVO findRiscaldamentiByCod(Integer codice){
		return MobiliaDatiBaseCache.getInstance().findRiscaldamentiByCod(codice);
	}

	/**
	 * Ritorna una lista di oggetti RiscaldamentiVO, con propriet� descrizione uguale a quella  
	 * dell'oggetto passato in input.
	 * 
	 * @param classeCliente
	 * @return ArrayList<RiscaldamentiVO>
	 */

	public ArrayList<RiscaldamentiVO> findRiscaldamentiByDescription(RiscaldamentiVO riscaldamento){
		
		ArrayList<RiscaldamentiVO> returnValue = new ArrayList<RiscaldamentiVO>();
		
		RiscaldamentiDAO rDAO = new RiscaldamentiDAO();
		returnValue = rDAO.getRiscaldamentoByDescrizione(RiscaldamentiVO.class.getName(), 
														 riscaldamento.getDescrizione());

		
		return returnValue;
	}
	
	/**
	 * Ritorna la lista di tutti i tipi riscaldamento presenti nella base dati. 
	 * 
	 * @return ArrayList<RiscaldamentiVO>
	 */

	public ArrayList<RiscaldamentiVO> getAllRiscaldamenti(){
		
		ArrayList<RiscaldamentiVO> returnValue = new ArrayList<RiscaldamentiVO>();
		
		RiscaldamentiDAO rDAO = new RiscaldamentiDAO();
		returnValue = rDAO.list(RiscaldamentiVO.class.getName());
		
		return returnValue;
	}
	
	public StatoConservativoVO findStatoConservativoByCod(Integer codice){
		return MobiliaDatiBaseCache.getInstance().findStatoConservativoByCod(codice);
	}

	/**
	 * Ritorna una lista di oggetti StatoConservativoVO, con propriet� descrizione uguale a quella  
	 * dell'oggetto passato in input.
	 * 
	 * @param statoConservativo
	 * @return ArrayList<StatoConservativoVO>
	 */

	public ArrayList<StatoConservativoVO> findStatoConservativoByDescription(StatoConservativoVO statoConservativo){
		
		ArrayList<StatoConservativoVO> returnValue = new ArrayList<StatoConservativoVO>();
		
		StatoConservativoDAO scDAO = new StatoConservativoDAO();
		returnValue = scDAO.getStatoConservativoByDescrizione(StatoConservativoVO.class.getName(), 
															  statoConservativo.getDescrizione());

		
		return returnValue;
	}

	/**
	 * Ritorna la lista di tutti i tipi di stati conservativi presenti nella base dati. 
	 * 
	 * @return ArrayList<StatoConservativoVO>
	 */

	public ArrayList<StatoConservativoVO> getAllStatoConservativo(){
		
		ArrayList<StatoConservativoVO> returnValue = new ArrayList<StatoConservativoVO>();
		
		StatoConservativoDAO scDAO = new StatoConservativoDAO();
		returnValue = scDAO.list(StatoConservativoVO.class.getName());
		
		return returnValue;
	}
	
	public TipologieImmobiliVO findTipologiaByCod(Integer codice){
		return MobiliaDatiBaseCache.getInstance().findTipologiaByCod(codice);
	}

	/**
	 * Ritorna una lista di oggetti TipologieImmobiliVO, con propriet� descrizione uguale a quella  
	 * dell'oggetto passato in input.
	 * 
	 * @param tipologiaImmobile
	 * @return ArrayList<TipologieImmobiliVO>
	 */

	public ArrayList<TipologieImmobiliVO> findTipologieImmobiliByDescription(TipologieImmobiliVO tipologiaImmobile){
		
		ArrayList<TipologieImmobiliVO> returnValue = new ArrayList<TipologieImmobiliVO>();
		
		TipologieImmobiliDAO tiDAO = new TipologieImmobiliDAO();
		returnValue = tiDAO.getTipologieImmobiliByDescrizione(TipologieImmobiliVO.class.getName(), 
															  tipologiaImmobile.getDescrizione());

		
		return returnValue;
	}

	/**
	 * Ritorna la lista di tutte le tipologie di immobile presenti nella base dati. 
	 * 
	 * @return ArrayList<TipologieImmobiliVO>
	 */

	public ArrayList<TipologieImmobiliVO> getAllTipologieImmobili(){
		
		ArrayList<TipologieImmobiliVO> returnValue = new ArrayList<TipologieImmobiliVO>();
		
		TipologieImmobiliDAO tiDAO = new TipologieImmobiliDAO();
		returnValue = tiDAO.list(TipologieImmobiliVO.class.getName());
		
		return returnValue;
	}

	/**
	 * Ritorna una lista di oggetti ImmagineVO, appartenenti all'immobile passato in input ed aventi come
	 * Utilizzare il metodo getImmobiliExist della classe winkhouse.export.helpers.ImmobiliHelper 
	 * per individuare una lista di immobili da passare al metodo.
	 *  
	 * @param immagine
	 * @param immobile
	 * @return ArrayList<ImmagineVO>
	 */

	public ArrayList<ImmagineVO> findImmagineByDescription(ImmagineVO immagine,ImmobiliVO immobile){
		
		ArrayList<ImmagineVO> returnValue = new ArrayList<ImmagineVO>();
		
		ImmaginiDAO iDAO = new ImmaginiDAO();
		ArrayList<ImmagineVO> tmp = iDAO.getImmaginiByImmobile(ImmagineVO.class.getName(),
									   		     			   immobile.getCodImmobile());
		
		Iterator<ImmagineVO> it = tmp.iterator(); 
		
		while(it.hasNext()){
			
			ImmagineVO tmpimg = it.next();
			
			if (tmpimg.getCodImmobile().intValue() == immobile.getCodImmobile().intValue()){
				returnValue.add(tmpimg);
			}
			
		}
		
		return returnValue;
	}
	
	/**
	 * Ritorna una lista di oggetti AllegatiImmobiliVO, con propriet� nome uguale a quella dell'oggetto, 
	 * di tipo AllegatiImmobiliVO passato in input.
	 * Vengono inoltre filtrati per l'immobile passato in input. (Utilizzare il metodo getImmobiliExist 
	 * della classe winkhouse.export.helpers.ImmobiliHelper.getImmobiliExist per individuare una lista di immobili da passare
	 * al metodo). 
	 * 
	 * @param allegato
	 * @param immobile
	 * @return ArrayList<AllegatiImmobiliVO>
	 */

	public ArrayList<AllegatiImmobiliVO> findAllegatiImmobiliByDescription(AllegatiImmobiliVO allegato,ImmobiliVO immobile){
		
		ArrayList<AllegatiImmobiliVO> returnValue = new ArrayList<AllegatiImmobiliVO>();
		
		AllegatiImmobiliDAO aiDAO = new AllegatiImmobiliDAO();
		ArrayList<AllegatiImmobiliVO> tmp = aiDAO.getAllegatiByImmobile(AllegatiImmobiliVO.class.getName(), 
												  					    immobile.getCodImmobile());
		
		Iterator<AllegatiImmobiliVO> it = tmp.iterator();
		
		while (it.hasNext()) {
			
			AllegatiImmobiliVO allegatiImmobiliVO = (AllegatiImmobiliVO) it.next();
			
			if (allegatiImmobiliVO.getNome().equalsIgnoreCase(allegato.getNome())){
				returnValue.add(allegato);
			}
			
		}
				
		return returnValue;
	}
	
	/**
	 * Ritorna una lista di oggetti AllegatiColloquiVO, con propriet� nome uguale a quella dell'oggetto, 
	 * di tipo AllegatiColloquiVO passato in input.
	 * Vengono inoltre filtrati per l'immobile passato in input. (Utilizzare il metodo getImmobiliExist 
	 * della classe winkhouse.export.helpers.getImmobiliExist per individuare una lista di immobili da passare
	 * al metodo). 
	 * 
	 * @param allegato
	 * @param colloquio
	 * @return ArrayList<AllegatiColloquiVO>
	 */

	public ArrayList<AllegatiColloquiVO> findAllegatiColloquiByDescription(AllegatiColloquiVO allegato,ColloquiVO colloquio){
		
		ArrayList<AllegatiColloquiVO> returnValue = new ArrayList<AllegatiColloquiVO>();
		
		AllegatiColloquiDAO acDAO = new AllegatiColloquiDAO();
		ArrayList<AllegatiColloquiVO> tmp = acDAO.getAllegatiByColloquio(AllegatiColloquiVO.class.getName(), 
																		 colloquio.getCodColloquio());
		
		Iterator<AllegatiColloquiVO> it = tmp.iterator();
		
		while (it.hasNext()) {
			
			AllegatiColloquiVO allegatiColloquiVO = (AllegatiColloquiVO) it.next();
			
			if (allegatiColloquiVO.getNome().equalsIgnoreCase(allegato.getNome())){
				returnValue.add(allegato);
			}
			
		}
				
		return returnValue;
	}
	
	/**
	 * Ritorna una lista di oggetti DatiCatastaliVO, con propriet� categoria, foglio, particella, subalterno uguale a quella dell'oggetto, 
	 * di tipo DatiCatastaliVO passato in input.
	 * Vengono inoltre filtrati per l'immobile passato in input. (Utilizzare il metodo getImmobiliExist 
	 * della classe winkhouse.export.helpers.ImmobiliHelper.getImmobiliExist per individuare una lista di immobili da passare
	 * al metodo). 
	 * 
	 * @param datiCatastali
	 * @param immobile
	 * @return ArrayList<DatiCatastaliVO>
	 */

	public ArrayList<DatiCatastaliVO> findDatiCatastaliByDescription(DatiCatastaliVO datiCatastali,ImmobiliVO immobile){
		
		ArrayList<DatiCatastaliVO> returnValue = new ArrayList<DatiCatastaliVO>();
		
		DatiCatastaliDAO dcDAO = new DatiCatastaliDAO();
		ArrayList<DatiCatastaliVO> tmp = dcDAO.findADatiCatastaliByCodImmobile(DatiCatastaliVO.class.getName(),  
																		 	   immobile.getCodImmobile());
		
		Iterator<DatiCatastaliVO> it = tmp.iterator();
		
		while (it.hasNext()) {
			
			DatiCatastaliVO datiCatastaliVO = (DatiCatastaliVO) it.next();
			
			if (datiCatastaliVO.getCategoria().equalsIgnoreCase(datiCatastali.getCategoria()) &&
				datiCatastaliVO.getFoglio().equalsIgnoreCase(datiCatastali.getFoglio()) &&
				datiCatastaliVO.getParticella().equalsIgnoreCase(datiCatastali.getParticella()) &&
				datiCatastaliVO.getSubalterno().equalsIgnoreCase(datiCatastali.getSubalterno())){
				
				returnValue.add(datiCatastali);
				
			}
			
		}
				
		return returnValue;
	}
	
	/**
	 * Ritorna una lista di oggetti CriteriRicercaModel, con propriet� getterMethodName, fromValue, toValue, logicalOperator, lineNumber,
	 * uguale a quella dell'oggetto di tipo CriteriRicercaModel passato in input.
	 * Vengono inoltre filtrati per il colloquio passato in input. (Utilizzare il metodo getColloquiExist 
	 * della classe winkhouse.export.helpers.ColloquiHelper.getColloquiExist per individuare una lista di immobili da passare
	 * al metodo). 
	 * 
	 * @param criterioricerca
	 * @param colloquio
	 * @return ArrayList<CriteriRicercaModel>
	 */

	public ArrayList<CriteriRicercaModel> findCriteriRicercaByDescription(CriteriRicercaModel criterioricerca,ColloquiVO colloquio){
		
		ArrayList<CriteriRicercaModel> returnValue = new ArrayList<CriteriRicercaModel>();
		
		ColloquiCriteriRicercaDAO ccrDAO = new ColloquiCriteriRicercaDAO();
		ArrayList<CriteriRicercaModel> tmp = ccrDAO.getCriteriRicercaByCriteria(CriteriRicercaModel.class.getName(),
										   										criterioricerca.getGetterMethodName(), 
 																			    criterioricerca.getFromValue(),
																				criterioricerca.getToValue(), 
																				criterioricerca.getLogicalOperator());
		
		Iterator<CriteriRicercaModel> it = tmp.iterator();
		
		while (it.hasNext()) {
			
			CriteriRicercaModel criteriRicercaModel = (CriteriRicercaModel) it.next();
			
			if (criteriRicercaModel.getCodColloquio().intValue() == colloquio.getCodColloquio().intValue()){
				
				returnValue.add(criteriRicercaModel);
				
			}
			
		}
				
		return returnValue;
	}
	
	/**
	 * Ritorna una lista di oggetti ContattiVO, con propriet� contatto uguale a quella dell'oggetto, 
	 * di tipo ContattiVO passato in input.
	 * 
	 * @param contatto
	 * @return ArrayList<DatiCatastaliVO>
	 */

	public ArrayList<ContattiVO> findContattiByDescription(ContattiVO contatto){
		
		ArrayList<ContattiVO> returnValue = new ArrayList<ContattiVO>();
		
		ContattiDAO cDAO = new ContattiDAO();
		returnValue = cDAO.getContattoByContatto(ContattiVO.class.getName(), contatto.getContatto());
		
				
		return returnValue;
	}
	
	/**
	 * Ritorna una lista di oggetti TipologiaStanzeVO, con propriet� descrizione uguale a quella  
	 * dell'oggetto passato in input.
	 * 
	 * @param tipologiaStanze
	 * @return ArrayList<TipologiaStanzeVO>
	 */

	public ArrayList<TipologiaStanzeVO> findTipologieStanzeByDescription(TipologiaStanzeVO tipologiaStanze){
		
		ArrayList<TipologiaStanzeVO> returnValue = new ArrayList<TipologiaStanzeVO>();
		
		TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();
		returnValue = tsDAO.getTipologiaStanzaByDescrizione(TipologiaStanzeVO.class.getName(), 
															tipologiaStanze.getDescrizione());

		
		return returnValue;
	}

	/**
	 * Ritorna un oggetto ImmobiliPropietariVO, con propriet� descrizione uguale a quella  
	 * dell'oggetto passato in input.
	 * 
	 * @param immobiliPropietariVO
	 * @return <ImmobiliPropietariVO>
	 */

	public ImmobiliPropietariVO findImmobiliPropieta(ImmobiliPropietariVO immobiliPropietariVO){
		
		ImmobiliPropietariVO returnValue = new ImmobiliPropietariVO();
		
		ImmobiliPropietariDAO ipDAO = new ImmobiliPropietariDAO();
		returnValue = ipDAO.getImmobiliPropietariByCodImmobileCodAnagrafica(immobiliPropietariVO.getCodImmobile(), immobiliPropietariVO.getCodAnagrafica());

		
		return returnValue;
	}

	
	/**
	 * Ritorna la lista di tutte le tipologie di stanze presenti nella base dati. 
	 * 
	 * @return ArrayList<TipologiaStanzeVO>
	 */

	public ArrayList<TipologiaStanzeVO> getAllTipologieStanze(){
		
		ArrayList<TipologiaStanzeVO> returnValue = new ArrayList<TipologiaStanzeVO>();
		
		TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();
		returnValue = tsDAO.list(TipologiaStanzeVO.class.getName());

		return returnValue;
	}
	
	/**
	 * Ritorna una lista di oggetti TipologiaContattiVO, con propriet� descrizione uguale a quella  
	 * dell'oggetto passato in input.
	 * 
	 * @param tipologiaStanze
	 * @return ArrayList<TipologiaContattiVO>
	 */

	public ArrayList<TipologiaContattiVO> findTipologiaContattiByDescription(TipologiaContattiVO tipologiaContatti){
		
		ArrayList<TipologiaContattiVO> returnValue = new ArrayList<TipologiaContattiVO>();
		
		TipologiaContattiDAO tcDAO = new TipologiaContattiDAO();
		returnValue = tcDAO.getTipologiaContattoByDescrizione(TipologiaContattiVO.class.getName(), 
															  tipologiaContatti.getDescrizione());

		
		return returnValue;
	}

	/**
	 * Ritorna la lista di tutte le tipologie di contatti presenti nella base dati. 
	 * 
	 * @return ArrayList<TipologiaContattiVO>
	 */

	public ArrayList<TipologiaContattiVO> getAllTipologiaContatti(){
		
		ArrayList<TipologiaContattiVO> returnValue = new ArrayList<TipologiaContattiVO>();
		
		TipologiaContattiDAO tcDAO = new TipologiaContattiDAO();
		returnValue = tcDAO.list(TipologiaContattiVO.class.getName());
		
		return returnValue;
	}

	
	/**
	 * Ritorna il tipo di valore del metodo passato in input
	 * @param Class, classe, rappresenta il tipo di oggetto a cui appartiene il metodo 
	 * @param String, methodName, il nome del metodo di cui si vuole sapere il tipo del valore di ritorno
	 * @return Class, il tipo di valore ritornato dal metodo.
	 */
	public Class getReturnTypeGetterMethod(Class classe,String methodName){
		return WinkhouseUtils.getInstance()
							 .getReturnTypeGetterMethod(classe, methodName);
	}
	
	/**
	 * Ritorna la lista delle tipologie colloqui presenti in winkhouse
	 * @return ArrayList<TipologieColloquiVO> la lista delle tipologie colloqui
	 */
	public ArrayList<TipologieColloquiVO> getTipologieColloqui(){
		return EnvSettingsFactory.getInstance()
						         .getTipologieColloqui();
	}

	/**
	 * Inserisce un record nella tabella tipologieimmobili ritornando true o false.
	 * Se il campo codTipologieImmobili, del parametro tipologiaImmobile, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param tipologiaImmobile
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateTipologiaImmobile(TipologieImmobiliVO tipologiaImmobile) throws SQLException{		
		
		TipologieImmobiliDAO tiDAO = new TipologieImmobiliDAO();
		return tiDAO.saveUpdateWithException(tipologiaImmobile, null, true);
		
	}
	
	/**
	 * Inserisce un record nella tabella CLASSICLIENTE ritornando true o false.
	 * Se il campo codClasseCliente, del parametro classiClienti, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param classiClienti
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateClassiClienti(ClassiClientiVO classiClienti) throws SQLException{		
		
		ClassiClientiDAO ccDAO = new ClassiClientiDAO();
		return ccDAO.saveUpdateWithException(classiClienti, null, true);
		
	}

	/**
	 * Inserisce un record nella tabella riscaldamenti ritornando true o false.
	 * Se il campo codRiscaldamento, del parametro riscaldamento, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param riscaldamento
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateRiscaldamento(RiscaldamentiVO riscaldamento) throws SQLException{		
		
		RiscaldamentiDAO rDAO = new RiscaldamentiDAO();
		return rDAO.saveUpdateWithException(riscaldamento, null, true);
		
	}	

	/**
	 * Inserisce un record nella tabella statoconservativo ritornando true o false.
	 * Se il campo codStatoConservativo, del parametro statoConservativo, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param statoConservativo
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateStatoConservativo(StatoConservativoVO statoConservativo) throws SQLException{		
		
		StatoConservativoDAO scDAO = new StatoConservativoDAO();
		return scDAO.saveUpdateWithException(statoConservativo, null, true);
		
	}	

	/**
	 * Inserisce un record nella tabella statoconservativo ritornando true o false.
	 * Se il campo codClasseEnergetica : Integer, del parametro classeEnergetica, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param classeEnergetica
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateClasseEnergetica(ClasseEnergeticaVO classeEnergetica) throws SQLException{		
		
		ClassiEnergeticheDAO ceDAO = new ClassiEnergeticheDAO();
		return ceDAO.saveUpdateWithException(classeEnergetica, null, true);
		
	}	

	/**
	 * Inserisce un record nella tabella tipologiacontatti ritornando true o false.
	 * Se il campo codTipologiaContatto : Integer, del parametro tipologiaContatti, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param tipologiaContatti
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateTipologiaContatti(TipologiaContattiVO tipologiaContatti) throws SQLException{		
		
		TipologiaContattiDAO tcDAO = new TipologiaContattiDAO();
		return tcDAO.saveUpdateWithException(tipologiaContatti, null, true);
		
	}	
	
	/**
	 * Inserisce un record nella tabella tipologiastanze ritornando true o false.
	 * Se il campo codTipologiaStanza : Integer, del parametro tipologiaStanze, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param tipologiaStanze
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateTipologiaStanze(TipologiaStanzeVO tipologiaStanze) throws SQLException{		
		
		TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();
		return tsDAO.saveUpdateWithException(tipologiaStanze, null, true);
		
	}	

	/**
	 * Inserisce un record nella tabella agenti ritornando true o false.
	 * Se il campo codAgente : Integer, del parametro agente, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param agente
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateAgente(AgentiVO agente) throws SQLException{		
		
		AgentiDAO aDAO = new AgentiDAO();
		return aDAO.saveUpdateWithException(agente, null, true);
		
	}	

	/**
	 * Inserisce un record nella tabella contatti ritornando true o false.
	 * Se il campo codContatto : Integer, del parametro contatto, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param contatto
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateContatto(ContattiVO contatto) throws SQLException{		
		
		ContattiDAO cDAO = new ContattiDAO();
		return cDAO.saveUpdateWithException(contatto, null, true);
		
	}	
	
	public boolean copyFile(String pathOrigine, String pathDestinazione){
		return WinkhouseUtils.getInstance().copiaFile(pathOrigine, pathDestinazione);
	}
	
	public String getPathImmagini(){
		return WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.IMAGEPATH);		
	}

	public String getPathAllegati(){
		return WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.ALLEGATIPATH);		
	}

}