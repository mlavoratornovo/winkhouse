package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AppuntamentiModel;
import winkhouse.vo.AppuntamentiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AppuntamentiXMLModel extends AppuntamentiModel 
								  implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 2;
	private String importOperation = null;
	
	public AppuntamentiXMLModel() {
		super();
	}

	public AppuntamentiXMLModel(AppuntamentiVO appuntamento) {
		super(appuntamento);
	}

	public AppuntamentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AppuntamentiXMLModel> APPUNTAMENTI_XML = new XMLFormat<AppuntamentiXMLModel>(AppuntamentiXMLModel.class){
		
        public void write(AppuntamentiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AppuntamentiXMLModel c_xml) throws XMLStreamException{

        	Integer codAppuntamento = xml.getAttribute("codAppuntamento", 0);
        	if (codAppuntamento == 0){
        		codAppuntamento = null;
        	}        	        	
        	c_xml.setCodAppuntamento(codAppuntamento);
        	Integer codPadre = xml.getAttribute("codPadre", 0);
        	if (codPadre == 0){
        		codPadre = null;
        	}        	        	        	
        	c_xml.setCodPadre(codPadre);
        	Integer codTipoAppuntamento = xml.getAttribute("codTipoAppuntamento", 0);
        	if (codTipoAppuntamento == 0){
        		codTipoAppuntamento = null;
        	}        	        	        	
        	c_xml.setCodTipoAppuntamento(codTipoAppuntamento);
        	c_xml.setDataInserimento(xml.getAttribute("dataAppuntamento", new Date()));
        	c_xml.setDataFineAppuntamento(xml.getAttribute("dataFineAppuntamento", new Date()));
        	c_xml.setDescrizione(xml.getAttribute("descrizione", ""));
        	c_xml.setLuogo(xml.getAttribute("luogo", ""));
        	c_xml.setiCalUID(xml.getAttribute("iCalUID", ""));
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
		return getCodAppuntamento();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
