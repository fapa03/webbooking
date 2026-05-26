<%@ page language="java" import="java.util.*"%>

<HTML>
<HEAD>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<TITLE></TITLE>
</HEAD>
<BODY onload=initScript()>
<%
	String destinationRfc = (String)request.getAttribute("destinationrfc");
	destinationRfc = (destinationRfc!=null?destinationRfc:"");
%>
<script >
var destinationRfc = "<%= destinationRfc %>";
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
window.close();
	
}


function getRow1(){
	window.close();
}

</script>
<form name=frm>
<div id="datadiv" style="BACKGROUND-COLOR: #cedee9; HEIGHT: 92pt; OVERFLOW: auto; WIDTH: 100%">
	<table align="left" width="100%" border=1 cellspacing="1" cellpadding="1">
		<tr>
		    <td align="left" width="15%"><b>Tipo de Dirección</b></td>
			<td align="left"  width="20%"><b>Ciudad</b></td>
			<td align="left"  width="50%"><b>Colonia</b></td>
			<td align="left"  width="25%"><b>Código Postal</b></td>			
       </tr>                
	<% 
		ArrayList result = (ArrayList) request.getAttribute("branchAddress");
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
			<input type=hidden name="am_strt_name" value="<%=values.get("AM_STRT_NAME")%>" >
			<input type=hidden name="am_drnr" value="<%=values.get("AM_DRNR")%>" >
			<input type=hidden name="am_phono1" value="<%=values.get("AM_PHNO1")%>" >
			<input type=hidden name="am_addr_code" value="<%=values.get("AM_ADDR_CODE")%>" >
			<input type=hidden name="destinationcoloniacode" value="<%=values.get("c16")%>" >
			<input type=hidden name="citycode" value="<%=values.get("c14")%>" >
			
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
<%
    if(result.size()== 1){
%>
	<input type=button class="button1" value="Aceptar" onClick="getRow1()">
<%
	}else{
%>
	<input type=button class="button1" value="Aceptar" onclick="getRow()">
<%
	}
%>
</td>
<td>
<input type=button class="button1" value="Cancelar" onclick="javascript:window.close()">
</td>
</tr>
</form>
</BODY>
