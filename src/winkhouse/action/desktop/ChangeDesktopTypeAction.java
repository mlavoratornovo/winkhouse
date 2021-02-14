package winkhouse.action.desktop;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.desktop.DesktopView;

public class ChangeDesktopTypeAction extends Action {

	public ChangeDesktopTypeAction() {
		// TODO Auto-generated constructor stub
	}

	public ChangeDesktopTypeAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ChangeDesktopTypeAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public ChangeDesktopTypeAction(String text, int style) {
		super(text, style);
		setImageDescriptor(Activator.getImageDescriptor("icons/view_tree.png"));
		setToolTipText("Attiva la modalità struttura");
	}

	
	@Override
	public void run() {
		DesktopView dv = (DesktopView)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
															   .getActivePage()
															   .getActivePart();
		if (isChecked()){
			dv.setDesktop_type(DesktopView.STRUTTURA_TYPE);
			dv.getDesktop().setInput(new ArrayList());
			setImageDescriptor(Activator.getImageDescriptor("icons/postit16.png"));
			setToolTipText("Attiva la modalità promemoria");
			dv.getDpa().setToolTipText("Cancella elemento dalla scrivania, non nell'archivio");
			dv.getSelettoreStructureLevel().getcLayout().setEnabled(true);
			dv.getAgenti().getCombo().setEnabled(false);
			dv.getApa().setToolTipText("Seleziona un elemento con la ricerca");
		}else{
			dv.setDesktop_type(DesktopView.PROMEMORIA_TYPE);
			dv.setAgente(WinkhouseUtils.getInstance().getLoggedAgent());
			setImageDescriptor(Activator.getImageDescriptor("icons/view_tree.png"));
			setToolTipText("Attiva la modalità struttura");
			dv.getDpa().setToolTipText("Cancella promemoria dall'archivio");
			dv.getSelettoreStructureLevel().getcLayout().setEnabled(false);
			dv.getAgenti().getCombo().setEnabled(true);
			dv.getApa().setToolTipText("Aggiungi promemoria");
		}
	}

}
