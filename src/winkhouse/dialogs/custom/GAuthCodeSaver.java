package winkhouse.dialogs.custom;


import java.io.IOException;
import java.security.GeneralSecurityException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import winkhouse.Activator;
import winkhouse.helper.GoogleCalendarV3Helper;

public class GAuthCodeSaver {
	
	private String idCode = null;
	private Text tcodice = null;
	
	public GAuthCodeSaver(String idcode) {
		this.idCode = idcode;
		createDialog();
	}
	class MouseListenerSave implements MouseListener{
		
		private String idCode;
		private Shell s = null;
		public MouseListenerSave(String idcode, Shell s){
			this.idCode = idcode;
			this.s = s;
		}
		@Override
		public void mouseDoubleClick(MouseEvent e) {
		}

		@Override
		public void mouseDown(MouseEvent e) {
		}

		@Override
		public void mouseUp(MouseEvent e) {
			GoogleCalendarV3Helper h;
			try {
				h = new GoogleCalendarV3Helper();
				h.authorizeToken(tcodice.getText(), this.idCode);
				s.close();
			} catch (GeneralSecurityException e1) {
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
										"Errore salvataggio codice", e1.getMessage());
			} catch (IOException e1) {
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"Errore salvataggio codice", e1.getMessage());
			}
			

		}
		
	}
	
	public void createDialog(){
		GridLayout gl = new GridLayout();
		Shell s = new Shell(PlatformUI.createDisplay(),SWT.CLOSE|SWT.TITLE|SWT.APPLICATION_MODAL);
		FormToolkit ft = new FormToolkit(Activator.getDefault()
												  .getWorkbench()
												  .getDisplay());
		s.setMinimumSize(320, 150);
		s.setSize(320, 200);
		s.setLayout(gl);
		s.setBackground(new Color(null,255,255,255));
		s.setImage(Activator.getImageDescriptor("icons/icowin/home16.png").createImage());
		s.setText("Codice di autorizzazione Google");
		
		Form f = ft.createForm(s);
		
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;

		f.setLayoutData(gd);
		f.getBody().setLayoutData(gd);
		f.getBody().setLayout(gl);
		
		
		Label lcodice = ft.createLabel(f.getBody(), "Codice Google : ");
		//lcodice.setBackground(new Color(null,100,100,100));
		lcodice.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		tcodice = ft.createText(f.getBody(), "", SWT.FLAT);
		
		tcodice.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Button b = ft.createButton(f.getBody(), "Salva", SWT.FLAT);
		b.addMouseListener(new MouseListenerSave(String.valueOf(this.idCode), s));
		ft.paintBordersFor(f.getBody());
		
		s.open();
		
	}


}