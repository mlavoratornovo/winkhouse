package winkhouse.view.preference;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.util.WinkhouseUtils;

public class PluginSystemSettingsPage extends PreferencePage {

	private Form form = null;
	private Text pluginPathText = null;

	public PluginSystemSettingsPage() {

	}

	public PluginSystemSettingsPage(String title) {
		super(title);

	}

	public PluginSystemSettingsPage(String title, ImageDescriptor image) {
		super(title, image);

	}

	@Override
	protected Control createContents(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		gridLayout.verticalSpacing = 8;
		gridLayout.horizontalSpacing = 0;

		form.getBody().setLayout(gridLayout);
		
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.minimumHeight = 25;
		
		toolkit.paintBordersFor(form.getBody());
		
		Label imagePathLabel = toolkit.createLabel(form.getBody(), "Percorso estensioni ");
		imagePathLabel.setBackground(imagePathLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Composite cimm = toolkit.createComposite(form.getBody());
		cimm.setBackground(cimm.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		toolkit.paintBordersFor(cimm);
		GridLayout gridLayoutImm = new GridLayout(2, false);
		gridLayoutImm.marginWidth = 1;
		gridLayoutImm.verticalSpacing = 2;
		gridLayoutImm.horizontalSpacing = 4;
		
		GridData gridDataImm = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridDataImm.minimumHeight = 25;
		gridDataImm.heightHint = 25;
		cimm.setLayout(gridLayoutImm);		
		cimm.setLayoutData(gridDataImm);
		pluginPathText = toolkit.createText(cimm, 
				                           (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.PLUGINSPATH).equalsIgnoreCase(""))
				                           ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.PLUGINSPATH)
				                           : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.PLUGINSPATH),
				                           SWT.FLAT);
		pluginPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		pluginPathText.setEnabled(false);
		
		ImageHyperlink ihConferma = toolkit.createImageHyperlink(cimm, SWT.WRAP);
		ihConferma.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		ihConferma.setBackground(ihConferma.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		ihConferma.setImage(Activator.getImageDescriptor("/icons/pathplugins.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/pathplugins-over.png").createImage());
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd = new DirectoryDialog(form.getShell());
				dd.setFilterPath(pluginPathText.getText());

		        dd.setText("Percorso estensioni");

		        dd.setMessage("Seleziona la cartella dove il programma ricerca le estensioni");

		        String dir = dd.open();
		        if (dir != null) {
		        	pluginPathText.setText(dir);
		        }
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		
		return form;
	}

	
	@Override
	protected void performApply() {
		WinkhouseUtils.getInstance().getPreferenceStore().setValue(WinkhouseUtils.PLUGINSPATH,pluginPathText.getText().trim());
		WinkhouseUtils.getInstance().savePreference();
	}

	@Override
	public boolean performCancel() {
		if (pluginPathText != null)
			pluginPathText.setText((WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.PLUGINSPATH).equalsIgnoreCase(""))
        						   ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.PLUGINSPATH)
        						   : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.PLUGINSPATH)
        						  );
		return true;
	}

	@Override
	protected void performDefaults() {
		pluginPathText.setText(WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.PLUGINSPATH));
		
	}

	@Override
	public boolean performOk() {
		try{
			performApply();
			return true;
		}catch(Exception e){
			return false;
		}
	}

	
}
