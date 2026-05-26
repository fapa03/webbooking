<%--
	* source      : ServiceLov.jsp
	* purpose     : UI For Additional Service LOV
	* created on  : 16-May-2006
	* version     : 1.0
	*                   
 --%>
<%@ page language="java" import="java.util.ArrayList, java.util.HashMap"%>

<HTML>
<HEAD>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
<TITLE></TITLE>
</HEAD>
<BODY onload=initScript()>
<script >
var colflag='false';
var rowid;
var rowidflag='true';
var oldobject;
var rownum;
var referenceId;

function initScript(){
	try{
		var obj= document.getElementById('tablerow0');		
		changeColor(obj);
		}catch(JSException){
		}
}
function changeColor(obj){	
	rowid=obj.id;
	if(colflag=='true')
		oldobject.style.backgroundColor='#fff';
	obj.style.backgroundColor='#ccc';
	oldobject=obj;	
	colflag='true';
}
function getRow(){
	if(rowidflag=='true'){
		rowindex=rowid.indexOf('w');	
		rownum=rowid.substring(rowindex+1,rowid.length);	
		//obj1 = (document.getElementById('tablecol1'+rownum));
		obj1 = (document.getElementById('service'+rownum));
		obj2 = (document.getElementById('tablecol2'+rownum));
		obj3 = (document.getElementById('tablecol3'+rownum));
		
		var edit = document.getElementById('WT_SRVC_EDIT'+rownum).value;
		
		//window.opener.document.forms[0].serviceAdditional.value = obj1.innerHTML;
		window.opener.document.forms[0].serviceAdditional.value = obj1.value;
		window.opener.document.forms[0].referenceId.value = obj2.innerHTML;
		window.opener.document.forms[0].importeValue.value = obj3.innerHTML;
		if (edit == 'Y') {
			window.opener.document.forms[0].importeValue.readOnly = false;
			window.opener.document.forms[0].importeValue.className = "textGreenBG";
			window.opener.document.forms[0].importeValue.focus();
		} else {
			window.opener.document.forms[0].importeValue.readOnly = true;
			window.opener.document.forms[0].importeValue.className = "text";
			
		}
		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}

</script>
<form name=frm>
<div id="datadiv"  HEIGHT: 92pt; OVERFLOW: auto; WIDTH: 100%">
	<table align="left" class="tablaDetallev2" width="100%" border=1 cellspacing="1" cellpadding="1">
		<tr>
			<td align="left" width="20%" ><b>Servicios Adicionales</b></td>
			<td align="left" width="20%" ><b>Referencia</b></td>
			<td align="left" width="40%" ><b>Importe</b></td>
       </tr>                
	<% 
		ArrayList result = (ArrayList) request.getAttribute("serviceRecords"); 
		if(result.size()==0){
	%>
			<script language=javascript>
				rowidflag='false';				
			</script>
			<tr>
				<td align="center" colspan=2 width="80%"><font size="5" color="red" >No Records</font></td>
			</tr>
	<%
		}else{
		for(int i=0;i < result.size(); i++){
		HashMap values = (HashMap) result.get(i);
	%>	
		<tr id="tablerow<%=i%>" name=t1 style="CURSOR: hand" onclick="changeColor(this)">
				<td align="left" id="tablecol1<%=i%>" width="40%"><%=values.get("NOMBRE")%>
				<input type="hidden" id="service<%=i%>" value = "<%=values.get("WT_SRVC_ID")%>"/>
				<input type="hidden" id="WT_SRVC_EDIT<%=i%>" value = "<%=values.get("WT_SRVC_EDIT")%>"/></td>
				<td align="left" id="tablecol2<%=i%>" width="30%"><%=values.get("WT_REFR_SRVC_ID")%></td>
				<td align="left" id="tablecol3<%=i%>" width="30%"><%=values.get("WT_TRIF_AMT")%></td>
		</tr>             
     <%
			}
		}
     %>               		
	</table>
</div>
<table width=20% align=center>
<tr>
<td>
<input type=button class="button1 buttonMedium" value="Aceptar" onclick="getRow()">
</td>
<td>
<input type=button class="button1 buttonMedium" value="Cancelar" onclick="javascript:window.close()">
</td>
</tr>
</form>
</BODY>
