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
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.common.RecapitiView;

public class ApriDettaglioRecapitiAction extends Action {

	private ArrayList<AnagraficheModel> anagrafiche = null;
	private boolean resetContatti = false;
	
	public ApriDettaglioRecapitiAction(ArrayList<AnagraficheModel> anagrafiche,boolean resetContatti) {
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
					
					ArrayList<AnagraficheModel> anagraficheinput = new ArrayList<AnagraficheModel>();
					
					for (AnagraficheModel anagrafica : anagrafiche) {
						PermessoDetail pd = ProfilerHelper.getInstance().getPermessoAnagrafica(anagrafica.getCodAnagrafica(), false);
						if (pd == null){
							if (resetContatti){
								anagrafica.setContatti(null);
							}							
							anagraficheinput.add(anagrafica);
						}else{
							if (pd.getCanwrite()){
								for (Object contatto : anagrafica.getContatti()){
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
					for (Iterator<AnagraficheModel> iterator = this.anagrafiche.iterator(); iterator.hasNext();) {
						AnagraficheModel anagrafica = iterator.next();
						if (anagrafica.getCodAnagrafica() != null){
							this.anagrafiche = new ArrayList<AnagraficheModel>();
							this.anagrafiche.add((AnagraficheModel)anagraficheDAO.getAnagraficheById(AnagraficheModel.class.getName(), anagrafica.getCodAnagrafica()));						
						}
					}
				}
				rv.setAnagrafiche(this.anagrafiche);
			}
			
		}
		
	}

	
}
