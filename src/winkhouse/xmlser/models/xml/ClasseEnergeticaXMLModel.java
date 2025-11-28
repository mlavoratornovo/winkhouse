package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.orm.Classienergetiche;
import winkhouse.vo.ClasseEnergeticaVO;

public class ClasseEnergeticaXMLModel extends Classienergetiche 
									  implements XMLSerializable {
	
	public ClasseEnergeticaXMLModel(Classienergetiche ceVO) {
		//this.setCodClasseEnergetica(ceVO.getCodClasseEnergetica());
		this.setDescrizione(ceVO.getDescrizione());
		this.setOrdine(ceVO.getOrdine());
		this.setNome(ceVO.getNome());
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
