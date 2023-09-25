	package winkhouse.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.AttributeDAO;
import winkhouse.dao.AttributeValueDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.model.AttributeModel;
import winkhouse.model.AttributeValueModel;
import winkhouse.model.EntityModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AffittiVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ImmobiliVO;

public class EntityHelper {

	public EntityHelper() {
		
	}
	
	public Boolean updateEntitiesAttributes(ArrayList<EntityModel> entities){

		Boolean returnValue = true;

		AttributeDAO aDAO = new AttributeDAO();
		Connection  con = ConnectionManager.getInstance().getConnection();
		
		for (EntityModel entityModel : entities) {
			
			ArrayList<AttributeModel> attributesDB = aDAO.getAttributeByIdEntity(entityModel.getIdClassEntity());			
			
			HashMap<Integer,AttributeModel> attributeDBMap = new HashMap<Integer,AttributeModel>();
			HashMap<Integer,AttributeModel> attributeDBMap_finder = new HashMap<Integer,AttributeModel>();
			
			for (AttributeModel attributeModel : attributesDB) {
				attributeDBMap.put(attributeModel.getIdAttribute(),attributeModel);
				attributeDBMap_finder.put(attributeModel.getIdAttribute(),attributeModel);
			}
			for (AttributeModel attributeModel : entityModel.getAttributes()){

				if (entityModel.getClassName().equalsIgnoreCase(ImmobiliVO.class.getName())){
					try {
						ImmobiliVO.class.getDeclaredField(attributeModel.getAttributeName());
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												"ATTENZIONE !",
												attributeModel.getAttributeName() + " : Nome di campo non utilizzabile dall'utente, \n cambiare il nome");

					} catch (SecurityException e) {
						returnValue = true;
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						returnValue = true;
						
					}
				}
				if (entityModel.getClassName().equalsIgnoreCase(AnagraficheVO.class.getName())){
					try {
						AnagraficheVO.class.getDeclaredField(attributeModel.getAttributeName());
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												"ATTENZIONE !",
												attributeModel.getAttributeName() + " : Nome di campo non utilizzabile dall'utente, \n cambiare il nome");

					} catch (SecurityException e) {
						returnValue = true;
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						returnValue = true;
						
					}
				}
				if (entityModel.getClassName().equalsIgnoreCase(ColloquiVO.class.getName())){
					try {
						ColloquiVO.class.getDeclaredField(attributeModel.getAttributeName());
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												"ATTENZIONE !",
												attributeModel.getAttributeName() + " : Nome di campo non utilizzabile dall'utente, \n cambiare il nome");

					} catch (SecurityException e) {
						returnValue = true;
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						returnValue = true;
						
					}
				}
				if (entityModel.getClassName().equalsIgnoreCase(AffittiVO.class.getName())){
					try {
						AffittiVO.class.getDeclaredField(attributeModel.getAttributeName());
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												"ATTENZIONE !",
												attributeModel.getAttributeName() + " : Nome di campo non utilizzabile dall'utente, \n cambiare il nome");

					} catch (SecurityException e) {
						returnValue = true;
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						returnValue = true;
						
					}
				}

				AttributeModel amchkdb = attributeDBMap.get(attributeModel.getIdAttribute());
				
				if (amchkdb == null){
					
					if ((attributeModel.getAttributeName() == null) ||
						(attributeModel.getAttributeName().equalsIgnoreCase(""))){
						
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												"ATTENZIONE !",
												entityModel.getDescription() + " : Nome campo mancante");
						returnValue = false;
						break;
					}else{
						if ((attributeModel.getFieldType() == null) ||
							(attributeModel.getFieldType().equalsIgnoreCase(""))){
							
							MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
													"ATTENZIONE !",
													entityModel.getDescription() + ", campo " + 
													attributeModel.getAttributeName() + " : Tipo campo mancante");						
							returnValue = false;
							break;
						}else{
							if (checkAttributeNameExist(attributeDBMap_finder, attributeModel.getAttributeName())){
								MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
														"ATTENZIONE !",
														entityModel.getDescription() + " : Il nome campo � gia presente in archivio");
								returnValue = false;
								break;

							}else{
								
								if (attributeModel.getFieldType().equalsIgnoreCase(Enum.class.getName()) && attributeModel.getAlEnums().size() == 0){
									MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
											"ATTENZIONE !",
											entityModel.getDescription() + " : Mancano i valori fissi");
									returnValue = false;
									break;
									
								}else{
									returnValue = aDAO.saveUpdate(attributeModel, con, false);
								}
								
							}
							if (!returnValue){
								break;
							}
						}
					}
				}else{
					if (amchkdb.getIdAttribute() == attributeModel.getIdAttribute()){
											
						if ((attributeModel.getAttributeName() == null) ||
							(attributeModel.getAttributeName().equalsIgnoreCase(""))){
								
							MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
													"ATTENZIONE !",
													entityModel.getDescription() + " : Nome campo mancante");
							returnValue = false;
							break;
						}else{
							if ((attributeModel.getFieldType() == null) ||
								(attributeModel.getFieldType().equalsIgnoreCase(""))){
								
								MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
														"ATTENZIONE !",
														entityModel.getDescription() + ", campo " + 
														attributeModel.getAttributeName() + " : Tipo campo mancante");						
								returnValue = false;
								break;
							}else{
								returnValue = aDAO.saveUpdate(attributeModel, con, false);
								if (!returnValue){
									break;
								}else{
									attributeDBMap.remove(attributeModel.getIdAttribute());					
								}
							}
								
						}
						
					}else{
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												"ATTENZIONE !",
												entityModel.getDescription() + " : Il nome campo � gia presente in archivio");
						returnValue = false;
						break;
					}
				}
				
			}
			
			if (!returnValue){
				break;
			}
			
			Iterator<Map.Entry<Integer,AttributeModel>> it = attributeDBMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, AttributeModel> entry = (Map.Entry<Integer, AttributeModel>) it.next();
				returnValue = deleteAttribute(entry.getValue(), con, false);
					
				if (!returnValue){
					break;
				}
				
			}
			
			if (!returnValue){
				break;
			}else{
				try {
					con.commit();					
				} catch (SQLException e) {
					e.printStackTrace();
					returnValue = false;
					break;
				}

			}

						
		}
		
		if (!returnValue){
			try {
				con.rollback();					
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
		}
		
		WinkhouseUtils.getInstance().resetObjectSearchGetters_Personal();

		return returnValue;
		
	}
	
	protected boolean checkAttributeNameExist(HashMap<Integer,AttributeModel> attributeMap, String attributeName){
		
		boolean returnValue = false;
		
		Iterator<AttributeModel> it = attributeMap.values().iterator();
		
		while(it.hasNext()){
			
			if (it.next().getAttributeName().equalsIgnoreCase(attributeName)){
				returnValue = true;
				break;
			}
			
		}
		
		return returnValue;
	}
	
	public boolean deleteAttribute(AttributeModel am, 
								   Connection con,
								   Boolean doCommit){
		
		
		boolean returnValue = true;
		
		AttributeValueDAO avDAO = new AttributeValueDAO();
		AttributeDAO aDAO = new AttributeDAO();
		
		ArrayList al = avDAO.getAttributeValuesByIdAttribute(AttributeValueModel.class.getName(), am.getIdAttribute(), con);
		
		if (al.size() > 0){
			
			if (MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
									  	 "Attenzione, rilevati valori",
									  	 "Sono stati rilevati " + al.size() + " valori collegati al campo " + am.getAttributeName() + ".\n" + 
									  	 "Continuando i valori andranno persi, procedo ?")){
				
				if (avDAO.deleteByAttributeId(am.getIdAttribute(), con, false)){
					
					if (aDAO.delete(am.getIdAttribute(), con, false)){
						
						if (doCommit){
							try {
								con.commit();								
							} catch (SQLException e) {
								e.printStackTrace();
								returnValue = false;
							}
						}
						
					}else{
						if (doCommit){
							try {
								con.rollback();
							} catch (SQLException e) {
								e.printStackTrace();								
							}
							returnValue = false;
						}
						
					}
					
				}else{
					if (doCommit){
						try {
							con.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						returnValue = false;
					}
				}
				
			}			
		}else{
			if (avDAO.deleteByAttributeId(am.getIdAttribute(), con, false)){
				
				if (aDAO.delete(am.getIdAttribute(), con, false)){
					
					if (doCommit){
						try {
							con.commit();								
						} catch (SQLException e) {
							e.printStackTrace();
							returnValue = false;
						}
					}
					
				}else{
					if (doCommit){
						try {
							con.rollback();
						} catch (SQLException e) {
							e.printStackTrace();								
						}
						returnValue = false;
					}
					
				}
				
			}else{
				if (doCommit){
					try {
						con.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					returnValue = false;
				}
			}
			
		}

		
		return returnValue;
		
	} 

	public boolean saveAttributeValue(AttributeValueModel avModel){
		
		AttributeValueDAO avDAO = new AttributeValueDAO();
		return avDAO.saveUpdate(avModel, null, true);
		
	}
}