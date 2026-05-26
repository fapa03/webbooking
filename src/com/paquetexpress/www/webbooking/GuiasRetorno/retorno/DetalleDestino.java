/**
 * DetalleDestino.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.GuiasRetorno.retorno;

public class DetalleDestino  implements java.io.Serializable {
    private java.lang.String siteId;

    private java.lang.String siteDescr;

    private java.lang.String branchId;

    private java.lang.String branchDescr;

    private java.lang.String clntName;

    private java.lang.String importe;

    private java.lang.String fechaCreacion;

    public DetalleDestino() {
    }

    public DetalleDestino(
           java.lang.String siteId,
           java.lang.String siteDescr,
           java.lang.String branchId,
           java.lang.String branchDescr,
           java.lang.String clntName,
           java.lang.String importe,
           java.lang.String fechaCreacion) {
           this.siteId = siteId;
           this.siteDescr = siteDescr;
           this.branchId = branchId;
           this.branchDescr = branchDescr;
           this.clntName = clntName;
           this.importe = importe;
           this.fechaCreacion = fechaCreacion;
    }


    /**
     * Gets the siteId value for this DetalleDestino.
     * 
     * @return siteId
     */
    public java.lang.String getSiteId() {
        return siteId;
    }


    /**
     * Sets the siteId value for this DetalleDestino.
     * 
     * @param siteId
     */
    public void setSiteId(java.lang.String siteId) {
        this.siteId = siteId;
    }


    /**
     * Gets the siteDescr value for this DetalleDestino.
     * 
     * @return siteDescr
     */
    public java.lang.String getSiteDescr() {
        return siteDescr;
    }


    /**
     * Sets the siteDescr value for this DetalleDestino.
     * 
     * @param siteDescr
     */
    public void setSiteDescr(java.lang.String siteDescr) {
        this.siteDescr = siteDescr;
    }


    /**
     * Gets the branchId value for this DetalleDestino.
     * 
     * @return branchId
     */
    public java.lang.String getBranchId() {
        return branchId;
    }


    /**
     * Sets the branchId value for this DetalleDestino.
     * 
     * @param branchId
     */
    public void setBranchId(java.lang.String branchId) {
        this.branchId = branchId;
    }


    /**
     * Gets the branchDescr value for this DetalleDestino.
     * 
     * @return branchDescr
     */
    public java.lang.String getBranchDescr() {
        return branchDescr;
    }


    /**
     * Sets the branchDescr value for this DetalleDestino.
     * 
     * @param branchDescr
     */
    public void setBranchDescr(java.lang.String branchDescr) {
        this.branchDescr = branchDescr;
    }


    /**
     * Gets the clntName value for this DetalleDestino.
     * 
     * @return clntName
     */
    public java.lang.String getClntName() {
        return clntName;
    }


    /**
     * Sets the clntName value for this DetalleDestino.
     * 
     * @param clntName
     */
    public void setClntName(java.lang.String clntName) {
        this.clntName = clntName;
    }


    /**
     * Gets the importe value for this DetalleDestino.
     * 
     * @return importe
     */
    public java.lang.String getImporte() {
        return importe;
    }


    /**
     * Sets the importe value for this DetalleDestino.
     * 
     * @param importe
     */
    public void setImporte(java.lang.String importe) {
        this.importe = importe;
    }


    /**
     * Gets the fechaCreacion value for this DetalleDestino.
     * 
     * @return fechaCreacion
     */
    public java.lang.String getFechaCreacion() {
        return fechaCreacion;
    }


    /**
     * Sets the fechaCreacion value for this DetalleDestino.
     * 
     * @param fechaCreacion
     */
    public void setFechaCreacion(java.lang.String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetalleDestino)) return false;
        DetalleDestino other = (DetalleDestino) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.siteId==null && other.getSiteId()==null) || 
             (this.siteId!=null &&
              this.siteId.equals(other.getSiteId()))) &&
            ((this.siteDescr==null && other.getSiteDescr()==null) || 
             (this.siteDescr!=null &&
              this.siteDescr.equals(other.getSiteDescr()))) &&
            ((this.branchId==null && other.getBranchId()==null) || 
             (this.branchId!=null &&
              this.branchId.equals(other.getBranchId()))) &&
            ((this.branchDescr==null && other.getBranchDescr()==null) || 
             (this.branchDescr!=null &&
              this.branchDescr.equals(other.getBranchDescr()))) &&
            ((this.clntName==null && other.getClntName()==null) || 
             (this.clntName!=null &&
              this.clntName.equals(other.getClntName()))) &&
            ((this.importe==null && other.getImporte()==null) || 
             (this.importe!=null &&
              this.importe.equals(other.getImporte()))) &&
            ((this.fechaCreacion==null && other.getFechaCreacion()==null) || 
             (this.fechaCreacion!=null &&
              this.fechaCreacion.equals(other.getFechaCreacion())));
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
        if (getSiteId() != null) {
            _hashCode += getSiteId().hashCode();
        }
        if (getSiteDescr() != null) {
            _hashCode += getSiteDescr().hashCode();
        }
        if (getBranchId() != null) {
            _hashCode += getBranchId().hashCode();
        }
        if (getBranchDescr() != null) {
            _hashCode += getBranchDescr().hashCode();
        }
        if (getClntName() != null) {
            _hashCode += getClntName().hashCode();
        }
        if (getImporte() != null) {
            _hashCode += getImporte().hashCode();
        }
        if (getFechaCreacion() != null) {
            _hashCode += getFechaCreacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DetalleDestino.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">DetalleDestino"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("siteId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "siteId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">siteId"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("siteDescr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "siteDescr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">siteDescr"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("branchId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "branchId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">branchId"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("branchDescr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "branchDescr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">branchDescr"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clntName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "clntName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">clntName"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importe");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "importe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">importe"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaCreacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "fechaCreacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">fechaCreacion"));
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
