<%@page contentType="text/html" import="java.util.ArrayList,bean.ReadxlDTO, bean.Global"%>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="WEB-INF/struts-bean.tld" prefix="bean" %>
<%
	Global global = (Global) session.getAttribute("sGlobal");
%>

<head>
<title>  Data File Import And Processing Page</title>
<script type="text/javascript" src="barraEspera.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

<link rel=stylesheet media=screen type=text/css href="css/estilos-reset.css">
<link rel=stylesheet media=screen type=text/css href="webbookingMonitor.css">
<link rel=stylesheet media=screen type=text/css href="css/estilos.css">

<link rel="stylesheet" media="screen" type="text/css" href="css/font-awesome.min.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>

<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />

<script type="text/javascript" charset="UTF-8" src="js/qz/bridgePrintQZ.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/QZTray.css">
	
<link rel="stylesheet" media="screen and (min-width: 1360px)" href="css/estilos-1366.css" />
<link rel="stylesheet" media="screen and (min-width: 1600px)" href="css/estilos-1600.css" />
<link rel="stylesheet" media="screen and (min-width: 1900px)" href="css/estilos-1900.css" />



<script language="javascript">

function init(){
	ocultarBarraEspera('mensaje','body');
	task = document.forms[0].currentTask.value;
	switch(task) {
		
	    case "":
	        document.forms[0].procesar.disabled=true;
	        document.forms[0].imprimir.disabled=true;
	        document.forms[0].descargarxls.disabled=true;
	        
	        if (document.forms[0].fileName.value !='') {
	        	if (confirm('Se encontró archivo '+document.forms[0].fileName.value+' pendiente de finalizar. ¿Desea recargar la información?' )) {
	        		showBarraEspera('mensaje','body','Cargando información de archivo');
	        		window.document.forms[0].action="webbookingFileUpload2.do?action=reload";
					window.document.forms[0].submit();
	        	} else {	        		
	        		showBarraEspera('mensaje','body','finalizando procesamiento de archivo');
	        		window.document.forms[0].action="webbookingFileUpload2.do?action=reinit";
					window.document.forms[0].submit();
	        	}
	        }
	        break;
	    case "reinit":
	        document.forms[0].procesar.disabled=true;
	        document.forms[0].imprimir.disabled=true;
	        document.forms[0].descargarxls.disabled=true;
	        break;
	    case "upload":
	        document.forms[0].imprimir.disabled=true;
	        document.forms[0].descargarxls.disabled=true;
	        break;
	    case "process":
	        document.forms[0].cargar.disabled=true;
	        document.forms[0].procesar.disabled=true;
	        break;
	    case "reload":
	        document.forms[0].cargar.disabled=true;
	        document.forms[0].procesar.disabled=true;
	        break;
	    case "print":
	        document.forms[0].cargar.disabled=true;
	        document.forms[0].procesar.disabled=true;
	        break;
	   case "log":
	        document.forms[0].cargar.disabled=true;
	        document.forms[0].procesar.disabled=true;
	        break;
	}
}

function disabledButtons(){
	if (document.forms[0].currentTask.value!="print" && document.forms[0].currentTask.value != "log") {
	    document.forms[0].procesar.disabled=true;
	    document.forms[0].imprimir.disabled=true;
	    document.forms[0].descargarxls.disabled=true;
	    document.forms[0].cargar.disabled=true;
	    document.forms[0].reiniciarPantalla.disabled=true;
	}
}

function validate() {
	var file=window.document.forms[0].theFile.value;
	if( file=='') {
	   alert("Favor de seleccionar un archivo .xls o .xlsx");
	   window.document.forms[0].theFile.focus();
	   return false;
	}
	showBarraEspera('mensaje','body','Cargando y Validando archivo');
	document.forms[0].action="webbookingFileUpload2.do?action=upload";
	return true;
}
function validateProcess() {
	//valida si el cliente quiere continuar con el proceso a pesar de las incorrectas. EMPP
	var countError=window.document.forms[0].countGuiaError.value;
	var countInfo = window.document.forms[0].countGuiaInfo.value;
	if (countError>0){
		
		if (countInfo < 1 ){
			alert ('El archivo no contiene registros correctos, seleccionar otro archivo.'); 
			return false
		}
		else if ( confirm('El archivo contiene '+countError+' registros fallidos. ¿Desea procesar la información?' )) {
			window.document.forms[0].action="webbookingFileUpload2.do?action=process";
			showBarraEspera('mensaje','body','Procesando Archivo');
			return true;
		}else {return false }
	}else {
		window.document.forms[0].action="webbookingFileUpload2.do?action=process";
		showBarraEspera('mensaje','body','Procesando Archivo');
		return true;
	}
}

function validatePrint() {	
	window.document.forms[0].action="webbookingFileUpload2.do?action=print";
	return true;
}

function validateLog() {	
	window.document.forms[0].action="webbookingFileUpload2.do?action=log"
	return true;
}

function goOut(page) {
	
	if (page == "guiabooking") {
		page = "thispage";
		if (document.forms[0].showWeb.value != 'Y') {
			alert('USUARIO NO AUTORIZADO PARA OPCION ELABORACION DE GUIA DE RETORNO');
			return;
		}
	}
	if (confirm("DESEA PERDER LA INFORMACION?")) {
		if (page == 'shipmenthistory') {//AAP01
			document.forms[0].action = "shipmentHistory.do";//AAP01
		}else{//AAP01
			document.forms[0].action = "webBookinggeneralMain.do?includeattribute=yes&page="+ page;//AAP01	
		}//AAP01S
		document.forms[0].submit();
	} else {
		return;
	}
}
		
	
function logOut(){
	window.document.forms[0].action="login.do?logout=yes";
	document.forms[0].submit();
	
}


function downloadPrintGuia(){
	isDownloaded=true;
	document.forms[0].currentTask.value = "printReturn";
	if(document.forms[0].allowPrintQZ.value == "Y"){
		initBridgePrintQZ('cadenaWW');
	}else{
		window.open('./DownloadFileGuia.jsp');	
	}	
}

function downloadPrintGuiaRetorno(){
	isDownloaded=true;
	document.forms[0].currentTask.value = "print";
	if(document.forms[0].allowPrintQZ.value == "Y"){
		initBridgePrintQZ('cadenaRet');
	}else{
		window.open('./DownloadFileGuiaReturn.jsp');	
	}
	
}

function downloadXlsGuia(){
	isDownloaded=true;
	document.forms[0].currentTask.value = "log";
	document.forms[0].action = "DownloadXlsFileGuia.jsp?fileName="+document.forms[0].fileName.value;
	window.open("./DownloadXlsFileGuia.jsp?fileName="+document.forms[0].fileName.value);
}

function downloadPdfGuia(guiaFile){ 
	window.open(document.forms[0].linkGuiaEtiPdf.value);
}

function reinicioPantalla() {
	/*no se envia action para reiniciar*/
	if (confirm('Si no ha impreso guias, no podrá recuperar los datos ¿Desea perder los cambios realizados?') ) {
		document.forms[0].flagGuiaReturn.value = "N";
		document.forms[0].flagGuiaEtiPdf.value = "N";
		window.document.forms[0].action="webbookingFileUpload2.do?action=reinit";
		showBarraEspera('mensaje','body','Finalizando evento actual');
		return true;
	} else {
		return false;
	}
	
}
function downloadPrintGuiaPDF(guia, acuse) {
	isDownloaded = true;
	window.open(guia, '_blank');
	
	if(acuse != ""){
		window.open(acuse, '_blank');
	}
}

</script>


<style type="text/css">
#everything-wrapper {
	background: transparent !important;
}

div.file-upload-zone {
	background-color: transparent;
	border: none;
	padding: 0 !important;
	margin: 0 !important;
	height: 55px;
}

.file-upload-zone {
	padding: 0;
	margin: 0;
}

.file-upload-zone .inner-area {
	background: transparent !important;
	border: none !important;
	padding: 0 !important;
	
}

.file-upload-zone .inner-area-b button {
	margin: 0;
}
</style>
    
</head>

<body id="body" onload="init();mostrarCapaCentro('mensaje');" onscroll="mostrarCapaCentro('mensaje');" class="backgroundStandard" >


<main>

 
<table border="0" cellspacing="0" accept-charset="utf-8" cellpadding="0" class="width100porcent backgroudBluePtx">
			<tr>
				<td>
					<div class="bodyWidth marginAutoCentro">
						<img src="images/logos/logoW.png" border="0" 
						onclick="goOut('mainpage');hideMenu();" 
							style="max-width: 138px;margin-top: 11px;margin-bottom: 9px;cursor:pointer;"> 
							<a id="menu-hamburger" class="Animated1" onclick="showMenu()">
								<div style="margin-top: 15px;">
									<span id="menuline1" class="Animated05"></span> <span
										id="menuline2" class="Animated05"></span> <span id="menuline3"
										class="Animated05"></span>
								</div>
							</a>
					</div>
				</td>
				<td >
				</td>
			</tr>
		</table>
		<div class="MenuOverlay displayNone">
			<a class="btnCloseMenu" onclick="hideMenu()">
				<div>
					<span class="lineOne"></span> 
					<span class="lineTwo"></span>
				</div>
			</a>
			<div class="itemsMenu marginAutoCentro bodyWidth">
				<logic:present name="loginForm">
					<html:hidden name="loginForm" property="userValidate" />
					<logic:equal name="loginForm" property="userValidate"
						value="validUser">
						<a onclick="goOut('mainpage');hideMenu();" style="">Menú Principal</a>
						<a href="clientDestinationEntry.do" style="">Registro de Cliente Destino</a>
						<logic:equal name="loginForm" property="showWeb" value="Y">
		         		<logic:equal name="loginForm" property="newBooking" value="Y">
		         			<a onclick="goOut('thispage');hideMenu();" style="">Elaboración de guías</a>
  						</logic:equal>
	  					</logic:equal> 
	  					<logic:equal name="loginForm" property="showPpg" value="Y">
							<a href="guiaConversionMonitor.do?currentTask=start">Elaboración de guías prepago</a>
			  			</logic:equal>
						<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de Guía</a>
						<a onclick="goOut('shipmenthistory');hideMenu();" style="">Histórico de Envíos</a>
						<a onclick="goOut('clientreport');hideMenu();" style="">Información	del Cliente</a>
						<logic:present name="loginForm">
							<logic:notEqual name="loginForm" property="milliSeconds"
								value="1000">
								<!-- si no viene de Customer central, se muestra cerrar sesion -->
								<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>
							</logic:notEqual>
						</logic:present>
					</logic:equal>
				</logic:present>
				<logic:present name="loginForm">
					<html:hidden name="loginForm" property="userValidate" />
					<logic:notEqual name="loginForm" property="userValidate"
						value="validUser">
						<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a>
						<logic:equal name="loginForm" property="showWeb" value="Y">
		    			<logic:equal name="loginForm" property="newBooking" value="Y">
		    				<a onclick="goOut('thispage');hideMenu();" style="">Elaboración de guías</a>
	  					</logic:equal>
		  				</logic:equal> 
		  				<html:hidden name="loginForm" property="showPpg"></html:hidden>
						<logic:equal name="loginForm" property="showPpg" value="Y">
							<ahref="guiaConversionMonitor.do?currentTask=start">Elaboración de guías prepago</a>
						</logic:equal>	
						<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de guías</a>
						<a onclick="goOut('shipmenthistory');hideMenu();" style="">Historico de envios</a>
						<a onclick="goOut('clientreport');hideMenu();" style="">Información	de cliente</a>
						<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar	sesión</a>
					</logic:notEqual>
				</logic:present>
			</div>
		</div>
		<table>
			<tr>
				<td style="height: 8px;">
					<textarea id="mensaje" class="textareacontenido" cols="60" readonly="readonly" rows="5" onblur="self.focus();" style="background-color: #EEEEEE; border-style: ridge; 
						overflow: hidden; text-align: center; vertical-align: middle; z-index: 2; visibility: hidden; position: absolute; top: 450px; left: 200px; width: 400px; height: 80px">
					</textarea>
				</td>
			</tr>
		</table>
  

<div id="everything-wrapper"> 

<div class="menu-superior-derecho sm-blue">
  Sucursal: <logic:present name="branchid">
				<bean:write name="branchName"/>
			</logic:present>
	<logic:present name="loginForm">
		<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->		
		&nbsp;&nbsp;<a class="txt-black" href="login.do?logout=yes">Terminar sesi&oacute;n</a>
		</logic:equal>
	</logic:present>
</div>
<div style="clear: both;"></div>


<jsp:include page="nocache.jsp" flush="false" />


<html:form action="/webbookingFileUpload2.do"  enctype="multipart/form-data" onsubmit="disabledButtons();">
 
 
 


<table cellspacing="0" cellpadding="0" border="0" class="marginAutoCentro bodyWidth" >
	<tr>
		<td><div class="titleSection fontBold" >Procesamiento de Hojas de c&aacute;lculo (Excel)</div>

<div style="clear: both;"></div>

		</td>
	</tr>
	<logic:present name="errormsgprint" scope="request">
	<tr>
		<td>
	 		<div align="center"> 
	 		<font style="font-size: 12pt; color: #ff0000; font-weight: bold"><bean:write name="errormsgprint" /></font></div>
		</td>
	</tr>
	</logic:present>
	<tr width="100%" >
		<td >
		  <div align="left" class="file-upload-zone">
              <div class="inner-area">      
        			<span class="txt-special-1">Nombre de archivo :</span>
        			<html:file property="theFile" />
              </div> 
              <div class="inner-area-b">
        			<html:submit style="padding: 10px;" onclick="return validate();" property="cargar" styleClass="btn-oldstyle">Cargar Archivo</html:submit>
        			<html:submit style="padding: 10px;"  onclick="return validateProcess()" property="procesar" styleClass="btn-oldstyle">Procesar</html:submit>
              </div>
          	  <logic:present name="FileUpLoad2" scope="request">
					<logic:equal name="FileUpLoad2" property="flagGuiaProcess" value="Y">
						<div class="inner-area-c">
		             		<strong class="fontBold">Registros Correctos :  </strong><span class="txt-special-1"><bean:write name="FileUpLoad2" property ="countGuiaInfo"/></span><br>
		        			<strong class="fontBold">Registros Incorrectos :  </strong><span class="txt-special-1"><bean:write name="FileUpLoad2" property ="countGuiaError"/></span><br>
		              </div>
					</logic:equal>
			</logic:present>
              
		  </div>
		</td>
	</tr>
	<tr width="100%">
		<td>
			<div style="overflow: scroll;" id="DivMainContent01" class="DivMainContent01-table-responsive">
				<table class="tableFixed tablaDetallev2" id="TableMainContent01">
					<tr>
						<th class="nobg widthSmall">id</th>
						<th class="nobg widthLarge">Nombre</th>
						<th class="widthMedium">RFC</th>
						<th class="widthMedium">Pais</th>
						<th class="widthMedium">Estado</th>
						<th class="widthMedium">Ciudad</th>
						<th class="widthMedium">Calle</th>
						<th class="widthMedium">N&uacute;mero</th>
						<th class="widthMedium">Colonia</th>
						<th class="widthMedium">C&oacute;digo Postal</th>
						<th class="widthMedium">Email</th>
						<th class="widthMedium">Tel&eacute;fono</th>
						<th class="widthLarge">Referencia Domicilio</th>
						<th class="widthSmall">Cantidad</th>
						<th class="widthSmall">Codigo bulto</th>
						<th class="widthSmall">Contenido</th>
						<th class="widthSmall">Tipo Tarifa</th>
						<th class="widthSmall">Peso</th>
						<th class="widthSmall">Volumen</th>
						<th class="widthSmall">Largo</th>
						<th class="widthSmall">Ancho</th>
						<th class="widthSmall">Alto</th>
						<th class="widthSmall">Tipo Entrega</th>
						<th class="widthSmall">Forma Pago</th>
						<th class="widthSmall">Acuse</th>
						<th class="widthSmall">COD</th>
						<th class="widthSmall">Valor Declarado</th>
						<th class="widthSmall">Cobertura</th>
						<th class="widthSmall">Comentarios</th>
						<th class="widthSmall">Referencia</th>
						<th class="widthSmall">Agente Aduanal</th>
						<th class="widthSmall">Pedimento</th>
						<th class="widthSmall">Centro de costo</th>
						<th class="encabezado-respuesta widthSmall">Clave Mensaje</th>
						<th class="encabezado-respuesta widthSmall">Tipo Mensaje</th>
						<th class="encabezado-respuesta widthLarge" >Descripcion Mensaje</th>
						<th class="encabezado-respuesta widthSmall">Rastreo</th>
						<th class="encabezado-respuesta widthSmall">Guia</th>
						<th class="encabezado-respuesta widthSmall">Importe</th>
						<th class="encabezado-respuesta widthSmall">Descargas</th>
						<th class="widthMedium">Cantidad rastreos retorno</th>
						<th class="widthSmall">Tipo Servicio</th><!--AAP25-->
						<th class="widthSmall">ID Producto</th>
					</tr>
				<%
				ArrayList conjuntoGuias = (ArrayList)request.getAttribute("matriz");
				if (conjuntoGuias == null || conjuntoGuias.size()==0) {
					for(int i=0;i <= 19; i++){
				%>	
					<tr>
						<td>&nbsp;</td><!-- ID cliente -->
						<td>&nbsp;</td><!-- Nombre -->
						<td>&nbsp;</td><!-- RFC -->
						<td>&nbsp;</td><!-- Pais -->
						<td>&nbsp;</td><!-- Estado -->
						<td>&nbsp;</td><!-- Ciudad -->
						<td>&nbsp;</td><!-- Calle -->
						<td>&nbsp;</td><!-- NÃƒÆ’Ã‚Âºmero -->
						<td>&nbsp;</td><!-- Colonia -->
						<td>&nbsp;</td><!-- CÃƒÆ’Ã‚Â³digo postal -->
						<td>&nbsp;</td><!-- Email -->
						<td>&nbsp;</td><!-- TelÃƒÆ’Ã‚Â©fono -->
						<td>&nbsp;</td><!-- Referencia domicilio -->
						<td>&nbsp;</td><!-- Cantidad -->
						<td>&nbsp;</td><!-- Codigo bulto -->
						<td>&nbsp;</td><!-- Contenido -->
						<td>&nbsp;</td><!-- Tipo tarifa -->
						<td>&nbsp;</td><!-- Peso -->
						<td>&nbsp;</td><!-- Volumen -->
						<td>&nbsp;</td><!-- Largo -->
						<td>&nbsp;</td><!-- Ancho -->
						<td>&nbsp;</td><!-- Alto -->
						<td>&nbsp;</td><!-- Tipo Entrega -->
						<td>&nbsp;</td><!-- Forma PAgo -->
						<td>&nbsp;</td><!-- Acuse -->
						<td>&nbsp;</td><!-- COD -->						
						<td>&nbsp;</td><!-- Valor declarado -->
						<td>&nbsp;</td><!-- Cobertura -->
						<td>&nbsp;</td><!-- Comentarios -->
						<td>&nbsp;</td><!-- Referencia -->
						<td>&nbsp;</td><!-- Agente aduanal -->
						<td>&nbsp;</td><!-- pedimento -->
						<td>&nbsp;</td><!-- centro de costo -->
						<td class="fila-respuesta">&nbsp;</td><!-- Clave mensaje -->
						<td class="fila-respuesta">&nbsp;</td><!-- Tipo mensaje -->
						<td class="fila-respuesta">&nbsp;</td><!-- Descripcion mensaje -->
						<td class="fila-respuesta">&nbsp;</td><!-- Rastreo -->
						<td class="fila-respuesta">&nbsp;</td><!-- Guia -->
						<td class="fila-respuesta">&nbsp;</td><!-- Importe -->
						<td class="fila-respuesta">&nbsp;</td><!-- Descargas -->
						<td>&nbsp;</td><!-- Cantidad rastreos retorno -->					
						<td>&nbsp;</td><!-- Tipo Servicio --><!--AAP25-->
						<td>&nbsp;</td><!-- id producto sat -->
					</tr>
				<%				
					}
				} else {
					if (!conjuntoGuias.isEmpty()) {
					String guia = "";
					String msgeType = "";
					String classStyleMsge = "proceso-exitoso";
					String classStyleName = "tabla-nombre-cliente";
					String title = "";
					for (int i = 0; i < conjuntoGuias.size(); i++) {
						guia = ((ReadxlDTO)conjuntoGuias.get(i)).getGuia();
						msgeType = ((ReadxlDTO)conjuntoGuias.get(i)).getTipoMensaje();
						title = ((ReadxlDTO)conjuntoGuias.get(i)).getDesMensaje();
						if (msgeType.equals("ERRO")) {
							classStyleMsge = "proceso-no-exitoso";
							classStyleName = "tabla-nombre-cliente-error";
						} else {
							classStyleMsge = "proceso-exitoso";
							classStyleName = "tabla-nombre-cliente";
						}
				%>
					<tr id="tablerow<%=i%>" onmouseover="this.style.backgroundColor='#FFF0CC';" 
						onmouseout="this.style.backgroundColor='white';this.style.cursor='default'">
						
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getIdClienteDest()%><input type=hidden name="idCliente<%=i%>" id="idCliente<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getIdClienteDest()%>"/></td><!-- ID cliente -->
						<td class="<%=classStyleName %>" title="<%=title %>"><%=((ReadxlDTO)conjuntoGuias.get(i)).getNombre()%><input type=hidden name="nombreClnt<%=i%>" id="nombreClnt<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getNombre()%>"/></td><!-- Nombre -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getRfc()%><input type=hidden name="RFC<%=i%>" id="RFC<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getRfc()%>"/></td><!-- RFC -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getPais()%><input type=hidden name="pais<%=i%>" id="pais<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getPais()%>"/></td><!-- Pais -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getEstado()%><input type=hidden name="estado<%=i%>" id="estado<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getEstado()%>"/></td><!-- Estado -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getCiudad()%><input type=hidden name="ciudad<%=i%>" id="ciudad<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getCiudad()%>"/></td><!-- Ciudad -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getCalle()%><input type=hidden name="calle<%=i%>" id="calle<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getCalle()%>"/></td><!-- Calle -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getNumero()%><input type=hidden name="numero<%=i%>" id="numero<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getNumero()%>"/></td><!-- NÃƒÆ’Ã‚Âºmero -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getColonia()%><input type=hidden name="colonia<%=i%>" id="colonia<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getColonia()%>"/></td><!-- Colonia -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getCp()%><input type=hidden name="CP<%=i%>" id="CP<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getCp()%>"/></td><!-- CÃƒÆ’Ã‚Â³digo postal -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).geteMail()%><input type=hidden name="eMail<%=i%>" id="eMail<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).geteMail()%>"/></td><!-- Email -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getTelefono()%><input type=hidden name="telefono<%=i%>" id="telefono<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getTelefono()%>"/></td><!-- TelÃƒÆ’Ã‚Â©fono -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getReferenciaDomicilio()%><input type=hidden name="refDom<%=i%>" id="refDom<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getReferenciaDomicilio()%>"/></td><!-- Referencia domicilio -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getCantidad()%><input type=hidden name="cantidad<%=i%>" id="cantidad<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getCantidad()%>"/></td><!-- Cantidad -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getCodigoBulto()%><input type=hidden name="codBulto<%=i%>" id="codBulto<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getCodigoBulto()%>"/></td><!-- Codigo bulto -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getContenido()%><input type=hidden name="contenido<%=i%>" id="contenido<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getContenido()%>"/></td><!-- Contenido -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getTipoTarifa()%><input type=hidden name="tipoTarifa<%=i%>" id="tipoTarifa<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getTipoTarifa()%>"/></td><!-- Tipo tarifa -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getPeso()%><input type=hidden name="peso<%=i%>" id="peso<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getPeso()%>"/></td><!-- Peso -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getVolumen()%><input type=hidden name="volumen<%=i%>" id="volumen<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getVolumen()%>"/></td><!-- Volumen -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getLargo()%><input type=hidden name="largo<%=i%>" id="largo<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getLargo()%>"/></td><!-- Largo -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getAncho()%><input type=hidden name="ancho<%=i%>" id="ancho<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getAncho()%>"/></td><!-- Ancho -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getAlto()%><input type=hidden name="alto<%=i%>" id="alto<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getAlto()%>"/></td><!-- Alto -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getTipoEntrega()%><input type=hidden name="tipoEntrega<%=i%>" id="tipoEntrega<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getTipoEntrega()%>"/></td><!-- Tipo Entrega -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getFormaPago()%><input type=hidden name="formaPago<%=i%>" id="formaPago<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getFormaPago()%>"/></td><!-- Forma Pago -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getAcuse()%><input type=hidden name="acuse<%=i%>" id="acuse<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getAcuse()%>"/></td><!-- Acuse -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getCod()%><input type=hidden name="COD<%=i%>" id="COD<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getCod()%>"/></td><!-- COD -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getValorDeclarado()%><input type=hidden name="valorDeclarado<%=i%>" id="valorDeclarado<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getValorDeclarado()%>"/></td><!-- Valor declarado -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getCobertura()%><input type=hidden name="cobertura<%=i%>" id="cobertura<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getCobertura()%>"/></td><!-- Cobertura -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getComentarios()%><input type=hidden name="comentarios<%=i%>" id="comentarios<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getComentarios()%>"/></td><!-- Comentarios -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getReferencia()%><input type=hidden name="referencia<%=i%>" id="referencia<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getReferencia()%>"/></td><!-- Referencia -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getAgenteAduanal()%><input type=hidden name="agenteAduanal<%=i%>" id="agenteAduanal<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getAgenteAduanal()%>"/></td><!-- Agente aduanal -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getPedimento()%><input type=hidden name="pedimento<%=i%>" id="pedimento<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getPedimento()%>"/></td><!-- pedimento -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getCentroCosto()%><input type=hidden name="centroCosto<%=i%>" id="centroCosto<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getCentroCosto()%>"/></td><!-- centro de costo -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getCveMensaje()%><input type=hidden name="cveMsje<%=i%>" id="cveMsje<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getCveMensaje()%>"/></td><!-- Clave mensaje -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getTipoMensaje()%><input type=hidden name="tipMsje<%=i%>" id="tipMsje<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getTipoMensaje()%>"/></td><!-- Tipo mensaje -->
						<td class="<%=classStyleMsge%>"><%=((ReadxlDTO)conjuntoGuias.get(i)).getDesMensaje()%><input type=hidden name="desMsje<%=i%>" id="desMsje<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getDesMensaje()%>"/></td><!-- Descripcion mensaje -->
						<td><a onclick="downloadPrintGuiaPDF('<%=((ReadxlDTO)conjuntoGuias.get(i)).getLinkPDF()%>','<%=((ReadxlDTO)conjuntoGuias.get(i)).getLinkPDFXT()%>');" style="color: blue; text-decoration: underline; cursor: pointer;"  title="Descargar carta porte en PDF" ><%=((ReadxlDTO)conjuntoGuias.get(i)).getRastreo()%></a></td><!-- rastreo -->									
						<td><a onclick="downloadPrintGuiaPDF('<%=((ReadxlDTO)conjuntoGuias.get(i)).getLinkPDF()%>','<%=((ReadxlDTO)conjuntoGuias.get(i)).getLinkPDFXT()%>');" style="color: blue; text-decoration: underline; cursor: pointer;"  title="Descargar carta porte en PDF" ><%=((ReadxlDTO)conjuntoGuias.get(i)).getGuia()%></a></td><!-- No de Guia -->															
						<td align="left"><%=((ReadxlDTO)conjuntoGuias.get(i)).getImporte()%><input type=hidden name="invc<%=i%>" id="invc<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getImporte()%>"/></td><!-- import -->
						<td align="center" style="vertical-align:middle"><input type=hidden name="linkPDF<%=i%>" id="linkPDF<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getLinkPDF()%>"/>
						<%
							if (guia != null && guia.trim().length()>0) {
						%>
							<a onclick="downloadPrintGuiaPDF('<%=((ReadxlDTO)conjuntoGuias.get(i)).getLinkPDF()%>','<%=((ReadxlDTO)conjuntoGuias.get(i)).getLinkPDFXT()%>');" style="cursor: pointer;" title="Descargar carta porte en PDF" target="_blank"><img height="35" width="25" src="images/download_PDF_small.png"></a>
						<%
							}
						%>
						</td><!-- pdf -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getCantidadRastreos()%><input type=hidden name="cantidadRastreos<%=i%>" id="cantidadRastreos<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getCantidadRastreos()%>"/></td><!-- Cantidad rastreos retorno -->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getSegId()%><input type=hidden name="tipoServicio<%=i%>" id="tipoServicio<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getSegId()%>"/></td><!-- Tipo Servicio --><!--AAP25-->
						<td><%=((ReadxlDTO)conjuntoGuias.get(i)).getProductId()%><input type=hidden name="idProducto<%=i%>" id="tipoServicio<%=i%>" value="<%=((ReadxlDTO)conjuntoGuias.get(i)).getProductId()%>"/></td><!-- ID Producto -->
					</tr>										
				<% 			
						}
						if (conjuntoGuias.size()<10) {
							for (int i=conjuntoGuias.size();i <= 20; i++) {
				%>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td><!--AAP25-->
						<td>&nbsp;</td><!-- id producto -->
					</tr>	
				<%				
							}
						}												
					}
				}										
				%>				
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div align="center">				
				<input type="button" name="imprimir" value="Imprimir guia" onclick="return downloadPrintGuia()" class="btn-oldstyle">
				<logic:present name="FileUpLoad2" scope="request">
					<logic:equal name="FileUpLoad2" property="flagGuiaReturn" value="Y">
						<input type="button" name="imprimirRetorno" value="Imprimir guias retorno" onclick="return downloadPrintGuiaRetorno()" class="btn-oldstyle">
					</logic:equal>
				</logic:present>
				<logic:present name="FileUpLoad2" scope="request">
					<logic:equal name="FileUpLoad2" property="flagGuiaEtiPdf" value="Y">
						<input type="button" name="descargarEtiPDF" value="Descargar etiquetas PDFs" onclick="return downloadPdfGuia()" class="btn-oldstyle">
					</logic:equal>
				</logic:present>
				<input type="button" name="descargarxls" value="Descargar Hoja de Excel" onclick="return downloadXlsGuia()" class="btn-oldstyle">
				<html:submit onclick="return reinicioPantalla()" property="reiniciarPantalla" styleClass="btn-oldstyle">Reiniciar pantalla</html:submit>
			</div>
		</td> 
	</tr>
	
</table>
<html:hidden property="fileName"/>
<html:hidden property="pathFile"/>
<html:hidden property="currentTask"/>
<html:hidden property="flagGuiaReturn"/>
<html:hidden property="flagGuiaEtiPdf"/>
<html:hidden property="linkGuiaEtiPdf"/>
<input type="hidden" id="cadenaWW" name="cadenaWW" value='<logic:present name="cadenaWW" scope="request"><bean:write name="cadenaWW"/></logic:present>'>
<input type="hidden" id="cadenaRet" name="cadenaRet" value='<logic:present name="cadenaRet" scope="request"><bean:write name="cadenaRet"/></logic:present>'>
<input type="hidden" id="allowPrintQZ" name="allowPrintQZ" value='<%=global.getAllowPrintQZ()%>'>
<html:hidden property="countGuiaError"/>
<html:hidden property="countGuiaInfo"/>

<div id="zonePrintQZ"></div>
</html:form>

</div>

</main>

</body>
</html>
