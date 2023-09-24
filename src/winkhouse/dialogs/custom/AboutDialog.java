package winkhouse.dialogs.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.osgi.framework.Version;

import winkhouse.Activator;




public class AboutDialog {
	
	private Form f = null;

	public AboutDialog() {createDialog();
	}


	public void createDialog(){
		GridLayout gl = new GridLayout();
		Shell s = new Shell(PlatformUI.createDisplay(),SWT.CLOSE|SWT.TITLE|SWT.APPLICATION_MODAL);
		FormToolkit ft = new FormToolkit(Activator.getDefault()
												  .getWorkbench()
												  .getDisplay());
		s.setMinimumSize(320, 200);
		s.setSize(320, 200);
		s.setLayout(gl);
		s.setBackground(new Color(null,255,255,255));
		s.setImage(Activator.getImageDescriptor("icons/icowin/home16.png").createImage());
		s.setText("About & Thanks");
		Form f = ft.createForm(s);
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 2;
		
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;

		GridData gd2 = new GridData();
		gd2.verticalAlignment = SWT.FILL;
		gd2.horizontalAlignment = SWT.FILL;
		gd2.grabExcessHorizontalSpace = true;
		gd2.grabExcessVerticalSpace = true;
		gd2.horizontalSpan = 2;
		f.setLayoutData(gd);
		f.getBody().setLayoutData(gd);
		f.getBody().setLayout(gl2);
		
		ImageHyperlink imlogo = ft.createImageHyperlink(f.getBody(), SWT.TOP);
		imlogo.setImage(Activator.getImageDescriptor("icons/home48.bmp").createImage());
		
		Version v = Activator.getDefault().getBundle().getVersion();
		Label ldesc = ft.createLabel(f.getBody(), "winkhouse v." + v.getMajor() + "."+v.getMinor()+"."+ v.getMicro() + "\n ", SWT.FLAT);
		ldesc.setLayoutData(gd);
		
		Composite c = ft.createComposite(f.getBody(), SWT.NONE);
		c.setLayoutData(gd2);
		c.setLayout(new GridLayout(3, false));
		
		
		ImageHyperlink imhsql = ft.createImageHyperlink(c, SWT.TOP);
		imhsql.setImage(Activator.getImageDescriptor("icons/hypersql_logo.png").createImage());

		ImageHyperlink imjavolution = ft.createImageHyperlink(c, SWT.TOP);
		imjavolution.setImage(Activator.getImageDescriptor("icons/javolution.jpg").createImage());

		ImageHyperlink imodf = ft.createImageHyperlink(c, SWT.TOP);
		imodf.setImage(Activator.getImageDescriptor("icons/odfdom.jpeg").createImage());

		s.open();
		
	}
}
