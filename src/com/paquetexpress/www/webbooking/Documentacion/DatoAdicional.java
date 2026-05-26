/**
 * DatoAdicional.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion;

public class DatoAdicional  implements java.io.Serializable {
    private java.lang.String claveDataAd;

    private java.lang.String valorDataAd;

    private java.lang.String dataAditional1;

    private java.lang.String dataAditional2;

    private java.lang.String dataAditional3;

    private java.lang.String dataAditional4;

    public DatoAdicional() {
    }

    public DatoAdicional(
           java.lang.String claveDataAd,
           java.lang.String valorDataAd,
           java.lang.String dataAditional1,
           java.lang.String dataAditional2,
           java.lang.String dataAditional3,
           java.lang.String dataAditional4) {
           this.claveDataAd = claveDataAd;
           this.valorDataAd = valorDataAd;
           this.dataAditional1 = dataAditional1;
           this.dataAditional2 = dataAditional2;
           this.dataAditional3 = dataAditional3;
           this.dataAditional4 = dataAditional4;
    }


    /**
     * Gets the claveDataAd value for this DatoAdicional.
     * 
     * @return claveDataAd
     */
    public java.lang.String getClaveDataAd() {
        return claveDataAd;
    }


    /**
     * Sets the claveDataAd value for this DatoAdicional.
     * 
     * @param claveDataAd
     */
    public void setClaveDataAd(java.lang.String claveDataAd) {
        this.claveDataAd = claveDataAd;
    }


    /**
     * Gets the valorDataAd value for this DatoAdicional.
     * 
     * @return valorDataAd
     */
    public java.lang.String getValorDataAd() {
        return valorDataAd;
    }


    /**
     * Sets the valorDataAd value for this DatoAdicional.
     * 
     * @param valorDataAd
     */
    public void setValorDataAd(java.lang.String valorDataAd) {
        this.valorDataAd = valorDataAd;
    }


    /**
     * Gets the dataAditional1 value for this DatoAdicional.
     * 
     * @return dataAditional1
     */
    public java.lang.String getDataAditional1() {
        return dataAditional1;
    }


    /**
     * Sets the dataAditional1 value for this DatoAdicional.
     * 
     * @param dataAditional1
     */
    public void setDataAditional1(java.lang.String dataAditional1) {
        this.dataAditional1 = dataAditional1;
    }


    /**
     * Gets the dataAditional2 value for this DatoAdicional.
     * 
     * @return dataAditional2
     */
    public java.lang.String getDataAditional2() {
        return dataAditional2;
    }


    /**
     * Sets the dataAditional2 value for this DatoAdicional.
     * 
     * @param dataAditional2
     */
    public void setDataAditional2(java.lang.String dataAditional2) {
        this.dataAditional2 = dataAditional2;
    }


    /**
     * Gets the dataAditional3 value for this DatoAdicional.
     * 
     * @return dataAditional3
     */
    public java.lang.String getDataAditional3() {
        return dataAditional3;
    }


    /**
     * Sets the dataAditional3 value for this DatoAdicional.
     * 
     * @param dataAditional3
     */
    public void setDataAditional3(java.lang.String dataAditional3) {
        this.dataAditional3 = dataAditional3;
    }


    /**
     * Gets the dataAditional4 value for this DatoAdicional.
     * 
     * @return dataAditional4
     */
    public java.lang.String getDataAditional4() {
        return dataAditional4;
    }


    /**
     * Sets the dataAditional4 value for this DatoAdicional.
     * 
     * @param dataAditional4
     */
    public void setDataAditional4(java.lang.String dataAditional4) {
        this.dataAditional4 = dataAditional4;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatoAdicional)) return false;
        DatoAdicional other = (DatoAdicional) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.claveDataAd==null && other.getClaveDataAd()==null) || 
             (this.claveDataAd!=null &&
              this.claveDataAd.equals(other.getClaveDataAd()))) &&
            ((this.valorDataAd==null && other.getValorDataAd()==null) || 
             (this.valorDataAd!=null &&
              this.valorDataAd.equals(other.getValorDataAd()))) &&
            ((this.dataAditional1==null && other.getDataAditional1()==null) || 
             (this.dataAditional1!=null &&
              this.dataAditional1.equals(other.getDataAditional1()))) &&
            ((this.dataAditional2==null && other.getDataAditional2()==null) || 
             (this.dataAditional2!=null &&
              this.dataAditional2.equals(other.getDataAditional2()))) &&
            ((this.dataAditional3==null && other.getDataAditional3()==null) || 
             (this.dataAditional3!=null &&
              this.dataAditional3.equals(other.getDataAditional3()))) &&
            ((this.dataAditional4==null && other.getDataAditional4()==null) || 
             (this.dataAditional4!=null &&
              this.dataAditional4.equals(other.getDataAditional4())));
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
        if (getClaveDataAd() != null) {
            _hashCode += getClaveDataAd().hashCode();
        }
        if (getValorDataAd() != null) {
            _hashCode += getValorDataAd().hashCode();
        }
        if (getDataAditional1() != null) {
            _hashCode += getDataAditional1().hashCode();
        }
        if (getDataAditional2() != null) {
            _hashCode += getDataAditional2().hashCode();
        }
        if (getDataAditional3() != null) {
            _hashCode += getDataAditional3().hashCode();
        }
        if (getDataAditional4() != null) {
            _hashCode += getDataAditional4().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatoAdicional.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">DatoAdicional"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("claveDataAd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "claveDataAd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">claveDataAd"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valorDataAd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "valorDataAd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">valorDataAd"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAditional1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "dataAditional1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">dataAditional1"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAditional2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "dataAditional2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">dataAditional2"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAditional3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "dataAditional3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">dataAditional3"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAditional4");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "dataAditional4"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">dataAditional4"));
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
