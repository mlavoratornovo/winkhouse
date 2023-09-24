package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.DatiCatastaliVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class DatiCatastaliXMLModel extends DatiCatastaliVO 
								   implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 3;
	private String importOperation = null;
	
	public DatiCatastaliXMLModel() {
		super();
	}

	public DatiCatastaliXMLModel(DatiCatastaliVO dcVO) {
		setCodDatiCatastali(dcVO.getCodDatiCatastali());
		setCodImmobile(dcVO.getCodImmobile());
		setCategoria(dcVO.getCategoria());
		setDimensione(dcVO.getDimensione());
		setFoglio(dcVO.getFoglio());
		setParticella(dcVO.getParticella());
		setRedditoAgricolo(dcVO.getRedditoAgricolo());
		setRedditoDomenicale(dcVO.getRedditoDomenicale());
		setRendita(dcVO.getRendita());
		setSubalterno(dcVO.getSubalterno());
	}

	public DatiCatastaliXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<DatiCatastaliXMLModel> DATICATASTALI_XML = new XMLFormat<DatiCatastaliXMLModel>(DatiCatastaliXMLModel.class){
		
        public void write(DatiCatastaliXMLModel dc_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, DatiCatastaliXMLModel dc_xml) throws XMLStreamException{
        	Integer codDatiCatastali = xml.getAttribute("codDatiCatastali", 0);
        	if (codDatiCatastali == 0){
        		codDatiCatastali = null;
        	}        	        	
        	dc_xml.setCodDatiCatastali(codDatiCatastali);
        	Integer codImmobile = xml.getAttribute("codImmobile", 0);
        	if (codImmobile == 0){
        		codImmobile = null;
        	}        	        	        	
        	dc_xml.setCodImmobile(codImmobile);
        	dc_xml.setCategoria(xml.getAttribute("categoria", ""));
        	dc_xml.setFoglio(xml.getAttribute("foglio", ""));
        	dc_xml.setParticella(xml.getAttribute("particella", ""));
        	dc_xml.setSubalterno(xml.getAttribute("subalterno", ""));
        	dc_xml.setDimensione(xml.getAttribute("dimensione", 0.0));
        	dc_xml.setRedditoAgricolo(xml.getAttribute("redditoAgricolo", 0.0));
        	dc_xml.setRedditoDomenicale(xml.getAttribute("redditoDomenicale", 0.0));
        	dc_xml.setRendita(xml.getAttribute("rendita", 0.0));
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
		return getCodDatiCatastali();
	}

	@Override
	public String toString() {
		return "foglio : " + super.getFoglio() + " categoria : " + super.getCategoria() + " particella :" + super.getParticella() +
				" subalterno : " + super.getSubalterno();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
