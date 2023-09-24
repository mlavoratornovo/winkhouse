package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class RiscaldamentiXMLModel extends RiscaldamentiVO 
								   implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 0;
	private String importOperation = null;
	
	public RiscaldamentiXMLModel() {
		super();
	}

	public RiscaldamentiXMLModel(RiscaldamentiVO rVO) {
		setCodRiscaldamento(rVO.getCodRiscaldamento());
		setDescrizione(rVO.getDescrizione());
	}

	public RiscaldamentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<RiscaldamentiXMLModel> RISCALDAMENTI_XML = new XMLFormat<RiscaldamentiXMLModel>(RiscaldamentiXMLModel.class){
		
        public void write(RiscaldamentiXMLModel ta_xml, OutputElement xml)throws XMLStreamException {
        }
        
        public void read(InputElement xml, RiscaldamentiXMLModel ta_xml) throws XMLStreamException{
        	Integer codRiscaldamento = xml.getAttribute("codRiscaldamento", 0);
        	if (codRiscaldamento == 0){
        		codRiscaldamento = null;
        	}        	        	        	        	        	
        	ta_xml.setCodRiscaldamento(codRiscaldamento);
        	ta_xml.setDescrizione(xml.getAttribute("descrizione", ""));
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
		return getCodRiscaldamento();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
