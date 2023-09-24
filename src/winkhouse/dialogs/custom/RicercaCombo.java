package winkhouse.dialogs.custom;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.dao.RicercheDAO;
import winkhouse.helper.ProfilerHelper;
import winkhouse.model.RicercheModel;
import winkhouse.perspective.AffittiPerspective;
import winkhouse.perspective.AnagrafichePerspective;
import winkhouse.perspective.ColloquiPerspective;
import winkhouse.perspective.ImmobiliPerspective;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.RicercheVO;
import winkhouse.wizard.RicercaWizard;

public class RicercaCombo extends ControlContribution {
	
	public final static String ID = "RICERCACOMBO";
	private RicercheDAO rDAO = null;
	private ComboViewer cv = null;
	private Integer tipoRicerca = null;
	
	public RicercaCombo() {
		super(ID);
		rDAO = new RicercheDAO();		
	}

	@Override
	protected Control createControl(Composite parent) {
		
		cv = new ComboViewer(parent, SWT.BORDER);
		cv.getCombo().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				cv.setInput(new Object());
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
//		cv.getCombo().addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focusLost(FocusEvent e) {
//			}
//			
//			@Override
//			public void focusGained(FocusEvent e) {
//				
//			}
//		});
		
		cv.setContentProvider(new IStructuredContentProvider() {

			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				
				String currentPerspectiveId = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.getPerspective().getId();
				
				if (currentPerspectiveId == null){
	    			currentPerspectiveId = (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.STARTPERSPECTIVE).equalsIgnoreCase(""))
							   ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.STARTPERSPECTIVE)
							   : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.STARTPERSPECTIVE);

				} 

				if (currentPerspectiveId.equalsIgnoreCase(ImmobiliPerspective.ID)){
					tipoRicerca = RicercheVO.RICERCHE_IMMOBILI;
					cv.getCombo().setToolTipText("Ricerche immobili");
				}else if (currentPerspectiveId.equalsIgnoreCase(AffittiPerspective.ID)){
					tipoRicerca = RicercheVO.RICERCHE_IMMOBILI_AFFITTI;
					cv.getCombo().setToolTipText("Ricerche affitti");
				}else if (currentPerspectiveId.equalsIgnoreCase(AnagrafichePerspective.ID)){
					tipoRicerca = RicercheVO.RICERCHE_ANAGRAFICHE;
					cv.getCombo().setToolTipText("Ricerche anagrafiche");
				}else if (currentPerspectiveId.equalsIgnoreCase(ColloquiPerspective.ID)){
					tipoRicerca = RicercheVO.RICERCHE_COLLOQUI;
					cv.getCombo().setToolTipText("Ricerche colloqui");
				}
				
				System.out.println(currentPerspectiveId);
		        if (tipoRicerca != null ){
		        	System.out.println(tipoRicerca);
		    		ArrayList al = rDAO.getRichercheByTipo(RicercheModel.class.getName(), tipoRicerca);
		    		return al.toArray();	
		        	
		        }
		        return new ArrayList().toArray();
			}
		});
		cv.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				
				return ((RicercheModel)element).getNome();
			}			

		});
		cv.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				if ((event.getSelection() != null) && (((StructuredSelection)event.getSelection()).getFirstElement() != null)){
					RicercheModel ricerca = (RicercheModel)((StructuredSelection)event.getSelection()).getFirstElement();
					if (ProfilerHelper.getInstance().getPermessoUI(RicercaWizard.ID)){

						String currentPerspectiveId = PlatformUI.getWorkbench()
							    								.getActiveWorkbenchWindow()
							    								.getActivePage()
							    								.getPerspective().getId();
								
						if (currentPerspectiveId != null){
							
							RicercaWizard wizard = null;
							if (currentPerspectiveId.equalsIgnoreCase(ImmobiliPerspective.ID)){
								wizard = new RicercaWizard(RicercaWizard.RICERCAFAST_IMMOBILI);
								wizard.getRicerca().setCriteriImmobili(ricerca.getCriteri());
							}else if (currentPerspectiveId.equalsIgnoreCase(AffittiPerspective.ID)){
								wizard = new RicercaWizard(RicercaWizard.RICERCAFAST_AFFITTI);
								wizard.getRicerca().setCriteriImmobiliAffitti(ricerca.getCriteri());
							}else if (currentPerspectiveId.equalsIgnoreCase(AnagrafichePerspective.ID)){
								wizard = new RicercaWizard(RicercaWizard.RICERCAFAST_ANAGRAFICHE);
								wizard.getRicerca().setCriteriAnagrafiche(ricerca.getCriteri());
							}else if (currentPerspectiveId.equalsIgnoreCase(ColloquiPerspective.ID)){
								wizard = new RicercaWizard(RicercaWizard.RICERCAFAST_COLLOQUI);
								wizard.getRicerca().setCriteriColloqui(ricerca.getCriteri());
							}

							IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();							
							WizardDialog dialog = new WizardDialog(window.getShell(), wizard);	
							dialog.setPageSize(400, 300);
							WinkhouseUtils.getInstance()
										  .setRicercaWiz(wizard);
							
							dialog.open();
						}else{
							MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
														   SWT.ERROR);
							mb.setText("impossibile eseguire ricerche in questa prospettiva");
							mb.setMessage("impossibile eseguire ricerche in questa prospettiva");			
							mb.open();													

						}

					}else{
						MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
								  					  "Controllo permessi accesso vista",
								  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
								  					  " non ha il permesso di accedere alla vista " + 
								  					  RicercaWizard.ID);
					}
					
				}else {
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
		  					  "Selezionare una ricerca",
		  					  "Selezione ricerca non ricevuta");
					
				}
				
			}
			
		});
		
		cv.getCombo().add("                                      ");
		
		return cv.getControl();
	}

	public ComboViewer getCv() {
		return cv;
	}

	
}
