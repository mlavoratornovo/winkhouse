package winkhouse.xmldeser.wizard.importer.pages;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.xmldeser.utils.ZipUtils;
import winkhouse.xmldeser.wizard.importer.ImporterWizard;

public class SelettoreOrigineImportazione extends WizardPage {

	private Image apri_cartella = Activator.getImageDescriptor("icons/wizardimport/folder.png").createImage();
	private Button b = null;
	private Button isZipped = null;
	private Label l = null;
	private Text t = null;
		
	public SelettoreOrigineImportazione(String pageName) {
		super(pageName);
	}

	public SelettoreOrigineImportazione(String pageName, String title,ImageDescriptor titleImage) {		
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite arg0) {
		
		setDescription("Seleziona la cartella di origine dati");		
		setTitle(((ImporterWizard)getWizard()).getVersion());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;		
		
		GridData gdFillHVCS3 = new GridData();
		gdFillHVCS3.grabExcessHorizontalSpace = true;
		gdFillHVCS3.grabExcessVerticalSpace = true;
		gdFillHVCS3.verticalAlignment = SWT.FILL;
		gdFillHVCS3.horizontalAlignment = SWT.FILL;		
		gdFillHVCS3.horizontalSpan = 3;

		GridData gdFillHVCS5 = new GridData();
		gdFillHVCS5.grabExcessHorizontalSpace = true;
		gdFillHVCS5.grabExcessVerticalSpace = true;
		gdFillHVCS5.verticalAlignment = SWT.FILL;
		gdFillHVCS5.horizontalAlignment = SWT.FILL;		
		gdFillHVCS5.horizontalSpan = 5;		
		
		Composite content = new Composite(arg0, SWT.NONE);
		content.setLayoutData(gdFillHV);
		
		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;		
		
		GridData gdFillHcs3 = new GridData();
		gdFillHcs3.grabExcessHorizontalSpace = true;
		gdFillHcs3.horizontalAlignment = SWT.FILL;
		gdFillHcs3.horizontalSpan = 3;
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		
		content.setLayout(gl);
	
		isZipped = new Button(content, SWT.CHECK);
		isZipped.setText("Utilizza file zip");
		isZipped.setLayoutData(gdFillHcs3);
		isZipped.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				((ImporterWizard)getWizard()).getImporterVO().setZipped(isZipped.getSelection());
				
				l.setText((isZipped.getSelection())?"File zip di origine dati : ":"Cartella di origine dati : ");	
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

		
		l = new Label(content, SWT.NONE);
		l.setText("Cartella di origine dati : ");
		
		t = new Text(content, SWT.NONE);
		t.setEditable(false);
		t.setLayoutData(gdFillH);
		
		Button b = new Button(content, SWT.FLAT);
		b.setImage(apri_cartella);
		b.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				
				String dir = null;
				
				if (!((ImporterWizard)getWizard()).getImporterVO().isZipped()){
					DirectoryDialog fd = new DirectoryDialog(getWizard().getContainer().getShell());
					fd.setMessage("Seleziona la cartella da dove importare i dati");
			        fd.setText("Seleziona la cartella da dove importare i dati");
			        
			        dir = fd.open();
				}else{
					FileDialog fd = new FileDialog(getWizard().getContainer().getShell());
			        fd.setText("Seleziona il file zip da dove importare i dati");
			        
			        dir = fd.open();
				}
				 
		        if (dir != null) {
		        	t.setText(dir);
		        	((ImporterWizard)getWizard()).getImporterVO()
		        								 .setPathExportFile(dir);
		        	
		        	((ImporterWizard)getWizard()).getImporterVO()
		        								 .setPathUpdated(true);
		        	
		        	getWizard().getContainer().updateButtons();
		        }
				
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
				
		setControl(content);
	}

}
