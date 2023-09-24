package winkhouse.model;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.ColloquiDAO;



public class ColloquiModelVisiteCollection extends ColloquiModel {

	private ArrayList<ColloquiModel> colloquiVisite = null;
	
	public ColloquiModelVisiteCollection(ImmobiliModel immobile) {
		setImmobileAbbinato(immobile);
	}

	public ArrayList<ColloquiModel> getColloquiVisite() {
		if (colloquiVisite == null){
			ColloquiDAO cDAO = new ColloquiDAO();
			colloquiVisite = cDAO.getColloquiByImmobile(ColloquiModel.class.getName(), 
													    getCodImmobileAbbinato());
		}
		return colloquiVisite;
	}

	public void setColloquiVisite(ArrayList<ColloquiModel> colloquiVisite) {
		this.colloquiVisite = colloquiVisite;
	}
	
	@Override
	public String toString(){
		String returnValue = "";
		if (getColloquiVisite() != null){
			Iterator<ColloquiModel> it = getColloquiVisite().iterator();
			while (it.hasNext()) {
				returnValue += it.next().toString();
			}			
		}
		return returnValue;
	}
	
}
