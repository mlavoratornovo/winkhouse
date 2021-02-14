package winkhouse.xmlser.models.xml;

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

public class AbbinamentiXMLModel extends AbbinamentiModel 
								 implements XMLSerializable {


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
        	xml.setAttribute("codAbbinamento", afa_xml.getCodAbbinamento());
        	xml.setAttribute("codAnagrafica", afa_xml.getCodAnagrafica());
        	xml.setAttribute("codImmobile", afa_xml.getCodImmobile());        	        	
        }
                
        public void read(InputElement xml, AbbinamentiXMLModel c_xml) throws XMLStreamException{
       }
        
   };	
	
}
