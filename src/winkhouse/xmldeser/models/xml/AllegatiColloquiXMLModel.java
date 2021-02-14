package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AllegatiColloquiModel;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AllegatiColloquiXMLModel extends AllegatiColloquiModel 
									  implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 4;
	private String importOperation = null;
	
	public AllegatiColloquiXMLModel() {
		super();	
	}

	public AllegatiColloquiXMLModel(AllegatiColloquiVO acVO) {
		setCodAllegatiColloquio(acVO.getCodAllegatiColloquio());
		setCodColloquio(acVO.getCodColloquio());
		setDescrizione(acVO.getDescrizione());
		setFromPath(acVO.getFromPath());
		setNome(acVO.getNome());
	}

	public AllegatiColloquiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<AllegatiColloquiXMLModel> ALLEGATICOLLOQUI_XML = new XMLFormat<AllegatiColloquiXMLModel>(AllegatiColloquiXMLModel.class){
		
        public void write(AllegatiColloquiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AllegatiColloquiXMLModel a_xml) throws XMLStreamException{
        	
        	Integer codAllegatiColloquio = xml.getAttribute("codAllegatiColloquio", 0);
        	if (codAllegatiColloquio == 0){
        		codAllegatiColloquio = null;	
        	}
        	a_xml.setCodAllegatiColloquio(codAllegatiColloquio);
        	Integer codColloquio = xml.getAttribute("codColloquio", 0);
        	if (codColloquio == 0){
        		codColloquio = null;	
        	}        	        	
        	a_xml.setCodColloquio(codColloquio);
        	a_xml.setDescrizione(xml.getAttribute("descrizione", ""));
        	a_xml.setFromPath(xml.getAttribute("fromPath", ""));
        	a_xml.setNome(xml.getAttribute("nome", ""));
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
		return getCodAllegatiColloquio();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	
	
}
