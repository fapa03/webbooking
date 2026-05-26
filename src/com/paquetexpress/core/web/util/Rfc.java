/**
 * @author:
 * Fecha de Creación: JOSE ARMANDO SANCHEZ
 * Descripción del programa:PARA VALIDAR LOS RFC FISICA O MORAL
 * -----------------------------------------------------------------------------
 * MODIFICACIONES:
 * -----------------------------------------------------------------------------
 * Clave:
 * Autor:
 * Fecha:
 * Descripción:
 * -----------------------------------------------------------------------------
 * Clave:
 * Autor:
 * Fecha:
 * Descripción:
 * -----------------------------------------------------------------------------
 */
package com.paquetexpress.core.web.util;

public class Rfc extends CadenaTexto {

	public boolean isRfcGenerico(String valorRFC) {
		boolean rfcGenerico = false;
		if (valorRFC != null) {
			if (valorRFC.equalsIgnoreCase("XAXX-010101-000")) {
				rfcGenerico = true;
			} else {
				valorRFC = valorRFC.replace("-", "");
				if (!validarRfcFisica(valorRFC)) {
					if (!validarRfcMoral(valorRFC)) {
						rfcGenerico = true;
					}
				}
			}
		} else {
			rfcGenerico = true;
		}
		return rfcGenerico;
	}
    public boolean validarRfcFisica(String rfc) {
        rfc = rfc.toUpperCase();
        String rfcTemp = rfc;
        //rfcTemp = validarPalabras(rfcTemp);
        boolean banderaValidar;
        ValidaFecha fecha = new ValidaFecha();
        if ((rfc.matches("[A-Z,Ñ,&]{4}[0-9]{2}[0,1][0-9]{3}[A-Z-0-9]{3}")) && (rfc.compareTo(rfcTemp) == 0)) {
            banderaValidar = fecha.validacionFecha(rfc.substring(4, 10), 5);
        } else {
            banderaValidar = false;
        }

        return banderaValidar;
    }

    public boolean validarRfcMoral(String rfc) {
        rfc = rfc.toUpperCase();
        String rfcTemp = rfc;
        //rfcTemp = validarPalabras(rfcTemp);
        boolean banderaValidar;
        ValidaFecha fecha = new ValidaFecha();
        if ((rfc.matches("[A-Z,Ñ,&]{3}[0-9]{2}[0,1][0-9]{3}[A-Z-0-9]{3}")) && (rfc.compareTo(rfcTemp) == 0)) {
            banderaValidar = fecha.validacionFecha(rfc.substring(3, 9), 5);
        } else {
            banderaValidar = false;
        }

        return banderaValidar;
    }
}
