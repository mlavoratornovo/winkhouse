package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class ColloquiCriteriRicercaXMLModel extends ColloquiCriteriRicercaVO
											implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 5;
	private String importOperation = null;
	
	public ColloquiCriteriRicercaXMLModel() {
		super();
	}

	public ColloquiCriteriRicercaXMLModel(ColloquiCriteriRicercaVO ccrVO) {
		setCodCriterioRicerca(ccrVO.getCodCriterioRicerca());
		setCodColloquio(ccrVO.getCodColloquio());
		setCodRicerca(ccrVO.getCodRicerca());
		setFromValue(ccrVO.getFromValue());
		setGetterMethodName(ccrVO.getGetterMethodName());
		setLineNumber(ccrVO.getLineNumber());
		setLogicalOperator(ccrVO.getLogicalOperator());
		setToValue(ccrVO.getToValue());
	}

	public ColloquiCriteriRicercaXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<ColloquiCriteriRicercaXMLModel> COLLOQUICRITERIRICERCA_XML = new XMLFormat<ColloquiCriteriRicercaXMLModel>(ColloquiCriteriRicercaXMLModel.class){
		
        public void write(ColloquiCriteriRicercaXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, ColloquiCriteriRicercaXMLModel a_xml) throws XMLStreamException{
        	Integer codCriterioRicerca = xml.getAttribute("codCriterioRicerca", 0);
        	if (codCriterioRicerca == 0){
        		codCriterioRicerca = null;
        	}        	        	
        	a_xml.setCodCriterioRicerca(codCriterioRicerca);
        	Integer codColloquio = xml.getAttribute("codColloquio", 0);
        	if (codColloquio == 0){
        		codColloquio = null;
        	}        	        	        	
        	a_xml.setCodColloquio(codColloquio);
        	Integer codRicerca = xml.getAttribute("codRicerca", 0);
        	if (codRicerca == 0){
        		codRicerca = null;
        	}        	        	        	
        	a_xml.setCodRicerca(codRicerca);
        	a_xml.setFromValue(xml.getAttribute("fromValue", ""));
        	a_xml.setToValue(xml.getAttribute("toValue", ""));
        	a_xml.setGetterMethodName(xml.getAttribute("getterMethodName", ""));
        	a_xml.setLogicalOperator(xml.getAttribute("logicalOperator", ""));
        	a_xml.setLineNumber(xml.getAttribute("lineNumber", 0));
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
