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

public class ImmobiliColloquiSearchEngine implements IRunnableWithProgress {
	
	private Integer codColloquio = null;
	private ArrayList returnValue = null;

	public ImmobiliColloquiSearchEngine(Integer codColloquio, ArrayList returnValue) {
		this.codColloquio = codColloquio;
		this.returnValue = returnValue;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		
		monitor.beginTask("inizio ricerca ...", 2);
		
		ColloquiDAO cDAO = new ColloquiDAO();
		ColloquiModel cm = (ColloquiModel)cDAO.getColloquioById(ColloquiModel.class.getName(), this.codColloquio);
		
		HashMap<Integer,ImmobiliModel> immobili = new HashMap<Integer, ImmobiliModel>();
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

}