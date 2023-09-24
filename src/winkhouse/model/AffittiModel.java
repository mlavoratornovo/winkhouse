package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.AffittiAllegatiDAO;
import winkhouse.dao.AffittiAnagraficheDAO;
import winkhouse.dao.AffittiRateDAO;
import winkhouse.dao.AffittiSpeseDAO;
import winkhouse.dao.AgentiDAO;
import winkhouse.dao.EntityDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.util.IEntityAttribute;
import winkhouse.vo.AffittiAllegatiVO;
import winkhouse.vo.AffittiVO;


public class AffittiModel extends AffittiVO implements IEntityAttribute{

	private ImmobiliModel immobile = null;
	private ArrayList<AffittiAnagraficheModel> anagrafiche = null;
	private AgentiModel agenteInseritore = null;
	private ArrayList<AffittiAllegatiVO> allegati = null;
	private ArrayList<AffittiRateModel> rate = null;
	private ArrayList<AffittiSpeseModel> spese = null;
	private Integer savedAnagrafiche = 0;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private Integer numeroAnagrafiche = null;
	private Double rataMenoCauzione = null;
	private Double cauzioneMenoRata = null;
	private ArrayList stanzeimmobile = null;
	private EntityModel entity = null;
	
	public AffittiModel() {
		super();
	}

	public AffittiModel(AffittiVO affitti) {
		setCodAffitti(affitti.getCodAffitti());
		setCodImmobile(affitti.getCodImmobile());
		setCodAgenteIns(affitti.getCodAgenteIns());
		setDataInizio(affitti.getDataInizio());
		setDataFine(affitti.getDataFine());
		setCauzione(affitti.getCauzione());
		setRata(affitti.getRata());
		setDescrizione(affitti.getDescrizione());
	}

	public AffittiModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public ImmobiliModel getImmobile() {
		if (immobile == null){
			if (super.getCodImmobile() != null){
				ImmobiliDAO iDAO = new ImmobiliDAO();
				immobile = (ImmobiliModel)iDAO.getImmobileById(ImmobiliModel.class.getName(), 
															   super.getCodImmobile());
			}
		}
		return immobile;
	}

	public ArrayList<AffittiAnagraficheModel> getAnagrafiche() {
		if (anagrafiche == null){
			if (super.getCodAffitti() != null){
				AffittiAnagraficheDAO aaDAO = new AffittiAnagraficheDAO();
				anagrafiche = aaDAO.getAffittiAnagraficheByAffitto(AffittiAnagraficheModel.class.getName(), 
													 			   super.getCodAffitti());
			}
		}
		return anagrafiche;
	}

	public AgentiModel getAgenteInseritore() {
		if (agenteInseritore == null){
			if (super.getCodAgenteIns() != null){
				AgentiDAO aDAO = new AgentiDAO();
				Object o = aDAO.getAgenteById(AgentiModel.class.getName(), super.getCodAgenteIns());
				if (o != null){
					agenteInseritore = (AgentiModel)o;
				}
			}
		}
		return agenteInseritore;
	}

	public ArrayList<AffittiAllegatiVO> getAllegati() {
		if (allegati == null){
			if (super.getCodAffitti() != null){
				AffittiAllegatiDAO aaDAO = new AffittiAllegatiDAO();
				allegati = aaDAO.getAffittiAllegatiByCodAffitto(AffittiAllegatiVO.class.getName(), 
													 			super.getCodAffitti());
			}
		}
		return allegati;
	}

	@Override
	public String toString() {
		return "Immobile : " + ((getImmobile() == null)?"":getImmobile().toString()) + " - " +
			   "Data inizio : " + formatter.format(getDataInizio()) + " - " +
			   "Data fine : " + ((getDataFine() != null)?formatter.format(getDataFine()):"") + " - " +
			   "Rata : " + getRata();
	}

	public ArrayList<AffittiRateModel> getRate() {
		if (rate == null){
			if (super.getCodAffitti() != null){
				AffittiRateDAO arDAO = new AffittiRateDAO();
				rate = arDAO.getAffittiRateByCodAffitto(AffittiRateModel.class.getName(), 
													 	super.getCodAffitti());
			}
		}		
		return rate;
	}

	public ArrayList<AffittiSpeseModel> getSpese() {
		if (spese == null){
			if (super.getCodAffitti() != null){
				AffittiSpeseDAO asDAO = new AffittiSpeseDAO();
				spese = asDAO.getAffittiSpeseByCodAffitto(AffittiSpeseModel.class.getName(), 
													 	  super.getCodAffitti());
			}
		}				
		return spese;
	}

	public Integer getSavedAnagrafiche() {
		savedAnagrafiche = 0;
		Iterator<AffittiAnagraficheModel> it = getAnagrafiche().iterator();
		while (it.hasNext()){
			AffittiAnagraficheModel aam = it.next();
			if (aam.getAnagrafica().getCodAnagrafica() != null && aam.getAnagrafica().getCodAnagrafica() != 0){
				savedAnagrafiche++;
			}
		}
		return savedAnagrafiche;
	}

	public void setAgenteInseritore(AgentiModel agenteInseritore) {
		this.agenteInseritore = agenteInseritore;
		if (agenteInseritore != null){
			setCodAgenteIns(agenteInseritore.getCodAgente());
		}else{
			setCodAgenteIns(0);
		}
	}

	public Integer getNumeroAnagrafiche() {
		if (numeroAnagrafiche == null){
			numeroAnagrafiche = (getAnagrafiche() != null)
								 ? getAnagrafiche().size()
								 : 0;
		}
		return numeroAnagrafiche;
	}

	public Double getRataMenoCauzione() {
		if (rataMenoCauzione == null){
			rataMenoCauzione = getRata() - getCauzione();
		}
		return rataMenoCauzione;
	}

	public Double getCauzioneMenoRata() {
		if (cauzioneMenoRata == null){
			cauzioneMenoRata = getCauzione() - getRata();
		}
		return cauzioneMenoRata;
	}

	public ArrayList getStanzeimmobile() {
		if (stanzeimmobile == null){
			if (getImmobile() != null){
				stanzeimmobile = getImmobile().getStanze();
			}
		}
		return stanzeimmobile;
	}

	
	@Override
	public Integer getIdInstanceObject() {
		return getCodAffitti();
	}


	@Override
	public EntityModel getEntity() {
		if (entity == null){
			
			EntityDAO eDAO = new EntityDAO();
			entity = eDAO.getEntityByClassName(AffittiVO.class.getName());
			
		}
		return entity;
	}

	
	@Override
	public ArrayList<AttributeModel> getAttributes() {

		if (getEntity() != null){
			return getEntity().getAttributes();
			
		}
		return null;
	}

	
	public void setAnagrafiche(ArrayList<AffittiAnagraficheModel> anagrafiche) {
		this.anagrafiche = anagrafiche;
	}

	
	public void setRate(ArrayList<AffittiRateModel> rate) {
		this.rate = rate;
	}

	
	public void setSpese(ArrayList<AffittiSpeseModel> spese) {
		this.spese = spese;
	}

	
	public void setStanzeimmobile(ArrayList stanzeimmobile) {
		this.stanzeimmobile = stanzeimmobile;
	}

	public void setAllegati(ArrayList<AffittiAllegatiVO> allegati) {
		this.allegati = allegati;
	}

}
