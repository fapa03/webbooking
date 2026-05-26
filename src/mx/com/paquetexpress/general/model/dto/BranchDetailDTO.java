package mx.com.paquetexpress.general.model.dto;

import java.lang.reflect.Array;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BranchDetailDTO {

	public String destSiteId;
	public String radSrvcItemDTOlist;

	public String getDestSiteId() {
		return destSiteId;
	}

	public void setDestSiteId(String destSiteId) {
		this.destSiteId = destSiteId;
	}

	public Object getRadSrvcItemDTOlist() {
		return radSrvcItemDTOlist;
	}

	public void setRadSrvcItemDTOlist(String radSrvcItemDTOlist) {
		this.radSrvcItemDTOlist = radSrvcItemDTOlist;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[{destSiteId:" + destSiteId + ", radSrvcItemDTOList:" + radSrvcItemDTOlist
				+ "}]";
	}

}
