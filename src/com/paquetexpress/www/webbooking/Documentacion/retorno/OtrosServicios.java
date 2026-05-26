/**
 * OtrosServicios.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion.retorno;

public class OtrosServicios  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.Documentacion.retorno.OtroServicio[] otroServicio;

    public OtrosServicios() {
    }

    public OtrosServicios(
           com.paquetexpress.www.webbooking.Documentacion.retorno.OtroServicio[] otroServicio) {
           this.otroServicio = otroServicio;
    }


    /**
     * Gets the otroServicio value for this OtrosServicios.
     * 
     * @return otroServicio
     */
    public com.paquetexpress.www.webbooking.Documentacion.retorno.OtroServicio[] getOtroServicio() {
        return otroServicio;
    }


    /**
     * Sets the otroServicio value for this OtrosServicios.
     * 
     * @param otroServicio
     */
    public void setOtroServicio(com.paquetexpress.www.webbooking.Documentacion.retorno.OtroServicio[] otroServicio) {
        this.otroServicio = otroServicio;
    }

    public com.paquetexpress.www.webbooking.Documentacion.retorno.OtroServicio getOtroServicio(int i) {
        return this.otroServicio[i];
    }

    public void setOtroServicio(int i, com.paquetexpress.www.webbooking.Documentacion.retorno.OtroServicio _value) {
        this.otroServicio[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OtrosServicios)) return false;
        OtrosServicios other = (OtrosServicios) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.otroServicio==null && other.getOtroServicio()==null) || 
             (this.otroServicio!=null &&
              java.util.Arrays.equals(this.otroServicio, other.getOtroServicio())));
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
        if (getOtroServicio() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOtroServicio());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOtroServicio(), i);
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
        new org.apache.axis.description.TypeDesc(OtrosServicios.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">OtrosServicios"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("otroServicio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "OtroServicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "OtroServicio"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
