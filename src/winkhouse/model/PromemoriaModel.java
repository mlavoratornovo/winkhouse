package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.dao.AgentiDAO;
import winkhouse.dao.PromemoriaLinksDAO;
import winkhouse.dao.PromemoriaOggettiDAO;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.PromemoriaLinksVO;
import winkhouse.vo.PromemoriaVO;

public class PromemoriaModel extends PromemoriaVO {

	private AgentiVO agente = null;
	private ArrayList<PromemoriaOggettiModel> linkedObjects = null;
	private ArrayList<PromemoriaLinksVO> linkurl = null;
	
	public PromemoriaModel() {
	}

	public PromemoriaModel(ResultSet rs) throws SQLException {
		super(rs);
		// TODO Auto-generated constructor stub
	}
	
	public AgentiVO getAgente() {
		if (agente == null){
			AgentiDAO adao = new AgentiDAO();
			agente = (AgentiVO)adao.getAgenteById(AgentiVO.class.getName(), getCodAgente());
		}
		return agente;
	}
	
	public void setAgente(AgentiVO agente) {
		this.agente = agente;
		setCodAgente(agente.getCodAgente());
	}
	
	public ArrayList<PromemoriaOggettiModel> getLinkedObjects() {
		if (linkedObjects == null){
			if (getCodPromemoria() != null && getCodPromemoria() != 0){
				PromemoriaOggettiDAO poDAO = new PromemoriaOggettiDAO();
				linkedObjects = poDAO.listByCodPromemoria(PromemoriaOggettiModel.class.getName(), getCodPromemoria());
			}else{
				linkedObjects = new ArrayList<PromemoriaOggettiModel>();
			}
		}
		return linkedObjects;
	}
	
	public void setLinkedObjects(ArrayList linkedObjects) {
		this.linkedObjects = linkedObjects;
	}

	public ArrayList<PromemoriaLinksVO> getLinksUrls(){
		
		if (linkurl == null){
			if (getCodPromemoria() != null){
				linkurl = new PromemoriaLinksDAO().listByCodPromemoria(PromemoriaLinksVO.class.getName(), getCodPromemoria());
			}
		}
		
		return linkurl;
	}
	
	public void setLinksUrls(ArrayList<PromemoriaLinksVO> linksurls){
		this.linkurl = linksurls; 
	}
}
