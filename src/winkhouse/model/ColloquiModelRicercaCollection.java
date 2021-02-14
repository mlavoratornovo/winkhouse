package winkhouse.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.ColloquiDAO;
import winkhouse.engine.search.AnagraficheSearchEngine;
import winkhouse.engine.search.SearchEngineImmobili;


public class ColloquiModelRicercaCollection extends ColloquiModel {

	private AnagraficheModel anagrafica = null;
	private ImmobiliModel immobile = null;
	private ArrayList<ColloquiModel> colloquiRicerca = null;
	private ArrayList<ImmobiliModel> listaimmobili = null;
	private ArrayList<AnagraficheModel> listaanagrafiche = null;
	
	public ColloquiModelRicercaCollection(AnagraficheModel anagrafica, ImmobiliModel immobile){
		this.anagrafica = anagrafica;
		this.immobile = immobile;
	}

	public ArrayList<ColloquiModel> getColloquiRicerca() {
		
		if (colloquiRicerca == null){
			
			if ((anagrafica != null) && (anagrafica.getCodAnagrafica() != null)){
				ColloquiDAO cDAO = new ColloquiDAO();				
				colloquiRicerca = cDAO.getColloquiByAnagraficaRicerca(ColloquiModel.class.getName(), 
																	  anagrafica.getCodAnagrafica());
			}else{
				colloquiRicerca = new ArrayList<ColloquiModel>();
			}
			
		}
		
		return colloquiRicerca;
	}

	public void setColloquiRicerca(ArrayList<ColloquiModel> colloquiRicerca) {
		this.colloquiRicerca = colloquiRicerca;
	}

	public ArrayList<AnagraficheModel> getListaanagrafiche(){
		
		if (listaanagrafiche == null){
			
			if (immobile.getCodImmobile() != null){
				
				listaanagrafiche = new ArrayList<AnagraficheModel>();
				AnagraficheSearchEngine ase = new AnagraficheSearchEngine(immobile.getCodImmobile(),
																		  listaanagrafiche);
				
				try {
					new ProgressMonitorDialog(PlatformUI.getWorkbench()
					 		    						.getActiveWorkbenchWindow()
					    		    					.getShell()).run(true, true, ase);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
		return listaanagrafiche;
		
	}
	
	public ArrayList<ImmobiliModel> getListaimmobili(){
		
		if (listaimmobili == null){
			
			HashMap hm = new HashMap<Integer, ImmobiliModel>();
						
			if (getColloquiRicerca() != null){
				
				listaimmobili = new ArrayList<ImmobiliModel>();
				Iterator<ColloquiModel> it = getColloquiRicerca().iterator();
				
				while (it.hasNext()){
					
					ColloquiModel cm = it.next();
					SearchEngineImmobili sei = new SearchEngineImmobili(cm.getCriteriRicerca());
					ArrayList al = sei.find();
					
					if (al != null){
						Iterator ite = al.iterator();
						while(ite.hasNext()){
							ImmobiliModel im = (ImmobiliModel)ite.next();
							hm.put(im.getCodImmobile(), im);
						}
					}
					
				}
				
				Iterator<Map.Entry<Integer, ImmobiliModel>> iter = hm.entrySet().iterator();
				
				while (iter.hasNext()){
					listaimmobili.add(iter.next().getValue());				
				}
				
			}
			
		}
		
		return listaimmobili;
		
	}
	
	@Override
	public String toString() {
		String returnValue = "";
		if (getColloquiRicerca() != null){
			Iterator it = getColloquiRicerca().iterator();
			while (it.hasNext()){
				returnValue += ((ColloquiModel)it.next()).toString();
			}
		}
		return returnValue;
	}
}
