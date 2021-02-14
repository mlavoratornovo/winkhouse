package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AgentiAppuntamentiModel;
import winkhouse.vo.AgentiAppuntamentiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AgentiAppuntamentiXMLModel extends AgentiAppuntamentiModel
										implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 3;
	private String importOperation = null;
	
	public AgentiAppuntamentiXMLModel() {
		super();
	}

	public AgentiAppuntamentiXMLModel(AgentiAppuntamentiVO apVO) {
		setCodAgente(apVO.getCodAgente());
		setCodAgentiAppuntamenti(apVO.getCodAgentiAppuntamenti());
		setCodAppuntamento(apVO.getCodAppuntamento());
		setNote(apVO.getNote());
	}

	public AgentiAppuntamentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<AgentiAppuntamentiXMLModel> AGENTIAPPUNTAMENTI_XML = new XMLFormat<AgentiAppuntamentiXMLModel>(AgentiAppuntamentiXMLModel.class){
		
        public void write(AgentiAppuntamentiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AgentiAppuntamentiXMLModel a_xml) throws XMLStreamException{
        	
        	Integer codAgentiAppuntamenti = xml.getAttribute("codAgentiAppuntamenti",0);
        	if (codAgentiAppuntamenti == 0){
        		codAgentiAppuntamenti = null;
        	}
        	a_xml.setCodAgentiAppuntamenti(codAgentiAppuntamenti);
        	Integer codAgente = xml.getAttribute("codAgente",0);
        	if (codAgente == 0){
        		codAgente = null;
        	}        	
        	a_xml.setCodAgente(codAgente);
        	Integer codAppuntamento = xml.getAttribute("codAppuntamento",0);
        	if (codAppuntamento == 0){
        		codAppuntamento = null;
        	}        	        	
        	a_xml.setCodAppuntamento(codAppuntamento);
        	a_xml.setNote(xml.getAttribute("note",""));
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
		return getCodAgentiAppuntamenti();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
