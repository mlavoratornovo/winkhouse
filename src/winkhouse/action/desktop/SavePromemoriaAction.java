package winkhouse.action.desktop;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.PromemoriaDAO;
import winkhouse.dao.PromemoriaLinksDAO;
import winkhouse.dao.PromemoriaOggettiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.model.PromemoriaModel;
import winkhouse.model.PromemoriaOggettiModel;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.desktop.PopUpDettaglioPromemoria;
import winkhouse.vo.PromemoriaLinksVO;

public class SavePromemoriaAction extends Action {

	private PopUpDettaglioPromemoria pudp = null;
	
	public SavePromemoriaAction(PopUpDettaglioPromemoria pudp) {
		setToolTipText("Salva i dati del promemoria");
		setImageDescriptor(Activator.getImageDescriptor("icons/document-save.png"));
		this.pudp = pudp;
	}

	@Override
	public void run() {
		
		PromemoriaModel pm = pudp.getPromemoria();
		
		if ((pm.getDescrizione() != null) && (!pm.getDescrizione().trim().equalsIgnoreCase(""))){
			
			PromemoriaDAO pdao = new PromemoriaDAO();
			PromemoriaOggettiDAO poDAO = new PromemoriaOggettiDAO();
			PromemoriaLinksDAO plDAO = new PromemoriaLinksDAO();
			
			if ((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null) && 
			    (Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))) &&
			    WinkhouseUtils.getInstance().getLoggedAgent() != null){
				pm.setCodUserUpdate(WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
			}
			Connection con = ConnectionManager.getInstance().getConnection();
			pm.setDateUpdate(new Date());
			if (pdao.saveUpdate(pm, con, false)){

					if (poDAO.deleteByCodPromemoria(pm.getCodPromemoria(), con, false)){
						for (PromemoriaOggettiModel iterable_element : pm.getLinkedObjects()) {
							iterable_element.setCodPromemoria(pm.getCodPromemoria());
							if (!poDAO.insert(iterable_element, con, false)){
								try {
									con.rollback();
								} catch (SQLException e) {
									e.printStackTrace();
								}
								MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						  				"Inserimento promemoria",
						  				"Si è verificato un errore durante il salvataggio");
								

							}
						}
						
						try {
							con.commit();
						} catch (SQLException e) {
							e.printStackTrace();
						}						
						pudp.closeMe();
					}else{
						try {
							con.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
								  				"Inserimento promemoria",
								  				"Si è verificato un errore durante il salvataggio");
					}

					if (plDAO.deleteByCodPromemoria(pm.getCodPromemoria(), con, false)){
						for (PromemoriaLinksVO iterable_element : pm.getLinksUrls()) {
							iterable_element.setCodPromemoria(pm.getCodPromemoria());
							if (!plDAO.insert(iterable_element, con, false)){
								try {
									con.rollback();
								} catch (SQLException e) {
									e.printStackTrace();
								}
								MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						  				"Inserimento promemoria",
						  				"Si è verificato un errore durante il salvataggio");
								

							}
						}
						
						try {
							con.commit();
						} catch (SQLException e) {
							e.printStackTrace();
						}						
						pudp.closeMe();
					}else{
						try {
							con.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
								  				"Inserimento promemoria",
								  				"Si è verificato un errore durante il salvataggio");
					}
					
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						  				"Inserimento promemoria",
						  				"Si è verificato un errore durante il salvataggio");
			}
		}else{
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
									  "Inserimento promemoria",
									  "Inserire il testo del promemoria");
		}
		
	}


}
