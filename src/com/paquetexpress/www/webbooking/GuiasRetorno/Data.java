/**
 * Data.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.GuiasRetorno;

public class Data  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.GuiasRetorno.Header header;

    private com.paquetexpress.www.webbooking.GuiasRetorno.SolicitudEnvio solicitudEnvio;

    public Data() {
    }

    public Data(
           com.paquetexpress.www.webbooking.GuiasRetorno.Header header,
           com.paquetexpress.www.webbooking.GuiasRetorno.SolicitudEnvio solicitudEnvio) {
           this.header = header;
           this.solicitudEnvio = solicitudEnvio;
    }


    /**
     * Gets the header value for this Data.
     * 
     * @return header
     */
    public com.paquetexpress.www.webbooking.GuiasRetorno.Header getHeader() {
        return header;
    }


    /**
     * Sets the header value for this Data.
     * 
     * @param header
     */
    public void setHeader(com.paquetexpress.www.webbooking.GuiasRetorno.Header header) {
        this.header = header;
    }


    /**
     * Gets the solicitudEnvio value for this Data.
     * 
     * @return solicitudEnvio
     */
    public com.paquetexpress.www.webbooking.GuiasRetorno.SolicitudEnvio getSolicitudEnvio() {
        return solicitudEnvio;
    }


    /**
     * Sets the solicitudEnvio value for this Data.
     * 
     * @param solicitudEnvio
     */
    public void setSolicitudEnvio(com.paquetexpress.www.webbooking.GuiasRetorno.SolicitudEnvio solicitudEnvio) {
        this.solicitudEnvio = solicitudEnvio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Data)) return false;
        Data other = (Data) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.header==null && other.getHeader()==null) || 
             (this.header!=null &&
              this.header.equals(other.getHeader()))) &&
            ((this.solicitudEnvio==null && other.getSolicitudEnvio()==null) || 
             (this.solicitudEnvio!=null &&
              this.solicitudEnvio.equals(other.getSolicitudEnvio())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getHeader() != null) {
            _hashCode += getHeader().hashCode();
        }
        if (getSolicitudEnvio() != null) {
            _hashCode += getSolicitudEnvio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Data.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">Data"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("header");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "Header"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">Header"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("solicitudEnvio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "SolicitudEnvio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">SolicitudEnvio"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
