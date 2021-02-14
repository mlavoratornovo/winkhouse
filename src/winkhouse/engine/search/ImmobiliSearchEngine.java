package winkhouse.engine.search;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import winkhouse.dao.ColloquiDAO;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;


public class ImmobiliSearchEngine implements IRunnableWithProgress {

	private Integer codAnagrafica = null;
	private ArrayList returnValue = null;
		

	public ImmobiliSearchEngine(Integer codAnagrafica, ArrayList returnValue){
		this.codAnagrafica = codAnagrafica;
		this.returnValue = returnValue;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
													 InterruptedException {
		
		ColloquiDAO cDAO = new ColloquiDAO();
		ArrayList colloqui = cDAO.getColloquiByAnagraficaRicerca(ColloquiModel.class.getName(), 
																 this.codAnagrafica);
		
		monitor.beginTask("scansione colloqui...", colloqui.size());
		Iterator it = colloqui.iterator();
		HashMap<Integer,ImmobiliModel> immobili = new HashMap<Integer, ImmobiliModel>();
		while (it.hasNext()){
			ColloquiModel cm = (ColloquiModel)it.next();
			SearchEngineImmobili sei = new SearchEngineImmobili(cm.getCriteriRicerca());
			ArrayList alImmobili = sei.find();
			if (alImmobili != null){
				Iterator ite = alImmobili.iterator();
				while (ite.hasNext()){
					ImmobiliModel im = (ImmobiliModel)ite.next();
					immobili.put(im.getCodImmobile(), im);
				}
			}
			monitor.worked(1);
		}
		
		Iterator<Map.Entry<Integer, ImmobiliModel>> ite = immobili.entrySet().iterator();
		monitor.subTask("indicizzazione immobili...");
		while(ite.hasNext()){
			Map.Entry me = ite.next();
			if (me.getValue() != null){
				returnValue.add(me.getValue());
			}
		}
		monitor.worked(1);

	}

	public void run() {

		ColloquiDAO cDAO = new ColloquiDAO();
		ArrayList colloqui = cDAO.getColloquiByAnagraficaRicerca(ColloquiModel.class.getName(), 
				 												 this.codAnagrafica);

		Iterator it = colloqui.iterator();
		HashMap<Integer,ImmobiliModel> immobili = new HashMap<Integer, ImmobiliModel>();
		while (it.hasNext()){
			ColloquiModel cm = (ColloquiModel)it.next();
			SearchEngineImmobili sei = new SearchEngineImmobili(cm.getCriteriRicerca());
			ArrayList alImmobili = sei.find();
			if (alImmobili != null){
				Iterator ite = alImmobili.iterator();
				while (ite.hasNext()){
					ImmobiliModel im = (ImmobiliModel)ite.next();
					immobili.put(im.getCodImmobile(), im);
				}
			}

		}

		Iterator<Map.Entry<Integer, ImmobiliModel>> ite = immobili.entrySet().iterator();

		while(ite.hasNext()){
			Map.Entry me = ite.next();
			if (me.getValue() != null){
				returnValue.add(me.getValue());
			}			
		}

	}
}
