<!DOCTYPE>
<%@ page language="java" import="java.util.ArrayList,java.util.HashMap, bean.ShipmentServiceDetail"%> 

<HTML lang="es">
<HEAD>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<TITLE></TITLE>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
</HEAD>
<BODY onload=initScript() oncontextmenu="return false">
<script >
var descArray = new Array();
var tariffArray = new Array();
<%	
	String action = request.getParameter("action");
	String index = request.getParameter("index");
	
	String pesoConvenio = request.getAttribute("pesoConvenio") == null ? "0" : request.getAttribute("pesoConvenio").toString();
	String voluConvenio = request.getAttribute("voluConvenio") == null ? "0" : request.getAttribute("voluConvenio").toString();
    
	int intIndex=-1;
	if(index!=null)
		intIndex=Integer.parseInt(index);
	
	ArrayList shipDetail = (ArrayList)session.getAttribute("servicesDetail");
	
	ArrayList descArray = new ArrayList();
	if (shipDetail != null){
		for(int i=0;i<shipDetail.size();i++){
			ShipmentServiceDetail ssd =   (ShipmentServiceDetail)shipDetail.get(i);
			//System.out.println("DESCRIPTION "+ssd.descripcion);
			//System.out.println("TARIFA "+ssd.tarifa);
			if(action!=null && action.equals("edit")){
				if(i==intIndex)
					continue;
			}
%>
		
			descArray[<%=i%>] = "<%= ssd.descripcion %>";
			tariffArray[<%=i%>] = "<%= ssd.tarifa %>";
		
<%
		}
	}
%>
var colflag='false';
var rowid;
var rowidflag='true';
var oldobject;
var rownum;
var pesoTarif = 0;


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
	obj.style.backgroundColor='#D0D2C5';
	oldobject=obj;	
	colflag='true';
}
function getRow(){
	var voluTarif = 0;
	var pesoConvenio = <%=pesoConvenio%>;
	var voluConvenio = <%=voluConvenio%>;
	if(rowidflag=='true'){
		rowindex=rowid.indexOf('w');
		rownum=rowid.substring(rowindex+1,rowid.length);
		tariff1 = (document.getElementById('tablecol1'+rownum));
		description = window.opener.document.forms[0].descripcion.value;
		/*if (!emptyTableCheck()){
			for(var i=0;i<descArray.length;i++){
				if(description == descArray[i] && tariff1.innerHTML==tariffArray[i]){
					alert('EL SERVICIO #1 YA ESTA REGISTRADO');
					return;
				}
			}	
		}*/
		
		if (tableDetailExist(description,tariff1)){
			alert('EL SERVICIO #1 YA ESTA REGISTRADO');
			return;
		}
		window.opener.document.forms[0].tarifa.value = tariff1.innerHTML;
		if (window.opener.document.forms[0].isShippingTypeSEG.value == 'N' && (new Number(window.opener.document.forms[0].weightVolumetric.value) > 0)) {
			window.opener.document.forms[0].weightVolumetric.value = "";
			window.opener.document.forms[0].volL.value = "";
			window.opener.document.forms[0].volH.value = "";
			window.opener.document.forms[0].volW.value = "";
		}
		if(window.opener.document.forms[0].ss_refr_srvc_id.value == "PACKETS" && 
				window.opener.document.forms[0].specialTariff.value.length > 0 &&
				window.opener.document.forms[0].clasifTarif.value == "0") {//AAP01
			window.opener.document.forms[0].peso.readOnly = true;
			window.opener.document.forms[0].volumen.readOnly = true;
		} else {
			if (document.forms[0].numReg.value == 1) {
				window.opener.document.forms[0].peso.value=document.forms[0].wt.value;
				window.opener.document.forms[0].volumen.value=document.forms[0].vol.value;
				
				pesoTarif = document.forms[0].wt.value;
				voluTarif = document.forms[0].vol.value;
			} else {
				window.opener.document.forms[0].peso.value=document.forms[0].wt[rownum].value;
				window.opener.document.forms[0].volumen.value=document.forms[0].vol[rownum].value;
				
				pesoTarif = document.forms[0].wt[rownum].value;
				voluTarif = document.forms[0].vol[rownum].value;
			}
			if(tariff1.innerHTML!='T7') {
				window.opener.document.forms[0].peso.readOnly = true;
				window.opener.document.forms[0].volumen.readOnly = true;
			}else{				
				window.opener.document.forms[0].peso.readOnly = false;
				window.opener.document.forms[0].volumen.readOnly = false;
				
				/*asignacion de peso y volumen de convenio para T7 tarifa A*/
				if (window.opener.document.forms[0].defaultservicescreen.value == "yes" && window.opener.document.forms[0].clasifTarif.value == "1") {					
					if (parseFloat(pesoConvenio)>parseFloat(pesoTarif)) {
						window.opener.document.forms[0].peso.value = pesoConvenio;
					}
					
					if (parseFloat(voluConvenio)>parseFloat(voluTarif)) {
						window.opener.document.forms[0].volumen.value = voluConvenio;
					}
				}
			}
		}
		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}

function tableDetailExist(description, tariff) {
	var detailExist = false;
    var table = window.opener.document.getElementById("tablaDetalle");
    for (var r = 1, n = table.rows.length; r < n; r++) {
        if (description == table.rows[r].cells[1].innerText &&
        		tariff == table.rows[r].cells[2].innerText){
        	detailExist = true;
        }
    }
    return detailExist;
}
</script>
<form name=frm>
<%
		ArrayList result = (ArrayList) request.getAttribute("tariffRecords");
		for(int i=0;i < result.size(); i++){
			HashMap values = (HashMap) result.get(i);
%>
			<input type=hidden name="wt" value="<%= values.get("WT") %>">
			<input type=hidden name="vol" value="<%= values.get("VOL") %>">
<%
	}
%>
<div>
	<h3>Seleccione tarifa</h3>
</div>
<div id="datadiv" style="HEIGHT: 92pt; OVERFLOW: auto; WIDTH: 100%">
	<table align="left" width="100%" border=1 cellspacing="1" cellpadding="1" class="tablaDetallev2" > 
		<caption hidden="hidden">Catalogo de tarifas</caption>
	<% 
		int numReg = result.size(); 
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
		<tr id="tablerow<%=i%>" name=t1 style="CURSOR: pointer;" onclick="changeColor(this)">
			<td align="left" id="tablecol1<%=i%>" width="15%"><%=values.get("TRIF1")%></td>
			<td align="left" id="tablecol2<%=i%>" width="15%"><%=values.get("FACT1")%></td>
			<td align="left" id="tablecol3<%=i%>" width="25%"><%=values.get("WT2")%></td>
			<td align="left" id="tablecol4<%=i%>" width="20%"><%=values.get("FACT2")%></td>
			<td align="left" id="tablecol4<%=i%>" width="25%"><%=values.get("VOL2")%></td>			
       </tr>
     <%
			}
		}
     %>               		
	</table>
</div>
<table width=20% align=center>
<caption hidden="hidden">Botones</caption>
<tr>
<td>
<input type=button class="button1 buttonMedium" value="Aceptar" onclick="getRow()">
</td>
<td>
<input type=button class="button1 buttonMedium" value="Cancelar" onclick="javascript:window.close()">
<input type="hidden" name="numReg" id="numReg" value="<%=numReg%>"/>
</td>
</tr>
</form>
</BODY>
</html>