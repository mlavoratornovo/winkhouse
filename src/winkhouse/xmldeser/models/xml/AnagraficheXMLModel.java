package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AnagraficheModel;
import winkhouse.vo.AnagraficheVO;
import winkhouse.xmldeser.utils.DateFormatUtils;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AnagraficheXMLModel extends AnagraficheModel 
								 implements XMLSerializable, ObjectTypeCompare {


	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 1;
	private String importOperation = null;
	
	public AnagraficheXMLModel() {
		super();
	}

	public AnagraficheXMLModel(AnagraficheVO anagraficheVO) {
		super(anagraficheVO);
	}

	public AnagraficheXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AnagraficheXMLModel> ANAGRAFICHE_XML = new XMLFormat<AnagraficheXMLModel>(AnagraficheXMLModel.class){
		
        public void write(AnagraficheXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AnagraficheXMLModel a_xml) throws XMLStreamException{
        	
        	Integer codAnagrafica = xml.getAttribute("codAnagrafica", 0);
        	if (codAnagrafica == 0){
        		codAnagrafica = null;
        	}        	
        	a_xml.setCodAnagrafica(codAnagrafica);
        	a_xml.setCognome(xml.getAttribute("cognome", ""));
        	a_xml.setNome(xml.getAttribute("nome", ""));
        	a_xml.setRagioneSociale(xml.getAttribute("ragioneSociale", ""));
        	a_xml.setPartitaIva(xml.getAttribute("partitaIva", ""));
        	a_xml.setCodiceFiscale(xml.getAttribute("codiceFiscale", ""));
        	a_xml.setProvincia(xml.getAttribute("provincia", ""));
        	a_xml.setCap(xml.getAttribute("cap", ""));
        	a_xml.setCitta(xml.getAttribute("citta", ""));
        	a_xml.setIndirizzo(xml.getAttribute("indirizzo", ""));
        	a_xml.setCommento(xml.getAttribute("commento", ""));
        	a_xml.setStorico(xml.getAttribute("storico", false));
        	
        	Integer codAgenteInseritore = xml.getAttribute("codAgenteInseritore", 0);
        	if (codAgenteInseritore == 0){
        		codAgenteInseritore = null;
        	}        	        	
        	a_xml.setCodAgenteInseritore(xml.getAttribute("codAgenteInseritore", 0));
        	Integer codClasseCliente = xml.getAttribute("codClasseCliente", 0);
        	if (codClasseCliente == 0){
        		codClasseCliente = null;
        	}        	        	
        	a_xml.setCodClasseCliente(xml.getAttribute("codClasseCliente", 0));
        	
        	Date datainserimento = null;
        	try {
        		datainserimento = DateFormatUtils.getInstace().parse(xml.getAttribute("dataInserimento").toString());
			} catch (Exception e) {
				datainserimento = new Date();
			}
        	
        	a_xml.setDataInserimento(datainserimento);
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
		return getCodAnagrafica();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}

	
}
