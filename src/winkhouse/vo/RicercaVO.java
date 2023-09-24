package winkhouse.vo;

import java.io.Serializable;
import java.util.ArrayList;

import winkhouse.model.RicercheModel;


public class RicercaVO implements Serializable{

	private int type = 0;
	private RicercheModel ricerca = null;
	private ArrayList criteriImmobili = null;
	private ArrayList criteriImmobiliAffitti = null;
	private ArrayList criteriAnagrafiche = null;
	private ArrayList criteriColloqui = null;
	private ArrayList risultati = null;
	
	public RicercaVO() {
		
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		
		this.type = type;
		this.ricerca = null;
		this.criteriImmobili = new ArrayList();
		this.criteriImmobiliAffitti = new ArrayList();
		this.criteriAnagrafiche = new ArrayList();
		this.risultati = null;
		
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

	
	public RicercheModel getRicerca() {
		return ricerca;
	}

	
	public void setRicerca(RicercheModel ricerca) {
		this.ricerca = ricerca;
	}

	public ArrayList getCriteriColloqui() {
		return criteriColloqui;
	}

	public void setCriteriColloqui(ArrayList criteriColloqui) {
		this.criteriColloqui = criteriColloqui;
	}

}
