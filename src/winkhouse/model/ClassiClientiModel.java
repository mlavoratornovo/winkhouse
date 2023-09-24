package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.vo.ClassiClientiVO;

public class ClassiClientiModel extends ClassiClientiVO {
	
	private String comune = null;
	
	public ClassiClientiModel() {
		// TODO Auto-generated constructor stub
	}

	public ClassiClientiModel(ResultSet rs, String comune) throws SQLException {
		super(rs);
		setComune(comune);
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}
	
	

}
