package winkhouse.xmldeser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils{

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
	            		            	
	            	String filename = ze.getName().substring(ze.getName().lastIndexOf("/")+1);
	            	
	            	String[] arr_path = ze.getName().split("/");
	                String path = unzipPath + File.separator + filename;
	                
	                	            	                
	                if (!ze.getName().endsWith("xml")){	                	
	                	String dirpath = unzipPath + File.separator + arr_path[arr_path.length-3] + File.separator + arr_path[arr_path.length-2];
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
	        	e.printStackTrace();
	        	returnValue = false;				
	            zin.close();
	            zin = null;
	            fis.close();
	        	
	        }
	        finally {
	            zin.close();
	            zin = null;
	            fis.close();
	        }
	    }
	    catch (Exception e) {
	    	returnValue = false;
			
	    }		
		
		return returnValue;
	}


}    

