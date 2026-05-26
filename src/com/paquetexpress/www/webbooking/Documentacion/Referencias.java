/**
 * Referencias.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion;

public class Referencias  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.Documentacion.Referencia[] referencia;

    public Referencias() {
    }

    public Referencias(
           com.paquetexpress.www.webbooking.Documentacion.Referencia[] referencia) {
           this.referencia = referencia;
    }


    /**
     * Gets the referencia value for this Referencias.
     * 
     * @return referencia
     */
    public com.paquetexpress.www.webbooking.Documentacion.Referencia[] getReferencia() {
        return referencia;
    }


    /**
     * Sets the referencia value for this Referencias.
     * 
     * @param referencia
     */
    public void setReferencia(com.paquetexpress.www.webbooking.Documentacion.Referencia[] referencia) {
        this.referencia = referencia;
    }

    public com.paquetexpress.www.webbooking.Documentacion.Referencia getReferencia(int i) {
        return this.referencia[i];
    }

    public void setReferencia(int i, com.paquetexpress.www.webbooking.Documentacion.Referencia _value) {
        this.referencia[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Referencias)) return false;
        Referencias other = (Referencias) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.referencia==null && other.getReferencia()==null) || 
             (this.referencia!=null &&
              java.util.Arrays.equals(this.referencia, other.getReferencia())));
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
        if (getReferencia() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReferencia());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReferencia(), i);
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
        new org.apache.axis.description.TypeDesc(Referencias.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">Referencias"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referencia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "Referencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "Referencia"));
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
