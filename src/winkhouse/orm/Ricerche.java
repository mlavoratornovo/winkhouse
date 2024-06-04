package winkhouse.orm;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;

import winkhouse.orm.auto._Ricerche;

public class Ricerche extends _Ricerche {

    private static final long serialVersionUID = 1L; 
    private ObjectContext editObjectContext = null;
    private ArrayList risultati = null;
	private ArrayList criteriImmobili = null;
	private ArrayList criteriImmobiliAffitti = null;
	private ArrayList criteriAnagrafiche = null;
	private ArrayList criteriColloqui = null;

	public ObjectContext getEditObjectContext() {
		return editObjectContext;
	}
	public void setEditObjectContext(ObjectContext editObjectContext) {
		this.editObjectContext = editObjectContext;
	}
	
	public int getCodRicerche() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }

	public ArrayList getRisultati() {
		return risultati;
	}
	
	public void setRisultati(ArrayList risultati) {
		this.risultati = risultati;
	}

	public ArrayList getCriteriImmobili() {
		return criteriImmobili;
	}

	public void setCriteriImmobili(ArrayList criteriImmobili) {
		this.criteriImmobili = criteriImmobili;
	}

	public ArrayList getCriteriAnagrafiche() {
		return criteriAnagrafiche;
	}

	public void setCriteriAnagrafiche(ArrayList criteriAnagrafiche) {
		this.criteriAnagrafiche = criteriAnagrafiche;
	}
	
	public ArrayList getCriteriImmobiliAffitti() {
		return criteriImmobiliAffitti;
	}

	public void setCriteriImmobiliAffitti(ArrayList criteriImmobiliAffitti) {
		this.criteriImmobiliAffitti = criteriImmobiliAffitti;
	}

	public ArrayList getCriteriColloqui() {
		return criteriColloqui;
	}

	public void setCriteriColloqui(ArrayList criteriColloqui) {
		this.criteriColloqui = criteriColloqui;
	}

}
