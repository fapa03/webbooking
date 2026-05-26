/**
 * OtroServicio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion;

public class OtroServicio  implements java.io.Serializable {
    private java.lang.String clave;

    private java.lang.String descripcion;

    private java.lang.String dataAditional1;

    private java.lang.String dataAditional2;

    public OtroServicio() {
    }

    public OtroServicio(
           java.lang.String clave,
           java.lang.String descripcion,
           java.lang.String dataAditional1,
           java.lang.String dataAditional2) {
           this.clave = clave;
           this.descripcion = descripcion;
           this.dataAditional1 = dataAditional1;
           this.dataAditional2 = dataAditional2;
    }


    /**
     * Gets the clave value for this OtroServicio.
     * 
     * @return clave
     */
    public java.lang.String getClave() {
        return clave;
    }


    /**
     * Sets the clave value for this OtroServicio.
     * 
     * @param clave
     */
    public void setClave(java.lang.String clave) {
        this.clave = clave;
    }


    /**
     * Gets the descripcion value for this OtroServicio.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this OtroServicio.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the dataAditional1 value for this OtroServicio.
     * 
     * @return dataAditional1
     */
    public java.lang.String getDataAditional1() {
        return dataAditional1;
    }


    /**
     * Sets the dataAditional1 value for this OtroServicio.
     * 
     * @param dataAditional1
     */
    public void setDataAditional1(java.lang.String dataAditional1) {
        this.dataAditional1 = dataAditional1;
    }


    /**
     * Gets the dataAditional2 value for this OtroServicio.
     * 
     * @return dataAditional2
     */
    public java.lang.String getDataAditional2() {
        return dataAditional2;
    }


    /**
     * Sets the dataAditional2 value for this OtroServicio.
     * 
     * @param dataAditional2
     */
    public void setDataAditional2(java.lang.String dataAditional2) {
        this.dataAditional2 = dataAditional2;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OtroServicio)) return false;
        OtroServicio other = (OtroServicio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.clave==null && other.getClave()==null) || 
             (this.clave!=null &&
              this.clave.equals(other.getClave()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              this.descripcion.equals(other.getDescripcion()))) &&
            ((this.dataAditional1==null && other.getDataAditional1()==null) || 
             (this.dataAditional1!=null &&
              this.dataAditional1.equals(other.getDataAditional1()))) &&
            ((this.dataAditional2==null && other.getDataAditional2()==null) || 
             (this.dataAditional2!=null &&
              this.dataAditional2.equals(other.getDataAditional2())));
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
        if (getClave() != null) {
            _hashCode += getClave().hashCode();
        }
        if (getDescripcion() != null) {
            _hashCode += getDescripcion().hashCode();
        }
        if (getDataAditional1() != null) {
            _hashCode += getDataAditional1().hashCode();
        }
        if (getDataAditional2() != null) {
            _hashCode += getDataAditional2().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OtroServicio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">OtroServicio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clave");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "clave"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">clave"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">descripcion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAditional1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "dataAditional1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">dataAditional1"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAditional2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "dataAditional2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">dataAditional2"));
        elemField.setMinOccurs(0);
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
