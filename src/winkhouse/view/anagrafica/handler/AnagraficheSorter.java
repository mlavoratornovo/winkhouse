package winkhouse.view.anagrafica.handler;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClassiClientiVO;

public class AnagraficheSorter extends ViewerSorter {

	public static AnagraficheSorter instance = null;
	
	public static AnagraficheSorter getInstance(){
		if (instance == null){
			instance = new AnagraficheSorter();
		}
		return instance;
	}
	
	private AnagraficheSorter() {
		
	}

	public AnagraficheSorter(Collator collator) {
		super(collator);
	}

	@Override
	public int category(Object element) {

		return super.category(element);
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if ((e1 instanceof AnagraficheVO) && (e2 instanceof AnagraficheVO)){
			return ((AnagraficheVO)e1).toString().compareToIgnoreCase(((AnagraficheVO)e2).toString());
		}
		if ((e1 instanceof ClassiClientiVO) && (e2 instanceof ClassiClientiVO)){
			return ((ClassiClientiVO)e1).toString().compareToIgnoreCase(((ClassiClientiVO)e2).toString());
		}
		if ((e1 instanceof AnagraficheVO) && (e2 instanceof ClassiClientiVO)){
			return 1;
		}
		if ((e1 instanceof ClassiClientiVO) && (e2 instanceof AnagraficheVO)){
			return -1;
		}
		return super.compare(viewer, e1, e2);
	}

	

}
