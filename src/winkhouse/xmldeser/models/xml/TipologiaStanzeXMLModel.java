package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class TipologiaStanzeXMLModel extends TipologiaStanzeVO 
									 implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 0;
	private String importOperation = null;
	
	public TipologiaStanzeXMLModel() {
		super();
	}

	public TipologiaStanzeXMLModel(TipologiaStanzeVO tsVO) {
		setCodTipologiaStanza(tsVO.getCodTipologiaStanza());
		setDescrizione(tsVO.getDescrizione());		
	}

	public TipologiaStanzeXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<TipologiaStanzeXMLModel> TIPISTANZE_XML = new XMLFormat<TipologiaStanzeXMLModel>(TipologiaStanzeXMLModel.class){
		
        public void write(TipologiaStanzeXMLModel ts_xml, OutputElement xml)throws XMLStreamException {
        }
        
        public void read(InputElement xml, TipologiaStanzeXMLModel ts_xml) throws XMLStreamException{
        	Integer codTipologiaStanza = xml.getAttribute("codTipologiaStanza", 0);
        	if (codTipologiaStanza == 0){
        		codTipologiaStanza = null;
        	}        	        	        	        	        	        	
        	ts_xml.setCodTipologiaStanza(codTipologiaStanza);
        	ts_xml.setDescrizione(xml.getAttribute("descrizione", ""));        	
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
		return getCodTipologiaStanza();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
