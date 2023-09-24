package winkhouse.helper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import winkhouse.Activator;
import winkhouse.dao.AppuntamentiDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.dao.GCalendarDAO;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine.AgenteResult;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine.ContattiResult;
import winkhouse.model.AgentiAppuntamentiModel;
import winkhouse.model.AgentiModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.vo.AppuntamentiVO;
import winkhouse.vo.GCalendarVO;
import winkhouse.wizard.GCalendarSyncWizard;

import com.google.api.services.calendar.model.Events;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;


public class GCalendarHelper {

	public GCalendarHelper (){}
	
	
	public boolean saveUpdateGCalendarVO(ArrayList<GCalendarVO> gcalendar){
		
		boolean returnValue = true;
		if (gcalendar != null){
			GCalendarDAO gcDAO = new GCalendarDAO();
			Iterator it = gcalendar.iterator();
			while (it.hasNext()){
				GCalendarVO gcal = (GCalendarVO)it.next();
				if (!gcDAO.saveUpdate(gcal, null, true)){
					returnValue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio indirizzo calendario ", 
											"Si è verificato un errore nel salvataggio indirizzo calendario : " + 
											gcal.getPrivateUrl());
				}
			}
		}
		
		return returnValue;
	}
	
	public boolean deleteGCalendarData(GCalendarVO gcalVO, Connection con, boolean doCommit){
		boolean returnValue = true;
		
		GCalendarDAO gcDAO = new GCalendarDAO();
		returnValue = gcDAO.deleteByCodGData(gcalVO.getCodGData(), con, doCommit);
		
		return returnValue;
	}
	
	public ArrayList<CalendarEntry> getCalendarEntriesByAccount(){
		ArrayList<CalendarEntry> returnValue = null;
		return returnValue;
	} 
	
	public com.google.api.services.calendar.Calendar getCalendarService(AgentiModel agente) {
		
		GoogleCalendarV3Helper gcv3h = null;
		com.google.api.services.calendar.Calendar c = null;
		
		try {
			gcv3h = new GoogleCalendarV3Helper();
			c = gcv3h.getClient(agente.getCodAgente());
		} catch (GeneralSecurityException e) {
			return null;
		}catch (IOException e) {
			return null;
		}
				
	    return c;

	}
	
//	public Events readGCalendarEntry(com.google.api.services.calendar.model.Calendar calendar, Date startSync, Date endSync){
//		
//		try {
//			GoogleCalendarV3Helper gcv3h = new GoogleCalendarV3Helper();			
//			Events feeds = gcv3h.showEvents(calendar,startSync,endSync);
//			return feeds;
//		} catch (GeneralSecurityException e) {
//			return null;
//		} catch (IOException e) {
//			return null;		
//		}	
//		
//	}
	
	public ArrayList<ContattiResult> addAppuntamentiFromEvents(ArrayList<ContattiResult> contatti){
		
		Iterator<ContattiResult> it = contatti.iterator();
		AppuntamentiHelper ah = new AppuntamentiHelper();
		
		while(it.hasNext()){
			
			ContattiResult cr = it.next();			
			Iterator<CalendarEventEntry> itcee = cr.getCee().iterator();
			
			while (itcee.hasNext()) {
				
				CalendarEventEntry calendarEventEntry = (CalendarEventEntry) itcee.next();
				AppuntamentiVO aVO = new AppuntamentiVO();
				aVO.setDataInserimento(new Date());
				
				Date datainizio = null;
				try {
					datainizio = new Date(calendarEventEntry.getTimes().get(0).getStartTime().getValue());
				} catch (Exception e1) {
					datainizio = new Date();
				}
				
				Date datafine = null;
				try {
					datafine = new Date(calendarEventEntry.getTimes().get(0).getEndTime().getValue());
				} catch (Exception e1) {
					datafine = datainizio;
				}
				
				
				aVO.setDataAppuntamento(datainizio);
				aVO.setDataFineAppuntamento(datafine);
				aVO.setDescrizione(calendarEventEntry.getPlainTextContent());
				String luogo = null;
				try {
					luogo = calendarEventEntry.getLocations().get(0).getValueString();
				} catch (Exception e) {
					luogo = "";
				}
				
				aVO.setLuogo(luogo);
				aVO.setiCalUID(calendarEventEntry.getIcalUID());
				
				AppuntamentiModel am = new AppuntamentiModel(aVO);
				AgentiAppuntamentiModel aaM = new AgentiAppuntamentiModel();
//				aaM.setCodAgente(cr.getCodAgente());
				
				am.getAgenti().add(aaM);
				if (!ah.saveAppuntamento(am)){
					calendarEventEntry.setIcalUID(null);
				}
					
			}
			
		}
		
		return contatti;
		
	}
	
	public ArrayList<ContattiResult> addEventToGCalendar(ArrayList<ContattiResult> contatti){
	
		Iterator<ContattiResult> it = contatti.iterator();
		AppuntamentiDAO apDAO = new AppuntamentiDAO();
		ColloquiDAO cDAO = new ColloquiDAO();
		
		while(it.hasNext()){
		
//			ContattiResult cr = it.next();			
//			
//			CalendarService cs = null;
//			try {
//				cs = getCalendarService(cr.getContatto(), 
//								   						WinkhouseUtils.getInstance()
//								   									  .DecryptString(cr.getGMailAccountData().getPwsKey()));
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			} 
//			
//			if (cs != null){
//				
//				AppuntamentiModel[] arrapm = new AppuntamentiModel[cr.getAppuntamenti().size()]; 
//				arrapm = cr.getAppuntamenti().toArray(arrapm);
//				
//				 for (int i = 0; i < arrapm.length; i++) {
//									
//					AppuntamentiModel ap = arrapm[i];			
//					CalendarEventEntry myEvent = new CalendarEventEntry();
//		
//					myEvent.setTitle(new PlainTextConstruct((ap.getTipoAppuntamento()!=null)
//														    ? ap.getTipoAppuntamento().getDescrizione()
//														    : ap.getDataStrAppuntamento()));
//					myEvent.setContent(new PlainTextConstruct(ap.getDescrizione()));
//						
//					//Create DateTime events and create a When object to hold them, then add
//					//the When event to the event
//					DateTime startTime = new DateTime(ap.getDataAppuntamento(),Calendar.getInstance().getTimeZone());// parseDateTime("2011-11-14T15:00:00-08:00");
//					DateTime endTime = new DateTime(ap.getDataFineAppuntamento(),Calendar.getInstance().getTimeZone()); //DateTime.parseDateTime("2011-11-14T17:00:00-08:00");
//					When eventTimes = new When();
//					eventTimes.setStartTime(startTime);
//					eventTimes.setEndTime(endTime);
//					Where where = new Where("","",ap.getLuogo());					
//					myEvent.addLocation(where);
//					myEvent.addTime(eventTimes);		
//		
//					try {
//						CalendarEventEntry insertedEntry = cs.insert(cr.getFullPrivateUrl(), myEvent);
//						String icaluid = insertedEntry.getIcalUID();
//						if (icaluid != null){ 
//							ap.setiCalUID(icaluid);
//							apDAO.saveUpdate(ap, null, true);
//						}
//					} catch (IOException e) {				
//						e.printStackTrace();
//					} catch (ServiceException e) {
//						e.printStackTrace();
//					}
//				 }
//				 
//				 ColloquiModel[] arrcm = new ColloquiModel[cr.getColloqui().size()]; 
//				 arrcm = cr.getColloqui().toArray(arrcm);
//				
//				 for (int i = 0; i < arrcm.length; i++) {
//									
//					ColloquiModel cm = arrcm[i];			
//					CalendarEventEntry myEvent = new CalendarEventEntry();
//		
//					myEvent.setTitle(new PlainTextConstruct(cm.toString()));
//					myEvent.setContent(new PlainTextConstruct(cm.getDescrizione()));
//						
//					//Create DateTime events and create a When object to hold them, then add
//					//the When event to the event
//					DateTime startTime = new DateTime(cm.getDataColloquio(),Calendar.getInstance().getTimeZone());// parseDateTime("2011-11-14T15:00:00-08:00");
//					DateTime endTime = new DateTime(cm.getDataColloquio(),Calendar.getInstance().getTimeZone()); //DateTime.parseDateTime("2011-11-14T17:00:00-08:00");
//					When eventTimes = new When();
//					eventTimes.setStartTime(startTime);
//					eventTimes.setEndTime(endTime);
//					Where where = new Where("","",cm.getLuogoIncontro() + " " + 
//											((cm.getImmobileAbbinato() != null)
//										     ? " Indirizzo : " + cm.getImmobileAbbinato().getIndirizzo()
//										     : ""));					
//					myEvent.addLocation(where);
//					myEvent.addTime(eventTimes);		
//		
//					try {
//						CalendarEventEntry insertedEntry = cs.insert(cr.getFullPrivateUrl(), myEvent);
//						String icaluid = insertedEntry.getIcalUID();
//						if (icaluid != null){ 
//							cm.setiCalUid(icaluid);
//							cDAO.saveUpdate(cm, null, true);
//						}
//					} catch (IOException e) {				
//						e.printStackTrace();
//					} catch (ServiceException e) {
//						e.printStackTrace();
//					}
//				 }
//				 
//				 
//			}			
		}
		
		return contatti;
		
	}
	
	public void synchAppuntamentiToGCalendar(int type, Date startSync, Date endSync, Shell s, ArrayList<AgenteResult> returnValue){
		
		GoogleCalendarSyncEngine gcse = new GoogleCalendarSyncEngine(type,																	  
																	 startSync,
																	 endSync,
																	 returnValue);
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(s);
		try {
			pmd.run(false, true, gcse);
		} catch (InvocationTargetException e) {
			MessageBox mb = new MessageBox(winkhouse.Activator
													  .getDefault()
													  .getWorkbench()
													  .getActiveWorkbenchWindow()
													  .getShell(),
													  SWT.ICON_ERROR);
			mb.setText("Si è verificato un errore nella sincronizzazione");
			mb.setMessage("Si è verificato un errore nella sincronizzazione degli appuntamenti di winkhouse \n" +
						  "Impossibile determinare gli appuntamenti di winkhouse da pubblicare su Google calendar");			
			mb.open();
		} catch (InterruptedException e) {
			MessageBox mb = new MessageBox(winkhouse.Activator
					  								  .getDefault()
					  								  .getWorkbench()
													  .getActiveWorkbenchWindow()
													  .getShell(),
													  SWT.ICON_ERROR);
			mb.setText("Si è verificato un errore nella sincronizzazione");
			mb.setMessage("Si è verificato un errore nella sincronizzazione degli appuntamenti di winkhouse \n" +
						  "Impossibile determinare gli appuntamenti di winkhouse da pubblicare su Google calendar");			
			mb.open();
		}
		
	}
	
	public void synchGCalendarToAppuntamenti(Date startSync, Date endSync, Shell s, ArrayList<AgenteResult> returnValue){
		
		GoogleCalendarSyncEngine gcse = new GoogleCalendarSyncEngine(GCalendarSyncWizard.GCalSyncVO.DOWNLOAD_GCALENDAR_EVENTS,																	 
																	 startSync,
																	 endSync,
																	 returnValue);
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(s);
		try {
			pmd.run(false, true, gcse);
		} catch (InvocationTargetException e) {
			MessageBox mb = new MessageBox(winkhouse.Activator
													  .getDefault()
													  .getWorkbench()
													  .getActiveWorkbenchWindow()
													  .getShell(),
													  SWT.ICON_ERROR);
			
			mb.setText("Si è verificato un errore nella sincronizzazione");
			mb.setMessage("Si è verificato un errore nella sincronizzazione degli appuntamenti di winkhouse \n" +
						  "Impossibile determinare gli appuntamenti di winkhouse da pubblicare su Google calendar");			
			mb.open();
		} catch (InterruptedException e) {
			MessageBox mb = new MessageBox(winkhouse.Activator
					  								  .getDefault()
					  								  .getWorkbench()
													  .getActiveWorkbenchWindow()
													  .getShell(),
													  SWT.ICON_ERROR);
			mb.setText("Si è verificato un errore nella sincronizzazione");
			mb.setMessage("Si è verificato un errore nella sincronizzazione degli appuntamenti di winkhouse \n" +
						  "Impossibile determinare gli appuntamenti di winkhouse da pubblicare su Google calendar");			
			mb.open();
		}
		
	}
	
	/*
        for (int i = 0; i < myResultsFeed.getEntries().size(); i++) {
        	CalendarEventEntry  entry = myResultsFeed.getEntries().get(i);
        	System.out.println(entry.getPublished() + " - " + entry.getTitle().getPlainText() + " - " +
        					   entry.getPlainTextContent() + " - " + entry.getAuthors().get(0).getEmail() + " - " + entry.getAuthors().get(0).getName());
        	System.out.println(entry.getIcalUID());
        	System.out.println("Start: " + entry.getTimes().get(0).getStartTime().toStringRfc822() + "End: " + entry.getTimes().get(0).getEndTime().toStringRfc822());
        	System.out.println(entry.getRecurrence().getValue() + " - " + entry.getTextContent().getLang());
            
          }
               
		
	}
*/ 
}
