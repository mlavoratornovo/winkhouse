package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.RicercheModel;
import winkhouse.vo.RicercheVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class RicercheXMLModel extends RicercheModel implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 1;
	private String importOperation = null;
	
	public RicercheXMLModel() {
		super();
	}

	public RicercheXMLModel(RicercheVO rVO){
		setCodRicerca(rVO.getCodRicerca());
		setTipo(rVO.getTipo());
		setDescrizione(rVO.getDescrizione());
		setNome(rVO.getNome());		
	}

	public RicercheXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<RicercheXMLModel> RICERCHE_XML = new XMLFormat<RicercheXMLModel>(RicercheXMLModel.class){
		
        public void write(RicercheXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, RicercheXMLModel a_xml) throws XMLStreamException{
        	Integer codRicerca = xml.getAttribute("codRicerca", 0);
        	if (codRicerca == 0){
        		codRicerca = null;
        	}        	        	        	        	        	
        	a_xml.setCodRicerca(codRicerca);
        	a_xml.setNome(xml.getAttribute("nome", ""));
        	a_xml.setDescrizione(xml.getAttribute("descrizione", ""));
        	a_xml.setTipo(xml.getAttribute("tipo", 0));
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
		return getCodRicerca();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
