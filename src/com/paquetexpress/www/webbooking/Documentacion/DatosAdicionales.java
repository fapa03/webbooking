/**
 * DatosAdicionales.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion;

public class DatosAdicionales  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.Documentacion.DatoAdicional[] datoAdicional;

    public DatosAdicionales() {
    }

    public DatosAdicionales(
           com.paquetexpress.www.webbooking.Documentacion.DatoAdicional[] datoAdicional) {
           this.datoAdicional = datoAdicional;
    }


    /**
     * Gets the datoAdicional value for this DatosAdicionales.
     * 
     * @return datoAdicional
     */
    public com.paquetexpress.www.webbooking.Documentacion.DatoAdicional[] getDatoAdicional() {
        return datoAdicional;
    }


    /**
     * Sets the datoAdicional value for this DatosAdicionales.
     * 
     * @param datoAdicional
     */
    public void setDatoAdicional(com.paquetexpress.www.webbooking.Documentacion.DatoAdicional[] datoAdicional) {
        this.datoAdicional = datoAdicional;
    }

    public com.paquetexpress.www.webbooking.Documentacion.DatoAdicional getDatoAdicional(int i) {
        return this.datoAdicional[i];
    }

    public void setDatoAdicional(int i, com.paquetexpress.www.webbooking.Documentacion.DatoAdicional _value) {
        this.datoAdicional[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosAdicionales)) return false;
        DatosAdicionales other = (DatosAdicionales) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.datoAdicional==null && other.getDatoAdicional()==null) || 
             (this.datoAdicional!=null &&
              java.util.Arrays.equals(this.datoAdicional, other.getDatoAdicional())));
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
        if (getDatoAdicional() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDatoAdicional());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDatoAdicional(), i);
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
        new org.apache.axis.description.TypeDesc(DatosAdicionales.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">DatosAdicionales"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datoAdicional");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "DatoAdicional"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "DatoAdicional"));
        elemField.setMinOccurs(0);
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
