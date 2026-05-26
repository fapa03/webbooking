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

public class ValidaFecha {

    public boolean validacionFecha(String fecha, int formato) {

        /*
         *Formatos Posibles:
        1. = AAAA/MM/DD 
        2. = AAAA/DD/MM
        3. = DD/MM/AAAA
        4. = MM/DD/AAAA
        5. = AA/MM/DD
        6. = AA/DD/MM
        7. = DD/MM/AA
        8. = MM/DD/AA
         */

        boolean pass = true;
        int format = formato;
        String a = null;
        String m = null;
        String d = null;

        switch (format) {
            case 1:
                //AAAA/MM/DD 
                a = fecha.substring(0, 4);
                m = fecha.substring(4, 6);
                d = fecha.substring(6, 8);
                break;
            case 2:
                // AAAA/DD/MM
                a = fecha.substring(0, 4);
                m = fecha.substring(6, 8);
                d = fecha.substring(4, 6);
                break;
            case 3:
                // DD/MM/AAAA 
                a = fecha.substring(6, 10);
                m = fecha.substring(3, 5);
                d = fecha.substring(0, 2);
                break;
            case 4:
                // MM/DD/AAAA
                a = fecha.substring(4, 8);
                m = fecha.substring(0, 2);
                d = fecha.substring(2, 4);
                break;
            case 5:
                // AA/MM/DD
                a = "20" + fecha.substring(0, 2);
                m = fecha.substring(2, 4);
                d = fecha.substring(4, 6);
                break;
            case 6:
                //  AA/DD/MM
                a = "20" + fecha.substring(0, 2);
                m = fecha.substring(4, 6);
                d = fecha.substring(2, 4);

                break;
            case 7:
                // DD/MM/AA
                a = "20" + fecha.substring(4, 6);
                m = fecha.substring(2, 4);
                d = fecha.substring(0, 2);
                break;
            case 8:
                // MM/DD/AA
                a = "20" + fecha.substring(4, 6);
                m = fecha.substring(0, 2);
                d = fecha.substring(2, 4);
                break;
            default:

                break;

        }

        if (validarAnio(a)) {
            if (validarMes(m)) {
                if (validarDia(d, m, a)) {
                    return pass;
                } else {
                    pass = false;
                }
            } else {
                pass = false;
            }
        } else {
            pass = false;
        }
        return pass;
    }

    private boolean validarDia(String ValorDia, String ValorMes, String ValorAno) {
        boolean exito = true;
        int mes = Integer.parseInt(ValorMes.trim());
        int dia = Integer.parseInt(ValorDia.trim());

        if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
            if (dia > 31) {
                exito = false;
            } else {
                exito = true;
            }
        } else {
            if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
                if (dia > 30) {
                    exito = false;
                } else {
                    exito = true;
                }
            } else {
                if (mes == 2) {
                    if (isBisiesto(ValorAno)) {
                        if (dia > 29) {
                            exito = false;
                        } else {
                            exito = true;
                        }
                    } else {
                        if (dia > 28) {
                            exito = false;
                        } else {
                            exito = true;
                        }
                    }
                }
            }
        }
        return exito;
    }

    private boolean isBisiesto(String valor) {
        boolean exito = false;
        if (isMods(valor, 4) && (!isMods(valor, 100) || isMods(valor, 400))) {
            exito = true;
        }
        return exito;
    }

    private boolean isMods(String valor, int valorE) {
        boolean exito = false;
        double divs = Double.valueOf(valor) % valorE;
        if (divs == 0) {
            exito = true;
        }
        return exito;
    }

    private boolean validarMes(String valor) {
        boolean exito = false;
        int mes = Integer.parseInt(valor.trim());
        if (mes > 0 && mes <= 12) {
            exito = true;
        }
        return exito;
    }

    private boolean validarAnio(String valor) {
        boolean exito = false;
        int anio = Integer.parseInt(valor.trim());
        if (anio > 1900) {
            exito = true;
        }
        return exito;
    }
}
