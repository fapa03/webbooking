/**
 * SolicitudEnvio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.GuiasRetorno;

public class SolicitudEnvio  implements java.io.Serializable {
    private java.lang.String guiaNo;

    private java.lang.String rastreoNo;

    private com.paquetexpress.www.webbooking.GuiasRetorno.DatosAdicionales datosAdicionales;

    private int cantidadRastreos;

    public SolicitudEnvio() {
    }

    public SolicitudEnvio(
           java.lang.String guiaNo,
           java.lang.String rastreoNo,
           com.paquetexpress.www.webbooking.GuiasRetorno.DatosAdicionales datosAdicionales,
           int cantidadRastreos) {
           this.guiaNo = guiaNo;
           this.rastreoNo = rastreoNo;
           this.datosAdicionales = datosAdicionales;
           this.cantidadRastreos = cantidadRastreos;
    }


    /**
     * Gets the guiaNo value for this SolicitudEnvio.
     * 
     * @return guiaNo
     */
    public java.lang.String getGuiaNo() {
        return guiaNo;
    }


    /**
     * Sets the guiaNo value for this SolicitudEnvio.
     * 
     * @param guiaNo
     */
    public void setGuiaNo(java.lang.String guiaNo) {
        this.guiaNo = guiaNo;
    }


    /**
     * Gets the rastreoNo value for this SolicitudEnvio.
     * 
     * @return rastreoNo
     */
    public java.lang.String getRastreoNo() {
        return rastreoNo;
    }


    /**
     * Sets the rastreoNo value for this SolicitudEnvio.
     * 
     * @param rastreoNo
     */
    public void setRastreoNo(java.lang.String rastreoNo) {
        this.rastreoNo = rastreoNo;
    }


    /**
     * Gets the datosAdicionales value for this SolicitudEnvio.
     * 
     * @return datosAdicionales
     */
    public com.paquetexpress.www.webbooking.GuiasRetorno.DatosAdicionales getDatosAdicionales() {
        return datosAdicionales;
    }


    /**
     * Sets the datosAdicionales value for this SolicitudEnvio.
     * 
     * @param datosAdicionales
     */
    public void setDatosAdicionales(com.paquetexpress.www.webbooking.GuiasRetorno.DatosAdicionales datosAdicionales) {
        this.datosAdicionales = datosAdicionales;
    }


    /**
     * Gets the cantidadRastreos value for this SolicitudEnvio.
     * 
     * @return cantidadRastreos
     */
    public int getCantidadRastreos() {
        return cantidadRastreos;
    }


    /**
     * Sets the cantidadRastreos value for this SolicitudEnvio.
     * 
     * @param cantidadRastreos
     */
    public void setCantidadRastreos(int cantidadRastreos) {
        this.cantidadRastreos = cantidadRastreos;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SolicitudEnvio)) return false;
        SolicitudEnvio other = (SolicitudEnvio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
                ((this.guiaNo==null && other.getGuiaNo()==null) || 
                 (this.guiaNo!=null &&
                  this.guiaNo.equals(other.getGuiaNo()))) &&
                ((this.rastreoNo==null && other.getRastreoNo()==null) || 
                 (this.rastreoNo!=null &&
                  this.rastreoNo.equals(other.getRastreoNo()))) &&
                ((this.datosAdicionales==null && other.getDatosAdicionales()==null) || 
                 (this.datosAdicionales!=null &&
                  this.datosAdicionales.equals(other.getDatosAdicionales()))) &&
                this.cantidadRastreos == other.getCantidadRastreos();
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
        if (getGuiaNo() != null) {
            _hashCode += getGuiaNo().hashCode();
        }
        if (getRastreoNo() != null) {
            _hashCode += getRastreoNo().hashCode();
        }
        if (getDatosAdicionales() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDatosAdicionales());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDatosAdicionales(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getCantidadRastreos();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SolicitudEnvio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">SolicitudEnvio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("guiaNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "guiaNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">guiaNo"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rastreoNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "rastreoNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">rastreoNo"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosAdicionales");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "DatosAdicionales"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", ">DatosAdicionales"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cantidadRastreos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/", "cantidadRastreos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
