package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;

import winkhouse.db.ConnectionManager;
import winkhouse.orm.Affitti;
import winkhouse.orm.Anagrafiche;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AffittiVO;


public class AffittiDAO extends BaseDAO {

	public final static String FIND_AFFITTO_BY_ID = "FIND_AFFITTO_BY_ID";
	public final static String FIND_AFFITTI_BY_CODIMMOBILE = "FIND_AFFITTI_BY_CODIMMOBILE";
	public final static String FIND_AFFITTI_BY_CODAGENTE = "FIND_AFFITTI_BY_CODAGENTE";
	public final static String FIND_AFFITTI_CODANAGRAFICA = "FIND_AFFITTI_CODANAGRAFICA";	
	public final static String INSERT_AFFITTO = "INSERT_AFFITTO";
	public final static String UPDATE_AFFITTO = "UPDATE_AFFITTO";
	public final static String DELETE_AFFITTO = "DELETE_AFFITTO";
	public final static String DELETE_AFFITTO_BY_CODIMMOBILE = "DELETE_AFFITTO_BY_CODIMMOBILE";
	
	public final static String FIND_AFFITTI_IMMOBILE_DATA = "FIND_AFFITTI_IMMOBILE_DATA";
	public final static String UPDATE_AFFITTI_AGENTEINSERITORE = "UPDATE_AFFITTI_AGENTE_INSERITORE";
	
	public final static String FIND_AFFITTI_IMMOBILE_NEXT_BETWEEN = "FIND_AFFITTI_IMMOBILE_NEXT_BETWEEN";
	public final static String FIND_AFFITTI_IMMOBILE_PREVIOUS_BETWEEN = "FIND_AFFITTI_IMMOBILE_PREVIOUS_BETWEEN";
	public final static String FIND_AFFITTI_IMMOBILE_CONTAIN_BETWEEN = "FIND_AFFITTI_IMMOBILE_CONTAIN_BETWEEN";
	public final static String FIND_AFFITTI_IMMOBILE_MIDDLE_BETWEEN = "FIND_AFFITTI_IMMOBILE_MIDDLE_BETWEEN";
	public final static String FIND_AFFITTI_IMMOBILE_DATAINIZIO = "FIND_AFFITTI_IMMOBILE_DATAINIZIO";
	public final static String FIND_AFFITTI_IMMOBILE_NEXT_BETWEEN_2 = "FIND_AFFITTI_IMMOBILE_NEXT_BETWEEN_2";
	public final static String FIND_AFFITTI_IMMOBILE_PREVIOUS_BETWEEN_2 = "FIND_AFFITTI_IMMOBILE_PREVIOUS_BETWEEN_2";
	public final static String FIND_AFFITTI_IMMOBILE_CONTAIN_BETWEEN_2 = "FIND_AFFITTI_IMMOBILE_CONTAIN_BETWEEN_2";
	public final static String FIND_AFFITTI_IMMOBILE_MIDDLE_BETWEEN_2 = "FIND_AFFITTI_IMMOBILE_MIDDLE_BETWEEN_2";
	public final static String FIND_AFFITTI_IMMOBILE_DATAINIZIO_2 = "FIND_AFFITTI_IMMOBILE_DATAINIZIO_2";
	public final static String UPDATE_AFFITTI_IMMOBILE_AGENTEUPDATE = "UPDATE_AFFITTI_IMMOBILE_AGENTE_UPDATE";
	
	
	public AffittiDAO() {
	}

	public Object getAffittoByID(String className, Integer codAffitto){
		return super.getObjectById(className, FIND_AFFITTO_BY_ID, codAffitto);
	}

	public Affitti getAffittoByID(int codAffitto){
		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
		return Cayenne.objectForPK(context,Affitti.class,codAffitto);

	}

	public <T> ArrayList<T> getAffittiByCodImmobile(String className, Integer codImmobile){
		return super.getObjectsByIntFieldValue(className, FIND_AFFITTI_BY_CODIMMOBILE, codImmobile);
	}

	public <T> ArrayList<T> getAffittiByCodAgente(String className, Integer codAgente){
		return super.getObjectsByIntFieldValue(className, FIND_AFFITTI_BY_CODAGENTE, codAgente);
	}

	public boolean deleteAffittoByID(Integer codAffitto, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTO, codAffitto, connection, doCommit);
	}

	public boolean updateAgenteInseritore(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_AFFITTI_AGENTEINSERITORE, codAgenteNew, codAgenteOld, con, doCommit);
	}

	public boolean deleteAffittoByCodImmobile(Integer codImmobile, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTO_BY_CODIMMOBILE, codImmobile, connection, doCommit);
	}
	
	public boolean saveUpdate(AffittiVO aVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getCodAffitti() == null) || (aVO.getCodAffitti() == 0))
						? getQuery(INSERT_AFFITTO)
						: getQuery(UPDATE_AFFITTO);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);	
			if (aVO.getCodImmobile() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, aVO.getCodImmobile());
			}
			if (aVO.getCodAgenteIns() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, aVO.getCodAgenteIns());
			}
						
			ps.setTimestamp(3, new Timestamp(aVO.getDataInizio().getTime()));
			
			if(aVO.getDataFine() != null){
				ps.setTimestamp(4, new Timestamp(aVO.getDataFine().getTime()));
			}else{
				ps.setNull(4, java.sql.Types.TIMESTAMP);
			}
			
			ps.setDouble(5, aVO.getCauzione());
			ps.setDouble(6, aVO.getRata());
			ps.setString(7, aVO.getDescrizione());
			
			if (aVO.getCodUserUpdate() == null){
				ps.setNull(8, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(8, aVO.getCodUserUpdate());
			}
			if (aVO.getDateUpdate() == null){
				aVO.setDateUpdate(new Date());
			}
			ps.setTimestamp(9, new Timestamp(aVO.getDateUpdate().getTime()));
			
			
			if ((aVO.getCodAffitti() != null) &&
				(aVO.getCodAffitti() != 0)){
				ps.setInt(10, aVO.getCodAffitti());
			}
			ps.executeUpdate();
			if ((aVO.getCodAffitti() == null) ||
				(aVO.getCodAffitti() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setCodAffitti(key);
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

	public boolean saveUpdateWithException(AffittiVO aVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getCodAffitti() == null) || (aVO.getCodAffitti() == 0))
						? getQuery(INSERT_AFFITTO)
						: getQuery(UPDATE_AFFITTO);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			if (aVO.getCodImmobile() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, aVO.getCodImmobile());
			}
			if (aVO.getCodAgenteIns() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, aVO.getCodAgenteIns());
			}
			ps.setTimestamp(3, new Timestamp(aVO.getDataInizio().getTime()));
			
			if(aVO.getDataFine() != null){
				ps.setTimestamp(4, new Timestamp(aVO.getDataFine().getTime()));
			}else{
				ps.setNull(4, java.sql.Types.TIMESTAMP);
			}
			
			ps.setDouble(5, aVO.getCauzione());
			ps.setDouble(6, aVO.getRata());
			ps.setString(7, aVO.getDescrizione());
			
			if (aVO.getCodUserUpdate() == null){
				ps.setNull(8, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(8, aVO.getCodUserUpdate());
			}
			if (aVO.getDateUpdate() == null){
				aVO.setDateUpdate(new Date());
			}
			ps.setTimestamp(9, new Timestamp(aVO.getDateUpdate().getTime()));
			
			
			if ((aVO.getCodAffitti() != null) &&
				(aVO.getCodAffitti() != 0)){
				ps.setInt(10, aVO.getCodAffitti());
			}
			ps.executeUpdate();
			if ((aVO.getCodAffitti() == null) ||
				(aVO.getCodAffitti() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setCodAffitti(key);
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

	protected Timestamp convertDateToTimestamp(Date data){

		Calendar c1 = Calendar.getInstance();
		c1.setTime(data);
		Calendar cinizio = Calendar.getInstance();
		cinizio.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cinizio.set(Calendar.MILLISECOND,0);
		Timestamp returnValue = new Timestamp(cinizio.getTimeInMillis());

		return returnValue;
		
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAffittiByDataInizio(String classType, Date dataInizio, Integer codImmobile, Integer codAffitto){
		
		ArrayList<T> returnValue = new ArrayList<T>();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		if (codAffitto != null && codAffitto != 0){
			query = getQuery(FIND_AFFITTI_IMMOBILE_DATAINIZIO);
		}else{
			query = getQuery(FIND_AFFITTI_IMMOBILE_DATAINIZIO_2);
		}
		
		Timestamp ts_inizio = convertDateToTimestamp(dataInizio);
		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setTimestamp(1, ts_inizio);
			ps.setTimestamp(2, ts_inizio);
			ps.setInt(3, codImmobile);
			if (codAffitto != null && codAffitto != 0){
				ps.setInt(4, codAffitto);
			}
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
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAffittiNextBetWeen(String classType, 
											Date dataInizio, 
											Date dataFine, 
											Integer codImmobile, 
											Integer codAffitto){
		
		ArrayList<T> returnValue = new ArrayList<T>();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		if (codAffitto != null && codAffitto != 0){
			query = getQuery(FIND_AFFITTI_IMMOBILE_NEXT_BETWEEN);
		}else{
			query = getQuery(FIND_AFFITTI_IMMOBILE_NEXT_BETWEEN_2);
		}
		
		Timestamp ts_inizio = convertDateToTimestamp(dataInizio);
		Timestamp ts_fine = convertDateToTimestamp(dataFine);
		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setTimestamp(1, ts_inizio);
			ps.setTimestamp(2, ts_fine);
			ps.setInt(3, codImmobile);
			if (codAffitto != null && codAffitto != 0){
				ps.setInt(4, codAffitto);
			}
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

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAffittiPreviousBetWeen(String classType, Date dataInizio, Date dataFine, Integer codImmobile, Integer codAffitto){
		
		ArrayList<T> returnValue = new ArrayList<T>();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		if (codAffitto != null && codAffitto != 0){
			query = getQuery(FIND_AFFITTI_IMMOBILE_PREVIOUS_BETWEEN);
		}else{
			query = getQuery(FIND_AFFITTI_IMMOBILE_PREVIOUS_BETWEEN_2);
		}
		
		Timestamp ts_inizio = convertDateToTimestamp(dataInizio);
		Timestamp ts_fine = convertDateToTimestamp(dataFine);
		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setTimestamp(1, ts_inizio);
			ps.setTimestamp(2, ts_fine);
			ps.setInt(3, codImmobile);
			if (codAffitto != null && codAffitto != 0){
				ps.setInt(4, codAffitto);
			}
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
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAffittiContainBetWeen(String classType, Date dataInizio, Date dataFine, Integer codImmobile, Integer codAffitto){
		
		ArrayList<T> returnValue = new ArrayList<T>();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		if (codAffitto != null && codAffitto != 0){
			query = getQuery(FIND_AFFITTI_IMMOBILE_CONTAIN_BETWEEN);
		}else{
			query = getQuery(FIND_AFFITTI_IMMOBILE_CONTAIN_BETWEEN_2);
		}
		
		Timestamp ts_inizio = convertDateToTimestamp(dataInizio);
		Timestamp ts_fine = convertDateToTimestamp(dataFine);
		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setTimestamp(1, ts_inizio);
			ps.setTimestamp(2, ts_fine);
			ps.setInt(3, codImmobile);
			if (codAffitto != null && codAffitto != 0){
				ps.setInt(4, codAffitto);
			}
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

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAffittiMiddleBetWeen(String classType, Date dataInizio, Date dataFine, Integer codImmobile, Integer codAffitto){
		
		ArrayList<T> returnValue = new ArrayList<T>();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = null;
		if (codAffitto != null && codAffitto != 0){
			query = getQuery(FIND_AFFITTI_IMMOBILE_MIDDLE_BETWEEN);
		}else{
			query = getQuery(FIND_AFFITTI_IMMOBILE_MIDDLE_BETWEEN_2);
		}
		
		Timestamp ts_inizio = convertDateToTimestamp(dataInizio);
		Timestamp ts_fine = convertDateToTimestamp(dataFine);
		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setTimestamp(1, ts_inizio);
			ps.setTimestamp(2, ts_fine);
			ps.setTimestamp(3, ts_inizio);
			ps.setTimestamp(4, ts_fine);			
			ps.setInt(5, codImmobile);
			if (codAffitto != null && codAffitto != 0){
				ps.setInt(6, codAffitto);
			}
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
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAffittiData(String classType, 
									Date dataInizio,
									Date dataFine,
									Integer codImmobile){
		
		ArrayList<T> returnValue = new ArrayList<T>();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(FIND_AFFITTI_IMMOBILE_DATA);
		
		Timestamp ts_inizio = convertDateToTimestamp(dataInizio);
		Timestamp ts_fine = convertDateToTimestamp(dataFine);
		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setTimestamp(1, ts_inizio);
			ps.setTimestamp(2, ts_fine);
			ps.setInt(3, codImmobile);
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
		/*	try {
				con.close();
			} catch (SQLException e) {
				con = null;
			}*/
			
		}		
				
		return returnValue;
		
	}	
	
	public <T> ArrayList<T> getAffittiByCodAnagrafica(String classType,Integer codAnagrafica){
		return super.getObjectsByIntFieldValue(classType, FIND_AFFITTI_CODANAGRAFICA, codAnagrafica);
	}
	
	public boolean updateAffittiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_AFFITTI_IMMOBILE_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
