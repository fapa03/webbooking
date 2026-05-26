function isNumber(evt,txt) {
    var charCode = (evt.which) ? evt.which : evt.keyCode
    //Teclas flecha izq, der, abajo, arriba, Enter
    if(charCode == 37 || charCode == 38 || charCode == 39 || charCode == 40 || charCode == 13){
    	return true;			
    }
    if(charCode == 46){
        if (txt.value.indexOf('.') > -1) {
            return false;
        }
    }else if (charCode > 31 && (charCode < 48 || charCode > 57))
       return false;
    return true;
 }

function cambioCentroCosto() {
	document.forms[0].currentTask.value = "cambioCCosto";
	document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
	document.forms[0].submit();	
}
function zoneextendedverify() {
	document.forms[0].currentTask.value = "zoneextendedverify";
	document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
	document.forms[0].submit();
}
function facturarCambios() {

//	if (document.forms[0].cash != null) {
//		if ((document.forms[0].cash.checked) || (document.forms[0].credit.checked)) {
//			if (document.forms[0].cash.checked)
//				document.forms[0].payMode.value = "ToPay";
//			if (document.forms[0].credit.checked)
//				document.forms[0].payMode.value = "PAID";
//		} else {
//			alert("SELECCIONE EL TIPO DE PAGO");
//			return false;
//		}
//	}
	
	if (document.forms[0].clientHasLocalCredit.value == "N") {
		alert("EL CLIENTE NO TIENE CREDITO HABILITADO. NO PUEDE AGREGAR SERVICIOS.");
		return false;
	} else {
		document.forms[0].payMode.value = "PAID";
	}
	if (document.forms[0].allowNewInvoice.value!="S") {
		alert("CAMBIE EL CODIGO DE CLIENTE A UNO QUE PERTENEZCA A ESTA SUCURSAL");
		openLov('sipwebclientonly');
		return false;
	}
	if (document.forms[0].fiscaladdresscode.value=="") {
			alert("SELECCIONE LA DIRECCION FISCAL DEL CLIENTE EN ESTA SUCURSAL");
			return false;
	}
	if (document.forms[0].aceptarNuevosCargos.value == "S") {
		var cadenaCambios = "Se facturaran los siguientes conceptos:";
		var vEXT = new Number(document.forms[0].nuevoValorExt.value);
		cadenaCambios = cadenaCambios + "\r\n Cargo por Zona Ext: $" + vEXT.toFixed(2);
		
		var vValorIVA = parseFloat(document.forms[0].valorIVA.value);
		var iva = parseFloat(vEXT) * (vValorIVA / 100);
		var fiva = new Number(iva);
		
		var vValorRetAmount = parseFloat(document.forms[0].valorRetAmount.value);
		var ret = 0;
		
		if (document.forms[0].clientType.value == "C" || document.forms[0].clientType.value == "G") {
			ret = vValorRetAmount;
		}
		var fret = new Number(ret);
		
		var ttotal = parseFloat(vEXT) + parseFloat(iva);
		var fttotal = new Number(ttotal);
		
		cadenaCambios = cadenaCambios + "\r\n IVA:                $ " + fiva.toFixed(2);
		if (ret > 0) {
			cadenaCambios=cadenaCambios + "\r\n Retencion:          $ " + fret.toFixed(2);
		}
		cadenaCambios = cadenaCambios + "\r\n ==================================";
		cadenaCambios = cadenaCambios + "\r\n Total Aprox.:       $ " + fttotal.toFixed(2);
		i = confirm(cadenaCambios + "\r\n Está seguro de preparar la factura?");
		if (i == true) {
			if (fttotal<=0) {
				alert('No se modificaron correctamente los servicios, favor de validar la informacion y reiniciar la Modificacion');
				return false;
			} else {
				document.forms[0].currentTask.value = "guideconfirm";
				document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
				document.forms[0].submit();
				document.forms[0].facturar.disabled = true;
			}
		}
	}
}

function callCreditMode() {
	if (document.forms[0].cash.checked) {
		alert("SOLO UN TIPO DE PAGO PUEDE SELECCIONAR");
		document.forms[0].credit.checked = false;
	} else
		document.forms[0].payMode.value = "PAID";
}

function callCashMode() {
	if (document.forms[0].credit.checked) {
		alert("SOLO UN TIPO DE PAGO PUEDE SELECCIONAR");
		document.forms[0].cash.checked = false;
	} else
		document.forms[0].payMode.value = "ToPay";
}

function changeGuia() {
	if (document.forms[0].guiaOld.value != "") {
		alert("No se puede volver a modificar un rastreo que ya fue modificada.");
	} else if (document.forms[0].aceptarNuevosCargos.value == "S") {
		alert("Ya se modifico este rastreo y no se pueden realizar mas cambios");
	} else {
		if (document.forms[0].modificar.value == "Modificar Documentacion") {
			extChk();
			if (document.forms[0].shipType.value !='ST' && (document.forms[0].ext.value == "N" || document.forms[0].ext.value == "S")) {
				alert("El RASTREO NO SE PUEDE MODIFICAR POR EL TIPO DE ENVIO SELECCIONADO");
				return;
			} else if (document.forms[0].ext.value == "N") {
				document.forms[0].extCheck.disabled = false;
			} else {
				if (!document.forms[0].extCheck.checked && !document.forms[0].modFormnoFlag.value == "Y"){
	        		alert("El rastreo no se puede modificar");
	 	            return;
	        	}
			}
			document.forms[0].modificar.value = "Finalizar cambios";
			document.forms[0].confirmar.disabled = true;
			document.forms[0].limpiar.disabled = true;
			document.forms[0].facturar.disabled = true;
			document.forms[0].modFormnoFlag.value = "Y";
		} else {
			if (document.forms[0].extCheck.checked && document.forms[0].extOrgnValue.value != 'S' && document.forms[0].modFormnoFlag.value == "Y"){
				document.forms[0].extCheck.disabled = true;
				document.forms[0].modificar.value = "Modificar Documentacion";
				document.forms[0].aceptarNuevosCargos.value = "S";
				document.forms[0].confirmar.disabled = true;
				document.forms[0].limpiar.disabled = false;
				document.forms[0].facturar.disabled = false;
				document.forms[0].modFormnoFlag.value = "N";
				document.forms[0].currentTask.value = "guidepreview";
				document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
				document.forms[0].submit();
	    	}else{
	    		alert('No se ha modificado el rastreo.');
	    	}
		}
	}

}
function changebranch() {
	document.forms[0].action = "changebranch.do";
	document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
	document.forms[0].submit();
}

function extChk() {
	if (document.forms[0].extCheck.checked) {
		document.forms[0].ext.value = "S";

	} else {
		document.forms[0].ext.value = "N";
	}
}

function changeDeliveryOcurre(str) {
	if (document.forms[0].deliveryType.value == "O") {
		document.forms[0].opcOcurre.disabled = false;
		document.forms[0].brnchOcurre.disabled = false;
		//document.getElementById('btnSucursales').style.visibility="visible";
		if (str != "init"){
			if (document.forms[0].opcOcurre.checked){
				submitFormMain('checkbranchtrue','Buscando sucursales ocurre','');
			}else{
				submitFormMain('checkbranchfalse','Buscando sucursales ocurre','');
			}
		}
		calcVolWeight();
	} else {
		document.forms[0].opcOcurre.disabled = true;
		document.forms[0].brnchOcurre.disabled = true;
		document.getElementById('btnSucursales').style.visibility="hidden";
	}
	if (document.forms[0].volH.value != "" && document.forms[0].volL.value != "" && document.forms[0].volW.value != ""){
		if (document.forms[0].currentTask.value == "checkbranchtrue" || document.forms[0].currentTask.value == "checkbranchfalse"){
			document.forms[0].packType.focus();
		}
	}
	
}
function updateBrnchOcu() {
	if (document.forms[0].volH.value != "" && document.forms[0].volL.value != "" && document.forms[0].volW.value != ""){
		changeDeliveryOcurre('');
		calcVolWeight();
	}
}
function changeSelectedAddr(str){
	var tmp = str.split("|");
	var addr = "Direcci\u00F3n : " + tmp[2];
	if (!(typeof tmp[2] === 'undefined')){
		document.getElementById("addr").textContent = unescape(addr);
	}else if (document.forms[0].deliveryType.value == "O" && str == ""){
		document.getElementById("addr").textContent = "Direcci\u00F3n : " + unescape(document.forms[0].defaultBrnchAddr.value.split("^")[0]);
	}
}
function brnchOnMap(str){
	var tmp = str.split("|");
	if(!(typeof tmp[3] === 'undefined')){
		window.open("https://www.google.com/maps/search/?api=1&query=" + tmp[3], "Sucursal "+tmp[0], "_blank");
	}else{
		window.open("https://www.google.com/maps/search/?api=1&query=" + document.forms[0].defaultBrnchAddr.value.split("^")[1], "Sucursal", "_blank");
	}
}

function searchBrnch(str) {
	var estado = str.split("|");
	if (str != ""){
		window.open('https://paquetexpress.com.mx/sucursales/'+estado[1].toLowerCase().replace(" ","-"), "Sucursales", "_blank");
	}else{
		window.open('https://paquetexpress.com.mx/sucursales/', "Sucursales", "_blank");	
	}
}

function zoneverify() {
	if ((document.forms[0].currentTask.value != "zoneverified")) {
		document.forms[0].destSite.value = document.forms[0].destSite.value.toUpperCase();
		document.forms[0].destSiteName.value = document.forms[0].destSiteName.value.toUpperCase();
		document.forms[0].oldDestSite.value = document.forms[0].destSite.value;
		document.forms[0].currentTask.value = "zoneverify";
		document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
		document.forms[0].submit();
	} else if (((document.forms[0].currentTask.value == "zoneverified"))
			&& ((document.forms[0].destSite.value != document.forms[0].oldDestSite.value))) {
		document.forms[0].destSite.value = document.forms[0].destSite.value.toUpperCase();
		document.forms[0].destSiteName.value = document.forms[0].destSiteName.value.toUpperCase();
		document.forms[0].oldDestSite.value = document.forms[0].destSite.value;
		document.forms[0].currentTask.value = "zoneverify";
		document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
		document.forms[0].submit();
	}
}
function trim(value) {
	return value.replace(/^\s+|\s+$/g, '');
}
function tiempoEntrega() {
	var tabla = document.getElementById('msjeAvi');
	var reng = tabla.rows;
	if (document.forms[0].deliveryType.value != "") {
		if (document.forms[0].horasEntregaOcu.value == "0") {
			reng[0].cells[0].innerHTML = 'Tiempo estimado de entrega no disponible.';
		} else {
			if (document.forms[0].deliveryType.value == "O") {
				// alert('Tiempo estimado de entrega es de:
				// '+document.forms[0].horasEntregaOcu.value+' Horas para
				// OCURRE.');
				reng[0].cells[0].innerHTML = 'Tiempo estimado de entrega es de: '
						+ document.forms[0].horasEntregaOcu.value + ' Horas.';
			} else {
				// alert('Tiempo estimado de entrega es de:
				// '+document.forms[0].horasEntregaEad.value+' Horas para
				// entrega DOMICILIO.');
				reng[0].cells[0].innerHTML = 'Tiempo estimado de entrega es de: '
						+ document.forms[0].horasEntregaEad.value + ' Horas.';
			}
			/*
			 * if (document.forms[0].deliveryType.value == "O") {
			 * //alert('Tiempo estimado de entrega es de:
			 * '+document.forms[0].horasEntregaOcu.value+' Horas para OCURRE.');
			 * reng[0].cells[0].innerHTML = 'Tiempo estimado de entrega es de:
			 * '+document.forms[0].horasEntregaOcu.value+' Horas para OCURRE.'; }
			 * else if (document.forms[0].deliveryType.value == "H") {
			 * //alert('Tiempo estimado de entrega es de:
			 * '+document.forms[0].horasEntregaEad.value+' Horas para entrega
			 * DOMICILIO.'); reng[0].cells[0].innerHTML = 'Tiempo estimado de
			 * entrega es de: '+document.forms[0].horasEntregaEad.value+' Horas
			 * para entrega DOMICILIO.'; } else { //alert('Tiempo estimado de
			 * entrega es de: '+document.forms[0].horasEntregaEad.value+' Horas
			 * para entrega a ZONA EXTENDIDA.'); reng[0].cells[0].innerHTML =
			 * 'Tiempo estimado de entrega es de:
			 * '+document.forms[0].horasEntregaEad.value+' Horas para entrega a
			 * ZONA EXTENDIDA.'; }
			 */
		}
		reng[0].cells[0].className = "LabelForm";
	}
}
function verifyDelvryType() {
	if ((document.forms[0].deliveryType.value != "O")) {
		if ((document.forms[0].deliveryType.value == "X") 
				&& (document.forms[0].ext.value == "N")) {
			alert('EL DOMICILIO DESTINO PERTENECE A UNA ZONA EXTENDIDA \r\nPERO EL RASTREO NO INCLUYE EL SERVICIO DE ENTREGA EN ZONA EXTENDIDA');
			return;
		}
	} else {
		if (document.forms[0].isExtendedZone.value == "S") {
			if (document.forms[0].destBranch.value.substring(3) == "70") {// AAP01
				var obj = document.forms[0].deliveryType;// AAP01
				var indice = obj.selectedIndex;// AAP01
				alert('NO SE PERMITE ENVIO OCURRE A ZONA EXTENDIDA.');// AAP01
				obj.selectedIndex = -1;
				obj.value = "";
				return false;
			}// AAP01
		}
	}
	if (trim(document.forms[0].destSite.value) == "") {
		alert('SELECCIONE LA CIUDAD DESTINO');
		return;
	} else if (trim(document.forms[0].destclave.value) == "") {
		alert("Capture el nombre del cliente");
		docuement.forms[0].destclave.focus();
		return false;
	} else if (trim(document.forms[0].destnombre.value) == "") {
		alert("Capture el nombre del cliente");
		docuement.forms[0].destnombre.focus();
		return false;
	} else if ((document.forms[0].destcolonia1.value == "")
			|| (document.forms[0].destcolonia2.value == "")) {
		alert("SELECCIONE LA DIRECCIÓN DESTINO");
		return false;
	}
	
	document.forms[0].currentTask.value = "delvryverify";
	document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
	
	if (document.forms[0].deliveryType.value == "O") {
		document.forms[0].opcOcurre.disabled = false;
		document.forms[0].brnchOcurre.disabled = false;
	} else {
		document.forms[0].opcOcurre.disabled = true;
		document.forms[0].brnchOcurre.disabled = true;
	}
	document.forms[0].submit();
	
//	if (document.forms[0].currentTask.value != "eadverified") {
//		document.forms[0].currentTask.value = "delvryverify";
//		document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
//		document.forms[0].submit();
//	} else if (((document.forms[0].currentTask.value == "eadverified"))
//			&& ((trim(document.forms[0].destSite.value) != trim(document.forms[0].oldDestSite.value)))) {
//		document.forms[0].currentTask.value = "delvryverify";
//		document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
//		document.forms[0].submit();
//	}
	tiempoEntrega();
}

var str;
function openKeyLov(str) {
	var prodDes = document.forms[0].productDescSat.value;

	if (document.forms[0].modFormnoFlag.value == "Y"){
		alert("FAVOR DE TERMINAR DE MODIFICAR EL RASTREO");
		document.forms[0].modificar.value = "Modificar Documentacion";
		changeGuia();
		return;
	}
	if(window.event.keyCode==13) {
		if (str == 'catProducts' && prodDes.length < 3){
			alert("CAPTURE 3 CARACTERES O MAS");
			return;
		}
		openLov(str);
	}
	if(window.event.keyCode==9 && str == 'catProducts' && prodDes.length < 3){
		openLov(str);
	}
}
function openLov(str) {
	var clientId = "";
	var destsite = document.forms[0].destSite.value;
	var destcode = document.forms[0].destclave.value;
	clientId = document.forms[0].clientId.value;
	var destnombre = document.forms[0].destnombre.value;
	/* Valida si el usuario se encuentra modificando el rastreo */
	if (document.forms[0].modFormnoFlag.value == "Y"){
		alert("FAVOR DE TERMINAR DE MODIFICAR EL RASTREO");
		document.forms[0].modificar.value = "Modificar Documentacion";
		changeGuia();
		return;
	}
	if (str == 'branchPP') {
		document.forms[0].destSite.value = document.forms[0].destSite.value.toUpperCase();
		document.forms[0].destSiteName.value = document.forms[0].destSiteName.value.toUpperCase();
		window.open('lov.do?type=' + str + '&clientId=' + clientId
						+ '&destsite=' + document.forms[0].destSite.value
						+ '&destsitename='
						+ document.forms[0].destSiteName.value, 'abc',
						'width=400,height=200,top=185,left=200,screenY=185,screenX=200');
	}

	if (str == 'clientwithsite') {
		if (destsite == '') {
			alert('SELECCIONE LA CIUDAD DESTINO');
			return;
		} else if ((document.forms[0].destclave.value == "")
				&& (destnombre == "")) {
			alert("Capture el nombre del cliente");
			document.forms[0].destclave.focus();
			return false;
		} else {
			document.forms[0].destnombre.value = document.forms[0].destnombre.value.toUpperCase();
			destnombre = document.forms[0].destnombre.value;
			destclave = document.forms[0].destclave.value;
			if (destclave.length == '' && destnombre.length < 2){
				alert("CAPTURE 2 CARACTERES O MAS");
				return false;
			}
			window.open('lov.do?type=' + str + '&siteId='
							+ document.forms[0].destSite.value
							+ '&destinationclient=' + destcode
							+ '&destinationclientname=' + destnombre, 'abc',
							'width=600,height=270,top=185,left=200,screenY=185,screenX=200');
		}
	}

	if (str == 'destinationaddressPP') {
		if (destsite == '') {
			alert('SELECCIONE LA CIUDAD DESTINO');
			return;
		} else if (destcode == '') {
			alert('SELECCIONE EL NUMERO DE CLIENTE');
			return;
		} else {
			
			let destClaveOrg=document.forms[0].destclave.value;
			let destClaveBk=document.forms[0].destClaveAux.value;
			
			if(destClaveBk==null || destClaveBk==""){
				alert('SELECCIONE EL CLIENTE');
				return;
			}
			
			if( (destClaveOrg!=destClaveBk) && (destClaveBk!=null && destClaveBk!="")){
				document.forms[0].destclave.value=destClaveBk;	
				destcode=document.forms[0].destclave.value;
			}
			
			let destNombreOrg=document.forms[0].destnombre.value;
			let destNombreBk=document.forms[0].destNombreAux.value;
			if((destNombreOrg!=destNombreBk) && (destNombreBk!=null && destNombreBk!="")){
				document.forms[0].destnombre.value=destNombreBk;
				destnombre=document.forms[0].destnombre.value;
			}
			window.open('lov.do?type=' + str + '&destClId=' + destcode
							+ '&addresstype=DC&destinationclient=' + destcode
							+ '&destsite=' + destsite, 'abc',
							'width=600,height=200,top=200,left=100,screenY=200,screenX=100');
		}
	}

	if (str == 'sipwebfiscaladdress') {
		window.open('lov.do?type=' + str + '&clientId=' + clientId, 'abc',
						'width=600,height=210,,top=200,left=100,screenY=200,screenX=100');
	} else if (str == 'sipweborginaddress') {
		window.open('lov.do?type=' + str + '&clientId=' + clientId, 'abc',
						'width=600,height=210,,top=200,left=100,screenY=200,screenX=100');
	} else if (str == 'sipwebclient') {
		document.forms[0].clientName.value = document.forms[0].clientName.value.toUpperCase();
		window.open('lov.do?type=' + str + '&clientname='
						+ document.forms[0].clientName.value, 'abc',
						'width=555,height=250,top=185,left=200,screenY=185,screenX=200');
	}

	if (str == 'fiscaladdress') {
		window.open('lov.do?type=' + str + '&clientId=' + clientId, 'abc', 'width=600,height=210,,top=200,left=100,screenY=200,screenX=100');
	} else if (str == 'client') {
		window.open('lov.do?type=' + str, 'abc', 'width=555,height=290,top=185,left=200,screenY=185,screenX=200');
	} else if (str == 'sipwebclientonly') {
		document.forms[0].clientName.value = document.forms[0].clientName.value.toUpperCase();
		window.open('lov.do?type=' + str + '&clientname='
						+ document.forms[0].clientName.value + '&clientId='
						+ document.forms[0].clientId.value, 'abc',
						'width=555,height=250,top=185,left=200,screenY=185,screenX=200');
	} else if (str == 'clientaddress') {
		window.open('lov.do?type=' + str + '&clientId=' + clientId, 'abc', 'width=600,height=200,,top=200,left=100,screenY=200,screenX=100');
	}  else if (str == 'cliententry'){
		if (destsite == '') {
			alert('SELECCIONE LA CIUDAD DESTINO');
			document.forms[0].destSite.focus();
			return;
		} else {
			document.forms[0].destclave.value="";
			document.forms[0].destnombre.value="";
			document.forms[0].destClaveAux.value="";
			document.forms[0].destNombreAux.value="";
			window.open('lov.do?type='+str,str,'width=830,height=499,top=185,left=0,screenY=100,screenX=100');
		}
	}
	if (str == 'catProducts'){
		if (document.forms[0].productDescSat.value == '') {
			alert("CAPTURE UN VALOR PARA 'CAT. PRODUCTOS'");
			return;
		}else{
			var prodDes = document.forms[0].productDescSat.value;
			if (str == 'catProducts' && prodDes.length < 3){
				alert("CAPTURE 3 CARACTERES O MAS");
				return;
			}
		}
		window.open('lov.do?type='+str+'&contenido='+document.forms[0].productDescSat.value,'abc','width=950,height=250,top=185,left=0,screenY=100,screenX=100');
	}
}

function loaddetail() {
	document.forms[0].currentTask.value = "load";
	document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
	document.forms[0].submit();
}
function submitins() {
	document.forms[0].currentTask.value = "insurance";
	document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
	document.forms[0].submit();
}

function convert() {
	if (document.forms[0].modFormnoFlag.value == "Y"){
		alert("FAVOR DE TERMINAR DE MODIFICAR EL RASTREO");
		document.forms[0].modificar.value = "Modificar Documentacion";
		changeGuia();
		return;
	}
	if (document.forms[0].aceptarNuevosCargos.value == "S") {
		alert("Este rastreo se modifico y no se puede confirmar. \r\n Recarge el rastreo o genere la factura para continuar");
		return false;
	}
	if ((document.forms[0].deliveryType.value != "O")) {
		if ((document.forms[0].deliveryType.value == "X")
				&& (document.forms[0].ext.value == "N")) {
			alert('EL DOMICILIO DESTINO PERTENECE A UNA ZONA EXTENDIDA \r\nPERO EL RASTREO NO INCLUYE EL SERVICIO DE ENTREGA EN ZONA EXTENDIDA');
			return false;
		}
		if ((document.forms[0].isExtendedZone.value == "S")
				&& (document.forms[0].ext.value == "N")) {
			alert("EL DOMICILIO DESTINO PERTENECE A UNA ZONA EXTENDIDA \r\nPERO EL RASTREO NO INCLUYE EL SERVICIO DE ENTREGA EN ZONA EXTENDIDA");
			return false;
		}
		if (document.forms[0].guiaHasErrors.value == "S") {
			alert("El rastreo contiene el siguiente error: \r\n"
					+ document.forms[0].guiaErrorType.value);
			return false;
		}

		if (document.forms[0].isExtendedZone.value == "S"
				&& document.forms[0].checkTelDir.value == 'Y') {// AAP02
			// document.getElementById('divRefDom').style.visibility='hidden';
			// document.getElementById('divRefDom').style.position='absolute';
			if (trim(document.forms[0].desttelefono.value) == '') {// AAP02
				alert("CAPTURE TELEFONO DE DOMICILIO DESTINO");// AAP02
				document.forms[0].desttelefono.focus();// AAP02
				return;// AAP02
			}// AAP02
		}// AAP02

		if (document.forms[0].isExtendedZone.value == "S"
				&& document.forms[0].checkRefDir.value == 'Y') {// AAP02
			// document.getElementById('divRefDom').style.visibility='hidden';
			// document.getElementById('divRefDom').style.position='absolute';
			if (trim(document.forms[0].destRefDom.value) == '') {// AAP02
				alert("CAPTURE REFERENCIA DE DOMICILIO DESTINO");// AAP02
				document.forms[0].destRefDom.focus();// AAP02
				return;// AAP02
			}// AAP02
		}// AAP02
	} else {
		if (document.forms[0].isExtendedZone.value == "S") {
			if (document.forms[0].destBranch.value.substring(3) == "70") {// AAP01
				var obj = document.forms[0].deliveryType;// AAP01
				//var indice = obj.selectedIndex;// AAP01
				alert('NO SE PERMITE ENVIO OCURRE A ZONA EXTENDIDA.');// AAP01
				obj.selectedIndex = -1;
				obj.value = "";
				return false;
			}// AAP01
		}
		
		if (document.forms[0].brncVrtl.value == "1"){
			alert('EL DESTINO SELECCIONADO ES UN DESTINO VIRTUAL, CUYO SERVICIO E.A.D. ES OBLIGATORIO');// AAP01
			obj.selectedIndex = -1;
			obj.value = "";
		}
		
	}

	if (document.forms[0].orgiencolonia1.value == "") {
		alert("SELECCIONE LA DIRECCIÓN ORIGEN");
		return false;
	} else if (document.forms[0].destSite.value == "") {
		alert("SELECCIONE LA PLAZA DESTINO");
		return false;
	} else if (document.forms[0].destclave.value == "") {
		alert("SELECCIONE EL CLIENTE DESTINO");
		return false;
	} else if ((document.forms[0].destcolonia1.value == "")
			|| (document.forms[0].destcolonia2.value == "")) {
		alert("SELECCIONE LA DIRECCIÓN DESTINO");
		return false;
	} else if (document.forms[0].content.value == "") {
		alert("CAPTURE EL CONTENIDO DEL ENVÍO");
		return false;
	} else if(document.forms[0].productIdSat.value == ""){
		alert("FAVOR DE SELECCIONAR UN PRODUCTO DEL CATALOGO");
		document.forms[0].productDescSat.focus();
		return;
	} else if (document.forms[0].deliveryType.value == "") {
		alert("CAPTURA EL TIPO DE ENTREGA");
		return false;
	}
	if (document.forms[0].checkRefDir.value == 'Y') {// AAP02
		document.forms[0].checkRefDir.value = 'V';// campo validado
		document.forms[0].destRefDom.readOnly = true;// AAP02
	}// AAP02

	if (document.forms[0].checkTelDir.value == 'Y') {// AAP02
		document.forms[0].checkTelDir.value = 'V';// campo validado
		document.forms[0].desttelefono.readOnly = true;// AAP02
	}// AAP02

	if (document.forms[0].reqAcuse.value == 'N'
			&& document.forms[0].actType.value == 'ACK-C') {// AAP02
		alert('CLIENTE ORIGEN NO TIENE REQUERIMIENTOS DE ACUSE CAPTURADOS.\nFAVOR DE CAPTURAR REQUERIMIENTOS EN CATALOGO DE CLIENTES.');// AAP02
		return false;
	}
	if(document.forms[0].reqAcuseXT.value == "N" && document.forms[0].actType.value == 'ACK-X'){
		alert('CLIENTE ORIGEN NO TIENE REQUERIMIENTOS DE ACUSE XT CAPTURADOS.\nFAVOR DE CAPTURAR REQUERIMIENTOS EN CATALOGO DE CLIENTES.');// AAP02
		return false;
	}
	if (!validaValoresMaximosSEG(1)) {
		return;
	}
	if (!validaValoresMinimos()){
		return;
	}
	var continuar = false;
	if (emailValidate("eMailOrigCheck", "eMailOrigText", " en campo CLIENTE ORIGEN")) {
		if (emailValidate("eMailDestCheck", "eMailDestText", " en campo CLIENTE DESTINO")) {
			continuar = true;
		}
	}
	if(continuar){
		if (document.forms[0].shipType.value !='ST' && document.forms[0].extCheck.checked) {
			alert("NO SE PERMITE ZONA EXTENDIDA PARA EL TIPO DE ENVIO SELECCIONADO");
			return;
		}else if (document.forms[0].neededConfirmationService2D.value == 'true') {
			if (confirm(document.forms[0].msjConfirmationService.value)) {
				document.forms[0].confirmationService2D.value = true;
			} else {
				alert(document.forms[0].alertNOConfirmationService.value);//JSA01
				return false;
			}
		}
	}
	
	if (document.forms[0].flagValidRefrClnt.value == '1') {
		if (trim(document.forms[0].listReferences.value) == '') {
			alert("Favor de capturar al menos una Referencia");
			return;
		}
	}
	
	/*Validación para capturar Número de Pedimento y Agente Aduanal*/
	if (document.forms[0].pedinumber.value == "" && document.forms[0].custagent.value != "") {
		alert("Favor de capturar Número de Pedimento");
		document.forms[0].pedinumber.focus();
		return false;
	}
	
	if (continuar) {
		document.forms[0].currentTask.value = "convert";
		document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
		document.forms[0].submit();
	}
}

function logout() {
	document.forms[0].action = "login.do?logout=yes";
	document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
	document.forms[0].submit();
}

function mainpage() {
	document.forms[0].action = "mainpage.do";
	document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
	document.forms[0].submit();
}

function Regresar() {
	document.forms[0].action = "guiaConversionMonitor.do";
	document.forms[0].currentTask.value = "loadSetDetail";
	document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
	document.forms[0].submit();
}

function resetfields() {

	document.forms[0].destSite.value = "";
	document.forms[0].destSiteName.value = "";
	document.forms[0].destclave.value = "";
	document.forms[0].destnombre.value = "";
	document.forms[0].destcolonia1.value = "";
	document.forms[0].destcolonia2.value = "";
	document.forms[0].destrfc.value = "";
	document.forms[0].desttelefono.value = "";
	document.forms[0].dest1.value = "";
	document.forms[0].dest2.value = "";
	document.forms[0].content.value = "";
	document.forms[0].productDescSat.value = "";
	document.forms[0].productIdSat.value = "";
	document.forms[0].modFormnoFlag.value = "N";
	document.forms[0].msgInfo.value = "";
	document.forms[0].pedinumber.value = "";
	document.forms[0].custagent.value = "";
	if (document.forms[0].modificar.value != "Finalizar cambios") document.forms[0].modFormnoFlag.value = "N";
	if (document.forms[0].checkRefDir.value == 'Y'
			|| document.forms[0].checkRefDir.value == 'V') {// AAP03
		document.getElementById('divRefDom').style.visibility = 'hidden';// AAP03
		document.getElementById('divRefDom').style.position = 'absolute';// AAP03
		document.forms[0].destRefDom.value = '';// AAP03
		document.forms[0].checkRefDir.value = '';// AAP03
	}// AAP03

	document.forms[0].eMailOrigCheck.checked = false;// AAP04
	cleanEmail(document.forms[0].eMailOrigCheck, document.forms[0].eMailOrigText);// AAP04
	document.forms[0].eMailDestCheck.checked = false;// AAP04
	cleanEmail(document.forms[0].eMailDestCheck, document.forms[0].eMailDestText);// AAP04
}

// function focus() {
// if(<%=session.getAttribute("zoneFocus")%> == '1') {
// document.forms[0].destclave.focus();
// }
// }

function init() {
	let accionSave = document.forms[0].currentTask.value;
	let pedimentoErr = accionSave != "load" ? document.forms[0].msgInfo.value : "";
	document.forms[0].pedinumber.value = accionSave != "load" ? document.forms[0].pedinumber.value : "";
	if (document.forms[0].reLoad.value == 'Y') {
		resetfields();
		loaddetail();
	}
	tiempoEntrega();
	focus();
	validCamposZE();
	
	/* Validación de número de pedimento */
	if (document.forms[0].pedinumber.value != '' && document.forms[0].pedinumber.value == 'error' && pedimentoErr != ''){
		alert(pedimentoErr);
		document.forms[0].pedinumber.value = '';
		document.forms[0].msgInfo.value = "";
		document.forms[0].pedinumber.focus();
	}
	
	/*VERIFICA SI HAY LISTA DE REFERENCIAS PARA MOSTRAR EN PANTALLA*/
	add_all_li();
	
	/*habilita / deshabilita selección de sucursal ocurre*/
	changeDeliveryOcurre("init");
	changeSelectedAddr(document.forms[0].brnchOcurre.value);
	
	/* Valida si el usuario se encuentra modificando el rastreo */
	if (document.forms[0].modFormnoFlag.value == "Y"){
		alert("FAVOR DE TERMINAR DE MODIFICAR EL RASTREO");
		document.forms[0].modificar.value = "Modificar Documentacion";
		changeGuia();
		return;
	}

	allowRadOrgnZp();
}

/* Función para validar si permite RAD Zona Plus */
function allowRadOrgnZp(){
	if (document.forms[0].assignedBranch.value.substring(3)=="70"){
		alert("DE MOMENTO NO PUEDE DOCUMENTAR RASTREOS PREPAGO CON ORIGEN ZONA PLUS");
		document.forms[0].currentTask.value="returnToMonitor";
		document.forms[0].submit();
	}
	if (document.forms[0].currentTask.value == "returnToMonitor"){
		Regresar();
	}
}

function validCamposZE() {
	/*
	 * alert("deliveryType: "+document.forms[0].deliveryType.value+"\n"+
	 * "isExtendedZone: "+document.forms[0].isExtendedZone.value);
	 */
	if (document.forms[0].isExtendedZone.value == "S") {
		/*
		 * se habilita referencia de domicilio (solo para zona extendida,
		 * definido desde el catalogo de clientes)
		 */

		if (document.forms[0].checkRefDir.value == 'Y'
				|| document.forms[0].checkRefDir.value == 'V') {
			document.getElementById('divRefDom').style.visibility = 'visible';
			document.getElementById('divRefDom').style.position = 'static';

			if (document.forms[0].checkRefDir.value == 'V') {
				document.forms[0].destRefDom.readOnly = true;// AAP03
			}
		}

		/*
		 * se habilita telefono (solo para zona extendida, definido desde el
		 * catalogo de clientes)
		 */
		if (document.forms[0].checkTelDir.value == 'Y') {
			document.forms[0].destinationtelefono.readOnly = false;// AAP03
		} else if (document.forms[0].checkTelDir.value == 'V') {
			document.forms[0].destinationtelefono.readOnly = true;// AAP03
		}
	}
}
/* funcion para limpiar objetos texto cuando se desmarque check de email */
function cleanEmail(checkObj, txtObj) {// //AAP04
	if (checkObj.checked) {
		txtObj.readOnly = false;
	} else {
		txtObj.readOnly = true;
		txtObj.value = "";
	}
}

/* funcion para validar Emails capturados */
function echeck(str, msje) {// //AAP04
	var at = "@";
	var dot = ".";
	var lat = str.indexOf(at);
	var lstr = str.length;

	if (str.indexOf(" ") != -1) {
		alert("Formato Invalido de E-mail '" + str + "'" + msje + ". No debe llevar espacios");
		return false;
	}

	if (str.indexOf(at) == -1) {
		alert("Formato Invalido de E-mail '" + str + "'" + msje + ". Necesita un '@'.");
		return false;
	}

	if (str.indexOf(at) == -1 || str.indexOf(at) == 0
			|| str.indexOf(at) == lstr) {
		alert("Formato Invalido de E-mail '" + str + "'" + msje + ". Necesita un Nombre de Usuario.");
		return false;
	}

	if (str.indexOf(dot) == -1 || str.indexOf(dot) == 0
			|| str.indexOf(dot) == lstr) {
		alert("Formato Invalido de E-mail '" + str + "'" + msje + ". Falta nombre de Cuenta.");
		return false;
	}

	if (str.indexOf(at, (lat + 1)) != -1) {
		alert("Formato Invalido de E-mail '" + str + "'" + msje + ".");
		return false;
	}

	if (str.substring(lat - 1, lat) == dot
			|| str.substring(lat + 1, lat + 2) == dot) {
		alert("Formato Invalido de E-mail '" + str + "'" + msje + ".");
		return false;
	}

	if (str.indexOf(dot, (lat + 2)) == -1) {
		alert("Formato Invalido de E-mail '" + str + "'" + msje + ".");
		return false;
	}
	return true;
}// AAP05

/* verifica checkbox de emails para validacion de los mismos. */
function emailValidate(checkObj, txtObj, msj) {// AAP04
	var continuar = true;
	var continuaCiclo = true;

	if (eval("document.forms[0]." + checkObj).checked) {
		if (eval("document.forms[0]." + txtObj).value == "") {// AAP04
			alert("capture al menos un Email" + msj);
			eval("document.forms[0]." + txtObj).focus();
			continuar = false;
		} else {
			var at = ";";
			var lat = eval("document.forms[0]." + txtObj).value.indexOf(at);
			var eMail = "";
			var cadena = "";
			if (lat == -1) {
				eMail = eval("document.forms[0]." + txtObj).value;
				continuaCiclo = false;
				if (!echeck(eMail, msj)) {
					eval("document.forms[0]." + txtObj).focus();// AAP04
					continuar = false;// AAP04
				}
			} else {
				eMail = eval("document.forms[0]." + txtObj).value.substring(0, lat);
				cadena = eval("document.forms[0]." + txtObj).value.substring(lat + 1);
			}

			while (continuaCiclo) {
				if (!echeck(eMail, msj)) {
					eval("document.forms[0]." + txtObj).focus();// AAP04
					continuar = false;// AAP04
					continuaCiclo = false;
				} else {
					lat = cadena.indexOf(at);
					if (lat == -1) {
						eMail = cadena;
						cadena = "";
					} else {
						eMail = cadena.substring(0, lat);
						cadena = cadena.substring(lat + 1);
					}

					if (eMail == "") {
						continuaCiclo = false;
					}
				}
			}

		}// AAP04
	}
	return continuar;// AAP04
}// AAP04
function setBack(valor){
	back=valor;
}
function add_Key_li(nuevoLi) {
	if(window.event.keyCode==124) {// no permite caracter |
		event.preventDefault();
		return false;
	} else {
		if(window.event.keyCode==13) {
			add_li_submit(nuevoLi);
		}
	}		
}

function submitFormMain(accion, mensaje, params) {
	showBarraEspera('mensaje','body', mensaje);	
	document.forms[0].currentTask.value = accion;	
	document.forms[0].action = 'guiaconversion.do?'+params;
	document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
	document.forms[0].submit();
}

function add_all_li() {
	if (document.forms[0].listReferences.value.length>0){
		var fListAll = new Array();
		fListAll = (document.forms[0].listReferences.value).split('|');
        document.forms[0].listReferences.value = "";
        for(var jj=0;jj<fListAll.length;jj++){
        	add_li(fListAll[jj]);            	
    	}
	}
}

function add_li_submit(nuevoLi) {
	nuevoLi = trim(nuevoLi);
    if(nuevoLi.length>0) {
    	if (document.forms[0].flagValidRefrClnt.value == '1') {
    		if(find_li(nuevoLi)) {
    			submitFormMain('validRefrClnt','Validando Referencia','');
    		}	
    	} else {
    		add_li (nuevoLi);
    	}
    }
    return false;
}
// http://www.lawebdelprogramador.com    
/**
 * Funcion que añade un <li> dentro del <ul>
 */
function add_li(nuevoLi) {
    //var nuevoLi=document.forms[0].reference.value;
	nuevoLi = trim(nuevoLi);
    if(nuevoLi.length>0) {        	
        if(find_li(nuevoLi)) {
        	var li=document.createElement('dt');
            li.id=nuevoLi;
            li.innerHTML="<span onclick='eliminar(this)'>X</span>"+nuevoLi;
            li.className = 'td';
            //li.className = 'text';
            document.getElementById("listaDesordenada").appendChild(li);
            document.forms[0].listReferences.value = document.forms[0].listReferences.value+nuevoLi+'|';
            document.forms[0].reference.value = '';
            
            if (document.forms[0].listReferences.value!=""){
        		document.getElementById('dataRefdiv').style.visibility='visible';
    			document.getElementById('dataRefdiv').style.position='static';
    			
    			
    			var fList = new Array();
    	        fList = (document.forms[0].listReferences.value).split('|');
    			
    			if (fList.length>5) {
    				document.getElementById('dataRefdiv').style.height='80px';
    			} else {
    				var pixHeight = (60/5) * fList.length;
    				document.getElementById('dataRefdiv').style.height = pixHeight+'px';        				
    			}
        	}
        }
    }
    return false;
}

/**
 * Funcion que busca si existe ya el <li> dentrol del <ul>
 * Devuelve true si no existe.
 */
function find_li(contenido) {
    var el = document.getElementById("listaDesordenada").getElementsByTagName("dt");
    var refer = "";
    for (var i=0; i<el.length; i++) {
    	refer = el[i].innerHTML;
    	refer = refer.replace(/<\/?span[^>]*>/g,"");//elimina tag <span>
    	refer = refer.replace(/<\/?SPAN[^>]*>/g,"");//elimina tag <SPAN>
        refer =  refer.substring(1);//elimina X
        //if(el[i].innerHTML == ('<span onclick="eliminar(this)">X</span>'+contenido)){
        if(refer == contenido){
        	alert('Ya existe en la lista referencia: '+contenido);
        	return false;
        }
            
    }
    return true;
}

/**
 * Funcion para eliminar los elementos
 * Tiene que recibir el elemento pulsado
 */
function eliminar(elemento) {
	
    var id=elemento.parentNode.getAttribute("id");
    node=document.getElementById(id);
    
    var refer = document.getElementById(id).innerHTML;        
    
    refer = refer.replace(/<\/?span[^>]*>/g,"");
    refer = refer.replace(/<\/?SPAN[^>]*>/g,"");
    refer =  refer.substring(1);
    
    if (document.forms[0].flagValidRefrClnt.value == '1') {
    	var fListVal = new Array();
    	fListVal = (document.forms[0].listReferences.value).split('|');            
    	
    	if (fListVal.length>2) {
    		var cont = 0;
        	var continuar = true;
    		for(jj=0;jj<fListVal.length;jj++){
        		if(fListVal[jj]==refer) {
        			if (jj==0) {
        				alert('La referencia no puede ser eliminada. Solamente eliminando el resto de referencias podra ser eliminada.');
        				continuar = false;
        				break;
        			}
        		}
        	}
        	
            if (!continuar) {
            	return continuar;
            }	
    	}            
    }        
    
    node.parentNode.removeChild(node);
    
    var fList = new Array();
    fList = (document.forms[0].listReferences.value).split('|');
    
    for(j=0;j<fList.length;j++){
		if(fList[j]==refer)
			fList[j]="";
	}
 // prepare the Fianl List
    document.forms[0].listReferences.value="";
	for(k=0;k<fList.length;k++){
		if(fList[k]!="")						
			document.forms[0].listReferences.value=document.forms[0].listReferences.value+fList[k]+"|";						
	}
	//alert("listReferences "+document.forms[0].listReferences.value);
	
	if (document.forms[0].listReferences.value==""){
		document.getElementById('dataRefdiv').style.visibility='hidden';
		document.getElementById('dataRefdiv').style.position='absolute';
	}
	
	if (fList.length>5) {
		document.getElementById('dataRefdiv').style.height='80px';
	} else {
		var pixHeight = (60/5) * fList.length;
		document.getElementById('dataRefdiv').style.height = pixHeight+'px';
	}
}
/*function scrollWindow() {
	window.scrollBy(0,screen.availHeight);
}*/

//Abre la ventana para eliminar guias seleccionadas en el onUnload de los jsp
function clearGuias() {
	
	if(document.forms[0].banCerrar.value !='false') {
		if (document.forms[0].clickedItems.value!=''){
			var t = 'currentTask=clearGuias'
				+'&clickedItems='+document.forms[0].clickedItems.value
				+'&idSetSel='+document.forms[0].idSetSel.value;

			//window.open('../AccionOS/REGSERVADIBLOQS.do?'+t,'_blank','menubar=no,location=no,toolbar=no,status=no,scrollbars=no,resizable=no,width=680,height=335 top=105, left=225');
			//alert('guiaConversionMonitor.do?'+t);
			window.open('guiaConversionMonitor.do?'+t,'_blank');	
		}
	}
}

// Funcion para impedir que refresque la pantalla con la tecla F5
document.onkeydown = function() {
	if (window.event && window.event.keyCode >= 112
			&& window.event.keyCode <= 123) {
		window.event.keyCode = 505;
	}
	if (window.event && window.event.keyCode == 505) {
		return false;
	}
}

function validaValoresMaximosSEG(tipo) {
	value = new Number(document.forms[0].volL.value);
	valueMAX = new Number(tipo == 1 ? document.forms[0].volLMAX.value : document.forms[0].volLMAX.value);
	vacioVolL = 'N', vacioVolH = 'N', vacioVolW = 'N';
	cadenaVolL = '', cadenaVolH = '', cadenaVolW = '';
	if (value > valueMAX) {
		if(tipo == 1 || document.forms[0].shipType.value != 'ST') {//VALIDACION DE SOBRES
			alert("El valor de lo largo sobrepasa el valor maximo que es: " + valueMAX);
			document.forms[0].volL.focus();
			return false;
		}
	}
	if (document.forms[0].volL.value == "" || value == 0) {
		cadenaVolL = "Favor de ingresar un valor mayor a 0 en lo largo.";
		if(document.forms[0].shipType.value != 'ST') {//VALIDACION DE SOBRES
			alert(cadenaVolL);
			document.forms[0].volL.focus();
			return false;
		} else {
			vacioVolL = 'Y';
		}
	}
	value = new Number(document.forms[0].volH.value);
	valueMAX = new Number(tipo == 1 ? document.forms[0].volHMAX.value : document.forms[0].volHMAX.value);
	if (value > valueMAX) {
		if(tipo == 1 || document.forms[0].shipType.value != 'ST') {//VALIDACION DE SOBRES
			alert("El valor del alto sobrepasa el valor maximo que es: " + valueMAX);
			document.forms[0].volH.focus();
			return false;
		}
	}
	if (document.forms[0].volH.value == "" || value == 0) {
		cadenaVolH = "Favor de ingresar un valor mayor a 0 en lo alto.";
		if(document.forms[0].shipType.value != 'ST') {//VALIDACION DE SOBRES
			alert(cadenaVolH);
			document.forms[0].volH.focus();
			return false;
		} else {
			vacioVolH = 'Y';
		}
	}
	value = new Number(document.forms[0].volW.value);
	valueMAX = new Number(tipo == 1 ? document.forms[0].volWMAX.value : document.forms[0].volWMAX.value);
	if (value > valueMAX) { 
		if(tipo == 1 || document.forms[0].shipType.value != 'ST') {//VALIDACION DE SOBRES
			alert("El valor del ancho sobrepasa el valor maximo que es: " + valueMAX);
			document.forms[0].volW.focus();
			return false;
		}
	}
	if (document.forms[0].volW.value == "" || value == 0) {
		cadenaVolW = "Favor de ingresar un valor mayor a 0 en lo ancho.";
		if(document.forms[0].shipType.value != 'ST') {//VALIDACION DE SOBRES
			alert(cadenaVolW);
			document.forms[0].volW.focus();
			return false;
		} else {
			vacioVolW = 'Y';
		}
	}

	//VALIDA CAJAS Y NO ERES SERVICIO EXPRESS
	if(document.forms[0].shipType.value == 'ST') {
		if(!(vacioVolL == 'Y' && vacioVolH == 'Y' && vacioVolW == 'Y')) {
			if(vacioVolL == 'Y') {
				alert(cadenaVolL);
				document.forms[0].volL.focus();
				return false;
			}
			if(vacioVolW == 'Y') {
				alert(cadenaVolW);
				document.forms[0].volW.focus();
				return false;
			}
			if(vacioVolH == 'Y') {
				alert(cadenaVolH);
				document.forms[0].volH.focus();
				return false;
			}
		}
	}
	if (document.forms[0].shipType.value != 'ST') {
		value = new Number(document.forms[0].weight.value);
		if (document.forms[0].weight.value == "" || value == 0) {
			alert("Favor de ingresar un valor mayor a 0 en el peso.");
			document.forms[0].weight.focus();
			return false;
		}
	}
	return true;
 }

function validaValoresMinimos() {
	
	 if (!medidasCapturadas()){
		 return true;
	 }
	
	 //Current values
	 const long =  Number(document.forms[0].volL.value);
	 const width =  Number(document.forms[0].volW.value);
	 const height =  Number(document.forms[0].volH.value);
	 const weight =  Number(document.forms[0].weight.value);
	 const volume =  Number(document.forms[0].volume.value);
	 
	 //Mininum values
	 const minLong =  Number(document.forms[0].volLMin.value);
	 const minWidth =  Number(document.forms[0].volWMin.value);
	 const minHeight =  Number(document.forms[0].volHMin.value);
	 const minWeight =  Number(document.forms[0].wghtMin.value);
	 const minVol =  Number(document.forms[0].volMin.value);
	 
	 // alert messages
	 const alerts = [];

	 if (weight < minWeight) alerts.push(`Peso: ${minWeight} kg`);
	 if (long < minLong) alerts.push(`Largo: ${minLong} cm`);
	 if (height < minHeight) alerts.push(`Alto: ${minHeight} cm`);
	 if (width < minWidth) alerts.push(`Ancho: ${minWidth} cm`);
	 if (volume < minVol) alerts.push(`Volumen: ${minVol}`);

	 // Display compact alert message
	 if (alerts.length > 0) {
		 const message = `Medidas m\u00EDnimas permitidas:\n ${alerts.join("\n")}`;
		 showToast("info", message, 6000);
		 document.forms[0].volL.focus();
		 return false;
	 }
	 return true;
}

function medidasCapturadas(){
	 return document.forms[0].volL.value != "" &&
	     document.forms[0].volW.value != "" &&
	     document.forms[0].volH.value != "" &&
	     document.forms[0].weight.value != "" &&
	     document.forms[0].volume.value != "";
}

function calcVolWeight() {
	var lengthValue = document.forms[0].volL.value;
	var widthValue = document.forms[0].volW.value;
	var heightValue = document.forms[0].volH.value;
	var weightDivisor = document.forms[0].factorDividorPesoVol.value;

	var l = parseFloat(lengthValue);
	var w = parseFloat(widthValue);
	var h = parseFloat(heightValue);

	var errorL = isNaN(l);
	var errorW = isNaN(w);
	var errorH = isNaN(h);
	if (errorL) {
		l = 0;
	}
	if (errorW) {
		w = 0;
	}
	if (errorH) {
		h = 0;
	}
	var calcVolumeTMP = (l/100) * (w/100) * (h/100);
	var calcVolWeightTMP = (l * w * h) / weightDivisor;
	calcVolWeightTMP = calcVolWeightTMP.toFixed(document.forms[0].cantPesoVolDecimales.value);
	calcVolumeTMP = calcVolumeTMP.toFixed(document.forms[0].cantPesoVolDecimales.value);
    var calcVolWeight = calcVolWeightTMP;
    var calcVolume = calcVolumeTMP;
    if (calcVolWeightTMP.toString().indexOf(".") >= 0) {
        var partInt = calcVolWeightTMP.toString().split(".")[0];
    	var partVolWeight = calcVolWeightTMP.toString().split(".")[1];
    	calcVolWeight = partInt+"."+partVolWeight.substr(0, document.forms[0].cantPesoVolDecimales.value);
    }
    if (calcVolume.toString().indexOf(".") >= 0) {
        var partIntVol = calcVolume.toString().split(".")[0];
    	var partVol = calcVolume.toString().split(".")[1];
    	calcVolume = partIntVol+"."+partVol.substr(0, document.forms[0].cantPesoVolDecimales.value);
    }
	//document.forms[0].weightVolumetric.value = calcVolWeight;
	if(!(l == 0 && w == 0 &&  h == 0)) {
		document.forms[0].volume.value = calcVolume;
	}else {
		document.forms[0].volume.value = document.forms[0].volumeOriginal.value ;
	}
	/*if(parseFloat(calcVolume) == 0) {
		document.forms[0].tarifa.value = "";
		document.forms[0].peso.value = "";
		document.forms[0].peso.readOnly = false;
		//document.forms[0].volumen.readOnly = true;
	}*/
	
 }

function clearDestData(){
	document.forms[0].destclave.value="";
	document.forms[0].destnombre.value="";
	document.forms[0].destClaveAux.value="";
	document.forms[0].destNombreAux.value="";
	clearDestData2();	
}

function clearDestData2() {
	document.forms[0].dest1.value="";
	document.forms[0].dest2.value="";
	document.forms[0].destcolonia1.value="";
	document.forms[0].destcolonia2.value="";
	document.forms[0].desttelefono.value="";
	document.forms[0].brncVrtl.value = "";
	document.forms[0].deliveryType.selectedIndex = -1;
	document.forms[0].deliveryType.value = "";
}