package winkhouse.view.preference;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import winkhouse.Activator;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseDBAgentUtils;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.PerspectiveInfo;

public class GeneralSettingPage extends PreferencePage {

	private Form form = null;
	private ComboViewer cbvce = null;
	private Text maxcloudresult = null;
	private Image azzera = Activator.getImageDescriptor("icons/history_clear.png").createImage();
	private Text criptKeyText = null;
	
	public GeneralSettingPage() {
	}

	public GeneralSettingPage(String title) {
		super(title);
	}

	public GeneralSettingPage(String title, ImageDescriptor image) {
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
		
		Label prospettivaLabel = toolkit.createLabel(form.getBody(), "Prospettiva di avvio ");
		prospettivaLabel.setBackground(prospettivaLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		cbvce = new ComboViewer(form.getBody(),SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
		
		cbvce.setLabelProvider(new ILabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener listener) {
				
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
				
			}
			
			@Override
			public String getText(Object element) {
				if (element instanceof WinkhouseUtils.PerspectiveInfo){
					return ((WinkhouseUtils.PerspectiveInfo)element).getDescrizione();
				}
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				if (element instanceof WinkhouseUtils.PerspectiveInfo){
					return ((WinkhouseUtils.PerspectiveInfo)element).getImmagine();
				}
				return null;
			}
		});
		cbvce.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof ArrayList){
					return ((ArrayList)inputElement).toArray();
				}
				return null;
			}
		});
		
		cbvce.setInput(WinkhouseUtils.getInstance().getPerspectiveInfo());
		
		cbvce.setSelection(getCurrentSelection());
		
		Label azzeraCredenziali = toolkit.createLabel(form.getBody(), "Credenziali Google");
		azzeraCredenziali.setBackground(prospettivaLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Button b_azzera = toolkit.createButton(form.getBody(), "", SWT.NONE);
		b_azzera.setToolTipText("Azzera le credenziali Google inserite per la pubblicazione su Calendar e Monitor WinkCloud");
		b_azzera.setImage(azzera);
		b_azzera.addMouseListener(new MouseListener() {

			
			@Override
			public void mouseUp(MouseEvent e) {
				File f = new File(Activator.getDefault().getStateLocation()
														.toFile()
														.toString() + 
														File.separator + 
				           								"gcredentials" + 
														File.separator + 
														"StoredCredential");
				if (f.exists()){
					f.delete();
					MessageDialog.openInformation(form.getBody().getShell(), "Cancellazione credenziali Google", "Cancellazione avvenuta con successo");
				}
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		Label maxNumRisultatiCloud = toolkit.createLabel(form.getBody(), "Massimo numero risultati cloud ");
		maxNumRisultatiCloud.setBackground(prospettivaLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		maxcloudresult = toolkit.createText(form.getBody(), 
										    (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.MAXCLOUDRESULT).equalsIgnoreCase(""))
										    ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.MAXCLOUDRESULT)
										    : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.MAXCLOUDRESULT));
		maxcloudresult.setToolTipText("E' il massimo numero di risultati restituiti per ogni richiesta cloud che arriva ai monitors");
		maxcloudresult.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				
				try {
					
					Integer.valueOf(e.text);
					
				} catch (NumberFormatException e1) {
					
					if (e.text.length() > 0){
						((Text)e.getSource()).setText(((Text)e.getSource()).getText().substring(0,e.text.length()-1));
					}else{
						((Text)e.getSource()).setText(WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.MAXCLOUDRESULT));
					}
					
				}
			}
			
		});
		
		Label l_zipPassword = toolkit.createLabel(form.getBody(), "Password per file zip");
		l_zipPassword.setBackground(l_zipPassword.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		String cryptKey = null;
				
		cryptKey = (WinkhouseUtils.getInstance().getPreferenceStore().getString(IWinkSysProperties.CRIPTKEY)==null ||
					WinkhouseUtils.getInstance().getPreferenceStore().getString(IWinkSysProperties.CRIPTKEY).trim().equalsIgnoreCase(""))
					? ""
					: WinkhouseUtils.getInstance().DecryptStringStandard(WinkhouseUtils.getInstance().getPreferenceStore().getString(IWinkSysProperties.CRIPTKEY));
		
		
		criptKeyText = toolkit.createText(form.getBody(), 
										  cryptKey,
				                          SWT.FLAT|SWT.PASSWORD);
		criptKeyText.setToolTipText(cryptKey);
		criptKeyText.setLayoutData(gridData);

		return form;
	}
	
	protected StructuredSelection getCurrentSelection(){
		StructuredSelection ss = null;
		String persp_id = (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.STARTPERSPECTIVE).equalsIgnoreCase(""))
						   ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.STARTPERSPECTIVE)
                           : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.STARTPERSPECTIVE);
						   
		PerspectiveInfo pi = WinkhouseUtils.getInstance().getPerspectiveByID(persp_id);
		
		ss = new StructuredSelection(pi);
		
		return ss;
	}

	@Override
	protected void performApply() {	
		StructuredSelection s = (StructuredSelection)cbvce.getSelection();
		if (s.getFirstElement() != null){
			WinkhouseUtils.getInstance()
			  			  .getPreferenceStore()
			              .setValue(WinkhouseUtils.STARTPERSPECTIVE,
                                    ((WinkhouseUtils.PerspectiveInfo)s.getFirstElement()).getId());
			
			
		}		  					   
		WinkhouseUtils.getInstance()
		  			  .getPreferenceStore()
		  			  .setValue(WinkhouseUtils.MAXCLOUDRESULT,
		  					    maxcloudresult.getText());

		if (criptKeyText.getText().trim().equalsIgnoreCase("")){
			WinkhouseUtils.getInstance()
			  .getPreferenceStore()
			  .setValue(IWinkSysProperties.CRIPTKEY,
					   "");			
		}else{
			String dummy = WinkhouseDBAgentUtils.getInstance().EncryptStringStandard(criptKeyText.getText().trim());
			WinkhouseUtils.getInstance()
			  			  .getPreferenceStore()
			  			  .setValue(IWinkSysProperties.CRIPTKEY,
			  					   dummy);
		}
		
		WinkhouseUtils.getInstance().savePreference();
	}

	@Override
	public boolean performOk() {
		return super.performOk();
	}
	
}
