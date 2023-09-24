package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.dao.GCalendarDAO;
import winkhouse.vo.GCalendarVO;
import winkhouse.vo.GDataVO;

public class GDataModel extends GDataVO {

	private ArrayList<GCalendarVO> calendarSettings = null;

	
	public GDataModel() {
		super();
	}

	public GDataModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public ArrayList<GCalendarVO> getCalendarSettings() {
		
		if (calendarSettings == null){
			GCalendarDAO gcDAO = new GCalendarDAO();
			calendarSettings  = (ArrayList)gcDAO.getGCalendarsByCodGData(GCalendarVO.class.getName(), 
																		   getCodGData());
			if (calendarSettings == null){
				calendarSettings = new ArrayList<GCalendarVO>();				
			}
		}
		return calendarSettings;
	}

	public void setCalendarSettings(ArrayList calendarSettings) {
		this.calendarSettings = calendarSettings;
	}



	
}
