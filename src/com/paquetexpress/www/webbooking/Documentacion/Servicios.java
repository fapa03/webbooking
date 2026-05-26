/**
 * Servicios.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion;

public class Servicios  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.Documentacion.OtrosServicios otrosServicios;

    private java.lang.String dlvyType;

    private java.lang.String ackType;

    private java.lang.Double codAmount;

    private java.lang.Double totlDeclVlue;

    private java.lang.String invType;

    private java.lang.String comments;

    private java.lang.String reference;

    private com.paquetexpress.www.webbooking.Documentacion.FormaPago formaPago;

    private com.paquetexpress.www.webbooking.Documentacion.Referencias referencias;

    public Servicios() {
    }

    public Servicios(
           com.paquetexpress.www.webbooking.Documentacion.OtrosServicios otrosServicios,
           java.lang.String dlvyType,
           java.lang.String ackType,
           java.lang.Double codAmount,
           java.lang.Double totlDeclVlue,
           java.lang.String invType,
           java.lang.String comments,
           java.lang.String reference,
           com.paquetexpress.www.webbooking.Documentacion.FormaPago formaPago,
           com.paquetexpress.www.webbooking.Documentacion.Referencias referencias) {
           this.otrosServicios = otrosServicios;
           this.dlvyType = dlvyType;
           this.ackType = ackType;
           this.codAmount = codAmount;
           this.totlDeclVlue = totlDeclVlue;
           this.invType = invType;
           this.comments = comments;
           this.reference = reference;
           this.formaPago = formaPago;
           this.referencias = referencias;
    }


    /**
     * Gets the otrosServicios value for this Servicios.
     * 
     * @return otrosServicios
     */
    public com.paquetexpress.www.webbooking.Documentacion.OtrosServicios getOtrosServicios() {
        return otrosServicios;
    }


    /**
     * Sets the otrosServicios value for this Servicios.
     * 
     * @param otrosServicios
     */
    public void setOtrosServicios(com.paquetexpress.www.webbooking.Documentacion.OtrosServicios otrosServicios) {
        this.otrosServicios = otrosServicios;
    }


    /**
     * Gets the dlvyType value for this Servicios.
     * 
     * @return dlvyType
     */
    public java.lang.String getDlvyType() {
        return dlvyType;
    }


    /**
     * Sets the dlvyType value for this Servicios.
     * 
     * @param dlvyType
     */
    public void setDlvyType(java.lang.String dlvyType) {
        this.dlvyType = dlvyType;
    }


    /**
     * Gets the ackType value for this Servicios.
     * 
     * @return ackType
     */
    public java.lang.String getAckType() {
        return ackType;
    }


    /**
     * Sets the ackType value for this Servicios.
     * 
     * @param ackType
     */
    public void setAckType(java.lang.String ackType) {
        this.ackType = ackType;
    }


    /**
     * Gets the codAmount value for this Servicios.
     * 
     * @return codAmount
     */
    public java.lang.Double getCodAmount() {
        return codAmount;
    }


    /**
     * Sets the codAmount value for this Servicios.
     * 
     * @param codAmount
     */
    public void setCodAmount(java.lang.Double codAmount) {
        this.codAmount = codAmount;
    }


    /**
     * Gets the totlDeclVlue value for this Servicios.
     * 
     * @return totlDeclVlue
     */
    public java.lang.Double getTotlDeclVlue() {
        return totlDeclVlue;
    }


    /**
     * Sets the totlDeclVlue value for this Servicios.
     * 
     * @param totlDeclVlue
     */
    public void setTotlDeclVlue(java.lang.Double totlDeclVlue) {
        this.totlDeclVlue = totlDeclVlue;
    }


    /**
     * Gets the invType value for this Servicios.
     * 
     * @return invType
     */
    public java.lang.String getInvType() {
        return invType;
    }


    /**
     * Sets the invType value for this Servicios.
     * 
     * @param invType
     */
    public void setInvType(java.lang.String invType) {
        this.invType = invType;
    }


    /**
     * Gets the comments value for this Servicios.
     * 
     * @return comments
     */
    public java.lang.String getComments() {
        return comments;
    }


    /**
     * Sets the comments value for this Servicios.
     * 
     * @param comments
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }


    /**
     * Gets the reference value for this Servicios.
     * 
     * @return reference
     */
    public java.lang.String getReference() {
        return reference;
    }


    /**
     * Sets the reference value for this Servicios.
     * 
     * @param reference
     */
    public void setReference(java.lang.String reference) {
        this.reference = reference;
    }


    /**
     * Gets the formaPago value for this Servicios.
     * 
     * @return formaPago
     */
    public com.paquetexpress.www.webbooking.Documentacion.FormaPago getFormaPago() {
        return formaPago;
    }


    /**
     * Sets the formaPago value for this Servicios.
     * 
     * @param formaPago
     */
    public void setFormaPago(com.paquetexpress.www.webbooking.Documentacion.FormaPago formaPago) {
        this.formaPago = formaPago;
    }


    /**
     * Gets the referencias value for this Servicios.
     * 
     * @return referencias
     */
    public com.paquetexpress.www.webbooking.Documentacion.Referencias getReferencias() {
        return referencias;
    }


    /**
     * Sets the referencias value for this Servicios.
     * 
     * @param referencias
     */
    public void setReferencias(com.paquetexpress.www.webbooking.Documentacion.Referencias referencias) {
        this.referencias = referencias;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Servicios)) return false;
        Servicios other = (Servicios) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.otrosServicios==null && other.getOtrosServicios()==null) || 
             (this.otrosServicios!=null &&
              this.otrosServicios.equals(other.getOtrosServicios()))) &&
            ((this.dlvyType==null && other.getDlvyType()==null) || 
             (this.dlvyType!=null &&
              this.dlvyType.equals(other.getDlvyType()))) &&
            ((this.ackType==null && other.getAckType()==null) || 
             (this.ackType!=null &&
              this.ackType.equals(other.getAckType()))) &&
            ((this.codAmount==null && other.getCodAmount()==null) || 
             (this.codAmount!=null &&
              this.codAmount.equals(other.getCodAmount()))) &&
            ((this.totlDeclVlue==null && other.getTotlDeclVlue()==null) || 
             (this.totlDeclVlue!=null &&
              this.totlDeclVlue.equals(other.getTotlDeclVlue()))) &&
            ((this.invType==null && other.getInvType()==null) || 
             (this.invType!=null &&
              this.invType.equals(other.getInvType()))) &&
            ((this.comments==null && other.getComments()==null) || 
             (this.comments!=null &&
              this.comments.equals(other.getComments()))) &&
            ((this.reference==null && other.getReference()==null) || 
             (this.reference!=null &&
              this.reference.equals(other.getReference()))) &&
            ((this.formaPago==null && other.getFormaPago()==null) || 
             (this.formaPago!=null &&
              this.formaPago.equals(other.getFormaPago()))) &&
            ((this.referencias==null && other.getReferencias()==null) || 
             (this.referencias!=null &&
              this.referencias.equals(other.getReferencias())));
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
        if (getOtrosServicios() != null) {
            _hashCode += getOtrosServicios().hashCode();
        }
        if (getDlvyType() != null) {
            _hashCode += getDlvyType().hashCode();
        }
        if (getAckType() != null) {
            _hashCode += getAckType().hashCode();
        }
        if (getCodAmount() != null) {
            _hashCode += getCodAmount().hashCode();
        }
        if (getTotlDeclVlue() != null) {
            _hashCode += getTotlDeclVlue().hashCode();
        }
        if (getInvType() != null) {
            _hashCode += getInvType().hashCode();
        }
        if (getComments() != null) {
            _hashCode += getComments().hashCode();
        }
        if (getReference() != null) {
            _hashCode += getReference().hashCode();
        }
        if (getFormaPago() != null) {
            _hashCode += getFormaPago().hashCode();
        }
        if (getReferencias() != null) {
            _hashCode += getReferencias().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Servicios.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">Servicios"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("otrosServicios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "OtrosServicios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">OtrosServicios"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dlvyType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "dlvyType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">dlvyType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ackType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "ackType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">ackType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "codAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totlDeclVlue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "totlDeclVlue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "invType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">invType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comments");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "comments"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">comments"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "reference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">reference"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formaPago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "formaPago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">formaPago"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referencias");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "Referencias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">Referencias"));
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
