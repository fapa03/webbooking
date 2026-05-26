<%@ page language="java" import="java.util.*,bean.ShipmentServiceDetail"%>

<HTML>
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
	String action = (String)request.getParameter("action");
	String index = (String)request.getParameter("index");	
	int intIndex=-1;
	if(index!=null)
		intIndex=Integer.parseInt(index);
	
	ArrayList shipDetail=null;	
	shipDetail = (ArrayList)session.getAttribute("servicesDetail");
	
	ArrayList descArray = new ArrayList();
	for(int i=0;i<shipDetail.size();i++){
		ShipmentServiceDetail ssd =   (ShipmentServiceDetail)shipDetail.get(i);
		//System.out.println("DESCRIPTION "+ssd.descripcion);
		//System.out.println("DESCRIPTION "+ssd.tarifa);
		if(action!=null && action.equals("edit")){
			if(i==intIndex)
				continue;
		}
%>
		
			descArray[<%=i%>] = "<%= ssd.descripcion %>";
			tariffArray[<%=i%>] = "<%= ssd.tarifa %>";
		
<%
		}	
%>

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
		oldobject.style.backgroundColor='#fff';
	obj.style.backgroundColor='#D0D2C5';
	oldobject=obj;	
	colflag='true';
}
function getRow(){
	
	if(rowidflag=='true'){
		rowindex=rowid.indexOf('w')	
		rownum=rowid.substring(rowindex+1,rowid.length);	
		
		obj1 = (document.getElementById('tablecol1'+rownum))
		obj2 = (document.getElementById('tablecol2'+rownum))
		description = window.opener.document.forms[0].descripcion.value;
		for(i=0;i<descArray.length;i++){
			//alert('DESCRIPTION '+descArray[i]);
			//alert('TARIFF '+tariffArray[i]);
			if(description == descArray[i]&& obj2.innerHTML==tariffArray[i]){
				alert('EL SERVICIO #1 YA ESTA REGISTRADO');
				return;
			}
		}
		
		window.opener.document.forms[0].tarifa.value = obj2.innerHTML;
		window.opener.document.forms[0].peso.value = '0';
		window.opener.document.forms[0].volumen.value = '0';
		window.opener.document.forms[0].volumen.readOnly = 'true';
		if (window.opener.document.forms[0].isShippingTypeSEG.value == 'N' && (new Number(window.opener.document.forms[0].weightVolumetric.value) > 0)) {
			window.opener.document.forms[0].weightVolumetric.value = "";
			window.opener.document.forms[0].volL.value = "";
			window.opener.document.forms[0].volH.value = "";
			window.opener.document.forms[0].volW.value = "";
		}
		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}
</script>
<form name=frm>
<div id="datadiv" style="height: 92pt;OVERFLOW: auto; WIDTH: 100%;">
	<table align="left" width="100%" border=1 cellspacing="1" cellpadding="1">
	<% 
		ArrayList result = (ArrayList) request.getAttribute("tariffRecords");
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
			<td align="left" id="tablecol2<%=i%>" width="60%"><%=values.get("TI_TRIF_ID")%></td>
			<td align="left" id="tablecol1<%=i%>" width="20%"><%=values.get("FACT1")%></td>
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
<input type=button class="button1" value="Aceptar" onclick="getRow()">
</td>
<td>
<input type=button class="button1" value="Cancelar" onclick="javascript:window.close()">
</td>
</tr>
</form>
</BODY>
</html>