package winkhouse.dialogs.custom;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;



public class ReportParamsDescriptionCellEditor extends DialogCellEditor {

	private ArrayList<WinkhouseUtils.ObjectSearchGetters> paramsSelected = null;
	private String entita = null;
	private ArrayList<WinkhouseUtils.ObjectSearchGetters> input = null;
	private ArrayList<WinkhouseUtils.ObjectSearchGetters> paramsSelectedSorted = null;
	
	public ReportParamsDescriptionCellEditor() {
	
	}

	public ReportParamsDescriptionCellEditor(Composite parent,
											 ArrayList paramsSelected, 
											 String entita) {
		super(parent);
		this.paramsSelected = paramsSelected;
		this.entita = entita;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		ParamDescriptionEditor pde = new ParamDescriptionEditor(cellEditorWindow);
		pde.open();
		String value = pde.getReturnValue();
		System.out.println(value);
		return value;
	}
	
	private class ParamDescriptionEditor{
		
	    private Shell s = null;
		private Display display = null;
		private CheckboxTableViewer cbtableGS = null;
		private Label lresult = null;
		private String result = "";
		private Table tableGS = null;
		
		public ParamDescriptionEditor(Control c){
			
			display = PlatformUI.getWorkbench().getDisplay();
			
			s = new Shell(display,SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL| SWT.RESIZE);
			s.setSize(450, 300);
			
			s.setLayout(new GridLayout());
			s.setBackground(new Color(null,255,255,255));
			
			GridData gd = new GridData();
			gd.verticalAlignment = SWT.FILL;
			gd.horizontalAlignment = SWT.FILL;
			gd.grabExcessHorizontalSpace = true;
			gd.grabExcessVerticalSpace = true;

			GridData gdH = new GridData();
			gdH.verticalAlignment = SWT.FILL;
			gdH.horizontalAlignment = SWT.FILL;
			gdH.grabExcessHorizontalSpace = true;			

			tableGS = new Table(s, SWT.FILL|SWT.FULL_SELECTION|SWT.CHECK);
			cbtableGS = new CheckboxTableViewer(tableGS);			
			
			cbtableGS.getTable().setLayoutData(gd);
			cbtableGS.getTable().setHeaderVisible(true);
			cbtableGS.getTable().setLinesVisible(true);
			cbtableGS.setContentProvider(new IStructuredContentProvider() {
				
				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				}
				
				@Override
				public void dispose() {
				}
				
				@Override
				public Object[] getElements(Object inputElement) {
					return input.toArray();
				}
			});
			cbtableGS.setLabelProvider(new LabelProvider(){

				@Override
				public String getText(Object element) {					
					return ((WinkhouseUtils.ObjectSearchGetters)element).getDescrizione();
				}
				
			});
			
			cbtableGS.addCheckStateListener(new ICheckStateListener(){
								
				@Override
				public void checkStateChanged(CheckStateChangedEvent event) {
					ObjectSearchGetters osg = (ObjectSearchGetters)event.getElement();
					if (event.getChecked()){
						paramsSelected.add(osg);
					}else{
						Iterator it = paramsSelected.iterator();
						int pos = 0;
						while (it.hasNext()) {
							ObjectSearchGetters osg1 = (ObjectSearchGetters) it.next();
							if (osg1.getKey().intValue() == osg.getKey().intValue()){
								break;
							}			
							pos++;
						}
						paramsSelected.remove(pos);
					}
					
					String value = "";
					Iterator it = paramsSelected.iterator();
					int i = 0;
					while (it.hasNext()) {
						ObjectSearchGetters osg1 = (ObjectSearchGetters) it.next();
						value += (i<paramsSelected.size()-1)
						 		 ? osg1.getDescrizione() + ","
						 		 : osg1.getDescrizione();
						 i++;
					}
					
					result = value;
					lresult.setText(value);					
				}
			});
			//cbtableGS.setLabelProvider();
/*			TableColumn tcCheck = new TableColumn(cbtableGS.getTable(),SWT.CENTER,0);
			tcCheck.setWidth(20);
			tcCheck.setText("");*/		
			
			TableColumn tcDescrizione = new TableColumn(cbtableGS.getTable(),SWT.CENTER,0);
			tcDescrizione.setWidth(200);
			tcDescrizione.setText("Descrizione");
			
			Label lLista = new Label(s, SWT.NONE);
			lLista.setText("Lista campi scelti : ");
			lLista.setBackground(new Color(null, 255, 255, 255));
			lresult = new Label(s, SWT.BORDER);
			lresult.setLayoutData(gdH);
			
			GridData gdright = new GridData();
			gdright.verticalAlignment = SWT.BOTTOM;
			gdright.horizontalAlignment = SWT.RIGHT;
			gdright.grabExcessHorizontalSpace = true;
			
			Composite cbuttonBar = new Composite(s,SWT.NONE);
			cbuttonBar.setBackground(new Color(null,255,255,255));
			GridLayout glbb = new GridLayout();
			glbb.numColumns = 2;
			cbuttonBar.setLayout(glbb);
			cbuttonBar.setLayoutData(gdright);
			
			ImageHyperlink ihConferma = new ImageHyperlink(cbuttonBar, SWT.WRAP);		
			ihConferma.setImage(Activator.getImageDescriptor("/icons/adept_commit.png").createImage());
			ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/adept_commit_hover.png").createImage());
			ihConferma.setBackground(new Color(null,255,255,255));
			ihConferma.addMouseListener(new MouseListener(){

				@Override
				public void mouseUp(MouseEvent e) {
					s.close();										
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}

				@Override
				public void mouseDown(MouseEvent e) {
				}
				
			});

			ImageHyperlink ihCancella = new ImageHyperlink(cbuttonBar, SWT.WRAP);		
			ihCancella.setImage(Activator.getImageDescriptor("/icons/button_cancel.png").createImage());
			ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/button_cancel_hover.png").createImage());
			ihCancella.setBackground(new Color(null,255,255,255));
			ihCancella.addMouseListener(new MouseListener(){

				@Override
				public void mouseUp(MouseEvent e) {
					lresult.setText("");
					result = "";
					s.close();				
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}

				@Override
				public void mouseDown(MouseEvent e) {
				}
				
			});			

			
		}
		
		public String getReturnValue(){
			return result;
		}
		
		public void open(){
			
			s.open();
			input = new ArrayList<WinkhouseUtils.ObjectSearchGetters>();
			if (entita.equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getImmobileReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getAnagraficheReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getColloquiReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.CONTATTI)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getContattiReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.AGENTICOLLOQUIO)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getAgentiColloquioReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.ALLEGATICOLLOQUIO)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getAllegatiColloquioReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHECOLLOQUIO)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getAnagraficheColloquioReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.CRITERIRICERCA)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getCriteriRicercaReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.IMMAGINI)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getImmagineReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.STANZEIMMOBILI)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getStanzeImmobileReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.ALLEGATIIMMOBILE)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getAllegatiImmobileReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.ABBINAMENTI)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getAbbinamentiReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.AFFITTIRATE)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getAffittiRateReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.AFFITTISPESE)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getAffittiSpeseReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.AFFITTIANAGRAFICHE)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getAffittiAnagraficheReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.DATICATASTALI)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getDatiCatastaliImmobileReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.TIPIAPPUNTAMENTI)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getTipiAppuntamentiReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.CLASSIENERGETICHE)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getClassiEnergeticheReportSearchGetters().clone();
			}
			if (entita.equalsIgnoreCase(WinkhouseUtils.CAMPIPERSONALI)){
				input = (ArrayList<WinkhouseUtils.ObjectSearchGetters>)
						WinkhouseUtils.getInstance().getCampiPersonaliReportSearchGetters().clone();
			}
			
			/*
			Iterator<WinkhouseUtils.ObjectSearchGetters> it = input.iterator();
			while (it.hasNext()) {
				WinkhouseUtils.ObjectSearchGetters object = it.next();
				try {
					Class cReturnType = WinkhouseUtils.getInstance()
														.getReturnTypeGetterMethod(Class.forName(entita), object.getMethodName());
					if (cReturnType.getName().equalsIgnoreCase(ArrayList.class.getName())){
						it.remove();
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			*/
			cbtableGS.setInput(input);
//			cbtableGS.setCheckedElements(paramsSelected.toArray());
			
			cbtableGS.setCheckedElements(markSelected(paramsSelected,input).toArray());
			while (!s.isDisposed()) {
			    	if (!display.readAndDispatch()) display.sleep();
			}
		}
		
		public ArrayList<WinkhouseUtils.ObjectSearchGetters> markSelected(ArrayList<WinkhouseUtils.ObjectSearchGetters> selected, 
								 ArrayList<WinkhouseUtils.ObjectSearchGetters> input){
			
			result = "";
			ArrayList<WinkhouseUtils.ObjectSearchGetters> alSelected = new ArrayList<WinkhouseUtils.ObjectSearchGetters>();
			Iterator<WinkhouseUtils.ObjectSearchGetters> itInput = input.iterator();
			while (itInput.hasNext()){
				WinkhouseUtils.ObjectSearchGetters osg = itInput.next();
				Iterator<WinkhouseUtils.ObjectSearchGetters> itSelected = selected.iterator();
				while (itSelected.hasNext()){
					WinkhouseUtils.ObjectSearchGetters osg2 = itSelected.next();
					if (osg.getMethodName().equalsIgnoreCase(osg2.getMethodName())){
						alSelected.add(osg2);
						result += osg2.getDescrizione() + ",";
					}
				}
			}
			if (result.length()>=1){
				result = result.substring(0, result.length()-1);
			}
			lresult.setText(result);
			return alSelected;
			
		}
	}

}
