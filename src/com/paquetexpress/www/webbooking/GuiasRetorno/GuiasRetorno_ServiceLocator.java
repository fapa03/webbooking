/**
 * GuiasRetorno_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.GuiasRetorno;

public class GuiasRetorno_ServiceLocator extends org.apache.axis.client.Service implements com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetorno_Service {

    public GuiasRetorno_ServiceLocator() {
    }


    public GuiasRetorno_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GuiasRetorno_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GuiasRetornoSOAP
    private java.lang.String GuiasRetornoSOAP_address = "http://localhost:8081/wsPaquetexpress/services/GuiasRetornoSOAP";

    public java.lang.String getGuiasRetornoSOAPAddress() {
        return GuiasRetornoSOAP_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GuiasRetornoSOAPWSDDServiceName = "GuiasRetornoSOAP";

    public java.lang.String getGuiasRetornoSOAPWSDDServiceName() {
        return GuiasRetornoSOAPWSDDServiceName;
    }

    public void setGuiasRetornoSOAPWSDDServiceName(java.lang.String name) {
        GuiasRetornoSOAPWSDDServiceName = name;
    }

    public com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetorno_PortType getGuiasRetornoSOAP() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GuiasRetornoSOAP_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGuiasRetornoSOAP(endpoint);
    }

    public com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetorno_PortType getGuiasRetornoSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetornoSOAPStub _stub = new com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetornoSOAPStub(portAddress, this);
            _stub.setPortName(getGuiasRetornoSOAPWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGuiasRetornoSOAPEndpointAddress(java.lang.String address) {
        GuiasRetornoSOAP_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetorno_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetornoSOAPStub _stub = new com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetornoSOAPStub(new java.net.URL(GuiasRetornoSOAP_address), this);
                _stub.setPortName(getGuiasRetornoSOAPWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("GuiasRetornoSOAP".equals(inputPortName)) {
            return getGuiasRetornoSOAP();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "GuiasRetorno");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "GuiasRetornoSOAP"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GuiasRetornoSOAP".equals(portName)) {
            setGuiasRetornoSOAPEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
