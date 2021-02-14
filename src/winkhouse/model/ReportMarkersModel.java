package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import winkhouse.vo.ReportMarkersVO;


public class ReportMarkersModel extends ReportMarkersVO {

	public ReportMarkersModel() {

	}

	public ReportMarkersModel(ResultSet rs) throws SQLException {
		super(rs);

	}

}
