package winkhouse.action.immagini;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.ImmaginiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.helper.ImmaginiHelper;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Immagini;
import winkhouse.orm.Immobili;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.immobili.ImmaginiImmobiliView;
import winkhouse.vo.ImmagineVO;



public class AggiungiImmaginiAction extends Action {

	public AggiungiImmaginiAction() {

	}

	public AggiungiImmaginiAction(String text) {
		super(text);
	}

	public AggiungiImmaginiAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public AggiungiImmaginiAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		if ((WinkhouseUtils.getInstance().getLastCodImmobileSelected() != null) && 
			(WinkhouseUtils.getInstance().getLastCodImmobileSelected().intValue() != 0)){
			
			ImmaginiImmobiliView iiv = (ImmaginiImmobiliView)PlatformUI.getWorkbench()
																	   .getActiveWorkbenchWindow()
					  							 					   .getActivePage()
					  							 					   .findView(ImmaginiImmobiliView.ID);
			Immobili im = iiv.getImmobile();
			if ((im != null) && (im.getCodImmobile() != 0)){
				FileDialog fd = new FileDialog(PlatformUI.getWorkbench()
						   								.getActiveWorkbenchWindow().getShell(),
						   					   SWT.OPEN|SWT.MULTI);
				
				String[] extensions = new String[]{"*.jpg","*.png","*.gif"};
				String[] extensionNames = new String[]{"jpg","png","gif"};

				fd.open();
				ImmaginiDAO immaginiDAO = new ImmaginiDAO();
				Immagini iVO = im.getEditObjectContext().newObject(Immagini.class);			
				if (fd.getFileNames().length > 0){
					for (int i = 0; i < fd.getFileNames().length; i++) {
						if (WinkhouseUtils.getInstance()
											.copiaFile(fd.getFilterPath() + File.separator + File.separator + fd.getFileNames()[i], 
													   WinkhouseUtils.getInstance()
													   				   .getPreferenceStore()
													   				   .getString(WinkhouseUtils.IMAGEPATH) + 
													   File.separator + File.separator + im.getCodImmobile() + 
													   File.separator + File.separator + fd.getFileNames()[i])
							){
							Connection con = ConnectionManager.getInstance().getConnection();						
//							iVO.setCodImmobile(WinkhouseUtils.getInstance().getLastCodImmobileSelected());
							iVO.setOrdine(im.getImmaginis().size()+1);
							iVO.setPathimmagine(fd.getFileNames()[i]);
							im.addToImmaginis(iVO);
//							if (immaginiDAO.saveUpdate(iVO, con, true)){								
//								im.addToImmaginis(iVO);
//							}else{
//								MessageBox mb = new MessageBox(Activator.getDefault()
//																		.getWorkbench()
//																		.getActiveWorkbenchWindow().getShell(),
//															   SWT.ERROR);
//								mb.setMessage("Errore salvataggio dati immagine");
//								mb.open();										
//								ImmaginiHelper ih = new ImmaginiHelper();
//								ArrayList al = new ArrayList();
//								al.add(iVO);
//								ih.deleteImmagini(al);											
//							}
						}else{
							MessageBox mb = new MessageBox(PlatformUI
																	.getWorkbench()
																	.getActiveWorkbenchWindow().getShell(),
														   SWT.ERROR);
							mb.setMessage("Errore nel copiare l'immagine");
							mb.open();										
							
						}
					}
					
				}				
				iiv.showImages();
			}else{
				MessageDialog.openWarning(PlatformUI.getWorkbench()
						     					   .getActiveWorkbenchWindow().getShell(),
						     			  "Attenzione", 
				  						  "Selezionare un file");
				
			}
		}else{
			MessageDialog.openWarning(PlatformUI.getWorkbench()
											   .getActiveWorkbenchWindow().getShell(),
									  "Attenzione", 
									  "Selezionare un immobile o, se nuovo, salvare l'immobile selezionato");

		}
	}

	
}
