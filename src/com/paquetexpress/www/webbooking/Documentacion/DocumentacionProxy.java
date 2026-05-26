package com.paquetexpress.www.webbooking.Documentacion;

public class DocumentacionProxy implements com.paquetexpress.www.webbooking.Documentacion.Documentacion_PortType {
  private String _endpoint = null;
  private com.paquetexpress.www.webbooking.Documentacion.Documentacion_PortType documentacion_PortType = null;
  
  public DocumentacionProxy() {
    _initDocumentacionProxy();
  }
  
  public DocumentacionProxy(String endpoint) {
    _endpoint = endpoint;
    _initDocumentacionProxy();
  }
  
  private void _initDocumentacionProxy() {
    try {
      documentacion_PortType = (new com.paquetexpress.www.webbooking.Documentacion.Documentacion_ServiceLocator()).getDocumentacionSOAP();
      if (documentacion_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)documentacion_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)documentacion_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (documentacion_PortType != null)
      ((javax.xml.rpc.Stub)documentacion_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.paquetexpress.www.webbooking.Documentacion.Documentacion_PortType getDocumentacion_PortType() {
    if (documentacion_PortType == null)
      _initDocumentacionProxy();
    return documentacion_PortType;
  }
  
  public com.paquetexpress.www.webbooking.Documentacion.retorno.DataResponse documentarGuia(com.paquetexpress.www.webbooking.Documentacion.Data parameters) throws java.rmi.RemoteException{
    if (documentacion_PortType == null)
      _initDocumentacionProxy();
    return documentacion_PortType.documentarGuia(parameters);
  }
  
  
}