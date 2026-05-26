<%@ page language="java" import="java.util.ArrayList, java.util.HashMap,bean.ShipmentServiceDetail"%>

<html>
<head>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
<title></title>
</HEAD>
<BODY onload=initScript()>
<script type="text/javascript">
var descArray = new Array();
<%
	String strTariffExists = "";//AAP01
	String action = request.getParameter("action");
	String index = request.getParameter("index");
	String isNewBooking = request.getParameter("isNewBooking")==null?"":request.getParameter("isNewBooking");//AAP01
	String clasifTarif = request.getParameter("clasifTarif")==null?"":request.getParameter("clasifTarif");//AAP02
	
	if (isNewBooking.equals("Y") ) {//AAP01
		strTariffExists = request.getParameter("defaultservicescreen");
	} else {//AAP01
		strTariffExists = (String) session.getAttribute("defaultservicescreen");
	}

	int intIndex=-1;
	if(index!=null) {
		intIndex=Integer.parseInt(index);
	}
	boolean tariffExists = false;
	if(strTariffExists !=null && strTariffExists.equalsIgnoreCase("yes")) {
		tariffExists=true;	
	}
	
	ArrayList shipDetail=null;
	if (tariffExists) {
		if (isNewBooking.equals("Y") ) {//AAP01
			shipDetail = (ArrayList)session.getAttribute("servicesDetail");//AAP01
		} else {//AAP01
			shipDetail = (ArrayList)session.getAttribute("servicesDetailDefault");//AAP01
		}//AAP01
	} else {
		shipDetail = (ArrayList)session.getAttribute("servicesDetail");
	}
	
	if (tariffExists && clasifTarif.equals("0")) {
		ArrayList descArray = new ArrayList();
		for(int i=0;i<shipDetail.size();i++){
			ShipmentServiceDetail ssd =   (ShipmentServiceDetail)shipDetail.get(i);
			if(action!=null && action.equals("edit")) {
				if(i==intIndex) {
					continue;
				}
			}
		}
	}
	
%>
var colflag = 'false';
var rowid;
rowidflag='true';
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
		oldobject.style.backgroundColor='#fff';
	obj.style.backgroundColor='#D0D2C5';
	oldobject=obj;	
	colflag='true';
}
function getRow(){
	if(rowidflag=='true') {
		rowindex = rowid.indexOf('w');
		rownum = rowid.substring(rowindex+1,rowid.length);	
		obj1 = document.getElementById('tablecol1'+rownum);
		obj2 = document.getElementById('tablecol2'+rownum);
		
		for(var i=0;i<descArray.length;i++){
			//alert('value '+descArray[i]);
			if(obj2.innerHTML==descArray[i]){
				alert('NO SE PUEDE DUPLICAR LA DESCRIPCION');
				return;
			}
		}
		window.opener.document.forms[0].descripcion.value = obj2.innerHTML;
		window.opener.document.forms[0].descripcioncode.value=obj1.innerHTML;
		window.opener.document.forms[0].ss_srvc_id.value=document.forms[0].ss_srvc_id[rownum].value;
		window.opener.document.forms[0].ss_refr_srvc_id.value=document.forms[0].ss_refr_srvc_id[rownum].value;		
<%
	if (!tariffExists) {
%>			
		if (oldSelectedDescription != obj2.innerHTML) {
			window.opener.document.forms[0].tarifa.value = '';
			window.opener.document.forms[0].peso.value = '';
			window.opener.document.forms[0].volumen.value = '';
		}
<%
	}
%>
		if (document.forms[0].ss_srvc_id[rownum].value=='SHP-E') {
			window.opener.document.forms[0].peso.value='1';
			window.opener.document.forms[0].volumen.value='0';
			window.opener.document.forms[0].volumen.readOnly=true;
		} else {
			if (window.opener.document.forms[0].isShippingTypeSEG.value == 'N') {
				window.opener.document.forms[0].peso.readOnly=false;
			}
			window.opener.document.forms[0].volumen.readOnly=false;
		}		
<%
		if (tariffExists && clasifTarif.equals("0")) {
%>
		
		if ((document.forms[0].weight.value.length>0) && (document.forms[0].ss_refr_srvc_id[rownum].value=="PACKETS")) {
			window.opener.document.forms[0].specialTariff.value="";
			window.opener.document.forms[0].peso.value="";
			window.opener.document.forms[0].volumen.value="";
			
			window.opener.document.forms[0].peso.value=document.forms[0].weight.value;
			window.opener.document.forms[0].volumen.value=document.forms[0].volume.value;
			window.opener.document.forms[0].volumen.readOnly=false;
			window.opener.document.forms[0].peso.readOnly=false;
			window.opener.document.forms[0].specialTariff.value="true";
			if(new Number(window.opener.document.forms[0].peso.value)  == 0  && new Number(window.opener.document.forms[0].volumen.value) == 0) {
				window.opener.document.forms[0].volL.value = "";
				window.opener.document.forms[0].volH.value = "";
				window.opener.document.forms[0].volW.value = "";
				window.opener.document.forms[0].weightVolumetric.value = "";
			}
			//window.opener.document.getElementById("req").style.visibility = 'hidden';
		}
<%
		}			
%>
		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}
</script>

<%
	String selectedDescription = request.getParameter("selecteddescription");
%>
			<script type="text/javascript">
				oldSelectedDescription='<%= selectedDescription %>';
			</script>
<form name=frm>
<%
		ArrayList result = (ArrayList) request.getAttribute("descriptionRecords");
		int count=0;
		for(int i=0;i < result.size(); i++){
			HashMap values = (HashMap) result.get(i);
%>
		
		<input type="hidden" name="ss_srvc_id" value=<%=values.get("SS_SRVC_ID")%> >
		<input type="hidden" name="ss_refr_srvc_id" value=<%=values.get("SS_REFR_SRVC_ID")%> >
<%
			String refer_id="";
			refer_id=(String)values.get("SS_REFR_SRVC_ID");
			
			if(count==0) {
				if(refer_id.equalsIgnoreCase("PACKETS")) {
					count++;
%>
		<input type="hidden" name="weight" value=<%=values.get("weight")%> >
		<input type="hidden" name="volume" value=<%=values.get("volume")%> >
		<%
				}
			}
		}	
%>
<div id="datadiv" style="OVERFLOW: auto; WIDTH: 100%;HEIGHT: 92pt">
	<table align="left" class="tablaDetallev2" width="100%" border=1 cellspacing="1" cellpadding="1">
		<tr>
			<td align="left" width="70%"><b>Descripción</b></td>
			<td align="left"  width="20%"><b>Código</b></td>              
       </tr>     
                       
<%	
		if(result.size()==0){
%>
			<script type="text/javascript">
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
				<td align="left" id="tablecol2<%=i%>" width="60%"><%=values.get("SS_DESC")%></td>
				<td align="left" id="tablecol1<%=i%>" width="20%"><%=values.get("SS_CODE")%></td>
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
</table>
</form>
</BODY>
</html>