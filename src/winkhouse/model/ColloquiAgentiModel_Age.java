package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.AgentiDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.vo.ColloquiAgentiVO;


public class ColloquiAgentiModel_Age extends ColloquiAgentiVO {

	private AgentiModel agente = null;
	private ColloquiModel colloquio = null;
	
	public ColloquiAgentiModel_Age() {
	}

	public ColloquiAgentiModel_Age(ColloquiAgentiVO caVO) {
		setCodAgente(caVO.getCodAgente());
		setCodColloquio(caVO.getCodColloquio());
		setCodColloquioAgenti(caVO.getCodColloquioAgenti());
		setCommento(caVO.getCommento());
	}
	
	public ColloquiAgentiModel_Age(ResultSet rs) throws SQLException {
		super(rs);
	}

	public AgentiModel getAgente() {
		if (agente == null){
			if ((super.getCodAgente() != null) && (super.getCodAgente() != 0)){
				AgentiDAO agentiDAO = new AgentiDAO();
				Object o = agentiDAO.getAgenteById(AgentiModel.class.getName(), super.getCodAgente());
				if (o != null){
					agente = (AgentiModel)o;
				}
			}
		}
		return agente;
	}

	@Override
	public String toString() {
		return getAgente().toString() + " " + getCommento();
	}
	
	public ColloquiModel getColloquio() {
		if (colloquio != null){
			ColloquiDAO cDAO = new ColloquiDAO();
			ColloquiModel cModel = (ColloquiModel)cDAO.getColloquioById(ColloquiModel.class.getName(), 
																	    getCodColloquio());
		}
		return colloquio;
	}

	public void setAgente(AgentiModel agente) {
		this.agente = agente;
	}

	public void setColloquio(ColloquiModel colloquio) {
		this.colloquio = colloquio;
	}
}
