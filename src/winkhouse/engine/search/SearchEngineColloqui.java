package winkhouse.engine.search;

import java.util.ArrayList;

import winkhouse.dao.BaseDAO;
import winkhouse.model.ColloquiModel;

public class SearchEngineColloqui extends SearchEngine {

	public SearchEngineColloqui(ArrayList criteria) {
		super(criteria);
	}

	@Override
	public ArrayList find() {
		BaseDAO bDAO = new BaseDAO();
		QueryBuilder qb = new QueryBuilder();
		String query = qb.buildQueryColloqui(criteria);
		if (verifyQuery()){
			return bDAO.listFinder(ColloquiModel.class.getName(), query);
		}else{
			return null;
		}
	}

	@Override
	public boolean verifyQuery() {
		try {
			BaseDAO bDAO = new BaseDAO();
			QueryBuilder qb = new QueryBuilder();
			String query = qb.buildQueryColloqui(criteria);
			bDAO.listFinder(ColloquiModel.class.getName(), query);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public String getQueryText(){
		QueryBuilder qb = new QueryBuilder();
		return qb.buildQueryColloqui(criteria);
	}

}