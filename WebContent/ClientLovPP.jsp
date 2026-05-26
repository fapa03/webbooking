<%@ page language="java" import="java.util.*"%>

<HTML>
<HEAD>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<TITLE></TITLE>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
</HEAD>
<BODY onload=initScript() class="backgroundStandard" >
<script>
var colflag='false';
var rowid;
var rowidflag='true';
var oldobject;
var rownum;
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
		oldobject.style.backgroundColor='#FFF';
	obj.style.backgroundColor='#CCC';
	oldobject=obj;	
	colflag='true';
}
function getRow(){
	
	if(rowidflag=='true'){
		rowindex=rowid.indexOf('w')	
		rownum=rowid.substring(rowindex+1,rowid.length);	
		obj1 = (document.getElementById('tablecol1'+rownum))
		obj2 = (document.getElementById('tablecol2'+rownum))	
		
		//if(document.getElementByName('destclave'))
		if(window.opener.document.forms[0].destinationclave!=null)
		{
		
		window.opener.document.forms[0].destinationclave.value = obj1.innerText;
		window.opener.document.forms[0].destinationnombre.value = obj2.innerText;
		}
		else if(window.opener.document.forms[0].destclave!=null)
		{
			window.opener.document.forms[0].destclave.value=obj1.innerText;
			window.opener.document.forms[0].destnombre.value=obj2.innerText;
			
			try{
				window.opener.document.forms[0].destClaveAux.value = obj1.innerText;
				window.opener.document.forms[0].destNombreAux.value=obj2.innerText;
			}catch(_){}
			

		}
		
		else if(window.opener.document.forms[0].clientName!=null)
		{
		
		window.opener.document.forms[0].clientName.value = obj2.innerText;
		
		window.opener.document.forms[0].clientId.value = obj1.innerText;
				
		}		
		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}

</script>
<form name=frm>
<div id="datadiv" 
	style=" HEIGHT: 149pt; OVERFLOW: auto; WIDTH: 540">
<table align="left" width="520" border=1 cellspacing="0" cellpadding="0" class="tablaDetallev2Header" >
	<tr width="520">
		<td align="left" width="450"><b>Cliente</b></td>
		<td align="left" width="30"><b>Cliente</b></td>
	</tr>
	<% 
		ArrayList result = (ArrayList) request.getAttribute("clientRecords"); 
		if(result.size()==0){
	%>
	<script language=javascript>
				rowidflag='false';				
			</script>
	<tr>
		<td align="center" colspan=2 width="550"><font size="5"
			color="red">No Records</font></td>
	</tr>
	<%
		}else{
		for(int i=0;i < result.size(); i++){
		HashMap values = (HashMap) result.get(i);
	%>
	<tr id="tablerow<%=i%>" name=t1 style="CURSOR: hand"
		onclick="changeColor(this)">
		<td align="left" id="tablecol2<%=i%>" width="75%"><%=values.get("clientName")%></td>
		<td align="left" id="tablecol1<%=i%>" width="25%"><%=values.get("clientId")%></td>
	</tr>
	<%
			}
		}
     %>
</table>

</div>
<table width="20%" align="center">
	<tr>
		<td><input type=button class="button1 buttonMedium" value="Aceptar"
			onclick="getRow()"></td>
		<td><input type=button class="button1 buttonMedium" value="Cancelar"
			onclick="javascript:window.close()"></td>
	</tr>
</table>
</form>
</BODY>