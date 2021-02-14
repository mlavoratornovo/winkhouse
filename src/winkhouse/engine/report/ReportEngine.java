package winkhouse.engine.report;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TableViewer;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.doc.draw.OdfDrawImage;
import org.odftoolkit.odfdom.doc.draw.OdfDrawTextBox;
import org.odftoolkit.odfdom.doc.office.OdfOfficeStyles;
import org.odftoolkit.odfdom.doc.office.OdfOfficeText;
import org.odftoolkit.odfdom.doc.style.OdfStyle;
import org.odftoolkit.odfdom.doc.style.OdfStyleChartProperties;
import org.odftoolkit.odfdom.doc.style.OdfStyleParagraphProperties;
import org.odftoolkit.odfdom.doc.style.OdfStyleTableCellProperties;
import org.odftoolkit.odfdom.doc.style.OdfStyleTableProperties;
import org.odftoolkit.odfdom.doc.style.OdfStyleTextProperties;
import org.odftoolkit.odfdom.doc.text.OdfTextParagraph;
import org.odftoolkit.odfdom.doc.text.OdfTextSpan;
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableColumnElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableRowElement;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.dom.style.props.OdfParagraphProperties;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import winkhouse.Activator;
import winkhouse.dao.AttributeDAO;
import winkhouse.dao.AttributeValueDAO;
import winkhouse.model.AffittiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.AttributeModel;
import winkhouse.model.AttributeValueModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.ReportModel;
import winkhouse.util.IEntityAttribute;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;
import winkhouse.vo.ImmagineVO;
import winkhouse.vo.ReportMarkersVO;



public class ReportEngine {

	public final static String MARKERELEMENT_TAGNAME = "text:placeholder";
	public final static String DOCUMENTBODY_TAGNAME = "office:body";
	public final static String MARKELEMENT_TAGTYPE = "text:placeholder-type";
	public final static String MARKELEMENT_TAGTYPENAME_TEXT = "text";
	public final static String MARKELEMENT_TAGTYPENAME_TABLE = "table";
	public final static String MARKELEMENT_TAGTYPENAME_TEXTBOX = "text-box";
	public final static String MARKELEMENT_TAGTYPENAME_IMAGE = "image";
	public final static String MARKELEMENT_TAGTYPENAME_OBJECT = "object";
	private static final String PAGEBREAK_STYLE_NAME = "nuovapagina";
	
	public final static String ERROR_IMAGE_FILENAME = "no-photo.jpg";
	private String pathImmagineErrore = Activator.getDefault()
												 .getStateLocation()
												 .toFile() + File.separator + ERROR_IMAGE_FILENAME;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterTime  = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	private SimpleDateFormat formatterENG  = new SimpleDateFormat("yyyy-MM-dd");
	
	public ReportEngine() {
		
	}
	
	public class MarkerLoader implements IRunnableWithProgress{

		private ArrayList<ReportMarkersVO> markers = null;
		private String reportPath = null;
		private ReportModel report = null;
		private TableViewer tvAssociazioni = null;
		
		public MarkerLoader(ArrayList<ReportMarkersVO> markers, 
							String reportPath,
							ReportModel report, 
							TableViewer tvAssociazioni){
			this.markers = markers;
			this.reportPath = reportPath;
			this.report = report;
			this.tvAssociazioni = tvAssociazioni;
		}
		
		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, 
														 InterruptedException {
			markers = getextractReportMarkers(reportPath);
        	report.setMarkers(markers);
        	tvAssociazioni.setInput(new Object());

		}
		
	}
	
	public ArrayList<ReportMarkersVO> getextractReportMarkers(String reportPath){
		
		ArrayList<ReportMarkersVO> returnValue = new ArrayList<ReportMarkersVO>();
		
		try {
			OdfDocument odfDocument = OdfTextDocument.loadDocument(reportPath);					
			NodeList nl = odfDocument.getContentDom().getElementsByTagName(MARKERELEMENT_TAGNAME);			
			for (int i = 0; i < nl.getLength(); i++) {
				ReportMarkersVO rmVO = new ReportMarkersVO();
				rmVO.setTipo(nl.item(i).getAttributes().getNamedItem(MARKELEMENT_TAGTYPE).getNodeValue());
				rmVO.setNome(nl.item(i).getTextContent());
				returnValue.add(rmVO);			
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnValue;
		
	}
	
	public Boolean doReport(ReportModel report, ArrayList elements, String destinationPath,int returnFileType){
		
		if (report.getIsList()){
			return createReportList(report, elements, destinationPath);
		}else{
			return createReport(report,elements,destinationPath,returnFileType);
		}
	}
	
	public Boolean createReportList(ReportModel report, ArrayList elements, String destinationPath){
		Boolean returnValue = true;
		
		try {
			OdfTextDocument odfdoc = OdfTextDocument.newTextDocument();
			String[] methodDescription = getMethodDescriptions(report.getMarkers().get(0));
			
			ArrayList params = getMarkerParametersValues(report.getMarkers().get(0).getParams());
						
			ArrayList objstoprint = new ArrayList();
			
			if (params.size() > 0){
				Iterator itParams = params.iterator();
				while (itParams.hasNext()){
					String param = (String)itParams.next();
					try {
						Integer.valueOf(param);
						if ((param != null) && (!param.equalsIgnoreCase(""))){
							objstoprint.add(elements.get(Integer.valueOf(param)-1));
						}
					} catch (NumberFormatException e) {
						objstoprint.add(filterImages(elements,param));
					}
				}				
			}else{
				objstoprint = elements;
			}

			Iterator itObjs = objstoprint.iterator();
			String[][] values = new String[objstoprint.size()][(methodDescription.length == 0)?1:methodDescription.length];
			String[] columnDescriptions = new String[methodDescription.length];
			int rows = 0;
			Class c;
			try {
				c = Class.forName(report.getTipo());
			} catch (Exception e) {
				c = null;
				e.printStackTrace();
			}

			if (c != null){
				for (int i = 0; i < methodDescription.length; i++) {
					ObjectSearchGetters osg = WinkhouseUtils.getInstance()
															  .findObjectSearchGettersByMethodName(methodDescription[i], 
																	  							   report.getTipo(), 
																		 						   WinkhouseUtils.FUNCTION_REPORT);
					columnDescriptions[i] = (osg != null)?osg.getDescrizione():"";
				}

				while (itObjs.hasNext()){
					
					Object o = itObjs.next();
					if (methodDescription.length == 0){					
						values[rows][0] = o.toString();
					}else{					
						for (int i = 0; i < methodDescription.length; i++) {
							values[rows][i] = String.valueOf(getMethodResult(o, c, methodDescription[i]));
						}
					}
					rows++;
					
				}
				
				tableMarker(odfdoc.getContentDom(), null, columnDescriptions, values);
				
			}else{
				tableMarker(odfdoc.getContentDom(), null, columnDescriptions, values);
			}
			
			odfdoc.save(new File(destinationPath));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	public Boolean createReport(ReportModel reportTemplate, ArrayList elements, String destinationPath,int returnFileType){
		
		Boolean returnValue = true;
		
		try {
			
			ArrayList<ReportMarkersVO> markers = reportTemplate.getMarkers();
			
			Iterator it = elements.iterator();
			int docCounter = 0;
			ArrayList<OdfDocument> alDocs = new  ArrayList<OdfDocument>();
			
			String templateReport = (WinkhouseUtils.getInstance()
													 .getPreferenceStore()
													 .getString(WinkhouseUtils.REPORTTEMPLATEPATH).equalsIgnoreCase(""))
									? WinkhouseUtils.getInstance()
											   		  .getPreferenceStore()
											   		  .getDefaultString(WinkhouseUtils.REPORTTEMPLATEPATH)
									: WinkhouseUtils.getInstance()
									   				  .getPreferenceStore()
									   				  .getString(WinkhouseUtils.REPORTTEMPLATEPATH);
			while (it.hasNext()){
				OdfDocument odfDocument = OdfTextDocument.loadDocument(templateReport + File.separator + 
																	   reportTemplate.getCodReport() + File.separator + 
																	   reportTemplate.getTemplate());
				
		//		OdfElement repoNode = odfDocument.getContentDom().getRootElement();
		//		OdfTextDocument odfpage = OdfTextDocument.newTextDocument();
				NodeList nl = odfDocument.getContentDom().getElementsByTagName(MARKERELEMENT_TAGNAME);
			
				Object methodResult = null;
				Object o = it.next();
				String pathPrefix = "";
				Iterator<ReportMarkersVO> itM = markers.iterator();
				
				while (itM.hasNext()){
					ReportMarkersVO rmVO = itM.next();
					ArrayList params = getMarkerParametersValues(rmVO.getParams());
					
					for (int i = 0; i < nl.getLength(); i++) {
						
						if (rmVO.getNome().equalsIgnoreCase(nl.item(i).getTextContent())){
							String classToPrint = null;							
							if (reportTemplate.getTipo().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
								methodResult = getMethodResult(o, ImmobiliModel.class, rmVO.getGetMethodName());
								classToPrint = ImmobiliModel.class.getName();
								pathPrefix = String.valueOf(((ImmobiliModel)o).getCodImmobile());
							}
							if (reportTemplate.getTipo().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
								methodResult = getMethodResult(o, AnagraficheModel.class, rmVO.getGetMethodName());
								classToPrint = AnagraficheModel.class.getName();
								pathPrefix = String.valueOf(((AnagraficheModel)o).getCodAnagrafica());
							}
							if (reportTemplate.getTipo().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
								methodResult = getMethodResult(o, ColloquiModel.class, rmVO.getGetMethodName());
								classToPrint = ColloquiModel.class.getName();
								pathPrefix = String.valueOf(((ColloquiModel)o).getCodColloquio());
							}
							if (reportTemplate.getTipo().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
								methodResult = getMethodResult(o, AppuntamentiModel.class, rmVO.getGetMethodName());
								classToPrint = AppuntamentiModel.class.getName();
								pathPrefix = String.valueOf(((AppuntamentiModel)o).getCodAppuntamento());
							}
							if (reportTemplate.getTipo().equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
								methodResult = getMethodResult(o, AffittiModel.class, rmVO.getGetMethodName());
								classToPrint = AffittiModel.class.getName();
								pathPrefix = String.valueOf(((AffittiModel)o).getCodAffitti());
							}
							
							List objstoprint = null;
							if (!(methodResult instanceof List)){
								methodResult = (methodResult == null)?"":methodResult;
								objstoprint = new ArrayList();								
								objstoprint.add(methodResult);
								if ((rmVO.getParamsDesc() != null) &&
									(!rmVO.getParamsDesc().equalsIgnoreCase(""))){									
									classToPrint = getClassNameObjList(reportTemplate.getTipo(), rmVO.getGetMethodName());
								}
							}else{
								objstoprint = new ArrayList();
								classToPrint = getClassNameObjList(reportTemplate.getTipo(), rmVO.getGetMethodName());
								if (params.size() == 0){
									objstoprint = (List)methodResult;
								}else{
									List results = (List)methodResult;
									if (results.size() > 0){										
										Iterator itParams = params.iterator();
										while (itParams.hasNext()){
											String param = (String)itParams.next();
											try {
												Integer.valueOf(param);
												if ((param != null) && (!param.equalsIgnoreCase(""))){
													objstoprint.add(results.get(Integer.valueOf(param)-1));
												}
											} catch (NumberFormatException e) {
												objstoprint = filterImages(results,param);
											}
										}
									}
								}
							} 
							
							odfPrintMarkerValues(nl.item(i), 
												 odfDocument.getContentDom(), 
												 objstoprint,
												 classToPrint,
												 getMethodDescriptions(rmVO), 
												 nl.item(i)
												   .getAttributes()
												   .getNamedItem(MARKELEMENT_TAGTYPE)
												   .getNodeValue(),
												   pathPrefix);
							
							
						}
						
					}
					
				}
				if ((elements.size() > 1) && 
						(returnFileType == WinkhouseUtils.ReportEngineRunner.STAMPA_UNICO_FILE)){
					alDocs.add(odfDocument);
				}else{
					odfDocument.save(destinationPath.substring(0,destinationPath.length() - 4) + 
									 docCounter + 
									 destinationPath.substring(destinationPath.length() - 4));
				}
				docCounter++;
			}		
			
			if ((elements.size() > 1) && 
				(returnFileType == WinkhouseUtils.ReportEngineRunner.STAMPA_UNICO_FILE)){
				
/*				for (int j = 0; j < elements.size(); j++) {
					alDocs.add(OdfTextDocument.loadDocument(destinationPath.substring(0,destinationPath.length() - 4) + 
							 								j + 
							 								destinationPath.substring(destinationPath.length() - 4)));
				}					
	*/			
				OdfDocument result = mergeDocuments(alDocs);
				result.save(destinationPath);				
				alDocs.clear();
				alDocs = null;
				System.gc();																	
				
			}
						
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = false;
			
		}finally{
			System.gc();
			WinkhouseUtils.getInstance()
							.tmpDirectoryDeleter(WinkhouseUtils.getInstance()
												 			     .getPreferenceStore()
												 			     .getString(WinkhouseUtils.REPORTTEMPLATEPATH)+
												 File.separator + 
												 reportTemplate.getCodReport()
												);

		}		
		
		return returnValue;
	}
	
	private ArrayList filterImages(List objsToPrint, String param){
		
		ArrayList returnValue = new ArrayList();
		Iterator it = objsToPrint.iterator();
		
		while (it.hasNext()){
			
			Object o = it.next();
		
			if (o instanceof ImmagineVO){
				if (((ImmagineVO)o).getImgPropsStr().contains(param)){
					returnValue.add(o);
				}
			}
			
		}
		
		return returnValue;
		
	}
	
	protected void odfPrintMarkerValues(Node markerOdf,
										OdfFileDom fileDom,
										List objsToPrint,
										String typeObjsToPrint,
										String[] methodDescription,
										String typeMarker,
										String pathPrefix){
		
		
		if (typeMarker.equalsIgnoreCase(MARKELEMENT_TAGTYPENAME_TEXT)){
			
			String toPrint = "";
			Iterator itObjs = objsToPrint.iterator();
			while (itObjs.hasNext()){
				Object o = itObjs.next();
				if (methodDescription.length == 0){
					if ((o instanceof Date) || (o instanceof Timestamp)){
						Calendar cal = Calendar.getInstance(Locale.ITALY);
						cal.setTime((Date)o);
						if ((cal.get(Calendar.HOUR_OF_DAY) == 0) &&
						    (cal.get(Calendar.MINUTE) == 0)){	
							toPrint += formatter.format(o) + " ";
						}else{
							toPrint += formatterTime.format(o) + " ";
						}
					}else{
						toPrint += o.toString() + " ";
					}
					
				}else{
					try {
						Class c = Class.forName(typeObjsToPrint);					
						for (int i = 0; i < methodDescription.length; i ++){
							Object objDesc = getMethodResult(o, c, methodDescription[i]);
							if (objDesc != null){
								if (objDesc instanceof ArrayList){
									toPrint += defaultPrintList((ArrayList)objDesc);
								}else{
									if ((objDesc instanceof Date) || (objDesc instanceof Timestamp)){
										Calendar cal = Calendar.getInstance(Locale.ITALY);
										cal.setTime((Date)o);
										if ((cal.get(Calendar.HOUR_OF_DAY) == 0) &&
										    (cal.get(Calendar.MINUTE) == 0)){	
											toPrint += formatter.format(objDesc) + " ";
										}else{
											toPrint += formatterTime.format(objDesc) + " ";
										}
									}else{
										toPrint += objDesc.toString() + " ";
									}
								}
							}
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			textMarker(fileDom, markerOdf, toPrint);
		}else if (typeMarker.equalsIgnoreCase(MARKELEMENT_TAGTYPENAME_IMAGE)){
			String[] imagesPath = new String[objsToPrint.size()];
			Iterator itObjs = objsToPrint.iterator();
			int count = 0;
			while (itObjs.hasNext()){
				Object o = itObjs.next();
				if (methodDescription.length == 0){
					imagesPath[count] = WinkhouseUtils.getInstance()
													    .getPreferenceStore()
													    .getString(WinkhouseUtils.IMAGEPATH) +
										File.separator + 
										pathPrefix + 
										File.separator + o.toString(); 
				}else{
					try {
						Class c = Class.forName(typeObjsToPrint);											
						imagesPath[count] = WinkhouseUtils.getInstance()
															.getPreferenceStore()
															.getString(WinkhouseUtils.IMAGEPATH) +
											File.separator + 
											pathPrefix + 
											File.separator + 
											(String)getMethodResult(o, c, methodDescription[0]);		
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				count++;
			}

			imageMarker(fileDom,markerOdf,imagesPath);
			
		}else if (typeMarker.equalsIgnoreCase(MARKELEMENT_TAGTYPENAME_TABLE)){
			
			Iterator itObjs = objsToPrint.iterator();
			String[][] values = new String[objsToPrint.size()][(methodDescription.length == 0)?1:methodDescription.length];
			String[] columnDescriptions = new String[methodDescription.length];
			int rows = 0;
			Class c;
			try {
				c = Class.forName(typeObjsToPrint);
			} catch (Exception e) {
				c = null;
				e.printStackTrace();
			}

			if (c != null){
				for (int i = 0; i < methodDescription.length; i++) {
					ObjectSearchGetters osg = WinkhouseUtils.getInstance()
															  .findObjectSearchGettersByMethodName(methodDescription[i], 
																		 						   typeObjsToPrint, 
																		 						   WinkhouseUtils.FUNCTION_REPORT);
					columnDescriptions[i] = (osg != null)?osg.getDescrizione():"";
				}

				while (itObjs.hasNext()){
					
					Object o = itObjs.next();
					if (methodDescription.length == 0){
						if ((o instanceof Date) || (o instanceof Timestamp)){
							Calendar cal = Calendar.getInstance(Locale.ITALY);
							cal.setTime((Date)o);
							if ((cal.get(Calendar.HOUR_OF_DAY) == 0) &&
							    (cal.get(Calendar.MINUTE) == 0)){	
								values[rows][0] = formatter.format(o) + " ";
							}else{
								values[rows][0] = formatterTime.format(o) + " ";
							}
						}else{
							values[rows][0] = o.toString();
						}
					}else{					
						for (int i = 0; i < methodDescription.length; i++) {
							Object obj = getMethodResult(o, c, methodDescription[i]);
							if ((obj instanceof Date) || (obj instanceof Timestamp)){
								Calendar cal = Calendar.getInstance(Locale.ITALY);
								cal.setTime((Date)obj);
								if ((cal.get(Calendar.HOUR_OF_DAY) == 0) &&
								    (cal.get(Calendar.MINUTE) == 0)){	
									values[rows][i] = formatter.format(obj) + " ";
								}else{
									values[rows][i] = formatterTime.format(obj) + " ";
								}
							}else{
								values[rows][i] = String.valueOf(obj);
							}
						}
					}
					rows++;
					
				}
				
				tableMarker(fileDom, markerOdf, columnDescriptions, values);
				
			}else{
				tableMarker(fileDom, markerOdf, columnDescriptions, values);
			}
		}else{
			String toPrint = "";
			Iterator itObjs = objsToPrint.iterator();
			while (itObjs.hasNext()){
				Object o = itObjs.next();
				if (methodDescription.length == 0){
					if ((o instanceof Date) || (o instanceof Timestamp)){
						Calendar cal = Calendar.getInstance(Locale.ITALY);
						cal.setTime((Date)o);
						if ((cal.get(Calendar.HOUR_OF_DAY) == 0) &&
						    (cal.get(Calendar.MINUTE) == 0)){	
							toPrint += formatter.format(o) + " ";
						}else{
							toPrint += formatterTime.format(o) + " ";
						}
					}else{
						toPrint += o.toString() + " ";
					}
					 
				}else{
					try {
						Class c = Class.forName(typeObjsToPrint);					
						for (int i = 0; i < methodDescription.length; i ++){
							Object obj = getMethodResult(o, c, methodDescription[i]);
							if ((obj instanceof Date) || (obj instanceof Timestamp)){
								Calendar cal = Calendar.getInstance(Locale.ITALY);
								cal.setTime((Date)obj);
								if ((cal.get(Calendar.HOUR_OF_DAY) == 0) &&
								    (cal.get(Calendar.MINUTE) == 0)){	
									toPrint += formatter.format(obj) + " ";
								}else{
									toPrint += formatterTime.format(obj) + " ";
								}
							}else{
								toPrint += obj.toString() + " ";
							}
							
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			textBoxMarker(fileDom, markerOdf, toPrint);
		}
		
	}
	
	private void textMarker(OdfFileDom odfDom, 
							Node markerNode, 
							String substitution){
		
		try {
			OdfTextSpan odfTS = new OdfTextSpan(odfDom);
			//OdfTextParagraph odfTN = new OdfTextParagraph(odfDom);
			if (markerNode.getParentNode() instanceof OdfTextParagraph){
				odfTS.setStyleName(((OdfTextParagraph)markerNode.getParentNode()).getStyleName());
			}
			odfTS.setTextContent(substitution);
	/*		System.out.println(markerNode.toString());
			markerNode.getParentNode()
					  .setNodeValue(markerNode.getParentNode()
							  				  .getNodeValue()
							  				  .replace(markerNode.toString(), substitution));*/
//			markerNode.getParentNode().getParentNode().replaceChild(odfTN, markerNode.getParentNode());			
			markerNode.getParentNode().replaceChild(odfTS, markerNode);
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
		
	private void imageMarker(OdfFileDom odfDom, Node markerimage, String[] pathImage){
		
		TableTableElement tte = new TableTableElement(odfDom);
		tte.setProperty(OdfStyleTableProperties.BorderModel, "collapsing");
		TableTableColumnElement ttce = new TableTableColumnElement(odfDom);					
		ttce.setTableNumberColumnsRepeatedAttribute(1);
		tte.appendChild(ttce);
				
		for (int x = 0;x < pathImage.length; x++){
			
			TableTableRowElement ttre = new TableTableRowElement(odfDom);
			
				TableTableCellElement ttcel = new TableTableCellElement(odfDom);
				//ttcel.setProperty(OdfStyleTableCellProperties.Border, "0.002cm solid #000000");
				OdfTextParagraph odfTN = new OdfTextParagraph(odfDom);			
				odfTN.setProperty (OdfStyleTextProperties.FontSize, "10pt");
				odfTN.setProperty (OdfStyleTextProperties.FontFamily, "Arial");
				odfTN.setProperty (OdfStyleParagraphProperties.TextAlign, "center"); 
				odfTN.setProperty (OdfStyleChartProperties.DataLabelNumber, "value");
				
				DrawFrameElement frame = odfTN.newDrawFrameElement();
				OdfDrawImage image = (OdfDrawImage) frame.newDrawImageElement();
				try {							
					image.newImage(new File(pathImage[x]).toURI());
				} catch (URISyntaxException e) {
					try {
						//controllare composizione url
						image.newImage(new File(pathImmagineErrore).toURI());
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} catch (Exception e) {
					try {
						//controllare composizione url
						image.newImage(new File(pathImmagineErrore).toURI());
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}				
				
				ttcel.appendChild(odfTN);
				ttre.appendChild(ttcel);
								
			tte.appendChild(ttre);
			
		}
		
		markerimage.getParentNode().getParentNode().replaceChild(tte, markerimage.getParentNode());		
		
	/*
		Node parent = markerimage.getParentNode();
		parent.removeChild(markerimage);					
		for (int i = 0;i < pathImage.length; i++){
			DrawFrameElement frame = ((OdfTextParagraph)parent).newDrawFrameElement();
			OdfDrawImage image = (OdfDrawImage) frame.newDrawImageElement();
			try {							
				image.newImage(new File(pathImage[i]).toURI());
			} catch (URISyntaxException e) {
				try {
					//controllare composizione url
					image.newImage(new File(pathImmagineErrore).toURI());
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {
				try {
					//controllare composizione url
					image.newImage(new File(pathImmagineErrore).toURI());
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}		
		*/
	}
	
	private Double castODFDimensionToDouble(String value){
		Double returnValue = null;		
		value = value.substring(0, value.length() - 2);
		returnValue = Double.valueOf(value);		
		return returnValue;
		
	}

	private void tableMarker(OdfFileDom odfDom, Node marker, String[] columnDescription, String[][] values){
		
		TableTableElement tte = new TableTableElement(odfDom);
		tte.setProperty(OdfStyleTableProperties.BorderModel, "collapsing");
		TableTableColumnElement ttce = new TableTableColumnElement(odfDom);					
		ttce.setTableNumberColumnsRepeatedAttribute((columnDescription.length == 0)?1:columnDescription.length);
		tte.appendChild(ttce);
		
		TableTableRowElement ttreHeader = new TableTableRowElement(odfDom);					
		for (int i = 0; i < columnDescription.length; i++ ){
			TableTableCellElement ttcel = new TableTableCellElement(odfDom);
			ttcel.setProperty(OdfStyleTableCellProperties.Border, "0.002cm solid #000000");
			OdfTextParagraph odfTN = new OdfTextParagraph(odfDom);			
			odfTN.setProperty (OdfStyleTextProperties.FontSize, "10pt");
			odfTN.setProperty (OdfStyleTextProperties.FontWeight, "bold");
			odfTN.setProperty (OdfStyleTextProperties.FontFamily, "Arial");
			odfTN.setProperty (OdfStyleParagraphProperties.TextAlign, "center"); 
			odfTN.setProperty (OdfStyleChartProperties.DataLabelNumber, "value");
			odfTN.setTextContent(columnDescription[i]);
			ttcel.appendChild(odfTN);
			ttreHeader.appendChild(ttcel);
		}
				
		tte.appendChild(ttreHeader);
		
		for (int x = 0;x < values.length; x++){
			
			TableTableRowElement ttre = new TableTableRowElement(odfDom);
			for (int y = 0; y < values[0].length; y++){
			
				TableTableCellElement ttcel = new TableTableCellElement(odfDom);
				ttcel.setProperty(OdfStyleTableCellProperties.Border, "0.002cm solid #000000");
				OdfTextParagraph odfTN = new OdfTextParagraph(odfDom);			
				odfTN.setProperty (OdfStyleTextProperties.FontSize, "10pt");
				odfTN.setProperty (OdfStyleTextProperties.FontFamily, "Arial");
				odfTN.setProperty (OdfStyleParagraphProperties.TextAlign, "center"); 
				odfTN.setProperty (OdfStyleChartProperties.DataLabelNumber, "value");
				odfTN.setTextContent(values[x][y]);
				ttcel.appendChild(odfTN);
				ttre.appendChild(ttcel);
								
			}
			tte.appendChild(ttre);
			
		}
		if (marker != null){
			marker.getParentNode().getParentNode().replaceChild(tte, marker.getParentNode());
		}else{
			/*odfDom.insertBefore(tte,
						  		odfDom.getRootElement()
						  			  .getLastChild()
						  			  .getLastChild()
						  			  );*/
			
			odfDom.getElementsByTagName(DOCUMENTBODY_TAGNAME)
				  .item(0)
				  .getLastChild()
				  .insertBefore(tte,
						  		odfDom.getElementsByTagName(DOCUMENTBODY_TAGNAME)
						  			  .item(0)
						  			  .getLastChild()
						  			  .getLastChild());
			
		}
		
	}
	
	private void textBoxMarker(OdfFileDom odfDom, Node marker,String substitution){
		Node parent = marker.getParentNode();
		parent.removeChild(marker);					
		DrawFrameElement frame = ((OdfTextParagraph)parent).newDrawFrameElement();					
		OdfDrawTextBox image = (OdfDrawTextBox) frame.newDrawTextBoxElement();
		image.setFoMinWidthAttribute("17cm");
		OdfTextParagraph odfTN2 = new OdfTextParagraph(odfDom);
		odfTN2.setTextContent(substitution);
		image.appendChild(odfTN2);										

	}
	
	private Object getMethodResult(Object instance, Class c, String methodName){
		Object returnValue = null;
		try {
			Method m = c.getMethod(methodName,null);
			returnValue = m.invoke(instance, null);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			returnValue = getMethodResultPersonal(instance, methodName);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	private Object getMethodResultPersonal(Object instance, String methodName){
		
		Object returnValue = null;
		
		if (instance instanceof IEntityAttribute){
			
			AttributeDAO aDAO = new AttributeDAO();
			AttributeModel attributeModel = aDAO.getAttributeByID(Integer.valueOf(methodName));
			AttributeValueDAO avDAO = new AttributeValueDAO();
			
			AttributeValueModel avm = avDAO.getAttributeValueByIdAttributeIdObject(attributeModel.getIdAttribute(),
														 						  ((IEntityAttribute)instance).getIdInstanceObject());
			if (avm != null){
				if (attributeModel.getFieldType().equalsIgnoreCase(Integer.class.getName())){
					try {
						returnValue = Integer.valueOf(avm.getFieldValue());
					} catch (NumberFormatException e) {
						returnValue = 0;
					}
				}else if (attributeModel.getFieldType().equalsIgnoreCase(Double.class.getName())){
					
					if (avm.getFieldValue().indexOf('.') > 0){
						try {
							returnValue = Double.valueOf(avm.getFieldValue());
						} catch (NumberFormatException e) {
							returnValue = 0.0;
						}
					}else{
						try {
							returnValue = Double.valueOf(avm.getFieldValue()+".0");
						} catch (NumberFormatException e) {
							returnValue = 0.0;
						}
						
					}		
				}else if (attributeModel.getFieldType().equalsIgnoreCase(Date.class.getName())){
					
					try {
						returnValue = formatterENG.parse(avm.getFieldValue());
					} catch (ParseException e) {
						returnValue = null;							
					} 
					
				}else{
					returnValue = avm.getFieldValue();
				}
					
			}
						
		}
		
		return returnValue;
		
	} 
	
	private String[] getMethodDescriptions(ReportMarkersVO rm){
		
		String [] returnValue = new String[0];
		
		if ((rm.getParamsDesc() != null) && 
			(!rm.getParamsDesc().equalsIgnoreCase(""))){
			
			returnValue = rm.getParamsDesc().split(",");
			
		}
		
		return returnValue;
	} 
	
	private ArrayList getMarkerParametersValues(String params){
		ArrayList returnValue = new ArrayList();
		
		String[] intervals = params.split(",");
		for (int i = 0; i < intervals.length; i++) {
			
			if (intervals[i].contains("..")){				
				returnValue.add(getBeginEndRange(intervals[i]));
			}else if (intervals[i].indexOf(".") > 1){
				returnValue.add(getExactRange(intervals[i]));
			}else{
				//returnValue.add(intervals[i].substring(1,intervals[i].length()-1));
				if (!intervals[i].equalsIgnoreCase("")){
					returnValue.add(intervals[i]);
				}				
			}
		}
				
		return returnValue;
	}

	private int[] getBeginEndRange(String param){
		
		int begin = Integer.valueOf(param.substring(1, param.indexOf("..")));
		int end = Integer.valueOf(param.substring(param.indexOf("..") + 2, param.indexOf("]")));
		int[] range = null;
		if (begin > end){
			range = new int[end-begin];
			int y = 0;
			for (int x = begin; x <= end; x++){
				range[y] = x;
				y++;						
			}
			
		}else if (begin < end){
			range = new int[end-begin];
			int y = 0;
			for (int x = end; x >= begin; x--){
				range[y] = x;
				y++;						
			}			
		}else{
			range = new int[1];
			range[0] = begin;
		}
		return range;
	}
	
	private int[] getExactRange(String param){
		int[] range = null;
		param = param.substring(1,param.length()-1);
		String[] strRange = param.split(".");
		range = new int[strRange.length];
		for (int i = 0; i < strRange.length; i++) {
			range[i] = Integer.valueOf(strRange[i]).intValue();			
		}
		
		return range;
	}
	
	private String getClassNameObjList(String entity, String methodName){
		String returnValue = null;
		ObjectSearchGetters osg = WinkhouseUtils.getInstance()
												  .findObjectSearchGettersByMethodName(
														  methodName, 
														  entity, 
														  WinkhouseUtils.FUNCTION_REPORT
														  							   );
		
		try {
			returnValue = osg.getParametrizedTypeName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
			returnValue = ImmobiliModel.class.getName();
		}else if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
			returnValue = AnagraficheModel.class.getName();
		}else if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
			returnValue = ColloquiModel.class.getName();
		}else if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.AGENTI)){
			returnValue = AgentiModel.class.getName();
		}else if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.AGENTICOLLOQUIO)){
			returnValue = ColloquiAgentiModel_Age.class.getName();
		}else if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.ALLEGATICOLLOQUIO)){
			returnValue = AllegatiColloquiVO.class.getName();
		}else if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHECOLLOQUIO)){
			returnValue = ColloquiAnagraficheModel_Ang.class.getName();
		}else if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.CONTATTI)){
			returnValue = ContattiModel.class.getName();
		}else if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.IMMAGINI)){
			returnValue = ImmagineModel.class.getName();
		}else if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.STANZEIMMOBILI)){
			returnValue = StanzeImmobiliModel.class.getName();
		}else if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.CRITERIRICERCA)){
			returnValue = ColloquiCriteriRicercaVO.class.getName();
		}
		*/
		return returnValue;
	}
	
	private static OdfDocument mergeDocuments(List<OdfDocument> odfDocuments) throws DOMException, Exception {
		OdfDocument odfDoc = null;
		OdfOfficeText odfText = null;
		OdfTextParagraph odfPara = null;

		// Make a copy of the Document List, we don't want to change the original one!
		List<OdfDocument> odfDocumentsCopy = new ArrayList<OdfDocument>(odfDocuments);

		if (!odfDocumentsCopy.isEmpty()) {
			odfDoc = odfDocumentsCopy.get(0);
			odfDocumentsCopy.remove(0);
	
			addPagebreakStyle(odfDoc);
	
			odfText = OdfElement.findFirstChildNode(OdfOfficeText.class, odfDoc.getOfficeBody());
	
			if (!(odfText.getLastChild() instanceof OdfTextParagraph)) {
				odfText.appendChild(new OdfTextParagraph(odfDoc.getContentDom()));
			}
	
			odfPara = (OdfTextParagraph) odfText.getLastChild();
			odfPara.setStyleName(PAGEBREAK_STYLE_NAME);
		}

		for (OdfDocument doc : odfDocumentsCopy) {
			odfText = OdfElement.findFirstChildNode(OdfOfficeText.class,doc.getOfficeBody());
	
			if (!(odfText.getLastChild() instanceof OdfTextParagraph)) {
				odfText.appendChild(new OdfTextParagraph(odfDoc.getContentDom()));
			}
	
			odfPara = (OdfTextParagraph) odfText.getLastChild();
			odfPara.setStyleName(PAGEBREAK_STYLE_NAME);
	
			odfDoc.getOfficeBody().appendChild(odfDoc.getContentDom().importNode(odfText, true));
		}

		return odfDoc;
	}
	
	private static void addPagebreakStyle(OdfDocument odfDoc) {
		
		OdfOfficeStyles styles = odfDoc.getDocumentStyles();

		OdfStyle styleElement = styles.newStyle(PAGEBREAK_STYLE_NAME, OdfStyleFamily.Paragraph);
		styleElement.setAttribute("style:family", "paragraph");
		styleElement.setAttribute("style:parent-style-name", "Standard");
		
		styleElement.setProperty(OdfParagraphProperties.BreakAfter, "page");
/*		OdfParagraphPropertiesElement paraElement = styleElement.createParagraphPropertiesElement();
		paraElement.setAttribute("fo:break-after", "page");
		styleElement.appendChild(paraElement);*/

		styles.appendChild(styleElement);
	}	
	
	private String defaultPrintList(ArrayList al){
		String returnValue = "";
		Iterator it = al.iterator();
		while (it.hasNext()){
			returnValue += it.next().toString() + " ";
		}
		return returnValue;
	}

}
