package winkhouse.action.colloqui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.db.ConnectionManager;
import winkhouse.helper.ColloquiHelper;
import winkhouse.model.ColloquiModel;
import winkhouse.view.colloqui.ColloquiTreeView;
import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.common.ColloquiView;



public class CancellaColloquio extends Action {

	public CancellaColloquio() {
	}

	public CancellaColloquio(String text) {
		super(text);
	}

	public CancellaColloquio(String text, ImageDescriptor image) {
		super(text, image);
	}

	public CancellaColloquio(String text, int style) {
		super(text, style);
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/edittrash.png");
	}

	@Override
	public String getText() {
		return "Cancella colloquio";
	}

	@Override
	public void run() {
		
		DettaglioColloquioView dcv = null;
		ColloquiView cv = null;
		ColloquiModel cm = null;
		ColloquiTreeView ctv = null;
		
		try {
			dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
					  								.getActiveWorkbenchWindow()
					  								.getActivePage()
					  								.getActivePart();
			cm = dcv.getColloquio() ;
		} catch (Exception e) {
			dcv = null;
			
		}
		try{
			cv = (ColloquiView)PlatformUI.getWorkbench()
					  					 .getActiveWorkbenchWindow()
					  					 .getActivePage()
					  					 .findView(ColloquiView.ID);
		} catch (Exception e) {
			cv = null;
		}
		try{
			ctv = (ColloquiTreeView)PlatformUI.getWorkbench()
					  					 .getActiveWorkbenchWindow()
					  					 .getActivePage()
					  					 .findView(ColloquiTreeView.ID);
			cm = (ColloquiModel)((StructuredSelection)ctv.getViewer().getSelection()).getFirstElement();
		} catch (Exception e) {
			ctv = null;			
		}

		if (cm != null){
			
			ColloquiHelper ch = new ColloquiHelper();
			Connection con  = ConnectionManager.getInstance()
											   .getConnection();
			
			HashMap hm = ch.deleteColloquio(cm, con, false);
			if ((Boolean)hm.get(ColloquiHelper.RESULT_DELETE_COLLOQUIO_DB)){
				if ((Boolean)hm.get(ColloquiHelper.RESULT_DELETE_ALLEGATI_DB)){
					ArrayList alAllegati = (ArrayList)hm.get(ColloquiHelper.LIST_DELETE_ALLEGATI_FILE);
					try {
						con.commit();
						if (!ch.deleteAllegatiColloqui(alAllegati)){
							MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING);
							mb.setText("Cancellazione");
							mb.setMessage("Cancellazione eseguita, ma alcuni file allegati non sono stati cancellati");
							mb.open();
						}else{
							MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION);
							mb.setText("Cancellazione");
							mb.setMessage("Cancellazione eseguita corretamente");			
							mb.open();
						}
						if (dcv != null){
							PlatformUI.getWorkbench()
									  .getActiveWorkbenchWindow()
									  .getActivePage()
									  .hideView(dcv);
						}
						if (cv != null){
							if (cv.getAnagrafica() != null){
								cv.getAnagrafica().setColloqui(null);
								cv.setAnagrafica(cv.getAnagrafica());
							}else if (cv.getImmobile() != null){
								cv.getImmobile().setColloqui(null);
								cv.setImmobile(cv.getImmobile());
							}	
						}
						if (ctv != null){
							ctv.getViewer().refresh();
						}
					} catch (SQLException e) {
						try {
							con.rollback();
							MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ERROR);
							mb.setText("Errore cancellazione");
							mb.setMessage("Cancellazione non eseguita, errore nella cancellazione dalla base dati");			
							mb.open();	
							
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}else{
					try {
						con.rollback();
						MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ERROR);
						mb.setText("Errore cancellazione");
						mb.setMessage("Cancellazione non eseguita, errore nella cancellazione dalla base dati");			
						mb.open();	
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

				}
			}else{
				try {
					con.rollback();
					MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ERROR);
					mb.setText("Errore cancellazione");
					mb.setMessage("Cancellazione non eseguita, errore nella cancellazione dalla base dati");			
					mb.open();	
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}					
			}
		}else{
			MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ERROR);
			mb.setText("Errore salvataggio");
			mb.setMessage("Ops!! Errore inaspettato durante la cancellazione colloquio");			
			mb.open();	
		}

	}
	

}
