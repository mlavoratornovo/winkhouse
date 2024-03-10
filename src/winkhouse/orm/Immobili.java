package winkhouse.orm;

import java.util.Iterator;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Immobili;

public class Immobili extends _Immobili {

    private static final long serialVersionUID = 1L; 

    public int getCodImmobile() {
        return Cayenne.intPKForObject(this);
    }
    
    public String getDescrizioneAnagrafichePropietarie() {
    	
    	String returnValue = "";
		
		if (this.getImmobilipropietaris() != null){			
			Iterator<Immobilipropietari> it = this.getImmobilipropietaris().iterator();
			while (it.hasNext()) {
				Immobilipropietari am = (Immobilipropietari) it.next();
				returnValue = returnValue + " - " + am.getAnagrafiche().toString();
			}
		}
		
		return returnValue;
    }

}
