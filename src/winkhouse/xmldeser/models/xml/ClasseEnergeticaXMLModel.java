package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class ClasseEnergeticaXMLModel extends ClasseEnergeticaVO 
									  implements XMLSerializable, ObjectTypeCompare {

	
	private boolean updateItem = false;
	protected final int TYPE_VALUE = 0;
	private String importOperation = null;
	
	public ClasseEnergeticaXMLModel() {
		super();
	}

	public ClasseEnergeticaXMLModel(ClasseEnergeticaVO ceVO) {
		setCodClasseEnergetica(ceVO.getCodClasseEnergetica());
		setDescrizione(ceVO.getDescrizione());
		setNome(ceVO.getNome());
		setOrdine(ceVO.getOrdine());
	}

	public ClasseEnergeticaXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<ClasseEnergeticaXMLModel> CLASSEENERGETICA_XML = new XMLFormat<ClasseEnergeticaXMLModel>(ClasseEnergeticaXMLModel.class){
		
        public void write(ClasseEnergeticaXMLModel ce_xml, OutputElement xml)throws XMLStreamException {
        }
        
        public void read(InputElement xml, ClasseEnergeticaXMLModel ce_xml) throws XMLStreamException{
        	Integer codClasseEnergetica = xml.getAttribute("codClasseEnergetica", 0);
        	if (codClasseEnergetica == 0){
        		codClasseEnergetica = null;
        	}        	        	
        	ce_xml.setCodClasseEnergetica(xml.getAttribute("codClasseEnergetica", 0));
        	ce_xml.setDescrizione(xml.getAttribute("descrizione", ""));
        	ce_xml.setOrdine(xml.getAttribute("ordine", 0));
        	ce_xml.setNome(xml.getAttribute("nome", ""));
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
		return getCodClasseEnergetica();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
