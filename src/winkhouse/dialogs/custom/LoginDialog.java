package winkhouse.dialogs.custom;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import winkhouse.Activator;
import winkhouse.dao.AgentiDAO;
import winkhouse.model.AgentiModel;
import winkhouse.model.ContattiModel;
import winkhouse.util.WinkhouseUtils;

public class LoginDialog {

	private Form f = null;
	private FormToolkit ft = null;
	private GridData gd = null;
	private GridData gd_h = null;
	private GridLayout gl_info = null;
	private GridData gd_h_r = null;
	
	private Text txt_username = null;	
	private Text txt_password = null;

	private TableViewer tvIndirizziCal = null;
	
	private Shell s = null;
	private ContattiModel contatto = null;
	
	private Display d = null;
	Image sfondo = Activator.getImageDescriptor("icons/logo.png").createImage();
	
	public LoginDialog(Display d) {
		this.d = d;
	}
	

	public void createDialog(){
		
		GridLayout gl = new GridLayout();
		
		s = new Shell(d,SWT.TITLE|SWT.ON_TOP|SWT.NO_TRIM);
		ft = new FormToolkit(d);
		s.setMinimumSize(220, 150);
		s.setSize(220, 150);
		s.setLayout(gl);
		
		s.setBackground(new Color(null,255,255,255));
		//s.setImage(Activator.getImageDescriptor("icons/icowin/home16.png").createImage());
		s.setText("Accesso a winkhouse ");
		
		Monitor primaryMonitor = d.getPrimaryMonitor ();
		Rectangle bounds = primaryMonitor.getBounds ();
		Rectangle rect = s.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2 ;
		int y = bounds.y + (bounds.height - rect.height) / 2 ;
		s.setLocation (x, y);		
		
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		s.setLayoutData(gd);
		
		gd_h = new GridData();
		gd_h.horizontalAlignment = SWT.FILL;
		gd_h.grabExcessHorizontalSpace = true;
		
		gd_h_r = new GridData();
		gd_h_r.horizontalAlignment = SWT.RIGHT;
		gd_h_r.verticalAlignment = SWT.BOTTOM;
		gd_h_r.grabExcessVerticalSpace = false;
		gd_h_r.grabExcessHorizontalSpace = true;
		
		
		gl_info = new GridLayout(1, false);
		
		
		f = ft.createForm(s);
		f.setLayoutData(gd);
		f.getBody().setLayout(gl);
		f.getBody().setLayoutData(gd);
		f.getBody().setBackgroundImage(sfondo);
		Label l_username = ft.createLabel(f.getBody(), "Nome utente");
		txt_username = ft.createText(f.getBody(), "");
		txt_username.setLayoutData(gd_h);
		
		Label l_password = ft.createLabel(f.getBody(), "Password");
		txt_password = ft.createText(f.getBody(), "", SWT.PASSWORD);
		txt_password.setLayoutData(gd_h);
		
		s.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				s.setFocus();
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Composite c_buttons = ft.createComposite(f.getBody());
		c_buttons.setLayout(new GridLayout(2,false));
		c_buttons.setLayoutData(gd_h_r);
		
		Button btn_ok = ft.createButton(c_buttons, "OK", SWT.FLAT);
		btn_ok.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				String dec_pws = WinkhouseUtils.getInstance().EncryptStringStandard(txt_password.getText());
				String username = txt_username.getText();
				AgentiModel aVO = new AgentiModel();
				aVO.setUsername(username);
				aVO.setPassword(dec_pws);
				AgentiDAO aDAO = new AgentiDAO();
				aVO = (AgentiModel)aDAO.loginAgente(AgentiModel.class.getName(), aVO, null); 
				if (aVO != null){
					WinkhouseUtils.getInstance().setLoggedAgent(aVO);
					s.close();
				}else{
					MessageDialog.openError(s, "Verifica username password", "Coppia Username e Password non presente in archivio");
				}
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		Button btn_chiudi = ft.createButton(c_buttons, "Cancel", SWT.FLAT);
		btn_chiudi.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				System.exit(0);
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		ft.paintBordersFor(f.getBody());
		s.open ();

		while(!s.isDisposed()){
			if (!d.readAndDispatch()){
				d.sleep();
			}
		}
	}
		
	
}
