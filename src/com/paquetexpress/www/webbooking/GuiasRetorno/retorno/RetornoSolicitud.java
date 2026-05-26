/**
 * RetornoSolicitud.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.GuiasRetorno.retorno;

public class RetornoSolicitud  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RastreoRetorno[] rastreosRetorno;

    public RetornoSolicitud() {
    }

    public RetornoSolicitud(
           com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RastreoRetorno[] rastreosRetorno) {
           this.rastreosRetorno = rastreosRetorno;
    }


    /**
     * Gets the rastreosRetorno value for this RetornoSolicitud.
     * 
     * @return rastreosRetorno
     */
    public com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RastreoRetorno[] getRastreosRetorno() {
        return rastreosRetorno;
    }


    /**
     * Sets the rastreosRetorno value for this RetornoSolicitud.
     * 
     * @param rastreosRetorno
     */
    public void setRastreosRetorno(com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RastreoRetorno[] rastreosRetorno) {
        this.rastreosRetorno = rastreosRetorno;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetornoSolicitud)) return false;
        RetornoSolicitud other = (RetornoSolicitud) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.rastreosRetorno==null && other.getRastreosRetorno()==null) || 
             (this.rastreosRetorno!=null &&
              java.util.Arrays.equals(this.rastreosRetorno, other.getRastreosRetorno())));
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
        if (getRastreosRetorno() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRastreosRetorno());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRastreosRetorno(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RetornoSolicitud.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">RetornoSolicitud"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rastreosRetorno");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "RastreosRetorno"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">RastreosRetorno"));
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
