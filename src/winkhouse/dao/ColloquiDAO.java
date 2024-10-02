package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.cayenne.query.SelectQuery;

import winkhouse.db.ConnectionManager;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Colloqui;
import winkhouse.orm.Immobili;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.ColloquiVO;


public class ColloquiDAO extends BaseDAO{
	
	public final static String LISTA_COLLOQUI = "LISTA_COLLOQUI";
	public final static String COLLOQUI_BY_IMMOBILE = "COLLOQUI_BY_IMMOBILE";
	public final static String COLLOQUI_BY_AGENTEINSERITORE = "COLLOQUI_BY_AGENTE_INSERITORE";
	public final static String COLLOQUI_BY_AGENTE_PARTECIPANTE = "COLLOQUI_BY_AGENTE_PARTECIPANTE";
	public final static String COLLOQUI_BY_TIPOLOGIEIMMOBILE = "COLLOQUI_BY_TIPOLOGIEIMMOBILE";
	public final static String COLLOQUI_BY_CLASSECLIENTE = "COLLOQUI_BY_CLASSECLIENTE";
	public final static String INSERT_COLLOQUI = "INSERT_COLLOQUI";
	public final static String UPDATE_COLLOQUI = "UPDATE_COLLOQUI";
	public final static String UPDATE_COLLOQUI_AGENTEINSERITORE = "UPDATE_COLLOQUI_AGENTE_INSERITORE";
	public final static String DELETE_COLLOQUI = "DELETE_COLLOQUI";
	public final static String COLLOQUI_BY_ID = "COLLOQUI_BY_ID";
	public final static String COLLOQUI_BY_ICALID = "COLLOQUI_BY_ICALID";
	public final static String COLLOQUI_BY_ANAGRAFICA = "COLLOQUI_BY_ANAGRAFICA";
	public final static String COLLOQUI_BY_ANAGRAFICA_RICERCA = "COLLOQUI_BY_ANAGRAFICA_RICERCA";
	public final static String COLLOQUI_BY_ANAGRAFICA_ALLTYPES = "COLLOQUI_BY_ANAGRAFICA_ALLTYPES";
	public final static String COLLOQUI_BY_TYPE = "COLLOQUI_BY_TYPE";
	public final static String COLLOQUI_SCADENZIERE_AGENTI_DA_A = "COLLOQUI_SCADENZIERE_AGENTI_DA_A";
	public final static String COLLOQUI_SCADENZIERE_DA_A = "COLLOQUI_SCADENZIERE_DA_A";
	public final static String COLLOQUI_AGENTI_DA_A = "COLLOQUI_AGENTI_DA_A";
	public final static String COLLOQUI_ANAGRAFICHE_DA_A = "COLLOQUI_ANAGRAFICHE_DA_A";
	public final static String LIST_COLLOQUI_AGENTI_DA_A_ICAL_NULL = "LIST_COLLOQUI_AGENTI_DA_A_ICAL_NULL";
	public final static String LIST_COLLOQUI_AGENTI_DA_A_NOT_ICAL_NULL = "LIST_COLLOQUI_AGENTI_DA_A_NOT_ICAL_NULL";
	public final static String LIST_COLLOQUI_BY_PROPERTIES = "LIST_COLLOQUI_BY_PROPERTIES";
	public final static String UPDATE_COLLOQUI_AGENTEUPDATE = "UPDATE_COLLOQUI_AGENTE_UPDATE";
	public final static String COLLOQUI_SENZA_PARTECIPANTI = "COLLOQUI_SENZA_PARTECIPANTI";
	public final static String COLLOQUI_SENZA_INSERITORE = "COLLOQUI_SENZA_INSERITORE";
	public final static String COLLOQUI_YEARS_DATAINSERIMENTO = "COLLOQUI_YEARS_DATAINSERIMENTO";
	public final static String COLLOQUI_MONTHS_DATAINSERIMENTO_BY_YEAR = "COLLOQUI_MONTHS_DATAINSERIMENTO_BY_YEAR";
	public final static String COLLOQUI_DATAINSERIMENTO_BY_YEAR_MONTH = "COLLOQUI_DATAINSERIMENTO_BY_YEAR_MONTH";
	public final static String COLLOQUI_YEARS_DATACOLLOQUIO = "COLLOQUI_YEARS_DATACOLLOQUIO";
	public final static String COLLOQUI_MONTHS_DATACOLLOQUIO_BY_YEAR = "COLLOQUI_MONTHS_DATACOLLOQUIO_BY_YEAR";
	public final static String COLLOQUI_DATACOLLOQUIO_BY_YEAR_MONTH = "COLLOQUI_DATACOLLOQUIO_BY_YEAR_MONTH";
	
	public ColloquiDAO() {

	}

	public <T> ArrayList<T> list(String classType){
		return super.list(classType, LISTA_COLLOQUI);
	}

	public <T> ArrayList<T> listYearsByDataInserimento(String classType){
		return super.list(classType, COLLOQUI_YEARS_DATAINSERIMENTO);
	}

	public <T> ArrayList<T> listYearsByDataColloquio(String classType){
		return super.list(classType, COLLOQUI_YEARS_DATACOLLOQUIO);
	}

	public <T> ArrayList<T> listMonthsByYearDataInserimento(String classType, Integer year){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_MONTHS_DATAINSERIMENTO_BY_YEAR, year);
	}

	public <T> ArrayList<T> listMonthsByYearDataColloquio(String classType, Integer year){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_MONTHS_DATACOLLOQUIO_BY_YEAR, year);
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> listColloquiDataInserimentoBy(String classType, Integer year, Integer month){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(COLLOQUI_DATAINSERIMENTO_BY_YEAR_MONTH);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, year);
			ps.setInt(2, month);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T)getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
		}		
		
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> listColloquiDataColloquioBy(String classType, Integer year, Integer month){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(COLLOQUI_DATACOLLOQUIO_BY_YEAR_MONTH);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, year);
			ps.setInt(2, month);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T)getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			
		}		
		
		return returnValue;
	}
	
	public boolean saveUpdate(ColloquiVO cVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((cVO.getCodColloquio() == null) || (cVO.getCodColloquio() == 0))
						? getQuery(INSERT_COLLOQUI)
						: getQuery(UPDATE_COLLOQUI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);		
			ps.setString(1, cVO.getDescrizione());
			
			if (cVO.getCodAgenteInseritore() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, cVO.getCodAgenteInseritore());
			}

			if (cVO.getCodImmobileAbbinato() == null) {
				ps.setNull(3, java.sql.Types.INTEGER);
			} else {
				ps.setInt(3, cVO.getCodImmobileAbbinato());
			}

			if (cVO.getCodTipologiaColloquio() == null) {
				ps.setNull(4, java.sql.Types.INTEGER);
			} else {
				ps.setInt(4, cVO.getCodTipologiaColloquio());
			}
						
			ps.setTimestamp(5, new Timestamp(cVO.getDataInserimento().getTime()));
			ps.setTimestamp(6, new Timestamp(cVO.getDataColloquio().getTime()));			
			ps.setString(7, cVO.getLuogoIncontro());
			ps.setBoolean(8, cVO.getScadenziere());
			ps.setString(9, cVO.getCommentoAgenzia());
			ps.setString(10, cVO.getCommentoCliente());

			if (cVO.getCodParent() == null) {
				ps.setNull(11, java.sql.Types.INTEGER);
			} else {
				ps.setInt(11, cVO.getCodParent());
			}
						
			ps.setString(12, cVO.getiCalUid());
			
			if (cVO.getCodUserUpdate() == null){
				ps.setNull(13, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(13, cVO.getCodUserUpdate());
			}
			if (cVO.getDateUpdate() == null){
				cVO.setDateUpdate(new Date());
			}

			ps.setTimestamp(14, new Timestamp(cVO.getDateUpdate().getTime()));
			
			
			if ((cVO.getCodColloquio() != null) &&
				(cVO.getCodColloquio() != 0)){
				ps.setInt(15, cVO.getCodColloquio());
			}
			ps.executeUpdate();
			if ((cVO.getCodColloquio() == null) ||
				(cVO.getCodColloquio() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					cVO.setCodColloquio(key);
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

	public boolean saveUpdateWithException(ColloquiVO cVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((cVO.getCodColloquio() == null) || (cVO.getCodColloquio() == 0))
						? getQuery(INSERT_COLLOQUI)
						: getQuery(UPDATE_COLLOQUI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);		
			ps.setString(1, cVO.getDescrizione());
			
			if (cVO.getCodAgenteInseritore() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, cVO.getCodAgenteInseritore());
			}

			if (cVO.getCodImmobileAbbinato() == null) {
				ps.setNull(3, java.sql.Types.INTEGER);
			} else {
				ps.setInt(3, cVO.getCodImmobileAbbinato());
			}

			if (cVO.getCodTipologiaColloquio() == null) {
				ps.setNull(4, java.sql.Types.INTEGER);
			} else {
				ps.setInt(4, cVO.getCodTipologiaColloquio());
			}
			
			ps.setTimestamp(5, new Timestamp(cVO.getDataInserimento().getTime()));
			ps.setTimestamp(6, new Timestamp(cVO.getDataColloquio().getTime()));			
			ps.setString(7, cVO.getLuogoIncontro());
			ps.setBoolean(8, cVO.getScadenziere());
			ps.setString(9, cVO.getCommentoAgenzia());
			ps.setString(10, cVO.getCommentoCliente());
			
			if (cVO.getCodParent() == null) {
				ps.setNull(11, java.sql.Types.INTEGER);
			} else {
				ps.setInt(11, cVO.getCodParent());
			}

			ps.setString(12, cVO.getiCalUid());
			
			if (cVO.getCodUserUpdate() == null){
				ps.setNull(13, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(13, cVO.getCodUserUpdate());
			}
			if (cVO.getDateUpdate() == null){
				cVO.setDateUpdate(new Date());
			}

			ps.setTimestamp(14, new Timestamp(cVO.getDateUpdate().getTime()));
			
			
			if ((cVO.getCodColloquio() != null) &&
				(cVO.getCodColloquio() != 0)){
				ps.setInt(15, cVO.getCodColloquio());
			}
			ps.executeUpdate();
			if ((cVO.getCodColloquio() == null) ||
				(cVO.getCodColloquio() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					cVO.setCodColloquio(key);
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

	public boolean updateAgenteInseritore(Integer codAgenteInseritoreOld, Integer codAgenteInseritoreNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_COLLOQUI_AGENTEINSERITORE, codAgenteInseritoreNew, codAgenteInseritoreOld, con, doCommit);
	}	
	
	public boolean delete(Integer codColloquio, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_COLLOQUI, codColloquio, con, doCommit);
	}	
		
	public Object getColloquioById(String classType, Integer codColloquio){
		return super.getObjectById(classType, COLLOQUI_BY_ID, codColloquio);
	}	

	public Colloqui getColloquioById(Integer codColloquio){
		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
		return Cayenne.objectForPK(context,Colloqui.class,codColloquio);
	}	

	public <T> ArrayList<T> getColloquiByImmobile(String classType,Integer codClasse){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_BY_IMMOBILE, codClasse);
	}
	
	public ArrayList<Colloqui> getColloquiByImmobile(ObjectContext context,Immobili immobile){
		return new ArrayList<Colloqui>(ObjectSelect.query(Colloqui.class)
				  .where(Colloqui.IMMOBILI.eq(immobile))														  				  
				  .select(context));										
	}

	public <T> ArrayList<T> getColloquiByAgenteInseritore(String classType,Integer codAgenteInseritore){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_BY_AGENTEINSERITORE, codAgenteInseritore);
	}
	
	public <T> ArrayList<T> getColloquiWithoutAgenteInseritore(String classType){
		return super.list(classType, COLLOQUI_SENZA_INSERITORE);
	}

	public <T> ArrayList<T> getColloquiByAnagrafica(String classType,Integer codAnagrafica){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_BY_ANAGRAFICA, codAnagrafica);
	}

	public <T> ArrayList<T> getColloquiByAnagraficaAllTypes(String classType,Integer codAnagrafica){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_BY_ANAGRAFICA_ALLTYPES, codAnagrafica);
	}

	public ArrayList<Colloqui> getColloquiByAnagraficaRicerca(ObjectContext context, Anagrafiche anagrafica){
		SelectQuery<Colloqui> query = new SelectQuery<Colloqui>(Colloqui.class);
		query.andQualifier(ExpressionFactory.matchExp("colloquianagrafiches.anagrafiche", anagrafica));		
		return new ArrayList<Colloqui>(context.performQuery(query));
	}

	public <T> ArrayList<T> getColloquiByAnagraficaRicerca(String classType,Integer codAnagrafica){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_BY_ANAGRAFICA_RICERCA, codAnagrafica);
	}

	public <T> ArrayList<T> getColloquiByTipologia(String classType,Integer codTipologia){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_BY_TYPE, codTipologia);
	}

	public <T> ArrayList<T> getColloquiByTipologiaImmobile(String classType,Integer codTipologiaImmobile){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_BY_TIPOLOGIEIMMOBILE, codTipologiaImmobile);
	}

	public <T> ArrayList<T> getColloquiByAgentePartecipante(String classType,Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_BY_AGENTE_PARTECIPANTE, codAgente);
	}

	public <T> ArrayList<T> getColloquiWithoutAgentePartecipante(String classType){
		return super.list(classType, COLLOQUI_SENZA_PARTECIPANTI);
	}

	public <T> ArrayList<T> getColloquiByClasseCliente(String classType,Integer codClasseCliente){
		return super.getObjectsByIntFieldValue(classType, COLLOQUI_BY_CLASSECLIENTE, codClasseCliente);
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> listColloquiByAgenteDaA(String classType, Integer codAgente, Date dataDA, Date dataA){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(COLLOQUI_SCADENZIERE_AGENTI_DA_A);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codAgente);
			ps.setTimestamp(2, new Timestamp(dataDA.getTime()));
			ps.setTimestamp(3, new Timestamp(dataA.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T)getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			
		}		
		
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> listColloquiByAgenteDaAInNotScadenziere(String classType, Integer codAgente, Date dataDA, Date dataA){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(COLLOQUI_AGENTI_DA_A);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codAgente);
			ps.setTimestamp(2, new Timestamp(dataDA.getTime()));
			ps.setTimestamp(3, new Timestamp(dataA.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T)getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			
		}		
		
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> listColloquiByAnagraficheDaAInNotScadenziere(String classType, Integer codAnagrafica, Date dataDA, Date dataA){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(COLLOQUI_ANAGRAFICHE_DA_A);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codAnagrafica);
			ps.setTimestamp(2, new Timestamp(dataDA.getTime()));
			ps.setTimestamp(3, new Timestamp(dataA.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T)getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			
		}		
		
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> listColloquiByAgenteDaA_ICALL_Null(String classType, Integer codAgente, Date dataDA, Date dataA){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LIST_COLLOQUI_AGENTI_DA_A_ICAL_NULL);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codAgente);
			ps.setTimestamp(2, new Timestamp(dataDA.getTime()));
			ps.setTimestamp(3, new Timestamp(dataA.getTime()));
			ps.setInt(4, codAgente);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T)getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			
		}		
		
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> listColloquiByAgenteDaA_Not_ICALL_Null(String classType, Integer codAgente, Date dataDA, Date dataA){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LIST_COLLOQUI_AGENTI_DA_A_NOT_ICAL_NULL);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codAgente);
			ps.setTimestamp(2, new Timestamp(dataDA.getTime()));
			ps.setTimestamp(3, new Timestamp(dataA.getTime()));
			ps.setInt(4, codAgente);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T)getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			
		}		
		
		return returnValue;
	}	
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> listColloquiByDaA(String classType, Date dataDA, Date dataA){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(COLLOQUI_SCADENZIERE_DA_A);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setTimestamp(1, new Timestamp(dataDA.getTime()));
			ps.setTimestamp(2, new Timestamp(dataA.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T)getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			
		}		
		
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getColloquiByProperties(String classType, ColloquiVO colloquio){
	
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LIST_COLLOQUI_BY_PROPERTIES);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			
			ps.setTimestamp(1, new Timestamp(colloquio.getDataColloquio().getTime()));
			ps.setTimestamp(2, new Timestamp(colloquio.getDataInserimento().getTime()));
			ps.setBoolean(3, colloquio.getScadenziere());
			ps.setString(4, colloquio.getLuogoIncontro());
			ps.setString(5, colloquio.getCommentoAgenzia());
			ps.setString(6, colloquio.getCommentoCliente());
			ps.setString(7, colloquio.getDescrizione());
			
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T)getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			
		}		
		
		return returnValue;
		
	}

	public <T> ArrayList<T> getColloquiByICalId(String classType, String icalid){
		return super.getObjectsByStringFieldValue(classType, COLLOQUI_BY_ICALID, icalid);		
	}

	public boolean updateColloquiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_COLLOQUI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}