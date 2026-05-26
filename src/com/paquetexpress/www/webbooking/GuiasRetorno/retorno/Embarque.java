/**
 * Embarque.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.GuiasRetorno.retorno;

public class Embarque  implements java.io.Serializable {
    private int quantity;

    private java.lang.String shpCode;

    private java.lang.String shpDescr;

    private java.lang.String content;

    private java.lang.String slabNo;

    public Embarque() {
    }

    public Embarque(
           int quantity,
           java.lang.String shpCode,
           java.lang.String shpDescr,
           java.lang.String content,
           java.lang.String slabNo) {
           this.quantity = quantity;
           this.shpCode = shpCode;
           this.shpDescr = shpDescr;
           this.content = content;
           this.slabNo = slabNo;
    }


    /**
     * Gets the quantity value for this Embarque.
     * 
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }


    /**
     * Sets the quantity value for this Embarque.
     * 
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    /**
     * Gets the shpCode value for this Embarque.
     * 
     * @return shpCode
     */
    public java.lang.String getShpCode() {
        return shpCode;
    }


    /**
     * Sets the shpCode value for this Embarque.
     * 
     * @param shpCode
     */
    public void setShpCode(java.lang.String shpCode) {
        this.shpCode = shpCode;
    }


    /**
     * Gets the shpDescr value for this Embarque.
     * 
     * @return shpDescr
     */
    public java.lang.String getShpDescr() {
        return shpDescr;
    }


    /**
     * Sets the shpDescr value for this Embarque.
     * 
     * @param shpDescr
     */
    public void setShpDescr(java.lang.String shpDescr) {
        this.shpDescr = shpDescr;
    }


    /**
     * Gets the content value for this Embarque.
     * 
     * @return content
     */
    public java.lang.String getContent() {
        return content;
    }


    /**
     * Sets the content value for this Embarque.
     * 
     * @param content
     */
    public void setContent(java.lang.String content) {
        this.content = content;
    }


    /**
     * Gets the slabNo value for this Embarque.
     * 
     * @return slabNo
     */
    public java.lang.String getSlabNo() {
        return slabNo;
    }


    /**
     * Sets the slabNo value for this Embarque.
     * 
     * @param slabNo
     */
    public void setSlabNo(java.lang.String slabNo) {
        this.slabNo = slabNo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Embarque)) return false;
        Embarque other = (Embarque) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.quantity == other.getQuantity() &&
            ((this.shpCode==null && other.getShpCode()==null) || 
             (this.shpCode!=null &&
              this.shpCode.equals(other.getShpCode()))) &&
            ((this.shpDescr==null && other.getShpDescr()==null) || 
             (this.shpDescr!=null &&
              this.shpDescr.equals(other.getShpDescr()))) &&
            ((this.content==null && other.getContent()==null) || 
             (this.content!=null &&
              this.content.equals(other.getContent()))) &&
            ((this.slabNo==null && other.getSlabNo()==null) || 
             (this.slabNo!=null &&
              this.slabNo.equals(other.getSlabNo())));
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
        _hashCode += getQuantity();
        if (getShpCode() != null) {
            _hashCode += getShpCode().hashCode();
        }
        if (getShpDescr() != null) {
            _hashCode += getShpDescr().hashCode();
        }
        if (getContent() != null) {
            _hashCode += getContent().hashCode();
        }
        if (getSlabNo() != null) {
            _hashCode += getSlabNo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Embarque.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">Embarque"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "quantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">quantity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "shpCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">shpCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpDescr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "shpDescr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">shpDescr"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">content"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("slabNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", "SlabNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/GuiasRetorno/retorno", ">SlabNo"));
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
