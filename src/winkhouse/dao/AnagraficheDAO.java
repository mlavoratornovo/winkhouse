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
import winkhouse.vo.AnagraficheVO;


public class AnagraficheDAO extends BaseDAO{

	public final static String LISTA_ANAGRAFICHE = "LISTA_ANAGRAFICHE";
	public final static String LISTA_ANAGRAFICHE_STORICO = "LISTA_ANAGRAFICHE_STORICO";
	
	public final static String ANAGRAFICHE_BY_CLASSE = "ANAGRAFICHE_BY_CLASSE";
	public final static String ANAGRAFICHE_BY_CLASSENULL = "ANAGRAFICHE_BY_CLASSENULL";
	public final static String ANAGRAFICHE_BY_CLASSE_STORICO = "ANAGRAFICHE_BY_CLASSE_STORICO";
	public final static String ANAGRAFICHE_BY_CLASSENULL_STORICO = "ANAGRAFICHE_BY_CLASSENULL_STORICO";
	public final static String ANAGRAFICHE_BY_AGENTEINSERITORE = "ANAGRAFICHE_BY_AGENTE_INSERITORE";
	public final static String ANAGRAFICHE_BY_AGENTEINSERITORENULL = "ANAGRAFICHE_BY_AGENTE_INSERITORENULL";
	public final static String ANAGRAFICHE_BY_CODIMMOBILE = "ANAGRAFICHE_BY_CODIMMOBILE";
	
	public final static String GET_ANAGRAFICHE_BY_COMUNE_CLASSE = "GET_ANAGRAFICHE_BY_COMUNE_CLASSE";
	public final static String GET_ANAGRAFICHE_BY_COMUNE_CLASSENULL = "GET_ANAGRAFICHE_BY_COMUNE_CLASSENULL";
	
	public final static String INSERT_ANAGRAFICHE = "INSERT_ANAGRAFICHE";
	public final static String UPDATE_ANAGRAFICHE = "UPDATE_ANAGRAFICHE";
	public final static String UPDATE_ANAGRAFICHE_AGENTEINSERITORE = "UPDATE_ANAGRAFICHE_AGENTE_INSERITORE";
	public final static String UPDATE_ANAGRAFICHE_AGENTEUPDATE = "UPDATE_ANAGRAFICHE_AGENTE_UPDATE";
	public final static String UPDATE_ANAGRAFICHE_CLASSE = "UPDATE_ANAGRAFICHE_CLASSE";
	public final static String DELETE_ANAGRAFICHE = "DELETE_ANAGRAFICHE";
	public final static String ANAGRAFICHE_BY_ID = "ANAGRAFICHE_BY_ID";
	public final static String ANAGRAFICHE_IN_COLLOQUI_BY_CLASSECLIENTE_STORICO = "ANAGRAFICHE_IN_COLLOQUI_BY_CLASSECLIENTE_STORICO";
	public final static String ANAGRAFICHE_IN_COLLOQUI_BY_CLASSECLIENTE = "ANAGRAFICHE_IN_COLLOQUI_BY_CLASSECLIENTE";
	
	public AnagraficheDAO() {
	}

	public <T> ArrayList<T> list(String classType){
		
		if (WinkhouseUtils.getInstance()
							.getTipoArchivio()){
			return super.list(classType, LISTA_ANAGRAFICHE_STORICO);
		}else{
			return super.list(classType, LISTA_ANAGRAFICHE);
		}
		
	}

	public boolean saveUpdate(AnagraficheVO aVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getCodAnagrafica() == null) || (aVO.getCodAnagrafica() == 0))
						? getQuery(INSERT_ANAGRAFICHE)
						: getQuery(UPDATE_ANAGRAFICHE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setString(1, aVO.getNome());
			ps.setString(2, aVO.getCognome());
			ps.setString(3, aVO.getRagioneSociale());
			ps.setString(4, aVO.getIndirizzo());
			
			if (aVO.getNcivico() == null){
				ps.setNull(5, java.sql.Types.VARCHAR);
			}else{
				ps.setString(5, aVO.getNcivico());
			}
			
			ps.setString(6, aVO.getProvincia());
			ps.setString(7, aVO.getCap());
			ps.setString(8, aVO.getCitta());
			ps.setTimestamp(9, new Timestamp((aVO.getDataInserimento()==null)
											  ? new Date().getTime()
											  : aVO.getDataInserimento().getTime()
											 ));
			ps.setString(10, aVO.getCommento());
			ps.setBoolean(11, aVO.getStorico());
			if (aVO.getCodClasseCliente() == null) {
				ps.setNull(12, java.sql.Types.INTEGER);
			} else {
				ps.setInt(12, aVO.getCodClasseCliente());
			}
			if (aVO.getCodAgenteInseritore() == null) {
				ps.setNull(13, java.sql.Types.INTEGER);
			} else {
				ps.setInt(13, aVO.getCodAgenteInseritore());
			}
						
			ps.setString(14, aVO.getCodiceFiscale());
			ps.setString(15, aVO.getPartitaIva());
			
			if (aVO.getCodUserUpdate() == null){
				ps.setNull(16, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(16, aVO.getCodUserUpdate());
			}
			
			if (aVO.getDateUpdate() == null){
				aVO.setDateUpdate(new Date());
			}
			
			ps.setTimestamp(17, new Timestamp(aVO.getDateUpdate().getTime()));			
			
			if ((aVO.getCodAnagrafica() != null) &&
				(aVO.getCodAnagrafica() != 0)){
				ps.setInt(18, aVO.getCodAnagrafica());
			}
			ps.executeUpdate();
			if ((aVO.getCodAnagrafica() == null) ||
				(aVO.getCodAnagrafica() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setCodAnagrafica(key);
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

	public boolean saveUpdateWithException(AnagraficheVO aVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getCodAnagrafica() == null) || (aVO.getCodAnagrafica() == 0))
						? getQuery(INSERT_ANAGRAFICHE)
						: getQuery(UPDATE_ANAGRAFICHE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, aVO.getNome());
			ps.setString(2, aVO.getCognome());
			ps.setString(3, aVO.getRagioneSociale());
			ps.setString(4, aVO.getIndirizzo());
			
			if (aVO.getNcivico() == null){
				ps.setNull(5, java.sql.Types.VARCHAR);
			}else{
				ps.setString(5, aVO.getNcivico());
			}
			
			ps.setString(6, aVO.getProvincia());
			ps.setString(7, aVO.getCap());
			ps.setString(8, aVO.getCitta());
			ps.setTimestamp(9, new Timestamp((aVO.getDataInserimento()==null)
											  ? new Date().getTime()
											  : aVO.getDataInserimento().getTime()
											 ));
			ps.setString(10, aVO.getCommento());
			ps.setBoolean(11, aVO.getStorico());
			if ((aVO.getCodClasseCliente() == null) || (aVO.getCodClasseCliente() == 0)) {
				ps.setNull(12, java.sql.Types.INTEGER);
			} else {
				ps.setInt(12, aVO.getCodClasseCliente());
			}
			if ((aVO.getCodAgenteInseritore() == null) || (aVO.getCodAgenteInseritore() == 0)) {
				ps.setNull(13, java.sql.Types.INTEGER);
			} else {
				ps.setInt(13, aVO.getCodAgenteInseritore());
			}
						
			ps.setString(14, aVO.getCodiceFiscale());
			ps.setString(15, aVO.getPartitaIva());
			
			if ((aVO.getCodUserUpdate() == null) || (aVO.getCodUserUpdate() == 0)){
				ps.setNull(16, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(16, aVO.getCodUserUpdate());
			}
			
			if (aVO.getDateUpdate() == null){
				aVO.setDateUpdate(new Date());
			}
			
			ps.setTimestamp(17, new Timestamp(aVO.getDateUpdate().getTime()));			
			
			if ((aVO.getCodAnagrafica() != null) &&
				(aVO.getCodAnagrafica() != 0)){
				ps.setInt(18, aVO.getCodAnagrafica());
			}
			ps.executeUpdate();
			if ((aVO.getCodAnagrafica() == null) ||
				(aVO.getCodAnagrafica() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setCodAnagrafica(key);
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
		return super.updateByIdWhereId(UPDATE_ANAGRAFICHE_AGENTEINSERITORE, codAgenteInseritoreNew, codAgenteInseritoreOld, con, doCommit);
	}

	public boolean updateAgenteUpdate(Integer codAgenteUpdateOld, Integer codAgenteUpdateNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_ANAGRAFICHE_AGENTEINSERITORE, codAgenteUpdateNew, codAgenteUpdateOld, con, doCommit);
	}

	public boolean updateClasseCliente(Integer codClasseClienteOld, Integer codClasseClienteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_ANAGRAFICHE_CLASSE, codClasseClienteNew, codClasseClienteOld, con, doCommit);
	}		
	
	public boolean delete(Integer codAnagrafica, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_ANAGRAFICHE, codAnagrafica, con, doCommit);
	}	
		
	public Object getAnagraficheById(String classType, Integer codAnagrafica){
		return super.getObjectById(classType, ANAGRAFICHE_BY_ID, codAnagrafica);
	}	
	
	public <T> ArrayList<T> getAnagraficheByClasse(String classType,Integer codClasse){
		if (WinkhouseUtils.getInstance()
							.getTipoArchivio()){
			if (codClasse != null){
				return super.getObjectsByIntFieldValue(classType, ANAGRAFICHE_BY_CLASSE_STORICO, codClasse);
			}else{
				return super.getObjectsByIntFieldValue(classType, ANAGRAFICHE_BY_CLASSENULL_STORICO, codClasse);
			}
		}else{
			if (codClasse != null){
				return super.getObjectsByIntFieldValue(classType, ANAGRAFICHE_BY_CLASSE, codClasse);
			}else{
				return super.getObjectsByIntFieldValue(classType, ANAGRAFICHE_BY_CLASSENULL, codClasse);
			}
		}
	}

	public <T> ArrayList<T> getAnagraficheColloquiByClasse(String classType,Integer codClasse){
		if (WinkhouseUtils.getInstance().getTipoArchivio()){
				return super.getObjectsByIntFieldValue(classType, ANAGRAFICHE_IN_COLLOQUI_BY_CLASSECLIENTE_STORICO, codClasse);
		}else{
				return super.getObjectsByIntFieldValue(classType, ANAGRAFICHE_IN_COLLOQUI_BY_CLASSECLIENTE, codClasse);
		}
	}

	public <T> ArrayList<T> getAnagraficheByAgenteInseritore(String classType,Integer codAgenteInseritore){
		if (codAgenteInseritore != null){
			return super.getObjectsByIntFieldValue(classType, ANAGRAFICHE_BY_AGENTEINSERITORE, codAgenteInseritore);
		}else{
			return super.getObjectsByIntFieldValue(classType, ANAGRAFICHE_BY_AGENTEINSERITORENULL, codAgenteInseritore);
		}
	}
	
	public <T> ArrayList<T> getAnagraficheByCodImmobile(String classType,Integer codImmobile){
		return super.getObjectsByIntFieldValue(classType, ANAGRAFICHE_BY_CODIMMOBILE, codImmobile);
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAnagraficheByComuneClasse(String classType, String comune, Integer codClasse){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		
		if (codClasse != null){
			query = getQuery(GET_ANAGRAFICHE_BY_COMUNE_CLASSE);
		}else{
			query = getQuery(GET_ANAGRAFICHE_BY_COMUNE_CLASSENULL);
		}
		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				ps.setString(1, comune);
				if (codClasse != null){
					ps.setInt(2, codClasse);
				}
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add((T) getRowObject(classType, rs));
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				if (rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				rs = null;
			}
			try {
				if (ps != null){
					ps.close();
				}
			} catch (SQLException e) {
				ps = null;
			}
			
		}		
		
		return returnValue;		
		
	}

}