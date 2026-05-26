var form;
$(document).ready(function() {
	$(".search").on("keyup", function() {
		$(".trEtiquetas").hide();
		var value = $(this).val().toUpperCase();
		$(".guiaRow").each(function(index) {
			$row = $(this);
			var $tdElement = $row.find("td:first");
			var $tdElement2 = $row.find("td:eq(1)");
			var $tdElement3 = $row.find("td:eq(2)");//AAP01
			
			var id = $tdElement.text().toUpperCase();
			var id2 = $tdElement2.text().toUpperCase();
			var id3 = $tdElement3.text().toUpperCase();//AAP01
			
			var matchedIndex = id.search(value);
			var matchedIndex2 = id2.search(value);
			var matchedIndex3 = id3.search(value);//AAP01
			
			if (matchedIndex > -1 || matchedIndex2 > -1 || matchedIndex3 > -1) {//AAP01
				$(this).show();
			} else {
				$(this).hide();				
			}
		});
	});
});
function initScript() {
	if(document.forms[0].currentTask.value == "download"){
		if(document.forms[0].allowPrintQZ.value == "Y"){
			initBridgePrintQZ('cadena');
		} else {
			ocultarBarraEspera('mensaje', 'body');
			window.open('./DownloadFileGuia.jsp');
		}
	}
}
function printGuias() {
	$("#buttonPrint").prop('disabled', true);
	showBarraEspera('mensaje','body','Cargando información de archivo');
	document.forms[0].currentTask.value = "print";
	document.forms[0].submit();
}
function refreshGuias() {//AAP01
	$("#buttonPrint").prop('disabled', true);
	showBarraEspera('mensaje','body','Cargando información');
	document.forms[0].currentTask.value = "start";
	document.forms[0].submit();
}
function toggleEtiqueta(guiaNumber, numEtiqueta) {
	var number = parseInt($("#totEtiq" + guiaNumber).text());
	if ($("#checkEtiquetaDtl" + guiaNumber + "-" + numEtiqueta).is(':checked')) {
		number++;
		$("#checkEtiquetas" + guiaNumber).prop('checked', true);
	} else {
		number--;
	}
	if (number >= 0)
		$("#totEtiq" + guiaNumber).text(number);
	if (number <= 0)
		$("#checkEtiquetas" + guiaNumber).prop('checked', false);
}

function toggleAllGuias() {//Selecciona todas las guias
	if ($('input[name="checkAllGuias"').is(':checked')) {
		$("input[name*='checkGuia']").each(function() {
			$(this).prop('checked', true);
		});
	} else {
		$("input[name*='checkGuia']").each(function() {
			$(this).prop('checked', false);
		});
	}

}
function toggleAllEtiquetas() {
	if ($('input[name="checkAllEtiquetas"').is(':checked')) {		
		
		$("input[name*='checkEtiquetas']").each(function() {
			$(this).prop('checked', true);
		});
		
		$("input[name*='checkEtiquetaDtl']").each(function() {
 			$(this).prop('checked', true);
		}); 
		
		$("div[id*='totEtiq']").each(function() {
 			$(this).text($(this).siblings('#numEtiqGuia').text());
		}); 
		
	} else {
		
		$("input[name*='checkEtiquetas']").each(function() {
 			$(this).prop('checked', false);
 		});
		
 		$("input[name*='checkEtiquetaDtl']").each(function() {
 			$(this).prop('checked', false);
 		}); 
 		
		$("div[id*='totEtiq']").each(function() {
 	 			$(this).text('0');
 			});
	}
}

function botonAcordeon(guiaNumber) {
	var x = false;
	if ($("#trEtiquetas" + guiaNumber + "").css('display') == 'table-row')
		x = true;
	$(".trEtiquetas").hide().slideUp("slow");
	if (x == false)
		$("#trEtiquetas" + guiaNumber + "").show().slideDown("slow");
}

function toggleEtiqByGuia(guiaNumber) {
	if ($("#checkEtiquetas" + guiaNumber).is(':checked')) {
		$("#totEtiq" + guiaNumber).text(
				$("#totEtiq" + guiaNumber).siblings('#numEtiqGuia').text());
		$("input[id*='checkEtiquetaDtl" + guiaNumber + "']").each(function() {
			$(this).prop('checked', true);
		});
	} else {
		$("#totEtiq" + guiaNumber).text('0');
		$("input[id*='checkEtiquetaDtl" + guiaNumber + "']").each(function() {
			$(this).prop('checked', false);
		});
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