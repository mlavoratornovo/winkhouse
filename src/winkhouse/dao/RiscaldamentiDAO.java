package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;

import winkhouse.db.ConnectionManager;
import winkhouse.model.RiscaldamentiModel;
import winkhouse.orm.Riscaldamenti;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.RiscaldamentiVO;


public class RiscaldamentiDAO extends BaseDAO{

	public final static String LISTA_RISCALDAMENTI = "LISTA_RISCALDAMENTI";
	public final static String LISTA_RISCALDAMENTI_COMUNE = "LISTA_RISCALDAMENTI_COMUNE";
	public final static String RISCALDAMENTI_BY_ID = "RISCALDAMENTI_BY_ID";
	public final static String RISCALDAMENTI_BY_NAME = "RISCALDAMENTI_BY_NAME";
	public final static String INSERT_RISCALDAMENTI = "INSERT_RISCALDAMENTI";
	public final static String UPDATE_RISCALDAMENTI = "UPDATE_RISCALDAMENTI";
	public final static String DELETE_RISCALDAMENTI = "DELETE_RISCALDAMENTI";
	public final static String GET_RISCALDAMENTI_AFFITTI = "GET_RISCALDAMENTI_AFFITTI";
	public final static String GET_RISCALDAMENTI_AFFITTI_COMUNE = "GET_RISCALDAMENTI_COMUNE_AFFITTI";
	public final static String UPDATE_RISCALDAMENTI_AGENTEUPDATE = "UPDATE_RISCALDAMENTI_AGENTE_UPDATE";
	
	public RiscaldamentiDAO(){
		
	}
	
	public <T> ArrayList<T> list(String classType){
		return super.list(classType, LISTA_RISCALDAMENTI);
	}

	public ArrayList<RiscaldamentiModel> listByComune(String comune){
		
		ArrayList<RiscaldamentiModel> returnValue = new ArrayList<RiscaldamentiModel>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LISTA_RISCALDAMENTI_COMUNE);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				ps.setString(1, comune);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(new RiscaldamentiModel(rs,comune));
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
	/*		try {
				con.close();
			} catch (SQLException e) {
				con = null;
			}*/
			
		}		
		
		return returnValue;

	}
	
	public ArrayList<RiscaldamentiModel> listByAffittiComune(String comune){
		
		ArrayList<RiscaldamentiModel> returnValue = new ArrayList<RiscaldamentiModel>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(GET_RISCALDAMENTI_AFFITTI_COMUNE);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				ps.setString(1, comune);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(new RiscaldamentiModel(rs,comune));
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
	/*		try {
				con.close();
			} catch (SQLException e) {
				con = null;
			}*/
			
		}		
		
		return returnValue;

	}
	
	public <T> ArrayList<T> listByAffitti(String classType){
		return super.list(classType, GET_RISCALDAMENTI_AFFITTI);
	}

	public boolean saveUpdate(RiscaldamentiVO rVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((rVO.getCodRiscaldamento() == null) || (rVO.getCodRiscaldamento() == 0))
						? getQuery(INSERT_RISCALDAMENTI)
						:getQuery(UPDATE_RISCALDAMENTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, rVO.getDescrizione());
			if ((rVO.getCodRiscaldamento() != null) && 
			    (rVO.getCodRiscaldamento() != 0)){
				ps.setInt(2, rVO.getCodRiscaldamento());
			}
			ps.executeUpdate();
			if ((rVO.getCodRiscaldamento() == null) ||
				(rVO.getCodRiscaldamento() != 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					rVO.setCodRiscaldamento(key);
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

	public boolean saveUpdateWithException(RiscaldamentiVO rVO, 
										   Connection connection, 
										   Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((rVO.getCodRiscaldamento() == null) || (rVO.getCodRiscaldamento() == 0))
						? getQuery(INSERT_RISCALDAMENTI)
						:getQuery(UPDATE_RISCALDAMENTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, rVO.getDescrizione());
			if ((rVO.getCodRiscaldamento() != null) && 
			    (rVO.getCodRiscaldamento() != 0)){
				ps.setInt(2, rVO.getCodRiscaldamento());
			}
			ps.executeUpdate();
			if ((rVO.getCodRiscaldamento() == null) ||
				(rVO.getCodRiscaldamento() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					rVO.setCodRiscaldamento(key);
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

	
	public boolean delete(Integer codRiscaldamento, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_RISCALDAMENTI, codRiscaldamento, con, doCommit);
	}	
		
	public Object getRiscaldamentiById(String classType, Integer codRiscaldamento){
		return super.getObjectById(classType, RISCALDAMENTI_BY_ID, codRiscaldamento);
	}	

	public Riscaldamenti getRiscaldamentoById(Integer codRiscaldamento){
		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
		return Cayenne.objectForPK(context,Riscaldamenti.class,codRiscaldamento);
	}

	public <T> ArrayList<T> getRiscaldamentoByDescrizione(String classType, String descrizione){
		return super.getObjectsByStringFieldValue(classType, RISCALDAMENTI_BY_NAME, descrizione);
	}	

	public boolean updateRiscaldamentiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_RISCALDAMENTI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
