function initScript() {
	
	ocultarBarraEspera('mensaje','body');
	MakeStaticHeader('conjuntoGuias', 240, 900, 40, false,'01');	
	
	//alert('document.forms[0].idSetSel.value '+document.forms[0].idSetSel.value+'|');
	//alert('document.forms[0].clickedItems.value '+document.forms[0].clickedItems.value+'|');
	 
	if (document.forms[0].currentTask.value=="" || document.forms[0].currentTask.value=="start"){
		allowRadOrgnZp(false);
	}
	if (document.forms[0].idSetSel.value !='') {
		scrollWindow();
		document.getElementById("detGuia01").style.visibility='visible';
		document.getElementById("detGuia01").style.position='static';
		document.getElementById("detGuia02").style.visibility='visible';
		document.getElementById("detGuia02").style.position='static';
		document.getElementById("detalleGuias").style.visibility='visible';
		document.getElementById("detalleGuias").style.position='static';		
		//document.getElementById("detalleGuias02").style.visibility='visible';
		//document.getElementById("detalleGuias02").style.position='static';
		
		                 //gridId, height, width, headerHeight, isFooter, idDiv
		MakeStaticHeader('detalleGuias', 290, 900, 40, false,'02');	
		
		/*desmarca guias seleccionadas de tabla clonada para titulo*/
		unCheckGuiasSelectHidden();
		
//		var valores = "";		
//		for(var i=0;i < document.forms[0].cb.length;i++){
//			valores = valores + ( "\ncb "+i
//			+"\n checked "+document.forms[0].cb[i].checked
//			+"\n value "+document.forms[0].cb[i].value);			
//		}
//		alert(valores);
		
		if (document.forms[0].tarifaSel.value != "") {
			if (document.forms[0].tarifaSel.value == "S") {//documentacion sobres
				document.forms[0].accionDoc.value = "guiaconversion.do";
			} else {//documentacion cajas
				document.forms[0].accionDoc.value = "guiaconversioncajas.do";
			}
		}
		
		/*seccion de asignaciones (solo para usuario administrador)*/
		showAsigIni();
	}
}

/* Función para validar si permite RAD zona plus */
function allowRadOrgnZp(reloadPage){
	if (document.forms[0].branchId.value.substring(3)=="70"){
		alert("DE MOMENTO NO PUEDE DOCUMENTAR PREPAGO CON ORIGEN ZONA PLUS");
		clearSelected();
		if (reloadPage){
			showBarraEspera('mensaje','body','Espere por favor...');
			document.forms[0].currentTask.value="start";
			document.forms[0].action="guiaConversionMonitor.do?insideTask=start";
			document.forms[0].target="_self";
			document.forms[0].submit();
			return false;
		}else{
			return true;
		}
	}else{
		return true;
	}
}

function unCheckGuiasSelectHidden() {
	if (document.forms[0].clickedItems.value == '') {
		for(var i=0;i < document.forms[0].cb.length;i++) {
			document.forms[0].cb[i].checked = false;
		}
	} else {
		if (document.forms[0].cb.length !== "undefined"){
			var div = document.forms[0].cb.length / 2;
			//var mod = document.forms[0].cb.length % 2;
			for(var i=0;i < div;i++) {
				document.forms[0].cb[i].checked = false;
			}
		}
	}
}

//function deleteRow(tableID) {
//	var table = tableID;
//    try {
//	    var rowCount = table.rows.length;
//	    for(var i=1; i<rowCount; i++) {	         
//	         table.deleteRow(i);
//             rowCount--;
//             i--;
//	    }
//    }catch(e) {
//    	alert(e);
//    }
//    return table;
//}

function MakeStaticHeader(gridId, height, width, headerHeight, isFooter, idDiv) {
	var tbl = document.getElementById(gridId);
	if (tbl) {
		var DivHR = document.getElementById('DivHeaderRow'+idDiv);
		var DivMC = document.getElementById('DivMainContent'+idDiv);
		var DivFR = document.getElementById('DivFooterRow'+idDiv);
		
		document.getElementById('DivRoot'+idDiv).style.height = (parseInt(height)) + 'px';//aap

		//*** Set divheaderRow Properties ****
		DivHR.style.height = headerHeight + 'px';
		DivHR.style.width = (parseInt(width) - 16) + 'px';
		DivHR.style.position = 'relative';
		DivHR.style.top = '0px';
		DivHR.style.zIndex = '10';
		DivHR.style.verticalAlign = 'top';

		//*** Set divMainContent Properties ****
		DivMC.style.width = width + 'px';
		DivMC.style.height = height + 'px';
		DivMC.style.position = 'relative';
		DivMC.style.top = -headerHeight + 'px';
		DivMC.style.zIndex = '1';

		//*** Set divFooterRow Properties ****
		DivFR.style.width = (parseInt(width) - 16) + 'px';
		DivFR.style.position = 'relative';
		DivFR.style.top = -headerHeight + 'px';
		DivFR.style.verticalAlign = 'top';
		DivFR.style.paddingtop = '2px';

		if (isFooter) {

			var tblfr = tbl.cloneNode(true);
			tblfr.removeChild(tblfr.getElementsByTagName('tbody')[0]);
			var tblBody = document.createElement('tbody');
			tblfr.style.width = '100%';
			tblfr.cellSpacing = "0";

			//*****In the case of Footer Row *******

			tblBody.appendChild(tbl.rows[tbl.rows.length - 1]);
			tblfr.appendChild(tblBody);
			DivFR.appendChild(tblfr);
			//document.getElementById('DivRoot'+idDiv).style.height = (parseInt(height) + (tblfr.style.height)) + 'px';//aap
		}

		//****Copy Header in divHeaderRow****
		//var tblHR = deleteRow(tbl.cloneNode(true));//aap
		DivHR.appendChild(tbl.cloneNode(true));
	}
}

function OnScrollDiv(Scrollablediv,id) {
	document.getElementById('DivHeaderRow'+id).scrollLeft = Scrollablediv.scrollLeft;
	document.getElementById('DivFooterRow'+id).scrollLeft = Scrollablediv.scrollLeft;
}

function selectedSet(id) {
	if (document.getElementById('mensaje').style.visibility == 'hidden') {
		/*asigna set actual para limpiar seleccion*/
		if (document.forms[0].idSetSel.value !='') {
			document.forms[0].idSetSelOld.value = document.forms[0].idSetSel.value;
			document.forms[0].contRastreos.value = 0;
			if (document.forms[0].cbAll[0].checked) {
				document.forms[0].cbAll[0].checked = false;
			}
		}
		
		document.forms[0].idSetSel.value = document.getElementById('idSet'+id).value;
		document.forms[0].zonaKmSel.value = document.getElementById('zonaKm'+id).value;
		document.forms[0].tarifaSel.value = document.getElementById('tarifa'+id).value;
		document.forms[0].pesoKgSel.value = document.getElementById('pesoKg'+id).value;
		document.forms[0].volumenSel.value = document.getElementById('volumen'+id).value;
		document.forms[0].numRastreoSel.value = document.getElementById('numRastreo'+id).value;
		document.forms[0].acuseSel.value = document.getElementById('acuse'+id).value;
		document.forms[0].valorDeclaradoSel.value = document.getElementById('valorDeclarado'+id).value;
		document.forms[0].EADSel.value = document.getElementById('EAD'+id).value;
		document.forms[0].RADSel.value = document.getElementById('RAD'+id).value;
		document.forms[0].EXTSel.value = document.getElementById('EXT'+id).value;	
		document.forms[0].mainSetSel.value = document.getElementById('mainSet'+id).value;
		document.forms[0].invcSel.value = document.getElementById('invc'+id).value;
		document.forms[0].shippingTypeSel.value = document.getElementById('shippingType'+id).value;
		document.forms[0].locationTypeSel.value = document.getElementById('locationType'+id).value;//AAP20
		document.forms[0].vigenciaSel.value = document.getElementById('vigencia'+id).value;//AAP27
		
		if (document.forms[0].tarifaSel.value == "S") {//documentacion sobres
			document.forms[0].accionDoc.value = "guiaconversion.do";
		} else {//documentacion cajas
			document.forms[0].accionDoc.value = "guiaconversioncajas.do";
		}
		
		
		document.forms[0].banCerrar.value = "false";
		document.forms[0].currentTask.value = "loadSetDetail";
		document.forms[0].action = "guiaConversionMonitor.do?insideTask="+document.forms[0].currentTask.value;
		showBarraEspera('mensaje','body','Espere por favor...Cargando Detalle de Guias');
		document.forms[0].target = "_self";
		document.forms[0].submit();	
	}	
}

function mainpage() {
	if (document.getElementById('mensaje').style.visibility == 'hidden') {
		//clearSelected();
		
		document.forms[0].banCerrar.value = "false";
		document.forms[0].currentTask.value = "mainpage";
		document.forms[0].action = "guiaConversionMonitor.do?insideTask="+document.forms[0].currentTask.value;
		showBarraEspera('mensaje','body','Espere por favor... Cargando menu principal');
		document.forms[0].target = "_self";
		document.forms[0].submit();
	}
}

function cambiarFiltro() {
	if (document.getElementById('mensaje').style.visibility == 'hidden') {
		//clearSelected();
		document.forms[0].banCerrar.value = "false";
		document.forms[0].currentTask.value = "cambioFiltro";
		document.forms[0].action = "guiaConversionMonitor.do?insideTask="+document.forms[0].currentTask.value;
		showBarraEspera('mensaje','body','Espere por favor... Cargando Detalle de Guias');
		document.forms[0].target = "_self";
		document.forms[0].submit();
	}
}

function clearSelected() {
	document.forms[0].idSetSel.value = "";
	document.forms[0].zonaKmSel.value = "";
	document.forms[0].tarifaSel.value = "";
	document.forms[0].pesoKgSel.value = "";
	document.forms[0].volumenSel.value = "";
	document.forms[0].numRastreoSel.value = "";
	document.forms[0].acuseSel.value = "";
	document.forms[0].valorDeclaradoSel.value = "";
	document.forms[0].EADSel.value = "";
	document.forms[0].RADSel.value = "";
	document.forms[0].EXTSel.value = "";
	document.forms[0].mainSetSel.value = "";
	document.forms[0].invcSel.value = "";
}

function getFinalList(guiaNo, icont, obj) {
	if (document.getElementById('mensaje').style.visibility == 'hidden') {		
		//var jsfinalGuiaNumbers = new Array();
		var fList = new Array();
		/*alert ("checkBox "+i+
				". Guia: "+guiaNo+
				". checado "+document.forms[0].cbGuias[i].checked+
				". valor "+document.forms[0].cbGuias[i].value+
				"\nobj checked "+obj.checked+"\n obj.value "+obj.value);*/	
		// get the Guia Numbers of Checked Items	
		//if (document.forms[0].cbGuias[i].checked) {
		if (obj.checked) {
			document.forms[0].clickedItems.value = document.forms[0].clickedItems.value + guiaNo + " ";
			fList = (document.forms[0].clickedItems.value).split(' ');
			//alert("clickedItems 01..."+document.forms[0].clickedItems.value);
		} else { // Remove the guia numbers for deselected check boxes..
			fList = (document.forms[0].clickedItems.value).split(' ');
			for (var j = 0; j < fList.length; j++) {
				if (fList[j] == guiaNo) {
					fList[j] = "";
				}
			}
		}
		
		//alert("fList "+fList.toString());
		// prepare the Final List
		document.forms[0].clickedItems.value = "";
		var contRastreos = 0;
		for (var k = 0; k < fList.length; k++) {
			if (fList[k] != "") {
				document.forms[0].clickedItems.value = document.forms[0].clickedItems.value + fList[k] + " ";
				contRastreos++;
			}
		}
		document.forms[0].contRastreos.value = contRastreos;
		/*se asigna guia para bloquear o desbloquear segun sea el caso.*/
		document.forms[0].currentGuia.value = guiaNo;
		//jsfinalGuiaNumbers = (document.forms[0].clickedItems.value).split(' ');
		//alert("FINAL GUIA NUMBERS..."+jsfinalGuiaNumbers.toString());
		//alert("clickedItems      ..."+document.forms[0].clickedItems.value);
		document.forms[0].currentTask.value = "controlGuia";
		//document.forms[0].banCerrar.value = "false";
		//document.forms[0].method = "POST";
		document.forms[0].target = "monitorIn";
		document.forms[0].action = "guiaConversionMonitor.do?insideTask="+document.forms[0].currentTask.value;
		showBarraEspera('mensaje','body','Espere por favor...validando guia');
		document.forms[0].submit();
	}	
}
function checkUnCheckAllGuias() {	
	var div = document.forms[0].cb.length / 2;
	
	if (document.forms[0].cbAll[0].checked) {	
		var fList = new Array();
		document.forms[0].clickedItems.value = "";
		var contRastreos = 0;
		for(var i=div;i < document.forms[0].cb.length;i++) {
			
			fList = (document.forms[0].cb[i].value).split('|');
			document.forms[0].cb[i].checked = true;
			document.forms[0].clickedItems.value = document.forms[0].clickedItems.value + ' '+fList[0];
			document.forms[0].currentGuia.value = document.forms[0].clickedItems.value;
			contRastreos++;
		}
		document.forms[0].contRastreos.value = contRastreos;
	} else {		
		for(var i=div;i < document.forms[0].cb.length;i++) {
			document.forms[0].cb[i].checked = false;					
		}
		document.forms[0].currentGuia.value = document.forms[0].clickedItems.value;
		document.forms[0].clickedItems.value = '';
		document.forms[0].contRastreos.value = 0;
	}
	
	if (document.forms[0].currentGuia.value!= '') {
		document.forms[0].currentTask.value = "controlGuia";
		//document.forms[0].banCerrar.value = "false";
		//document.forms[0].method = "POST";
		document.forms[0].target = "monitorIn";
		document.forms[0].action = "guiaConversionMonitor.do?insideTask="+document.forms[0].currentTask.value;
		showBarraEspera('mensaje','body','Espere por favor...validando guias');
		document.forms[0].submit();
	}	
}
function documentarGuia() {
	if (document.getElementById('mensaje').style.visibility == 'hidden') {
		if (document.forms[0].tarifaSel.value == "S") {
			//var valores = "";
			var contador = 0;
			
			for(var i=0;i < document.forms[0].cb.length;i++){
				/*valores = valores + ( "\ncb "+i
				+"\n checked "+document.forms[0].cb[i].checked
				+"\n value "+document.forms[0].cb[i].value);*/
				if (document.forms[0].cb[i].checked) {
					contador++;
				}
				if (contador>1) {
					alert("Documentacion por rango no permitida para sobres");
					//obj.checked = false;
					return;
				}
			}
			//alert(valores);
		}
		
		//clearSelected();
		//var valores ="";
		var asignada = "";
		var userAsig = "";
		var userAsigDif = "";
		var contador = 0;
		var fList = new Array();
		for (var i=0;i < document.forms[0].cb.length;i++) {			
			if (document.forms[0].cb[i].checked) {
				contador++;
				fList = (document.forms[0].cb[i].value).split('|');
				//asignada = document.forms[0].cb[i].value;
				//asignada = asignada.substring(asignada.indexOf('|')+1, asignada.lenght);
				asignada = fList[1];
				userAsig = fList[3];
				//alert('asignada '+asignada);
				//document.forms[0].guiasAsigFlag.value = asignada;
				
				if (asignada=="Y") {
					if (userAsig != document.forms[0].origenUserClave.value) {
						userAsigDif = "Y";
						break;	
					}		
				}
			}
		}
		
		if (contador==0) {
			alert("Seleccione al menos una guía para documentar");		
		} else {
			if (userAsigDif =='Y') {
				alert('Guia asignada a otro usuario. Favor de quitar asignación para documentar');
			} else {
				if (allowRadOrgnZp(true)){
					document.forms[0].action = document.forms[0].accionDoc.value;
					document.forms[0].banCerrar.value = "false";
					document.forms[0].currentTask.value = "load";
					showBarraEspera('mensaje','body','Espere por favor...Cargando Pantalla de documentacion');
					document.forms[0].target = "_self";
					document.forms[0].submit();	
				}
			}				
		}
	}
}


//Abre la ventana para eliminar guias seleccionadas en el onUnload de los jsp
function clearGuias() {	 
	if(document.forms[0].banCerrar.value !='false') {
		if (document.forms[0].clickedItems.value!=''){
			var t = 'currentTask=clearGuias'
				+'&clickedItems='+document.forms[0].clickedItems.value
				+'&idSetSel='+document.forms[0].idSetSel.value;

			//window.open('../AccionOS/REGSERVADIBLOQS.do?'+t,'_blank','menubar=no,location=no,toolbar=no,status=no,scrollbars=no,resizable=no,width=680,height=335 top=105, left=225');
			window.open('guiaConversionMonitor.do?'+t,'_blank');	
		}
	}
}

function showAsigIni() {
	if ( document.getElementById("objSucursalAsig")) {
		document.getElementById("asignacionesGuias").style.visibility='visible';
		document.getElementById("asignacionesGuias").style.position='static';
		seleccionado();
		/*Seccopn para cargar PDF's de los rastreos seleccionados (si se presionó botón "Crear PDF"*/
		if (document.forms[0].genPDF.value == "Y") {			
			var link = '';
			//alert('GenCartaPorte?trackingNoGen='+document.forms[0].trackingNoGen.value);
			link = document.forms[0].urlRastreoPDF.value+'GenRastreoPrepago';
			
			/*genera PDF por set*/
			if (document.forms[0].idSetSelPDF.value!='') {
				link = link + '?refNo='+document.forms[0].idSetSelPDF.value;
				document.forms[0].clickedItemsPDF.value = '';
			}
			
			/*valida si es solo un rastreo para que siga funcionando el link de PDF con parametro trackingNoGen*/
			if (document.forms[0].clickedItemsPDF.value!='') {
				var fListPDF = new Array();
				fListPDF = (trim(document.forms[0].clickedItemsPDF.value)).split(' ');
				
				if (fListPDF.length==1) {
					link = link + '?trackingNoGen='+fListPDF[0];
					document.forms[0].clickedItemsPDF.value = '';
				}
			}
			
			//window.open(link, '_blank');

			document.forms[0].action = link;
			document.forms[0].target = "_blank";
			document.forms[0].submit();
			
			document.forms[0].genPDF.value = 'N';
			document.forms[0].clickedItemsPDF.value = '';
			document.forms[0].urlRastreoPDF.value = '';
			document.forms[0].idSetSelPDF.value = '';
			
			document.forms[0].target = "_self";			
		}
	}
}

function seleccionado() { 
	//alert('entro a seleccionado()');
	for (var x = 0; x < document.forms[0].tipoAsignacion.length; x++) {
		if (document.forms[0].tipoAsignacion[x].checked) {			
			if (document.forms[0].tipoAsignacion[x].value=='cte') {
				showAsigCte();
			} else if (document.forms[0].tipoAsignacion[x].value=='gpo'){
				showAsigGpo();
			} else {
				showSinAsi();
			}
			break; 
		} 
	}		
} 

function showAsig(obj) {
	document.forms[0].cveSucursalAsig.value = "";
	document.forms[0].desSucursalAsig.value = "";
	document.forms[0].cveClienteAsig.value = "";
	document.forms[0].desClienteAsig.value = "";
	document.forms[0].cveUserAsig.value = "";
	document.forms[0].desUserAsig.value = "";
	
	if (obj.value == 'cte'){
		showAsigCte();
	} else if (obj.value == 'gpo') {
		showAsigGpo();
	} else {
		showSinAsi();
	}
	scrollWindow();
}

function showAsigCte() {
	document.getElementById("objSucursalAsig").style.visibility='visible';
	document.getElementById("objSucursalAsig").style.position='static';
	
	document.getElementById("objClienteAsig").style.visibility='visible';
	document.getElementById("objClienteAsig").style.position='static';
	
	document.getElementById("objUserAsig").style.visibility='visible';
	document.getElementById("objUserAsig").style.position='static';		
}

function showAsigGpo() {
	document.getElementById("objSucursalAsig").style.visibility='hidden';
	document.getElementById("objSucursalAsig").style.position='absolute';
	
	document.getElementById("objClienteAsig").style.visibility='visible';
	document.getElementById("objClienteAsig").style.position='static';
	
	document.getElementById("objUserAsig").style.visibility='visible';
	document.getElementById("objUserAsig").style.position='static';
}

function showSinAsi() {
	document.getElementById("objSucursalAsig").style.visibility='hidden';
	document.getElementById("objSucursalAsig").style.position='absolute';
	
	document.getElementById("objClienteAsig").style.visibility='hidden';
	document.getElementById("objClienteAsig").style.position='absolute';
	
	document.getElementById("objUserAsig").style.visibility='hidden';
	document.getElementById("objUserAsig").style.position='absolute';
}

function openLov(str) {
	var opcSelAsig = "";
	for (var x = 0; x < document.forms[0].tipoAsignacion.length; x++) {
		if (document.forms[0].tipoAsignacion[x].checked) {
			opcSelAsig = document.forms[0].tipoAsignacion[x].value;
			break; 
		} 
	}
	
	if (str == 'brncAsig') {
		var destinationcode = '';
		//window.open ('lov.do?type='+str+'&selectedBranch='+destinationcode,'abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
		abrirVentana('lov.do?type='+str+'&selectedBranch='+destinationcode, 'abc', 400, 200);
	} else if (str == 'clntAsig') {
		var parm = '';
		if (opcSelAsig == 'cte') {
			if (document.forms[0].cveSucursalAsig.value =='') {
				alert('Capture sucursal para asignación de cliente');
				return;
			} else {
				parm = '&siteId='+document.forms[0].cveSucursalAsig.value;	
			}
			
		} else {
			parm = '&clientId='+document.forms[0].clientId.value;
		}
		abrirVentana('lov.do?type='+str+'&opcSelAsig='+opcSelAsig +parm, 'abc', 690, 300);
	} else if (str == 'userAsig') {
		if (document.forms[0].cveClienteAsig.value =='') {
			alert('Capture cliente para asignación de usuario');
			return;
		} else {
			parm = '&siteId='+document.forms[0].cveSucursalAsig.value;	
		}		
		abrirVentana('lov.do?type='+str+'&opcSelAsig='+opcSelAsig +'&cveClienteAsig='+document.forms[0].cveClienteAsig.value, 'abc', 690, 300);
	}
}

function asignarGuias() {
	var continuar = false;
	if (document.forms[0].clientePropietario.value != document.forms[0].clientId.value) {
		alert("Usuario no autorizado para asignación de set de guías");
		return;
	}
	
	var opcSelAsig = "";
	for (var x = 0; x < document.forms[0].tipoAsignacion.length; x++) {
		if (document.forms[0].tipoAsignacion[x].checked) {
			opcSelAsig = document.forms[0].tipoAsignacion[x].value;
			break; 
		} 
	}	
	
	//var valores ="";
	var asignada = "";
	var contador = 0;
	var fList = new Array();
	for (var i=0;i < document.forms[0].cb.length;i++) {
		
		if (document.forms[0].cb[i].checked) {
			contador++;
			fList = (document.forms[0].cb[i].value).split('|');
			//asignada = document.forms[0].cb[i].value;
			//asignada = asignada.substring(asignada.indexOf('|')+1, asignada.lenght);
			asignada = fList[1];
			//alert('asignada '+asignada);
			//document.forms[0].guiasAsigFlag.value = asignada;
			
			if (asignada=="Y") {
				break;
			}
		}
	}
	
	if (contador==0) {
		alert("Seleccione al menos una guía para asignar.");
	} else {
		if (opcSelAsig =='sin') {
			if (asignada == "Y") {
				if (confirm("Existen guías asignadas en lo seleccionado. ¿Desea sobreescribir los cambios?")){					
					continuar = true;
				} else {
					continuar = false;
					return;
				}				
			} else {
				alert('Las guías seleccionadas ya estan sin asignar.');
				continuar = false;
				return;
			}		
		} else {
			if (document.forms[0].cveClienteAsig.value =='') {
				alert('Seleccione cliente para realizar asignación');
				continuar = false;
				return;
			} else {
				if (document.forms[0].cveUserAsig.value =='') {
					if (confirm("¿Desea asignar guías a nivel cliente?")) {
						continuar = true;
					} else {
						continuar = false;
						return;
					}
				} else {
					continuar = true;
				}
			}
			
			if (continuar) {
				if (asignada == "Y") {					
					if (confirm("Existen guías asignadas en lo seleccionado. ¿Desea sobreescribir los cambios?")) {
						continuar = true;
					} else {
						continuar = false;
						return;
					}
				} else {
					continuar = true;
				}
			}
		}
	}
	
	if (continuar) {
		document.forms[0].currentTask.value = "asignaGuia";
		document.forms[0].banCerrar.value = "false";
		document.forms[0].action = "guiaConversionMonitor.do?insideTask="+document.forms[0].currentTask.value;
		showBarraEspera('mensaje','body','Espere por favor...Asignando guías');
		document.forms[0].target = "_self";
		document.forms[0].submit();
	}
}

function asignarGuiasPDF(opc) {
	if (document.getElementById('mensaje').style.visibility == 'hidden') {
		var continuar = false;
		if (document.forms[0].clientePropietario.value != document.forms[0].clientId.value) {
			alert("Usuario no autorizado para asignación de set de guías");
			return;
		}
		
//		var opcSelAsig = "";
//		for (var x = 0; x < document.forms[0].tipoAsignacion.length; x++) {
//			if (document.forms[0].tipoAsignacion[x].checked) {
//				opcSelAsig = document.forms[0].tipoAsignacion[x].value;
//				break; 
//			} 
//		}	
		var asignada = "";
		var contador = 0;
		var fList = new Array();
		
		/* **********************************
		 * Crea PDF por guias seleccionadas *
		 * **********************************/
		if (opc == "0") {
			for (var i=0;i < document.forms[0].cb.length;i++) {
				
				if (document.forms[0].cb[i].checked) {
					contador++;
					fList = (document.forms[0].cb[i].value).split('|');
					//asignada = document.forms[0].cb[i].value;
					//asignada = asignada.substring(asignada.indexOf('|')+1, asignada.lenght);
					asignada = fList[1];
					//alert('asignada '+asignada);
					//document.forms[0].guiasAsigFlag.value = asignada;
					
					if (asignada=="Y") {
						break;
					}
				}
			}
			
			if (contador==0) {
				alert("Seleccione al menos una guía para crear PDF.");
			//} else if (contador==1) {
			} else {
				
				document.forms[0].cveClienteAsig.value = document.forms[0].clientId.value;
				document.forms[0].cveUserAsig.value = 'PDF';
				document.forms[0].genPDF.value = 'Y';
				document.forms[0].clickedItemsPDF.value = document.forms[0].clickedItems.value;
				
				if (asignada == "Y") {
					if (confirm("Existen guías asignadas en lo seleccionado. ¿Desea sobreescribir los cambios?")) {
						continuar = true;				
					} else {
						continuar = false;
						document.forms[0].cveClienteAsig.value = "";
						document.forms[0].cveUserAsig.value = "";
						document.forms[0].genPDF.value = 'N';
						document.forms[0].clickedItemsPDF.value = "";
						return;
					}
				} else {
					continuar = true;
				}
			//} else {
				//alert('No se permite generacion de PDF por rango de guias');
			}
			
			/* *******************************
			 * Crea PDF por set seleccionado *
			 * *******************************/
		} else if (opc == "1") {
			if (confirm("¿Desea obtener PDF de set?")) {
				if (document.forms[0].cb.length>0) {
					fList = (document.forms[0].cb[0].value).split('|');
					//asignada = document.forms[0].cb[i].value;
					//asignada = asignada.substring(asignada.indexOf('|')+1, asignada.lenght);
					asignada = fList[1];
					
					/*alert('asignada '+asignada+''+
							'\nrastreo '+fList[0]+
							'\ncliente asig '+fList[2]+
							'\nusuario asig '+fList[3]		
					);*/
				}
				
				if (asignada == "Y") {
					alert("Las guías mostradas, son guías asignadas. No puede generar PDF por este medio. Seleccione guías requeridas y presione botón \"Crear PDF\"");
					continuar = false;
					document.forms[0].cveClienteAsig.value = "";
					document.forms[0].cveUserAsig.value = "";
					document.forms[0].genPDF.value = 'N';
					document.forms[0].clickedItemsPDF.value = "";
					document.forms[0].idSetSelPDF.value = "";
					return;
				} else {
					continuar = true;
					document.forms[0].cveClienteAsig.value = document.forms[0].clientId.value;
					document.forms[0].cveUserAsig.value = 'PDF_ALL';
					document.forms[0].genPDF.value = 'Y';
					document.forms[0].clickedItemsPDF.value = document.forms[0].clickedItems.value;
					document.forms[0].idSetSelPDF.value = document.forms[0].idSetSel.value;
				}	
			}			
		}
		
		if (continuar) {
			document.forms[0].currentTask.value = "asignaGuia";
			document.forms[0].banCerrar.value = "false";
			document.forms[0].action = "guiaConversionMonitor.do?insideTask="+document.forms[0].currentTask.value;
			showBarraEspera('mensaje','body','Espere por favor...Asignando guías');
			document.forms[0].target = "_self";
			document.forms[0].submit();
		}	
	}	
}

//retorna las cordenadas de posicion y tamaño
//ejemplo de uso:  window.open('aaa.jsp?llave=01'+getCoordenadas(450, 200), 'ventanax');
function getCoordenadas(xWidth, xHeight) {
	return 'dependent=yes, width='+xWidth+', height='+xHeight+', top='+((screen.availHeight - xHeight) / 2)+', left='+((screen.availWidth - xWidth) / 2);
}

/*funciones iframe*/
function initMonitorI(){
	parent.ocultarBarraEspera('mensaje','body');
	parent.document.forms[0].currentGuia.value = "";
	if (document.forms[0].error.value != "") {
		actualizaCheckGuias();
		alert(document.forms[0].error.value);
		document.forms[0].error.value = "";
		parent.document.forms[0].clickedItems.value = document.forms[0].clickedItems.value;
	}
	//parent.document.forms[0].banCerrar.value = "true";
	
}

function actualizaCheckGuias() {
	/*desmarca todos los checkbox*/
	for(var i=0;i < parent.document.forms[0].cb.length;i++) {
		parent.document.forms[0].cb[i].checked = false;		
	}
	
	var div = parent.document.forms[0].cb.length / 2;
	
	var fList = new Array();
	fList = (document.forms[0].clickedItems.value).split(' ');
	var valueCheck = new Array();
	
	for (var k = 0; k < fList.length; k++) {		
		for(var i=div;i < parent.document.forms[0].cb.length;i++) {			
			valueCheck = (parent.document.forms[0].cb[i].value).split('|');
			//document.forms[0].cb[i].checked = true;
			//document.forms[0].clickedItems.value = document.forms[0].clickedItems.value + ' '+valueCheck[0];
			//alert('valueCheck[0] '+valueCheck[0] +' fList[k] '+fList[k]);
			if (valueCheck[0] == fList[k]) {
				parent.document.forms[0].cb[i].checked = true;
				break;
			}
		}
	}
	parent.document.forms[0].contRastreos.value = fList.length - 1;
}

/*funciones iframe fin*/
//Abre un ventana centrada
//ejemplo de uso:  abrirVentana('aaa.jsp?llave=01', 'ventanax', 450, 200);
function abrirVentana(url, xWindow, xWidth, xHeight) {
	window.open(url, xWindow, getCoordenadas(xWidth, xHeight));
}

//Funcion para impedir que regrese al jsp anterior con el backspace o con alt + <--
var back='true';
function noBack(key){
	var valor=key.keyCode;		
	if(back=='true'){
		if(key.altKey){
			event.returnValue=false;
		}else{
			if(valor=='8'){
				event.returnValue=false;
			}
		}	
	}	
}
//Cambia el valor a la variable back...para que funcione el back espace en los inputs.
//En el onFocus=false; en el onBlur=true
function setBack(valor){
	back=valor;
}
//Funcion para impedir que refresque la pantalla con la tecla F5
//document.onkeydown = function(){ 
//    if(window.event && window.event.keyCode >= 112 && window.event.keyCode <= 123 ){
//   		window.event.keyCode = 505; 
//    }
//    if(window.event && window.event.keyCode == 505){ 
//    	return false;    
//    } 
//}
document.onkeydown=function(e) {
    var event = window.event || e;
    if (event.keyCode == 116) {
        event.keyCode = 0;
        alert("Esta accion no esta permitida");
        return false;
    }
}


function scrollWindow() {
	window.scrollBy(0,screen.availHeight);
}

function trim(value) {
	return value.replace(/^\s+|\s+$/g, '');
}