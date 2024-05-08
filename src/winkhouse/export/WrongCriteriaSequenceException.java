package winkhouse.export;

import java.util.ArrayList;

import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.orm.Colloquicriteriricerca;

public class WrongCriteriaSequenceException extends Exception {

	private ArrayList<Colloquicriteriricerca> criteri = null;
	private String sqlQuery = null;                                            
	
	public WrongCriteriaSequenceException(ArrayList<Colloquicriteriricerca> criteri,String sqlQuery) {
		this.criteri = criteri;
		this.sqlQuery = sqlQuery;
	}

	public ArrayList<Colloquicriteriricerca> getCriteri() {
		return criteri;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}
	
}