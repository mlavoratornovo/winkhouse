package winkhouse.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerMonthVO {

	private Integer num = null;
	private Integer year = null;
	
	public IntegerMonthVO(ResultSet rs) throws SQLException {
		num = rs.getInt("NUMTIME");
	}

	public Integer getNum() {
		return num;
	}

	@Override
	public String toString() {		
		return String.valueOf(num);
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
	
}
