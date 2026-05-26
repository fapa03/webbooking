
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<TITLE>Input the Type of Manifest</TITLE>
<SCRIPT language="javascript">
function fun()
{
var val=document.all.v1.value;
window.opener.document.all.txt_selection.value=val;
window.close();
}

function fun1()
{

var val=document.all.v2.value;
window.opener.document.all.txt_selection.value=val;
window.close();
}
</SCRIPT>
</HEAD>

<BODY bgcolor='#FFF0CC'>

<form name="dialog">

	<table align="left" width="100%" border=1 cellspacing="1" cellpadding="1">
		<tr>
			<td align="left"  width="50%" ><b><span style="width:100"><input type="radio" name="v1"  value= "BR" onclick="fun();">BORDER </b></td>
            <td align="left" width="50%" ><b>
<input type="radio" name="v2"  value= "IN" onclick="fun1();">INTERNAL</b></td>
			
       </tr>                
</table>


</form>

</BODY>
</html:html>
