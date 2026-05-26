package com.paquetexpress.core.web.util;

import com.fasterxml.jackson.databind.DeserializationFeature;

import java.util.ArrayList;
import java.util.List;

import mx.com.paquetexpress.dto.message.Message;
import mx.com.paquetexpress.dto.message.body.Body;
import mx.com.paquetexpress.dto.message.body.response.Response;
import mx.com.paquetexpress.dto.message.header.Header;
import mx.com.paquetexpress.dto.message.header.device.Device;
import mx.com.paquetexpress.dto.message.header.security.Security;
import mx.com.paquetexpress.dto.message.header.target.Target;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import mx.com.paquetexpress.http.Request;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author evillegas
 */
public class MessageApi {
    
    private com.fasterxml.jackson.databind.ObjectMapper mapper = null;        
    private String urlServer = "";
    
    public MessageApi(String urlServer){
        this.urlServer = urlServer;
    }
    
    private Response response = new Response();
    JsonNode json ;
    ObjectMapper mapper2 = new ObjectMapper();
    public Object executeMessageApi(Object data, String uri, String token) throws Exception{
        Object object = null;
        try{
            //MessageApi messageApi = new MessageApi();
            Message message = new Message();
            
            Security sec = new Security("", "", 2, token);
            Device devic = new Device("webbooking", "Web", "", "");
            Target targ = new Target("webbooking", "v1.0", "webbooking", "wbk", "CRUD");
            Header head = new Header(sec, devic, targ, "JSON", "ESP");
            
            message.setHeader(head);
            message.setBody(new Body());
            message.getBody().setRequest(new mx.com.paquetexpress.dto.message.body.request.Request());
            message.getBody().getRequest().setData(data);
            
            List<NameValuePair> cabeceras = new ArrayList<NameValuePair>();            
            cabeceras.add(new BasicNameValuePair("Content-Type", "application/json"));
            //cabeceras.add(new BasicNameValuePair("Content-Type", "application/javascript"));//json            
            Request peticion = new Request();
            
            //JsonNode json = mapper2.valueToTree(message);
            //System.out.println(""+json.toString());
            
            message = peticion.post(urlServer+uri, message, cabeceras);            
            
            initMapper();            
            object = mapper.convertValue(message.getBody().getResponse().getData(), Object.class);
            this.response = message.getBody().getResponse();//variable para llevar mensaje completo a logica de programa
        }catch(Exception ex){
            throw ex;
        }
        return object;
    }	
    
    private void initMapper() throws Exception {
         try{
             mapper = new com.fasterxml.jackson.databind.ObjectMapper(); // can reuse, share globally
             mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
             mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         }catch(Exception ex){
             throw ex;
         }
     }

    public Response getResponse() {
        return this.response;
    }   
}