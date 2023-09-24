package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.StanzeImmobiliModel;
import winkhouse.vo.StanzeImmobiliVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class StanzeImmobiliXMLModel extends StanzeImmobiliModel 
									implements XMLSerializable, ObjectTypeCompare {


	private boolean updateItem = false;
	protected final int TYPE_VALUE = 3;
	private String importOperation = null;
	
	public StanzeImmobiliXMLModel() {
		super();
	}

	public StanzeImmobiliXMLModel(StanzeImmobiliVO siVO) {
		super(siVO);
	}

	public StanzeImmobiliXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<StanzeImmobiliXMLModel> STANZEIMMOBILI_XML = new XMLFormat<StanzeImmobiliXMLModel>(StanzeImmobiliXMLModel.class){
		
        public void write(StanzeImmobiliXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, StanzeImmobiliXMLModel a_xml) throws XMLStreamException{
        	Integer codStanzeImmobili = xml.getAttribute("codStanzeImmobili", 0);
        	if (codStanzeImmobili == 0){
        		codStanzeImmobili = null;
        	}        	        	        	        	        	
        	a_xml.setCodStanzeImmobili(codStanzeImmobili);
        	Integer codTipologiaStanza = xml.getAttribute("codTipologiaStanza", 0);
        	if (codTipologiaStanza == 0){
        		codTipologiaStanza = null;
        	}        	        	        	        	        	        	
        	a_xml.setCodTipologiaStanza(codTipologiaStanza);
        	Integer codImmobile = xml.getAttribute("codImmobile", 0);
        	if (codImmobile == 0){
        		codImmobile = null;
        	}        	        	        	        	        	        	
        	a_xml.setCodImmobile(codImmobile);
        	a_xml.setMq(xml.getAttribute("mq", 0));
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
		return getCodStanzeImmobili();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
