package winkhouse.db.server;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.hsqldb.lib.tar.DbBackupMain;
import org.hsqldb.lib.tar.TarMalformatException;

import winkhouse.Activator;
import winkhouse.db.ConnectionManager;

public class BackUpHelper {

	public BackUpHelper() {}

	class executeBackUp implements Runnable{

		@Override
		public void run() {
			
			executeBackUp();

		}
		
	}
	
//	public ScheduledFuture startScheduledJob(long daysdelay, long dayperiod){
//		
//		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);		
//		
//		if (daysdelay == 0){
//			daysdelay = getDelayInit();
//		}
//		if (dayperiod == 0){
//			dayperiod = getDelayInit();
//		}
//		if (WinkhouseDBAgentUtils.getInstance().getBackupThread() != null){
//			WinkhouseDBAgentUtils.getInstance().getBackupThread().cancel(false);
//		}
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//	    ScheduledFuture<?> backupHandle = scheduler.scheduleAtFixedRate(new executeBackUp(), daysdelay, dayperiod, TimeUnit.DAYS);
//	    WinkhouseDBAgentUtils.getInstance().getPreferenceStore().setValue(WinkhouseDBAgentUtils.LAST_START_BACKUPDATI, sdf.format(new Date()));
//	    try {
//			WinkhouseDBAgentUtils.getInstance().getPreferenceStore().save();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	    
//	    return backupHandle;
//	}
	
//	public long getDelayInit(){
//		
//		long return_value = 0;
//		
//		String periodo = (WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.PERIODOBACKUPDATI).equalsIgnoreCase(""))
//				  		  ? ""
//				  		  : WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.PERIODOBACKUPDATI);
//
//		if (periodo.equalsIgnoreCase("1 giorno")){
//			return_value = 1;
//		}
//		if (periodo.equalsIgnoreCase("1 settimana")){
//			return_value = 7;
//		}
//		if (periodo.equalsIgnoreCase("1 mese")){			
//			return_value = 30;						
//		}
//
//		
//		return return_value;
//		
//	}
	
//	public void backuptaskStatus(){
//		
//		boolean abilita = (WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.ABILITABACKUPDATI).equalsIgnoreCase(""))
//						   ? false
//		                   : Boolean.valueOf(WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.ABILITABACKUPDATI));
//		
//		
//		if (abilita){
//			
//			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//							
//			Date last_start = null;
//			try {
//				last_start = sdf.parse(WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.LAST_START_BACKUPDATI));
//			} catch (ParseException e) {
//				last_start = new Date();
//				WinkhouseDBAgentUtils.getInstance().getPreferenceStore().setValue(WinkhouseDBAgentUtils.LAST_START_BACKUPDATI, sdf.format(last_start));
//			    try {
//					WinkhouseDBAgentUtils.getInstance().getPreferenceStore().save();
//				} catch (IOException ioe) {
//					ioe.printStackTrace();
//				}				
//			}
//			
//			int days = WinkhouseDBAgentUtils.getInstance().differenceInDays(last_start, new Date());
//			
//			startScheduledJob(days,getDelayInit());
//				
//		}
//		
//	}

	public boolean restoreDBBackUp(){
		
		boolean return_value = true;
		
		String pathDati = Activator.getDefault()
				   				   .getStateLocation()
				   				   .toFile()
				   				   .toString() + File.separator + "datidb";
		
		FileDialog fd = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		fd.setFilterExtensions(new String[]{"*.tar.gz"});
	    String pathDatiTar = fd.open();
			
		if (pathDatiTar != null){
			try {
				HSQLDBHelper.getInstance().chiudiDatabase();
				
				DbBackupMain.main(new String[] {"--extract","--overwrite",pathDatiTar,pathDati});
//				HSQLDBHelper.getInstance().startHSQLDB();
			} catch (IOException e) {
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
										"Errore ripristino database",
										"Si � verificato un errore nella lettura del file di backup");
				return_value = false;
			} catch (TarMalformatException e) {
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
										"Errore ripristino database",
										"Si � verificato un errore nella apertura del file di backup");
				return_value = false;
			}
		}
		return return_value;
		
	}

    public boolean executeBackUp(){
		
		boolean return_value = true;
		
//		String pathDati = (WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.CARTELLADATI).equalsIgnoreCase(""))
//				   ? Activator.getDefault().getStateLocation().toFile().toString()+DataBasePage.DEFAULT_DB_FOLDER
//				   : WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.CARTELLADATI);

		DirectoryDialog fd = new DirectoryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		String pathbackup = fd.open();
		pathbackup += File.separator;
		File f = new File(pathbackup);
		f.mkdirs();
		
		String query = "BACKUP DATABASE TO '" + pathbackup + "' NOT BLOCKING";
		
		Connection con = ConnectionManager.getInstance().getConnection();
		PreparedStatement ps = null;
				
		if (con != null){
			try{
				
				ps = con.prepareStatement(query);
				ps.execute();		
				con.commit();
			
			}catch(SQLException sql){
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				sql.printStackTrace();
				return_value = false;
			}finally{
				try {
					ps.close();
				} catch (SQLException e) {
					ps = null;
				}
				try {
					con.close();
				} catch (SQLException e) {
					con = null;
				}
				
			}
		}
				
		return return_value;
		
	}
}
