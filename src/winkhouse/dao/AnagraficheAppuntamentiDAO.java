package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.AnagraficheAppuntamentiVO;


public class AnagraficheAppuntamentiDAO extends BaseDAO {

	public final static String INSERT_ANAGRAFICHEAPPUNTAMENTI = "INSERT_ANAGRAFICHEAPPUNTAMENTI";
	public final static String UPDATE_ANAGRAFICHEAPPUNTAMENTI = "UPDATE_ANAGRAFICHEAPPUNTAMENTI";
	public final static String LIST_ANAGRAFICHEAPPUNTAMENTI_BY_CODAPPUNTAMENTO = "LIST_ANAGRAFICHEAPPUNTAMENTI_BY_CODAPPUNTAMENTO";
	public final static String LIST_ANAGRAFICHEAPPUNTAMENTI_BY_CODANAGRAFICA = "LIST_ANAGRAFICHEAPPUNTAMENTI_BY_CODANAGRAFICA";
	public final static String DELETE_ANAGRAFICHEAPPUNTAMENTI = "DELETE_ANAGRAFICHEAPPUNTAMENTI";
	public final static String DELETE_ANAGRAFICHEAPPUNTAMENTI_BY_CODANAGRAFICA = "DELETE_ANAGRAFICHEAPPUNTAMENTI_BY_CODANAGRAFICA";
	public final static String DELETE_ANAGRAFICHEAPPUNTAMENTI_BY_CODAPPUNTAMENTO = "DELETE_ANAGRAFICHEAPPUNTAMENTI_BY_CODAPPUNTAMENTO";
	public final static String UPDATE_ANAGRAFICHEAPPUNTAMENTI_AGENTEUPDATE = "UPDATE_ANAGRAFICHEAPPUNTAMENTI_AGENTE_UPDATE";
	
	public AnagraficheAppuntamentiDAO() {}

	public ArrayList listAnagraficheAppuntamentiByAppuntamento(String classType, Integer codAppuntamento){
		return super.getObjectsByIntFieldValue(classType, 
											   LIST_ANAGRAFICHEAPPUNTAMENTI_BY_CODAPPUNTAMENTO, 
											   codAppuntamento);
	} 
	
	public ArrayList listAnagraficheAppuntamentiByAnagrafica(String classType, Integer codAnagrafica){
		return super.getObjectsByIntFieldValue(classType, 
											   LIST_ANAGRAFICHEAPPUNTAMENTI_BY_CODANAGRAFICA, 
											   codAnagrafica);
	}
	
	public boolean deleteAnagraficheAppuntamenti(Integer codAnagraficheAppuntamenti,Connection con ,Boolean doCommit){
		return super.deleteObjectById(DELETE_ANAGRAFICHEAPPUNTAMENTI, codAnagraficheAppuntamenti, con, doCommit);
	}
	
	public boolean deleteAnagraficheAppuntamentiByAnagrafica(Integer codAnagrafiche,Connection con ,Boolean doCommit){
		return super.deleteObjectById(DELETE_ANAGRAFICHEAPPUNTAMENTI_BY_CODANAGRAFICA, codAnagrafiche, con, doCommit);
	}

	public boolean deleteAnagraficheAppuntamentiByAppuntamento(Integer codAppuntamento,Connection con ,Boolean doCommit){
		return super.deleteObjectById(DELETE_ANAGRAFICHEAPPUNTAMENTI_BY_CODAPPUNTAMENTO, codAppuntamento, con, doCommit);
	}
	
	public boolean saveUpdate(AnagraficheAppuntamentiVO aaVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aaVO.getCodAnagraficheAppuntamenti() == null) || (aaVO.getCodAnagraficheAppuntamenti() == 0))
						? getQuery(INSERT_ANAGRAFICHEAPPUNTAMENTI)
						: getQuery(UPDATE_ANAGRAFICHEAPPUNTAMENTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			if (aaVO.getCodAnagrafica() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, aaVO.getCodAnagrafica());
			}			
			if (aaVO.getCodAppuntamento() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, aaVO.getCodAppuntamento());
			}			
			
			ps.setString(3, aaVO.getNote());
			if ((aaVO.getCodAnagraficheAppuntamenti() != null) &&
				(aaVO.getCodAnagraficheAppuntamenti() != 0)){
				ps.setInt(4, aaVO.getCodAnagraficheAppuntamenti());
			}
			ps.executeUpdate();
			if ((aaVO.getCodAnagraficheAppuntamenti() == null) ||
				(aaVO.getCodAnagraficheAppuntamenti() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aaVO.setCodAnagraficheAppuntamenti(key);
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

	public boolean updateAnagraficheAppuntamentiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_ANAGRAFICHEAPPUNTAMENTI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	


}
