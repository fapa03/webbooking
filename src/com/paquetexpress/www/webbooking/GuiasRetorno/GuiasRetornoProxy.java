package com.paquetexpress.www.webbooking.GuiasRetorno;

public class GuiasRetornoProxy implements com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetorno_PortType {
  private String _endpoint = null;
  private com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetorno_PortType guiasRetorno_PortType = null;
  
  public GuiasRetornoProxy() {
    _initGuiasRetornoProxy();
  }
  
  public GuiasRetornoProxy(String endpoint) {
    _endpoint = endpoint;
    _initGuiasRetornoProxy();
  }
  
  private void _initGuiasRetornoProxy() {
    try {
      guiasRetorno_PortType = (new com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetorno_ServiceLocator()).getGuiasRetornoSOAP();
      if (guiasRetorno_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)guiasRetorno_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)guiasRetorno_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (guiasRetorno_PortType != null)
      ((javax.xml.rpc.Stub)guiasRetorno_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetorno_PortType getGuiasRetorno_PortType() {
    if (guiasRetorno_PortType == null)
      _initGuiasRetornoProxy();
    return guiasRetorno_PortType;
  }
  
  public com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse genGuiasRetorno(com.paquetexpress.www.webbooking.GuiasRetorno.Data parameters) throws java.rmi.RemoteException{
    if (guiasRetorno_PortType == null)
      _initGuiasRetornoProxy();
    return guiasRetorno_PortType.genGuiasRetorno(parameters);
  }
  
  
}