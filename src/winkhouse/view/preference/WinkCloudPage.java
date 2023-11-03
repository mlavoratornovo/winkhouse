package winkhouse.view.preference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import winkhouse.Activator;
import winkhouse.util.WinkhouseUtils;

public class WinkCloudPage extends PreferencePage {
	
	private Form form = null;
//	private TableViewer turls = null;
//	private ArrayList<String> urlsvalue = new ArrayList<String>();
	private TextCellEditor tceurl = null;
//	private Image addbutton = Activator.getImageDescriptor("icons/filenew.png").createImage();
//	private Image delbutton = Activator.getImageDescriptor("icons/edittrash.png").createImage();
//	private Image winkcloudbutton = Activator.getImageDescriptor("icons/winkclouid.png").createImage();
	private Text winkID = null;
	private Button add = null; 
	
	public WinkCloudPage() {
	}

	public WinkCloudPage(String title) {
		super(title);
	}

	public WinkCloudPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent) {
		
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		gridLayout.verticalSpacing = 8;
		gridLayout.horizontalSpacing = 0;

		form.getBody().setLayout(gridLayout);
		
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.minimumHeight = 25;
		GridData gridDataHV = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		toolkit.paintBordersFor(form.getBody());
		
		Label l_uuid = toolkit.createLabel(form.getBody(), "Porta di ascolto");
		l_uuid.setBackground(l_uuid.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		winkID = toolkit.createText(form.getBody(), WinkhouseUtils.getInstance().getPreferenceStore().getString("winkcloudid"), SWT.FLAT);
		winkID.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		winkID.setText((WinkhouseUtils.getInstance().getPreferenceStore().getString("winkcloudport")!="" && WinkhouseUtils.getInstance().getPreferenceStore().getString("winkcloudport")!=null)
						? WinkhouseUtils.getInstance().getPreferenceStore().getString("winkcloudport")
						: "80");
		Label l_login = toolkit.createLabel(form.getBody(), "Consenti solo utenti registrati");
		l_login.setBackground(l_login.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		add = toolkit.createButton(form.getBody(), null, SWT.CHECK);
		add.setSelection(WinkhouseUtils.getInstance().getPreferenceStore().getBoolean("onlyregisteredusers"));
		
		return form;
	}

	@Override
	protected void performApply() {
		try {
			Integer linsteningPort = Integer.valueOf(winkID.getText());
			if (linsteningPort > 0 && linsteningPort < 65535){
				WinkhouseUtils.getInstance().getPreferenceStore().setValue("winkcloudport", linsteningPort);
			}else{
				MessageDialog.openError(this.getShell(), "Errore valore porta ascolto", "Il valore assegnato alla porta di ascolto deve essere un numero compreso tra 0 e 65535");
			}
			WinkhouseUtils.getInstance().getPreferenceStore().setValue("onlyregisteredusers", add.getSelection());
		} catch (Exception e) {
			MessageDialog.openError(this.getShell(), "Errore valore porta ascolto", "Il valore assegnato alla porta di ascolto deve essere un numero compreso tra 0 e 65535");
		}
			
		try {
			WinkhouseUtils.getInstance().getPreferenceStore().save();
		} catch (IOException e) {			
			MessageDialog.openError(this.getShell(), "Errore nel salvataggio delle preferenze", e.getMessage());
		}
			
	}

}
