package beanUtil;

import mx.com.paquetexpress.general.model.dto.LgnUserMstrDTO;
import mx.com.paquetexpress.general.model.dto.LgnUserMstrResponseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paquetexpress.core.web.util.MessageApi;

public class LoginLdap {
	private String urlServer = ConnectDB.getApiLoginLDAPExt();
	private String tokenApi = "";
	private ObjectMapper mapper = null;
	public LgnUserMstrResponseDTO login(String userAccess, String userPass, String userType, String lang, String useUrl) {
		
		LgnUserMstrDTO lgnUserMstrDTO = new LgnUserMstrDTO();
		LgnUserMstrResponseDTO lgnUserMstrResponseDTO = null;				
		
		try {
			if (useUrl.equals("INT")) {
				urlServer = ConnectDB.getApiLoginLDAPInt();
			} else {
				urlServer = ConnectDB.getApiLoginLDAPExt();
			}			
			MessageApi messageApi = new MessageApi(urlServer);
			lgnUserMstrDTO.setUserAccess(userAccess);
			lgnUserMstrDTO.setUserPass(userPass);
			lgnUserMstrDTO.setUserType(userType);
			lgnUserMstrDTO.setLang(lang);
			lgnUserMstrDTO.setPlatForm("DL");
			initMapper();
			
			lgnUserMstrResponseDTO = mapper.convertValue(messageApi.executeMessageApi(lgnUserMstrDTO, "api/login/v1/loginpqtx", tokenApi), LgnUserMstrResponseDTO.class);
		} catch (Exception e) {
			System.out.println(" error "+e);
			e.printStackTrace();
		}
		return lgnUserMstrResponseDTO;
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
