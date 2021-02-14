package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AffittiSpeseModel;
import winkhouse.vo.AffittiSpeseVO;
import winkhouse.xmldeser.utils.DateFormatUtils;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AffittiSpeseXMLModel extends AffittiSpeseModel 
								  implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 4;
	private String importOperation = null;
	
	public AffittiSpeseXMLModel() {
		super();
	}

	public AffittiSpeseXMLModel(AffittiSpeseVO asVO) {
		super(asVO);
	}

	public AffittiSpeseXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AffittiSpeseXMLModel> AFFITTISPESE_XML = new XMLFormat<AffittiSpeseXMLModel>(AffittiSpeseXMLModel.class){
		
        public void write(AffittiSpeseXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AffittiSpeseXMLModel a_xml) throws XMLStreamException{
        	
        	Integer codAffittiSpese = xml.getAttribute("codAffittiSpese",0);
        	if (codAffittiSpese == 0){
        		codAffittiSpese = null;
        	}
        	a_xml.setCodAffittiSpese(codAffittiSpese);
        	a_xml.setDescrizione(xml.getAttribute("descrizione",""));
        	Integer codAffitto = xml.getAttribute("codAffitto",0);
        	if (codAffitto == 0){
        		codAffitto = null;
        	}        	
        	a_xml.setCodAffitto(xml.getAttribute("codAffitto",0));
        	Integer codAnagrafica = xml.getAttribute("codAnagrafica",0);
        	if (codAnagrafica == 0){
        		codAnagrafica = null;
        	}        	
        	a_xml.setCodAnagrafica(xml.getAttribute("codAnagrafica",0));
        	Integer codParent = xml.getAttribute("codParent",0);
        	if (codParent == 0){
        		codParent = null;
        	}        	
        	a_xml.setCodParent(xml.getAttribute("codParent",0));

        	Date dataFine = null;
        	try {
        		dataFine = DateFormatUtils.getInstace().parse(xml.getAttribute("dataFine").toString());
			} catch (Exception e) {
				dataFine = new Date();
			}
        	a_xml.setDataFine(dataFine);
        	
        	Date dataInizio = null;
        	try {
        		dataInizio = DateFormatUtils.getInstace().parse(xml.getAttribute("dataInizio").toString());
			} catch (Exception e) {
				dataInizio = new Date();
			}
        	a_xml.setDataInizio(dataInizio);
        	
        	Date dataPagato = null;
        	try {
        		dataPagato = DateFormatUtils.getInstace().parse(xml.getAttribute("dataPagato").toString());
			} catch (Exception e) {
				dataPagato = new Date();
			}
        	a_xml.setDataPagato(dataPagato);
        	
        	Date scadenza = null;
        	try {
        		scadenza = DateFormatUtils.getInstace().parse(xml.getAttribute("scadenza").toString());
			} catch (Exception e) {
				dataPagato = new Date();
			}
        	a_xml.setScadenza(scadenza);
        	
        	a_xml.setImporto(xml.getAttribute("importo",0.0));
        	a_xml.setVersato(xml.getAttribute("versato",0.0));
        	
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
		return getCodAffittiSpese();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
