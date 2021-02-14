package winkhouse.engine.search;

import java.util.ArrayList;

public abstract class SearchEngine {
	
	public static int SEARCH_IMMOBILI = 0;
	public static int SEARCH_ANAGRAFICA = 1;

	protected ArrayList criteria = null;
	
	public SearchEngine(ArrayList criteria) {
		this.criteria = (ArrayList)criteria.clone();
	}
	
	public abstract ArrayList find();
	
	public abstract boolean verifyQuery();

}
