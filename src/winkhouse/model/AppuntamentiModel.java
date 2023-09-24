package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.AgentiAppuntamentiDAO;
import winkhouse.dao.AnagraficheAppuntamentiDAO;
import winkhouse.dao.TipiAppuntamentiDAO;
import winkhouse.dao.WinkGCalendarDAO;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AppuntamentiVO;
import winkhouse.vo.TipiAppuntamentiVO;


public class AppuntamentiModel extends AppuntamentiVO {

	private ArrayList<AgentiAppuntamentiModel> agenti = null;
	private ArrayList<AnagraficheAppuntamentiModel> anagrafiche = null;	
	private String dataStrAppuntamento = null;
	private String oraStrAppuntamento = null;
	private String dataFineStrAppuntamento = null;
	private String oraFineStrAppuntamento = null;	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
	private TipiAppuntamentiVO tipoAppuntamento = null;
	private ArrayList<WinkGCalendarModel> winkGCalendarModels = null;
	
	public AppuntamentiModel() {
		super();
	}
	
	public AppuntamentiModel(AppuntamentiVO appuntamento) {
		setCodAppuntamento(appuntamento.getCodAppuntamento());
		setDataAppuntamento(appuntamento.getDataAppuntamento());
		setDataFineAppuntamento(appuntamento.getDataFineAppuntamento());
		setDataInserimento(appuntamento.getDataInserimento());
		setDescrizione(appuntamento.getDescrizione());
		setLuogo(appuntamento.getLuogo());
		setCodTipoAppuntamento(appuntamento.getCodTipoAppuntamento());
		setiCalUID(appuntamento.getiCalUID());
	}

	public AppuntamentiModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public ArrayList<AgentiAppuntamentiModel> getAgenti() {
		if (agenti == null){
			if (getCodAppuntamento() != null){
				AgentiAppuntamentiDAO aaDAO = new AgentiAppuntamentiDAO();
				agenti = aaDAO.listAgentiAppuntamentiByAppuntamento(AgentiAppuntamentiModel.class.getName(), 
																	getCodAppuntamento());
			}
		}
		return agenti;
	}

	public void setAgenti(ArrayList<AgentiAppuntamentiModel> agenti) {
		this.agenti = agenti;
	}

	public ArrayList<AnagraficheAppuntamentiModel> getAnagrafiche() {
		if (anagrafiche == null){
			if (getCodAppuntamento() != null){
				AnagraficheAppuntamentiDAO aaDAO = new AnagraficheAppuntamentiDAO();
				anagrafiche = aaDAO.listAnagraficheAppuntamentiByAppuntamento(AnagraficheAppuntamentiModel.class.getName(), 
																			  getCodAppuntamento());
			}
		}
		return anagrafiche;
	}

	public void setAnagrafiche(ArrayList<AnagraficheAppuntamentiModel> anagrafiche) {
		this.anagrafiche = anagrafiche;
	}
		
	public String getAnagraficheDescription(){
		
		String returnValue = "";
		ArrayList<AnagraficheAppuntamentiModel> anag = getAnagrafiche();
		if (anag != null){
			Iterator<AnagraficheAppuntamentiModel> it = anag.iterator();
			while(it.hasNext()){
				AnagraficheModel am = it.next().getAnagrafica();				
				returnValue += (am != null)?am.toString() + " ":"";
			}			
		}

		return returnValue;
	}
	
	public String getAgentiDescription(){
		
		String returnValue = "";
		ArrayList<AgentiAppuntamentiModel> ag = getAgenti();
		if (ag != null){
			Iterator<AgentiAppuntamentiModel> it = ag.iterator();
			while(it.hasNext()){
				AgentiVO aVO = it.next().getAgente();
				returnValue += (aVO != null)?aVO.toString() + " ":"";
			}			
		}

		return returnValue;
	}

	@Override
	public String toString() {
		return getCodAppuntamento().toString() + " - " +
			   getDataAppuntamento().toString() + " - " + 
			   getDataFineStrAppuntamento() +
			   ((getLuogo() != null && !getLuogo().equalsIgnoreCase(""))? " - " + getLuogo(): "") +  
			   ((getDescrizione() != null && !getDescrizione().equalsIgnoreCase(""))? " - " + getDescrizione(): "");
	}

	public String getDataStrAppuntamento() {
		if (dataStrAppuntamento == null){
			dataStrAppuntamento = formatter.format(getDataAppuntamento().getTime());
		}
		return dataStrAppuntamento;
	}

	public String getOraStrAppuntamento() {
		if (oraStrAppuntamento == null){
			oraStrAppuntamento = formatterTime.format(getDataAppuntamento().getTime());
		}
		return oraStrAppuntamento;
	}

	public TipiAppuntamentiVO getTipoAppuntamento() {
		if (tipoAppuntamento == null){
			if (getCodTipoAppuntamento() != null){
				TipiAppuntamentiDAO taDAO = new TipiAppuntamentiDAO();
				tipoAppuntamento = (TipiAppuntamentiVO)taDAO.getTipiAppuntamentiByID(TipiAppuntamentiVO.class.getName(), getCodTipoAppuntamento());
			}
		}
		return tipoAppuntamento;
	}

	public void setTipoAppuntamento(TipiAppuntamentiVO tipoAppuntamento) {
		if (tipoAppuntamento != null){
			setCodTipoAppuntamento(tipoAppuntamento.getCodTipoAppuntamento());
		}else{
			setCodTipoAppuntamento(null);
		}
		this.tipoAppuntamento = tipoAppuntamento;
	}

	public String getDataFineStrAppuntamento() {
		if (dataFineStrAppuntamento == null){
			if (getDataFineAppuntamento() != null){
				dataFineStrAppuntamento = formatter.format(getDataFineAppuntamento().getTime());
			}
		}
		return dataFineStrAppuntamento;
	}

	public String getOraFineStrAppuntamento() {
		if (oraFineStrAppuntamento == null){
			if (getDataFineAppuntamento() != null){
				oraFineStrAppuntamento = formatterTime.format(getDataFineAppuntamento().getTime());
			}
		}		
		return oraFineStrAppuntamento;
	}

	
	public ArrayList<WinkGCalendarModel> getWinkGCalendarModels() {
		if (winkGCalendarModels == null){
			if (getCodAppuntamento() != null){
				WinkGCalendarDAO wgcDAO = new WinkGCalendarDAO();
				winkGCalendarModels = wgcDAO.getWinkGCalendarByCodAppuntamento(getCodAppuntamento());
			}
		}
		return winkGCalendarModels;
	}

	public void setWinkGCalendarModels(
			ArrayList<WinkGCalendarModel> winkGCalendarModels) {
		this.winkGCalendarModels = winkGCalendarModels;
	}
	
}
