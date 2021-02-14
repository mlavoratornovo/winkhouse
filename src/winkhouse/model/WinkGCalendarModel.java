package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AppuntamentiDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.vo.WinkGCalendarVO;

public class WinkGCalendarModel extends WinkGCalendarVO {

	private AgentiModel agente = null;
	private ColloquiModel colloquio = null;
	private AppuntamentiModel appuntamento = null;
	
	public WinkGCalendarModel(Integer codAgente, Integer codAppuntamento,
							  Integer codColloquio, String calendarId, String eventId) {
		super(codAgente, codAppuntamento, codColloquio, calendarId, eventId);
	}

	public WinkGCalendarModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public AgentiModel getAgente() {
		if (agente == null){
			AgentiDAO aDAO = new AgentiDAO();
			agente = (AgentiModel)aDAO.getAgenteById(AgentiModel.class.getName(), this.getCodAgente());
		}
		return agente;
	}

	public ColloquiModel getColloquio() {
		if ((colloquio == null) && (getCodColloquio() != null)){
			ColloquiDAO cDAO = new ColloquiDAO();
			colloquio = (ColloquiModel)cDAO.getColloquioById(ColloquiModel.class.getName(), this.getCodColloquio());
		}		
		return colloquio;
	}

	public AppuntamentiModel getAppuntamento() {
		if ((appuntamento == null) && (getCodAppuntamento() != null)){
			AppuntamentiDAO aDAO = new AppuntamentiDAO();
			appuntamento = (AppuntamentiModel)aDAO.getAppuntamentoByID(AppuntamentiModel.class.getName(), this.getCodAppuntamento());
		}		
		return appuntamento;
	}

	
	
}
