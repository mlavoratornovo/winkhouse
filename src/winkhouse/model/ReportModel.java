package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.dao.ReportMarkersDAO;
import winkhouse.vo.ReportMarkersVO;
import winkhouse.vo.ReportVO;


public class ReportModel extends ReportVO {

	private ArrayList<ReportMarkersVO> markers = null;
	
	public ReportModel() {
	}

	public ReportModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public ArrayList<ReportMarkersVO> getMarkers() {
		if (markers == null){
			ReportMarkersDAO rmDAO = new ReportMarkersDAO();
			markers = rmDAO.getMarkersByReport(super.getCodReport());
		}
		return markers;
	}

	public void setMarkers(ArrayList<ReportMarkersVO> markers) {
		this.markers = markers;
	}

	
}
