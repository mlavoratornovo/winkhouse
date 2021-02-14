package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.vo.AgentiVO;

public class AgentiColloqui extends AgentiModel {

	public static final int AGENTI_COLLOQUIO_PARTECIPANTI = 0;
	public static final int AGENTI_COLLOQUIO_INSERITORI = 1;
	
	private int agenti_colloquio_type = 0;
	
	public AgentiColloqui() {
		
	}

	public AgentiColloqui(AgentiVO agentiVO) {
		super(agentiVO);		
	}

	public AgentiColloqui(ResultSet rs) throws SQLException {
		super(rs);
		agenti_colloquio_type = rs.getInt("agenti_colloquio_type");
	}

	public int getAgenti_colloquio_type() {
		return agenti_colloquio_type;
	}

	public void setAgenti_colloquio_type(int agenti_colloquio_type) {
		this.agenti_colloquio_type = agenti_colloquio_type;
	}

}
