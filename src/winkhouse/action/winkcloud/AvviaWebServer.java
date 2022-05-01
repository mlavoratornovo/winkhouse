package winkhouse.action.winkcloud;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.MonitorHTTPModel;
import winkhouse.model.winkcloud.jobs.HTTPJob;

public class AvviaWebServer extends Action {
	
	private HTTPJob job = null;
	private MonitorHTTPModel monitor = null;	
	
	public AvviaWebServer() {
		// TODO Auto-generated constructor stub
	}

	public AvviaWebServer(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public AvviaWebServer(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public AvviaWebServer(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		if (this.isChecked()){
			this.job.schedule();
			setToolTipText("Arresta server");
		}else{
			this.job.stop();
			setToolTipText("Avvia server");
		}
				
	}

	public HTTPJob getJob() {
		return job;
	}

	public void setJob(HTTPJob job) {
		this.job = job;
	}

	public MonitorHTTPModel getMonitor() {
		return monitor;
	}

	public void setMonitor(MonitorHTTPModel monitor) {
		this.monitor = monitor;
	}
	
	

}
