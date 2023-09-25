package winkhouse.helper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.GDataDAO;
import winkhouse.model.GDataModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.GCalendarVO;
import winkhouse.vo.GDataVO;

public class GDataHelper {

	public GDataHelper (){}
	
	
//	public ArrayList cleanCriptedStrings(){
//		GDataDAO gdDAO = new GDataDAO();
//		ArrayList al = (ArrayList)gdDAO.getGoogleDataList(GDataModel.class.getName());
//		Iterator it = al.iterator();
//		while (it.hasNext()) {
//			GDataModel gdata = (GDataModel) it.next();
//			gdata.setPwsKey(WinkhouseUtils.getInstance()
//										  .DecryptString(gdata.getPwsKey()));
//			
//		}
//		
//		return al;
		
//	}
	
//	public void decriptAll(ArrayList<GDataVO> al){
//		Iterator<GDataVO> itgd = al.iterator();
//		while (itgd.hasNext()) {
//			GDataVO gDataVO = (GDataVO) itgd.next();
//			saveUpdateGDataVO(gDataVO);			
//		}
//	}
	
//	public boolean saveUpdateGDataVO(GDataVO gdata){
//		
//		boolean returnValue = true;
//		gdata.setPwsKey(WinkhouseUtils.getInstance()
//									  .EncryptString(gdata.getPwsKey()));
//		GDataDAO gdDAO = new GDataDAO();
//		if (!gdDAO.saveUpdate(gdata, null, true)){
//			returnValue = false;
//		}
//		
//		return returnValue;
//	}
	
	public boolean deleteGDataByContatto(Integer codContatto, Connection con, Boolean doCommit){
		
		boolean returnValue = true;
		
		GCalendarHelper gcH = new GCalendarHelper();
		GDataDAO gdDAO = new GDataDAO();
		GDataModel gdM = (GDataModel)gdDAO.getGoogleDataByCodContatto(GDataModel.class.getName(), codContatto);
		
		if (gdDAO.deleteGoogleDataByCodContatto(codContatto, con, doCommit)){			
			if (gdM.getCalendarSettings() != null){
				Iterator it = gdM.getCalendarSettings().iterator();
				while(it.hasNext()){
					GCalendarVO gcVO = (GCalendarVO)it.next();
					returnValue = gcH.deleteGCalendarData(gcVO, con, doCommit);
				}
			}
		
		}else{
			returnValue = false;
		}
		
		return returnValue;
		
	}

}