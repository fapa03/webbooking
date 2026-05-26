<%@ page language="java" import="java.util.ArrayList,java.util.HashMap"%>

<HTML>
<HEAD>
<link rel=stylesheet media=screen type=text/css href=webbookingMonitor.css>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>

<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />

<TITLE>Sucursal a Asignar</TITLE>
</HEAD>
<BODY onload=initScript() class="backgroundStandard">

<script>
function centrarVentana() {
	window.moveTo((screen.availWidth - document.body.clientWidth) / 2, (screen.availHeight - document.body.clientHeight) / 2);
}
var colflag='false';
var rowid;
rowidflag='true';
var oldobject;
var rownum;
function initScript(){
	try{
		var obj= document.getElementById('tablerow0');		
		changeColor(obj);
		centrarVentana();
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
		obj1 = (document.getElementById('tablecol1'+rownum));
		obj2 = (document.getElementById('tablecol2'+rownum));

		var oldSelectedDestinationcode = window.opener.document.forms[0].cveSucursalAsig.value;

		window.opener.document.forms[0].cveSucursalAsig.value = obj1.innerHTML;
		window.opener.document.forms[0].desSucursalAsig.value = obj2.innerHTML;
		
		if(oldSelectedDestinationcode!=obj1.innerHTML){
			window.opener.document.forms[0].cveClienteAsig.value="";
			window.opener.document.forms[0].desClienteAsig.value="";
			window.opener.document.forms[0].cveUserAsig.value="";
			window.opener.document.forms[0].desUserAsig.value="";
		}
		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}
</script>
<form name=frm>
<div id="datadiv"
	style="OVERFLOW: auto; WIDTH: 100%; HEIGHT: 112pt">
<table align="left" width="100%" class="tablaDetallev2 marginAutoCentro tablaDetallev2Header" >
	<tr >
		<th align="left" width="60%"><b>Sucursal</b></th>
		<th align="left" width="20%"><b>Siglas</b></th>
	</tr>

	<%
		String selectedBranch = request.getParameter("selectedBranch");		
	%>
	<script language=javascript>
		oldSelectedBranch='<%= selectedBranch %>';
	</script>

	<%

		ArrayList result = (ArrayList) request.getAttribute("branchRecords");
		if(result.size()==0){
	%>
	<script language=javascript>
				rowidflag='false';				
			</script>
	<tr class="tablaDetallev2Body">
		<td align="center" colspan=2 width="80%"><font size="5"
			color="red">No Records</font></td>
	</tr>
	<%
		}else{
			for(int i=0;i < result.size(); i++){
			HashMap values = (HashMap) result.get(i);
	%>
	<tr id="tablerow<%=i%>" name=t1 style="CURSOR: hand" onclick="changeColor(this)" class="tablaDetallev2Body">
		<td align="left" id="tablecol2<%=i%>" width="60%"><%=values.get("siteName")%></td>
		<td align="left" id="tablecol1<%=i%>" width="20%"><%=values.get("siteId")%></td>
	</tr>
	<%
     	}
     }
     %>
</table>
</div>
<table width=20% align=center>
	<tr>
		<td><input type=button class="button1 buttonMedium" value="Aceptar"
			onclick="getRow()"></td>
			<td>&nbsp;</td>
		<td><input type=button class="button1 buttonMedium" value="Cancelar"
			onclick="javascript:window.close()"></td>
	</tr>
	</form>
</BODY>