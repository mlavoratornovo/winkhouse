package winkhouse.model;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.widgets.Display;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

import winkhouse.dao.ContattiDAO;
import winkhouse.dao.PermessiDAO;
import winkhouse.dao.PermessiUIDAO;
import winkhouse.helper.GoogleCalendarV3Helper;
import winkhouse.vo.AgentiVO;


public class AgentiModel extends AgentiVO {

	private ArrayList contatti = null;
	private ArrayList<PermessiUIModel> permessiPerspectiveUIModels = null;
	private ArrayList<PermessiUIModel> permessiViewUIModels = null;
	private ArrayList<PermessiUIModel> permessiDialogUIModels = null;
	private ArrayList<PermessiModel> permessiModels = null;
	private CalendarList cle = null;
	private Object[] cleobjarr = null;

	public AgentiModel() {
	}

	public AgentiModel(AgentiVO agentiVO) {
		setCap(agentiVO.getCap());
		setCitta(agentiVO.getCitta());
		setCodAgente(agentiVO.getCodAgente());
		setCognome(agentiVO.getCognome());
		setIndirizzo(agentiVO.getIndirizzo());
		setNome(agentiVO.getNome());
		setProvincia(agentiVO.getProvincia());
	}

	public AgentiModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public ArrayList getContatti() {
		if (contatti == null){
			if (getCodAgente() != null && getCodAgente() != 0){
				ContattiDAO cDAO = new ContattiDAO();			
				contatti = cDAO.listByAgente(ContattiModel.class.getName(), super.getCodAgente());
			}else{
				contatti = new ArrayList();
			}
		}
		return contatti;
	}

	public void setContatti(ArrayList contatti) {
		this.contatti = contatti;
	}
	
	public ArrayList getGMailContatti(){
		
		ArrayList<ContattiModel> al = new ArrayList<ContattiModel>();
		Iterator<ContattiModel> it = getContatti().iterator();
		
		while(it.hasNext()){
			ContattiModel cm = it.next();
			if (cm.isGMailAccount()){
				al.add(cm);
			}
		}
		
		return al;
	}

	public ArrayList<PermessiUIModel> getPermessiUIPerspectiveModels(){
		
		if (permessiPerspectiveUIModels == null){
			if (getCodAgente() != null && getCodAgente() != 0){
				permessiPerspectiveUIModels = new PermessiUIDAO().getPermessiPerspectiveByAgente(PermessiUIModel.class.getName(), this.getCodAgente());
			}
			
		}
		
		return permessiPerspectiveUIModels;
		
	}

	public ArrayList<PermessiUIModel> getPermessiUIViewModels(){
		
		if (permessiViewUIModels == null){
			if (getCodAgente() != null && getCodAgente() != 0){
				permessiViewUIModels = new PermessiUIDAO().getPermessiViewByAgente(PermessiUIModel.class.getName(), this.getCodAgente());
			}
			
		}
		
		return permessiViewUIModels;
		
	}

	public ArrayList<PermessiUIModel> getPermessiUIDialogModels(){
		
		if (permessiDialogUIModels == null){
			if (getCodAgente() != null && getCodAgente() != 0){
				permessiDialogUIModels = new PermessiUIDAO().getPermessiDialogByAgente(PermessiUIModel.class.getName(), this.getCodAgente());
			}
		}
		
		return permessiDialogUIModels;
		
	}

	public ArrayList<PermessiModel> getPermessiModels(){
		
		if (permessiModels == null){
			if (getCodAgente() != null && getCodAgente() != 0){
				permessiModels = new PermessiDAO().getPermessiByAgente(PermessiModel.class.getName(), this.getCodAgente());
			}
			
		}
		
		return permessiModels;
		
	}
	
	public void resetPermessi(){
		permessiModels = null;
		permessiDialogUIModels = null;
		permessiPerspectiveUIModels = null;
		permessiViewUIModels = null;
	}

	
	public ArrayList<PermessiUIModel> getPermessiPerspectiveUIModels() {
		return permessiPerspectiveUIModels;
	}

	
	public void setPermessiPerspectiveUIModels(
			ArrayList<PermessiUIModel> permessiPerspectiveUIModels) {
		this.permessiPerspectiveUIModels = permessiPerspectiveUIModels;
	}

	
	public ArrayList<PermessiUIModel> getPermessiViewUIModels() {
		return permessiViewUIModels;
	}

	
	public void setPermessiViewUIModels(
			ArrayList<PermessiUIModel> permessiViewUIModels) {
		this.permessiViewUIModels = permessiViewUIModels;
	}

	
	public ArrayList<PermessiUIModel> getPermessiDialogUIModels() {
		return permessiDialogUIModels;
	}

	
	public void setPermessiDialogUIModels(
			ArrayList<PermessiUIModel> permessiDialogUIModels) {
		this.permessiDialogUIModels = permessiDialogUIModels;
	}

	
	public void setPermessiModels(ArrayList<PermessiModel> permessiModels) {
		this.permessiModels = permessiModels;
	}

	
	public CalendarList getCle() {
		
		if (cle == null) {
			
			try {
				GoogleCalendarV3Helper gcv3h = new GoogleCalendarV3Helper();
				Calendar client = gcv3h.getClient(getCodAgente());
				if (client != null){
					cle = gcv3h.showCalendars(client);
				}
			} catch (GeneralSecurityException e) {
				return null;
			} catch (IOException e) {
				return null;
			}			
		}
		
		return cle;
	}
	
	public Object[] getCleObjs() {
		
		if (cleobjarr == null && getCle() != null && getCle().getItems() != null) {
			
			ArrayList returnValue = new ArrayList();
			  
		    for (CalendarListEntry entry : getCle().getItems()) {
		    	returnValue.add(entry);
		    }
		    cleobjarr = returnValue.toArray();
		}
		return cleobjarr;
	}
	

	public void setCle(CalendarList cle) {
		this.cle = cle;
	}
	
}
