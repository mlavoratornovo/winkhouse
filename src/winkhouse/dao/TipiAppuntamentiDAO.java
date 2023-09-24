package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.TipiAppuntamentiVO;

public class TipiAppuntamentiDAO extends BaseDAO {

	public final static String LISTA_TIPI_APPUNTAMENTI = "LISTA_TIPI_APPUNTAMENTI";
	public final static String TIPI_APPUNTAMENTI_BY_ID = "TIPI_APPUNTAMENTI_BY_ID";
	public final static String TIPI_APPUNTAMENTI_BY_NAME = "TIPI_APPUNTAMENTI_BY_NAME";
	public final static String INSERT_TIPI_APPUNTAMENTI = "INSERT_TIPI_APPUNTAMENTI";
	public final static String UPDATE_TIPI_APPUNTAMENTI = "UPDATE_TIPI_APPUNTAMENTI";
	public final static String DELETE_TIPI_APPUNTAMENTI = "DELETE_TIPI_APPUNTAMENTI";
	public final static String UPDATE_TIPI_APPUNTAMENTI_AGENTEUPDATE = "UPDATE_TIPI_APPUNTAMENTI_AGENTE_UPDATE";

	public TipiAppuntamentiDAO() {

	}
	
	public ArrayList listTipiAppuntamenti(String className){
		return super.list(className, LISTA_TIPI_APPUNTAMENTI);
	};
	
	public Object getTipiAppuntamentiByID(String className,Integer codTipiAppuntamenti){
		return super.getObjectById(className, TIPI_APPUNTAMENTI_BY_ID, codTipiAppuntamenti);
	}
	
	public ArrayList getTipiAppuntamentiByName(String className,String name){
		return super.getObjectsByStringFieldValue(className, TIPI_APPUNTAMENTI_BY_NAME, name);
	}
	
	public boolean delete(Integer codTipoAppuntamento, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_TIPI_APPUNTAMENTI, codTipoAppuntamento, con, doCommit);
	}
	
	public boolean saveUpdate(TipiAppuntamentiVO taVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((taVO.getCodTipoAppuntamento() == null) || (taVO.getCodTipoAppuntamento() == 0))
						? getQuery(INSERT_TIPI_APPUNTAMENTI)
						:getQuery(UPDATE_TIPI_APPUNTAMENTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);	
			ps.setString(1, taVO.getDescrizione());
			ps.setString(2, taVO.getgCalColor());
			ps.setInt(3, taVO.getOrdine());
			if ((taVO.getCodTipoAppuntamento() != null) && 
			    (taVO.getCodTipoAppuntamento() != 0)){
				ps.setInt(4, taVO.getCodTipoAppuntamento());
			}
			ps.executeUpdate();
			if ((taVO.getCodTipoAppuntamento() == null) || 
				(taVO.getCodTipoAppuntamento() == 0)){ 
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					taVO.setCodTipoAppuntamento(key);
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

	public boolean updateTipiAppuntamentiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_TIPI_APPUNTAMENTI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
