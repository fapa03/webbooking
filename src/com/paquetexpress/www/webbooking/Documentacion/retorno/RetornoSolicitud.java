/**
 * RetornoSolicitud.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion.retorno;

public class RetornoSolicitud  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.Documentacion.retorno.DetalleEmbarque detalleEmbarque;

    private com.paquetexpress.www.webbooking.Documentacion.retorno.Servicios servicios;

    private com.paquetexpress.www.webbooking.Documentacion.retorno.Mensajes mensajes;

    private java.lang.String formNo;

    private java.lang.String guiaNo;

    private java.lang.String guiaNoTemp;

    private java.lang.String cadenaImpresion;

    public RetornoSolicitud() {
    }

    public RetornoSolicitud(
           com.paquetexpress.www.webbooking.Documentacion.retorno.DetalleEmbarque detalleEmbarque,
           com.paquetexpress.www.webbooking.Documentacion.retorno.Servicios servicios,
           com.paquetexpress.www.webbooking.Documentacion.retorno.Mensajes mensajes,
           java.lang.String formNo,
           java.lang.String guiaNo,
           java.lang.String guiaNoTemp,
           java.lang.String cadenaImpresion) {
           this.detalleEmbarque = detalleEmbarque;
           this.servicios = servicios;
           this.mensajes = mensajes;
           this.formNo = formNo;
           this.guiaNo = guiaNo;
           this.guiaNoTemp = guiaNoTemp;
           this.cadenaImpresion = cadenaImpresion;
    }


    /**
     * Gets the detalleEmbarque value for this RetornoSolicitud.
     * 
     * @return detalleEmbarque
     */
    public com.paquetexpress.www.webbooking.Documentacion.retorno.DetalleEmbarque getDetalleEmbarque() {
        return detalleEmbarque;
    }


    /**
     * Sets the detalleEmbarque value for this RetornoSolicitud.
     * 
     * @param detalleEmbarque
     */
    public void setDetalleEmbarque(com.paquetexpress.www.webbooking.Documentacion.retorno.DetalleEmbarque detalleEmbarque) {
        this.detalleEmbarque = detalleEmbarque;
    }


    /**
     * Gets the servicios value for this RetornoSolicitud.
     * 
     * @return servicios
     */
    public com.paquetexpress.www.webbooking.Documentacion.retorno.Servicios getServicios() {
        return servicios;
    }


    /**
     * Sets the servicios value for this RetornoSolicitud.
     * 
     * @param servicios
     */
    public void setServicios(com.paquetexpress.www.webbooking.Documentacion.retorno.Servicios servicios) {
        this.servicios = servicios;
    }


    /**
     * Gets the mensajes value for this RetornoSolicitud.
     * 
     * @return mensajes
     */
    public com.paquetexpress.www.webbooking.Documentacion.retorno.Mensajes getMensajes() {
        return mensajes;
    }


    /**
     * Sets the mensajes value for this RetornoSolicitud.
     * 
     * @param mensajes
     */
    public void setMensajes(com.paquetexpress.www.webbooking.Documentacion.retorno.Mensajes mensajes) {
        this.mensajes = mensajes;
    }


    /**
     * Gets the formNo value for this RetornoSolicitud.
     * 
     * @return formNo
     */
    public java.lang.String getFormNo() {
        return formNo;
    }


    /**
     * Sets the formNo value for this RetornoSolicitud.
     * 
     * @param formNo
     */
    public void setFormNo(java.lang.String formNo) {
        this.formNo = formNo;
    }


    /**
     * Gets the guiaNo value for this RetornoSolicitud.
     * 
     * @return guiaNo
     */
    public java.lang.String getGuiaNo() {
        return guiaNo;
    }


    /**
     * Sets the guiaNo value for this RetornoSolicitud.
     * 
     * @param guiaNo
     */
    public void setGuiaNo(java.lang.String guiaNo) {
        this.guiaNo = guiaNo;
    }


    /**
     * Gets the guiaNoTemp value for this RetornoSolicitud.
     * 
     * @return guiaNoTemp
     */
    public java.lang.String getGuiaNoTemp() {
        return guiaNoTemp;
    }


    /**
     * Sets the guiaNoTemp value for this RetornoSolicitud.
     * 
     * @param guiaNoTemp
     */
    public void setGuiaNoTemp(java.lang.String guiaNoTemp) {
        this.guiaNoTemp = guiaNoTemp;
    }


    /**
     * Gets the cadenaImpresion value for this RetornoSolicitud.
     * 
     * @return cadenaImpresion
     */
    public java.lang.String getCadenaImpresion() {
        return cadenaImpresion;
    }


    /**
     * Sets the cadenaImpresion value for this RetornoSolicitud.
     * 
     * @param cadenaImpresion
     */
    public void setCadenaImpresion(java.lang.String cadenaImpresion) {
        this.cadenaImpresion = cadenaImpresion;
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
            ((this.detalleEmbarque==null && other.getDetalleEmbarque()==null) || 
             (this.detalleEmbarque!=null &&
              this.detalleEmbarque.equals(other.getDetalleEmbarque()))) &&
            ((this.servicios==null && other.getServicios()==null) || 
             (this.servicios!=null &&
              this.servicios.equals(other.getServicios()))) &&
            ((this.mensajes==null && other.getMensajes()==null) || 
             (this.mensajes!=null &&
              this.mensajes.equals(other.getMensajes()))) &&
            ((this.formNo==null && other.getFormNo()==null) || 
             (this.formNo!=null &&
              this.formNo.equals(other.getFormNo()))) &&
            ((this.guiaNo==null && other.getGuiaNo()==null) || 
             (this.guiaNo!=null &&
              this.guiaNo.equals(other.getGuiaNo()))) &&
            ((this.guiaNoTemp==null && other.getGuiaNoTemp()==null) || 
             (this.guiaNoTemp!=null &&
              this.guiaNoTemp.equals(other.getGuiaNoTemp()))) &&
            ((this.cadenaImpresion==null && other.getCadenaImpresion()==null) || 
             (this.cadenaImpresion!=null &&
              this.cadenaImpresion.equals(other.getCadenaImpresion())));
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
        if (getDetalleEmbarque() != null) {
            _hashCode += getDetalleEmbarque().hashCode();
        }
        if (getServicios() != null) {
            _hashCode += getServicios().hashCode();
        }
        if (getMensajes() != null) {
            _hashCode += getMensajes().hashCode();
        }
        if (getFormNo() != null) {
            _hashCode += getFormNo().hashCode();
        }
        if (getGuiaNo() != null) {
            _hashCode += getGuiaNo().hashCode();
        }
        if (getGuiaNoTemp() != null) {
            _hashCode += getGuiaNoTemp().hashCode();
        }
        if (getCadenaImpresion() != null) {
            _hashCode += getCadenaImpresion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RetornoSolicitud.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">RetornoSolicitud"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detalleEmbarque");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "DetalleEmbarque"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">DetalleEmbarque"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("servicios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "Servicios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">Servicios"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensajes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "Mensajes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">Mensajes"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "formNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">formNo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("guiaNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "guiaNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">guiaNo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("guiaNoTemp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "guiaNoTemp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">guiaNoTemp"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cadenaImpresion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "cadenaImpresion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">cadenaImpresion"));
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
