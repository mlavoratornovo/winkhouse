package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ColloquiModel;
import winkhouse.vo.ColloquiVO;
import winkhouse.xmldeser.utils.DateFormatUtils;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class ColloquiXMLModel extends ColloquiModel implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 3;
	private String importOperation = null;
	
	public ColloquiXMLModel() {
		super();
	}

	public ColloquiXMLModel(ColloquiVO cVO) {
		super(cVO);
	}

	public ColloquiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<ColloquiXMLModel> COLLOQUIO_XML = new XMLFormat<ColloquiXMLModel>(ColloquiXMLModel.class){
		
        public void write(ColloquiXMLModel c_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, ColloquiXMLModel c_xml) throws XMLStreamException{
        	
        	Integer codColloquio = xml.getAttribute("codColloquio",0);
        	if (codColloquio == 0){
        		codColloquio = null;
        	}        	        	        	        	
        	c_xml.setCodColloquio(codColloquio);
        	
        	c_xml.setScadenziere(xml.getAttribute("scadenziere", false));
        	
        	Integer codAgenteInseritore = xml.getAttribute("codAgenteInseritore", 0);
        	if (codAgenteInseritore == 0){
        		codAgenteInseritore = null;
        	}else{
        		c_xml.setAgenteInseritore(null);
        		if (c_xml.getAgenteInseritore() == null){
        			codAgenteInseritore = null;
        		}
        	}        	        	        	
        	c_xml.setCodAgenteInseritore(codAgenteInseritore);
        	
        	Integer codImmobileAbbinato = xml.getAttribute("codImmobileAbbinato", 0);
        	if (codImmobileAbbinato == 0){
        		codImmobileAbbinato = null;
        	}        	        	
        	c_xml.setCodImmobileAbbinato(codImmobileAbbinato);
        	
        	Integer codParent = xml.getAttribute("codParent", 0);
        	if (codParent == 0){
        		codParent = null;
        	}        	
        	c_xml.setCodParent(codParent);
        	Integer codTipologiaColloquio = xml.getAttribute("codTipologiaColloquio", 0);
        	if (codTipologiaColloquio == 0){
        		codTipologiaColloquio = null;
        	}
        	c_xml.setCodTipologiaColloquio(codTipologiaColloquio);
        	c_xml.setDescrizione(xml.getAttribute("descrizione", ""));
        	c_xml.setCommentoCliente(xml.getAttribute("commentoCliente", ""));
        	c_xml.setCommentoAgenzia(xml.getAttribute("commentoAgenzia", ""));
        	c_xml.setiCalUid(xml.getAttribute("iCalUid", ""));
        	c_xml.setLuogoIncontro(xml.getAttribute("luogoIncontro", ""));
        	
        	Date dataColloquio = null;
        	try {
        		dataColloquio = DateFormatUtils.getInstace().parse(xml.getAttribute("dataColloquio").toString());
			} catch (Exception e) {
				dataColloquio = new Date();
			}
        	c_xml.setDataColloquio(dataColloquio);
        	
        	Date dataInserimento = null;
        	try {
        		dataInserimento = DateFormatUtils.getInstace().parse(xml.getAttribute("dataInserimento").toString());
			} catch (Exception e) {
				dataInserimento = new Date();
			}
        	c_xml.setDataInserimento(dataInserimento);
        	
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
		return getCodColloquio();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
