package winkhouse.engine.search;

import java.util.ArrayList;

import winkhouse.dao.BaseDAO;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Colloquicriteriricerca;
import winkhouse.orm.Immobili;


public class SearchEngineImmobili extends SearchEngine{	
	
	public SearchEngineImmobili(ArrayList<Colloquicriteriricerca> criteria){
		super(criteria);
	}
	
	public ArrayList<Immobili> find() {
		BaseDAO bDAO = new BaseDAO();
		QueryBuilder qb = new QueryBuilder();
		String query = qb.buildQueryImmobili(criteria);
		if (verifyQuery()){
			return bDAO.listFinder(ImmobiliModel.class.getName(), query);
		}else{
			return null;
		}
	}
	
	public boolean verifyQuery(){
		try {
			BaseDAO bDAO = new BaseDAO();
			QueryBuilder qb = new QueryBuilder();
			String query = qb.buildQueryImmobili(criteria);
			bDAO.listFinder(ImmobiliModel.class.getName(), query);
			return true;
		} catch (Exception e) {
			return false;		}
	}
	
	public String getQueryText(){
		QueryBuilder qb = new QueryBuilder();
		return qb.buildQueryImmobili(criteria);
	}

}