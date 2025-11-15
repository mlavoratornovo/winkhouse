package winkhouse.dao;

import java.util.ArrayList;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Comuni;
import winkhouse.orm.Immobili;
import winkhouse.util.WinkhouseUtils;
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
 	
 	public ArrayList<Comuni> getComuniStartWith(String comune){
 		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
 		return new ArrayList<Comuni>(ObjectSelect.query(Comuni.class)
		 		   									  .where(Comuni.COMUNE.startsWithIgnoreCase(comune))
		 		   									  .select(context)); 		
 	}
 	
 	public ArrayList<Comuni> getComuniEndWith(String comune){
 		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
 		return new ArrayList<Comuni>(ObjectSelect.query(Comuni.class)
		 		   									  .where(Comuni.COMUNE.endsWithIgnoreCase(comune))
		 		   									  .select(context)); 		
 	}

 	public ArrayList<Comuni> getComuniBetWeenWith(String comune){
 		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
 		return new ArrayList<Comuni>(ObjectSelect.query(Comuni.class)
		 		   									  .where(Comuni.COMUNE.containsIgnoreCase(comune))
		 		   									  .select(context)); 		

 	}
 	
 	public ArrayList<Comuni> getComuniMatchWith(String comune){
 		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
 		return new ArrayList<Comuni>(ObjectSelect.query(Comuni.class)
		 		   									  .where(Comuni.COMUNE.eq(comune))
		 		   									  .select(context));
 	}
 	 	
 	public ArrayList<Comuni> getProvincieAnagrafiche(){
 		ArrayList<Comuni> retVal = new ArrayList<Comuni>();
 		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
 		ObjectSelect.query(Anagrafiche.class);
		ArrayList<String> al = new ArrayList<String>(ObjectSelect.columnQuery(Anagrafiche.class,Anagrafiche.PROVINCIA)
		 		   									  			 .distinct()
		 		   									  			 .select(context));
 		al.forEach(provincia -> {
 			Comuni c = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Comuni.class);
 			c.setProvincia(provincia);
 			retVal.add(c);
 		});
 		return retVal;
 	}
 	
 	public ArrayList<Comuni> getComuniByProvincia(String provincia){
 		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
 		return new ArrayList<Comuni>(ObjectSelect.query(Comuni.class)
		 		   									  .where(Comuni.PROVINCIA.eq(provincia))
		 		   									  .select(context));

 	}

 	public ArrayList<Comuni> getProvincieImmobili(){
 		ArrayList<Comuni> retVal = new ArrayList<Comuni>();
 		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
 		ObjectSelect.query(Immobili.class);
		ArrayList<String> al = new ArrayList<String>(ObjectSelect.columnQuery(Immobili.class,Immobili.PROVINCIA)
		 		   									  			 .distinct()
		 		   									  			 .select(context));
 		al.forEach(provincia -> {
 			Comuni c = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Comuni.class);
 			c.setProvincia(provincia);
 			retVal.add(c);
 		});
 		return retVal;
 	}

 	public ArrayList<Comuni> getComuniByProvinciaImmobili(String provincia){
 		ArrayList<Comuni> retVal = new ArrayList<Comuni>();
 		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
 		ObjectSelect.query(Immobili.class);
		ArrayList<String> al = new ArrayList<String>(ObjectSelect.columnQuery(Immobili.class,Immobili.CITTA)
		 		   									  			 .distinct()
		 		   									  			 .where(Immobili.PROVINCIA.eq(provincia))
		 		   									  			 .select(context));
 		al.forEach(comune -> {
 			Comuni c = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Comuni.class);
 			c.setComune(comune);
 			retVal.add(c);
 		});
 		return retVal;
 		 		
 	}

 	public ArrayList<Comuni> getComuniByProvinciaAnagrafiche(String provincia){
 		ArrayList<Comuni> retVal = new ArrayList<Comuni>();
 		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
 		ObjectSelect.query(Anagrafiche.class);
		ArrayList<String> al = new ArrayList<String>(ObjectSelect.columnQuery(Anagrafiche.class,Anagrafiche.CITTA)
		 		   									  			 .distinct()
		 		   									  			 .where(Anagrafiche.PROVINCIA.eq(provincia))
		 		   									  			 .select(context));
 		al.forEach(comune -> {
 			Comuni c = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Comuni.class);
 			c.setComune(comune);
 			retVal.add(c);
 		});
 		return retVal;
 	}

}
