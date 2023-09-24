package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.vo.AbbinamentiVO;


public class AbbinamentiModel extends AbbinamentiVO {

	private ImmobiliModel immobile = null;
	private AnagraficheModel anagrafica = null;
	
	private String rifImmobile = null;
	private String cittaImmobile = null;
	private String indirizzoImmobile = null;
	private String nomeCognomePropietarioImmobile = null;

	private String nomeCognomeAnagrafica = null;
	private String ragioneSocialeAnagrafica = null;
	private String indirizzoAnagrafica = null;
	private String cittaAnagrafica = null;

	public AbbinamentiModel() {
	
	}

	public AbbinamentiModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public ImmobiliModel getImmobile() {
		if (immobile == null){
			ImmobiliDAO iDAO = new ImmobiliDAO();
			try {
				immobile = (ImmobiliModel)iDAO.getImmobileById(ImmobiliModel.class.getName(), super.getCodImmobile());
			} catch (Exception e) {
				immobile = null;
			}
		}
		return immobile;
	}

	public void setImmobile(ImmobiliModel immobile) {
		super.setCodImmobile(immobile.getCodImmobile());
		this.immobile = immobile;
	}

	public AnagraficheModel getAnagrafica() {
		if (anagrafica == null){
			AnagraficheDAO aDAO = new AnagraficheDAO();
			try {
				anagrafica = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), super.getCodAnagrafica());
			} catch (Exception e) {
				anagrafica = null;
			}
		}
		return anagrafica;
	}

	public void setAnagrafica(AnagraficheModel anagrafica) {
		super.setCodAnagrafica(anagrafica.getCodAnagrafica());
		this.anagrafica = anagrafica;
	}

	public String getRifImmobile() {
		if (rifImmobile == null){
			if (getImmobile() != null){
				rifImmobile = getImmobile().getRif();
			}
		}
		return rifImmobile;
	}

	public String getCittaImmobile() {
		if (cittaImmobile == null){
			if (getImmobile() != null){
				cittaImmobile = getImmobile().getCitta();
			}
		}
		return cittaImmobile;
	}

	public String getIndirizzoImmobile() {
		if (indirizzoImmobile == null){
			if (getImmobile() != null){
				indirizzoImmobile = getImmobile().getIndirizzo();
			}
		}
		return indirizzoImmobile;
	}

	public String getNomeCognomePropietarioImmobile() {
		if (nomeCognomePropietarioImmobile == null){
			if (getImmobile() != null){
				nomeCognomePropietarioImmobile = getImmobile().getNomeCognomeAnagrafica();
			}
		}
		return nomeCognomePropietarioImmobile;
	}

	public String getNomeCognomeAnagrafica() {
		if (nomeCognomeAnagrafica == null){
			if (getAnagrafica() != null){
				nomeCognomeAnagrafica = getAnagrafica().getNome() + " " + 
										getAnagrafica().getCognome();
			}
		}
		return nomeCognomeAnagrafica;
	}

	public String getRagioneSocialeAnagrafica() {
		if (ragioneSocialeAnagrafica == null){
			if (getAnagrafica() != null){
				ragioneSocialeAnagrafica = getAnagrafica().getRagioneSociale(); 
										  
			}
		}		
		return ragioneSocialeAnagrafica;
	}

	public String getIndirizzoAnagrafica() {
		if (indirizzoAnagrafica == null){
			if (getAnagrafica() != null){
				indirizzoAnagrafica = getAnagrafica().getIndirizzo(); 
										  
			}
		}				
		return indirizzoAnagrafica;
	}

	public String getCittaAnagrafica() {
		if (cittaAnagrafica == null){
			if (getAnagrafica() != null){
				cittaAnagrafica = getAnagrafica().getCitta(); 
										  
			}
		}						
		return cittaAnagrafica;
	}

}
