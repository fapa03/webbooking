<%--
Author		:	Ramachandran.V
Date		:	25-March-2003

FileName	:	JavWebBookingServices.jsp
FormBean	:	JavWebBookingServicessForm.class
ActionBean	:	JavWebBookingSetvicesAction.class
OtherClasses	:	none
CSS FileName	:	webbooking.css
--%>
<%@ page info="WebBooking - Services"%>
<%@ page import="java.util.*,bean.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
	//AAPP//System.out.println("inicio de servicios calculados ");
	Global global = (Global) session.getAttribute("sGlobal");
	boolean dispAmntFlag = false;
	
	if(global != null && (global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
		dispAmntFlag = true;
			String valordeclarado = (String) request
					.getAttribute("valordeclarado");
			JavWebBookingServicesForm serForm = (JavWebBookingServicesForm) session
					.getAttribute("webBookingservices");
			String aditionalFlag = global.additionalServiceFlag;
			String flete = "&nbsp;";
			String servicios = "&nbsp;";
			String descuento = "&nbsp;";
			String subTotal = "&nbsp;";
			String iva = "&nbsp;";
			String ivaRet = "&nbsp;";
			String total = "&nbsp;";

			String action = request.getParameter("action");
			String actionval = request.getParameter("actionval");
			String erroraction = (String) request.getAttribute("erroraction");
			String errormsgentrega = (String) request
					.getAttribute("errormsgentrega");
			String errormsgcod = (String) request.getAttribute("errormsgcod");
			String errormsgstatus = (String) request
					.getAttribute("errormsgstatus");
			String erroravailcred = (String) request
					.getAttribute("erroravailcred");
			String errmsgenvelope = serForm.getErrmsgenvelope();
			String calculationShown = request.getParameter("shown");
			
			//AAPP//System.out.println("CALCULATION SHOWN " + calculationShown);
			//AAPP//System.out.println("SERVICES JSP VALOR DECLARADO " + valordeclarado);

			if (actionval != null && actionval.equals("calculate"))
				action = actionval;

			if ((erroraction != null && erroraction.equals("true"))
					|| (errormsgcod != null && errormsgcod.length() > 0)
					|| (errormsgstatus != null && errormsgstatus.length() > 0)
					|| (errormsgentrega != null && errormsgentrega.length() > 0)
					|| (erroravailcred != null && erroravailcred.length() > 0)
					|| (errmsgenvelope != null && errmsgenvelope.length() > 0)
					|| (calculationShown != null && calculationShown
							.equals("false"))
					|| (valordeclarado != null && valordeclarado.equals("nan")))
				action = null;

			ServicesTotal servicesTotal = (ServicesTotal) session
					.getAttribute("servicestotal");
			boolean shown = false;
			if (action != null && servicesTotal != null) {
				shown = true;
				flete = (servicesTotal.flete != null ? "$"
						+ servicesTotal.flete : "");
				servicios = (servicesTotal.servicios != null ? "$"
						+ servicesTotal.servicios : "");
				descuento = (servicesTotal.descuento != null ? "$"
						+ servicesTotal.descuento : "");
				subTotal = (servicesTotal.subTotal != null ? "$"
						+ servicesTotal.subTotal : "");
				iva = (servicesTotal.iva != null ? "$" + servicesTotal.iva : "");
				ivaRet = (servicesTotal.ivaRet != null ? "$"
						+ servicesTotal.ivaRet : "");
				total = (servicesTotal.total != null ? "$"
						+ servicesTotal.total : "");
			}			
%>

<html:html>

<head>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<title>Services</title>
<link rel="stylesheet" href="webbooking.css" type="text/css">
</head>

<body onload=initScript() marginwidth="0" marginheight="0" topmargin=0
	leftmargin=0 background="images/bg.gif">

<script language="javascript" src="common.js">
</script>

<script language=javascript>

function changeBranch()
{

  document.forms[0].action="webBookingservices.do?operation="+"checkbranch";
   document.forms[0].submit();
}

<%
	String defaultscreen = (String)session.getAttribute("defaultservicescreen");
	String hitCount = (String)session.getAttribute("sHitCount");	
	if(hitCount!=null){
%>
var hitCount = <%= hitCount %>
<%
	}else{
%>
var hitCount = 0;
<%
	}
%>

<%			
	if(defaultscreen !=	null && defaultscreen.equals("yes")){
%>
var screen = 'default';
<%
	}else{
%>
var screen = 'tariffscreen';
<%
	}	
%>


<%			
	if(shown){
%>
var shown = 'true';
<%
	}else{
%>
var shown = 'false';
<%
	}	
%>
function openLov(str){
		if(str=='service'){
			window.open('lov.do?type='+str,'abc','width=600,height=200,,top=200,left=100,screenY=200,screenX=100');
		}
	}
function submitForm1(obj){
	
	deletehitcount = document.forms[0].deletehitcount.value;
	objName = obj.name;
	

	if(objName=='Agregar'){		
		
		
		if( document.forms[0].serviceAdditional.value != null && 
			trim(document.forms[0].serviceAdditional.value).length >0)
		{
			document.forms[0].action = 'webBookingservices.do?action=add';
			document.forms[0].submit();
 	    }
		else
		{
			alert("Seleccione el servicio adicional");
		}
	}
}
// Deleting 
	function doDelete(index){
			document.forms[0].action = "webBookingservices.do?action=delete&index="+index;
		    document.forms[0].submit();

	}
function goOut(page){	
	/*if(isGuiaGenerated){
		if(confirm("IF YOU HAVE DOWNLOADED BOTH FILES MEANS CLICK 'OK'  ELSE CLICK 'CANCEL'")){
			document.forms[0].action="webBookinggeneral.do?includeattribute=yes";
			document.forms[0].submit();
			return;
		}else{
			alert("PLEASE DOWNLOAD BOTH FILES");
			return;
		}
	}*/
	if(confirm("DESEA PERDER LA INFORMACION?")){
		document.forms[0].action="webBookinggeneral.do?includeattribute=yes&page="+page;
		document.forms[0].submit();
	}else{
		return;
	}
}

function logOut(){
	/*if(isGuiaGenerated){
		if(confirm("IF YOU HAVE DOWNLOADED BOTH FILES MEANS CLICK 'OK'  ELSE CLICK 'CANCEL'")){
			document.forms[0].action="login.do?logout=yes";
			document.forms[0].submit();
			return;
		}else{
			alert("PLEASE DOWNLOAD BOTH FILES");
			return;
		}
	}*/	
	document.forms[0].action="login.do?logout=yes";
	document.forms[0].submit();
}

function initScript(){	
	
<%
	if(valordeclarado!=null && valordeclarado.equals("nan")){
%>
		alert("POR FAVOR, CAPTURE SOLO NUMEROS");
		document.forms[0].valordeclarado.select();
		return;
<%
	}
%>
	valordeclarado=document.forms[0].valordeclarado.value;
	duplicateguianumber=document.forms[0].duplicateguianumber.value;
	successmessage=document.forms[0].successmessage.value;
	confirmgenerate=document.forms[0].confirmgenerate.value;	
	errmsgenvelope=document.forms[0].errmsgenvelope.value;
	if(errmsgenvelope!=''){
		if(confirm(errmsgenvelope)){
			document.forms[0].valordeclarado.value="";
			document.forms[0].valordeclarado.readOnly=true;
			document.forms[0].seguro.value="";
			document.forms[0].errmsgenvelope.value="";
			return;
		}else{
			document.forms[0].errmsgenvelope.value="";
			return;
		}
	}
	if(duplicateguianumber!=''){
		alert(duplicateguianumber);
		document.forms[0].duplicateguianumber.value="";
	//	document.forms[0].guiano.select();
	}
	if(confirmgenerate=='yes'){
		//Added R.Mohan Babu in order to disable all the text boxes after the generar is clicked.	
		document.forms[0].guiano.readOnly = true;	
		if(confirm("DESEA CONFIRMAR LA GENERACIÓN DE LA GUÍA?")){
			if(trim(document.forms[0].guiano.value)==''){
				alert("CAPTURE EL NUMERO DE GUIA");
				document.forms[0].guiano.focus();
				return;
			}
			document.forms[0].action = 'webBookingservices.do?action=generate&confirm=yes';
			document.forms[0].confirmgenerate.value="";
			document.forms[0].submit();
		}else{
			document.forms[0].confirmgenerate.value="";
			return;
		}	
	
	}
	if(successmessage!=''){
		alert(successmessage);		
		/*alert("YOU CAN DOWNLOAD THE GUIAPRINT AND BARCODE FILES FOR PRINTING PURPOSE NOW!");
		document.forms[0].successmessage.value="";
		isGuiaGenerated=true;
		document.forms[0].Calculate.disabled=true;
		document.forms[0].generale.disabled=true;
		document.forms[0].servicios.disabled=true;
		
		document.forms[0].entrega.disabled=true;
		document.forms[0].acusederecibo.disabled=true;
		document.forms[0].valorcod.disabled=true;
		document.forms[0].valordeclarado.disabled=true;
		document.forms[0].cobertura.disabled=true;
		document.forms[0].seguro.disabled=true;
		document.forms[0].guiano.disabled=true;
		
		document.forms[0].download1.disabled=false;
		document.forms[0].download2.disabled=false;
		return;*/
	}
	<%
	String valorvalue = (String)session.getAttribute("valorvalue");
	//AAPP//System.out.println("VALOR VALUE IN JSP "+valorvalue);
	if(valorvalue!=null && valorvalue.equals("null")){
	%>
	document.forms[0].valordeclarado.readOnly=true;	
	<%
	}
	%>
	//Added by R.Mohan Babu on 10/06/2004 in order to make Guia No as read only
	document.forms[0].guiano.readOnly = true;
	
}

var isGuiaGenerated=false;
function callGeneral(){	
	/*if(isGuiaGenerated){
		if(confirm("IF YOU HAVE DOWNLOADED BOTH FILES MEANS CLICK 'OK'  ELSE CLICK 'CANCEL'")){
			document.forms[0].action="webBookinggeneral.do?includeattribute=yes";
			document.forms[0].submit();
			return;
		}else{
			alert("PLEASE DOWNLOAD BOTH FILES");
			return;
		}
		
	}*/
	document.forms[0].action='webBookingservices.do?to=bookinggeneral';
	document.forms[0].submit();
}
function callServicesDetail(){	
	/*if(isGuiaGenerated){
		if(confirm("IF YOU HAVE DOWNLOADED BOTH FILES MEANS CLICK 'OK'  ELSE CLICK 'CANCEL'")){
			document.forms[0].action="webBookinggeneral.do?includeattribute=yes";
			document.forms[0].submit();
			return;
		}else{
			alert("PLEASE DOWNLOAD BOTH FILES");
			return;
		}
	}*/
	document.forms[0].action='webBookingservices.do?to=bookingdetail&screen='+screen;
	document.forms[0].submit();
}

/*function isNumberForDec(obj){
	fval=trim(obj.value);
	if(isNaN(fval)){
		alert("POR FAVOR, CAPTURE SOLO NUMEROS");
		document.forms[0].valordeclarado.focus();
		return;
	}else{
		number = toNumber(fval);
		obj.value=number;
	}
}*/

function submitForm(obj){
	/*fval=trim(obj.value);
	if(isNaN(fval)){
		alert("POR FAVOR, CAPTURE SOLO NUMEROS");
		document.forms[0].valordeclarado.select();
		return;
	}else{
		number = toNumber(fval);
		obj.value=number;
	}*/ //Commented on 23rd May for numeric value error
	
	/* str='insuranceamount';	// Commented and below set of line added by R.Mohan Babu on 10/06/2004
	document.forms[0].action='webBookingservices.do?onchange='+str;
	document.forms[0].submit();*/

	/* The parameter named action is Added by R.Mohan Babu*(10/06/2004) in order to do the calculation and display on the side screen */
	/* The ReadOnly of the Valor Declarado is checked in order to redirect the page with onchange & action parameters */
	str='insuranceamount';
	if(document.forms[0].valordeclarado.value != "" ||(document.forms[0].valordeclarado.value == "" && document.forms[0].valordeclarado.readOnly == false)){
		document.forms[0].action='webBookingservices.do?onchange='+str+'&action=calculate';
		//alert('webBookingservices.do?onchange='+str+'&action=calculate');
	}else{
		document.forms[0].action='webBookingservices.do?action=calculate';
		//alert('webBookingservices.do?action=calculate');
	} 
		
	document.forms[0].submit();
}
function setSeguro(obj){
	document.forms[0].action='webBookingservices.do?onchange=cobertura';
	document.forms[0].submit();
}

function generateGuia(str){
	if(hitCount==0){
		alert("FAVOR DE DOCUMENTAR AL MENOS UN SERVICIO");
		document.forms[0].action='webBookingservices.do?norecords=yes&screen='+screen;
		document.forms[0].submit();
		return;
	}
	/*if(str=='generate'){
		if(document.forms[0].guiano.value==''){
			alert("CAPTURE EL NUMERO DE GUIA");
			document.forms[0].guiano.focus();
			return;
		}
	}*/
	/*
	document.forms[0].action='webBookingservices.do?action='+str; // Commented and below set of line added by R.Mohan Babu on 10/06/2004
	document.forms[0].submit();*/
	var comboEntrega = document.forms[0].entrega;//AAP01
	var tipoEntrega  = comboEntrega.options[comboEntrega.selectedIndex].value;//AAP01
	var zonaExtendida = document.forms[0].zonaExtendida.value.substring(0,1);//AAP01
	var permiteZonaExt = document.forms[0].zonaExtendida.value.substring(1,2);//AAP02
	var costoEntrega = document.forms[0].zonaExtendida.value.substring(2);//AAP01
	
	if(str == "generate" ) {
		var band=false;//AAP01
		//AAP01		
		
		/*condicion para tipo de entrega " Entrega a domicilio"*/
		if (tipoEntrega == '2'){
			if (permiteZonaExt == 'N'){//AAP02
				alert('Cliente no Autorizado para EAD a Zona Plus');
			} else {//AAP02
				/*condicion para validar zona extendida*/
				if (zonaExtendida == 'Y'){
					alert('El domicilio destino es zona plus. Favor de documentar en la opcion "Nueva Elaboración de Guias" que viene al final del menu principal.');
// 					if(confirm("El domicilio destino es zona extendida. Costo por unidad $"+costoEntrega+" + I.V.A. ¿Desea generar Guia?")){
// 						/*condicion para validar tarifa*/
// 						if (document.forms[0].tarifaInvalida.value == "Y"){
// 							alert('Tarifa Invalida para zona extendida');	
// 						}else{
// 							band = true;	
// 						}				
// 					}
				}else{
					band = true;
				}//AAP01	
			}//AAP02			
		} else {
			band = true;
		}	
		
		if (band){//AAP01
			document.forms[0].action='webBookingservices.do?action='+str;
			document.forms[0].submit();			
		}//AAP01
	}
	else {
		//AAP02
		if (tipoEntrega =='2'){
			if (permiteZonaExt == 'N'){//AAP02
				alert('Cliente no Autorizado para EAD a Zona Plus');
			} else {//AAP02
				//AAP01
				if (zonaExtendida == 'Y'){
					alert('El domicilio destino es zona plus. Favor de documentar en la opcion "Nueva Elaboración de Guias" que viene al final del menu principal.');
// 					if(confirm("El domicilio destino es zona extendida. Costo por unidad $"+costoEntrega+" + I.V.A. ¿Desea Calcular el costo?")){
// 						/*condicion para validar tarifa*/
// 						if (document.forms[0].tarifaInvalida.value == "Y"){
// 							alert('Tarifa Invalida para zona extendida');	
// 						}else{
// 							submitForm(document.forms[0].guiano);	
// 						}					
// 					}
				}else{
					submitForm(document.forms[0].guiano);
				}//AAP01
			}//AAP02			
		} else {//AAP02
			submitForm(document.forms[0].guiano);
		}
	}
}

function showAditionalServices(){
	if(hitCount==0){
		alert("FAVOR DE DOCUMENTAR AL MENOS UN SERVICIO");
		document.forms[0].action='webBookingservices.do?norecords=yes&screen='+screen;
		document.forms[0].submit();
		return;
	}else{
		// Commented and below set of line added by R.Mohan Babu on 10/06/2004
		/*document.forms[0].action="webBookingservices.do?show=aditional&shown="+shown;
		document.forms[0].submit();*/
		/* Added the parameter onchange=insuranceamount to the url by R.Mohan Babu(10/06/2004) to get the refreshed value*/

		//Below code commented By Sam[12-06-06] , when set null value to Valor Declarado and press additional services 
		//means it does't affect the  Insurance 
		/*
		if(document.forms[0].valordeclarado.value != "")
			document.forms[0].action="webBookingservices.do?show=aditional&shown="+shown+'&onchange=insuranceamount';
		else
			document.forms[0].action="webBookingservices.do?show=aditional&shown="+shown;
		*/
		//Below code added by Sam [12-06-06]
		//document.forms[0].action="webBookingservices.do?show=aditional&shown="+shown+'&onchange=insuranceamount';
		//Above line commented and below code added (previous code which was commented is placed below) by B.Emerson //on 09/08/2006 in order to fix the bug (when additional service button pressed, page didn't display) reported by Hugo //on 08/08/2006
		if(document.forms[0].valordeclarado.value != "")
			document.forms[0].action="webBookingservices.do?show=aditional&shown="+shown+'&onchange=insuranceamount';
		else
			document.forms[0].action="webBookingservices.do?show=aditional&shown="+shown;
	
		document.forms[0].submit();

	}
}

/*function downloadPrintGuia(){
	document.forms[0].action = "DownloadFileGuia.jsp"
	document.forms[0].submit();
}
function downloadPrintBarcode(){
	document.forms[0].action = "DownloadFileBarcode.jsp"
	document.forms[0].submit();
}*/
/*FUNCION PARA DAR FORMATO DE PESOS AL VALOR QUE SE CAPTURA*/
function formatoPesos(key,obj){
	var cod=key.keyCode;
	var band = true;
	var punto = false;
	var valor = obj.value;
	
	/*si se presionó un punto se activa la bandera punto*/
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
</script>

<html:form action="webBookingservices">
	<jsp:include page="nocache.jsp" flush="false" />
	<table border="0" cellspacing="0" cellpadding="0" width="769">
		<tr>
			<td width="429" height="77"><IMG border=0 height=79
				src="images/inner_top01.jpg" useMap=#peMap width=429></td>
			<td width="342" height="77">
			<div align="left"><IMG height=79 src="images/inner_top02.jpg"
				width=341></div>
			</td>
		</tr>
		<tr>
			<td colspan="2" height="3"></td>
		</tr>
	</table>
	<table width="68%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><!--  New Image  --> <logic:present name="loginForm">
				<html:hidden name="loginForm" property="userValidate" />

				<logic:equal name="loginForm" property="userValidate"
					value="validUser">

					<img src="images/toplink.gif" width="771" height="22"
						usemap="#mainMap" border="0">
					<map name="mainMap">
						<!-- added by bala -->
						<area shape="rect" coords="565,5,680,18"
							href="javascript:goOut('clientreport')">

						<area shape="rect" coords="460,5,555,17"
							href="javascript:goOut('shipmenthistory')">

						<area shape="rect" coords="1,4,135,18"
							href="javascript:goOut('cliententry')">

						<area shape="rect" coords="145,5,226,18"
							href="javascript:goOut('mainpage')">

						<area shape="rect" coords="236,5,342,18"
							href="javascript:goOut('thispage')">

						<area shape="rect" coords="352,5,450,18"
							href="javascript:goOut('guiacancel')">

						<area shape="rect" coords="690,4,772,18"
							href="login.do?logout=yes" alt="Terminar Sesión"
							title="Terminar Sesión">
						<!-- ended  by bala -->

					</map>
				</logic:equal>
			</logic:present> <!--  Old Image  --> <logic:present name="loginForm">
				<html:hidden name="loginForm" property="userValidate" />

				<logic:notEqual name="loginForm" property="userValidate"
					value="validUser">

					<img src="images/toplink1.gif" width="771" height="22"
						usemap="#mainMap" border="0">
					<map name="mainMap">
						<area shape="rect" coords="67,6,151,18"
							href="javascript:goOut('mainpage')">
						<area shape="rect" coords="165,6,275,17"
							href="javascript:goOut('thispage')">
						<area shape="rect" coords="288,6,390,17"
							href="javascript:goOut('guiacancel')">
						<area shape="rect" coords="404,6,506,17"
							href="javascript:goOut('shipmenthistory')">
						<area shape="rect" coords="520,6,644,17"
							href="javascript:goOut('clientreport')">
						<area shape="rect" coords="676,6,764,17"
							href="login.do?logout=yes" alt="Terminar Sesión"
							title="Terminar Sesión">



					</map>
				</logic:notEqual>
			</logic:present></td>
		</tr>
		<tr>
			<td>
			<div align="right"><img src="images/book_servicos.gif" width="521"
				height="25" usemap="#Map3" border="0"> <map name="Map3">
				<area shape="rect" coords="314,7,441,19"
					href="javascript:callServicesDetail()">
				<area shape="rect" coords="241,7,304,20"
					href="javascript:callGeneral()">
			</map></div>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
	</table>
	<map name="peMap">
		<area shape="RECT" coords="13,19,235,71"
			href="http://www.paquetexpress.com.mx" target="_blank"
			alt="www.paquetexpress.com" title="www.paquetexpress.com">
	</map>
	<table border="0" align=center cellspacing="0" cellpadding="0"
		width="95%">
		<logic:present name="errormsgentrega" scope="request">
			<tr>
				<td><font
					style="font-size: 10pt; color: #ff0000; font-weight: bold"> <bean:write
					name="errormsgentrega" /> </font></td>
			</tr>
		</logic:present>
		<logic:present name="errormsgcod" scope="request">
			<tr>
				<td><font
					style="font-size: 10pt; color: #ff0000; font-weight: bold"> <bean:write
					name="errormsgcod" /> </font></td>
			</tr>
		</logic:present>
		<logic:present name="erroravailcred" scope="request">
			<tr>
				<td><font
					style="font-size: 10pt; color: #ff0000; font-weight: bold"> <bean:write
					name="erroravailcred" /> </font></td>
			</tr>
		</logic:present>
		<!-- Error message for status added by palanivel -->
		<logic:present name="errormsgstatus" scope="request">
			<tr>
				<td><font
					style="font-size: 10pt; color: #ff0000; font-weight: bold"> <bean:write
					name="errormsgstatus" /> </font></td>
			</tr>
		</logic:present>
		<logic:present name="errMsgAdditional" scope="request">
			<tr>
				<td><font
					style="font-size: 10pt; color: #ff0000; font-weight: bold"> <bean:write
					name="errMsgAdditional" /> </font></td>
			</tr>
		</logic:present>
	</table>
	<strong><font face="Times">Servicios</font></strong>
	<br>
	<table width="780" border="0" cellspacing="0" cellpadding="0"
		bgcolor="#cedee9">
		<tr>
			<td height="200">
			<table bgcolor="#cedee9" align=center border="0" cellpadding="0"
				cellspacing="0" width="100%">
				<tr>
					<td width="71%">
					<table border="0" cellpadding="0" cellspacing="1" width="100%">
						<tr>
							<td colspan=6>&nbsp;</td>
						</tr>
						<%
						//if ((global.displayAmountFlag != null)&& (global.displayAmountFlag.equalsIgnoreCase("Y"))|| (flete.equalsIgnoreCase("&nbsp;"))) {
							if(dispAmntFlag || (flete.equalsIgnoreCase("&nbsp;")))	
							{
						%>
						<tr>
							<td width="19%">
							<p align="right">Entrega</p>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%"><html:select size="1" property="entrega"
								onchange="return changeBranch();">
								<html:option value="1">OCURRE</html:option>
								<html:option value="2">EAD</html:option>
							</html:select></td>
							<td width="11%">
							<div align="right">Flete</div>
							</td>
							<td width="0%">&nbsp;</td>
							<td width="20%" align=right class="td" height="21"><%=flete%></td>
						<tr>
							<td width="19%">
							<div align="right">Acuse de Recibo</div>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%"><html:select size="1" property="acusederecibo">
								<html:options property="ackvalue" labelProperty="acklabel" />
							</html:select></td>
							<td width="11%">
							<div align="right">Descuento</div>
							</td>
							<td width="0%">&nbsp;</td>

							<td width="20%" align=right class="td" height="21"><%=descuento%></td>



						</tr>
						<tr>
							<td width="19%">
							<p align="right">Valor COD</p>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%">
							<p align="left"><html:text property="valorcod" size="16"
								styleClass="text" onblur="isNumber(this)" /></p>
							</td>

							<td width="11%">
							<div align="right">Servicios</div>
							</td>
							<td width="0%">&nbsp;</td>
							<td width="20%" align=right class="td" height="21"><%=servicios%></td>
							
						</tr>
						<tr>
							<td width="19%">
							<div align="right">Valor Declarado</div>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%"><html:text property="valordeclarado" onkeypress="return formatoPesos(event,this);"
								styleClass="text" onchange="submitForm(this)" /></td>
							<td width="11%">
							<div align="right">Sub-Total</div>
							</td>
							<td width="0%">&nbsp;</td>
							<td width="20%" align=right class="td" height="21"><%=subTotal%></td>
						</tr>
						<tr>
							<td width="19%">
							<p align="right">Cobertura</p>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%">
							<div align="left"><html:select size="1" property="cobertura"
								onchange="setSeguro()">
								<html:options property="insurancetype"
									labelProperty="insurancetypelabel" />
							</html:select></div>
							</td>

							<td width="11%">
							<div align="right">I.V.A</div>
							</td>
							<td width="0%"></td>
							<td width="20%" align=right class="td" height="21"><%=iva%></td>
						</tr>
						<tr>
							<td width="19%">
							<div align="right">Informacion del Seguro</div>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%"><html:textarea property="seguro"
								style="HEIGHT: 20px; WIDTH: 261px" readonly="true" /></td>
							<td width="11%">
							<div align="right">I.V.A. Ret.</div>
							</td>
							<td width="0%">&nbsp;</td>
							<td width="20%" align=right class="td" height="21"><%=ivaRet%></td>
						</tr>
						<tr>
							<td width="19%">
							<div align="right"></div>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%">&nbsp;</td>
							<td width="11%">
							<div align="right">Total</div>
							</td>
							<td width="0%">&nbsp;</td>
							<td width="20%" align=right class="td" height="21"><%=total%></td>
						</tr>
						<%
						} 
						else 
						{
						%>
						<tr>
							<td width="19%">
							<p align="right">Entrega</p>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%"><html:select size="1" property="entrega"
								onchange="return changeBranch();">
								<html:option value="1">OCURRE</html:option>
								<html:option value="2">EAD</html:option>
							</html:select></td>
							<td width="11%">
							<div align="right">Flete</div>
							</td>
							<td width="0%">&nbsp;</td>
							<td width="20%" align=right class="td" height="21"><font size="4"><b>**********</b></font></td>
						<tr>
							<td width="19%">
							<div align="right">Acuse de Recibo</div>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%"><html:select size="1" property="acusederecibo">
								<html:options property="ackvalue" labelProperty="acklabel" />
							</html:select></td>
							<td width="11%">
							<div align="right">Descuento</div>
							</td>
							<td width="0%">&nbsp;</td>

							<td width="20%" align=right class="td" height="21"><font size="4"><b>**********</b></font></td>



						</tr>
						<tr>
							<td width="19%">
							<p align="right">Valor COD</p>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%">
							<p align="left"><html:text property="valorcod" size="16"
								styleClass="text" onblur="isNumber(this)" /></p>
							</td>

							<td width="11%">
							<div align="right">Servicios</div>
							</td>
							<td width="0%">&nbsp;</td>
							<td width="20%" align=right class="td" height="21"><font size="4"><b>**********</b></font></td>
							
							
						</tr>
						<tr>
							<td width="19%">
							<div align="right">Valor Declarado</div>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%"><html:text property="valordeclarado"
								styleClass="text" onchange="submitForm(this)" /></td>
							<td width="11%">
							<div align="right">Sub-Total</div>
							</td>
							<td width="0%">&nbsp;</td>
							<td width="20%" align=right class="td" height="21"><font size="4"><b>**********</b></font></td>
						</tr>
						<tr>
							<td width="19%">
							<p align="right">Cobertura</p>
							</td>
							<td width="1%">&nbsp;</td>

							<td width="50%">
							<div align="left"><html:select size="1" property="cobertura"
								onchange="setSeguro()">
								<html:options property="insurancetype"
									labelProperty="insurancetypelabel" />
							</html:select></div>
							</td>

							<td width="11%">
							<div align="right">I.V.A</div>
							</td>
							<td width="0%"></td>
							<td width="20%" align=right class="td" height="21"><font size="4"><b>**********</b></font></td>
						</tr>
						<tr>
							<td width="19%">
							<div align="right">Informacion del Seguro</div>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%"><html:textarea property="seguro"
								style="HEIGHT: 20px; WIDTH: 261px" readonly="true" /></td>
							<td width="11%">
							<div align="right">I.V.A. Ret.</div>
							</td>
							<td width="0%">&nbsp;</td>
							<td width="20%" align=right class="td" height="21"><font size="4"><b>**********</b></font></td>
						</tr>
						<tr>
							<td width="19%">
							<div align="right"></div>
							</td>
							<td width="1%">&nbsp;</td>
							<td width="50%">&nbsp;</td>
							<td width="11%">
							<div align="right">Total</div>
							</td>
							<td width="0%">&nbsp;</td>
							<td width="20%" align=right class="td" height="21"><font size="4"><b>**********</b></font></td>
						</tr>
						<%
						}
						%>
						<!--Code added by  palanivel  For Displaying Additional Service Lov  -->
						<%
					if (serForm.getShowAdditional() != null
							&& serForm.getShowAdditional()
									.equalsIgnoreCase("Y")) {

						%>
						<tr>
							<td width="11%" align="right" valign="top">Additional Services</td>
							<td width="0%">&nbsp;</td>
							<td><html:text property="serviceAdditional" styleClass="text"
								readonly="true" onclick="openLov('service')" /> <html:button
								property="servicebut" style="WIDTH:35px" value="..."
								styleClass="button1" onclick="openLov('service')" />
							&nbsp;Importe <html:text property="importeValue"
								styleClass="text" readonly="true" /> <html:button
								property="Agregar" styleClass="bookbutton" value="Agregar"
								onclick="submitForm1(this)" /></td>

						</tr>


						<tr>
							<td width="1%">&nbsp;</td>
							<td colspan=2 align="left">
							<table>
								<TR>
									<TD width="52%">Additional Service</TD>
									<TD width="48%" align="left">Importe</TD>
									<TD width="20%">&nbsp</TD>
								</TR>
							</table>
							</td>
						</tr>
						<tr>
							<td width="1%">&nbsp;</td>
							<td colspan=2 align="left">
							<div
								style='width: 300; height: 80; overflow: auto; border: 0 solid black'
								align="center">
							<table>

								<%java.util.ArrayList servicesDetailArray = (java.util.ArrayList) session
								.getAttribute("aditionalServicesDetail");
						int size = 0;
						if (servicesDetailArray != null) {
							size = servicesDetailArray.size();

							for (int i = 0; i < size; i++) {
								AdditionalService serviceRecordsRecs = (AdditionalService) servicesDetailArray
										.get(i);

								%>
								<tr>
									<TD width="60%" height=20 class="td"><%=serviceRecordsRecs.service%></TD>
									<TD width="60%" height=20 class="td"><%=serviceRecordsRecs.importe%></TD>
									<td width="10%"><a href="javascript:doDelete('<%= i %>')"><img
										src='images/delete.jpg' width="17" border="0" height="17"></a></td>
								</tr>
								<%}
							for (int i = size; i < 3; i++) {

							%>
								<TR>
									<TD width="60%" height=20 class="td"></TD>
									<TD width="60%" height=20 class="td"></TD>
									<td width="10%"><img src='images/delete.jpg' width="17"
										border="0" height="17"></td>
								</TR>
								<%}

						} else

						{

							%>


								<%for (int i = 1; i <= 3; i++) {

							%>

								<TR>
									<TD width="60%" height=20 class="td"></TD>
									<TD width="60%" height=20 class="td"></TD>
									<td width="10%"><img src='images/delete.jpg' width="17"
										border="0" height="17"></td>
								</TR>

								<%}
						}

					%>
							</table>
							</div>
							</td>
						</tr>
						<%}

					%>

						<!-- End -->
						<!--<tr>
							<td>
							<div align="right">No. Guía</div>
							</td>
							<td>&nbsp;</td>
							<td width="40%">
							<html:text property="guiano" styleClass="text" />
							</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						--><tr>
							<td>
							<!-- Removed GUIA NO field row and added it as hidden field on 25-11-2010-->
							<html:hidden property="guiano"  />
							<div align="right">Comentarios</div>
							</td>
							<td>&nbsp;</td>
							<td colspan=4><html:text styleClass="comment" maxlength="255"
								property="comments" /></td>
						</tr>
						<tr>
							<td>
							<div align="right">Referencia</div>
							</td>
							<td>&nbsp;</td>
							<td colspan=4><html:text styleClass="comment" maxlength="65"
								property="reference" /></td>
						</tr>
						<tr>
							<td colspan=6>
							<table border=0 align=center width="60%">
								<tr>
									<td width=15% align=right><html:button property="Calculate"
										value="Calcular" styleClass="button"
										onclick="generateGuia('calculate')" /></td>
									<td width="5%">
									<div align="right"><html:button property="generale"
										styleClass="bookbutton" value="Generar"
										onclick="generateGuia('generate')" /></div>
									</td>
									<td width="15%"><%if (aditionalFlag != null
							&& aditionalFlag.equalsIgnoreCase("Y")) {

					%> <html:button property="servicios" styleClass="bookbutton"
										value="Servicios Adicionales"
										onclick="showAditionalServices()" /> <%} else {

					%> <html:button property="servicios" styleClass="bookbutton"
										value="Servicios Adicionales" disabled="true" /> <%}

				%></td>
								</tr>
							</table>
							</td>
						</tr>

						
					</table>
					</td>
				</tr>
			</table>
			<input type="hidden" name="deletehitcount"> 
			<html:hidden property="serviceshitcount" /> 
			<html:hidden property="duplicateguianumber" /> 
			<html:hidden property="successmessage" /> 
			<html:hidden property="confirmgenerate" /> 
			<html:hidden property="errmsgenvelope" /> 
			<html:hidden property="showAdditional" />
			<html:hidden property="referenceId" />
			<html:hidden property="zonaExtendida"/> <!-- AAP01 -->
			<html:hidden property="tarifaInvalida"/> <!-- AAP02 -->
	</html:form>
</body>
</html:html>
