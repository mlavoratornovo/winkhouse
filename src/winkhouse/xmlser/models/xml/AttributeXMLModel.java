package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;

import winkhouse.model.AttributeModel;

public class AttributeXMLModel extends AttributeModel 
							   implements XMLSerializable {

	private Integer idObject = null;
	
	public AttributeXMLModel(AttributeModel attribute) {
		this.setAttributeName(attribute.getAttributeName());
		this.setFieldType(attribute.getFieldType());
		this.setIdAttribute(attribute.getIdAttribute());
		this.setIdClassEntity(attribute.getIdClassEntity());
		this.setLinkedIdEntity(attribute.getLinkedIdEntity());
	}

	public AttributeXMLModel(ResultSet rs) throws SQLException {
		super(rs);
		// TODO Auto-generated constructor stub
	}

	protected static final XMLFormat<AttributeXMLModel> ATTRIBUTE_XML = new XMLFormat<AttributeXMLModel>(AttributeXMLModel.class){
		
        public void write(AttributeXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("attributeName", a_xml.getAttributeName());
        	xml.setAttribute("fieldType", a_xml.getFieldType());
        	xml.setAttribute("idAttribute", a_xml.getIdAttribute());
        	xml.setAttribute("idClassEntity", a_xml.getIdClassEntity());
        	xml.setAttribute("linkedIdEntity", a_xml.getLinkedIdEntity());
        }
                
        public void read(InputElement xml, AttributeXMLModel c_xml) throws XMLStreamException{
       }
        
   };

	
    public Integer getIdObject() {
		return idObject;
	}

	
    public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}	

}
