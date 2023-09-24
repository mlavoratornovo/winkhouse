package winkhouse.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class ZipUtils{
	
	public boolean unZip4jArchivio(String pathZipFile,String unzipPath){
		
		boolean returnValue = true;
		HashMap<String,String> dircreated = new HashMap();
		try {
			
			ZipFile zf = new ZipFile(pathZipFile);
			if (zf.isEncrypted()){
				
				String cryptKey = WinkhouseUtils.getInstance().DecryptStringStandard(WinkhouseUtils.getInstance().getPreferenceStore().getString(IWinkSysProperties.CRIPTKEY));
				zf.setPassword(cryptKey.toCharArray());
				
			}
			@SuppressWarnings("unchecked")
	        List<FileHeader> fileHeaders = zf.getFileHeaders();

	        for(FileHeader fileHeader : fileHeaders) {
	        	if (fileHeader.isDirectory()) {
	        		zf.extractFile(fileHeader, fileHeader.getFileName());
	            }
	        }
			zf.extractAll(unzipPath);
		} catch (IOException e) {
			returnValue = false;
			e.printStackTrace();
		} catch (Exception e) {
			returnValue = false;
			e.printStackTrace();
		}
		
		return returnValue;
		
	}

	public boolean zip4jArchivio(String pathtozip, String pathzipfile){
		
		boolean returnValue = true;
		
		try {
			String cryptKey = (WinkhouseUtils.getInstance().getPreferenceStore().getString(IWinkSysProperties.CRIPTKEY)==null ||
							   WinkhouseUtils.getInstance().getPreferenceStore().getString(IWinkSysProperties.CRIPTKEY).trim().equalsIgnoreCase(""))
							   ? ""
							   : WinkhouseUtils.getInstance().DecryptStringStandard(WinkhouseUtils.getInstance().getPreferenceStore().getString(IWinkSysProperties.CRIPTKEY));
			
			ZipFile zf = new ZipFile(pathzipfile);
			ZipParameters zipParameters = new ZipParameters();
			
			File f_pathtozip = new File(pathtozip);
			if (f_pathtozip.isDirectory()){
				
				if (!cryptKey.equalsIgnoreCase("")){
					zipParameters.setEncryptFiles(true);
					zipParameters.setEncryptionMethod(EncryptionMethod.AES);
					zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
					zf.setPassword(cryptKey.toCharArray());
				
				}
				File[] files = f_pathtozip.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()){
						if (!cryptKey.equalsIgnoreCase("")){
							zf.addFolder(files[i], zipParameters);		
						}else{
							zf.addFolder(files[i]);
						}
					}else{
						if (!cryptKey.equalsIgnoreCase("")){
							zf.addFile(files[i], zipParameters);		
						}else{
							zf.addFile(files[i]);
						}
		
					}
				}
					
				
				
			}else{
				if (!cryptKey.equalsIgnoreCase("")){
					zipParameters.setEncryptFiles(true);
					zipParameters.setEncryptionMethod(EncryptionMethod.AES);
					zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
					zf.setPassword(cryptKey.toCharArray());
					zf.addFile(f_pathtozip,zipParameters);
				}else{
					zf.addFile(f_pathtozip);
				}
			}
			
		} catch (IOException e) {
			returnValue = false;
			e.printStackTrace();
		} catch (Exception e) {
			returnValue = false;
			e.printStackTrace();
		}
		
		return returnValue;
		
	} 
	
	public boolean unZipArchivio(String pathZipFile,String unzipPath){
	
		boolean returnValue = true;
				
	    try {
	        File f = new File(unzipPath);
	        if(!f.isDirectory()) {
	            f.mkdirs();
	        }
	        FileInputStream fis = new FileInputStream(pathZipFile);
	        ZipInputStream zin = new ZipInputStream(fis);
	        
	        try {
	            ZipEntry ze = null;
	            while ((ze = zin.getNextEntry()) != null) {
	            		            	
	            	String filename = ze.getName().substring(ze.getName().lastIndexOf("\\")+1);
	            		            	
	                String path = unzipPath + File.separator + filename;
	                
	                	            	                
	                if (!ze.getName().endsWith("xml")){	     
	                	
	                	File fpath = new File (ze.getName());
	                	String dirpath = unzipPath + File.separator + fpath.getParentFile();
	                	File dirpathfile = new File(dirpath);
	                	dirpathfile.mkdirs();
	                	path = dirpath + File.separator + filename;
	                }
	                
                    FileOutputStream fout = new FileOutputStream(path, false);
                    try {
                        for (int c = zin.read(); c != -1; c = zin.read()) {
                            fout.write(c);
                        }
                        zin.closeEntry();
                    }
                    finally {
                        fout.close();
                    }
                    
	            }
	        }catch(Exception e){
	        	//e.printStackTrace();
	        	returnValue = false;				
	            zin.close();
	            zin = null;
	            fis.close();
	            fis = null;
	        	
	        }
	        finally {
	            zin.close();
	            zin = null;
	            fis.close();
	            fis = null;
	        }
	    }
	    catch (Exception e) {
	    	returnValue = false;
			
	    }		
		
		return returnValue;
	}


}    

