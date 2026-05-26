/**
 * SolicitudEnvio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion;

public class SolicitudEnvio  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.Documentacion.ClienteDestino clienteDestino;

    private com.paquetexpress.www.webbooking.Documentacion.DetalleEmbarque detalleEmbarque;

    private com.paquetexpress.www.webbooking.Documentacion.Servicios servicios;

    private java.lang.String custAgnt;

    private java.lang.String ghPediNumb;
    
    private String lastWrongPediNumb;

    private com.paquetexpress.www.webbooking.Documentacion.DatosAdicionales datosAdicionales;

    public SolicitudEnvio() {
    }

    public SolicitudEnvio(
           com.paquetexpress.www.webbooking.Documentacion.ClienteDestino clienteDestino,
           com.paquetexpress.www.webbooking.Documentacion.DetalleEmbarque detalleEmbarque,
           com.paquetexpress.www.webbooking.Documentacion.Servicios servicios,
           java.lang.String custAgnt,
           java.lang.String ghPediNumb,
           String lastWrongPediNumb,
           com.paquetexpress.www.webbooking.Documentacion.DatosAdicionales datosAdicionales) {
           this.clienteDestino = clienteDestino;
           this.detalleEmbarque = detalleEmbarque;
           this.servicios = servicios;
           this.custAgnt = custAgnt;
           this.ghPediNumb = ghPediNumb;

           this.ghPediNumb = ghPediNumb;
           this.datosAdicionales = datosAdicionales;
    }


    /**
     * Gets the clienteDestino value for this SolicitudEnvio.
     * 
     * @return clienteDestino
     */
    public com.paquetexpress.www.webbooking.Documentacion.ClienteDestino getClienteDestino() {
        return clienteDestino;
    }


    /**
     * Sets the clienteDestino value for this SolicitudEnvio.
     * 
     * @param clienteDestino
     */
    public void setClienteDestino(com.paquetexpress.www.webbooking.Documentacion.ClienteDestino clienteDestino) {
        this.clienteDestino = clienteDestino;
    }


    /**
     * Gets the detalleEmbarque value for this SolicitudEnvio.
     * 
     * @return detalleEmbarque
     */
    public com.paquetexpress.www.webbooking.Documentacion.DetalleEmbarque getDetalleEmbarque() {
        return detalleEmbarque;
    }


    /**
     * Sets the detalleEmbarque value for this SolicitudEnvio.
     * 
     * @param detalleEmbarque
     */
    public void setDetalleEmbarque(com.paquetexpress.www.webbooking.Documentacion.DetalleEmbarque detalleEmbarque) {
        this.detalleEmbarque = detalleEmbarque;
    }


    /**
     * Gets the servicios value for this SolicitudEnvio.
     * 
     * @return servicios
     */
    public com.paquetexpress.www.webbooking.Documentacion.Servicios getServicios() {
        return servicios;
    }


    /**
     * Sets the servicios value for this SolicitudEnvio.
     * 
     * @param servicios
     */
    public void setServicios(com.paquetexpress.www.webbooking.Documentacion.Servicios servicios) {
        this.servicios = servicios;
    }


    /**
     * Gets the custAgnt value for this SolicitudEnvio.
     * 
     * @return custAgnt
     */
    public java.lang.String getCustAgnt() {
        return custAgnt;
    }


    /**
     * Sets the custAgnt value for this SolicitudEnvio.
     * 
     * @param custAgnt
     */
    public void setCustAgnt(java.lang.String custAgnt) {
        this.custAgnt = custAgnt;
    }


    /**
     * Gets the ghPediNumb value for this SolicitudEnvio.
     * 
     * @return ghPediNumb
     */
    public java.lang.String getGhPediNumb() {
        return ghPediNumb;
    }


    /**
     * Sets the ghPediNumb value for this SolicitudEnvio.
     * 
     * @param ghPediNumb
     */
    public void setGhPediNumb(java.lang.String ghPediNumb) {
        this.ghPediNumb = ghPediNumb;
    }


    /**
     * Gets the datosAdicionales value for this SolicitudEnvio.
     * 
     * @return datosAdicionales
     */
    public com.paquetexpress.www.webbooking.Documentacion.DatosAdicionales getDatosAdicionales() {
        return datosAdicionales;
    }


    /**
     * Sets the datosAdicionales value for this SolicitudEnvio.
     * 
     * @param datosAdicionales
     */
    public void setDatosAdicionales(com.paquetexpress.www.webbooking.Documentacion.DatosAdicionales datosAdicionales) {
        this.datosAdicionales = datosAdicionales;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SolicitudEnvio)) return false;
        SolicitudEnvio other = (SolicitudEnvio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.clienteDestino==null && other.getClienteDestino()==null) || 
             (this.clienteDestino!=null &&
              this.clienteDestino.equals(other.getClienteDestino()))) &&
            ((this.detalleEmbarque==null && other.getDetalleEmbarque()==null) || 
             (this.detalleEmbarque!=null &&
              this.detalleEmbarque.equals(other.getDetalleEmbarque()))) &&
            ((this.servicios==null && other.getServicios()==null) || 
             (this.servicios!=null &&
              this.servicios.equals(other.getServicios()))) &&
            ((this.custAgnt==null && other.getCustAgnt()==null) || 
             (this.custAgnt!=null &&
              this.custAgnt.equals(other.getCustAgnt()))) &&
            ((this.ghPediNumb==null && other.getGhPediNumb()==null) || 
             (this.ghPediNumb!=null &&
              this.ghPediNumb.equals(other.getGhPediNumb()))) &&
            ((this.datosAdicionales==null && other.getDatosAdicionales()==null) || 
             (this.datosAdicionales!=null &&
              this.datosAdicionales.equals(other.getDatosAdicionales())));
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
        if (getClienteDestino() != null) {
            _hashCode += getClienteDestino().hashCode();
        }
        if (getDetalleEmbarque() != null) {
            _hashCode += getDetalleEmbarque().hashCode();
        }
        if (getServicios() != null) {
            _hashCode += getServicios().hashCode();
        }
        if (getCustAgnt() != null) {
            _hashCode += getCustAgnt().hashCode();
        }
        if (getGhPediNumb() != null) {
            _hashCode += getGhPediNumb().hashCode();
        }
        if (getDatosAdicionales() != null) {
            _hashCode += getDatosAdicionales().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SolicitudEnvio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">SolicitudEnvio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clienteDestino");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "ClienteDestino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">ClienteDestino"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detalleEmbarque");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "DetalleEmbarque"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">DetalleEmbarque"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("servicios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "Servicios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">Servicios"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("custAgnt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "custAgnt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">custAgnt"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ghPediNumb");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "ghPediNumb"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">ghPediNumb"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosAdicionales");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", "DatosAdicionales"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/", ">DatosAdicionales"));
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

	/**
	 * @return the lastWrongPediNumb
	 */
	public String getLastWrongPediNumb() {
		return lastWrongPediNumb;
	}

	/**
	 * @param lastWrongPediNumb the lastWrongPediNumb to set
	 */
	public void setLastWrongPediNumb(String lastWrongPediNumb) {
		this.lastWrongPediNumb = lastWrongPediNumb;
	}

}
