package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import winkhouse.db.ConnectionManager;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.ImmobiliVO;


public class ImmobiliDAO extends BaseDAO{
	
	public final static String LISTA_IMMOBILI = "LISTA_IMMOBILI";
	public final static String LISTA_IMMOBILI_STORICO = "LISTA_IMMOBILI_STORICO";
	
	public final static String IMMOBILI_BY_TIPOLOGIA = "IMMOBILI_BY_TIPOLOGIA";	
	public final static String IMMOBILI_BY_TIPOLOGIANULL = "IMMOBILI_BY_TIPOLOGIANULL";
	public final static String IMMOBILI_BY_TIPOLOGIA_STORICO = "IMMOBILI_BY_TIPOLOGIA_STORICO";
	public final static String IMMOBILI_BY_TIPOLOGIANULL_STORICO = "IMMOBILI_BY_TIPOLOGIANULL_STORICO";
	public final static String IMMOBILI_BY_TIPOLOGIA_ISAFFITTI = "IMMOBILI_BY_TIPOLOGIA_ISAFFITTI";
	public final static String IMMOBILI_BY_TIPOLOGIANULL_ISAFFITTI = "IMMOBILI_BY_TIPOLOGIANULL_ISAFFITTI";
	public final static String IMMOBILI_BY_TIPOLOGIA_ISAFFITTI_STORICO = "IMMOBILI_BY_TIPOLOGIA_ISAFFITTI_STORICO";
	public final static String IMMOBILI_BY_TIPOLOGIANULL_ISAFFITTI_STORICO = "IMMOBILI_BY_TIPOLOGIANULL_ISAFFITTI_STORICO";
	public final static String IMMOBILI_BY_TIPOLOGIA_COMUNE = "IMMOBILI_BY_TIPOLOGIA_COMUNE";
	public final static String IMMOBILI_BY_TIPOLOGIANULL_COMUNE = "IMMOBILI_BY_TIPOLOGIANULL_COMUNE";
	public final static String IMMOBILI_BY_TIPOLOGIA_STORICO_COMUNE = "IMMOBILI_BY_TIPOLOGIA_STORICO_COMUNE";
	public final static String IMMOBILI_BY_TIPOLOGIANULL_STORICO_COMUNE = "IMMOBILI_BY_TIPOLOGIANULL_STORICO_COMUNE";
	public final static String IMMOBILI_BY_TIPOLOGIA_ISAFFITTI_COMUNE = "IMMOBILI_BY_TIPOLOGIA_ISAFFITTI_COMUNE";
	public final static String IMMOBILI_BY_TIPOLOGIANULL_ISAFFITTI_COMUNE = "IMMOBILI_BY_TIPOLOGIANULL_ISAFFITTI_COMUNE";
	public final static String IMMOBILI_BY_TIPOLOGIA_STORICO_ISAFFITTI_COMUNE = "IMMOBILI_BY_TIPOLOGIA_ISAFFITTI_STORICO_COMUNE";
	public final static String IMMOBILI_BY_TIPOLOGIANULL_STORICO_ISAFFITTI_COMUNE = "IMMOBILI_BY_TIPOLOGIANULL_ISAFFITTI_STORICO_COMUNE";
	
	public final static String IMMOBILI_BY_AGENTEINSERITORE = "IMMOBILI_BY_AGENTE_INSERITORE";
	public final static String IMMOBILI_BY_AGENTENULLINSERITORE = "IMMOBILI_BY_AGENTENULL_INSERITORE";
	public final static String IMMOBILI_BY_AGENTEINSERITORE_STORICO = "IMMOBILI_BY_AGENTE_INSERITORE_STORICO";
	public final static String IMMOBILI_BY_AGENTENULLINSERITORE_STORICO = "IMMOBILI_BY_AGENTENULL_INSERITORE_STORICO";
	
	public final static String IMMOBILI_BY_RISCALDAMENTO = "IMMOBILI_BY_RISCALDAMENTO";
	public final static String IMMOBILI_BY_RISCALDAMENTONULL = "IMMOBILI_BY_RISCALDAMENTONULL";
	public final static String IMMOBILI_BY_RISCALDAMENTO_STORICO = "IMMOBILI_BY_RISCALDAMENTO_STORICO";
	public final static String IMMOBILI_BY_RISCALDAMENTONULL_STORICO = "IMMOBILI_BY_RISCALDAMENTONULL_STORICO";
	public final static String IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI = "IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI";
	public final static String IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI = "IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI";
	public final static String IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI_STORICO = "IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI_STORICO";
	public final static String IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI_STORICO = "IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI_STORICO";
	public final static String IMMOBILI_BY_RISCALDAMENTO_COMUNE = "IMMOBILI_BY_RISCALDAMENTO_COMUNE";
	public final static String IMMOBILI_BY_RISCALDAMENTONULL_COMUNE = "IMMOBILI_BY_RISCALDAMENTONULL_COMUNE";
	public final static String IMMOBILI_BY_RISCALDAMENTO_STORICO_COMUNE = "IMMOBILI_BY_RISCALDAMENTO_STORICO_COMUNE";
	public final static String IMMOBILI_BY_RISCALDAMENTONULL_STORICO_COMUNE = "IMMOBILI_BY_RISCALDAMENTONULL_STORICO_COMUNE";
	public final static String IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI_COMUNE = "IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI_COMUNE";
	public final static String IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI_COMUNE = "IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI_COMUNE";
	public final static String IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI_STORICO_COMUNE = "IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI_STORICO_COMUNE";
	public final static String IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI_STORICO_COMUNE = "IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI_STORICO_COMUNE";
	
	public final static String IMMOBILI_BY_STATOCONSERVATIVO = "IMMOBILI_BY_STATO_CONSERVATIVO";
	public final static String IMMOBILI_BY_STATOCONSERVATIVONULL = "IMMOBILI_BY_STATO_CONSERVATIVONULL";
	public final static String IMMOBILI_BY_STATOCONSERVATIVO_STORICO = "IMMOBILI_BY_STATO_CONSERVATIVO_STORICO";
	public final static String IMMOBILI_BY_STATOCONSERVATIVONULL_STORICO = "IMMOBILI_BY_STATO_CONSERVATIVONULL_STORICO";
	public final static String IMMOBILI_BY_STATOCONSERVATIVO_ISAFFITTI = "IMMOBILI_BY_STATO_CONSERVATIVO_ISAFFITTI";
	public final static String IMMOBILI_BY_STATOCONSERVATIVONULL_ISAFFITTI = "IMMOBILI_BY_STATO_CONSERVATIVONULL_ISAFFITTI";
	public final static String IMMOBILI_BY_STATOCONSERVATIVO_ISAFFITTI_STORICO = "IMMOBILI_BY_STATO_CONSERVATIVO_ISAFFITTI_STORICO";
	public final static String IMMOBILI_BY_STATOCONSERVATIVONULL_ISAFFITTI_STORICO = "IMMOBILI_BY_STATO_CONSERVATIVONULL_ISAFFITTI_STORICO";
	public final static String IMMOBILI_BY_STATOCONSERVATIVO_COMUNE = "IMMOBILI_BY_STATO_CONSERVATIVO_COMUNE";
	public final static String IMMOBILI_BY_STATOCONSERVATIVONULL_COMUNE = "IMMOBILI_BY_STATO_CONSERVATIVONULL_COMUNE";
	public final static String IMMOBILI_BY_STATOCONSERVATIVO_STORICO_COMUNE = "IMMOBILI_BY_STATO_CONSERVATIVO_STORICO_COMUNE";
	public final static String IMMOBILI_BY_STATOCONSERVATIVONULL_STORICO_COMUNE = "IMMOBILI_BY_STATO_CONSERVATIVONULL_STORICO_COMUNE";
	public final static String IMMOBILI_BY_STATOCONSERVATIVO_ISAFFITTI_COMUNE = "IMMOBILI_BY_STATO_CONSERVATIVO_ISAFFITTI_COMUNE";
	public final static String IMMOBILI_BY_STATOCONSERVATIVONULL_ISAFFITTI_COMUNE = "IMMOBILI_BY_STATO_CONSERVATIVONULL_ISAFFITTI_COMUNE";
	public final static String IMMOBILI_BY_STATOCONSERVATIVO_ISAFFITTI_STORICO_COMUNE = "IMMOBILI_BY_STATO_CONSERVATIVO_ISAFFITTI_STORICO_COMUNE";
	public final static String IMMOBILI_BY_STATOCONSERVATIVONULL_ISAFFITTI_STORICO_COMUNE = "IMMOBILI_BY_STATO_CONSERVATIVONULL_ISAFFITTI_STORICO_COMUNE";
	
	public final static String IMMOBILI_BY_CODANAGRAFICA = "IMMOBILI_BY_CODANAGRAFICA";
	
	public final static String INSERT_IMMOBILI = "INSERT_IMMOBILI";
	public final static String UPDATE_IMMOBILI = "UPDATE_IMMOBILI";
	public final static String UPDATE_IMMOBILI_AGENTEINSERITORE = "UPDATE_IMMOBILI_AGENTE_INSERITORE";
	public final static String UPDATE_IMMOBILI_AGENTEUPDATE = "UPDATE_IMMOBILI_AGENTE_UPDATE";
	public final static String UPDATE_IMMOBILI_RISCALDAMENTO = "UPDATE_IMMOBILI_RISCALDAMENTO";
	public final static String UPDATE_IMMOBILI_STATOCONSERVATIVO = "UPDATE_IMMOBILI_STATO_CONSERVATIVO";
	public final static String UPDATE_IMMOBILI_TIPOLOGIAIMMOBILI = "UPDATE_IMMOBILI_TIPOLOGIA_IMMOBILI";
	public final static String UPDATE_IMMOBILI_ANAGRAFICA = "UPDATE_IMMOBILI_ANAGRAFICA";
	public final static String DELETE_IMMOBILI = "DELETE_IMMOBILI";
	public final static String IMMOBILI_BY_ID = "IMMOBILI_BY_ID";
	public final static String IMMOBILI_BY_RIF = "IMMOBILI_BY_RIF";
	public final static String IMMOBILI_BY_ANAGRAFICA = "IMMOBILI_BY_ANAGRAFICA";
	public final static String GET_MAX_CODIMMOBILE = "GET_MAX_CODIMMOBILE";
	
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICA = "LIST_IMMOBILI_BY_CODCLASSEENERGETICA";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL = "LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO = "LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO = "LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICA_ISAFFITTI = "LIST_IMMOBILI_BY_CODCLASSEENERGETICA_ISAFFITTI";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_ISAFFITTI = "LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_ISAFFITTI";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_ISAFFITTI = "LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_ISAFFITTI";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO_ISAFFITTI = "LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO_ISAFFITTI";

	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICA_COMUNE = "LIST_IMMOBILI_BY_CODCLASSEENERGETICA_COMUNE";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_COMUNE = "LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_COMUNE";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_COMUNE = "LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_COMUNE";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO_COMUNE = "LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO_COMUNE";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICA_ISAFFITTI_COMUNE = "LIST_IMMOBILI_BY_CODCLASSEENERGETICA_ISAFFITTI_COMUNE";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_ISAFFITTI_COMUNE = "LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_ISAFFITTI_COMUNE";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_ISAFFITTI_COMUNE = "LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_ISAFFITTI_COMUNE";
	public final static String LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO_ISAFFITTI_COMUNE = "LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO_ISAFFITTI_COMUNE";
	
	public final static String IMMOBILI_IN_COLLOQUI_BY_TIPOLOGIAIMMOBILI = "IMMOBILI_IN_COLLOQUI_BY_TIPOLOGIAIMMOBILI";
	public final static String IMMOBILI_IN_COLLOQUI_BY_TIPOLOGIAIMMOBILI_STORICO = "IMMOBILI_IN_COLLOQUI_BY_TIPOLOGIAIMMOBILI_STORICO";
	
	public final static String UPDATE_IMMOBILI_CODCLASSEENERGETICA = "UPDATE_IMMOBILI_CODCLASSEENERGETICA";

	
	public ImmobiliDAO() {
		super();
	}
	
	public ArrayList list(String classType){
		if (WinkhouseUtils.getInstance()
							.getTipoArchivio()){
			return super.list(classType, LISTA_IMMOBILI_STORICO);
		}else{
			return super.list(classType, LISTA_IMMOBILI);
		}
	}
	
	public ArrayList listImmobiliByCodClasseEnergetica(String ClassName,Integer codClasseEnergetica){
		return super.getObjectsByIntFieldValue(ClassName, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   								? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO
				   								: LIST_IMMOBILI_BY_CODCLASSEENERGETICA, 
				   							   codClasseEnergetica);
	}		

	public ArrayList listImmobiliByCodClasseEnergeticaIsAffitti(String ClassName,Integer codClasseEnergetica){
		return super.getObjectsByIntFieldValue(ClassName, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   								? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_ISAFFITTI
				   								: LIST_IMMOBILI_BY_CODCLASSEENERGETICA_ISAFFITTI, 
				   							   codClasseEnergetica);
	}		

	public ArrayList listImmobiliByCodClasseEnergeticaComune(String ClassName,Integer codClasseEnergetica,String comune){
		return super.getObjectsByIntStrFieldValue(ClassName, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   								? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_COMUNE
				   								: LIST_IMMOBILI_BY_CODCLASSEENERGETICA_COMUNE, 
				   							   codClasseEnergetica,
				   							   comune);
		
	}		

	public ArrayList listImmobiliByCodClasseEnergeticaIsAffittiComune(String ClassName,Integer codClasseEnergetica,String comune){
		return super.getObjectsByIntStrFieldValue(ClassName, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   								? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_ISAFFITTI_COMUNE
				   								: LIST_IMMOBILI_BY_CODCLASSEENERGETICA_ISAFFITTI_COMUNE, 
				   							   codClasseEnergetica,
				   							   comune);
	}		
	
	public ArrayList getImmobiliByTipologia(String classType, Integer codTipologia){
		return super.getObjectsByIntFieldValue(classType, 
											   (WinkhouseUtils.getInstance().getTipoArchivio())
											    ? (codTipologia != null)
											      ? IMMOBILI_BY_TIPOLOGIA_STORICO
											      : IMMOBILI_BY_TIPOLOGIANULL_STORICO
											    : (codTipologia != null)
											      ? IMMOBILI_BY_TIPOLOGIA
											      : IMMOBILI_BY_TIPOLOGIANULL, 
											   codTipologia);
	}

	public ArrayList getImmobiliColloquiByTipologiaImmobile(String classType, Integer codTipologia){
		return super.getObjectsByIntFieldValue(classType, 
											   ((WinkhouseUtils.getInstance().getTipoArchivio())
											     ? IMMOBILI_IN_COLLOQUI_BY_TIPOLOGIAIMMOBILI_STORICO
											     : IMMOBILI_IN_COLLOQUI_BY_TIPOLOGIAIMMOBILI),
											   codTipologia);
	}

	public ArrayList getImmobiliByTipologiaComune(String classType, Integer codTipologia, String comune){
		return super.getObjectsByIntStrFieldValue(classType, 
											   (WinkhouseUtils.getInstance().getTipoArchivio())
											    ? (codTipologia != null)
											      ? IMMOBILI_BY_TIPOLOGIA_STORICO_COMUNE
											      : IMMOBILI_BY_TIPOLOGIANULL_STORICO_COMUNE
											    : (codTipologia != null)
											      ? IMMOBILI_BY_TIPOLOGIA_COMUNE
											      : IMMOBILI_BY_TIPOLOGIANULL_COMUNE, 
											   codTipologia,
											   comune);
	}

	public ArrayList getImmobiliByTipologiaIsAffitti(String classType, Integer codTipologia){
		return super.getObjectsByIntFieldValue(classType, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   							   ? (codTipologia != null)
				   								 ? IMMOBILI_BY_TIPOLOGIA_ISAFFITTI_STORICO
				   								 : IMMOBILI_BY_TIPOLOGIANULL_ISAFFITTI_STORICO
				   							   : (codTipologia != null)
				   							     ? IMMOBILI_BY_TIPOLOGIA_ISAFFITTI
				   							     : IMMOBILI_BY_TIPOLOGIANULL_ISAFFITTI,
				   							   codTipologia);
	}

	public ArrayList getImmobiliByTipologiaIsAffittiComune(String classType, Integer codTipologia, String comune){
		return super.getObjectsByIntStrFieldValue(classType, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   							   ? (codTipologia != null)
				   								 ? IMMOBILI_BY_TIPOLOGIA_STORICO_ISAFFITTI_COMUNE
				   								 : IMMOBILI_BY_TIPOLOGIANULL_STORICO_ISAFFITTI_COMUNE
				   							   : (codTipologia != null)
				   							     ? IMMOBILI_BY_TIPOLOGIA_ISAFFITTI_COMUNE
				   							     : IMMOBILI_BY_TIPOLOGIANULL_ISAFFITTI_COMUNE, 
				   							   codTipologia,
				   							   comune);
	}
	
	public ArrayList getImmobiliByAgente(String classType, Integer codAgenteInseritore){
		return super.getObjectsByIntFieldValue(classType, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   							   	? (codAgenteInseritore != null)
				   							   	  ? IMMOBILI_BY_AGENTEINSERITORE_STORICO
				   							   	  : IMMOBILI_BY_AGENTENULLINSERITORE_STORICO
				   							   	: (codAgenteInseritore != null)
				   							   	  ? IMMOBILI_BY_AGENTEINSERITORE
				   							   	  : IMMOBILI_BY_AGENTENULLINSERITORE, 
				   							   	codAgenteInseritore);
	}
	
	public ArrayList getImmobiliByRiscaldamento(String classType, Integer codRiscaldamento){
		return super.getObjectsByIntFieldValue(classType, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   							    ? (codRiscaldamento != null)
				   							      ? IMMOBILI_BY_RISCALDAMENTO_STORICO
				   							      : IMMOBILI_BY_RISCALDAMENTONULL_STORICO
				   							    : (codRiscaldamento != null)
				   							      ? IMMOBILI_BY_RISCALDAMENTO
				   							      : IMMOBILI_BY_RISCALDAMENTONULL, 
					   						   codRiscaldamento);
	}
	
	public ArrayList getImmobiliByRiscaldamentoIsAffitti(String classType, Integer codRiscaldamento){
		return super.getObjectsByIntFieldValue(classType, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   							    ? (codRiscaldamento != null)
				   							      ? IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI_STORICO
				   							      : IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI_STORICO
				   							    : (codRiscaldamento != null)
				   							      ? IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI
				   							      : IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI, 
					   						   codRiscaldamento);
	}

	public ArrayList getImmobiliByRiscaldamentoComune(String classType, Integer codRiscaldamento, String comune){
		return super.getObjectsByIntStrFieldValue(classType, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   							    ? (codRiscaldamento != null)
				   							      ? IMMOBILI_BY_RISCALDAMENTO_STORICO_COMUNE
				   							      : IMMOBILI_BY_RISCALDAMENTONULL_STORICO_COMUNE
				   							    : (codRiscaldamento != null)
				   							      ? IMMOBILI_BY_RISCALDAMENTO_COMUNE
				   							      : IMMOBILI_BY_RISCALDAMENTONULL_COMUNE, 
					   						   codRiscaldamento,comune);
	}
	
	public ArrayList getImmobiliByRiscaldamentoIsAffittiComune(String classType, Integer codRiscaldamento, String comune){
		return super.getObjectsByIntStrFieldValue(classType, 
				   							   (WinkhouseUtils.getInstance().getTipoArchivio())
				   							    ? (codRiscaldamento != null)
				   							      ? IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI_STORICO_COMUNE
				   							      : IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI_STORICO_COMUNE
				   							    : (codRiscaldamento != null)
				   							      ? IMMOBILI_BY_RISCALDAMENTO_ISAFFITTI_COMUNE
				   							      : IMMOBILI_BY_RISCALDAMENTONULL_ISAFFITTI_COMUNE, 
					   						   codRiscaldamento, comune);
	}

	public ArrayList getImmobiliByStatoConservativo(String classType, Integer codStatoConservativo){
		return super.getObjectsByIntFieldValue(classType, 
											   (WinkhouseUtils.getInstance().getTipoArchivio())
											   	? (codStatoConservativo != null)
											   	  ? IMMOBILI_BY_STATOCONSERVATIVO_STORICO
											   	  : IMMOBILI_BY_STATOCONSERVATIVONULL_STORICO
											   	: (codStatoConservativo != null)
											   	  ? IMMOBILI_BY_STATOCONSERVATIVO
											   	  : IMMOBILI_BY_STATOCONSERVATIVONULL, 
											   codStatoConservativo);
	}

	public ArrayList getImmobiliByStatoConservativoIsAffitti(String classType, Integer codStatoConservativo){
		return super.getObjectsByIntFieldValue(classType, 
											   (WinkhouseUtils.getInstance().getTipoArchivio())
											   	? (codStatoConservativo != null)
											   	  ? IMMOBILI_BY_STATOCONSERVATIVO_ISAFFITTI_STORICO
											   	  : IMMOBILI_BY_STATOCONSERVATIVONULL_ISAFFITTI_STORICO
											   	: (codStatoConservativo != null)
											   	  ? IMMOBILI_BY_STATOCONSERVATIVO_ISAFFITTI
											   	  : IMMOBILI_BY_STATOCONSERVATIVONULL_ISAFFITTI, 
											   codStatoConservativo);
	}

	public ArrayList getImmobiliByClasseEnergeticaIsAffitti(String classType, Integer codClasseEnergetica){
		return super.getObjectsByIntFieldValue(classType, 
											   (WinkhouseUtils.getInstance().getTipoArchivio())
											   	? (codClasseEnergetica != null)
											   	  ? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_ISAFFITTI
											   	  : LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO_ISAFFITTI
											   	: (codClasseEnergetica != null)
											   	  ? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_ISAFFITTI
											   	  : LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_ISAFFITTI, 
											   	codClasseEnergetica);
	}

	public ArrayList getImmobiliByClasseEnergetica(String classType, Integer codClasseEnergetica){
		return super.getObjectsByIntFieldValue(classType, 
											   (WinkhouseUtils.getInstance().getTipoArchivio())
											   	? (codClasseEnergetica != null)
											   	  ? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO
											   	  : LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO
											   	: (codClasseEnergetica != null)
											   	  ? LIST_IMMOBILI_BY_CODCLASSEENERGETICA
											   	  : LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL, 
											   	codClasseEnergetica);
	}

	public ArrayList getImmobiliByClasseEnergeticaIsAffittiComune(String classType, Integer codClasseEnergetica, String comune){
		return super.getObjectsByIntStrFieldValue(classType, 
											   (WinkhouseUtils.getInstance().getTipoArchivio())
											   	? (codClasseEnergetica != null)
											   	  ? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_ISAFFITTI_COMUNE
											   	  : LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO_ISAFFITTI_COMUNE
											   	: (codClasseEnergetica != null)
											   	  ? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_ISAFFITTI_COMUNE
											   	  : LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_ISAFFITTI_COMUNE, 
											   	codClasseEnergetica,
											   	comune);
	}

	public ArrayList getImmobiliByClasseEnergeticaComune(String classType, Integer codClasseEnergetica, String comune){
		return super.getObjectsByIntStrFieldValue(classType, 
											   (WinkhouseUtils.getInstance().getTipoArchivio())
											   	? (codClasseEnergetica != null)
											   	  ? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_STORICO_COMUNE
											   	  : LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_STORICO_COMUNE
											   	: (codClasseEnergetica != null)
											   	  ? LIST_IMMOBILI_BY_CODCLASSEENERGETICA_COMUNE
											   	  : LIST_IMMOBILI_BY_CODCLASSEENERGETICANULL_COMUNE, 
											   	codClasseEnergetica,
											   	comune);
	}

	public ArrayList getImmobiliByStatoConservativoComune(String classType, Integer codStatoConservativo,String comune){
		return super.getObjectsByIntStrFieldValue(classType, 
											   (WinkhouseUtils.getInstance().getTipoArchivio())
											   	? (codStatoConservativo != null)
											   	  ? IMMOBILI_BY_STATOCONSERVATIVO_STORICO_COMUNE
											   	  : IMMOBILI_BY_STATOCONSERVATIVONULL_STORICO_COMUNE
											   	: (codStatoConservativo != null)
											   	  ? IMMOBILI_BY_STATOCONSERVATIVO_COMUNE
											   	  : IMMOBILI_BY_STATOCONSERVATIVONULL_COMUNE, 
											   codStatoConservativo,
											   comune);
	}

	public ArrayList getImmobiliByStatoConservativoIsAffittiComune(String classType, Integer codStatoConservativo,String comune){
		return super.getObjectsByIntStrFieldValue(classType, 
											   (WinkhouseUtils.getInstance().getTipoArchivio())
											   	? (codStatoConservativo != null)
											   	  ? IMMOBILI_BY_STATOCONSERVATIVO_ISAFFITTI_STORICO_COMUNE
											   	  : IMMOBILI_BY_STATOCONSERVATIVONULL_ISAFFITTI_STORICO_COMUNE
											   	: (codStatoConservativo != null)
											   	  ? IMMOBILI_BY_STATOCONSERVATIVO_ISAFFITTI_COMUNE
											   	  : IMMOBILI_BY_STATOCONSERVATIVONULL_ISAFFITTI_COMUNE, 
											   codStatoConservativo,
											   comune);
	}
	
	public boolean delete(Integer codImmobile, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_IMMOBILI, codImmobile, con, doCommit);
	}

	public boolean updateAgenteInseritore(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_IMMOBILI_AGENTEINSERITORE, codAgenteNew, codAgenteOld, con, doCommit);
	}

	public boolean updateRiscaldamento(Integer codRiscaldamentoOld, Integer codRiscaldamentoNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_IMMOBILI_RISCALDAMENTO, codRiscaldamentoNew, codRiscaldamentoOld, con, doCommit);
	}

	public boolean updateStatoConservativo(Integer codStatoConservativoOld, Integer codStatoConservativoNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_IMMOBILI_STATOCONSERVATIVO, codStatoConservativoNew, codStatoConservativoOld, con, doCommit);
	}

	public boolean updateTipologiaImmobili(Integer codTipologiaImmobileOld, Integer codTipologiaImmobileNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_IMMOBILI_TIPOLOGIAIMMOBILI, codTipologiaImmobileNew, codTipologiaImmobileOld, con, doCommit);
	}
	
	public boolean updateClasseEnergetica(Integer codClasseEnergeticaOld, Integer codClasseEnergeticaNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_IMMOBILI_CODCLASSEENERGETICA, codClasseEnergeticaNew, codClasseEnergeticaOld, con, doCommit);
	}

	public boolean updateAnagrafica(Integer codAnagraficaOld, Integer codAnagraficaNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_IMMOBILI_ANAGRAFICA, codAnagraficaNew, codAnagraficaOld, con, doCommit);
	}

	public Object getImmobileById(String classType, Integer codImmobile){
		return super.getObjectById(classType, IMMOBILI_BY_ID, codImmobile);
	}
	
	public Object getImmobileByRif(String classType, String rif){
		return super.getObjectsByStringFieldValue(classType, IMMOBILI_BY_RIF, rif);
	}
	
	public boolean saveUpdate(ImmobiliVO iVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((iVO.getCodImmobile() == null) || (iVO.getCodImmobile() == 0))
						? getQuery(INSERT_IMMOBILI)
						: getQuery(UPDATE_IMMOBILI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, iVO.getRif());
			ps.setString(2, iVO.getIndirizzo());
			if (iVO.getNcivico() == null){
				ps.setNull(3, java.sql.Types.VARCHAR);
			}else{
				ps.setString(3, iVO.getNcivico());
			}
			ps.setString(4, iVO.getProvincia());
			ps.setString(5, iVO.getCap());
			ps.setString(6, iVO.getCitta());
			ps.setString(7, iVO.getZona());
			ps.setTimestamp(8, new Timestamp(iVO.getDataInserimento().getTime()));
			ps.setTimestamp(9, new Timestamp(iVO.getDataLibero().getTime()));
			ps.setString(10, iVO.getDescrizione());
			ps.setString(11, iVO.getMutuoDescrizione());
			ps.setDouble(12, iVO.getPrezzo());
			ps.setDouble(13, iVO.getMutuo());
			ps.setDouble(14, iVO.getSpese());
			ps.setString(15, iVO.getVarie());
			ps.setBoolean(16, iVO.getVisione());
			ps.setBoolean(17, iVO.getStorico());
			ps.setBoolean(18, iVO.getAffittabile());
			ps.setInt(19, iVO.getMq());
			ps.setInt(20, iVO.getAnnoCostruzione());
			
			if (iVO.getCodAgenteInseritore() == null) {
				ps.setNull(21, java.sql.Types.INTEGER);
			} else {
				ps.setInt(21, iVO.getCodAgenteInseritore());
			}						
			
			if (iVO.getCodAnagrafica() == null) {
				ps.setNull(22, java.sql.Types.INTEGER);
			} else {
				ps.setInt(22, iVO.getCodAnagrafica());
			}
						
			if (iVO.getCodRiscaldamento() == null) {
				ps.setNull(23, java.sql.Types.INTEGER);
			} else {
				ps.setInt(23, iVO.getCodRiscaldamento());
			}
			
			if (iVO.getCodStato() == null) {
				ps.setNull(24, java.sql.Types.INTEGER);
			} else {
				ps.setInt(24, iVO.getCodStato());
			}
			
			if (iVO.getCodTipologia() == null) {
				ps.setNull(25, java.sql.Types.INTEGER);
			} else {
				ps.setInt(25, iVO.getCodTipologia());
			}
			
			if (iVO.getCodClasseEnergetica() == null) {
				ps.setNull(26, java.sql.Types.INTEGER);
			} else {
				ps.setInt(26, iVO.getCodClasseEnergetica());
			}
			
			if (iVO.getCodUserUpdate() == null){
				ps.setNull(27, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(27, iVO.getCodUserUpdate());
			}
			
			if (iVO.getDateUpdate() == null){
				iVO.setDateUpdate(new Date());
			}

			ps.setTimestamp(28, new Timestamp(iVO.getDateUpdate().getTime()));
			
			if ((iVO.getCodImmobile() != null) &&
				(iVO.getCodImmobile() != 0)){
				ps.setInt(29, iVO.getCodImmobile());
			}
			ps.executeUpdate();
			if ((iVO.getCodImmobile() == null) ||
				(iVO.getCodImmobile() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					iVO.setCodImmobile(key);
					generatedkey = true;
					break;
				}
			}
			returnValue = true;
			if (doCommit){
				con.commit();
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				if (generatedkey){
					rs.close();
				}
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			try {
				if (doCommit){
					con.close();
				}
			} catch (SQLException e) {
				con = null;
			}
			
		}		
				
		return returnValue;
	}
	
	public boolean saveUpdateWithException(ImmobiliVO iVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((iVO.getCodImmobile() == null) || (iVO.getCodImmobile() == 0))
						? getQuery(INSERT_IMMOBILI)
						: getQuery(UPDATE_IMMOBILI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, iVO.getRif());
			ps.setString(2, iVO.getIndirizzo());
			if (iVO.getNcivico() == null){
				ps.setNull(3, java.sql.Types.VARCHAR);
			}else{
				ps.setString(3, iVO.getNcivico());
			}
			ps.setString(4, iVO.getProvincia());
			ps.setString(5, iVO.getCap());
			ps.setString(6, iVO.getCitta());
			ps.setString(7, iVO.getZona());
			ps.setTimestamp(8, new Timestamp(iVO.getDataInserimento().getTime()));
			ps.setTimestamp(9, new Timestamp(iVO.getDataLibero().getTime()));
			ps.setString(10, iVO.getDescrizione());
			ps.setString(11, iVO.getMutuoDescrizione());
			ps.setDouble(12, iVO.getPrezzo());
			ps.setDouble(13, iVO.getMutuo());
			ps.setDouble(14, iVO.getSpese());
			ps.setString(15, iVO.getVarie());
			ps.setBoolean(16, iVO.getVisione());
			ps.setBoolean(17, iVO.getStorico());
			ps.setBoolean(18, iVO.getAffittabile());
			ps.setInt(19, iVO.getMq());
			ps.setInt(20, iVO.getAnnoCostruzione());
			
			if (iVO.getCodAgenteInseritore() == null) {
				ps.setNull(21, java.sql.Types.INTEGER);
			} else {
				ps.setInt(21, iVO.getCodAgenteInseritore());
			}						
			
			if (iVO.getCodAnagrafica() == null) {
				ps.setNull(22, java.sql.Types.INTEGER);
			} else {
				ps.setInt(22, iVO.getCodAnagrafica());
			}
						
			if (iVO.getCodRiscaldamento() == null) {
				ps.setNull(23, java.sql.Types.INTEGER);
			} else {
				ps.setInt(23, iVO.getCodRiscaldamento());
			}
			
			if (iVO.getCodStato() == null) {
				ps.setNull(24, java.sql.Types.INTEGER);
			} else {
				ps.setInt(24, iVO.getCodStato());
			}
			
			if (iVO.getCodTipologia() == null) {
				ps.setNull(25, java.sql.Types.INTEGER);
			} else {
				ps.setInt(25, iVO.getCodTipologia());
			}
			
			if (iVO.getCodClasseEnergetica() == null) {
				ps.setNull(26, java.sql.Types.INTEGER);
			} else {
				ps.setInt(26, iVO.getCodClasseEnergetica());
			}
			
			if (iVO.getCodUserUpdate() == null){
				ps.setNull(27, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(27, iVO.getCodUserUpdate());
			}
			
			if (iVO.getDateUpdate() == null){
				iVO.setDateUpdate(new Date());
			}

			ps.setTimestamp(28, new Timestamp(iVO.getDateUpdate().getTime()));
			
			if ((iVO.getCodImmobile() != null) &&
				(iVO.getCodImmobile() != 0)){
				ps.setInt(29, iVO.getCodImmobile());
			}
			ps.executeUpdate();
			if ((iVO.getCodImmobile() == null) ||
				(iVO.getCodImmobile() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					iVO.setCodImmobile(key);
					generatedkey = true;
					break;
				}
			}
			returnValue = true;
			if (doCommit){
				con.commit();
			}
		}catch(SQLException sql){
			throw sql;
		}finally{
			try {
				if (generatedkey){
					rs.close();
				}
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			try {
				if (doCommit){
					con.close();
				}
			} catch (SQLException e) {
				con = null;
			}
			
		}		
				
		return returnValue;
	}

	public ArrayList getImmobiliByAnagrafica(String classType, Integer codAnagrafica){
		return super.getObjectsByIntFieldValue(classType, IMMOBILI_BY_ANAGRAFICA, codAnagrafica);
	}

	public Integer getMaxCodImmobile(){
		ArrayList al = super.list(ImmobiliVO.class.getName(), GET_MAX_CODIMMOBILE);
		return (al.size() == 0)?0:((ImmobiliVO)al.get(0)).getCodImmobile();
	}

	public ArrayList getImmobiliByAnagraficaPropietario(String classType, Integer codAnagrafica){
		return super.getObjectsByIntFieldValue(classType, IMMOBILI_BY_CODANAGRAFICA, codAnagrafica);
	}

	public boolean updateImmobiliAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_IMMOBILI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
