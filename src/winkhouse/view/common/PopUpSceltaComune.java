package winkhouse.view.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import winkhouse.Activator;
import winkhouse.dao.ComuniDAO;
import winkhouse.vo.ComuniVO;


public class PopUpSceltaComune {

	private Shell popup = null;
	
	private String comune = null;
	private Button st_uguale = null;
	private Button st_inizia = null;
	private Button st_finisce = null;
	private Button st_compreso = null;
	private Text t_comune = null;
	private TableViewer tv = null;
	private Object callerObj = null;
	private String setterMethodName = null;
		
	
	public PopUpSceltaComune(Object callerObj, String setterMethod, String comune) {
		this.callerObj = callerObj;
		this.setterMethodName = setterMethod;
		this.comune = comune;

		createContent();
	}
		
	private void createContent(){
		popup = new Shell(Activator.getDefault().getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell(),
				  		  SWT.APPLICATION_MODAL|SWT.SHELL_TRIM);
		popup.setText("Ricerca codice istat comune");
		popup.setImage(Activator.getDefault()
							    .getImageDescriptor("icons/kfind.png")
							    .createImage());
		popup.setBackground(new Color(null,255,255,255));
		popup.setSize(400, 400);
				
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		
		GridData gdH = new GridData();
		gdH.grabExcessHorizontalSpace = true;
		gdH.horizontalAlignment = SWT.FILL;
		
		GridData gdH2 = new GridData();
		gdH2.grabExcessHorizontalSpace = true;
		gdH2.horizontalAlignment = SWT.FILL;
		gdH2.horizontalSpan = 2;

		GridData gdH3 = new GridData();
		gdH3.grabExcessHorizontalSpace = true;
		gdH3.horizontalAlignment = SWT.FILL;
		gdH3.verticalAlignment = SWT.FILL;
		gdH3.heightHint = 55;

		popup.setLayout(new GridLayout());
			
		FormToolkit ft = new FormToolkit(popup.getDisplay());
		Form f = ft.createForm(popup);
		f.getBody().setLayout(new GridLayout());
		f.setLayoutData(gd);		
		
		Composite c_search_type = ft.createComposite(f.getBody());
		c_search_type.setLayout(new RowLayout(SWT.HORIZONTAL));
		c_search_type.setLayoutData(gdH);
		st_uguale = ft.createButton(c_search_type, "uguale", SWT.RADIO);
		st_inizia = ft.createButton(c_search_type, "inizia", SWT.RADIO);
		st_finisce = ft.createButton(c_search_type, "finisce", SWT.RADIO);
		st_compreso = ft.createButton(c_search_type, "compreso", SWT.RADIO);		
		
		Composite c = ft.createComposite(f.getBody());
		c.setLayoutData(gdH3);
		c.setLayout(new GridLayout(2, false));
		
		Label l_ricerca_comune = ft.createLabel(c, "Comune da cercare");		
		l_ricerca_comune.setLayoutData(gdH2);
		
		t_comune = ft.createText(c, (this.comune != null)?this.comune:"");
		t_comune.setLayoutData(gdH);
		
		Button find = ft.createButton(c, "", SWT.FLAT);
		find.setImage(Activator.getImageDescriptor("icons/ricercabig.png").createImage());
		find.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				ComuniDAO cDAO = new ComuniDAO();
				if (st_uguale.getSelection()){
					tv.setInput(cDAO.getComuniMatchWith(t_comune.getText().toLowerCase()));
				}
				if (st_inizia.getSelection()){
					tv.setInput(cDAO.getComuniStartWith(t_comune.getText().toLowerCase()));
				}
				if (st_finisce.getSelection()){
					tv.setInput(cDAO.getComuniEndWith(t_comune.getText().toLowerCase()));
				}
				if (st_compreso.getSelection()){
					tv.setInput(cDAO.getComuniBetWeenWith(t_comune.getText().toLowerCase()));
				}
				
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		tv = new TableViewer(f.getBody(), SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tv.getTable().setLayoutData(gd);
		
		tv.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Object[] getElements(Object arg0) {
				if (arg0 instanceof ArrayList){
					return ((ArrayList) arg0).toArray();
				}
				return null;
			}
			
		});
		tv.setLabelProvider(new ITableLabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getColumnText(Object arg0, int arg1) {
				switch(arg1){
					case 0: return ((ComuniVO)arg0).getCodIstat();
					case 1: return ((ComuniVO)arg0).getComune();
					case 2: return ((ComuniVO)arg0).getProvincia();
					case 3: return ((ComuniVO)arg0).getRegione();
					case 4: return ((ComuniVO)arg0).getCap();
											
				}
				return null;
			}
			
			@Override
			public Image getColumnImage(Object arg0, int arg1) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		tv.addPostSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				ComuniVO cVO = (ComuniVO)((StructuredSelection)arg0.getSelection()).getFirstElement();
				returnValue(cVO);
				//popup.close();
				
			}
		});
		
		tv.getTable().setHeaderVisible(true);
		tv.getTable().setLinesVisible(true);

		TableColumn tcCodIstat = new TableColumn(tv.getTable(),SWT.LEFT,0);
		tcCodIstat.setText("Codice istat");
		tcCodIstat.setWidth(50);

		TableColumn tcComune = new TableColumn(tv.getTable(),SWT.LEFT,1);
		tcComune.setText("Comune");
		tcComune.setWidth(150);

		TableColumn tcProvincia = new TableColumn(tv.getTable(),SWT.LEFT,2);
		tcProvincia.setText("Provincia");
		tcProvincia.setWidth(100);

		TableColumn tcRegione = new TableColumn(tv.getTable(),SWT.LEFT,3);
		tcRegione.setText("Regione");
		tcRegione.setWidth(100);
		
		TableColumn tcCap = new TableColumn(tv.getTable(),SWT.LEFT,4);
		tcCap.setText("Cap");
		tcCap.setWidth(50);
		

		ft.paintBordersFor(f.getBody());
		ft.paintBordersFor(c);
		popup.open();
	}
	
	public void setRisultatiRicerca(ArrayList<ComuniVO> comuni){
		tv.setInput(comuni);		
	}
	
	private void returnValue(ComuniVO returnObj){
		
		if ((callerObj != null) && (setterMethodName != null)){
		
				try {
					Method m = callerObj.getClass().getMethod(setterMethodName, ComuniVO.class);
					m.invoke(callerObj, returnObj);			
					popup.close();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			
		}
	}
	
}
