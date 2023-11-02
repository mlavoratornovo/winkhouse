package winkhouse.dialogs.custom;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import winkhouse.Activator;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.MarkerSpecialParam;



public class ReportParamsCellEditor extends DialogCellEditor {

	private FormToolkit ft = null;
	private Form f = null;
	private Shell s = null;
	private String params = "";
	public final int INTERVALLO = 0;
	public final int LISTA = 1;
	public final int SPECIAL = 2;
	private Text tinizio = null;
	private Text tfine = null;
    private Text tlista = null;
    private Label lSelected = null;
	private int paramType = INTERVALLO;;

	
	public ReportParamsCellEditor() {
	}

	public ReportParamsCellEditor(Composite parent,String params) {
		super(parent);
		this.params = params;
	}


	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		
		ParamEditor pe = new ParamEditor(cellEditorWindow);
		
		if (params.indexOf("..") > -1){
			paramType = INTERVALLO;
			String begin = params.substring(0, params.indexOf(".."));
			tinizio.setText(begin);
			String end = params.substring(params.indexOf("..")+2);
			tfine.setText(end);
		}else if (params.indexOf(",") > -1){
			paramType = LISTA;
			tlista.setText(params);
		}else if (params.startsWith("LOC")){
			paramType = SPECIAL;
			lSelected.setText(params);
		}
		
		pe.open();
		return pe.getReturnValue();
	}
	
	private class ParamEditor{
				
		private ScrolledForm f = null;
		
		private Section sIntervallo = null;
		
		private Section sLista = null;
	    
	    private Section sAltro = null;
	    private ComboViewer c = null;
	    private Shell s = null;
		private Display display = null;
		private String returnValue = null;
		private Button bLista = null;
		private Button bIntervallo = null;
		private Button bSpecial = null;
		
		public ParamEditor(Control c){
			
			
			display = PlatformUI.getWorkbench().getDisplay();
				
			s = new Shell(display,SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL|SWT.RESIZE);
			s.setSize(450, 300);
			
			s.setLayout(new GridLayout());
			s.setBackground(new Color(null,255,255,255));
			
			GridData gd = new GridData();
			gd.verticalAlignment = SWT.FILL;
			gd.horizontalAlignment = SWT.FILL;
			gd.grabExcessHorizontalSpace = true;
			gd.grabExcessVerticalSpace = true;
			
			GridData gd2 = new GridData();
			gd2.verticalAlignment = SWT.FILL;
			gd2.horizontalAlignment = SWT.FILL;
			gd2.grabExcessHorizontalSpace = true;
			
			GridData gdright = new GridData();
			gdright.verticalAlignment = SWT.BOTTOM;
			gdright.horizontalAlignment = SWT.RIGHT;
			gdright.grabExcessHorizontalSpace = true;		
			
			FormToolkit ft = new FormToolkit(PlatformUI.getWorkbench().getDisplay());
			f = ft.createScrolledForm(s);
			
			f.setLayoutData(gd);
			f.getBody().setLayout(new GridLayout());
			
			Composite cRadioChoice = new Composite(f.getBody(),SWT.NONE);
			cRadioChoice.setBackground(new Color(null,255,255,255));
			cRadioChoice.setLayoutData(gd2);		
			GridLayout gl1 = new GridLayout();
			cRadioChoice.setLayout(gl1);
			
			Group g = new Group(cRadioChoice,SWT.SHADOW_IN);
			g.setBackground(new Color(null,255,255,255));
			GridLayout gl3 = new GridLayout();
			gl3.numColumns = 3;
			g.setLayout(gl3);
			g.setLayoutData(gd2);
			
			bIntervallo = new Button(g,SWT.RADIO);
			bIntervallo.setText("Intervallo");
			bIntervallo.setBackground(new Color(null,255,255,255));
			bIntervallo.addMouseListener(new MouseListener(){

				@Override
				public void mouseDoubleClick(MouseEvent e) {}

				@Override
				public void mouseDown(MouseEvent e) {}

				@Override
				public void mouseUp(MouseEvent e) {	
					paramType = INTERVALLO;
					sIntervallo.setEnabled(true);
					sIntervallo.setExpanded(true);
					sLista.setEnabled(false);
					sLista.setExpanded(false);
					sAltro.setEnabled(false);
					sAltro.setExpanded(false);
					f.reflow(true);

				}
				
			});
			
			bLista = new Button(g,SWT.RADIO);

			bLista.setText("Lista");
			bLista.setBackground(new Color(null,255,255,255));
			bLista.addMouseListener(new MouseListener(){

				@Override
				public void mouseDoubleClick(MouseEvent e) {}

				@Override
				public void mouseDown(MouseEvent e) {}

				@Override
				public void mouseUp(MouseEvent e) {
					paramType = LISTA;
					sLista.setEnabled(true);
					sLista.setExpanded(true);
					sIntervallo.setEnabled(false);
					sIntervallo.setExpanded(false);
					sAltro.setEnabled(false);
					sAltro.setExpanded(false);
					f.reflow(true);								
				}
				
			});

			
			bSpecial = new Button(g,SWT.RADIO);
			
			bSpecial.setText("Altro");
			bSpecial.setBackground(new Color(null,255,255,255));
			bSpecial.addMouseListener(new MouseListener(){

				@Override
				public void mouseDoubleClick(MouseEvent e) {}

				@Override
				public void mouseDown(MouseEvent e) {}

				@Override
				public void mouseUp(MouseEvent e) {
					paramType = SPECIAL;
					sAltro.setEnabled(true);
					sAltro.setExpanded(true);
					sIntervallo.setEnabled(false);
					sIntervallo.setExpanded(false);
					sLista.setEnabled(false);
					sLista.setExpanded(false);
					f.reflow(true);								
				}
				
			});

			
			sectionIntervallo();
			sectionLista();
			sectionAltro();
			
			Composite cbuttonBar = new Composite(f.getBody(),SWT.NONE);
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
					if (verifyDataInput()){
						s.close();
					}					
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
					setReturnValue("");
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
		
		public void open() {
//			s.setSize(500, 400);
		//	s.pack();
		    s.open();
			if (paramType == INTERVALLO){
				bIntervallo.setSelection(true);				
				sIntervallo.setEnabled(true);
				sIntervallo.setExpanded(true);
				sLista.setEnabled(false);
				sLista.setExpanded(false);
				sAltro.setEnabled(false);
				sAltro.setExpanded(false);
				f.reflow(true);				
			}else{
				bIntervallo.setSelection(false);
			}
			if (paramType == LISTA){
				bLista.setSelection(true);
				sLista.setEnabled(true);
				sLista.setExpanded(true);
				sIntervallo.setEnabled(false);
				sIntervallo.setExpanded(false);
				sAltro.setEnabled(false);
				sAltro.setExpanded(false);
				f.reflow(true);		
			}else{
				bLista.setSelection(false);
			}
			if (paramType == SPECIAL){
				bSpecial.setSelection(true);
				sAltro.setEnabled(true);
				sAltro.setExpanded(true);
				sIntervallo.setEnabled(false);
				sIntervallo.setExpanded(false);
				sLista.setEnabled(false);
				sLista.setExpanded(false);
				f.reflow(true);								
			}else{
				bSpecial.setSelection(false);
			}
		    while (!s.isDisposed()) {
		    	if (!display.readAndDispatch()) display.sleep();
		    }
		}
		
		private boolean verifyDataInput(){
			boolean okData = true;
			if (paramType == INTERVALLO){
				if (tinizio.getText().equalsIgnoreCase("") && 
					tfine.getText().equalsIgnoreCase("")){
					MessageBox mb = new MessageBox(s,SWT.ERROR);
					mb.setText("Errore inserimento");
					mb.setMessage("Inserire i parametri");			
					mb.open();
					okData = false;
				}else{
					returnValue = tinizio.getText() + ".." + tfine.getText();
				}
			}
			if (paramType == LISTA){
				if (tlista.getText().equalsIgnoreCase("")){
					MessageBox mb = new MessageBox(s,SWT.ERROR);
					mb.setText("Errore inserimento");
					mb.setMessage("Inserire i parametri");			
					mb.open();
					okData = false;
				}else{
					returnValue = tlista.getText();
				}
			}
			if (paramType == SPECIAL){
				if (lSelected.getText().equalsIgnoreCase("")){
					MessageBox mb = new MessageBox(s,SWT.ERROR);
					mb.setText("Errore inserimento");
					mb.setMessage("Inserire i parametri");			
					mb.open();			
					okData = false;
				}else{
					returnValue = lSelected.getText();
				}
			}
			return okData;
		}
		
		private void sectionIntervallo(){
			
			GridData gd = new GridData();
			gd.verticalAlignment = SWT.FILL;
			gd.horizontalAlignment = SWT.FILL;
			gd.grabExcessHorizontalSpace = true;
			gd.grabExcessVerticalSpace = true;

			GridData gdsection = new GridData();
			gdsection.verticalAlignment = SWT.FILL;
			gdsection.horizontalAlignment = SWT.FILL;
			gdsection.grabExcessHorizontalSpace = true;
			gdsection.heightHint= 80;
			
			GridData gdText = new GridData();
			gdText.verticalAlignment = SWT.FILL;
			gdText.horizontalAlignment = SWT.FILL;
			gdText.grabExcessHorizontalSpace = true;		
			
			sIntervallo = new Section(f.getBody(),Section.TITLE_BAR|Section.TWISTIE);
			sIntervallo.setEnabled(false);
			sIntervallo.setText("INTERVALLO : Inserire le posizioni di inizio e fine");
			
			sIntervallo.setLayout(new GridLayout());
			sIntervallo.setLayoutData(gdsection);
			sIntervallo.setBackground(new Color(null,255,255,255));
			
			Composite cIntervallo = new Composite(sIntervallo,SWT.NONE);
			cIntervallo.setBackground(new Color(null,255,255,255));
			GridLayout gdIntervallo = new GridLayout();
			gdIntervallo.numColumns = 2;
			
			cIntervallo.setLayout(gdIntervallo);
			cIntervallo.setLayoutData(gd);
			
			Label linizio = new Label(cIntervallo,SWT.NONE);
			linizio.setText("posizione iniziale");
			Label lfine = new Label(cIntervallo,SWT.NONE);
			lfine.setText("posizione finale");
			
			tinizio = new Text(cIntervallo,SWT.BORDER);
			tinizio.addVerifyListener(new VerifyListener(){

				@Override
				public void verifyText(VerifyEvent e) {
					if (e.keyCode == 127){
						e.doit = true;
					}else if(e.keyCode == 8){
						e.doit = true;
					}else if(e.keyCode == 0){
						e.doit = true;
					}else{
						try {
							Integer.parseInt(String.valueOf(e.character));
						} catch (Exception e1) {
							e.doit = false;
						}
					}
				}
				
			});
			tinizio.setLayoutData(gdText);
			tfine = new Text(cIntervallo,SWT.BORDER);
			tfine.addVerifyListener(new VerifyListener(){

				@Override
				public void verifyText(VerifyEvent e) {
					if (e.keyCode == 127){
						e.doit = true;
					}else if(e.keyCode == 8){
						e.doit = true;
					}else if(e.keyCode == 0){
						e.doit = true;
					}else{
						try {
							Integer.parseInt(String.valueOf(e.character));
						} catch (Exception e1) {
							e.doit = false;
						}
					}
				}
				
			});

			tfine.setLayoutData(gdText);
			
			sIntervallo.setClient(cIntervallo);		
			
		}

		private void sectionLista(){
			
			GridData gd = new GridData();
			gd.verticalAlignment = SWT.FILL;
			gd.horizontalAlignment = SWT.FILL;
			gd.grabExcessHorizontalSpace = true;
			gd.grabExcessVerticalSpace = true;

			GridData gdsection = new GridData();
			gdsection.verticalAlignment = SWT.FILL;
			gdsection.horizontalAlignment = SWT.FILL;
			gdsection.grabExcessHorizontalSpace = true;
			gdsection.heightHint= 80;
			
			GridData gdText = new GridData();
			gdText.verticalAlignment = SWT.FILL;
			gdText.horizontalAlignment = SWT.FILL;
			gdText.grabExcessHorizontalSpace = true;		
			
			sLista = new Section(f.getBody(),Section.TITLE_BAR|Section.TWISTIE);
			sLista.setEnabled(false);
			sLista.setText("LISTA : Inserire le posizioni separate dalla virgola es: 1,2,3 ");
			
			sLista.setLayout(new GridLayout());
			sLista.setLayoutData(gdsection);
			sLista.setBackground(new Color(null,255,255,255));
			
			Composite cIntervallo = new Composite(sLista,SWT.NONE);
			cIntervallo.setBackground(new Color(null,255,255,255));
			GridLayout gdIntervallo = new GridLayout();		
			
			cIntervallo.setLayout(gdIntervallo);
			cIntervallo.setLayoutData(gd);
			
			Label llista = new Label(cIntervallo,SWT.NONE);
			llista.setText("Lista posizioni");
			
			tlista = new Text(cIntervallo,SWT.BORDER);
			tlista.addVerifyListener(new VerifyListener(){

				@Override
				public void verifyText(VerifyEvent e) {
					if (e.keyCode == 127){
						e.doit = true;
					}else if(e.keyCode == 8){
						e.doit = true;
					}else if(e.keyCode == 0){
						e.doit = true;
					}else if(e.character == ','){
						e.doit = true;
					}else{
						try {
							Integer.parseInt(String.valueOf(e.character));
						} catch (Exception e1) {
							e.doit = false;
						}
					}					
				}
				
			});
			tlista.setLayoutData(gdText);
			
			sLista.setClient(cIntervallo);		
			
		}
		
		private void sectionAltro(){
			
			GridData gd = new GridData();
			gd.verticalAlignment = SWT.FILL;
			gd.horizontalAlignment = SWT.FILL;
			gd.grabExcessHorizontalSpace = true;
			gd.grabExcessVerticalSpace = true;

			GridData gdsection = new GridData();
			gdsection.verticalAlignment = SWT.FILL;
			gdsection.horizontalAlignment = SWT.FILL;
			gdsection.grabExcessHorizontalSpace = true;
			gdsection.heightHint= 80;
			
			GridData gdText = new GridData();
			gdText.verticalAlignment = SWT.FILL;
			gdText.horizontalAlignment = SWT.FILL;
			gdText.grabExcessHorizontalSpace = true;		

			GridData gd2H = new GridData();
			gd2H.verticalAlignment = SWT.FILL;
			gd2H.horizontalAlignment = SWT.FILL;
			gd2H.grabExcessHorizontalSpace = true;		
			gd2H.horizontalSpan = 3;
			
			sAltro = new Section(f.getBody(),Section.TITLE_BAR|Section.TWISTIE);
			sAltro.setEnabled(false);
			sAltro.setText("ALTRO : Selezionare le opzioni");
			
			sAltro.setLayout(new GridLayout());
			sAltro.setLayoutData(gdsection);
			sAltro.setBackground(new Color(null,255,255,255));
			
			Composite cIntervallo = new Composite(sAltro,SWT.NONE);
			cIntervallo.setBackground(new Color(null,255,255,255));		
			GridLayout gdIntervallo = new GridLayout();		
			gdIntervallo.numColumns = 3;
		
			cIntervallo.setLayout(gdIntervallo);
			cIntervallo.setLayoutData(gd);
			
			Label llista = new Label(cIntervallo,SWT.NONE);
			llista.setText("Lista opzioni");
			llista.setLayoutData(gd2H);
			
			c = new ComboViewer(cIntervallo,SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED | SWT.BORDER);			
			c.getCombo().setLayoutData(gdText);
			c.setContentProvider(new IStructuredContentProvider(){

				@Override
				public Object[] getElements(Object inputElement) {
					
					return WinkhouseUtils.getInstance()
										   .getMakersSpecialParams()
										   .toArray();
				}

				@Override
				public void dispose() {				
				}

				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				}
				
			});
			
			c.setLabelProvider(new LabelProvider(){

				@Override
				public String getText(Object element) {
					return ((MarkerSpecialParam)element).getDescrizione();
				}
				
			});
			c.setInput(new Object());
			
			ImageHyperlink ihConferma = new ImageHyperlink(cIntervallo, SWT.WRAP);		
			ihConferma.setImage(Activator.getImageDescriptor("/icons/adept_commit.png").createImage());
			ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/adept_commit_hover.png").createImage());
			ihConferma.setBackground(new Color(null,255,255,255));
			ihConferma.addMouseListener(new MouseListener(){

				@Override
				public void mouseUp(MouseEvent e) {
					StructuredSelection ss =(StructuredSelection)c.getSelection();
					MarkerSpecialParam msp = (MarkerSpecialParam)ss.getFirstElement();
					
					if (findSpecialParam(msp.getCodstr())){
						MessageBox mb = new MessageBox(s,SWT.ICON_ERROR);
						mb.setMessage("Parametro presente");
						mb.open();
					}else{
						lSelected.setText(
								(lSelected.getText().equalsIgnoreCase(""))
								?msp.getCodstr()
								:lSelected.getText()+"," + msp.getCodstr()
						);						
					}
					
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}

				@Override
				public void mouseDown(MouseEvent e) {
				}
				
			});

			ImageHyperlink ihCancella = new ImageHyperlink(cIntervallo, SWT.WRAP);		
			ihCancella.setImage(Activator.getImageDescriptor("/icons/button_cancel.png").createImage());
			ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/button_cancel_hover.png").createImage());
			ihCancella.setBackground(new Color(null,255,255,255));
			ihCancella.addMouseListener(new MouseListener(){

				@Override
				public void mouseUp(MouseEvent e) {
					lSelected.setText("");
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}

				@Override
				public void mouseDown(MouseEvent e) {
				}
				
			});	
			Label lseldes = new Label(cIntervallo,SWT.NONE);
			lseldes.setText("selezionati : ");
			lseldes.setLayoutData(gd2H);
			lSelected = new Label(cIntervallo,SWT.NONE);			
			lSelected.setLayoutData(gd2H);
			
			sAltro.setClient(cIntervallo);		
			
		}

		public String getReturnValue() {
			return returnValue;
		}

		public void setReturnValue(String returnValue) {
			this.returnValue = returnValue;
		}
		
		private boolean findSpecialParam(String strcod){
			boolean returnValue = false;
			String[] params = lSelected.getText().split(",");
			for (int i = 0; i < params.length; i++) {
				if (strcod.equalsIgnoreCase(params[i])){
					returnValue = true;
					break;
				}
			}
			return returnValue;
		}
		
	}

}
