	function showBarraEspera(idMensaje,idBody,mensaje){		
		document.getElementById(idMensaje).value=mensaje;
		document.getElementById(idMensaje).style.visibility='visible';
		document.getElementById(idBody).style.cursor = 'wait';                           
	}//fin showBarraEspera
	
	//Funcion para ocultar la barra de espera.
	function ocultarBarraEspera(idMensaje,idBody){
		document.getElementById(idMensaje).style.visibility='hidden';
		document.getElementById(idBody).style.cursor = 'default';	
	}
	/**
	* Funci�n que calcula el centro de la ventana actual o de un rect�ngulo de
	* las dimensiones que se le pasen, devuelve el v�rtice superior izquierdo
	*
	*/
	window.center = function(){
	   var hWnd = (arguments[0] != null) ? arguments[0] : {width:0,height:0};
	   var _x = 0;
	   var _y = 0;
	   var offsetX = 0;
	   var offsetY = 0;
	   //IE
	   
	   if(!window.pageYOffset){
		  //strict mode
	      if(!(document.documentElement.scrollTop == 0)){
	         offsetY = document.documentElement.scrollTop;
	         offsetX = document.documentElement.scrollLeft;
	      }
	      //quirks mode
	      else{
	         offsetY = document.body.scrollTop;
	         offsetX = document.body.scrollLeft;
	      }
	   }
	   //w3c
	   else{
	      offsetX = window.pageXOffset;
	      offsetY = window.pageYOffset;
	   }
	   _x = ((screen.width-hWnd.width)/2)+offsetX-200;
	   _y = ((screen.height-hWnd.height)/2)+offsetY-40;
	   return{x:_x,y:_y};
	}
	
	function mostrarCapaCentro(capaId) {
		var miCapa = document.getElementById(capaId);
	
		//if (miCapa.style.visibility=='visible'){
		    var punto = window.center();
            miCapa.style.top = punto.y + "px";
			//centro la capa en el eje y
            miCapa.style.left = punto.x + "px";
			//centro la capa en el eje x
            miCapa.style.display="block";
			//muestro la capa
		//}
	}	