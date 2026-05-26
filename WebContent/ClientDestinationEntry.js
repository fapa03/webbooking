/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creaci�n: 15/11/2012
 * Compa��a: KUMARAN.
 * Descripci�n del programa: js para validaciones y procesos de registro CLIENTE DESTINO.
 * FileName	:	ClientDestinationEntry.js
 * FormBean	:	ClientDestinationEntry.class
 * ActionBean	:	ClientDestinationEntry.class
 * OtherClasses	:	none
 * CSS FileName	:	webbooking.css
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP03
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 02/07/2013
 * Descripci�n:  Se agregaron validaciones para rfc y tipo de cliente	
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n:  
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 	
 * ------------------------------------------------------------------ 
 */
function formreset() {
	document.forms[0].nombre.value = '';
	document.forms[0].telefono.value = '';
	document.forms[0].celular.value = '';
	document.forms[0].calle.value = '';
	document.forms[0].numero.value = '';
	document.forms[0].codigoCliente.value = '';
	document.forms[0].codigoInt.value = '';
	document.forms[0].email.value = '';
	document.forms[0].assignedToBranch.value = '';
	document.forms[0].assignedToBranchRef.value = '';
	document.forms[0].estado.value = '';
	document.forms[0].municipio.value = '';
	document.forms[0].zona.value = '';
	document.forms[0].pais.value = '';
	document.forms[0].colonia.value = '';
	document.forms[0].ciudad.value = '';
	document.forms[0].codigoPostal.value = '';
	document.forms[0].sucursal.value = '';
	document.forms[0].errorMessages.value = '';
	document.forms[0].rfc1.value = '';//AAP03
	document.forms[0].rfc2.value = '';//AAP03
	document.forms[0].rfc3.value = '';//AAP03
	document.forms[0].tipoCliente[0].selected = true;//AAP03
	document.forms[0].regimenFiscalDes.value = '';
	document.forms[0].regimenFiscalId.value = '';
	document.forms[0].usoCfdiDes.value = '';
	document.forms[0].usoCfdiId.value = '';
	document.forms[0].validRFC.value = 'N';
	document.forms[0].validFiscal.value = 'N';
	document.forms[0].validData.checked = false;
	
	if (document.forms[0].ead.checked == true) {
		document.forms[0].ead.checked = false;
	}
	if (document.forms[0].eadCheck.value == 'Y') {
		document.forms[0].eadCheck.value = 'N';
	}
	
	document.forms[0].bloqueaCodCliente.value = "N";
	document.forms[0].codigoCliente.readOnly = false;
	document.forms[0].guardar.value = "Insertar";
}

/**
 * Trims the preceeding and leading spaces.
 * 
 * @param tmp objects value
 * 
 * Usage: b = trimString(a.value);
 */
function trimString(tmp) {
	if (tmp == '') {
		return tmp;
	}
		
	var strlength = tmp.length;
	var trimstr = '';
	var startindex = 0;
	var endindex = strlength;
	for ( var index = 0; index < strlength; index++) {
		if (tmp.charAt(index) != ' ') {
			startindex = index;
			break;
		}
	}
	for ( var index = strlength - 1; index >= 0; index--) {
		if (tmp.charAt(index) != ' ') {
			endindex = index;
			break;
		}
	}
	if (startindex == 0 && endindex == strlength) {
		trimstr = '';
	} else {
		trimstr = tmp.substring(startindex, endindex + 1);
	}		
	return trimstr;
}

var lovWindow = null;
function openLov(str) {
	// to get Postal code
	postalLevel = document.forms[0].level.value;
	postalCode = document.forms[0].code.value;
	postalType = document.forms[0].type.value;
	
		// check Assigned To Branch lov
	if (str == 'assignedToBranch') {
		window.open('lov.do?type=' + str, 'abc', 'width=400,height=200,top=210,left=350,screenY=185,screenX=200');
		abrirVentana('lov.do?type=' + str, 'abc', 400, 200);
		// check cities lov
	} else if (str == 'cities') {
		// Added By Sam [08-06-2006]
		if (document.forms[0].sucursal.value == "") {
			alert('SYS-800067 Sucursal Asignada es obligatoria, por favor proporcione un valor..');			
			return false;
		}
		// this is for sucursal id to give into cities to fetch the record
		var dest = document.forms[0].sucursal.value;
		window.open('lov.do?type=' + str + '&sucursal=' + dest, 'abc', 'width=430,height=200,top=210,left=350,screenY=185,screenX=200');
		// check postal lov
	} else if (str == 'postal') {
		postalLevel = '4';

		var paramZipCode = '';
		if (document.forms[0].codigoPostal.value!=''){
			paramZipCode = '&find='+document.forms[0].codigoPostal.value;
		}
		lovWindow = abrirVentana('zipCodes.do?currentTask=start' + paramZipCode, 'abc', 1000, 435);
		
	} else if (str == 'regClientDest'){
		if (document.forms[0].nombre.value == "") {
			alert('SYS-800067 Capture un criterio de busqueda para nombre');
			return false;
		}
		var nombreDest = document.forms[0].nombre.value;
		abrirVentana('lov.do?type=' + str + '&nombreDest=' + nombreDest, 'abc', 600, 200);
	}  else if (str == 'regimFiscal'){
		var rfc1 = document.forms[0].rfc1.value.toUpperCase();
		var rfc2 = document.forms[0].rfc2.value.toUpperCase();
		var rfc3 = document.forms[0].rfc3.value.toUpperCase();
		var clntType = document.forms[0].tipoCliente.value;
		if (rfc1 == '' || rfc2 == '' || rfc3 == ''){
			alert("SYS-800067 Capture el RFC completo");
			return false;
		}
		/* Si RFC es generico, los campos se actualizan solos */
		if ((rfc1 == "XAXX" && rfc2 == "010101" && rfc3 == "000") || document.forms[0].validRFC.value == 'N'){
			return false;
		}
		abrirVentana('lov.do?type=' + str+ '&rfc='+rfc1+rfc2+rfc3+'&clntType='+clntType, 'Catalogo Regimen Fiscal', 600, 206);
	} else if (str == 'usoCfdi'){
		rfc1 = document.forms[0].rfc1.value.toUpperCase();
		rfc2 = document.forms[0].rfc2.value.toUpperCase();
		rfc3 = document.forms[0].rfc3.value.toUpperCase();
		var regimFiscalId = document.forms[0].regimenFiscalId.value;
		if (rfc1 == '' || rfc2 == '' || rfc3 == ''){
			alert("SYS-800067 Capture el RFC completo");
			return false;
		}
		/* Si RFC es generico, los campos se actualizan solos */
		if ((rfc1 == "XAXX" && rfc2 == "010101" && rfc3 == "000") || document.forms[0].validRFC.value == 'N'){
			return false;
		}
		abrirVentana('lov.do?type=' + str+ '&rfc='+rfc1+rfc2+rfc3 +'&regimFiscalId='+regimFiscalId, 'Catalogo uso CFDI', 600, 206);
	} else if (str=='datosFiscalesForm'){
		rfc1 = document.forms[0].rfc1.value.toUpperCase();
		rfc2 = document.forms[0].rfc2.value.toUpperCase();
		rfc3 = document.forms[0].rfc3.value.toUpperCase();
		
		var tipoCliente = document.forms[0].tipoCliente.value;
		if (rfc1 == '' || rfc2 == '' || rfc3 == ''){
			alert("SYS-800067 Capture el RFC completo");
			return false;
		}
		document.forms[0].openFiscalForm.value ="Y";
		abrirVentana('lov.do?type=' + str+ '&rfc='+rfc1+rfc2+rfc3+'&tipoCliente='+tipoCliente, 'Captura de Datos Fiscales', 643, 594);
	}
}

function getCoordenadas(xWidth, xHeight) {
	return 'dependent=yes, width='+xWidth+', height='+xHeight+', top='+((screen.availHeight - xHeight) / 2)+', left='+((screen.availWidth - xWidth) / 2);
}



//Abre un ventana centrada
//ejemplo de uso:  abrirVentana('aaa.jsp?llave=01', 'ventanax', 450, 200);
function abrirVentana(url, xWindow, xWidth, xHeight) {
	return window.open(url, xWindow, getCoordenadas(xWidth, xHeight));
}

function openLovNombreKey(str) {
	if(window.event.keyCode==13) {
		openLov(str);
	}
}
// submit the Destination Client Details and back to the Main Menu Screen
function saveAndBack(modo) {
	if (modo == "main_page") {
		if (document.forms[0].fastClntCap.value == "true"){
			window.close();
			return false;
		}
		showBarraEspera('mensaje','body','Espere por favor...validando Informacion');
		document.forms[0].mode.value = modo;
		document.forms[0].submit();
		return;
	}
	let nombreDest = document.forms[0].nombre.value;
	var rfc = (document.forms[0].rfc1.value + document.forms[0].rfc2.value + document.forms[0].rfc3.value).toUpperCase();
	if (nombreDest.length < 2){
		alert('SYS-800067 Capture al menos 2 caracteres en el nombre')
		return false;
	}
	if (!document.forms[0].codigoCliente.readOnly && document.forms[0].codigoCliente.value !="") {
		alert('La aplicaci\u00F3n esta en modo insertar. Se generar\u00E1 un consecutivo autom\u00E1tico para el numero de cliente Paquetexpress ');
		document.forms[0].codigoCliente.value = "";
	}
	if (document.forms[0].validRFC.value == 'N' && !validRfcSat()){
		if (document.forms[0].validRFC.value == 'N' && !notValidRfc()){
			document.forms[0].rfc1.focus();
			document.forms[0].errorMessages.value = "El RFC ingresado es invalido, favor de ingresar uno correcto. Desea asignar el RFC generico (XAXX010101000)?";
			return false;
		}
	}
	if (document.forms[0].tieneCredito == "N"){
		if (document.forms[0].regimenFiscalId.value == ""){
			alert('SYS-800067 Capture Regimen Fiscal');
			document.forms[0].regimenFiscalDes.focus();
			return false;
		}
		if (document.forms[0].usoCfdiId.value == ""){
			alert('SYS-800067 Capture USO CFDI');
			document.forms[0].usoCfdiDes.focus();
			return false;
		}
	}
	
	let currRFC = document.forms[0].currRFC.value.replaceAll("-","");
	if (rfc != "XAXX010101000"){
		if (!isValidRFC(rfc)){
			return false;
		}
	}
	
	if (rfc != currRFC && document.forms[0].openFiscalForm.value == "N" && rfc != "XAXX010101000"){
		document.forms[0].errorMessages.value = "Cancelar para capturar los Datos Fiscales o Aceptar para continuar con RFC PUBLICO EN GENERAL(XAXX-010101-000)";
		let datosFiscalesOrgn = JSON.parse(document.forms[0].datosFiscalesTmpString.value);
		//let datosFiscalesNew = JSON.parse(document.forms[0].datosFiscalesString.value);
		if (datosFiscalesOrgn.zipCode == ""){ //&& datosFiscalesNew.zipCode == ""){
			if (confirm(document.forms[0].errorMessages.value)){
				document.forms[0].rfc1.value = "XAXX";
				document.forms[0].rfc2.value = "010101";
				document.forms[0].rfc3.value = "000";
				rfc = document.forms[0].rfc1.value + document.forms[0].rfc2.value + document.forms[0].rfc3.value;
				document.forms[0].genericRFC.value = "Y";
				document.forms[0].tipoCliente.value = "I";
				document.forms[0].errorMessages.value = "Se asigno el RFC generico";
				document.forms[0].guardar.focus();
				document.getElementById("btnDatosFiscales").style.display = "none";
				return true;
			}else{
				document.forms[0].rfc1.focus();
				return false;
			}
		}
		
		let completeOrValid = valCompleteFiscalData(document.forms[0].datosFiscalesTmpString.value,
							document.forms[0].datosFiscalesString.value);
		if (!completeOrValid){
			alert("Favor de capturar datos fiscales");
			return false;
		}
	}
	
	document.forms[0].mode.value = modo;
	hacerUpperCase();
	if (document.forms[0].validRFC.value == 'Y' || isValidRFC(rfc)) {//AAP03
		showBarraEspera('mensaje','body','Espere por favor...validando Informacion');
		document.forms[0].submit();	
	}//AAP03	
}

function isValidRFC(rfc) {
	  // Regular expressions for each part of the RFC
	  let regex1 = /^[A-Z\u00D1\&]{3,4}$/;
	  let regex2 = /\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])/;
	  let regex3 = /^[A-Z0-9]{3}$/;
	  let clientType = document.forms[0].tipoCliente.value;
	  let errorMsg = "";
	  let result;
	  
	  //Validate client type and rfc type
	  if (clientType == 'I' && rfc.length != 13){
		  errorMsg = 'EL CAMPO DE INICIALES DEL RFC DEBE TENER UNA LONGITUD DE 4 CARACTERES SIN NUMEROS.\n';
		  document.forms[0].rfc1.focus();
	  }
	  
	  if (clientType != 'I' && rfc.length != 12){
		  errorMsg = 'EL CAMPO DE INICIALES DEL RFC DEBE TENER UNA LONGITUD DE 3 CARACTERES SIN NUMEROS.\n';
		  document.forms[0].rfc1.focus();
	  }
	  
	  // Extract the different parts of the RFC
	  let part1 = document.forms[0].rfc1.value;
	  let part2 = document.forms[0].rfc2.value;
	  let part3 = document.forms[0].rfc3.value;
	  part1 = part1.toUpperCase();
	  part2 = part2.toUpperCase();
	  part3 = part3.toUpperCase();
	  
	  // Validate each part using the corresponding regular expression
	  let isValidPart1 = regex1.test(part1);
	  let isValidPart2 = regex2.test(part2);
	  let isValidPart3 = regex3.test(part3);

	  // Validate the date portion of the RFC using the isValidRFCDate function
	  let year = parseInt(part2.substr(0, 2), 10);
	  let month = parseInt(part2.substr(2, 2), 10);
	  let day = parseInt(part2.substr(4, 2), 10);
	  let isValidDate = isValidRFCDate(year, month, day);
	  
	  if (!isValidPart1){
		  let len = clientType == 'I' ? 4 : 3;
		  errorMsg = 'EL CAMPO DE INICIALES DEL RFC DEBE TENER UNA LONGITUD DE '+len+' CARACTERES SIN NUMEROS.\n';
		  document.forms[0].rfc1.focus();
	  }
	  
	  if (!isValidDate || !isValidPart2){
		  errorMsg += 'LA FECHA NO ES VALIDA.\n';
	  }
	  
	  if (!isValidPart3){
		  errorMsg += 'LA HOMOCLAVE DEL RFC DEBE TENER 3 CARACTERES/DIGITOS SIN CARACTERES ESPECIALES.';
		  document.forms[0].rfc2.focus();
	  }
	  if (isValidPart1 && isValidPart2 && isValidPart3 && isValidDate && errorMsg == ''){
		  result = true;
	  }else{
		  errorMsg = 'RFC Invalido: ' + errorMsg;
		  alert(errorMsg);
		  result = false;
	  }
	  // Return true if all parts are valid, false otherwise
	  return result;
	}

function isValidRFCDate(year, month, day) {
	  const date = new Date(year+2000, month - 1, day);
	  return date.getFullYear() === year+2000 &&
	    date.getMonth() === month - 1 &&
	    date.getDate() === day;
	}



function hacerUpperCase() {
	document.forms[0].codigoInt.value = document.forms[0].codigoInt.value.toUpperCase();
	document.forms[0].nombre.value = document.forms[0].nombre.value.toUpperCase();
	document.forms[0].calle.value = document.forms[0].calle.value.toUpperCase();
	document.forms[0].numero.value = document.forms[0].numero.value.toUpperCase();
}
// To get the branch Name
function getBranchName() {
	//document.forms[0].mode.value = "getProcedure";
	document.forms[0].mode.value = "getDescBranch";
	showBarraEspera('mensaje','body','Espere por favor...Cargando nombre de sucursal');
	document.forms[0].submit();
}

//EAD check box
function eadVerify() {
	if (document.forms[0].ead.checked == true) {
		document.forms[0].eadCheck.value = 'Y';
	} else {
		document.forms[0].eadCheck.value = 'N';
	}
}
function loadDetailKey() {
	if(window.event.keyCode==13) {
		loaddetail();
	}
}
function loaddetail() {
	if (document.forms[0].codigoCliente.value == "") {
		if (document.forms[0].codigoInt.value == "") {
			alert('Capture codigo de cliente o codigo Interno para realizar busqueda');
			document.forms[0].codigoCliente.focus();
			return false;
		}
	}
	showBarraEspera('mensaje','body','Espere por favor...Cargando detalles');
	document.forms[0].action = "clientDestinationEntry.do?mode=loaddetail";
	document.forms[0].submit();
}

function onLoad() {	
	
	
	ocultarBarraEspera('mensaje','body');
	if (document.forms[0].ead.value == "Y") {
		document.forms[0].ead.checked = true;
	} else if (document.forms[0].eadCheck.value == "Y") {
		document.forms[0].ead.checked = true;
	}
	if (document.forms[0].mode.value == 'postalDetails') {
		document.forms[0].estado.focus();
	}
	
	if (document.forms[0].mode.value == "") {
		document.forms[0].estado.readOnly = true;
		document.forms[0].municipio.readOnly = true;
		document.forms[0].zona.readOnly = true;
		document.forms[0].pais.readOnly = true;
		document.forms[0].colonia.readOnly = true;
		document.forms[0].nombre.focus();
	} else if (document.forms[0].mode.value == "loaddetail") {
		if (document.forms[0].nombre.value != "") {
			document.forms[0].bloqueaCodCliente.value = "Y";
		}
		
	} else if (document.forms[0].mode.value == "getProcedure") {
		document.forms[0].telefono.focus();
	} else if (document.forms[0].mode.value == "postalDetails") {
		document.forms[0].ead.focus();
	} else if (document.forms[0].mode.value == "saveAndBack") {
		if (document.forms[0].errorMessages.value == "") {
			let toastMessage = "Cliente "+document.forms[0].nombre.value+" registrado exitosamente!";
			showToast("default",toastMessage, 6000);
			if (document.forms[0].fastClntCap.value == "true"){
				let lovWindow = "";
				try{
					//Viene de documentación WW
					window.opener.document.forms[0].destinationnombre.value = document.forms[0].nombre.value;
					lovWindow = 'clientNew';
				}catch(e){
					//Viene de documentación PP Cajas
					window.opener.document.forms[0].destnombre.value = document.forms[0].nombre.value;
					lovWindow = 'clientwithsite';
				}
				 window.close();
			}
			formreset();
		}else{
			let errorMsgStart = document.forms[0].errorMessages.value.substring(0,27);
			let errorMsg = document.forms[0].errorMessages.value.substring(27);
			try {
				document.forms[0].errorMessages.value = errorMsgStart + decodeURIComponent(escape(errorMsg));
			} catch (e) {
				document.forms[0].errorMessages.value = errorMsgStart + decodeURIComponent(unescape(errorMsg));
			}
			document.forms[0].errorMessages.value = document.forms[0].errorMessages.value.replaceAll(".",".\n");
			if (document.forms[0].validFiscal.value == "N"){
				let rfc = document.forms[0].rfc1.value+document.forms[0].rfc2.value+document.forms[0].rfc3.value;
				if (confirm(document.forms[0].errorMessages.value+
						"\nPor errores en Datos fiscales capturados se asign\u00F3 temporalmente el RFC P\u00DABLICO EN GENERAL (XAXX-010101-000). \nCancelar para actualizar Datos Fiscales y mantener el RFC "+rfc+" o Aceptar para continuar.")){
					let toastMessage = "Cliente "+document.forms[0].nombre.value+" registrado exitosamente.";
					showToast("warning",toastMessage, null);
					formreset();
				}else{
					document.forms[0].codigoCliente.value =  document.forms[0].codigoClienteHid.value;
					document.forms[0].guardar.value = "Actualizar";
					document.forms[0].bloqueaCodCliente.value = "Y";
					if (document.forms[0].rfc1.value.length > 3){
						document.forms[0].tipoCliente.value = 'I';
					}else{
						document.forms[0].tipoCliente.value = 'C';
					}
				}
			}else{
				notValidRfc();
				document.forms[0].errorMessages.value = "El RFC ingresado es invalido, favor de ingresar uno correcto. Desea asignar el RFC generico (XAXX010101000)?";
				document.forms[0].rfc1.focus();
			}
		}		
	} else if(document.forms[0].mode.value == "invalidRfc") {
		notValidRfc();
		document.forms[0].errorMessages.value = "El RFC ingresado es invalido, favor de ingresar uno correcto. Desea asignar el RFC generico (XAXX010101000)?";
		document.forms[0].rfc1.focus();
	} else if(document.forms[0].mode.value == "validateRFC" &&
			document.forms[0].validRFC.value == "Y") {
	} else if(document.forms[0].mode.value == "validateRFC" &&
			document.forms[0].validRFC.value == "N") {
		document.forms[0].rfc1.focus();
	}
	
	if (document.forms[0].bloqueaCodCliente.value == "Y") {
		document.forms[0].codigoCliente.readOnly = true;
		document.forms[0].guardar.value = "Actualizar";
	} else {
		document.forms[0].guardar.value = "Insertar";
	}
	var rfc = document.forms[0].rfc1.value+document.forms[0].rfc2.value+document.forms[0].rfc3.value;
	rfc = rfc.toString().toUpperCase();
	if (rfc != "XAXX010101000"){
		if (document.forms[0].validFiscal.value == "Y"){
			 document.getElementById("validData").removeAttribute("disabled");
			 document.getElementById("validData").setAttribute("checked","checked");
			 document.getElementById("validData").setAttribute("disabled","disabled");   
		 }
		document.getElementById("btnDatosFiscales").style.display = "block";
	}else{
		document.getElementById("btnDatosFiscales").style.display = "none";
	}
	if (document.forms[0].fastClntCap.value == "true"){
		const params = new URLSearchParams(window.location.search);
		const callType = params.get('type');
		if (callType != '' && callType == "cliententry"){
			validRfcSat();
		}
	}
}

function notValidRfc(){
	document.forms[0].bloqueaCodCliente.value = "Y"
	document.forms[0].codigoCliente.readOnly = true;
	var rfc = document.forms[0].rfc1.value.toUpperCase() + document.forms[0].rfc2.value + document.forms[0].rfc3.value;
	if (document.forms[0].validRFC.value == 'N'&& rfc != "XAXX010101000"){
		if (document.forms[0].errorMessages.value == ""){
			document.forms[0].errorMessages.value = "El RFC ingresado es invalido, favor de ingresar uno correcto. Desea asignar el RFC generico (XAXX010101000)?";
		}else if (document.forms[0].errorMessages.value.includes("El RFC ingresado") &&
				!document.forms[0].errorMessages.value.includes("RFC generico")){
			document.forms[0].errorMessages.value = document.forms[0].errorMessages.value+" Desea asignar el RFC generico (XAXX010101000)?";
		}
		
		if (confirm(document.forms[0].errorMessages.value)){
			document.forms[0].rfc1.value = "XAXX";
			document.forms[0].rfc2.value = "010101";
			document.forms[0].rfc3.value = "000";
			document.forms[0].genericRFC.value = "Y";
			document.forms[0].errorMessages.value = "Se asigno el RFC generico";
			document.forms[0].guardar.focus();
			return true;
		}else{
			document.forms[0].rfc1.focus();
			return false;
		}
	}
}

// to get Postal details
function getPostalDetails() {
	if (document.forms[0].pDetailsC.value == "1") {
		document.forms[0].mode.value = "postalDetails";
		showBarraEspera('mensaje','body','Espere por favor...Cargando Informacion de Codigo Postal');
		document.forms[0].submit();
	}
}

function valZipCodeData(str) {
	if(window.event.keyCode==13) {
		if (document.forms[0].codigoPostal.value != '' && document.forms[0].codigoPostal.value.length<5) {
			alert('Codigo postal invalido');
			return false;
		}
		openLov(str);
	}	
}
//se detona desde el catalogo de codigos postales
function setValSelZipCode() {
	
	document.forms[0].codigoPostal.value = lovWindow.document.forms[0].zipCodeSel.value;
	document.forms[0].colonia.value = lovWindow.document.forms[0].colDesSel.value;
	document.forms[0].geCode.value = lovWindow.document.forms[0].colCodSel.value;
	document.forms[0].geLevl.value = lovWindow.document.forms[0].colLevSel.value;
	document.forms[0].geType.value = lovWindow.document.forms[0].colTypeSel.value;
	
	document.forms[0].ciudad.value = lovWindow.document.forms[0].ciuDesSel.value;	
	document.forms[0].assignedToBranch.value = lovWindow.document.forms[0].idSucSel.value;
	document.forms[0].sucursal.value = lovWindow.document.forms[0].idSucSel.value;
	document.forms[0].estado.value = lovWindow.document.forms[0].edoDesSel.value;
	document.forms[0].municipio.value = lovWindow.document.forms[0].munDesSel.value;
	document.forms[0].zona.value = lovWindow.document.forms[0].zonaDesSel.value;
	document.forms[0].pais.value = lovWindow.document.forms[0].paisDesSel.value;
	
	getBranchName();
}
// main menu on top of the screen
function goOut(page) {
	if (page == "guiabooking") {
		page = "thispage";
	}
		
	if (confirm("DESEA PERDER LA INFORMACION?")) {
		showBarraEspera('mensaje','body','Espere por favor...Saliendo de Registro de Clientes');
		if (page=='shipmenthistory') {//AAP03
			document.forms[0].action="shipmentHistory.do";//AAP03
		} else {//AAP03
			document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page="+page;//AAP03	
		}//AAP03
		document.forms[0].submit();
	} else {
		return;
	}
}

function echeck(str) {
	var at = "@";
	var dot = ".";
	var lat = str.indexOf(at);
	var lstr = str.length;
	
	if (str.indexOf(" ") != -1) {
		alert("Formato Invalido de E-mail. No debe llevar espacios. \""+str+"\"");
		return false;
	}
	
	if (str.indexOf(at) == -1) {
		alert("Formato Invalido de E-mail. Necesita un '@'. \""+str+"\"");
		return false;
	}

	if (str.indexOf(at) == -1 || str.indexOf(at) == 0
			|| str.indexOf(at) == lstr) {
		alert("Formato Invalido de E-mail. Necesita un Nombre de Usuario. \""+str+"\"");
		return false;
	}

	if (str.indexOf(dot) == -1 || str.indexOf(dot) == 0
			|| str.indexOf(dot) == lstr) {
		alert("Formato Invalido de E-mail. Falta nombre de Cuenta. \""+str+"\"");
		return false;
	}

	if (str.indexOf(at, (lat + 1)) != -1) {
		alert("Formato Invalido de E-mail. \""+str+"\"");
		return false;
	}

	if (str.substring(lat - 1, lat) == dot
			|| str.substring(lat + 1, lat + 2) == dot) {
		alert("Formato Invalido de E-mail. \""+str+"\"");
		return false;
	}
	
	if (str.indexOf(dot, (lat + 2)) == -1) {
		alert("Formato Invalido de E-mail \""+str+"\"");
		return false;
	}
	return true;
}

function emailValidate() {	
	 var flag = true;	    
	    if (document.forms[0].email.value != "") {
	    	var eMailList = document.forms[0].email.value.split(';');        
	        for (var j = 0; j < eMailList.length; j++) {
	            var eMailArray = eMailList[j].trim();
	            if (!echeck(eMailArray)) {
	                document.forms[0].email.value = eMailArray; 
	                setTimeout(function() { document.forms[0].email.focus(); }, 1); 
	                flag = false;
	                break;
	            }
	        }
	    }
	return flag;
}


/* Valida si un campo esta vacio. *///AAP03
function isVacio(idElemento,campo) {
	var band = validaVacio(eval("document.forms[0]."+idElemento).value);//AAP03
	if(band){
		alert('EL CAMPO DE '+campo+' NO PUEDE QUEDAR VACIO.');
		eval("document.forms[0]."+idElemento).focus();
	}
	return band;
}

/*valida el campo que no este vacio ni con espacios //AAP03*/
function validaVacio(TextField){//AAP03

	var i    = 0;
	var band = false;		
	if(TextField==''){
		band = true;
	}else{
	    for (i = 0 ; i < TextField.length ; i++){
		   if (TextField.charAt(i) != '' && TextField.charAt(i) != ' '){
		   		band = false;
		   		return band;
		    }else{
		    	band = true;
		    }
		 }			
	}
	return band;	
}	

function validacionesRFC(){//AAP03
	var continua = true;
	var band = true;
	var indice = document.forms[0].tipoCliente.selectedIndex;
	var rfc = document.forms[0].rfc1.value + document.forms[0].rfc2.value + document.forms[0].rfc3.value;
	if (document.forms[0].tipoCliente[indice].value == "I") {//tipo cliente individuo
		band = true;
	} else {//tipo cliente compa�ia
		band = false;
	}
	continua = rfc == "XAXX010101000" ? true : validarRFC(band);
	
	return continua;
}

function validarRFC(isIndividuo){//AAP03
	if (isIndividuo){
		//eval("document.forms[0].rfc1").value;
		
		if (eval("document.forms[0].rfc1").value!='' || eval("document.forms[0].rfc2").value!='' ){
			if( isLongitud('rfc1', 4) && isLongitudDos('rfc2') && isLongitudTresRes('rfc3') ){
			//if( isLongitudDos('rfc2') ){
				if( validarEspacioRes('rfc1') ){				
					if( validarFecha('rfc2') ){
						return true;
					}else{
						return false;
					}
				}		
			} else {
				return false;
			}
		}else{
			return true;
		}	
	}else{
		if(isLongitud('rfc1', 3) && isLongitudDos('rfc2') && isLongitudTresCom('rfc3') ){
			if (validarEspacioCom('rfc1') ){
				if( validarFecha('rfc2') ){
					return true;
				} else {
					return false;
				}				
			}
		}			
	}
}
//valida longitud de las iniciales del rfc
function isLongitud(elemento, posiciones){//AAP03
	var long=eval("document.forms[0]."+elemento).value.length;
	//if(long == 4 || long == 3 ){
	if(long == posiciones) {		
		return true;
	}else{
		alert('EL CAMPO DE INICIALES DEL RFC DEBE DE SER DE '+posiciones+' CARACTERES');
		eval("document.forms[0]."+elemento).focus();
		return false;
	}
}
//valida longitud de la fecha del rfc
function isLongitudDos(elemento){//AAP03
	var long=eval("document.forms[0]."+elemento).value.length;
	if(long==6){
		return true;
	}else{
		alert('EL CAMPO DE FECHA DEL RFC DEBE DE SER DE 6 DIGITOS');
		eval("document.forms[0]."+elemento).focus();
		return false;
	}
}
//valida la longitud de la homoclave para residencial
function isLongitudTresRes(elemento){//AAP03
	if(eval("document.forms[0]."+elemento).value!=''){
		var long=eval("document.forms[0]."+elemento).value.length;
		if(long==3){
			return true;
		}else{
			alert('EL CAMPO DE LA HOMOCLAVE DEL RFC DEBE DE SER DE 3 CARACTERES/DIGITOS');
			eval("document.forms[0]."+elemento).focus();
			return false;
		}
	}else{
		return true;
	}		
}
//valida la longitud de la homoclave para comercial
function isLongitudTresCom(elemento){//AAP03
	var long=eval("document.forms[0]."+elemento).value.length;
	if(long==3){
		return true;
	}else{
		alert('EL CAMPO DE LA HOMOCLAVE DEL RFC DEBE DE SER DE 3 CARACTERES/DIGITOS');
		eval("document.forms[0]."+elemento).focus();
		return false;
	}
}
//valida que no tenga espacios las iniciales cuando es residencial
function validarEspacioRes(elemento){//AAP03
	var i=eval("document.forms[0]."+elemento).value.indexOf(' ');
	if(i>-1){
		alert('LAS INICIALES RECIBIDAS DEL RFC NO SON VALIDAS');
		eval("document.forms[0]."+elemento).focus();
		return false;
	}else{
		return true;
	}
}
//valida que no tenga espacios las iniciales cuando es comercial
function validarEspacioCom(elemento){//AAP03
	var i=eval("document.forms[0]."+elemento).value.indexOf(' ');
	var o=eval("document.forms[0]."+elemento).value.indexOf('&');
	var u=eval("document.forms[0]."+elemento).value.indexOf('#');
	
	if(i>-1){
		if(i>=3 ){
			
			return true;
		}else{
			alert('LAS INICIALES RECIBIDAS DEL RFC NO SON VALIDAS');
			eval("document.forms[0]."+elemento).focus();
			return false;			
		}
	}else{
		return true;
	}
}
//chakar fecha sea valida para el rfc
function validarFecha(){//AAP03
	var x=eval("document.forms[0].rfc2").value;
	var a='20'+x.substring(0,2);
	var m=x.substring(2,4);
	var d=x.substring(4,6);	
	
	if( validarMes(m) ){
		if( validarDia(d,m,a) ){
			return true;
		}else{
			alert('EL FORMATO DE DIA EN LA FECHA DE RFC ES INVALIDO');
			eval("document.forms[0].rfc2").focus();
			return false;
		}
	}else{
		alert('EL FORMATO DE MES EN LA FECHA DE RFC ES INVALIDO');
		eval("document.forms[0].rfc2").focus();
		return false;
	}			
}
//valida si el mes del rfc es menor de 12
function validarMes(valor){//AAP03
	if(valor==8 || valor==9){//AAP03
		return true;
	}else{
		var intMes=parseInt(valor);
		if(intMes>0 && intMes<=12 ){
			return true;
		}else{
			return false;
		}
	}//AAP03
}
//Cheka que el dia del rfc sea valido
function validarDia(valorDia,valorMes,valorA){
	var intMes=0;
	var intDia=0;
	if(valorMes==8){//AAP03
		intMes=8;
	}else{
		if(valorMes==9){
			intMes=9;
		}else{
			intMes=parseInt(valorMes);			
		}	
	}//AAP03

	if(valorDia==8){
		intDia=8;
	}else{
		if(valorDia==9){
			intDia=9;
		}else{
			intDia = parseInt(valorDia);			
		}	
	}

	if( intMes==1 || intMes==3 || intMes==5 || intMes==7 || intMes==8 || intMes==10 || intMes==12  ){
		if(intDia>31){
			return false;
		}else{
			return true;
		}
	}else{
		if( intMes==4 || intMes==6 || intMes==9 || intMes==11 ){
			if(intDia>30){
				return false;
			}else{
				return true;
			}
		}else{
			if( intMes==2 ){
				if( isBisiesto(valorA) ){
					if(intDia>28){
						return false;
					}else{
						return true;
					}
				}else{
					if(intDia>29){
						return false;
					}else{
						return true;
					}
				}
			}
		}
	}
}
//regresa si un a�o es bisiesto
function isBisiesto(valor){//AAP03
	if(isMod4(valor) && (!isMod100(valor) || isMod400(valor)) ){
		return false;  
	}else{
		return true; 
	}
}
//Validar si el a�o es modulo de 4
function isMod4(valor){//AAP03
	var div4= parseInt(valor)/4;		
	var isPunto=String(div4).indexOf('.');
	var i=parseInt(isPunto);

	if(i>-1){
		return false;
	}else{
		return true;
	}		
}
//Validar si el a�o es modulo de 100
function isMod100(valor){//AAP03
	var div4= parseInt(valor)/100;		
	var isPunto=String(div4).indexOf('.');
	var i=parseInt(isPunto);

	if(i>-1){
		return false;
	}else{
		return true;
	}		
}
//Validar si el a�o es modulo de 400
function isMod400(valor){//AAP03
	var div4= parseInt(valor)/400;		
	var isPunto=String(div4).indexOf('.');
	var i=parseInt(isPunto);

	if(i>-1){
		return false;
	}else{
		return true;
	}
}
function isNumberKey(evt) {
   var charCode = (evt.which) ? evt.which : event.keyCode
   if (charCode > 31 && (charCode < 48 || charCode > 57))
      return false;

   return true;
}
//Funcion para impedir que regrese al jsp anterior con el backspace o con alt + <--
var back='true';
function noBack(key){
	//var valor=key.keyCode;
	var valor = (window.event) ? key.keyCode : key.which;
	if(back=='true'){
		if(key.altKey){
			//event.returnValue=false;
			event.preventDefault();
		}else{
			if(valor=='8'){
				//event.returnValue=false;
				event.preventDefault();
			}
		}	
	}	
}
//Cambia el valor a la variable back...para que funcione el back espace en los inputs.
//En el onFocus=false; en el onBlur=true
function setBack(valor){
	back=valor;
}
document.onkeydown=function(e) {
    var event = window.event || e;
    if (event.keyCode == 116) {
        event.keyCode = 0;
        alert("Esta accion no esta permitida");
        return false;
    }
}

/* Valida el RFC ante el sat */
function validRfcSat(){
	
	var rfc = document.forms[0].rfc1.value.toUpperCase() + document.forms[0].rfc2.value + document.forms[0].rfc3.value;
	
	if (rfc != "XAXX010101000"){
		document.getElementById("btnDatosFiscales").style.display = "block";
	}else{
		document.getElementById("btnDatosFiscales").style.display = "none";
	}
	
	if (rfc.length >= 12 && ((rfc != "XAXX010101000" && document.forms[0].tipoCliente.value == 'I') || document.forms[0].tipoCliente.value == 'C' )){
		/* Validación local */
		if (rfc == "XAXX010101000" || isValidRFC(rfc)){
			
			/* Validación SAT */
			showBarraEspera('mensaje','body','Espere por favor...validando RFC');
			document.forms[0].mode.value = "validateRFC";
			if (document.forms[0].guardar.value == "Insertar"){
				document.forms[0].mode.value += "-Insertar";
			}
			document.forms[0].rfc1.focus();
			document.forms[0].genericRFC="N";
			document.forms[0].submit();
		}else{
			document.forms[0].mode.value = "validateRFC";
			if (document.forms[0].guardar.value == "Insertar"){
				document.forms[0].mode.value += "-Insertar";
			}
			document.forms[0].validRFC.value = "N";
			document.forms[0].genericRFC.value = "N";
			document.forms[0].rfc3.focus();
			document.forms[0].submit();
		}
	}else if(rfc == "XAXX010101000"){
		document.forms[0].mode.value = "validateRFC";
		if (document.forms[0].guardar.value == "Insertar"){
			document.forms[0].mode.value += "-Insertar";
		}
		document.forms[0].validRFC.value = "Y";
		document.forms[0].genericRFC.value = "Y";
		document.forms[0].submit();
		
	}else if(rfc.length >= 12){
		document.forms[0].genericRFC="N";
		document.forms[0].validRFC.value="N";
		//document.forms[0].submit();
	}else{
		document.forms[0].genericRFC="N";
		document.forms[0].validRFC.value="N";
	}
}
	
function valCompleteFiscalData(orgnFiscalData, newFiscalData) {
    const rfc = document.forms[0].rfc1.value.toUpperCase() + document.forms[0].rfc2.value + document.forms[0].rfc3.value;
    const defaultRfc = "XAXX010101000";
    const newDataEmpty = newFiscalData != "";
    
    if (rfc === defaultRfc) {
        return true; // Default tax ID, no need to validate fiscal data
    }
    
    if (!newDataEmpty){
    	return false; //Is a different RFC compared to the current and it doesnt have captured Fiscal Data.
    }
    
    const firstData = JSON.parse(orgnFiscalData);
    const secondData = JSON.parse(newFiscalData);
    firstData['taxId'] = firstData['taxId'].replaceAll('-','')
	secondData['taxId'] = secondData['taxId'].replaceAll('-','')
	secondData['clntType'] = secondData['clntType'] == "M" || secondData['clntType'] == "C"? "M" : 
							secondData['clntType'] == "" ? secondData['clntType'] : "I";
    
    const requiredFields = ["taxId", "clntType", "zipCode", "regmFiscal"];
    const isSecondDataEmpty = Object.keys(secondData).length === 0;
    
    if (isSecondDataEmpty){
    	return false;
    }

    if (firstData.validFlag === "N" && !isSecondDataEmpty) {	
        return requiredFields.every(field => secondData[field] != "");
    }
    
    if (firstData.validFlag === "Y" && !isSecondDataEmpty && document.forms[0].guardar.value != "Insertar") {
        return requiredFields.every(field => firstData[field].toUpperCase() === secondData[field].toUpperCase());
    }
    
    if (firstData.validFlag === "Y" && !isSecondDataEmpty && document.forms[0].guardar.value == "Insertar") {
    	if (firstData['taxId'].toUpperCase() == secondData['taxId'].toUpperCase()){
    		return requiredFields.every(field => firstData[field].toUpperCase() === secondData[field].toUpperCase());
    	}
    }

    return false;
}