package winkhouse.action.stampa;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import winkhouse.Activator;
import winkhouse.dao.ReportDAO;
import winkhouse.helper.ReportHelper;
import winkhouse.model.ReportModel;
import winkhouse.util.WinkhouseUtils;



public class StampaAnagraficheListAction extends Action implements IAction,
		IMenuCreator {

	private Menu fMenu;

	public StampaAnagraficheListAction(String text, int style) {
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
		
		private ReportModel report = null;
		
		public ReportAction(String label, ReportModel report){
			super(label);
			this.report = report;
		}

		@Override
		public void run() {
			
			if ((WinkhouseUtils.getInstance().getRicercaWiz() != null) && 
				(WinkhouseUtils.getInstance().getRicercaWiz()
	    									   .getRicerca() != null) &&
				(WinkhouseUtils.getInstance().getRicercaWiz()
	    		    						   .getRicerca()
	    		    						   .getRisultati() != null)){
				
				ReportHelper rh = new ReportHelper();
				if (report.getIsList()){
					rh.doReportList(WinkhouseUtils.getInstance()
													.getRicercaWiz()
							   						.getRicerca()
							   						.getRisultati(), report);
				}else{
					rh.doReport(WinkhouseUtils.getInstance()
												.getRicercaWiz()
							   					.getRicerca()
							   					.getRisultati(), report);
				}
								
				
			}
		}
		
		
	}

	@Override
	public Menu getMenu(Control parent) {
		
		if (fMenu != null)
			fMenu.dispose();
		fMenu = new Menu(parent);
				
		ReportDAO rDAO = new ReportDAO();
		ArrayList al = rDAO.getReportListByTipologia(ReportModel.class.getName(),
								  				 	 WinkhouseUtils.ANAGRAFICHE);

		al.addAll(rDAO.getReportByTipologia(ReportModel.class.getName(),
				  WinkhouseUtils.ANAGRAFICHE));

		
		Iterator it = al.iterator();
		while (it.hasNext()){
			ReportModel rm = (ReportModel)it.next();
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