package winkhouse.engine.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import winkhouse.dao.ColloquiDAO;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;


public class AnagraficheSearchEngine implements IRunnableWithProgress {

	private Integer codImmobile = null;
	private ArrayList returnValue = null;
	private HashMap<Integer,AnagraficheModel> anagrafiche = null;
	private Comparator cFindImmobile = new Comparator<ImmobiliModel>(){

		@Override
		public int compare(ImmobiliModel o1, ImmobiliModel o2) {
			if (o1.getCodImmobile().intValue() == o2.getCodImmobile().intValue()){
				return 0;
			}else if (o1.getCodImmobile().intValue() < o2.getCodImmobile().intValue()){
				return -1;
			}else{
				return 1;
			}			
		}
		
	};
	
	public AnagraficheSearchEngine(Integer codImmobile, ArrayList returnValue) {
		this.codImmobile = codImmobile;
		this.returnValue = (returnValue == null)? new ArrayList():returnValue;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
													 InterruptedException {
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File("c:\\logmedubug.log"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		anagrafiche = new HashMap<Integer, AnagraficheModel>();
		ColloquiDAO cDAO = new ColloquiDAO();
		ArrayList colloquiRicerca = cDAO.getColloquiByTipologia(ColloquiModel.class.getName(), new Integer(1));
		Iterator it = colloquiRicerca.iterator();
		monitor.beginTask("scansione colloqui...", colloquiRicerca.size());
		ImmobiliModel imKey = new ImmobiliModel(); 
		imKey.setCodImmobile(codImmobile);     
		while (it.hasNext()){ 
			ColloquiModel cm = (ColloquiModel)it.next();
			SearchEngineImmobili sei = new SearchEngineImmobili(cm.getCriteriRicerca());
			ArrayList alImmobili = sei.find();
			if (alImmobili != null){
				Collections.sort(alImmobili, cFindImmobile);
				if (Collections.binarySearch(alImmobili, imKey, cFindImmobile) > -1){
					ArrayList alAnagrafiche = cm.getAnagrafiche();					
					monitor.subTask("rilevazione anagrafiche...");
					Iterator iterator = alAnagrafiche.iterator();
					while (iterator.hasNext()){
						ColloquiAnagraficheModel_Ang am = (ColloquiAnagraficheModel_Ang)iterator.next();
						anagrafiche.put(am.getCodAnagrafica(), am.getAnagrafica());
					}
				}
			}else{
				if (writer != null){
					writer.println(cm.getCodColloquio().toString());
				}
				
				
			}
			monitor.worked(1);
		}
		
		if (writer != null){
			writer.close();		
		}
		
		Iterator<Map.Entry<Integer, AnagraficheModel>> ite = anagrafiche.entrySet().iterator();
		monitor.subTask("indicizzazione anagrafiche...");
		while(ite.hasNext()){
			Map.Entry me = ite.next();
			if (me.getValue() != null){
				returnValue.add(me.getValue());
			}
		}
		monitor.worked(1);
		
	}

	public void run() {

		anagrafiche = new HashMap<Integer, AnagraficheModel>();
		ColloquiDAO cDAO = new ColloquiDAO();
		ArrayList colloquiRicerca = cDAO.getColloquiByTipologia(ColloquiModel.class.getName(), new Integer(1));
		Iterator it = colloquiRicerca.iterator();

		ImmobiliModel imKey = new ImmobiliModel(); 
		imKey.setCodImmobile(codImmobile);     
		while (it.hasNext()){ 
			ColloquiModel cm = (ColloquiModel)it.next();
			SearchEngineImmobili sei = new SearchEngineImmobili(cm.getCriteriRicerca());
			ArrayList alImmobili = sei.find();
			if (alImmobili != null){
				Collections.sort(alImmobili, cFindImmobile);
				if (Collections.binarySearch(alImmobili, imKey, cFindImmobile) > -1){
					ArrayList alAnagrafiche = cm.getAnagrafiche();

					Iterator iterator = alAnagrafiche.iterator();
					while (iterator.hasNext()){
						ColloquiAnagraficheModel_Ang am = (ColloquiAnagraficheModel_Ang)iterator.next();
						anagrafiche.put(am.getCodAnagrafica(), am.getAnagrafica());
					}
				}
			}

		}
		Iterator<Map.Entry<Integer, AnagraficheModel>> ite = anagrafiche.entrySet().iterator();

		while(ite.hasNext()){
			Map.Entry me = ite.next();
			if (me.getValue() != null){
				returnValue.add(me.getValue());
			}
		}


	}
	
}
