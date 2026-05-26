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


public class CadenaTexto {
    StringBuilder concatena = new StringBuilder();

    protected String quitarCompuestos(String palabra, String[] compuestos) {
        for (int i = 0; i < compuestos.length; ++i) {
            palabra = palabra.replace(compuestos[i], " ");
        }

        String[] compuestosTempI = new String[compuestos.length];
        String[] compuestosTempF = new String[compuestos.length];
        System.arraycopy(compuestos, 0, compuestosTempI, 0, compuestos.length);
        System.arraycopy(compuestos, 0, compuestosTempF, 0, compuestos.length);

        String palabraTemp = "";
        int i;
        int j;
        for (j = 0; j < compuestosTempI.length; ++j) {
            compuestosTempI[j] = quitarEspacioInicio(compuestosTempI[j]);
            for (i = 0; i < compuestosTempI[j].length(); ++i) {
                if (palabra.length() >= compuestosTempI[j].length()) {
                    palabraTemp = concatena.delete(0,concatena.length())
                            .append(palabraTemp).append( String.valueOf(palabra.charAt(i)) ).toString();
                }
            }

            if (compuestosTempI[j].equals(palabraTemp)) {
                palabra = palabra.replace(compuestosTempI[j], "");
            }

            palabraTemp = "";
        }

        for (j = 0; j < compuestosTempF.length; ++j) {
            compuestosTempF[j] = quitarEspacioFinal(compuestosTempF[j]);
            for (i = 0; i < compuestosTempF[j].length(); ++i) {
                if (palabra.length() >= compuestosTempF[j].length()) {
                    palabraTemp = concatena.delete(0,concatena.length())
                            .append(palabraTemp).append( String.valueOf(palabra.charAt(palabra.length() - i - 1)) ).toString();
                }
            }
            palabraTemp = invertirCadena(palabraTemp);
            if (compuestosTempF[j].equals(palabraTemp)) {
                palabra = palabra.replace(compuestosTempF[j], "");
            }
            palabraTemp = "";
        }

        for (i = 0; i < compuestos.length; ++i) {
            palabra = palabra.replace(compuestos[i], " ");
        }
        return palabra;
    }

    protected String validarPalabras(String curp) {
        String[] palabrasInconvenientes = {"BACA", "LOCO", "BAKA", "LOKA", "BUEI", "LOKO", "BUEY", "MAME", "CACA", "MAMO", "CACO", "MEAR", "CAGA", "MEAS", "CAGO", "MEON", "CAKA", "MIAR", "CAKO", "MION", "COGE", "MOCO", "COGI", "MOKO", "COJA", "MULA", "COJE", "MULO", "COJI", "NACA", "COJO", "NACO", "COLA", "PEDA", "CULO", "PEDO", "FALO", "PENE", "FETO", "PIPI", "GETA", "PITO", "GUEI", "POPO", "GUEY", "PUTA", "JETA", "PUTO", "JOTO", "QULO", "KACA", "RATA", "KACO", "ROBA", "KAGA", "ROBE", "KAGO", "ROBO", "KAKA", "RUIN", "KAKO", "SENO", "KOGE", "TETA", "KOGI", "VACA", "KOJA", "VAGA", "KOJE", "VAGO", "KOJI", "VAKA", "KOJO", "VUEI", "KOLA", "VUEY", "KULO", "WUEI", "LILO", "WUEY", "LOCA"};

        String[] palabrasConvenientes = {"BXCA", "LXCO", "BXKA", "LXKA", "BXEI", "LXKO", "BXEY", "MXME", "CXCA", "MXMO", "CXCO", "MXAR", "CXGA", "MXAS", "CXGO", "MXON", "CXKA", "MXAR", "CXKO", "MXON", "CXGE", "MXCO", "CXGI", "MXKO", "CXJA", "MXLA", "CXJE", "MXLO", "CXJI", "NXCA", "CXJO", "NXCO", "CXLA", "PXDA", "CXLO", "PXDO", "FXLO", "PXNE", "FXTO", "PXPI", "GXTA", "PXTO", "GXEI", "PXPO", "GXEY", "PXTA", "JXTA", "PXTO", "JXTO", "QXLO", "KXCA", "RXTA", "KXCO", "RXBA", "KXGA", "RXBE", "KXGO", "RXBO", "KXKA", "RXIN", "KXKO", "SXNO", "KXGE", "TXTA", "KXGI", "VXCA", "KXJA", "VXGA", "KXJE", "VXGO", "KXJI", "VXKA", "KXJO", "VXEI", "KXLA", "VXEY", "KXLO", "WXEI", "LXLO", "WXEY", "LXCA"};

        for (int i = 0; i < palabrasInconvenientes.length; ++i) {
            curp = curp.replace(palabrasInconvenientes[i], palabrasConvenientes[i]);
        }

        curp = curp.replace("Ñ", "X");
        return curp;
    }

    protected boolean esVocal(String vocal) {
        boolean banderaVocal = false;
        if (("A".equals(vocal)) || ("E".equals(vocal)) || ("I".equals(vocal)) || ("O".equals(vocal)) || ("U".equals(vocal))) {
            banderaVocal = true;
        }

        return banderaVocal;
    }

    protected char obtieneConsonante(String palabra) {
        boolean bandera = true;
        int contador = 1;
        char letra = '\000';
        while (bandera) {
            if (esVocal(String.valueOf(palabra.charAt(contador)))) {
                ++contador;
            }

            letra = palabra.charAt(contador);

            bandera = false;
        }

        return letra;
    }

    protected String quitarEspacios(String cadena) {
        String cadenaTemp = "";
        int i;
        if (String.valueOf(cadena.charAt(0)).equals(" ")) {
            for (i = 1; i < cadena.length(); ++i) {
                cadenaTemp = concatena.delete(0,concatena.length())
                        .append(cadenaTemp).append( String.valueOf(cadena.charAt(i)) ).toString();
            }
            cadena = cadenaTemp;
        }
        
        if (String.valueOf(cadena.charAt(cadena.length() - 1)).equals(" ")) {
            for (i = 0; i < cadena.length() - 1; ++i) {
                cadenaTemp = concatena.delete(0,concatena.length())
                        .append(cadenaTemp).append( String.valueOf(cadena.charAt(i)) ).toString();
            }
            cadena = cadenaTemp;
        }
        return cadena;
    }

    protected String quitarEspacioInicio(String cadena) {
        String cadenaTemp = "";
        if (String.valueOf(cadena.charAt(0)).equals(" ")) {
            for (int i = 1; i < cadena.length(); ++i) {
                cadenaTemp = concatena.delete(0,concatena.length())
                        .append( cadenaTemp ).append( String.valueOf(cadena.charAt(i)) ).toString();
            }
            cadena = cadenaTemp;
        }
        return cadena;
    }

    protected String quitarEspacioFinal(String cadena) {
        String cadenaTemp = "";
        if (String.valueOf(cadena.charAt(cadena.length() - 1)).equals(" ")) {
            for (int i = 0; i < cadena.length() - 1; ++i) {
                cadenaTemp = concatena.delete(0,concatena.length())
                        .append(cadenaTemp).append( String.valueOf(cadena.charAt(i)) ).toString();
            }
            cadena = cadenaTemp;
        }
        return cadena;
    }

    protected String quitarNombres(String nombre) {
        String[] nombres = {"MAR\u00C1A ", "JOS\u00C9 ", "MARIA ", "JOSE "};
        for (int i = 0; i < nombres.length; ++i) {
            if (nombre.contains(nombres[i])) {
                nombre = nombre.replace(nombres[i], "");
            }
        }
        return nombre;
    }

    protected String invertirCadena(String cadena) {
        String cadenaInv = "";
        for (int i = 0; i < cadena.length(); ++i) {
            cadenaInv = concatena.delete(0,concatena.length())
                    .append(cadenaInv).append( cadena.charAt(cadena.length() - 1 - i) ).toString();
        }
        return cadenaInv;
    }
}
