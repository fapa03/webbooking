<%@ page info="WebBooking - General"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.* , bean.Global"%>
<%
	Global global = (Global) session.getAttribute("sGlobal");
 
%>
<html>

<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>Nombre</title>
<LINK href="Styles.css" type=text/css rel=stylesheet>
<script lanaguage="javascript">
function submitform() {
	if(document.forms[0].guiaNumber.value=="")
	alert("INGRESE EL NÚMERO DE RASTREO");
	else
	document.forms[0].submit();
}
function logout() {
	document.forms[0].action="login.do?logout=yes";
	document.forms[0].submit();
}

function changebranch() {
	document.forms[0].action="changebranch.do";
	document.forms[0].submit();
}
function mainpage() {
	document.forms[0].action="mainpage.do";
	document.forms[0].submit();
}

function downloadPrintGuia() {
	isDownloaded=true;
	document.forms[0].action = "DownloadFileGuia.jsp";
	document.forms[0].submit();
}

function downloadPrintGuiaPDF() {
	isDownloaded = true;
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

function convert(){
	if (document.forms[0].controlWait.value == 'N') {
		document.forms[0].controlWait.value="Y";
		isDownloaded=true;
		document.forms[0].action = "guiaConversionMonitor.do?insideTask=otherBook";
		document.forms[0].currentTask.value="start";
		document.forms[0].submit();
	}	
}

function guiaselection(){
	isDownloaded=true;
	//document.forms[0].action = "DownloadFileGuia.jsp"
	document.forms[0].currentTask.value="start";
	document.forms[0].submit();
}

function printZPLZebraBridge() {
	initBridgePrintQZ('cadena');
}

</script>

	<script type="text/javascript" charset="UTF-8" src="js/qz/bridgePrintQZ.js"></script>
	<link rel="stylesheet" media="screen" type="text/css" href="css/QZTray.css">

</head>


<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<html:form name="invoiceConversionForm"
	type="paquetexpress.internal.prepaid.conversion.form.ConversionForm"
	action="guiaconversion">
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="53%"><img src="images/mainTop.jpg" height="74"></td>
						<td width="47%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="52%">
								<div align="right"><strong><font color="#000066"
									size="2" face="Arial">Nombre&nbsp;&nbsp;</font></strong></div>
								</td>
								<td width="2%"><strong><font color="#000066"
									size="2" face="Arial">:</font></strong></td>
								<td width="46%">
								<div align="left">
									<font color="#990000" size="2" face="Verdana">
										<logic:present name="sGlobal">
											<bean:write name="sGlobal" property ="origenUserNombre"/>											
										</logic:present>
									</font></div>
								</td>
							</tr>
							<tr>
								<td>
								<div align="right"><strong><font color="#000066"
									size="2" face="Arial">Sucursal&nbsp;</font></strong></div>
								</td>
								<td width="2%"><strong><font color="#000066"
									size="2" face="Arial">:</font></strong></td>
								<td>
								<div align="left">
									<font color="#990000" size="2" face="Verdana">
										<logic:present name="branchid">
											<bean:write name="branchName"/>
										</logic:present>
									</font>
								</div>
								</td>
							</tr>
							<tr>

								<td COLSPAN="3">
								<p align="right"><logic:present name="admin">
									<INPUT TYPE="BUTTON" VALUE="Cambiar de Sucursal"
										class="buttondisplay1" ONCLICK="changebranch();">
								</logic:present>
								<logic:present name="loginForm">
									<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
									<INPUT TYPE="BUTTON" VALUE="Terminar Sesión"
										class="buttondisplay" onclick="logout()">
									</logic:equal>
								</logic:present>
								<INPUT
									TYPE="BUTTON" VALUE=" Menú Principal" class="buttondisplay"
									onclick="mainpage()">
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</table>

	<p>&nbsp;</p>

	<table border="0" width="100%">
		<tr>
			<td width="100%" colspan="2" class="MainHead">
			<p align="center">Imprimir Guías
			</td>
		</tr>
		<tr>
			<td width="50%">&nbsp;</td>
			<td width="50%">&nbsp;</td>
		</tr>
		<logic:present name="error">
			<tr>
				<td width="100%" colspan="2" class="LabelError" align="center"><bean:write name="error"/></td>

			</tr>
		</logic:present>
		<tr>
			<td width="999" align="center" colspan=3 class="LabelError"></td>
		</tr>
		<tr> 
	      <td width="999" align="center" colspan=3><img src="images/download_GN.gif" usemap="#GUIANOR" border="0"> 
	      </td>      
	    </tr>
		<tr> 
	      <td width="999" align="center" colspan=3><img src="images/download_GN_PDF.gif" usemap="#GUIANORPDF" border="0"> 
	      </td>      
	    </tr>
	    
	    <tr style="<%= ( !global.getAllowPrintQZ().equalsIgnoreCase("Y") ? "display: none;" : "" ) %>">
	      <td width="999" align="center" colspan=3>
		      <a onclick="printZPLZebraBridge()" style="cursor: pointer;">
		        <img src="images/download_PRINT.gif" border="0"> 
		      </a>
	      </td>
	    </tr>		    
	    
		<tr>
			<td colspan="3">
			<div align="center"><INPUT TYPE="BUTTON" name="origin"
				VALUE="Documentar otra Guía" class="buttonlargedisplay"
				onclick="convert()" colspan="3" styleClass="width: 75; height: 22">
			</div>
			</td>
		</tr>
	</table>
	<map name="GUIANOR">
		<area shape="poly"
			coords="105,31,105,69,87,69,87,85,153,85,153,69,135,69,135,31"
			href="javascript:downloadPrintGuia()">
	</map>
	<map name="GUIANORPDF">
		<area shape="poly"
			coords="105,31,105,69,87,69,87,85,153,85,153,69,135,69,135,31"
			href="javascript:downloadPrintGuiaPDF()">
	</map>
	<html:hidden property="currentTask" />
	<input type="hidden" name="trackingNoGen" value='<bean:write name="trackingNoGen"/>'/>	
	<input type="hidden" name="trackingNoGenRet" value='<bean:write name="trackingNoGenRet"/>'/>	
	<input type="hidden" name="getCartaPorte" value='<bean:write name="getCartaPorte"/>'/>
	<input type="hidden" name="controlWait" value = "N">
		<input type="hidden" id="cadena" name="cadena" value='<bean:write name="cadena"/>'>	
	<div id="zonePrintQZ"></div>
	

</html:form>
</body>