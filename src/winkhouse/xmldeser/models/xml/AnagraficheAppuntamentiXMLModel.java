package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AnagraficheAppuntamentiModel;
import winkhouse.vo.AnagraficheAppuntamentiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AnagraficheAppuntamentiXMLModel extends AnagraficheAppuntamentiModel 
											 implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 3;
	private String importOperation = null;
	
	public AnagraficheAppuntamentiXMLModel() {
		super();
	}

	public AnagraficheAppuntamentiXMLModel(AnagraficheAppuntamentiVO aaVO) {
		setCodAnagrafica(aaVO.getCodAnagrafica());
		setCodAnagraficheAppuntamenti(aaVO.getCodAnagraficheAppuntamenti());
		setCodAppuntamento(aaVO.getCodAppuntamento());
		setNote(aaVO.getNote());
	}

	public AnagraficheAppuntamentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AnagraficheAppuntamentiXMLModel> ANAGRAFICHEAPPUNTAMENTI_XML = new XMLFormat<AnagraficheAppuntamentiXMLModel>(AnagraficheAppuntamentiXMLModel.class){
		
        public void write(AnagraficheAppuntamentiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAnagraficheAppuntamenti", a_xml.getCodAnagraficheAppuntamenti());
			xml.setAttribute("codAppuntamento", a_xml.getCodAppuntamento());
			xml.setAttribute("note", a_xml.getNote());
			xml.setAttribute("codAnagrafica", a_xml.getCodAnagrafica());
        }
                
        public void read(InputElement xml, AnagraficheAppuntamentiXMLModel a_xml) throws XMLStreamException{
        	
        	Integer codAnagraficheAppuntamenti = xml.getAttribute("codAnagraficheAppuntamenti", 0);
        	if (codAnagraficheAppuntamenti == 0){
        		codAnagraficheAppuntamenti = null;
        	}
        	a_xml.setCodAnagraficheAppuntamenti(codAnagraficheAppuntamenti);
        	Integer codAppuntamento = xml.getAttribute("codAppuntamento", 0);
        	if (codAppuntamento == 0){
        		codAppuntamento = null;
        	}        	
        	a_xml.setCodAppuntamento(codAppuntamento);
        	a_xml.setNote(xml.getAttribute("note", ""));
        	a_xml.setCodAnagrafica(xml.getAttribute("codAppuntamento", 0));
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
		return getCodAnagraficheAppuntamenti();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
