package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.vo.ImmobiliVO;


public class Immobili_AffittiModel extends ImmobiliModel {

	public Immobili_AffittiModel() {
		super();
	}

	public Immobili_AffittiModel(ImmobiliVO iVO) {
		super(iVO);
	}

	public Immobili_AffittiModel(ResultSet rs) throws SQLException {
		super(rs);
	}

}
