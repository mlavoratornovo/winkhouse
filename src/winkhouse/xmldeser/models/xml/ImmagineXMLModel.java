package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ImmagineModel;
import winkhouse.vo.ImmagineVO;
import winkhouse.xmldeser.utils.ObjectTypeCompare;

public class ImmagineXMLModel extends ImmagineModel implements XMLSerializable, ObjectTypeCompare {

	private boolean updateItem = false;
	protected final int TYPE_VALUE = 3;
	private String importOperation = null;
	
	public ImmagineXMLModel() {
		super();
	}

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
        }
                
        public void read(InputElement xml, ImmagineXMLModel i_xml) throws XMLStreamException{
        	Integer codImmobile = xml.getAttribute("codImmobile", 0);
        	if (codImmobile == 0){
        		codImmobile = null;
        	}        	        	
        	i_xml.setCodImmobile(codImmobile);
        	Integer codImmagine = xml.getAttribute("codImmagine", 0);
        	if (codImmagine == 0){
        		codImmagine = null;
        	}        	        	        	
        	i_xml.setCodImmagine(codImmagine);
        	i_xml.setPathImmagine(xml.getAttribute("pathImmagine", ""));
        	i_xml.setImgPropsStr(xml.getAttribute("imgPropsStr", ""));
        	i_xml.setOrdine(xml.getAttribute("ordine", 0));
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
		return getCodImmagine();
	}	

	public String getImportOperation() {
		return importOperation;
	}
	
	public void setImportOperation(String importOperation) {
		this.importOperation = importOperation;
	}	

}
