var siteData = [];
var objeto = {
	clienteConvenio:"",
	clienteMiembro:"",
	miembroNombre:"",
	site:"",
	siteName:"",
	direccion:"",
	sucursal:"",
	sucursalNombre:""
};
var actual = {};
//Se utiliza para obtener las plazas de un usuario
function getPlazas(){
	$.get( "./SiteChange", function() {
		console.log("Consultando plazas...");
		$("#ul_combo").html("");
	})
	.done(function(datos){;
		var li ="";
		siteData = datos;
		if(siteData.length>0){
			$('#btn_cambio').prop('disabled', false);
			$.each(siteData,function(i,key){
		    	if(i==0){
		    		$("#combo:first-child").text(key.siteName).append("<span class='caret'></span>");
		    	    $("#combo:first-child").val(key.site);
		    	    $("#dialog_addr").text(key.direccion);
		    	    $("#lbl_nuevo_cliente_miembro").text(key.clienteMiembro + " | " + key.miembroNombre);
		    	    objeto = key;
		    	}
		    	li+= "<li id='" + i + "'><a href='#'>"+ key.siteName + " " + key.clienteMiembro + " | " + key.miembroNombre +"</a></li>";
		    	$("#ul_combo").append(li);
		    	li = "";
		    });
		}
		else{
			alert("El usuario no tiene acceso a ninguna plaza");
			$('#btn_cambio').prop('disabled', true);
		}
	},"json")
	.fail(function(){
		alert("Ocurrio un problema al obtener la información");
	});
}

//Cambiamos el texto del combo
$("#ul_combo").on("click","li a",function(){
	var index = $(this).parent().attr("id");
	objeto = siteData[index];
    $("#combo:first-child").text(objeto.siteName).append("<span class='caret'></span>");
    $("#combo:first-child").val(objeto.site);
    $("#dialog_addr").text(objeto.direccion);
    $("#lbl_nuevo_cliente_miembro").text(objeto.clienteMiembro + " | " + siteData[index].miembroNombre);
});

$('#btn_cambio').on("click",function(){
	var pregunta = confirm("Esta seguro de cambiar de plaza?");
	if(pregunta){
		$.ajax({
			url:"./SiteChange",
			type:"POST",
			data: JSON.stringify(objeto),
			dataType:"json",
			contentType:"application/json; charset=utf-8",
			success:function(data){
				actual.clienteId = objeto.clienteConvenio;
				actual.clienteNombre = objeto.miembroNombre;
				actual.siteClave = objeto.site;
				actual.siteName = objeto.siteName;
				actual.clienteConvenio = objeto.clienteConvenio;
				$("#mensaje_lbl").text("Plaza: "  + actual.siteName);
				Lobibox.notify('info', {
        		    size: 'mini',
        		    msg: 'Se ha asignado la nueva plaza'
        		});
				$("#sitio_modal").modal('hide');
			},
			error:function(a,b,c){
				console.log("a: " + JSON.stringify(a));
				console.log("b: " + b);
				console.log("c: " + c);
			}
		});
	}
});

