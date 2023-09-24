package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.AffittiAllegatiVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AffittiAllegatiXMLModel extends AffittiAllegatiVO 
									 implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 4;
	private String importOperation = null;
	
	public AffittiAllegatiXMLModel() {
		super();
	}

	public AffittiAllegatiXMLModel(AffittiAllegatiVO aaVO) {
		setCodAffittiAllegati(aaVO.getCodAffittiAllegati());
		setCodAffitto(aaVO.getCodAffitto());
		setDescrizione(aaVO.getDescrizione());
		setNome(aaVO.getNome());
		setFromPath(aaVO.getFromPath());
	}

	public AffittiAllegatiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<AffittiAllegatiXMLModel> AFFITTIALLEGATI_XML = new XMLFormat<AffittiAllegatiXMLModel>(AffittiAllegatiXMLModel.class){
		
        public void write(AffittiAllegatiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AffittiAllegatiXMLModel a_xml) throws XMLStreamException{
        	
        	Integer codAffittiAllegati = xml.getAttribute("codAffittiAllegati", 0);
        	if (codAffittiAllegati == 0){
        		codAffittiAllegati = null;
        	}
        	a_xml.setCodAffittiAllegati(codAffittiAllegati);
        	
        	Integer CodAffitto = xml.getAttribute("CodAffitto",0);
        	if (CodAffitto == 0){
        		CodAffitto = null;
        	}        	
        	a_xml.setCodAffitto(CodAffitto);
        	
        	a_xml.setNome(xml.getAttribute("nome",""));
        	a_xml.setDescrizione(xml.getAttribute("descrizione",""));
        	a_xml.setFromPath(xml.getAttribute("fromPath",""));
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
		return getCodAffittiAllegati();
	}	
   
	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
