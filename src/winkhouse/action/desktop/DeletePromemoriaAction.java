package winkhouse.action.desktop;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.PromemoriaDAO;
import winkhouse.dao.PromemoriaLinksDAO;
import winkhouse.dao.PromemoriaOggettiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.model.PromemoriaModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.desktop.DesktopView;
import winkhouse.view.desktop.model.MyNode;
import winkhouse.vo.PromemoriaLinksVO;
import winkhouse.vo.PromemoriaOggettiVO;
import winkhouse.wizard.RicercaWizard;

public class DeletePromemoriaAction extends Action {

	private DesktopView desktop = null;	
	
	public DeletePromemoriaAction(DesktopView desktop) {
		this.setToolTipText("Cancella promemoria dall'archivio");
		this.setImageDescriptor(Activator.getImageDescriptor("icons/edittrash.png"));
		this.desktop = desktop;
	}
	
	@Override
	public void run() {
		
		DesktopView dv = (DesktopView)PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getActivePage()
												.getActivePart();

		if (dv.getDesktop_type() == DesktopView.PROMEMORIA_TYPE){

			ArrayList al = (ArrayList)desktop.getDesktop().getInput();
			Iterator it = al.iterator();
			boolean result = true;
			
			PromemoriaDAO pDAO = new PromemoriaDAO();
			PromemoriaOggettiDAO poDAO = new PromemoriaOggettiDAO();
			PromemoriaLinksDAO plDAO = new PromemoriaLinksDAO();
			
			Connection con = ConnectionManager.getInstance().getConnection();
			while (it.hasNext()) {
				
				MyNode myNode = (MyNode) it.next();
				
				Iterator<MyNode> itcon = myNode.getConnectedTo().iterator();
				
				while(itcon.hasNext()){
					
					MyNode myNodeCon = itcon.next();
					
					if (myNodeCon.getType().equalsIgnoreCase(WinkhouseUtils.PROMEMORIA)){
						if (myNodeCon.isSelected() == true){
							if (poDAO.deleteByCodPromemoria(Integer.valueOf(myNodeCon.getId()), con, false)){
								if (plDAO.deleteByCodPromemoria(Integer.valueOf(myNodeCon.getId()), con, false)){
									if (pDAO.deletePromemoria(Integer.valueOf(myNodeCon.getId()), con, false) == false){
										result = false;
										break;
									}
								}else{
									result = false;
									break;					
								}
									
							}else{
								result = false;
								break;					
							}
														
						}
					}
					if (myNodeCon.getType().equalsIgnoreCase(WinkhouseUtils.IMMOBILI) || myNodeCon.getType().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
						if (myNodeCon.isSelected() == true){
							PromemoriaOggettiVO povo = new PromemoriaOggettiVO();
							povo.setCodOggetto(Integer.valueOf(myNodeCon.getId()));					
							povo.setCodPromemoria(((PromemoriaModel)myNodeCon.getObjModel()).getCodPromemoria());
							if (myNodeCon.getType().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
								povo.setTipo(RicercaWizard.IMMOBILI);	
							}
							if (myNodeCon.getType().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
								povo.setTipo(RicercaWizard.ANAGRAFICHE);	
							}										
							if (!poDAO.delete(povo, con, false)){
								result = false;
								break;					
							}
						}				
					}
					if (myNodeCon.getType().equalsIgnoreCase(MyNode.URLLINK) || myNodeCon.getType().equalsIgnoreCase(MyNode.FILELINK)){
						if (myNodeCon.isSelected() == true){
							PromemoriaLinksVO plvo = new PromemoriaLinksVO();
							plvo.setUrilink(myNodeCon.getName());					
							plvo.setCodPromemoria(((PromemoriaModel)myNodeCon.getObjModel()).getCodPromemoria());
							if (!plDAO.delete(plvo, con, false)){
								result = false;
								break;					
							}
						}										
					}					
				}
				if (myNode.getType().equalsIgnoreCase(WinkhouseUtils.PROMEMORIA)){
					if (myNode.isSelected() == true){
						if (poDAO.deleteByCodPromemoria(Integer.valueOf(myNode.getId()), con, false)){
							if (plDAO.deleteByCodPromemoria(Integer.valueOf(myNode.getId()), con, false)){
								if (pDAO.deletePromemoria(Integer.valueOf(myNode.getId()), con, false) == false){
									result = false;
									break;
								}
							}else{
								result = false;
								break;					
							}
								
						}else{
							result = false;
							break;					
						}
													
					}
				}
				if (myNode.getType().equalsIgnoreCase(WinkhouseUtils.IMMOBILI) || myNode.getType().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
					if (myNode.isSelected() == true){
						PromemoriaOggettiVO povo = new PromemoriaOggettiVO();
						povo.setCodOggetto(Integer.valueOf(myNode.getId()));					
						povo.setCodPromemoria(((PromemoriaModel)myNode.getObjModel()).getCodPromemoria());
						if (myNode.getType().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
							povo.setTipo(RicercaWizard.IMMOBILI);	
						}
						if (myNode.getType().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
							povo.setTipo(RicercaWizard.ANAGRAFICHE);	
						}										
						if (!poDAO.delete(povo, con, false)){
							result = false;
							break;					
						}
					}				
				}
				
			}
			
			try {
				if (result){
					con.commit();
				}else{
					con.rollback();
				}
				desktop.setAgente(desktop.getAgente());
			} catch (SQLException e) {
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
										"Errore cancellazione promemoria",
										"Si è verificato un errore durante la cancellazione dei promemoria");
			}
			
		}else{
			if (desktop.getDesktop().getSelection() != null){
				StructuredSelection o = (StructuredSelection)desktop.getDesktop().getSelection();
				if (o.getFirstElement() != null){
					MyNode n = (MyNode)o.getFirstElement();
					ArrayList al = (ArrayList)desktop.getDesktop().getInput();
		            if (!PlatformUI.getWorkbench().getDisplay().isDisposed()){
		            	al = deleteHerarchy((ArrayList<MyNode>)al.clone(), n, n.getStructureLevel());
		            	desktop.getDesktop().setInput(al);
		            	PlatformUI.getWorkbench().getDisplay().asyncExec(new refreshRunner(al));
		            }
	            }
			}
			
			
//			ArrayList alclone = (ArrayList)al.clone();
//			
//			for (int i = 0; i < al.size(); i++) {
//				array_type array_element = al[i];
//				
//			}
//			
//			Iterator it = al.iterator();
//			boolean result = true;
//			
//			while (it.hasNext()) {
//				
//				MyNode myNode = (MyNode) it.next();
//				
//				Iterator<MyNode> itcon = myNode.getConnectedTo().iterator();
//				
//				while(itcon.hasNext()){
//					
//					MyNode myNodeCon = itcon.next();
//				}
//			}

//			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
//									  "Funzione non disponibile",
//									  "La funzione di cancellazione non è disponibile in modalità struttura, \n per pulire la scrivania passare alla modalità promemoria e poi ritornare in modalità struttura");			
			
		}
	}

	private ArrayList<MyNode> deleteHerarchy(ArrayList<MyNode> container, MyNode node, Integer startStructureLevel){
//		ArrayList<MyNode> wc = (ArrayList)container.clone();
		for (MyNode cnode : node.getConnectedTo()){
			if (cnode.getStructureLevel() >= startStructureLevel){
				container = deleteItemFromArray(container, cnode);
			}
		}
		container = deleteItemFromArray(container, node);
		return container;
	}
	private ArrayList<MyNode> deleteItemFromArray(ArrayList<MyNode> container, MyNode node){
		for (MyNode cnode : container){
			if ((cnode.getType().equalsIgnoreCase(node.getType())) && 
				(cnode.getId().equalsIgnoreCase(node.getId())) &&
				(cnode.getStructureLevel() == node.getStructureLevel())){
				container.remove(cnode);
				break;
			}
		}
		return container;
	}
	
	class refreshRunner implements Runnable {
		ArrayList<MyNode> al = null;
		
		public refreshRunner(ArrayList<MyNode> container){
			this.al = container;		
		}
		public void run() {			
			desktop.getDesktop().refresh();

		}
	}
		
		
		
	}
