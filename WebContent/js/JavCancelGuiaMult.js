var form;

$(document).ready(function() {
	$(".search").on("keyup", function() {
		//busca si el filtro de fecha es activo
		$("#checkAllguias").prop('checked', false);	
			$(".trEtiquetas").hide();
			var value = $(this).val().toUpperCase();
			$(".guiaRow").each(function(index) {
				$row = $(this);
				var $tdElement = $row.find("td:eq(3)");
				var id = $tdElement.text().toUpperCase();
				$("input[name*='checkGuia']").each(function() {
					$(this).prop('checked', false);
				});
				var coincide = validTextSearch (value, $row); 
				if (coincide ){
					$(this).show();
				}else{
					$(this).hide();
				}
			});
	});
});

function obtenerFechaActual(){
	 var fechaHoraActual = new Date();
	 var year = fechaHoraActual.getFullYear();
	 var mes = ('0' + (fechaHoraActual.getMonth() + 1)).slice(-2); // Agregar cero a la izquierda si es necesario
	 var dia = ('0' + fechaHoraActual.getDate()).slice(-2);
	 var fechaFormateada = year + mes + dia;
	return fechaFormateada;
}

function validTextSearch(textoBuscado, $row){	 
	textoBuscado = textoBuscado.toUpperCase();
	var $tdElement = $row.find("td:first");
	var $tdElement2 = $row.find("td:eq(1)");
	var $tdElement4 = $row.find("td:eq(2)");
	var $tdElement5 = $row.find("td:eq(4)");
	var $tdElement6 = $row.find("td:eq(5)");
	var $tdElement7 = $row.find("td:eq(6)");
	var id = $tdElement.text().toUpperCase();
	var id2 = $tdElement2.text().toUpperCase();
	var id4 = $tdElement4.text().toUpperCase();
	var id5 = $tdElement5.text().toUpperCase();
	var id6 = $tdElement6.text().toUpperCase();
	var id7 = $tdElement7.text().toUpperCase();
	var matchedIndex = id.search(textoBuscado);
	var matchedIndex2 = id2.search(textoBuscado);
	var matchedIndex4 = id4.search(textoBuscado);
	var matchedIndex5 = id5.search(textoBuscado);
	var matchedIndex6 = id6.search(textoBuscado);
	var matchedIndex7 = id7.search(textoBuscado);
	if (matchedIndex > -1 || matchedIndex2 > -1 || matchedIndex4 > -1 || matchedIndex5 > -1 || matchedIndex6 > -1 || matchedIndex7 > -1) {
		return true; 
	}else {
		return false; 		
	}	   
}

function obtenerFechaActual(){
	 var fechaHoraActual = new Date();
	 var year = fechaHoraActual.getFullYear();
	 var mes = ('0' + (fechaHoraActual.getMonth() + 1)).slice(-2); // Agregar cero a la izquierda si es necesario
	 var dia = ('0' + fechaHoraActual.getDate()).slice(-2);
	 var fechaFormateada = year + mes + dia;
	return fechaFormateada;
}

function initScript() { 
	var flagRadio = document.forms[0].filtroPor.value
	if (flagRadio == "1"){
		 $("#filtro1").prop('checked', true);
	 }else if (flagRadio == "2" ){
		 $("#filtro2").prop('checked', true);
	 }
	
	ocultarBarraEspera('mensaje','body');
	 
	if(document.forms[0].currentTask.value == "download" || document.forms[0].currentTask.value == ""){
		openModal()
	}
}

function openModal(){
	$("#resumen_modal").modal({	
		show:true,
		backdrop: 'static', 
		keyboard: false
	});
}

function closeModal(){
	$("#resumen_modal").modal({
		show:false,
		backdrop: 'static', 
		keyboard: true
	});
	showBarraEspera('mensaje','body','Actualizando informaci\u00f3n de gu\u00edas');
	document.forms[0].currentTask.value = "start";
	document.forms[0].submit();
}

function descargarxls(){
	document.forms[0].currentTask.value = "download";
	document.forms[0].submit();
}

function cancelGuias() {
	document.forms[0].currentTask.value = "cancelMult";
	$("#buttonCancel").prop('disabled', true);
	showBarraEspera('mensaje','body','Cargando informaci\u00f3n de archivo');
	document.forms[0].submit();
}

function refreshGuias() {
	$("#buttonPrint").prop('disabled', true);
	showBarraEspera('mensaje','body','Cargando informaci\u00f3n');
	document.forms[0].currentTask.value = "start";
	document.forms[0].submit();
}

function toggleAllGuias() {
	if ($('input[name="checkAllGuias"').is(':checked')) {
		$("input[name*='checkGuia']").each(function() {
			if ($(this).is(':visible')){ 
				$(this).prop('checked', true);
			}
		});
	} else {
		$("input[name*='checkGuia']").each(function() {
			if ($(this).is(':visible')){ 
				$(this).prop('checked', false);
			}
		});
	}
}

function goOut(page){
	if (document.getElementById('mensaje').style.visibility=='hidden') {
		if (confirm("DESEA PERDER LA INFORMACI\u00d3N? ")) {
			showBarraEspera('mensaje','body','Espere por favor...Saliendo de documentaci\u00f3n');
			document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page="+page;
			document.forms[0].submit();
		} else {
			return;
		}
	}	
}

function cambioFiltroPor(){
	showBarraEspera('mensaje','body','Cargando informaci\u00f3n');
	document.forms[0].currentTask.value="inicial";
	document.forms[0].submit();
}

function buscarRangoFechas(){
	diferencia =  obtenerDiferencia($("#fechaInicio").val()) - obtenerDiferencia($("#fechaFin").val()); 
	
	var fechaInicio = $("#fechaInicio").val();
	var fechaFin = $("#fechaFin").val();
	
	var formtFechaInicial = cambiarFormtFecha($("#fechaInicio").val()); 
	var formtFechaFinal = cambiarFormtFecha($("#fechaFin").val()); 
	
	fechaInicio= fechaInicio.replaceAll("-", "");  
	fechaFin= fechaFin.replaceAll("-", "");  
	
	if ((fechaInicio > fechaFin) && (fechaFin > 0)){
		alert("La fecha incial no puede ser mayor a la fecha final");
		 $("#fechaInicio").val(""); 
	}else if (fechaFin > obtenerFechaActual()) {
		alert("La fecha final no puede ser mayor a la fecha actual");
		 $("#fechaFin").val(""); 
	}else if (diferencia > document.forms[0].daysRange.value ){ //Se genera en caso de quee el rango buscado sobrepase del rango de dias. 
		alert ("El rango de d\u00edas buscados no debe pasar de mas de " + document.forms[0].daysRange.value + " d\u00edas." )
	}else if ((fechaInicio == 0) || (fechaFin == 0)){
		alert("Los campos de fecha no deben estar vac\u00edos ");	
	}else{ 
		showBarraEspera('mensaje','body','Cargando informaci\u00f3n');
		document.forms[0].currentTask.value="start";
		document.forms[0].fechaInicial.value= formtFechaInicial;
		document.forms[0].fechaFinal.value= formtFechaFinal; 
		document.forms[0].infoDate.value= "Las guias mostradas corresponden del dia " + formtFechaInicial +  " al " + formtFechaFinal;
		document.forms[0].submit();
	}
}

function obtenerDiferencia(fecha){
	var fechaActual = new Date();
	var fechaBuscada = new Date(fecha);
	var diferenciaMilisegundos = (fechaActual - fechaBuscada);
	// Convertir la diferencia de milisegundos a días
	var milisegundosEnUnDia = 1000 * 60 * 60 * 24;
	var diferenciaDias = Math.floor(diferenciaMilisegundos / milisegundosEnUnDia);
	return diferenciaDias ; 
}

function cambiarFormtFecha(fecha){ //Se convierte a formato DD/MM/YYYY
	var partesFecha = fecha.split('-'); 
	FechaBuscada = partesFecha[2] + "-" +  partesFecha[1] + "-" + partesFecha[0];
	return FechaBuscada; 
}

function reinitGuias(){
	showBarraEspera('mensaje','body','Cargando informaci\u00f3n');
	document.forms[0].currentTask.value="";
	document.forms[0].submit();
}
