package beanUtil;

import mx.com.paquetexpress.dto.message.body.response.Response;
import mx.com.paquetexpress.general.model.dto.MdfUserMstrDTO;
import mx.com.paquetexpress.general.model.dto.MdfUserMstrResponseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paquetexpress.core.web.util.MessageApi;

public class ModifyPasswordLdap {
	private String urlServer = ConnectDB.getApiLoginLDAPExt();
	//private String urlServer = "http://187.141.146.59:7007/RadRestFul";
	private String tokenApi = "";
	private ObjectMapper mapper = null;
	private Response response = new Response(); 
	
	public MdfUserMstrResponseDTO mdfdUserData(String useUrl, String userAccess, String userPass, String userName, String userType, String userCreation,
			String userEmail, String userClient, String action, String typeModule ) {
		
		MdfUserMstrDTO mdfUserMstrDTO = new MdfUserMstrDTO();
		MdfUserMstrResponseDTO mdfUserMstrResponseDTO = null;				
		
		try {
			if (useUrl.equals("INT")) {
				urlServer = ConnectDB.getApiLoginLDAPIntWL();
			} else {
				urlServer = ConnectDB.getApiLoginLDAPExtWL();
			}			
			MessageApi messageApi = new MessageApi(urlServer);
			mdfUserMstrDTO.setUserAccess(userAccess);
			mdfUserMstrDTO.setUserPass(userPass);
			mdfUserMstrDTO.setUserName(userName);
			mdfUserMstrDTO.setUserType(userType);
			mdfUserMstrDTO.setUserCreation(userCreation);
			mdfUserMstrDTO.setUserEmail(userEmail);
			mdfUserMstrDTO.setUserClient(userClient);
			mdfUserMstrDTO.setAction(action);
			mdfUserMstrDTO.setTypeModule(typeModule);
			
			initMapper();
			//marca error de casteo al usar mapper.convertValue
			//mdfUserMstrResponseDTO = mapper.convertValue(messageApi.executeMessageApi(mdfUserMstrDTO, "api/generic/useropenldapglp", tokenApi), MdfUserMstrResponseDTO.class);
			
			//posible ejemplo de casteo
			//listRetorno = (List<ComunDTO>) mapper.convertValue(response.getData(), new TypeReference<List<ComunDTO>>() {
	           //});
			messageApi.executeMessageApi(mdfUserMstrDTO, "api/generic/useropenldapglp", tokenApi);			
			response = messageApi.getResponse();//variable para llevar mensaje completo a logica de programa	
		} catch (Exception e) {
			System.out.println(" error "+e);
			e.printStackTrace();
		}
		return mdfUserMstrResponseDTO;
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

	public Response getResponse() {
		return response;
	}	
}
