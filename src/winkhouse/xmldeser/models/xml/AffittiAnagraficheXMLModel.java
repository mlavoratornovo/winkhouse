package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AffittiAnagraficheModel;
import winkhouse.vo.AffittiAnagraficheVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AffittiAnagraficheXMLModel extends AffittiAnagraficheModel
										implements XMLSerializable,ObjectTypeCompare{

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 4;
	private String importOperation = null;
	
	public AffittiAnagraficheXMLModel() {
		super();
	}

	public AffittiAnagraficheXMLModel(AffittiAnagraficheVO aaVO) {
		setCodAffittiAnagrafiche(aaVO.getCodAffittiAnagrafiche());
		setCodAffitto(aaVO.getCodAffitto());
		setCodAnagrafica(aaVO.getCodAnagrafica());
		setNota(aaVO.getNota());
	}

	public AffittiAnagraficheXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<AffittiAnagraficheXMLModel> AFFITTIANAGRAFICHE_XML = new XMLFormat<AffittiAnagraficheXMLModel>(AffittiAnagraficheXMLModel.class){
		
        public void write(AffittiAnagraficheXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AffittiAnagraficheXMLModel a_xml) throws XMLStreamException{
        	
        	Integer codAffittiAnagrafiche = xml.getAttribute("codAffittiAnagrafiche",0);
        	if (codAffittiAnagrafiche == 0){
        		codAffittiAnagrafiche = null;
        	}
        	a_xml.setCodAffittiAnagrafiche(codAffittiAnagrafiche);
        	
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
        	
        	a_xml.setNota(xml.getAttribute("nota",""));
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
		return getCodAffittiAnagrafiche();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}

	
	@Override
	public String toString() {	
		return "codice affitto : " + getCodAffitto() + 
				" codice anagrafica : " + getCodAnagrafica();

	}	

}
