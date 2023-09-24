package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class CriteriRicercaXMLModel extends CriteriRicercaModel 
									implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 4;
	private String importOperation = null;
	
	public CriteriRicercaXMLModel() {
		super();
	}

	public CriteriRicercaXMLModel(ColloquiCriteriRicercaVO ccrVO) {
		super(ccrVO);
	}

	public CriteriRicercaXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<CriteriRicercaXMLModel> CRITERIORICERCA_XML = new XMLFormat<CriteriRicercaXMLModel>(CriteriRicercaXMLModel.class){
		
        public void write(CriteriRicercaXMLModel cr_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, CriteriRicercaXMLModel cr_xml) throws XMLStreamException{
        	Integer codCriterioRicerca = xml.getAttribute("codCriterioRicerca", 0);
        	if (codCriterioRicerca == 0){
        		codCriterioRicerca = null;
        	}        	        	
        	cr_xml.setCodCriterioRicerca(codCriterioRicerca);
        	Integer codColloquio = xml.getAttribute("codColloquio", 0);
        	if (codColloquio == 0){
        		codColloquio = null;
        	}        	        	        	
        	cr_xml.setCodColloquio(codColloquio);
        	Integer codRicerca = xml.getAttribute("codRicerca", 0);
        	if (codRicerca == 0){
        		codRicerca = null;
        	}        	        	        	
        	cr_xml.setCodRicerca(codRicerca);
        	cr_xml.setFromValue(xml.getAttribute("fromValue", ""));
        	cr_xml.setToValue(xml.getAttribute("toValue", ""));
        	cr_xml.setGetterMethodName(xml.getAttribute("getterMethodName", ""));
        	cr_xml.setLogicalOperator(xml.getAttribute("logicalOperator", ""));
        	cr_xml.setLineNumber(xml.getAttribute("lineNumber", 0));

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
		return getCodCriterioRicerca();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
