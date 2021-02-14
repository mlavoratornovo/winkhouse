package winkhouse.model.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ReportMarkersModel;
import winkhouse.model.ReportModel;

public class ReportXMLModel extends ReportModel implements XMLSerializable {

	public ReportXMLModel(){}
	
	public ReportXMLModel(ReportModel rm) {
		super.setCodReport(rm.getCodReport());
		super.setDescrizione(rm.getDescrizione());
		super.setMarkers(rm.getMarkers());
		super.setNome(rm.getNome());
		super.setTemplate(rm.getTemplate());
		super.setTipo(rm.getTipo());
		super.setIsList(rm.getIsList());
	}

	public ReportXMLModel(ResultSet rs) throws SQLException {
		super(rs);		
	}

	protected static final XMLFormat<ReportXMLModel> REPORT_XML = new XMLFormat<ReportXMLModel>(ReportXMLModel.class) {
         public void write(ReportXMLModel report, OutputElement xml) {
        	 try {
				xml.setAttribute("codReport", report.getCodReport());
        	 } catch (XMLStreamException e) {
				e.printStackTrace();
        	 }
       	 	 try {
				xml.setAttribute("descrizione", report.getDescrizione());
			 } catch (XMLStreamException e) {
				e.printStackTrace();
			 }
			 try {
				xml.setAttribute("nome", report.getNome());
			 } catch (XMLStreamException e) {
				e.printStackTrace();
			 }
			 try {
				xml.setAttribute("template", report.getTemplate());
			 } catch (XMLStreamException e) {
				e.printStackTrace();
			 }
			 try {
				xml.setAttribute("tipo", report.getTipo());
			 } catch (XMLStreamException e) {
				e.printStackTrace();
			 }
			 try {
				xml.setAttribute("lista", report.getIsList());
			 } catch (XMLStreamException e) {
				e.printStackTrace();
			 }
			 
			 ArrayList al = report.getMarkers();
			 Iterator it = al.iterator();
			 FastList fl = new FastList();
			 while(it.hasNext()){
				 ReportMarkersXMLModel rmXMLModel = new ReportMarkersXMLModel((ReportMarkersModel)it.next());
				 fl.add(rmXMLModel);
			 }
			 try {			 
					xml.add(fl);
				 } catch (XMLStreamException e) {
					e.printStackTrace();
				 }
			 
			
         }
         public void read(InputElement xml, ReportXMLModel report) {
        	 try {
				report.setCodReport(xml.getAttribute("codReport", 0));
			} catch (XMLStreamException e) {
				report.setCodReport(0);
				e.printStackTrace();
			}
        	 try {
				report.setDescrizione(xml.getAttribute("descrizione", ""));
			} catch (XMLStreamException e) {
				report.setDescrizione("");
				e.printStackTrace();
			}
        	 try {
				report.setNome(xml.getAttribute("nome", ""));
			} catch (XMLStreamException e) {
				report.setNome("");
				e.printStackTrace();
			}
        	 try {
				report.setTemplate(xml.getAttribute("template", ""));
			} catch (XMLStreamException e) {
				report.setTemplate("");
				e.printStackTrace();
			}
        	 try {
				report.setTipo(xml.getAttribute("tipo", ""));
			} catch (XMLStreamException e) {
				report.setTipo("");
				e.printStackTrace();
			}

			try {
				report.setIsList(xml.getAttribute("lista", false));
			} catch (XMLStreamException e) {
				report.setTipo("");
				e.printStackTrace();
			}
			
        	 ArrayList markers = new ArrayList();
        	 FastList fl = null;
			try {
				fl = (FastList)xml.getNext();
			} catch (XMLStreamException e) {
				fl = new FastList();
				e.printStackTrace();
			}
        	 Iterator it = fl.iterator();
        	 while (it.hasNext()) {
        		 ReportMarkersXMLModel type = (ReportMarkersXMLModel) it.next();
        		 markers.add(type);
			 }
        	 report.setMarkers(markers);
        }
    };

}
