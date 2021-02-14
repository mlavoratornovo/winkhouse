package winkhouse.view.report;

import java.util.ArrayList;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.report.NuovoReport;
import winkhouse.action.report.RefreshReportTreeAction;
import winkhouse.dao.ReportDAO;
import winkhouse.model.ReportModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ReportTypes;
import winkhouse.view.report.handler.DettaglioReportHandler;
import winkhouse.vo.ImmobiliVO;



public class ReportTreeView extends ViewPart {

	public final static String ID = "winkhouse.reporttreeview";
	private TreeViewer viewer = null;
	private ReportDAO reportDAO = new ReportDAO();
	
	public ReportTreeView() {
	}

	class ViewContentProvider implements ITreeContentProvider {
		
		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof WinkhouseUtils.ReportTypes){
				ArrayList reports = new ArrayList();
				reports.addAll(reportDAO.getReportByTipologia(ReportModel.class.getName(), 
													  		  ((WinkhouseUtils.ReportTypes)parentElement).getClassName()));
				reports.addAll(reportDAO.getReportListByTipologia(ReportModel.class.getName(), 
				  		  										  ((WinkhouseUtils.ReportTypes)parentElement).getClassName()));
				return reports.toArray();
			}else{				
				ArrayList alTipiReport = new ArrayList();
				alTipiReport.add(WinkhouseUtils.getInstance(). new ReportTypes(WinkhouseUtils.IMMOBILI,"Immobili"));
				alTipiReport.add(WinkhouseUtils.getInstance(). new ReportTypes(WinkhouseUtils.ANAGRAFICHE,"Anagrafiche"));
				alTipiReport.add(WinkhouseUtils.getInstance(). new ReportTypes(WinkhouseUtils.COLLOQUI,"Colloqui"));
				alTipiReport.add(WinkhouseUtils.getInstance(). new ReportTypes(WinkhouseUtils.APPUNTAMENTI,"Appuntamenti"));
				alTipiReport.add(WinkhouseUtils.getInstance(). new ReportTypes(WinkhouseUtils.AFFITTI,"Affitti"));
				return alTipiReport.toArray();
			}
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {			
			return element instanceof WinkhouseUtils.ReportTypes;
		}

	}

	class ViewLabelProvider extends LabelProvider {
		
		public String getText(Object obj) {
			String returnValue = null;
			if (obj instanceof ReportTypes){
				returnValue = ((ReportTypes)obj).getReportTypeName();
			}
			if (obj instanceof ReportModel){
				returnValue = ((ReportModel)obj).getNome();
			}			
			return returnValue;
		}

		public Image getImage(Object obj) {
			if (obj instanceof ReportModel){
				return Activator.getDefault().getImageDescriptor("icons/kontact_news16.png").createImage();
			}else{
				if (((ReportTypes)obj).getClassName().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){			
					return Activator.getDefault().getImageDescriptor("icons/gohome.png").createImage();
				}else if (((ReportTypes)obj).getClassName().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
					return Activator.getDefault().getImageDescriptor("icons/anagrafiche.png").createImage();
				}else if(((ReportTypes)obj).getClassName().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
					return Activator.getDefault().getImageDescriptor("icons/colloqui.png").createImage();
				}else if(((ReportTypes)obj).getClassName().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
					return Activator.getDefault().getImageDescriptor("icons/korgac.png").createImage();
				}else{
					return Activator.getDefault().getImageDescriptor("icons/affitti.png").createImage();
				}
			}
		}
	}

	
	@Override
	public void createPartControl(Composite parent) {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(new RefreshReportTreeAction("Aggiorna",Activator.getImageDescriptor("icons/adept_reinstall12.png")));
		mgr.add(new NuovoReport("Nuovo report",Activator.getImageDescriptor("icons/filenew.png")));		
		
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.getTree().addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				if (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof ReportModel){
					ReportModel rm = (ReportModel)((StructuredSelection)viewer.getSelection()).getFirstElement();
					DettaglioReportHandler.getInstance().getDettaglioImmobile(rm);					
				}
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				System.out.println("MouseEvent");
				if((e.button == 2)||(e.button == 3)){
					
					Object o = ((StructuredSelection)viewer.getSelection()).getFirstElement();
					Menu m = new Menu(viewer.getTree());
					
					if (o instanceof String){
					
						MenuItem miNuovo = new MenuItem(m,SWT.PUSH);
						miNuovo.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
						miNuovo.setText("Nuovo report");
						miNuovo.addSelectionListener(new SelectionListener(){

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}

							@Override
							public void widgetSelected(SelectionEvent e) {
								
								ReportModel rModel;
								
	/*								
									DettaglioReportView drv = (DettaglioReportView)PlatformUI.getWorkbench()
																							 .getActiveWorkbenchWindow()
																							 .getActivePage()
																							 .showView(DettaglioReportView.ID);
																							 */
									rModel = new ReportModel();
									rModel.setTipo(((WinkhouseUtils.ReportTypes)
													((StructuredSelection)viewer.getSelection()).getFirstElement()).getClassName());
									DettaglioReportHandler.getInstance().getDettaglioImmobile(rModel);	
//									drv.setReport(rModel);
									
																		
							}
							
						});
						
					}
					if (o instanceof ImmobiliVO){
						
						MenuItem miCancella = new MenuItem(m,SWT.PUSH);
						miCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
						miCancella.setText("Cancella report");
						
					}
					
					
					viewer.getTree().setMenu(m);
				}
				
			}
			
		});
		
		viewer.setInput(new Object());

	}

	@Override
	public void setFocus() {

	}

	public TreeViewer getViewer() {
		return viewer;
	}

	public void setViewer(TreeViewer viewer) {
		this.viewer = viewer;
	}

}
