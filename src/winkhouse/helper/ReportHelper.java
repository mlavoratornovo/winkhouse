package winkhouse.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.ReportDAO;
import winkhouse.dao.ReportMarkersDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.model.ReportModel;
import winkhouse.model.xml.ReportMarkersXMLModel;
import winkhouse.model.xml.ReportXMLModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.ReportMarkersVO;



public class ReportHelper {

	private String pathTemplateReport = WinkhouseUtils.getInstance()
	  													.getPreferenceStore()
	  													.getString(WinkhouseUtils.REPORTTEMPLATEPATH);

	public ReportHelper(){}
	
	private Comparator<ReportMarkersVO> comparerMarkers = new Comparator<ReportMarkersVO>(){

		@Override
		public int compare(ReportMarkersVO arg0, ReportMarkersVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodMarker()!=null) && (arg1.getCodMarker()!=null)){				
				if ((arg0.getCodMarker().intValue() == arg1.getCodMarker().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodMarker().intValue() < arg1.getCodMarker().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodMarker().intValue() > arg1.getCodMarker().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodMarker()!=null) && (arg1.getCodMarker()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		
	
	public boolean saveReport(ReportModel rm){
		boolean returnValue = true;
		
		ReportDAO rDAO = new ReportDAO();
		ReportMarkersDAO rmDAO = new ReportMarkersDAO();
		String tmpPath = null;
		if (rm != null){
			Connection con = ConnectionManager.getInstance()
											  .getConnection();
			if (rm.getTemplate().indexOf(File.separator) > 0){
				tmpPath = rm.getTemplate();
				rm.setTemplate(rm.getTemplate().substring(rm.getTemplate()
															.lastIndexOf(File.separator) + 1,
														  rm.getTemplate().length()));
			}
			
			rm.setDateUpdate(new Date());
			if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
				rm.setCodUserUpdate(WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
			}

			if (rDAO.saveUpdate(rm, con, false)){
				
				if (updateListaMarkers(rm,con)){
						
						if (tmpPath != null){
							
							if (WinkhouseUtils.getInstance()
								 		 	    .copiaFile(tmpPath,
								 		 	    		   pathTemplateReport + File.separator + 
								 		 	    		   rm.getCodReport() + File.separator + 
								 		 	    		   rm.getTemplate())){

							}else{
								returnValue = false;
							}
						}

				}else{
					returnValue = false;
					try {
						con.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else{
				returnValue = false;
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
			
			if (returnValue){
				try {
					con.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
			
			
		}else{
			returnValue = false;
		}
		
			
		return returnValue;
	}
	
	public boolean updateListaMarkers(ReportModel report,
			   						  Connection con){

		boolean returnValue = true;

		ArrayList reportMarkers = new ReportMarkersDAO().getMarkersByReport(report.getCodReport()); 
		Collections.sort(reportMarkers,comparerMarkers);
		ArrayList listaReportMarkers = report.getMarkers();
		if (listaReportMarkers != null){

			ReportMarkersDAO rmDAO = new ReportMarkersDAO();			
			Iterator it  = listaReportMarkers.iterator();

			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					ReportMarkersVO reportMarker = (ReportMarkersVO)o;
					int index = Collections.binarySearch(reportMarkers, reportMarker,comparerMarkers);
					if (index >= 0){
						reportMarkers.remove(index);
						Collections.sort(reportMarkers,comparerMarkers);
					}
					reportMarker.setCodReport(report.getCodReport());
									
					if (!rmDAO.saveUpdate(reportMarker, con, false)){					
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
												"Errore salvataggio marker", 
												"Si è verificato un errore nel salvataggio del marker : " + 
												reportMarker.getCodMarker());

					}					
				}
			}

			Iterator ite = reportMarkers.iterator();
			while (ite.hasNext()){
				ReportMarkersVO reportMarker = (ReportMarkersVO)ite.next();
				if (!rmDAO.deleteReportMarkersByID(reportMarker.getCodMarker(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione marker", 
											"Si è verificato un errore nella cancellazione del marker : " + 
											reportMarker.getCodMarker());

				}
			}
		}

		return returnValue;
	}

	public boolean deleteReport(ReportModel report){
		
		boolean returnValue = true;
		
		ReportDAO rDAO = new ReportDAO();
		ReportMarkersDAO rmDAO = new ReportMarkersDAO();
		Connection con = ConnectionManager.getInstance().getConnection();
		
		
		if (rmDAO.deleteReportMarkersByReport(report.getCodReport(), con, false)){
			if (rDAO.deleteReport(report.getCodReport(), con, false)){
				if (!report.getIsList()){
					File f = new File(pathTemplateReport + File.separator +
							          report.getCodReport() + File.separator + 
							          report.getTemplate());
					if (f.exists()){
						if (!f.delete()){
							returnValue = false;
						}
					}else{
						returnValue = true;
					}
				}
			}				
		}
		if (returnValue){
			try {
				con.commit();
				con.close();
			} catch (SQLException e) {
				returnValue = false;
				e.printStackTrace();
			}
		}else{
			try {
				con.rollback();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return returnValue;
	}
	

	
	public void doReport(ArrayList objsToPrint, ReportModel report){
		
		FileDialog fd = new FileDialog(PlatformUI.getWorkbench()
				 								 .getActiveWorkbenchWindow()
				 								 .getShell(), SWT.SAVE);
		String[] extensions = new String[]{"*.odt"};
		String[] extensionNames = new String[]{"odt"};
		fd.setFilterExtensions(extensions);
		fd.setFilterNames(extensionNames);
		String path = fd.open();
		//String extension = extensionNames[fd.getFilterIndex()];
		if ((path != null) && (!path.equalsIgnoreCase(""))){
			int result = 1;
			if (objsToPrint.size() > 1){
				MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench()
						 										   .getActiveWorkbenchWindow()
						 										   .getShell(), 
														 "unico file / più file", 
														 null,
														 "Desideri il risultato su un unico file o un file per ogni elemento ?", 
														 MessageDialog.QUESTION, 
														 new String[] {"Unico file", "Un file per ogni elemento"}, 0);
				result = dialog.open();
				
			}

			WinkhouseUtils.ReportEngineRunner fmuReportEngineRunner = WinkhouseUtils.getInstance()
																						.new ReportEngineRunner(objsToPrint, report, path, result);
			ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
																			.getActiveWorkbenchWindow()
																			.getShell());
			
			try {
				pmd.run(false, true, fmuReportEngineRunner);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}else{
			MessageDialog.openError(PlatformUI.getWorkbench()
											  .getActiveWorkbenchWindow()
											  .getShell(), 
									"Errore di stampa",
									"Inserire il file di output per stampa");
		}
		
	}
	
	public void doReportList(ArrayList objsToPrint, ReportModel report){
		
		FileDialog fd = new FileDialog(PlatformUI.getWorkbench()
				 								 .getActiveWorkbenchWindow()
				 								 .getShell(), SWT.SAVE);
		String[] extensions = new String[]{"*.odt"};
		String[] extensionNames = new String[]{"odt"};
		fd.setFilterExtensions(extensions);
		fd.setFilterNames(extensionNames);
		String path = fd.open();
		//String extension = extensionNames[fd.getFilterIndex()];
		if ((path != null) && (!path.equalsIgnoreCase(""))){
			int result = 1;

			WinkhouseUtils.ReportEngineRunner fmuReportEngineRunner = WinkhouseUtils.getInstance().new ReportEngineRunner(objsToPrint, report, path, result);
			ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
																			.getActiveWorkbenchWindow()
																			.getShell());
			
			try {
				pmd.run(true, true, fmuReportEngineRunner);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}else{
			MessageDialog.openError(PlatformUI.getWorkbench()
											  .getActiveWorkbenchWindow()
											  .getShell(), 
									"Errore di stampa",
									"Si è verificato un errore durante la stampa");
		}
		
	}
	
	public void exportReport(ReportModel rm){
		
		byte[] buf = new byte[1024];
		
		System.gc();
		FileDialog fd = new FileDialog(PlatformUI.getWorkbench()
				 								 .getActiveWorkbenchWindow()
				 								 .getShell(), SWT.SAVE);
		
		String[] extensions = new String[]{"*.exr"};
		String[] extensionNames = new String[]{"exr"};
		fd.setFilterExtensions(extensions);
		fd.setFilterNames(extensionNames);
		
		String path = fd.open();
		
		if ((path != null) && (!path.equalsIgnoreCase(""))){
		
			ReportXMLModel rXML = new ReportXMLModel(rm);
			
			String pathwork = Activator.getDefault()
			 		 				   .getStateLocation()
			 		 				   .toFile() + File.separator + "tmpExport";
			
			File fpw = new File(pathwork);
			if (fpw.exists()){
				String[] filenames = fpw.list();
				for (int i = 0; i < filenames.length; i++) {
					System.out.println(new File(pathwork + File.separator + filenames[i]).delete());
				}
			}
			
			OutputStream out = null;
			try {
				File f = new File(pathwork);
				f.mkdirs();
				f = new File(pathwork + File.separator + "data.xml");
				out = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (out != null){
			    XMLObjectWriter writer = null;
			    boolean okWrite = true;
				try {
					writer = new XMLObjectWriter().setOutput(out);
				} catch (XMLStreamException e) {				
					e.printStackTrace();
					okWrite = false;
				}
			    try {
					writer.write(rXML);
				} catch (XMLStreamException e) {
					e.printStackTrace();
					okWrite = false;
				}
			    try {
					writer.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
					okWrite = false;
				}
				if (okWrite){
					if (!rXML.getIsList()){ 
						String templatePath = (WinkhouseUtils.getInstance()
															   .getPreferenceStore()
															   .getString(WinkhouseUtils.REPORTTEMPLATEPATH)
															   .equalsIgnoreCase(""))
											  ? WinkhouseUtils.getInstance()
													  			.getPreferenceStore()
													  			.getDefaultString(WinkhouseUtils.REPORTTEMPLATEPATH)
								   			  : WinkhouseUtils.getInstance()
								   			  					.getPreferenceStore()
								   			  					.getString(WinkhouseUtils.REPORTTEMPLATEPATH);
						if (WinkhouseUtils.getInstance()
										    .copiaFile(templatePath + 
													   File.separator + 
													   rm.getCodReport() +
													   File.separator +
													   rm.getTemplate(),
													   pathwork + 
													   File.separator +
													   rm.getTemplate())){
							
							ZipOutputStream zipout = null;
							try {
								zipout = new ZipOutputStream(new FileOutputStream(path));
							} catch (FileNotFoundException e) {
	
								e.printStackTrace();
							}
							if (zipout != null){
								FileInputStream in = null;
								try {
									in = new FileInputStream(pathwork + File.separator + "data.xml");
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
						        try {
									zipout.putNextEntry (new ZipEntry(pathwork + File.separator + "data.xml"));
								} catch (IOException e) {
									e.printStackTrace();
								}
		
						        // Transfer bytes from the file to the ZIP file
						        int len;
						        try {
									while ((len = in.read(buf)) > 0) {
										zipout.write(buf, 0, len);
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
		
						        // Complete the entry
						        try {
									zipout.closeEntry();
								} catch (IOException e) {
									e.printStackTrace();
								}
						        try {
									in.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
								
								FileInputStream in2 = null;
								try {
									in2 = new FileInputStream(pathwork + File.separator + rm.getTemplate());
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
						        try {
									zipout.putNextEntry (new ZipEntry(pathwork + File.separator + rm.getTemplate()));
								} catch (IOException e) {
									e.printStackTrace();
								}
		
						        // Transfer bytes from the file to the ZIP file
						        int len2;
						        try {
									while ((len2 = in2.read(buf)) > 0) {
										zipout.write(buf, 0, len2);
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
		
						        // Complete the entry
						        try {
									zipout.closeEntry();
								} catch (IOException e) {
									
									e.printStackTrace();
								}
						        try {
									in2.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
								
								try {
									zipout.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
	
							}
							
						}
						
					}else{
						
						ZipOutputStream zipout = null;
						try {
							zipout = new ZipOutputStream(new FileOutputStream(path));
						} catch (FileNotFoundException e) {

							e.printStackTrace();
						}
						if (zipout != null){
							FileInputStream in = null;
							try {
								in = new FileInputStream(pathwork + File.separator + "data.xml");
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
					        try {
								zipout.putNextEntry (new ZipEntry(pathwork + File.separator + "data.xml"));
							} catch (IOException e) {
								e.printStackTrace();
							}
	
					        // Transfer bytes from the file to the ZIP file
					        int len;
					        try {
								while ((len = in.read(buf)) > 0) {
									zipout.write(buf, 0, len);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
	
					        // Complete the entry
					        try {
								zipout.closeEntry();
							} catch (IOException e) {
								e.printStackTrace();
							}
					        try {
								in.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
														
							try {
								zipout.close();
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
						
					}
											
				}
				
			}
			
		}		
		
	}
	
	public void importReport(){
		byte[] buf = new byte[1024];
		System.gc();
		FileDialog fd = new FileDialog(PlatformUI.getWorkbench()
				 								 .getActiveWorkbenchWindow()
				 								 .getShell(), SWT.OPEN);
		
		String[] extensions = new String[]{"*.exr"};
		String[] extensionNames = new String[]{"exr"};
		fd.setFilterExtensions(extensions);
		fd.setFilterNames(extensionNames);
		
		String path = fd.open();
		
		if ((path != null) && (!path.equalsIgnoreCase(""))){

			String pathwork = Activator.getDefault()
			   						   .getStateLocation()
			   						   .toFile() + File.separator + "tmpExport";

			File fpw = new File(pathwork);
			if (fpw.exists()){
				String[] filenames = fpw.list();
				for (int i = 0; i < filenames.length; i++) {
					System.out.println(new File(pathwork + File.separator + filenames[i]).delete());
				}
			}else{
				fpw.mkdirs();
			}
			
			

			ZipFile zipFile = null;
			try {
				zipFile = new ZipFile(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (zipFile != null){
				Enumeration entries = zipFile.entries();
		
			    while(entries.hasMoreElements()) {
			    	ZipEntry entry = (ZipEntry)entries.nextElement();
			    	try {
			    		if (entry.getName().lastIndexOf(File.separator) == -1){
							copyInputStream(zipFile.getInputStream(entry),
					          		  		new BufferedOutputStream(new FileOutputStream(pathwork + 
					          				  											  File.separator + 
					          				  											  entry.getName())));			    			
			    		}else{
							copyInputStream(zipFile.getInputStream(entry),
							          		  new BufferedOutputStream(new FileOutputStream(pathwork + 
							          				  										File.separator + 
							          				  										entry.getName()
							          				  											 .substring(entry.getName().lastIndexOf(File.separator)))));
			    		}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			    }
			    
			    try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				InputStream in = null;
				try {
					in = new FileInputStream(pathwork+File.separator+"data.xml");
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				}
				if (in != null){
					ReportXMLModel rXMLModel = null;
					XMLObjectReader reader = null;
					try {
						reader = new XMLObjectReader().setInput(in);
					} catch (XMLStreamException e) {
						e.printStackTrace();
					}
					if (reader != null){
						try {
							rXMLModel = (ReportXMLModel)reader.read();
							reader.close();
							rXMLModel.setCodReport(0);
							if (!rXMLModel.getIsList()){
								rXMLModel.setTemplate(pathwork + File.separator + rXMLModel.getTemplate());
							}
							Iterator it = rXMLModel.getMarkers().iterator();
							while(it.hasNext()){
								ReportMarkersXMLModel rmXML = (ReportMarkersXMLModel)it.next();
								rmXML.setCodMarker(0);
								rmXML.setCodReport(0);								
							}
							saveReport(rXMLModel);
						} catch (XMLStreamException e) {							
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private void copyInputStream(InputStream in, OutputStream out)
	  throws IOException
	  {
	    byte[] buffer = new byte[1024];
	    int len;

	    while((len = in.read(buffer)) >= 0)
	      out.write(buffer, 0, len);

	    in.close();
	    out.close();
	  }

}
