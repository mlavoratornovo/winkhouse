package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.GCalendarVO;

public class GCalendarXMLModel extends GCalendarVO 
							   implements XMLSerializable {

	public GCalendarXMLModel(GCalendarVO gcVO) {
		setCodGCalendar(gcVO.getCodGCalendar());
		setCodGData(gcVO.getCodGData());
		setPrivateUrl(gcVO.getPrivateUrl());
		setAllUrl(gcVO.getAllUrl());
	}

	public GCalendarXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<GCalendarXMLModel> GCALENDAR_XML = new XMLFormat<GCalendarXMLModel>(GCalendarXMLModel.class){
		
        public void write(GCalendarXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codGCalendar", a_xml.getCodGCalendar());
			xml.setAttribute("codGData", a_xml.getCodGData());
			xml.setAttribute("allUrl", a_xml.getAllUrl());
			xml.setAttribute("privateUrl", a_xml.getPrivateUrl());
        }
                
        public void read(InputElement xml, GCalendarXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	


}
