package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.vo.AffittiAnagraficheVO;


public class AffittiAnagraficheModel extends AffittiAnagraficheVO {

	private AnagraficheModel anagrafica = null;
	private String nomeCognomeAnagrafica = null;
	private String indirizzoAnagrafica = null;
	private String cittaAnagrafica = null;
	private String codicefiscaleAnagrafica = null;
	private String primoRecapitoAnagrafica = null;
	private String secondoRecapitoAnagrafica = null;
	private String capAnagrafica = null;
	private String provinciaAnagrafica = null;	
	private String ragioneSocialeAnagrafica = null;
	private String pivaAnagrafica = null;	
	
	
	public AffittiAnagraficheModel() {
		super();
	}

	public AffittiAnagraficheModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public AnagraficheModel getAnagrafica() {
		if (anagrafica == null){
			AnagraficheDAO aDAO = new AnagraficheDAO();
			anagrafica = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(),
																   super.getCodAnagrafica());
		}
		return anagrafica;
	}

	@Override
	public String toString() {
		return getAnagrafica().toString();
	}

	public void setAnagrafica(AnagraficheModel anagrafica) {
		this.anagrafica = anagrafica;
		if (anagrafica != null){
			setCodAnagrafica(anagrafica.getCodAnagrafica());
		}
	}


	public String getIndirizzoAnagrafica() {
		if (indirizzoAnagrafica == null){
			if (getAnagrafica() != null){
				indirizzoAnagrafica = getAnagrafica().getIndirizzo();
			}else{
				indirizzoAnagrafica = "";
			}
		}		
		return indirizzoAnagrafica;
	}

	public String getCittaAnagrafica() {
		if (cittaAnagrafica == null){
			if (getAnagrafica() != null){
				cittaAnagrafica = getAnagrafica().getCitta();
			}else{
				cittaAnagrafica = "";
			}
		}				
		return cittaAnagrafica;
	}

	public String getCodicefiscaleAnagrafica() {
		if (codicefiscaleAnagrafica == null){
			if (getAnagrafica() != null){
				codicefiscaleAnagrafica = getAnagrafica().getCodiceFiscale();
			}else{
				codicefiscaleAnagrafica = "";
			}
		}						
		return codicefiscaleAnagrafica;
	}

	public String getPrimoRecapitoAnagrafica() {
		if (primoRecapitoAnagrafica == null){
			if (getAnagrafica() != null){
				ArrayList al = getAnagrafica().getContatti();
				if (al.size() > 0){
					primoRecapitoAnagrafica = (al.get(0) != null)
											  ? ((ContattiModel)al.get(0)).toString()
											  : "";
				}else{
					primoRecapitoAnagrafica = "";
			    }
			}else{
				primoRecapitoAnagrafica = "";
			}
			
		}
		return primoRecapitoAnagrafica;
	}

	public String getSecondoRecapitoAnagrafica() {
		if (secondoRecapitoAnagrafica == null){
			if (getAnagrafica() != null){
				ArrayList al = getAnagrafica().getContatti();
				if (al.size() > 0){
					secondoRecapitoAnagrafica = (al.get(1) != null)
										  		? ((ContattiModel)al.get(1)).toString()
										  		: "";
				}else{
					secondoRecapitoAnagrafica = "";
				}
			}else{
				secondoRecapitoAnagrafica = "";
			}
		}		
		return secondoRecapitoAnagrafica;
	}

	public String getNomeCognomeAnagrafica() {
		if (nomeCognomeAnagrafica == null){
			if (getAnagrafica() != null){
				nomeCognomeAnagrafica = getAnagrafica().getNome() + " " +
										getAnagrafica().getCognome();
			}else{
				nomeCognomeAnagrafica = "";
			}
		}
		return nomeCognomeAnagrafica;
	}

	public String getCapAnagrafica() {
		if (capAnagrafica == null){
			if (getAnagrafica() != null){
				capAnagrafica = getAnagrafica().getCap();
			}else{
				capAnagrafica = "";
			}
			
		}
		return capAnagrafica;
	}

	public String getProvinciaAnagrafica() {
		if (provinciaAnagrafica == null){
			if (getAnagrafica() != null){
				provinciaAnagrafica = getAnagrafica().getProvincia();
			}else{
				provinciaAnagrafica = "";
			}
			
		}		
		return provinciaAnagrafica;
	}

	public String getRagioneSocialeAnagrafica() {
		if (ragioneSocialeAnagrafica == null){
			if (getAnagrafica() != null){
				ragioneSocialeAnagrafica = getAnagrafica().getRagioneSociale();
			}else{
				ragioneSocialeAnagrafica = "";
			}
			
		}		

		return ragioneSocialeAnagrafica;
	}

	public String getPivaAnagrafica() {
		if (pivaAnagrafica == null){
			if (getAnagrafica() != null){
				pivaAnagrafica = getAnagrafica().getPartitaIva();
			}else{
				pivaAnagrafica = "";
			}
			
		}		

		return pivaAnagrafica;
	}
	
	

}
