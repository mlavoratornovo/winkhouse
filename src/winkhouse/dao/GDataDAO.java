package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.GDataVO;

public class GDataDAO extends BaseDAO {

	public final static String GOOGLE_DATA_BY_ID = "GOOGLE_DATA_BY_ID";
	public final static String GOOGLE_DATA_BY_CODCONTATTO = "GOOGLE_DATA_BY_CODCONTATTO";
	public final static String GOOGLE_DATA_SAVE = "GOOGLE_DATA_SAVE";
	public final static String GOOGLE_DATA_UPDATE = "GOOGLE_DATA_UPDATE";
	public final static String GOOGLE_DATA_DELETE = "GOOGLE_DATA_DELETE";
	public final static String GOOGLE_DATA_LIST = "GOOGLE_DATA_LIST";
	public final static String UPDATE_GOOGLE_DATA_AGENTEUPDATE = "UPDATE_GOOGLE_DATA_AGENTE_UPDATE";
	
	public GDataDAO() {
		super();
	}
	
	public Object getGoogleDataById(String classType, Integer codGData){
		return super.getObjectById(classType, GOOGLE_DATA_BY_ID, codGData);		
	}

	public Object getGoogleDataByCodContatto(String classType, Integer codContatto){
		return super.getObjectById(classType, GOOGLE_DATA_BY_CODCONTATTO, codContatto);		
	}
	
	public boolean deleteGoogleDataByCodContatto(Integer codContatto,Connection con, boolean doCommit){
		return super.deleteObjectById(GOOGLE_DATA_DELETE, codContatto, con, doCommit);
	}
	
	public Object getGoogleDataList(String className){
		return super.list(className, GOOGLE_DATA_LIST);
	}

	public boolean saveUpdate(GDataVO gdVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)
						 ? ConnectionManager.getInstance().getConnection()
						 : connection;
		PreparedStatement ps = null;
		String query = ((gdVO.getCodGData() == null) || (gdVO.getCodGData() == 0))
						? getQuery(GOOGLE_DATA_SAVE)
						: getQuery(GOOGLE_DATA_UPDATE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			if (gdVO.getCodContatto() == null) {
				ps.setInt(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, gdVO.getCodContatto());
			}
			
			ps.setString(2, gdVO.getPwsKey());
			ps.setString(3, gdVO.getDescrizione());
			if ((gdVO.getCodGData() != null) &&
				(gdVO.getCodGData() != 0)){
				ps.setInt(4, gdVO.getCodGData());
			}
			ps.executeUpdate();
			if ((gdVO.getCodGData() == null) ||
				(gdVO.getCodGData() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					gdVO.setCodGData(key);
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

	public boolean updateGoogleDataAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_GOOGLE_DATA_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
