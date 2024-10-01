package winkhouse.action.permessi;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.db.ConnectionManager;
import winkhouse.helper.PermessiUIHelper;
import winkhouse.model.AgentiModel;
import winkhouse.orm.Agenti;
import winkhouse.view.permessi.DettaglioPermessiAgenteView;

public class SalvaPermessiAgente extends Action {

	public SalvaPermessiAgente() {
		setImageDescriptor(Activator.getImageDescriptor("icons/document-save.png"));
		setToolTipText("Salva i permessi dell'agente");
	}

	@Override
	public void run() {
		
		DettaglioPermessiAgenteView div = (DettaglioPermessiAgenteView)PlatformUI.getWorkbench()
				 																 .getActiveWorkbenchWindow()
				 																 .getActivePage()
				 																 .getActivePart();
		
		Connection con = ConnectionManager.getInstance().getConnection();
		
		PermessiUIHelper pUI = new PermessiUIHelper();
		Agenti agente = div.getAgente();
		// TODO cayenne
//		if (pUI.updateListaPermessiAgente(agente, con, false)){
//			try {
//				con.commit();
//			} catch (SQLException e) {
//				con = null;
//				e.printStackTrace();
//			}
//		}else{
//			try {
//				con.rollback();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		agente.resetPermessi();
		div.setAgente(agente);
		
	}
	
	


}
