package winkhouse.view.anagrafica.handler;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import winkhouse.model.AnagraficheModel;
import winkhouse.util.WinkhouseUtils;


public class AnagraficheFilter extends ViewerFilter {

	private static AnagraficheFilter instance = null;
	
	private AnagraficheFilter() {
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof AnagraficheModel){
			if (
					(WinkhouseUtils.getInstance().getAngraficheFilterType() == WinkhouseUtils.PROPIETARI) &&
					(((AnagraficheModel)element).getImmobili() != null) &&
					(((AnagraficheModel)element).getImmobili().size() > 0)
				   ){
				return true;
			}else if (
					(WinkhouseUtils.getInstance().getAngraficheFilterType() == WinkhouseUtils.RICHIEDENTI) &&
					(
						(((AnagraficheModel)element).getImmobili() == null) ||
						(((AnagraficheModel)element).getImmobili().size() == 0)
					)
					
					){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}

	public static AnagraficheFilter getInstance() {
		if (instance == null){
			instance = new AnagraficheFilter();
		}
		return instance;
	}

}
