<%--
Authur		:	KUMARAN
Date		:	

FileName	:	
FormBean	:	
ActionBean	:	
OtherClasses	:	none
CSS FileName	:	webbooking.css
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 03/07/2013
 * Descripción:  Se agregaron validaciones y procesos para guias flete por cobrar. 
 * y objeto para capturar referencia relacionada a manifiesto
 * ------------------------------------------------------------------
--%>
<%@page contentType="text/html" %>
<%@page  import="java.util.ArrayList, bean.JavManifestGenerationForm, bean.JavManifestRecord, bean.Global"%>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="WEB-INF/struts-bean.tld" prefix="bean" %>
<%
	Global global =(Global)session.getAttribute("sGlobal");
	boolean dispAmntFlag = false;
	if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
	dispAmntFlag = true;

	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache");		//HTTP 1.0
	response.setDateHeader ("Expires", 0);			//prevents caching at the proxy server
	
	String systemDate  = (String)request.getAttribute("systemdate");
	long systemLongDate = Long.parseLong((String)request.getAttribute("systemlongdate"));
	//AAPP
%>

<html:html>
<head>
<script type="text/javascript" src="barraEspera.js"></script>
<link rel="stylesheet" href="webbooking.css">
<title>Manifest Generation</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />



<script type="text/JavaScript"> 
var systemDateLong=0;  
	function callPrint(){
		showBarraEspera('mensaje','body','Espere por favor...Cargando Pantalla para Impresion');
		document.forms[0].operation.value="Print";
		document.forms[0].submit();
	}
   function genNextManifest(){
	 ocultarBarraEspera('mensaje','body');
	 
	 /*verifica mensaje de error de guias ya incluidas en manifiestos anteriores*/
	 if (document.forms[0].msjErr.value != '') {
	 	alert(document.forms[0].msjErr.value);//muestra mensaje de error
	 	document.forms[0].msjErr.value = '';//limpia mensaje de error
	 	document.forms[0].operation.value = "Normal";
		document.forms[0].disableGenerate.value = "false";
		document.forms[0].clickedItems.value = "";
		document.forms[0].B6.disabled = true;
		document.forms[0].B7.disabled = true;		
	 	callShipmentHistoryLog();//regresa a pantalla inicial de historico de envios.
	 	return true;
	 }
   	 document.forms[0].collectionTime.value='<%=systemDate%>';   	 
   	 systemDateLong = new Date(document.forms[0].collectionTime.value).getTime();
     var dataFlag=document.forms[0].noDataFound.value;
	
	 if(dataFlag=="" || dataFlag=="N"){
		/*seccion para habilitar y deshabilitar los botones de paginacion*/
	    var maxPage = parseInt(document.forms[0].maxPageIndex.value);
		var currentPage = parseInt(document.forms[0].currentPage.value);
		if (currentPage == 1) {
			document.forms[0].B7.disabled = true;
		} else if (currentPage == maxPage) {
			document.forms[0].B6.disabled = true;
		}		 
		  var genFlag=document.forms[0].disableGenerate.value;		  
		  	if(genFlag=="true"){			  
				  var genNext=confirm("MANIFIESTO GENERADO CON ÉXITO. DESEA PREPARAR OTRO MANIFIESTO?");
				  if(genNext){	
				  	document.forms[0].operation.value="Normal";		    				    				  	
					document.forms[0].clickedItems.value="";
					document.forms[0].disableGenerate.value="false";					
				    document.forms[0].submit();
					}
				  else {
		        
						document.forms[0].operation.value="Normal";
						document.forms[0].disableGenerate.value="false";
						document.forms[0].clickedItems.value="";				
				        document.forms[0].B6.disabled=true;
						document.forms[0].B7.disabled=true;												
					 }			  
		  
			}
	  }
	  
	 /*SIEMPRE SE ESCONDERÁ EL CENTRO DE COSTO, ya no se utiliza para la version de manifiestos por direccion*/
	 document.getElementById('ccosto1').style.visibility = 'hidden';
	 document.getElementById('ccosto2').style.visibility = 'hidden';
   }
   
	function getFinalList(guiaNo,i){
		var jsfinalGuiaNumbers = new Array();						
		var fList=new Array();		
		// get the Guia Numbers of Checked Items
		if(document.forms[0].cb[i].checked){							
				  
		document.forms[0].clickedItems.value= document.forms[0].clickedItems.value + guiaNo + " ";						
		fList=(document.forms[0].clickedItems.value).split(' ');
		}
		else{ 					// Remove the guia numbers for  deselected check boxes..
		fList=(document.forms[0].clickedItems.value).split(' ');
		
		for(j=0;j<fList.length;j++){
		if(fList[j]==guiaNo)
		fList[j]="";
		}
	} 


// prepare the Fianl List
document.forms[0].clickedItems.value="";
	for(k=0;k<fList.length;k++){
			if(fList[k]!="")						
			document.forms[0].clickedItems.value=document.forms[0].clickedItems.value+fList[k]+" ";						
			}
			jsfinalGuiaNumbers=(document.forms[0].clickedItems.value).split(' ');				
	}

	function checkMandatory(){
		if(document.forms[0].preferedAddress.value==""){
			alert("FAVOR DE SELECCIONAR UNA DIRECCION"); //Prefered Addres Selection
			return;
		}else if(document.forms[0].collectionTime.value==""){
			 alert("CAPTURE LA HORA DE COBRANZA");//Enter Collection Time
			 document.forms[0].collectionTime.focus();
			 return;
		}else if(document.forms[0].clickedItems.value==""){
			alert("TIENE QUE SELECCIONAR AL MENOS UN RENGLON PARA GENERAR EL MANIFIESTO");//Select one row for generation
			return;		
		}else if(checkDate()){//Added by rama
			showBarraEspera('mensaje','body','Espere por favor...Generando Manifiesto');
			document.forms[0].operation.value="Generate";
			document.forms[0].submit();
		}
	}
	
	//Adde By Rama
	function checkDate(){
		
		var dd=0, mm=0, yy=0, hh=0, mi=0;
		var sMonDays=new Array;
		var cDtSep = '/';

		sDtTi =trim(document.forms[0].collectionTime.value);
		dtObj=document.forms[0].collectionTime;
		if((sDtTi.indexOf("/")==-1) || (sDtTi.indexOf(":")==-1) || (sDtTi.indexOf(" ")==-1)){
			alert("FECHA INVALIDA, DEBE SER EN EL FORMATO DD/MM/YYYY HH:MI");//Date Format validation
		    dtObj.select();
			dtObj.focus();
			return false;
         }
			
		if (sDtTi != "" || sDtTi != null){
			lstDtTi= sDtTi.split(' ');
			sDt = lstDtTi[0];
			sTi = lstDtTi[1];
			lstDt = sDt.split(cDtSep);
			lstTi = sTi.split(':');
				
			dd = parseInt(lstDt[0], 10);
			mm = parseInt(lstDt[1], 10);
			yy = parseInt(lstDt[2], 10);
			hh = parseInt(lstTi[0], 10);
			mi = parseInt(lstTi[1], 10);
		
			if (isNaN(dd) || isNaN(mm) || isNaN(yy) || isNaN(hh) || isNaN(mi) ||
				(yy>99 && yy<1000) || (mm<1 || mm>12) || (dd<1 || dd>31) ||
				(hh<0 || hh>23) || (mi<0 || mi>59) ){
				alert('FECHA INVALIDA, DEBE SER EN EL FORMATO DD/MM/YYYY HH:MI');//Date Format validation
				dtObj.select();
				dtObj.focus();
				return false;
			}
			sMonDays[0] =31;
			sMonDays[1] =29;
			sMonDays[2] =31;
			sMonDays[3] =30;
			sMonDays[4] =31;
			sMonDays[5] =30;
			sMonDays[6] =31;
			sMonDays[7] =31;
			sMonDays[8] =30;
			sMonDays[9] =31;
			sMonDays[10]=30;
			sMonDays[11]=31;

			yy = yy>50 && yy<100?(1900+yy):(yy<50?(2000+yy):yy);
			if (!((yy%4==0 && yy%100!=0) || yy%400==0)){ //not a leap year
				sMonDays[1]=28; 
			}

			if (dd<1 || dd>sMonDays[mm-1]){
				alert('EL DIA TIENE QUE ESTAR ENTRE 1 Y EL ULTIMO DEL MES');
				dtObj.select();
				dtObj.focus();
				return false;
			}
		}
		
		dateString = trim(document.forms[0].collectionTime.value);
		date  = new Date(dateString);
		enteredLongDate = date.getTime();
		if(enteredLongDate<systemDateLong){
			alert("ENTER DATE GREATER THAN SYSTEM DATE");
			return false;
		}else{
			return true;
		}
	}
	
	/* //Commented by rama
	function  chkDate(){
		var dd=0, mm=0, yy=0, hh=0, mi=0;
		var sMonDays=new Array;
		var cDtSep = '/';

		sDtTi =trim(document.forms[0].collectionTime.value);
		dtObj=document.forms[0].collectionTime;
		if((sDtTi.indexOf("/")==-1) || (sDtTi.indexOf(":")==-1) || (sDtTi.indexOf(" ")==-1)){
			alert("FECHA INVALIDA, DEBE SER EN EL FORMATO DD/MM/YYYY HH:MI");
		    dtObj.select();
			dtObj.focus();
			return false;
         }
			
		if (sDtTi != "" || sDtTi != null){
			lstDtTi= sDtTi.split(' ');
			
			sDt = lstDtTi[0];
			sTi = lstDtTi[1];

			lstDt = sDt.split(cDtSep);
			lstTi = sTi.split(':');
		
				
		dd = parseInt(lstDt[0], 10);
		mm = parseInt(lstDt[1], 10);
		yy = parseInt(lstDt[2], 10);
		hh = parseInt(lstTi[0], 10);
		mi = parseInt(lstTi[1], 10);
	
	
		alert("dd.."+dd+"   mm..."+mm+"  yy..."+yy);
		
		if (isNaN(dd) || isNaN(mm) || isNaN(yy) || isNaN(hh) || isNaN(mi) ||
			(yy>99 && yy<1000) || (mm<1 || mm>12) || (dd<1 || dd>31) ||
			(hh<0 || hh>23) || (mi<0 || mi>59) ){
			alert('FECHA INVALIDA, DEBE SER EN EL FORMATO DD/MM/YYYY HH:MI');
			dtObj.select();
			dtObj.focus();
			return false;
		}


		sMonDays[0] =31;
		sMonDays[1] =29;
		sMonDays[2] =31;
		sMonDays[3] =30;
		sMonDays[4] =31;
		sMonDays[5] =30;
		sMonDays[6] =31;
		sMonDays[7] =31;
		sMonDays[8] =30;
		sMonDays[9] =31;
		sMonDays[10]=30;
		sMonDays[11]=31;

		yy = yy>50 && yy<100?(1900+yy):(yy<50?(2000+yy):yy);

		if (!((yy%4==0 && yy%100!=0) || yy%400==0)){ //not a leap year
			sMonDays[1]=28; 
		}

		if (dd<1 || dd>sMonDays[mm-1]){
			alert('EL DIA TIENE QUE ESTAR ENTRE 1 Y EL ULTIMO DEL MES');
			dtObj.select();
			dtObj.focus();
			return false;
		}
  
		//Commented by rama	
		sysDt = new Date(<%= systemLongDate %>);
		    
		alert("  sysdate..."+sysDt.getDate()+" sysMonth..."+(sysDt.getMonth()+1)+"  sysyear.."+sysDt.getYear());
		
	  	  if (yy<sysDt.getYear() || yy>sysDt.getYear() ){
			 alert("TIENE QUE SER EL AÑO EN CURSO");
			 dtObj.select();
			 dtObj.focus();
			 return false;
		   }	   
		  
		  else if (yy==sysDt.getYear() && mm<sysDt.getMonth()+1){
			alert("EL MES NO DEBE SER MENOR AL MES ACTUAL");
			 dtObj.select();
			 dtObj.focus();
			 return false;
		 }else if(yy==sysDt.getYear() && (mm==sysDt.getMonth()+1) && dd<sysDt.getDate()){
			alert("LA FECHA DE COBRANZA TIENE QUE ESTAR DESPUES DE LA FECHA ACTUAL");			
			dtObj.select();
			dtObj.focus();
			return false;
		}	
				
		if(hh>23 || hh<0 ){
		  alert("LA HORA DE RECOLECCION, NO DEBE DE SER MENOR QUE LA HORA ACTUAL");
		  dtObj.select();
		  dtObj.focus();
		  return false;
		}
		if(yy==sysDt.getYear() && (mm==sysDt.getMonth()+1) && dd==sysDt.getDate()){
				
			if(hh < sysDt.getHours()){
			 alert("LA HORA DE RECOLECCION, NO DEBE DE SER MENOR QUE LA HORA ACTUAL");
			  dtObj.select();
			  dtObj.focus();
			  return false;
			}  
					
			else if(hh < sysDt.getHours() || (hh==sysDt.getHours() && mi<=sysDt.getMinutes())){
			  alert("LA HORA DE RECOLECCION, NO DEBE DE SER MENOR QUE LA HORA ACTUAL");
			  dtObj.select();
		 	  dtObj.focus();
			  return false;		
			}  
		}
		sdd = (dd>9?dd:("0"+dd))+"";
		smm = (mm>9?mm:("0"+mm))+"";
		syy = yy+"";
		shh = (hh>9?hh:("0"+hh))+"";
		smi = (mi>9?mi:("0"+mi))+"";

		dtObj.value = sdd+cDtSep+smm+cDtSep+syy+" "+shh+":"+smi;
    }
 	return true;	
}
*/
function trim(val){
	len = 0;
	
	try
	{ // check the val is valid string 
		len = val.length;
	}
	catch(exp)
	{
		return "";
	}
	for(i=0; i<val.length && val.substr(i,1)==' '; i++);
	for(j=val.length-1; j>-1 && val.substr(j,1)==' '; j--);
	j++;
	if (i<j) val=val.substring(i, j);
		else val="";
	
	var n2sp = 0;
	while ( (nssp=val.indexOf('  ')) != -1){
		val = val.replace('  ', ' ');
	}
	return val;
}


	function doSubmitNext(){
		var startIndex =parseInt(document.forms[0].curRow.value); 
		var endIndex = parseInt(document.forms[0].endRow.value);
		var pageIndex = parseInt(document.forms[0].pageIndex.value);
		var maxPage = parseInt(document.forms[0].maxPageIndex.value);
		var currentPage = parseInt(document.forms[0].currentPage.value);
		if( currentPage== maxPage){						
			alert("USTED ESTA VIENDO LA ULTIMA PAGINA");
		} else{
			showBarraEspera('mensaje','body','Espere por favor...Avanzando Página');
			document.forms[0].curRow.value = startIndex + 10;
			document.forms[0].endRow.value = endIndex + 10;
			document.forms[0].pageIndex.value =pageIndex+ 1;
			document.forms[0].currentPage.value = currentPage +1;
			document.forms[0].submit();					
		}
	}


	function doSubmitPrevious(){
		var startIndex = document.forms[0].curRow.value; 
		var endIndex = document.forms[0].endRow.value;
		var pageIndex = document.forms[0].pageIndex.value;
		var maxPage = document.forms[0].maxPageIndex.value;
		var currentPage = document.forms[0].currentPage.value;	
		if(document.forms[0].currentPage.value == 1){
			alert("USTED ESTA VIENDO LA PRIMER PAGINA");
		} else {
			showBarraEspera('mensaje','body','Espere por favor...Retrocediendo Página');
			document.forms[0].curRow.value = parseInt(startIndex) - 10;
			document.forms[0].endRow.value = parseInt(endIndex)- 10;
			document.forms[0].pageIndex.value = parseInt(pageIndex) - 1;
			document.forms[0].currentPage.value = parseInt(currentPage) -1;
			document.forms[0].submit();					
		}	
	}

	function callWebGuiaDetails(){	
		var clist = document.forms[0].clickedItems.value.split(" ");
		if(clist.length>2){
			alert("FAVOR DE SELECCIONAR SOLAMENTE UNA GUIA");
			return false;
		}else if(clist.length==1){
			alert("FAVOR DE SELECCIONAR UNA GUIA PARA VER EL DETALLE");
			return false;
		}else{
			showBarraEspera('mensaje','body','Espere por favor...Cargando Detalle de Guia');
			document.forms[0].action="webguiadetails.do?fromFlag=MG&hidGuiaNumber="+clist[0];	
			document.forms[0].submit();
		}
	}

	function callShipmentHistoryLog() {
		document.forms[0].operation.value = "";
		document.forms[0].formaPago.value = "";
		document.forms[0].manifiestoType.value = "";
		document.forms[0].docuType.value = "";
		document.forms[0].action = "shipmentHistory.do";//AAP01
		document.forms[0].submit();
	}
		
	function callAddressLOV() {
		document.forms[0].preferedAddress.value = "";
		var newWin = window.open('lov.do?type=clientaddress&addresstype=OC','','height=300,width=650');
	}	
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
    function cambioCentroCosto(obj) {
    	var indice = obj.selectedIndex;
    	document.forms[0].totalRecord.value = 0;
    	document.forms[0].centroCostoSel.value = obj[indice].value;
		
    	document.forms[0].operation.value="";
		document.forms[0].action = "manifestgeneration.do";
		document.forms[0].submit();
    }
    
    function goOut(page) {
    	if (page == "guiabooking") {
    		page = "thispage";
    	}
    		
    	if (confirm("DESEA PERDER LA INFORMACION?")) {
    		showBarraEspera('mensaje','body','Espere por favor...Saliendo de Registro de Clientes');
    		if (page=='shipmenthistory') {//AAP03
    			document.forms[0].action="shipmentHistory.do";//AAP03
    		}
    		else {//AAP03
    			document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page="+page;//AAP03	
    		}//AAP03
    		document.forms[0].submit();
    	} else {
    		return;
    	}
	}
</script>

</head>
<body id="body" onLoad="genNextManifest();mostrarCapaCentro('mensaje');" onscroll="mostrarCapaCentro('mensaje');" topmargin="0" leftmargin="0" marginwidth=0 marginheight=0 class="backgroundStandard">
<jsp:include page="include.jsp" flush="true" />

<table border="0" cellspacing="0" cellpadding="0" class="width100porcent backgroudBluePtx">
			<tr>
				<td  >
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
						<logic:equal name="loginForm" property="userValidate" value="validUser">
  								<a onclick="goOut('mainpage');hideMenu();"> Menú Principal </a>
  								<a onclick="goOut('cliententry');hideMenu();">Registro de Cliente Destino</a>
  								<logic:equal name="loginForm" property="showWeb" value="Y">
									<logic:equal name="loginForm" property="newBooking" value="Y">
  										<a onclick="goOut('guiabooking');hideMenu();"> Elaboración de Guias</a>
  									</logic:equal>
  								</logic:equal> 
  								<html:hidden name="loginForm" property="showPpg"></html:hidden>
								<logic:equal name="loginForm" property="showPpg" value="Y">
									<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
								</logic:equal>
  								<a onclick="goOut('guiacancel');hideMenu();"> Cancelación de guia </a>
  								<a onclick="goOut('shipmenthistory');hideMenu();"> Histórico de Envíos </a>
								<a onclick="goOut('clientreport');hideMenu();">Información del Cliente  </a>				
								
								<logic:present name="loginForm">
									<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
								  	<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>	
								  	</logic:equal>
								</logic:present>																
 						</logic:equal>
					</logic:present> 		
			</div>		
		</div>		
<table>
	<tr>
		<td style="height: 8px;">
			<textarea id="mensaje" class="textareacontenido" cols="46" readonly="readonly" rows="4" onblur="self.focus();" style="background-color: #EEEEEE; border-style: ridge; 
				overflow: hidden; text-align: center; vertical-align: middle; z-index: 2; visibility: hidden; position: absolute; top: 450px; left: 200px; width: 350px; height: 80px">
			</textarea>
		</td>
	</tr>	
</table>

<jsp:include page="nocache.jsp" flush="true" />
</table>
<html:form  action="manifestgeneration"> 
<table border="0" width="100%" cellspacing="0" cellpadding="0" class="marginAutoCentro bodyWidth">
	<tr>
	<td width="100%">
		<strong class="titleSection fontBold">
				Generación de manifiestos
		</strong
		></td>
	</tr>
	<tr align="center">
		
		<td width="100%">
			<div align="center">
				<table style="width: 100%">
					<tr>
						<td align="left" ><strong>Plaza: </strong><bean:write name="sGlobal" property ="assignedSite"/>&nbsp;<bean:write name="sGlobal" property ="siteName"/></td>
						
					</tr>
				</table>
			</div>
		</td>
	</tr>
	
  <tr>
    <td width="100%" height="384">
        <table border="0" width="97%" cellspacing="0" cellpadding="0">
          <tr> <!-- AAP01 -->          			
            <td width="26%" colspan="2">
            	<b>
            	<font face="Arial" size="2">
            		<logic:present name="sGlobal">
						<bean:write name="sGlobal" property ="origenUserClave"/>											
					</logic:present>
				</font>
				</b>
				:
				<font face="Arial" size="2">
            		<logic:present name="sGlobal">
						<bean:write name="sGlobal" property ="origenUserNombre"/>											
					</logic:present>
				</font>
			</td>
            <td width="26%" align="center" valign="top">
              <p align="right" style="margin-top: 5px;"><font size="2" face="Arial">Referencia Manifiesto&nbsp;</font></td>
            	<td width="39%" valign="middle">
            		<div class="inputV2"  style = "width:330;">
					<div class="group">       
						<html:text styleClass="comment" maxlength="65" property="reference" onblur="setBack('true');" onfocus="setBack('false');"/>
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
					</div>
	            	
				</td>	
          </tr>
          <tr>          			
            <td width="25%" rowspan="3"><font face="Arial" size="2">&nbsp;Dirección de Recolección
              </font><br>
              &nbsp;<html:textarea property="preferedAddress"  rows="4" cols="25" styleClass="textareareadonly" readonly="true"/>
			</td>
            <td width="7%" valign="bottom" rowspan="3">
			<input type="button" style="FONT-WEIGHT:bolder;COLOR:lightyellow;FONT-FAMILY:Arial;FONT-SIZE:12px;BACKGROUND-COLOR:#003366;height:22;width:80;visibility: hidden;" value="Direcciones" name="B5" onClick="callAddressLOV()"></td>
            <td width="26%">
              <p align="right"><font size="2" face="Arial">Hora de Recolección&nbsp; </font></p>
            </td>
            
          <td width="39%" valign="center" >
          	<div class="inputV2"  style = "width:163; float: left;">
				<div class="group">       
					<input type="text" name="collectionTime">
																					      
					<span class="highlight"></span>
					<span class="bar" ></span>
				</div>
			</div>
			<div style="float: left; padding-top: 5px;">
				(dd/mm/yyyy hh24:mi)
			</div>
          	
          	
          </td>
		   </tr>
          <tr>
            <td width="26%" align="center" valign="top">
              <p align="right" style="margin-top: 5px;"><font size="2" face="Arial">Manifiesto&nbsp;</font></td>
            <td width="39%"  valign="top">
            	<div class="inputV2"  style = "width:163; float: left;">
				<div class="group">       
					<html:text property="manifestNumber"  readonly="true"/>
																					      
					<span class="highlight"></span>
					<span class="bar" ></span>
				</div>
				</div>
            	
            </td>
          </tr>
          <tr>
            <td width="26%" align="center" valign="top">
              <div id="ccosto1"><p align="right"><font size="2" face="Arial">Centro de Costo&nbsp;</font></div></td>
            <td width="39%"  valign="top">
	            <div id="ccosto2">
	            	<html:select property="centrosCosto" onchange="cambioCentroCosto(this);"><!-- AAP05 -->
						<html:options property="centrosCostoValue" labelProperty="centrosCostoLabel" /><!-- AAP05 -->									
					</html:select><!-- AAP05 -->
				</div>
			</td>
          </tr>
          <tr>
            <td width="100%" colspan="5">
              <table border="0" width="100%"  cellpadding="0" height="155" class="tablaDetallev2">
                <tr class="tablaDetallev2Header">                
                <th width=2>&nbsp;</th>
                <th width="106" height="41" valign="bottom" align="left"><font face="Arial" size="2">No 
                  Guia</font></th>
                  
                <th width="109" height="41" valign="bottom" align="left"><font face="Arial" size="2">No. 
                  Rastreo</font></th>
                  
                <th width="169" height="41" valign="bottom" align="left"><font face="Arial" size="2">Sucursal 
                  Destino</font></th>
              
                <th width="227" height="41" valign="bottom" align="left"><font face="Arial" size="2">Cliente 
                  Destino</font></th>
                  
                <th width="101" height="41" valign="bottom" align="right"> 
                  <p align="right"><font face="Arial" size="2">Importe</font></th>
                  
                <th width="110" height="41" valign="bottom" align="right"> 
                  <p align="center"><font face="Arial" size="2">No.de<br>
                    Paquetes</font></th>
                  
                <th width="101" height="41"></th>
                </tr>
				
				 <%     
					JavManifestGenerationForm   manifestGenerationForm = ( JavManifestGenerationForm ) request.getAttribute("manifestGenerationForm"); 
					ArrayList manifestDetails =(ArrayList)request.getAttribute("sManifestDetails");					
					int lenmanifestDetails = manifestDetails.size();
					String checked = null;
					for(int i=0;i<lenmanifestDetails; i++){ 
						JavManifestRecord manifestRecords = (JavManifestRecord)manifestDetails.get(i);
						checked = "";
						if ( manifestGenerationForm.getClickedItems() != null && manifestGenerationForm.getClickedItems().indexOf(manifestRecords.getGuiaNumber().trim()) != -1 )
							checked = "checked";
									
				 %>    
							
                <tr class="tablaDetallev2Body">                
                <td width=2>&nbsp;</td>
                <td width="60" height="17" nowrap class="td" align="left">&nbsp;<%=manifestRecords.getOrigBranch()%><%=manifestRecords.getFormNumber()%> 
                  <input type="hidden" name="hidguiaNumber">
                </td>                  
                <td width="80" height="17" nowrap class="td">&nbsp;<%=manifestRecords.getGuiaNumber()%> 
                  <input type="hidden" name="hidformNumber"></td>                  
                <td width="150" height="17" nowrap class="td">&nbsp; <%= manifestRecords.getDestBranch()%>
                  <input type="hidden" name="hiddestBranch"></td>                  
                <td width="275" height="17" nowrap class="td">&nbsp;<%= manifestRecords.getDestClientName()%>
                  <input type="hidden" name="hiddestClientName"></td>       
                 <%
					if(dispAmntFlag)
    	         	{
                 %>           
		            	<td width="75" height="17" nowrap class="td" align="right">
						   	<input type="hidden" name="hidguiaAmount">&nbsp;<%= manifestRecords.getGuiaAmount()%>
						</td>
				<%
					}
					else
					{
				%>
						<td width="75" height="17" nowrap class="td" align="right">
						   	<input type="hidden" name="hidguiaAmount"> <font size="4"><b>**********</b></font>
						</td>
				<%
					}
				%>
				                  
                <td width="60" height="17" nowrap class="td" align="right"><input type="hidden" name="hidnumberPack">&nbsp;	<%= manifestRecords.getNumPack()%>
				</td>
                  
                <td width="101" height="17">
                  <input type="checkbox"   onClick="getFinalList('<%=manifestRecords.getGuiaNumber()%>','<%=i%>')" name="cb" value="<%=manifestRecords.getGuiaNumber()%>" <%= checked %> ></td>
                </tr>				
							
				<%
              	}
				if(lenmanifestDetails <10){
              	int emptyLoop = 10-lenmanifestDetails;
				
                for(int j=0;j<emptyLoop;j++){
              %>	
               <tr> 
					<td width=2>&nbsp;</td>
					<td width="60" height="17" nowrap class="td" align="right">&nbsp; </td>                  
					<td width="80" height="17" nowrap class="td">&nbsp; </td>                  
					<td width="150" height="17" nowrap class="td">&nbsp; </td>                  
					<td width="275" height="17" nowrap class="td">&nbsp;</td>                  
					<td width="75" height="17" nowrap class="td" align="right">&nbsp;</td>                  
					<td width="60" height="17" nowrap class="td" align="right">&nbsp;</td>                  
					<td width="101" height="17"><input type="checkbox" name="cb" value="" disabled=true> </td>
                </tr>				
					
              <%		
                }
              	}
              %>
	
			<tr>
			             
          </tr>
        </table>
    </td>
  </tr>
  </table>
      </td>
  </tr>
</table>

<table width=100% border=0 class="marginAutoCentro bodyWidth" style="padding-top: 12;">
<tr>
	<td>
	<table align="left" border=0 width="100%" cellspacing="0" cellpadding="0" class=" " >
		<tr style="text-align: center;" >								  
	      <td style="text-align: center;"> 
			 
			 <input type="button"  class="button buttonMedium"  onClick="callShipmentHistoryLog()" value="Regresar" name="B1"></td>
			<%
				String strdsiableFlag=(String)request.getAttribute("sDisabledFlag");
				String strDataFlag=(String)request.getAttribute("sDataFlag");
				//AAPP
				    if(strDataFlag!=null && strDataFlag.equalsIgnoreCase("Yes")){
										
			%>			
			 <td > 
			   <input type="button" onClick="checkMandatory()" class="button" value="Generar"  disabled=true name="B2">
			</td>   
												 
			 <td > 
               <input type="button" class="button buttonLarge" value="Detalle de Guía" onClick="callWebGuiaDetails()" name="B4" disabled=true></td>                  
			<td > 
                <input type="button" class="button buttonMedium" value="Imprimir" name="B3" disabled=true></td>                  
			<td > 
              	<input type="button" onClick="doSubmitPrevious()" value="Anterior" name="B7" class="button buttonMedium"  disabled=true></td>                  
			<td> 
               <input type="button"  onClick="doSubmitNext()" value="Siguiente"  name="B6" class="button buttonMedium" disabled=true>
			 </td>				 
			<% 
			   }						     
			   if(strDataFlag!=null && strDataFlag.equalsIgnoreCase("No")){		  
			       if(strdsiableFlag!=null && strdsiableFlag.equals("true")){				   
 			%>
												 
			<td> 
			   <input type="button" onClick="checkMandatory()" class="button buttonMedium" value="Generar" disabled=true name="B2">   
			</td> 
						  
			<%
			  } else{
			 %>
			 <td >
			 	<logic:present name="sGlobal">
			 		<logic:equal name="sGlobal" property="disableMFGen" value="0"> 
			    		<input type="button" onClick="checkMandatory()" class="button buttonMedium " value="Generar"  name="B2">
			    	</logic:equal>
			    	<logic:notEqual name="sGlobal" property="disableMFGen" value="0">
			    		<input type="button" onClick="alert('funcionalidad deshabilitada');" class="button buttonMedium " value="Generar"  name="B2">
			    	</logic:notEqual>
			    </logic:present>   
			</td> 
			<%
			   }
			%>
						  
			 <td > 
			   <input type="button" class="button buttonLarge" value="Detalle de Guía" onClick="callWebGuiaDetails()" name="B4" ></td>                  
			<td > 
			    <input type="button" class="button buttonMedium" value="Imprimir" name="B3" onclick="callPrint()"></td>                  
			<td > 
			  	<input type="button" onClick="doSubmitPrevious()" value="Anterior" name="B7" class="button buttonMedium">
			  	</td>                  
			<td > 
			   <input type="button"  onClick="doSubmitNext()" value="Siguiente"  name="B6" class="button buttonMedium" >
			   </td>
			<%
			   }
			%>  	
						
				 </tr>				 
             </table>
             </td>
           </tr>				 
		</table>
<html:hidden property="operation" value="Normal"/> 
<html:hidden property="totalRecord"/> 
<html:hidden property="noDataFound"/>
<html:hidden property="manifestIssueDate"/>	
<html:hidden property="curRow"/>
<html:hidden property="endRow"/> 
<html:hidden property="pageIndex"/> 
<html:hidden property="currentPage"/> 
<html:hidden property="maxPageIndex"/>
<html:hidden property="clickedItems"/> 
<html:hidden property="disableGenerate"/>
<html:hidden property="manifestAmount"/>
<html:hidden property="sumPack"/>
<html:hidden property="numOrdered"/>
<html:hidden property="cttNumber"/>
<html:hidden property="addressCode"/>
<html:hidden property="addressType"/>
<html:hidden property="doorNumber"/>
<html:hidden property="streetName"/>
<html:hidden property="phoneNumber"/>
<html:hidden property="suitNumber"/>
<html:hidden property="floorNumber"/>	
<html:hidden property="addressLine1"/>
<html:hidden property="addressLine2"/>
<html:hidden property="addressLine3"/>
<html:hidden property="addressLine4"/>
<html:hidden property="addressLine5"/>
<html:hidden property="addressLine6"/>
<html:hidden property="addressLine7"/>
<html:hidden property="zipCode"/>
<html:hidden property="formaPago"/><!-- AAP01 -->
<html:hidden property="manifiestoType"/>
<html:hidden property="docuType"/><!-- AAP02 -->
<html:hidden property="centroCostoSel"/><!-- AAP02 -->
<html:hidden property="filtroPor"/><!-- AAP02 -->
<html:hidden property="msjErr"/><!-- AAP02 -->
<html:hidden property="preferedAddressCode"/>
<input type="hidden" name='showWeb' value='<bean:write name="loginForm" property ="showWeb"/>'/>
</html:form>
</body>
</html:html>



<script>
setTimeout(function () {
	$(" .header-logo").css({"display": "none"})	
}, 1);


</script>