<%@ page language="java" import="java.util.*"%>

<HTML>
<HEAD>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<TITLE></TITLE>
<link rel="stylesheet" media="screen" type="text/css" href="webbookingMonitor.css">
<link rel="stylesheet" media="screen" type="text/css" href="webbooking.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
</HEAD>
<BODY onload=initScript() oncontextmenu="return false" class="backgroundStandard" >
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
		oldobject.style.backgroundColor='#fff';
	obj.style.backgroundColor='#ccc';
	oldobject=obj;	
	colflag='true';
}
function getRow(){
	if(rowidflag=='true'){
		rowindex=rowid.indexOf('w')	
		rownum=rowid.substring(rowindex+1,rowid.length);	
		fiscalcolonia1 = (document.getElementById('tablecol2'+rownum));
		fiscalcolonia2 = (document.getElementById('tablecol3'+rownum));
		/*if((window.opener.document.forms[0].fiscalrfc.value=='null'))
			{
			alert("EL CLIENTE NO TIENE R.F.C., no es posible emitir la factura");
						javascript:window.close();
					return;
			}
			if((window.opener.document.forms[0].fiscalrfc.value==''))
			{
			alert("EL CLIENTE NO TIENE R.F.C., no es posible emitir la factura");
							javascript:window.close();
					return;
	}*/
		
		window.opener.document.forms[0].fiscalcolonia1.value = fiscalcolonia1.innerHTML;
		window.opener.document.forms[0].fiscalcolonia2.value = fiscalcolonia2.innerHTML;	
		
		//document.forms[0].am_strt_name.value
		window.opener.document.forms[0].fiscal1.value=document.forms[0].am_strt_name[rownum].value;
		window.opener.document.forms[0].fiscal2.value=document.forms[0].am_drnr[rownum].value;
		window.opener.document.forms[0].fiscaltelefono.value=document.forms[0].am_phono1[rownum].value;
		window.opener.document.forms[0].fiscaladdresscode.value=document.forms[0].am_addr_code[rownum].value;
		//alert(document.forms[0].rfc.value);
		window.opener.document.forms[0].fiscalrfc.value=document.forms[0].rfc[rownum].value;
		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}

function getRow1(){
	rowindex=rowid.indexOf('w')	
	rownum=rowid.substring(rowindex+1,rowid.length);	
	fiscalcolonia1 = (document.getElementById('tablecol2'+rownum));
	fiscalcolonia2 = (document.getElementById('tablecol3'+rownum));
	//alert(window.opener.document.forms[0].fiscalrfc.value);
	/*if((window.opener.document.forms[0].fiscalrfc.value=='null'))
	{
	alert("EL CLIENTE NO TIENE R.F.C., no es posible emitir la factura");
				javascript:window.close();
			return;
	}
	if((window.opener.document.forms[0].fiscalrfc.value==''))
	{
	alert("EL CLIENTE NO TIENE R.F.C., no es posible emitir la factura");
					javascript:window.close();
			return;
	}
	*/
		window.opener.document.forms[0].fiscalcolonia1.value = fiscalcolonia1.innerHTML;
			window.opener.document.forms[0].fiscalcolonia2.value = fiscalcolonia2.innerHTML;		
			window.opener.document.forms[0].fiscal1.value=document.forms[0].am_strt_name.value;
			window.opener.document.forms[0].fiscal2.value=document.forms[0].am_drnr.value;
			window.opener.document.forms[0].fiscaltelefono.value=document.forms[0].am_phono1.value;
			window.opener.document.forms[0].fiscaladdresscode.value=document.forms[0].am_addr_code.value;
			//alert(document.forms[0].rfc.value);
	window.opener.document.forms[0].fiscalrfc.value=document.forms[0].rfc.value;
	
	window.close();	
}
</script>
<form name=frm>
<div  id="datadiv" style=" HEIGHT: 92pt; OVERFLOW: auto; WIDTH: 100%">
	<table align="left" width="100%" border=1 cellspacing="1" cellpadding="1" class="tablaDetallev2" >
		<tr>
		    <td align="left" width="15%"><b>Tipo de Dirección</b></td>
			<td align="left"  width="20%"><b>Ciudad</b></td>
			<td align="left"  width="50%"><b>Colonia</b></td>
			<td align="left"  width="25%"><b>Código Postal</b></td>			
       </tr>                
	
	<% 
	
	
		ArrayList result = (ArrayList) request.getAttribute("fiscalAddressRecords");		
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
			<td align="left" id="tablecol1<%=i%>" width="15%"><%=values.get("AM_ADDR_STYP")%></td>
			<td align="left" id="tablecol2<%=i%>" width="20%"><%=values.get("u14")%></td>
			<td align="left" id="tablecol3<%=i%>" width="50%"><%=values.get("u16")%></td>
			<td align="left" id="tablecol4<%=i%>" width="25%"><%=values.get("Zipcode")%></td>
			<input type=hidden name="am_strt_name" value=<%=values.get("AM_STRT_NAME")%> >
			<input type=hidden name="am_drnr" value=<%=values.get("AM_DRNR")%> >
			<input type=hidden name="am_phono1" value=<%=values.get("AM_PHNO1")%> >
			<input type=hidden name="am_addr_code" value=<%=values.get("AM_ADDR_CODE")%> >
			<input type="hidden" name="rfc" value=<%=values.get("rfc")%>>
       </tr>             
     <%
			}
		}
     %>               		
	</table>
</div>
<table width=20% align=center>
<tr>
<td >
<%
    if(result.size()== 1){
%>
	<input type=button class="button1 buttonMedium" value="Aceptar" onClick="getRow1()">
<%
	}else{
%>
	<input type=button class="button1 buttonMedium" value="Aceptar" onclick="getRow()">
<%
	}
%>
</td>
<td>
<input type=button class="button1 buttonMedium" style="margin-left: 15px;" value="Cancelar" onclick="javascript:window.close()">
</td>
</tr>
</form>
</BODY>
</html>