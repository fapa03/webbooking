<!--/**
 * @author:  
 * Fecha de Creación: 
 * Compañía: KUMARAN.
 * Descripción del programa: JSP CATALOGO CLIENTES.
 * ------------------------------------------------------------------------------ 
 * MODIFICACIONES:
 * ------------------------------------------------------------------------------
 * Clave: AAP01
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 22/11/2011
 * Descripción: Se modificó la obtencion de datos. ahora sale toda la informacion
 * de la variable de request "destnationAddressRecords" y se le dió formato al codigo.
 * ------------------------------------------------------------------------------ 
 * Clave: AAP02
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 26/07/2012  
 * Descripción: Se le agregó lógica para mostrar la lista de registros sin obtener direcciones 
 * y al seleccionar uno de los registros se ejecuta nueva funcion getRowSelect() para someter la forma del catalogo
 * y obtener solamente la informacion complementaria del registro seleccionado. 
 * Tambien se reacomodó la asignacion de los valores a los objetos de la pantalla de documentacion, para las validaciones de 
 * las sucursales que esten asignadas correctamente.
 * ------------------------------------------------------------------------------
 * Clave: AAP04
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 02/07/2013
 * Descripción: Se agregó variable CM_DEST_PAID_FLAG y objeto para obtener informacion para alimentar variable.
 * ------------------------------------------------------------------------------
 */
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.ArrayList, java.util.HashMap, bean.Global"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link rel=stylesheet media=screen type=text/css href=webbooking.css />
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>

<title>Catalogo de Clientes</title>
</head>
<body onload="initScript()">
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
		oldobject.style.backgroundColor='#fff';
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
		plazaName = (document.getElementById('tablecol4'+rownum));
		plazaSite = (document.getElementById('tablecol5'+rownum));				
		
		if (document.getElementById('branch'+rownum).value == 'null') {
			window.opener.document.forms[0].destinationcode.value = "";
			window.opener.document.forms[0].destinationbranch.value = "";
			window.opener.document.forms[0].destinationcodeIni.value = "";//AAP06
			window.opener.document.forms[0].destinationbranchIni.value = "";//AAP06
			window.opener.document.forms[0].brncVrtl.value = "";			
			alert('La colonia no esta asignada correctamente a la sucursal, favor de seleccionar o asignar otra direccion al cliente destino');
			
			window.opener.document.forms[0].destinationclave.value="";
			window.opener.document.forms[0].destinationnombre.value="";
			window.opener.document.forms[0].destino1.value="";
			window.opener.document.forms[0].destino2.value="";
			window.opener.document.forms[0].destinationcolonia1.value="";
			window.opener.document.forms[0].destinationcolonia2.value="";
			window.opener.document.forms[0].destinationzipcode.value="";
			window.opener.document.forms[0].destinationtelefono.value="";
			window.opener.document.forms[0].destinationsite.value = "";
			window.opener.document.forms[0].destinationsitecode.value = "";
			window.opener.document.forms[0].permiteDestino.value = "";//AAP04
			window.opener.document.forms[0].eMailDestText.value = "";//AAP05
			window.opener.document.forms[0].eMailDestBD.value = "";//AAP05
			window.opener.document.forms[0].destinationrfc.value = "";//AAP05
			window.close();
			
		} else {
			var branchvalue=document.getElementById('branch'+rownum).value.substring(0,3);
			if (plazaSite.innerHTML == branchvalue) {
				window.opener.document.forms[0].destinationnombre.value = obj2.innerText;
				window.opener.document.forms[0].destinationclave.value = obj1.innerText;
				window.opener.document.forms[0].destinationsite.value = plazaName.innerText;
				window.opener.document.forms[0].destinationsitecode.value = plazaSite.innerText;
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
				window.opener.document.forms[0].destinationcodeIni.value = eval("document.forms[0].branch"+rownum).value;//AAP06
				window.opener.document.forms[0].destinationbranchIni.value = eval("document.forms[0].branchname"+rownum).value;//AAP06
				
				window.opener.document.forms[0].permiteDestino.value = document.getElementById('CM_DEST_PAID_FLAG'+rownum).value;//AAP04
				//window.opener.document.forms[0].eMailDestText.value = document.getElementById('AM_MAIL_ID'+rownum).value;//AAP05
				window.opener.document.forms[0].eMailDestBD.value = document.getElementById('AM_MAIL_ID'+rownum).value;//AAP05
				window.opener.document.forms[0].destinationrfc.value = document.getElementById('CM_TAX_ID'+rownum).value;//AAP05
				
				branchvalue = document.getElementById('branch'+rownum).value.substring(3);
				if (branchvalue == '70') {//AAP03
					window.opener.document.forms[0].destinationRefDom.value = document.getElementById('AM_COMT'+rownum).value;//AAP03
					window.opener.document.forms[0].checkRefDir.value = 'Y';//AAP03
					if (eval("document.forms[0].am_phono1"+rownum).value =='') {//AAP03
						window.opener.document.forms[0].checkTelDir.value = 'Y';//AAP03						
					}//AAP03
				}//AAP03
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

/* funcion para someter la forma y enviar la clave del cliente seleccionado a lovaction para que obtenga 
 * la informacion complementaria de este cliente
 *///AAP02
function getRowSelect() {
	/*if (rowidflag=='true') {
		rowindex=rowid.indexOf('w');
		rownum=rowid.substring(rowindex+1,rowid.length);
		obj1 = (document.getElementById('tablecol1'+rownum));
		obj2 = (document.getElementById('tablecol2'+rownum));
		plazaName = (document.getElementById('tablecol4'+rownum));
		plazaSite = (document.getElementById('tablecol5'+rownum));
		window.open('lov.do?type=clientNew&destClave='+obj1.innerHTML+'&destNombre=&destinationsitecode='+plazaSite.innerHTML,'abc','width=700,height=200,top=185,left=140,screenY=200,screenX=100');
	}*/
	getRow();
}

</script>
<form name="fff" >
	<div id="datadiv" style="HEIGHT: 180pt; OVERFLOW: auto; WIDTH: 100%;">
		<table id="tablaDetalle" align="left" width="100%" border='1' cellspacing="1" cellpadding="1" style="" class="tablaDetallev2" >
			<tr class="tablaDetallev2Header">
				<td align="left" width="15%" ><b>Claves</b></td>
				<td align="left"  width="40%" ><b>Cliente Destino</b></td>
				<td align="left"  width="25%" ><b>Clave Int.</b></td>
				<td align="left"  width="20%" ><b>Sucursal Destino</b></td>
	            <td align="left"  width="15%" ><b>Siglas</b></td>
	            <td align="left"  width="20%" ><b>Tipo Cliente</b></td>
	            <td align="left"  width="20%" ><b>Cr&eacute;dito</b></td>
	           	<td align="left"  width="20%" ><b>Colonia</b></td>
	            <td align="left"  width="20%" ><b>Ciudad</b></td>
	            <td align="left"  width="15%" ><b>Cobertura</b></td>
	       </tr>                
<% 		 
	ArrayList addressList = (ArrayList) request.getAttribute("destnationAddressRecords");
	if (addressList.size() == 0) {
%>
			<script language=javascript>
				rowidflag='false';				
			</script>
			<tr>
				<td align="center" colspan=10 width="100%"><font size="5" color="red" >No Records</font></td>
			</tr>
<%
	} else if (addressList.size() == 1) {//AAP02

		//AAP//System.out.println("result.size()=="+addressList.size());
		HashMap addressValues = null;
		
		for (int i=0;i < addressList.size(); i++) {
			addressValues = (HashMap) addressList.get(i);
%>	
			<tr class="tablaDetallev2Body" id="tablerow<%=i%>" bgcolor="#fff" name="t1" style="cursor: pointer;" onclick="changeColor(this)"  >
	   			<td align="left" id="tablecol1<%=i%>" width="15%" class=""><%=addressValues.get("clientId")%></td>
	      		<td align="left" id="tablecol2<%=i%>" width="40%" class=""><%=addressValues.get("clientName")%></td>
				<td align="left" id="tablecol3<%=i%>" width="25%" class=""><%=addressValues.get("PQId") == null ? " " :addressValues.get("PQId")%></td>
				<td align="left" id="tablecol4<%=i%>" width="20%" class=""><%=addressValues.get("siteName")%></td>
				<td align="left" id="tablecol5<%=i%>" width="15%" class=""><%=addressValues.get("siteId")%></td>
				<td align="left" id="tablecol9<%=i%>" width="20%" class=""><%=addressValues.get("CM_CLNT_TYPE")%></td>
				<td align="left" id="tablecol10<%=i%>" width="20%" class=""><%=addressValues.get("CM_CREDIT_STATUS")%></td>
				<td align="left" id="tablecol6<%=i%>" width="20%" class=""><%=addressValues.get("u16") == null ? " ":addressValues.get("u16")%></td>
				<td align="left" id="tablecol7<%=i%>" width="20%" class=""><%=addressValues.get("u14") == null ? " ":addressValues.get("u14")%></td>
				<td align="left" id="tablecol8<%=i%>" width="15%" class=""><%=addressValues.get("branch").equals("null") ? "N/A":addressValues.get("branch")%></td>

				<input type=hidden name="am_addr_styp<%=i%>" id="am_addr_styp<%=i%>" value="<%=addressValues.get("AM_ADDR_STYP")%>" />
				<input type=hidden name="val14<%=i%>" id="val14<%=i%>" value="<%=addressValues.get("u14")%>" />
				<input type=hidden name="val16<%=i%>" id="val16<%=i%>" value="<%=addressValues.get("u16")%>" />
				<input type=hidden name="zipcode<%=i%>" id="zipcode<%=i%>" value="<%=addressValues.get("Zipcode")%>" />			
				<input type=hidden name="am_strt_name<%=i%>" id="am_strt_name<%=i%>" value="<%=addressValues.get("AM_STRT_NAME")%>" />
				<input type=hidden name="am_drnr<%=i%>" id="am_drnr<%=i%>" value="<%=addressValues.get("AM_DRNR")%>" >
				<input type=hidden name="am_phono1<%=i%>" id="am_phono1<%=i%>" value="<%=addressValues.get("AM_PHNO1")%>" >
				<input type=hidden name="am_addr_code<%=i%>" id="am_addr_code<%=i%>" value="<%=addressValues.get("AM_ADDR_CODE")%>" />
				<input type=hidden name="destinationcoloniacode<%=i%>" id="destinationcoloniacode<%=i%>" value="<%=addressValues.get("c16")%>" />
				<input type=hidden name="citycode<%=i%>" id="citycode<%=i%>" value="<%=addressValues.get("c14")%>" />
				<input type=hidden name="branch<%=i%>" id="branch<%=i%>" value="<%=addressValues.get("branch")%>" />
				<input type=hidden name="branchname<%=i%>" id="branchname<%=i%>" value="<%=addressValues.get("branchname")%>" />
				<input type=hidden name="AM_GETY_CODE<%=i%>" id="AM_GETY_CODE<%=i%>" value="<%=addressValues.get("AM_GETY_CODE")%>" />
				<input type=hidden name="AM_GETY_LEVL<%=i%>" id="AM_GETY_LEVL<%=i%>" value="<%=addressValues.get("AM_GETY_LEVL")%>" />
				<input type=hidden name="AM_GETY_TYPE<%=i%>" id="AM_GETY_TYPE<%=i%>" value="<%=addressValues.get("AM_GETY_TYPE")%>" />
				<input type=hidden name="AM_COMT<%=i%>" id="AM_COMT<%=i%>" value="<%=addressValues.get("AM_COMT")%>" />
				<input type=hidden name="CM_DEST_PAID_FLAG<%=i%>" id="CM_DEST_PAID_FLAG<%=i%>" value="<%=addressValues.get("CM_DEST_PAID_FLAG")%>" /><!-- AAP04 -->
				<input type=hidden name="AM_MAIL_ID<%=i%>" id="AM_MAIL_ID<%=i%>" value="<%=addressValues.get("AM_MAIL_ID")%>" /><!-- AAP05 -->
				<input type=hidden name="CM_TAX_ID<%=i%>" id="CM_TAX_ID<%=i%>" value="<%=addressValues.get("CM_TAX_ID")%>" /><!-- AAP05 -->
				
			</tr>             
<%
		}
	} else {//AAP02
		//AAP//System.out.println("result.size()=="+addressList.size());
		HashMap addressValues = null;
		for (int i=0;i < addressList.size(); i++) {
			addressValues = (HashMap) addressList.get(i);
			%>	
			<tr id="tablerow<%=i%>" class="tablaDetallev2Body" name="t1" style="cursor: pointer;" onclick="changeColor(this)">
	   			<td align="left" id="tablecol1<%=i%>" width="15%" class=""><%=addressValues.get("clientId")%></td>
	      		<td align="left" id="tablecol2<%=i%>" width="40%" class=""><%=addressValues.get("clientName")%></td>
				<td align="left" id="tablecol3<%=i%>" width="25%" class=""><%=addressValues.get("PQId") == null ? " " : addressValues.get("PQId")%></td>
				<td align="left" id="tablecol4<%=i%>" width="20%" class=""><%=addressValues.get("siteName")%></td>
				<td align="left" id="tablecol5<%=i%>" width="15%" class=""><%=addressValues.get("siteId")%></td>
				<td align="left" id="tablecol9<%=i%>" width="20%" class=""><%=addressValues.get("CM_CLNT_TYPE")%></td>
				<td align="left" id="tablecol10<%=i%>" width="20%" class=""><%=addressValues.get("CM_CREDIT_STATUS")%></td>
				<td align="left" id="tablecol6<%=i%>" width="20%" class=""><%=addressValues.get("u16") == null ? " ": addressValues.get("u16")%></td>
				<td align="left" id="tablecol7<%=i%>" width="20%" class=""><%=addressValues.get("u14") == null ? " ": addressValues.get("u14")%></td>
				<td align="left" id="tablecol8<%=i%>" width="15%" class=""><%=addressValues.get("branch").equals("null") ? "N/A":addressValues.get("branch")%></td>
				
				<input type=hidden name="am_addr_styp<%=i%>" id="am_addr_styp<%=i%>" value="<%=addressValues.get("AM_ADDR_STYP")%>" />
				<input type=hidden name="val14<%=i%>" id="val14<%=i%>" value="<%=addressValues.get("u14")%>" />
				<input type=hidden name="val16<%=i%>" id="val16<%=i%>" value="<%=addressValues.get("u16")%>" />
				<input type=hidden name="zipcode<%=i%>" id="zipcode<%=i%>" value="<%=addressValues.get("Zipcode")%>" />
				<input type=hidden name="am_strt_name<%=i%>" id="am_strt_name<%=i%>" value="<%=addressValues.get("AM_STRT_NAME")%>" />
				<input type=hidden name="am_drnr<%=i%>" id="am_drnr<%=i%>" value="<%=addressValues.get("AM_DRNR")%>" >
				<input type=hidden name="am_phono1<%=i%>" id="am_phono1<%=i%>" value="<%=addressValues.get("AM_PHNO1")%>" >
				<input type=hidden name="am_addr_code<%=i%>" id="am_addr_code<%=i%>" value="<%=addressValues.get("AM_ADDR_CODE")%>" />
				<input type=hidden name="destinationcoloniacode<%=i%>" id="destinationcoloniacode<%=i%>" value="<%=addressValues.get("c16")%>" />
				<input type=hidden name="citycode<%=i%>" id="citycode<%=i%>" value="<%=addressValues.get("c14")%>" />
				<input type=hidden name="branch<%=i%>" id="branch<%=i%>" value="<%=addressValues.get("branch")%>" />
				<input type=hidden name="branchname<%=i%>" id="branchname<%=i%>" value="<%=addressValues.get("branchname")%>" />
				<input type=hidden name="AM_GETY_CODE<%=i%>" id="AM_GETY_CODE<%=i%>" value="<%=addressValues.get("AM_GETY_CODE")%>" />
				<input type=hidden name="AM_GETY_LEVL<%=i%>" id="AM_GETY_LEVL<%=i%>" value="<%=addressValues.get("AM_GETY_LEVL")%>" />
				<input type=hidden name="AM_GETY_TYPE<%=i%>" id="AM_GETY_TYPE<%=i%>" value="<%=addressValues.get("AM_GETY_TYPE")%>" />
				<input type=hidden name="AM_COMT<%=i%>" id="AM_COMT<%=i%>" value="<%=addressValues.get("AM_COMT")%>" />
				<input type=hidden name="CM_DEST_PAID_FLAG<%=i%>" id="CM_DEST_PAID_FLAG<%=i%>" value="<%=addressValues.get("CM_DEST_PAID_FLAG")%>" /><!-- AAP04 -->
				<input type=hidden name="AM_MAIL_ID<%=i%>" id="AM_MAIL_ID<%=i%>" value="<%=addressValues.get("AM_MAIL_ID")%>" /><!-- AAP05 -->
				<input type=hidden name="CM_TAX_ID<%=i%>" id="CM_TAX_ID<%=i%>" value="<%=addressValues.get("CM_TAX_ID")%>" /><!-- AAP05 -->
			</tr>             
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
<%
	if (addressList.size() > 1) {//AAP02
%>				
				<input type=button class="button2 buttonMedium" value="Aceptar" onclick="getRowSelect()"/>
<%
	} else {//AAP02	
%>				
				<input type=button class="button2 buttonMedium" value="Aceptar" onclick="getRow()"/>
<%
	}	
%>
			</td>
			<td>
				<input type=button class="button2 buttonMedium" value="Cancelar" onclick="javascript:window.close()"/>
			</td>
		</tr>
	</table>
</form>
</body>
</html>