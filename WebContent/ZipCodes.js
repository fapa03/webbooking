/*variables para el control de colores en seleccion de renglon*/
var colflag='false';
var rowid;
var rowidflag='true';
var oldobject;
var rownum;
/*fin de variables*/
function changeColor(obj) {
	rowid=obj.id;
	if(colflag=='true') {
		oldobject.style.backgroundColor='white';
	}
	obj.style.backgroundColor='#D0D2C5';
	oldobject=obj;	
	colflag='true';
}

function validColor(obj){
	if(colflag=='true') {
		if (obj.id != oldobject.id) {
			obj.style.backgroundColor='white';
			obj.style.cursor='default'
		}
	} else {
		obj.style.backgroundColor='white';
		obj.style.cursor='default'
	}
}
function initScript() {
	botonAcordeon();
	
	if (document.forms[0].msjeErr.value !='') {
		alert(document.forms[0].msjeErr.value);
		document.forms[0].msjeErr.value = '';
	}
	
	ocultarBarraEspera('mensaje','body');
	//MakeStaticHeader('conjuntoGuias', 240, 769, 40, false,'01');//ORIGINAL	
	MakeStaticHeader('conjuntoGuias', 280, 900, 30, false,'01');
	
	//alert('document.forms[0].idSetSel.value '+document.forms[0].idSetSel.value+'|');
	//alert('document.forms[0].clickedItems.value '+document.forms[0].clickedItems.value+'|');
	 

}

function botonAcordeon(){
	var acc = document.getElementsByClassName("accordion");
	var i;

	for (i = 0; i < acc.length; i++) {
		
	    acc[i].onclick = function(){
	        this.classList.toggle("active");
	        this.nextElementSibling.classList.toggle("show");
	        
	        if (document.forms[0].valAcordion.value=='off') {
	        	document.forms[0].valAcordion.value = 'on';
	        } else {
	        	document.forms[0].valAcordion.value = 'off';
	        }
	  }
	}
	
	if (document.forms[0].valAcordion.value == 'on') {
		//this.classList.toggle("active");
		document.getElementById("btnAcordeon").className += " active";
		document.getElementById("divAcordeon").className += " show";
        //this.nextElementSibling.classList.toggle("show");
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

function fastFindKey() {
	if(window.event.keyCode==13) {
		fastFind();
	}
}

function fastFind(){
	var continuar = true;
	if (trim(document.forms[0].find.value)=='') {
		alert("Capture valor en Busqueda Rapida");
		continuar = false;
	} else {
		//Ingrese más de 4 caracteres en su búsqueda.
		var arrayData = (document.forms[0].find.value).split('+');
		var dataLength = 0;
		for (var k = 0; k < arrayData.length; k++) {			
			if (arrayData[k]!=null) {
				dataLength =  dataLength + arrayData[k].length;
			}
		}
		
		if (dataLength<=4) {
			alert("Ingrese más de 4 caracteres en su busqueda.");
			continuar = false;			
		}			
	}
	
	if (continuar){
		document.forms[0].currentTask.value = "fastFind";
		showBarraEspera('mensaje','body','Espere por favor...buscando Código postal');
		document.forms[0].target = "_self";
		document.forms[0].submit();
	}
}

function selectEstado(){	
	//if (document.forms[0].zcEstado.value !='SEL') {
		document.forms[0].currentTask.value = "selectEstado";
		showBarraEspera('mensaje','body','Espere por favor...buscando Delegaciones / Municipios');
		document.forms[0].target = "_self";
		document.forms[0].submit();
	//}
}

function selectDelMun(){	
	//if (document.forms[0].zcDelMun.value !='SEL') {
		document.forms[0].currentTask.value = "selectDelMun";
		showBarraEspera('mensaje','body','Espere por favor...buscando Delegaciones / Municipios');
		document.forms[0].target = "_self";
		document.forms[0].submit();
	//}
}

function findDetailsKey() {
	if(window.event.keyCode==13) {
		findDetails();
	}
}

function findDetails(){
	if (document.getElementById('mensaje').style.visibility == 'hidden') {
		var continuar = true;
		var needCol = false;
		
		if (document.forms[0].zcEstado.value =='SEL') {
			alert('Seleccione al menos Estado');
			continuar= false;
		} else if (document.forms[0].zcDelMun.value =='SEL' && document.forms[0].zcCiuPob.value =='SEL') {
			needCol = true;
		}
		
		if (needCol && trim(document.forms[0].colonia.value)=='') {
			alert('Capture filtro en Colonia / Asentamiento');
			continuar = false;
		}
		
		if (continuar) {
			document.forms[0].currentTask.value = "findByDetail";
			showBarraEspera('mensaje','body','Espere por favor...buscando Código postal');
			document.forms[0].target = "_self";
			document.forms[0].submit();	
		}			
	}
}

function selectedSet(id) {
	if (document.getElementById('mensaje').style.visibility == 'hidden') {
		
		document.forms[0].zipCodeSel.value = document.getElementById('zipCode'+id).value;
		document.forms[0].colDesSel.value = document.getElementById('colDes'+id).value;
		document.forms[0].colCodSel.value = document.getElementById('colCod'+id).value;
		document.forms[0].colLevSel.value = document.getElementById('colLev'+id).value;
		document.forms[0].colTypeSel.value = document.getElementById('colType'+id).value;
		document.forms[0].idSucSel.value = document.getElementById('idSuc'+id).value;
		document.forms[0].ciuDesSel.value = document.getElementById('ciuDes'+id).value;
		document.forms[0].ciuCodSel.value = document.getElementById('ciuCod'+id).value;
		document.forms[0].ciuLevSel.value = document.getElementById('ciuLev'+id).value;
		document.forms[0].ciuTypeSel.value = document.getElementById('ciuType'+id).value;
		document.forms[0].munDesSel.value = document.getElementById('munDes'+id).value;	
		document.forms[0].munCodSel.value = document.getElementById('munCod'+id).value;
		document.forms[0].munLevSel.value = document.getElementById('munLev'+id).value;
		document.forms[0].munTypeSel.value = document.getElementById('munType'+id).value;
		document.forms[0].edoDesSel.value = document.getElementById('edoDes'+id).value;
		document.forms[0].edoCodSel.value = document.getElementById('edoCod'+id).value;
		document.forms[0].edoLevSel.value = document.getElementById('edoLev'+id).value;
		document.forms[0].edoTypeSel.value = document.getElementById('edoType'+id).value;
		document.forms[0].paisDesSel.value = document.getElementById('paisDes'+id).value;
		document.forms[0].paisCodSel.value = document.getElementById('paisCod'+id).value;
		document.forms[0].paisLevSel.value = document.getElementById('paisLev'+id).value;
		document.forms[0].paisTypeSel.value = document.getElementById('paisType'+id).value;
		document.forms[0].zonaDesSel.value = document.getElementById('zonaDes'+id).value;
		document.forms[0].zonaCodSel.value = document.getElementById('zonaCod'+id).value;
		document.forms[0].zonaLevSel.value = document.getElementById('zonaLev'+id).value;
		document.forms[0].zonaTypeSel.value = document.getElementById('zonaType'+id).value;	
	}	
}

function asigValselect(){
	if (document.getElementById('mensaje').style.visibility == 'hidden') {
		if (document.forms[0].zipCodeSel.value == ''){
			alert("Seleccione una direccion");
		} else {
			opener.setValSelZipCode();
			window.close();
		}	
	}
}

function clearSelected() {
	document.forms[0].zipCodeSel.value = "";
	document.forms[0].colDesSel.value = "";
	document.forms[0].colCodSel.value = "";
	document.forms[0].colLevSel.value = "";
	document.forms[0].colTypeSel.value = "";
	document.forms[0].idSucSel.value = "";
	document.forms[0].ciuDesSel.value = "";
	document.forms[0].ciuCodSel.value = "";
	document.forms[0].ciuLevSel.value = "";
	document.forms[0].ciuTypeSel.value = "";
	document.forms[0].munDesSel.value = "";	
	document.forms[0].munCodSel.value = "";
	document.forms[0].munLevSel.value = "";
	document.forms[0].munTypeSel.value = "";
	document.forms[0].edoDesSel.value = "";
	document.forms[0].edoCodSel.value = "";
	document.forms[0].edoLevSel.value = "";
	document.forms[0].edoTypeSel.value = "";
	document.forms[0].paisDesSel.value = "";
	document.forms[0].paisCodSel.value = "";
	document.forms[0].paisLevSel.value = "";
	document.forms[0].paisTypeSel.value = "";
	document.forms[0].zonaDesSel.value = "";
	document.forms[0].zonaCodSel.value = "";
	document.forms[0].zonaLevSel.value = "";
	document.forms[0].zonaTypeSel.value = "";
}



//retorna las cordenadas de posicion y tamaño
//ejemplo de uso:  window.open('aaa.jsp?llave=01'+getCoordenadas(450, 200), 'ventanax');
function getCoordenadas(xWidth, xHeight) {
	return 'dependent=yes, width='+xWidth+', height='+xHeight+', top='+((screen.availHeight - xHeight) / 2)+', left='+((screen.availWidth - xWidth) / 2);
}



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