package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;

import winkhouse.model.AbbinamentiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.vo.AbbinamentiVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class AbbinamentiXMLModel extends AbbinamentiModel 
								 implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 3;
	private String importOperation = null;
	
	public AbbinamentiXMLModel() {
		super();
	}

	public AbbinamentiXMLModel(AbbinamentiVO abbinamentiVO){
		setCodAbbinamento(abbinamentiVO.getCodAbbinamento());
		setCodAnagrafica(abbinamentiVO.getCodAnagrafica());
		setCodImmobile(abbinamentiVO.getCodImmobile());
	}
	
	public AbbinamentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AbbinamentiXMLModel> ABBINAMENTI_XML = new XMLFormat<AbbinamentiXMLModel>(AbbinamentiXMLModel.class){
		
        public void write(AbbinamentiXMLModel afa_xml, OutputElement xml)throws XMLStreamException {
        }
                
        public void read(InputElement xml, AbbinamentiXMLModel c_xml) throws XMLStreamException{
        	
        	Integer codAbbinamento = xml.getAttribute("codAbbinamento", 0);
        	if (codAbbinamento == 0){
        		codAbbinamento = null;
        	}
        	c_xml.setCodAbbinamento(codAbbinamento);
        	
        	Integer codAnagrafica = xml.getAttribute("codAnagrafica", 0);
        	if (codAnagrafica == 0){
        		codAnagrafica = null;
        	}        	
        	c_xml.setCodAnagrafica(codAnagrafica);
        	
        	Integer codImmobile = xml.getAttribute("codImmobile",0);
        	if (codImmobile == 0){
        		codImmobile = null;
        	}        	        	
        	c_xml.setCodImmobile(codImmobile);
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
		return getCodAbbinamento();
	}

	
	public String getImportOperation() {
		return importOperation;
	}
	

	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
