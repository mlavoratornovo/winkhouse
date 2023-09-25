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
	private ArrayList<TipologieImmobiliVO> tipologieImmobili = null;
	private ArrayList<StatoConservativoVO> statiConservativi = null;
	private ArrayList<RiscaldamentiVO> riscaldamenti = null;
	private ArrayList<AgentiVO> agenti = null;
	
	private ArrayList<ClassiClientiVO> classiClienti = null;
	private ArrayList<TipologiaContattiVO> tipologieContatti = null;
	private ArrayList<TipologieColloquiVO> tipologieColloqui = null;
	private ArrayList<TipologiaStanzeVO> tipologieStanze = null;
	private ArrayList<TipiAppuntamentiVO> tipiAppuntamenti = null;
	private ArrayList<ClasseEnergeticaVO> classiEnergetiche = null;
	
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
	
	public ArrayList<ClasseEnergeticaVO> getClassiEnergetiche() {
		if (classiEnergetiche == null){
			ClassiEnergeticheDAO ceDAO = new ClassiEnergeticheDAO();
			classiEnergetiche = ceDAO.listClassiEnergetiche(ClasseEnergeticaVO.class.getName());
		}
		return classiEnergetiche;
	}

	public void setClassiEnergetiche(
			ArrayList<ClasseEnergeticaVO> classiEnergetiche) {
		this.classiEnergetiche = classiEnergetiche;
	}

	
	public ArrayList<TipologieImmobiliVO> getTipologieImmobili() {
		if (tipologieImmobili == null){
			TipologieImmobiliDAO tiDAO = new TipologieImmobiliDAO();
			tipologieImmobili = tiDAO.list(TipologieImmobiliVO.class.getName());
		}
		return tipologieImmobili;
	}

	public void setTipologieImmobili(
			ArrayList<TipologieImmobiliVO> tipologieImmobili) {
		this.tipologieImmobili = tipologieImmobili;
	}

	public ArrayList<StatoConservativoVO> getStatiConservativi() {
		if (statiConservativi == null){
			StatoConservativoDAO scDAO = new StatoConservativoDAO();
			statiConservativi = scDAO.list(StatoConservativoVO.class.getName());
		}
		return statiConservativi;
	}

	public void setStatiConservativi(
			ArrayList<StatoConservativoVO> statiConservativi) {
		this.statiConservativi = statiConservativi;
	}

	public ArrayList<RiscaldamentiVO> getRiscaldamenti() {
		if (riscaldamenti == null){
			RiscaldamentiDAO rDAO = new RiscaldamentiDAO();
			riscaldamenti = rDAO.list(RiscaldamentiVO.class.getName());
		}
		return riscaldamenti;
	}

	public void setRiscaldamenti(ArrayList<RiscaldamentiVO> riscaldamenti) {
		this.riscaldamenti = riscaldamenti;
	}

	public ArrayList<AgentiVO> getAgenti() {
		if (agenti == null){
			AgentiDAO aDAO = new AgentiDAO();
			agenti = aDAO.list(AgentiVO.class.getName());
		}
		return agenti;
	}

	public void setAgenti(ArrayList<AgentiVO> agenti) {
		this.agenti = agenti;
	}

	public ArrayList<ClassiClientiVO> getClassiClienti() {
		if(classiClienti == null){
			ClassiClientiDAO ccDAO = new ClassiClientiDAO();
			classiClienti = ccDAO.list(ClassiClientiVO.class.getName());
		}
		return classiClienti;
	}

	public void setClassiClienti(ArrayList<ClassiClientiVO> classiClienti) {
		this.classiClienti = classiClienti;
	}

	public ArrayList<TipologiaContattiVO> getTipologieContatti() {
		if (tipologieContatti == null){
			TipologiaContattiDAO tcDAO = new TipologiaContattiDAO();
			tipologieContatti = tcDAO.list(TipologiaContattiVO.class.getName());
		}
		return tipologieContatti;
	}

	public void setTipologieContatti(
			ArrayList<TipologiaContattiVO> tipologieContatti) {
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

	public ArrayList<TipologiaStanzeVO> getTipologieStanze() {
		if (tipologieStanze == null){
			TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();
			tipologieStanze = tsDAO.list(TipologiaStanzeVO.class.getName());
		}
		return tipologieStanze;
	}

	public void setTipologieStanze(ArrayList<TipologiaStanzeVO> tipologieStanze) {
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

	public TipologieImmobiliVO findTipologiaByCod(Integer codice){
		ArrayList<TipologieImmobiliVO> work = (ArrayList<TipologieImmobiliVO>)getTipologieImmobili().clone();
		Collections.sort(work, comparatorCodTipologieImmobili);
		TipologieImmobiliVO tiVO = new TipologieImmobiliVO();
		tiVO.setCodTipologiaImmobile(codice);
		int index = Collections.binarySearch(work, tiVO, comparatorCodTipologieImmobili);
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

	
	public ArrayList<TipiAppuntamentiVO> getTipiAppuntamenti() {
		if (tipiAppuntamenti == null){
			TipiAppuntamentiDAO taDAO = new TipiAppuntamentiDAO();
			tipiAppuntamenti = taDAO.listTipiAppuntamenti(TipiAppuntamentiVO.class.getName());
		}
		
		return tipiAppuntamenti;
	}

	public void setTipiAppuntamenti(ArrayList<TipiAppuntamentiVO> tipiAppuntamenti) {
		this.tipiAppuntamenti = tipiAppuntamenti;
	}
		
}