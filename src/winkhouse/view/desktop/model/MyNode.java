package winkhouse.view.desktop.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import winkhouse.dao.AffittiDAO;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.model.AbbinamentiModel;
import winkhouse.model.AffittiAnagraficheModel;
import winkhouse.model.AffittiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ColloquiModelAnagraficaCollection;
import winkhouse.model.ColloquiModelRicercaCollection;
import winkhouse.model.ColloquiModelVisiteCollection;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.PromemoriaModel;
import winkhouse.model.PromemoriaOggettiModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.desktop.DesktopView;
import winkhouse.vo.PromemoriaLinksVO;
import winkhouse.vo.TipologieColloquiVO;
import winkhouse.wizard.RicercaWizard;

public class MyNode {

	private final String id;
	private final String name;
	private final String type;
	private List<MyNode> connections;
	private boolean selected = false;
	private Integer structureLevel = 0;
	private Object objModel = null;
	private Integer desktopViewType = null;
	public final static String FILELINK = "filelink";
	public final static String URLLINK = "urllink";
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public MyNode(Integer desktopViewType, String id, String name, Object objModel, String type, Integer structureLevel) {
		this.desktopViewType = desktopViewType;
	    this.id = id;
	    this.name = name;
	    this.type = type;
	    this.connections = null;
	    this.selected = false;
	    this.objModel = objModel;
	    this.structureLevel = structureLevel;
	  }

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setConnectedTo(List connections){
		this.connections = connections;
	}
	
	public List<MyNode> getConnectedTo() {
		if (connections == null){
			if (desktopViewType == DesktopView.PROMEMORIA_TYPE){
				connections = new ArrayList<MyNode>();
				for (PromemoriaOggettiModel pom : ((PromemoriaModel)objModel).getLinkedObjects()) {
					if (pom.getTipo() == RicercaWizard.IMMOBILI){
						connections.add(new MyNode(this.desktopViewType, 
												   String.valueOf(((ImmobiliModel)pom.getLinkedObject()).getCodImmobile()), 
												   ((ImmobiliModel)pom.getLinkedObject()).toString(), 
												   (ImmobiliModel)pom.getLinkedObject(),
												   WinkhouseUtils.IMMOBILI,
												   this.structureLevel + 1));
					}
					if (pom.getTipo() == RicercaWizard.ANAGRAFICHE){
						connections.add(new MyNode(this.desktopViewType, 
												   String.valueOf(((AnagraficheModel)pom.getLinkedObject()).getCodAnagrafica()), 
												   ((AnagraficheModel)pom.getLinkedObject()).toString(), 
												   (AnagraficheModel)pom.getLinkedObject(),
												   WinkhouseUtils.ANAGRAFICHE,
												   this.structureLevel + 1));
					}
					
				}
				for (PromemoriaLinksVO plv : ((PromemoriaModel)objModel).getLinksUrls()) {
					if (plv.getIsFile()){
						connections.add(new MyNode(this.desktopViewType, 
												   String.valueOf(plv.hashCode()), 
												   plv.getUrilink(), 
												   objModel,
												   FILELINK,
												   this.structureLevel + 1));
					}else{					
						connections.add(new MyNode(this.desktopViewType, 
												   String.valueOf(plv.hashCode()), 
								   				   plv.getUrilink(), 
												   objModel,
												   URLLINK,
												   this.structureLevel + 1));
					}
					
				}
				
			}
			if (desktopViewType == DesktopView.STRUTTURA_TYPE){
				connections = new ArrayList<MyNode>();
				AffittiDAO aDAO = new AffittiDAO();
				if (objModel instanceof ImmobiliModel){
										
					ArrayList<AbbinamentiModel> alAbbinamentiModels = ((ImmobiliModel)objModel).getAnagraficheAbbinate();
					for (AbbinamentiModel am : alAbbinamentiModels) {
						MyNode mynode = new MyNode(this.desktopViewType,
								   				   am.getCodAbbinamento().toString(),
								   				   am.getAnagrafica().getCodAnagrafica().toString() + " " + am.getAnagrafica().toString(),
								   				   am,
								   				   WinkhouseUtils.ANAGRAFICHE,
												   this.structureLevel + 1);
						connections.add(mynode);
						
					}
					
					for (AnagraficheModel anagraficheModel : ((ImmobiliModel)objModel).getAnagrafichePropietarie()) {
						MyNode mynode = new MyNode(this.desktopViewType,
				   				   				   anagraficheModel.getCodAnagrafica().toString(),
				   				   				   anagraficheModel.getCodAnagrafica().toString() + " " + anagraficheModel.toString(),
				   				   				   anagraficheModel,
				   				   				   WinkhouseUtils.ANAGRAFICHE,
												   this.structureLevel + 1);
						connections.add(mynode);
						
					}
					
					ArrayList<ColloquiModelVisiteCollection> al_cmvc = ((ImmobiliModel)objModel).getColloqui();
					
					for (ColloquiModelVisiteCollection cmvc : al_cmvc) {
						
						Iterator<ColloquiModel> it = cmvc.getColloquiVisite().iterator();
						while(it.hasNext()){
							ColloquiModel cm = it.next();
							MyNode mynode = new MyNode(this.desktopViewType,
													   cm.getCodColloquio().toString(),
													   cm.getCodColloquio().toString() + " " + cm.getTipologia().toString() + " " + cm.getDataColloquio().toString(),
													   cm,
					   				   				   WinkhouseUtils.COLLOQUI,
													   this.structureLevel + 1);
							connections.add(mynode);
						}
						
					}
										
					ArrayList<AffittiModel> affittiModels = aDAO.getAffittiByCodImmobile(AffittiModel.class.getName(), ((ImmobiliModel)objModel).getCodImmobile());
					for (AffittiModel affittiModel : affittiModels) {
						MyNode mynode = new MyNode(this.desktopViewType,
												   affittiModel.getCodAffitti().toString(),
												   affittiModel.toString(),
												   affittiModel,
												   WinkhouseUtils.AFFITTI,
												   this.structureLevel + 1);
						connections.add(mynode);						
					}
				}
				if (objModel instanceof AnagraficheModel){
					
					ArrayList<ImmobiliModel> alImmobiliModels = ((AnagraficheModel)objModel).getImmobili();
					for (ImmobiliModel am : alImmobiliModels) {
						MyNode mynode = new MyNode(this.desktopViewType,
								   				   am.getCodImmobile().toString(),
								   				   am.toString(),
								   				   am,
								   				   WinkhouseUtils.IMMOBILI,
												   this.structureLevel + 1);
						connections.add(mynode);
						
					}
					ArrayList<AffittiModel> affittiModels = aDAO.getAffittiByCodAnagrafica(AffittiModel.class.getName(), ((AnagraficheModel)objModel).getCodAnagrafica());
					for (AffittiModel affittiModel : affittiModels) {
						MyNode mynode = new MyNode(this.desktopViewType,
												   affittiModel.getCodAffitti().toString(),
												   affittiModel.toString(),
												   affittiModel,
												   WinkhouseUtils.AFFITTI,
												   this.structureLevel + 1);
						connections.add(mynode);						
					}
					
//					ArrayList<AbbinamentiModel> alAbbinamentiModels = ((AnagraficheModel)objModel).getAbbinamenti();
//					for (AbbinamentiModel am : alAbbinamentiModels) {
//						MyNode mynode = new MyNode(this.desktopViewType,
//								   				   am.getCodAbbinamento().toString(),
//								   				   am.getImmobile().toString(),
//								   				   am,
//								   				   WinkhouseUtils.IMMOBILI);
//						connections.add(mynode);
//						
//					}

					ArrayList al_cmvc = ((AnagraficheModel)objModel).getColloqui();
					for (Object cmvc : al_cmvc) {
						if (cmvc instanceof ColloquiModelAnagraficaCollection){
							
							for (Object cm : ((ColloquiModelAnagraficaCollection)cmvc).getColloqui()){
								MyNode mynode = new MyNode(this.desktopViewType,
														   ((ColloquiModel)cm).getCodColloquio().toString(),
														   ((ColloquiModel)cm).getCodColloquio().toString() + " " + ((ColloquiModel)cm).getTipologia().toString() + " " + ((ColloquiModel)cm).getDataColloquio().toString(),
														   (ColloquiModel)cm,
														   WinkhouseUtils.COLLOQUI,
														   this.structureLevel + 1);
								

								connections.add(mynode);								
							}
						}
						if (cmvc instanceof ColloquiModelRicercaCollection){
							for (Object cm : ((ColloquiModelRicercaCollection)cmvc).getColloquiRicerca()){
								MyNode mynode = new MyNode(this.desktopViewType,
										   				   ((ColloquiModel)cm).getCodColloquio().toString(),
										   				   ((ColloquiModel)cm).getCodColloquio().toString() + " " + ((ColloquiModel)cm).getTipologia().toString() + " " + ((ColloquiModel)cm).getDataColloquio().toString(),
										   				   (ColloquiModel)cm,
										   				   WinkhouseUtils.COLLOQUI,
														   this.structureLevel + 1);
								
//								((ColloquiModel)cm).setCriteriRicerca(null);
//								
//								SearchEngineImmobili sei = new SearchEngineImmobili(((ColloquiModel)cm).getCriteriRicerca());
//								ArrayList alImmobili = sei.find();
//								for (Object immobile: alImmobili){
//									
//									mynode.addConnection(new MyNode(this.desktopViewType,
//																   ((ImmobiliModel)immobile).getCodImmobile().toString(),
//																   ((ImmobiliModel)immobile).toString(),
//																   ((ImmobiliModel)immobile),
//																   WinkhouseUtils.IMMOBILI));
//
//								}

								connections.add(mynode);
							}
							
						}
						
						
						
					}

				
				}
				if (objModel instanceof ColloquiModel){
					
					ColloquiModel cm = ((ColloquiModel)objModel);
					if (cm.getCodTipologiaColloquio() == 1){
						SearchEngineImmobili sei = new SearchEngineImmobili(cm.getCriteriRicerca());
						ArrayList alImmobili = sei.find();
						for (Object immobile: alImmobili){
							MyNode mynode = new MyNode(this.desktopViewType,
													   ((ImmobiliModel)immobile).getCodImmobile().toString(),
													   ((ImmobiliModel)immobile).toString(),
													   ((ImmobiliModel)immobile),
													   WinkhouseUtils.IMMOBILI,
													   this.structureLevel + 1);
							connections.add(mynode);
	
						}
					}else{
						
						for (ColloquiAnagraficheModel_Ang anagraficheModel : cm.getAnagrafiche()) {
							if (anagraficheModel.getAnagrafica() != null){
								MyNode mynode = new MyNode(this.desktopViewType,
						   				   				   anagraficheModel.getAnagrafica().getCodAnagrafica().toString(),
						   				   				   anagraficheModel.getAnagrafica().getCodAnagrafica().toString() + " " + anagraficheModel.getAnagrafica().toString(),
						   				   				   anagraficheModel,
						   				   				   WinkhouseUtils.ANAGRAFICHE,
														   this.structureLevel + 1);
								connections.add(mynode);
							}
							
						}
					}
					
				}
				if (objModel instanceof AffittiModel){
					
					for (AffittiAnagraficheModel anagraficheModel : ((AffittiModel)objModel).getAnagrafiche()) {
						if (anagraficheModel.getAnagrafica() != null){
							MyNode mynode = new MyNode(this.desktopViewType,
					   				   				   anagraficheModel.getAnagrafica().getCodAnagrafica().toString(),
					   				   				   anagraficheModel.getAnagrafica().getCodAnagrafica().toString() + " " + anagraficheModel.getAnagrafica().toString(),
					   				   				   anagraficheModel.getAnagrafica(),
					   				   				   WinkhouseUtils.ANAGRAFICHE,
													   this.structureLevel + 1);
							connections.add(mynode);
						}
					}
					
				}
			}
		}
	    return connections;
	}
	
	public Object getObjModel() {
		return objModel;
	}
	
	public void addConnection(MyNode node){
		connections = (connections == null)? new ArrayList<MyNode>() : connections;
		if (!nodeExist(node)){
			connections.add(node);
		}
	}
	
	protected boolean nodeExist(MyNode node){
		for (MyNode cnode : connections){
			if ((cnode.getType().equalsIgnoreCase(node.getType())) && 
				(cnode.getId().equalsIgnoreCase(node.getId())) && 
				(cnode.getStructureLevel() == node.getStructureLevel())){
				return true;
			}
		}
		return false;
	}
	
	public void setObjModel(Object objModel) {
		this.objModel = objModel;
	}

	public String getType() {
		return type;
	}

	public static MyNode cloneObject(MyNode obj){
		MyNode clone = new MyNode(obj.getDesktopViewType(),obj.getId(),obj.getName(),obj.getObjModel(),obj.getType(),
				   				  obj.structureLevel);
		clone.setConnectedTo(obj.getConnectedTo());
        return clone;
    }

	public Integer getDesktopViewType() {
		return desktopViewType;
	}

	public void setDesktopViewType(Integer desktopViewType) {
		this.desktopViewType = desktopViewType;
	}

	public Integer getStructureLevel() {
		return structureLevel;
	}
}
