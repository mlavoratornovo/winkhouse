package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.vo.ClasseEnergeticaVO;

public class ClasseEnergeticaModel extends ClasseEnergeticaVO {

	private String comune = null;
	
	public ClasseEnergeticaModel() {
		// TODO Auto-generated constructor stub
	}

	public ClasseEnergeticaModel(ResultSet rs,String comune) throws SQLException {
		super(rs);
		this.comune = comune;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

}
