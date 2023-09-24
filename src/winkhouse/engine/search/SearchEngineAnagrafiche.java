package winkhouse.engine.search;

import java.util.ArrayList;

import winkhouse.dao.BaseDAO;
import winkhouse.model.AnagraficheModel;


public class SearchEngineAnagrafiche extends SearchEngine {

	public SearchEngineAnagrafiche(ArrayList criteria) {
		super(criteria);

	}

	@Override
	public ArrayList find() {
		BaseDAO bDAO = new BaseDAO();
		QueryBuilder qb = new QueryBuilder();
		String query = qb.buildQueryAnagrafiche(criteria);
		if (verifyQuery()){
			return bDAO.listFinder(AnagraficheModel.class.getName(), query);
		}else{
			return null;
		}
	}

	@Override
	public boolean verifyQuery() {
		try {
			BaseDAO bDAO = new BaseDAO();
			QueryBuilder qb = new QueryBuilder();
			String query = qb.buildQueryAnagrafiche(criteria);
			bDAO.listFinder(AnagraficheModel.class.getName(), query);
			return true;
		} catch (Exception e) {
			return false;		}
	}
	
	public String getQueryText(){
		QueryBuilder qb = new QueryBuilder();
		return qb.buildQueryAnagrafiche(criteria);
	}

}
