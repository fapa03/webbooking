/**
 * DomicilioDestino.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion;

public class DomicilioDestino  implements java.io.Serializable {
    private java.lang.String pais;

    private java.lang.String estado;

    private java.lang.String ciudad;

    private java.lang.String strtName;

    private java.lang.String drnr;

    private java.lang.String colonyName;

    private java.lang.String zipCode;

    private java.lang.String amMailId;

    private java.lang.String phoneNo;

    private java.lang.String destinationRefDom;

    public DomicilioDestino() {
    }

    public DomicilioDestino(
           java.lang.String pais,
           java.lang.String estado,
           java.lang.String ciudad,
           java.lang.String strtName,
           java.lang.String drnr,
           java.lang.String colonyName,
           java.lang.String zipCode,
           java.lang.String amMailId,
           java.lang.String phoneNo,
           java.lang.String destinationRefDom) {
           this.pais = pais;
           this.estado = estado;
           this.ciudad = ciudad;
           this.strtName = strtName;
           this.drnr = drnr;
           this.colonyName = colonyName;
           this.zipCode = zipCode;
           this.amMailId = amMailId;
           this.phoneNo = phoneNo;
           this.destinationRefDom = destinationRefDom;
    }


    /**
     * Gets the pais value for this DomicilioDestino.
     * 
     * @return pais
     */
    public java.lang.String getPais() {
        return pais;
    }


    /**
     * Sets the pais value for this DomicilioDestino.
     * 
     * @param pais
     */
    public void setPais(java.lang.String pais) {
        this.pais = pais;
    }


    /**
     * Gets the estado value for this DomicilioDestino.
     * 
     * @return estado
     */
    public java.lang.String getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this DomicilioDestino.
     * 
     * @param estado
     */
    public void setEstado(java.lang.String estado) {
        this.estado = estado;
    }


    /**
     * Gets the ciudad value for this DomicilioDestino.
     * 
     * @return ciudad
     */
    public java.lang.String getCiudad() {
        return ciudad;
    }


    /**
     * Sets the ciudad value for this DomicilioDestino.
     * 
     * @param ciudad
     */
    public void setCiudad(java.lang.String ciudad) {
        this.ciudad = ciudad;
    }


    /**
     * Gets the strtName value for this DomicilioDestino.
     * 
     * @return strtName
     */
    public java.lang.String getStrtName() {
        return strtName;
    }


    /**
     * Sets the strtName value for this DomicilioDestino.
     * 
     * @param strtName
     */
    public void setStrtName(java.lang.String strtName) {
        this.strtName = strtName;
    }


    /**
     * Gets the drnr value for this DomicilioDestino.
     * 
     * @return drnr
     */
    public java.lang.String getDrnr() {
        return drnr;
    }


    /**
     * Sets the drnr value for this DomicilioDestino.
     * 
     * @param drnr
     */
    public void setDrnr(java.lang.String drnr) {
        this.drnr = drnr;
    }


    /**
     * Gets the colonyName value for this DomicilioDestino.
     * 
     * @return colonyName
     */
    public java.lang.String getColonyName() {
        return colonyName;
    }


    /**
     * Sets the colonyName value for this DomicilioDestino.
     * 
     * @param colonyName
     */
    public void setColonyName(java.lang.String colonyName) {
        this.colonyName = colonyName;
    }


    /**
     * Gets the zipCode value for this DomicilioDestino.
     * 
     * @return zipCode
     */
    public java.lang.String getZipCode() {
        return zipCode;
    }


    /**
     * Sets the zipCode value for this DomicilioDestino.
     * 
     * @param zipCode
     */
    public void setZipCode(java.lang.String zipCode) {
        this.zipCode = zipCode;
    }


    /**
     * Gets the amMailId value for this DomicilioDestino.
     * 
     * @return amMailId
     */
    public java.lang.String getAmMailId() {
        return amMailId;
    }


    /**
     * Sets the amMailId value for this DomicilioDestino.
     * 
     * @param amMailId
     */
    public void setAmMailId(java.lang.String amMailId) {
        this.amMailId = amMailId;
    }


    /**
     * Gets the phoneNo value for this DomicilioDestino.
     * 
     * @return phoneNo
     */
    public java.lang.String getPhoneNo() {
        return phoneNo;
    }


    /**
     * Sets the phoneNo value for this DomicilioDestino.
     * 
     * @param phoneNo
     */
    public void setPhoneNo(java.lang.String phoneNo) {
        this.phoneNo = phoneNo;
    }


    /**
     * Gets the destinationRefDom value for this DomicilioDestino.
     * 
     * @return destinationRefDom
     */
    public java.lang.String getDestinationRefDom() {
        return destinationRefDom;
    }


    /**
     * Sets the destinationRefDom value for this DomicilioDestino.
     * 
     * @param destinationRefDom
     */
    public void setDestinationRefDom(java.lang.String destinationRefDom) {
        this.destinationRefDom = destinationRefDom;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DomicilioDestino)) return false;
        DomicilioDestino other = (DomicilioDestino) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.pais==null && other.getPais()==null) || 
             (this.pais!=null &&
              this.pais.equals(other.getPais()))) &&
            ((this.estado==null && other.getEstado()==null) || 
             (this.estado!=null &&
              this.estado.equals(other.getEstado()))) &&
            ((this.ciudad==null && other.getCiudad()==null) || 
             (this.ciudad!=null &&
              this.ciudad.equals(other.getCiudad()))) &&
            ((this.strtName==null && other.getStrtName()==null) || 
             (this.strtName!=null &&
              this.strtName.equals(other.getStrtName()))) &&
            ((this.drnr==null && other.getDrnr()==null) || 
             (this.drnr!=null &&
              this.drnr.equals(other.getDrnr()))) &&
            ((this.colonyName==null && other.getColonyName()==null) || 
             (this.colonyName!=null &&
              this.colonyName.equals(other.getColonyName()))) &&
            ((this.zipCode==null && other.getZipCode()==null) || 
             (this.zipCode!=null &&
              this.zipCode.equals(other.getZipCode()))) &&
            ((this.amMailId==null && other.getAmMailId()==null) || 
             (this.amMailId!=null &&
              this.amMailId.equals(other.getAmMailId()))) &&
            ((this.phoneNo==null && other.getPhoneNo()==null) || 
             (this.phoneNo!=null &&
              this.phoneNo.equals(other.getPhoneNo()))) &&
            ((this.destinationRefDom==null && other.getDestinationRefDom()==null) || 
             (this.destinationRefDom!=null &&
              this.destinationRefDom.equals(other.getDestinationRefDom())));
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
        if (getPais() != null) {
            _hashCode += getPais().hashCode();
        }
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        if (getCiudad() != null) {
            _hashCode += getCiudad().hashCode();
        }
        if (getStrtName() != null) {
            _hashCode += getStrtName().hashCode();
        }
        if (getDrnr() != null) {
            _hashCode += getDrnr().hashCode();
        }
        if (getColonyName() != null) {
            _hashCode += getColonyName().hashCode();
        }
        if (getZipCode() != null) {
            _hashCode += getZipCode().hashCode();
        }
        if (getAmMailId() != null) {
            _hashCode += getAmMailId().hashCode();
        }
        if (getPhoneNo() != null) {
            _hashCode += getPhoneNo().hashCode();
        }
        if (getDestinationRefDom() != null) {
            _hashCode += getDestinationRefDom().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DomicilioDestino.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">DomicilioDestino"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pais");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "pais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">pais"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">estado"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ciudad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "ciudad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">ciudad"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("strtName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "strtName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">strtName"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drnr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "drnr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">drnr"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("colonyName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "colonyName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">colonyName"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("zipCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "zipCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">zipCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amMailId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "amMailId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">amMailId"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phoneNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "phoneNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">phoneNo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationRefDom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "destinationRefDom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">destinationRefDom"));
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
