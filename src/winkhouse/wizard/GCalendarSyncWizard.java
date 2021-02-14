package winkhouse.wizard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import winkhouse.Activator;
import winkhouse.action.agenda.CercaAppuntamentiAction;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine.AgenteResult;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine.ContattiResult;
import winkhouse.model.AgentiModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.view.agenda.CalendarioView;
import winkhouse.view.agenda.ListaAppuntamentiView;
import winkhouse.wizard.gcalendar.GCalSyncOpeartions;
import winkhouse.wizard.gcalendar.GCalendarUploaderResult;
import winkhouse.wizard.gcalendar.SelezioneAgenti;



public class GCalendarSyncWizard extends Wizard {

	public final static String ID = "winkhouse.wizardgcalendarsync";
	
	private SelezioneAgenti selezioneAgenti = null;
	private GCalSyncOpeartions gCalSyncOperations = null;
	private GCalendarUploaderResult gCalendarUploaderResult = null; 	
	
	private GCalSyncVO gcalsyncVO = null;
	
	
	public class GCalSyncVO{
		
		public final static int UPLOAD_APPUNTAMENTI_MODEL = 0; 
		public final static int DOWNLOAD_GCALENDAR_EVENTS = 1;
		
		private ArrayList<AgentiModel> alagenti = null;
//		private HashMap<AgentiModel,ArrayList<CalendarListEntry>> hpselcal = null; 
		private HashMap<Integer,AgenteResult> alagentiResult = null;
		private ArrayList<ContattiResult> contattiAppuntamentiUPL = null;
		private int operarationType = UPLOAD_APPUNTAMENTI_MODEL; 
		
		private Date startDate = null;
		private Date endDate = null;
		
		private HashMap<String,GoogleCalendarSyncEngine.ItemUpload> al_item_upload = null; 
		private HashMap<String,GoogleCalendarSyncEngine.ItemDownload> al_item_download = null;
		
		private boolean upl_from_detail = false;
		private AppuntamentiModel from_appuntamento_detail = null;
		private ColloquiModel from_colloqui_detail = null;
		
		public GCalSyncVO(){
			startDate = new Date();
			endDate = new Date();
		}

//		public ArrayList<ContattiModel> getAlcontatti() {
//			if (alcontatti == null){
//				alcontatti = new ArrayList<ContattiModel>();
//			}
//			return alcontatti;
//		}
//
//		public void setAlcontatti(ArrayList<ContattiModel> alcontatti) {
//			this.alcontatti = alcontatti;
//		}
//
		public Date getStartDate() {
			return startDate;
		}

		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		public HashMap<Integer,AgenteResult> getAlagentiResult() {
			if (alagentiResult == null){
				alagentiResult = new HashMap<Integer,AgenteResult>();
			}
			return alagentiResult;
		}
		
		public void resetAlagentiResultValues(){
			Iterator<Map.Entry<Integer,AgenteResult>> it = getAlagentiResult().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, AgenteResult> entry = (Map.Entry<Integer, AgenteResult>) it.next();
				entry.getValue().setHmEvents(null);
			}
		}

		public void setAlagentiResult(HashMap<Integer,AgenteResult> alagentiResult) {
			this.alagentiResult = alagentiResult;
		}

		public ArrayList<ContattiResult> getContattiAppuntamentiUPL() {
			if (contattiAppuntamentiUPL == null){
				contattiAppuntamentiUPL = new ArrayList<GoogleCalendarSyncEngine.ContattiResult>();
			}
			return contattiAppuntamentiUPL;
		}

		public void setContattiAppuntamentiUPL(
				ArrayList<ContattiResult> contattiAppuntamentiUPL) {
			this.contattiAppuntamentiUPL = contattiAppuntamentiUPL;
		}

		public int getOperarationType() {
			return operarationType;
		}

		public void setOperarationType(int operarationType) {
			this.operarationType = operarationType;
		}
		
		public ArrayList<AgentiModel> getAlagenti() {
			
			if (alagenti == null){
				alagenti = new ArrayList<AgentiModel>();
			}
			
			return alagenti;
			
		}
		
		public void setAlagenti(ArrayList<AgentiModel> alagenti) {
			this.alagenti = alagenti;
		}

		public boolean isAlmost1CaledarSelected(){
			
			if (getAlagentiResult().entrySet().size() == 0){
				return false;
			}else{
				Iterator it = getAlagentiResult().entrySet().iterator();
				while(it.hasNext()){
					Map.Entry me = (Map.Entry)it.next();
					if (((AgenteResult)me.getValue()).getCleSelected().size() > 0){
						return true;
					}
				}
				
			}
			
			return false;
			
		}

		public Date getEndDate() {
			return endDate;
		}

		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		
		public HashMap<String,GoogleCalendarSyncEngine.ItemUpload> getHm_item_upload() {
			if (al_item_upload == null){
				al_item_upload = new HashMap<String,GoogleCalendarSyncEngine.ItemUpload>();
			}
			return al_item_upload;
		}
		
		public void setHm_item_upload(HashMap<String,GoogleCalendarSyncEngine.ItemUpload> al_item_upload) {
			this.al_item_upload = al_item_upload;
		}

		
		public HashMap<String,GoogleCalendarSyncEngine.ItemDownload> getAl_item_download() {
			if (al_item_download == null){
				al_item_download = new HashMap<String,GoogleCalendarSyncEngine.ItemDownload>();
			}
			return al_item_download;
		}

		
		public void setAl_item_download(HashMap<String,GoogleCalendarSyncEngine.ItemDownload> al_item_download) {
			this.al_item_download = al_item_download;
		}

		
		public boolean isUpl_from_detail() {
			return upl_from_detail;
		}

		public void setUpl_from_detail(boolean upl_from_detail) {
			this.upl_from_detail = upl_from_detail;
		}

		public AppuntamentiModel getFrom_appuntamento_detail() {
			return from_appuntamento_detail;
		}

		public void setFrom_appuntamento_detail(AppuntamentiModel from_appuntamento_detail) {
			this.from_appuntamento_detail = from_appuntamento_detail;
		}

		public ColloquiModel getFrom_colloqui_detail() {
			return from_colloqui_detail;
		}

		public void setFrom_colloqui_detail(ColloquiModel from_colloqui_detail) {
			this.from_colloqui_detail = from_colloqui_detail;
		}
				
	}
	
	public GCalendarSyncWizard() {
		
	}

	@Override
	public boolean performFinish() {
		if (getGcalsyncVO().getFrom_colloqui_detail() == null){
			CercaAppuntamentiAction caa2 = new CercaAppuntamentiAction(CalendarioView.class.getName());
			caa2.run();					
			caa2 = new CercaAppuntamentiAction(ListaAppuntamentiView.class.getName());
			caa2.run();
		}
		return true;
	}

	@Override
	public void addPages() {
		
		selezioneAgenti = new SelezioneAgenti("selezioneAgenti", 
											  "Selezione agenti", 
											  Activator.getImageDescriptor("icons/wizardgcalendar/agenti.png"));
		addPage(selezioneAgenti);
		
		gCalSyncOperations = new GCalSyncOpeartions("gCalSync", 
				  							  		"Sincronizzazione eventi agenda con google calendar", 
				  							  		Activator.getImageDescriptor("icons/wizardgcalendar/gcalsync.png"));
		addPage(gCalSyncOperations);
		
		gCalendarUploaderResult = new GCalendarUploaderResult("GCalUploadResult",
															  "Upload appuntamenti winkhouse verso Google Calendar",
															  Activator.getImageDescriptor("icons/wizardgcalendar/uploadevents.png"));
		addPage(gCalendarUploaderResult);

	}

	@Override
	public boolean canFinish() {
		
		return super.canFinish();
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		
		IWizardPage returnValue = null;
		
		if (page instanceof SelezioneAgenti){
			
			if (getGcalsyncVO().isUpl_from_detail()){
				if (getGcalsyncVO().isAlmost1CaledarSelected()){
					gCalSyncOperations.setSyncResult(getGcalsyncVO().getAlagentiResult());
				}else{
					gCalSyncOperations.getTree().setInput(null);
				}
				returnValue = gCalSyncOperations;
				
			}else{
				
				if (getGcalsyncVO().isAlmost1CaledarSelected()){					
					returnValue = gCalSyncOperations;					
				}
				
			}
			
		}
		
		if (page instanceof GCalSyncOpeartions){
			
			if (getGcalsyncVO().getOperarationType() == GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL && getGcalsyncVO().getHm_item_upload().size() > 0){
				returnValue = gCalendarUploaderResult;
				gCalendarUploaderResult.refreshControls();
				gCalendarUploaderResult.refreshLabels();
			}
			
			if (getGcalsyncVO().getOperarationType() == GCalSyncVO.DOWNLOAD_GCALENDAR_EVENTS && getGcalsyncVO().getAl_item_download().size() > 0){
				returnValue = gCalendarUploaderResult;
				gCalendarUploaderResult.refreshControls();
				gCalendarUploaderResult.refreshLabels();
			}
			
		}		
		
		return returnValue;
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		
		return super.getPreviousPage(page);
	}


	public GCalSyncVO getGcalsyncVO() {
		if (gcalsyncVO == null){
			gcalsyncVO = new GCalSyncVO();
		}
		return gcalsyncVO;
	}

	public void setGcalsyncVO(GCalSyncVO gcalsyncVO) {
		this.gcalsyncVO = gcalsyncVO;
	}

}
