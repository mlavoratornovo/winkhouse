package winkhouse.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import winkhouse.dao.PermessiDAO;
import winkhouse.dao.PermessiUIDAO;
import winkhouse.engine.search.SearchEngineAnagrafiche;
import winkhouse.engine.search.SearchEngineColloqui;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.PermessiModel;
import winkhouse.model.PermessiUIModel;
import winkhouse.orm.Immobili;
import winkhouse.perspective.DesktopPerspective;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;

public class ProfilerHelper {

	private static ProfilerHelper instance = null;
	private HashMap<String,Boolean> permessiui = null;
	private HashMap<Integer,PermessoDetail> permessicolloqui = null;	
	private HashMap<Integer,PermessoDetail> permessiimmobili = null;
	private HashMap<Integer,PermessoDetail> permessianagrafiche = null;
	private HashMap<Integer,PermessoDetail> permessiaffitti = null;
	
	public class PermessoDetail{
		
		private Boolean isnot = null;
		private Boolean canwrite = null;
		
		public PermessoDetail(Boolean isnot,Boolean canwrite){
			this.isnot = isnot;
			this.canwrite = canwrite;
		}

		public Boolean getIsnot() {
			return isnot;
		}

		public void setIsnot(Boolean isnot) {
			this.isnot = isnot;
		}

		public Boolean getCanwrite() {
			return canwrite;
		}

		public void setCanwrite(Boolean canwrite) {
			this.canwrite = canwrite;
		}
		
	}
	
	private ProfilerHelper(){
		if (WinkhouseUtils.getInstance().getLoggedAgent()!= null){
			loadPermessiUIModels();
		}
	}
	
	public static ProfilerHelper getInstance(){
		if (instance == null){
			instance = new ProfilerHelper();			
		}
		return instance;
	}
	
	protected void loadPermessiUIModels(){
		
		permessiui = new HashMap<String,Boolean>();
		PermessiUIDAO pUIDAO = new PermessiUIDAO();
		ArrayList<PermessiUIModel> puimodels = pUIDAO.getPermessiByAgente(PermessiUIModel.class.getName(), 
																		  WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
		
		for (PermessiUIModel permessiUIModel : puimodels) {
			
			if (permessiUIModel.getDialogId() != null){
				permessiui.put(permessiUIModel.getDialogId(), true);
			}
			if (permessiUIModel.getViewId() != null){
				permessiui.put(permessiUIModel.getViewId(), true);
			}
			if (permessiUIModel.getPerspectiveId() != null){
				permessiui.put(permessiUIModel.getPerspectiveId(), true);
			}
			
		}
		
	}
	
	public boolean isLoginActive(){
		
		return (WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
				? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
				: false;
	}
	
	public HashMap<Integer,PermessoDetail> getPermessiImmobile(Boolean clearCache){
		
		if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
			
			if (permessiimmobili == null){
				permessiimmobili = new HashMap<Integer,PermessoDetail>();
				PermessiDAO pDAO = new PermessiDAO();
				ArrayList<PermessiModel> pmodels = pDAO.getPermessiImmobiliByAgente(PermessiModel.class.getName(), 
										 											WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
				
				Iterator<PermessiModel> it = pmodels.iterator();
				while(it.hasNext()){
					PermessiModel pm = it.next();
					SearchEngineImmobili sei = new SearchEngineImmobili(pm.getRicercaModel().getCriteri());
					ArrayList<Immobili> immobili = sei.find();
					for (Immobili immobiliModel : immobili) {				
						permessiimmobili.put(immobiliModel.getCodImmobile(), new PermessoDetail(pm.getIsNot(),pm.getCanWrite()));
					}
				}
				
			}
		}else{
			permessiimmobili = new HashMap<Integer,PermessoDetail>();
		}
		
		return permessiimmobili;
				
	}

	public HashMap<Integer,PermessoDetail> getPermessiColloqui(Boolean clearCache){
		
		if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
			
			if (permessicolloqui == null){
				permessicolloqui = new HashMap<Integer,PermessoDetail>();
				PermessiDAO pDAO = new PermessiDAO();
				ArrayList<PermessiModel> pmodels = pDAO.getPermessiImmobiliByAgente(PermessiModel.class.getName(), 
										 											WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
				
				Iterator<PermessiModel> it = pmodels.iterator();
				while(it.hasNext()){
					PermessiModel pm = it.next();
					SearchEngineColloqui sei = new SearchEngineColloqui(pm.getRicercaModel().getCriteri());
					ArrayList<ColloquiModel> colloqui = sei.find();
					for (ColloquiModel colloquiModel : colloqui) {				
						permessicolloqui.put(colloquiModel.getCodColloquio(), new PermessoDetail(pm.getIsNot(),pm.getCanWrite()));
					}
				}
				
			}
		}else{
			permessicolloqui = new HashMap<Integer,PermessoDetail>();
		}
		
		return permessicolloqui;
				
	}

	public HashMap<Integer,PermessoDetail> getPermessiAffitto(Boolean clearCache){
		
		if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
			
		
			if (permessiaffitti == null){
				permessiaffitti = new HashMap<Integer,PermessoDetail>();
				PermessiDAO pDAO = new PermessiDAO();
				ArrayList<PermessiModel> pmodels = pDAO.getPermessiAffittiByAgente(PermessiModel.class.getName(), 
										 										   WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
				
				Iterator<PermessiModel> it = pmodels.iterator();
				while(it.hasNext()){
					PermessiModel pm = it.next();
					SearchEngineImmobili sei = new SearchEngineImmobili(pm.getRicercaModel().getCriteri());
					ArrayList<Immobili> immobili = sei.find();
					for (Immobili immobiliModel : immobili) {				
						permessiaffitti.put(immobiliModel.getCodImmobile(), new PermessoDetail(pm.getIsNot(),pm.getCanWrite()));
					}
				}
				
			}
			
		}else{
			permessiaffitti = new HashMap<Integer,PermessoDetail>();
		}
		
		return permessiaffitti;
		
	}

	public HashMap<Integer,PermessoDetail> getPermessiAnagrafica(Boolean clearCache){
		
	
		if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
				
			if (permessianagrafiche == null){
				permessianagrafiche = new HashMap<Integer,PermessoDetail>();
				PermessiDAO pDAO = new PermessiDAO();
				ArrayList<PermessiModel> pmodels = pDAO.getPermessiAnagraficheByAgente(PermessiModel.class.getName(), 
										 										   	   WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
				
				Iterator<PermessiModel> it = pmodels.iterator();
				while(it.hasNext()){
					PermessiModel pm = it.next();
					SearchEngineAnagrafiche sea = new SearchEngineAnagrafiche(pm.getRicercaModel().getCriteri());
					ArrayList<AnagraficheModel> anagrafiche = sea.find();
					for (AnagraficheModel anagraficaModel : anagrafiche) {				
						permessianagrafiche.put(anagraficaModel.getCodAnagrafica(), new PermessoDetail(pm.getIsNot(),pm.getCanWrite()));
					}
				}
				
			}
			
		}else{
			permessianagrafiche = new HashMap<Integer,PermessoDetail>();
		}
		
		return permessianagrafiche;
		
	}

	public PermessoDetail getPermessoImmobile(Integer codImmobile,Boolean clearCache){
		
		return getPermessiImmobile(clearCache).get(codImmobile);
		
	}

	public PermessoDetail getPermessoAffitto(Integer codImmobile,Boolean clearCache){
		
		return getPermessiAffitto(clearCache).get(codImmobile);
		
	}	

	public PermessoDetail getPermessoAnagrafica(Integer codAnagrafica,Boolean clearCache){
		
		return getPermessiAnagrafica(clearCache).get(codAnagrafica);
		
	}	

	public ArrayList filterImmobili(ArrayList immobili,Boolean clearCache){
		
		if (isLoginActive()){
			ArrayList filteredResult = new ArrayList();
			Iterator it = immobili.iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof ImmobiliModel){
					PermessoDetail pd = getPermessiImmobile(clearCache).get(((ImmobiliModel)o).getCodImmobile());
					if (pd != null){
						if (!pd.getIsnot()){
							filteredResult.add(o);
						}
					}else{
						if ((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT) != null) && 
								(Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT)) == false)){					         
								filteredResult.add(o);
							}
					}
					
				}else{
					filteredResult.add(o);
				}
			}
			
			return filteredResult;
			
		}else{
			return immobili;
		}
				
	}

	public ArrayList filterColloqui(ArrayList colloqui,Boolean clearCache){
		
		if (isLoginActive()){
			ArrayList filteredResult = new ArrayList();
			Iterator it = colloqui.iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof ColloquiModel){
					PermessoDetail pd = getPermessiColloqui(clearCache).get(((ColloquiModel)o).getCodColloquio());
					if (pd != null){
						if (!pd.getIsnot()){
							filteredResult.add(o);
						}
					}else{
						if ((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT) != null) && 
								(Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT)) == false)){					         
								filteredResult.add(o);
							}
					}
					
				}else{
					filteredResult.add(o);
				}
			}
			
			return filteredResult;
			
		}else{
			return colloqui;
		}
				
	}

	public ArrayList filterAnagrafiche(ArrayList anagrafiche,Boolean clearCache){
		
		if (isLoginActive()){
			ArrayList filteredResult = new ArrayList();
			Iterator it = anagrafiche.iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof AnagraficheModel){
					PermessoDetail pd = getPermessiAnagrafica(clearCache).get(((AnagraficheModel)o).getCodAnagrafica());
					if (pd != null){
						if (!pd.getIsnot()){
							filteredResult.add(o);
						}
					}else{
						if ((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT) != null) && 
							(Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT)) == false)){					         
							filteredResult.add(o);
						}
					}
					
				}else{
					filteredResult.add(o);
				}
			}
			
			return filteredResult;
			
		}else{
			return anagrafiche;
		}
		
	}
	
	public ArrayList filterAffitti(ArrayList immobili,Boolean clearCache){
		
		if (isLoginActive()){
			ArrayList filteredResult = new ArrayList();
			Iterator it = immobili.iterator();
			while(it.hasNext()){
				Object o = it.next();
				if (o instanceof ImmobiliModel){
					PermessoDetail pd = getPermessiAffitto(clearCache).get(((ImmobiliModel)o).getCodImmobile());
					if (pd != null){
						if (!pd.getIsnot()){
							filteredResult.add(o);
						}
					}else{
						if ((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT) != null) && 
								(Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT)) == false)){					         
								filteredResult.add(o);
							}
					}
					
				}else{
					filteredResult.add(o);
				}
			}
			
			return filteredResult;
			
		}else{
			return immobili;
		}
		
	}
	
	public boolean canOpenImmobileDetail(Integer codImmobile){
		if (getPermessoImmobile(codImmobile, false) != null){
			return true;
		}else{
			if ((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT) != null)
			     ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT))
			     : false){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public boolean getPermessoUI(String UIId){
		
		if (((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICTUI) != null)
			  ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICTUI))
			  : false) &&
			permessiui != null){
			
			if (!UIId.equalsIgnoreCase(DesktopPerspective.ID)){
				return permessiui.containsKey(UIId);
			}else{
				return true;
			}
			
		}else{
			return true;
		}
		
	}

	//public boolean getPermesso()
}