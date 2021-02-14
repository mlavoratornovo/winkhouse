package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AttributeModel;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AttributeXMLModel extends AttributeModel 
							   implements XMLSerializable, ObjectTypeCompare  {

	private Integer idObject = null;
	protected final int TYPE_VALUE = 5;
	private String importOperation = null;
	
	public AttributeXMLModel() {
		super();
	}
	
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
        }
                
        public void read(InputElement xml, AttributeXMLModel c_xml) throws XMLStreamException{
        	c_xml.setAttributeName(xml.getAttribute("attributeName", ""));
        	c_xml.setFieldType(xml.getAttribute("fieldType", ""));
        	Integer idAttribute = xml.getAttribute("idAttribute", 0);
        	if (idAttribute == 0){
        		idAttribute = null;
        	}        	        	
        	c_xml.setIdAttribute(idAttribute);
        	Integer idClassEntity = xml.getAttribute("idClassEntity", 0);
        	if (idClassEntity == 0){
        		idClassEntity = null;
        	}        	        	        	
        	c_xml.setIdClassEntity(idClassEntity);
        	Integer linkedIdEntity = xml.getAttribute("linkedIdEntity", 0);
        	if (linkedIdEntity == 0){
        		linkedIdEntity = null;
        	}        	        	        	
        	c_xml.setLinkedIdEntity(linkedIdEntity);
       }
        
   };

	
    public Integer getIdObject() {
		return idObject;
	}
	
    public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}	

    @Override
	public int getTypeValue() {
		return TYPE_VALUE;
	}
	
    @Override
	public int getUniqueKey() {
		return getIdAttribute();
	}

    @Override
	public String getImportOperation() {
		return importOperation;
	}	

	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
