package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class StatoConservativoXMLModel extends StatoConservativoVO 
									   implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 0;
	private String importOperation = null;
	
	public StatoConservativoXMLModel() {
		super();
	}

	public StatoConservativoXMLModel(StatoConservativoVO scVO) {
		setCodStatoConservativo(scVO.getCodStatoConservativo());
		setDescrizione(scVO.getDescrizione());
	}

	public StatoConservativoXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<StatoConservativoXMLModel> STATOCONSERVATIVO_XML = new XMLFormat<StatoConservativoXMLModel>(StatoConservativoXMLModel.class){
		
        public void write(StatoConservativoXMLModel sc_xml, OutputElement xml)throws XMLStreamException {
        }
        
        public void read(InputElement xml, StatoConservativoXMLModel sc_xml) throws XMLStreamException{
        	Integer codStatoConservativo = xml.getAttribute("codStatoConservativo", 0);
        	if (codStatoConservativo == 0){
        		codStatoConservativo = null;
        	}        	        	        	        	        	        	
        	sc_xml.setCodStatoConservativo(codStatoConservativo);
        	sc_xml.setDescrizione(xml.getAttribute("descrizione", ""));
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
		return getCodStatoConservativo();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
