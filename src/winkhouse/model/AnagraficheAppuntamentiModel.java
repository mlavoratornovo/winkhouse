package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.vo.AnagraficheAppuntamentiVO;


public class AnagraficheAppuntamentiModel extends AnagraficheAppuntamentiVO {

	private AnagraficheModel anagrafica = null;
	
	public AnagraficheAppuntamentiModel() {
		super();
	}

	public AnagraficheAppuntamentiModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public AnagraficheModel getAnagrafica() {
		if ((anagrafica == null) && (getCodAnagrafica() != 0)){
			AnagraficheDAO aDAO = new AnagraficheDAO();
			anagrafica = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), getCodAnagrafica());
		}
		return anagrafica;
	}

	public void setAnagrafica(AnagraficheModel anagrafica) {
		this.anagrafica = anagrafica;
		setCodAnagrafica(anagrafica.getCodAnagrafica());
	}

	@Override
	public String toString() {
		return getAnagrafica().toString() + " - " + getNote();
	}

}
