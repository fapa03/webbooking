/**
 * Embarque.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion;

public class Embarque  implements java.io.Serializable {
    private int quantity;

    private java.lang.String shpCode;

    private java.lang.String content;

    private java.lang.String slabNo;

    private java.lang.Double weight;

    private java.lang.Double volume;

    private java.lang.Double longShip;

    private java.lang.Double widthShip;

    private java.lang.Double highShip;

    public Embarque() {
    }

    public Embarque(
           int quantity,
           java.lang.String shpCode,
           java.lang.String content,
           java.lang.String slabNo,
           java.lang.Double weight,
           java.lang.Double volume,
           java.lang.Double longShip,
           java.lang.Double widthShip,
           java.lang.Double highShip) {
           this.quantity = quantity;
           this.shpCode = shpCode;
           this.content = content;
           this.slabNo = slabNo;
           this.weight = weight;
           this.volume = volume;
           this.longShip = longShip;
           this.widthShip = widthShip;
           this.highShip = highShip;
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


    /**
     * Gets the weight value for this Embarque.
     * 
     * @return weight
     */
    public java.lang.Double getWeight() {
        return weight;
    }


    /**
     * Sets the weight value for this Embarque.
     * 
     * @param weight
     */
    public void setWeight(java.lang.Double weight) {
        this.weight = weight;
    }


    /**
     * Gets the volume value for this Embarque.
     * 
     * @return volume
     */
    public java.lang.Double getVolume() {
        return volume;
    }


    /**
     * Sets the volume value for this Embarque.
     * 
     * @param volume
     */
    public void setVolume(java.lang.Double volume) {
        this.volume = volume;
    }


    /**
     * Gets the longShip value for this Embarque.
     * 
     * @return longShip
     */
    public java.lang.Double getLongShip() {
        return longShip;
    }


    /**
     * Sets the longShip value for this Embarque.
     * 
     * @param longShip
     */
    public void setLongShip(java.lang.Double longShip) {
        this.longShip = longShip;
    }


    /**
     * Gets the widthShip value for this Embarque.
     * 
     * @return widthShip
     */
    public java.lang.Double getWidthShip() {
        return widthShip;
    }


    /**
     * Sets the widthShip value for this Embarque.
     * 
     * @param widthShip
     */
    public void setWidthShip(java.lang.Double widthShip) {
        this.widthShip = widthShip;
    }


    /**
     * Gets the highShip value for this Embarque.
     * 
     * @return highShip
     */
    public java.lang.Double getHighShip() {
        return highShip;
    }


    /**
     * Sets the highShip value for this Embarque.
     * 
     * @param highShip
     */
    public void setHighShip(java.lang.Double highShip) {
        this.highShip = highShip;
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
            ((this.content==null && other.getContent()==null) || 
             (this.content!=null &&
              this.content.equals(other.getContent()))) &&
            ((this.slabNo==null && other.getSlabNo()==null) || 
             (this.slabNo!=null &&
              this.slabNo.equals(other.getSlabNo()))) &&
            ((this.weight==null && other.getWeight()==null) || 
             (this.weight!=null &&
              this.weight.equals(other.getWeight()))) &&
            ((this.volume==null && other.getVolume()==null) || 
             (this.volume!=null &&
              this.volume.equals(other.getVolume()))) &&
            ((this.longShip==null && other.getLongShip()==null) || 
             (this.longShip!=null &&
              this.longShip.equals(other.getLongShip()))) &&
            ((this.widthShip==null && other.getWidthShip()==null) || 
             (this.widthShip!=null &&
              this.widthShip.equals(other.getWidthShip()))) &&
            ((this.highShip==null && other.getHighShip()==null) || 
             (this.highShip!=null &&
              this.highShip.equals(other.getHighShip())));
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
        if (getContent() != null) {
            _hashCode += getContent().hashCode();
        }
        if (getSlabNo() != null) {
            _hashCode += getSlabNo().hashCode();
        }
        if (getWeight() != null) {
            _hashCode += getWeight().hashCode();
        }
        if (getVolume() != null) {
            _hashCode += getVolume().hashCode();
        }
        if (getLongShip() != null) {
            _hashCode += getLongShip().hashCode();
        }
        if (getWidthShip() != null) {
            _hashCode += getWidthShip().hashCode();
        }
        if (getHighShip() != null) {
            _hashCode += getHighShip().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Embarque.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">Embarque"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "quantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">quantity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "shpCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">shpCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">content"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("slabNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "slabNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">slabNo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("weight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "weight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("volume");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "volume"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("longShip");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "longShip"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("widthShip");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "widthShip"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("highShip");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "highShip"));
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
