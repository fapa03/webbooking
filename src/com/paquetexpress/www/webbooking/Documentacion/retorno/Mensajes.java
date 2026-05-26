/**
 * Mensajes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion.retorno;

public class Mensajes  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.Documentacion.retorno.Mensaje[] mensaje;

    public Mensajes() {
    }

    public Mensajes(
           com.paquetexpress.www.webbooking.Documentacion.retorno.Mensaje[] mensaje) {
           this.mensaje = mensaje;
    }


    /**
     * Gets the mensaje value for this Mensajes.
     * 
     * @return mensaje
     */
    public com.paquetexpress.www.webbooking.Documentacion.retorno.Mensaje[] getMensaje() {
        return mensaje;
    }


    /**
     * Sets the mensaje value for this Mensajes.
     * 
     * @param mensaje
     */
    public void setMensaje(com.paquetexpress.www.webbooking.Documentacion.retorno.Mensaje[] mensaje) {
        this.mensaje = mensaje;
    }

    public com.paquetexpress.www.webbooking.Documentacion.retorno.Mensaje getMensaje(int i) {
        return this.mensaje[i];
    }

    public void setMensaje(int i, com.paquetexpress.www.webbooking.Documentacion.retorno.Mensaje _value) {
        this.mensaje[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Mensajes)) return false;
        Mensajes other = (Mensajes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.mensaje==null && other.getMensaje()==null) || 
             (this.mensaje!=null &&
              java.util.Arrays.equals(this.mensaje, other.getMensaje())));
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
        if (getMensaje() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMensaje());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMensaje(), i);
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
        new org.apache.axis.description.TypeDesc(Mensajes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">Mensajes"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensaje");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "Mensaje"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "Mensaje"));
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
