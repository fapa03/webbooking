<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="java.util.ArrayList, java.util.HashMap, bean.Global"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Catalogo de Clientes</title>
<style type="text/css">
td,th {
	color: #363636;
	padding: .4em;
}
tr {
	border: 1px dotted gray;
}
thead th,tfoot th {
	background: #5C443A;	
	color: #FFFFFF;
	padding: 3px 10px 3px 10px;
	text-align: left;
	text-transform: uppercase;
}
tbody th,tbody td {
	text-align: left;
	vertical-align: top;
}
tfoot td {
	background: #5C443A;
	color: #FFFFFF;
	padding-top: 3px;
}
.odd {
	background: #fff;
}
.tBodytr:hover {
	background: #99BCBF;
	border: 1px solid #03476F;
	color: #000000;
}
.tableDetail {
	background: #D3E4E5;
	border: 1px solid gray;
	border-collapse: collapse;
	color: #fff;
	font: normal 12px verdana, arial, helvetica, sans-serif;
}
</style>
</head>
<body onload = "initScript()">
<script >
var colflag='false';
var rowid;
var rowidflag='true';
var oldobject;
var oldBackgroundColor;
var oldColor;
var rownum;
function initScript() {
	try {
		var obj= document.getElementById('tablerow0');	
		oldBackgroundColor = document.getElementById('tablerow0').style.backgroundColor;
		oldColor = document.getElementById('tablerow0').style.color;
		changeColor(obj);
		getRowInit();
	} catch(JSException) {
	}
}

function changeColor(obj) {
	rowid=obj.id;
	
	if(colflag=='true') {
		//oldobject.style.backgroundColor='#cedee9';
		
		oldobject.style.backgroundColor = oldBackgroundColor;
		oldobject.style.color = oldColor;
	}
	
	oldBackgroundColor = obj.style.backgroundColor;
	oldColor = obj.style.color;
	
	obj.style.backgroundColor='#FFF0CC';
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
		plazaName = (document.getElementById('tablecol4'+rownum));
		plazaSite = (document.getElementById('tablecol5'+rownum));
		
		window.opener.document.forms[0].destinationnombre.value = obj2.innerHTML;
		window.opener.document.forms[0].destinationclave.value = obj1.innerHTML;
		window.opener.document.forms[0].destinationsite.value = plazaName.innerHTML;
		window.opener.document.forms[0].destinationsitecode.value = plazaSite.innerHTML;
		window.opener.document.forms[0].destinationzipcode.value = document.getElementById('zipcode'+rownum).value;
		window.opener.document.forms[0].destinationaddresscode.value = document.getElementById('am_addr_code'+rownum).value;
		window.opener.document.forms[0].destinationcoloniacode.value = document.getElementById('destinationcoloniacode'+rownum).value;
		window.opener.document.forms[0].citycode.value = document.getElementById('citycode'+rownum).value;
		window.opener.document.forms[0].getycode.value = document.getElementById('AM_GETY_CODE'+rownum).value;
		window.opener.document.forms[0].getylevl.value = document.getElementById('AM_GETY_LEVL'+rownum).value;
		window.opener.document.forms[0].getytype.value = document.getElementById('AM_GETY_TYPE'+rownum).value;
		window.opener.document.forms[0].destinationcolonia1.value = eval("document.forms[0].val16"+rownum).value;
		window.opener.document.forms[0].destinationcolonia2.value = eval("document.forms[0].val14"+rownum).value;
		window.opener.document.forms[0].destino1.value = eval("document.forms[0].am_strt_name"+rownum).value;
		window.opener.document.forms[0].destino2.value = eval("document.forms[0].am_drnr"+rownum).value;
		window.opener.document.forms[0].destinationtelefono.value = eval("document.forms[0].am_phono1"+rownum).value;
		window.opener.document.forms[0].destinationcode.value = eval("document.forms[0].branch"+rownum).value;
		window.opener.document.forms[0].destinationbranch.value = eval("document.forms[0].branchname"+rownum).value;		
		
		if (document.getElementById('branch'+rownum).value == 'null') {
			window.opener.document.forms[0].destinationcode.value = "";
			window.opener.document.forms[0].destinationbranch.value = "";
			alert('La colonia no esta asignada correctamente a la sucursal, favor de seleccionar o asignar otra direccion al cliente destino');
			
			window.opener.document.forms[0].destinationclave.value="";
			window.opener.document.forms[0].destinationnombre.value="";
			window.opener.document.forms[0].destino1.value="";
			window.opener.document.forms[0].destino2.value="";
			window.opener.document.forms[0].destinationcolonia1.value="";
			window.opener.document.forms[0].destinationcolonia2.value="";
			window.opener.document.forms[0].destinationzipcode.value="";
			window.opener.document.forms[0].destinationtelefono.value="";
			window.close();
			
		} else {
			var branchvalue=document.getElementById('branch'+rownum).value.substring(0,3);
			if (plazaSite.innerHTML == branchvalue) {
				//pendiente enviar rfc destino
				opener.callForm('thispage');
				window.close();				
			} else {
				alert('La sucursal no esta asignada correctamente a la direccion del cliente, favor de seleccionar o asignar otra direccion al cliente destino');
				window.close();
			}
		}
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}	
}

</script>
<form name="fff" >
	<div id="datadiv" style="height: 130pt; overflow: auto; width: 100%">
		<table id="tablaDetalle" class="tableDetail"  align="left" width="100%"  cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<th align="left" width="20%" ><b>Clave</b></th>
					<th align="left"  width="30%" ><b>Cliente Destino</b></th>
					<th align="left"  width="30%" ><b>Clave Int.</b></th>
					<th align="left"  width="30%" ><b>Sucursal Destino</b></th>
		            <th align="left"  width="20%" ><b>Siglas</b></th>	       
		       </tr>   
	       </thead> 
	       <tbody>            
<%
		ArrayList addressList = (ArrayList) request.getAttribute("destnationAddressRecords");
		String estilo = "odd tBodytr";
		//AAP//System.out.println("result.size()=="+addressList.size());
		HashMap addressValues = null;
		for(int i=0;i < addressList.size(); i++){
			addressValues = (HashMap) addressList.get(i);
%>	
				<tr id="tablerow<%=i%>" onclick="changeColor(this)" class="<%=estilo%>">
		   			<td align="left" id="tablecol1<%=i%>" width="20%"><%=addressValues.get("clientId")%></td>
		      		<td align="left" id="tablecol2<%=i%>" width="35%"><%=addressValues.get("clientName")%></td>
					<td align="left" id="tablecol3<%=i%>" width="30%"><%=addressValues.get("PQId")%></td>
					<td align="left" id="tablecol4<%=i%>" width="30%"><%=addressValues.get("siteName")%></td>
					<td align="left" id="tablecol5<%=i%>" width="20%"><%=addressValues.get("siteId")%></td>						
				</tr>             
<%
			if ("odd tBodytr".equals(estilo)) {
				estilo = "tBodytr";
			} else {
				estilo = "odd tBodytr";	
			}
		}
%>       
			</tbody>
		</table>
	</div>
	<table width=20% align=center>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td>
				<input type=button class="" value="Aceptar" onclick="getRow()"/>
			</td>
			<td>
				<input type=button class="" value="Cancelar" onclick="window.close()"/>
			</td>
		</tr>
	</table>
</form>
</body>
</html>