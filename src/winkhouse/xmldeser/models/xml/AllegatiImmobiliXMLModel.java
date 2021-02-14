package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AllegatiImmobiliXMLModel extends AllegatiImmobiliVO 
									  implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 3;
	private String importOperation = null;
	
	public AllegatiImmobiliXMLModel() {
		super();
	}

	public AllegatiImmobiliXMLModel(AllegatiImmobiliVO aiVO) {
		setCodAllegatiImmobili(aiVO.getCodAllegatiImmobili());
		setCodImmobile(aiVO.getCodImmobile());
		setCommento(aiVO.getCommento());
		setFromPath(aiVO.getFromPath());
		setNome(aiVO.getNome());
	}

	public AllegatiImmobiliXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AllegatiImmobiliXMLModel> ALLEGATIIMMOBILI_XML = new XMLFormat<AllegatiImmobiliXMLModel>(AllegatiImmobiliXMLModel.class){
		
        public void write(AllegatiImmobiliXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AllegatiImmobiliXMLModel a_xml) throws XMLStreamException{
        	
        	Integer codAllegatiImmobili = xml.getAttribute("codAllegatiImmobili", 0);
        	if (codAllegatiImmobili == 0){
        		codAllegatiImmobili = null;
        	}
        	a_xml.setCodAllegatiImmobili(codAllegatiImmobili);
        	Integer codImmobile = xml.getAttribute("codImmobile", 0);
        	if (codImmobile == 0){
        		codImmobile = null;
        	}        	
        	a_xml.setCodImmobile(xml.getAttribute("codImmobile", 0));
        	a_xml.setNome(xml.getAttribute("nome", ""));
        	a_xml.setCommento(xml.getAttribute("commento", ""));
        	a_xml.setFromPath(xml.getAttribute("fromPath", ""));
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
		return getCodAllegatiImmobili();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
