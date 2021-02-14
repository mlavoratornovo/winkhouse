package winkhouse.action.plugins;

import org.eclipse.jface.action.Action;

import winkhouse.export.exporter.IWinkPlugin;


public class PluginsLauncher extends Action {

	IWinkPlugin plug = null;
	
	public PluginsLauncher(IWinkPlugin plug) {
		this.plug = plug;
		setText(this.plug.getText());
		setImageDescriptor(this.plug.getImage());
		setToolTipText(this.plug.getTooltip());
	}

	@Override
	public void run() {
		this.plug.run();
	}
	
	


}
