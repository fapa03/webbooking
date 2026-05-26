<%@page contentType="text/html"%>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head>
<title>  Data File Import And Processing Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<script language="javascript">

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

function validate()
{

	var file=window.document.forms[0].theFile.value;
		alert('fil '+file);
if( file=='')
	{
	   alert("Please Select File");
	   window.document.forms[0].theFile.focus();
	   return false;
	}
	document.forms[0].action="webbookingFileUpload.do?action=upload"
  // document.forms[0].submit();
	return true;
}
function validateProcess()
{
	
	window.document.forms[0].action="webbookingFileUpload.do?action=process"
  // document.forms[0].submit();
   return true;

}
function validatePrint()
{
	
	window.document.forms[0].action="webbookingFileUpload.do?action=print"
	//document.forms[0].submit();
	return true;
}

function validateLog()
{
	
	window.document.forms[0].action="webbookingFileUpload.do?action=log"
	//document.forms[0].submit();
	return true;
}

function goOut(page){
			
			if(page=="guiabooking")
			page="thispage";
			
		if(confirm("DESEA PERDER LA INFORMACION?")){
					//window.document.forms[0].action="webBookinggeneral.do?includeattribute=yes&page="+page;
					//window.document.forms[0].submit();
					//window.location="webBookinggeneral.do?includeattribute=yes&page="+page;
					if (page=='shipmenthistory') {//AAP01
						window.location="shipmentHistory.do";//AAP01
					} else {//AAP01
						window.location="webBookinggeneral.do?includeattribute=yes&page="+page;//AAP01	
					}//AAP01
				}else{
					return;
				}		
	}
	
	
	
function logOut(){
	window.document.forms[0].action="login.do?logout=yes";
	document.forms[0].submit();
	
}



</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('images/elabo_o.gif','images/cancel_o.gif','images/histori_o.gif','images/infor_o.gif')">
<jsp:include page="nocache.jsp" flush="false" />
<jsp:include page="include.jsp" flush="false" />
<html:form action="/webbookingFileUpload.do"  enctype="multipart/form-data">

<table width="68%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td><img src="images/toplink.gif" width="771" height="22" usemap="#mainMap" border="0">
      <map name="mainMap"> 
		
		  <area shape="rect" coords="565,5,680,18" href="javascript:goOut('clientreport')">
       
		<area shape="rect" coords="460,5,555,17" href="javascript:goOut('shipmenthistory')">
        
		<area shape="rect" coords="1,4,135,18" href="javascript:goOut('cliententry')">
        
		<area shape="rect" coords="145,5,226,18" href="javascript:goOut('mainpage')">
        
		<area shape="rect" coords="236,5,342,18" href="javascript:goOut('guiabooking')">
        
		<area shape="rect" coords="352,5,450,18" href="javascript:goOut('guiacancel')">
       
	   <area shape="rect" coords="690,4,772,18" href="login.do?logout=yes" alt="Terminar Sesión" title="Terminar Sesión">
	   <!-- Ended By bala -->
      </map>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>


	
<table cellspacing="0" cellpadding="0" border="0" align="left" width="100%">
  <tr width="100%">
		<td colspan="3" width="40%"><div align="center"><font color="blue" size="5"> Data File Import & Process Form</font></div></td>
		</tr>
  <tr width="100%" > 
    <td rowspan="2" width="44%"  align="left"> 
     <table width="100%" border="0" cellspacing="0" cellpadding="0" align="left">
	<logic:present name="errormsgprint" scope="request">
	<tr ><td>
	 <div align="center" > <font style="font-size: 15pt; color: #ff0000; font-weight: bold"><bean:write name="errormsgprint" /></font>
	 </div>
	</td>
	</tr>
	</logic:present>

	
	   <tr>
		<td>     <div align="left">
		<font color="red"><html:errors/></font></div></td>
		</tr>
			<tr width="100%" ><td >
	  <div align="left">
	&nbsp;&nbsp;&nbsp;&nbsp;File Name :
		<html:file property="theFile"  /> </div>
	</td>
	</tr>
	
<tr>
<td  > 
<div align="center"> <html:submit onclick=" return validate()">Cargar Archivo</html:submit> <html:submit onclick=" return validateProcess()">Procesar</html:submit></div></td>
</tr><tr>
<td> <div align="center">
<html:submit onclick="return validatePrint()">Imprimir Guías</html:submit><html:submit onclick=" return validateLog()">View Log</html:submit></div>
</td>
</tr>
<!-- Added by Murugesapandian related to Destination client entry image link 16/05/2006 -->
  
<!-- Added by Bala -->

<!-- Added by Murugesapandian related to Destination client entry image link 16/05/2006-->
	  </table>
       </td>
    <td align="left" > 
      <div align="left"><img src="images/main_grt01.jpg" width="411" height="104"></div>
    </td>
  </tr>
  <tr> 
    <td colspan="2" align="left"> 
      <div align="left"><img src="images/main_bottom.jpg" width="411" height="119" usemap="#changePassMap" border="0"></div>
      <div align="left"></div>
    </td>
  </tr>
</table>
<map name="changePassMap"> 
  <area shape="rect" coords="109,5,270,18" href="JavChangePassword.jsp" alt="Cambiar Contraseña" title="Cambiar Contraseña">
  <area shape="rect" coords="141,23,271,36" href="login.do?logout=yes" alt="Terminar Sesión" title="Terminar Sesión">
</map>
</html:form>
</body>
</html>
