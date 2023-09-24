package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.TipiAppuntamentiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class TipiAppuntamentiXMLModel extends TipiAppuntamentiVO 
									  implements XMLSerializable, ObjectTypeCompare{

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 0;
	private String importOperation = null;
	
	public TipiAppuntamentiXMLModel() {
		super();
	}

	public TipiAppuntamentiXMLModel(TipiAppuntamentiVO taVO) {
		setCodTipoAppuntamento(taVO.getCodTipoAppuntamento());
		setDescrizione(taVO.getDescrizione());
		setgCalColor(taVO.getgCalColor());
		setOrdine(taVO.getOrdine());
	}

	public TipiAppuntamentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<TipiAppuntamentiXMLModel> TIPIAPPUNTAMENTI_XML = new XMLFormat<TipiAppuntamentiXMLModel>(TipiAppuntamentiXMLModel.class){
		
        public void write(TipiAppuntamentiXMLModel ta_xml, OutputElement xml)throws XMLStreamException {
        }
        
        public void read(InputElement xml, TipiAppuntamentiXMLModel ta_xml) throws XMLStreamException{
        	Integer codTipoAppuntamento = xml.getAttribute("codTipoAppuntamento", 0);
        	if (codTipoAppuntamento == 0){
        		codTipoAppuntamento = null;
        	}        	        	        	        	        	        	
        	ta_xml.setCodTipoAppuntamento(codTipoAppuntamento);
        	ta_xml.setDescrizione(xml.getAttribute("descrizione", ""));
        	ta_xml.setgCalColor(xml.getAttribute("gCalColor", ""));
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
		return getCodTipoAppuntamento();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
