package paquetexpress.internal.prepaid.monitor.dao;

import java.util.ArrayList;
import java.util.Comparator;

public class SetComparatorByTarifZone implements Comparator<ArrayList>{
	private boolean asc;
	public SetComparatorByTarifZone(boolean asc) {
		this.asc = asc;
	}
	
	@Override
    public int compare(ArrayList o1, ArrayList o2) {
        int ret;
        if (asc) {
            ret = o1.get(14).toString().compareTo(o2.get(14).toString());
        } else {
            ret = o2.get(14).toString().compareTo(o1.get(14).toString());
        }
        return ret;
    }

}
