function initScript() {
	
	ocultarBarraEspera('mensaje','body');
	MakeStaticHeader('conjuntoGuias', 240, 769, 40, false,'01');	
	
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
		MakeStaticHeader('detalleGuias', 290, 769, 40, false,'02');	
	}
}


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
		
		document.forms[0].currentTask.value = "loadSetDetail";
		document.forms[0].action = "usedGuia.do";
		showBarraEspera('mensaje','body','Espere por favor...Cargando Detalle de Guias');
		document.forms[0].submit();	
	}	
}

function mainpage() {
	if (document.getElementById('mensaje').style.visibility == 'hidden') {
		//clearSelected();
		document.forms[0].currentTask.value = "mainpage";
		document.forms[0].action = "usedGuia.do";
		showBarraEspera('mensaje','body','Espere por favor... Cargando menu principal');
		document.forms[0].submit();
	}
}

function cambiarFiltro() {
	if (document.getElementById('mensaje').style.visibility == 'hidden') {
		//clearSelected();
		document.forms[0].currentTask.value = "cambioFiltro";
		document.forms[0].action = "usedGuia.do";
		showBarraEspera('mensaje','body','Espere por favor... Cargando Detalle de Guias');
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