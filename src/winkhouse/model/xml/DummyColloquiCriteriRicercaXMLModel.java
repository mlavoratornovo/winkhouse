package winkhouse.model.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.vo.ColloquiCriteriRicercaVO;

public class DummyColloquiCriteriRicercaXMLModel extends ColloquiCriteriRicercaModel {
	
	private CriteriRicercaModel criteriRicercaModel = null;
	
	public DummyColloquiCriteriRicercaXMLModel() {
	}

	public DummyColloquiCriteriRicercaXMLModel(ColloquiCriteriRicercaVO ccrVO) {
		super(ccrVO);
	}

	public DummyColloquiCriteriRicercaXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<DummyColloquiCriteriRicercaXMLModel> COLLOQUICRITERIRICERCA_XML = new XMLFormat<DummyColloquiCriteriRicercaXMLModel>(DummyColloquiCriteriRicercaXMLModel.class){
		
        public void write(DummyColloquiCriteriRicercaXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codCriterioRicerca", a_xml.getCodCriterioRicerca());
			xml.setAttribute("codColloquio", a_xml.getCodColloquio());
			xml.setAttribute("codRicerca", a_xml.getCodRicerca());
			xml.setAttribute("fromValue", a_xml.getFromValue());
			xml.setAttribute("toValue", a_xml.getToValue());
			xml.setAttribute("getterMethodName", a_xml.getGetterMethodName());
			xml.setAttribute("logicalOperator", a_xml.getLogicalOperator());
			xml.setAttribute("lineNumber", a_xml.getLineNumber());
        }
                
        public void read(InputElement xml, DummyColloquiCriteriRicercaXMLModel a_xml) throws XMLStreamException{
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

   public CriteriRicercaModel toCriteriRicercaModel(){
	   
	   if (criteriRicercaModel == null){
		   criteriRicercaModel = new CriteriRicercaModel();
		   criteriRicercaModel.setFromValue(this.getFromValue());
		   criteriRicercaModel.setToValue(this.getToValue());
		   criteriRicercaModel.setLogicalOperator(this.getLogicalOperator());
		   criteriRicercaModel.setGetterMethodName(this.getGetterMethodName());
		   criteriRicercaModel.setLogicalOperator(this.getLogicalOperator());
		   criteriRicercaModel.setLineNumber(this.getLineNumber());
	   }
	   
	   return criteriRicercaModel;
	   
   }

}
