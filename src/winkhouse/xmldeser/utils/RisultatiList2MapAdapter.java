package winkhouse.xmldeser.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RisultatiList2MapAdapter {

	public ArrayList risultatiMerge = null;
	
	public RisultatiList2MapAdapter(ArrayList risultatiMerge){
		this.risultatiMerge = risultatiMerge; 
	}
	
	public HashMap adapt(){
		
		HashMap returnValue = new HashMap();
		
		Iterator it = this.risultatiMerge.iterator();
		
		while (it.hasNext()){
			
			Object o = it.next();
			addItem(returnValue, o);
			
		}
		
		return returnValue;
		
	}
	
	protected void addItem(HashMap returnValue, Object o){
		
		if (returnValue.get(o.getClass().getName()) == null){
			ArrayList al = new ArrayList();
			al.add(o);
			returnValue.put(o.getClass().getName(), al);
		}else{
			((ArrayList)returnValue.get(o.getClass().getName())).add(o);
		}
		
	}
	
}
