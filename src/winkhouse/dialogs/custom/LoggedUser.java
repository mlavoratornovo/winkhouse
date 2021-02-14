package winkhouse.dialogs.custom;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import winkhouse.vo.AgentiVO;

public class LoggedUser extends ControlContribution {
	
	private Label l_logged_user = null;
	public final static String ID = "LOGUSER";
	
	public LoggedUser(){
		super(ID);
	} 
	
	@Override
	protected Control createControl(Composite parent) {
		
		l_logged_user = new Label(parent, SWT.FLAT);	
		l_logged_user.setForeground(new Color(null, 50, 187, 7));
		l_logged_user.setAlignment(SWT.CENTER);
		l_logged_user.setFont(new Font(null, new FontData("Ariel", 12, SWT.BOLD)));		
		l_logged_user.setText("                                                                                       ");
        
        
		return l_logged_user;
	}
	
	public void setAgenteVO(AgentiVO agente){
		l_logged_user.setText("Benvenuto agente : " + agente.getCognome() + " " + agente.getNome());
	}


}
