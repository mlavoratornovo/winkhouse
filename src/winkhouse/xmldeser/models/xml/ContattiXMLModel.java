package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ContattiModel;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class ContattiXMLModel extends ContattiModel implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 2;
	private String importOperation = null;
	
	public ContattiXMLModel() {
		super();
	}

	public ContattiXMLModel(ContattiVO cVO) {
		super(cVO);
	}

	public ContattiXMLModel(ResultSet rs) throws SQLException {
		super(rs);	
	}

	protected static final XMLFormat<ContattiXMLModel> CONTATTI_XML = new XMLFormat<ContattiXMLModel>(ContattiXMLModel.class){
		
        public void write(ContattiXMLModel c_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, ContattiXMLModel c_xml) throws XMLStreamException{
        	Integer codContatto = xml.getAttribute("codContatto", 0);
        	if (codContatto == 0){
        		codContatto = null;
        	}        	        	
        	c_xml.setCodContatto(codContatto);
        	Integer codAgente = xml.getAttribute("codAgente", 0);
        	if (codAgente == 0){
        		codAgente = null;
        	}        	        	        	
        	c_xml.setCodAgente(codAgente);
        	Integer codAnagrafica = xml.getAttribute("codAnagrafica", 0);
        	if (codAnagrafica == 0){
        		codAnagrafica = null;
        	}        	        	        	
        	c_xml.setCodAnagrafica(codAnagrafica);
        	Integer codTipologiaContatto = xml.getAttribute("codTipologiaContatto", 0);
        	if (codTipologiaContatto == 0){
        		codTipologiaContatto = null;
        	}        	        	        	
        	c_xml.setCodTipologiaContatto(codTipologiaContatto);
        	c_xml.setDescrizione(xml.getAttribute("descrizione", ""));
        	c_xml.setContatto(xml.getAttribute("contatto", ""));
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
		return getCodContatto();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
