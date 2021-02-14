package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ImmobiliModel;
import winkhouse.vo.ImmobiliVO;
import winkhouse.xmldeser.utils.DateFormatUtils;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class ImmobiliXMLModel extends ImmobiliModel 
							  implements XMLSerializable, ObjectTypeCompare {


	private boolean updateItem = false;
	protected final int TYPE_VALUE = 2;
	private String importOperation = null;
	
	public ImmobiliXMLModel() {
		super();
	}

	public ImmobiliXMLModel(ImmobiliVO iVO) {
		super(iVO);
	}

	public ImmobiliXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<ImmobiliXMLModel> IMMOBILI_XML = new XMLFormat<ImmobiliXMLModel>(ImmobiliXMLModel.class){
		
        public void write(ImmobiliXMLModel i_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, ImmobiliXMLModel i_xml) throws XMLStreamException{
        	Integer codImmobile = xml.getAttribute("codImmobile", 0);
        	if (codImmobile == 0){
        		codImmobile = null;
        	}        	        	        	
        	i_xml.setCodImmobile(codImmobile);
        	i_xml.setRif(xml.getAttribute("rif", ""));
        	i_xml.setAnnoCostruzione(xml.getAttribute("annoCostruzione", 0));
        	i_xml.setIndirizzo(xml.getAttribute("indirizzo", ""));
        	i_xml.setCitta(xml.getAttribute("citta", ""));
        	i_xml.setZona(xml.getAttribute("zona", ""));
        	i_xml.setProvincia(xml.getAttribute("provincia", ""));
        	i_xml.setCap(xml.getAttribute("cap", ""));
        	Integer codAnagrafica = xml.getAttribute("codAnagrafica", 0);
        	if (codAnagrafica == 0){
        		codAnagrafica = null;
        	}        	        	        	        	
        	i_xml.setCodAnagrafica(xml.getAttribute("codAnagrafica", 0));
        	i_xml.setStorico(xml.getAttribute("storico", false));
        	Integer codAgenteInseritore = xml.getAttribute("codAgenteInseritore", 0);
        	if (codAgenteInseritore == 0){
        		codAgenteInseritore = null;
        	}else{
        		i_xml.setAgenteInseritore(null);
        		if (i_xml.getAgenteInseritore() == null){
        			codAgenteInseritore = null;
        		}
        	}        	        	        	        	
        	i_xml.setCodAgenteInseritore(codAgenteInseritore);
        	Integer codTipologia = xml.getAttribute("codTipologia", 0);
        	if (codTipologia == 0){
        		codTipologia = null;
        	}        	        	        	        	
        	i_xml.setCodTipologia(codTipologia);
        	Integer codStato = xml.getAttribute("codStato", 0);
        	if (codStato == 0){
        		codStato = null;
        	}        	        	        	        	
        	i_xml.setCodStato(codStato);
        	Integer codRiscaldamento = xml.getAttribute("codRiscaldamento", 0);
        	if (codRiscaldamento == 0){
        		codRiscaldamento = null;
        	}        	        	        	        	
        	i_xml.setCodRiscaldamento(codRiscaldamento);
        	Integer codClasseEnergetica = xml.getAttribute("codClasseEnergetica", 0);
        	if (codClasseEnergetica == 0){
        		codClasseEnergetica = null;
        	}        	        	        	        	
        	i_xml.setCodClasseEnergetica(codClasseEnergetica);
        	
        	Date datainserimento = null;
        	try {
        		datainserimento = DateFormatUtils.getInstace().parse(xml.getAttribute("dataInserimento").toString());
			} catch (Exception e) {
				datainserimento = new Date();
			}
        	i_xml.setDataInserimento(datainserimento);       
        	i_xml.setPrezzo(xml.getAttribute("prezzo", 0.0));
        	i_xml.setMutuo(xml.getAttribute("mutuo", 0.0));
        	i_xml.setSpese(xml.getAttribute("spese", 0.0));
        	i_xml.setVarie(xml.getAttribute("varie", ""));
        	i_xml.setMutuoDescrizione(xml.getAttribute("mutuoDescrizione", ""));
        	i_xml.setMq(xml.getAttribute("mq", 0));
        	i_xml.setAffittabile(xml.getAttribute("affittabile", false));
        	i_xml.setVisione(xml.getAttribute("visione", false));
        	i_xml.setDescrizione(xml.getAttribute("descrizione", ""));
        	
        	Date dataLibero = null;
        	try {
        		dataLibero = DateFormatUtils.getInstace().parse(xml.getAttribute("dataLibero").toString());
			} catch (Exception e) {
				dataLibero = new Date();
			}
        	i_xml.setDataLibero(dataLibero);
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
		return getCodImmobile();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
