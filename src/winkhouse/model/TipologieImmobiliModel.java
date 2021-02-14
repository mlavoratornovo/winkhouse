package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.vo.TipologieImmobiliVO;

public class TipologieImmobiliModel extends TipologieImmobiliVO {

	private String comune = null;
	
	public TipologieImmobiliModel() {
		// TODO Auto-generated constructor stub
	}

	public TipologieImmobiliModel(ResultSet rs, String comune) throws SQLException {
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
