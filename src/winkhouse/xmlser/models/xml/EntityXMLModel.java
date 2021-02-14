package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.EntityModel;
import winkhouse.vo.EntityVO;

public class EntityXMLModel extends EntityModel 
							implements XMLSerializable {
	
	private Integer idObject = null;
	
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
        	xml.setAttribute("idClassEntity", e_xml.getIdClassEntity());
        	xml.setAttribute("description", e_xml.getDescription());
        	xml.setAttribute("className", e_xml.getClassName());        	        	
        }
                
        public void read(InputElement xml, EntityXMLModel c_xml) throws XMLStreamException{
       }
        
   };

	
    public Integer getIdObject() {
		return idObject;
	}

	
    public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}	

}
