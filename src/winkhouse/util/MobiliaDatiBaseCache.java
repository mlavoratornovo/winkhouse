package winkhouse.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.dao.AgentiDAO;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.dao.StatoConservativoDAO;
import winkhouse.dao.TipiAppuntamentiDAO;
import winkhouse.dao.TipologiaContattiDAO;
import winkhouse.dao.TipologiaStanzeDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.orm.Agenti;
import winkhouse.orm.Classicliente;
import winkhouse.orm.Classienergetiche;
import winkhouse.orm.Riscaldamenti;
import winkhouse.orm.Statoconservativo;
import winkhouse.orm.Tipiappuntamenti;
import winkhouse.orm.Tipologiastanze;
import winkhouse.orm.Tipologiecontatti;
import winkhouse.orm.Tipologieimmobili;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipiAppuntamentiVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.vo.TipologieColloquiVO;
import winkhouse.vo.TipologieImmobiliVO;


public class MobiliaDatiBaseCache {

	private static MobiliaDatiBaseCache instance = null;
	private ArrayList<Tipologieimmobili> tipologieImmobili = null;
	private ArrayList<Statoconservativo> statiConservativi = null;
	private ArrayList<Riscaldamenti> riscaldamenti = null;
	private ArrayList<Agenti> agenti = null;
	
	private ArrayList<Classicliente> classiClienti = null;
	private ArrayList<Tipologiecontatti> tipologieContatti = null;
	private ArrayList<TipologieColloquiVO> tipologieColloqui = null;
	private ArrayList<Tipologiastanze> tipologieStanze = null;
	private ArrayList<Tipiappuntamenti> tipiAppuntamenti = null;
	private ArrayList<Classienergetiche> classiEnergetiche = null;
	
	private MobiliaDatiBaseCache(){}
	
	public static MobiliaDatiBaseCache getInstance(){
		if (instance == null){
			instance = new MobiliaDatiBaseCache();			
		}
		return instance;
	}
	
	public Comparator<TipologieImmobiliVO> comparatorTipologieImmobili = new Comparator<TipologieImmobiliVO>(){

		@Override
		public int compare(TipologieImmobiliVO arg0,
						   TipologieImmobiliVO arg1) {
			return arg0.getDescrizione().compareToIgnoreCase(arg0.getDescrizione());			
		}
		
	};

	public Comparator<StatoConservativoVO> comparatorStatoConservativo = new Comparator<StatoConservativoVO>(){

		@Override
		public int compare(StatoConservativoVO arg0,
						   StatoConservativoVO arg1) {
			return arg0.getDescrizione().compareToIgnoreCase(arg0.getDescrizione());			
		}
		
	};

	public Comparator<RiscaldamentiVO> comparatorRiscaldamenti = new Comparator<RiscaldamentiVO>(){

		@Override
		public int compare(RiscaldamentiVO arg0,
						   RiscaldamentiVO arg1) {
			return arg0.getDescrizione().compareToIgnoreCase(arg0.getDescrizione());			
		}
		
	};

	public Comparator<TipologieImmobiliVO> comparatorCodTipologieImmobili = new Comparator<TipologieImmobiliVO>(){

		@Override
		public int compare(TipologieImmobiliVO arg0,
						   TipologieImmobiliVO arg1) {
			if (arg0.getCodTipologiaImmobile().intValue() == arg1.getCodTipologiaImmobile().intValue()){
				return 0;
			}else if (arg0.getCodTipologiaImmobile().intValue() < arg1.getCodTipologiaImmobile().intValue()){
				return -1;
			}else{
				return 1;
			}			
		}
		
	};

	public Comparator<StatoConservativoVO> comparatorCodStatoConservativo = new Comparator<StatoConservativoVO>(){

		@Override
		public int compare(StatoConservativoVO arg0,
						   StatoConservativoVO arg1) {
			if (arg0.getCodStatoConservativo().intValue() == arg1.getCodStatoConservativo().intValue()){
				return 0;
			}else if (arg0.getCodStatoConservativo().intValue() < arg1.getCodStatoConservativo().intValue()){
				return -1;
			}else{
				return 1;
			}			
			
		}
		
	};

	public Comparator<RiscaldamentiVO> comparatorCodRiscaldamenti = new Comparator<RiscaldamentiVO>(){

		@Override
		public int compare(RiscaldamentiVO arg0,
						   RiscaldamentiVO arg1) {
			if (arg0.getCodRiscaldamento().intValue() == arg1.getCodRiscaldamento().intValue()){
				return 0;
			}else if (arg0.getCodRiscaldamento().intValue() < arg1.getCodRiscaldamento().intValue()){
				return -1;
			}else{
				return 1;
			}			
		}
		
	};	

	public Comparator<AgentiVO> comparatorCodAgenti = new Comparator<AgentiVO>(){

		@Override
		public int compare(AgentiVO arg0,
						   AgentiVO arg1) {
			if (arg0.getCodAgente().intValue() == arg1.getCodAgente().intValue()){
				return 0;
			}else if (arg0.getCodAgente().intValue() < arg1.getCodAgente().intValue()){
				return -1;
			}else{
				return 1;
			}			
		}
		
	};	
	
	public Comparator<ClassiClientiVO> comparatorCodClassiClienti = new Comparator<ClassiClientiVO>(){

		@Override
		public int compare(ClassiClientiVO arg0,
						   ClassiClientiVO arg1) {
			if (arg0.getCodClasseCliente().intValue() == arg1.getCodClasseCliente().intValue()){
				return 0;
			}else if (arg0.getCodClasseCliente().intValue() < arg1.getCodClasseCliente().intValue()){
				return -1;
			}else{
				return 1;
			}			
		}
		
	};	
	
	public Comparator<TipiAppuntamentiVO> comparatorCodTipiAppuntamenti = new Comparator<TipiAppuntamentiVO>(){

		@Override
		public int compare(TipiAppuntamentiVO arg0,
						   TipiAppuntamentiVO arg1) {
			if (arg0.getCodTipoAppuntamento().intValue() == arg1.getCodTipoAppuntamento().intValue()){
				return 0;
			}else if (arg0.getCodTipoAppuntamento().intValue() < arg1.getCodTipoAppuntamento().intValue()){
				return -1;
			}else{
				return 1;
			}			
		}
		
	};	

	public Comparator<ClasseEnergeticaVO> comparatorClassiEnergetiche = new Comparator<ClasseEnergeticaVO>(){

		@Override
		public int compare(ClasseEnergeticaVO arg0,
						   ClasseEnergeticaVO arg1) {
			if (arg0.getCodClasseEnergetica().intValue() == arg1.getCodClasseEnergetica().intValue()){
				return 0;
			}else if (arg0.getCodClasseEnergetica().intValue() < arg1.getCodClasseEnergetica().intValue()){
				return -1;
			}else{
				return 1;
			}			
		}
		
	};
	
	public Comparator<ClasseEnergeticaVO> comparatorCodClasseEnergetica = new Comparator<ClasseEnergeticaVO>(){

		@Override
		public int compare(ClasseEnergeticaVO arg0,
						   ClasseEnergeticaVO arg1) {
			if (arg0.getCodClasseEnergetica().intValue() == arg1.getCodClasseEnergetica().intValue()){
				return 0;
			}else if (arg0.getCodClasseEnergetica().intValue() < arg1.getCodClasseEnergetica().intValue()){
				return -1;
			}else{
				return 1;
			}			
		}
		
	};	
	
	public ArrayList<Classienergetiche> getClassiEnergetiche() {
		if (classiEnergetiche == null){
			ClassiEnergeticheDAO ceDAO = new ClassiEnergeticheDAO();
			classiEnergetiche = ceDAO.listClassiEnergetiche();
		}
		return classiEnergetiche;
	}

	public void setClassiEnergetiche(
			ArrayList<Classienergetiche> classiEnergetiche) {
		this.classiEnergetiche = classiEnergetiche;
	}

	
	public ArrayList<Tipologieimmobili> getTipologieImmobili() {
		if (tipologieImmobili == null){
			TipologieImmobiliDAO tiDAO = new TipologieImmobiliDAO();
			tipologieImmobili = tiDAO.list();
		}
		return tipologieImmobili;
	}

	public void setTipologieImmobili(
			ArrayList<Tipologieimmobili> tipologieImmobili) {
		this.tipologieImmobili = tipologieImmobili;
	}

	public ArrayList<Statoconservativo> getStatiConservativi() {
		if (statiConservativi == null){
			StatoConservativoDAO scDAO = new StatoConservativoDAO();
			statiConservativi = scDAO.list();
		}
		return statiConservativi;
	}

	public void setStatiConservativi(
			ArrayList<Statoconservativo> statiConservativi) {
		this.statiConservativi = statiConservativi;
	}

	public ArrayList<Riscaldamenti> getRiscaldamenti() {
		if (riscaldamenti == null){
			RiscaldamentiDAO rDAO = new RiscaldamentiDAO();
			riscaldamenti = rDAO.list();
		}
		return riscaldamenti;
	}

	public void setRiscaldamenti(ArrayList<Riscaldamenti> riscaldamenti) {
		this.riscaldamenti = riscaldamenti;
	}

	public ArrayList<Agenti> getAgenti() {
		if (agenti == null){
			AgentiDAO aDAO = new AgentiDAO();
			agenti = aDAO.list(WinkhouseUtils.getInstance().getCayenneObjectContext());
		}
		return agenti;
	}

	public void setAgenti(ArrayList<Agenti> agenti) {
		this.agenti = agenti;
	}

	public ArrayList<Classicliente> getClassiClienti() {
		if(classiClienti == null){
			ClassiClientiDAO ccDAO = new ClassiClientiDAO();
			classiClienti = ccDAO.list(null);
		}
		return classiClienti;
	}

	public void setClassiClienti(ArrayList<Classicliente> classiClienti) {
		this.classiClienti = classiClienti;
	}

	public ArrayList<Tipologiecontatti> getTipologieContatti() {
		if (tipologieContatti == null){
			TipologiaContattiDAO tcDAO = new TipologiaContattiDAO();
			tipologieContatti = tcDAO.list();
		}
		return tipologieContatti;
	}

	public void setTipologieContatti(
			ArrayList<Tipologiecontatti> tipologieContatti) {
		this.tipologieContatti = tipologieContatti;
	}

	public ArrayList<TipologieColloquiVO> getTipologieColloqui() {
		if (tipologieColloqui == null){
			tipologieColloqui = EnvSettingsFactory.getInstance().getTipologieColloqui();
		}
		return tipologieColloqui;
	}

	public ArrayList<TipologieColloquiVO> getTipologieColloquiWithoutRicerca() {
		ArrayList<TipologieColloquiVO> tipologieColloquiWithoutRicerca = (ArrayList<TipologieColloquiVO>)EnvSettingsFactory.getInstance()
																														   .getTipologieColloqui()
																														   .clone();
		tipologieColloquiWithoutRicerca.remove(0);
		
		
		return tipologieColloquiWithoutRicerca;
	}

	public void setTipologieColloqui(
			ArrayList<TipologieColloquiVO> tipologieColloqui) {
		this.tipologieColloqui = tipologieColloqui;
	}

	public ArrayList<Tipologiastanze> getTipologieStanze() {
		if (tipologieStanze == null){
			TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();
			tipologieStanze = tsDAO.list();
		}
		return tipologieStanze;
	}

	public void setTipologieStanze(ArrayList<Tipologiastanze> tipologieStanze) {
		this.tipologieStanze = tipologieStanze;
	}
	
	public TipologieImmobiliVO findTipologiaByDescrizione(String descrizione){
		ArrayList<TipologieImmobiliVO> work = (ArrayList<TipologieImmobiliVO>)getTipologieImmobili().clone();
		Collections.sort(work, comparatorTipologieImmobili);
		TipologieImmobiliVO tiVO = new TipologieImmobiliVO();
		tiVO.setDescrizione(descrizione);
		int index = Collections.binarySearch(work, tiVO, comparatorTipologieImmobili);
		if (index > -1){
			return work.get(index);
		}else{
			return null;
		}
	}
	
	public StatoConservativoVO findStatoConservativoByDescrizione(String descrizione){
		ArrayList<StatoConservativoVO> work = (ArrayList<StatoConservativoVO>)getStatiConservativi().clone();
		Collections.sort(work, comparatorStatoConservativo);
		StatoConservativoVO scVO = new StatoConservativoVO();
		scVO.setDescrizione(descrizione);
		int index = Collections.binarySearch(work, scVO, comparatorStatoConservativo);
		if (index > -1){
			return work.get(index);
		}else{
			return null;
		}
	}

	public RiscaldamentiVO findRiscaldamentiByDescrizione(String descrizione){
		ArrayList<RiscaldamentiVO> work = (ArrayList<RiscaldamentiVO>)getRiscaldamenti().clone();
		Collections.sort(work, comparatorRiscaldamenti);
		RiscaldamentiVO rVO = new RiscaldamentiVO();
		rVO.setDescrizione(descrizione);
		int index = Collections.binarySearch(work, rVO, comparatorRiscaldamenti);
		if (index > -1){
			return work.get(index);
		}else{
			return null;
		}
	}
	
	public StatoConservativoVO findStatoConservativoByCod(Integer codice){
		ArrayList<StatoConservativoVO> work = (ArrayList<StatoConservativoVO>)getStatiConservativi().clone();
		Collections.sort(work, comparatorCodStatoConservativo);
		StatoConservativoVO scVO = new StatoConservativoVO();
		scVO.setCodStatoConservativo(codice);
		int index = Collections.binarySearch(work, scVO, comparatorCodStatoConservativo);
		if (index > -1){
			return work.get(index);
		}else{
			return null;
		}
	}

	public RiscaldamentiVO findRiscaldamentiByCod(Integer codice){
		ArrayList<RiscaldamentiVO> work = (ArrayList<RiscaldamentiVO>)getRiscaldamenti().clone();
		Collections.sort(work, comparatorCodRiscaldamenti);
		RiscaldamentiVO rVO = new RiscaldamentiVO();
		rVO.setCodRiscaldamento(codice);
		int index = Collections.binarySearch(work, rVO, comparatorCodRiscaldamenti);
		if (index > -1){
			return work.get(index);
		}else{
			return null;
		}
	}

	public ClassiClientiVO findClassiClientiByCod(Integer codice){
		ArrayList<ClassiClientiVO> work = (ArrayList<ClassiClientiVO>)getClassiClienti().clone();
		Collections.sort(work, comparatorCodClassiClienti);
		ClassiClientiVO ccVO = new ClassiClientiVO();
		ccVO.setCodClasseCliente(codice);
		int index = Collections.binarySearch(work, ccVO, comparatorCodClassiClienti);
		if (index > -1){
			return work.get(index);
		}else{
			return null;
		}
	}
	
	public AgentiVO findAgentiByCod(Integer codice){
		ArrayList<AgentiVO> work = (ArrayList<AgentiVO>)getAgenti().clone();
		Collections.sort(work, comparatorCodAgenti);
		AgentiVO aVO = new AgentiVO();
		aVO.setCodAgente(codice);
		int index = Collections.binarySearch(work, aVO, comparatorCodAgenti);
		if (index > -1){
			return work.get(index);
		}else{
			return null;
		}
	}

	public ClasseEnergeticaVO findClasseEnergeticaByCod(Integer codice){
		ArrayList<ClasseEnergeticaVO> work = (ArrayList<ClasseEnergeticaVO>)getClassiEnergetiche().clone();
		Collections.sort(work, comparatorClassiEnergetiche);
		ClasseEnergeticaVO ceVO = new ClasseEnergeticaVO();
		ceVO.setCodClasseEnergetica(codice);
		int index = Collections.binarySearch(work, ceVO, comparatorClassiEnergetiche);
		if (index > -1){
			return work.get(index);
		}else{
			return null;
		}
	}
	
	public TipiAppuntamentiVO findTipiAppuntamentiByCod(Integer codice){
		ArrayList<TipiAppuntamentiVO> work = (ArrayList<TipiAppuntamentiVO>)getTipiAppuntamenti().clone();
		Collections.sort(work, comparatorCodTipiAppuntamenti);
		TipiAppuntamentiVO taVO = new TipiAppuntamentiVO();
		taVO.setCodTipoAppuntamento(codice);
		int index = Collections.binarySearch(work, taVO, comparatorCodTipiAppuntamenti);
		if (index > -1){
			return work.get(index);
		}else{
			return null;
		}
	}

	
	public ArrayList<Tipiappuntamenti> getTipiAppuntamenti() {
		if (tipiAppuntamenti == null){
			TipiAppuntamentiDAO taDAO = new TipiAppuntamentiDAO();
			tipiAppuntamenti = taDAO.listTipiAppuntamenti(Tipiappuntamenti.class.getName());
		}
		
		return tipiAppuntamenti;
	}

	public void setTipiAppuntamenti(ArrayList<Tipiappuntamenti> tipiAppuntamenti) {
		this.tipiAppuntamenti = tipiAppuntamenti;
	}
		
}