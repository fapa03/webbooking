<%@ page language="java" import="java.util.*"%>

<HTML>
<HEAD>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<TITLE></TITLE>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
</HEAD>
<BODY onload=initScript()>

<script>
var colflag='false';
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

		//var oldSelectedDestinationcode = window.opener.document.forms[0].destSite.value;

		window.opener.document.forms[0].destSiteName.value = obj2.innerHTML;
		window.opener.document.forms[0].destSite.value = obj1.innerHTML;		
		//alert('oldSelectedDestinationcode '+oldSelectedDestinationcode + ' obj1.innerHTML '+obj1.innerHTML);
		//if(!(oldSelectedDestinationcode==obj1.innerHTML)){
			window.opener.document.forms[0].destclave.value="";
			window.opener.document.forms[0].destnombre.value="";
			window.opener.document.forms[0].dest1.value="";
			window.opener.document.forms[0].dest2.value="";
			window.opener.document.forms[0].destcolonia1.value="";
			window.opener.document.forms[0].destcolonia2.value="";
			window.opener.document.forms[0].desttelefono.value="";
			window.opener.document.forms[0].brncVrtl.value = "";
			window.opener.document.forms[0].deliveryType.selectedIndex = -1;
			window.opener.document.forms[0].deliveryType.value = "";
		//}

		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}
</script>
<form name=frm>
<div id="datadiv"
	style=" OVERFLOW: auto; WIDTH: 100%; HEIGHT: 92pt">
<table align="left" width="100%" border=1 cellspacing="1" class="tablaDetallev2"
	cellpadding="1">
	<tr>
		<td align="left" width="60%"><b>Sucursal</b></td>
		<td align="left" width="20%"><b>Siglas</b></td>
	</tr>

	<%
		String selectedBranch = (String)request.getParameter("selectedBranch");		
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
	<tr>
		<td align="center" colspan=2 width="80%"><font size="5"
			color="red">No Records</font></td>
	</tr>
	<%
		}else{
			for(int i=0;i < result.size(); i++){
			HashMap values = (HashMap) result.get(i);
	%>
	<tr id="tablerow<%=i%>" name=t1 style="CURSOR: hand"
		onclick="changeColor(this)">
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
		<td><input type=button class="button1 buttonMedium" style="margin-top: 15px;" value="Aceptar"
			onclick="getRow()"></td>
		<td><input type=button class="button1 buttonMedium" style="margin-top: 15px;"  value="Cancelar"
			onclick="javascript:window.close()"></td>
	</tr>
	</form>
</BODY>