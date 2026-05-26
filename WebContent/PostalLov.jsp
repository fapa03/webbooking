<%@ page language="java" import="java.util.*"%>

<HTML>
<HEAD>
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link rel=stylesheet media=screen type=text/css href=webbooking.css />
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">

<script type="text/javascript" src="js/v2/global.js"></script>

<TITLE>Catálogo de códigos postales</TITLE>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
</HEAD>
<BODY onload=initScript()  >
<script >
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
		oldobject.style.backgroundColor='#ffffff';
	obj.style.backgroundColor='#D0D2C5';
	oldobject=obj;	
	colflag='true';
}
function getRow(){
	if(rowidflag=='true'){
		rowindex=rowid.indexOf('w');
		rownum=rowid.substring(rowindex+1,rowid.length);	
		obj1 = (document.getElementById('tablecol1'+rownum));
		obj2 = (document.getElementById('tablecol2'+rownum));

		var	oldCodigoPostal = window.opener.document.forms[0].codigoPostal.value;


		//Modified My Sam [07-06-2006] ,for fix the error for no. of records is one
		//alert(rownum);
		var noOfRows = 0;
		if(document.forms[0].geCode1.length == undefined){
			noOfRows = 0;
		}else{
			noOfRows = document.forms[0].geCode1.length;
		}
		if(noOfRows == 0){
			window.opener.document.forms[0].geLevl.value = document.forms[0].geLevl1.value;
			window.opener.document.forms[0].geType.value = document.forms[0].geType1.value;
			window.opener.document.forms[0].geCode.value = document.forms[0].geCode1.value;
		}else{
			window.opener.document.forms[0].geLevl.value = document.forms[0].geLevl1[rownum].value;
			window.opener.document.forms[0].geType.value = document.forms[0].geType1[rownum].value;
			window.opener.document.forms[0].geCode.value = document.forms[0].geCode1[rownum].value;
		}

		if(oldCodigoPostal != obj1.innerHTML){
			window.opener.document.forms[0].colonia.value="";
			window.opener.document.forms[0].estado.value="";
			window.opener.document.forms[0].zona.value="";
			window.opener.document.forms[0].municipio.value="";
			window.opener.document.forms[0].pais.value="";
		}

		window.opener.document.forms[0].codigoPostal.value = obj1.innerHTML;
		window.opener.document.forms[0].colonia.value = obj2.innerHTML;


		window.opener.document.forms[0].pDetailsC.value = "1";
		window.opener.document.forms[0].estado.focus();
		window.close();

	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}

</script>
<form name=frm>
<div id="datadiv" style="HEIGHT: 92pt; OVERFLOW: auto; WIDTH: 100%">
	<table align="left" width="100%" border=1 cellspacing="1" cellpadding="1" class="tablaDetallev2 ">
		<tr class="tablaDetallev2Header" >
			<td align="left"  width="50%" ><b><span style="width:100">Codigo Postal</b></td>
			<td align="left"  width="50%" ><b><span style="width:100">Colonia</b></td>
<!-- 			<td align="left"  width="70%" ><b><span style="width:100">ge_levl</b></td>
			<td align="left"  width="70%" ><b><span style="width:100">ge_type</b></td>
			<td align="left"  width="70%" ><b><span style="width:100">ge_code</b></td>
 -->       </tr>                
	<% 
		ArrayList result = (ArrayList) request.getAttribute("postalRecords"); 
		if(result.size()==0){
	%>
			<script language=javascript>
				rowidflag='false';				
			</script>
			<tr class="tablaDetallev2Body">
				<td align="center" colspan=5 width="100%"><font size="5" color="red" >No Records</font></td>
			</tr>
	<%
		}else{
		for(int i=0;i < result.size(); i++){
		HashMap values = (HashMap) result.get(i);
	%>	
		<tr id="tablerow<%=i%>" name=t1 style="CURSOR: hand" onclick="changeColor(this)" class="tablaDetallev2Body">
				<td align="left" id="tablecol1<%=i%>" width="50%"><%=values.get("ZIP_CODE")%></td>
				<td align="left" id="tablecol2<%=i%>" width="50%"><%=values.get("ge_desc")%></td>
				
			<input type=hidden name="geLevl1" value=<%=values.get("ge_levl")%> >
			 <input type=hidden name="geType1" value=<%=values.get("ge_type")%> >
			<input type=hidden name="geCode1" value=<%=values.get("ge_code")%> >

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
