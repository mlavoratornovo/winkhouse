package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AffittiRateModel;
import winkhouse.vo.AffittiRateVO;
import winkhouse.xmldeser.utils.DateFormatUtils;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AffittiRateXMLModel extends AffittiRateModel 
								 implements XMLSerializable,ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 4;
	private String importOperation = null;
	
	public AffittiRateXMLModel() {
		super();
	}

	public AffittiRateXMLModel(AffittiRateVO arVO) {
		super(arVO);
	}

	public AffittiRateXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AffittiRateXMLModel> AFFITTIRATE_XML = new XMLFormat<AffittiRateXMLModel>(AffittiRateXMLModel.class){
		
        public void write(AffittiRateXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AffittiRateXMLModel a_xml) throws XMLStreamException{
        	
        	Integer codAffittiRate = xml.getAttribute("codAffittiRate",0);
        	if (codAffittiRate == 0){
        		codAffittiRate = null;
        	}
        	a_xml.setCodAffittiRate(codAffittiRate);
        	Integer codAffitto = xml.getAttribute("codAffitto",0);
        	if (codAffitto == 0){
        		codAffitto = null;
        	}        	
        	a_xml.setCodAffitto(codAffitto);
        	Integer codAnagrafica = xml.getAttribute("codAnagrafica",0);
        	if (codAnagrafica == 0){
        		codAnagrafica = null;
        	}        	        	
        	a_xml.setCodAnagrafica(codAnagrafica);
        	Integer codParent = xml.getAttribute("codParent",0);
        	if (codAnagrafica == 0){
        		codAnagrafica = null;
        	}        	        	        	
        	a_xml.setCodParent(codAnagrafica);
        	a_xml.setNota(xml.getAttribute("nota",""));
        	a_xml.setMese(xml.getAttribute("mese",0));

        	Date scadenza = null;
        	try {
        		scadenza = DateFormatUtils.getInstace().parse(xml.getAttribute("scadenza").toString());
			} catch (Exception e) {
				scadenza = new Date();
			}
        	a_xml.setScadenza(scadenza);

        	Date dataPagato = null;
        	try {
        		dataPagato = DateFormatUtils.getInstace().parse(xml.getAttribute("dataPagato").toString());
			} catch (Exception e) {
				dataPagato = new Date();
			}
        	a_xml.setDataPagato(dataPagato);
        	
        	a_xml.setImporto(xml.getAttribute("importo",0.0));
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
		return getCodAffittiRate();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}

	
	@Override
	public String toString() {
		return "codice affitto : " + this.getCodAffitto() + " codice anagrafica : " + this.getCodAnagrafica() + 
			   " importo : " + this.getImporto() + " mese : " + this.getNomeMese();
	}	

	
}
