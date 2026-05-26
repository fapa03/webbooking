/**
 * RastreoRetorno.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.GuiasRetorno.retorno;

public class RastreoRetorno  implements java.io.Serializable {
    private java.lang.String rastreoNo;

    private java.lang.String guiaNo;

    private com.paquetexpress.www.webbooking.GuiasRetorno.retorno.Embarque[] detalleEmbarque;

    private com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DetalleDestino detalleDestino;

    private java.lang.String cadenaImpresion;

    private com.paquetexpress.www.webbooking.GuiasRetorno.retorno.Mensaje[] mensajes;

    private com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DatoAdicional[] datosAdicionales;

    public RastreoRetorno() {
    }

    public RastreoRetorno(
           java.lang.String rastreoNo,
           java.lang.String guiaNo,
           com.paquetexpress.www.webbooking.GuiasRetorno.retorno.Embarque[] detalleEmbarque,
           com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DetalleDestino detalleDestino,
           java.lang.String cadenaImpresion,
           com.paquetexpress.www.webbooking.GuiasRetorno.retorno.Mensaje[] mensajes,
           com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DatoAdicional[] datosAdicionales) {
           this.rastreoNo = rastreoNo;
           this.guiaNo = guiaNo;
           this.detalleEmbarque = detalleEmbarque;
           this.detalleDestino = detalleDestino;
           this.cadenaImpresion = cadenaImpresion;
           this.mensajes = mensajes;
           this.datosAdicionales = datosAdicionales;
    }


    /**
     * Gets the rastreoNo value for this RastreoRetorno.
     * 
     * @return rastreoNo
     */
    public java.lang.String getRastreoNo() {
        return rastreoNo;
    }


    /**
     * Sets the rastreoNo value for this RastreoRetorno.
     * 
     * @param rastreoNo
     */
    public void setRastreoNo(java.lang.String rastreoNo) {
        this.rastreoNo = rastreoNo;
    }


    /**
     * Gets the guiaNo value for this RastreoRetorno.
     * 
     * @return guiaNo
     */
    public java.lang.String getGuiaNo() {
        return guiaNo;
    }


    /**
     * Sets the guiaNo value for this RastreoRetorno.
     * 
     * @param guiaNo
     */
    public void setGuiaNo(java.lang.String guiaNo) {
        this.guiaNo = guiaNo;
    }


    /**
     * Gets the detalleEmbarque value for this RastreoRetorno.
     * 
     * @return detalleEmbarque
     */
    public com.paquetexpress.www.webbooking.GuiasRetorno.retorno.Embarque[] getDetalleEmbarque() {
        return detalleEmbarque;
    }


    /**
     * Sets the detalleEmbarque value for this RastreoRetorno.
     * 
     * @param detalleEmbarque
     */
    public void setDetalleEmbarque(com.paquetexpress.www.webbooking.GuiasRetorno.retorno.Embarque[] detalleEmbarque) {
        this.detalleEmbarque = detalleEmbarque;
    }


    /**
     * Gets the detalleDestino value for this RastreoRetorno.
     * 
     * @return detalleDestino
     */
    public com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DetalleDestino getDetalleDestino() {
        return detalleDestino;
    }


    /**
     * Sets the detalleDestino value for this RastreoRetorno.
     * 
     * @param detalleDestino
     */
    public void setDetalleDestino(com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DetalleDestino detalleDestino) {
        this.detalleDestino = detalleDestino;
    }


    /**
     * Gets the cadenaImpresion value for this RastreoRetorno.
     * 
     * @return cadenaImpresion
     */
    public java.lang.String getCadenaImpresion() {
        return cadenaImpresion;
    }


    /**
     * Sets the cadenaImpresion value for this RastreoRetorno.
     * 
     * @param cadenaImpresion
     */
    public void setCadenaImpresion(java.lang.String cadenaImpresion) {
        this.cadenaImpresion = cadenaImpresion;
    }


    /**
     * Gets the mensajes value for this RastreoRetorno.
     * 
     * @return mensajes
     */
    public com.paquetexpress.www.webbooking.GuiasRetorno.retorno.Mensaje[] getMensajes() {
        return mensajes;
    }


    /**
     * Sets the mensajes value for this RastreoRetorno.
     * 
     * @param mensajes
     */
    public void setMensajes(com.paquetexpress.www.webbooking.GuiasRetorno.retorno.Mensaje[] mensajes) {
        this.mensajes = mensajes;
    }


    /**
     * Gets the datosAdicionales value for this RastreoRetorno.
     * 
     * @return datosAdicionales
     */
    public com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DatoAdicional[] getDatosAdicionales() {
        return datosAdicionales;
    }


    /**
     * Sets the datosAdicionales value for this RastreoRetorno.
     * 
     * @param datosAdicionales
     */
    public void setDatosAdicionales(com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DatoAdicional[] datosAdicionales) {
        this.datosAdicionales = datosAdicionales;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RastreoRetorno)) return false;
        RastreoRetorno other = (RastreoRetorno) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.rastreoNo==null && other.getRastreoNo()==null) || 
             (this.rastreoNo!=null &&
              this.rastreoNo.equals(other.getRastreoNo()))) &&
            ((this.guiaNo==null && other.getGuiaNo()==null) || 
             (this.guiaNo!=null &&
              this.guiaNo.equals(other.getGuiaNo()))) &&
            ((this.detalleEmbarque==null && other.getDetalleEmbarque()==null) || 
             (this.detalleEmbarque!=null &&
              java.util.Arrays.equals(this.detalleEmbarque, other.getDetalleEmbarque()))) &&
            ((this.detalleDestino==null && other.getDetalleDestino()==null) || 
             (this.detalleDestino!=null &&
              this.detalleDestino.equals(other.getDetalleDestino()))) &&
            ((this.cadenaImpresion==null && other.getCadenaImpresion()==null) || 
             (this.cadenaImpresion!=null &&
              this.cadenaImpresion.equals(other.getCadenaImpresion()))) &&
            ((this.mensajes==null && other.getMensajes()==null) || 
             (this.mensajes!=null &&
              java.util.Arrays.equals(this.mensajes, other.getMensajes()))) &&
            ((this.datosAdicionales==null && other.getDatosAdicionales()==null) || 
             (this.datosAdicionales!=null &&
              java.util.Arrays.equals(this.datosAdicionales, other.getDatosAdicionales())));
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
        if (getRastreoNo() != null) {
            _hashCode += getRastreoNo().hashCode();
        }
        if (getGuiaNo() != null) {
            _hashCode += getGuiaNo().hashCode();
        }
        if (getDetalleEmbarque() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDetalleEmbarque());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDetalleEmbarque(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDetalleDestino() != null) {
            _hashCode += getDetalleDestino().hashCode();
        }
        if (getCadenaImpresion() != null) {
            _hashCode += getCadenaImpresion().hashCode();
        }
        if (getMensajes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMensajes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMensajes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RastreoRetorno.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">RastreoRetorno"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rastreoNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "rastreoNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">rastreoNo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("guiaNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "guiaNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">guiaNo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detalleEmbarque");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "DetalleEmbarque"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">DetalleEmbarque"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detalleDestino");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "DetalleDestino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">DetalleDestino"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cadenaImpresion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "cadenaImpresion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">cadenaImpresion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensajes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "Mensajes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">Mensajes"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosAdicionales");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "DatosAdicionales"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">DatosAdicionales"));
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
