package winkhouse.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerYearVO {

	private Integer num = null;
	
	public IntegerYearVO(ResultSet rs) throws SQLException {
		num = rs.getInt("NUMTIME");
	}

	public Integer getNum() {
		return num;
	}

	@Override
	public String toString() {		
		return String.valueOf(num);
	}
	
	
}
