package winkhouse.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.GDataDAO;
import winkhouse.dao.TipologiaContattiDAO;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.TipologiaContattiVO;


public class ContattiModel extends ContattiVO {

	private TipologiaContattiVO tipologia = null;	
	private String descrizionePropietario = null;
	private AnagraficheModel anagrafica = null;
	private AgentiModel agente = null;
	private GDataModel gdataModel = null;
	private URL fullPrivateUrl = null;
	private boolean canedit = true;	
	

	public ContattiModel() {
		super();
	}

	public ContattiModel(ContattiVO cVO) {
		setCodAgente(cVO.getCodAgente());
		setCodAnagrafica(cVO.getCodAnagrafica());
		setCodContatto(cVO.getCodContatto());
		setCodTipologiaContatto(cVO.getCodTipologiaContatto());
		setContatto(cVO.getContatto());
		setDescrizione(cVO.getDescrizione());
	}
	
	public ContattiModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public TipologiaContattiVO getTipologia() {
		if (tipologia == null){
			if ((super.getCodTipologiaContatto() != null) && 
			    (super.getCodTipologiaContatto() != 0)){
				TipologiaContattiDAO tcDAO = new TipologiaContattiDAO();
				Object o = tcDAO.getTipologiaContattoById("winkhouse.vo.TipologiaContattiVO", getCodTipologiaContatto());
				if (o != null){
					tipologia = (TipologiaContattiVO)o;
				}
			}
		}
		return tipologia;
	}

	public void setTipologia(TipologiaContattiVO tipologia) {
		this.tipologia = tipologia;
		if (tipologia != null){
			setCodTipologiaContatto(tipologia.getCodTipologiaContatto());
		}
	}

	@Override
	public String toString() {
		String returnValue = "";
		if (getTipologia() != null){
			returnValue += getTipologia().getDescrizione() + " contatto : " + getContatto();
		}else{
			returnValue += " contatto : " + getContatto();
		}
		return returnValue;
	}
	
	public String getDesTipologiaContatto(){
		return getTipologia().getDescrizione();
	}
	
	public String getDescrizionePropietario(){
		
		if (descrizionePropietario == null){
		
			if ((getCodAgente() != 0) && (getCodAnagrafica() == 0)){
				AgentiDAO agentiDAO = new AgentiDAO();
				AgentiVO aVO = (AgentiVO)agentiDAO.getAgenteById(AgentiVO.class.getName(), 
																 getCodAgente());
				descrizionePropietario = aVO.getCognome() + " " + aVO.getNome();
			}else if ((getCodAgente() == 0) && (getCodAnagrafica() != 0)){
				AnagraficheDAO anagraficheDAO = new AnagraficheDAO();
				AnagraficheVO anagraficheVO = (AnagraficheVO)anagraficheDAO.getAnagraficheById(AnagraficheVO.class.getName(),
																							   getCodAnagrafica());
				descrizionePropietario = anagraficheVO.getCognome() + " " + anagraficheVO.getNome();
			}else{
				descrizionePropietario = "";
			}
			
		}
		
		return descrizionePropietario;
	}

	public AgentiModel getAgente() {
		if ((agente == null) && 
			(getCodAgente().intValue() != 0)){
				
			AgentiDAO aDAO = new AgentiDAO();
				agente = (AgentiModel)aDAO.getAgenteById(AgentiModel.class.getName(), 
														 getCodAgente());
				
		}		
		return agente;
	}

	public AnagraficheModel getAnagrafica() {
		if ((anagrafica == null) &&
			(getCodAnagrafica() != null) &&	
			(getCodAnagrafica().intValue() != 0)){
			
			AnagraficheDAO aDAO = new AnagraficheDAO();
			anagrafica = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
																   getCodAnagrafica());
			
		}
		return anagrafica;
	}
	
	public boolean isGMailAccount(){
		return WinkhouseUtils.getInstance()
					  		 .isGMailAccount(this.getContatto());
	}

	public GDataModel getGMailAccountData(){
		
		if (gdataModel == null){
			if (isGMailAccount()){
				GDataDAO gdDAO = new GDataDAO();
				gdataModel = (GDataModel)gdDAO.getGoogleDataByCodContatto(GDataModel.class.getName(), 
													 		  			  this.getCodContatto());
			}			
		}
		
		return gdataModel;
	}
	
	public URL getFullPrivateUrl() {
		if (this.isGMailAccount()){
			if (fullPrivateUrl == null){			
				try {
					fullPrivateUrl = new URL("http://www.google.com/calendar/feeds/"+this.getContatto()+"/private/full");
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		return fullPrivateUrl;
	}

	
	public boolean isCanedit() {
		return canedit;
	}

	
	public void setCanedit(boolean canedit) {
		this.canedit = canedit;
	}

	public void setAnagrafica(AnagraficheModel anagrafica) {
		this.anagrafica = anagrafica;
	}

	public void setAgente(AgentiModel agente) {
		this.agente = agente;
	}

	
}
