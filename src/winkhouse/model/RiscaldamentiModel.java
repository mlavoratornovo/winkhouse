package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.vo.RiscaldamentiVO;

public class RiscaldamentiModel extends RiscaldamentiVO {

	private String comune = null;
	
	public RiscaldamentiModel() {
		// TODO Auto-generated constructor stub
	}

	public RiscaldamentiModel(ResultSet rs,String comune) throws SQLException {
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
