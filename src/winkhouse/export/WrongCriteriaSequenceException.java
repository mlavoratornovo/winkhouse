package winkhouse.export;

import java.util.ArrayList;

import winkhouse.model.ColloquiCriteriRicercaModel;

public class WrongCriteriaSequenceException extends Exception {

	private ArrayList<ColloquiCriteriRicercaModel> criteri = null;
	private String sqlQuery = null;                                            
	
	public WrongCriteriaSequenceException(ArrayList<ColloquiCriteriRicercaModel> criteri,String sqlQuery) {
		this.criteri = criteri;
		this.sqlQuery = sqlQuery;
	}

	public ArrayList<ColloquiCriteriRicercaModel> getCriteri() {
		return criteri;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}
	
	


}
