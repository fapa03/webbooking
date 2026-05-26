/**
 * DataResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.GuiasRetorno.retorno;

public class DataResponse  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RetornoSolicitud retornoSolicitud;

    public DataResponse() {
    }

    public DataResponse(
           com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RetornoSolicitud retornoSolicitud) {
           this.retornoSolicitud = retornoSolicitud;
    }


    /**
     * Gets the retornoSolicitud value for this DataResponse.
     * 
     * @return retornoSolicitud
     */
    public com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RetornoSolicitud getRetornoSolicitud() {
        return retornoSolicitud;
    }


    /**
     * Sets the retornoSolicitud value for this DataResponse.
     * 
     * @param retornoSolicitud
     */
    public void setRetornoSolicitud(com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RetornoSolicitud retornoSolicitud) {
        this.retornoSolicitud = retornoSolicitud;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DataResponse)) return false;
        DataResponse other = (DataResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.retornoSolicitud==null && other.getRetornoSolicitud()==null) || 
             (this.retornoSolicitud!=null &&
              this.retornoSolicitud.equals(other.getRetornoSolicitud())));
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
        if (getRetornoSolicitud() != null) {
            _hashCode += getRetornoSolicitud().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DataResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">DataResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retornoSolicitud");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "RetornoSolicitud"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">RetornoSolicitud"));
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
