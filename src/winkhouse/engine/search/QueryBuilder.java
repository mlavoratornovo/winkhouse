package winkhouse.engine.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliAffittiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.QueryConst;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.ColloquiCriteriRicercaVO;


public class QueryBuilder {
	
	private SimpleDateFormat formatterENG = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.s");
	private SimpleDateFormat formatterIT = new SimpleDateFormat("dd/MM/yyyy");
	private AttributeDAO attributeDAO = null;
	
	public QueryBuilder(){
		attributeDAO = new AttributeDAO();
	}	
	
	Comparator<ColloquiCriteriRicercaVO> cLineNumber = new Comparator<ColloquiCriteriRicercaVO>(){

		@Override
		public int compare(ColloquiCriteriRicercaVO o1, ColloquiCriteriRicercaVO o2){
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

	Comparator<ColloquiCriteriRicercaVO> cMethodName = new Comparator<ColloquiCriteriRicercaVO>(){

		@Override
		public int compare(ColloquiCriteriRicercaVO o1, ColloquiCriteriRicercaVO o2){
			return o1.getGetterMethodName().compareTo(o2.getGetterMethodName());			
		}
		
	};
	
	public String buildQueryAnagrafiche(ArrayList criteria){
		
		boolean insertCampiPersonali = (findGetCampiPersonaliMethod(criteria) != null);
		Boolean storico = WinkhouseUtils.getInstance().getTipoArchivio();
		
		String returnValue = "SELECT DISTINCT A.* FROM ANAGRAFICHE A " +
				             "INNER JOIN (SELECT CODANAGRAFICA FROM ANAGRAFICHE WHERE STORICO = "+ String.valueOf(storico) + ") AST " +
				             "ON A.CODANAGRAFICA = AST.CODANAGRAFICA ";
		
		Collections.sort(criteria, cMethodName);
		ColloquiCriteriRicercaVO ccrKey = new ColloquiCriteriRicercaVO();
		ccrKey.setGetterMethodName(AnagraficheMethodName.GET_CONTATTI);
		int indexContatti = Collections.binarySearch(criteria, ccrKey, cMethodName);
		if (indexContatti > -1){
			returnValue += "LEFT JOIN CONTATTI C ON A.CODANAGRAFICA = C.CODANAGRAFICA ";
		}
		ccrKey.setGetterMethodName(AnagraficheMethodName.GET_DESCRIZIONE_CLASSECLIENTE);
		int indexClassiCliente = Collections.binarySearch(criteria, ccrKey, cMethodName);
		if (indexClassiCliente > -1){
			returnValue += "LEFT JOIN CLASSICLIENTE CC ON A.CODCLASSECLIENTE = CC.CODCLASSECLIENTE ";
		}
		ccrKey.setGetterMethodName(AnagraficheMethodName.GET_NOME_AGENTEINSERITORE);
		int indexAgentiNome = Collections.binarySearch(criteria, ccrKey, cMethodName);
		if (indexAgentiNome > -1){
			returnValue += "LEFT JOIN AGENTI AG ON A.CODAGENTEINSERITORE = AG.CODAGENTE ";
		}
		ccrKey.setGetterMethodName(AnagraficheMethodName.GET_COGNOME_AGENTEINSERITORE);
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
			ColloquiCriteriRicercaVO ccrVO = (ColloquiCriteriRicercaVO)it.next();
		    if ((ccrVO.getGetterMethodName().equalsIgnoreCase(QueryConst.OPEN_PARENTESI)) ||
		    	(ccrVO.getGetterMethodName().equalsIgnoreCase(QueryConst.CLOSE_PARENTESI))	
		       ){
		    	returnValue += ccrVO.getGetterMethodName(); 
		    }else{
				WinkhouseUtils.ObjectSearchGetters isg = WinkhouseUtils.getInstance()
																	   .findObjectSearchGettersByMethodName(ccrVO.getGetterMethodName(), 
																				   								WinkhouseUtils.ANAGRAFICHE, 
																				   								WinkhouseUtils.FUNCTION_SEARCH);
		    	
		   // 	returnValue += isg.getColumnName();
				if (!ccrVO.getIsPersonal()){
			    	if (!ccrVO.getFromValue().equalsIgnoreCase("")){
			    		if (
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_NOME)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_COGNOME)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_COMMENTO)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CITTA)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_PROVINCIA)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_INDIRIZZO)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_DESCRIZIONE_CLASSECLIENTE)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_NOME_AGENTEINSERITORE)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_COGNOME_AGENTEINSERITORE)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CONTATTI)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CAP)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODICE_FISCALE)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_PARTITA_IVA))
			    			){
			    			returnValue += " LCASE(" + isg.getColumnName() + ") LIKE " + " '%" + ccrVO.getFromValue().replace("'", "''").toLowerCase() + "%' " ;
			    		}else{
			    			if (
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODAGENTEINSERITORE)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODCLASSECLIENTE)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODANAGRAFICA))
			    				){
			    				
		    					returnValue += isg.getColumnName() + " = " + ccrVO.getFromValue(); 
			    				
			    			}else{
	
				    			if (((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase("")))
				    				){
				    					returnValue += "(" + isg.getColumnName() + " >= " + 
				    								   ((ccrVO.getGetterMethodName()
				    										  .equalsIgnoreCase(AnagraficheMethodName.GET_DATAINSERIMENTO))?" '"+ccrVO.getFromValue()+"' ":ccrVO.getFromValue())+ 
				    								   " AND " + isg.getColumnName() + " <= " +
				    								   ((ccrVO.getGetterMethodName()
				    										  .equalsIgnoreCase(AnagraficheMethodName.GET_DATAINSERIMENTO))?" '"+ccrVO.getToValue()+"' ":ccrVO.getToValue())+ 
		
				    								   ") ";
				    			}
				    			if (((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getToValue() == null) || (ccrVO.getToValue().equalsIgnoreCase("")))
				    				){
				    					returnValue += isg.getColumnName() + " >= " +
				    								   ((ccrVO.getGetterMethodName()
				    										  .equalsIgnoreCase(AnagraficheMethodName.GET_DATAINSERIMENTO))?" '"+ccrVO.getFromValue()+"' ":ccrVO.getFromValue())+ 
				    								   " "; 
				    								   	
					    		}
				    			if (((ccrVO.getFromValue() == null) || (ccrVO.getFromValue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase("")))
				    				){
				    				returnValue += isg.getColumnName() + " <= " + 
				    							   ((ccrVO.getGetterMethodName()
												          .equalsIgnoreCase(AnagraficheMethodName.GET_DATAINSERIMENTO))?" '"+ccrVO.getToValue()+"' ":ccrVO.getToValue())+ 
		
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
	
	protected ColloquiCriteriRicercaVO findGetNumeroStanzeMethod(ArrayList criteri){
		
		ColloquiCriteriRicercaVO returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_NUMEROSTANZE)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected ColloquiCriteriRicercaVO findGetTipoStanzeMethod(ArrayList criteri){
		
		ColloquiCriteriRicercaVO returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliMethodName.MTIPOLOGIASTANZA)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected ColloquiCriteriRicercaVO findGetMqStanzeMethod(ArrayList criteri){
		
		ColloquiCriteriRicercaVO returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliMethodName.MMQSTANZA)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected ColloquiCriteriRicercaVO findGetCampiPersonaliMethod(ArrayList criteri){
		
		ColloquiCriteriRicercaVO returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getIsPersonal()){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}
	
	protected ColloquiCriteriRicercaVO findGetPeriodoAffittoMethod(ArrayList criteri){
		
		ColloquiCriteriRicercaVO returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO) ||
			    ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAUZIONE) ||
				ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_RATA)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected ColloquiCriteriRicercaVO findGetSpeseAffittoMethod(ArrayList criteri){
		
		ColloquiCriteriRicercaVO returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_SPESA) ||
				ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_SPESA)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected ColloquiCriteriRicercaVO findGetRateAffittoMethod(ArrayList criteri){
		
		ColloquiCriteriRicercaVO returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_RATA) ||
				ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_RATA)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected ColloquiCriteriRicercaVO findGetAnagraficheAffittoMethod(ArrayList criteri){
		
		ColloquiCriteriRicercaVO returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.COGNOME_ANAGRAFICA) ||
				ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.NOME_ANAGRAFICA) ||
				ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.RAGIONESOCIALE_ANAGRAFICA)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}

	protected ColloquiCriteriRicercaVO findGetAffittoMethod(ArrayList criteri){
		
		ColloquiCriteriRicercaVO returnValue = null;
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAUZIONE)){
				returnValue = ccrvo;
				break;
			}
		}
		
		return returnValue;
		
	}
	
	protected ArrayList<ColloquiCriteriRicercaVO> findGetColloquioAgentiMethod(ArrayList criteri){
		
		ArrayList<ColloquiCriteriRicercaVO> returnValue = new ArrayList<ColloquiCriteriRicercaVO>();
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_AGENTI)){
				returnValue.add(ccrvo);
			}
		}
		
		return returnValue;
		
	}
	
	protected ArrayList<ColloquiCriteriRicercaVO> findGetColloquioAnagraficheMethod(ArrayList criteri){
		
		ArrayList<ColloquiCriteriRicercaVO> returnValue =  new ArrayList<ColloquiCriteriRicercaVO>();
		
		Iterator it = criteri.iterator();
		
		while(it.hasNext()){
			ColloquiCriteriRicercaVO ccrvo = (ColloquiCriteriRicercaVO)it.next();
			if (ccrvo.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_ANAGRAFICHE)){
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
			
			ColloquiCriteriRicercaVO ccrVO = (ColloquiCriteriRicercaVO)it.next();
			
		    if ((ccrVO.getGetterMethodName().equalsIgnoreCase(QueryConst.OPEN_PARENTESI)) ||
		    	(ccrVO.getGetterMethodName().equalsIgnoreCase(QueryConst.CLOSE_PARENTESI))	
		       ){
		    	returnValue += ccrVO.getGetterMethodName() + " "; 
		    }else{
				WinkhouseUtils.ObjectSearchGetters isg = WinkhouseUtils.getInstance()
																	   .findObjectSearchGettersByMethodName(ccrVO.getGetterMethodName(), 
																					 						WinkhouseUtils.IMMOBILI, 
																					 						WinkhouseUtils.FUNCTION_SEARCH);
		    	
				if (ccrVO.getIsPersonal() == false){
					
			    	if (!ccrVO.getFromValue().equalsIgnoreCase("")){
			    		if (
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_ZONA)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CITTA)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_PROVINCIA)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAP)) || 
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_RIF)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_INDIRIZZO))			    				
			    			){
			    			returnValue += " LCASE(" + isg.getColumnName() + ") LIKE " + "'%" + ccrVO.getFromValue().replace("'", "''").toLowerCase() + "%'" ;
			    		}else{
			    			if (
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODAGENTEINSERITORE)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODRISCALDAMENTO)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODSTATO)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODTIPOLOGIA)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODCLASSEENERGETICA)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliMethodName.MTIPOLOGIASTANZA)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODIMMOBILE))
			    				){
			    					returnValue += isg.getColumnName() + " = " + ccrVO.getFromValue();
			    				
			    			}else{
				    			if (((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase("")))
				    				){
				    					returnValue += " (" + isg.getColumnName() + " >= " + 
				    								   ((ccrVO.getGetterMethodName()
				    										  .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO))?" '"+ccrVO.getFromValue()+"' ":ccrVO.getFromValue())+ 
				    								   " AND " + isg.getColumnName() + " <= " +
				    								   ((ccrVO.getGetterMethodName()
				    										  .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO))?" '"+ccrVO.getToValue()+"' ":ccrVO.getToValue())+ 
		
				    								   ") ";
				    			}
				    			if (((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getToValue() == null) || (ccrVO.getToValue().equalsIgnoreCase("")))
				    				){
				    					returnValue += isg.getColumnName() + " >= " +
				    								   ((ccrVO.getGetterMethodName()
				    										  .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO))?" '"+ccrVO.getFromValue()+"' ":ccrVO.getFromValue())+ 
				    								   " "; 
				    								   	
					    		}
				    			if (((ccrVO.getFromValue() == null) || (ccrVO.getFromValue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase("")))
				    				){
				    				returnValue += isg.getColumnName() + " <= " + 
				    							   ((ccrVO.getGetterMethodName()
												          .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO))?" '"+ccrVO.getToValue()+"' ":ccrVO.getToValue())+ 
		
				    							   " ";	
					    		}
			    			}
			    		}
			    	}else{
		    			if (((ccrVO.getFromValue() == null) || (ccrVO.getFromValue().equalsIgnoreCase(""))) && 
			    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase("")))
			    				){
			    				returnValue += isg.getColumnName() + " <= " + 
			    							   ((ccrVO.getGetterMethodName()
											          .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO))?" '"+ccrVO.getToValue()+"' ":ccrVO.getToValue())+ 
	
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
	
	protected String buildCampiPersonalizzatiWhereClause(ColloquiCriteriRicercaVO ccrVO){
		
		String returnValue = "";
		
		AttributeModel am = attributeDAO.getAttributeByID(Integer.valueOf(ccrVO.getGetterMethodName()));
		returnValue += "( CPAV.IDFIELD = " + ccrVO.getGetterMethodName() + " AND ";
		
		if (am.getFieldType().equalsIgnoreCase(String.class.getName()) || am.getFieldType().equalsIgnoreCase(Enum.class.getName())){
		
			if ((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().trim().equalsIgnoreCase(""))){
				returnValue += " LCASE(CPAV.FIELDVALUE) LIKE '%"+ccrVO.getFromValue().toLowerCase()+"%') ";
			}
			
		}
		
		if (am.getFieldType().equalsIgnoreCase(Integer.class.getName())){
			
			if ((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getToValue() == null) || (ccrVO.getToValue().trim().equalsIgnoreCase(""))){
				
				returnValue += "CAST(CPAV.FIELDVALUE AS INTEGER) >= " + ccrVO.getFromValue() + ") ";
				
			}
			
			if ((ccrVO.getFromValue() == null) || (ccrVO.getFromValue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getToValue() != null) && (!ccrVO.getToValue().trim().equalsIgnoreCase(""))){
					
					returnValue += "CAST(CPAV.FIELDVALUE AS INTEGER) <= " + ccrVO.getToValue() + ") ";
					
			}

			if ((ccrVO.getFromValue() != null) & (!ccrVO.getFromValue().trim().equalsIgnoreCase("")) &&
					(ccrVO.getToValue() != null) && (!ccrVO.getToValue().trim().equalsIgnoreCase(""))){
						
					returnValue += "CAST(CPAV.FIELDVALUE AS INTEGER) >= " + ccrVO.getFromValue() + " AND " +
								   " CAST(CPAV.FIELDVALUE AS INTEGER) <= " + ccrVO.getToValue() + ") ";
						
			}

		}
		
		if (am.getFieldType().equalsIgnoreCase(Date.class.getName())){
			
			if ((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getToValue() == null) || (ccrVO.getToValue().trim().equalsIgnoreCase(""))){
				
				returnValue += "CAST(CPAV.FIELDVALUE AS DATE) >= '" + ccrVO.getFromValue() + "') ";
				
			}
			
			if ((ccrVO.getFromValue() == null) || (ccrVO.getFromValue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getToValue() != null) && (!ccrVO.getToValue().trim().equalsIgnoreCase(""))){
					
					returnValue += "CAST(CPAV.FIELDVALUE AS DATE) <= '" + ccrVO.getToValue() + "') ";
					
			}

			if ((ccrVO.getFromValue() != null) & (!ccrVO.getFromValue().trim().equalsIgnoreCase("")) &&
					(ccrVO.getToValue() != null) && (!ccrVO.getToValue().trim().equalsIgnoreCase(""))){
						
					returnValue += "CAST(CPAV.FIELDVALUE AS DATE) >= '" + ccrVO.getFromValue() + "' " + " AND " +
								   " CAST(CPAV.FIELDVALUE AS DATE) <= '" + ccrVO.getToValue() + "' ) ";
						
			}

		}
		
		if (am.getFieldType().equalsIgnoreCase(Double.class.getName())){
			
			if ((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getToValue() == null) || (ccrVO.getToValue().trim().equalsIgnoreCase(""))){
				
				returnValue += "CAST(CPAV.FIELDVALUE AS DOUBLE) >= " + ccrVO.getFromValue() + ") ";
				
			}
			
			if ((ccrVO.getFromValue() == null) || (ccrVO.getFromValue().trim().equalsIgnoreCase("")) &&
				(ccrVO.getToValue() != null) && (!ccrVO.getToValue().trim().equalsIgnoreCase(""))){
					
					returnValue += "CAST(CPAV.FIELDVALUE AS DOUBLE) <= " + ccrVO.getToValue() + ") ";
					
			}

			if ((ccrVO.getFromValue() != null) & (!ccrVO.getFromValue().trim().equalsIgnoreCase("")) &&
					(ccrVO.getToValue() != null) && (!ccrVO.getToValue().trim().equalsIgnoreCase(""))){
						
					returnValue += "CAST(CPAV.FIELDVALUE AS DOUBLE) >= " + ccrVO.getFromValue() + " AND " +
								   " CAST(CPAV.FIELDVALUE AS DOUBLE) <= " + ccrVO.getToValue() + ") ";
						
			}
		}
		if (am.getFieldType().equalsIgnoreCase(Boolean.class.getName())){
			
			if ((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().trim().equalsIgnoreCase(""))){
				returnValue += " CAST(CPAV.FIELDVALUE AS BOOLEAN) = " + ccrVO.getFromValue().toLowerCase()+ ") ";
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
			ColloquiCriteriRicercaVO ccrVO = (ColloquiCriteriRicercaVO)it.next();
		    if ((ccrVO.getGetterMethodName().equalsIgnoreCase(QueryConst.OPEN_PARENTESI)) ||
		    	(ccrVO.getGetterMethodName().equalsIgnoreCase(QueryConst.CLOSE_PARENTESI))	
		       ){
		    	returnValue += ccrVO.getGetterMethodName() + " "; 
		    }else{
				WinkhouseUtils.ObjectSearchGetters isg = WinkhouseUtils.getInstance()
																			 .findObjectSearchGettersByMethodName(ccrVO.getGetterMethodName(), 
																					 							  WinkhouseUtils.AFFITTIIMMOBILI, 
																					 							  WinkhouseUtils.FUNCTION_SEARCH);
		    	
				if (!ccrVO.getIsPersonal()){
			    	if (!ccrVO.getFromValue().equalsIgnoreCase("")){
			    		if (
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_ZONA)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CITTA)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_PROVINCIA)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAP)) || 
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_RIF)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_INDIRIZZO)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.NOME_ANAGRAFICA)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.COGNOME_ANAGRAFICA)) ||
			    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.RAGIONESOCIALE_ANAGRAFICA))
			    			
			    			){
			    			returnValue += " LCASE(" + isg.getColumnName() + ") LIKE " + "'%" + ccrVO.getFromValue().toLowerCase() + "%'" ;
			    		}else{
			    			if (
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODAGENTEINSERITORE)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODRISCALDAMENTO)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODSTATO)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODTIPOLOGIA)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODCLASSEENERGETICA)) ||
			    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.MTIPOLOGIASTANZA))
			    				){
			    					returnValue += isg.getColumnName() + " = " + ccrVO.getFromValue();
			    				
			    			}else{
				    			if (((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase("")))
				    				){
				    				if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO)){

				    					returnValue += " (" + isg.getColumnName() + " >= " + " '"+ccrVO.getFromValue()+"' " + 
	    								   	           " AND " + isg.getColumnName() + " <= " + " '"+ccrVO.getToValue()+"' " +
	    								   	           ") ";	   
				    					
				    				}else if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_RATA) ||
				    					      ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_RATA) ||
				    					      ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_SPESA) ||
				    					      ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_SPESA)){

				    					returnValue += " (" + isg.getColumnName() + " >= " + " '"+ccrVO.getFromValue()+" 00:00:00.0' " + 
		    								   	   " AND " + isg.getColumnName() + " <= " + " '"+ccrVO.getToValue()+" 00:00:00.0' " +
		    								   	   ") ";	   
				    					
				    				}else if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO)){
				    					returnValue += " (A.DATAINIZIO > '" + ccrVO.getToValue() + " 00:00:00.0' OR A.DATAFINE < '" + 
				    								   ccrVO.getFromValue() + " 00:00:00.0' )"; 
				    				}else{
				    					returnValue += " (" + isg.getColumnName() + " >= " +ccrVO.getFromValue()+ 
		    								   	   	   " AND " + isg.getColumnName() + " <= " + ccrVO.getToValue()+
		    								   	   	   ") ";	   
	
				    				}
				    			}
				    			if (((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getToValue() == null) || (ccrVO.getToValue().equalsIgnoreCase("")))
				    				){
				    				if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO)){
				    					
				    					returnValue += isg.getColumnName() + " >= " + " '" + ccrVO.getFromValue()+ "' "; 
				    					
				    				}else if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_RATA) ||
				    					      ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_RATA) ||
				    					      ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_SPESA) ||
				    					      ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_SPESA)){

				    					returnValue += isg.getColumnName() + " >= " + " '"+ccrVO.getFromValue()+" 00:00:00.0' ";	   
				    					
				    					
				    				}else if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO)){
				    					returnValue += " A.DATAFINE < '" + ccrVO.getFromValue() + " 00:00:00.0' "; 
				    				}else{
				    					returnValue += " (" + isg.getColumnName() + " >= " +ccrVO.getFromValue()+ ") ";	   
	
				    				}
				    				
				    								   	
					    		}
				    			if (((ccrVO.getFromValue() == null) || (ccrVO.getFromValue().equalsIgnoreCase(""))) && 
				    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase("")))
				    				){
				    				if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO)){
	
				    					returnValue += isg.getColumnName() + " <= " + " '" + ccrVO.getToValue()+ "' "; 
				    				
				    				}else if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_RATA) ||
				    					      ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_RATA) ||
				    					      ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.SCADENZA_SPESA) ||
				    					      ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.DATAPAGATO_SPESA)){

				    					returnValue += isg.getColumnName() + " <= " + " '"+ccrVO.getToValue()+" 00:00:00.0' ";	   
				    									    				
				    				}else if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO)){			    					
				    					returnValue += " A.DATAINIZIO > '" + ccrVO.getToValue() + " 00:00:00.0' "; 
	
				    				}else{
				    					returnValue += " (" + isg.getColumnName() + " <= " + ccrVO.getToValue() + ") ";	   
	
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
	
	private String fillPartecipantiColloquioParameter(ArrayList<ColloquiCriteriRicercaVO> criteria, String fieldName){
		
		String returnValue = "";
		int count = 0;
		
		Iterator<ColloquiCriteriRicercaVO> it = criteria.iterator();
		
		while (it.hasNext()) {
			
			ColloquiCriteriRicercaVO type = (ColloquiCriteriRicercaVO) it.next();
			returnValue += fieldName + " = " + type.getFromValue();
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
		
		ArrayList<ColloquiCriteriRicercaVO> agenti = findGetColloquioAgentiMethod(criteria);
		ArrayList<ColloquiCriteriRicercaVO> anagrafiche = findGetColloquioAnagraficheMethod(criteria);
		
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
				
				ColloquiCriteriRicercaVO ccrVO = (ColloquiCriteriRicercaVO)it.next();
				
			    if ((ccrVO.getGetterMethodName().equalsIgnoreCase(QueryConst.OPEN_PARENTESI)) ||
			    	(ccrVO.getGetterMethodName().equalsIgnoreCase(QueryConst.CLOSE_PARENTESI))	
			       ){
			    	returnValue += ccrVO.getGetterMethodName() + " "; 
			    }else{
					WinkhouseUtils.ObjectSearchGetters isg = WinkhouseUtils.getInstance()
																		   .findObjectSearchGettersByMethodName(ccrVO.getGetterMethodName(), 
																						 						WinkhouseUtils.COLLOQUI, 
																						 						WinkhouseUtils.FUNCTION_SEARCH);
			    	
					if (ccrVO.getIsPersonal() == false){
						
				    	if (!ccrVO.getFromValue().equalsIgnoreCase("")){
				    		if (
				    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_COMMENTO_AGENZIA)) ||
				    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_COMMENTO_CLIENTE)) ||
				    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_DESCRIZIONE)) ||
				    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_LUOGO_INCONTRO)) 
				    			){
				    			returnValue += " LCASE(" + isg.getColumnName() + ") LIKE " + "'%" + ccrVO.getFromValue().replace("'", "''").toLowerCase() + "%'" ;
				    		}else{
				    			if (
				    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_AGENTI)) ||
				    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_ANAGRAFICHE)) ||
				    					(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_IMMOBILE_ABBINATO))
				    				){
				    					returnValue += isg.getColumnName() + " = " + ccrVO.getFromValue();
				    				
				    			}else{
					    			if (((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().equalsIgnoreCase(""))) && 
					    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase(""))) &&
					    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))
					    				){
					    					returnValue += " (" + isg.getColumnName() + " >= " + 
					    								   ((ccrVO.getGetterMethodName()
					    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))?" '"+ccrVO.getFromValue()+" 00:00:00' ":ccrVO.getFromValue())+ 
					    								   " AND " + isg.getColumnName() + " <= " +
					    								   ((ccrVO.getGetterMethodName()
					    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))?" '"+ccrVO.getToValue()+" 23:59:59' ":ccrVO.getToValue())+ 
			
					    								   ") ";
					    			}
					    			if (((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().equalsIgnoreCase(""))) && 
					    				((ccrVO.getToValue() == null) || (ccrVO.getToValue().equalsIgnoreCase("")))  &&
					    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))
					    				){
					    					returnValue += isg.getColumnName() + " >= " +
					    								   ((ccrVO.getGetterMethodName()
					    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))?" '"+ccrVO.getFromValue()+" 00:00:00' ":ccrVO.getFromValue())+ 
					    								   " "; 
					    								   	
						    		}
					    			if (((ccrVO.getFromValue() == null) || (ccrVO.getFromValue().equalsIgnoreCase(""))) && 
					    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase("")))  &&
					    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))
					    				){
					    				returnValue += isg.getColumnName() + " <= " + 
					    							   ((ccrVO.getGetterMethodName()
													          .equalsIgnoreCase(ColloquiMethodName.GET_DATA_COLLOQUIO))?" '"+ccrVO.getToValue()+" 23:59:59' ":ccrVO.getToValue())+ 
			
					    							   " ";	
						    		}
					    			if (((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().equalsIgnoreCase(""))) && 
						    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase(""))) &&
						    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))
						    				){
						    					returnValue += " (" + isg.getColumnName() + " >= " + 
						    								   ((ccrVO.getGetterMethodName()
						    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))?" '"+ccrVO.getFromValue()+" 00:00:00' ":ccrVO.getFromValue())+ 
						    								   " AND " + isg.getColumnName() + " <= " +
						    								   ((ccrVO.getGetterMethodName()
						    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))?" '"+ccrVO.getToValue()+" 23:59:59' ":ccrVO.getToValue())+ 
				
						    								   ") ";
						    			}
						    			if (((ccrVO.getFromValue() != null) && (!ccrVO.getFromValue().equalsIgnoreCase(""))) && 
						    				((ccrVO.getToValue() == null) || (ccrVO.getToValue().equalsIgnoreCase(""))) &&
						    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))
						    				){
						    					returnValue += isg.getColumnName() + " >= " +
						    								   ((ccrVO.getGetterMethodName()
						    										  .equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))?" '"+ccrVO.getFromValue()+" 00:00:00' ":ccrVO.getFromValue())+ 
						    								   " "; 
						    								   	
							    		}
						    			if (((ccrVO.getFromValue() == null) || (ccrVO.getFromValue().equalsIgnoreCase(""))) && 
						    				((ccrVO.getToValue() != null) && (!ccrVO.getToValue().equalsIgnoreCase("")))  &&
						    				(ccrVO.getGetterMethodName().equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))
						    				){
						    				returnValue += isg.getColumnName() + " <= " + 
						    							   ((ccrVO.getGetterMethodName()
														          .equalsIgnoreCase(ColloquiMethodName.GET_DATA_INSERIMENTO))?" '"+ccrVO.getToValue()+" 23:59:59' ":ccrVO.getToValue())+ 
				
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
