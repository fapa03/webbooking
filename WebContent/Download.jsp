<%--
Authur		:	Ramachandran.V
Date		:	28-May-2003

FileName	:	Download.jsp
CSS FileName	:	webbooking.css
--%>

<%@ page info="WebBooking - Download" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html:html>
<head>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<title>Download</title>
</head>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" background="images/bg.gif">
<jsp:include page="nocache.jsp" flush="false" />
<script language="javascript">
function downloadPrintGuia(){
		isDownloaded=true;
		document.forms[0].action = "DownloadFileGuia.jsp"
		document.forms[0].submit();
}
function downloadPrintBarcode(){
	document.forms[0].action = "DownloadFileBarcode.jsp"
	document.forms[0].submit();
}
function bookAnother(){
	if(confirm("Presione el botón “Aceptar” únicamente si usted ya descargo el archivo e Imprimió correctamente la Guía y sus etiquetas.")){
		document.forms[0].action="webBookinggeneral.do?includeattribute=yes";
		document.forms[0].submit();
		return;
	}else{
		return;
	}
}
function goOut(page){
	if(confirm("Presione el botón “Aceptar” únicamente si usted ya descargo el archivo e Imprimió correctamente la Guía y sus etiquetas.")){
		document.forms[0].action="webBookinggeneral.do?includeattribute=yes&page="+page;
		document.forms[0].submit();
		return;
	}else{
		return;
	}
}
function logOut(){	
	if(confirm("Presione el botón “Aceptar” únicamente si usted ya descargo el archivo e Imprimió correctamente la Guía y sus etiquetas.")){
		document.forms[0].action="login.do?logout=yes";
		document.forms[0].submit();
		return;
	}else{
		return;
	}
}
</script>
<html:form action="webBookinggeneral.do">
<table border="0" cellspacing="0" cellpadding="0" width="769">
  <tr> 
    <td width="429" height="77"><img src="images/inner_top01.jpg" border="0" usemap="#peMap" width="429" height="79"></td>
    <td width="342" height="77"> 
      <div align="left"><img src="images/inner_top02.jpg" width="341" height="79" border="0"></div>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="3"></td>
  </tr>
</table>
  <table width="769" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td> 
        <div align="right"><img src="images/download_toplink.jpg" width="616" height="20" usemap="#Toplink" border="0"></div>
      </td>
    </tr>
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
				Guía Generada con Éxito.
            </font></b></font> </td>
          </tr>
          <tr> 
            <td width="11%"> 
              <div align="right"><img src="images/bullet.gif" width="5" height="5"></div>
            </td>
            <td width="1%">&nbsp;</td>
            <td width="88%"><font color="#000066" size="2" face="Arial"><b><font color="#CC3300">
				El proceso de documentación estará completo solo si usted DESCARGA el archivo que contiene la Impresión de la Guía y la(s) Etiqueta(s).
            </font></b> </font></td>
          </tr>
          <tr> 
            <td width="11%"> 
              <div align="right"><img src="images/bullet.gif" width="5" height="5"></div>
            </td>
            <td width="1%">&nbsp;</td>
            <td width="88%"><b><font color="#CC3300" size="2" face="Arial">
				Por favor Descargue el archivo para que la Guía y Etiqueta(s) sea(n) impresa(s).
			</font></b></td>
          </tr>
        </table>
      </td>
    </tr>
  </table>  <table width="769" border="0" cellspacing="0" cellpadding="0" align="left">
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
      <td width="365" height="40">&nbsp;</td>
      <td width="35" height="40">&nbsp;</td>
      <td width="361" height="40">&nbsp;</td>
    </tr>
    <tr> 
      <td colspan="3"> 
        <div align="center"> 
               <html:button property="bookanother" value="Documentar otra Guía" styleClass="bookbutton" onclick="bookAnother()" />
        </div>
      </td>
    </tr>
  </table>
  <map name="peMap"> 
    <area shape="rect" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com">
  </map>
<map name="GUIANOR">
  <area shape="poly" coords="105,31,105,69,87,69,87,85,153,85,153,69,135,69,135,31" href="javascript:downloadPrintGuia()">
</map>
<map name="Toplink"> 
  <area shape="rect" coords="520,3,612,17" href="javascript:logOut()">
  <area shape="rect" coords="418,3,500,17" href="javascript:goOut('mainpage')">
</map>
</html:form>
</body>
</html:html>
