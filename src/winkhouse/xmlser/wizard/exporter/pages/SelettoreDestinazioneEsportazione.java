package winkhouse.xmlser.wizard.exporter.pages;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.xmlser.helper.XMLExportHelper;
import winkhouse.xmlser.utils.ZipUtils;
import winkhouse.xmlser.wizard.exporter.ExporterWizard;

public class SelettoreDestinazioneEsportazione extends WizardPage {

	private Image apri_cartella = Activator.getImageDescriptor("icons/wizardexport/folder.png").createImage();
	private Image connesso = Activator.getImageDescriptor("icons/wizardexport/connect_established.png").createImage();
	private Image sconnesso = Activator.getImageDescriptor("icons/wizardexport/connect_no.png").createImage();
	private Text t = null;
	private Button b = null;
	private Label l = null;
	
	public SelettoreDestinazioneEsportazione(String pageName) {
		super(pageName);
	}

	public SelettoreDestinazioneEsportazione(String pageName, String title,ImageDescriptor titleImage) {		
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite arg0) {
		
		setDescription("Selezione della posizione dove creare la cartella di esportazione");		
		setTitle(((ExporterWizard)getWizard()).getVersion());
		
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
//		content.setBackground(new Color(null,100,100,100));
		
		Button r_zip = new Button(content,SWT.CHECK);
		r_zip.setText("Esporta come file zip");
		r_zip.setLayoutData(gdFillHcs3);		
		r_zip.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {				
				if (((Button)arg0.getSource()).getSelection()){
					((ExporterWizard)getWizard()).getExporterVO().setZipped(true);					
				}else{
					((ExporterWizard)getWizard()).getExporterVO().setZipped(false);
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
		
		l = new Label(content, SWT.NONE);
		l.setText("Cartella di destinazione");
		
		t = new Text(content, SWT.NONE);
		t.setEditable(false);
		t.setLayoutData(gdFillH);
		
		b = new Button(content, SWT.FLAT);
		b.setImage(apri_cartella);
		b.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				
				DirectoryDialog dd = new DirectoryDialog(getWizard().getContainer().getShell());				

		        dd.setText("Cartella di salvataggio dell'esportazione");

		        dd.setMessage("Seleziona la cartella dove salvare la cartella generata dall'esportazione");

		        String dir = dd.open();
		        if (dir != null) {
		        	t.setText(dir);
		        	((ExporterWizard)getWizard()).getExporterVO()
		        								 .setPathExportFile(dir);
		        	
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
