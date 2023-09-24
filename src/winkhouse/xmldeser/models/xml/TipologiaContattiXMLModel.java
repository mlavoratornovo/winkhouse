package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class TipologiaContattiXMLModel extends TipologiaContattiVO 
									   implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 0;
	private String importOperation = null;
	
	public TipologiaContattiXMLModel() {
		super();
	}

	public TipologiaContattiXMLModel(TipologiaContattiVO tcVO) {
		setCodTipologiaContatto(tcVO.getCodTipologiaContatto());
		setDescrizione(tcVO.getDescrizione());
	}

	public TipologiaContattiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<TipologiaContattiXMLModel> TIPOCONTATTI_XML = new XMLFormat<TipologiaContattiXMLModel>(TipologiaContattiXMLModel.class){
		
        public void write(TipologiaContattiXMLModel tc_xml, OutputElement xml)throws XMLStreamException {
        }
        
        public void read(InputElement xml, TipologiaContattiXMLModel tc_xml) throws XMLStreamException{
        	Integer codTipologiaContatto = xml.getAttribute("codTipologiaContatto", 0);
        	if (codTipologiaContatto == 0){
        		codTipologiaContatto = null;
        	}        	        	        	        	        	        	
        	tc_xml.setCodTipologiaContatto(codTipologiaContatto);
        	tc_xml.setDescrizione(xml.getAttribute("descrizione", ""));
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
		return getCodTipologiaContatto();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
