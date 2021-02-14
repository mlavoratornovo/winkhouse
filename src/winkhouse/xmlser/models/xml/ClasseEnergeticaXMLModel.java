package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.ClasseEnergeticaVO;

public class ClasseEnergeticaXMLModel extends ClasseEnergeticaVO 
									  implements XMLSerializable {

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
        	xml.setAttribute("codClasseEnergetica", ce_xml.getCodClasseEnergetica());
			xml.setAttribute("descrizione", ce_xml.getDescrizione());
			xml.setAttribute("ordine", ce_xml.getOrdine());        	
			xml.setAttribute("nome", ce_xml.getNome());
        }
        
        public void read(InputElement xml, ClasseEnergeticaXMLModel ce_xml) throws XMLStreamException{
       }
        
   };	

}
