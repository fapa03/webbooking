/**
 * Mensaje.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.GuiasRetorno.retorno;

public class Mensaje  implements java.io.Serializable {
    private java.lang.Integer cveMsjeRetorno;

    private java.lang.String desMsjeRetorno;

    private java.lang.String tratamientoMsje;

    private java.lang.String tipoMsje;

    public Mensaje() {
    }

    public Mensaje(
           java.lang.Integer cveMsjeRetorno,
           java.lang.String desMsjeRetorno,
           java.lang.String tratamientoMsje,
           java.lang.String tipoMsje) {
           this.cveMsjeRetorno = cveMsjeRetorno;
           this.desMsjeRetorno = desMsjeRetorno;
           this.tratamientoMsje = tratamientoMsje;
           this.tipoMsje = tipoMsje;
    }


    /**
     * Gets the cveMsjeRetorno value for this Mensaje.
     * 
     * @return cveMsjeRetorno
     */
    public java.lang.Integer getCveMsjeRetorno() {
        return cveMsjeRetorno;
    }


    /**
     * Sets the cveMsjeRetorno value for this Mensaje.
     * 
     * @param cveMsjeRetorno
     */
    public void setCveMsjeRetorno(java.lang.Integer cveMsjeRetorno) {
        this.cveMsjeRetorno = cveMsjeRetorno;
    }


    /**
     * Gets the desMsjeRetorno value for this Mensaje.
     * 
     * @return desMsjeRetorno
     */
    public java.lang.String getDesMsjeRetorno() {
        return desMsjeRetorno;
    }


    /**
     * Sets the desMsjeRetorno value for this Mensaje.
     * 
     * @param desMsjeRetorno
     */
    public void setDesMsjeRetorno(java.lang.String desMsjeRetorno) {
        this.desMsjeRetorno = desMsjeRetorno;
    }


    /**
     * Gets the tratamientoMsje value for this Mensaje.
     * 
     * @return tratamientoMsje
     */
    public java.lang.String getTratamientoMsje() {
        return tratamientoMsje;
    }


    /**
     * Sets the tratamientoMsje value for this Mensaje.
     * 
     * @param tratamientoMsje
     */
    public void setTratamientoMsje(java.lang.String tratamientoMsje) {
        this.tratamientoMsje = tratamientoMsje;
    }


    /**
     * Gets the tipoMsje value for this Mensaje.
     * 
     * @return tipoMsje
     */
    public java.lang.String getTipoMsje() {
        return tipoMsje;
    }


    /**
     * Sets the tipoMsje value for this Mensaje.
     * 
     * @param tipoMsje
     */
    public void setTipoMsje(java.lang.String tipoMsje) {
        this.tipoMsje = tipoMsje;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Mensaje)) return false;
        Mensaje other = (Mensaje) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cveMsjeRetorno==null && other.getCveMsjeRetorno()==null) || 
             (this.cveMsjeRetorno!=null &&
              this.cveMsjeRetorno.equals(other.getCveMsjeRetorno()))) &&
            ((this.desMsjeRetorno==null && other.getDesMsjeRetorno()==null) || 
             (this.desMsjeRetorno!=null &&
              this.desMsjeRetorno.equals(other.getDesMsjeRetorno()))) &&
            ((this.tratamientoMsje==null && other.getTratamientoMsje()==null) || 
             (this.tratamientoMsje!=null &&
              this.tratamientoMsje.equals(other.getTratamientoMsje()))) &&
            ((this.tipoMsje==null && other.getTipoMsje()==null) || 
             (this.tipoMsje!=null &&
              this.tipoMsje.equals(other.getTipoMsje())));
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
        if (getCveMsjeRetorno() != null) {
            _hashCode += getCveMsjeRetorno().hashCode();
        }
        if (getDesMsjeRetorno() != null) {
            _hashCode += getDesMsjeRetorno().hashCode();
        }
        if (getTratamientoMsje() != null) {
            _hashCode += getTratamientoMsje().hashCode();
        }
        if (getTipoMsje() != null) {
            _hashCode += getTipoMsje().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Mensaje.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">Mensaje"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cveMsjeRetorno");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "cveMsjeRetorno"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desMsjeRetorno");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "desMsjeRetorno"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">desMsjeRetorno"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tratamientoMsje");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "tratamientoMsje"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">tratamientoMsje"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoMsje");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "tipoMsje"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">tipoMsje"));
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
