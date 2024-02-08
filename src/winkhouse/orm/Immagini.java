package winkhouse.orm;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Immagini;

public class Immagini extends _Immagini {

    private static final long serialVersionUID = 1L; 
    HashMap proprieta = null;
    
    public int getCodImmagine() {
    	return Cayenne.intPKForObject(this);
    }

	public HashMap getProprieta() {
		if (proprieta == null){
			proprieta = new HashMap<String, String>();
			Pattern p = Pattern.compile("\\|");
			if (this.getImgprops() != null){
			    String[] immagineStr = p.split(getImgprops());					
			    for (int i = 0; i < immagineStr.length; i++) {
			    	if (!immagineStr[i].trim().equalsIgnoreCase("")){
			    		proprieta.put(immagineStr[i], null);
			    	}
				}
			}
		}
		return proprieta;
	}

}
