<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:html locale="true">
<head>
<link rel="stylesheet" href="webbooking.css">

<title>Struts File Upload Example</title>
<html:base/>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<jsp:include page="nocache.jsp" flush="false" />
<jsp:include page="include.jsp" flush="false" />

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<jsp:include page="nocache.jsp" flush="false" />
<html:form action  = "/clientDestinationEntry">

<table border="0" cellspacing="0" cellpadding="0" width="769">
  <tr> 
    <td width="429" height="77"><img src="images/inner_top01.jpg" border="0" usemap="#peMap" width="429" height="79"></td>
    <td width="342" height="77"> 
      <div align="left"><img src="images/inner_top02.jpg" width="341" height="79"></div>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="3"></td>
  </tr>
</table>
<map name="peMap"> 
  <area shape="rect" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com">
</map>

<table cellspacing="0" cellpadding="0" border="0" align="left">
  <tr> 
    <td colspan="3"> <img src="images/main_top01.jpg" width="770" height="45"></td>
  </tr>
  <tr> 
    <td rowspan="2" width="250" valign="top" align="left"> 
      <table width="70%" border="0" cellspacing="0" cellpadding="0" align="left">
      
<!-- Added by Murugesapandian related to Destination client entry image link 16/05/2006 -->
  
<!-- Added by Bala -->




<!-- Added by Murugesapandian related to Destination client entry image link 16/05/2006-->
	  </table>
      <div align="left"></div>
    </td>
    <td width="40">&nbsp;</td>
    <td width="482"> 
      <div align="right"><img src="images/main_grt01.jpg" width="287" height="104"></div>
    </td>
  </tr>
  <tr> 
    <td colspan="2" align="right"> 
      <div align="right"><img src="images/main_bottom.jpg" width="511" height="119" usemap="#changePassMap" border="0"></div>
      <div align="right"></div>
    </td>
  </tr>
</table>
<map name="changePassMap"> 
  <area shape="rect" coords="109,5,270,18" href="JavChangePassword.jsp" alt="Cambiar Contraseña" title="Cambiar Contraseña">
  <area shape="rect" coords="141,23,271,36" href="login.do?logout=yes" alt="Terminar Sesión" title="Terminar Sesión">
</map>
</html:form>
</body>
</html:html>
