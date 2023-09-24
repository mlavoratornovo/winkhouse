package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.vo.ColloquiAnagraficheVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class ColloquiAnagraficheXMLModel extends ColloquiAnagraficheVO
										 implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 4;
	private String importOperation = null;
	
	public ColloquiAnagraficheXMLModel() {
		super();	
	}

	public ColloquiAnagraficheXMLModel(ColloquiAnagraficheModel_Ang caVO) {
		setCodAnagrafica(caVO.getCodAnagrafica());
		setCodColloquio(caVO.getCodColloquio());
		setCodColloquioAnagrafiche(caVO.getCodColloquioAnagrafiche());
		setCommento(caVO.getCommento());
	}

	public ColloquiAnagraficheXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<ColloquiAnagraficheXMLModel> COLLOQUIANAGRAFICHE_XML = new XMLFormat<ColloquiAnagraficheXMLModel>(ColloquiAnagraficheXMLModel.class){
		
        public void write(ColloquiAnagraficheXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, ColloquiAnagraficheXMLModel a_xml) throws XMLStreamException{
        	Integer codAnagrafica = xml.getAttribute("codAnagrafica", 0);
        	if (codAnagrafica == 0){
        		codAnagrafica = null;
        	}        	        	
        	a_xml.setCodAnagrafica(codAnagrafica);
        	Integer codColloquio = xml.getAttribute("codColloquio", 0);
        	if (codColloquio == 0){
        		codColloquio = null;
        	}        	        	        	
        	a_xml.setCodColloquio(codColloquio);
        	Integer codColloquioAnagrafiche = xml.getAttribute("codColloquioAnagrafiche", 0);
        	if (codColloquioAnagrafiche == 0){
        		codColloquioAnagrafiche = null;
        	}        	        	        	
        	a_xml.setCodColloquioAnagrafiche(codColloquioAnagrafiche);
        	a_xml.setCommento(xml.getAttribute("commento", ""));
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
		return getCodColloquioAnagrafiche();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

	@Override
	public String toString() {
		return "codice colloquio : " + getCodColloquio().toString() + 
			   " codice anagrafica : " + getCodAnagrafica().toString() + 
			   " commento : " + getCommento();
	}


}
