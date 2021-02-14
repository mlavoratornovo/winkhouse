package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.RicercheDAO;
import winkhouse.vo.PermessiVO;

public class PermessiModel extends PermessiVO {

	private RicercheModel ricerca = null;
	
	public PermessiModel() {
		
	}

	public PermessiModel(ResultSet rs) throws SQLException {
		super(rs);
		// TODO Auto-generated constructor stub
	}

	public RicercheModel getRicercaModel(){
		
		if (ricerca == null){
			RicercheDAO rDAO = new RicercheDAO();
			ricerca = (RicercheModel)rDAO.getRicercaById(RicercheModel.class.getName(), super.getCodRicerca());
		}
		
		return ricerca;
		
	}
}
