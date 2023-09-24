package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AgentiModel;
import winkhouse.model.ContattiModel;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.ContattiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;


public class AgentiXMLModel extends AgentiModel implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 0;
	private String importOperation = null;
	
	public AgentiXMLModel() {
		super();
	}
	
	public AgentiXMLModel(AgentiVO agentiVO) {
		super(agentiVO);
	}

	public AgentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<AgentiXMLModel> AGENTI_XML = new XMLFormat<AgentiXMLModel>(AgentiXMLModel.class){
		
        public void write(AgentiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AgentiXMLModel a_xml) throws XMLStreamException{
        	Integer codAgente = xml.getAttribute("codAgente",0);
        	if (codAgente == 0){
        		codAgente = null;
        	}
        	a_xml.setCodAgente(codAgente);
        	a_xml.setNome(xml.getAttribute("nome",""));
        	a_xml.setCognome(xml.getAttribute("cognome",""));
        	a_xml.setProvincia(xml.getAttribute("provincia", ""));
        	a_xml.setCap(xml.getAttribute("cap",""));
        	a_xml.setCitta(xml.getAttribute("citta",""));
        	a_xml.setIndirizzo(xml.getAttribute("indirizzo",""));
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
		return getCodAgente();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
