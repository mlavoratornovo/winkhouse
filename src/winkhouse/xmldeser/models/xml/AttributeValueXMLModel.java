package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AttributeValueModel;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AttributeValueXMLModel extends AttributeValueModel
									implements XMLSerializable, ObjectTypeCompare  {

	private Integer idObject = null;
	protected final int TYPE_VALUE = 6;
	private String importOperation = null;

	public AttributeValueXMLModel(){}
	
	public AttributeValueXMLModel(AttributeValueModel value) {
		this.setFieldValue(value.getFieldValue());
		this.setIdField(value.getIdField());
		this.setIdObject(value.getIdObject());
		this.setIdValue(value.getIdValue());
	}

	public AttributeValueXMLModel(ResultSet rs) throws SQLException {
		super(rs);
		// TODO Auto-generated constructor stub
	}

	protected static final XMLFormat<AttributeValueXMLModel> ATTRIBUTEVALUE_XML = new XMLFormat<AttributeValueXMLModel>(AttributeValueXMLModel.class){
		
        public void write(AttributeValueXMLModel av_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("fieldValue", av_xml.getFieldValue());
        	xml.setAttribute("idField", av_xml.getIdField());
        	xml.setAttribute("idObject", av_xml.getIdObject());
        	xml.setAttribute("idValue", av_xml.getIdValue());
        }
                
        public void read(InputElement xml, AttributeValueXMLModel c_xml) throws XMLStreamException{
        	
        	c_xml.setFieldValue(xml.getAttribute("fieldValue", ""));
        	Integer idField = xml.getAttribute("idField", 0);
        	if (idField == 0){
        		idField = null;
        	}        	        	
        	c_xml.setIdField(idField);
        	Integer idObject = xml.getAttribute("idObject", 0);
        	if (idObject == 0){
        		idObject = null;
        	}        	        	        	
        	c_xml.setIdObject(idObject);
        	Integer idValue = xml.getAttribute("idValue", 0);
        	if (idValue == 0){
        		idValue = null;
        	}        	        	        	
        	c_xml.setIdValue(idValue);
       }
        
   };	
   
   @Override
	public int getTypeValue() {
		return TYPE_VALUE;
	}
	
   @Override
	public int getUniqueKey() {
		return getIdValue();
	}

   @Override
	public String getImportOperation() {
		return importOperation;
	}	

	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
