
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:html>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<TITLE>Input the Type of Manifest</TITLE>
<SCRIPT language="javascript">
var val="";
function fun()
{
val=document.all.v1.value;
}

function fun1()
{

val=document.all.v2.value;

}

function acceptValue()
{

if((val=="") || (val.length < 0) )
	{
	alert("Por favor, seleccione una opción");
	return false;
	}
	window.opener.document.all.txt_selection.value=val;
	if((document.dialog.v1.checked==true)&&(document.dialog.v2.checked==true))
	{
		alert("Usted no puede seleccionar ambas opciones");
		return false;
	}

	
window.close();
}

</SCRIPT>
<link rel="stylesheet" href="webbooking.css">
</HEAD>

<BODY bgcolor='#CEDEE9'>

<form name="dialog">
<table>
	<tr><td>
	<table align="left" width="100%" border=1>
		<tr><td align="center"><B>SEPARACION DE GUIAS POR DESTINO</B></TD></TR>

			<tr><td  ><b>  ENVIOS ENTRE CIUDADES FRONTERIZAS </b>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="v1"  value= "IN"  onclick="fun();"></td></tr>       <tr>
            <td><b>
  ENVIOS DE CIUDAD FRONTERA  A CIUDAD NO FRONTERA </b><input type="checkbox" name="v2"  value= "BR" onclick="fun1();"></td>
			
       </tr>    </table>
 </td></tr>
 <tr><td>
	   <table align="left" width="100%" border=0> 
	   <tr><td align="center"> <input type='button' value='Aceptar' style='width:55;height:20;font-size:11' class='button'  onclick='acceptValue();'></td></tr></table></table>
	   




</form>

</BODY>
</html:html>
