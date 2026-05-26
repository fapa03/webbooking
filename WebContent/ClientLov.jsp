<%@ page language="java" import="java.util.*"%>

<HTML>
<HEAD>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<TITLE></TITLE>
</HEAD>
<BODY onload=initScript()>
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
		oldobject.style.backgroundColor='#cedee9';
	obj.style.backgroundColor='#FFF0CC';
	oldobject=obj;	
	colflag='true';
}
function getRow(){
	if(rowidflag=='true'){
		rowindex=rowid.indexOf('w');
		rownum=rowid.substring(rowindex+1,rowid.length);	
		obj1 = document.getElementById('tablecol1'+rownum);
		obj2 = document.getElementById('tablecol2'+rownum);	
		
		
		var oldSelecteddestinationclave = window.opener.document.forms[0].destinationclave.value;
		
		window.opener.document.forms[0].destinationnombre.value = obj2.innerHTML;
		window.opener.document.forms[0].destinationclave.value = obj1.innerHTML;
		
		if(!(oldSelecteddestinationclave==obj1.innerHTML)){
			window.opener.document.forms[0].destino1.value="";
			window.opener.document.forms[0].destino2.value="";
			window.opener.document.forms[0].destinationcolonia1.value="";
			window.opener.document.forms[0].destinationcolonia2.value="";
			window.opener.document.forms[0].destinationtelefono.value="";			
			window.opener.document.forms[0].destinationbranch.value="";
			window.opener.document.forms[0].brncVrtl.value="";
			
			if (document.forms[0].isNewBooking.value == 'Y') {
				/*reinicia valores de detalle*/
				opener.resetDetail();			
			}
		}
		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}

</script>
<form name=frm>
<div id="datadiv" style="BACKGROUND-COLOR: #cedee9; HEIGHT: 92pt; OVERFLOW: auto; WIDTH: 100%">
	<table align="left" width="100%" border=1 cellspacing="1" cellpadding="1">
		<tr>
			<td align="left"  width="60%" ><b>Cliente</b></td>
            <td align="left" width="20%" ><b>Clave</b></td>
       </tr>                
	<% 
	String isNewBooking = request.getParameter("isNewBooking")==null?"":request.getParameter("isNewBooking");//AAP01	
	ArrayList result = (ArrayList) request.getAttribute("clientRecords"); 
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
				<td align="left" id="tablecol2<%=i%>" width="60%"><%=values.get("clientName")%></td>
				<td align="left" id="tablecol1<%=i%>" width="20%"><%=values.get("clientId")%></td>                
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
</table>
<input type="hidden" name="isNewBooking" id="isNewBooking" value=<%=isNewBooking%> />
</form>
</BODY>
