package winkhouse.action.colloqui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Colloqui;
import winkhouse.orm.Immobili;
import winkhouse.perspective.AnagrafichePerspective;
import winkhouse.perspective.ColloquiPerspective;
import winkhouse.perspective.ImmobiliPerspective;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.view.anagrafica.handler.DettaglioAnagraficaHandler;
import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.immobili.DettaglioImmobileView;
import winkhouse.view.immobili.handler.DettaglioImmobiliHandler;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ImmobiliVO;

public class NuovoColloquioAction extends Action {


	public NuovoColloquioAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {	
		
		String currentPerspectiveId = PlatformUI.getWorkbench()
			    								.getActiveWorkbenchWindow()
												.getActivePage()
												.getPerspective().getId();
												
		if ((currentPerspectiveId != null) && 
		    (currentPerspectiveId.equalsIgnoreCase(ImmobiliPerspective.ID))){
			
			if (WinkhouseUtils.getInstance()
							  .getLastCodImmobileSelected() != null){

				ImmobiliDAO iDAO = new ImmobiliDAO();
				Immobili o = iDAO.getImmobileById(WinkhouseUtils.getInstance().getLastCodImmobileSelected());
				
				if (o != null){
					
					DettaglioImmobileView div = DettaglioImmobiliHandler.getInstance().getDettaglioImmobile(o);
												 					    
					Immobili im = div.getImmobile();
					
					Colloqui cm = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Colloqui.class);
					
					try {
						DettaglioColloquioView dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
							    													   .getActiveWorkbenchWindow()
							    													   .getActivePage()															 
							    													   .showView(DettaglioColloquioView.ID,String.valueOf(cm.getCodColloquio()),IWorkbenchPage.VIEW_CREATE);
						dcv.setColloquio(cm);
						dcv.setImmobile(im);
						dcv.setFocus();
						
						PlatformUI.getWorkbench()
		   		 		 					  .getActiveWorkbenchWindow()
		   		 		 					  .getActivePage()
		   		 		 					  .bringToTop(dcv);
				
					} catch (PartInitException e) {
		
						e.printStackTrace();
					}
					
				}else{
					
						MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
			   					   					   SWT.ICON_WARNING);
						mb.setText("Attenzione, selezione immobile non valida");
						mb.setMessage("L'immobile selezionato non è presente in archivio, \n " +
									  "potrebbe essere stato cancellato da un altro utente");			
						mb.open();																    	
				 					
				}				
			}else{
				MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						   					   SWT.ICON_WARNING);
				mb.setText("Attenzione, selezione immobile");
				mb.setMessage("Selezionare un dettaglio immobile");			
				mb.open();													

			}
			
		}

		if ((currentPerspectiveId != null) && 
			(currentPerspectiveId.equalsIgnoreCase(AnagrafichePerspective.ID))){

		   if (WinkhouseUtils.getInstance()
			  	 			 .getLastCodAnagraficaSelected() != null){
			   
				AnagraficheDAO aDAO = new AnagraficheDAO();
				Anagrafiche o = aDAO.getAnagraficheById(WinkhouseUtils.getInstance().getLastCodAnagraficaSelected());
						
				if (o != null){
							
					
					DettaglioAnagraficaView dav = DettaglioAnagraficaHandler.getInstance().getDettaglioAnagrafica(o);
												 					        
//					AnagraficheModel am = dav.getAnagrafica();
					
					Colloqui cm = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Colloqui.class);
					
					try {
						DettaglioColloquioView dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
							    													   .getActiveWorkbenchWindow()
							    													   .getActivePage()															 
							    													   .showView(DettaglioColloquioView.ID,String.valueOf(cm.getCodColloquio()),IWorkbenchPage.VIEW_CREATE);
						dcv.setColloquio(cm);
						dcv.addAnagrafica(o);
						
						Activator.getDefault().getWorkbench()
		   		 		 					  .getActiveWorkbenchWindow()
		   		 		 					  .getActivePage()
		   		 		 					  .bringToTop(dcv);
				
						dcv.setFocus();

						
					} catch (PartInitException e) {
		
						e.printStackTrace();
					}
					
			    }else{
					MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
		   					   					   SWT.ICON_WARNING);
					mb.setText("Attenzione, selezione anagrafica non valida");
					mb.setMessage("L'anagrafica selezionata non � presente in archivio, \n " +
								  "potrebbe essere stata cancellata da un altro utente");			
					mb.open();																    	
			    }
			
		   }else{
				MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
	   					   					   SWT.ICON_WARNING);
				mb.setText("Attenzione, selezione anagrafica non valida");
				mb.setMessage("Selezionare un dettaglio anagrafica");			
				mb.open();													
			   
		   }
		 
		}
		
		if ((currentPerspectiveId != null) && 
			(currentPerspectiveId.equalsIgnoreCase(ColloquiPerspective.ID))){
			Colloqui cm = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Colloqui.class);
			try {
				DettaglioColloquioView dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
					    													   .getActiveWorkbenchWindow()
					    													   .getActivePage()															 
					    													   .showView(DettaglioColloquioView.ID,String.valueOf(cm.getCodColloquio()),IWorkbenchPage.VIEW_CREATE);
				dcv.setColloquio(cm);				
				
				PlatformUI.getWorkbench()
   		 		 					  .getActiveWorkbenchWindow()
   		 		 					  .getActivePage()
   		 		 					  .bringToTop(dcv);
		
				dcv.setFocus();

				
			} catch (PartInitException e) {

				e.printStackTrace();
			}

		}		
		
	}
	
}