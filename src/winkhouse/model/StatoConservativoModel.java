package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.vo.StatoConservativoVO;

public class StatoConservativoModel extends StatoConservativoVO {

	private String comune = null;
	
	public StatoConservativoModel() {
		// TODO Auto-generated constructor stub
	}

	public StatoConservativoModel(ResultSet rs, String comune) throws SQLException {
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
