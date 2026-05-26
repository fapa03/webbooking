<%@ page language="java" import="java.util.*"%>

<HTML>
<HEAD>
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<link rel=stylesheet media=screen type=text/css href=webbooking.css />
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">

<script type="text/javascript" src="js/v2/global.js"></script>

<TITLE>Catálogo de ciudades</TITLE>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
</HEAD>
<BODY onload=initScript() >
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
	obj.style.backgroundColor='#D0D2C5 ';
	oldobject=obj;	
	colflag='true';
}

function getRow(){

	if(rowidflag=='true'){

		rowindex=rowid.indexOf('w');
		rownum=rowid.substring(rowindex+1,rowid.length);	
		obj1 = (document.getElementById('tablecol1'+rownum));
		
		//Added By Sam[07-06-2006] for getting Postal Code based on the  code of City
		obj2 = (document.getElementById('tablecol2'+rownum));

		//Added By Sam[08-06-2006] 
		var	oldCode = window.opener.document.forms[0].code.value;
		var	oldType = window.opener.document.forms[0].type.value;

		window.opener.document.forms[0].ciudad.value	= obj1.innerHTML;

		//Added By Sam[07-06-2006] for getting Postal Code based on the  code of City
		window.opener.document.forms[0].code.value		= obj2.innerHTML;
		
		//Added By Sam[07-06-2006] for getting Postal Code based on the  Type of City
		window.opener.document.forms[0].type.value		= document.forms[0].geType1[rownum].value;

		if(!(oldCode == obj2.innerHTML && oldType == document.forms[0].geType1[rownum].value)){
			window.opener.document.forms[0].colonia.value="";
			window.opener.document.forms[0].codigoPostal.value="";
			window.opener.document.forms[0].estado.value="";
			window.opener.document.forms[0].zona.value="";
			window.opener.document.forms[0].municipio.value="";
			window.opener.document.forms[0].pais.value="";
		}

		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}

</script>
<form name=frm>
<div id="datadiv" style=" HEIGHT: 92pt; OVERFLOW: auto; WIDTH: 100%">
	<table align="left" width="100%" border=1 cellspacing="1" cellpadding="1" class="tablaDetallev2 ">
		<tr class="tablaDetallev2Header">
			<td align="left"  width="30%" ><b><span style="width:100">Ciudad</b></td>
            <td align="left" width="15%" ><b>Clave</b></td>
			<td align="left" width="50%" ><b>Estado</b></td>
       </tr>                
	<% 
		ArrayList result = (ArrayList) request.getAttribute("citiesRecords"); 
		if(result.size()==0){
	%>
			<script language=javascript>
				rowidflag='false';				
			</script>
			<tr class="tablaDetallev2Body">
				<td align="center" colspan=3 width="100%"><font size="5" color="red" >No Records</font></td>
			</tr>
	<%
		}else{
		for(int i=0;i < result.size(); i++){
		HashMap values = (HashMap) result.get(i);
	%>	
		<tr id="tablerow<%=i%>" name=t1 style="CURSOR: hand" onclick="changeColor(this)" class="tablaDetallev2Body">
				<td align="left" id="tablecol1<%=i%>" width="30%"><%=values.get("GE_DESC")%></td>
				<td align="left" id="tablecol2<%=i%>" width="15%"><%=values.get("GE_CODE")%></td>
				<td align="left" id="tablecol3<%=i%>" width="50%"><%=values.get("ESTADO")%></td>
				<input type=hidden name="geType1" value=<%=values.get("GE_TYPE")%> >
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
