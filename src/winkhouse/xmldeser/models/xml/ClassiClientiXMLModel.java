package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class ClassiClientiXMLModel extends ClassiClientiVO 
								   implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 0;
	private String importOperation = null;
	
	public ClassiClientiXMLModel() {
		super();	
	}

	public ClassiClientiXMLModel(ClassiClientiVO ccVO) {
		setCodClasseCliente(ccVO.getCodClasseCliente());
		setDescrizione(ccVO.getDescrizione());
		setOrdine(ccVO.getOrdine());
	}

	public ClassiClientiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<ClassiClientiXMLModel> CLASSICLIENTI_XML = new XMLFormat<ClassiClientiXMLModel>(ClassiClientiXMLModel.class){
		
        public void write(ClassiClientiXMLModel ta_xml, OutputElement xml)throws XMLStreamException {
        }
        
        public void read(InputElement xml, ClassiClientiXMLModel ta_xml) throws XMLStreamException{
        	Integer codClasseCliente = xml.getAttribute("codClasseCliente", 0);
        	if (codClasseCliente == 0){
        		codClasseCliente = null;
        	}        	        	
        	ta_xml.setCodClasseCliente(codClasseCliente);
        	ta_xml.setDescrizione(xml.getAttribute("descrizione", ""));
        	ta_xml.setOrdine(xml.getAttribute("ordine", 0));
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
		return getCodClasseCliente();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
