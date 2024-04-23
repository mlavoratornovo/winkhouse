package winkhouse.action.stampa;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.ReportDAO;
import winkhouse.helper.ReportHelper;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ReportModel;
import winkhouse.orm.Colloqui;
import winkhouse.orm.Report;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.colloqui.DettaglioColloquioView;



public class StampaColloquiAction extends Action implements IAction,
		IMenuCreator {

	private Menu fMenu;

	public StampaColloquiAction(String text, int style) {
		super(text, style);
		setImageDescriptor(Activator.getImageDescriptor("icons/fileprint.png"));
		setMenuCreator(this); 
	}

	
	@Override
	public void dispose() {
		if (fMenu != null) {
			fMenu.dispose();
			fMenu = null;
		}
	}
	
	private class ReportAction extends Action{
		
		private Report report = null;
		
		public ReportAction(String label, Report report){
			super(label);
			this.report = report;
		}

		@Override
		public void run() {
			IWorkbenchPart vp = PlatformUI.getWorkbench()
					  				.getActiveWorkbenchWindow()
					  				.getActivePage()
					  				.getActivePart();
			
			if (vp instanceof DettaglioColloquioView){
				Colloqui cm = ((DettaglioColloquioView)vp).getColloquio();
				ReportHelper rh = new ReportHelper();
				ArrayList al = new ArrayList();
				al.add(cm);
				rh.doReport(al, report);
			}
			
		}
		
		
	}

	@Override
	public Menu getMenu(Control parent) {
		
		if (fMenu != null)
			fMenu.dispose();
		fMenu = new Menu(parent);
				
		ReportDAO rDAO = new ReportDAO();
		ArrayList<Report> al = rDAO.getReportByTipologia(WinkhouseUtils.COLLOQUI);
			
		Iterator<Report> it = al.iterator();
		while (it.hasNext()){
			Report rm = it.next();
			addActionToMenu(fMenu, new ReportAction(rm.getNome(), rm));
		}
		
		return fMenu; 
	}
	
	protected void addActionToMenu(Menu parent, Action action) {
		ActionContributionItem item = new ActionContributionItem(action);
		item.fill(parent, -1);
	}

	@Override
	public Menu getMenu(Menu parent) {

		return null;
	}

}
