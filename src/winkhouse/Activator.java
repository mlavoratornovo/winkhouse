package winkhouse;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import winkhouse.util.WinkhouseUtils;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	private ArrayList<Long> pluginsIds = null; 
	// The plug-in ID
	public static final String PLUGIN_ID = "winkhouse";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;		
		
		pluginsIds = new ArrayList<Long>();
		String pluginsPath = WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.PLUGINSPATH);
		String defaultPluginsPath = WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.PLUGINSPATH);
		File f = null;
		if ((pluginsPath == null) || (pluginsPath.equalsIgnoreCase(""))){
			f = new File(defaultPluginsPath);
		}else{
			f = new File(pluginsPath);
		}
		f.mkdirs();
		
		File[] fileList = f.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			try{
				Bundle d = context.installBundle(fileList[i].toURI().toString());
				d.start();
				pluginsIds.add(d.getBundleId());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
		Iterator<Long> it = pluginsIds.iterator();
		
		while (it.hasNext()) {
			Long long1 = (Long) it.next();
			Bundle b = context.getBundle(long1);
			if (b != null){
				b.stop();
			}
		}		
		
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
