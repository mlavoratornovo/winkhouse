package winkhouse.dialogs.custom;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import winkhouse.Activator;

import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;

public class WinkBrowserPopUp {
	
	private Shell s = null;
	private Browser winkbrowser = null;
	private String url = null;
	private String redirectUrl = null;
	private Object returnObject = null;
	private String returnMethod = null;
	
	public WinkBrowserPopUp(String url,Object returnObject,String returnMethod) {
		
		this.url = url;
		this.returnObject = returnObject;
		this.returnMethod = returnMethod;
		
		createDialog();
	}
	
	private class ShellCloseListener implements Listener{
		
		private VerificationCodeReceiver vcr = null;
		
		public ShellCloseListener(VerificationCodeReceiver vcr){
			this.vcr = vcr;
		}

		@Override
		public void handleEvent(Event event) {
			try {
				this.vcr.stop();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
			
	}
	
	private void createDialog(){
			
		GridLayout gl = new GridLayout();
		s = new Shell(PlatformUI.createDisplay(),SWT.CLOSE|SWT.MAX|SWT.MIN|SWT.TITLE|SWT.APPLICATION_MODAL|SWT.RESIZE);
		FormToolkit ft = new FormToolkit(Activator.getDefault()
									  			  .getWorkbench()
									  			  .getDisplay());
		s.setMinimumSize(500, 500);
		s.setSize(400, 300);
		s.setLayout(gl);
		s.setBackground(new Color(null,255,255,255));
		s.setImage(Activator.getImageDescriptor("icons/home16.bmp").createImage());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;

		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;

		Composite container = new Composite(s,SWT.NONE);		
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
				
		winkbrowser = new Browser(container, SWT.NONE);
		winkbrowser.setLayoutData(gdFillHV);
		winkbrowser.setJavascriptEnabled(true);
		winkbrowser.addProgressListener(new ProgressListener() {
			
			@Override
			public void completed(ProgressEvent event) {

				String text = winkbrowser.getText();								
				Document doc = Jsoup.parse(text);
				
				if (doc.getElementById("code") != null){
					
					Element e = doc.getElementById("code");
					String code = e.attr("value");
					setterCall(code);
					s.close();
				}
			
			}
			
			@Override
			public void changed(ProgressEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		winkbrowser.setUrl(url);
		
		s.open ();
		Display display = Display.getDefault();
		
        while (!s.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }

	}
	
	protected void setterCall(String code){
		
		if ((this.returnObject != null) && (this.returnMethod != null)){
			
			Class[] paramarr = new Class[1];
			paramarr[0] = String.class;
			try {
				Method m = returnObject.getClass().getMethod(returnMethod, paramarr);
				m.invoke(returnObject, code);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}