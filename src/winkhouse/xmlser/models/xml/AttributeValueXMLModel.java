package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;

import winkhouse.model.AttributeValueModel;

public class AttributeValueXMLModel extends AttributeValueModel
									implements XMLSerializable {

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
       }
        
   };	

}
