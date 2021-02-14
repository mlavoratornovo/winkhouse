package winkhouse.model.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ReportMarkersModel;

public class ReportMarkersXMLModel extends ReportMarkersModel 
								   implements XMLSerializable{

	public ReportMarkersXMLModel(){}
	
	public ReportMarkersXMLModel(ReportMarkersModel rmm) {
		super.setCodMarker(rmm.getCodMarker());
		super.setCodReport(rmm.getCodReport());
		super.setGetMethodName(rmm.getGetMethodName());
		super.setNome(rmm.getNome());
		super.setTipo(rmm.getTipo());
		super.setParams(rmm.getParams());
		super.setParamsDesc(rmm.getParamsDesc());
	}

	public ReportMarkersXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<ReportMarkersXMLModel> REPORTMARKERS_XML = new XMLFormat<ReportMarkersXMLModel>(ReportMarkersXMLModel.class) {
        public void write(ReportMarkersXMLModel reportMarker, OutputElement xml) {
        	try {
				xml.setAttribute("codMarker", reportMarker.getCodMarker());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	try {
				xml.setAttribute("codReport", reportMarker.getCodReport());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	try {
				xml.setAttribute("getMethodName", reportMarker.getGetMethodName());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	try {
				xml.setAttribute("nome", reportMarker.getNome());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	try {
				xml.setAttribute("tipo", reportMarker.getTipo());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	try {
				xml.setAttribute("params", reportMarker.getParams());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	try {
				xml.setAttribute("paramsDesc", reportMarker.getParamsDesc());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        }
        
        public void read(InputElement xml, ReportMarkersXMLModel reportMarker) {
        	try {
				reportMarker.setCodMarker(xml.getAttribute("codMarker", 0));
			} catch (XMLStreamException e) {
				e.printStackTrace();
				reportMarker.setCodMarker(0);
			}
        	try {
				reportMarker.setCodReport(xml.getAttribute("codReport", 0));
			} catch (XMLStreamException e) {
				e.printStackTrace();
				reportMarker.setCodReport(0);
			}
        	try {
				reportMarker.setGetMethodName(xml.getAttribute("getMethodName", ""));
			} catch (XMLStreamException e) {
				e.printStackTrace();
				reportMarker.setGetMethodName("");
			}
        	try {
				reportMarker.setNome(xml.getAttribute("nome", ""));
			} catch (XMLStreamException e) {
				e.printStackTrace();
				reportMarker.setNome("");
			}
        	try {
				reportMarker.setParams(xml.getAttribute("params", ""));
			} catch (XMLStreamException e) {
				e.printStackTrace();
				reportMarker.setParams("");
			}
        	try {
				reportMarker.setParamsDesc(xml.getAttribute("paramsDesc", ""));
			} catch (XMLStreamException e) {
				e.printStackTrace();
				reportMarker.setParamsDesc("");
			}
        	try {
				reportMarker.setTipo(xml.getAttribute("tipo", ""));
			} catch (XMLStreamException e) {
				e.printStackTrace();
				reportMarker.setTipo("");
			}			
       }
   };


}
