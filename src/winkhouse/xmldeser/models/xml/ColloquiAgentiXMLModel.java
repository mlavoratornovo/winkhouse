package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.ColloquiAgentiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class ColloquiAgentiXMLModel extends ColloquiAgentiVO 
									implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 4;
	private String importOperation = null;
	
	public ColloquiAgentiXMLModel() {
		super();	
	}

	public ColloquiAgentiXMLModel(ColloquiAgentiVO caVO) {
		setCodAgente(caVO.getCodAgente());
		setCodColloquio(caVO.getCodColloquio());
		setCodColloquioAgenti(caVO.getCodColloquioAgenti());
		setCommento(caVO.getCommento());
	}

	public ColloquiAgentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<ColloquiAgentiXMLModel> COLLOQUIAGENTI_XML = new XMLFormat<ColloquiAgentiXMLModel>(ColloquiAgentiXMLModel.class){
		
        public void write(ColloquiAgentiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, ColloquiAgentiXMLModel a_xml) throws XMLStreamException{
        	Integer codAgente = xml.getAttribute("codAgente", 0);
        	if (codAgente == 0){
        		codAgente = null;
        	}        	        	
        	a_xml.setCodAgente(codAgente);
        	Integer codColloquio = xml.getAttribute("codColloquio", 0);
        	if (codColloquio == 0){
        		codColloquio = null;
        	}        	        	        	
        	a_xml.setCodColloquio(codColloquio);
        	Integer codColloquioAgenti = xml.getAttribute("codColloquioAgenti", 0);
        	if (codColloquioAgenti == 0){
        		codColloquioAgenti = null;
        	}        	        	        	
        	a_xml.setCodColloquioAgenti(codColloquioAgenti);
        	a_xml.setCommento(xml.getAttribute("commento", ""));
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
		return getCodColloquioAgenti();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
