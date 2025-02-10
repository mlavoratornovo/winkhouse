package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Property;
import org.apache.cayenne.query.ObjectSelect;

import winkhouse.db.ConnectionManager;
import winkhouse.db.orm.CayenneContextManager;
import winkhouse.model.ClassiClientiModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Classicliente;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.ClassiClientiVO;


public class ClassiClientiDAO extends BaseDAO{

	public ClassiClientiDAO() {
	}
	
	public final static String LISTA_CLASSICLIENTI = "LISTA_CLASSI_CLIENTI";
	public final static String LISTA_CLASSICLIENTI_COMUNE = "LISTA_CLASSICLIENTI_COMUNE";
	public final static String CLASSICLIENTI_BY_ID = "CLASSI_CLIENTI_BY_ID";
	public final static String CLASSICLIENTI_BY_NAME = "CLASSI_CLIENTI_BY_NAME";
	public final static String INSERT_CLASSICLIENTI = "INSERT_CLASSI_CLIENTI";
	public final static String UPDATE_CLASSICLIENTI = "UPDATE_CLASSI_CLIENTI";
	public final static String DELETE_CLASSICLIENTI = "DELETE_CLASSI_CLIENTI";
	public final static String UPDATE_CLASSICLIENTI_AGENTEUPDATE = "UPDATE_CLASSICLIENTI_AGENTE_UPDATE";
	
	public ArrayList<Classicliente> list(ObjectContext context){
		context = (context != null)?context:WinkhouseUtils.getInstance().getNewCayenneObjectContext();
		Property<String> propertyName = Property.create("ordine", String.class);
		return new ArrayList<Classicliente>(ObjectSelect.query(Classicliente.class).orderBy(propertyName.asc()).select(context));		
	}

	public ArrayList<ClassiClientiModel> listByComune(String comune){
		
		ArrayList<ClassiClientiModel> returnValue = new ArrayList<ClassiClientiModel>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LISTA_CLASSICLIENTI_COMUNE);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				ps.setString(1, comune);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(new ClassiClientiModel(rs,comune));
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
	
	public boolean saveUpdate(ClassiClientiVO clVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((clVO.getCodClasseCliente() == null) || (clVO.getCodClasseCliente() == 0))
						? getQuery(INSERT_CLASSICLIENTI)
						:getQuery(UPDATE_CLASSICLIENTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);	
			ps.setString(1, clVO.getDescrizione());
			ps.setInt(2, clVO.getOrdine());
			if ((clVO.getCodClasseCliente() != null) && 
			    (clVO.getCodClasseCliente() != 0)){
				ps.setInt(3, clVO.getCodClasseCliente());
			}
			ps.executeUpdate();
			if ((clVO.getCodClasseCliente() == null) || 
				(clVO.getCodClasseCliente() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					clVO.setCodClasseCliente(key);
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

	public boolean saveUpdateWithException(ClassiClientiVO clVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((clVO.getCodClasseCliente() == null) || (clVO.getCodClasseCliente() == 0))
						? getQuery(INSERT_CLASSICLIENTI)
						:getQuery(UPDATE_CLASSICLIENTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);	
			ps.setString(1, clVO.getDescrizione());
			ps.setInt(2, clVO.getOrdine());
			if ((clVO.getCodClasseCliente() != null) && 
			    (clVO.getCodClasseCliente() != 0)){
				ps.setInt(3, clVO.getCodClasseCliente());
			}
			ps.executeUpdate();
			if ((clVO.getCodClasseCliente() == null) || 
				(clVO.getCodClasseCliente() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					clVO.setCodClasseCliente(key);
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
	
	public boolean delete(Integer codClasseClienti, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_CLASSICLIENTI, codClasseClienti, con, doCommit);
	}
	
	public Classicliente getClasseClienteById(Integer idClasseClienti){
		ObjectContext context = WinkhouseUtils.getInstance().getCayenneObjectContext();
		return Cayenne.objectForPK(context,Classicliente.class,idClasseClienti);
	}
	public Object getClasseClienteById(String classType, Integer idClasseClienti){
		return super.getObjectById(classType, CLASSICLIENTI_BY_ID, idClasseClienti);
	}	

	public <T> ArrayList<T> getClasseClienteByDescrizione(String classType, String descrizione){
		return super.getObjectsByStringFieldValue(classType, CLASSICLIENTI_BY_NAME, descrizione);
	}	
	
	public boolean updateClasseClienteAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_CLASSICLIENTI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}