package winkhouse.export.helpers;

import winkhouse.util.WinkhouseUtils;

public class SystemPreferenceHelper {

	public String getArchivioImmaginiPath(){
		
		return WinkhouseUtils.getInstance()
					  		 .getPreferenceStore()
					  		 .getString(WinkhouseUtils.IMAGEPATH);
		
	}
	
	public String getArchivioAllegatiPath(){
		
		return WinkhouseUtils.getInstance()
							 .getPreferenceStore()
							 .getString(WinkhouseUtils.ALLEGATIPATH);
		
	}
	
	
	
}
