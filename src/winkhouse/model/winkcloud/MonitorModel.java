package winkhouse.model.winkcloud;

import java.util.ArrayList;
import winkhouse.view.winkcloud.QueryFilesView;

public interface MonitorModel {
	
	public void start();
	
	public void setCloudQueries(ArrayList<CloudQueryModel> cloudQueries);
	
	public ArrayList<CloudQueryModel> getCloudQueries();
	
	public CloudMonitorState getStato();
	
	public void setStato(CloudMonitorState stato);
	
	public boolean isChkvar();
	
	public void stop();
	
	public long getPollingIntervall();
	
	public void setPollingIntervall(long pollingIntervall);
	
	public ConnectorTypes getType();
	
	public boolean save(String pathFileName);

	public void setQueryFilesView(QueryFilesView qfv);
	
}
