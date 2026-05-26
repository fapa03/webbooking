/**
 * ClienteDestino.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion;

public class ClienteDestino  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.Documentacion.DomicilioDestino domicilioDestino;

    private java.lang.String destClntId;

    private java.lang.String destCustClntId;

    private java.lang.String destClntName;

    public ClienteDestino() {
    }

    public ClienteDestino(
           com.paquetexpress.www.webbooking.Documentacion.DomicilioDestino domicilioDestino,
           java.lang.String destClntId,
           java.lang.String destCustClntId,
           java.lang.String destClntName) {
           this.domicilioDestino = domicilioDestino;
           this.destClntId = destClntId;
           this.destCustClntId = destCustClntId;
           this.destClntName = destClntName;
    }


    /**
     * Gets the domicilioDestino value for this ClienteDestino.
     * 
     * @return domicilioDestino
     */
    public com.paquetexpress.www.webbooking.Documentacion.DomicilioDestino getDomicilioDestino() {
        return domicilioDestino;
    }


    /**
     * Sets the domicilioDestino value for this ClienteDestino.
     * 
     * @param domicilioDestino
     */
    public void setDomicilioDestino(com.paquetexpress.www.webbooking.Documentacion.DomicilioDestino domicilioDestino) {
        this.domicilioDestino = domicilioDestino;
    }


    /**
     * Gets the destClntId value for this ClienteDestino.
     * 
     * @return destClntId
     */
    public java.lang.String getDestClntId() {
        return destClntId;
    }


    /**
     * Sets the destClntId value for this ClienteDestino.
     * 
     * @param destClntId
     */
    public void setDestClntId(java.lang.String destClntId) {
        this.destClntId = destClntId;
    }


    /**
     * Gets the destCustClntId value for this ClienteDestino.
     * 
     * @return destCustClntId
     */
    public java.lang.String getDestCustClntId() {
        return destCustClntId;
    }


    /**
     * Sets the destCustClntId value for this ClienteDestino.
     * 
     * @param destCustClntId
     */
    public void setDestCustClntId(java.lang.String destCustClntId) {
        this.destCustClntId = destCustClntId;
    }


    /**
     * Gets the destClntName value for this ClienteDestino.
     * 
     * @return destClntName
     */
    public java.lang.String getDestClntName() {
        return destClntName;
    }


    /**
     * Sets the destClntName value for this ClienteDestino.
     * 
     * @param destClntName
     */
    public void setDestClntName(java.lang.String destClntName) {
        this.destClntName = destClntName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ClienteDestino)) return false;
        ClienteDestino other = (ClienteDestino) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.domicilioDestino==null && other.getDomicilioDestino()==null) || 
             (this.domicilioDestino!=null &&
              this.domicilioDestino.equals(other.getDomicilioDestino()))) &&
            ((this.destClntId==null && other.getDestClntId()==null) || 
             (this.destClntId!=null &&
              this.destClntId.equals(other.getDestClntId()))) &&
            ((this.destCustClntId==null && other.getDestCustClntId()==null) || 
             (this.destCustClntId!=null &&
              this.destCustClntId.equals(other.getDestCustClntId()))) &&
            ((this.destClntName==null && other.getDestClntName()==null) || 
             (this.destClntName!=null &&
              this.destClntName.equals(other.getDestClntName())));
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
        if (getDomicilioDestino() != null) {
            _hashCode += getDomicilioDestino().hashCode();
        }
        if (getDestClntId() != null) {
            _hashCode += getDestClntId().hashCode();
        }
        if (getDestCustClntId() != null) {
            _hashCode += getDestCustClntId().hashCode();
        }
        if (getDestClntName() != null) {
            _hashCode += getDestClntName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ClienteDestino.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">ClienteDestino"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domicilioDestino");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "DomicilioDestino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">DomicilioDestino"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destClntId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "destClntId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">destClntId"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destCustClntId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "destCustClntId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">destCustClntId"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destClntName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "destClntName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">destClntName"));
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
