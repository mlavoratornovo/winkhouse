package winkhouse.dao;

import java.util.ArrayList;

import winkhouse.vo.ComuniVO;

public class ComuniDAO extends BaseDAO {

	public static String LIST_COMUNI_LIKE = "LIST_COMUNI_LIKE";
	public static String LIST_COMUNI_MATCH = "LIST_COMUNI_MATCH";
	public static String GET_COMUNE_BY_CODCOMUNE = "GET_COMUNE_BY_CODCOMUNE";
	public static String GET_PROVINCIE_BY_ANAGRAFICHE = "GET_PROVINCIE_BY_ANAGRAFICHE";
	public static String GET_PROVINCIE_BY_IMMOBILI = "GET_PROVINCIE_BY_IMMOBILI";
	public static String GET_COMUNI_BY_PROVINCIA = "GET_COMUNI_BY_PROVINCIA";
	public static String GET_COMUNI_BY_PROVINCIA_IMMOBILI = "GET_COMUNI_BY_PROVINCIA_IMMOBILI";
	public static String GET_COMUNI_BY_PROVINCIA_ANAGRAFICHE = "GET_COMUNI_BY_PROVINCIA_ANAGRAFICHE";
	
 	public ComuniDAO() {
	}

 	public ArrayList getComuniStartWith(String comune){
 		return super.getObjectsByStringFieldValue(ComuniVO.class.getName(), LIST_COMUNI_LIKE, comune+"%");
 	}
 	
 	public ArrayList getComuniEndWith(String comune){
 		return super.getObjectsByStringFieldValue(ComuniVO.class.getName(), LIST_COMUNI_LIKE, "%"+comune);
 	}

 	public ArrayList getComuniBetWeenWith(String comune){
 		return super.getObjectsByStringFieldValue(ComuniVO.class.getName(), LIST_COMUNI_LIKE, "%"+comune+"%");
 	}
 	
 	public ArrayList getComuniMatchWith(String comune){
 		return super.getObjectsByStringFieldValue(ComuniVO.class.getName(), LIST_COMUNI_MATCH, comune);
 	}
 	
 	public Object getComuniByCodComune(Integer codComune){
 		return super.getObjectById(ComuniVO.class.getName(), GET_COMUNE_BY_CODCOMUNE, codComune);
 	}
 	
 	public ArrayList getProvincieAnagrafiche(){
 		return super.list(ComuniVO.class.getName(), GET_PROVINCIE_BY_ANAGRAFICHE);
 	}
 	
 	public ArrayList getComuniByProvincia(String provincia){
 		return super.getObjectsByStringFieldValue(ComuniVO.class.getName(), GET_COMUNI_BY_PROVINCIA, provincia);
 	}

 	public ArrayList getProvincieImmobili(){
 		return super.list(ComuniVO.class.getName(), GET_PROVINCIE_BY_IMMOBILI);
 	}

 	public ArrayList getComuniByProvinciaImmobili(String provincia){
 		return super.getObjectsByStringFieldValue(ComuniVO.class.getName(), GET_COMUNI_BY_PROVINCIA_IMMOBILI,provincia);
 	}

 	public ArrayList getComuniByProvinciaAnagrafiche(String provincia){
 		return super.getObjectsByStringFieldValue(ComuniVO.class.getName(), GET_COMUNI_BY_PROVINCIA_ANAGRAFICHE,provincia);
 	}

}
