package winkhouse.engine.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.orm.Colloquicriteriricerca;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliAffittiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.QueryConst;
import winkhouse.util.WinkhouseUtils;


public class QueryBuilder {
	
	private SimpleDateFormat formatterENG = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.s");
	private SimpleDateFormat formatterIT = new SimpleDateFormat("dd/MM/yyyy");
	private AttributeDAO attributeDAO = null;
	
	public QueryBuilder(){
		attributeDAO = new AttributeDAO();
	}	
	
	Comparator<Colloquicriteriricerca> cLineNumber = new Comparator<Colloquicriteriricerca>(){

		@Override
		public int compare(Colloquicriteriricerca o1, Colloquicriteriricerca o2){
			if ((o1.getLineNumber() == null) && (o2.getLineNumber() == null)){
				return 0;
			}else if ((o1.getLineNumber() != null) && (o2.getLineNumber() == null)){
				return -1;
			}else if ((o1.getLineNumber() == null) && (o2.getLineNumber() != null)){
				return 1;
			}else if (o1.getLineNumber().intValue() == o2.getLineNumber().intValue()){
				return 0;
			}else if (o1.getLineNumber().intValue() < o2.getLineNumber().intValue()){
				return -1;
			}else{
				return 1;
			}			
		}
		
	};

	Comparator<Colloquicriteriricerca> cMethodName = new Comparator<Colloquicriteriricerca>(){

		@Override
		public int compare(Colloquicriteriricerca o1, Colloquicriteriricerca o2){
			return o1.getGettermethodname().compareTo(o2.getGettermethodname());			
		}
		
	};
	
	public String buildQueryAnagrafiche(ArrayList criteria){
		
		boolean insertCampiPersonali = (findGetCampiPersonaliMethod(criteria) != null);
		Boolean storico = WinkhouseUtils.getInstance().getTipoArchivio();
		
		String returnValue = "SELECT DISTINCT A.* FROM ANAGRAFICHE A " +
				             "INNER JOIN (SELECT CODANAGRAFICA FROM ANAGRAFICHE WHERE STORICO = "+ String.valueOf(storico) + ") AST " +
				             "ON A.CODANAGRAFICA = AST.CODANAGRAFICA ";
		
		Collections.sort(criteria, cMethodName);
		Colloquicriteriricerca ccrKey = new Colloquicriteriricerca();
		ccrKey.setGettermethodname(AnagraficheMethodName.GET_CONTATTI);
		int indexContatti = Collections.binarySearch(criteria, ccrKey, cMethodName);
		if (indexContatti > -1){
			returnValue += "LEFT JOIN CONTATTI C ON A.CODANAGRAFICA = C.CODANAGRAFICA ";
		}
		ccrKey.setGettermethodname(AnagraficheMethodName.GET_DESCRIZIONE_CLASSECLIENTE);
		int indexClassiCliente = Collections.binarySearch(criteria, ccrKey, cMethodName);
		if (indexClassiCliente > -1){
			returnValue += "LEFT JOIN CLASSICLIENTE CC ON A.CODCLASSECLIENTE = CC.CODCLASSECLIENTE ";
		}
		ccrKey.setGettermethodname(AnagraficheMethodName.GET_NOME_AGENTEINSERITORE);
		int indexAgentiNome = Collections.binarySearch(criteria, ccrKey, cMethodName);
		if (indexAgentiNome > -1){
			returnValue += "LEFT JOIN AGENTI AG ON A.CODAGENTEINSERITORE = AG.CODAGENTE ";
		}
		ccrKey.setGettermethodname(AnagraficheMethodName.GET_COGNOME_AGENTEINSERITORE);
		int indexAgentiCognome = Collections.binarySearch(criteria, ccrKey, cMethodName);
		if (indexAgentiCognome > -1){
			returnValue += "LEFT JOIN AGENTI AG ON A.CODAGENTEINSERITORE = AG.CODAGENTE ";
		}
		if (insertCampiPersonali){
			returnValue += "LEFT JOIN ATTRIBUTEVALUE CPAV ON A.CODANAGRAFICA = CPAV.IDOBJECT " + 
						   "LEFT JOIN ATTRIBUTE CPA ON CPA.IDATTRIBUTE = CPAV.IDFIELD " +
						   "LEFT JOIN ENTITY CPE ON CPA.IDCLASSENTITY = CPE.IDCLASSENTITY "; 

		}
		
		returnValue += "WHERE 1=1 AND ";
		
		if (insertCampiPersonali){
			returnValue += "CPE.CLASSNAME = 'winkhouse.vo.AnagraficheVO' AND";
		}
		
		Collections.sort(criteria, cLineNumber);
		Iterator it = criteria.iterator();
		int count = 1;
		while (it.hasNext()){
			Colloquicriteriricerca ccrVO = (Colloquicriteriricerca)it.next();
		    if ((ccrVO.getGettermethodname().equalsIgnoreCase(QueryConst.OPEN_PARENTESI)) ||
		    	(ccrVO.getGettermethodname().equalsIgnoreCase(QueryConst.CLOSE_PARENTESI))	
		       ){
		    	returnValue += ccrVO.getGettermethodname(); 
		    }else{
				WinkhouseUtils.ObjectSearchGetters isg = WinkhouseUtils.getInstance()
																	   .findObjectSearchGettersByMethodName(ccrVO.getGettermethodname(), 
																				   								WinkhouseUtils.ANAGRAFICHE, 
																				   								WinkhouseUtils.FUNCTION_SEARCH);
		    	
		   // 	returnValue += isg.getColumnName();
				if (!ccrVO.isIspersonal()){
			    	if (!ccrVO.getFromvalue().equalsIgnoreCase("")){
			    		if (
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_NOME)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_COGNOME)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_COMMENTO)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_CITTA)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_PROVINCIA)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_INDIRIZZO)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_DESCRIZIONE_CLASSECLIENTE)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_NOME_AGENTEINSERITORE)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_COGNOME_AGENTEINSERITORE)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_CONTATTI)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_CAP)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_CODICE_FISCALE)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_PARTITA_IVA))
			    			){
			    			returnValue += " LCASE(" + isg.getColumnName() + ") LIKE " + " '%" + ccrVO.getFromvalue().replace("'", "''").toLowerCase() + "%' " ;
			    		}else{
			    			if (
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_CODAGENTEINSERITORE)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_CODCLASSECLIENTE)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(AnagraficheMethodName.GET_CODANAGRAFICA))
			    				){
			    				
		    					returnValue += isg.getColumnName() + " = " + ccrVO.getFromvalue(); 
			    				
			    			}else{
	
				    			if (((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase("")))
				    				){
				    					returnValue += "(" + isg.getColumnName() + " >= " + 
				    								   ((ccrVO.getGettermethodname()
				    										  .equalsIgnoreCase(AnagraficheMethodName.GET_DATAINSERIMENTO))?" '"+ccrVO.getFromvalue()+"' ":ccrVO.getFromvalue())+ 
				    								   " AND " + isg.getColumnName() + " <= " +
				    								   ((ccrVO.getGettermethodname()
				    										  .equalsIgnoreCase(AnagraficheMethodName.GET_DATAINSERIMENTO))?" '"+ccrVO.getTovalue()+"' ":ccrVO.getTovalue())+ 
		
				    								   ") ";
				    			}
				    			if (((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getTovalue() == null) || (ccrVO.getTovalue().equalsIgnoreCase("")))
				    				){
				    					returnValue += isg.getColumnName() + " >= " +
				    								   ((ccrVO.getGettermethodname()
				    										  .equalsIgnoreCase(AnagraficheMethodName.GET_DATAINSERIMENTO))?" '"+ccrVO.getFromvalue()+"' ":ccrVO.getFromvalue())+ 
				    								   " "; 
				    								   	
					    		}
				    			if (((ccrVO.getFromvalue() == null) || (ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase("")))
				    				){
				    				returnValue += isg.getColumnName() + " <= " + 
				    							   ((ccrVO.getGettermethodname()
												          .equalsIgnoreCase(AnagraficheMethodName.GET_DATAINSERIMENTO))?" '"+ccrVO.getTovalue()+"' ":ccrVO.getTovalue())+ 
		
				    							   " ";	
					    		}	
				    		}
			    		}
			    	}
				}else{
					returnValue += buildCampiPersonalizzatiWhereClause(ccrVO);
				}
		    }
			if (count != criteria.size()){
				returnValue += (ccrVO.getLogicalOperator() != null)? " "+ ccrVO.getLogicalOperator() + " " : "";
			}

		    count++;
		}
		
		return returnValue;

	}
	
	protected Colloquicriteriricerca findGetNumeroStanzeMethod(ArrayList criteri){
		
		Colloquicriteriricerca returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.getGettermethodname() != null && 
				ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_NUMEROSTANZE)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected Colloquicriteriricerca findGetTipoStanzeMethod(ArrayList criteri){
		
		Colloquicriteriricerca returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.getGettermethodname() != null && 
				ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliMethodName.MTIPOLOGIASTANZA)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected Colloquicriteriricerca findGetMqStanzeMethod(ArrayList criteri){
		
		Colloquicriteriricerca returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.getGettermethodname() != null && 
				ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliMethodName.MMQSTANZA)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected Colloquicriteriricerca findGetCampiPersonaliMethod(ArrayList criteri){
		
		Colloquicriteriricerca returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.isIspersonal()){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}
	
	protected Colloquicriteriricerca findGetPeriodoAffittoMethod(ArrayList criteri){
		
		Colloquicriteriricerca returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.getGettermethodname() != null && 
				(ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO) ||
			     ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAUZIONE) ||
				 ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_RATA))){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected Colloquicriteriricerca findGetSpeseAffittoMethod(ArrayList criteri){
		
		Colloquicriteriricerca returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.getGettermethodname() != null && 
				(ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_SPESA) ||
				 ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_SPESA))){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected Colloquicriteriricerca findGetRateAffittoMethod(ArrayList criteri){
		
		Colloquicriteriricerca returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.getGettermethodname() != null && 
				(ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_RATA) ||
				 ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_RATA))){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected Colloquicriteriricerca findGetAnagraficheAffittoMethod(ArrayList criteri){
		
		Colloquicriteriricerca returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.getGettermethodname() != null && 
				(ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.COGNOME_ANAGRAFICA) ||
				 ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.NOME_ANAGRAFICA) ||
				 ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.RAGIONESOCIALE_ANAGRAFICA))){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected Colloquicriteriricerca findGetAffittoMethod(ArrayList criteri){
		
		Colloquicriteriricerca returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.getGettermethodname() != null && 
				ccrvo.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAUZIONE)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}
	
	protected ArrayList<Colloquicriteriricerca> findGetColloquioAgentiMethod(ArrayList criteri){
		
		ArrayList<Colloquicriteriricerca> returnValue = new ArrayList<Colloquicriteriricerca>();
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.getGettermethodname() != null && 
				ccrvo.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_AGENTI)){
				returnValue.add(ccrvo);
			}
		}
		
		return returnValue;
		
	}
	
	protected ArrayList<Colloquicriteriricerca> findGetColloquioAnagraficheMethod(ArrayList criteri){
		
		ArrayList<Colloquicriteriricerca> returnValue =  new ArrayList<Colloquicriteriricerca>();
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			Colloquicriteriricerca ccrvo = (Colloquicriteriricerca)it.next();
			if (ccrvo.getGettermethodname() != null && 
				ccrvo.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_ANAGRAFICHE)){
				returnValue.add(ccrvo);				
			}
		}
		
		return returnValue;
		
	}

	public String buildQueryImmobili(ArrayList criteria){
		
		String returnValue = "SELECT DISTINCT I.* FROM IMMOBILI I ";
		
		String numeroStanze = " INNER JOIN (SELECT COUNT (SI.CODIMMOBILE) AS NUMSTANZE,CODIMMOBILE  FROM STANZEIMMOBILI SI GROUP BY CODIMMOBILE) AS NS " + 
							  "ON I.CODIMMOBILE = NS.CODIMMOBILE ";
		
		String campiDinamici = "LEFT JOIN ATTRIBUTEVALUE CPAV ON I.CODIMMOBILE = CPAV.IDOBJECT " + 
						   	   "LEFT JOIN ATTRIBUTE CPA ON CPA.IDATTRIBUTE = CPAV.IDFIELD " +
						   	   "LEFT JOIN ENTITY CPE ON CPA.IDCLASSENTITY = CPE.IDCLASSENTITY AND CPE.CLASSNAME = 'winkhouse.vo.ImmobiliVO' ";				
 
		String Stanze = "LEFT JOIN STANZEIMMOBILI SI ON SI.CODIMMOBILE = I.CODIMMOBILE ";
		
		
		boolean insertStanze = (findGetNumeroStanzeMethod(criteria) != null);
		boolean insertCampiPersonali = (findGetCampiPersonaliMethod(criteria) != null);
		boolean insertTipoMqstanze = (findGetTipoStanzeMethod(criteria) != null) || (findGetMqStanzeMethod(criteria) != null);		
		
		Boolean storico = WinkhouseUtils.getInstance().getTipoArchivio();
		
		if (insertStanze){
			returnValue += "" + numeroStanze;
		}
		
		if (insertCampiPersonali){
			returnValue += campiDinamici;
		}
		
		if (insertTipoMqstanze){
			returnValue += Stanze; 
		}
		
		returnValue += " WHERE 1=1 AND I.STORICO = " + storico.toString();
		
//		if (insertStanze){
//			if (insertCampiPersonali){
//				returnValue += "FROM IMMOBILI I INNER JOIN " +
//						   	   "(SELECT COUNT (SI.CODIMMOBILE) AS NUMSTANZE,CODIMMOBILE  FROM STANZEIMMOBILI SI GROUP BY CODIMMOBILE) AS NS " +
//						   	   "ON I.CODIMMOBILE = NS.CODIMMOBILE AND I.STORICO = "+ String.valueOf(storico) + " " +
//						   	   "LEFT JOIN ATTRIBUTEVALUE CPAV ON I.CODIMMOBILE = CPAV.IDOBJECT " + 
//						   	   "LEFT JOIN ATTRIBUTE CPA ON CPA.IDATTRIBUTE = CPAV.IDFIELD " +
//						   	   "LEFT JOIN ENTITY CPE ON CPA.IDCLASSENTITY = CPE.IDCLASSENTITY " + 
//						   	   "WHERE CPE.CLASSNAME = 'winkhouse.vo.ImmobiliVO' ";				
//			}else{
//				returnValue += "FROM IMMOBILI I INNER JOIN " +
//							   "(SELECT COUNT (SI.CODIMMOBILE) AS NUMSTANZE,CODIMMOBILE  FROM STANZEIMMOBILI SI GROUP BY CODIMMOBILE) AS NS " +
//							   "ON I.CODIMMOBILE = NS.CODIMMOBILE AND I.STORICO = "+ String.valueOf(storico) + " " +
//							   "WHERE 1=1 ";
//			}
//
//		}else{
//			if (insertCampiPersonali){
//				returnValue += "FROM IMMOBILI I " +
//							   "LEFT JOIN ATTRIBUTEVALUE CPAV ON I.CODIMMOBILE = CPAV.IDOBJECT AND I.STORICO = "+ String.valueOf(storico) + " " +
//					   	       "LEFT JOIN ATTRIBUTE CPA ON CPA.IDATTRIBUTE = CPAV.IDFIELD " +
//					   	       "LEFT JOIN ENTITY CPE ON CPA.IDCLASSENTITY = CPE.IDCLASSENTITY " + 
//							   "WHERE CPE.CLASSNAME = 'winkhouse.vo.ImmobiliVO' ";
//			}else{
//				returnValue += "FROM IMMOBILI I INNER JOIN (SELECT CODIMMOBILE FROM IMMOBILI WHERE STORICO = "+ String.valueOf(storico) + ") IMS " +
//						       " ON I.CODIMMOBILE = IMS.CODIMMOBILE " +
//						       "WHERE 1=1 ";
//			}
//		}
		
		Collections.sort(criteria, cLineNumber);
		if (criteria.size() > 0){
			returnValue += " AND ";
		}
		
		Iterator it = criteria.iterator();
		int count = 1;
		while (it.hasNext()){
			
			Colloquicriteriricerca ccrVO = (Colloquicriteriricerca)it.next();
			
		    if (ccrVO.getGettermethodname() != null && 
		    	((ccrVO.getGettermethodname().equalsIgnoreCase(QueryConst.OPEN_PARENTESI)) ||
		    	(ccrVO.getGettermethodname().equalsIgnoreCase(QueryConst.CLOSE_PARENTESI)))	
		       ){
		    	returnValue += ccrVO.getGettermethodname() + " "; 
		    }else{
				WinkhouseUtils.ObjectSearchGetters isg = WinkhouseUtils.getInstance()
																	   .findObjectSearchGettersByMethodName(ccrVO.getGettermethodname(), 
																					 						WinkhouseUtils.IMMOBILI, 
																					 						WinkhouseUtils.FUNCTION_SEARCH);
		    	
				if (ccrVO.isIspersonal() == false){
					
			    	if (ccrVO.getFromvalue()!= null && !ccrVO.getFromvalue().equalsIgnoreCase("")){
			    		if (
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_ZONA)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CITTA)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_PROVINCIA)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAP)) || 
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_RIF)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_INDIRIZZO))			    				
			    			){
			    			returnValue += " LCASE(" + isg.getColumnName() + ") LIKE " + "'%" + ccrVO.getFromvalue().replace("'", "''").toLowerCase() + "%'" ;
			    		}else{
			    			if (
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODAGENTEINSERITORE)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODRISCALDAMENTO)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODSTATO)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODTIPOLOGIA)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODCLASSEENERGETICA)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliMethodName.MTIPOLOGIASTANZA)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliMethodName.GET_CODIMMOBILE))
			    				){
			    					returnValue += isg.getColumnName() + " = " + ccrVO.getFromvalue();
			    				
			    			}else{
				    			if (((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase("")))
				    				){
				    					returnValue += " (" + isg.getColumnName() + " >= " + 
				    								   ((ccrVO.getGettermethodname()
				    										  .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO))?" '"+ccrVO.getFromvalue()+"' ":ccrVO.getFromvalue())+ 
				    								   " AND " + isg.getColumnName() + " <= " +
				    								   ((ccrVO.getGettermethodname()
				    										  .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO))?" '"+ccrVO.getTovalue()+"' ":ccrVO.getTovalue())+ 
		
				    								   ") ";
				    			}
				    			if (((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getTovalue() == null) || (ccrVO.getTovalue().equalsIgnoreCase("")))
				    				){
				    					returnValue += isg.getColumnName() + " >= " +
				    								   ((ccrVO.getGettermethodname()
				    										  .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO))?" '"+ccrVO.getFromvalue()+"' ":ccrVO.getFromvalue())+ 
				    								   " "; 
				    								   	
					    		}
				    			if (((ccrVO.getFromvalue() == null) || (ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase("")))
				    				){
				    				returnValue += isg.getColumnName() + " <= " + 
				    							   ((ccrVO.getGettermethodname()
												          .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO))?" '"+ccrVO.getTovalue()+"' ":ccrVO.getTovalue())+ 
		
				    							   " ";	
					    		}
			    			}
			    		}
			    	}else{
		    			if (((ccrVO.getFromvalue() == null) || (ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
			    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase("")))
			    				){
			    				returnValue += isg.getColumnName() + " <= " + 
			    							   ((ccrVO.getGettermethodname()
											          .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO))?" '"+ccrVO.getTovalue()+"' ":ccrVO.getTovalue())+ 
	
			    							   " ";	
				    		}

			    	}		    	
				}else{
					returnValue += buildCampiPersonalizzatiWhereClause(ccrVO);
				}
		    }
			if (count != criteria.size()){
					returnValue += (ccrVO.getLogicalOperator() != null)? " " + ccrVO.getLogicalOperator() + " " : "";
			}

		    count++;
		}
		if (returnValue.endsWith("AND ") == true){
			returnValue = returnValue.substring(0, returnValue.length() - 4);
		} 
		if (returnValue.endsWith("OR ") == true){
			returnValue = returnValue.substring(0, returnValue.length() - 3);
		} 
		//System.out.println(returnValue);
		return returnValue;
	}
	
	protected String buildCampiPersonalizzatiWhereClause(Colloquicriteriricerca ccrVO){
		
		String returnValue = "";
		
		AttributeModel am = attributeDAO.getAttributeByID(Integer.valueOf(ccrVO.getGettermethodname()));
		returnValue += "( CPAV.IDFIELD = " + ccrVO.getGettermethodname() + " AND ";
		
		if (am.getFieldType().equalsIgnoreCase(String.class.getName()) || am.getFieldType().equalsIgnoreCase(Enum.class.getName())){
		
			if ((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().trim().equalsIgnoreCase(""))){
				returnValue += " LCASE(CPAV.FIELDVALUE) LIKE '%"+ccrVO.getFromvalue().toLowerCase()+"%') ";
			}
			
		}
		
		if (am.getFieldType().equalsIgnoreCase(Integer.class.getName())){
			
			if ((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getTovalue() == null) || (ccrVO.getTovalue().trim().equalsIgnoreCase(""))){
				
				returnValue += "CAST(CPAV.FIELDVALUE AS INTEGER) >= " + ccrVO.getFromvalue() + ") ";
				
			}
			
			if ((ccrVO.getFromvalue() == null) || (ccrVO.getFromvalue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().trim().equalsIgnoreCase(""))){
					
					returnValue += "CAST(CPAV.FIELDVALUE AS INTEGER) <= " + ccrVO.getTovalue() + ") ";
					
			}

			if ((ccrVO.getFromvalue() != null) & (!ccrVO.getFromvalue().trim().equalsIgnoreCase("")) &&
					(ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().trim().equalsIgnoreCase(""))){
						
					returnValue += "CAST(CPAV.FIELDVALUE AS INTEGER) >= " + ccrVO.getFromvalue() + " AND " +
								   " CAST(CPAV.FIELDVALUE AS INTEGER) <= " + ccrVO.getTovalue() + ") ";
						
			}

		}
		
		if (am.getFieldType().equalsIgnoreCase(Date.class.getName())){
			
			if ((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getTovalue() == null) || (ccrVO.getTovalue().trim().equalsIgnoreCase(""))){
				
				returnValue += "CAST(CPAV.FIELDVALUE AS DATE) >= '" + ccrVO.getFromvalue() + "') ";
				
			}
			
			if ((ccrVO.getFromvalue() == null) || (ccrVO.getFromvalue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().trim().equalsIgnoreCase(""))){
					
					returnValue += "CAST(CPAV.FIELDVALUE AS DATE) <= '" + ccrVO.getTovalue() + "') ";
					
			}

			if ((ccrVO.getFromvalue() != null) & (!ccrVO.getFromvalue().trim().equalsIgnoreCase("")) &&
					(ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().trim().equalsIgnoreCase(""))){
						
					returnValue += "CAST(CPAV.FIELDVALUE AS DATE) >= '" + ccrVO.getFromvalue() + "' " + " AND " +
								   " CAST(CPAV.FIELDVALUE AS DATE) <= '" + ccrVO.getTovalue() + "' ) ";
						
			}

		}
		
		if (am.getFieldType().equalsIgnoreCase(Double.class.getName())){
			
			if ((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getTovalue() == null) || (ccrVO.getTovalue().trim().equalsIgnoreCase(""))){
				
				returnValue += "CAST(CPAV.FIELDVALUE AS DOUBLE) >= " + ccrVO.getFromvalue() + ") ";
				
			}
			
			if ((ccrVO.getFromvalue() == null) || (ccrVO.getFromvalue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().trim().equalsIgnoreCase(""))){
					
					returnValue += "CAST(CPAV.FIELDVALUE AS DOUBLE) <= " + ccrVO.getTovalue() + ") ";
					
			}

			if ((ccrVO.getFromvalue() != null) & (!ccrVO.getFromvalue().trim().equalsIgnoreCase("")) &&
					(ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().trim().equalsIgnoreCase(""))){
						
					returnValue += "CAST(CPAV.FIELDVALUE AS DOUBLE) >= " + ccrVO.getFromvalue() + " AND " +
								   " CAST(CPAV.FIELDVALUE AS DOUBLE) <= " + ccrVO.getTovalue() + ") ";
						
			}
		}
		if (am.getFieldType().equalsIgnoreCase(Boolean.class.getName())){
			
			if ((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().trim().equalsIgnoreCase(""))){
				returnValue += " CAST(CPAV.FIELDVALUE AS BOOLEAN) = " + ccrVO.getFromvalue().toLowerCase()+ ") ";
			}

		}
		
		return returnValue;
	} 
	
	public String buildQueryImmobiliAffitti(ArrayList criteria){

		String returnValue = "SELECT DISTINCT I.* FROM IMMOBILI I ";
		
		String Stanze = "LEFT JOIN STANZEIMMOBILI SI ON SI.CODIMMOBILE = I.CODIMMOBILE ";
		
		boolean insertStanze = (findGetNumeroStanzeMethod(criteria) != null);
/*		if (insertStanze){
			returnValue += ",NS.NUMSTANZE ";
		}
*/		
		Boolean storico = WinkhouseUtils.getInstance().getTipoArchivio();
		if (insertStanze){
			returnValue += " INNER JOIN " + 
						   "(SELECT COUNT (SI.CODIMMOBILE) AS NUMSTANZE,CODIMMOBILE  FROM STANZEIMMOBILI SI GROUP BY CODIMMOBILE) AS NS " +
						   "ON I.CODIMMOBILE = NS.CODIMMOBILE AND I.STORICO = "+ String.valueOf(storico) + " " ;

		}else{
			returnValue += " INNER JOIN (SELECT CODIMMOBILE FROM IMMOBILI WHERE STORICO = "+ String.valueOf(storico) + ") IMS " +
				           " ON I.CODIMMOBILE = IMS.CODIMMOBILE ";
		}
		boolean insertTipoMqstanze = (findGetTipoStanzeMethod(criteria) != null) || (findGetMqStanzeMethod(criteria) != null);
		boolean speseaffitto = (findGetSpeseAffittoMethod(criteria) != null);
		boolean rateaffitto = (findGetRateAffittoMethod(criteria) != null);
		boolean periodoaffitto = (findGetPeriodoAffittoMethod(criteria) != null);
		boolean anagraficheaffitto = (findGetAnagraficheAffittoMethod(criteria) != null);
		boolean insertCampiPersonali = (findGetCampiPersonaliMethod(criteria) != null);
		
		if (periodoaffitto || speseaffitto || rateaffitto || anagraficheaffitto || insertCampiPersonali){ 
			returnValue += " LEFT JOIN AFFITTI A ON I.CODIMMOBILE = A.CODIMMOBILE ";
			if (speseaffitto){
				returnValue += " LEFT JOIN AFFITTISPESE AFS ON A.CODAFFITTI = AFS.CODAFFITTO";
			}
			if (rateaffitto){
				returnValue += " LEFT JOIN AFFITTIRATE AR ON A.CODAFFITTI = AR.CODAFFITTO ";
			}
			if (anagraficheaffitto){
				returnValue += " LEFT JOIN AFFITTIANAGRAFICHE AAN ON A.CODAFFITTI = AAN.CODAFFITTO ";
				returnValue += " LEFT JOIN ANAGRAFICHE AN ON AAN.CODANAGRAFICA = AN.CODANAGRAFICA ";
			}
			
			if (insertCampiPersonali){
				returnValue += " LEFT JOIN ATTRIBUTEVALUE CPAV ON A.CODAFFITTI = CPAV.IDOBJECT " + 
				   	           "LEFT JOIN ATTRIBUTE CPA ON CPA.IDATTRIBUTE = CPAV.IDFIELD " +
				   	           "LEFT JOIN ENTITY CPE ON CPA.IDCLASSENTITY = CPE.IDCLASSENTITY "; 

			

			}

		}
		if (insertTipoMqstanze){
			returnValue += Stanze; 
		}

		returnValue += " WHERE I.AFFITTO = TRUE ";
		if (insertCampiPersonali){
			returnValue += " AND CPE.CLASSNAME = 'winkhouse.vo.AffittiVO'";
		}
		
		if (criteria.size() > 0){
			returnValue += " AND ";
		}
		
		
		Collections.sort(criteria, cLineNumber);
		Iterator it = criteria.iterator();
		int count = 1;
		while (it.hasNext()){
			Colloquicriteriricerca ccrVO = (Colloquicriteriricerca)it.next();
		    if ((ccrVO.getGettermethodname().equalsIgnoreCase(QueryConst.OPEN_PARENTESI)) ||
		    	(ccrVO.getGettermethodname().equalsIgnoreCase(QueryConst.CLOSE_PARENTESI))	
		       ){
		    	returnValue += ccrVO.getGettermethodname() + " "; 
		    }else{
				WinkhouseUtils.ObjectSearchGetters isg = WinkhouseUtils.getInstance()
																			 .findObjectSearchGettersByMethodName(ccrVO.getGettermethodname(), 
																					 							  WinkhouseUtils.AFFITTIIMMOBILI, 
																					 							  WinkhouseUtils.FUNCTION_SEARCH);
		    	
				if (!ccrVO.isIspersonal()){
			    	if (!ccrVO.getFromvalue().equalsIgnoreCase("")){
			    		if (
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_ZONA)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CITTA)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_PROVINCIA)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAP)) || 
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_RIF)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_INDIRIZZO)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.NOME_ANAGRAFICA)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.COGNOME_ANAGRAFICA)) ||
			    				(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.RAGIONESOCIALE_ANAGRAFICA))
			    			
			    			){
			    			returnValue += " LCASE(" + isg.getColumnName() + ") LIKE " + "'%" + ccrVO.getFromvalue().toLowerCase() + "%'" ;
			    		}else{
			    			if (
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODAGENTEINSERITORE)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODRISCALDAMENTO)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODSTATO)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODTIPOLOGIA)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODCLASSEENERGETICA)) ||
			    					(ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.MTIPOLOGIASTANZA))
			    				){
			    					returnValue += isg.getColumnName() + " = " + ccrVO.getFromvalue();
			    				
			    			}else{
				    			if (((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase("")))
				    				){
				    				if (ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO)){

				    					returnValue += " (" + isg.getColumnName() + " >= " + " '"+ccrVO.getFromvalue()+"' " + 
	    								   	           " AND " + isg.getColumnName() + " <= " + " '"+ccrVO.getTovalue()+"' " +
	    								   	           ") ";	   
				    					
				    				}else if (ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_RATA) ||
				    					      ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_RATA) ||
				    					      ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_SPESA) ||
				    					      ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_SPESA)){

				    					returnValue += " (" + isg.getColumnName() + " >= " + " '"+ccrVO.getFromvalue()+" 00:00:00.0' " + 
		    								   	   " AND " + isg.getColumnName() + " <= " + " '"+ccrVO.getTovalue()+" 00:00:00.0' " +
		    								   	   ") ";	   
				    					
				    				}else if (ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO)){
				    					returnValue += " (A.DATAINIZIO > '" + ccrVO.getTovalue() + " 00:00:00.0' OR A.DATAFINE < '" + 
				    								   ccrVO.getFromvalue() + " 00:00:00.0' )"; 
				    				}else{
				    					returnValue += " (" + isg.getColumnName() + " >= " +ccrVO.getFromvalue()+ 
		    								   	   	   " AND " + isg.getColumnName() + " <= " + ccrVO.getTovalue()+
		    								   	   	   ") ";	   
	
				    				}
				    			}
				    			if (((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getTovalue() == null) || (ccrVO.getTovalue().equalsIgnoreCase("")))
				    				){
				    				if (ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO)){
				    					
				    					returnValue += isg.getColumnName() + " >= " + " '" + ccrVO.getFromvalue()+ "' "; 
				    					
				    				}else if (ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_RATA) ||
				    					      ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_RATA) ||
				    					      ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_SPESA) ||
				    					      ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_SPESA)){

				    					returnValue += isg.getColumnName() + " >= " + " '"+ccrVO.getFromvalue()+" 00:00:00.0' ";	   
				    					
				    					
				    				}else if (ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO)){
				    					returnValue += " A.DATAFINE < '" + ccrVO.getFromvalue() + " 00:00:00.0' "; 
				    				}else{
				    					returnValue += " (" + isg.getColumnName() + " >= " +ccrVO.getFromvalue()+ ") ";	   
	
				    				}
				    				
				    								   	
					    		}
				    			if (((ccrVO.getFromvalue() == null) || (ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase("")))
				    				){
				    				if (ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO)){
	
				    					returnValue += isg.getColumnName() + " <= " + " '" + ccrVO.getTovalue()+ "' "; 
				    				
				    				}else if (ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_RATA) ||
				    					      ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_RATA) ||
				    					      ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_SPESA) ||
				    					      ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_SPESA)){

				    					returnValue += isg.getColumnName() + " <= " + " '"+ccrVO.getTovalue()+" 00:00:00.0' ";	   
				    									    				
				    				}else if (ccrVO.getGettermethodname().equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO)){			    					
				    					returnValue += " A.DATAINIZIO > '" + ccrVO.getTovalue() + " 00:00:00.0' "; 
	
				    				}else{
				    					returnValue += " (" + isg.getColumnName() + " <= " + ccrVO.getTovalue() + ") ";	   
	
				    				}
				    				
					    		}
			    			}
			    		}
			    	}
				}else{
					returnValue += buildCampiPersonalizzatiWhereClause(ccrVO);
				}
		    }
			if (count != criteria.size()){
					returnValue += (ccrVO.getLogicalOperator() != null)? " " + ccrVO.getLogicalOperator() + " " : "";
			}

		    count++;
		}
				
		return returnValue;
	}
	
	private String fillPartecipantiColloquioParameter(ArrayList<Colloquicriteriricerca> criteria, String fieldName){
		
		String returnValue = "";
		int count = 0;
		
		Iterator<Colloquicriteriricerca> it = criteria.iterator();
		
		while (it.hasNext()) {
			
			Colloquicriteriricerca type = (Colloquicriteriricerca) it.next();
			returnValue += fieldName + " = " + type.getFromvalue();
			count ++;
			if (count != criteria.size()){
				returnValue += (type.getLogicalOperator() != null)? " " + type.getLogicalOperator() + " " : "";
			}
			
		}

		return returnValue;
	}

	public String buildQueryColloqui(ArrayList criteria){
		
		String returnValue = "SELECT DISTINCT C.* FROM COLLOQUI C ";
		
		String colloquiAgenti = " C.CODCOLLOQUIO IN (SELECT CODCOLLOQUIO FROM COLLOQUIAGENTI WHERE %s) ";
		String colloquiAnagrafiche = " C.CODCOLLOQUIO IN (SELECT CODCOLLOQUIO FROM COLLOQUIANAGRAFICHE WHERE %s) ";
		
		
		String campiDinamici = "LEFT JOIN ATTRIBUTEVALUE CPAV ON C.CODCOLLOQUIO = CPAV.IDOBJECT " + 
						   	   "LEFT JOIN ATTRIBUTE CPA ON CPA.IDATTRIBUTE = CPAV.IDFIELD " +
						   	   "LEFT JOIN ENTITY CPE ON CPA.IDCLASSENTITY = CPE.IDCLASSENTITY AND CPE.CLASSNAME = 'winkhouse.vo.ColloquiVO' ";
		
		boolean insertCampiPersonali = (findGetCampiPersonaliMethod(criteria) != null);
		if (insertCampiPersonali){
			returnValue += campiDinamici;
		}
 		
		returnValue += " WHERE 1=1 ";
		
		ArrayList<Colloquicriteriricerca> agenti = findGetColloquioAgentiMethod(criteria);
		ArrayList<Colloquicriteriricerca> anagrafiche = findGetColloquioAnagraficheMethod(criteria);
		
		if (agenti.size() > 0){
			criteria.removeAll(agenti);
			colloquiAgenti = colloquiAgenti.replace("%s", fillPartecipantiColloquioParameter(agenti,"CODAGENTE"));
		}
		
		if (anagrafiche.size() > 0){
			criteria.removeAll(anagrafiche);
			colloquiAnagrafiche = colloquiAnagrafiche.replace("%s", fillPartecipantiColloquioParameter(anagrafiche,"CODANAGRAFICA"));
		}
		
		Collections.sort(criteria, cLineNumber);
		if ((criteria.size() > 0) || (agenti.size() > 0 || anagrafiche.size() > 0)){
			returnValue += " AND ";
		}
		
		if (criteria.size() > 0){
			Iterator it = criteria.iterator();
			int count = 1;
			while (it.hasNext()){
				
				Colloquicriteriricerca ccrVO = (Colloquicriteriricerca)it.next();
				
			    if ((ccrVO.getGettermethodname().equalsIgnoreCase(QueryConst.OPEN_PARENTESI)) ||
			    	(ccrVO.getGettermethodname().equalsIgnoreCase(QueryConst.CLOSE_PARENTESI))	
			       ){
			    	returnValue += ccrVO.getGettermethodname() + " "; 
			    }else{
					WinkhouseUtils.ObjectSearchGetters isg = WinkhouseUtils.getInstance()
																		   .findObjectSearchGettersByMethodName(ccrVO.getGettermethodname(), 
																						 						WinkhouseUtils.COLLOQUI, 
																						 						WinkhouseUtils.FUNCTION_SEARCH);
			    	
					if (ccrVO.isIspersonal() == false){
						
				    	if (!ccrVO.getFromvalue().equalsIgnoreCase("")){
				    		if (
				    				(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_COMMENTO_AGENZIA)) ||
				    				(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_COMMENTO_CLIENTE)) ||
				    				(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_DESCRIZIONE)) ||
				    				(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_LUOGO_INCONTRO)) 
				    			){
				    			returnValue += " LCASE(" + isg.getColumnName() + ") LIKE " + "'%" + ccrVO.getFromvalue().replace("'", "''").toLowerCase() + "%'" ;
				    		}else{
				    			if (
				    					(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_AGENTI)) ||
				    					(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_ANAGRAFICHE)) ||
				    					(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_IMMOBILE_ABBINATO))
				    				){
				    					returnValue += isg.getColumnName() + " = " + ccrVO.getFromvalue();
				    				
				    			}else{
					    			if (((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
					    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase(""))) &&
					    				(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))
					    				){
					    					returnValue += " (" + isg.getColumnName() + " >= " + 
					    								   ((ccrVO.getGettermethodname()
					    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))?" '"+ccrVO.getFromvalue()+" 00:00:00' ":ccrVO.getFromvalue())+ 
					    								   " AND " + isg.getColumnName() + " <= " +
					    								   ((ccrVO.getGettermethodname()
					    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))?" '"+ccrVO.getTovalue()+" 23:59:59' ":ccrVO.getTovalue())+ 
			
					    								   ") ";
					    			}
					    			if (((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
					    				((ccrVO.getTovalue() == null) || (ccrVO.getTovalue().equalsIgnoreCase("")))  &&
					    				(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))
					    				){
					    					returnValue += isg.getColumnName() + " >= " +
					    								   ((ccrVO.getGettermethodname()
					    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))?" '"+ccrVO.getFromvalue()+" 00:00:00' ":ccrVO.getFromvalue())+ 
					    								   " "; 
					    								   	
						    		}
					    			if (((ccrVO.getFromvalue() == null) || (ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
					    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase("")))  &&
					    				(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))
					    				){
					    				returnValue += isg.getColumnName() + " <= " + 
					    							   ((ccrVO.getGettermethodname()
													          .equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))?" '"+ccrVO.getTovalue()+" 23:59:59' ":ccrVO.getTovalue())+ 
			
					    							   " ";	
						    		}
					    			if (((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
						    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase(""))) &&
						    				(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))
						    				){
						    					returnValue += " (" + isg.getColumnName() + " >= " + 
						    								   ((ccrVO.getGettermethodname()
						    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))?" '"+ccrVO.getFromvalue()+" 00:00:00' ":ccrVO.getFromvalue())+ 
						    								   " AND " + isg.getColumnName() + " <= " +
						    								   ((ccrVO.getGettermethodname()
						    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))?" '"+ccrVO.getTovalue()+" 23:59:59' ":ccrVO.getTovalue())+ 
				
						    								   ") ";
						    			}
						    			if (((ccrVO.getFromvalue() != null) && (!ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
						    				((ccrVO.getTovalue() == null) || (ccrVO.getTovalue().equalsIgnoreCase(""))) &&
						    				(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))
						    				){
						    					returnValue += isg.getColumnName() + " >= " +
						    								   ((ccrVO.getGettermethodname()
						    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))?" '"+ccrVO.getFromvalue()+" 00:00:00' ":ccrVO.getFromvalue())+ 
						    								   " "; 
						    								   	
							    		}
						    			if (((ccrVO.getFromvalue() == null) || (ccrVO.getFromvalue().equalsIgnoreCase(""))) && 
						    				((ccrVO.getTovalue() != null) && (!ccrVO.getTovalue().equalsIgnoreCase("")))  &&
						    				(ccrVO.getGettermethodname().equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))
						    				){
						    				returnValue += isg.getColumnName() + " <= " + 
						    							   ((ccrVO.getGettermethodname()
														          .equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))?" '"+ccrVO.getTovalue()+" 23:59:59' ":ccrVO.getTovalue())+ 
				
						    							   " ";	
							    		}
				    			    }
				    			}
				    		}
					}else{
						returnValue += buildCampiPersonalizzatiWhereClause(ccrVO);
					}
			    }
			    
				if (count != criteria.size()){
					returnValue += (ccrVO.getLogicalOperator() != null)? " " + ccrVO.getLogicalOperator() + " " : "";
				}
				
				if (agenti.size() > 0){
					returnValue += colloquiAgenti;
				}
				
				if (anagrafiche.size() > 0){
					returnValue += colloquiAnagrafiche;
				}
			    count++;
			}

		}else{
			if (agenti.size() > 0){		
				if (anagrafiche.size() > 0){
					returnValue += colloquiAgenti + " OR ";
				}else{
					returnValue += colloquiAgenti;
				}
			}
			
			if (anagrafiche.size() > 0){
				returnValue += colloquiAnagrafiche;
			}
			
		}
		if (returnValue.endsWith("AND ") == true){
			returnValue = returnValue.substring(0, returnValue.length() - 4);
		} 
		if (returnValue.endsWith("OR ") == true){
			returnValue = returnValue.substring(0, returnValue.length() - 3);
		} 
		//System.out.println(returnValue);
		return returnValue;
	}

}