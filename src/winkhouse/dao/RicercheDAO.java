package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.RicercheVO;


public class RicercheDAO extends BaseDAO {

	public final static String RICERCHE_BY_ID = "RICERCHE_BY_ID";
	public final static String RICERCHE_BY_TIPO = "RICERCHE_BY_TIPO";
	public final static String INSERT_RICERCHE = "INSERT_RICERCHE";
	public final static String UPDATE_RICERCHE = "UPDATE_RICERCHE";
	public final static String DELETE_RICERCHE = "DELETE_RICERCHE";	
	public final static String UPDATE_RICERCHE_AGENTEUPDATE = "UPDATE_RICERCHE_AGENTE_UPDATE";
	
	public RicercheDAO() {

	}

	public Object getRicercaById (String classType, Integer codRicerca){
		return super.getObjectById(classType, RICERCHE_BY_ID, codRicerca);
	}

	public ArrayList getRichercheByTipo(String classType, Integer codTipo){
		return super.getObjectsByIntFieldValue(classType, RICERCHE_BY_TIPO, codTipo);
	}
	
	public boolean delete(Integer codRicerca, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_RICERCHE, codRicerca, con, doCommit);
	}
		
	public boolean saveUpdate(RicercheVO rVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((rVO.getCodRicerca() == null) || (rVO.getCodRicerca() == 0))
						? getQuery(INSERT_RICERCHE)
						:getQuery(UPDATE_RICERCHE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, rVO.getNome());
			ps.setString(2, rVO.getDescrizione());
			ps.setInt(3, rVO.getTipo());
			if ((rVO.getCodRicerca() != null) && 
				(rVO.getCodRicerca() != 0)){
				ps.setInt(4, rVO.getCodRicerca());
			}
			ps.executeUpdate();
			if ((rVO.getCodRicerca() == null) || 
				(rVO.getCodRicerca() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					rVO.setCodRicerca(key);
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

	public boolean updateRichercheAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_RICERCHE_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
