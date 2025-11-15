package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AllegatiColloquiDAO;
import winkhouse.dao.ColloquiAgentiDAO;
import winkhouse.dao.ColloquiAnagraficheDAO;
import winkhouse.dao.ColloquiCriteriRicercaDAO;
import winkhouse.dao.EntityDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.WinkGCalendarDAO;
import winkhouse.orm.Agenti;
import winkhouse.orm.Tipologiecolloqui;
import winkhouse.util.IEntityAttribute;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.TipologieColloquiVO;


public class ColloquiModel extends ColloquiVO implements IEntityAttribute{

	private Agenti agenteInseritore = null;
	private ImmobiliModel immobileAbbinato = null;
	private Tipologiecolloqui tipologia = null;
	private ArrayList allegati = null;
	private ArrayList<ColloquiAgentiModel_Age> agenti = null;
	private ArrayList<ColloquiAnagraficheModel_Ang> anagrafiche = null;
	private ArrayList criteriRicerca = null;	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
	private EntityModel entity = null;
	private ArrayList<WinkGCalendarModel> winkGCalendarModels = null;
	
	public ColloquiModel() {
		super();
	}

	public ColloquiModel(ColloquiVO cVO) {
		setCodAgenteInseritore(cVO.getCodAgenteInseritore());
		setCodColloquio(cVO.getCodColloquio());
		setCodImmobileAbbinato(cVO.getCodImmobileAbbinato());
		setCodParent(cVO.getCodParent());
		setCodTipologiaColloquio(cVO.getCodTipologiaColloquio());
		setCommentoAgenzia(cVO.getCommentoAgenzia());
		setCommentoCliente(cVO.getCommentoCliente());
		setDataColloquio(cVO.getDataColloquio());
		setDataInserimento(cVO.getDataInserimento());
		setDescrizione(cVO.getDescrizione());
		setiCalUid(cVO.getiCalUid());
		setLuogoIncontro(cVO.getLuogoIncontro());
		setScadenziere(cVO.getScadenziere());		
	}

	public ColloquiModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public Agenti getAgenteInseritore() {
		if (agenteInseritore == null){
			if ((super.getCodAgenteInseritore() != null) &&
				(super.getCodAgenteInseritore() != 0)){
				AgentiDAO agentiDAO = new AgentiDAO();
				Object o = agentiDAO.getAgenteById(AgentiVO.class.getName(), super.getCodAgenteInseritore());
				if (o != null){
					agenteInseritore = (Agenti)o;
				}	
			}	
		}
		return agenteInseritore;
	}
	
	public String getNomeCognomeAgenteInseritore(){
		if (getAgenteInseritore() != null){
			return getAgenteInseritore().getNome() + " " + getAgenteInseritore().getCognome();
		}else{
			return "";
		}
	}

	public void setAgenteInseritore(Agenti agenteInseritore) {
		this.agenteInseritore = agenteInseritore;
		if (agenteInseritore != null){
			super.setCodAgenteInseritore(agenteInseritore.getCodAgente());
		}else{
			super.setCodAgenteInseritore(0);
		}
	}

	public ImmobiliModel getImmobileAbbinato() {
		if (immobileAbbinato == null){
			if ((super.getCodImmobileAbbinato() != null) &&
				(super.getCodImmobileAbbinato() != 0)){
				ImmobiliDAO immobiliDAO = new ImmobiliDAO();
				Object o = immobiliDAO.getImmobileById(ImmobiliModel.class.getName(), super.getCodImmobileAbbinato());
				if (o != null){
					immobileAbbinato = (ImmobiliModel)o;
				}	
			}	
		}
		
		return immobileAbbinato;
	}

	public void setImmobileAbbinato(ImmobiliModel immobileAbbinato) {		
		this.immobileAbbinato = immobileAbbinato;
		if (this.immobileAbbinato == null){
			super.setCodImmobileAbbinato(null);
		}else{
			super.setCodImmobileAbbinato(immobileAbbinato.getCodImmobile());
		}
	}

	public Tipologiecolloqui getTipologia() {
		if (tipologia == null){
			if ((super.getCodTipologiaColloquio() != null) &&
				(super.getCodTipologiaColloquio() != 0)){
				ArrayList<Tipologiecolloqui> al = MobiliaDatiBaseCache.getInstance().getTipologieColloqui();
			    Iterator<Tipologiecolloqui> it = al.iterator();
			    while(it.hasNext()){
			    	Tipologiecolloqui tcVO = it.next();
					if (tcVO.getCodTipologieColloquio() == super.getCodTipologiaColloquio()){
						tipologia = tcVO;
						break;
					}
				}
			}	
		}
		return tipologia;
	}

	public void setTipologia(Tipologiecolloqui tipologia) {
		
		this.tipologia = tipologia;
		if (tipologia != null){
			super.setCodTipologiaColloquio(tipologia.getCodTipologieColloquio());
		}else{
			super.setCodTipologiaColloquio(0);
		}
	}

	public ArrayList getAllegati() {
		if (allegati == null){
			if (super.getCodColloquio() != null){
				AllegatiColloquiDAO allegatiColloquiDAO = new AllegatiColloquiDAO();				
				allegati = allegatiColloquiDAO.getAllegatiByColloquio(AllegatiColloquiVO.class.getName(), super.getCodColloquio());
			}else{
				allegati = new ArrayList();
			}
		}
		return allegati;
	}

	public void setAllegati(ArrayList allegati) {
		this.allegati = allegati;
	}

	public ArrayList<ColloquiAgentiModel_Age> getAgenti() {
		if (agenti == null){
			if (super.getCodColloquio() != null){
				ColloquiAgentiDAO colloquiAgentiDAO = new ColloquiAgentiDAO();				
				agenti = colloquiAgentiDAO.getColloquiAgentiByColloquio(ColloquiAgentiModel_Age.class.getName(), super.getCodColloquio());
			}else{
				agenti = new ArrayList<ColloquiAgentiModel_Age>();
			}
		}
		return agenti;
	}

	public void setAgenti(ArrayList agenti) {
		this.agenti = agenti;
	}

	public ArrayList<ColloquiAnagraficheModel_Ang> getAnagrafiche() {
		if (anagrafiche == null){
			if (super.getCodColloquio() != null){
				ColloquiAnagraficheDAO colloquiAnagraficheDAO = new ColloquiAnagraficheDAO();				
				anagrafiche = colloquiAnagraficheDAO.getColloquiAnagraficheByColloquio(ColloquiAnagraficheModel_Ang.class.getName(), 
																					   super.getCodColloquio());
			}else{
				anagrafiche = new ArrayList<ColloquiAnagraficheModel_Ang>();
			}
		}
		
		return anagrafiche;
	}

	public void setAnagrafiche(ArrayList anagrafiche) {
		this.anagrafiche = anagrafiche;
	}

	public ArrayList getCriteriRicerca() {
		
		if (this.criteriRicerca == null){
			
			if(getCodColloquio() != null && getCodColloquio() != 0){
				
				ColloquiCriteriRicercaDAO colloquiCriteriRicercaDAO = new ColloquiCriteriRicercaDAO();				
				criteriRicerca = colloquiCriteriRicercaDAO.getColloquiCriteriRicercaByColloquio(ColloquiCriteriRicercaModel.class.getName(), super.getCodColloquio());
				Iterator it = this.criteriRicerca.iterator();
				
				while (it.hasNext()) {
					ColloquiCriteriRicercaModel ccrm = (ColloquiCriteriRicercaModel) it.next();
					ccrm.setTypeSearch(RicercheModel.RICERCHE_IMMOBILI);
					
				}
				
			}else{
				this.criteriRicerca = new ArrayList();
			}
		}
		
		return this.criteriRicerca;
	}

	public void setCriteriRicerca(ArrayList criteriRicerca) {
		this.criteriRicerca = criteriRicerca;
	}
/*
	public String getControlDescription() {
		if (controlDescription == null){
			controlDescription = this.getCodColloquio() + " - " + 
								 formatter.format(super.getDataInserimento()) + " " + 
								 formatterTime.format(super.getDataInserimento());
		}
		return controlDescription;
	}
*/
	@Override
	public String toString() {		
		return "Codice : " + this.getCodColloquio() + " - " + 
			   "Data colloquio : " + formatter.format(this.getDataColloquio()) + " " + formatterTime.format(this.getDataColloquio()) + " - " + 
			   "Tipologia : " + (this.getTipologia() == null? "":this.getTipologia().getDescrizione());
	}
	
	public String getAnagraficheDescription(){
		
		String returnValue = "";
		ArrayList<ColloquiAnagraficheModel_Ang> anag = getAnagrafiche();
		if (anag != null){
			Iterator<ColloquiAnagraficheModel_Ang> it = anag.iterator();
			while(it.hasNext()){
				AnagraficheModel am = it.next().getAnagrafica();				
				returnValue += (am != null)?am.toString() + " ":"";
			}			
		}

		return returnValue;
	}
	
	public String getAgentiDescription(){
		
		String returnValue = "";
		ArrayList<ColloquiAgentiModel_Age> ag = getAgenti();
		if (ag != null){
			Iterator<ColloquiAgentiModel_Age> it = ag.iterator();
			while(it.hasNext()){
				AgentiVO aVO = it.next().getAgente();
				returnValue += (aVO != null)?aVO.toString() + " ":"";
			}			
		}

		return returnValue;
	}
	
	public void resetCache(){
		setAgenti(null);
		setAllegati(null);
		setAnagrafiche(null);
		setCriteriRicerca(null);
		immobileAbbinato = null;
		tipologia = null;
		agenteInseritore = null;
	}

	public EntityModel getEntity() {
		
		if (entity == null){
			
//			EntityDAO eDAO = new EntityDAO();
//			entity = eDAO.getEntityByClassName(ColloquiVO.class.getName());
			
		}
		return entity;
	}

	public void setEntity(EntityModel entity) {
		this.entity = entity;
	}

	@Override
	public Integer getIdInstanceObject() {
		return getCodColloquio();
	}

	public ArrayList<AttributeModel> getAttributes() {
		
		if (getEntity() != null){
			return getEntity().getAttributes();
			
		}
		return null;
	}

	public ArrayList<WinkGCalendarModel> getWinkGCalendarModels() {
		if (winkGCalendarModels == null){
			if (super.getCodColloquio() != null){
				WinkGCalendarDAO wgcDAO = new WinkGCalendarDAO();
				winkGCalendarModels = wgcDAO.getWinkGCalendarByCodColloquio(getCodColloquio());
			}else{
				winkGCalendarModels = new ArrayList();
			}
				
		}
		return winkGCalendarModels;
	}

	
	public void setWinkGCalendarModels(
			ArrayList<WinkGCalendarModel> winkGCalendarModels) {
		this.winkGCalendarModels = winkGCalendarModels;
	}

}
