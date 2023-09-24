package winkhouse.wizard.colloqui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
//import org.eclipse.core.databinding.beans.PojoObservables;
//import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.widgets.calendarcombo.CalendarCombo;
import org.eclipse.nebula.widgets.calendarcombo.ICalendarListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.AgentiVO;
import winkhouse.wizard.ColloquiWizard;



public class DatiComuniColloquio extends WizardPage {

	private Composite container = null;
	private CalendarCombo dcdataincontro = null;	
	private Text oraincontro = null;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");	
	private SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private ComboViewer cvagenteinseritore = null;
	private Text tDescrizione = null; 
	private Text tLuogo = null;	
	
	public DatiComuniColloquio(String pageName) {
		super(pageName);

	}

	public DatiComuniColloquio(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);

	}
	
	private Comparator<AgentiVO> comparatorAgenti = new Comparator<AgentiVO>(){

		@Override
		public int compare(AgentiVO arg0,AgentiVO arg1) {
			if (arg0.getCodAgente().intValue() == arg1.getCodAgente().intValue()){
				return 0;
			}else if (arg0.getCodAgente().intValue() > arg1.getCodAgente().intValue()){
				return 1;
			}else{
				return -1;
			}				
		}
		
	};
	
	@Override
	public void createControl(Composite parent) {
		
		setTitle(getName());				
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 2;
				
		container = new Composite(parent,SWT.NONE);
		container.setLayout(gl2);
		container.setLayoutData(gdFillHV);				
		
		/*		
		TipologieColloquiDAO tcDAO = new TipologieColloquiDAO();
		
		if (((ColloquiWizard)getWizard()).getColloquio().getTipologiaColloquio().getCodTipologiaColloquio() != 
			tcDAO.list().get(0).getCodTipologiaColloquio()){
*/		
		Label labelDataIncontro = new Label(container,SWT.NONE);
		labelDataIncontro.setText("Data e Ora incontro");
		
		Label labelAgenteInseritore = new Label(container,SWT.NONE);
		labelAgenteInseritore.setText("Agente inseritore");
		
		Composite cDataIncontro = new Composite(container,SWT.NONE);
		GridLayout glDataIncontro = new GridLayout();
		glDataIncontro.numColumns = 3;
		cDataIncontro.setLayout(glDataIncontro);
		dcdataincontro = new CalendarCombo(cDataIncontro,SWT.READ_ONLY|SWT.DOUBLE_BUFFERED);		
		GridData dfdcdata = new GridData();
		dfdcdata.minimumWidth = 200;
		dfdcdata.widthHint = 200;		
		dcdataincontro.setLayoutData(dfdcdata);
		dcdataincontro.addCalendarListener(new ICalendarListener() {
			
			@Override
			public void popupClosed() {}
			
			@Override
			public void dateRangeChanged(Calendar arg0, Calendar arg1) {}
			
			@Override
			public void dateChanged(Calendar arg0) {
				try {
					Date tmp = formatterDateTime.parse(formatter.format(arg0.getTime()) + " " + oraincontro.getText());
					((ColloquiWizard)getWizard()).getColloquio().setDataColloquio(tmp);					
				} catch (ParseException e) {
					((ColloquiWizard)getWizard()).getColloquio().setDataColloquio(new Date());
				}
				
			}
		});

		oraincontro = new Text(cDataIncontro,SWT.BORDER);
		oraincontro.setText(formatterTime.format(new Date()));
		oraincontro.setTextLimit(5);
		oraincontro.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
		
				
				try {
//					System.out.println(formatterDateTime.parse(dcdataincontro.getDateAsString() + " " + oraincontro.getText()));
					Date tmp = formatterDateTime.parse(dcdataincontro.getDateAsString() + " " + oraincontro.getText());
	//				System.out.println(tmp.toString());
					((ColloquiWizard)getWizard()).getColloquio().setDataColloquio(tmp);
				} catch (ParseException e1) {					
					((ColloquiWizard)getWizard()).getColloquio().setDataColloquio(new Date());
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		
		oraincontro.addVerifyListener(new VerifyListener(){

			@Override
			public void verifyText(VerifyEvent e) {
				
					System.out.println(e.keyCode);
					if (e.keyCode == 0){
						e.doit = true;
					}else if (e.keyCode == 127){
						e.doit = true;
					}else if(e.keyCode == 8){
						e.doit = true;
					}else{
						if(e.start == 0){
							try {
								int uno = Integer.valueOf(String.valueOf(e.character)).intValue();
								if (uno > 2){
									e.doit = false;
								}else{
									e.doit = true;
								}
							} catch (NumberFormatException e1) {							
								e.doit = false;
							}
						}
						if(e.start == 1){
							try {
								int due = Integer.valueOf(String.valueOf(e.character)).intValue();
								e.doit = true;
							} catch (NumberFormatException e1) {
								e.doit = false;
							}
						}
						
						if(e.start == 2){
							if (e.character != ':'){
								e.doit = false;
							}else{
								e.doit = true;
							}
						}
						if(e.start == 3){
							try {
								int tre = Integer.valueOf(String.valueOf(e.character)).intValue();
								if (tre > 5){							
									e.doit = false;
								}else{
									e.doit = true;
								}
							} catch (NumberFormatException e1) {
								oraincontro.setText(oraincontro.getText(0, 2) + "0");
								e.doit = false;
							}
						}
						if(e.start == 4){
							try {
								int quattro = Integer.valueOf(String.valueOf(e.character)).intValue();
								e.doit = true;
							} catch (NumberFormatException e1) {
								oraincontro.setText(oraincontro.getText(0, 3) + "0");
								e.doit = false;
							}
						}
					}
					
				}
				
		});
		
		if (((ColloquiWizard)getWizard()).getColloquio().getDataColloquio() == null){
			((ColloquiWizard)getWizard()).getColloquio().setDataColloquio(new Date());
		}
		
		dcdataincontro.setText(formatter.format(
				((ColloquiWizard)getWizard()).getColloquio().getDataColloquio()
												)
							   );


		if (((ColloquiWizard)getWizard()).getColloquio().getDataColloquio() != null){
			oraincontro.setText(formatterTime.format(
					((ColloquiWizard)getWizard()).getColloquio().getDataColloquio()
													)
								   );
		}
		
		
		GridData gdExpH = new GridData();
		gdExpH.grabExcessHorizontalSpace = true;		
		gdExpH.horizontalAlignment = SWT.FILL;

		cvagenteinseritore = new ComboViewer(container,SWT.BORDER);
		cvagenteinseritore.getCombo().setLayoutData(gdExpH);

		cvagenteinseritore.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				
				return MobiliaDatiBaseCache.getInstance().getAgenti().toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {				
			}
			
		});
		
		cvagenteinseritore.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				return ((AgentiVO)element).getCognome() + " " + ((AgentiVO)element).getNome();
			}
			
		});
		
		cvagenteinseritore.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				((ColloquiWizard)getWizard()).getColloquio().setAgenteInseritore((AgentiVO)((StructuredSelection)event.getSelection()).getFirstElement());
				((ColloquiWizard)getWizard()).getContainer().updateButtons();
			}
			
		});
						
		cvagenteinseritore.setInput(new Object());
		
		if (((ColloquiWizard)getWizard()).getColloquio().getAgenteInseritore() != null){
			int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getAgenti(), ((ColloquiWizard)getWizard()).getColloquio().getAgenteInseritore(), comparatorAgenti);
			Object[] sel = new Object[1];
			sel[0] = MobiliaDatiBaseCache.getInstance().getAgenti().get(index);
			StructuredSelection ss = new StructuredSelection(sel);
			cvagenteinseritore.setSelection(ss);
		}

		GridData gd2 = new GridData();
		gd2.horizontalSpan = 2;
		gd2.grabExcessHorizontalSpace = true;
		gd2.horizontalAlignment = SWT.FILL;
				
		Label labelDescrizione = new Label(container,SWT.NONE);
		labelDescrizione.setText("Descrizione");
		labelDescrizione.setLayoutData(gd2);
		
		GridData gdDescrizione = new GridData();
		gdDescrizione.horizontalSpan = 2;
		gdDescrizione.grabExcessHorizontalSpace = true;
		gdDescrizione.grabExcessVerticalSpace = true;
		gdDescrizione.verticalAlignment = SWT.FILL;
		gdDescrizione.horizontalAlignment = SWT.FILL;
		gdDescrizione.minimumHeight = 50;
		
		tDescrizione = new Text(container,SWT.MULTI);
		tDescrizione.setLayoutData(gdDescrizione);
		
		Label labelLuogo = new Label(container,SWT.NONE);
		labelLuogo.setText("Luogo");
		labelLuogo.setLayoutData(gd2);
		
		tLuogo = new Text(container,SWT.MULTI);
		tLuogo.setLayoutData(gd2);
		
		DataBindingContext bindingContext = new DataBindingContext();
		bindDatiComuni(bindingContext);

		setControl(container);
	}
	
	private void bindDatiComuni(DataBindingContext bindingContext){
		
		if (((ColloquiWizard)getWizard()).getColloquio() != null){
			
			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tDescrizione), 
									 PojoProperties.value("descrizione").observe(((ColloquiWizard)getWizard()).getColloquio()),
									 null, 
									 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tLuogo), 
									 PojoProperties.value("luogoIncontro").observe(((ColloquiWizard)getWizard()).getColloquio()),
									 null, 
									 null);
			
		}
	}	

}

