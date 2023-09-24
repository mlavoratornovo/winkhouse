package winkhouse.action.agenda;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.Activator;
import winkhouse.helper.GCalendarHelper;
import winkhouse.model.AgentiModel;
import winkhouse.util.WinkhouseUtils;

import com.google.gdata.client.Query.CustomParameter;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;



public class GoogleCalendarAction extends Action {

	public GoogleCalendarAction() {

	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/google_calendar.png");
	}

	@Override
	public String getText() {
		return "Sincronizzazione Google Calendar";
	}

	@Override
	public void run() {
			
//		calendarList();
		//addEvent();
		//read();

	}
	
	private void addEvent(){
		
		CalendarService myService = new CalendarService("exampleCo-exampleApp-1.0");
        try {
			myService.setUserCredentials("ideagratis@gmail.com", "2wsx4rfv");
		} catch (AuthenticationException e) {
			
			e.printStackTrace();
		}

		URL postURL = null;
		try {
			postURL = new URL("http://www.google.com/calendar/feeds/tester/private/full");
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		CalendarEventEntry myEvent = new CalendarEventEntry();

		//Set the title and description
		myEvent.setTitle(new PlainTextConstruct("Pi Day Party"));
		myEvent.setContent(new PlainTextConstruct("I am throwing a Pi Day Party!"));

		//Create DateTime events and create a When object to hold them, then add
		//the When event to the event
		DateTime startTime = DateTime.parseDateTime("2011-11-14T15:00:00-08:00");
		DateTime endTime = DateTime.parseDateTime("2011-11-14T17:00:00-08:00");
		When eventTimes = new When();
		eventTimes.setStartTime(startTime);
//		eventTimes.setEndTime(endTime);
		myEvent.addTime(eventTimes);		

		// POST the request and receive the response:
		try {
			CalendarEventEntry insertedEntry = myService.insert(postURL, myEvent);
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ServiceException e) {
			
			e.printStackTrace();
		}

	}
	
	private void calendarList(){
		CalendarService myService = new CalendarService("exampleCo-exampleApp-1.0");
        try {
			myService.setUserCredentials("ideagratis@gmail.com", "2wsx4rfv");
		} catch (AuthenticationException e) {
			
			e.printStackTrace();
		}

        URL feedUrl = null;
		try {
			feedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
        CalendarFeed resultFeed = null;
		try {
			resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ServiceException e) {
			
			e.printStackTrace();
		}

        System.out.println("Your calendars:");
        System.out.println();

        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
          CalendarEntry entry = resultFeed.getEntries().get(i);
          
          System.out.println("\t" + entry.getTitle().getPlainText());
          System.out.println("\t" + entry.getId());
        }
	}

	public List<CalendarEventEntry> read(AgentiModel agente, Date startSync){
		
		/*
		CalendarService myService = new CalendarService("exampleCo-exampleApp-1.0");
        try {
			myService.setUserCredentials("ideagratis@gmail.com", "2wsx4rfv");
		} catch (AuthenticationException e) {
			
			e.printStackTrace();
		}
		*/
		GCalendarHelper gch = new GCalendarHelper();
		com.google.api.services.calendar.Calendar myService = null;
//		try {
//			myService = gch.getCalendarService(agente);
//		} catch (IOException e1) {
//			
//			e1.printStackTrace();
//		}
		
		URL postURL = null;
//		try {
		//	postURL = cm.getFullPrivateUrl();
			//postURL = new URL("http://www.google.com/calendar/feeds/ideagratis@gmail.com/private/full");
/*		} catch (MalformedURLException e) {			
			e.printStackTrace();
		}
	*/	
		
		//Create a new query object and set the parameters
		CalendarQuery myQuery = new CalendarQuery(postURL);
		Calendar c = Calendar.getInstance();
		c.setTime(startSync);

		myQuery.setMinimumStartTime(new DateTime(c.getTime(),Calendar.getInstance().getTimeZone()));
 		//myQuery.setAuthor("ideagratis@gmail.com");
 		myQuery.addCustomParameter(new CustomParameter("futureevents","false"));
 		myQuery.addCustomParameter(new CustomParameter("singleevents","true"));
 	
		//Send the request with the built query URL
		CalendarEventFeed myResultsFeed = null;
//		try {
//			myResultsFeed = myService.query(myQuery, CalendarEventFeed.class);
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			
//			e.printStackTrace();
//		}
		return myResultsFeed.getEntries();
		//Take the first match and print the title
/*        for (int i = 0; i < myResultsFeed.getEntries().size(); i++) {
        	CalendarEventEntry  entry = myResultsFeed.getEntries().get(i);
        	System.out.println(entry.getPublished() + " - " + entry.getTitle().getPlainText() + " - " +
        					   entry.getPlainTextContent() + " - " + entry.getAuthors().get(0).getEmail() + " - " + entry.getAuthors().get(0).getName());
        	System.out.println(entry.getIcalUID());
        	System.out.println("Start: " + entry.getTimes().get(0).getStartTime().toStringRfc822() + "End: " + entry.getTimes().get(0).getEndTime().toStringRfc822());*/
//        	System.out.println(entry.getRecurrence().getValue() + " - " + entry.getTextContent().getLang());
            
          //}
                
		
	}

}
