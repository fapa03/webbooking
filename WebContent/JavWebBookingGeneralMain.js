/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creaci�n: 15/11/2012
 * Compa��a: KUMARAN.
 * Descripci�n del programa: js para validaciones y procesos de DOCUMENTACION DE GUIA.
 * FileName	:	
 * FormBean	:	
 * ActionBean	:	
 * OtherClasses	:	none
 * CSS FileName	:	webbooking.css
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP04
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 01/07/2013
 * Descripci�n:  Se agregaron validaciones para FLETE POR COBRAR
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
	function cambioCentroCosto() {
		if (document.getElementById('mensaje').style.visibility == 'hidden') {			
			showBarraEspera('mensaje','body','Espere por favor...obteniendo Direcci\u00F3n de Centro de Costo');
			document.forms[0].changeCCosto.value = 'Y';
			document.forms[0].accion = "check";
			document.forms[0].submit();
		}
	}
	function openKeyLov(str) {
		var prodDes = document.forms[0].productDescSat.value;
		if(window.event.keyCode==13) {
			if (str == 'catProducts' && prodDes.length < 3){
				alert("CAPTURE 3 CARACTERES O M\u00C1S");
				return;
			}
			openLov(str);
		}
		if(window.event.keyCode==9 && str == 'catProducts' && prodDes.length < 3){
			openLov(str);
		}
	}
	function searchBrnch(str) {
		let estado = str.split("|");
		if (str != ""){
			window.open('https://paquetexpress.com.mx/sucursales/'+estado[1].toLowerCase().replace(" ","-"), "Sucursales", "_blank");
		}else{
			window.open('https://paquetexpress.com.mx/sucursales/', "Sucursales", "_blank");	
		}
	}
	function openLov(str) {
		if (document.getElementById('mensaje').style.visibility == 'hidden') {
			var destinationcode = document.forms[0].destinationsitecode.value;
			var destinationClientId = document.forms[0].destinationclave.value;
			
			/*condiciones para informacion general del cliente*/
			if (str == 'branch') {
				window.open('lov.do?type='+str+'&selectedBranch='+destinationcode+'&isNewBooking=Y','abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
			} else if (str == 'branchaddress') {
				window.open('lov.do?type='+str+'&sucursal='+ document.forms[0].destinationcode.value ,'abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
			} else if (str == 'client') {
				if (destinationcode == '') {
					alert('SELECCIONE LA CIUDAD DESTINO');
					return;
				} else {
					window.open('lov.do?type='+str+'&destinationsitecode='+destinationcode+'&isNewBooking=Y','abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
				}
			} else if ( str == 'clientNew') {
				
				var destClave = document.forms[0].destinationclave.value;
				var destNombre = document.forms[0].destinationnombre.value;
				var flagCCosto = document.forms[0].flagCCAssignedBrnc.value;	
				
				if (flagCCosto == "N"){
					alert('La dirección del centro de costo no contiene una cobertura asociada. Por favor, llamar a un asesor para solucionar su problema.');
					resetClientInfo();
					return 
				}
				
				if (valAddrWoCvge()){
					resetClientInfo();
					return;
				}
				
				if(destClave=='' && destNombre=='') {
					alert('Capture un criterio de B\u00FAsqueda');
					return;
				}
				
				if (destClave == '' && destNombre.length < 2){
					alert("CAPTURE 2 CARACTERES O MAS");
					return false;
				}
				window.open('lov.do?type='+str+'&destClave='+destClave+'&destNombre='+destNombre+'&destinationsitecode='+destinationcode,'abc','width=950,height=310,top=185,left=0,screenY=200,screenX=100');
				
				
			} else if (str == 'destinationaddress') {
				var destinationclave = document.forms[0].destinationclave.value;
				if (destinationcode == '') {
					alert('SELECCIONE LA CIUDAD DESTINO');
					return;
				} else if (destinationClientId == '') {
					alert('SELECCIONE EL NUMERO DE CLIENTE');
					return;
				} else {		
					window.open('lov.do?type='+str+'&destClId='+destinationClientId+'&addresstype=DC&destinationclient='+destinationclave+'&destinationsitecode='+destinationcode+'&isNewBooking=Y','abc','width=600,height=200,top=200,left=100,screenY=200,screenX=100');
				}
			} else if (str == 'fiscaladdress') {
				window.open('lov.do?type='+str,'abc','width=600,height=200,,top=200,left=100,screenY=200,screenX=100');
				
				/*seccion para captura de detalle*/
			} else if (str == 'description') {
				var selDesc = document.forms[0].descripcion.value;
				var defaultservicescreen = document.forms[0].defaultservicescreen.value;
				var clasifTarif = document.forms[0].clasifTarif.value;//AAP02 VARIABLE PARA INDICAR SI LA TARIFA ES ANTERIOR (0) O NUEVA (1)
				window.open('lov.do?type='+str+'&selecteddescription='+selDesc+'&isNewBooking=Y&defaultservicescreen='+defaultservicescreen+'&clasifTarif='+clasifTarif,'abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
			} else if (str == 'tariff') {
				if (document.forms[0].isShippingTypeSEG.value == 'Y' || document.forms[0].forceCaptureDimensions.value == 'Y') {
					alert("PARA ESTE TIPO DE ENVIO ES NECESARIO INGRESAR PESO, LARGO, ALTO, ANCHO.");
					return;
				}
				var clasifTarif = document.forms[0].clasifTarif.value;//AAP02 VARIABLE PARA INDICAR SI LA TARIFA ES ANTERIOR (0) O NUEVA (1)
				var defaultservicescreenKm = document.forms[0].defaultservicescreenKm.value;//AAP02 VARIABLE PARA INDICAR SI el convenio es por kilometro
				var action = document.forms[0].actionfordup.value;
				if (document.forms[0].descripcion.value == '') {
					alert("CAPTURE UN VALOR PARA 'DESCRIPCION'");
					return;
				}
				var index = -1;
				for (var i=0; i<document.forms[0].radioselect.length; i++) {
					var checkedValue = document.forms[0].radioselect[i].checked;
					if (checkedValue) {
						index = i;
						break;
					}
				}
				var serviceId = document.forms[0].ss_srvc_id.value;
				window.open('lov.do?type='+str+'&serviceid='+serviceId+'&action='+action+'&index='+index+'&clasifTarif='+clasifTarif+'&defaultservicescreenKm='+defaultservicescreenKm+'&destinationsitecode='+destinationcode,'abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
			} else if (str == 'service') {
				var defaultservicescreenKm = document.forms[0].defaultservicescreenKm.value;//AAP02 VARIABLE PARA INDICAR SI el convenio es por kilometro
				window.open('lov.do?type='+str+'&defaultservicescreenKm='+defaultservicescreenKm,'abc','width=600,height=200,top=200,left=100,screenY=200,screenX=100');
			} else if (str == 'catProducts'){
				if (!validateProductDes()){
					return;
				}
				if (document.forms[0].formaPago.value == 'TO_PAY'){
					window.open('lov.do?type='+str+'&contenido='+document.forms[0].productDescSat.value+"&clntDestId="+document.forms[0].destinationclave.value,'abc','width=950,height=250,top=185,left=0,screenY=100,screenX=100');
				}else{
					window.open('lov.do?type='+str+'&contenido='+document.forms[0].productDescSat.value,'abc','width=950,height=250,top=185,left=0,screenY=100,screenX=100');
				}
			} else if (str == 'cliententry'){
				window.open('lov.do?type='+str,str,'width=830,height=499,top=185,left=0,screenY=100,screenX=100');
				resetClientInfo(); 
				resetDetail();				
			}
		}
	}
	
	function validateProductDes(){
		let prodDes = document.forms[0].productDescSat.value;
		if (prodDes == '') {
			alert("CAPTURE UN VALOR PARA 'CAT. PRODUCTOS'");
			return false;
		}
		const hyphenIndex = prodDes.indexOf('-');
		let validProd = true;
		if (hyphenIndex !== -1) {
			const idPart = prodDes.substring(0, hyphenIndex).trim();
			const descriptionPart = prodDes.substring(hyphenIndex + 1).trim();
			if (idPart.length < 3 && descriptionPart.length < 3){
				validProd = false;
			}
		  }else{
			if (prodDes.length < 3){
				validProd = false;
			}
		}
		if (!validProd){
			alert("CAPTURE 3 CARACTERES O MAS");
			return validProd;
		}
		return true;
	}
	
	function callForm(str) {
		if (document.getElementById('mensaje').style.visibility=='hidden') {
			var destinationcode = document.forms[0].destinationsitecode.value;
			var destinationClientId = document.forms[0].destinationclave.value;
			var addressCode = document.forms[0].destinationaddresscode.value;		
		
			if (destinationcode=='') {
				alert('SELECCIONE LA CIUDAD DESTINO');
				return;
			} else if (destinationClientId == '') {
				alert('SELECCIONE EL NUMERO DE CLIENTE');
				return;
			} else if (addressCode == '') {
				alert('POR FAVOR SELECCIONE LA DIRECCION DESTINO');
				return;
			} else {
				showBarraEspera('mensaje','body','Espere por favor...validando Destino');
				/*reinicia valores de detalle*/
				resetDetail();
				if ('thispage' == str){
					disabledObjectsDetail(false);
				}
				document.forms[0].action="webBookinggeneralMain.do?";
				document.forms[0].submit();
			}
		}
	}
	
	function resetDetail(){
		/*se reinicia variable init para limpiar los objetos de detalle*/
		document.forms[0].accion.value ='init';		
		var hitCount = document.forms[0].hitCount.value;
		if (hitCount>0) {
			borraren(hitCount);			
			/*procedimiento para reiniciar la seccion de servicios*/
			resetServices();
		}
		resetValuesDetail();
		disabledObjectsDetail(true);
		document.forms[0].hitCount.value = 0;
	
		
		document.forms[0].showAdditional.value = "";		
	}
	
	/*Funcion que elimina el detalle*/
	function borraren(hitCount) {
		var tabla = document.getElementById('tablaDetalle');
		var reng = tabla.rows;
		for (var i = 1; i <= hitCount; i++) {
			var celdas = reng[i].cells;
			for (var j = 0; j < celdas.length; j++) {
				if (j === celdas.length - 1) {
					document.forms[0].radioselect[i - 1].disabled = true;
				} else {
					celdas[j].innerHTML = "&nbsp;";
				}
			}
		}
	}//fin borraren

	function initScript() {		
		ocultarBarraEspera('mensaje','body');
		let pedimentoErr = document.forms[0].errMsgPediNum.value;
		
		if (valAddrWoCvge()){
			resetClientInfo();
			resetDetail();
			return;
		}
		
        if (document.forms[0].shippingType.value == ''){
            resetClientInfo();
            resetDetail();    
        }
		var borderbranchcheck=document.forms[0].borderbranchcheck.value;
		var shiperrmsg=document.forms[0].shiperrmsg.value;
		var returnFlag = false;
		if (shiperrmsg!='') {
			document.forms[0].shiperrmsg.value = "";
			//alert(shiperrmsg);
			showToast("error", shiperrmsg, 6000);
			if(document.forms[0].accion.value == 'Editar'){
				document.forms[0].addbutton.value = "Editar";
				var valor = document.forms[0].radioselectCurrent.value;
				document.forms[0].radioselect[valor].checked = true;
			}else if (document.forms[0].accion.value == 'tarifChange'){
				document.forms[0].tarifa.focus();
			}else if (document.forms[0].accion.value == 'calculate'  ||
					document.forms[0].accion.value == 'showAditional'){
				resetServices();
				disabledObjectsServices(false);
			} else if (document.forms[0].accion.value == 'satProduct'){
				document.forms[0].productDescSat.focus();
			} else if (document.forms[0].accion.value == 'blockDoc'){
				document.forms[0].destinationclave.focus();
				resetServices();
				disabledObjectsDetail(true);
				
			}
			if(shiperrmsg.includes("ESTE CENTRO DE COSTO")){
				shiperrmsg = "";
				cambioCentroCosto();
			}
			returnFlag = true;
		}
		
		/* Validación de número de pedimento */
		if (document.forms[0].errMsgPediNum.value != ''){
			alert(document.forms[0].errMsgPediNum.value);
			document.forms[0].pedinumber.value = "";
			document.forms[0].custagent.value = "";
			showMsgOnInit = true;
			document.forms[0].pedinumber.focus();
		}
		
		var flagCCosto = document.forms[0].flagCCAssignedBrnc.value;	
		
		if (flagCCosto == "N"){
			resetClientInfo();
			resetDetail(); 
			return 
		}
		
		/*si ya hay texto capturado desde el catalogo de clientes, se le aplica readonly a los campos editables*/
		if (trim(document.forms[0].destinationnombre.value) !='' && trim(document.forms[0].destinationclave.value) !='') {
			document.forms[0].destinationnombre.readOnly = true;
			document.forms[0].destinationclave.readOnly = true;
		}
	
		if (document.forms[0].sendeMailOrigBD.value == "Y" && trim(document.forms[0].eMailOrigText.value) != '') {//AAP05
			document.forms[0].eMailOrigCheck.checked = true;//AAP05
		}//AAP05
		
		if (document.forms[0].sendeMailDestBD.value == "Y" && trim(document.forms[0].eMailDestText.value) != '') {//AAP05
			document.forms[0].eMailDestCheck.checked = true;//AAP05
		}//AAP05
		
		validationTypeServicesSEG();
		/*habilita / deshabilita seccion de captura de detalle*/
		disableAdding();
		
		/*habilita / deshabilita selección de sucursal ocurre*/
		changeDeliveryOcurre("init");
		changeSelectedAddr(document.forms[0].brnchOcurre.value);
		
		/*Valida rad origen 70*/
		allowRadOrgnZp();
		
		/* Validación de errores al generar guía */
		valCcpProdErr();
		
		if (document.forms[0].destinationcode.value.substring(3) == "70" && document.forms[0].formaPago.value == 'TO_PAY') {//AAP04
			var str = 'NO SE PERMITE FLETE POR COBRAR A ZONA *PLUS.\n';
			alert(str);
			document.forms[0].formaPago.value = 'PAID';
			document.forms[0].formaPago.selected = true;
			returnFlag = true;
		}
		reloadFromBackButton();
		if (returnFlag){
			return;
		}
	}
	
	function reloadFromBackButton(){
		// Check if the browser supports the PerformanceNavigationTiming interface
		if (window.performance && window.performance.getEntriesByType) {
		  // Get the navigation entries
		  var navigationEntries = window.performance.getEntriesByType('navigation');

		  if (navigationEntries.length > 0) {
		    // Get the first navigation entry
		    var navigationEntry = navigationEntries[0];

		    // Check the type of navigation
		    if (navigationEntry.type === 'back_forward') {
		    	window.top.location = window.top.location;
		    }
		  }
		}
	}
	
	/*Función para validar si permite RAD zona extendida*/
	function allowRadOrgnZp(){
		if(document.forms[0].orginbranchcode.value.substring(3) == '70' && document.forms[0].allowRadZe.value == 'N'){
			alert("NO CUENTA CON PERMISO DE DOCUMENTAR RAD ZP");
			let selectElement = document.forms[0].centrosCosto;
			let selectedDefault = document.forms[0].centrosCostoDefault.value;
			let selectedIndex = Array.from(selectElement.options).findIndex(option => option.value === selectedDefault);
			document.forms[0].centrosCosto.options.selectedIndex=selectedIndex;
			showBarraEspera('mensaje','body','Espere por favor...Saliendo de documentacion');
			document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page=mainpage";
			document.forms[0].submit();
			return;
		}
	}
	
	function valAddrWoCvge(){
		if (document.forms[0].validCCAddrCvge.value=="N"){
			showToast("error", "Direcci\u00F3n de centro de costos no estructurada (sin cobertura).", 6000);
			return true;
		}
		return false;
	}
	
	/*funcion para limitar el maximo de caracteres de un objeto*/
	function ismaxlength(obj, limCaracteres) {
		if (obj.value.length > limCaracteres) {
			obj.value = obj.value.substring(0, limCaracteres);
		}
	}
	
	function goOut(page){
		if (document.getElementById('mensaje').style.visibility=='hidden') {
			if (confirm("DESEA PERDER LA INFORMACION?")) {
				showBarraEspera('mensaje','body','Espere por favor...Saliendo de documentacion');
				document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page="+page;
				document.forms[0].submit();
			} else {
				return;
			}
		}
	}
	
	function validaSelect(obj) {
		if (document.getElementById('mensaje').style.visibility=='hidden') {
			var value = trim(obj.value);
			if (value.length!=0) {
				if (document.forms[0].destinationnombre.readOnly) {
					setBack('true');//DESHABILITA TECLA DE RETROCESO
					obj.select();
				} else {
					setBack('false');//HABILITA TECLA DE RETROCESO					
				}
			}
		}
	}
	
	function textoSeleccionado() {
		if (document.forms[0].destinationnombre.readOnly) {
			if (confirm('\u00BFDesea cambiar cliente destino?')) {
				resetClientInfo();
				resetDetail();				
			}
		}
	}
    
    function resetClientInfo(){
        document.forms[0].destinationclave.value = "";
        document.forms[0].destinationnombre.value = "";
        document.forms[0].destino1.value = "";
        document.forms[0].destino2.value = "";
        document.forms[0].destinationcolonia1.value = "";
        document.forms[0].destinationcolonia2.value = "";
        document.forms[0].destinationtelefono.value = "";
        document.forms[0].destinationcode.value = "";
        document.forms[0].destinationbranch.value = "";
        document.forms[0].destinationcodeIni.value = "";//AAP06
        document.forms[0].destinationbranchIni.value = "";//AAP06
        document.forms[0].destinationsite.value = "";
        document.forms[0].destinationsitecode.value = "";
        document.forms[0].destinationnombre.readOnly = false;
        document.forms[0].destinationclave.readOnly = false;
        document.forms[0].brncVrtl.value = "";
        document.forms[0].shippingType.value = "";
        document.forms[0].valordeclarado.value = 0;
        document.forms[0].destinationrfc.value = "";
    	document.forms[0].pedinumber.value = "";
		document.forms[0].custagent.value = "";
		document.forms[0].errMsgAdditional.value = '';
        if (document.forms[0].checkTelDir.value == 'Y' || document.forms[0].checkTelDir.value == 'V') {//AAP03
            document.forms[0].destinationtelefono.readOnly = true;//AAP03
            document.forms[0].checkTelDir.value = '';
        }
        if (document.forms[0].checkRefDir.value == 'Y' || document.forms[0].checkRefDir.value == 'V') {//AAP03
            document.getElementById('divRefDom').style.visibility='hidden';//AAP03
            document.getElementById('divRefDom').style.position='absolute';//AAP03
            document.forms[0].destinationRefDom.value = '';//AAP03
            document.forms[0].checkRefDir.value = '';//AAP03
        }//AAP03
    }

	/*************************************************************
	 *funciones utilizadas para la seccion de captura de detalle *
	 *************************************************************/
	function disableAdding() {		
		var hitCount = document.forms[0].hitCount.value;
		var accion = document.forms[0].accion.value;
		var deshabilitarDet = false;//bandera para deshabilitar objetos de captura de detalle
		var deshabilitarSer = false;//bandera para deshabilitar objetos de calculo de servicios
		if (accion == '') {
			deshabilitarDet = true;
			deshabilitarSer = true;
		} else {
			/*se habilita referencia de domicilio (solo para zona extendida, definido desde el catalogo de clientes)*/
			if (document.forms[0].checkRefDir.value == 'Y' || document.forms[0].checkRefDir.value == 'V') {
				document.getElementById('divRefDom').style.visibility='visible';
				document.getElementById('divRefDom').style.position='static';
				
				if (document.forms[0].checkRefDir.value=='V') {
					document.forms[0].destinationRefDom.readOnly = true;//AAP03
				}
			}
			/*se habilita telefono (solo para zona extendida, definido desde el catalogo de clientes)*/
			if (document.forms[0].checkTelDir.value == 'Y' ) {
				document.forms[0].destinationtelefono.readOnly = false;//AAP03	
			} else if (document.forms[0].checkTelDir.value=='V') {
				document.forms[0].destinationtelefono.readOnly = true;//AAP03	
			}
			
			if (hitCount == 6){
				deshabilitarDet = true;
			} else {
				document.forms[0].shippingType.focus();
				if (hitCount == 0) {
					deshabilitarSer = true;
				}
			}
			
			if (accion =='init') {
				document.forms[0].actionfordup.value='add';							
			} else if (accion == 'Agregar') {
				document.forms[0].actionfordup.value='add';
				resetValuesDetail();
			} else if (accion == 'Editar') {
				document.forms[0].actionfordup.value='edit';
				resetValuesDetail();
			} else {
				document.forms[0].actionfordup.value='add';
			}
		}
		
		if (deshabilitarDet) {
			disabledObjectsDetail(deshabilitarDet);			
		}		
		
		/*condiciones para validacion de tarifa especial o tarifa de piso al cargar la pantalla*/
		var defaultservicescreen = document.forms[0].defaultservicescreen.value;
		var clasifTarif = document.forms[0].clasifTarif.value;//AAP02 VARIABLE PARA INDICAR SI LA TARIFA ES ANTERIOR (0) O NUEVA (1)
		if (defaultservicescreen == 'yes' && clasifTarif == '0') {
			if (accion == 'edit') {
				if (document.forms[0].ss_srvc_id.value == 'SHP-E' && document.forms[0].isShippingTypeSEG.value == 'N') {
					document.forms[0].volumen.readOnly = true;				
				}
			}		
			ocultaInfoTarifa();
		} else {
			var calculationdone = document.forms[0].calculationdone.value;

			if (calculationdone == 'weight') {
				alert("LA TARIFA ES APLICADA SOBRE EL PESO");
			} else if ( calculationdone == 'volume') {
				alert("LA TARIFA ES APLICADA SOBRE EL VOLUMEN");
			}
			document.forms[0].calculationdone.value = '';			
		}
		
		/*validacion para ocultar los importes*/
		if (document.forms[0].displayAmountFlag.value == 'N') {
			ocultaInfoImportes(hitCount);
		}
		
		/*validaciones para seccion de calculo de servicios*/
		if (deshabilitarSer) {
			disabledObjectsServices(deshabilitarSer);
		}
		if (hitCount>0){
			initScriptServicios();
		}	
	}
	
	/*Funcion que oculta la informacion de tarifa*/
	function ocultaInfoTarifa() {
		/*oculta la captura de la tarifa*/
		document.getElementById('infTarifa1').style.visibility = 'hidden';
		document.getElementById('infTarifa3').style.visibility = 'hidden';
		
		/*oculta las celdas de la tarifa en la tabla*/
		var tabla = document.getElementById('tablaDetalle');
		var reng = tabla.rows;
		var contRow = 0;
		for (var rowDetail of reng){
			if (contRow == 0){
				rowDetail.deleteCell(5);
				rowDetail.insertCell(5);
				rowDetail.cells[5].width = '1%';
				rowDetail.cells[2].width = '48%';
				contRow++;
			}else{
				rowDetail.deleteCell(6);
				rowDetail.insertCell(6);
				rowDetail.cells[6].width = '1%';
			}
		}
	}//fin ocultaInfoTarifa	
	
	/*Funcion que oculta la informacion de importes*/
	function ocultaInfoImportes(hitCount){
		/*oculta las celdas del importe en la tabla*/
		var tabla = document.getElementById('tablaDetalle');
		var reng = tabla.rows;
		if (hitCount != 0) {
			for (var i=1;i<=hitCount;i++) {
				reng[i].cells[13].innerHTML = "**********";
			}
			/*oculta importes de calculo de servicios*/
			ocultaInfoImportesServicios("**********");
		}		
	}//fin ocultaInfoImportes
	
	function submitForm(obj) {
		if (document.getElementById('mensaje').style.visibility=='hidden') {
			var objValue = obj.value;
			var cantidad = trim(document.forms[0].cantidad.value);
			var descripcion = trim(document.forms[0].descripcion.value);
			var contenido = trim(document.forms[0].contenido.value);
			var tarifa  =trim(document.forms[0].tarifa.value);
			var peso = trim(document.forms[0].peso.value);
			var volumen = trim(document.forms[0].volumen.value);
			
			var defaultservicescreen = document.forms[0].defaultservicescreen.value;		
			var tarifType = document.forms[0].tarifType.value;
			var clasifTarif = document.forms[0].clasifTarif.value;//AAP02 VARIABLE PARA INDICAR SI LA TARIFA ES ANTERIOR (0) O NUEVA (1)
			
			var tabla = document.getElementById('tablaDetalle');
			var reng = tabla.rows;
			
			/*validaciones de pantalla informacion general*/	
			if (document.forms[0].checkTelDir.value == 'Y' ){//AAP03
				if (trim(document.forms[0].destinationtelefono.value)=='') {//AAP03
					alert("CAPTURE TELEFONO DE DOMICILIO DESTINO");
					document.forms[0].destinationtelefono.focus();
					return;
				}//AAP03
			}//AAP03
			if (document.forms[0].checkRefDir.value == 'Y') {//AAP03
				if (trim(document.forms[0].destinationRefDom.value) == '') {//AAP03
					alert("CAPTURE REFERENCIA DE DOMICILIO DESTINO");//AAP03
					document.forms[0].destinationRefDom.focus();//AAP03
					return;//AAP03
				}//AAP03
			}//AAP03
			/*fin validaciones de pantalla informacion general*/
			var isSobre = document.forms[0].descripcion.value == 'SOBRE' ? 'Y' :'N'
			if (isSobre == 'Y') {
				if (!validaValoresMaximosSEG(1)) {
					return;
				}
			} else {
				if (!validaValoresMaximosSEG(2)) {
					return;
				}
			}
			if (defaultservicescreen == 'yes') {
				if (tarifType == 'C' && isSobre != 'Y') {
					if (peso > 0  &&  volumen == 0 ) {
					   alert("SE REQUIERE VOLUMEN");
					   return;
					}
					if (volumen > 0  &&  peso == 0 ) {
						alert("SE REQUIERE PESO");
						return;
					}
				}
				
				/*validacion que solo aplica para las tarifas especiales anteriores*/
				if (defaultservicescreen == 'yes' && clasifTarif == '0') {
					if (parseFloat(peso) < parseFloat(document.forms[0].pesoDB.value) && parseFloat(volumen) < parseFloat(document.forms[0].volumenDB.value)){
						if (parseFloat(peso) < parseFloat(document.forms[0].pesoDB.value)){
							alert("Peso capturado ["+peso+"] debe ser Mayor o igual a Peso convenido ["+document.forms[0].pesoDB.value+"]");
							return;
						}
						if (parseFloat(volumen) < parseFloat(document.forms[0].volumenDB.value)){
							alert("Volumen capturado ["+volumen+"] debe ser Mayor o igual a Volumen convenido ["+document.forms[0].volumenDB.value+"]");
							return;
						}
					}	
				}
			}
					
			var weightVolumetricValue = document.forms[0].weightVolumetric.value;
			var l = parseFloat(weightVolumetricValue);
		
			var errorL = isNaN(l);
			if (errorL) {
				l = 0;
			}
			
			if(cantidad==''){
				alert("CAPTURE UN VALOR PARA 'CANTIDAD'");
				document.forms[0].cantidad.focus();
				return;
			}else if(descripcion==''){
				alert("CAPTURE UN VALOR PARA 'DESCRIPCION'");
				document.forms[0].lovbutton1.focus();
				return;
			}else if(contenido=='') {
				alert("CAPTURE UN VALOR PARA 'CONTENIDO'");
				document.forms[0].contenido.focus();
				return;
			}else if((defaultservicescreen == 'no' || defaultservicescreen == 'yes' && clasifTarif == '1') && (tarifa=='' && document.forms[0].shippingType.value == 'STD-T' && l <= 0)){
				alert("CAPTURE UN VALOR PARA 'TARIFA'");
				document.forms[0].lovbutton2.focus();
				return;
			} else if(document.forms[0].productIdSat.value == ""){
				alert("FAVOR DE SELECCIONAR UN PRODUCTO DEL CATALOGO");
				document.forms[0].productDescSat.focus();
				return;
			} else if ((peso=='' || parseFloat(peso) == 0) && document.forms[0].ss_srvc_id.value != 'SHP-E' && document.forms[0].tarifDefaultChck.value == 'false') {
				alert("CAPTURE UN VALOR PARA PESO.");
				document.forms[0].peso.value="";
				document.forms[0].peso.focus();
				return;
			}else if((volumen=='' || parseFloat(volumen) == 0) && document.forms[0].ss_srvc_id.value != 'SHP-E' && document.forms[0].tarifDefaultChck.value == 'false'){
				alert("CAPTURE UN VALOR PARA VOLUMEN.");
				document.forms[0].volumen.value="";
				document.forms[0].volumen.focus();
				return;
			}		
			
			if (isNaN(peso)) {
				alert("CAPTURE SOLO NUMEROS EN EL CAMPO 'PESO'");
				document.forms[0].peso.select();
				return;
			} else if (isNaN(volumen)) {
				alert("CAPTURE SOLO NUMEROS EN EL CAMPO 'VOLUMEN'");
				document.forms[0].volumen.select();
				return;
			}
			if (tarifa=='T7') {
				if(parseFloat(peso)<61 && parseFloat(volumen)<0.32){
					alert("PESO Y VOLUMEN NO DEBEN DE SER MENORES QUE 61 Y 0.32 RESPECTIVAMENTE");
					return;
				}
				if (document.forms[0].orginbranchcode.value.includes("70") && document.forms[0].allowRadZeT7.value == 'N'){
					alert("NO CUENTA CON PERMISO DE DOCUMENTAR RAD ZP T7");
					document.forms[0].tarifa.focus();
					return;
				}
			}
			
			//Valida valores mínimos
			if (!validaValoresMinimos(isSobre)){
				return;
			}
			
			let existTarifasR = false, existMedidasR = false;
			let toRTarifa = false, toRMedidas = false;
			var l = 0;
			var w = 0;
			var h = 0;
			/*Revisa si hay líneas de envios de un tipo*/
			for(var j=0;j<reng.length-1;++j){				
				if (reng[j].cells[9].innerHTML != '&nbsp;' && reng[j].cells[8].innerHTML != '<p align="right">Largo</p>'){
					l = parseFloat(reng[j].cells[9].innerHTML);
					w = parseFloat(reng[j].cells[11].innerHTML);
					h = parseFloat(reng[j].cells[10].innerHTML);
				}
				if((l > 0 || w > 0 || h > 0) || document.forms[0].tarifType.value == "C"){
					existMedidasR = true;
				}else if((reng[j].cells[9].innerHTML =="0" || reng[j].cells[10].innerHTML =="0" ||
						reng[j].cells[11].innerHTML =="0") && reng[j].cells[6].innerHTML != "S"){
					existTarifasR = true;
				}
			}
			/*Flag de la línea de envio a registrar*/
			if (document.forms[0].tarifa.value != "S" && document.forms[0].tarifa.value != ''){
				toRTarifa = true;
			}
			if (parseFloat(document.forms[0].volL.value) > 0 && parseFloat(document.forms[0].volW.value) > 0 && parseFloat(document.forms[0].volH.value) > 0){
				toRMedidas = true;
			}
			/*	Validación para que no ingrese líneas de envio
			 *	con tarifas o medidas al mismo tiempo
			 **/
			if (toRMedidas){//Registrar con medidas
				if (existTarifasR){//Ya cuenta con registros de tarifas y no permite avanzar
					alert("CONTINUE CAPTURANDO ENVIOS ESPECIFICANDO TARIFAS");
					return;
				}
			}else if (toRTarifa){//Registrar tarifa
				if (existMedidasR){//Ya cuenta con registros de medidas y no permite avanzar
					alert("CONTINUE CAPTURANDO ENVIOS ESPECIFICANDO MEDIDAS");
					return;
				}
			}
			
			if (!validateMaxQty()){
				document.forms[0].cantidad.focus();
				return;
			}
			
			if (document.forms[0].checkRefDir.value == 'Y') {//AAP03
				document.forms[0].checkRefDir.value = 'V';//campo validado
				document.forms[0].destinationRefDom.readOnly = true;//AAP03			
			}//AAP03
			
			if (document.forms[0].checkTelDir.value == 'Y' ){//AAP03
				document.forms[0].checkTelDir.value = 'V';//campo validado
				document.forms[0].destinationtelefono.readOnly = true;//AAP03
			}//AAP03
			
			if (!validateProductDes()){
				return;
			}
			
			showBarraEspera('mensaje','body','Espere por favor...Agregando detalle capturado');
			/*asigna valor a hidden accion para utilizarlo en beanAction de la forma*/
			document.forms[0].accion.value = objValue;
	
			document.forms[0].action='webBookinggeneralMain.do';
			document.forms[0].submit();
		}
	}	
	
	function disabledObjectsDetail(band) {		
		document.forms[0].formaPago.disabled = band;
		document.forms[0].addbutton.disabled = band;
		document.forms[0].lovbutton1.disabled = band;
		document.forms[0].lovbutton2.disabled = band;
		document.forms[0].cantidad.disabled = band;
		document.forms[0].descripcion.disabled = band;
		document.forms[0].contenido.disabled = band;
		document.forms[0].tarifa.disabled = band;
		document.forms[0].peso.disabled = band;
		document.forms[0].volumen.disabled = band;
		document.forms[0].editbutton.disabled = band;
		document.forms[0].deletebutton.disabled = band;
		document.forms[0].productDescSat.disabled = band;
		document.forms[0].opcOcurre.disabled = band;
		document.forms[0].brnchOcurre.disabled = band;
	}
	
	function resetValuesDetail(){
		let disableValues = document.forms[0].cantidad.disabled;
		if (disableValues){
			disabledObjectsDetail(false);
		}
		document.forms[0].cantidad.value = "";
		document.forms[0].descripcion.value = "";
		document.forms[0].contenido.value = "";
		document.forms[0].tarifa.value = "";	
		document.forms[0].peso.value = "";
		document.forms[0].volumen.value = "";
		document.forms[0].volH.value = "";
		document.forms[0].volW.value = "";
		document.forms[0].volL.value = "";
		document.forms[0].weightVolumetric.value = "";
		document.forms[0].productDescSat.value = "";
		document.forms[0].productIdSat.value = "";
		document.forms[0].opcOcurre.checked = false;
		document.forms[0].brnchOcurre.value = "";
		document.forms[0].entrega.value = 2;
		document.forms[0].defaultBrnchAddr.value = "";
		document.getElementById("addr").textContent = "";
		document.forms[0].errMsgAdditional.value = "";
		document.forms[0].flagValidProductId.value = "N";
		document.forms[0].validFiscal.value = "N";
		if (disableValues){
			disabledObjectsDetail(true);
		}
	}
	
	function deleteRow(obj){
		var objValue = obj.value;
		if (document.getElementById('mensaje').style.visibility=='hidden') {
			var checkstatus = false;
			for (let rowDetail of document.forms[0].radioselect){
				var checkedValue = rowDetail.checked;
				if(checkedValue) {
					checkstatus=true;
				} else {
					continue;
				}
			}
			if(!checkstatus) {
				alert("FAVOR DE SELECCIONAR UN REGISTRO A BORRAR");
				return;
			}
			
			if(confirm("DESEA BORRAR EL REGISTRO SELECCIONADO?")) {
				showBarraEspera('mensaje','body','Espere por favor...Eliminando Registro');
				/*asigna valor a hidden accion para utilizarlo en beanAction de la forma*/
				document.forms[0].accion.value = objValue;
				
				document.forms[0].action = 'webBookinggeneralMain.do';
				document.forms[0].submit();		
			}
		}
	}
	
	/*limpia captura de detalle cuando selecciona un renglon de la tabla*/
	function selectRenglon() {
		/*si son 6 renglones capturados.. deshabilita los campos */			
		if (document.forms[0].hitCount.value == 6){
			disabledObjectsDetail(true);
		}
		resetValuesDetail();
		document.forms[0].editbutton.disabled = false;
		document.forms[0].deletebutton.disabled = false;
		document.forms[0].addbutton.value = "Agregar";
	}
	
	function editRow(obj) {
		var checkstatus = false;
		var objValue = obj.value;
		if (document.getElementById('mensaje').style.visibility == 'hidden') {
			for(var i=0;i<document.forms[0].radioselect.length;i++){
				var checkedValue = document.forms[0].radioselect[i].checked;
				if(checkedValue) {
					checkstatus=true;
					mostrarRenglon(i+1);
					
					/*asigna valor a hidden accion para utilizarlo en beanAction de la forma*/
					document.forms[0].accion.value = objValue;		
				} else {
					continue;
				}
			}
			if(checkstatus){
				/*si son 6 renglones capturados.. habilita los campos para editar 
				 * el renglon seleccionado*/			
				if (document.forms[0].hitCount.value == 6){
					disabledObjectsDetail(false);
				}
				
				if (document.forms[0].tarifa.value !='T7') {
					document.forms[0].peso.readOnly = true;
					document.forms[0].volumen.readOnly = true;
				}
				if (document.forms[0].isShippingTypeSEG.value == 'Y' ||  Number(document.forms[0].weightVolumetric.value) > 0 || document.forms[0].forceCaptureDimensions.value == 'Y') {
					document.forms[0].peso.readOnly = false;
					document.forms[0].volumen.readOnly = true;
				}
				disabledObjectsServices(true);
			} else {
				alert("FAVOR DE SELECCIONAR UN REGISTRO A EDITAR");
				return;
			}
		}
	}
	
	/*Funcion que muestra el renglon seleccionado en los campos de captura de detalle*/
	function mostrarRenglon(index){
		var tabla = document.getElementById('tablaDetalle');
		var reng = tabla.rows;		
		
		document.forms[0].cantidad.value = trim(reng[index].cells[0].innerHTML);
		document.forms[0].descripcion.value = trim(reng[index].cells[1].innerHTML);
		document.forms[0].contenido.value = trim(reng[index].cells[2].innerHTML);
		document.forms[0].productIdSat.value = trim(reng[index].cells[3].innerHTML);
		document.forms[0].productDescSat.value = trim(reng[index].cells[4].innerHTML);
		//Se recorre una agregar el campo descripción
		document.forms[0].tarifa.value = trim(reng[index].cells[6].innerHTML);
		document.forms[0].peso.value = trim(reng[index].cells[7].innerHTML);
		document.forms[0].volumen.value = trim(reng[index].cells[8].innerHTML);
		document.forms[0].volL.value = trim(reng[index].cells[9].innerHTML);
		document.forms[0].volH.value = trim(reng[index].cells[10].innerHTML);
		document.forms[0].volW.value = trim(reng[index].cells[11].innerHTML);
		document.forms[0].weightVolumetric.value = trim(reng[index].cells[12].innerHTML);
		
		document.forms[0].editbutton.disabled = true;
		document.forms[0].deletebutton.disabled = true;
		document.forms[0].addbutton.disabled = false;
		document.forms[0].addbutton.value = "Editar";
		document.forms[0].actionfordup.value = "edit";
	}//fin mostrarRenglon
	
	
	/*************************************************************
	 *funciones utilizadas para la seccion de servicios			 *
	 *************************************************************/
	function resetServices() {
		ocultaInfoImportesServicios("");
		disabledObjectsServices(true);
		/*limpia la variable accionServices para no ejecutar nada en la parte del beanAccion*/
		document.forms[0].accionServices.value = '';
		/*limpia variable que muestra la captura de los servicios adicionales, para que vuelva a ser validado*/
		document.forms[0].showAdditional.value = '';
		
		document.forms[0].valorcod.value = '';//AAP03
		
		document.forms[0].formaPago[0].selected = true;//AAP04
		document.forms[0].formaPago.disabled = true;//AAP04
	}
	
	/* **************************************************
	 * oculta importes de calculo de servicios			*
	 ****************************************************/
	function ocultaInfoImportesServicios(str) {
		document.getElementById('importeFlete').innerHTML = str;
		document.getElementById('importeDescuento').innerHTML = str;
		document.getElementById('importeServicios').innerHTML = str;
		document.getElementById('importeSubTotal').innerHTML = str;
		document.getElementById('importeIva').innerHTML = str;
		document.getElementById('importeIvaRet').innerHTML = str;
		document.getElementById('importeTotal').innerHTML = str;
		
		if (document.forms[0].showAdditional.value == "Y" && str == "") {
			document.forms[0].serviceAdditional.value = str;
			document.forms[0].importeValue.value = str;
			borrarTablaServAdi();
		}
	}
	
	function disabledObjectsServices(band) {
		document.forms[0].formaPago.disabled = band;
		document.forms[0].entrega.disabled = band;
		document.forms[0].acusederecibo.disabled = band;
		document.forms[0].valorcod.disabled = band;
		document.forms[0].valordeclarado.disabled = band;
		document.forms[0].cobertura.disabled = band;		
		document.forms[0].Calculate.disabled = band;
		document.forms[0].generale.disabled = band;
		document.forms[0].servicios.disabled = band;
		document.forms[0].comments.disabled = band;
		document.forms[0].reference.disabled = band;
		document.forms[0].seguro.disabled = band;
		
		if (document.forms[0].showAdditional.value == "Y") {
			document.forms[0].servicebut.disabled = band;
			document.forms[0].AgregarServ.disabled = band;			
		}
	}
	
	/*Funcion que elimina el detalle de los servicios adicionales*/
	function borrarTablaServAdi(hitCount){
		var tabla = document.getElementById('tablaAdicionales');
		var reng = tabla.rows;
		for (var i=0;i<3;i++) {
			for(var j=0;j<reng.length-1;j++){
				if(j == reng.length ){
					alert(reng[i].cells[j].innerHTML);
				} else {
					reng[i].cells[j].innerHTML = "";
				}
			}
		}
	}//fin borraren
	
	function initScriptServicios(){	
		/*se asigna la variable accion como OK para que no realize procesos en la captura de detalle
		 * cuando se manipule la seccion de calculo de servicios*/
		document.forms[0].accion.value = 'serviceDetailOK';
		if (document.forms[0].accionServices.value=='showAditional') {			
			document.getElementById('PantAdicionales').style.visibility='visible';
			document.getElementById('PantAdicionales').style.position='static';
		}	

		/*condicion para validar si permite habilitar el boton de servicios adicionales*/
		if (document.forms[0].aditionalFlag.value == 'N' || document.getElementById('PantAdicionales').style.visibility == 'visible') {
			document.forms[0].servicios.disabled = true;
		}
		
		/* deshabilita captura de COD si el destino es zona extendida*///AAP03
		if (document.forms[0].destinationcode.value.substring(3) == "70") {//AAP03
			document.forms[0].valorcod.value = '';//AAP03
			document.forms[0].valorcod.readOnly = true;//AAP03
		} //AAP03
		
		
		//se habilita campo flete por cobrar.//AAP04
		if (document.forms[0].allowedFXC.value == 'Y') {//AAP04
			document.forms[0].formaPago.disabled = false;//AAP04
		}//AAP04
		
		if (document.forms[0].eMailOrigCheck.checked) {
			document.forms[0].eMailOrigText.readOnly = false;
		}
		
		if (document.forms[0].eMailDestCheck.checked) {
			document.forms[0].eMailDestText.readOnly = false;
		}
		
		/*VERIFICA SI HAY LISTA DE REFERENCIAS PARA MOSTRAR EN PANTALLA*/
		add_all_li();
		
		var duplicateguianumber = document.forms[0].duplicateguianumber.value;
		var successmessage = document.forms[0].successmessage.value;
		var confirmgenerate = document.forms[0].confirmgenerate.value;	
		var errmsgenvelope = document.forms[0].errmsgenvelope.value;
		let pedimentoErrMsg = document.forms[0].errMsgPediNum.value;

		if (document.forms[0].errMsgPediNum.value!=''){
			document.forms[0].pedinumber.focus();
			return;
		}
		
		if(errmsgenvelope!=''){
			if(confirm(errmsgenvelope)){
				document.forms[0].valordeclarado.value = "";
				document.forms[0].valordeclarado.readOnly = true;
				document.forms[0].seguro.value = "";
				document.forms[0].errmsgenvelope.value = "";
				return;
			}else{
				document.forms[0].errmsgenvelope.value = "";
				return;
			}
		}

		if(duplicateguianumber!=''){
			alert(duplicateguianumber);
			document.forms[0].duplicateguianumber.value="";
		}
		if (parseFloat(document.forms[0].valordeclarado.value) > parseFloat(document.forms[0].maxDeclAmnt.value)){
			return;
		}
		if(confirmgenerate=='yes' && document.forms[0].flagValidProductId.value == 'Y'){
			//Added R.Mohan Babu in order to disable all the text boxes after the generar is clicked.	
			document.forms[0].guiano.readOnly = true;	
			
			if(confirm("DESEA CONFIRMAR LA GENERACION DE LA GUIA?")){
				submitFormMainServices('generate', 'Espere por favor... Generando gu\u00EDa','isNewBooking=Y');
			}else{
				document.forms[0].confirmgenerate.value = "";
				document.forms[0].accionServices.value = '';
				return;
			}
		}

		if(successmessage!='') {
			alert(successmessage);			
		}

		if (document.forms[0].valorvalue.value == "N") {
			document.forms[0].valordeclarado.readOnly=true;	
		}		
	}
	
	function changeBranch(obj) {
		var permitirCambio = true;
		if (document.getElementById('mensaje').style.visibility=='hidden') {			
			if (document.forms[0].destinationcode.value.substring(3) == "70") {//AAP03
				var indice = obj.selectedIndex;//AAP03
				if (obj[indice].value == '1') {//AAP03
					alert('NO SE PERMITE ENVIO OCURRE A ZONA PLUS.');//AAP03
					permitirCambio = false;//AAP03
					for (let option of obj) {
						if (option.value == '2') {
							option.selected = true;
						}//AAP03
					}//AAP03
				}//AAP03
			}//AAP03
			if (permitirCambio) {//AAP03
				submitFormMainServices('checkbranch','Validando Sucursal','');	
			}//AAP03
		}
	}
	
	function changeDeliveryOcurre(str) {
		if (document.forms[0].entrega.value == 1) {
			document.forms[0].opcOcurre.disabled = false;
			document.forms[0].brnchOcurre.disabled = false;
			if (str != "init"){
				if (document.forms[0].opcOcurre.checked){
					submitFormMainServices('checkbranchtrue','Buscando sucursales ocurre','');
				}else{
					submitFormMainServices('checkbranchfalse','Buscando sucursales ocurre','');
				}
			}
		} else {
			document.forms[0].opcOcurre.disabled = true;
			document.forms[0].brnchOcurre.disabled = true;
			document.getElementById('btnSucursales').style.visibility="hidden";
		}
	}
	function changeSelectedAddr(str){
		var tmp = str.split("|");
		var addr = "Direcci\u00F3n : " + tmp[2];
		if ((typeof tmp[2] !== 'undefined')){
			document.getElementById("addr").textContent = unescape(addr);
		}else if (document.forms[0].entrega.value == "1" && str == ""){
			document.getElementById("addr").textContent = "Direcci\u00F3n : " + unescape(document.forms[0].defaultBrnchAddr.value.split("^")[0]);
		}
	}
	function brnchOnMap(str){
		var tmp = str.split("|");
		if((typeof tmp[3] !== 'undefined')){
			window.open("https://www.google.com/maps/search/?api=1&query=" + tmp[3], "Sucursal "+tmp[0], "_blank");
		}else{
			window.open("https://www.google.com/maps/search/?api=1&query=" + document.forms[0].defaultBrnchAddr.value.split("^")[1], "Sucursal", "_blank");
		}
	}
	
	function submitFormServicesAditional(obj) {
		var objName = obj.value;
		if (document.getElementById('mensaje').style.visibility=='hidden') {
			if(objName == 'Agregar') {
				if( document.forms[0].serviceAdditional.value != null && 
					trim(document.forms[0].serviceAdditional.value).length >0) {
					if (trim(document.forms[0].importeValue.value).length >0){						
						if (parseFloat(document.forms[0].importeValue.value)>0) {
							submitFormMainServices('addService','Agregando Servicio Adicional','');
						} else {
							alert("Capture valor mayor 0.00");
						}							
					} else {
						alert("Capture valor valido en importe");
					}					
		 	    } else {
					alert("Seleccione el servicio adicional");
				}
			}
		}
	}
	
	// Deleting 
	function doDelete(index){
		if (document.getElementById('mensaje').style.visibility=='hidden') {
			submitFormMainServices('deleteService','Eliminando Servicio Adicional','index='+index);
		}
	}
	
	function submitFormValorDeclarado(obj) {
		if (document.getElementById('mensaje').style.visibility=='hidden') {
			var val = parseFloat(obj.value);
			if (val == 0) {
				obj.value = "";
			}
			if (parseFloat(document.forms[0].valordeclarado.value) > parseFloat(document.forms[0].maxDeclAmnt.value)){
				alert("Monto M\u00E1ximo a Capturar de Valor Declarado para este Envio: $"+document.forms[0].maxDeclAmnt.value);
				document.forms[0].valordeclarado.focus();
				return;
			}
			/*se asigna el valor 'cobertura' para obtener el cambio de cobertura en el beanAction*/			
			submitFormMainServices('insuranceamount','Calculando Costo de Valor Declarado','');			
		}
	}
	
	function submitFormMainServices(accion, mensaje, params) {
		showBarraEspera('mensaje','body', mensaje);
		document.forms[0].accionServices.value = accion;
		document.forms[0].action = 'webBookinggeneralMain.do?'+params;
		document.forms[0].submit();
	}
	
	function getIndiceActual(){
		var comboCobertura = document.forms[0].cobertura;
		document.forms[0].indiceActual.value = comboCobertura.selectedIndex; 
	}
	
	function setSeguro(obj){
		var comboCobertura = document.forms[0].cobertura;
		if (document.getElementById('mensaje').style.visibility == 'hidden') {
			if (document.forms[0].valordeclarado.value == ''){
				alert("CAPTURE UN VALOR PARA 'VALOR DECLARADO'");
				document.forms[0].valordeclarado.focus();
				comboCobertura.selectedIndex = document.forms[0].indiceActual.value;
				return;
			}
			
			if(isNaN(document.forms[0].valordeclarado.value)){
				alert("POR FAVOR, CAPTURE SOLO NUMEROS");
				document.forms[0].valordeclarado.select();
				comboCobertura.selectedIndex = document.forms[0].indiceActual.value;
				return;
			}		
			
			/*se asigna el valor 'cobertura' para obtener el cambio de cobertura en el beanAction*/
			submitFormMainServices('cobertura', 'Validando Cobertura', '');
		}
	}

	function generateGuia(str){

		let pedimentoErr = document.forms[0].errMsgPediNum.value;
		if(document.forms[0].acusederecibo.value=="ACK-X"){
			if(document.forms[0].formaPago.value=="TO_PAY"){
				alert("NO PUEDE ENVIAR UN ACUSE-XT FLETE POR COBRAR");
				return;
			}
			if(document.forms[0].hasEnvelope.value=="Y"){
				alert("NO PUEDE ENVIAR UN ACUSE-XT CON SOBRE");
				return;
			}
		}
		/*Validación para capturar Número de Pedimento y Agente Aduanal*/
		if (document.forms[0].pedinumber.value == "" && document.forms[0].custagent.value != "") {
			alert("Favor de capturar N\u00FAmero de Pedimento");
			document.forms[0].pedinumber.focus();
			return;
		}
		
		/* Validación de número de pedimento */
		if (document.forms[0].errMsgPediNum.value != ''){
			if (!showMsgOnInit){
				alert(pedimentoErr);
			}
			document.forms[0].errMsgPediNum.value = "";
			document.forms[0].pedinumber.focus();
			showMsgOnInit = false;
			return;
		}
		
		var hitCount = document.forms[0].hitCount.value;
		var band = false;
		if(hitCount==0){
			alert("FAVOR DE DOCUMENTAR AL MENOS UN SERVICIO");
			document.forms[0].action='webBookinggeneralMain.do?norecords=yes&screen='+screen;
			document.forms[0].submit();
			return;
		}
		var comboEntrega = document.forms[0].entrega;//AAP01
		var tipoEntrega  = comboEntrega.options[comboEntrega.selectedIndex].value;//AAP01
		var zonaExtendida = document.forms[0].zonaExtendida.value;//AAP01
		var permiteZonaExt = "";//AAP02
		var costoEntrega = "";//AAP01
		var montoMaxOl = "";
		
		if (zonaExtendida !="") {
			zonaExtendida = document.forms[0].zonaExtendida.value.substring(0,1);//AAP01
			permiteZonaExt = document.forms[0].zonaExtendida.value.substring(1,2);//AAP02
			costoEntrega = document.forms[0].zonaExtendida.value.substring(2);//AAP01
			montoMaxOl = document.forms[0].montoMaxOl.value;//AAP03
		}
		if (document.forms[0].flagValidProductId.value != 'Y' && str != 'calculate'){
			valCcpProdErr();
			if (document.forms[0].errMsgAdditional.value != null && document.forms[0].errMsgAdditional.value != ''){
				return;
			}else{
				if (str == 'generate' && document.forms[0].flagValidProductId.value != 'Y' )	document.forms[0].flagValidProductId.value = 'generate';
				validacionesFXC(document.forms[0].formaPago, 'TO_PAY_GEN');
			}
		}
		
		var msgConfirm = "";
		var msgBarraEs = "";
		if (document.getElementById('mensaje').style.visibility=='hidden') {
			if(str == "generate" ) {
				if (document.forms[0].flagValidRefrClnt.value == '1') {
					if (trim(document.forms[0].listReferences.value) == '') {
						alert("Favor de capturar al menos una Referencia");
						return;
					}
				}
				//msgConfirm = "El domicilio destino es zona extendida. Costo por unidad $"+costoEntrega+" + I.V.A. Desea generar guia?";
				msgConfirm = "El domicilio destino es zona extendida. Existe costo adicional por unidad. ¿Desea generar guia?";
				msgBarraEs = "Realizando validaciones y c\u00E1lculo de envío";
			} else {
				//msgConfirm = "El domicilio destino es zona extendida. Costo por unidad $"+costoEntrega+" + I.V.A. Desea calcular costo?";
				msgConfirm = "El domicilio destino es zona extendida. Existe costo adicional por unidad. ¿Desea calcular costo?";
				msgBarraEs = "Realizando c\u00E1lculo de envío";						
			}
			
			/*condicion para tipo de entrega " Entrega a domicilio"*/
			if (tipoEntrega == '2') {
				if (permiteZonaExt == 'N') {
					alert('Cliente no Autorizado para EAD a Zona Plus');
				} else {
					/*condicion para validar zona extendida*/
					if (zonaExtendida == 'Y') {
						/*condicion para validar tarifa*/
						if (document.forms[0].tarifaInvalida.value == "Y") {
							alert('Tarifa Invalida para EAD zona plus...\n'+ 
									'No debe capturar tarifa T7 peso igual o mayor a 61 y volumen igual o mayor a 0.32');
						} else {
							/*validacion para monto maximo asegurado (en caso de que se haya capturado.)*/
							if (document.forms[0].destinationcode.value.substring(3) == "70"){
								if (montoMaxOl=="") {///AAP03
									if(confirm(msgConfirm)) {
										band = true;
									}
								} else {
									alert('Monto M\u00E1ximo a Capturar de Valor Declarado para este Envio: $'+montoMaxOl);
								}
							}else{
								band = true;
							}
						}
					} else {
						/*valida monto cuando es EAD NORMAL con operador logistico*/
						if (montoMaxOl=="") {///AAP03
							band = true;
						} else if (montoMaxOl!=""){
							alert('Monto M\u00E1ximo a Capturar de Valor Declarado para este Envio: $'+montoMaxOl);
						}
						
					}
				}
			} else {
				band = true;
			}
			if (document.forms[0].tarifaInvalidaZp.value == "Y"){
				alert('Tarifa Invalida para RAD zona plus...\n'+ 
						'No debe capturar tarifa T7 peso igual o mayor a 61 y volumen igual o mayor a 0.32');
				band = false;
			}
			submitFormValorDeclarado(document.forms[0].valordeclarado);
			if (parseFloat(document.forms[0].valordeclarado.value) > parseFloat(document.forms[0].maxDeclAmnt.value)){
				band = false;
			}
			if (band) {
				if (emailValidate("eMailOrigCheck", "eMailOrigText", " en campo CLIENTE ORIGEN")) {//AAP05					
					if (emailValidate("eMailDestCheck", "eMailDestText", " en campo CLIENTE DESTINO")) {//AAP05
						if (document.forms[0].flagValidProductId.value == 'Y' || str == 'calculate'){
							submitFormMainServices(str, msgBarraEs,'');
						}else{
							return;
						}
					}//AAP05
				}//AAP05
			}			
		}
	}

	function showAditionalServices(){
		var hitCount = document.forms[0].hitCount.value;
		if (document.getElementById('mensaje').style.visibility=='hidden') {
			if(hitCount == 0){
				alert("FAVOR DE DOCUMENTAR AL MENOS UN SERVICIO");
				return;
			}else{
				submitFormMainServices('showAditional','Cargando Servicios Adicionales','');
			}
		}
	}
	
	function valAckCliente(obj,ackType){
		var ack = false;
		var ackXT = false;
		var indice = obj.selectedIndex;
		var formulario = document.forms[0];
		if (obj[indice].value == ackType) {
			ack=true;
		}
		console.log("Formulario.reqAcuse: " + obj[indice].value);
		if(obj[indice].value=="ACK-X"){
			ackXT = true;
			console.log("Activo la bandera: ackXT = true");
		}
		if(ackXT){
			console.log("Valido formulario.requAcuseXT.value == 'N'\nEl valor es: " + formulario.reqAcuseXT.value=="N");
			if(formulario.reqAcuseXT.value=="N"){
				alert('Cliente no tiene requerimientos de acuse XT registrados... \n Favor de comunicarse con su Ejecutivo Paquetexpress para el registro de la informaci\u00F3n');
				for (let option of obj){
					if (option.value == 'ACK-N'){
						option.selected = true;
					}
				}
			}
			else{
				ackXT = true;
			}
		}
		
		if (ack ) {
			
			if (formulario.reqAcuse.value == 'N') {
				alert('Cliente no tiene requerimientos de acuse registrados... \n Favor de comunicarse con su Ejecutivo Paquetexpress para el registro de la informaci\u00F3n');
				for (let option of obj){
					if (option.value == 'ACK-N'){
						option.selected = true;
					}
				}
			}
			
		} else {
			ack = true;
		}		
	}
	
	function validacionesFXC(obj, modoPagoType) {//AAP04
		var reset = false;
		var indice = obj.selectedIndex;
		var str = '';
		/*flete por cobrar*/
		if (modoPagoType == 'TO_PAY_GEN'){
			if (obj[indice].value == 'TO_PAY' && document.forms[0].flagValidProductId.value == 'N'){
				submitFormMainServices('validateFxcCcp','Validando FXC','');	
			}else{
				submitFormMainServices('validateFxcCcp','Validando productos','');
			}
		}else if (modoPagoType != 'init'){
			if (obj[indice].value == modoPagoType) {//AAP04
				//ack=true;
				if (document.forms[0].destinationcode.value.substring(3) == "70") {//AAP04
					str = 'NO SE PERMITE FLETE POR COBRAR A ZONA PLUS.\n';
					reset = true;
				} //AAP04
				
				if (document.forms[0].permiteDestino.value == "N") {//AAP04
					str = str +'CLIENTE DESTINO NO PERMITE FLETE POR COBRAR.\n';
					reset = true;
				} //AAP04
				
				if (document.forms[0].isSoloSobre.value == "Y") {//AAP04
					str = str +'EL ENVIO DE SOBRES TIENE QUE SER PAGADO EN ORIGEN.\n';
					reset = true;
				} //AAP04
				
				if (reset){
					alert(str);
					obj[0].selected = true;//AAP04
				} else {
					/*se manda llamar funcion para limpiar importes*/
					ocultaInfoImportesServicios("");
					submitFormMainServices('validateFxcCcp','Validando FXC','');
				}
			} else {
				ocultaInfoImportesServicios("");
				submitFormMainServices('validateFxcCcp','Validando productos','');
				
			}	
		}
	}	
	
	/*funcion para limpiar objetos texto cuando se desmarque check de email*/
	function cleanEmail(checkObj, txtObj, hiddenObj) {//AAP05
		if (checkObj.checked) {
			txtObj.readOnly = false;
			txtObj.value = hiddenObj.value;
		} else {
			txtObj.readOnly = true;
			txtObj.value = "";			
		}
	}
	
	/*funcion para validar Emails capturados*/
	function echeck(str, msje) {//AAP05
		var at = "@";
		var dot = ".";
		var lat = str.indexOf(at);
		var lstr = str.length;
		
		if (str.indexOf(" ") != -1) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +". No debe llevar espacios");
			return false;
		}
		
		if (str.indexOf(at) == -1) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +". Necesita un '@'.");
			return false;
		}
		
		if (str.indexOf(at) == -1 || str.indexOf(at) == 0
				|| str.indexOf(at) == lstr) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +". Necesita un Nombre de Usuario.");
			return false;
		}
		
		if (str.indexOf(dot) == -1 || str.indexOf(dot) == 0
				|| str.indexOf(dot) == lstr) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +". Falta nombre de Cuenta.");
			return false;
		}
		
		if (str.indexOf(at, (lat + 1)) != -1) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +".");
			return false;
		}
		
		if (str.substring(lat - 1, lat) == dot
				|| str.substring(lat + 1, lat + 2) == dot) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +".");
			return false;
		}
		
		if (str.indexOf(dot, (lat + 2)) == -1) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +".");
			return false;
		}
		return true;
	}//AAP05
	
	/* verifica checkbox de emails para validacion de los mismos.*/
	function emailValidate(checkObj, txtObj, msj) {//AAP05
		var continuar = true;
		var continuaCiclo = true;
		
		if (eval("document.forms[0]."+checkObj).checked) {
			if (eval("document.forms[0]."+txtObj).value == "") {//AAP05
				alert("capture al menos un Email"+msj);
				eval("document.forms[0]."+txtObj).focus();
				continuar = false;
			} else {
				//eval("document.forms[0]."+txtObj).value = "";//AAP05
				var at = ";";
				var lat = eval("document.forms[0]."+txtObj).value.indexOf(at);
				var eMail = "";
				var cadena = "";
				if (lat ==-1) {
					eMail = eval("document.forms[0]."+txtObj).value;
					continuaCiclo = false;
					if (!echeck(eMail, msj)) {
						eval("document.forms[0]."+txtObj).focus();//AAP05
						continuar = false;//AAP05						
					}					
				} else {
					eMail = eval("document.forms[0]."+txtObj).value.substring(0,lat);
					cadena = eval("document.forms[0]."+txtObj).value.substring(lat+1);
				}
				
				while (continuaCiclo) {					
					if (!echeck(eMail, msj)) {
						eval("document.forms[0]."+txtObj).focus();//AAP05
						continuar = false;//AAP05
						continuaCiclo = false;
					} else {
						lat = cadena.indexOf(at);
						if (lat == -1) {
							eMail = cadena;
							cadena = "";
						} else {
							eMail = cadena.substring(0,lat);
							cadena = cadena.substring(lat+1);	
						}						
						
						if (eMail=="") {
							continuaCiclo = false;
						}
					}
				}
				
			}//AAP05	
		}
		return continuar;//AAP05
	}//AAP05
	
	//Funcion para impedir que regrese al jsp anterior con el backspace o con alt + <--
	var back='true';
	function noBack(key){
		var valor=key.keyCode;		
		if(back=='true'){
			if(key.altKey){
				//event.returnValue=false;
				if (event) {
					event.preventDefault();
				}
			}else{
				if(valor=='8'){
					//event.returnValue=false;
					if (event) {
						event.preventDefault();
					}
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
    /*FUNCION PARA DAR FORMATO DE PESOS AL VALOR QUE SE CAPTURA*/
    function formatoPesos(key,obj){
    	var cod=key.keyCode;
    	var band = true;
    	var punto = false;
    	var valor = obj.value;
		
		/*si se presion� un punto se activa la bandera punto*/
    	if (cod =='46'){
    		punto = true;
    	}
    	if (key.keyCode>=48 && 57>=key.keyCode){
    		numero = true;
    	}else{
    		/*si no se presiono numero ni punto se retorna la bandera false*/
    		if (!punto)
    			band = false;
    	}
    	
    	/*si valor es diferente de blancos, valida formato
    	y la tecla presionada es diferente de enter*/
    	var i = 0;
    	if (valor!='' && cod!='13'){

			 var Pos = valor.indexOf(".");
			 var lon = valor.length;

    		for (i = 0 ; i < lon ; i++){
			   if (valor.charAt(i) == '.'){
			   		/*si se presiono punto y ya existe uno escrito retorna bandera false*/
			   		if (punto)
				   		band = false;
				   	
				   	/*corta a dos decimales el valor*/	
				   	if (lon > Pos+2){
				    	valor = valor.substring(0,Pos+2);
				    	obj.value = valor;
				    }//fin corta
			    }//fin si es punto
			 }//fin ciclo
    	}//fin si el valor es diferente de blanco    	
    	return band;
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
    
    function add_all_li() {
    	if (document.forms[0].listReferences.value.length>0){
    		var fListAll = new Array();
    		fListAll = (document.forms[0].listReferences.value).split('|');
            document.forms[0].listReferences.value = "";
            for (let reference of fListAll){
            	add_li(reference);
            }
    	}
    	
    	/*si viene de validar referencia, realiza scroll automatico al final de la pantalla*/
    	if (document.forms[0].accionServices.value == 'validRefrClnt') {
    		scrollWindow();
    	}
    }
    
    function add_li_submit(nuevoLi) {
    	nuevoLi = trim(nuevoLi);
        if(nuevoLi.length>0) {
        	if (document.forms[0].flagValidRefrClnt.value == '1') {
        		if(find_li(nuevoLi)) {
        			submitFormMainServices('validRefrClnt','Validando Referencia','');
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
    	nuevoLi = trim(nuevoLi);
        if(nuevoLi.length>0) {        	
            if(find_li(nuevoLi)) {
            	var li=document.createElement('dt');
                li.id=nuevoLi;
                li.innerHTML="<span onclick='eliminar(this)'>X</span>"+nuevoLi;
                li.className = 'td';
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
        for (let reference of el){
        	refer = reference.innerHTML;
        	refer = refer.replace(/<\/?span[^>]*>/g,"");//elimina tag <span>
        	refer =  refer.substring(1);//elimina X
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
        var node=document.getElementById(id);
        
        var refer = document.getElementById(id).innerHTML;        
        
        refer = refer.replace(/<\/?span[^>]*>/g,"");
        refer = refer.replace(/<\/?SPAN[^>]*>/g,"");
        refer =  refer.substring(1);
        
        if (document.forms[0].flagValidRefrClnt.value == '1') {
        	var fListVal = new Array();
        	fListVal = (document.forms[0].listReferences.value).split('|');            
        	
        	if (fListVal.length>2) {
            	var continuar = true;
        		for(var jj=0;jj<fListVal.length;jj++){
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
        
        for(var j=0;j<fList.length;j++){
    		if(fList[j]==refer)
    			fList[j]="";
    	}
     // prepare the Fianl List
        document.forms[0].listReferences.value="";
    	for (let reference of fList){
    		if (reference!=""){
    			document.forms[0].listReferences.value=document.forms[0].listReferences.value+reference+"|";
    		}
    	}
    	
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
    function eventOnClicShippingType() {
		document.forms[0].changeShippingTypeValorOLD.value = document.forms[0].shippingType.value;
    }
	function setShippingType(obj){
		var valorNew = document.forms[0].shippingType.value;
		var valorOLD= document.forms[0].changeShippingTypeValorOLD.value;
		if (document.getElementById('mensaje').style.visibility == 'hidden') {
			if (confirm('\u00BFDesea cambiar el tipo de env\u00EDo?')) {
				if (valorNew != valorOLD){
					document.forms[0].entrega.value = "2";
					resetDetail();
					disabledObjectsDetail(false);
					validationTypeServicesSEG();
					document.forms[0].changeShippingType.value = "Y";
					submitFormMainServices('changeShippingType','Cargando cambio de tipo de envio','');
					return;
				}			
			} else {
				document.forms[0].shippingType.value = document.forms[0].changeShippingTypeValorOLD.value;
			}
		}
		document.forms[0].changeShippingType.value = "N";
	}
	
	 function validationTypeServicesSEG(){
		 if (document.forms[0].isShippingTypeSEG.value == 'N') {
			document.forms[0].volL.value = '0';
			document.forms[0].volH.value = '0';
			document.forms[0].volW.value = '0';
		} else {
			document.forms[0].volumen.disabled = true;
			document.forms[0].peso.readOnly = false;
		}
	 }
	 
	 function validaValoresMaximosSEG(tipo) {
		var value =  Number(document.forms[0].volL.value);
		var valueMAX = Number(tipo == 1 ? document.forms[0].volLMAXSEGSobre.value : document.forms[0].volLMAXSEG.value);
		var vacioVolL = 'N', vacioVolH = 'N', vacioVolW = 'N';
		var cadenaVolL = '', cadenaVolH = '', cadenaVolW = '';
		if (value > valueMAX) {
			alert("LARGO NO DEBE EXCEDER DE " + valueMAX + ".");
			document.forms[0].volL.focus();
			return false;
		}
		if (document.forms[0].volL.value == "" || value == 0) {
			cadenaVolL = "Favor de ingresar un valor mayor a 0 en lo largo.";
			if(document.forms[0].isShippingTypeSEG.value == 'Y' || document.forms[0].forceCaptureDimensions.value == 'Y') {
				alert(cadenaVolL);
				document.forms[0].volL.focus();
				return false;
			} else {
				vacioVolL = 'Y';
			}
		}
		value = Number(document.forms[0].volH.value);
		valueMAX =  Number(tipo == 1 ? document.forms[0].volHMAXSEGSobre.value : document.forms[0].volHMAXSEG.value);
		if (value > valueMAX) {
			alert("ALTO NO DEBE EXCEDER DE " + valueMAX + ".");
			document.forms[0].volH.focus();
			return false;
		}
		if (document.forms[0].volH.value == "" || value == 0) {
			cadenaVolH = "Favor de ingresar un valor mayor a 0 en lo alto.";
			if(document.forms[0].isShippingTypeSEG.value == 'Y' || document.forms[0].forceCaptureDimensions.value == 'Y') {
				alert(cadenaVolH);
				document.forms[0].volH.focus();
				return false;
			} else {
				vacioVolH = 'Y';
			}
		}

		value = Number(document.forms[0].volW.value);
		valueMAX = Number(tipo == 1 ? document.forms[0].volWMAXSEGSobre.value : document.forms[0].volWMAXSEG.value);
		if (value > valueMAX) { 
			alert("ANCHO NO DEBE EXCEDER DE " + valueMAX + ".");
			document.forms[0].volW.focus();
			return false;
		}
		if (document.forms[0].volW.value == "" || value == 0) {
			cadenaVolW = "Favor de ingresar un valor mayor a 0 en lo ancho.";
			if(document.forms[0].isShippingTypeSEG.value == 'Y' || document.forms[0].forceCaptureDimensions.value == 'Y') {
				alert(cadenaVolW);
				document.forms[0].volW.focus();
				return false;
			} else {
				vacioVolW = 'Y';
			}
		}

		//VALIDA CAJAS Y NO ERES SERVICIO EXPRESS
		if(document.forms[0].isShippingTypeSEG.value == 'N') {
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
			value = Number(document.forms[0].volumen.value);
			
			valueMAX = Number(tipo == 1 ? document.forms[0].volMAXSobre.value : document.forms[0].volMAX.value);
			if (value > valueMAX) {
				alert("VOLUMEN NO DEBE EXCEDER DE "+valueMAX + " METROS CUBICOS.");
				if((vacioVolL == 'Y' && vacioVolH == 'Y' && vacioVolW == 'Y')) {
					document.forms[0].volumen.focus();
				} else {
					document.forms[0].volL.focus();
				}
				return false;
			}
		}
		value = Number(document.forms[0].peso.value);
		valueMAX = Number(tipo == 1 ? document.forms[0].pesoMAXSEGSobre.value : document.forms[0].pesoMAXSEG.value);
		if (value > valueMAX) {
			alert("PESO NO DEBE EXCEDER DE "+valueMAX + " KG.");
			document.forms[0].peso.focus();
			return false;
		}
		if (document.forms[0].isShippingTypeSEG.value == 'Y' || document.forms[0].forceCaptureDimensions.value == 'Y') {
			value = Number(document.forms[0].peso.value);
			if (document.forms[0].peso.value == "" || value == 0) {
				alert("Favor de ingresar un valor mayor a 0 en el peso.");
				document.forms[0].peso.focus();
				return false;
			}
			if (document.forms[0].destinationcode.value.indexOf('70') > -1 && document.forms[0].forceCaptureDimensions.value == 'N') {
				alert("NO SE PERMITE ZONA PLUS PARA EL TIPO DE ENVIO SELECCIONADO");
				return false;
			}
		}
		return true;
	 }
	 
	 const getMinValue = (prefix, type) => Number(document.forms[0][`${prefix}${type}`].value);
	 
	 function validaValoresMinimos(isSobre) {
		  
		 if (!medidasCapturadas()){
			 return true;
		 }
		 
		 if (document.forms[0].tarifa.value != "" && document.forms[0].tarifa.value != "S"){
			 return true;
		 }
		 
		 //Current values
		 const long =  Number(document.forms[0].volL.value);
		 const width =  Number(document.forms[0].volW.value);
		 const height =  Number(document.forms[0].volH.value);
		 const weight =  Number(document.forms[0].peso.value);
		 const volume =  Number(document.forms[0].volumen.value);
		 
		 //Mininum values
		 const isPackage = isSobre == 'N';
		 const minValues = {
				 long: getMinValue('volLMin', isPackage ? 'Paq' : 'Env'),
				 width: getMinValue('volWMin', isPackage ? 'Paq' : 'Env'),
				 height: getMinValue('volHMin', isPackage ? 'Paq' : 'Env'),
				 weight: getMinValue('wghtMin', isPackage ? 'Paq' : 'Env'),
				 volume: getMinValue('volMin', isPackage ? 'Paq' : 'Env'),
		};
		 
		 // alert messages
		 const alerts = [];

		 if (weight < minValues.weight) alerts.push(`Peso: ${minValues.weight} kg`);
		 if (long < minValues.long) alerts.push(`Largo: ${minValues.long} cm`);
		 if (height < minValues.height) alerts.push(`Alto: ${minValues.height} cm`);
		 if (width < minValues.width) alerts.push(`Ancho: ${minValues.width} cm`);
		 if (volume < minValues.volume) alerts.push(`Volumen: ${minValues.volume}`);

		 // Display compact alert message
		 if (alerts.length > 0) {
			 const message = `Medidas m\u00EDnimas permitidas (cm):\n ${alerts.join("\n")}`;
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
		     document.forms[0].peso.value != "" &&
		     document.forms[0].volumen.value != "";
	 }

	 function calcVolWeight() {
		var lengthValue = document.forms[0].volL.value;
		var widthValue = document.forms[0].volW.value;
		var heightValue = document.forms[0].volH.value;
		var weightDivisor = document.forms[0].factorDividorPesoVol.value;

		var l = parseFloat(lengthValue);
		var w = parseFloat(widthValue);
		var h = parseFloat(heightValue);
	
		var errorL = isNaN(l);// || (l.toString().length != lengthValue.length);
		var errorW = isNaN(w);// || (w.toString().length != widthValue.length);
		var errorH = isNaN(h);// || (h.toString().length != heightValue.length);
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
		document.forms[0].weightVolumetric.value = calcVolWeight;
		if(!(l == 0 && w == 0 &&  h == 0)) {
			document.forms[0].volumen.value = calcVolume;
			if(document.forms[0].tarifa.value != "" && document.forms[0].tarifa.value != 'S') {
				document.forms[0].tarifa.value = "";
				document.forms[0].peso.value = "";
			}
			document.forms[0].peso.readOnly = false;
			document.forms[0].volumen.readOnly = false;
		}
		
	 }
	 
	function scrollWindow() {
		window.scrollBy(0,screen.availHeight);
	}
	
	function onchangeVolumen() {//JSA01
		document.forms[0].volL.value = "";
		document.forms[0].volW.value = "";
		document.forms[0].volH.value = "";
		document.forms[0].weightVolumetric.value = "";
	}	
	
	function valCcpProdErr(){
		if (document.forms[0].errMsgAdditional.value != null && document.forms[0].errMsgAdditional.value != ''){
			if (document.forms[0].accionServices.value == 'invalidFiscal' && document.forms[0].formaPago.value == 'TO_PAY' &&
					document.forms[0].validFiscal.value == "N"){
				if (confirm(document.forms[0].errMsgAdditional.value)){
					document.forms[0].validFiscal.value = "Y";
					document.forms[0].errMsgAdditional.value = "";
				}else{
					document.forms[0].validFiscal.value = "N";
				}
			}else if (document.forms[0].flagValidProductId.value != 'Y'){
				errorValMaxDecl();				
			}
		}else if (document.forms[0].accionServices.value == 'generate' && document.forms[0].flagValidProductId.value == 'Y'){
			generateGuia("generate");
		}
	}
	
	function errorValMaxDecl(){
		if (!document.forms[0].errMsgAdditional.value.includes("Valor Declarado para este Envio")){
			alert(document.forms[0].errMsgAdditional.value);
		}else{
			if (parseFloat(document.forms[0].valordeclarado.value) > parseFloat(document.forms[0].maxDeclAmnt.value)){
				alert(document.forms[0].errMsgAdditional.value);
				document.forms[0].valordeclarado.focus();
			}else{
				document.forms[0].errMsgAdditional.value = "";
			}
		}
	}
	
	
	function emptyTableCheck() {
		let emptyTable = false;
		let emptyRowsCount = 0;
		let emptyCellsCount = 0;
	    let table = document.getElementById("tablaDetalle");
	    for (let r = 1, n = table.rows.length; r < n; r++) {
	        for (let c = 1, m = table.rows[r].cells.length; c < m; c++) {
	            if (table.rows[r].cells[c].innerHTML == "&nbsp;"){
	            	emptyCellsCount++;
	            }
	        }
	        if (emptyCellsCount>0){
	        	emptyRowsCount++;
	        }
	    }
		emptyTable = emptyRowsCount==6? true : false;
		if (emptyTable){
			resetServices();
		}
	}
	
	function validateMaxQty(){
		let qty = parseInt(document.forms[0].cantidad.value);
		let maxQty = parseInt(document.forms[0].maxQtyPack.value);
		let intPattern = /^-?\d+$/;
		if (qty > maxQty){
			showToast("error", "Cantidad de paquetes debe ser menor a "+document.forms[0].maxQtyPack.value, 6000);
			return false;
		}else if (!intPattern.test(document.forms[0].cantidad.value)){
			showToast("error", "Capturar n\u00FAmero entero en Cantidad", 6000);
			return false;
		}
		return true;
	}
	
	var showMsgOnInit = false;