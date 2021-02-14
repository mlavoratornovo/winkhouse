package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AffittiModel;
import winkhouse.vo.AffittiVO;
import winkhouse.xmldeser.utils.DateFormatUtils;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AffittiXMLModel extends AffittiModel implements XMLSerializable,ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 3;
	private String importOperation = null;
	
	public AffittiXMLModel() {
		super();
	}	

	public AffittiXMLModel(AffittiVO affitti) {
		super(affitti);
	}

	public AffittiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AffittiXMLModel> AFFITTI_XML = new XMLFormat<AffittiXMLModel>(AffittiXMLModel.class){
		
        public void write(AffittiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AffittiXMLModel c_xml) throws XMLStreamException{
        	Integer codAffitti = xml.getAttribute("codAffitti", 0);
        	if (codAffitti == null){
        		codAffitti = null;
        	}
        	c_xml.setCodAffitti(codAffitti);
        	Integer codImmobile = xml.getAttribute("codImmobile",0);
        	if (codImmobile == null){
        		codImmobile = null;
        	}        	
        	c_xml.setCodImmobile(codImmobile);
        	Integer codAgenteIns = xml.getAttribute("codAgenteIns",0);
        	if (codImmobile == null){
        		codImmobile = null;
        	}        	        	
        	c_xml.setCodAgenteIns(codAgenteIns);
        	c_xml.setDescrizione(xml.getAttribute("descrizione",""));
        	c_xml.setCauzione(xml.getAttribute("cauzione",0.0));
        	c_xml.setRata(xml.getAttribute("rata",0.0));
        	
        	Date dataInizio = null;
        	try {
        		dataInizio = DateFormatUtils.getInstace().parse(xml.getAttribute("dataInizio").toString());
			} catch (Exception e) {
				dataInizio = new Date();
			}

        	c_xml.setDataInizio(dataInizio);
        	
        	Date dataFine = null;
        	try {
        		dataFine = DateFormatUtils.getInstace().parse(xml.getAttribute("dataFine").toString());
			} catch (Exception e) {
				dataFine = new Date();
			}

        	c_xml.setDataFine(dataFine);
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
		return getCodAffitti();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
