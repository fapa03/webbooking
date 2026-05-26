/// Authentication setup ///
qz.security.setCertificatePromise(function (resolve, reject) {
	resolve("-----BEGIN CERTIFICATE----- MIIFhzCCA3GgAwIBAgIFMTQ0MDkwCwYJKoZIhvcNAQEFMIGYMQswCQYDVQQGEwJV UzELMAkGA1UECAwCTlkxGzAZBgNVBAoMElFaIEluZHVzdHJpZXMsIExMQzEbMBkG A1UECwwSUVogSW5kdXN0cmllcywgTExDMRkwFwYDVQQDDBBxemluZHVzdHJpZXMu Y29tMScwJQYJKoZIhvcNAQkBFhhzdXBwb3J0QHF6aW5kdXN0cmllcy5jb20wHhcN MjIwNjE5MDQwMDAwWhcNMjcwNzA5MDQwMDAwWjCCAVAxCzAJBgNVBAYMAk1YMRAw DgYDVQQIDAdTSU5BTE9BMREwDwYDVQQHDAhNQVpBVExBTjE6MDgGA1UECgwxT1BF UkFET1JBIERFIFNFUlZJQ0lPUyBQQVFVRVRFWFBSRVNTIFMuQS4gREUgQy5WLjE6 MDgGA1UECwwxT1BFUkFET1JBIERFIFNFUlZJQ0lPUyBQQVFVRVRFWFBSRVNTIFMu QS4gREUgQy5WLjE6MDgGA1UEAwwxT1BFUkFET1JBIERFIFNFUlZJQ0lPUyBQQVFV RVRFWFBSRVNTIFMuQS4gREUgQy5WLjEqMCgGCSqGSIb3DQEJAQwbbWRpbWFzQHBh cXVldGV4cHJlc3MuY29tLm14MTwwOgYDVQQNDDNyZW5ld2FsLW9mLWFiYTg0NjQ3 MjJkMmNjOWZjYTBlNTJiYzJkNjZkZTVlZGQxYmZkZjYwggEgMAsGCSqGSIb3DQEB AQOCAQ8AMIIBCgKCAQEA37kbjW2coBjvVc8ONW73JeNzMeVkZOxac+yAmcRDOO9G 5U5sZnDvmVm5zLAofeSbCoKB09EedX/7134U7WR3jUsk++C9ZC0zdyjRwzwv5MxW paufSjpZa+mNmlGWBiZeHfbIVc93LP+On7Q7TCHz1vVJikxpAMcRzAw6c3FEmi3f f9LnwcZ0RjWfUl4c2RgLqmpmGw1X4lPmk9kDnTb3aFYW/5q0Dmg04ezoVW7bKox3 9jUymOFezqGaQym1DTZo/lQkl7b1hNWLyVtmHm3yWixmijWm/IVFmKikn9HLDsZf oKs3fEAe0M7Gg8p8ZA2lhlNfIljmxFHImSFV/nMCvQIDAQABoyMwITAfBgNVHSME GDAWgBSQplC3hNS56l/yBYQTeEXoqXVUXDALBgkqhkiG9w0BAQUDggIBABgOkoCN kNr2N9DZBzySYBHCRx0/w+rvSWrKw6C0AH5tcIaMyWHtyRxsR18H8SmgfN6mYJqt tU8xl09sjAWGAEAfxct1IdcKZYWMH0azQ4ldIk5D+f6O3DDGXUiWRDPMZJI67i7f 17bY1GpKLIqDd4ZRNMXX2ANiOizMIETOtt2+NCPDYb86YNjDraI7CSTlRsrgS4Zr gZRLq2Qe2LryFiIjm9izJB0wh+wkcIm1U9ODgPR3+Xg32CmO52ch42+dVDRHXSL0 3ppPk0VLmpOijfImYIwUMNMGnd40BGSN8nhaO0oFCskfiaNpyUKjgbMPMiMeI6bq wXzBFHZhLywX8R+PqrXKv59TOaB+JX46uXHqkqTUnKMy6y30fIV/ooVnrSARcCny e5URLoPKlD2x32Ml3SI/I3fMZGLSgHgF9nT9qQHzHKFy2zwrPziuXH9bEb0bDUpa L9VNtKYEtlYBjglUty0C23vNu91/rwYYTwbww+ndKP0nnsISX/dyCU4jvLBjkZGF 2ylgpl1brc71mWEHXzEEIFXees1QnDQt1YjNv6vwrAPJ/eaeOP6KhYhXANXGdJlv dC4upJpPo9QZMgv6VkuR+DWojxVi3tj/oZ4CaOS1X0c0Bf9PQi8Vdu41Yy2CPM0o TCJAwf2P/jfVp1WoVDG9OKQz3sLvimYku7/s -----END CERTIFICATE----- --START INTERMEDIATE CERT-- -----BEGIN CERTIFICATE----- MIIFEjCCA/qgAwIBAgICEAAwDQYJKoZIhvcNAQELBQAwgawxCzAJBgNVBAYTAlVT MQswCQYDVQQIDAJOWTESMBAGA1UEBwwJQ2FuYXN0b3RhMRswGQYDVQQKDBJRWiBJ bmR1c3RyaWVzLCBMTEMxGzAZBgNVBAsMElFaIEluZHVzdHJpZXMsIExMQzEZMBcG A1UEAwwQcXppbmR1c3RyaWVzLmNvbTEnMCUGCSqGSIb3DQEJARYYc3VwcG9ydEBx emluZHVzdHJpZXMuY29tMB4XDTE1MDMwMjAwNTAxOFoXDTM1MDMwMjAwNTAxOFow gZgxCzAJBgNVBAYTAlVTMQswCQYDVQQIDAJOWTEbMBkGA1UECgwSUVogSW5kdXN0 cmllcywgTExDMRswGQYDVQQLDBJRWiBJbmR1c3RyaWVzLCBMTEMxGTAXBgNVBAMM EHF6aW5kdXN0cmllcy5jb20xJzAlBgkqhkiG9w0BCQEWGHN1cHBvcnRAcXppbmR1 c3RyaWVzLmNvbTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBANTDgNLU iohl/rQoZ2bTMHVEk1mA020LYhgfWjO0+GsLlbg5SvWVFWkv4ZgffuVRXLHrwz1H YpMyo+Zh8ksJF9ssJWCwQGO5ciM6dmoryyB0VZHGY1blewdMuxieXP7Kr6XD3GRM GAhEwTxjUzI3ksuRunX4IcnRXKYkg5pjs4nLEhXtIZWDLiXPUsyUAEq1U1qdL1AH EtdK/L3zLATnhPB6ZiM+HzNG4aAPynSA38fpeeZ4R0tINMpFThwNgGUsxYKsP9kh 0gxGl8YHL6ZzC7BC8FXIB/0Wteng0+XLAVto56Pyxt7BdxtNVuVNNXgkCi9tMqVX xOk3oIvODDt0UoQUZ/umUuoMuOLekYUpZVk4utCqXXlB4mVfS5/zWB6nVxFX8Io1 9FOiDLTwZVtBmzmeikzb6o1QLp9F2TAvlf8+DIGDOo0DpPQUtOUyLPCh5hBaDGFE ZhE56qPCBiQIc4T2klWX/80C5NZnd/tJNxjyUyk7bjdDzhzT10CGRAsqxAnsjvMD 2KcMf3oXN4PNgyfpbfq2ipxJ1u777Gpbzyf0xoKwH9FYigmqfRH2N2pEdiYawKrX 6pyXzGM4cvQ5X1Yxf2x/+xdTLdVaLnZgwrdqwFYmDejGAldXlYDl3jbBHVM1v+uY 5ItGTjk+3vLrxmvGy5XFVG+8fF/xaVfo5TW5AgMBAAGjUDBOMB0GA1UdDgQWBBSQ plC3hNS56l/yBYQTeEXoqXVUXDAfBgNVHSMEGDAWgBQDRcZNwPqOqQvagw9BpW0S BkOpXjAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQAJIO8SiNr9jpLQ eUsFUmbueoxyI5L+P5eV92ceVOJ2tAlBA13vzF1NWlpSlrMmQcVUE/K4D01qtr0k gDs6LUHvj2XXLpyEogitbBgipkQpwCTJVfC9bWYBwEotC7Y8mVjjEV7uXAT71GKT x8XlB9maf+BTZGgyoulA5pTYJ++7s/xX9gzSWCa+eXGcjguBtYYXaAjjAqFGRAvu pz1yrDWcA6H94HeErJKUXBakS0Jm/V33JDuVXY+aZ8EQi2kV82aZbNdXll/R6iGw 2ur4rDErnHsiphBgZB71C5FD4cdfSONTsYxmPmyUb5T+KLUouxZ9B0Wh28ucc1Lp rbO7BnjW -----END CERTIFICATE-----");
});

/// Connection ///
function launchQZ() {
    if (!qz.websocket.isActive()) {
        window.location.assign("qz:launch");
        startConnection({retries: 5, delay: 1});
    }
}

function startConnection(config) {
    if (!qz.websocket.isActive()) {
        updateState('Waiting', 'default');

        qz.websocket.connect(config).then(function () {
            updateState('Active', 'success');
            findVersion();
        }).catch(handleConnectionError);
    } else {
        //displayError("Ya existe una impresora activa.");
        //alert('An active connection with QZ already exists.  Alert-warning');
    }
}

function endConnection() {
    if (qz.websocket.isActive()) {
        qz.websocket.disconnect().then(function () {
            updateState('Inactive', 'default');
        }).catch(handleConnectionError);
    } else {
        displayMessage('No active connection with QZ exists.', 'alert-warning');
    }
}

function listNetworkInfo() {
    qz.websocket.getNetworkInfo().then(function (data) {
        if (data.macAddress == null) {
            data.macAddress = 'UNKNOWN';
        }
        if (data.ipAddress == null) {
            data.ipAddress = "UNKNOWN";
        }

        var macFormatted = '';
        for (var i = 0; i < data.macAddress.length; i++) {
            macFormatted += data.macAddress[i];
            if (i % 2 == 1 && i < data.macAddress.length - 1) {
                macFormatted += ":";
            }
        }

        displayMessage("<strong>IP:</strong> " + data.ipAddress + "<br/><strong>Physical Address:</strong> " + macFormatted);
    }).catch(displayError);
}

/// Detection ///
function findPrinter(query, set, origenPrint, printer, typePrinter, defaultPrinter) {
    $("#printerSearch").val(query);

    qz.printers.find(query).then(function (data) {

        displayMessage("<strong>Found:</strong> " + data);
        mostrarMensaje("<strong>Se asigno la impresora:</strong><br> " + data);
        if (set) {
            setPrinter(data);
            if (origenPrint === '1') {
                if (typePrinter === "S") {
                    if (printer !== namePrinterGuia) {
                        printZPL2();
                    } else {
                        var cf = getUpdatedConfig();
                        cf.setPrinter(defaultPrinter);
                        printGuia();
                    }
                } else {
                    printGroupping();
                }
//                printGroupping();
            } else if (origenPrint === '2') {
                printSEG();
            } else if (origenPrint === '3') {
                printMNI();
            }
            console.log("Se envio impresion de etiquetas y guias en..." + data);

        }
    }).catch(displayError);
    return "Y";
}

function findDefaultPrinter(set) {
    qz.printers.getDefault().then(function (data) {
        if (set) {
            setPrinter(data);
        }
    }).catch(displayError);
}

function findPrinters() {
    qz.printers.find().then(function (data) {
        var list = '';
        for (var i = 0; i < data.length; i++) {
            list += "&nbsp; " + data[i] + "<br/>";
        }

        displayMessage("<strong>Available printers:</strong><br/>" + list);
    }).catch(displayError);
}


/// Raw Printers ///
//function printEPL() {
//    var config = getUpdatedConfig();
//
//    var printData = [
//        '\nN\n',
//        'q609\n',
//        'Q203,26\n',
//        'B5,26,0,1A,3,7,152,B,"1234"\n',
//        'A310,26,0,3,1,1,N,"SKU 00000 MFG 0000"\n',
//        'A310,56,0,3,1,1,N,"QZ PRINT APPLET"\n',
//        'A310,86,0,3,1,1,N,"TEST PRINT SUCCESSFUL"\n',
//        'A310,116,0,3,1,1,N,"FROM SAMPLE.HTML"\n',
//        'A310,146,0,3,1,1,N,"QZINDUSTRIES.COM"\n',
//        {type: 'raw', format: 'image', data: 'assets/img/image_sample_bw.png', options: {language: 'EPL', x: 150, y: 300}},
//        '\nP1,1\n'
//    ];
//
//    qz.print(config, printData).catch(displayError);
//}

function printZPL12() {
    var config = getUpdatedConfig();

    var printData = [
        '^XA\n',
        '^FO50,50^ADN,36,20^FDPRINTED USING QZ PRINT PLUGIN ' + qzVersion + '\n',
        {type: 'raw', format: 'image', data: '/glp/resources/js/printZPL/assets/img/image_sample_bw.png', options: {language: 'ZPLII'}},
        '^FS\n',
        '^XZ\n'
    ];

    qz.print(config, printData).catch(displayError);
}

var namePrinterEtiqueta = "";
var namePrinterGuia = "";
var namePrinterGroupping = "";

function validaInitPrinter(xhr, status, args) {
    var sucess = eval("args.sucess");
    if (typeof (sucess) !== "undefined") {
        sucess = sucess.replace('"', "");
        sucess = sucess.replace('"', "");
        var type = $("#form\\:solicitudes\\:labelTypePrint").val();
        if ((type !== "N") && (type !== "0") && (type !== "")) {
            if (sucess === "Y") {
                initPrinter();
            }
        }
    }
}

//<![CDATA[
function handleChangeTypePrint(xhr, status, args) {
    try {
        var typePrint = eval("args.typePrint");
        typePrint = typePrint.replace('"', "");
        typePrint = typePrint.replace('"', "");

        var typeConfig = eval("args.typeConfig");
        typeConfig = typeConfig.replace('"', "");
        typeConfig = typeConfig.replace('"', "");

        $("#form\\:solicitudes\\:labelTypePrint").val(typePrint);
        loadCookie(typePrint, typeConfig);
    } catch (err) {
        console.log(err.message);
    }

}
// ]]>

//<![CDATA[
function handleChangeTypePrintOnLoadNew(xhr, status, args) {
    try {
        var typePrint = eval("args.typePrint");
        typePrint = typePrint.replace('"', "");
        typePrint = typePrint.replace('"', "");

        var typeConfig = eval("args.typeConfig");
        typeConfig = typeConfig.replace('"', "");
        typeConfig = typeConfig.replace('"', "");

        $(status).val(typePrint);
    } catch (err) {
        console.log(err.message);
    }

}

function handleChangeTypePrintOnLoad(xhr, status, args) {
    try {
        var typePrint = eval("args.typePrint");
        typePrint = typePrint.replace('"', "");
        typePrint = typePrint.replace('"', "");

        var typeConfig = eval("args.typeConfig");
        typeConfig = typeConfig.replace('"', "");
        typeConfig = typeConfig.replace('"', "");

        $("#form\\:solicitudes\\:labelTypePrint").val(typePrint);
    } catch (err) {
        console.log(err.message);
    }

}
// ]]>    

function loadCookie(type, config) {
    document.cookie = "typePrint=;";
    ;
    document.cookie = "typePrint=" + type;
    console.log(document.cookie);
    if (config !== "") {
        if (config === "N") {
            initPrinter();
        }
    }
}

function initPrinterSEG() {
    startConnection();
    goToPrintSEG('ptxGroupingGLP', 'A');
}

function initPrinterMNI() {
    startConnection();
    goToPrintMNI('ptxGroupingGLP', 'A');
}

function goToPrintSEG(printer, type) {
    if (type !== "") {
        setTimeout(function () {
            asignarImpresoraSEG(printer, type);
        }, 1000);
    } else {
        displayError("Problemas al leer el tipo de impresion.");
    }
}

function goToPrintMNI(printer, type) {
    if (type !== "") {
        setTimeout(function () {
            asignarImpresoraMNI(printer, type);
        }, 1000);
    } else {
        displayError("Problemas al leer el tipo de impresion.");
    }
}

function asignarImpresoraSEG(printer, type) {
    var defaultPrinter = "";

    qz.printers.find().then(function (data) {
        //alert(data);
        for (var i = 0; i < data.length; i++) {
            if (data[i].indexOf(printer) > -1) {
                defaultPrinter = data[i];
                //break;
            }
        }

        if (defaultPrinter !== "") {
            findPrinter(defaultPrinter, true, '2', '', '', '');
//                setTimeout(function () {
//                    printSEG();
//                    console.log("Se envio impresion de etiquetas y guias en..." + defaultPrinter);
//                }, 1000);
        }
    }).catch(displayError);
}

function asignarImpresoraMNI(printer, type) {
    var defaultPrinter = "";

    qz.printers.find().then(function (data) {
        //alert(data);
        for (var i = 0; i < data.length; i++) {
            if (data[i].indexOf(printer) > -1) {
                defaultPrinter = data[i];
                //break;
            }
        }

        if (defaultPrinter !== "") {
            findPrinter(defaultPrinter, true, '3', '', '', '');
//                setTimeout(function () {
//                    printMNI();
//                    console.log("Se envio impresion de etiquetas y guias en..." + defaultPrinter);
//                }, 1000);
        }
    }).catch(displayError);
}

function printSEG() {
    var config = getUpdatedConfig();
    var zpl = $("#frmTable\\:txtZPLGrouping").html();
    //console.log(zpLLabel);
    if (zpl !== "") {
        var printDataLabel = [
            zpl
        ];
        qz.print(config, printDataLabel).catch(displayError);
    }
}

function printMNI() {
    var config = getUpdatedConfig();
    var zpl = $("#frmTable\\:txtZPLGrouping").html();
    //console.log(zpLLabel);
    if (zpl !== "") {
        var printDataLabel = [
            zpl
        ];
        qz.print(config, printDataLabel).catch(displayError);
    }
}

function initPrinter() {
    startConnection();
    var type = $("#form\\:solicitudes\\:labelTypePrint").val();
    if (type === "S") {
        namePrinterEtiqueta = $("#form\\:solicitudes\\:outPutImpLabel").val();
        namePrinterGuia = $("#form\\:solicitudes\\:outPutImpGuia").val();
        goToPrint(namePrinterEtiqueta, 'S');
    } else {
        if (type === "A") {
            namePrinterGroupping = $("#form\\:solicitudes\\:outPutImpAll").val();
            goToPrint(namePrinterGroupping, 'A');
        }
    }
}

function goToPrintGroupping(printer) {

    var zplInput = $("#form\\:txtZPLGrouping").val();
    if (zplInput !== "") {
        setTimeout(function () {
            asignarImpresora(printer);
        }, 0000);
    } else {
        displayError("Seleccione al menos una gu�a para imprimir.");
    }
}

function goToPrint(printer, type) {
    if (type !== "") {
        setTimeout(function () {
            asignarImpresora(printer, type);
        }, 1000);
    } else {
        displayError("Problemas al leer el tipo de impresion.");
    }
}

function asignarImpresora(printer, type) {
    var defaultPrinter = "";

    qz.printers.find().then(function (data) {
        for (var i = 0; i < data.length; i++) {
            if (data[i].indexOf(printer) > -1) {
                defaultPrinter = data[i];
            }
        }

        if (defaultPrinter !== "") {
            findPrinter(defaultPrinter, true, '1', printer, type, defaultPrinter);
//                    console.log('5');
//                    if (type === "S") {
//                        if (printer !== namePrinterGuia) {
//
//                            setTimeout(function () {
//                                printZPL2();
//                                console.log("Se envio impresion de etiquetas en..." + defaultPrinter);
//                            }, 1250);
//                        } else {
//                            setTimeout(function () {
//                                var cf = getUpdatedConfig();
//                                cf.setPrinter(defaultPrinter);
//                                printGuia();
//                                console.log("Se envio impresion de guias en.." + defaultPrinter);
//                            }, 1250);
//
//                        }
//                    }else{
//                        setTimeout(function () {
//                            printGroupping();
//                            console.log("Se envio impresion de etiquetas y guias en..." + defaultPrinter);
//                        }, 1250);
//                    }

        } else {
            alert("No se encontro la impresora: " + printer);
        }

    }).catch(displayError);
}

function printGroupping() {
    var config = getUpdatedConfig();
    var zplGroup = $("#form\\:txtZPLGrouping").html();
    //console.log(zpLLabel);
    if (zplGroup !== "") {

        var printDataLabel = [
            '^ XA',
            '~DGZ,03540,010,00001FFFFFFFFFFFFFF8',
            '0001FFFFFFFFFFFFFFF8',
            '0003FFFFFFFFFFFFFFF8',
            '001FFFFFFFFFFFFFFFF8',
            '007FFFFFFFFFFFFFFFF8',
            '007FFFFFFFFFFFFFFFF8',
            '017FFFFFFFFFFFFFFFF8',
            '03FFFFFFFFFFFFFFFFF8',
            '07FFFFFFFFFFFFFFFFF8',
            '0FFFFFFFFFFFFFFFFFF8',
            '0FFFFFFFFFFFFFFFFFF8',
            '0FFFFFFFFFFFFFFFFFF8',
            '1FFFFFFFFFFFFFFFFFF8',
            '1FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '7FFFFFFFFFFFFFFFFFF8',
            '7FFFFFFFFFFFFFFFFFF8',
            '7FFFF800000000000000',
            '7FFFE000000000000000',
            '7FFF0000000000000000',
            '7FFE0000000000000000',
            '7FFC01FFFFFFFFFFFFF8',
            '7FF80FFFFFFFFFFFFFF8',
            '7FF03FFFFFFFFFFFFFF8',
            '7FE0FFFFFFFFFFFFFFF8',
            '7FC1FFFFFFFFFFFFFFF8',
            '7F81FFFFFFFFFFFFFFF8',
            '7F83FFFFFFFFFFFFFFF8',
            '7F83FFFFFFFFFFFFFFF8',
            '7F07FFFFFFFFFFFFFFF8',
            '7F0FFFC0000000000000',
            '7F0FFE00000000000000',
            '7F0FF800000000000000',
            '7F0FF000000000000000',
            '7F0FE000000000000000',
            '7F1FE000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC000001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001FC01FC000',
            '7F1FC000001FE03FC000',
            '7F1FC000000FFFFF8000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000003FFFE0000',
            '7F1FC0000000FFFC0000',
            '7F1FC00000003F700000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC030000000000000',
            '7F1FC03FC00000000000',
            '7F1FC03FFF0000000000',
            '7F1FC03FFFF000000000',
            '7F1FC03FFFFFC0000000',
            '7F1FC03FFFFFFF000000',
            '7F1FC03FFFFFFFF20000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC0003F07FFFFC000',
            '7F1FC0003F0001FFC000',
            '7F1FC0003F003FFFC000',
            '7F1FC0003F7FFFFFC000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC003FFFFFFFF8000',
            '7F1FC03FFFFFFFC00000',
            '7F1FC03FFFFFFE000000',
            '7F1FC03FFFFF00000000',
            '7F1FC03FFFC000000000',
            '7F1FC03FFE0000000000',
            '7F1FC03F000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000002010000000',
            '7F1FC00003FFFF000000',
            '7F1FC0003FFFFFE00000',
            '7F1FC0007FFFFFFC0000',
            '7F1FC003FFFFFFFE0000',
            '7F1FC007FFFFFFFF0000',
            '7F1FC00FFFFFFFFF8000',
            '7F1FC00FFF000FFFC000',
            '7F1FC01FF000007F8000',
            '7F1FC03FE200003FC000',
            '7F1FC03FC600001FE000',
            '7F1FC03FCE00001FE000',
            '7F1FC03FFFC0001FE000',
            '7F1FC03FFF80001FE000',
            '7F1FC03FFF00003FC000',
            '7F1FC01FFE0000BFC000',
            '7F1FC00FFE0003FFC000',
            '7F1FC00FFFFFFFFF8000',
            '7F1FC03FFFFFFFFF0000',
            '7F1FC07FFFFFFFFE0000',
            '7F1FC0FCFFFFFFE80000',
            '7F1FC03C1FFFFFE00000',
            '7F1FC03807FFFF000000',
            '7F1FC010000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC001FFFFFFFFC000',
            '7F1FC003FFFFFFFFC000',
            '7F1FC007FFFFFFFFC000',
            '7F1FC00FFFFFFFFFC000',
            '7F1FC01FFFFFFFFFC000',
            '7F1FC01FFF0000000000',
            '7F1FC03FC00000000000',
            '7F1FC03FC00000000000',
            '7F1FC03F800000000000',
            '7F1FC03F800000000000',
            '7F1FC03FC00000000000',
            '7F1FC03FC00000000000',
            '7F1FC01FF80000000000',
            '7F1FC01FFFFFFFFFC000',
            '7F1FC017FFFFFFFFC000',
            '7F1FC00FFFFFFFFFC000',
            '7F1FC007FFFFFFFFC000',
            '7F1FC001FFFFFFFFC000',
            '7F1FC0007FFFFFFFC000',
            '7F1FC000040000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC000001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC0200000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC000001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC020000000000000',
            '7F1FC030000000000000',
            '7F1FC03F00000001C000',
            '7F1FC03FE000000FC000',
            '7F1FC03FF40000BFC000',
            '7F1FC03FFF0003FFC000',
            '7F1FC03FFFE01FFFC000',
            '7F1FC03FFFFCFFFFC000',
            '7F1FC006FFFFFFFD8000',
            '7F1FC0007FFFFFF00000',
            '7F1FC00003FFFF000000',
            '7F1FC00003FFFF000000',
            '7F1FC0001BFFFFC00000',
            '7F1FC001FFFFFFFC0000',
            '7F1FC007FFFBFFFF8000',
            '7F1FC03FFFD86FFFC000',
            '7F1FC03FFF8007FFC000',
            '7F1FC03FFE0003FFC000',
            '7F1FC03FF000003FC000',
            '7F1FC03F80000007C000',
            '7F1FC03E00000001C000',
            '7F1FC030000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC020000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC020001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001FC00FC000',
            '7F1FC000001FE01FC000',
            '7F1FC000000FFFFFC000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000001FFFF0000',
            '7F1FC0000001FFFC0000',
            '7F1FC00000007FF80000',
            '7F1FC000000003000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC00000FF800FC000',
            '7F1FC000037F800FC000',
            '7F1FC00007FF800FC000',
            '7F1FC000DFFFC0078000',
            '7F1FC007FFFFD03FC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFE7FFFF8000',
            '7F1FC03FFF03FFFF8000',
            '7F1FC03FFC03FFFF0000',
            '7F1FC03FC000FFFC0000',
            '7F1FC03E00001FE00000',
            '7F1FC038000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC008001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC00000000FE00000',
            '7F1FC00FE0007FFC0000',
            '7F1FC01FE001FFFF0000',
            '7F1FC03FC003FFFF8000',
            '7F1FC03FC00FFFFFC000',
            '7F1FC03FC01FFFFFC000',
            '7F1FC03FC01FFFFFE000',
            '7F1FC03FC07FF01FE000',
            '7F1FC03FFF7FE00FE000',
            '7F1FC01FFFFFC00FE000',
            '7F1FC017FFFE000FC000',
            '7F1FC00FFFFE001FC000',
            '7F1FC007FFFC001FC000',
            '7F1FC001FFF8003F8000',
            '7F1FC0007BC000000000',
            '7F1FC000040000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC00000000FE00000',
            '7F1FC00FE0007FFC0000',
            '7F1FC01FE000FFFF0000',
            '7F1FC03F8003FFFF8000',
            '7F1FC03FC00FFFFFC000',
            '7F1FC03FC01FFFFFC000',
            '7F1FC03FC01FFBFFC000',
            '7F1FC03F807FF81FE000',
            '7F1FC03FE8FFE00FE000',
            '7F1FC01FFFFFC00FE000',
            '7F1FC00FFFFF800FC000',
            '7F1FC00FFFFF001FC000',
            '7F1FC003FFF8001FC000',
            '7F1FC001FFF8003F8000',
            '7F1FC0017FD000000000',
            '7F1FC0001F0000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC034000000000000',
            '7F1FC038000000000000',
            '7F1FC03C000000000000',
            '7F1FC020000000000000',
            '7F1FC020000000000000',
            '7F1FC03C000000000000',
            '7F1FC004000000000000',
            '7F1FC038000000000000',
            '7F1FC020000000000000',
            '00000000000000000000',
            zplGroup
        ];
        qz.print(config, printDataLabel).catch(displayError);
    }
}

function initMacAddress() {
    var macAddr = getMacAddress();
    if (macAddr !== '') {
        myRemote([{name: 'macAddr', value: macAddr}]);
    } else {
        displayError("Error 425.");
    }
    //initPrinter();
}

//getMacAddress();

function getMacAddress() {
    var macFormatted = '';
    //startConnection();
    //setTimeout(function () {
    qz.websocket.getNetworkInfo().then(function (data) {
        if (data.macAddress == null) {
            data.macAddress = 'UNKNOWN';
        }
        if (data.ipAddress == null) {
            data.ipAddress = "UNKNOWN";
        }

        for (var i = 0; i < data.macAddress.length; i++) {
            macFormatted += data.macAddress[i];
            if (i % 2 == 1 && i < data.macAddress.length - 1) {
                macFormatted += ":";
            }
        }

        myRemoteMacAddr([{name: 'macAddr', value: macFormatted}]);
        console.log("formated.." + macFormatted);
    }).catch(displayError);

    //}, 3000);

}

//    function myFunctionAsyncron() {
//        startConnection();
//        var p = new Promise();
//        setTimeout(p.complete, 2000);
//        return p;
//    }
//    
//    function myMack() {
//        getMacAddress();
//        var p = new Promise();
//        setTimeout(p.complete, 2000)        
//        return p;
//    }    

//myRemoteValidaCookiePrint();
//validaTypePrintCookie();

function validaTypePrintCookie() {
    var cookieTypePrint = document.cookie;
    console.log(cookieTypePrint);
    myRemoteValidaCookiePrint([{name: 'typePrint', value: cookieTypePrint}]);
}

//    function validaCookiePrint() {
//        var flag = "";
//        flag = $("#form\\:solicitudes\\:flagMacAddrInput").val();
//        console.log("FLAG.." + flag);
//    }

//function validaExisteMac() {
//    var flag = "";
//    flag = $("#form\\:solicitudes\\:flagMacAddrInput").val();
//    console.log("FLAG.." + flag);
//    //if (flag=="N"){
//    var promiseMackAddr = myFunctionAsyncron();
//    promiseMackAddr.then(function () {
//        var mac = myMack();
//        mac.then(function () {
//        });
//    });
//}

//    var promiseMackAddr = myFunctionAsyncron();
//        promiseMackAddr.then(function () {   
//        var mac = myMack();
//        mac.then(function () {           
//        });    
//       
//     });    


function Promise() {

    var self = this;
    var thenCallback = null;

    self.then = function (callback) {
        thenCallback = callback;
    };

    self.complete = function (args) {
        if (thenCallback && typeof thenCallback === 'function') {
            thenCallback(args);
        }
    };
}


function readFileProperties() {
}

function getZPLLogoPtx(zpl) {
    var logo = [
        '^ XA',
        '~DGZ,03540,010,00001FFFFFFFFFFFFFF8',
        '0001FFFFFFFFFFFFFFF8',
        '0003FFFFFFFFFFFFFFF8',
        '001FFFFFFFFFFFFFFFF8',
        '007FFFFFFFFFFFFFFFF8',
        '007FFFFFFFFFFFFFFFF8',
        '017FFFFFFFFFFFFFFFF8',
        '03FFFFFFFFFFFFFFFFF8',
        '07FFFFFFFFFFFFFFFFF8',
        '0FFFFFFFFFFFFFFFFFF8',
        '0FFFFFFFFFFFFFFFFFF8',
        '0FFFFFFFFFFFFFFFFFF8',
        '1FFFFFFFFFFFFFFFFFF8',
        '1FFFFFFFFFFFFFFFFFF8',
        '3FFFFFFFFFFFFFFFFFF8',
        '3FFFFFFFFFFFFFFFFFF8',
        '3FFFFFFFFFFFFFFFFFF8',
        '3FFFFFFFFFFFFFFFFFF8',
        '7FFFFFFFFFFFFFFFFFF8',
        '7FFFFFFFFFFFFFFFFFF8',
        '7FFFF800000000000000',
        '7FFFE000000000000000',
        '7FFF0000000000000000',
        '7FFE0000000000000000',
        '7FFC01FFFFFFFFFFFFF8',
        '7FF80FFFFFFFFFFFFFF8',
        '7FF03FFFFFFFFFFFFFF8',
        '7FE0FFFFFFFFFFFFFFF8',
        '7FC1FFFFFFFFFFFFFFF8',
        '7F81FFFFFFFFFFFFFFF8',
        '7F83FFFFFFFFFFFFFFF8',
        '7F83FFFFFFFFFFFFFFF8',
        '7F07FFFFFFFFFFFFFFF8',
        '7F0FFFC0000000000000',
        '7F0FFE00000000000000',
        '7F0FF800000000000000',
        '7F0FF000000000000000',
        '7F0FE000000000000000',
        '7F1FE000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC000001F800FC000',
        '7F1FC000001F800FC000',
        '7F1FC000001F800FC000',
        '7F1FC000001FC01FC000',
        '7F1FC000001FE03FC000',
        '7F1FC000000FFFFF8000',
        '7F1FC0000007FFFF8000',
        '7F1FC0000007FFFF8000',
        '7F1FC0000003FFFE0000',
        '7F1FC0000000FFFC0000',
        '7F1FC00000003F700000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC030000000000000',
        '7F1FC03FC00000000000',
        '7F1FC03FFF0000000000',
        '7F1FC03FFFF000000000',
        '7F1FC03FFFFFC0000000',
        '7F1FC03FFFFFFF000000',
        '7F1FC03FFFFFFFF20000',
        '7F1FC0003FFFFFFFC000',
        '7F1FC0003FFFFFFFC000',
        '7F1FC0003F07FFFFC000',
        '7F1FC0003F0001FFC000',
        '7F1FC0003F003FFFC000',
        '7F1FC0003F7FFFFFC000',
        '7F1FC0003FFFFFFFC000',
        '7F1FC003FFFFFFFF8000',
        '7F1FC03FFFFFFFC00000',
        '7F1FC03FFFFFFE000000',
        '7F1FC03FFFFF00000000',
        '7F1FC03FFFC000000000',
        '7F1FC03FFE0000000000',
        '7F1FC03F000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000002010000000',
        '7F1FC00003FFFF000000',
        '7F1FC0003FFFFFE00000',
        '7F1FC0007FFFFFFC0000',
        '7F1FC003FFFFFFFE0000',
        '7F1FC007FFFFFFFF0000',
        '7F1FC00FFFFFFFFF8000',
        '7F1FC00FFF000FFFC000',
        '7F1FC01FF000007F8000',
        '7F1FC03FE200003FC000',
        '7F1FC03FC600001FE000',
        '7F1FC03FCE00001FE000',
        '7F1FC03FFFC0001FE000',
        '7F1FC03FFF80001FE000',
        '7F1FC03FFF00003FC000',
        '7F1FC01FFE0000BFC000',
        '7F1FC00FFE0003FFC000',
        '7F1FC00FFFFFFFFF8000',
        '7F1FC03FFFFFFFFF0000',
        '7F1FC07FFFFFFFFE0000',
        '7F1FC0FCFFFFFFE80000',
        '7F1FC03C1FFFFFE00000',
        '7F1FC03807FFFF000000',
        '7F1FC010000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC0003FFFFFFFC000',
        '7F1FC001FFFFFFFFC000',
        '7F1FC003FFFFFFFFC000',
        '7F1FC007FFFFFFFFC000',
        '7F1FC00FFFFFFFFFC000',
        '7F1FC01FFFFFFFFFC000',
        '7F1FC01FFF0000000000',
        '7F1FC03FC00000000000',
        '7F1FC03FC00000000000',
        '7F1FC03F800000000000',
        '7F1FC03F800000000000',
        '7F1FC03FC00000000000',
        '7F1FC03FC00000000000',
        '7F1FC01FF80000000000',
        '7F1FC01FFFFFFFFFC000',
        '7F1FC017FFFFFFFFC000',
        '7F1FC00FFFFFFFFFC000',
        '7F1FC007FFFFFFFFC000',
        '7F1FC001FFFFFFFFC000',
        '7F1FC0007FFFFFFFC000',
        '7F1FC000040000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC000001FC000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC0000000001FC000',
        '7F1FC0000000001FC000',
        '7F1FC0000000001FC000',
        '7F1FC0000000001FC000',
        '7F1FC0000000001FC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC0200000001FC000',
        '7F1FC0000000001FC000',
        '7F1FC0000000001FC000',
        '7F1FC0000000001FC000',
        '7F1FC0000000001FC000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC000001FC000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC020000000000000',
        '7F1FC030000000000000',
        '7F1FC03F00000001C000',
        '7F1FC03FE000000FC000',
        '7F1FC03FF40000BFC000',
        '7F1FC03FFF0003FFC000',
        '7F1FC03FFFE01FFFC000',
        '7F1FC03FFFFCFFFFC000',
        '7F1FC006FFFFFFFD8000',
        '7F1FC0007FFFFFF00000',
        '7F1FC00003FFFF000000',
        '7F1FC00003FFFF000000',
        '7F1FC0001BFFFFC00000',
        '7F1FC001FFFFFFFC0000',
        '7F1FC007FFFBFFFF8000',
        '7F1FC03FFFD86FFFC000',
        '7F1FC03FFF8007FFC000',
        '7F1FC03FFE0003FFC000',
        '7F1FC03FF000003FC000',
        '7F1FC03F80000007C000',
        '7F1FC03E00000001C000',
        '7F1FC030000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC020000000000000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC020001F800FC000',
        '7F1FC000001F800FC000',
        '7F1FC000001F800FC000',
        '7F1FC000001FC00FC000',
        '7F1FC000001FE01FC000',
        '7F1FC000000FFFFFC000',
        '7F1FC0000007FFFF8000',
        '7F1FC0000007FFFF8000',
        '7F1FC0000001FFFF0000',
        '7F1FC0000001FFFC0000',
        '7F1FC00000007FF80000',
        '7F1FC000000003000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC00000FF800FC000',
        '7F1FC000037F800FC000',
        '7F1FC00007FF800FC000',
        '7F1FC000DFFFC0078000',
        '7F1FC007FFFFD03FC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFE7FFFF8000',
        '7F1FC03FFF03FFFF8000',
        '7F1FC03FFC03FFFF0000',
        '7F1FC03FC000FFFC0000',
        '7F1FC03E00001FE00000',
        '7F1FC038000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FFFFFFFFFC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC00FE01FC000',
        '7F1FC03FC008001FC000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC00000000FE00000',
        '7F1FC00FE0007FFC0000',
        '7F1FC01FE001FFFF0000',
        '7F1FC03FC003FFFF8000',
        '7F1FC03FC00FFFFFC000',
        '7F1FC03FC01FFFFFC000',
        '7F1FC03FC01FFFFFE000',
        '7F1FC03FC07FF01FE000',
        '7F1FC03FFF7FE00FE000',
        '7F1FC01FFFFFC00FE000',
        '7F1FC017FFFE000FC000',
        '7F1FC00FFFFE001FC000',
        '7F1FC007FFFC001FC000',
        '7F1FC001FFF8003F8000',
        '7F1FC0007BC000000000',
        '7F1FC000040000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC00000000FE00000',
        '7F1FC00FE0007FFC0000',
        '7F1FC01FE000FFFF0000',
        '7F1FC03F8003FFFF8000',
        '7F1FC03FC00FFFFFC000',
        '7F1FC03FC01FFFFFC000',
        '7F1FC03FC01FFBFFC000',
        '7F1FC03F807FF81FE000',
        '7F1FC03FE8FFE00FE000',
        '7F1FC01FFFFFC00FE000',
        '7F1FC00FFFFF800FC000',
        '7F1FC00FFFFF001FC000',
        '7F1FC003FFF8001FC000',
        '7F1FC001FFF8003F8000',
        '7F1FC0017FD000000000',
        '7F1FC0001F0000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC000000000000000',
        '7F1FC034000000000000',
        '7F1FC038000000000000',
        '7F1FC03C000000000000',
        '7F1FC020000000000000',
        '7F1FC020000000000000',
        '7F1FC03C000000000000',
        '7F1FC004000000000000',
        '7F1FC038000000000000',
        '7F1FC020000000000000',
        '00000000000000000000',
        zpl
    ];
    return logo;
}

function printZPL2() {
    printLabel();
}

function printLabel() {
    var config = getUpdatedConfig();
    var zpLLabel = $("#form\\:txtZPLLabel").html();
    if (zpLLabel !== "") {

        var printDataLabel = [
            '^ XA',
            '~DGZ,03540,010,00001FFFFFFFFFFFFFF8',
            '0001FFFFFFFFFFFFFFF8',
            '0003FFFFFFFFFFFFFFF8',
            '001FFFFFFFFFFFFFFFF8',
            '007FFFFFFFFFFFFFFFF8',
            '007FFFFFFFFFFFFFFFF8',
            '017FFFFFFFFFFFFFFFF8',
            '03FFFFFFFFFFFFFFFFF8',
            '07FFFFFFFFFFFFFFFFF8',
            '0FFFFFFFFFFFFFFFFFF8',
            '0FFFFFFFFFFFFFFFFFF8',
            '0FFFFFFFFFFFFFFFFFF8',
            '1FFFFFFFFFFFFFFFFFF8',
            '1FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '7FFFFFFFFFFFFFFFFFF8',
            '7FFFFFFFFFFFFFFFFFF8',
            '7FFFF800000000000000',
            '7FFFE000000000000000',
            '7FFF0000000000000000',
            '7FFE0000000000000000',
            '7FFC01FFFFFFFFFFFFF8',
            '7FF80FFFFFFFFFFFFFF8',
            '7FF03FFFFFFFFFFFFFF8',
            '7FE0FFFFFFFFFFFFFFF8',
            '7FC1FFFFFFFFFFFFFFF8',
            '7F81FFFFFFFFFFFFFFF8',
            '7F83FFFFFFFFFFFFFFF8',
            '7F83FFFFFFFFFFFFFFF8',
            '7F07FFFFFFFFFFFFFFF8',
            '7F0FFFC0000000000000',
            '7F0FFE00000000000000',
            '7F0FF800000000000000',
            '7F0FF000000000000000',
            '7F0FE000000000000000',
            '7F1FE000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC000001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001FC01FC000',
            '7F1FC000001FE03FC000',
            '7F1FC000000FFFFF8000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000003FFFE0000',
            '7F1FC0000000FFFC0000',
            '7F1FC00000003F700000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC030000000000000',
            '7F1FC03FC00000000000',
            '7F1FC03FFF0000000000',
            '7F1FC03FFFF000000000',
            '7F1FC03FFFFFC0000000',
            '7F1FC03FFFFFFF000000',
            '7F1FC03FFFFFFFF20000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC0003F07FFFFC000',
            '7F1FC0003F0001FFC000',
            '7F1FC0003F003FFFC000',
            '7F1FC0003F7FFFFFC000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC003FFFFFFFF8000',
            '7F1FC03FFFFFFFC00000',
            '7F1FC03FFFFFFE000000',
            '7F1FC03FFFFF00000000',
            '7F1FC03FFFC000000000',
            '7F1FC03FFE0000000000',
            '7F1FC03F000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000002010000000',
            '7F1FC00003FFFF000000',
            '7F1FC0003FFFFFE00000',
            '7F1FC0007FFFFFFC0000',
            '7F1FC003FFFFFFFE0000',
            '7F1FC007FFFFFFFF0000',
            '7F1FC00FFFFFFFFF8000',
            '7F1FC00FFF000FFFC000',
            '7F1FC01FF000007F8000',
            '7F1FC03FE200003FC000',
            '7F1FC03FC600001FE000',
            '7F1FC03FCE00001FE000',
            '7F1FC03FFFC0001FE000',
            '7F1FC03FFF80001FE000',
            '7F1FC03FFF00003FC000',
            '7F1FC01FFE0000BFC000',
            '7F1FC00FFE0003FFC000',
            '7F1FC00FFFFFFFFF8000',
            '7F1FC03FFFFFFFFF0000',
            '7F1FC07FFFFFFFFE0000',
            '7F1FC0FCFFFFFFE80000',
            '7F1FC03C1FFFFFE00000',
            '7F1FC03807FFFF000000',
            '7F1FC010000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC001FFFFFFFFC000',
            '7F1FC003FFFFFFFFC000',
            '7F1FC007FFFFFFFFC000',
            '7F1FC00FFFFFFFFFC000',
            '7F1FC01FFFFFFFFFC000',
            '7F1FC01FFF0000000000',
            '7F1FC03FC00000000000',
            '7F1FC03FC00000000000',
            '7F1FC03F800000000000',
            '7F1FC03F800000000000',
            '7F1FC03FC00000000000',
            '7F1FC03FC00000000000',
            '7F1FC01FF80000000000',
            '7F1FC01FFFFFFFFFC000',
            '7F1FC017FFFFFFFFC000',
            '7F1FC00FFFFFFFFFC000',
            '7F1FC007FFFFFFFFC000',
            '7F1FC001FFFFFFFFC000',
            '7F1FC0007FFFFFFFC000',
            '7F1FC000040000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC000001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC0200000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC000001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC020000000000000',
            '7F1FC030000000000000',
            '7F1FC03F00000001C000',
            '7F1FC03FE000000FC000',
            '7F1FC03FF40000BFC000',
            '7F1FC03FFF0003FFC000',
            '7F1FC03FFFE01FFFC000',
            '7F1FC03FFFFCFFFFC000',
            '7F1FC006FFFFFFFD8000',
            '7F1FC0007FFFFFF00000',
            '7F1FC00003FFFF000000',
            '7F1FC00003FFFF000000',
            '7F1FC0001BFFFFC00000',
            '7F1FC001FFFFFFFC0000',
            '7F1FC007FFFBFFFF8000',
            '7F1FC03FFFD86FFFC000',
            '7F1FC03FFF8007FFC000',
            '7F1FC03FFE0003FFC000',
            '7F1FC03FF000003FC000',
            '7F1FC03F80000007C000',
            '7F1FC03E00000001C000',
            '7F1FC030000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC020000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC020001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001FC00FC000',
            '7F1FC000001FE01FC000',
            '7F1FC000000FFFFFC000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000001FFFF0000',
            '7F1FC0000001FFFC0000',
            '7F1FC00000007FF80000',
            '7F1FC000000003000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC00000FF800FC000',
            '7F1FC000037F800FC000',
            '7F1FC00007FF800FC000',
            '7F1FC000DFFFC0078000',
            '7F1FC007FFFFD03FC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFE7FFFF8000',
            '7F1FC03FFF03FFFF8000',
            '7F1FC03FFC03FFFF0000',
            '7F1FC03FC000FFFC0000',
            '7F1FC03E00001FE00000',
            '7F1FC038000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC008001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC00000000FE00000',
            '7F1FC00FE0007FFC0000',
            '7F1FC01FE001FFFF0000',
            '7F1FC03FC003FFFF8000',
            '7F1FC03FC00FFFFFC000',
            '7F1FC03FC01FFFFFC000',
            '7F1FC03FC01FFFFFE000',
            '7F1FC03FC07FF01FE000',
            '7F1FC03FFF7FE00FE000',
            '7F1FC01FFFFFC00FE000',
            '7F1FC017FFFE000FC000',
            '7F1FC00FFFFE001FC000',
            '7F1FC007FFFC001FC000',
            '7F1FC001FFF8003F8000',
            '7F1FC0007BC000000000',
            '7F1FC000040000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC00000000FE00000',
            '7F1FC00FE0007FFC0000',
            '7F1FC01FE000FFFF0000',
            '7F1FC03F8003FFFF8000',
            '7F1FC03FC00FFFFFC000',
            '7F1FC03FC01FFFFFC000',
            '7F1FC03FC01FFBFFC000',
            '7F1FC03F807FF81FE000',
            '7F1FC03FE8FFE00FE000',
            '7F1FC01FFFFFC00FE000',
            '7F1FC00FFFFF800FC000',
            '7F1FC00FFFFF001FC000',
            '7F1FC003FFF8001FC000',
            '7F1FC001FFF8003F8000',
            '7F1FC0017FD000000000',
            '7F1FC0001F0000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC034000000000000',
            '7F1FC038000000000000',
            '7F1FC03C000000000000',
            '7F1FC020000000000000',
            '7F1FC020000000000000',
            '7F1FC03C000000000000',
            '7F1FC004000000000000',
            '7F1FC038000000000000',
            '7F1FC020000000000000',
            '00000000000000000000',
            zpLLabel
        ];
        qz.print(config, printDataLabel).catch(displayError);
        goToPrintGuia();
    }

}

function goToPrintGuia() {
    goToPrint(namePrinterGuia, 'S');
}

function printGuia() {
    var config = getUpdatedConfig();
    var zpLGuia = $("#form\\:txtZPLGuia").html();
    if (zpLGuia !== "") {

        var printDataLabel = [
            '^ XA',
            '~DGZ,03540,010,00001FFFFFFFFFFFFFF8',
            '0001FFFFFFFFFFFFFFF8',
            '0003FFFFFFFFFFFFFFF8',
            '001FFFFFFFFFFFFFFFF8',
            '007FFFFFFFFFFFFFFFF8',
            '007FFFFFFFFFFFFFFFF8',
            '017FFFFFFFFFFFFFFFF8',
            '03FFFFFFFFFFFFFFFFF8',
            '07FFFFFFFFFFFFFFFFF8',
            '0FFFFFFFFFFFFFFFFFF8',
            '0FFFFFFFFFFFFFFFFFF8',
            '0FFFFFFFFFFFFFFFFFF8',
            '1FFFFFFFFFFFFFFFFFF8',
            '1FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '3FFFFFFFFFFFFFFFFFF8',
            '7FFFFFFFFFFFFFFFFFF8',
            '7FFFFFFFFFFFFFFFFFF8',
            '7FFFF800000000000000',
            '7FFFE000000000000000',
            '7FFF0000000000000000',
            '7FFE0000000000000000',
            '7FFC01FFFFFFFFFFFFF8',
            '7FF80FFFFFFFFFFFFFF8',
            '7FF03FFFFFFFFFFFFFF8',
            '7FE0FFFFFFFFFFFFFFF8',
            '7FC1FFFFFFFFFFFFFFF8',
            '7F81FFFFFFFFFFFFFFF8',
            '7F83FFFFFFFFFFFFFFF8',
            '7F83FFFFFFFFFFFFFFF8',
            '7F07FFFFFFFFFFFFFFF8',
            '7F0FFFC0000000000000',
            '7F0FFE00000000000000',
            '7F0FF800000000000000',
            '7F0FF000000000000000',
            '7F0FE000000000000000',
            '7F1FE000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC000001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001FC01FC000',
            '7F1FC000001FE03FC000',
            '7F1FC000000FFFFF8000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000003FFFE0000',
            '7F1FC0000000FFFC0000',
            '7F1FC00000003F700000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC030000000000000',
            '7F1FC03FC00000000000',
            '7F1FC03FFF0000000000',
            '7F1FC03FFFF000000000',
            '7F1FC03FFFFFC0000000',
            '7F1FC03FFFFFFF000000',
            '7F1FC03FFFFFFFF20000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC0003F07FFFFC000',
            '7F1FC0003F0001FFC000',
            '7F1FC0003F003FFFC000',
            '7F1FC0003F7FFFFFC000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC003FFFFFFFF8000',
            '7F1FC03FFFFFFFC00000',
            '7F1FC03FFFFFFE000000',
            '7F1FC03FFFFF00000000',
            '7F1FC03FFFC000000000',
            '7F1FC03FFE0000000000',
            '7F1FC03F000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000002010000000',
            '7F1FC00003FFFF000000',
            '7F1FC0003FFFFFE00000',
            '7F1FC0007FFFFFFC0000',
            '7F1FC003FFFFFFFE0000',
            '7F1FC007FFFFFFFF0000',
            '7F1FC00FFFFFFFFF8000',
            '7F1FC00FFF000FFFC000',
            '7F1FC01FF000007F8000',
            '7F1FC03FE200003FC000',
            '7F1FC03FC600001FE000',
            '7F1FC03FCE00001FE000',
            '7F1FC03FFFC0001FE000',
            '7F1FC03FFF80001FE000',
            '7F1FC03FFF00003FC000',
            '7F1FC01FFE0000BFC000',
            '7F1FC00FFE0003FFC000',
            '7F1FC00FFFFFFFFF8000',
            '7F1FC03FFFFFFFFF0000',
            '7F1FC07FFFFFFFFE0000',
            '7F1FC0FCFFFFFFE80000',
            '7F1FC03C1FFFFFE00000',
            '7F1FC03807FFFF000000',
            '7F1FC010000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC0003FFFFFFFC000',
            '7F1FC001FFFFFFFFC000',
            '7F1FC003FFFFFFFFC000',
            '7F1FC007FFFFFFFFC000',
            '7F1FC00FFFFFFFFFC000',
            '7F1FC01FFFFFFFFFC000',
            '7F1FC01FFF0000000000',
            '7F1FC03FC00000000000',
            '7F1FC03FC00000000000',
            '7F1FC03F800000000000',
            '7F1FC03F800000000000',
            '7F1FC03FC00000000000',
            '7F1FC03FC00000000000',
            '7F1FC01FF80000000000',
            '7F1FC01FFFFFFFFFC000',
            '7F1FC017FFFFFFFFC000',
            '7F1FC00FFFFFFFFFC000',
            '7F1FC007FFFFFFFFC000',
            '7F1FC001FFFFFFFFC000',
            '7F1FC0007FFFFFFFC000',
            '7F1FC000040000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC000001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC0200000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC0000000001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC000001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC020000000000000',
            '7F1FC030000000000000',
            '7F1FC03F00000001C000',
            '7F1FC03FE000000FC000',
            '7F1FC03FF40000BFC000',
            '7F1FC03FFF0003FFC000',
            '7F1FC03FFFE01FFFC000',
            '7F1FC03FFFFCFFFFC000',
            '7F1FC006FFFFFFFD8000',
            '7F1FC0007FFFFFF00000',
            '7F1FC00003FFFF000000',
            '7F1FC00003FFFF000000',
            '7F1FC0001BFFFFC00000',
            '7F1FC001FFFFFFFC0000',
            '7F1FC007FFFBFFFF8000',
            '7F1FC03FFFD86FFFC000',
            '7F1FC03FFF8007FFC000',
            '7F1FC03FFE0003FFC000',
            '7F1FC03FF000003FC000',
            '7F1FC03F80000007C000',
            '7F1FC03E00000001C000',
            '7F1FC030000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC020000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC020001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001F800FC000',
            '7F1FC000001FC00FC000',
            '7F1FC000001FE01FC000',
            '7F1FC000000FFFFFC000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000007FFFF8000',
            '7F1FC0000001FFFF0000',
            '7F1FC0000001FFFC0000',
            '7F1FC00000007FF80000',
            '7F1FC000000003000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC00000FF800FC000',
            '7F1FC000037F800FC000',
            '7F1FC00007FF800FC000',
            '7F1FC000DFFFC0078000',
            '7F1FC007FFFFD03FC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFE7FFFF8000',
            '7F1FC03FFF03FFFF8000',
            '7F1FC03FFC03FFFF0000',
            '7F1FC03FC000FFFC0000',
            '7F1FC03E00001FE00000',
            '7F1FC038000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FFFFFFFFFC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC00FE01FC000',
            '7F1FC03FC008001FC000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC00000000FE00000',
            '7F1FC00FE0007FFC0000',
            '7F1FC01FE001FFFF0000',
            '7F1FC03FC003FFFF8000',
            '7F1FC03FC00FFFFFC000',
            '7F1FC03FC01FFFFFC000',
            '7F1FC03FC01FFFFFE000',
            '7F1FC03FC07FF01FE000',
            '7F1FC03FFF7FE00FE000',
            '7F1FC01FFFFFC00FE000',
            '7F1FC017FFFE000FC000',
            '7F1FC00FFFFE001FC000',
            '7F1FC007FFFC001FC000',
            '7F1FC001FFF8003F8000',
            '7F1FC0007BC000000000',
            '7F1FC000040000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC00000000FE00000',
            '7F1FC00FE0007FFC0000',
            '7F1FC01FE000FFFF0000',
            '7F1FC03F8003FFFF8000',
            '7F1FC03FC00FFFFFC000',
            '7F1FC03FC01FFFFFC000',
            '7F1FC03FC01FFBFFC000',
            '7F1FC03F807FF81FE000',
            '7F1FC03FE8FFE00FE000',
            '7F1FC01FFFFFC00FE000',
            '7F1FC00FFFFF800FC000',
            '7F1FC00FFFFF001FC000',
            '7F1FC003FFF8001FC000',
            '7F1FC001FFF8003F8000',
            '7F1FC0017FD000000000',
            '7F1FC0001F0000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC000000000000000',
            '7F1FC034000000000000',
            '7F1FC038000000000000',
            '7F1FC03C000000000000',
            '7F1FC020000000000000',
            '7F1FC020000000000000',
            '7F1FC03C000000000000',
            '7F1FC004000000000000',
            '7F1FC038000000000000',
            '7F1FC020000000000000',
            '00000000000000000000',
            zpLGuia
        ];
        qz.print(config, printDataLabel).catch(displayError);
    }
}

function printTest() {
    startConnection();
    var printData = [
        '^XA\n',
        '^FO50,50^ADN,36,20^FDPRINTED USING QZ PRINT PLUGIN \n',
        {type: 'raw', format: 'image', data: '/glp/resources/images/etiqueta/logo.png', options: {language: 'ZPLII'}},
        '^FS\n',
        '^XZ\n'
    ];
    setTimeout(function () {
        var cf = getUpdatedConfig();
        cf.setPrinter("ptxEtiquetaGLP");
        qz.print(getUpdatedConfig(), printData).catch(displayError);
    }, 2000);


}

function printZPL() {
    var config = getUpdatedConfig();

    var printData = [
        '^XA\n',
        '^FO50,50^ADN,36,20^FDPRINTED USING QZ PRINT PLUGIN \n',
        {type: 'raw', format: 'image', data: '/glp/resources/images/etiqueta/logo.png', options: {language: 'ZPLII'}},
        '^FS\n',
        '^XZ\n'
    ];

    qz.print(config, printData).catch(displayError);
}

//{ type: 'raw', format: 'image', data: 'assets/img/image_sample_bw.png', options: { language: 'ZPLII' } },
function printESCP() {
    var config = getUpdatedConfig();

    var printData = [
        {type: 'raw', format: 'image', data: 'assets/img/image_sample_bw.png', options: {language: 'ESCP', dotDensity: 'single'}},
        {type: 'raw', data: '\nPrinted using qz-print plugin.\n\n\n\n\n\n'}
    ];

    qz.print(config, printData).catch(displayError);
}

function printEPCL() {
    var config = getUpdatedConfig();

    var printData = [];
    $.merge(printData, convertEPCL('+RIB 4'));     // Monochrome ribbon
    $.merge(printData, convertEPCL('F'));          // Clear monochrome print buffer
    $.merge(printData, convertEPCL('+C 8'));       // Adjust monochrome intensity
    $.merge(printData, convertEPCL('&R'));         // Reset magnetic encoder
    $.merge(printData, convertEPCL('&CDEW 0 0'));  // Set R/W encoder to ISO default
    $.merge(printData, convertEPCL('&CDER 0 0'));  // Set R/W encoder to ISO default
    $.merge(printData, convertEPCL('&SVM 0'));     // Disable magnetic encoding verifications
    $.merge(printData, convertEPCL('T 80 600 0 1 0 45 1 QZ INDUSTRIES'));   // Write text buffer
    $.merge(printData, convertEPCL('&B 1 123456^INDUSTRIES/QZ^789012'));    // Write mag strip buffer
    $.merge(printData, convertEPCL('&E*'));        // Encode magnetic data
    $.merge(printData, convertEPCL('I 10'));       // Print card (10 returns to print ready pos.)
    $.merge(printData, convertEPCL('MO'));         // Move card to output hopper

    qz.print(config, printData).catch(displayError);
}

/**
 * EPCL helper function that appends a single line of EPCL data, taking into account
 * special EPCL NUL characters, data length, escape character and carriage return
 */
function convertEPCL(data) {
    if (data == null || data.length == 0) {
        console.warn('Empty EPCL data, skipping');
    }

    // Data length for this command, in 2 character Hex (base 16) format
    var len = (data.length + 2).toString(16);
    if (len.length < 2) {
        len = '0' + len;
    }

    return [
        {type: 'raw', format: 'hex', data: 'x00x00x00'}, // Append 3 NULs
        {type: 'raw', format: 'hex', data: 'x' + len}, // Append our command length, in base16
        {type: 'raw', format: 'plain', data: data}, // Append our command
        {type: 'raw', format: 'plain', data: '\r'}        // Append carriage return
    ];
}

function printBase64() {
    var config = getUpdatedConfig();

    // Send base64 encoded characters/raw commands to qz using data type 'base64'.
    // This will automatically convert provided base64 encoded text into text/ascii/bytes, etc.
    // This example is for EPL and contains an embedded image.
    // Please adapt to your printer language.

    //noinspection SpellCheckingInspection
    var printData = [
        {
            type: 'raw',
            format: 'base64',
            data: 'Ck4KcTYwOQpRMjAzLDI2CkI1LDI2LDAsMUEsMyw3LDE1MixCLCIxMjM0IgpBMzEwLDI2LDAsMywx' +
                    'LDEsTiwiU0tVIDAwMDAwIE1GRyAwMDAwIgpBMzEwLDU2LDAsMywxLDEsTiwiUVogUFJJTlQgQVBQ' +
                    'TEVUIgpBMzEwLDg2LDAsMywxLDEsTiwiVEVTVCBQUklOVCBTVUNDRVNTRlVMIgpBMzEwLDExNiww' +
                    'LDMsMSwxLE4sIkZST00gU0FNUExFLkhUTUwiCkEzMTAsMTQ2LDAsMywxLDEsTiwiUVpJTkRVU1RS' +
                    'SUVTLkNPTSIKR1cxNTAsMzAwLDMyLDEyOCz/////////6SSSX///////////////////////////' +
                    '//////////6UlUqX////////////////////////////////////8kqkpKP/////////////////' +
                    '//////////////////6JUpJSVf//////////////////////////////////9KpKVVU+////////' +
                    '//////////////////////////8KSSlJJf5/////////////////////////////////9KUqpVU/' +
                    '/7////////////////////////////////9KqUkokf//P///////////////////////////////' +
                    '+VKUqpZP//+P///////////////////////////////ElKUlSf///9f/////////////////////' +
                    '////////+ipSkqin////y/////////////////////////////+lVUpUlX/////r////////////' +
                    '/////////////////qlJKUql/////+n////////////////////////////BFKVKUl//////8v//' +
                    '/////////////////////////zVSlKUp///////0f//////////////////////////wiSlSUpf/' +
                    '//////q///////////////////////////KqlJUpV///////+R//////////////////////////' +
                    '4UlKSpSX///////9T/////////6L///////////////BKlKpSqP///////1X////////0qg/23/V' +
                    'VVVVVVf//8CSlJKklf///////kv///////+pS0/JP8AAAAAAB///wFSlSSpV///////+pf//////' +
                    '/pUoq+qfwAAAAAAH//+AClSqpUT///////9S///////8pJUlkr+AAAAAAA///4AFJSSSUv//////' +
                    '/yl///////KVUpTUv8AAAAAAH///gBKSqlVU////////lX//////6UkqoiU/wAAAAAA///+ABKpJ' +
                    'Uko////////JH//////UpIiqlJ/AAAAAAD///wACkSUpJX///////6q//////6pVVSqiv4AAAAAA' +
                    'f///AAJVVIqpP///////pI//////pSVtSSq/wAAAAAD///8AAJSlVJVf///////Sp/////8Sq//U' +
                    'qL/ttttoAP///wAAUpVSpJ///////+pT/////qkn//UlH/////AB////AABKUSpSX///////5Sn/' +
                    '///+lJ//+pS/////4AP///8AABKUkpVP///////ylP////1Kv//+qr/////AA////4AAKVVJUl//' +
                    '/////+lKf////KS///8kv////8AH////gAAKSSpJR///////9Kq////9Kv///5Kf////gAf///+A' +
                    'AAUlUqov///////1JT////lS////qn////8AD////4AABKpKSqf///////Skj///+kr////JH///' +
                    '/wAf////wAACkqUlK///////8pKv///ypf///9V////+AD/////AAAFKUVSj///////wqlP///JT' +
                    '////yR////wAP////8AAAFKqkpv///////JSlf//9Sv////U/////AB/////4AAAVIpKRf//////' +
                    '+ElV///pS////8of///4AP/////gAAASZVKr///////4qkj///Sn////0v////AA//////AAABUS' +
                    'VJH///////glJn//8pP////KH///8AH/////+AAACtUlVf//////+ClRP//qV////9K////gA///' +
                    '///4AAACEpJK///////8BSqf/+lX////yr///8AD//////wAAAVUqVH///////gUlU//5Rf////R' +
                    'P///gAf//////gAAApKqTP//////8AVSV//pU////6qf//+AD//////+AAAAqkki//////8AEpVL' +
                    '/+qP////1L///wAP//////4AAACSVVB/////+AFUpKX/9KP////Sv//+AB///////AAAAEqSgH//' +
                    '//+ACkpSUv/lV////6k///4AP//////+AAAAUlSgf////gAJKRUpf/ST////1J///AA///////4A' +
                    'AAAVJVB////gAtVFUpV/8lX///+Vf//4AH///////gAAABKSSD///wASSVVJSR/1Vf///8kf//gA' +
                    '///////+AAAABVUof//4AElUpKqqv/SL////1L//8AD///////4AAAABJJQ//8AFVJKVKSSP+qj/' +
                    '///Kv//gAf///////gAAAAKSpT/+ACkqSlKUkqf5Rf///6S//+AD///////+AAAAAKqpP/ABJKVS' +
                    'klKqU/xUf///qp//wAP///////4AAAAAkko+gASVKUlVKlKX/VK///9Sf/+AB////////gAAAACp' +
                    'UrgAKqVKVJKSlKf+Sl///0kf/4AP///////+AAAAABSVIAFJUlKqSUpKV/0pX//8qr//AA//////' +
                    '//8AAAAACklACSopKSVUqVKX/qpH//okv/4AH////////gAAAAAVVKBUpUqUkkpKSk//SSv/xVK/' +
                    '/AAAAAAD////AAAAAAJKWSUpVKVVUqVSp/+qqH9SlR/8AAAAAAH///4AAAAABSUklJSSlJJKUkpf' +
                    '/8klQFSo//gAAAAAA////wAAAAABVKqlUkqlSqkqqU//6pUqkkof8AAAAAAB/r//AAAAAAElEpSK' +
                    'qSlSSpJKL//pUqpVKr/wAAAAAAP8v/8AAAAAAJLKUqkkpSqkqSVf//yUkpKSv+AAAAAAAfqf/wAA' +
                    'AAAAVClKVVUoklUqqp///UpKVVS/wAAAAAAD+S//AAAAAAAlpSkkkpVKkpKSX///JVKTpR+AAAAA' +
                    'AAH9X/8AAAAAABRUpVJUqqSpSUlf///SSk/Sv4AAAAAAA/y//wAAAAAAFSVUlSUkUkpUqr////VS' +
                    'v9S/AAAAAAAB/3//AAAAAAAFUkpSlJMqqUpJP////13/pT////////////8AAAAAAAEpJSlSqUkk' +
                    'pVS////////Un////////////wAAAAAABJVSlSpUqpUpJX///////8q/////////////gAAAAAAC' +
                    'pSqkkpKSUpSSP///////5L////////////+AAAAAAACSkVVKSklKpVV///////+SX///////////' +
                    '/4AAAAAAAFSqJKlSqqiVSX///////9U/////////////gAAAAAAASpVSlSkklVJU////////yr//' +
                    '//////////+AAAAAAAAkpJSklKpKSUp////////kn////////////4AAAAAAABJSqlKqkqUqVf//' +
                    '/////5K/////////////gAAAAAAACpUlKpJKUqlI////////1L////////////+AAAAAAAAFSVKS' +
                    'SqkpFKX////////SX////////////4AAAAAAAAiklKlSSpTKKv///////9U/////////////wAAA' +
                    'AAAABSpSlSqlSiVJ////////pV/////////////AAAAAAAAVUpSkklSlUqX////////Uv///////' +
                    '/////8AAAAAAAAkqUpVJJSqpVf///////8pf////////////4AAAAAAAFJKUpKqUpJUT////////' +
                    '4r/////////////wAAAAAAAKqVKVKUqSSVX///////+Uv/////////////gAAAAAAASUlKSkpKql' +
                    'S////////+qf/////////////AAAAAAAEkpKUlUpJJCn////////iH///////////wAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH/4B+A8AH/AAAAA' +
                    'AAAAAAAAAAAAAA//AAfwD4H4HwAAf/4H4DwB//gAAAAAAAAAAAAAAAAAD/+AB/APgfgfAAB//wfw' +
                    'PAf/+AAAAAAAAAAAAAgAAAAP/8AH8AfB+D4AAH//B/g8D//4AAAAAAAAAAAADwAAAA//4A/4B8H4' +
                    'PgAAfB+H+DwP4HgAAAAAAAAAAAAPwAAAD4fgD/gHw/w+AAB8D4f8PB+AGAAAAAAAAAAAAA/wAAAP' +
                    'g+Af/AfD/D4AAHwPh/48HwAAAAAAAAAAAAAAB/4AAA+D4B98A+P8PAAAfA+Hvjw+AAAAAAAAAAAA' +
                    'AAAB/4AAD4PgH3wD4/x8AAB8H4e/PD4AAAAAAAAAAAAAAAB/8AAPh8A+PgPn/nwAAH//B5+8Pg/4' +
                    'AH/j/x/4/8f+AA/8AA//wD4+A+eefAAAf/4Hj7w+D/gAf+P/H/j/x/4AA/wAD/+APj4B5554AAB/' +
                    '/AeP/D4P+AB/4/8f+P/H/gAD/AAP/wB8HwH3nvgAAH/wB4f8Pw/4AH/j/x/4/8f+AA/8AA//AH//' +
                    'Af+f+AAAfAAHg/wfAPgAAAAAAAAAAAAAf/AAD5+A//+B/w/4AAB8AAeD/B+A+AAAAAAAAAAAAAH/' +
                    'gAAPj8D//4D/D/AAAHwAB4H8H+D4AAAAAAAAAAAAB/4AAA+H4P//gP8P8AAAfAAHgPwP//gAAAAA' +
                    'AAAAAAAP8AAAD4fh+A/A/w/wAAB8AAeA/Af/+AAAAAAAAAAAAA/AAAAPg/HwB8B+B+AAAHwAB4B8' +
                    'Af/4AAAAAAAAAAAADwAAAA+B+fAHwH4H4AAAfAAHgHwAf4AAAAAAAAAAAAAIAAAAD4H/8Afgfgfg' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' +
                    'AAAAAAAAAAAAAAAAAAAAAAAAClAxLDEK'
        }
    ];

    qz.print(config, printData).catch(displayError);
}

function printXML() {
    var config = getUpdatedConfig();

    var printData = [
        {type: 'raw', format: 'xml', data: 'assets/zpl_sample.xml', options: {xmlTag: 'v7:Image'}}
    ];

    qz.print(config, printData).catch(displayError);
}

function printHex() {
    var config = getUpdatedConfig();

    var printData = [
        {type: 'raw', format: 'hex', data: '4e0d0a713630390d0a513230332c32360d0a42352c32362c'},
        {type: 'raw', format: 'hex', data: '302c31412c332c372c3135322c422c2231323334220d0a41'},
        {type: 'raw', format: 'hex', data: '3331302c32362c302c332c312c312c4e2c22534b55203030'},
        {type: 'raw', format: 'hex', data: '303030204d46472030303030220d0a413331302c35362c30'},
        {type: 'raw', format: 'hex', data: '2c332c312c312c4e2c22515a205072696e7420506c756769'},
        {type: 'raw', format: 'hex', data: '6e220d0a413331302c38362c302c332c312c312c4e2c2254'},
        {type: 'raw', format: 'hex', data: '657374207072696e74207375636365737366756c220d0a41'},
        {type: 'raw', format: 'hex', data: '3331302c3131362c302c332c312c312c4e2c2266726f6d20'},
        {type: 'raw', format: 'hex', data: '73616d706c652e68746d6c220d0a413331302c3134362c30'},
        {type: 'raw', format: 'hex', data: '2c332c312c312c4e2c227072696e7448657828292066756e'},
        {type: 'raw', format: 'hex', data: '6374696f6e2e220d0a50312c310d0a'}
    ];

    qz.print(config, printData).catch(displayError);
}

function printFile(file) {
    var config = getUpdatedConfig();

    var printData = [
        {type: 'raw', format: 'file', data: 'C:/Oracle/Middleware/user_projects/domains/base_domain/paquetexpressConfig/etiqueta/' + file}
    ];

    qz.print(config, printData).catch(displayError);
}


/// Pixel Printers ///
function printHTML() {
    var config = getUpdatedConfig();

    var colA = '<h2>*&nbsp; QZ Print Plugin HTML Printing &nbsp;*</h2>' +
            '<span style="color: #F00;">Version:</span> ' + qzVersion + '<br/>' +
            '<span style="color: #F00;">Visit:</span> https://qz.io/';
    var colB = '<img src="' + getPath() + '/assets/img/image_sample.png">';

    var printData = [
        {
            type: 'html',
            format: 'plain',
            data: '<html>' +
                    '   <table style="font-family: monospace; border: 1px;">' +
                    '       <tr style="height: 6cm;">' +
                    '           <td valign="top">' + colA + '</td>' +
                    '           <td valign="top">' + colB + '</td>' +
                    '       </tr>' +
                    '   </table>' +
                    '</html>'
        }
    ];

    qz.print(config, printData).catch(displayError);
}

function printPDF() {
    var config = getUpdatedConfig();

    var printData = [
        {type: 'pdf', data: 'assets/pdf_sample.pdf'}
    ];

    qz.print(config, printData).catch(displayError);
}

function printImage() {
    var config = getUpdatedConfig();

    var printData = [
        {type: 'image', data: 'assets/img/image_sample.png'}
    ];

    qz.print(config, printData).catch(displayError);
}


/// Serial ///
function listSerialPorts() {
    qz.serial.findPorts().then(function (data) {
        var list = '';
        for (var i = 0; i < data.length; i++) {
            list += "&nbsp; " + data[i] + "<br/>";
        }

        displayMessage("<strong>Available serial ports:</strong><br/>" + list);
    }).catch(displayError);
}

function openSerialPort() {
    var widthVal = $("#serialWidth").val();
    if (!widthVal) {
        widthVal = null;
    }

    var bounds = {
        begin: $("#serialBegin").val(),
        end: $("#serialEnd").val(),
        width: widthVal
    };

    qz.serial.openPort($("#serialPort").val(), bounds).then(function () {
        displayMessage("Serial port opened");
    }).catch(displayError);
}

function sendSerialData() {
    var properties = {
        baudRate: $("#serialBaud").val(),
        dataBits: $("#serialData").val(),
        stopBits: $("#serialStop").val(),
        parity: $("#serialParity").val(),
        flowControl: $("#serialFlow").val()
    };

    qz.serial.sendData($("#serialPort").val(), $("#serialCmd").val(), properties).catch(displayError);
}

function closeSerialPort() {
    qz.serial.closePort($("#serialPort").val()).then(function () {
        displayMessage("Serial port closed");
    }).catch(displayError);
}


/// USB ///
function listUsbDevices() {
    qz.usb.listDevices(true).then(function (data) {
        var list = '';
        for (var i = 0; i < data.length; i++) {
            var device = data[i];
            if (device.hub) {
                list += "USB Hub";
            }

            list += "<p>" +
                    "   VendorID: <code>0x" + device.vendorId + "</code>" +
                    usbButton(["usbVendor", "usbProduct"], [device.vendorId, device.productId]) + "<br/>" +
                    "   ProductID: <code>0x" + device.productId + "</code><br/>";

            if (device.manufacturer) {
                list += "   Manufacturer: <code>" + device.manufacturer + "</code><br/>";
            }
            if (device.product) {
                list += "   Product: <code>" + device.product + "</code><br/>";
            }

            list += "</p><hr/>";
        }

        pinMessage("<strong>Available usb devices:</strong><br/>" + list);
    }).catch(displayError);
}

function listUsbDeviceInterfaces() {
    qz.usb.listInterfaces($("#usbVendor").val(), $("#usbProduct").val()).then(function (data) {
        var list = '';
        for (var i = 0; i < data.length; i++) {
            list += "&nbsp; <code>0x" + data[i] + "</code>" + usbButton(["usbInterface"], [data[i]]) + "<br/>";
        }

        displayMessage("<strong>Available device interfaces:</strong><br/>" + list);
    }).catch(displayError);
}

function listUsbInterfaceEndpoints() {
    qz.usb.listEndpoints($("#usbVendor").val(), $("#usbProduct").val(), $("#usbInterface").val()).then(function (data) {
        var list = '';
        for (var i = 0; i < data.length; i++) {
            list += "&nbsp; <code>0x" + data[i] + "</code>" + usbButton(["usbEndpoint"], [data[i]]) + "<br/>";
        }

        displayMessage("<strong>Available interface endpoints:</strong><br/>" + list);
    }).catch(displayError);
}

function claimUsbDevice() {
    qz.usb.claimDevice($("#usbVendor").val(), $("#usbProduct").val(), $("#usbInterface").val()).then(function () {
        displayMessage("USB Device claimed");
    }).catch(displayError);
}

function sendUsbData() {
    qz.usb.sendData($("#usbVendor").val(), $("#usbProduct").val(), $("#usbEndpoint").val(), $("usbData").val()).catch(displayError);
}

function readUsbData() {
    qz.usb.readData($("#usbVendor").val(), $("#usbProduct").val(), $("#usbEndpoint").val(), $("#usbResponse").val()).then(function (data) {
        displayMessage("<strong>Raw response:</strong> " + data + "<br/>");
    }).catch(displayError);
}

function openUsbStream() {
    qz.usb.openStream($("#usbVendor").val(), $("#usbProduct").val(), $("#usbEndpoint").val(), $("#usbResponse").val(), $("#usbStream").val()).then(function () {
        pinMessage("Waiting on device", '' + $("#usbVendor").val().replace(/^0x/, '').toLowerCase() + $("#usbProduct").val().replace(/^0x/, '').toLowerCase());
    }).catch(displayError);
}

function closeUsbStream() {
    qz.usb.closeStream($("#usbVendor").val(), $("#usbProduct").val(), $("#usbEndpoint").val()).then(function () {
        $('#' + $("#usbVendor").val() + $("#usbProduct").val()).attr('id', '').html("Stream closed");
    }).catch(displayError);
}

function toggleScale() {
    window.readingWeight = !readingWeight;

    if (readingWeight) {
        $("#scaleToggle").html("Read as Raw");
    } else {
        $("#scaleToggle").html("Read as Weight");
    }
}

function releaseUsbDevice() {
    qz.usb.releaseDevice($("#usbVendor").val(), $("#usbProduct").val()).then(function () {
        displayMessage("USB Device released");
    }).catch(displayError);
}


/// Resets ///
function resetRawOptions() {
    $("#rawPerSpool").val(1);
    $("#rawEncoding").val(null);
    $("#rawEndOfDoc").val(null);
    $("#rawAltPrinting").prop('checked', false);
    $("#rawCopies").val(1);
}

function resetPixelOptions() {
    $("#pxlColorType").val("color");
    $("#pxlCopies").val(1);
    $("#pxlDensity").val('');
    $("#pxlDuplex").prop('checked', false);
    $("#pxlInterpolation").val("");
    $("#pxlOrientation").val("");
    $("#pxlPaperThickness").val(null);
    $("#pxlPrinterTray").val(null);
    $("#pxlRotation").val(0);
    $("#pxlScale").prop('checked', true);
    $("#pxlUnitsIN").prop('checked', true);

    $("#pxlMargins").val(0).css('display', '');
    $("#pxlMarginsTop").val(0);
    $("#pxlMarginsRight").val(0);
    $("#pxlMarginsBottom").val(0);
    $("#pxlMarginsLeft").val(0);
    $("#pxlMarginsActive").prop('checked', false);
    $("#pxlMarginsGroup").css('display', 'none');

    $("#pxlSizeWidth").val('');
    $("#pxlSizeHeight").val('');
    $("#pxlSizeActive").prop('checked', false);
    $("#pxlSizeGroup").css('display', 'none');
}

function checkSizeActive() {
    if ($("#pxlSizeActive").prop('checked')) {
        $("#pxlSizeGroup").css('display', '');
    } else {
        $("#pxlSizeGroup").css('display', 'none');
    }
}

function checkMarginsActive() {
    if ($("#pxlMarginsActive").prop('checked')) {
        $("#pxlMarginsGroup").css('display', '');
        $("#pxlMargins").css('display', 'none');
    } else {
        $("#pxlMarginsGroup").css('display', 'none');
        $("#pxlMargins").css('display', '');
    }
}

function resetSerialOptions() {
    $("#serialPort").val('');
    $("#serialCmd").val('');
    $("#serialBegin").val("0x0002"); //String.fromCharCode(2)
    $("#serialEnd").val("0x000D"); //String.fromCharCode(13)

    $("#serialBaud").val(9600);
    $("#serialData").val(8);
    $("#serialStop").val(1);
    $("#serialParity").val('NONE');
    $("#serialFlow").val('NONE');

    // M/T PS60 - 9600, 7, 1, EVEN, NONE
}

function resetUsbOptions() {
    $("#usbVendor").val('');
    $("#usbProduct").val('');

    $("#usbInterface").val('');
    $("#usbEndpoint").val('');
    $("#usbData").val('');
    $("#usbResponse").val(8);
    $("#usbStream").val(10);

    // M/T PS60 - V:0x0EB8 P:0xF000, I:0x0 E:0x81 (precision of 2)
    // Dymo S100 - V:0x0922 P:0x8009, I:0x0 E:0x82 (precision of 1)
}


/// Page load ///
$(document).ready(function () {
    window.readingWeight = false;

    resetRawOptions();
    resetPixelOptions();
    resetSerialOptions();
    resetUsbOptions();

    $("#printerSearch").on('keyup', function (e) {
        if (e.which == 13 || e.keyCode == 13) {
            findPrinter($('#printerSearch').val(), true);
            return false;
        }
    });

});

qz.websocket.setClosedCallbacks(function (evt) {
    updateState('Inactive', 'default');
    console.log(evt);

    if (evt.reason) {
        displayMessage("<strong>Connection closed:</strong> " + evt.reason, 'alert-warning');
    }
});

qz.websocket.setErrorCallbacks(handleConnectionError);

qz.serial.setSerialCallbacks(function (port, output) {
    console.log('Serial', port, 'received output', output);
    displayMessage("Received output from serial port [" + port + "]: <em>" + output + "</em>");
});

qz.usb.setUsbCallbacks(function (keys, data) {
    var pin = $('#' + keys[0] + keys[1]);

    if (data.error == undefined) {
        if (window.readingWeight) {
            pin.html("<strong>Weight:</strong> " + readScaleData(data, 2));
        } else {
            pin.html("<strong>Raw data:</strong> " + data);
        }
    } else {
        console.log(data.error);
        pin.html("<strong>Error:</strong> " + data.error);
    }
});


var qzVersion = 0;
function findVersion() {
    qz.api.getVersion().then(function (data) {
        $("#qz-version").html(data);
        qzVersion = data;
    }).catch(displayError);
}

$("#askFileModal").on("shown.bs.modal", function () {
    $("#askFile").focus().select();
});
$("#askHostModal").on("shown.bs.modal", function () {
    $("#askHost").focus().select();
});


/// Helpers ///
function handleConnectionError(err) {
    updateState('Error', 'danger');
    if (err.target != undefined) {
        if (err.target.readyState >= 2) { //if CLOSING or CLOSED
            displayError("Connection to QZ Tray was closed");
            mensajes("El programa QZ Tray esta cerrado, inicielo para continuar");
            hideDialog(true);
        } else {
        	mensajes("Error al iniciar programa QZ Tray");
            displayError("A connection error occurred, check log for details");
            console.error(err);
            hideDialog(true);
        }
    } else {
    	
    	mensajes("El programa QZ Tray esta cerrado");
    	hideDialog(true);
//        displayError(err);
    }
}

function setmensajeImpresionStatusError(txt) {
    alert(txt);
}

function activaPrinter() {
    var lbl = $("#form\\:outPutImp").val();
    if (lbl === "Checked") {
        startConnection();
    }
}
function mostrarMensaje(err) {
    $("#msjeImpresion").html(err); 
}

function displayError(err) {
    console.error(err);
    var error = err;
    var search1 = "_qz.websocket.connection.sendData is not a function";
    var search2 = "Unable to establish connection with QZ";
    var search3 = "El objeto no acepta la propiedad o el m";
    if ((error.message.indexOf(search1) > -1) || (error.message.indexOf(search2) > -1) || (error.message.indexOf(search3) > -1)) {
        $("#msjeErrorImpresion").html("Error: verifique el estatus del programa de impresi�n <strong>QZ Tray</strong>. <br>\n\
                    Para encenderlo vaya a esta ruta en el explorador de archivos C:\\Program Files\\QZ Tray\\qz-tray.exe <br>\n\
                    Si este software no esta instalado en su equipo o no lo encuentra en la ruta favor de contactarse con Sistemas. \n\
                    ");
    } else {
        $("#msjeErrorImpresion").html(err);
    }
}

//function displayMessage(msg, css) {
//    if (css == undefined) {
//        css = 'alert-info';
//    }
//
//    var timeout = setTimeout(function () {
//        //$('#' + timeout).alert('close'); 
//    }, 5000);
//
//    var alert = $("<div/>").addClass('alert alert-dismissible fade in ' + css)
//            .css('max-height', '20em').css('overflow', 'auto')
//            .attr('id', timeout).attr('role', 'alert');
//    alert.html("<button type='button' class='close' data-dismiss='alert'>&times;</button>" + msg);
//
//    $("#qz-alert").append(alert);
//}

//function pinMessage(msg, id, css) {
//    if (css == undefined) {
//        css = 'alert-info';
//    }
//
//    var alert = $("<div/>").addClass('alert alert-dismissible fade in ' + css)
//            .css('max-height', '20em').css('overflow', 'auto').attr('role', 'alert')
//            .html("<button type='button' class='close' data-dismiss='alert'>&times;</button>");
//
//    var text = $("<div/>").html(msg);
//    if (id != undefined) {
//        text.attr('id', id);
//    }
//
//    alert.append(text);
//
//    $("#qz-pin").append(alert);
//}

function updateState(text, css) {
    $("#qz-status").html(text);
    $("#qz-connection").removeClass().addClass('panel panel-' + css);

    if (text === "Inactive" || text === "Error") {
        $("#launch").show();
    } else {
        $("#launch").hide();
    }
}

//function getPath() {
//    var path = window.location.href;
//    return path.substring(0, path.lastIndexOf("/"));
//}

//function usbButton(ids, data) {
//    var click = "";
//    for (var i in ids) {
//        click += "$('#" + ids[i] + "').val('0x" + data[i] + "');$('#" + ids[i] + "').fadeOut(300).fadeIn(500);";
//    }
//    return '<button class="btn btn-default btn-xs" onclick="' + click + '" data-dismiss="alert">Use This</button>';
//}

/** Attempts to parse scale reading from USB raw output */
//function readScaleData(data) {
//    // Get status
//    var status = parseInt(data[1], 16);
//    switch (status) {
//        case 1: // fault
//        case 5: // underweight
//        case 6: // overweight
//        case 7: // calibrate
//        case 8: // re-zero
//            status = 'Error';
//            break;
//        case 3: // busy
//            status = 'Busy';
//            break;
//        case 2: // stable at zero
//        case 4: // stable non-zero
//        default:
//            status = 'Stable';
//    }
//
//    // Get precision
//    var precision = parseInt(data[3], 16);
//    precision = precision ^ -256; //unsigned to signed
//
//    // xor on 0 causes issues
//    if (precision == -256) {
//        precision = 0;
//    }
//
//    // Get weight
//    data.splice(0, 4);
//    data.reverse();
//    var weight = parseInt(data.join(''), 16);
//
//    weight *= Math.pow(10, precision);
//    weight = weight.toFixed(Math.abs(precision));
//
//    // Get units
//    var units = parseInt(data[2], 16);
//    switch (units) {
//        case 3:
//            units = 'kg';
//            break;
//        case 11:
//            units = 'oz';
//            break;
//        case 12:
//        default:
//            units = 'lbs';
//    }
//
//    return weight + units + ' - ' + status;
//}


/// QZ Config ///
var cfg = null;
function getUpdatedConfig() {
    if (cfg == null) {
        cfg = qz.configs.create(null);
    }

    updateConfig();
    return cfg
}

function updateConfig() {
    var pxlSize = null;
    if ($("#pxlSizeActive").prop('checked')) {
        pxlSize = {
            width: $("#pxlSizeWidth").val(),
            height: $("#pxlSizeHeight").val()
        };
    }

    var pxlMargins = $("#pxlMargins").val();
    if ($("#pxlMarginsActive").prop('checked')) {
        pxlMargins = {
            top: $("#pxlMarginsTop").val(),
            right: $("#pxlMarginsRight").val(),
            bottom: $("#pxlMarginsBottom").val(),
            left: $("#pxlMarginsLeft").val()
        };
    }

    var copies = 1;
    var jobName = null;
    if ($("#rawTab").hasClass("active")) {
        copies = $("#rawCopies").val();
        jobName = $("#rawJobName").val();
    } else {
        copies = $("#pxlCopies").val();
        jobName = $("#pxlJobName").val();
    }

    cfg.reconfigure({
        altPrinting: $("#rawAltPrinting").prop('checked'),
        encoding: $("#rawEncoding").val(),
        endOfDoc: $("#rawEndOfDoc").val(),
        perSpool: $("#rawPerSpool").val(),
        colorType: $("#pxlColorType").val(),
        copies: copies,
        jobName: jobName,
        density: $("#pxlDensity").val(),
        duplex: $("#pxlDuplex").prop('checked'),
        interpolation: $("#pxlInterpolation").val(),
        margins: pxlMargins,
        orientation: $("#pxlOrientation").val(),
        paperThickness: $("#pxlPaperThickness").val(),
        printerTray: $("#pxlPrinterTray").val(),
        rotation: $("#pxlRotation").val(),
        scaleContent: $("#pxlScale").prop('checked'),
        size: pxlSize,
        units: $("input[name='pxlUnits']:checked").val()
    });
}

function setPrintFile() {
    setPrinter({file: $("#askFile").val()});
    $("#askFileModal").modal('hide');
}

function setPrintHost() {
    setPrinter({host: $("#askHost").val(), port: $("#askPort").val()});
    $("#askHostModal").modal('hide');
}

function setPrinter(printer) {
    var cf = getUpdatedConfig();
    cf.setPrinter(printer);

    if (typeof printer === 'object' && printer.name == undefined) {
        var shown;
        if (printer.file != undefined) {
            shown = "<em>FILE:</em> " + printer.file;
        }
        if (printer.host != undefined) {
            shown = "<em>HOST:</em> " + printer.host + ":" + printer.port;
        }

        $("#configPrinter").html(shown);
    } else {
        if (printer.name != undefined) {
            printer = printer.name;
        }

        if (printer == undefined) {
            printer = 'NONE';
        }
        $("#configPrinter").html(printer);
    }
}

function  setmensajeImpresionStatus(txt) {
//    $("#mensajeImpresionStatus").html(txt);
}

var zplCadena = "";

function printZPLZebra(zpl) {
	if (typeof (zpl) !== "undefined") {
		if (zpl !== "") {
		    zplCadena = zpl;
		    imprimirZebraQZ();
		}else{
			mensajes("No hay información para imprimir");
            hideDialog(true);
		}
	}else{
		mensajes("No hay información para imprimir");
          hideDialog(true);
	}
}

function startConnectionCallback(config, callback) {
    if (!qz.websocket.isActive()) {
        updateState('Waiting', 'default');

        qz.websocket.connect(config).then(function () {
            callback();
            updateState('Active', 'success');
            findVersion();
        }).catch(handleConnectionError);

    } else {
        callback();
    }
}

function setTerminoStartConnection() {
    setmensajeImpresionStatus("Conexion iniciada...");
    terminoStartConnection = true;
}

var myIntervalImpresion = null;
var terminoStartConnection = false;

function imprimirZebraQZ() {
    terminoStartConnection = false;
    clearInterval(myIntervalImpresion);
    var config = getUpdatedConfig();
    mensajes("Iniciando conexión...");
    
    $("#imgPrinter").addClass("displayNone");

    setTimeout(function () {
        startConnectionCallback(config, setTerminoStartConnection);
    }, 1000);

    myIntervalImpresion = setInterval(function () {
        if (terminoStartConnection) {
            clearInterval(myIntervalImpresion);
            mensajes("Buscando impresoras");
//            var printer = "zebraPaquetexpress";

            var defaultPrinter = "";
            qz.printers.find().then(function (data) {
            	mensajes("Impresoras encontradas");
        		$('#rowImpresoras').removeClass("displayNone");
        		
        		var impresoraCookie = getImpresoraCookie();
        		var encontroImpresora = false;
            	for (var i = 0; i < data.length; i++) {
            		
            		var itemActual = $('<option>',
                   	     {
                   	        value: data[i],
                   	        text : data[i]
                   	    });
            		
            		if (impresoraCookie !== "N"){
	            		if (impresoraCookie === data[i]){
	            			itemActual.attr("selected", "selected");	            			
	            			encontroImpresora = true;
	            		}
            		}            	 
            	     $('#selectPrinters').append(itemActual);            	                 	     
            	}
            	if (encontroImpresora){
            		onSelectImpresora(impresoraCookie);
            	}
            }).catch(displayError);
        }
    }, 1000);
}

function getImpresoraCookie(){
	var listCookies = document.cookie;
	var list = listCookies.split(";");
	var encontroBandeja = false;
	var nameCookie = "impresoraWBZebra";
	var impresoraCookie = "N";
	for (var x in list) {
	    if (typeof (list[x]) !== "undefined") {
	    	var actual =list[x].trim(); 
	    	var splitActual = actual.split("=");
	    	var name = splitActual[0];
	        if (name === nameCookie) {
	            impresoraCookie = splitActual[1];
	            break;
	        }
	    } 
	}
	return impresoraCookie;
}

function sendImpresion(){
	var defaultPrinter = $('#selectPrinters').val();
	if (defaultPrinter !== "-"){
		mensajes("Asignando impresora...");		
		
		var config = getUpdatedConfig();
		mensajes("Preparando impresion...");
		$('#rowImpresoras').addClass("displayNone");
		$("#imgPrinter").removeClass("displayNone");
		imprimirFin(defaultPrinter, true, config);
		
		document.cookie = "impresoraWBZebra=" + defaultPrinter;
       
	}else{
		mensajes("Selecciona una impresora");
	}
}

function onSelectImpresora(defaultPrinter){
	if (defaultPrinter !== "-"){
		$("#buttonPrintSend").attr("style","");
	}else{
		$("#buttonPrintSend").attr("style","display:none;");
		mensajes("Selecciona una impresora");
	}
}

function imprimirFin(query, set, config) {
    qz.printers.find(query).then(function (data) {
        if (set) {
            setPrinter(data);
            var zpLLabel = zplCadena;
            if (zpLLabel !== "") {
            	
            	try {
            		
	                var printDataLabel = [
	                	'^ XA', '~DGZ,03540,010,00001FFFFFFFFFFFFFF8', '0001FFFFFFFFFFFFFFF8', '0003FFFFFFFFFFFFFFF8', '001FFFFFFFFFFFFFFFF8', '007FFFFFFFFFFFFFFFF8', '007FFFFFFFFFFFFFFFF8', '017FFFFFFFFFFFFFFFF8', '03FFFFFFFFFFFFFFFFF8', '07FFFFFFFFFFFFFFFFF8', '0FFFFFFFFFFFFFFFFFF8', '0FFFFFFFFFFFFFFFFFF8', '0FFFFFFFFFFFFFFFFFF8', '1FFFFFFFFFFFFFFFFFF8', '1FFFFFFFFFFFFFFFFFF8', '3FFFFFFFFFFFFFFFFFF8', '3FFFFFFFFFFFFFFFFFF8', '3FFFFFFFFFFFFFFFFFF8', '3FFFFFFFFFFFFFFFFFF8', '7FFFFFFFFFFFFFFFFFF8', '7FFFFFFFFFFFFFFFFFF8', '7FFFF800000000000000', '7FFFE000000000000000', '7FFF0000000000000000', '7FFE0000000000000000', '7FFC01FFFFFFFFFFFFF8', '7FF80FFFFFFFFFFFFFF8', '7FF03FFFFFFFFFFFFFF8', '7FE0FFFFFFFFFFFFFFF8', '7FC1FFFFFFFFFFFFFFF8', '7F81FFFFFFFFFFFFFFF8', '7F83FFFFFFFFFFFFFFF8', '7F83FFFFFFFFFFFFFFF8', '7F07FFFFFFFFFFFFFFF8', '7F0FFFC0000000000000', '7F0FFE00000000000000', '7F0FF800000000000000', '7F0FF000000000000000', '7F0FE000000000000000', '7F1FE000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC000001F800FC000', '7F1FC000001F800FC000', '7F1FC000001F800FC000', '7F1FC000001FC01FC000', '7F1FC000001FE03FC000', '7F1FC000000FFFFF8000', '7F1FC0000007FFFF8000', '7F1FC0000007FFFF8000', '7F1FC0000003FFFE0000', '7F1FC0000000FFFC0000', '7F1FC00000003F700000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC030000000000000', '7F1FC03FC00000000000', '7F1FC03FFF0000000000', '7F1FC03FFFF000000000', '7F1FC03FFFFFC0000000', '7F1FC03FFFFFFF000000', '7F1FC03FFFFFFFF20000', '7F1FC0003FFFFFFFC000', '7F1FC0003FFFFFFFC000', '7F1FC0003F07FFFFC000', '7F1FC0003F0001FFC000', '7F1FC0003F003FFFC000', '7F1FC0003F7FFFFFC000', '7F1FC0003FFFFFFFC000', '7F1FC003FFFFFFFF8000', '7F1FC03FFFFFFFC00000', '7F1FC03FFFFFFE000000', '7F1FC03FFFFF00000000', '7F1FC03FFFC000000000', '7F1FC03FFE0000000000', '7F1FC03F000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000002010000000', '7F1FC00003FFFF000000', '7F1FC0003FFFFFE00000', '7F1FC0007FFFFFFC0000', '7F1FC003FFFFFFFE0000', '7F1FC007FFFFFFFF0000', '7F1FC00FFFFFFFFF8000', '7F1FC00FFF000FFFC000', '7F1FC01FF000007F8000', '7F1FC03FE200003FC000', '7F1FC03FC600001FE000', '7F1FC03FCE00001FE000', '7F1FC03FFFC0001FE000', '7F1FC03FFF80001FE000', '7F1FC03FFF00003FC000', '7F1FC01FFE0000BFC000', '7F1FC00FFE0003FFC000', '7F1FC00FFFFFFFFF8000', '7F1FC03FFFFFFFFF0000', '7F1FC07FFFFFFFFE0000', '7F1FC0FCFFFFFFE80000', '7F1FC03C1FFFFFE00000', '7F1FC03807FFFF000000', '7F1FC010000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC0003FFFFFFFC000', '7F1FC001FFFFFFFFC000', '7F1FC003FFFFFFFFC000', '7F1FC007FFFFFFFFC000', '7F1FC00FFFFFFFFFC000', '7F1FC01FFFFFFFFFC000', '7F1FC01FFF0000000000', '7F1FC03FC00000000000', '7F1FC03FC00000000000', '7F1FC03F800000000000', '7F1FC03F800000000000', '7F1FC03FC00000000000', '7F1FC03FC00000000000', '7F1FC01FF80000000000', '7F1FC01FFFFFFFFFC000', '7F1FC017FFFFFFFFC000', '7F1FC00FFFFFFFFFC000', '7F1FC007FFFFFFFFC000', '7F1FC001FFFFFFFFC000', '7F1FC0007FFFFFFFC000', '7F1FC000040000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC000001FC000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC0200000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC000001FC000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC020000000000000', '7F1FC030000000000000', '7F1FC03F00000001C000', '7F1FC03FE000000FC000', '7F1FC03FF40000BFC000', '7F1FC03FFF0003FFC000', '7F1FC03FFFE01FFFC000', '7F1FC03FFFFCFFFFC000', '7F1FC006FFFFFFFD8000', '7F1FC0007FFFFFF00000', '7F1FC00003FFFF000000', '7F1FC00003FFFF000000', '7F1FC0001BFFFFC00000', '7F1FC001FFFFFFFC0000', '7F1FC007FFFBFFFF8000', '7F1FC03FFFD86FFFC000', '7F1FC03FFF8007FFC000', '7F1FC03FFE0003FFC000', '7F1FC03FF000003FC000', '7F1FC03F80000007C000', '7F1FC03E00000001C000', '7F1FC030000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC020000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC020001F800FC000', '7F1FC000001F800FC000', '7F1FC000001F800FC000', '7F1FC000001FC00FC000', '7F1FC000001FE01FC000', '7F1FC000000FFFFFC000', '7F1FC0000007FFFF8000', '7F1FC0000007FFFF8000', '7F1FC0000001FFFF0000', '7F1FC0000001FFFC0000', '7F1FC00000007FF80000', '7F1FC000000003000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC00000FF800FC000', '7F1FC000037F800FC000', '7F1FC00007FF800FC000', '7F1FC000DFFFC0078000', '7F1FC007FFFFD03FC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFE7FFFF8000', '7F1FC03FFF03FFFF8000', '7F1FC03FFC03FFFF0000', '7F1FC03FC000FFFC0000', '7F1FC03E00001FE00000', '7F1FC038000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC008001FC000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC00000000FE00000', '7F1FC00FE0007FFC0000', '7F1FC01FE001FFFF0000', '7F1FC03FC003FFFF8000', '7F1FC03FC00FFFFFC000', '7F1FC03FC01FFFFFC000', '7F1FC03FC01FFFFFE000', '7F1FC03FC07FF01FE000', '7F1FC03FFF7FE00FE000', '7F1FC01FFFFFC00FE000', '7F1FC017FFFE000FC000', '7F1FC00FFFFE001FC000', '7F1FC007FFFC001FC000', '7F1FC001FFF8003F8000', '7F1FC0007BC000000000', '7F1FC000040000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC00000000FE00000', '7F1FC00FE0007FFC0000', '7F1FC01FE000FFFF0000', '7F1FC03F8003FFFF8000', '7F1FC03FC00FFFFFC000', '7F1FC03FC01FFFFFC000', '7F1FC03FC01FFBFFC000', '7F1FC03F807FF81FE000', '7F1FC03FE8FFE00FE000', '7F1FC01FFFFFC00FE000', '7F1FC00FFFFF800FC000', '7F1FC00FFFFF001FC000', '7F1FC003FFF8001FC000', '7F1FC001FFF8003F8000', '7F1FC0017FD000000000', '7F1FC0001F0000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC034000000000000', '7F1FC038000000000000', '7F1FC03C000000000000', '7F1FC020000000000000', '7F1FC020000000000000', '7F1FC03C000000000000', '7F1FC004000000000000', '7F1FC038000000000000', '7F1FC020000000000000', '00000000000000000000',
	                    zpLLabel
	                ];
	                qz.print(config, printDataLabel).catch(displayError);
	                                
	                console.log("Se envio impresion a..." + data);
	                
	                mensajes("Se envio impresion a " + data);
	                 
	                $("#iconCheckSuccess").attr("style","");
	                
	                hideDialog(true);
	                
             	} catch (x) {
            		console.log(x);
            		hideDialog(false);
            	}
            	
            } else {
                alert("No hay datos para imprimir");
                hideDialog(false);
            }
             
        }
    }).catch(displayError);
}

function hideDialog(time){
	var tiempo = 0;		
	if (time){
		tiempo = 5000;		
	}	 
	setTimeout(function () {
     	$("#zonePrintQZ", parent.document).html("");
     }, tiempo);
}


function mensajes(txt){
	$("#textMsj").html(txt);
}

qz.security.setSignaturePromise(function (toSign) {
    return function (resolve, reject) {
    	$.ajax("MessageSigner?request=" + toSign).then(resolve, reject);
    };
});

var urlRest = "";

function setValueUrl(val) {
    urlRest = val;
}
function getLogo() {
    return '^ XA', '~DGZ,03540,010,00001FFFFFFFFFFFFFF8', '0001FFFFFFFFFFFFFFF8', '0003FFFFFFFFFFFFFFF8', '001FFFFFFFFFFFFFFFF8', '007FFFFFFFFFFFFFFFF8', '007FFFFFFFFFFFFFFFF8', '017FFFFFFFFFFFFFFFF8', '03FFFFFFFFFFFFFFFFF8', '07FFFFFFFFFFFFFFFFF8', '0FFFFFFFFFFFFFFFFFF8', '0FFFFFFFFFFFFFFFFFF8', '0FFFFFFFFFFFFFFFFFF8', '1FFFFFFFFFFFFFFFFFF8', '1FFFFFFFFFFFFFFFFFF8', '3FFFFFFFFFFFFFFFFFF8', '3FFFFFFFFFFFFFFFFFF8', '3FFFFFFFFFFFFFFFFFF8', '3FFFFFFFFFFFFFFFFFF8', '7FFFFFFFFFFFFFFFFFF8', '7FFFFFFFFFFFFFFFFFF8', '7FFFF800000000000000', '7FFFE000000000000000', '7FFF0000000000000000', '7FFE0000000000000000', '7FFC01FFFFFFFFFFFFF8', '7FF80FFFFFFFFFFFFFF8', '7FF03FFFFFFFFFFFFFF8', '7FE0FFFFFFFFFFFFFFF8', '7FC1FFFFFFFFFFFFFFF8', '7F81FFFFFFFFFFFFFFF8', '7F83FFFFFFFFFFFFFFF8', '7F83FFFFFFFFFFFFFFF8', '7F07FFFFFFFFFFFFFFF8', '7F0FFFC0000000000000', '7F0FFE00000000000000', '7F0FF800000000000000', '7F0FF000000000000000', '7F0FE000000000000000', '7F1FE000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC000001F800FC000', '7F1FC000001F800FC000', '7F1FC000001F800FC000', '7F1FC000001FC01FC000', '7F1FC000001FE03FC000', '7F1FC000000FFFFF8000', '7F1FC0000007FFFF8000', '7F1FC0000007FFFF8000', '7F1FC0000003FFFE0000', '7F1FC0000000FFFC0000', '7F1FC00000003F700000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC030000000000000', '7F1FC03FC00000000000', '7F1FC03FFF0000000000', '7F1FC03FFFF000000000', '7F1FC03FFFFFC0000000', '7F1FC03FFFFFFF000000', '7F1FC03FFFFFFFF20000', '7F1FC0003FFFFFFFC000', '7F1FC0003FFFFFFFC000', '7F1FC0003F07FFFFC000', '7F1FC0003F0001FFC000', '7F1FC0003F003FFFC000', '7F1FC0003F7FFFFFC000', '7F1FC0003FFFFFFFC000', '7F1FC003FFFFFFFF8000', '7F1FC03FFFFFFFC00000', '7F1FC03FFFFFFE000000', '7F1FC03FFFFF00000000', '7F1FC03FFFC000000000', '7F1FC03FFE0000000000', '7F1FC03F000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000002010000000', '7F1FC00003FFFF000000', '7F1FC0003FFFFFE00000', '7F1FC0007FFFFFFC0000', '7F1FC003FFFFFFFE0000', '7F1FC007FFFFFFFF0000', '7F1FC00FFFFFFFFF8000', '7F1FC00FFF000FFFC000', '7F1FC01FF000007F8000', '7F1FC03FE200003FC000', '7F1FC03FC600001FE000', '7F1FC03FCE00001FE000', '7F1FC03FFFC0001FE000', '7F1FC03FFF80001FE000', '7F1FC03FFF00003FC000', '7F1FC01FFE0000BFC000', '7F1FC00FFE0003FFC000', '7F1FC00FFFFFFFFF8000', '7F1FC03FFFFFFFFF0000', '7F1FC07FFFFFFFFE0000', '7F1FC0FCFFFFFFE80000', '7F1FC03C1FFFFFE00000', '7F1FC03807FFFF000000', '7F1FC010000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC0003FFFFFFFC000', '7F1FC001FFFFFFFFC000', '7F1FC003FFFFFFFFC000', '7F1FC007FFFFFFFFC000', '7F1FC00FFFFFFFFFC000', '7F1FC01FFFFFFFFFC000', '7F1FC01FFF0000000000', '7F1FC03FC00000000000', '7F1FC03FC00000000000', '7F1FC03F800000000000', '7F1FC03F800000000000', '7F1FC03FC00000000000', '7F1FC03FC00000000000', '7F1FC01FF80000000000', '7F1FC01FFFFFFFFFC000', '7F1FC017FFFFFFFFC000', '7F1FC00FFFFFFFFFC000', '7F1FC007FFFFFFFFC000', '7F1FC001FFFFFFFFC000', '7F1FC0007FFFFFFFC000', '7F1FC000040000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC000001FC000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC0200000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC0000000001FC000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC000001FC000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC020000000000000', '7F1FC030000000000000', '7F1FC03F00000001C000', '7F1FC03FE000000FC000', '7F1FC03FF40000BFC000', '7F1FC03FFF0003FFC000', '7F1FC03FFFE01FFFC000', '7F1FC03FFFFCFFFFC000', '7F1FC006FFFFFFFD8000', '7F1FC0007FFFFFF00000', '7F1FC00003FFFF000000', '7F1FC00003FFFF000000', '7F1FC0001BFFFFC00000', '7F1FC001FFFFFFFC0000', '7F1FC007FFFBFFFF8000', '7F1FC03FFFD86FFFC000', '7F1FC03FFF8007FFC000', '7F1FC03FFE0003FFC000', '7F1FC03FF000003FC000', '7F1FC03F80000007C000', '7F1FC03E00000001C000', '7F1FC030000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC020000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC020001F800FC000', '7F1FC000001F800FC000', '7F1FC000001F800FC000', '7F1FC000001FC00FC000', '7F1FC000001FE01FC000', '7F1FC000000FFFFFC000', '7F1FC0000007FFFF8000', '7F1FC0000007FFFF8000', '7F1FC0000001FFFF0000', '7F1FC0000001FFFC0000', '7F1FC00000007FF80000', '7F1FC000000003000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC00000FF800FC000', '7F1FC000037F800FC000', '7F1FC00007FF800FC000', '7F1FC000DFFFC0078000', '7F1FC007FFFFD03FC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFE7FFFF8000', '7F1FC03FFF03FFFF8000', '7F1FC03FFC03FFFF0000', '7F1FC03FC000FFFC0000', '7F1FC03E00001FE00000', '7F1FC038000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FFFFFFFFFC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC00FE01FC000', '7F1FC03FC008001FC000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC00000000FE00000', '7F1FC00FE0007FFC0000', '7F1FC01FE001FFFF0000', '7F1FC03FC003FFFF8000', '7F1FC03FC00FFFFFC000', '7F1FC03FC01FFFFFC000', '7F1FC03FC01FFFFFE000', '7F1FC03FC07FF01FE000', '7F1FC03FFF7FE00FE000', '7F1FC01FFFFFC00FE000', '7F1FC017FFFE000FC000', '7F1FC00FFFFE001FC000', '7F1FC007FFFC001FC000', '7F1FC001FFF8003F8000', '7F1FC0007BC000000000', '7F1FC000040000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC00000000FE00000', '7F1FC00FE0007FFC0000', '7F1FC01FE000FFFF0000', '7F1FC03F8003FFFF8000', '7F1FC03FC00FFFFFC000', '7F1FC03FC01FFFFFC000', '7F1FC03FC01FFBFFC000', '7F1FC03F807FF81FE000', '7F1FC03FE8FFE00FE000', '7F1FC01FFFFFC00FE000', '7F1FC00FFFFF800FC000', '7F1FC00FFFFF001FC000', '7F1FC003FFF8001FC000', '7F1FC001FFF8003F8000', '7F1FC0017FD000000000', '7F1FC0001F0000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC000000000000000', '7F1FC034000000000000', '7F1FC038000000000000', '7F1FC03C000000000000', '7F1FC020000000000000', '7F1FC020000000000000', '7F1FC03C000000000000', '7F1FC004000000000000', '7F1FC038000000000000', '7F1FC020000000000000', '00000000000000000000';
}
