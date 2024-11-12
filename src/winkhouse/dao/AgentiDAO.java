package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

import winkhouse.db.ConnectionManager;
import winkhouse.db.orm.CayenneContextManager;
import winkhouse.orm.Agenti;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Immobili;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AgentiVO;


public class AgentiDAO extends BaseDAO{

	public final static String LISTA_AGENTI = "LISTA_AGENTI";	
	public final static String FIND_AGENTE_BY_ID = "FIND_AGENTE_BY_ID";
	public final static String FIND_AGENTE_BY_NOME_COGNOME_CITTA_INDIRIZZO = "FIND_AGENTE_BY_NOME_COGNOME_CITTA_INDIRIZZO";
	public final static String INSERT_AGENTE = "INSERT_AGENTE";
	public final static String UPDATE_AGENTE = "UPDATE_AGENTE";
	public final static String DELETE_AGENTE = "DELETE_AGENTE";
	public final static String CHK_SET_PASSWORDS = "CHK_SET_PASSWORDS";
	public final static String LOGIN_AGENTE = "LOGIN_AGENTE";
	public final static String UPDATE_AGENTI_UPDATE = "LOGIN_AGENTE_UPDATE";
	public final static String AGENTI_COLLOQUI_PARTECIPANTI = "AGENTI_COLLOQUI_PARTECIPANTI";
	public final static String AGENTI_COLLOQUI_INSERITORI = "AGENTI_COLLOQUI_INSERITORI";
	
	public AgentiDAO() {
		
	}	
	
	public Object getAgenteById(String classType, Integer idAgente){
		return super.getObjectById(classType, FIND_AGENTE_BY_ID, idAgente);
	}	

	public Agenti getAgenteById(Integer idAgente){
		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
		return Cayenne.objectForPK(context,Agenti.class,idAgente);
	}	

	public <T> ArrayList<T> list(String classType){
		return super.list(classType, LISTA_AGENTI);
	}
	
	public ArrayList<Agenti> list(ObjectContext context){		
		return new ArrayList<Agenti>(ObjectSelect.query(Agenti.class).select(context));
	}

	public <T> ArrayList<T> listAgentiColloquiPartecipanti(String classType){
		return super.list(classType, AGENTI_COLLOQUI_PARTECIPANTI);
	}

	public <T> ArrayList<T> listAgentiColloquiInseritori(String classType){
		return super.list(classType, AGENTI_COLLOQUI_INSERITORI);
	}
	
	public boolean delete(Integer codAgente, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_AGENTE, codAgente, con, doCommit);
	}	

	public boolean saveUpdate(AgentiVO aVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getCodAgente() == null) || (aVO.getCodAgente() == 0))
						? getQuery(INSERT_AGENTE)
						: getQuery(UPDATE_AGENTE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setString(1, aVO.getCognome());
			ps.setString(2, aVO.getNome());
			ps.setString(3, aVO.getIndirizzo());
			ps.setString(4, aVO.getProvincia());
			ps.setString(5, aVO.getCap());
			ps.setString(6, aVO.getCitta());
			if ((aVO.getUsername() == null) || (aVO.getUsername().trim().equalsIgnoreCase(""))){
				ps.setNull(7, java.sql.Types.VARCHAR);
			}else{ 
				ps.setString(7, aVO.getUsername());
			}
			if ((aVO.getPassword() == null) || (aVO.getPassword().trim().equalsIgnoreCase(""))){
				ps.setNull(8, java.sql.Types.VARCHAR);
			}else{ 
				ps.setString(8, WinkhouseUtils.getInstance().EncryptStringStandard(aVO.getPassword()));
			}						
			
			if ((aVO.getCodAgente() != null) &&					
				(aVO.getCodAgente() != 0)){
				ps.setInt(9, aVO.getCodAgente());
			}
			ps.executeUpdate();
			if ((aVO.getCodAgente() == null) ||
				(aVO.getCodAgente() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setCodAgente(key);
					generatedkey = true;
					break;
				}
			}
			returnValue = true;
			if (doCommit){
				con.commit();
			}
		}catch(SQLException sql){
			if (doCommit){
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
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

	public boolean saveUpdateWithException(AgentiVO aVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getCodAgente() == null) || (aVO.getCodAgente() == 0))
						? getQuery(INSERT_AGENTE)
						: getQuery(UPDATE_AGENTE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setString(1, aVO.getCognome());
			ps.setString(2, aVO.getNome());
			ps.setString(3, aVO.getIndirizzo());
			ps.setString(4, aVO.getProvincia());
			ps.setString(5, aVO.getCap());
			ps.setString(6, aVO.getCitta());
			if ((aVO.getUsername() == null) || (aVO.getUsername().trim().equalsIgnoreCase(""))){
				ps.setNull(7, java.sql.Types.VARCHAR);
			}else{ 
				ps.setString(7, aVO.getUsername());
			}
			if ((aVO.getPassword() == null) || (aVO.getPassword().trim().equalsIgnoreCase(""))){
				ps.setNull(8, java.sql.Types.VARCHAR);
			}else{ 
				ps.setString(8, WinkhouseUtils.getInstance().EncryptStringStandard(aVO.getPassword()));
			}			
			if ((aVO.getCodAgente() != null) &&
				(aVO.getCodAgente() != 0)){
				ps.setInt(9, aVO.getCodAgente());
			}
			ps.executeUpdate();
			if ((aVO.getCodAgente() == null) ||
				(aVO.getCodAgente() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setCodAgente(key);
					generatedkey = true;
					break;
				}
			}
			returnValue = true;
			if (doCommit){
				con.commit();
			}
		}catch(SQLException sql){
			if (doCommit){
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
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
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> findAgentiByNomeCognomeCittaIndirizzo(String classType,
														   AgentiVO aVO, 
														   Connection connection){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(FIND_AGENTE_BY_NOME_COGNOME_CITTA_INDIRIZZO);
						
		try{			
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, aVO.getCognome());
			ps.setString(2, aVO.getNome());
			ps.setString(3, aVO.getCitta());			
			ps.setString(4, aVO.getIndirizzo());
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T) getRowObject(classType, rs));
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
	
	public <T> ArrayList<T> checkSetPassword(String classType){
		return super.list(classType, CHK_SET_PASSWORDS);
	}
	
	public Agenti loginAgente(Agenti aVO){
		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
		return ObjectSelect.query(Agenti.class)
						   .where(Agenti.USERNAME.eq(aVO.getUsername()))
						   .and(Agenti.PASSWORD.eq(aVO.getPassword()))
						   .selectOne(context);			
	}
	
	public Object loginAgente(String classType,AgentiVO aVO,Connection connection){

		Object returnValue = null;
		
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(LOGIN_AGENTE);
		
		try{			
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, aVO.getUsername());
			ps.setString(2, aVO.getPassword());
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue = getRowObject(classType, rs);
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

	public boolean updateAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_AGENTI_UPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	


}