let mode = "";
let allowModif = true;

window.addEventListener('beforeunload', function (e) {
	let rfc = window.opener.document.forms[0].rfc1.value + window.opener.document.forms[0].rfc2.value + window.opener.document.forms[0].rfc3.value;
	let container = document.querySelectorAll('input');
	let capturedData = false;
	Array.from(container).filter(input => {
		if (input.value != "" && input.name != "rfc" &&
			input.type !== "button" && input.type !== "checkbox" &&
			!input.classList.contains('button1') && !input.classList.contains('button')
			){ 
			capturedData = true;
		}
	});
	if (mode != "save" && rfc != "XAXX010101000") {
		if (capturedData) {
			if (window.opener.document.forms[0].currRFC.value.replaceAll("-","") != document.forms[0].rfc.value) {
				window.opener.document.forms[0].openFiscalForm.value="N"
			}
			e.preventDefault();
			e.returnValue = '';
		}else{
			window.opener.document.forms[0].openFiscalForm.value="N";
		}
	}
});
function initScript() {
	const queryString = window.location.search;

	const urlParams = new URLSearchParams(queryString);

	document.forms[0].rfc.value = urlParams.get('rfc');
	document.forms[0].clntType.value = urlParams.get('tipoCliente');

	try {
		/* Se presentan los datos obtenidos */
		let datosFiscalesObj;
		let datosFiscalesObjSaved = JSON.parse(window.opener.document.forms[0].datosFiscalesTmpString.value);
		let datosFiscalesObjSession = JSON.parse(window.opener.document.forms[0].datosFiscalesString.value);
		let showCapturedData = false;
		if (datosFiscalesObjSession.usoCfdi != ''){
			datosFiscalesObj = datosFiscalesObjSession;
			showCapturedData = true;
		}else{
			datosFiscalesObj = datosFiscalesObjSaved;
		}
		datosFiscalesObj.clntType = datosFiscalesObj.clntType == "M" || datosFiscalesObj.clntType == "C"? "C" : 
									datosFiscalesObj.clntType == "" ? document.forms[0].clntType.value : "I";
		valClntType(datosFiscalesObj.clntType);
		if (datosFiscalesObj.taxId != "") {
			let guardarValue = window.opener.document.forms[0].guardar.value;
			let show = guardarValue == ("Insertar" && datosFiscalesObj.validFlag != "Y") || showCapturedData;
			showData(datosFiscalesObj, show);

			let validFlag = datosFiscalesObj.validFlag;
			let validData = document.getElementById("validData");

			validData.removeAttribute("disabled");

			if (validFlag === "N") {
				validData.removeAttribute("checked");
				validData.setAttribute("disabled", "disabled");
			} else {
				validData.setAttribute("checked", "checked");
				validData.setAttribute("disabled", "disabled");
			}
		}
	} catch (e) {
	}

	if (window.opener.document.forms[0].tieneCredito.value == "S" || document.getElementById("validData").checked ||
		window.opener.document.forms[0].validFiscal.value == "Y") {
		if (window.opener.document.forms[0].validFiscal.value == "Y") {
			document.getElementById("validData").removeAttribute("disabled");
			document.getElementById("validData").setAttribute("checked", "checked");
			document.getElementById("validData").setAttribute("disabled", "disabled");
		} else {
			document.getElementById("validData").removeAttribute("disabled");
			document.getElementById("validData").removeAttribute("checked");
			document.getElementById("validData").setAttribute("disabled", "disabled");
		}
		if (window.opener.document.forms[0].guardar.value != "Insertar" && 
			window.opener.document.forms[0].currRFC.value.replaceAll("-","") == document.forms[0].rfc.value) {
			disableCaptura(true);
			allowModif = false;
		}
	} else {
		disableCaptura(false);
		allowModif = true;
	}
}

function showData(datosFiscalesObj, showFlag) {
	const queryString = window.location.search;
	let clntType = datosFiscalesObj.clntType;
	const urlParams = new URLSearchParams(queryString);

	document.forms[0].rfc.value = urlParams.get('rfc');
	document.forms[0].clntType.value = clntType;
	valClntType(clntType);

	if (showFlag) {
		if (clntType == "I") {
			document.forms[0].nombre.value = datosFiscalesObj.firstName;
			document.forms[0].primerApellido.value = datosFiscalesObj.lastName1;
			document.forms[0].segundoApellido.value = datosFiscalesObj.lastName2;
		} else if (clntType== "C") {
			document.forms[0].razonSocial.value = datosFiscalesObj.businessName;
			document.forms[0].regmCapital.value = datosFiscalesObj.capitalRegm;
		}
		document.forms[0].calle.value = datosFiscalesObj.street;
		document.forms[0].nExt.value = datosFiscalesObj.nExt;
		document.forms[0].nInt.value = datosFiscalesObj.nInt;
		document.forms[0].codigoPostal.value = datosFiscalesObj.zipCode;
		document.forms[0].colonia.value = datosFiscalesObj.colony;
		document.forms[0].ciudad.value = datosFiscalesObj.city;
		document.forms[0].municipio.value = datosFiscalesObj.municipality;
		document.forms[0].estado.value = datosFiscalesObj.state;
		document.forms[0].regimenFiscalId.value = datosFiscalesObj.regmFiscal;
		document.forms[0].usoCfdiId.value = datosFiscalesObj.usoCfdi;
		document.forms[0].regimenFiscalDes.value = window.opener.document.forms[0].regimenFiscalDes.value;
		document.forms[0].usoCfdiDes.value = window.opener.document.forms[0].usoCfdiDes.value;
	}
}

function getRow() {

	//Variables para PersonaFisica/Moral
	let rfc = document.getElementById("rfc").value;
	let tipoCliente = document.getElementById("clntType").value;
	let razonSocial, regmCapital;
	let nombre, primerApellido, segundoApellido;
	let missingData = [];
	let missingDataFound = false;

	//Variables para Domicilio Fiscal
	let calle, nExt, nInt, codigoPostal,
		colonia, ciudad, municipio, estado;

	let parentForm = window.opener.document.forms[0];

	if (parentForm.tieneCredito.value == "S" &&
		document.forms[0].rfc.value == (parentForm.rfc1.value + parentForm.rfc2.value + parentForm.rfc3.value)) {
		alert("El RFC est\u00e1 signado a cliente con CREDITO, no es posible modificarlo. Favor de comunicarse con su ejecutivo Paquetexpress para ajustes de cliente.")
		return;
	}
	
	if (!allowModif){
		window.close();
		return;
	}

	//Inicia validación de por tipo de persona
	if (tipoCliente == "C") {
		razonSocial = document.forms[0].razonSocial.value;
		regmCapital = document.forms[0].regmCapital.value;
		nombre = "";
		primerApellido = "";
		segundoApellido = "";
		missingData = emptyFieldsCheck('#personaMoral');
	} else if (tipoCliente == 'I') {
		nombre = document.forms[0].nombre.value;
		primerApellido = document.forms[0].primerApellido.value;
		segundoApellido = document.forms[0].segundoApellido.value;
		razonSocial = "";
		regmCapital = "";
		missingData = emptyFieldsCheck('#personaFisica');
	}

	missingDataFound = alertCustom(missingData, document.forms[0].nombre);
	//Termina validación de por tipo de persona
	
	if (missingDataFound){
		return false;
	}

	//Validación domicilio fiscal
	calle = document.forms[0].calle.value;
	nExt = document.forms[0].nExt.value;
	nInt = document.forms[0].nInt.value;
	codigoPostal = document.forms[0].codigoPostal.value;
	colonia = document.forms[0].colonia.value;
	ciudad = document.forms[0].ciudad.value;
	municipio = document.forms[0].municipio.value;
	estado = document.forms[0].estado.value;

	//Inicia validación de Domicilio Fiscal
	missingData = emptyFieldsCheck('#domicilioFiscal');
	missingDataFound = alertCustom(missingData, document.forms[0].calle);
	//Termina validación de Domicilio Fiscal
	
	if (missingDataFound){
		return false;
	}

	//Inicia validación de Regimen Fiscal y Uso CFDI
	missingData = emptyFieldsCheck('#regfiUCfdi');
	missingDataFound = alertCustom(missingData, document.forms[0].regimenFiscalDes);
	//Termina validación de Regimen Fiscal y Uso CFDI

	if (missingDataFound){
		return false;
	}
	//Guardar información
	let datosFiscales = {
		taxId: rfc,
		clntType: tipoCliente,
		businessName: razonSocial,
		capitalRegm: regmCapital,
		firstName: nombre,
		lastName1: primerApellido,
		lastName2: segundoApellido,
		street: calle,
		nInt: nInt,
		nExt: nExt,
		zipCode: codigoPostal,
		colony: colonia,
		city: ciudad,
		municipality: municipio,
		state: estado,
		regmFiscal: document.forms[0].regimenFiscalId.value,
		usoCfdi: document.forms[0].usoCfdiId.value,
		validFlag: parentForm.validData.checked ? "Y" : "N",
	};

	parentForm.regimenFiscalId.value = document.forms[0].regimenFiscalId.value;
	parentForm.regimenFiscalDes.value = document.forms[0].regimenFiscalDes.value;
	parentForm.usoCfdiId.value = document.forms[0].usoCfdiId.value;
	parentForm.usoCfdiDes.value = document.forms[0].usoCfdiDes.value;

	let datosFiscalesJson = JSON.stringify(datosFiscales);
	parentForm.datosFiscales.value = datosFiscalesJson;
	parentForm.datosFiscalesString.value = datosFiscalesJson;

	let datosFiscalesOrgn = JSON.parse(parentForm.datosFiscalesTmpString.value);
	if (datosFiscalesOrgn.taxId != "" && parentForm.validFiscal.value == "Y") {
		const fields = [
			'zipCode',
			'businessName',
			'firstName',
			'lastName1',
			'lastName2',
			'regmFiscal',
			'usoCfdi',
			'validFlag'
		];
		datosFiscalesOrgn.taxId = datosFiscalesOrgn.taxId.replaceAll('/-/g', '');

		let hasChanged = fields.some(field => datosFiscalesOrgn[field] !== datosFiscales[field]);

		if (hasChanged) {
			const shouldContinue = confirm('Datos Fiscales Incorrectos. Presione Aceptar para continuar con RFC PUBLICO EN GENERAL.\n Presione Cancelar para corregir Datos Fiscales');

			if (shouldContinue) {
				parentForm.rfc1.value = "XAXX";
				parentForm.rfc2.value = "010101";
				parentForm.rfc3.value = "000";
				parentForm.datosFiscales.value = "";
				parentForm.datosFiscalesString.value = "";
				parentForm.tipoCliente.value = "I";
				parentForm.submit();
				mode = "save";
				window.close();
			} else {
				parentForm.errorMessages.value = "Datos Fiscales Incorrectos. Favor de corregir los datos o continuar con RFC PUBLICO EN GENERAL(XAXX-010101-000).";
			}
		} else {
			parentForm.openFiscalForm.value = "Y";
			assignTaxIdInParentAndClose(datosFiscales.taxId, true);
		}
	} else {
		assignTaxIdInParentAndClose(datosFiscales.taxId, true);
	}
}

function assignTaxIdInParentAndClose(taxId, closeWindow) {
	if (!isValidRFC(taxId)) return;
	let parentForm = window.opener.document.forms[0];
	let clientType = document.getElementById("clntType").value;
	let taxIdParts = divideTaxId(taxId, clientType);
	parentForm.rfc1.value = taxIdParts[0];
	parentForm.rfc2.value = taxIdParts[1];
	parentForm.rfc3.value = taxIdParts[2];
	mode = "save";
	if (closeWindow) window.close();
}

function divideTaxId(rfc, clientType) {
	let name, dateOfBirthOrIncorp, homoclave;
	const rfcType = rfc.charAt(3);

	// Check the RFC type (person or business)
	if (clientType === "C") {
		name = rfc.substring(0, 3);
		dateOfBirthOrIncorp = rfc.substring(3, 9);
		homoclave = rfc.substring(9);
	} else if (clientType === "I") {
		name = rfc.substring(0, 4);
		dateOfBirthOrIncorp = rfc.substring(4, 10);
		homoclave = rfc.substring(10);
	} else {
		alert("Tipo de cliente no v\u00E1lido: " + clientType);
	}

	return [name, dateOfBirthOrIncorp, homoclave];
}

function isValidRFC(rfc) {
	// Regular expressions for each part of the RFC
	let regex1 = /^[A-Z\u00D1\&]{3,4}$/;
	let regex2 = /\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])/;
	let regex3 = /^[A-Z0-9]{3}$/;
	let clientType = document.getElementById("clntType").value;
	let errorMsg = "";
	let result;
	let rfcParts = divideTaxId(rfc, clientType);

	//Validate client type and rfc type
	if (clientType == 'I' && rfc.length != 13) {
		errorMsg = 'EL CAMPO DE INICIALES DEL RFC DEBE TENER UNA LONGITUD DE 4 CARACTERES SIN NUMEROS.\n';
		document.forms[0].rfc.focus();
	}

	if (clientType != 'I' && rfc.length != 12) {
		errorMsg = 'EL CAMPO DE INICIALES DEL RFC DEBE TENER UNA LONGITUD DE 3 CARACTERES SIN NUMEROS.\n';
		document.forms[0].rfc.focus();
	}

	// Extract the different parts of the RFC
	let part1 = rfcParts[0];
	let part2 = rfcParts[1];
	let part3 = rfcParts[2];
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

	if (!isValidPart1) {
		let len = clientType == 'I' ? 4 : 3;
		errorMsg = 'EL CAMPO DE INICIALES DEL RFC DEBE TENER UNA LONGITUD DE ' + len + ' CARACTERES SIN NUMEROS.\n';
		document.forms[0].rfc.focus();
	}

	if (!isValidDate || !isValidPart2) {
		errorMsg += 'LA FECHA NO ES VALIDA.\n';
	}

	if (!isValidPart3) {
		errorMsg += 'LA HOMOCLAVE DEL RFC DEBE TENER 3 CARACTERES/DIGITOS SIN CARACTERES ESPECIALES.';
		document.forms[0].rfc.focus();
	}
	if (isValidPart1 && isValidPart2 && isValidPart3 && isValidDate && errorMsg == '') {
		result = true;
	} else {
		errorMsg = 'RFC Invalido: ' + errorMsg;
		alert(errorMsg);
		result = false;
		document.forms[0].rfc.focus();
	}
	// Return true if all parts are valid, false otherwise
	return result;
}

function isValidRFCDate(year, month, day) {
	const date = new Date(year + 2000, month - 1, day);
	return date.getFullYear() === year + 2000 &&
		date.getMonth() === month - 1 &&
		date.getDate() === day;
}

function emptyFieldsCheck(containerType) {
	let missingData = [];
	let container = document.querySelectorAll(containerType);

	Array.from(container).filter(input => {
		if (input.value == "") missingData.push(input.name)
	});
	return missingData;
}

function alertCustom(message, fieldToFocus) {
	if (message.length > 0) {
		alert('Captura la siguiente informaci\u00F3n: ' + message);
		fieldToFocus.focus();
		window.opener.document.forms[0].openFiscalForm.value="N";
		return true;
	}
}

function closeWindow() {
	mode = "close";
	window.close();
}

function valClntType(clntType) {
	let fsPM = document.getElementById("fsPM");
	let fsPF = document.getElementById("fsPF");

	if (clntType != '' && clntType == "C") {
		fsPF.style.display = "none";
		fsPM.style.display = "block";
	} else if (clntType != '' && clntType == "I") {
		fsPF.style.display = "block";
		fsPM.style.display = "none";
	}
}

function rfcChange() {

	if (!isValidRFC(document.forms[0].rfc.value)) {
		return;
	}

	if (document.forms[0].rfc.value != window.opener.document.forms[0].rfc1.value + window.opener.document.forms[0].rfc2.value + window.opener.document.forms[0].rfc3.value) {
		window.opener.document.forms[0].mode.value = "validateRFCFiscal-" + document.forms[0].rfc.value;
		window.opener.document.forms[0].submit();
	}
}
function resetForm() {
	if (window.opener.document.forms[0].tieneCredito.value != "S" && !document.getElementById("validData").checked) {
		document.forms[0].nombre.value = "";
		document.forms[0].primerApellido.value = "";
		document.forms[0].segundoApellido.value = "";
		document.forms[0].razonSocial.value = "";
		document.forms[0].regmCapital.value = "";
		document.forms[0].calle.value = "";
		document.forms[0].nExt.value = "";
		document.forms[0].nInt.value = "";
		document.forms[0].codigoPostal.value = "";
		document.forms[0].colonia.value = "";
		document.forms[0].ciudad.value = "";
		document.forms[0].municipio.value = "";
		document.forms[0].estado.value = "";
		document.forms[0].regimenFiscalDes.value = "";
		document.forms[0].usoCfdiDes.value = "";
		document.forms[0].codigoPostalBut.disabled = false;
		document.forms[0].regimFiscBtn.disabled = false;
		document.forms[0].usoCfdiBtn.disabled = false;
		window.opener.document.forms[0].tieneCredito.value = "";
		window.opener.document.forms[0].datosFiscalesString.value = "";
		mode = "";
		if (document.getElementById("clntType").value == "I") {
			document.forms[0].nombre.focus();
		} else {
			document.forms[0].razonSocial.focus();
		}
	} else {
		if (window.opener.document.forms[0].tieneCredito.value == "S") {
			alert("El RFC est\u00e1 signado a cliente con CREDITO, no es posible modificarlo. Favor de comunicarse con su ejecutivo Paquetexpress para ajustes de cliente.");
		}
	}
}
function openLov(str) {
	let rfc = document.getElementById("rfc").value.toUpperCase();
	if (str == 'regimFiscal') {
		let clntType = document.getElementById("clntType").value.toUpperCase();

		if (rfc == "" && rfc.length < 12) {
			alert("Capture el RFC completo");
			return false;
		}
		abrirVentana('lov.do?type=' + str + '&rfc=' + rfc + '&clntType=' + clntType, 'Catalogo Regimen Fiscal', 600, 206);
	} else if (str == 'usoCfdi') {
		let regimFiscalId = document.forms[0].regimenFiscalId.value;

		if (rfc.length < 12) {
			alert("Capture el RFC completo");
			return false;
		}
		abrirVentana('lov.do?type=' + str + '&rfc=' + rfc + '&regimFiscalId=' + regimFiscalId, 'Catalogo uso CFDI', 600, 206);
	} else if (str == 'postal') {
		let paramZipCode = '';
		if (document.forms[0].codigoPostal.value != '') {
			paramZipCode = '&find=' + document.forms[0].codigoPostal.value + '&searchType=all';
		}
		lovWindow = abrirVentana('zipCodes.do?currentTask=start' + paramZipCode, 'abc', 1000, 560);

	}
}

function getCoordenadas(xWidth, xHeight) {
	return 'dependent=yes, width=' + xWidth + ', height=' + xHeight + ', top=' + ((screen.availHeight - xHeight) / 2) + ', left=' + ((screen.availWidth - xWidth) / 2);
}

//Abre un ventana centrada
//ejemplo de uso:  abrirVentana('aaa.jsp?llave=01', 'ventanax', 450, 200);
function abrirVentana(url, xWindow, xWidth, xHeight) {
	return window.open(url, xWindow, getCoordenadas(xWidth, xHeight));
}

function changeCase(input) {
	input.value = input.value.toUpperCase();
}
function setBack(valor) {
	let back = valor;
}

//se detona desde el catalogo de codigos postales
function setValSelZipCode() {
	document.forms[0].codigoPostal.value = lovWindow.document.forms[0].zipCodeSel.value;
	document.forms[0].colonia.value = lovWindow.document.forms[0].colDesSel.value;
	document.forms[0].ciudad.value = lovWindow.document.forms[0].ciuDesSel.value;
	document.forms[0].municipio.value = lovWindow.document.forms[0].munDesSel.value;
	document.forms[0].estado.value = lovWindow.document.forms[0].edoDesSel.value;
}

function disableCaptura(flag) {
	document.forms[0].nombre.disabled = flag;
	document.forms[0].primerApellido.disabled = flag;
	document.forms[0].segundoApellido.disabled = flag;
	document.forms[0].razonSocial.disabled = flag;
	document.forms[0].regmCapital.disabled = flag;
	document.forms[0].calle.disabled = flag;
	document.forms[0].nExt.disabled = flag;
	document.forms[0].nInt.disabled = flag;
	document.forms[0].codigoPostal.disabled = flag;
	document.forms[0].colonia.disabled = flag;
	document.forms[0].ciudad.disabled = flag;
	document.forms[0].municipio.disabled = flag;
	document.forms[0].estado.disabled = flag;
	document.forms[0].regimenFiscalDes.disabled = flag;
	document.forms[0].usoCfdiDes.disabled = flag;
	document.forms[0].codigoPostalBut.disabled = flag;
	document.forms[0].regimFiscBtn.disabled = flag;
	document.forms[0].usoCfdiBtn.disabled = flag;
	if (document.forms[0].validData.checked) {
		document.forms[0].rfc.disabled = true;
	}
}