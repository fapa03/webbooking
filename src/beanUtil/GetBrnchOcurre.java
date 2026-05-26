package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.paquetexpress.core.web.util.MessageApi;

import bean.Resources;
import bean.ShipmentServiceDetail;
import mx.com.paquetexpress.general.model.dto.BranchDetailDTO;
import mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse;

public class GetBrnchOcurre {
	private String urlServer = ConnectDB.getApibrncOCUExt();
	private String getToken = "SELECT PM_VLUE1_ID FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = 'WEB' and pm_parm_type = 'TOKEN' AND ROWNUM=1";
	private ObjectMapper mapper = null;
	StringBuffer concatena = new StringBuffer();
	final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	Resources resources = new Resources();
	
	@SuppressWarnings("unchecked")
	public ArrayList<BranchDetailDTOResponse> getBrnchsOcurre(String tokenApi, String destSiteId, String radSrvcItemDTOlist) {

		BranchDetailDTO branchDetailDTO = new BranchDetailDTO();
		ArrayList<BranchDetailDTOResponse> sucursales = null;
		
		Object objTmp = null;
		String useUrl = "INT";

		try {

			if (useUrl.equals("INT")) {
				urlServer = ConnectDB.getApibrncOCUInt();
			} else {
				urlServer = ConnectDB.getApibrncOCUExt();
			}
			MessageApi messageApi = new MessageApi(urlServer);
			branchDetailDTO.setDestSiteId(destSiteId);
			branchDetailDTO.setRadSrvcItemDTOlist(radSrvcItemDTOlist);
			initMapper();
			objTmp = stringToObject(branchDetailDTO.toString());
			messageApi.executeMessageApi(objTmp, "api/info/v1/brncOCU", tokenApi);
			sucursales = (ArrayList<BranchDetailDTOResponse>) messageApi.getResponse().getData();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sucursales;
	}

	private Object stringToObject(String detail) {
		Gson gson = new Gson();
		String tmp;
		tmp = detail.replaceAll(":", "\":\"");
		tmp = tmp.replaceAll(", ", "\",\"");
		tmp = tmp.replace("{", "{\"");
		tmp = tmp.replace("}", "\"}");
		tmp = tmp.replace("\"[", "[");
		tmp = tmp.replace("]\"", "]");
		tmp = tmp.replaceAll("=", "\":\"");
		return gson.fromJson(tmp, Object.class);
	}
	
	public String getToken(Connection con) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(getToken);
			rs = pst.executeQuery();
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public ArrayList<BranchDetailDTOResponse> getBrnchsOcurre(String tokenApi, String destSiteId, String weight, double volL,
			double volH, double volW, String volume, String slabNo) {

		BranchDetailDTO branchDetailDTO = new BranchDetailDTO();
		ArrayList<BranchDetailDTOResponse> sucursales = null;
		Object objTmp = null;
		String useUrl = "INT";
		String radSrvcItemDTOlist = "";
		
		if (volume.startsWith(",")) {
			volume = "0" + volume.replace(",", ".");
		} else {
			volume = volume.replace(",", ".");
		}
		try {

			if (useUrl.equals("INT")) {
				urlServer = ConnectDB.getApibrncOCUInt();
			} else {
				urlServer = ConnectDB.getApibrncOCUExt();
			}
			MessageApi messageApi = new MessageApi(urlServer);
			branchDetailDTO.setDestSiteId(destSiteId);
			radSrvcItemDTOlist = prepareData("ENVELOPES", Float.parseFloat(weight), (float) volL, (float) volW,
					(float) volH, Float.parseFloat(volume), slabNo);
			branchDetailDTO.setRadSrvcItemDTOlist(radSrvcItemDTOlist);
			initMapper();

			objTmp = stringToObject(branchDetailDTO.toString());
			messageApi.executeMessageApi(objTmp, "api/info/v1/brncOCU", tokenApi);
			sucursales = (ArrayList<BranchDetailDTOResponse>) messageApi.getResponse().getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sucursales;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<BranchDetailDTOResponse> getBrnchsOcurre(String tokenApi, String destSiteId, String weight, String volL,
			String volH, String volW, String volume, String slabNo) {

		BranchDetailDTO branchDetailDTO = new BranchDetailDTO();
		ArrayList<BranchDetailDTOResponse> sucursales = null;
		
		Object objTmp = null;
		String useUrl = "INT";
		String radSrvcItemDTOlist = "";
		if ("".equals(volH) || "".equals(volL) || "".equals(volW)) {
			volH = "0";
			volL = "0";
			volW = "0";
		}
		if (volume.startsWith(",")) {
			volume = "0" + volume.replace(",", ".");
		} else {
			volume = volume.replace(",", ".");
		}
		if (weight.equals("")) {
			weight = "0";
		}
		try {

			if (useUrl.equals("INT")) {
				urlServer = ConnectDB.getApibrncOCUInt();
			} else {
				urlServer = ConnectDB.getApibrncOCUExt();
			}
			MessageApi messageApi = new MessageApi(urlServer);
			branchDetailDTO.setDestSiteId(destSiteId);
			radSrvcItemDTOlist = prepareData("PACKETS", Float.parseFloat(weight), Float.parseFloat(volL),
					Float.parseFloat(volW), Float.parseFloat(volH), Float.parseFloat(volume), slabNo);
			branchDetailDTO.setRadSrvcItemDTOlist(radSrvcItemDTOlist);
			initMapper();

			objTmp = stringToObject(branchDetailDTO.toString());
			messageApi.executeMessageApi(objTmp, "api/info/v1/brncOCU", tokenApi);
			sucursales = (ArrayList<BranchDetailDTOResponse>) messageApi.getResponse().getData();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sucursales;
	}

	public String prepareData(String serviceId, Float weight, Float volL, Float volW, Float volH, Float volume,
			String slabNo) {
		Map<String, String> datos = new HashMap<String, String>();

		if (!serviceId.equals("") && !serviceId.isEmpty()) {
			datos.put("srvcId", "" + serviceId + "");
		}
		datos.put("weight", weight.toString());
		datos.put("volL", volL.toString());
		datos.put("volW", volW.toString());
		datos.put("volH", volH.toString());
		datos.put("slabNo", slabNo.toString());
		datos.put("volume", volume.toString());

		return "[" + datos.toString() + "]";
	}

	public BranchDetailDTOResponse mapToDTO(LinkedHashMap<?, ?> branch) {
		BranchDetailDTOResponse sucursal = null;
		try {
			initMapper();
			sucursal = mapper.convertValue(branch, new TypeReference<BranchDetailDTOResponse>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sucursal;
	}

	public String getMaxDetail(ArrayList<?> servicesDetailArray) {
		Float volume = 0f, weight = 0f, L = 0f, W = 0f, H = 0f;
		String tmp = "";
		ShipmentServiceDetail ssd = null;
		int size = 0;

		if (servicesDetailArray != null) {
			size = servicesDetailArray.size();
		}
		ssd = null;
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				weight = weight < Float.parseFloat(ssd.getPeso()) ? Float.parseFloat(ssd.getPeso()) : weight;
				volume = volume < Float.parseFloat(ssd.getVolumen()) ? Float.parseFloat(ssd.getVolumen()) : volume;
				L = L < Float.parseFloat(ssd.getVolL()) ? Float.parseFloat(ssd.getVolL()) : L;
				W = W < Float.parseFloat(ssd.getVolW()) ? Float.parseFloat(ssd.getVolW()) : W;
				H = H < Float.parseFloat(ssd.getVolH()) ? Float.parseFloat(ssd.getVolH()) : H;
			}

			tmp = prepareData(ssd.getRefServiceId(), weight, L, W, H, volume, ssd.getTarifa());
			tmp = tmp.replaceAll("=", ":");

		}
		return tmp;
	}

	private void initMapper() throws Exception {
		try {
			mapper = new ObjectMapper(); // can reuse, share globally
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		} catch (Exception ex) {
			throw ex;
		}
	}
}
