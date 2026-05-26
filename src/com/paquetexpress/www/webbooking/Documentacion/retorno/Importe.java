/**
 * Importe.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion.retorno;

public class Importe  implements java.io.Serializable {
    private java.lang.Double shpAmnt;

    private java.lang.Double discAmnt;

    private java.lang.Double srvcAmnt;

    private java.lang.Double subTotlAmnt;

    private java.lang.Double taxAmnt;

    private java.lang.Double taxRetAmnt;

    private java.lang.Double totalAmnt;

    public Importe() {
    }

    public Importe(
           java.lang.Double shpAmnt,
           java.lang.Double discAmnt,
           java.lang.Double srvcAmnt,
           java.lang.Double subTotlAmnt,
           java.lang.Double taxAmnt,
           java.lang.Double taxRetAmnt,
           java.lang.Double totalAmnt) {
           this.shpAmnt = shpAmnt;
           this.discAmnt = discAmnt;
           this.srvcAmnt = srvcAmnt;
           this.subTotlAmnt = subTotlAmnt;
           this.taxAmnt = taxAmnt;
           this.taxRetAmnt = taxRetAmnt;
           this.totalAmnt = totalAmnt;
    }


    /**
     * Gets the shpAmnt value for this Importe.
     * 
     * @return shpAmnt
     */
    public java.lang.Double getShpAmnt() {
        return shpAmnt;
    }


    /**
     * Sets the shpAmnt value for this Importe.
     * 
     * @param shpAmnt
     */
    public void setShpAmnt(java.lang.Double shpAmnt) {
        this.shpAmnt = shpAmnt;
    }


    /**
     * Gets the discAmnt value for this Importe.
     * 
     * @return discAmnt
     */
    public java.lang.Double getDiscAmnt() {
        return discAmnt;
    }


    /**
     * Sets the discAmnt value for this Importe.
     * 
     * @param discAmnt
     */
    public void setDiscAmnt(java.lang.Double discAmnt) {
        this.discAmnt = discAmnt;
    }


    /**
     * Gets the srvcAmnt value for this Importe.
     * 
     * @return srvcAmnt
     */
    public java.lang.Double getSrvcAmnt() {
        return srvcAmnt;
    }


    /**
     * Sets the srvcAmnt value for this Importe.
     * 
     * @param srvcAmnt
     */
    public void setSrvcAmnt(java.lang.Double srvcAmnt) {
        this.srvcAmnt = srvcAmnt;
    }


    /**
     * Gets the subTotlAmnt value for this Importe.
     * 
     * @return subTotlAmnt
     */
    public java.lang.Double getSubTotlAmnt() {
        return subTotlAmnt;
    }


    /**
     * Sets the subTotlAmnt value for this Importe.
     * 
     * @param subTotlAmnt
     */
    public void setSubTotlAmnt(java.lang.Double subTotlAmnt) {
        this.subTotlAmnt = subTotlAmnt;
    }


    /**
     * Gets the taxAmnt value for this Importe.
     * 
     * @return taxAmnt
     */
    public java.lang.Double getTaxAmnt() {
        return taxAmnt;
    }


    /**
     * Sets the taxAmnt value for this Importe.
     * 
     * @param taxAmnt
     */
    public void setTaxAmnt(java.lang.Double taxAmnt) {
        this.taxAmnt = taxAmnt;
    }


    /**
     * Gets the taxRetAmnt value for this Importe.
     * 
     * @return taxRetAmnt
     */
    public java.lang.Double getTaxRetAmnt() {
        return taxRetAmnt;
    }


    /**
     * Sets the taxRetAmnt value for this Importe.
     * 
     * @param taxRetAmnt
     */
    public void setTaxRetAmnt(java.lang.Double taxRetAmnt) {
        this.taxRetAmnt = taxRetAmnt;
    }


    /**
     * Gets the totalAmnt value for this Importe.
     * 
     * @return totalAmnt
     */
    public java.lang.Double getTotalAmnt() {
        return totalAmnt;
    }


    /**
     * Sets the totalAmnt value for this Importe.
     * 
     * @param totalAmnt
     */
    public void setTotalAmnt(java.lang.Double totalAmnt) {
        this.totalAmnt = totalAmnt;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Importe)) return false;
        Importe other = (Importe) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.shpAmnt==null && other.getShpAmnt()==null) || 
             (this.shpAmnt!=null &&
              this.shpAmnt.equals(other.getShpAmnt()))) &&
            ((this.discAmnt==null && other.getDiscAmnt()==null) || 
             (this.discAmnt!=null &&
              this.discAmnt.equals(other.getDiscAmnt()))) &&
            ((this.srvcAmnt==null && other.getSrvcAmnt()==null) || 
             (this.srvcAmnt!=null &&
              this.srvcAmnt.equals(other.getSrvcAmnt()))) &&
            ((this.subTotlAmnt==null && other.getSubTotlAmnt()==null) || 
             (this.subTotlAmnt!=null &&
              this.subTotlAmnt.equals(other.getSubTotlAmnt()))) &&
            ((this.taxAmnt==null && other.getTaxAmnt()==null) || 
             (this.taxAmnt!=null &&
              this.taxAmnt.equals(other.getTaxAmnt()))) &&
            ((this.taxRetAmnt==null && other.getTaxRetAmnt()==null) || 
             (this.taxRetAmnt!=null &&
              this.taxRetAmnt.equals(other.getTaxRetAmnt()))) &&
            ((this.totalAmnt==null && other.getTotalAmnt()==null) || 
             (this.totalAmnt!=null &&
              this.totalAmnt.equals(other.getTotalAmnt())));
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
        if (getShpAmnt() != null) {
            _hashCode += getShpAmnt().hashCode();
        }
        if (getDiscAmnt() != null) {
            _hashCode += getDiscAmnt().hashCode();
        }
        if (getSrvcAmnt() != null) {
            _hashCode += getSrvcAmnt().hashCode();
        }
        if (getSubTotlAmnt() != null) {
            _hashCode += getSubTotlAmnt().hashCode();
        }
        if (getTaxAmnt() != null) {
            _hashCode += getTaxAmnt().hashCode();
        }
        if (getTaxRetAmnt() != null) {
            _hashCode += getTaxRetAmnt().hashCode();
        }
        if (getTotalAmnt() != null) {
            _hashCode += getTotalAmnt().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Importe.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">Importe"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpAmnt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpAmnt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("discAmnt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "discAmnt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("srvcAmnt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "srvcAmnt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subTotlAmnt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "subTotlAmnt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxAmnt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "taxAmnt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxRetAmnt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "taxRetAmnt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalAmnt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "totalAmnt"));
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
