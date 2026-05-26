/**
 * Servicios.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paquetexpress.www.webbooking.Documentacion.retorno;

public class Servicios  implements java.io.Serializable {
    private com.paquetexpress.www.webbooking.Documentacion.retorno.OtrosServicios otrosServicios;

    private com.paquetexpress.www.webbooking.Documentacion.retorno.Importe importe;

    private java.lang.String ackType;

    private java.lang.Double ackTypeAmt;

    private java.lang.Double ackTypeAmtDisc;

    private java.lang.Double ackTypeAmtTax;

    private java.lang.Double ackTypeAmtRetTax;

    private java.lang.Double codAmount;

    private java.lang.Double codTypeAmt;

    private java.lang.Double codTypeAmtDisc;

    private java.lang.Double codTypeAmtTax;

    private java.lang.Double codTypeAmtRetTax;

    private java.lang.String dlvyType;

    private java.lang.Double dlvyTypeAmt;

    private java.lang.Double dlvyTypeAmtDisc;

    private java.lang.Double dlvyTypeAmtTax;

    private java.lang.Double dlvyTypeAmtRetTax;

    private java.lang.Double totldeclVlue;

    private java.lang.String invType;

    private java.lang.Double invTypeAmt;

    private java.lang.Double invTypeAmtDisc;

    private java.lang.Double invTypeAmtTax;

    private java.lang.Double invTypeAmtRetTax;

    private java.lang.String radType;

    private java.lang.Double radTypeAmt;

    private java.lang.Double radTypeAmtDisc;

    private java.lang.Double radTypeAmtTax;

    private java.lang.Double radTypeAmtRetTax;

    private java.lang.String shpEType;

    private java.lang.Double shpETypeAmt;

    private java.lang.Double shpETypeAmtDisc;

    private java.lang.Double shpETypeAmtTax;

    private java.lang.Double shpETypeAmtRetTax;

    private java.lang.String shpType;

    private java.lang.Double shpTypeAmt;

    private java.lang.Double shpTypeAmtDisc;

    private java.lang.Double shpTypeAmtTax;

    private java.lang.Double shpTypeAmtRetTax;

    private java.lang.String formaPago;

    public Servicios() {
    }

    public Servicios(
           com.paquetexpress.www.webbooking.Documentacion.retorno.OtrosServicios otrosServicios,
           com.paquetexpress.www.webbooking.Documentacion.retorno.Importe importe,
           java.lang.String ackType,
           java.lang.Double ackTypeAmt,
           java.lang.Double ackTypeAmtDisc,
           java.lang.Double ackTypeAmtTax,
           java.lang.Double ackTypeAmtRetTax,
           java.lang.Double codAmount,
           java.lang.Double codTypeAmt,
           java.lang.Double codTypeAmtDisc,
           java.lang.Double codTypeAmtTax,
           java.lang.Double codTypeAmtRetTax,
           java.lang.String dlvyType,
           java.lang.Double dlvyTypeAmt,
           java.lang.Double dlvyTypeAmtDisc,
           java.lang.Double dlvyTypeAmtTax,
           java.lang.Double dlvyTypeAmtRetTax,
           java.lang.Double totldeclVlue,
           java.lang.String invType,
           java.lang.Double invTypeAmt,
           java.lang.Double invTypeAmtDisc,
           java.lang.Double invTypeAmtTax,
           java.lang.Double invTypeAmtRetTax,
           java.lang.String radType,
           java.lang.Double radTypeAmt,
           java.lang.Double radTypeAmtDisc,
           java.lang.Double radTypeAmtTax,
           java.lang.Double radTypeAmtRetTax,
           java.lang.String shpEType,
           java.lang.Double shpETypeAmt,
           java.lang.Double shpETypeAmtDisc,
           java.lang.Double shpETypeAmtTax,
           java.lang.Double shpETypeAmtRetTax,
           java.lang.String shpType,
           java.lang.Double shpTypeAmt,
           java.lang.Double shpTypeAmtDisc,
           java.lang.Double shpTypeAmtTax,
           java.lang.Double shpTypeAmtRetTax,
           java.lang.String formaPago) {
           this.otrosServicios = otrosServicios;
           this.importe = importe;
           this.ackType = ackType;
           this.ackTypeAmt = ackTypeAmt;
           this.ackTypeAmtDisc = ackTypeAmtDisc;
           this.ackTypeAmtTax = ackTypeAmtTax;
           this.ackTypeAmtRetTax = ackTypeAmtRetTax;
           this.codAmount = codAmount;
           this.codTypeAmt = codTypeAmt;
           this.codTypeAmtDisc = codTypeAmtDisc;
           this.codTypeAmtTax = codTypeAmtTax;
           this.codTypeAmtRetTax = codTypeAmtRetTax;
           this.dlvyType = dlvyType;
           this.dlvyTypeAmt = dlvyTypeAmt;
           this.dlvyTypeAmtDisc = dlvyTypeAmtDisc;
           this.dlvyTypeAmtTax = dlvyTypeAmtTax;
           this.dlvyTypeAmtRetTax = dlvyTypeAmtRetTax;
           this.totldeclVlue = totldeclVlue;
           this.invType = invType;
           this.invTypeAmt = invTypeAmt;
           this.invTypeAmtDisc = invTypeAmtDisc;
           this.invTypeAmtTax = invTypeAmtTax;
           this.invTypeAmtRetTax = invTypeAmtRetTax;
           this.radType = radType;
           this.radTypeAmt = radTypeAmt;
           this.radTypeAmtDisc = radTypeAmtDisc;
           this.radTypeAmtTax = radTypeAmtTax;
           this.radTypeAmtRetTax = radTypeAmtRetTax;
           this.shpEType = shpEType;
           this.shpETypeAmt = shpETypeAmt;
           this.shpETypeAmtDisc = shpETypeAmtDisc;
           this.shpETypeAmtTax = shpETypeAmtTax;
           this.shpETypeAmtRetTax = shpETypeAmtRetTax;
           this.shpType = shpType;
           this.shpTypeAmt = shpTypeAmt;
           this.shpTypeAmtDisc = shpTypeAmtDisc;
           this.shpTypeAmtTax = shpTypeAmtTax;
           this.shpTypeAmtRetTax = shpTypeAmtRetTax;
           this.formaPago = formaPago;
    }


    /**
     * Gets the otrosServicios value for this Servicios.
     * 
     * @return otrosServicios
     */
    public com.paquetexpress.www.webbooking.Documentacion.retorno.OtrosServicios getOtrosServicios() {
        return otrosServicios;
    }


    /**
     * Sets the otrosServicios value for this Servicios.
     * 
     * @param otrosServicios
     */
    public void setOtrosServicios(com.paquetexpress.www.webbooking.Documentacion.retorno.OtrosServicios otrosServicios) {
        this.otrosServicios = otrosServicios;
    }


    /**
     * Gets the importe value for this Servicios.
     * 
     * @return importe
     */
    public com.paquetexpress.www.webbooking.Documentacion.retorno.Importe getImporte() {
        return importe;
    }


    /**
     * Sets the importe value for this Servicios.
     * 
     * @param importe
     */
    public void setImporte(com.paquetexpress.www.webbooking.Documentacion.retorno.Importe importe) {
        this.importe = importe;
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
     * Gets the ackTypeAmt value for this Servicios.
     * 
     * @return ackTypeAmt
     */
    public java.lang.Double getAckTypeAmt() {
        return ackTypeAmt;
    }


    /**
     * Sets the ackTypeAmt value for this Servicios.
     * 
     * @param ackTypeAmt
     */
    public void setAckTypeAmt(java.lang.Double ackTypeAmt) {
        this.ackTypeAmt = ackTypeAmt;
    }


    /**
     * Gets the ackTypeAmtDisc value for this Servicios.
     * 
     * @return ackTypeAmtDisc
     */
    public java.lang.Double getAckTypeAmtDisc() {
        return ackTypeAmtDisc;
    }


    /**
     * Sets the ackTypeAmtDisc value for this Servicios.
     * 
     * @param ackTypeAmtDisc
     */
    public void setAckTypeAmtDisc(java.lang.Double ackTypeAmtDisc) {
        this.ackTypeAmtDisc = ackTypeAmtDisc;
    }


    /**
     * Gets the ackTypeAmtTax value for this Servicios.
     * 
     * @return ackTypeAmtTax
     */
    public java.lang.Double getAckTypeAmtTax() {
        return ackTypeAmtTax;
    }


    /**
     * Sets the ackTypeAmtTax value for this Servicios.
     * 
     * @param ackTypeAmtTax
     */
    public void setAckTypeAmtTax(java.lang.Double ackTypeAmtTax) {
        this.ackTypeAmtTax = ackTypeAmtTax;
    }


    /**
     * Gets the ackTypeAmtRetTax value for this Servicios.
     * 
     * @return ackTypeAmtRetTax
     */
    public java.lang.Double getAckTypeAmtRetTax() {
        return ackTypeAmtRetTax;
    }


    /**
     * Sets the ackTypeAmtRetTax value for this Servicios.
     * 
     * @param ackTypeAmtRetTax
     */
    public void setAckTypeAmtRetTax(java.lang.Double ackTypeAmtRetTax) {
        this.ackTypeAmtRetTax = ackTypeAmtRetTax;
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
     * Gets the codTypeAmt value for this Servicios.
     * 
     * @return codTypeAmt
     */
    public java.lang.Double getCodTypeAmt() {
        return codTypeAmt;
    }


    /**
     * Sets the codTypeAmt value for this Servicios.
     * 
     * @param codTypeAmt
     */
    public void setCodTypeAmt(java.lang.Double codTypeAmt) {
        this.codTypeAmt = codTypeAmt;
    }


    /**
     * Gets the codTypeAmtDisc value for this Servicios.
     * 
     * @return codTypeAmtDisc
     */
    public java.lang.Double getCodTypeAmtDisc() {
        return codTypeAmtDisc;
    }


    /**
     * Sets the codTypeAmtDisc value for this Servicios.
     * 
     * @param codTypeAmtDisc
     */
    public void setCodTypeAmtDisc(java.lang.Double codTypeAmtDisc) {
        this.codTypeAmtDisc = codTypeAmtDisc;
    }


    /**
     * Gets the codTypeAmtTax value for this Servicios.
     * 
     * @return codTypeAmtTax
     */
    public java.lang.Double getCodTypeAmtTax() {
        return codTypeAmtTax;
    }


    /**
     * Sets the codTypeAmtTax value for this Servicios.
     * 
     * @param codTypeAmtTax
     */
    public void setCodTypeAmtTax(java.lang.Double codTypeAmtTax) {
        this.codTypeAmtTax = codTypeAmtTax;
    }


    /**
     * Gets the codTypeAmtRetTax value for this Servicios.
     * 
     * @return codTypeAmtRetTax
     */
    public java.lang.Double getCodTypeAmtRetTax() {
        return codTypeAmtRetTax;
    }


    /**
     * Sets the codTypeAmtRetTax value for this Servicios.
     * 
     * @param codTypeAmtRetTax
     */
    public void setCodTypeAmtRetTax(java.lang.Double codTypeAmtRetTax) {
        this.codTypeAmtRetTax = codTypeAmtRetTax;
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
     * Gets the dlvyTypeAmt value for this Servicios.
     * 
     * @return dlvyTypeAmt
     */
    public java.lang.Double getDlvyTypeAmt() {
        return dlvyTypeAmt;
    }


    /**
     * Sets the dlvyTypeAmt value for this Servicios.
     * 
     * @param dlvyTypeAmt
     */
    public void setDlvyTypeAmt(java.lang.Double dlvyTypeAmt) {
        this.dlvyTypeAmt = dlvyTypeAmt;
    }


    /**
     * Gets the dlvyTypeAmtDisc value for this Servicios.
     * 
     * @return dlvyTypeAmtDisc
     */
    public java.lang.Double getDlvyTypeAmtDisc() {
        return dlvyTypeAmtDisc;
    }


    /**
     * Sets the dlvyTypeAmtDisc value for this Servicios.
     * 
     * @param dlvyTypeAmtDisc
     */
    public void setDlvyTypeAmtDisc(java.lang.Double dlvyTypeAmtDisc) {
        this.dlvyTypeAmtDisc = dlvyTypeAmtDisc;
    }


    /**
     * Gets the dlvyTypeAmtTax value for this Servicios.
     * 
     * @return dlvyTypeAmtTax
     */
    public java.lang.Double getDlvyTypeAmtTax() {
        return dlvyTypeAmtTax;
    }


    /**
     * Sets the dlvyTypeAmtTax value for this Servicios.
     * 
     * @param dlvyTypeAmtTax
     */
    public void setDlvyTypeAmtTax(java.lang.Double dlvyTypeAmtTax) {
        this.dlvyTypeAmtTax = dlvyTypeAmtTax;
    }


    /**
     * Gets the dlvyTypeAmtRetTax value for this Servicios.
     * 
     * @return dlvyTypeAmtRetTax
     */
    public java.lang.Double getDlvyTypeAmtRetTax() {
        return dlvyTypeAmtRetTax;
    }


    /**
     * Sets the dlvyTypeAmtRetTax value for this Servicios.
     * 
     * @param dlvyTypeAmtRetTax
     */
    public void setDlvyTypeAmtRetTax(java.lang.Double dlvyTypeAmtRetTax) {
        this.dlvyTypeAmtRetTax = dlvyTypeAmtRetTax;
    }


    /**
     * Gets the totldeclVlue value for this Servicios.
     * 
     * @return totldeclVlue
     */
    public java.lang.Double getTotldeclVlue() {
        return totldeclVlue;
    }


    /**
     * Sets the totldeclVlue value for this Servicios.
     * 
     * @param totldeclVlue
     */
    public void setTotldeclVlue(java.lang.Double totldeclVlue) {
        this.totldeclVlue = totldeclVlue;
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
     * Gets the invTypeAmt value for this Servicios.
     * 
     * @return invTypeAmt
     */
    public java.lang.Double getInvTypeAmt() {
        return invTypeAmt;
    }


    /**
     * Sets the invTypeAmt value for this Servicios.
     * 
     * @param invTypeAmt
     */
    public void setInvTypeAmt(java.lang.Double invTypeAmt) {
        this.invTypeAmt = invTypeAmt;
    }


    /**
     * Gets the invTypeAmtDisc value for this Servicios.
     * 
     * @return invTypeAmtDisc
     */
    public java.lang.Double getInvTypeAmtDisc() {
        return invTypeAmtDisc;
    }


    /**
     * Sets the invTypeAmtDisc value for this Servicios.
     * 
     * @param invTypeAmtDisc
     */
    public void setInvTypeAmtDisc(java.lang.Double invTypeAmtDisc) {
        this.invTypeAmtDisc = invTypeAmtDisc;
    }


    /**
     * Gets the invTypeAmtTax value for this Servicios.
     * 
     * @return invTypeAmtTax
     */
    public java.lang.Double getInvTypeAmtTax() {
        return invTypeAmtTax;
    }


    /**
     * Sets the invTypeAmtTax value for this Servicios.
     * 
     * @param invTypeAmtTax
     */
    public void setInvTypeAmtTax(java.lang.Double invTypeAmtTax) {
        this.invTypeAmtTax = invTypeAmtTax;
    }


    /**
     * Gets the invTypeAmtRetTax value for this Servicios.
     * 
     * @return invTypeAmtRetTax
     */
    public java.lang.Double getInvTypeAmtRetTax() {
        return invTypeAmtRetTax;
    }


    /**
     * Sets the invTypeAmtRetTax value for this Servicios.
     * 
     * @param invTypeAmtRetTax
     */
    public void setInvTypeAmtRetTax(java.lang.Double invTypeAmtRetTax) {
        this.invTypeAmtRetTax = invTypeAmtRetTax;
    }


    /**
     * Gets the radType value for this Servicios.
     * 
     * @return radType
     */
    public java.lang.String getRadType() {
        return radType;
    }


    /**
     * Sets the radType value for this Servicios.
     * 
     * @param radType
     */
    public void setRadType(java.lang.String radType) {
        this.radType = radType;
    }


    /**
     * Gets the radTypeAmt value for this Servicios.
     * 
     * @return radTypeAmt
     */
    public java.lang.Double getRadTypeAmt() {
        return radTypeAmt;
    }


    /**
     * Sets the radTypeAmt value for this Servicios.
     * 
     * @param radTypeAmt
     */
    public void setRadTypeAmt(java.lang.Double radTypeAmt) {
        this.radTypeAmt = radTypeAmt;
    }


    /**
     * Gets the radTypeAmtDisc value for this Servicios.
     * 
     * @return radTypeAmtDisc
     */
    public java.lang.Double getRadTypeAmtDisc() {
        return radTypeAmtDisc;
    }


    /**
     * Sets the radTypeAmtDisc value for this Servicios.
     * 
     * @param radTypeAmtDisc
     */
    public void setRadTypeAmtDisc(java.lang.Double radTypeAmtDisc) {
        this.radTypeAmtDisc = radTypeAmtDisc;
    }


    /**
     * Gets the radTypeAmtTax value for this Servicios.
     * 
     * @return radTypeAmtTax
     */
    public java.lang.Double getRadTypeAmtTax() {
        return radTypeAmtTax;
    }


    /**
     * Sets the radTypeAmtTax value for this Servicios.
     * 
     * @param radTypeAmtTax
     */
    public void setRadTypeAmtTax(java.lang.Double radTypeAmtTax) {
        this.radTypeAmtTax = radTypeAmtTax;
    }


    /**
     * Gets the radTypeAmtRetTax value for this Servicios.
     * 
     * @return radTypeAmtRetTax
     */
    public java.lang.Double getRadTypeAmtRetTax() {
        return radTypeAmtRetTax;
    }


    /**
     * Sets the radTypeAmtRetTax value for this Servicios.
     * 
     * @param radTypeAmtRetTax
     */
    public void setRadTypeAmtRetTax(java.lang.Double radTypeAmtRetTax) {
        this.radTypeAmtRetTax = radTypeAmtRetTax;
    }


    /**
     * Gets the shpEType value for this Servicios.
     * 
     * @return shpEType
     */
    public java.lang.String getShpEType() {
        return shpEType;
    }


    /**
     * Sets the shpEType value for this Servicios.
     * 
     * @param shpEType
     */
    public void setShpEType(java.lang.String shpEType) {
        this.shpEType = shpEType;
    }


    /**
     * Gets the shpETypeAmt value for this Servicios.
     * 
     * @return shpETypeAmt
     */
    public java.lang.Double getShpETypeAmt() {
        return shpETypeAmt;
    }


    /**
     * Sets the shpETypeAmt value for this Servicios.
     * 
     * @param shpETypeAmt
     */
    public void setShpETypeAmt(java.lang.Double shpETypeAmt) {
        this.shpETypeAmt = shpETypeAmt;
    }


    /**
     * Gets the shpETypeAmtDisc value for this Servicios.
     * 
     * @return shpETypeAmtDisc
     */
    public java.lang.Double getShpETypeAmtDisc() {
        return shpETypeAmtDisc;
    }


    /**
     * Sets the shpETypeAmtDisc value for this Servicios.
     * 
     * @param shpETypeAmtDisc
     */
    public void setShpETypeAmtDisc(java.lang.Double shpETypeAmtDisc) {
        this.shpETypeAmtDisc = shpETypeAmtDisc;
    }


    /**
     * Gets the shpETypeAmtTax value for this Servicios.
     * 
     * @return shpETypeAmtTax
     */
    public java.lang.Double getShpETypeAmtTax() {
        return shpETypeAmtTax;
    }


    /**
     * Sets the shpETypeAmtTax value for this Servicios.
     * 
     * @param shpETypeAmtTax
     */
    public void setShpETypeAmtTax(java.lang.Double shpETypeAmtTax) {
        this.shpETypeAmtTax = shpETypeAmtTax;
    }


    /**
     * Gets the shpETypeAmtRetTax value for this Servicios.
     * 
     * @return shpETypeAmtRetTax
     */
    public java.lang.Double getShpETypeAmtRetTax() {
        return shpETypeAmtRetTax;
    }


    /**
     * Sets the shpETypeAmtRetTax value for this Servicios.
     * 
     * @param shpETypeAmtRetTax
     */
    public void setShpETypeAmtRetTax(java.lang.Double shpETypeAmtRetTax) {
        this.shpETypeAmtRetTax = shpETypeAmtRetTax;
    }


    /**
     * Gets the shpType value for this Servicios.
     * 
     * @return shpType
     */
    public java.lang.String getShpType() {
        return shpType;
    }


    /**
     * Sets the shpType value for this Servicios.
     * 
     * @param shpType
     */
    public void setShpType(java.lang.String shpType) {
        this.shpType = shpType;
    }


    /**
     * Gets the shpTypeAmt value for this Servicios.
     * 
     * @return shpTypeAmt
     */
    public java.lang.Double getShpTypeAmt() {
        return shpTypeAmt;
    }


    /**
     * Sets the shpTypeAmt value for this Servicios.
     * 
     * @param shpTypeAmt
     */
    public void setShpTypeAmt(java.lang.Double shpTypeAmt) {
        this.shpTypeAmt = shpTypeAmt;
    }


    /**
     * Gets the shpTypeAmtDisc value for this Servicios.
     * 
     * @return shpTypeAmtDisc
     */
    public java.lang.Double getShpTypeAmtDisc() {
        return shpTypeAmtDisc;
    }


    /**
     * Sets the shpTypeAmtDisc value for this Servicios.
     * 
     * @param shpTypeAmtDisc
     */
    public void setShpTypeAmtDisc(java.lang.Double shpTypeAmtDisc) {
        this.shpTypeAmtDisc = shpTypeAmtDisc;
    }


    /**
     * Gets the shpTypeAmtTax value for this Servicios.
     * 
     * @return shpTypeAmtTax
     */
    public java.lang.Double getShpTypeAmtTax() {
        return shpTypeAmtTax;
    }


    /**
     * Sets the shpTypeAmtTax value for this Servicios.
     * 
     * @param shpTypeAmtTax
     */
    public void setShpTypeAmtTax(java.lang.Double shpTypeAmtTax) {
        this.shpTypeAmtTax = shpTypeAmtTax;
    }


    /**
     * Gets the shpTypeAmtRetTax value for this Servicios.
     * 
     * @return shpTypeAmtRetTax
     */
    public java.lang.Double getShpTypeAmtRetTax() {
        return shpTypeAmtRetTax;
    }


    /**
     * Sets the shpTypeAmtRetTax value for this Servicios.
     * 
     * @param shpTypeAmtRetTax
     */
    public void setShpTypeAmtRetTax(java.lang.Double shpTypeAmtRetTax) {
        this.shpTypeAmtRetTax = shpTypeAmtRetTax;
    }


    /**
     * Gets the formaPago value for this Servicios.
     * 
     * @return formaPago
     */
    public java.lang.String getFormaPago() {
        return formaPago;
    }


    /**
     * Sets the formaPago value for this Servicios.
     * 
     * @param formaPago
     */
    public void setFormaPago(java.lang.String formaPago) {
        this.formaPago = formaPago;
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
            ((this.importe==null && other.getImporte()==null) || 
             (this.importe!=null &&
              this.importe.equals(other.getImporte()))) &&
            ((this.ackType==null && other.getAckType()==null) || 
             (this.ackType!=null &&
              this.ackType.equals(other.getAckType()))) &&
            ((this.ackTypeAmt==null && other.getAckTypeAmt()==null) || 
             (this.ackTypeAmt!=null &&
              this.ackTypeAmt.equals(other.getAckTypeAmt()))) &&
            ((this.ackTypeAmtDisc==null && other.getAckTypeAmtDisc()==null) || 
             (this.ackTypeAmtDisc!=null &&
              this.ackTypeAmtDisc.equals(other.getAckTypeAmtDisc()))) &&
            ((this.ackTypeAmtTax==null && other.getAckTypeAmtTax()==null) || 
             (this.ackTypeAmtTax!=null &&
              this.ackTypeAmtTax.equals(other.getAckTypeAmtTax()))) &&
            ((this.ackTypeAmtRetTax==null && other.getAckTypeAmtRetTax()==null) || 
             (this.ackTypeAmtRetTax!=null &&
              this.ackTypeAmtRetTax.equals(other.getAckTypeAmtRetTax()))) &&
            ((this.codAmount==null && other.getCodAmount()==null) || 
             (this.codAmount!=null &&
              this.codAmount.equals(other.getCodAmount()))) &&
            ((this.codTypeAmt==null && other.getCodTypeAmt()==null) || 
             (this.codTypeAmt!=null &&
              this.codTypeAmt.equals(other.getCodTypeAmt()))) &&
            ((this.codTypeAmtDisc==null && other.getCodTypeAmtDisc()==null) || 
             (this.codTypeAmtDisc!=null &&
              this.codTypeAmtDisc.equals(other.getCodTypeAmtDisc()))) &&
            ((this.codTypeAmtTax==null && other.getCodTypeAmtTax()==null) || 
             (this.codTypeAmtTax!=null &&
              this.codTypeAmtTax.equals(other.getCodTypeAmtTax()))) &&
            ((this.codTypeAmtRetTax==null && other.getCodTypeAmtRetTax()==null) || 
             (this.codTypeAmtRetTax!=null &&
              this.codTypeAmtRetTax.equals(other.getCodTypeAmtRetTax()))) &&
            ((this.dlvyType==null && other.getDlvyType()==null) || 
             (this.dlvyType!=null &&
              this.dlvyType.equals(other.getDlvyType()))) &&
            ((this.dlvyTypeAmt==null && other.getDlvyTypeAmt()==null) || 
             (this.dlvyTypeAmt!=null &&
              this.dlvyTypeAmt.equals(other.getDlvyTypeAmt()))) &&
            ((this.dlvyTypeAmtDisc==null && other.getDlvyTypeAmtDisc()==null) || 
             (this.dlvyTypeAmtDisc!=null &&
              this.dlvyTypeAmtDisc.equals(other.getDlvyTypeAmtDisc()))) &&
            ((this.dlvyTypeAmtTax==null && other.getDlvyTypeAmtTax()==null) || 
             (this.dlvyTypeAmtTax!=null &&
              this.dlvyTypeAmtTax.equals(other.getDlvyTypeAmtTax()))) &&
            ((this.dlvyTypeAmtRetTax==null && other.getDlvyTypeAmtRetTax()==null) || 
             (this.dlvyTypeAmtRetTax!=null &&
              this.dlvyTypeAmtRetTax.equals(other.getDlvyTypeAmtRetTax()))) &&
            ((this.totldeclVlue==null && other.getTotldeclVlue()==null) || 
             (this.totldeclVlue!=null &&
              this.totldeclVlue.equals(other.getTotldeclVlue()))) &&
            ((this.invType==null && other.getInvType()==null) || 
             (this.invType!=null &&
              this.invType.equals(other.getInvType()))) &&
            ((this.invTypeAmt==null && other.getInvTypeAmt()==null) || 
             (this.invTypeAmt!=null &&
              this.invTypeAmt.equals(other.getInvTypeAmt()))) &&
            ((this.invTypeAmtDisc==null && other.getInvTypeAmtDisc()==null) || 
             (this.invTypeAmtDisc!=null &&
              this.invTypeAmtDisc.equals(other.getInvTypeAmtDisc()))) &&
            ((this.invTypeAmtTax==null && other.getInvTypeAmtTax()==null) || 
             (this.invTypeAmtTax!=null &&
              this.invTypeAmtTax.equals(other.getInvTypeAmtTax()))) &&
            ((this.invTypeAmtRetTax==null && other.getInvTypeAmtRetTax()==null) || 
             (this.invTypeAmtRetTax!=null &&
              this.invTypeAmtRetTax.equals(other.getInvTypeAmtRetTax()))) &&
            ((this.radType==null && other.getRadType()==null) || 
             (this.radType!=null &&
              this.radType.equals(other.getRadType()))) &&
            ((this.radTypeAmt==null && other.getRadTypeAmt()==null) || 
             (this.radTypeAmt!=null &&
              this.radTypeAmt.equals(other.getRadTypeAmt()))) &&
            ((this.radTypeAmtDisc==null && other.getRadTypeAmtDisc()==null) || 
             (this.radTypeAmtDisc!=null &&
              this.radTypeAmtDisc.equals(other.getRadTypeAmtDisc()))) &&
            ((this.radTypeAmtTax==null && other.getRadTypeAmtTax()==null) || 
             (this.radTypeAmtTax!=null &&
              this.radTypeAmtTax.equals(other.getRadTypeAmtTax()))) &&
            ((this.radTypeAmtRetTax==null && other.getRadTypeAmtRetTax()==null) || 
             (this.radTypeAmtRetTax!=null &&
              this.radTypeAmtRetTax.equals(other.getRadTypeAmtRetTax()))) &&
            ((this.shpEType==null && other.getShpEType()==null) || 
             (this.shpEType!=null &&
              this.shpEType.equals(other.getShpEType()))) &&
            ((this.shpETypeAmt==null && other.getShpETypeAmt()==null) || 
             (this.shpETypeAmt!=null &&
              this.shpETypeAmt.equals(other.getShpETypeAmt()))) &&
            ((this.shpETypeAmtDisc==null && other.getShpETypeAmtDisc()==null) || 
             (this.shpETypeAmtDisc!=null &&
              this.shpETypeAmtDisc.equals(other.getShpETypeAmtDisc()))) &&
            ((this.shpETypeAmtTax==null && other.getShpETypeAmtTax()==null) || 
             (this.shpETypeAmtTax!=null &&
              this.shpETypeAmtTax.equals(other.getShpETypeAmtTax()))) &&
            ((this.shpETypeAmtRetTax==null && other.getShpETypeAmtRetTax()==null) || 
             (this.shpETypeAmtRetTax!=null &&
              this.shpETypeAmtRetTax.equals(other.getShpETypeAmtRetTax()))) &&
            ((this.shpType==null && other.getShpType()==null) || 
             (this.shpType!=null &&
              this.shpType.equals(other.getShpType()))) &&
            ((this.shpTypeAmt==null && other.getShpTypeAmt()==null) || 
             (this.shpTypeAmt!=null &&
              this.shpTypeAmt.equals(other.getShpTypeAmt()))) &&
            ((this.shpTypeAmtDisc==null && other.getShpTypeAmtDisc()==null) || 
             (this.shpTypeAmtDisc!=null &&
              this.shpTypeAmtDisc.equals(other.getShpTypeAmtDisc()))) &&
            ((this.shpTypeAmtTax==null && other.getShpTypeAmtTax()==null) || 
             (this.shpTypeAmtTax!=null &&
              this.shpTypeAmtTax.equals(other.getShpTypeAmtTax()))) &&
            ((this.shpTypeAmtRetTax==null && other.getShpTypeAmtRetTax()==null) || 
             (this.shpTypeAmtRetTax!=null &&
              this.shpTypeAmtRetTax.equals(other.getShpTypeAmtRetTax()))) &&
            ((this.formaPago==null && other.getFormaPago()==null) || 
             (this.formaPago!=null &&
              this.formaPago.equals(other.getFormaPago())));
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
        if (getImporte() != null) {
            _hashCode += getImporte().hashCode();
        }
        if (getAckType() != null) {
            _hashCode += getAckType().hashCode();
        }
        if (getAckTypeAmt() != null) {
            _hashCode += getAckTypeAmt().hashCode();
        }
        if (getAckTypeAmtDisc() != null) {
            _hashCode += getAckTypeAmtDisc().hashCode();
        }
        if (getAckTypeAmtTax() != null) {
            _hashCode += getAckTypeAmtTax().hashCode();
        }
        if (getAckTypeAmtRetTax() != null) {
            _hashCode += getAckTypeAmtRetTax().hashCode();
        }
        if (getCodAmount() != null) {
            _hashCode += getCodAmount().hashCode();
        }
        if (getCodTypeAmt() != null) {
            _hashCode += getCodTypeAmt().hashCode();
        }
        if (getCodTypeAmtDisc() != null) {
            _hashCode += getCodTypeAmtDisc().hashCode();
        }
        if (getCodTypeAmtTax() != null) {
            _hashCode += getCodTypeAmtTax().hashCode();
        }
        if (getCodTypeAmtRetTax() != null) {
            _hashCode += getCodTypeAmtRetTax().hashCode();
        }
        if (getDlvyType() != null) {
            _hashCode += getDlvyType().hashCode();
        }
        if (getDlvyTypeAmt() != null) {
            _hashCode += getDlvyTypeAmt().hashCode();
        }
        if (getDlvyTypeAmtDisc() != null) {
            _hashCode += getDlvyTypeAmtDisc().hashCode();
        }
        if (getDlvyTypeAmtTax() != null) {
            _hashCode += getDlvyTypeAmtTax().hashCode();
        }
        if (getDlvyTypeAmtRetTax() != null) {
            _hashCode += getDlvyTypeAmtRetTax().hashCode();
        }
        if (getTotldeclVlue() != null) {
            _hashCode += getTotldeclVlue().hashCode();
        }
        if (getInvType() != null) {
            _hashCode += getInvType().hashCode();
        }
        if (getInvTypeAmt() != null) {
            _hashCode += getInvTypeAmt().hashCode();
        }
        if (getInvTypeAmtDisc() != null) {
            _hashCode += getInvTypeAmtDisc().hashCode();
        }
        if (getInvTypeAmtTax() != null) {
            _hashCode += getInvTypeAmtTax().hashCode();
        }
        if (getInvTypeAmtRetTax() != null) {
            _hashCode += getInvTypeAmtRetTax().hashCode();
        }
        if (getRadType() != null) {
            _hashCode += getRadType().hashCode();
        }
        if (getRadTypeAmt() != null) {
            _hashCode += getRadTypeAmt().hashCode();
        }
        if (getRadTypeAmtDisc() != null) {
            _hashCode += getRadTypeAmtDisc().hashCode();
        }
        if (getRadTypeAmtTax() != null) {
            _hashCode += getRadTypeAmtTax().hashCode();
        }
        if (getRadTypeAmtRetTax() != null) {
            _hashCode += getRadTypeAmtRetTax().hashCode();
        }
        if (getShpEType() != null) {
            _hashCode += getShpEType().hashCode();
        }
        if (getShpETypeAmt() != null) {
            _hashCode += getShpETypeAmt().hashCode();
        }
        if (getShpETypeAmtDisc() != null) {
            _hashCode += getShpETypeAmtDisc().hashCode();
        }
        if (getShpETypeAmtTax() != null) {
            _hashCode += getShpETypeAmtTax().hashCode();
        }
        if (getShpETypeAmtRetTax() != null) {
            _hashCode += getShpETypeAmtRetTax().hashCode();
        }
        if (getShpType() != null) {
            _hashCode += getShpType().hashCode();
        }
        if (getShpTypeAmt() != null) {
            _hashCode += getShpTypeAmt().hashCode();
        }
        if (getShpTypeAmtDisc() != null) {
            _hashCode += getShpTypeAmtDisc().hashCode();
        }
        if (getShpTypeAmtTax() != null) {
            _hashCode += getShpTypeAmtTax().hashCode();
        }
        if (getShpTypeAmtRetTax() != null) {
            _hashCode += getShpTypeAmtRetTax().hashCode();
        }
        if (getFormaPago() != null) {
            _hashCode += getFormaPago().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Servicios.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">Servicios"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("otrosServicios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "OtrosServicios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">OtrosServicios"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importe");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "Importe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">Importe"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ackType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "ackType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">ackType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ackTypeAmt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "ackTypeAmt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ackTypeAmtDisc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "ackTypeAmtDisc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ackTypeAmtTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "ackTypeAmtTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ackTypeAmtRetTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "ackTypeAmtRetTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "codAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codTypeAmt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "codTypeAmt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codTypeAmtDisc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "codTypeAmtDisc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codTypeAmtTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "codTypeAmtTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codTypeAmtRetTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "codTypeAmtRetTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dlvyType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "dlvyType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">dlvyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dlvyTypeAmt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "dlvyTypeAmt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dlvyTypeAmtDisc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "dlvyTypeAmtDisc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dlvyTypeAmtTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "dlvyTypeAmtTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dlvyTypeAmtRetTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "dlvyTypeAmtRetTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totldeclVlue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "totldeclVlue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "invType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">invType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invTypeAmt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "invTypeAmt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invTypeAmtDisc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "invTypeAmtDisc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invTypeAmtTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "invTypeAmtTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invTypeAmtRetTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "invTypeAmtRetTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("radType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "radType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">radType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("radTypeAmt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "radTypeAmt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("radTypeAmtDisc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "radTypeAmtDisc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("radTypeAmtTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "radTypeAmtTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("radTypeAmtRetTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "radTypeAmtRetTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpEType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpEType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">shpEType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpETypeAmt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpETypeAmt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpETypeAmtDisc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpETypeAmtDisc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpETypeAmtTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpETypeAmtTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpETypeAmtRetTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpETypeAmtRetTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">shpType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpTypeAmt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpTypeAmt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpTypeAmtDisc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpTypeAmtDisc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpTypeAmtTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpTypeAmtTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shpTypeAmtRetTax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "shpTypeAmtRetTax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formaPago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", "formaPago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.paquetexpress.com/webbooking/Documentacion/retorno", ">formaPago"));
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
