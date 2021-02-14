package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.AgentiDAO;
import winkhouse.vo.AgentiAppuntamentiVO;


public class AgentiAppuntamentiModel extends AgentiAppuntamentiVO {

	private AgentiModel agente = null;
	
	public AgentiAppuntamentiModel() {
		super();
	}

	public AgentiAppuntamentiModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public AgentiModel getAgente() {
		if ((agente == null) && (getCodAgente() != null && getCodAgente() != 0) ){
			AgentiDAO aDAO = new AgentiDAO();
			agente = (AgentiModel)aDAO.getAgenteById(AgentiModel.class.getName(), getCodAgente());
		}
		return agente;
	}

	public void setAgente(AgentiModel agente) {
		this.agente = agente;
		setCodAgente(agente.getCodAgente());
	}

	@Override
	public String toString() {
		return getAgente().toString() + " - " +getNote();
	}
	
	

}
