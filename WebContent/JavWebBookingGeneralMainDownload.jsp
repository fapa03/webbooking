<%@ page info="WebBooking - Download" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList, bean.Global"%>
<%
	Global global = (Global) session.getAttribute("sGlobal");
 
%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />

	<script type="text/javascript" charset="iso-8859-1" src="js/qz/bridgePrintQZ.js"></script>
	<link rel="stylesheet" media="screen" type="text/css" href="css/QZTray.css">

<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<title>Download</title>
</head>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="backgroundStandard" >
<jsp:include page="nocache.jsp" flush="false" />

<script type="text/javascript" charset="iso-8859-1">
function downloadPrintGuia(){
	isDownloaded=true;
	document.forms[0].action = "DownloadFileGuia.jsp";
	document.forms[0].submit();
}

function downloadPrintGuiaPdf(){
	isDownloaded=true;
	var link = '';
	link = document.forms[0].getCartaPorte.value+'GenCartaPorte?trackingNoGen='+document.forms[0].trackingNoGen.value;	
	let pdfFormat = "<%=  "Y".equalsIgnoreCase(global.getPdfFormat()) ? "Y" : "N" %>";
	
	if (pdfFormat === "Y"){
		link = link + "&measure=4x6";
	}
	window.open(link, '_blank');
	if(document.forms[0].trackingNoGenRet.value != ''){
		link = document.forms[0].getCartaPorte.value+'GenCartaPorte?trackingNoGen='+document.forms[0].trackingNoGenRet.value;		
		if (pdfFormat === "Y"){
			link = link + "&measure=4x6";
		}
		window.open(link, '_blank');
	}
}

function downloadPrintBarcode(){
	document.forms[0].action = "DownloadFileBarcode.jsp";
	document.forms[0].submit();
}
function bookAnother(){
	if(confirm("Presione el bot\u00F3n Aceptar \u00FAnicamente si usted ya descargo el archivo e Imprimi\u00F3 correctamente la Gu\u00EDa y sus etiquetas.")){
		document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes";
		document.forms[0].submit();
		return;
	}else{
		return;
	}
}
function goOut(page){
	if(confirm("Presione el bot\u00F3n Aceptar \u00FAnicamente si usted ya descargo el archivo e Imprimi\u00F3 correctamente la Gu\u00EDa y sus etiquetas.")){
		document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page="+page;
		document.forms[0].submit();
		return;
	}else{
		return;
	}
}
function logOut(){	
	if(confirm("Presione el bot\u00F3n Aceptar \u00FAnicamente si usted ya descargo el archivo e Imprimi\u00F3 correctamente la Gu\u00EDa y sus etiquetas.")){
		document.forms[0].action="login.do?logout=yes";
		document.forms[0].submit();
		return;
	}else{
		return;
	}
}

	function printZPLZebraBridge() {
		initBridgePrintQZ('cadena');
// 		printZPLZebra(document.forms[0].cadena.value);
	}
</script>
<html:form action="webBookinggeneralMain.do">
<!-- <table border="0" cellspacing="0" cellpadding="0" class="marginAutoCentro bodyWidth" > -->
<!--   <tr>  -->
<!--     <td width="429" height="77"><img src="images/inner_top01.jpg" border="0" usemap="#peMap" width="429" height="79"></td> -->
<!--      <td width="342" height="77">    -->
<!--       <div align="left"><img src="images/inner_top02.jpg" width="341" height="79" border="0"></div> -->
<!--     </td> -->
<!--   </tr> -->
<!--   <tr>  -->
<!--     <td colspan="2" height="3"></td> -->
<!--   </tr> -->
<!-- </table> -->

<table border="0" cellspacing="0" cellpadding="0" class="width100porcent backgroudBluePtx">
			<tr>
				<td  >
					<div class="bodyWidth marginAutoCentro">
						<img src="images/logos/logoW.png" border="0" usemap="#peMap"
							style="max-width: 138px;margin-top: 11px;margin-bottom: 9px;"> 
							
							
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
				<a onclick="goOut('mainpage')" style="">Men&uacute; principal</a>
				<logic:present name="loginForm">
					<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
						<a onclick="logOut()" style="">Terminar sesi&oacute;n</a>
					</logic:equal>
				</logic:present>		 
			</div>
 			
		</div>			

  <table class="marginAutoCentro bodyWidth" border="0" cellspacing="0" cellpadding="0">
     
     
    <tr>
      <td height="30">&nbsp;</td>
    </tr>
    <tr> 
      <td> 
        <table width="100%" border="0" cellspacing="0" cellpadding="3">
          <tr> 
            <td width="11%"> 
              <div align="right"><img src="images/bullet.gif" width="5" height="5"></div>
            </td>
            <td width="1%">&nbsp;</td>
            <td width="88%"><font color="#000066" size="1" face="Arial"><b><font size="2" color="#CC3300">
				Gu&iacute;a Generada con &eacute;xito.
            </font></b></font> </td>
          </tr>
          <tr> 
            <td width="11%"> 
              <div align="right"><img src="images/bullet.gif" width="5" height="5"></div>
            </td>
            <td width="1%">&nbsp;</td>
            <td width="88%"><font color="#000066" size="2" face="Arial"><b><font color="#CC3300">
				El proceso de documentaci&oacute;n estar&aacute; completo solo si usted DESCARGA el archivo que contiene la Impresi&oacute;n de la Gu&iacute;a y la(s) Etiqueta(s).
            </font></b> </font></td>
          </tr>
          <tr> 
            <td width="11%"> 
              <div align="right"><img src="images/bullet.gif" width="5" height="5"></div>
            </td>
            <td width="1%">&nbsp;</td>
            <td width="88%"><b><font color="#CC3300" size="2" face="Arial">
				Por favor Descargue el archivo para que la Gu&iacute;a y Etiqueta(s) sea(n) impresa(s).
			</font></b></td>
          </tr>
          <tr style="<%= ( !global.getAllowPrintQZ().equalsIgnoreCase("Y") ? "display: none;" : "" ) %>"> 
            <td width="11%"> 
              <div align="right"><img src="images/bullet.gif" width="5" height="5"></div>
            </td>
            <td width="1%">&nbsp;</td>
            <td width="88%"><b><font color="#CC3300" size="2" face="Arial">
				Para imprimir inicie el programa QZ Tray.
			</font></b></td>
          </tr>          
        </table>
      </td>
    </tr>
  </table>  
  <table class="marginAutoCentro bodyWidth" border="0" cellspacing="0" cellpadding="0" >
    <tr> 
      <td colspan="3" height="30">&nbsp;</td>
    </tr>
    <tr> 
      <td width="365">&nbsp;</td>
      <td width="35">&nbsp;</td>
      <td width="361">&nbsp;</td>
    </tr>
    <tr> 
      <td width="769" align="center" colspan=3><img src="images/download_GN.gif" usemap="#GUIANOR" border="0"> 
      </td>      
    </tr>
	<tr> 
      <td width="769" align="center" colspan=3><img src="images/download_GN_PDF.gif" usemap="#GUIANORPDF" border="0"> 
      </td>      
    </tr>
    
    <tr style="<%= ( !global.getAllowPrintQZ().equalsIgnoreCase("Y") ? "display: none;" : "" ) %>">
      <td width="769" align="center" colspan=3>
      <a  onclick="printZPLZebraBridge()" style="cursor: pointer;">
        <img src="images/download_PRINT.gif" border="0"> 
      </a>
      </td>
    </tr>
  
    <tr> 
      <td width="365" height="40">&nbsp;</td>
      <td width="35" height="40">&nbsp;</td>
      <td width="361" height="40">&nbsp;</td>
    </tr>
    <tr> 
      <td colspan="3"> 
        <div align="center"> 
               <html:button property="bookanother" value="Documentar otra Gu&#237;a" styleClass="bookbutton" onclick="bookAnother()" />
        </div>
      </td>
    </tr>
  </table>
  <map name="peMap"> 
    <area shape="rect" coords="13,19,235,71" href="javascript:goOut('mainpage')">
  </map>
<map name="GUIANOR">
  <area shape="poly" coords="105,31,105,69,87,69,87,85,153,85,153,69,135,69,135,31" href="javascript:downloadPrintGuia();">
</map>
<map name="GUIANORPDF">
  <area shape="poly" coords="105,31,105,69,87,69,87,85,153,85,153,69,135,69,135,31" href="javascript:downloadPrintGuiaPdf();">
</map>
<map name="Toplink"> 
  <area shape="rect" coords="520,3,612,17" href="javascript:logOut()">
  <area shape="rect" coords="418,3,500,17" href="javascript:goOut('mainpage')">
</map>
<div id="zonePrintQZ"></div>

<input type="hidden" name="trackingNoGen" value='<bean:write name="trackingNoGen"/>'/>
<input type="hidden" name="trackingNoGenRet" value='<bean:write name="trackingNoGenRet"/>'/>
<input type="hidden" name="getCartaPorte" value='<bean:write name="getCartaPorte"/>'/>
<input type="hidden" name="paquetes" value='<bean:write name="paquetes"/>'>
<input type="hidden" id="cadena" name="cadena" value='<bean:write name="cadena"/>'>
</html:form>
</body>
</html:html>
