package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.GCalendarVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class GCalendarXMLModel extends GCalendarVO 
							   implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 0;
	private String importOperation = null;
	
	public GCalendarXMLModel() {
		super();
	}

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
        }
                
        public void read(InputElement xml, GCalendarXMLModel a_xml) throws XMLStreamException{
        	Integer codGCalendar = xml.getAttribute("codGCalendar", 0);
        	if (codGCalendar == 0){
        		codGCalendar = null;
        	}        	        	
        	a_xml.setCodGCalendar(codGCalendar);
        	Integer codGData = xml.getAttribute("idClassEntity", 0);
        	if (codGData == 0){
        		codGData = null;
        	}        	        	        	
        	a_xml.setCodGData(codGData);
        	a_xml.setAllUrl(xml.getAttribute("allUrl", ""));
        	a_xml.setPrivateUrl(xml.getAttribute("privateUrl", ""));        	
       }
        
   };
	
    public boolean isUpdateItem() {
		return updateItem;
	}

	public void setUpdateItem(boolean updateItem) {
		this.updateItem = updateItem;
	}	

    public int getTypeValue() {
		return TYPE_VALUE;
	}	

	public int getUniqueKey() {
		return getCodGCalendar();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
