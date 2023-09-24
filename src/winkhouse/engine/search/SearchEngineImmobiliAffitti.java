package winkhouse.engine.search;

import java.util.ArrayList;

import winkhouse.dao.BaseDAO;
import winkhouse.model.ImmobiliModel;


public class SearchEngineImmobiliAffitti extends SearchEngine {

	public SearchEngineImmobiliAffitti(ArrayList criteria) {
		super(criteria);

	}

	@Override
	public ArrayList find() {
		BaseDAO bDAO = new BaseDAO();
		QueryBuilder qb = new QueryBuilder();
		String query = qb.buildQueryImmobiliAffitti(criteria);
		if (verifyQuery()){
			return bDAO.listFinder(ImmobiliModel.class.getName(), query);
		}else{
			return null;
		}
	}

	@Override
	public boolean verifyQuery() {
		try {
			BaseDAO bDAO = new BaseDAO();
			QueryBuilder qb = new QueryBuilder();
			String query = qb.buildQueryImmobiliAffitti(criteria);
			bDAO.listFinder(ImmobiliModel.class.getName(), query);
			return true;
		} catch (Exception e) {
			return false;		}
	}

	public String getQueryText(){
		QueryBuilder qb = new QueryBuilder();
		return qb.buildQueryImmobiliAffitti(criteria);
	}
	
}
