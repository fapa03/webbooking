<!--/**
 * @author:  
 * Fecha de Creación: 21/02/2012
 * Compañía: KUMARAN.
 * Descripción del programa: JSP CATALOGO CLIENTES (opción registro clientes destino).
 * ------------------------------------------------------------------------------ 
 * MODIFICACIONES:
 * ------------------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 
 * 
 * ------------------------------------------------------------------------------ 
 * Clave: 
 * Autor: 
 * Fecha:  
 * Descripción: 
 * ------------------------------------------------------------------------------
 */
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.ArrayList, bean.Global"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<link rel=stylesheet media=screen type=text/css href=webbooking.css />
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">

<script type="text/javascript" src="js/v2/global.js"></script>

<title>Catalogo de Clientes</title>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
</head>
<body onload="initScript()" >
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
		getRowInit();
	}catch(JSException){
	}
}

function changeColor(obj) {
	rowid=obj.id;
	if(colflag=='true') {
		oldobject.style.backgroundColor='#ffffff';
	}
	obj.style.backgroundColor='#D0D2C5';
	oldobject=obj;
	colflag='true';
}

function getRowInit() {
	var tabla = document.getElementById('tablaDetalle');
	var reng = tabla.rows;
	if (reng.length==2) {
		getRow();
	}
}

function getRow() {
	if (rowidflag=='true') {
		rowindex=rowid.indexOf('w');
		rownum=rowid.substring(rowindex+1,rowid.length);
		obj1 = (document.getElementById('tablecol1'+rownum));
		obj2 = (document.getElementById('tablecol2'+rownum));
		if (obj1.innerHTML=="ERROR"){
			opener.showToast("info",obj2.innerHTML, 6000);
			window.close();
			return;
		}
		obj3 = (document.getElementById('tablecol3'+rownum));
		plazaName = (document.getElementById('tablecol4'+rownum));
		plazaSite = (document.getElementById('tablecol5'+rownum));
		
		window.opener.document.forms[0].nombre.value = obj2.innerHTML;
		window.opener.document.forms[0].codigoCliente.value = obj1.innerHTML;
		window.opener.document.forms[0].codigoInt.value = obj3.innerHTML;
		/*se ejecuta funcion que carga el detalle del cliente, para editar*/
		opener.loaddetail();
		window.close();
	} else {
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}

</script>
<form name="fff" >
	<div id="datadiv" style="HEIGHT: 100pt; OVERFLOW: auto; WIDTH: 100%">
		<table id="tablaDetalle" align="left" width="100%" border='1' cellspacing="1" cellpadding="1" class="tablaDetallev2 ">
			<tr class="tablaDetallev2Header">
				<td align="left" width="20%" ><b>Clave</b></td>
				<td align="left"  width="30%" ><b>Cliente Destino</b></td>
				<td align="left"  width="30%" ><b>Clave Int.</b></td>
				<td align="left"  width="30%" ><b>Sucursal Destino</b></td>
	            <td align="left"  width="20%" ><b>Siglas</b></td>	       
	       </tr>                
<% 		 
	ArrayList addressList = (ArrayList) request.getAttribute("clientesDest");
	if(addressList.size()== 0){
%>
			<script language=javascript>
				rowidflag='false';				
			</script>
			<tr class="tablaDetallev2Body">
				<td align="center" colspan=5 width="100%"><font size="5" color="red" >No Records</font></td>
			</tr>
<%
	}else{

		for(int i=0;i < addressList.size(); i++) {
%>	
			<tr id="tablerow<%=i%>" style="CURSOR: hand" onclick="changeColor(this)" class="tablaDetallev2Body">
	   			<td align="left" id="tablecol1<%=i%>" width="20%"><%=((ArrayList) addressList.get(i)).get(0).toString()%></td>
	      		<td align="left" id="tablecol2<%=i%>" width="35%"><%=((ArrayList) addressList.get(i)).get(1).toString()%></td>
				<td align="left" id="tablecol3<%=i%>" width="30%"><%=((ArrayList) addressList.get(i)).get(2).toString()%></td>
				<td align="left" id="tablecol4<%=i%>" width="30%"><%=((ArrayList) addressList.get(i)).get(3).toString()%></td>
				<td align="left" id="tablecol5<%=i%>" width="20%"><%=((ArrayList) addressList.get(i)).get(4).toString()%></td>				
<%
		}
	}
%>       
		</table>
	</div>
	<table width=20% align=center>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td>
				<input type=button class="button2 buttonMedium" value="Aceptar" onclick="getRow()"/>
			</td>
			<td>
				<input type=button class="button2 buttonMedium" value="Cancelar" onclick="window.close()"/>
			</td>
		</tr>
	</table>
</form>
</body>
</html>