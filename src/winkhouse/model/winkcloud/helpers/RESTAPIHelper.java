package winkhouse.model.winkcloud.helpers;

import static spark.Spark.get;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliMethodName;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.ObjectId;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.ObjectIdQuery;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.cayenne.query.SelectQuery;

import javolution.util.FastList;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.stream.XMLStreamException;

import com.google.gdata.data.acl.AclScope.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import spark.Request;
import winkhouse.db.orm.CayenneContextManager;
import winkhouse.engine.search.ImmobiliSearchEngine;
import winkhouse.engine.search.SearchEngineAnagrafiche;
import winkhouse.engine.search.SearchEngineColloqui;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.CloudQueryOrigin;
import winkhouse.model.winkcloud.RicercaModel;
import winkhouse.orm.Abbinamenti;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Classicliente;
import winkhouse.orm.Classienergetiche;
import winkhouse.orm.Colloqui;
import winkhouse.orm.Colloquianagrafiche;
import winkhouse.orm.Immobili;
import winkhouse.orm.Riscaldamenti;
import winkhouse.orm.Statoconservativo;
import winkhouse.orm.Tipologiastanze;
import winkhouse.orm.Tipologiecolloqui;
import winkhouse.orm.Tipologiecontatti;
import winkhouse.orm.Tipologieimmobili;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipologiaContattiVO;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.vo.TipologieColloquiVO;
import winkhouse.vo.TipologieImmobiliVO;

public class RESTAPIHelper {
	
	public final static String XML_TAG_MOBILE ="ricerca";

	public RESTAPIHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public String getClassiEnergetiche(){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<ClasseEnergeticaVO> returnValue = new ArrayList<ClasseEnergeticaVO>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		List<Classienergetiche> classiEnergetiche = ObjectSelect.query(Classienergetiche.class).select(context);
		for (Iterator iterator = classiEnergetiche.iterator(); iterator
				.hasNext();) {
			Classienergetiche classienergetiche2 = (Classienergetiche) iterator
					.next();
			returnValue.add(new ClasseEnergeticaVO(classienergetiche2));
			
		}
		return gson.toJson(returnValue);
	}
	
	public String getClasseEnergeticaById(Integer id){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		
				
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		
		Classienergetiche classeEnergetica = (Classienergetiche) Cayenne.objectForPK(context,"Classienergetiche", id);
		
		return gson.toJson(new ClasseEnergeticaVO(classeEnergetica));
	}
	
	public String getTipologieImmobili(){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<TipologieImmobiliVO> returnValue = new ArrayList<TipologieImmobiliVO>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		List<Tipologieimmobili> tipologieimmobili = ObjectSelect.query(Tipologieimmobili.class).select(context);
		for (Iterator iterator = tipologieimmobili.iterator(); iterator
				.hasNext();) {
			Tipologieimmobili tipologieimmobili2 = (Tipologieimmobili) iterator
					.next();
			returnValue.add(new TipologieImmobiliVO(tipologieimmobili2));
			
		}
		return gson.toJson(returnValue);
	}
	
	public String getTipologieImmobiliById(Integer id){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		
				
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		
		Tipologieimmobili tipologieImmobili = (Tipologieimmobili) Cayenne.objectForPK(context,"Tipologieimmobili", id);
		
		return gson.toJson(new TipologieImmobiliVO(tipologieImmobili));
	}	
	
	public String getClassiClienti(){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<ClassiClientiVO> returnValue = new ArrayList<ClassiClientiVO>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		List<Classicliente> classiCliente = ObjectSelect.query(Classicliente.class).select(context);
		for (Iterator iterator = classiCliente.iterator(); iterator
				.hasNext();) {
			Classicliente classicliente2 = (Classicliente) iterator
					.next();
			returnValue.add(new ClassiClientiVO(classicliente2));
			
		}
		return gson.toJson(returnValue);
	}
	
	public String getClassiClientiById(Integer id){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		
				
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		
		Classicliente classicliente = (Classicliente) Cayenne.objectForPK(context,"Classicliente", id);
		
		return gson.toJson(new ClassiClientiVO(classicliente));
	}	

	public String getTipologieColloqui(){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<TipologieColloquiVO> returnValue = new ArrayList<TipologieColloquiVO>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		List<Tipologiecolloqui> tipologiecolloqui = ObjectSelect.query(Tipologiecolloqui.class).select(context);
		for (Iterator iterator = tipologiecolloqui.iterator(); iterator
				.hasNext();) {
			Tipologiecolloqui tipologiecolloqui2 = (Tipologiecolloqui) iterator
					.next();
			returnValue.add(new TipologieColloquiVO(tipologiecolloqui2));
			
		}
		return gson.toJson(returnValue);
	}
	
	public String getTipologieColloquiById(Integer id){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		
				
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		
		Tipologiecolloqui tipologiecolloqui = (Tipologiecolloqui) Cayenne.objectForPK(context,"Tipologiecolloqui", id);
		
		return gson.toJson(new TipologieColloquiVO(tipologiecolloqui));
	}	

	public String getTipologieStanze(){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<TipologiaStanzeVO> returnValue = new ArrayList<TipologiaStanzeVO>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		List<Tipologiastanze> tipologiastanze = ObjectSelect.query(Tipologiastanze.class).select(context);
		for (Iterator iterator = tipologiastanze.iterator(); iterator
				.hasNext();) {
			Tipologiastanze tipologiastanze2 = (Tipologiastanze) iterator
					.next();
			returnValue.add(new TipologiaStanzeVO(tipologiastanze2));
			
		}
		return gson.toJson(returnValue);
	}
	
	public String getTipologieStanzeById(Integer id){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		
				
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		
		Tipologiastanze tipologiastanze = (Tipologiastanze) Cayenne.objectForPK(context,"Tipologiastanze", id);
		
		return gson.toJson(new TipologiaStanzeVO(tipologiastanze));
	}	
	
	public String getTipologieContattiById(Integer id){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		
				
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		
		Tipologiecontatti tipologiecontatti = (Tipologiecontatti) Cayenne.objectForPK(context,"Tipologiecontatti", id);
		
		return gson.toJson(new TipologiaContattiVO(tipologiecontatti));
	}	

	public String getTipologieContatti(){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<TipologiaContattiVO> returnValue = new ArrayList<TipologiaContattiVO>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		List<Tipologiecontatti> tipologiecontatti = ObjectSelect.query(Tipologiecontatti.class).select(context);
		for (Iterator iterator = tipologiecontatti.iterator(); iterator
				.hasNext();) {
			Tipologiecontatti tipologiecontatti2 = (Tipologiecontatti) iterator
					.next();
			returnValue.add(new TipologiaContattiVO(tipologiecontatti2));
			
		}
		return gson.toJson(returnValue);
	}
	
	public String getRiscaldamentiById(Integer id){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		
				
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		
		Riscaldamenti riscaldamenti = (Riscaldamenti) Cayenne.objectForPK(context,"Riscaldamenti", id);
		
		return gson.toJson(new RiscaldamentiVO(riscaldamenti));
	}	

	public String getRiscaldamenti(){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<RiscaldamentiVO> returnValue = new ArrayList<RiscaldamentiVO>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		List<Riscaldamenti> riscaldamenti = ObjectSelect.query(Riscaldamenti.class).select(context);
		for (Iterator iterator = riscaldamenti.iterator(); iterator
				.hasNext();) {
			Riscaldamenti riscaldamenti2 = (Riscaldamenti) iterator
					.next();
			returnValue.add(new RiscaldamentiVO(riscaldamenti2));
			
		}
		return gson.toJson(returnValue);
	}

	public String getStatoConservativoById(Integer id){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		
				
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		
		Statoconservativo statoconservativo = (Statoconservativo) Cayenne.objectForPK(context,"Statoconservativo", id);
		
		return gson.toJson(new StatoConservativoVO(statoconservativo));
	}	

	public String getStatoConservativo(){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<StatoConservativoVO> returnValue = new ArrayList<StatoConservativoVO>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		List<Statoconservativo> statoconservativo = ObjectSelect.query(Statoconservativo.class).select(context);
		for (Iterator iterator = statoconservativo.iterator(); iterator
				.hasNext();) {
			Statoconservativo statoconservativo2 = (Statoconservativo) iterator
					.next();
			returnValue.add(new StatoConservativoVO(statoconservativo2));
			
		}
		return gson.toJson(returnValue);
	}
	
	public CriteriRicercaModel serchCoreCriteria(Request req){
		CriteriRicercaModel returnValue = new CriteriRicercaModel();
		String id = null;
		try {
			id = req.queryParams("id");
		} catch (Exception e) {
			id = "";
		}	
		returnValue.setFromValue(id);
		String tipo = req.queryParams("tipo");
		switch (tipo) {
		case "ce": 
			returnValue.setGetterMethodName(ImmobiliMethodName.GET_CODCLASSEENERGETICA);	
			break;
		case "ti":
			returnValue.setGetterMethodName(ImmobiliMethodName.GET_CODTIPOLOGIA);
			break;
		case "cc":
			returnValue.setGetterMethodName(AnagraficheMethodName.GET_CODCLASSECLIENTE);
			break;
		case "tc":			
			returnValue.setGetterMethodName(ColloquiMethodName.GET_CODTIPOLOGIA);
			break;
		case "ts":
			returnValue.setGetterMethodName("Tipi stanze");
			break;
		case "tcon":
			returnValue.setGetterMethodName("Tipi contatti");
			break;
		case "ri":
			returnValue.setGetterMethodName(ImmobiliMethodName.GET_CODRISCALDAMENTO);
			break;
		case "sc":
			returnValue.setGetterMethodName(ImmobiliMethodName.GET_CODSTATO);
			break;

		default:
			break;
		}

		return returnValue;
	}
	
	public String searchCore(Request req){
		
		String returnValue = null;
		String tipo = req.queryParams("tipo");
		Integer id = null;
		try {
			id = Integer.valueOf(req.queryParams("id"));
		} catch (Exception e) {
			id = null;
		}
		switch (tipo) {
		case "ce": 
			if (id == null){
				returnValue = this.getClassiEnergetiche();	
			}else{
				returnValue = this.getClasseEnergeticaById(id);
			}			
			break;
		case "ti":
			if (id == null){
				returnValue = this.getTipologieImmobili();	
			}else{
				returnValue = this.getTipologieImmobiliById(id);
			}			
			break;
		case "cc":
			if (id == null){
				returnValue = this.getClassiClienti();	
			}else{
				returnValue = this.getClassiClientiById(id);
			}			
			break;
		case "tc":
			if (id == null){
				returnValue = this.getTipologieColloqui();	
			}else{
				returnValue = this.getTipologieColloquiById(id);
			}			
			break;
		case "ts":
			if (id == null){
				returnValue = this.getTipologieStanze();	
			}else{
				returnValue = this.getTipologieStanzeById(id);
			}			
			break;
		case "tcon":
			if (id == null){
				returnValue = this.getTipologieContatti();	
			}else{
				returnValue = this.getTipologieContattiById(id);
			}			
			break;
		case "ri":
			if (id == null){
				returnValue = this.getRiscaldamenti();	
			}else{
				returnValue = this.getRiscaldamentiById(id);
			}			
			break;
		case "sc":
			if (id == null){
				returnValue = this.getStatoConservativo();	
			}else{
				returnValue = this.getStatoConservativoById(id);
			}			
			break;

		default:
			break;
		}
		
		return returnValue;
		
	}
	
	public String getColloquiByImmobile(Integer idImmobile){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<ColloquiVO> returnValue = new ArrayList<ColloquiVO>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		Immobili i = (Immobili) Cayenne.objectForPK(context,"Immobili", idImmobile);
		for (Iterator iterator = i.getColloquis().iterator(); iterator
				.hasNext();) {
			Colloqui colloqui = (Colloqui) iterator
					.next();
			returnValue.add(new ColloquiVO(colloqui));
			
		}

		return gson.toJson(returnValue);
	}
	
	public String getColloquiByAnagrafica(Integer idAnagrafica){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<ColloquiVO> returnValue = new ArrayList<ColloquiVO>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		Anagrafiche i = (Anagrafiche) Cayenne.objectForPK(context,"Anagrafiche", idAnagrafica);		
		for (Iterator iterator = i.getColloquianagrafiches().iterator(); iterator
				.hasNext();) {
			Colloquianagrafiche colloqui = (Colloquianagrafiche) iterator
					.next();
			returnValue.add(new ColloquiVO(colloqui.getColloqui()));
			
		}

		return gson.toJson(returnValue);
	}
	
	public String getImmobiliAbbinatiByAnagrafica(Integer idAnagrafica){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<ImmobiliModel> returnValue = new ArrayList<ImmobiliModel>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		Anagrafiche i = (Anagrafiche) Cayenne.objectForPK(context,"Anagrafiche", idAnagrafica);		
		for (Iterator iterator = i.getAbbinamentis().iterator(); iterator
				.hasNext();) {
			Abbinamenti abbinamenti = (Abbinamenti) iterator
					.next();
			returnValue.add(new ImmobiliModel(abbinamenti.getImmobili()));
			
		}

		return gson.toJson(returnValue);
	}

	public String getAnagraficheAbbinateByImmobile(Integer idImmobile){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		

		ArrayList<AnagraficheModel> returnValue = new ArrayList<AnagraficheModel>();
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		Immobili i = (Immobili) Cayenne.objectForPK(context,"Immobili", idImmobile);
		
		for (Iterator iterator = i.getAbbinamentis().iterator(); iterator
				.hasNext();) {
			Abbinamenti abbinamenti = (Abbinamenti) iterator
					.next();
			returnValue.add(new AnagraficheModel(abbinamenti.getAnagrafiche()));
			
		}

		return gson.toJson(returnValue);
	}
	
	public boolean postImmobile(Request req, CloudQueryModel cqm){
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		
		java.lang.reflect.Type listOfMyClassObject = new TypeToken<ArrayList<ImmobiliModel>>() {}.getType();
		List<ImmobiliModel> retval = gson.fromJson(req.body(), listOfMyClassObject);
		if (cqm.getResults() != null){
			cqm.getResults().addAll(new ArrayList(retval));
		}else{
			cqm.setResults(new ArrayList(retval));
		}
		
		return true;
	}
	
	public ArrayList searchItems(Request req){
		
		ArrayList returnValue = new ArrayList();
		ArrayList<CriteriRicercaModel> criteri = new ArrayList<CriteriRicercaModel>();		
		
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();		
		
		java.lang.reflect.Type listOfMyClassObject = new TypeToken<ArrayList<RicercaModel>>() {}.getType();
		List<RicercaModel> retval = gson.fromJson(req.body(), listOfMyClassObject);
		returnValue.add(retval);
		
		Iterator<RicercaModel> it = retval.iterator();
		
		while (it.hasNext()) {
			RicercaModel type = it.next();
			criteri.add(type.toCriteriRicercaModel());
		}
		if (criteri.size()>0){
			criteri.get(criteri.size()-1).setLogicalOperator("");
		}

		if (retval.size() > 0 && retval.get(0).getSearchType().equalsIgnoreCase("SEARCH_IMMOBILI")){
			
			try {
				returnValue.add(searchImmobiliData(criteri));
				returnValue.add(CloudQueryOrigin.SEARCH_IMMOBILI);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return returnValue;
		}
		if (retval.size() > 0 && retval.get(0).getSearchType().equalsIgnoreCase("SEARCH_ANAGRAFICHE")){
			try {
				returnValue.add(searchAngraficheData(criteri));
				returnValue.add(CloudQueryOrigin.SEARCH_ANAGRAFICHE);
			} catch (Exception e) {
				e.printStackTrace();
			}				
			return returnValue;
		}
		if (retval.size() > 0 && retval.get(0).getSearchType().equalsIgnoreCase("SEARCH_COLLOQUI")){
			try {
				returnValue.add(searchColloquiData(criteri));
				returnValue.add(retval.get(0).getSearchType());
			} catch (Exception e) {
				e.printStackTrace();
			}				
			return returnValue;
		}

		
		return null;
	}
	
	public ArrayList loadFromString(String exportXMLString,String xmlTag,Class classToDeserialize){
		
		ArrayList returnValue = null;
		XMLObjectReader xmlor = null;
		try {
			
			xmlor = XMLObjectReader.newInstance(new ByteArrayInputStream(exportXMLString.getBytes()));
			xmlor.setBinding(getBinding());
			returnValue = new ArrayList();
			while (xmlor.hasNext()){
				
				Object o = xmlor.read(xmlTag, classToDeserialize);
				returnValue.add(o);
				
			}
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (xmlor != null){
				try {
					xmlor.close();
				} catch (XMLStreamException e) {
					xmlor = null;
				}
			}			
		}
		
		return returnValue;
	}
	
	private XMLBinding getBinding(){
		
		XMLBinding returnvalue = new XMLBinding();
		
		returnvalue.setAlias(RicercaModel.class, "ricerca");
		returnvalue.setAlias(FastList.class, "lista");
		
		return returnvalue;
		
	}

	
	protected String searchImmobiliData(ArrayList<CriteriRicercaModel> criteria){
		SearchEngineImmobili sei = new SearchEngineImmobili(criteria);
		ArrayList immobili = sei.find();
		for (Object object : immobili) {
			((ImmobiliModel)object).resolveDepedencies();
		}
		GsonBuilder jsonBuilder = new GsonBuilder();		
		Gson gson = jsonBuilder.create();
		
		return gson.toJson(immobili);

	}
	
	public String searchAngraficheData(ArrayList<CriteriRicercaModel> criteria){
		SearchEngineAnagrafiche sea = new SearchEngineAnagrafiche(criteria);
		ArrayList anagrafiche = sea.find();
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();
		return gson.toJson(anagrafiche);
		
	}

	public String searchColloquiData(ArrayList<CriteriRicercaModel> criteria){
		SearchEngineColloqui sec = new SearchEngineColloqui(criteria);
		ArrayList colloqui = sec.find();
		GsonBuilder jsonBuilder = new GsonBuilder();
		Gson gson = jsonBuilder.create();
		return gson.toJson(colloqui);
		
	}

}
