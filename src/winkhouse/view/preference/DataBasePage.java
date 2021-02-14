package winkhouse.view.preference;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import winkhouse.db.server.HSQLDBHelper;
import winkhouse.util.WinkhouseUtils;


public class DataBasePage extends PreferencePage {
	
	private Text posizioneDatabaseText = null;
	private Text usernameDatabaseText = null;
	private Text passwordDatabaseText = null;
	private Text imagePathText = null;
	private Text allegatiPathText = null;
	private Text reportTemplateText = null;
	private Button bundleDB = null;
	private Form form = null;	
	
	public DataBasePage() {				
		setPreferenceDefaults();
	}

	public DataBasePage(String title) {
		super(title);	
		setPreferenceDefaults();
	}

	public DataBasePage(String title, ImageDescriptor image) {
		super(title, image);				
		setPreferenceDefaults();
	}
	
	private void setPreferenceDefaults(){
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();							
		} catch (UnknownHostException e) {				
			e.printStackTrace();
		}
		
		WinkhouseUtils.getInstance()
						.getPreferenceStore()
						.setDefault(WinkhouseUtils.POSIZIONEDB, 
								    (addr != null)?addr.getHostAddress():"");

		WinkhouseUtils.getInstance()
						.getPreferenceStore()
						.setDefault(WinkhouseUtils.USERNAME ,"SA");
		WinkhouseUtils.getInstance()
						.getPreferenceStore()
						.setDefault(WinkhouseUtils.PASSWORD ,"");
		WinkhouseUtils.getInstance()
						.getPreferenceStore()
						.setDefault(WinkhouseUtils.IMAGEPATH ,
								    Activator.getDefault()
								   			 .getStateLocation()
								   			 .toFile()
								   			 .toString()+File.separator+"immagini");
		WinkhouseUtils.getInstance()
						.getPreferenceStore()
						.setDefault(WinkhouseUtils.ALLEGATIPATH ,
								    Activator.getDefault()
								   			 .getStateLocation()
								   			 .toFile()
								   			 .toString()+File.separator+"allegati");
		WinkhouseUtils.getInstance()
						.getPreferenceStore()
						.setDefault(WinkhouseUtils.REPORTTEMPLATEPATH ,
								    Activator.getDefault()
								   			 .getStateLocation()
								   			 .toFile()
								   			 .toString()+File.separator+"template");					
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

		Label origineDBLabel = toolkit.createLabel(form.getBody(), "Origine dati ");
		origineDBLabel.setBackground(origineDBLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		bundleDB = toolkit.createButton(form.getBody(), "Utilizza base dati autonoma", SWT.CHECK);
		bundleDB.setBackground(origineDBLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		bundleDB.setSelection(WinkhouseUtils.getInstance().getPreferenceStore().getBoolean(WinkhouseUtils.BUNDLEDB));
		bundleDB.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (((Button)e.getSource()).getSelection() == true){
					posizioneDatabaseText.setEnabled(false);
				}else{
					posizioneDatabaseText.setEnabled(true);
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		Label posizioneDatabaseLabel = toolkit.createLabel(form.getBody(), "Posizione dati ");
		posizioneDatabaseLabel.setBackground(posizioneDatabaseLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		posizioneDatabaseText = toolkit.createText(form.getBody(), 
		                                           (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.POSIZIONEDB).equalsIgnoreCase(""))
				                                    ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.POSIZIONEDB)
				                                    : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.POSIZIONEDB),
				                                   SWT.FLAT);
		posizioneDatabaseText.setLayoutData(gridData);
		if (WinkhouseUtils.getInstance().getPreferenceStore().getBoolean(WinkhouseUtils.BUNDLEDB)){
			posizioneDatabaseText.setEnabled(false);
		}
	/*	
		Label usernameDatabaseLabel = toolkit.createLabel(form.getBody(), "Nome utente ");
		usernameDatabaseLabel.setBackground(usernameDatabaseLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		usernameDatabaseText = toolkit.createText(form.getBody(), 
				                                  (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.USERNAME).equalsIgnoreCase(""))
				                                   ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.USERNAME)
				                                   : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.USERNAME),
				                                  SWT.FLAT);
		usernameDatabaseText.setLayoutData(gridData);
		
		Label passwordDatabaseLabel = toolkit.createLabel(form.getBody(), "Password ");
		passwordDatabaseLabel.setBackground(passwordDatabaseLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		passwordDatabaseText = toolkit.createText(form.getBody(), 
				                                  (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.PASSWORD).equalsIgnoreCase(""))
				                                   ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.PASSWORD)
				                                   : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.PASSWORD),
				                                  SWT.FLAT);
		passwordDatabaseText.setLayoutData(gridData);
*/
		Label imagePathLabel = toolkit.createLabel(form.getBody(), "Percorso immagini ");
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
		imagePathText = toolkit.createText(cimm, 
				                           (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.IMAGEPATH).equalsIgnoreCase(""))
				                           ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.IMAGEPATH)
				                           : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.IMAGEPATH),
				                           SWT.FLAT);
		imagePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		imagePathText.setEnabled(false);
		
		ImageHyperlink ihConferma = toolkit.createImageHyperlink(cimm, SWT.WRAP);
		ihConferma.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		ihConferma.setBackground(ihConferma.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		ihConferma.setImage(Activator.getImageDescriptor("/icons/pathimmagini.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/pathimmagini_over.png").createImage());
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd = new DirectoryDialog(form.getShell());
				dd.setFilterPath(imagePathText.getText());

		        dd.setText("Percorso immagini");

		        dd.setMessage("Seleziona la cartella dove il programma copia le immagini");

		        String dir = dd.open();
		        if (dir != null) {
		        	imagePathText.setText(dir);
		        }
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		Label allegatiPathLabel = toolkit.createLabel(form.getBody(), "Percorso allegati ");
		allegatiPathLabel.setBackground(allegatiPathLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Composite ca = toolkit.createComposite(form.getBody());
		toolkit.paintBordersFor(ca);
		ca.setBackground(ca.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridLayout gridLayoutAll = new GridLayout(2, false);
		gridLayoutAll.marginWidth = 1;
		gridLayoutAll.verticalSpacing = 4;
		gridLayoutAll.horizontalSpacing = 4;
		
		GridData gridDataAll = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridDataAll.minimumHeight = 25;
		gridDataAll.heightHint = 25;
		ca.setLayout(gridLayoutAll);		
		ca.setLayoutData(gridDataAll);
		allegatiPathText = toolkit.createText(ca, 
				                           (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.ALLEGATIPATH).equalsIgnoreCase(""))
				                           ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.ALLEGATIPATH)
				                           : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.ALLEGATIPATH),
				                           SWT.FLAT);
		allegatiPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		allegatiPathText.setEnabled(false);
		
		ImageHyperlink ihConfermaAl = toolkit.createImageHyperlink(ca, SWT.WRAP);	
		ihConfermaAl.setBackground(ihConfermaAl.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		ihConfermaAl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		ihConfermaAl.setImage(Activator.getImageDescriptor("/icons/pathallegati.png").createImage());
		ihConfermaAl.setHoverImage(Activator.getImageDescriptor("/icons/pathallegati_over.png").createImage());
		ihConfermaAl.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd = new DirectoryDialog(form.getShell());
				dd.setFilterPath(allegatiPathText.getText());

		        dd.setText("Percorso allegati");

		        dd.setMessage("Seleziona la cartella dove il programma copia gli allegati dei colloqui");

		        String dir = dd.open();
		        if (dir != null) {
		        	allegatiPathText.setText(dir);
		        }
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		
		Label reporttemplatePathLabel = toolkit.createLabel(form.getBody(), "Percorso template report ");
		reporttemplatePathLabel.setBackground(reporttemplatePathLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Composite ctr = toolkit.createComposite(form.getBody());
		toolkit.paintBordersFor(ctr);
		ctr.setBackground(ctr.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridLayout gridLayoutTR = new GridLayout(2, false);
		gridLayoutTR.marginWidth = 1;
		gridLayoutTR.verticalSpacing = 4;
		gridLayoutTR.horizontalSpacing = 4;		
		
		GridData gridDataTR = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridDataTR.minimumHeight = 25;
		gridDataTR.heightHint = 25;
		ctr.setLayout(gridLayoutTR);		
		ctr.setLayoutData(gridDataTR);
		reportTemplateText = toolkit.createText(ctr, 
				                           (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.REPORTTEMPLATEPATH).equalsIgnoreCase(""))
				                           ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.REPORTTEMPLATEPATH)
				                           : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.REPORTTEMPLATEPATH),
				                           SWT.FLAT);
		reportTemplateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		reportTemplateText.setEnabled(false);
		
		ImageHyperlink ihConfermaTR = toolkit.createImageHyperlink(ctr, SWT.WRAP);
		ihConfermaTR.setBackground(ihConfermaTR.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		ihConfermaTR.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		ihConfermaTR.setImage(Activator.getImageDescriptor("/icons/fileopen.png").createImage());
		ihConfermaTR.setHoverImage(Activator.getImageDescriptor("/icons/fileopen_over.png").createImage());
		ihConfermaTR.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd = new DirectoryDialog(form.getShell());
				dd.setFilterPath(allegatiPathText.getText());

		        dd.setText("Percorso template report");

		        dd.setMessage("Seleziona la cartella dove il programma copia i template dei report");

		        String dir = dd.open();
		        if (dir != null) {
		        	reportTemplateText.setText(dir);
		        }
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		Label passwordDatabase = toolkit.createLabel(form.getBody(), "Password accesso base dati");
		passwordDatabase.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		String cryptPws = WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.USERDBPWD);
		if (!cryptPws.equalsIgnoreCase("")){
			cryptPws = WinkhouseUtils.getInstance().DecryptStringStandard(cryptPws);
		}

		passwordDatabaseText = toolkit.createText(form.getBody(), "", SWT.PASSWORD);
		passwordDatabaseText.setText(cryptPws);

		
		return form;
	}

	@Override
	protected void performApply() {
		
		boolean oldbundlevalue = WinkhouseUtils.getInstance().getPreferenceStore().getBoolean(WinkhouseUtils.BUNDLEDB);

		WinkhouseUtils.getInstance().getPreferenceStore().setValue(WinkhouseUtils.POSIZIONEDB,posizioneDatabaseText.getText().trim());
		WinkhouseUtils.getInstance().getPreferenceStore().setValue(WinkhouseUtils.IMAGEPATH,imagePathText.getText().trim());
		WinkhouseUtils.getInstance().getPreferenceStore().setValue(WinkhouseUtils.ALLEGATIPATH,allegatiPathText.getText().trim());
		WinkhouseUtils.getInstance().getPreferenceStore().setValue(WinkhouseUtils.REPORTTEMPLATEPATH,reportTemplateText.getText().trim());
		WinkhouseUtils.getInstance().getPreferenceStore().setValue(WinkhouseUtils.BUNDLEDB,bundleDB.getSelection());
		
		String encPws = WinkhouseUtils.getInstance().EncryptStringStandard(passwordDatabaseText.getText());
		
		WinkhouseUtils.getInstance()
		 			  .getPreferenceStore()
		 			  .setValue(WinkhouseUtils.USERDBPWD,encPws);

		WinkhouseUtils.getInstance().savePreference();
		
		
		if (oldbundlevalue != bundleDB.getSelection()){
			if (bundleDB.getSelection()){
				if (MessageDialog.openConfirm(getShell(), "Riavvio winkhouse", "La voce \"Utilizza base dati autonoma\" è stata selezionata.\n"
						 												 + "Se nel pc è attivo il programma winkhouseDBAgent, chiuderlo tramite File -> Exit."
						 												 + "Per rendere effettivo il cambiamento della base dati \n"
						 												 + "è necessario riavviare il programma, RIAVVIARE ORA ?")){
					HSQLDBHelper.getInstance().chiudiDatabase();
					PlatformUI.getWorkbench().restart();	 													 
						 												 }
				
			}else{
				if (MessageDialog.openConfirm(getShell(), "Riavvio winkhouse", "La voce \"Utilizza base dati autonoma\" è stata cambiata, \n"
																	 + " per rendere effettivo il cambiamento della base dati \n"
																	 + "è necessario riavviare il programma, RIAVVIARE ORA ?")){
					HSQLDBHelper.getInstance().chiudiDatabase();
					PlatformUI.getWorkbench().restart();
				}
			}
		}

	}

	@Override
	public boolean performCancel() {
		
		if (posizioneDatabaseText != null)
		posizioneDatabaseText.setText((WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.POSIZIONEDB).equalsIgnoreCase(""))
        						       ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.POSIZIONEDB)
        						       : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.POSIZIONEDB)
        						     );
/*		if (usernameDatabaseText != null)
		usernameDatabaseText.setText((WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.USERNAME).equalsIgnoreCase(""))
			      					  ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.USERNAME)
			                          : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.USERNAME)
			                        );
		if (passwordDatabaseText != null)
		passwordDatabaseText.setText((WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.PASSWORD).equalsIgnoreCase(""))
			      				      ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.PASSWORD)
			                          : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.PASSWORD)
			                        );*/
		if (imagePathText != null)
		imagePathText.setText((WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.IMAGEPATH).equalsIgnoreCase(""))
			      					  ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.IMAGEPATH)
			      					  : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.IMAGEPATH)
              						);

		if (allegatiPathText != null)
			allegatiPathText.setText((WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.ALLEGATIPATH).equalsIgnoreCase(""))
				      					  ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.ALLEGATIPATH)
				      					  : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.ALLEGATIPATH)
	              						);
		
		return true;
	}

	@Override
	protected void performDefaults() {
		posizioneDatabaseText.setText(WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.POSIZIONEDB));
/*        usernameDatabaseText.setText(WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.USERNAME));
        passwordDatabaseText.setText(WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.PASSWORD));*/
        imagePathText.setText(WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.IMAGEPATH));
        allegatiPathText.setText(WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.ALLEGATIPATH));
        passwordDatabaseText.setText(WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.USERDBPWD));
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
