package winkhouse.export.utils;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

import winkhouse.util.CriteriaTableUtilsFactory;

public class CriteriaTablesHelper {

	public CriteriaTablesHelper() {
		// TODO Auto-generated constructor stub
	}

	public TableViewer getImmobiliCriteriaTV(Composite container, ArrayList itemsSource){
		
		CriteriaTableUtilsFactory ctuf = new CriteriaTableUtilsFactory();
		return ctuf.getSearchImmobiliCriteriaTable(container, itemsSource);
		
	}
	
	public TableViewer getAnagraficheCriteriaTV(Composite container, ArrayList itemsSource){
		
		CriteriaTableUtilsFactory ctuf = new CriteriaTableUtilsFactory();
		return ctuf.getSearchAnagraficheCriteriaTable(container, itemsSource);
		
	}
	
	
}
