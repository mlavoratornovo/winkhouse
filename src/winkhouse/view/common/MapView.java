package winkhouse.view.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.map.MapViewRefreshAction;
import winkhouse.util.json.JSONArray;
import winkhouse.util.json.JSONObject;

public class MapView extends ViewPart {

	private Browser b = null;
	
	private String previousurl = null;
	private String currenturl = null;
	
	private Text numero = null;
	private Text indirizzo = null;
	private Text citta = null;
	
	private Table tindirizzi = null;
	private TableViewer tvindirizzi = null;
	
	public final static String ID = "winkhouse.mapview";
		
 	public MapView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		
		FormToolkit ft = new FormToolkit(getViewSite().getShell().getDisplay());
		Form f = ft.createForm(parent);
		f.setImage(Activator.getImageDescriptor("icons/html.png").createImage());
		f.setText("Geo Localizzazione");
		f.getBody().setLayout(new GridLayout());
		f.getBody().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		getViewSite().getActionBars().getToolBarManager().add(new MapViewRefreshAction("Ricarica pagina google map", 
										 					  Activator.getImageDescriptor("icons/adept_reinstall.png")));

		
		f.updateToolBar();
		
		
		Composite c = ft.createComposite(f.getBody(),SWT.FLAT);
		
		c.setLayout(new GridLayout(2,false));
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite cindirizzi = ft.createComposite(c,SWT.FLAT);
		cindirizzi.setLayout(new GridLayout(4,false));		
		//cindirizzi.setBackground(new Color(null, 100,100,100));
		
		GridData gd_cindirizzi = new GridData(SWT.LEFT, SWT.TOP, false, true);
		gd_cindirizzi.widthHint	= 420;
		
		cindirizzi.setLayoutData(gd_cindirizzi);
		
		Label lNumero = ft.createLabel(cindirizzi, "Numero");
		Label lIndirizzo = ft.createLabel(cindirizzi, "Indirizzo");
		Label lCitta = ft.createLabel(cindirizzi, "Città");
		Label ldummy = ft.createLabel(cindirizzi, "");
			
		GridData gd_numero = new GridData();
		gd_numero.widthHint	= 25;
		numero = ft.createText(cindirizzi, "");
		numero.setLayoutData(gd_numero);
		
		GridData gd_indirizzo = new GridData();
		gd_indirizzo.widthHint	= 150;		
		indirizzo = ft.createText(cindirizzi, "");
		indirizzo.setLayoutData(gd_indirizzo);
		
		GridData gd_citta = new GridData();
		gd_citta.widthHint	= 150;				
		citta = ft.createText(cindirizzi, "");
		citta.setLayoutData(gd_citta);
		
		Button bcerca = ft.createButton(cindirizzi, "", SWT.FLAT);
		bcerca.setImage(Activator.getImageDescriptor("icons/kfind.png").createImage());
		bcerca.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				searchGeoDataInternal(indirizzo.getText(), numero.getText(), citta.getText());
				
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
		
		GridData gd_tindirizzi = new GridData();
		gd_tindirizzi.grabExcessHorizontalSpace = true;
		gd_tindirizzi.grabExcessVerticalSpace = true;
		gd_tindirizzi.horizontalAlignment = SWT.LEFT;		
		gd_tindirizzi.horizontalSpan = 4;		
		gd_tindirizzi.widthHint	= 400;
		gd_tindirizzi.heightHint= 300;
				
		tindirizzi = ft.createTable(cindirizzi, SWT.FLAT);
		tvindirizzi = new TableViewer(tindirizzi);
		tindirizzi.setLinesVisible(true);
		tindirizzi.setLayoutData(gd_tindirizzi);
		tvindirizzi.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement != null && inputElement instanceof ArrayList){
					return ((ArrayList)inputElement).toArray();
				}
				return null;
			}
			
		});
		tvindirizzi.setLabelProvider(new ILabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getText(Object element) {				
				return ((JSONObject)element).getString("display_name");
			}
			
			@Override
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		tvindirizzi.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				if ((event.getSelection() != null) && (((StructuredSelection)event.getSelection()).getFirstElement() != null)){
					JSONObject ja = (JSONObject)((StructuredSelection)event.getSelection()).getFirstElement();
					updateBrowser(ja.getString("lat"), ja.getString("lon"));
				}
				
			}
		});
		
		TableColumn tc_indirizzo = new TableColumn(tindirizzi, SWT.NONE, 0);
		tc_indirizzo.setText("indirizzo");
		tc_indirizzo.setWidth(390);
//		indirizzo = ft.createText(toolbar, "", SWT.FLAT);
//		indirizzo.setEditable(false);
//		indirizzo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		indirizzo.setFont(new Font(null, new FontData("Arial", 12, SWT.BOLD)));
				
		b = new Browser(c, SWT.NONE);
		b.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		ft.paintBordersFor(cindirizzi);		
		ft.paintBordersFor(f.getBody());
		ft.paintBordersFor(c);
	}
	
	public void updateBrowser(String latitudine,String longitudine){
				
		if (latitudine != null && longitudine != null){
			
			BufferedInputStream bis = new BufferedInputStream(getClass().getResourceAsStream("/winkhouse/configuration/osm.html"));
			String html = "";
			
			byte[] tmp = null;
			try {
				tmp = new byte[bis.available()];
				bis.read(tmp);
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bis.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			String content = new String(tmp);
			content = content.replaceAll("<latitude>", latitudine );
			content = content.replaceAll("<longitude>", longitudine);
			b.setText(content);		
			b.addProgressListener(new ProgressListener() {
				
				@Override
				public void completed(ProgressEvent event) {
					MapView.this.getSite().getShell().setCursor(new Cursor(null, SWT.CURSOR_ARROW));
					
				}
				
				@Override
				public void changed(ProgressEvent event) {
					MapView.this.getSite().getShell().setCursor(new Cursor(null, SWT.CURSOR_WAIT));
					
				}
			});
		}else{
			BufferedInputStream bis = new BufferedInputStream(getClass().getResourceAsStream("/winkhouse/configuration/no_find_osm.html"));
			String html = "";
			tvindirizzi.setInput(new ArrayList());
			
			byte[] tmp = null;
			try {
				tmp = new byte[bis.available()];
				bis.read(tmp);
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bis.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			String content2 = new String(tmp);
			b.setText(content2);
		}

	}
	
	public void searchGeoData(String indirizzo,String numero, String citta){
		
		String url_nominatim = "https://nominatim.openstreetmap.org/search?q=<citta>+<indirizzo>+<numero>,&format=json&polygon_geojson=1&addressdetails=1";
		
		numero = (numero != null)?numero:"";
		indirizzo = (indirizzo != null)?indirizzo:"";
		citta = (citta != null)?citta:"";
		
		url_nominatim = url_nominatim.replaceAll("<numero>", numero.replaceAll(" ", "+"));
		url_nominatim = url_nominatim.replaceAll("<indirizzo>", indirizzo.replaceAll(" ", "+"));
		url_nominatim = url_nominatim.replaceAll("<citta>", citta.replaceAll(" ", "+"));
		boolean update = true;
		if (previousurl == null){
			currenturl = url_nominatim;
			previousurl = currenturl;
		}else{
			if (!previousurl.equalsIgnoreCase(url_nominatim)){
				previousurl = currenturl;
				currenturl = url_nominatim;
			}else{
				update = false;
			}
		}
		this.citta.setText(citta);
		this.indirizzo.setText(indirizzo);
		this.numero.setText((numero != null)?numero:"");
/*		if (update){
			JSONArray ja = updateBrowser(currenturl);
			if (ja.getMyArrayList().size() > 0){
				tvindirizzi.setInput(ja.getMyArrayList());
				TableItem ti = tvindirizzi.getTable().getItem(0);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();
		
				StructuredSelection ss = new StructuredSelection(sel);
				
				tvindirizzi.setSelection(ss, true);
		
				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvindirizzi.getTable();
				tvindirizzi.getTable().notifyListeners(SWT.Selection, ev);
				updateBrowser(((JSONObject)ja.getMyArrayList().get(0)).getString("lat"),((JSONObject)ja.getMyArrayList().get(0)).getString("lon"));
			}else{
				updateBrowser(null, null);
			}
			
		}*/
	}	

	private void searchGeoDataInternal(String indirizzo,String numero, String citta){
		
		String url_nominatim = "https://nominatim.openstreetmap.org/search?q=<citta>+<indirizzo>+<numero>,&format=json&polygon_geojson=1&addressdetails=1";
		
		numero = (numero != null)?numero:"";
		indirizzo = (indirizzo != null)?indirizzo:"";
		citta = (citta != null)?citta:"";

		url_nominatim = url_nominatim.replaceAll("<numero>", numero.replaceAll(" ", "+"));
		url_nominatim = url_nominatim.replaceAll("<indirizzo>", indirizzo.replaceAll(" ", "+"));
		url_nominatim = url_nominatim.replaceAll("<citta>", citta.replaceAll(" ", "+"));
		boolean update = true;
//		if (previousurl == null){
//			currenturl = url_nominatim;
//			previousurl = currenturl;
//		}else{
//			if (!previousurl.equalsIgnoreCase(url_nominatim)){
//				previousurl = currenturl;
//				currenturl = url_nominatim;
//			}else{
//				update = false;
//			}
//		}
		if (update){
			JSONArray ja = updateBrowser(url_nominatim);
			if (ja.getMyArrayList().size() > 0){
				tvindirizzi.setInput(ja.getMyArrayList());
				TableItem ti = tvindirizzi.getTable().getItem(0);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();
		
				StructuredSelection ss = new StructuredSelection(sel);
				
				tvindirizzi.setSelection(ss, true);
		
				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvindirizzi.getTable();
				tvindirizzi.getTable().notifyListeners(SWT.Selection, ev);
				updateBrowser(((JSONObject)ja.getMyArrayList().get(0)).getString("lat"),((JSONObject)ja.getMyArrayList().get(0)).getString("lon"));
			}else{
				updateBrowser(null, null);
			}
			
		}
	}
	
	private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
	
	private CloseableHttpClient noSslHttpClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		
	      SSLContext sslContext = new SSLContextBuilder()  
	                .loadTrustMaterial(null, new TrustSelfSignedStrategy())  
	                .build();  
	      return HttpClientBuilder.create()
	    		  				  .setSSLContext(sslContext)
	                              .setConnectionManager(new PoolingHttpClientConnectionManager(
	                            		  					RegistryBuilder.<ConnectionSocketFactory>create()  
	                            		  								   .register("http", PlainConnectionSocketFactory.INSTANCE)  
	                            		  								   .register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))  
	                            		  								   .build()  
	                                    ))  
	                .build();  
	}  
	
	public JSONArray updateBrowser(String url_nominatim){
		
		String latitude = null;
		String longitude = null;
		String content = null;
		JSONArray jsonA = null;
		
		try {
			
			 SSLContext ctx = SSLContext.getInstance("TLS");
		     ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
		     SSLContext.setDefault(ctx);

		     URL url = new URL(url_nominatim);
		     HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		     conn.setHostnameVerifier(new HostnameVerifier() {
		            @Override
		            public boolean verify(String arg0, SSLSession arg1) {
		                return true;
		            }
		     });		     
		     BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)conn.getContent()));
		     StringBuilder result = new StringBuilder();
		     String line;
		     while((line = reader.readLine()) != null) {
		         result.append(line);
		     }
		     conn.disconnect();
			
			jsonA = new JSONArray(result.toString());
			
		} catch (ClientProtocolException e) {
			MessageDialog.openError(this.getSite().getShell(), "Errore connessione openstreetmap", "Impossibile connettersi a Openstreetmap");
		} catch (IOException e) {
			MessageDialog.openError(this.getSite().getShell(), "Errore connessione openstreetmap", "Impossibile connettersi a Openstreetmap");
		}catch (Exception e){
			MessageDialog.openError(this.getSite().getShell(), "Errore connessione openstreetmap", "Impossibile connettersi a Openstreetmap");
		}
		
		return jsonA;

	} 	
	
	@Override
	public void setFocus() {
	}

	public String getCurrenturl() {
		return currenturl;
	}

	public void setCurrenturl(String currenturl) {
		this.currenturl = currenturl;
	}

	
}