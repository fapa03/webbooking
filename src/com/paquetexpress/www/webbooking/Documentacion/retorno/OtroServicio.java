/**
 * OtroServicio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion.retorno;

public class OtroServicio  implements java.io.Serializable {
    private java.lang.String clave;

    private java.lang.String descripcion;

    private java.lang.Double serviceAmnt;

    private java.lang.Double otrTypeAmtDisc;

    private java.lang.Double otrTypeAmtTax;

    private java.lang.Double otrTypeAmtRetTax;

    public OtroServicio() {
    }

    public OtroServicio(
           java.lang.String clave,
           java.lang.String descripcion,
           java.lang.Double serviceAmnt,
           java.lang.Double otrTypeAmtDisc,
           java.lang.Double otrTypeAmtTax,
           java.lang.Double otrTypeAmtRetTax) {
           this.clave = clave;
           this.descripcion = descripcion;
           this.serviceAmnt = serviceAmnt;
           this.otrTypeAmtDisc = otrTypeAmtDisc;
           this.otrTypeAmtTax = otrTypeAmtTax;
           this.otrTypeAmtRetTax = otrTypeAmtRetTax;
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
     * Gets the serviceAmnt value for this OtroServicio.
     * 
     * @return serviceAmnt
     */
    public java.lang.Double getServiceAmnt() {
        return serviceAmnt;
    }


    /**
     * Sets the serviceAmnt value for this OtroServicio.
     * 
     * @param serviceAmnt
     */
    public void setServiceAmnt(java.lang.Double serviceAmnt) {
        this.serviceAmnt = serviceAmnt;
    }


    /**
     * Gets the otrTypeAmtDisc value for this OtroServicio.
     * 
     * @return otrTypeAmtDisc
     */
    public java.lang.Double getOtrTypeAmtDisc() {
        return otrTypeAmtDisc;
    }


    /**
     * Sets the otrTypeAmtDisc value for this OtroServicio.
     * 
     * @param otrTypeAmtDisc
     */
    public void setOtrTypeAmtDisc(java.lang.Double otrTypeAmtDisc) {
        this.otrTypeAmtDisc = otrTypeAmtDisc;
    }


    /**
     * Gets the otrTypeAmtTax value for this OtroServicio.
     * 
     * @return otrTypeAmtTax
     */
    public java.lang.Double getOtrTypeAmtTax() {
        return otrTypeAmtTax;
    }


    /**
     * Sets the otrTypeAmtTax value for this OtroServicio.
     * 
     * @param otrTypeAmtTax
     */
    public void setOtrTypeAmtTax(java.lang.Double otrTypeAmtTax) {
        this.otrTypeAmtTax = otrTypeAmtTax;
    }


    /**
     * Gets the otrTypeAmtRetTax value for this OtroServicio.
     * 
     * @return otrTypeAmtRetTax
     */
    public java.lang.Double getOtrTypeAmtRetTax() {
        return otrTypeAmtRetTax;
    }


    /**
     * Sets the otrTypeAmtRetTax value for this OtroServicio.
     * 
     * @param otrTypeAmtRetTax
     */
    public void setOtrTypeAmtRetTax(java.lang.Double otrTypeAmtRetTax) {
        this.otrTypeAmtRetTax = otrTypeAmtRetTax;
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
            ((this.serviceAmnt==null && other.getServiceAmnt()==null) || 
             (this.serviceAmnt!=null &&
              this.serviceAmnt.equals(other.getServiceAmnt()))) &&
            ((this.otrTypeAmtDisc==null && other.getOtrTypeAmtDisc()==null) || 
             (this.otrTypeAmtDisc!=null &&
              this.otrTypeAmtDisc.equals(other.getOtrTypeAmtDisc()))) &&
            ((this.otrTypeAmtTax==null && other.getOtrTypeAmtTax()==null) || 
             (this.otrTypeAmtTax!=null &&
              this.otrTypeAmtTax.equals(other.getOtrTypeAmtTax()))) &&
            ((this.otrTypeAmtRetTax==null && other.getOtrTypeAmtRetTax()==null) || 
             (this.otrTypeAmtRetTax!=null &&
              this.otrTypeAmtRetTax.equals(other.getOtrTypeAmtRetTax())));
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
        if (getServiceAmnt() != null) {
            _hashCode += getServiceAmnt().hashCode();
        }
        if (getOtrTypeAmtDisc() != null) {
            _hashCode += getOtrTypeAmtDisc().hashCode();
        }
        if (getOtrTypeAmtTax() != null) {
            _hashCode += getOtrTypeAmtTax().hashCode();
        }
        if (getOtrTypeAmtRetTax() != null) {
            _hashCode += getOtrTypeAmtRetTax().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OtroServicio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">OtroServicio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clave");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "clave"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">clave"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">descripcion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceAmnt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "serviceAmnt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("otrTypeAmtDisc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "otrTypeAmtDisc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("otrTypeAmtTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "otrTypeAmtTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("otrTypeAmtRetTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "otrTypeAmtRetTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
