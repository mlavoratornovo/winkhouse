package winkhouse.model;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.ColloquiDAO;


public class ColloquiModelAnagraficaCollection extends ColloquiModel {
	
	private ArrayList colloqui = null;
	
	public ColloquiModelAnagraficaCollection(AnagraficheModel aModel){
		ArrayList anagrafiche = new ArrayList();
		anagrafiche.add(aModel);
		setAnagrafiche(anagrafiche);
	}

	public ArrayList getColloqui() {
		if (colloqui == null){
			colloqui = new ArrayList();
			ColloquiDAO cDAO = new ColloquiDAO();
			if (getAnagrafiche() != null){
				Iterator it = getAnagrafiche().iterator();
				while (it.hasNext()){
					AnagraficheModel am = (AnagraficheModel)it.next();
					if (am.getCodAnagrafica() != null){
						colloqui.addAll(cDAO.getColloquiByAnagrafica(ColloquiModel.class.getName(),am.getCodAnagrafica()));
					}
				}
			}
		}
		return colloqui;
	}

	public void setColloqui(ArrayList colloqui) {
		this.colloqui = colloqui;
	}

	@Override
	public String toString() {
		String returnValue = "";
		if (getColloqui() != null){
			Iterator it = getColloqui().iterator();
			while (it.hasNext()){
				returnValue += ((ColloquiModel)it.next()).toString();
			}
		}
		return returnValue;
	}
	
}
