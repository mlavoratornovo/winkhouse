package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class TipologieImmobiliXMLModel extends TipologieImmobiliVO 
									   implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 0;
	private String importOperation = null;
	
	public TipologieImmobiliXMLModel() {
		super();
	}

	public TipologieImmobiliXMLModel(TipologieImmobiliVO tiVO) {
		setCodTipologiaImmobile(tiVO.getCodTipologiaImmobile());
		setDescrizione(tiVO.getDescrizione());
	}

	public TipologieImmobiliXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<TipologieImmobiliXMLModel> TIPIIMMOBILI_XML = new XMLFormat<TipologieImmobiliXMLModel>(TipologieImmobiliXMLModel.class){
		
        public void write(TipologieImmobiliXMLModel ti_xml, OutputElement xml)throws XMLStreamException {
        }
        
        public void read(InputElement xml, TipologieImmobiliXMLModel ti_xml) throws XMLStreamException{
        	Integer codTipologiaImmobile = xml.getAttribute("codTipologiaImmobile", 0);
        	if (codTipologiaImmobile == 0){
        		codTipologiaImmobile = null;
        	}        	        	        	        	        	        	
        	ti_xml.setCodTipologiaImmobile(codTipologiaImmobile);
        	ti_xml.setDescrizione(xml.getAttribute("descrizione", ""));        	
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
		return getCodTipologiaImmobile();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
