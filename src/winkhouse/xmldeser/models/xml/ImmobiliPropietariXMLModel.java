package winkhouse.xmldeser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.export.helpers.AnagraficheHelper;
import winkhouse.export.helpers.ImmobiliHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.vo.ImmobiliPropietariVO;

public class ImmobiliPropietariXMLModel extends ImmobiliPropietariVO 
									    implements XMLSerializable{

	private AnagraficheModel anagrafica = null;
	private ImmobiliModel immobile = null;
	
	public ImmobiliPropietariXMLModel(){};
	
	public ImmobiliPropietariXMLModel(ImmobiliPropietariVO immobiliPropietariVO) {
		setCodAnagrafica(immobiliPropietariVO.getCodAnagrafica());
		setCodImmobile(immobiliPropietariVO.getCodImmobile());
	}

	public ImmobiliPropietariXMLModel(ResultSet rs) throws SQLException {
		super(rs);
		// TODO Auto-generated constructor stub
	}
	
	public AnagraficheModel getAnagrafica(){
		if (anagrafica == null){
			AnagraficheHelper ah = new AnagraficheHelper();
			anagrafica = ah.getAnagraficheByID(getCodAnagrafica());			
		}
		return anagrafica;
	}
	
	public ImmobiliModel getImmobile(){
		if (immobile == null){
			ImmobiliHelper ih = new ImmobiliHelper();
			immobile = ih.getImmobileById(getCodImmobile());			
		}
		return immobile;
	}	
	
	protected static final XMLFormat<ImmobiliPropietariXMLModel> IMMOBILIPROPIETARI_XML = new XMLFormat<ImmobiliPropietariXMLModel>(ImmobiliPropietariXMLModel.class){
		
        public void write(ImmobiliPropietariXMLModel afa_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAnagrafica", afa_xml.getCodAnagrafica());
        	xml.setAttribute("codImmobile", afa_xml.getCodImmobile());        	        	
        }
                
        public void read(InputElement xml, ImmobiliPropietariXMLModel c_xml) throws XMLStreamException{
        	Integer codAnagrafica = xml.getAttribute("codAnagrafica", 0);
        	if (codAnagrafica == 0){
        		codAnagrafica = null;
        	}        	        	        	
        	c_xml.setCodAnagrafica(codAnagrafica);
        	Integer codImmobile = xml.getAttribute("codImmobile", 0);
        	if (codImmobile == 0){
        		codImmobile = null;
        	}        	        	        	        	
        	c_xml.setCodImmobile(codImmobile);
       }
        
   };	

}
