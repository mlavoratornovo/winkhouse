package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.EntityModel;
import winkhouse.vo.EntityVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class EntityXMLModel extends EntityModel 
							implements XMLSerializable, ObjectTypeCompare {
	
	private Integer idObject = null;
	protected final int TYPE_VALUE = 4;
	private String importOperation = null;
	
	public EntityXMLModel(){
		super();
	}
	
	public EntityXMLModel(EntityVO evo) {
		this.setClassName(evo.getClassName());
		this.setDescription(evo.getDescription());
		this.setIdClassEntity(evo.getIdClassEntity());
	}

	public EntityXMLModel(ResultSet rs) throws SQLException {
		super(rs);
		// TODO Auto-generated constructor stub
	}

	protected static final XMLFormat<EntityXMLModel> ENTITY_XML = new XMLFormat<EntityXMLModel>(EntityXMLModel.class){
		
        public void write(EntityXMLModel e_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, EntityXMLModel c_xml) throws XMLStreamException{
        	Integer idClassEntity = xml.getAttribute("idClassEntity", 0);
        	if (idClassEntity == 0){
        		idClassEntity = null;
        	}        	        	
        	c_xml.setIdClassEntity(idClassEntity);
        	c_xml.setDescription(xml.getAttribute("description", ""));
        	c_xml.setClassName(xml.getAttribute("className", ""));
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
		return getIdClassEntity();
	}

	
    @Override
	public String getImportOperation() {
		return importOperation;
	}	

	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
