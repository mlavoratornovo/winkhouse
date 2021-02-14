package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ImmagineModel;
import winkhouse.vo.ImmagineVO;

public class ImmagineXMLModel extends ImmagineModel implements XMLSerializable {

	public ImmagineXMLModel(ImmagineVO iVO) {
		setCodImmagine(iVO.getCodImmagine());
		setCodImmobile(iVO.getCodImmobile());
		setImgPropsStr(iVO.getImgPropsStr());
		setOrdine(iVO.getOrdine());
		setPathImmagine(iVO.getPathImmagine());
	}

	public ImmagineXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<ImmagineXMLModel> IMMAGINE_XML = new XMLFormat<ImmagineXMLModel>(ImmagineXMLModel.class){
		
        public void write(ImmagineXMLModel i_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codImmobile", i_xml.getCodImmobile());
        	xml.setAttribute("codImmagine", i_xml.getCodImmagine());
        	xml.setAttribute("pathImmagine", i_xml.getPathImmagine());
        	xml.setAttribute("imgPropsStr", i_xml.getImgPropsStr());
        	xml.setAttribute("ordine", i_xml.getOrdine());        	
        }
                
        public void read(InputElement xml, ImmagineXMLModel i_xml) throws XMLStreamException{
       }
        
   };	
}
