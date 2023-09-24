package winkhouse.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.eclipse.jface.preference.PreferenceStore;

import winkhouse.Activator;
import winkhouse.dao.WinkSysDAO;
import winkhouse.vo.WinkSysVO;
//import winkhouse.view.preference.BackUpPage;


public class WinkhouseDBAgentUtils {

	public final static String DATABASE_PREFERENCE_FILE = "database.preference"; 
	public final static String CARTELLADATI = "posizionedb";
	public final static String CARTELLABACKUPDATI = "posizionebackupdb";
	public final static String ABILITABACKUPDATI = "abilitabackupdb";
	public final static String PERIODOBACKUPDATI = "periodobackupdb";
	public final static String USERDBPWD = "userdbpwd";
	
	private final static String PATHKEYVIEW = "0023dsjietnnvpo98zzajfwe8c3r";
	
	public final static String LAST_START_BACKUPDATI = "last_start_backupdb";
	
	public final static String NOME_IP_MACCHINA_DATI = "nomeipmacchinadati";
			
	private PreferenceStore preferenceStore = null;
	private String storeFileName = Activator.getDefault().getStateLocation().toFile() + File.separator + DATABASE_PREFERENCE_FILE;
	private static WinkhouseDBAgentUtils instance = null;
	
	private ScheduledFuture backupThread = null;
	private HashMap<String,String> hm_winkSys = null; 
	
	public static WinkhouseDBAgentUtils getInstance(){
		if (instance == null){
			instance = new WinkhouseDBAgentUtils();
		}
		return instance;
	}
	
	private WinkhouseDBAgentUtils() {
//		createPreferenceStore();
	}

	public class MarkerSpecialParam{
		
		private Integer codint = null;
		private String codstr = null;
		private String descrizione = null;
		
		public MarkerSpecialParam(Integer codint, String codstr, String descrizione){
			setCodint(codint);
			setCodstr(codstr);
			setDescrizione(descrizione);
		}

		public Integer getCodint() {
			return codint;
		}

		public void setCodint(Integer codint) {
			this.codint = codint;
		}

		public String getCodstr() {
			return codstr;
		}

		public void setCodstr(String codstr) {
			this.codstr = codstr;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}
		
	}
	
	public class ObjectSearchGetters{
		
		private Integer key = null;
		private String methodName = null;
		private String descrizione = null;
		private String columnName = null;
		private String parametrizedTypeName = null;

		public ObjectSearchGetters(){
		}		
		
		public ObjectSearchGetters(Integer key,String methodName,String descrizione,String columnName,String parametrizedTypeName){
			setMethodName(methodName);
			setDescrizione(descrizione);
			setKey(key);
			setColumnName(columnName);
			setParametrizedTypeName(parametrizedTypeName);
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public String getParametrizedTypeName() {
			return parametrizedTypeName;
		}

		public void setParametrizedTypeName(String parametrizedTypeName) {
			this.parametrizedTypeName = parametrizedTypeName;
		}
		
	}
	
	private Comparator<ObjectSearchGetters> getSetComparator = new Comparator<ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters o1, ObjectSearchGetters o2) {
			return o1.methodName.compareTo(o2.getMethodName());
		}
		
	};

	private Comparator<ObjectSearchGetters> getSetComparatorDesc = new Comparator<ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters o1, ObjectSearchGetters o2) {
			return o1.getDescrizione().compareTo(o2.getDescrizione());
		}
		
	};

	private Comparator<ObjectSearchGetters> getSetComparatorId = new Comparator<ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters o1, ObjectSearchGetters o2) {
			if (o1.getKey().intValue() == o2.getKey().intValue()){
				return 0;
			}else if (o1.getKey().intValue() < o2.getKey().intValue()){
				return -1;
			}else{
				return 1;
			}
		}
		
	};
	
//	private void createPreferenceStore(){
//		
//		preferenceStore = new PreferenceStore(storeFileName);
//		try {
//			preferenceStore.load();
//			preferenceStore.setDefault(WinkhouseDBAgentUtils.CARTELLADATI, 
//									   Activator.getDefault()
//									   			.getStateLocation()
//									   			.toFile()
//									   			.toString()+DataBasePage.DEFAULT_DB_FOLDER);
//			InetAddress addr = null;
//			try {
//				addr = InetAddress.getLocalHost();			
//				
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			preferenceStore.setDefault(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI, 
//									   (addr != null)?addr.getHostAddress():"");
//
//			preferenceStore.setDefault(WinkhouseDBAgentUtils.CARTELLABACKUPDATI, 
//					   				   Activator.getDefault()
//					   				   			.getStateLocation()
//					   				   			.toFile()
//					   				   			.toString()+BackUpPage.DEFAULT_BACKUP_FOLDER);
//
//			preferenceStore.setDefault(WinkhouseDBAgentUtils.ABILITABACKUPDATI,false); 
//			preferenceStore.setDefault(WinkhouseDBAgentUtils.PERIODOBACKUPDATI,"");
//			preferenceStore.setDefault(WinkhouseDBAgentUtils.USERDBPWD,"");
//						
//		} catch (IOException e) {
//			File f = new File(storeFileName);
//			try {
//				f.createNewFile();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}		
//	}

	public PreferenceStore getPreferenceStore() {
		return preferenceStore;
	}

	public void setPreferenceStore(PreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
	}
	
	public void savePreference(){
		try {
			getPreferenceStore().save(new FileOutputStream(new File(storeFileName)),"");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//    public boolean copiaFile(String pathOrigine, String pathDestinazione) {
//		  
//		    boolean returnValue = true;
//			try {
//				File destinazione = new File(pathDestinazione);
//				
//				if (!destinazione.exists()){				
//					returnValue = doCopy(pathOrigine, pathDestinazione);
//				}else{
//					if (MessageDialog.openConfirm(Activator.getDefault()
//														   .getWorkbench().getActiveWorkbenchWindow().getShell(), 
//												  "File esistente", 
//												  "Negli archivi � presente un file con lo stesso nome \n" + 
//											 	  "sovrascrivo il file in archivio con quello attuale ?")){
//						
//						returnValue = doCopy(pathOrigine, pathDestinazione);
//						
//					}				
//					
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return returnValue;
//	    
//	  }
	  
	private boolean doCopy(String pathOrigine, String pathDestinazione) {
		  
		  	boolean returnValue = true;
		  	
			try {
				File origine = new File(pathOrigine);
				File destinazione = new File(pathDestinazione);

				FileInputStream fis = new FileInputStream(origine);
				String pathdestinazione = pathDestinazione.substring(0, pathDestinazione.lastIndexOf("\\")); 
				File f = new File(pathdestinazione);
				System.out.println(f.mkdirs());
				
				  FileOutputStream fos = new FileOutputStream(destinazione);

				  byte [] dati = new byte[fis.available()];
				  fis.read(dati);
				  fos.write(dati);
				  
				  fos.flush();
				  fis.close();
				  fos.close();
			} catch (FileNotFoundException e) {
				returnValue = false;
				e.printStackTrace();
			} catch (IOException e) {
				returnValue = false;
				e.printStackTrace();
			}
			return returnValue;

	  }

	public ScheduledFuture getBackupThread() {
		return backupThread;
	}

	public void setBackupThread(ScheduledFuture backupThread) {
		this.backupThread = backupThread;
	}

	public int differenceInDays(Date date1, Date date2) {
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(date1);
		c2.setTime(date2);
		int diffDay = 0;

		if (c1.before(c2)) {
			diffDay = countDiffDay(c1, c2);
		} else {
			diffDay = countDiffDay(c2, c1);
		}

		return diffDay;
		
	}


	public int countDiffDay(Calendar c1, Calendar c2) {
		
		int returnInt = 0;
	    while (!c1.after(c2)) {
	      c1.add(Calendar.DAY_OF_MONTH, 1);
	      returnInt++;
	    }

	    if (returnInt > 0) {
	      returnInt = returnInt - 1;
	    }

	    return (returnInt);
	}
	
//	public String getSecret(){
//    	
//    	String cryptKey = null;    	
//    		
//    	StringEncrypter wse = new StringEncrypter(WinkhouseDBAgentUtils.PATHKEYVIEW);
//		cryptKey = wse.decrypt(cryptKey);
//		
//    	return cryptKey;
////		return WinkhouseDBAgentUtils.PATHKEYVIEW;
//    }
	
	public HashMap<String, String> getHm_winkSys() {
		
		if (hm_winkSys == null){
			
			hm_winkSys = new HashMap<String, String>();
			
			WinkSysDAO wsDAO = new WinkSysDAO();
			ArrayList<WinkSysVO> al_wsvos = wsDAO.getProperties();
			
			for (WinkSysVO winkSysVO : al_wsvos) {
				hm_winkSys.put(winkSysVO.getPropertyName(), winkSysVO.getPropertyValue());
			}
			
		}
		return hm_winkSys;
	}	
	
	public void setHm_winkSys(HashMap<String, String> hm) {
		hm_winkSys = hm;
	}
        
	private class StringEncrypter {

	    Cipher ecipher;
	    Cipher dcipher;


	    /**
	     * Constructor used to create this object.  Responsible for setting
	     * and initializing this object's encrypter and decrypter Chipher instances
	     * given a Secret Key and algorithm.
	     * @param key        Secret Key used to initialize both the encrypter and
	     *                   decrypter instances.
	     * @param algorithm  Which algorithm to use for creating the encrypter and
	     *                   decrypter instances.
	     */
	    public StringEncrypter(SecretKey key, String algorithm) {
	        try {
	            ecipher = Cipher.getInstance(algorithm);
	            dcipher = Cipher.getInstance(algorithm);
	            ecipher.init(Cipher.ENCRYPT_MODE, key);
	            dcipher.init(Cipher.DECRYPT_MODE, key);
	        } catch (NoSuchPaddingException e) {
	            System.out.println("EXCEPTION: NoSuchPaddingException");
	        } catch (NoSuchAlgorithmException e) {
	            System.out.println("EXCEPTION: NoSuchAlgorithmException");
	        } catch (InvalidKeyException e) {
	            System.out.println("EXCEPTION: InvalidKeyException");
	        }
	    }


	    /**
	     * Constructor used to create this object.  Responsible for setting
	     * and initializing this object's encrypter and decrypter Chipher instances
	     * given a Pass Phrase and algorithm.
	     * @param passPhrase Pass Phrase used to initialize both the encrypter and
	     *                   decrypter instances.
	     */
	    public StringEncrypter(String passPhrase) {

	        // 8-bytes Salt
	        byte[] salt = {
	            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
	            (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
	        };

	        // Iteration count
	        int iterationCount = 19;

	        try {

	            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
	            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

	            ecipher = Cipher.getInstance(key.getAlgorithm());
	            dcipher = Cipher.getInstance(key.getAlgorithm());

	            // Prepare the parameters to the cipthers
	            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

	            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
	            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

	        } catch (InvalidAlgorithmParameterException e) {
	            System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
	        } catch (InvalidKeySpecException e) {
	            System.out.println("EXCEPTION: InvalidKeySpecException");
	        } catch (NoSuchPaddingException e) {
	            System.out.println("EXCEPTION: NoSuchPaddingException");
	        } catch (NoSuchAlgorithmException e) {
	            System.out.println("EXCEPTION: NoSuchAlgorithmException");
	        } catch (InvalidKeyException e) {
	            System.out.println("EXCEPTION: InvalidKeyException");
	        }
	    }


	    /**
	     * Takes a single String as an argument and returns an Encrypted version
	     * of that String.
	     * @param str String to be encrypted
	     * @return <code>String</code> Encrypted version of the provided String
	     */
	    private String encrypt(String str) {
	        try {
	            // Encode the string into bytes using utf-8
	            byte[] utf8 = str.getBytes("UTF8");

	            // Encrypt
	            byte[] enc = ecipher.doFinal(utf8);

	            // Encode bytes to base64 to get a string
	            return "";//new sun.misc.BASE64Encoder().encode(enc);

	        } catch (BadPaddingException e) {
	        } catch (IllegalBlockSizeException e) {
	        } catch (UnsupportedEncodingException e) {
	        } catch (IOException e) {
	        }
	        return null;
	    }


	    /**
	     * Takes a encrypted String as an argument, decrypts and returns the
	     * decrypted String.
	     * @param str Encrypted String to be decrypted
	     * @return <code>String</code> Decrypted version of the provided String
	     */
	    private String decrypt(String str) {

	        try {

	            // Decode base64 to get bytes
	            byte[] dec = null;//new sun.misc.BASE64Decoder().decodeBuffer(str);

	            // Decrypt
	            byte[] utf8 = dcipher.doFinal(dec);

	            // Decode using utf-8
	            return new String(utf8, "UTF8");

	        } catch (BadPaddingException e) {
	        } catch (IllegalBlockSizeException e) {
	        	e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	        } catch (IOException e) {
	        }
	        return null;
	    }

	}
	    
//    public String setSecret(String encryptString){
//		StringEncrypter wse = new StringEncrypter(WinkhouseDBAgentUtils.PATHKEYVIEW);
//		return wse.encrypt(encryptString);
//    }
    
    public String DecryptString(String encrypString){
    	
		String decryptResult = null;
		
		StringEncrypter se = new StringEncrypter(PATHKEYVIEW);
		decryptResult = se.decrypt(encrypString);						
		
		return decryptResult;
    	
    }
        
    public String EncryptString(String dencrypString){
    	
		String encryptResult = null;
		
		StringEncrypter se = new StringEncrypter(PATHKEYVIEW);
		encryptResult = se.encrypt(dencrypString);						
		
		return encryptResult;
    	
    }

    public String EncryptStringStandard(String dencrypString){
    	
		String encryptResult = null;
		
		StringEncrypter se = new StringEncrypter(PATHKEYVIEW);
		encryptResult = se.encrypt(dencrypString);						
		
		return encryptResult;
    	
    }
}
