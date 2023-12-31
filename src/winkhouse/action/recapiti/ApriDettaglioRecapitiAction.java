package winkhouse.action.recapiti;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.helper.ProfilerHelper;
import winkhouse.helper.ProfilerHelper.PermessoDetail;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ContattiModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Contatti;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.common.RecapitiView;

public class ApriDettaglioRecapitiAction extends Action {

	private ArrayList<Anagrafiche> anagrafiche = null;
	private boolean resetContatti = false;
	
	public ApriDettaglioRecapitiAction(ArrayList<Anagrafiche> anagrafiche,boolean resetContatti) {
		this.anagrafiche = anagrafiche;
		this.resetContatti = resetContatti; 
	}

	@Override
	public void run() {

		RecapitiView rv = null;
		
		rv = (RecapitiView)PlatformUI.getWorkbench()
									 .getActiveWorkbenchWindow()
				  					 .getActivePage()
				  					 .findView(RecapitiView.ID);
		
		if (rv != null){
			
			if (((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
			    ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
				: false)){			

				if (ProfilerHelper.getInstance().getPermessoUI(RecapitiView.ID)){
					
					ArrayList<Anagrafiche> anagraficheinput = new ArrayList<Anagrafiche>();
					
					for (Anagrafiche anagrafica : anagrafiche) {
						PermessoDetail pd = ProfilerHelper.getInstance().getPermessoAnagrafica(anagrafica.getId(), false);
						if (pd == null){
							if (resetContatti){
								for (Contatti contatto: anagrafica.getContattis()) {
									anagrafica.removeFromContattis(contatto);
								}								
							}							
							anagraficheinput.add(anagrafica);
						}else{
							if (pd.getCanwrite()){
								for (Object contatto : anagrafica.getContattis()){
									((ContattiModel)contatto).setCanedit(true);
								}
							}
							anagraficheinput.add(anagrafica);
						}
					}
					rv.setAnagrafiche(anagraficheinput);
					
				}
			}else{
				if (this.anagrafiche != null){
					AnagraficheDAO anagraficheDAO = new AnagraficheDAO();
					for (Iterator<Anagrafiche> iterator = this.anagrafiche.iterator(); iterator.hasNext();) {
						Anagrafiche anagrafica = iterator.next();
						if (anagrafica.getId() != 0){
							this.anagrafiche = new ArrayList<Anagrafiche>();
							this.anagrafiche.add((Anagrafiche)anagraficheDAO.getAnagraficheById(anagrafica.getId()));						
						}
					}
				}
				rv.setAnagrafiche(this.anagrafiche);
			}
			
		}
		
	}

	
}