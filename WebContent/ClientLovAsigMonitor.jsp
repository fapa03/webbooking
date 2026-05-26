<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.*"%>

<html>
<head>
<link rel=stylesheet media=screen type=text/css href=webbookingMonitor.css>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>

<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />

<title>Cliente a Asignar</title>
</head>
<body onload="initScript()" class="backgroundStandard"  >
<script >
var colflag='false';
var rowid;
var rowidflag='true';
var oldobject;
var rownum;

function openKeyLov() {
	if(window.event.keyCode==13) {
		getSalesMan();
	}
}

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

function getSalesMan() {
	document.forms[0].asigClientName.value = document.forms[0].asigClientName.value.toUpperCase();
	document.forms[0].action="lov.do";
	document.forms[0].submit();
}

function getRow(){
	
	if(rowidflag=='true'){
		
		rowindex=rowid.indexOf('w');
		rownum=rowid.substring(rowindex+1,rowid.length);	
		var obj1 = (document.getElementById('tablecol1'+rownum));
		var obj2 = (document.getElementById('tablecol2'+rownum));	
		window.opener.document.forms[0].cveClienteAsig.value = obj2.innerHTML;
		window.opener.document.forms[0].desClienteAsig.value =obj1.innerHTML;
		
		window.opener.document.forms[0].cveUserAsig.value="";
		window.opener.document.forms[0].desUserAsig.value="";
		
		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
	
}

</script>
<form name="frm" method="post">
<div id="datadiv" style="WIDTH: 670">
	<table align="left" width="650" border=1  class="tablaDetallev2" cellspacing="0" cellpadding="0">
		<tr width="650">
		
			<td align="left" class="labelText">
			<div style="float:left; padding-top:6px;"><b>Cliente: </b></div>
			<div class="inputV2" style="float:left;">
                        <div class="group">
                            <input type="text" name="asigClientName" size="69" onkeypress="openKeyLov()" class="upper">                                       
                            <span class="highlight"></span>
                            <span class="bar" ></span>
                        </div>
                    </div>
			<div style="float:left;">
			&nbsp;
			<input type=button class="lov buttonMore" value="..." onclick="getSalesMan();">
			</div>				
			</td>
		</tr>
	</table>
</div>
<logic:present name="clientRecords">
<div id="data" style="HEIGHT: 159pt; OVERFLOW: auto; WIDTH: 670">
	<table align="left" width="650" class="tablaDetallev2">			
		<tr width="650" class="tablaDetallev2Header">
			<th align="left" width="50"> <b>ID Cliente</b></th>
			<th align="left" width="450"><b>Nombre Cliente</b></th>
			<th align="left" width="100"><b>Sucursal</b></th>
       </tr>
	<%
		ArrayList result = (ArrayList) request.getAttribute("clientRecords");
		if(result.size()==0) {
			for(int i=0;i <= 9; i++) {
	%>
			<script language=javascript>
				rowidflag='false';				
			</script>
			<tr class="tablaDetallev2Body">
				<td align="left" width="25%">&nbsp;</td>
				<td align="left" width="50%">&nbsp;</td>
				<td align="left" width="25%">&nbsp;</td>
			</tr>
	<%
			}
		} else {
			HashMap values = null;
			for(int i=0;i < result.size(); i++){
				values = (HashMap) result.get(i);
		
	%>	
		<tr id="tablerow<%=i%>" name=t1 style="CURSOR: hand" onclick="changeColor(this)" class="tablaDetallev2Body">
			<td align="left" id="tablecol2<%=i%>" width="25%" ><%=values.get("clientId")%></td>
			<td align="left" id="tablecol1<%=i%>" width="50%" ><%=values.get("clientName")%></td>
			<td align="left" id="tablecol1<%=i%>" width="25%" ><%=values.get("siteName")%></td>
       </tr>
    <%
     			values.clear();
			}
			
			if (result.size()<9){
				for(int i=result.size();i <= 9; i++){
	%>
					<tr class="tablaDetallev2Body">
						<td align="left" width="25%" class="labelText">&nbsp;</td>
						<td align="left" width="50%" class="labelText">&nbsp;</td>
						<td align="left" width="25%" class="labelText">&nbsp;</td>
					</tr>
	<%					
				}
			}
		}
     %>               		
	
</TABLE>
</div>
<table ALIGN="CENTER">
	<tr ALIGN="CENTER">
		<td  COLSPAN="3">
			<input type=button class="button buttonMedium" value="Aceptar" onclick="getRow()">	
			<input type=button class="button buttonMedium" value="Cancelar" onclick="javascript:window.close()">
		</td>
	</tr>
</table>
</logic:present>
<input type="hidden" name="type" value="<%=request.getParameter("type")%>"/>
<input type="hidden" name="opcSelAsig" id="opcSelAsig" value="<%=request.getParameter("opcSelAsig")%>"/>
<input type="hidden" name="siteId" id="siteId" value="<%=request.getParameter("siteId")%>"/>
</form>
</body>
</html>