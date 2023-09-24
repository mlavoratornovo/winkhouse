/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.eclipse.jface.action.Action
 *  org.eclipse.jface.resource.ImageDescriptor
 *  org.eclipse.jface.viewers.Viewer
 *  org.eclipse.jface.viewers.ViewerFilter
 *  org.eclipse.ui.PlatformUI
 */
package winkhouse.action.winkcloud;

import java.util.Date;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.PlatformUI;
import winkhouse.Activator;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.CloudQueryOrigin;
import winkhouse.view.winkcloud.QueryFilesView;

public class CercaQueriesAction
extends Action {
    public CercaQueriesAction() {
    }

    public CercaQueriesAction(String text) {
        super(text);
    }

    public CercaQueriesAction(String text, ImageDescriptor image) {
        super(text, image);
    }

    public CercaQueriesAction(String text, int style) {
        super(text, style);
    }

    public ImageDescriptor getImageDescriptor() {
        return Activator.getImageDescriptor("icons/kfind.png");
    }

    public void run() {
        QueryFilesView qfv = (QueryFilesView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("winkhouse.queryfiles");
        Date da = null;
        Date a = null;
        CloudQueryOrigin cqo = qfv.getSelTipo();
        if (da == null && a == null && cqo == null) {
            qfv.getTvQueryFiles().setFilters(new ViewerFilter[0]);
        } else {
            qfv.getTvQueryFiles().addFilter((ViewerFilter)new CloudQueryFilter(da, a, cqo));
        }
    }

    class CloudQueryFilter
    extends ViewerFilter {
        private Date da = null;
        private Date a = null;
        private CloudQueryOrigin cqo = null;

        public CloudQueryFilter(Date da, Date a, CloudQueryOrigin cqo) {
            this.da = da;
            this.a = a;
            this.cqo = cqo;
        }

        public boolean select(Viewer viewer, Object parentElement, Object element) {
            boolean daok = true;
            boolean aok = true;
            boolean tipook = true;
            if (this.da != null || this.a != null || this.cqo != null) {
                if (this.da != null && ((CloudQueryModel)element).getDataRicezioneFile().compareTo(this.da) < 0) {
                    daok = false;
                }
                if (this.a != null && ((CloudQueryModel)element).getDataRicezioneFile().compareTo(this.a) > 0) {
                    aok = false;
                }
                if (this.cqo != null) {
                    if (((CloudQueryModel)element).getType() != this.cqo) {
                        tipook = false;
                    }
                } else {
                    tipook = true;
                }
                return daok && aok && tipook;
            }
            return true;
        }
    }
}

