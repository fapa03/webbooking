/**
 * Header.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.GuiasRetorno;

public class Header  implements java.io.Serializable {
    private java.lang.String orgnClntId;

    private java.lang.String clntPswd;

    private java.lang.String agreementKey;

    private com.paquetexpress.www.webbooking.GuiasRetorno.TypeEvent typeEvent;

    public Header() {
    }

    public Header(
           java.lang.String orgnClntId,
           java.lang.String clntPswd,
           java.lang.String agreementKey,
           com.paquetexpress.www.webbooking.GuiasRetorno.TypeEvent typeEvent) {
           this.orgnClntId = orgnClntId;
           this.clntPswd = clntPswd;
           this.agreementKey = agreementKey;
           this.typeEvent = typeEvent;
    }


    /**
     * Gets the orgnClntId value for this Header.
     * 
     * @return orgnClntId
     */
    public java.lang.String getOrgnClntId() {
        return orgnClntId;
    }


    /**
     * Sets the orgnClntId value for this Header.
     * 
     * @param orgnClntId
     */
    public void setOrgnClntId(java.lang.String orgnClntId) {
        this.orgnClntId = orgnClntId;
    }


    /**
     * Gets the clntPswd value for this Header.
     * 
     * @return clntPswd
     */
    public java.lang.String getClntPswd() {
        return clntPswd;
    }


    /**
     * Sets the clntPswd value for this Header.
     * 
     * @param clntPswd
     */
    public void setClntPswd(java.lang.String clntPswd) {
        this.clntPswd = clntPswd;
    }


    /**
     * Gets the agreementKey value for this Header.
     * 
     * @return agreementKey
     */
    public java.lang.String getAgreementKey() {
        return agreementKey;
    }


    /**
     * Sets the agreementKey value for this Header.
     * 
     * @param agreementKey
     */
    public void setAgreementKey(java.lang.String agreementKey) {
        this.agreementKey = agreementKey;
    }


    /**
     * Gets the typeEvent value for this Header.
     * 
     * @return typeEvent
     */
    public com.paquetexpress.www.webbooking.GuiasRetorno.TypeEvent getTypeEvent() {
        return typeEvent;
    }


    /**
     * Sets the typeEvent value for this Header.
     * 
     * @param typeEvent
     */
    public void setTypeEvent(com.paquetexpress.www.webbooking.GuiasRetorno.TypeEvent typeEvent) {
        this.typeEvent = typeEvent;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Header)) return false;
        Header other = (Header) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.orgnClntId==null && other.getOrgnClntId()==null) || 
             (this.orgnClntId!=null &&
              this.orgnClntId.equals(other.getOrgnClntId()))) &&
            ((this.clntPswd==null && other.getClntPswd()==null) || 
             (this.clntPswd!=null &&
              this.clntPswd.equals(other.getClntPswd()))) &&
            ((this.agreementKey==null && other.getAgreementKey()==null) || 
             (this.agreementKey!=null &&
              this.agreementKey.equals(other.getAgreementKey()))) &&
            ((this.typeEvent==null && other.getTypeEvent()==null) || 
             (this.typeEvent!=null &&
              this.typeEvent.equals(other.getTypeEvent())));
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
        if (getOrgnClntId() != null) {
            _hashCode += getOrgnClntId().hashCode();
        }
        if (getClntPswd() != null) {
            _hashCode += getClntPswd().hashCode();
        }
        if (getAgreementKey() != null) {
            _hashCode += getAgreementKey().hashCode();
        }
        if (getTypeEvent() != null) {
            _hashCode += getTypeEvent().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Header.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">Header"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orgnClntId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "orgnClntId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">orgnClntId"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clntPswd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "clntPswd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">clntPswd"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("agreementKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "agreementKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">agreementKey"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeEvent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "typeEvent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">typeEvent"));
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
