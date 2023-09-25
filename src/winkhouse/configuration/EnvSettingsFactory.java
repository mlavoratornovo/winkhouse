package winkhouse.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javolution.util.FastMap;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;
import winkhouse.Activator;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.TipologieColloquiVO;

public class EnvSettingsFactory {
	
	private static EnvSettingsFactory instance = null;
	public final static String TIPOLOGIECOLLOQUI_FILENAME = "tipologiecolloqui.xml";
	public final static String MESI_FILENAME = "mesi";
	private FastMap<String,String> queries = null;
	private ArrayList<TipologieColloquiVO> tipologieColloqui = null;
	private static Locale locale = new Locale("it");
	private FastMap<String,String> gcaleventcolors = null;
	
	private EnvSettingsFactory(){
		
	}
	
	public static EnvSettingsFactory getInstance(){
		if (instance == null){
			instance = new EnvSettingsFactory();
		}
		return instance;
	}
	
	public FastMap<String, String> getQueries(){
		
		try {
					
			if (queries == null){
				
				InputStream is = getClass().getClassLoader()
										   .getResourceAsStream("winkhouse/configuration/DBQueries.xml");
				
				XMLObjectReader or = XMLObjectReader.newInstance(is);
				queries = (FastMap<String,String>)or.read();
				or.close();
				
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return queries;
	}
	
	public ArrayList<TipologieColloquiVO> getTipologieColloqui(){
		
		if (tipologieColloqui == null){
			tipologieColloqui = new ArrayList<TipologieColloquiVO>();
		    XMLObjectReader reader = null;
			try {
				reader = XMLObjectReader.newInstance(getClass().getClassLoader()
										.getResourceAsStream("winkhouse/configuration/"+TIPOLOGIECOLLOQUI_FILENAME)); 
			} catch (XMLStreamException e1) {
				e1.printStackTrace();
			}

		    try {
		    	while (reader.hasNext()){ 
					try {	
					  TipologieColloquiVO tipologia = reader.read("TipologieColloqui", TipologieColloquiVO.class);
					  tipologieColloqui.add(tipologia);
					} catch (XMLStreamException e) {
						e.printStackTrace();
					}
					  
				}
				reader.close();
			} catch (XMLStreamException e) {
				//e.printStackTrace();
			}
			
		}
		
		return tipologieColloqui;
	}
	
	public ResourceBundle getDefaultMesiDescription(){
		return ResourceBundle.getBundle("winkhouse.configuration."+MESI_FILENAME, this.locale);
	}

	public FastMap<String, String> getLocalGCalColors(){
		
		try {
					
			if (gcaleventcolors == null){
				
				InputStream is;
				try {
					is = new FileInputStream(new File(Activator.getDefault()
															   .getStateLocation()
															   .toFile() + 
													  File.separator + 
													  WinkhouseUtils.GCAL_COLOR_CACHE_FILE
													  )
											);
					
					XMLObjectReader or = XMLObjectReader.newInstance(is);
					gcaleventcolors = (FastMap<String,String>)or.read();
					or.close();

				} catch (Exception e) {
					gcaleventcolors = new FastMap<String, String>();
				}
								
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return gcaleventcolors;
	}
	
	public boolean saveGCalColorCache(FastMap fmGCalColorCache){
		
		boolean returnValue = true;
		
		OutputStream os = null;
		
		try {
			FileOutputStream fos = new FileOutputStream(
										new File(Activator.getDefault()
														  .getStateLocation()
														  .toFile() + 
												 File.separator + 
												 WinkhouseUtils.GCAL_COLOR_CACHE_FILE));
			
			XMLObjectWriter ow = XMLObjectWriter.newInstance(fos);
			ow.write(fmGCalColorCache);
			ow.flush();
			ow.close();
			returnValue = true;
		} catch (FileNotFoundException e) {
			returnValue = false;
		} catch (IllegalStateException e) {
			returnValue = false;
		} catch (XMLStreamException e) {
			returnValue = false;
		}
		  		
		return returnValue;
	}

}