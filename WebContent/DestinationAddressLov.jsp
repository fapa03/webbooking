<%@ page language="java" import="java.util.*"%>

<HTML>
<HEAD>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<TITLE></TITLE>
<link rel="stylesheet" href="css/stylev2.css">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
</HEAD>
<BODY onload=initScript()>
<%
	String destinationRfc = (String)request.getAttribute("destinationrfc");
	destinationRfc = (destinationRfc!=null?destinationRfc:"");
	String isNewBooking = request.getParameter("isNewBooking")==null?"":request.getParameter("isNewBooking");
	//AAP//System.out.println("isNewBooking "+isNewBooking);
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
	if(rowidflag=='true'){
		rowindex=rowid.indexOf('w');		
		rownum=rowid.substring(rowindex+1,rowid.length);		
		destinationcolonia2 = (document.getElementById('tablecol2'+rownum));
		destinationcolonia1 = (document.getElementById('tablecol3'+rownum));
		
		
		if(document.forms[0].branch[rownum].value!="null") {
			var branchvalue = document.forms[0].branch[rownum].value.substring(0,3);
			if(window.opener.document.forms[0].destinationsitecode.value==branchvalue) {
				window.opener.document.forms[0].destinationcolonia1.value = destinationcolonia1.innerHTML;
				window.opener.document.forms[0].destinationcolonia2.value = destinationcolonia2.innerHTML;	
		
				//Se asigna codigo postal a hidden de forma general. AAP01
				window.opener.document.forms[0].destinationzipcode.value=document.getElementById('tablecol4'+rownum).innerHTML;
				
				window.opener.document.forms[0].destino1.value=document.forms[0].am_strt_name[rownum].value;
				window.opener.document.forms[0].destino2.value=document.forms[0].am_drnr[rownum].value;
				window.opener.document.forms[0].destinationtelefono.value=document.forms[0].am_phono1[rownum].value;
				window.opener.document.forms[0].destinationaddresscode.value=document.forms[0].am_addr_code[rownum].value;
				window.opener.document.forms[0].destinationcoloniacode.value=document.forms[0].destinationcoloniacode[rownum].value;
				window.opener.document.forms[0].citycode.value=document.forms[0].citycode[rownum].value;
				window.opener.document.forms[0].getycode.value=document.forms[0].AM_GETY_CODE[rownum].value;
				window.opener.document.forms[0].getylevl.value=document.forms[0].AM_GETY_LEVL[rownum].value;
				window.opener.document.forms[0].getytype.value=document.forms[0].AM_GETY_TYPE[rownum].value;
				window.opener.document.forms[0].destinationrfc.value=destinationRfc;
				window.opener.document.forms[0].destinationcode.value=document.forms[0].branch[rownum].value;
				window.opener.document.forms[0].destinationbranch.value=document.forms[0].branchname[rownum].value;
				
				branchvalue = document.forms[0].branch[rownum].value.substring(3);
				if (branchvalue == '70') {//AAP03
					window.opener.document.forms[0].destinationRefDom.value = document.getElementById('AM_COMT'+rownum).value;//AAP03
					window.opener.document.forms[0].checkRefDir.value = 'Y';//AAP03
					if (document.forms[0].am_phono1[rownum].value =='') {//AAP03
						window.opener.document.forms[0].checkTelDir.value = 'Y';//AAP03						
					}//AAP03
				} else {//AAP03
					window.opener.document.forms[0].checkRefDir.value = 'N';
					window.opener.document.forms[0].checkTelDir.value = 'N';
					window.opener.document.forms[0].destinationRefDom.value = '';
				}
				opener.callForm('thispage');
			} else {
				//alert('Mapping is not proper,Please select someother client address');
				alert('La sucursal no esta asignada correctamente a la direccion del cliente, favor de seleccionar o asignar otra direccion al cliente destino');
			}
		
		} else {
			window.opener.document.forms[0].destinationclave.value="";
			window.opener.document.forms[0].destinationnombre.value="";
			window.opener.document.forms[0].destino1.value="";
			window.opener.document.forms[0].destino2.value="";
			window.opener.document.forms[0].destinationcolonia1.value="";
			window.opener.document.forms[0].destinationcolonia2.value="";
			window.opener.document.forms[0].destinationzipcode.value="";//AAP01
			window.opener.document.forms[0].destinationtelefono.value="";
			window.opener.document.forms[0].destinationcode.value="";
			window.opener.document.forms[0].destinationbranch.value="";
			window.opener.document.forms[0].brncVrtl.value="";			
			
			window.opener.document.forms[0].permiteDestino.value = "";//AAP04
			window.opener.document.forms[0].eMailDestText.value = "";//AAP05
			window.opener.document.forms[0].eMailDestBD.value = "";//AAP05
			window.opener.document.forms[0].destinationrfc.value = "";//AAP05
			/*se necesitan??
			//window.opener.document.forms[0].destinationsite.value = "";
			//window.opener.document.forms[0].destinationsitecode.value = "";
			*/
			//alert('Colony mapping is not proper,Please select someother  client address');
			alert('La colonia no esta asignada correctamente a la sucursal, favor de seleccionar o asignar otra direccion al cliente destino');			
		}
		window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}


function getRow1(){
	rowindex=rowid.indexOf('w');		
	rownum=rowid.substring(rowindex+1,rowid.length);	
	destinationcolonia2 = (document.getElementById('tablecol2'+rownum));
	destinationcolonia1 = (document.getElementById('tablecol3'+rownum));
	
	var branchvalue=document.forms[0].branch.value.substring(0,3);
	
	if(document.forms[0].branch.value!="null") {
		if(window.opener.document.forms[0].destinationsitecode.value==branchvalue) {
			window.opener.document.forms[0].destinationcolonia1.value = destinationcolonia1.innerHTML;
			window.opener.document.forms[0].destinationcolonia2.value = destinationcolonia2.innerHTML;
			//Se asigna codigo postal a hidden de forma general.//AAP01
			window.opener.document.forms[0].destinationzipcode.value=document.getElementById('tablecol4'+rownum).innerHTML;//AAP01
			window.opener.document.forms[0].destino1.value=document.forms[0].am_strt_name.value;
			window.opener.document.forms[0].destino2.value=document.forms[0].am_drnr.value;
			window.opener.document.forms[0].destinationtelefono.value=document.forms[0].am_phono1.value;
			window.opener.document.forms[0].destinationaddresscode.value=document.forms[0].am_addr_code.value;
			window.opener.document.forms[0].destinationcoloniacode.value=document.forms[0].destinationcoloniacode.value;
			window.opener.document.forms[0].citycode.value=document.forms[0].citycode.value;
			window.opener.document.forms[0].destinationrfc.value=destinationRfc;	
			window.opener.document.forms[0].destinationcode.value=document.forms[0].branch.value;
			window.opener.document.forms[0].destinationbranch.value=document.forms[0].branchname.value;
			window.opener.document.forms[0].getycode.value=document.forms[0].AM_GETY_CODE.value;
			window.opener.document.forms[0].getylevl.value=document.forms[0].AM_GETY_LEVL.value;
			window.opener.document.forms[0].getytype.value=document.forms[0].AM_GETY_TYPE.value;
			
			branchvalue = document.forms[0].branch.value.substring(3);
			if (branchvalue == '70') {//AAP03
				window.opener.document.forms[0].destinationRefDom.value = document.getElementById('AM_COMT'+rownum).value;//AAP03
				window.opener.document.forms[0].checkRefDir.value = 'Y';//AAP03
				if (document.forms[0].am_phono1.value =='') {//AAP03
					window.opener.document.forms[0].checkTelDir.value = 'Y';//AAP03						
				}//AAP03
			} else {//AAP03
				window.opener.document.forms[0].checkRefDir.value = 'N';
				window.opener.document.forms[0].checkTelDir.value = 'N';
				window.opener.document.forms[0].destinationRefDom.value = '';
			}
			opener.callForm('thispage');
		} else {
			//alert('Mapping is not proper,Please select someother client address');
			alert('La sucursal no esta asignada correctamente a la direccion del cliente, favor de seleccionar o asignar otra direccion al cliente destino');
		}
		
	} else {
		window.opener.document.forms[0].destinationclave.value="";
		window.opener.document.forms[0].destinationnombre.value="";
		window.opener.document.forms[0].destino1.value="";
		window.opener.document.forms[0].destino2.value="";
		window.opener.document.forms[0].destinationcolonia1.value="";
		window.opener.document.forms[0].destinationcolonia2.value="";
		window.opener.document.forms[0].destinationzipcode.value="";
		window.opener.document.forms[0].destinationtelefono.value="";
		window.opener.document.forms[0].destinationcode.value="";
		window.opener.document.forms[0].destinationbranch.value="";
		window.opener.document.forms[0].brncVrtl.value="";
		
		window.opener.document.forms[0].permiteDestino.value = "";//AAP04
		window.opener.document.forms[0].eMailDestText.value = "";//AAP05
		window.opener.document.forms[0].eMailDestBD.value = "";//AAP05
		window.opener.document.forms[0].destinationrfc.value = "";//AAP05
		/*se necesitan??
		//window.opener.document.forms[0].destinationsite.value = "";
		//window.opener.document.forms[0].destinationsitecode.value = "";
		*/
		//alert('Colony mapping is not proper,Please select someother client address');
		alert('La colonia no esta asignada correctamente a la sucursal, favor de seleccionar o asignar otra direccion al cliente destino');	
	}	
	window.close();
}

</script>
<form name=frm>
<div id="datadiv" style="BACKGROUND-COLOR: #cedee9; HEIGHT: 92pt; OVERFLOW: auto; WIDTH: 100%">
	<input type="hidden" name="isNewBooking" id="isNewBooking" value="<%=isNewBooking%>" >
	<table align="left" width="100%" border=1 cellspacing="1" cellpadding="1">
		<tr>
		    <td align="left" width="15%"><b>Tipo de Dirección</b></td>
			<td align="left"  width="20%"><b>Ciudad</b></td>
			<td align="left"  width="50%"><b>Colonia</b></td>
			<td align="left"  width="25%"><b>Código Postal</b></td>			
       </tr>                
	<% 
		ArrayList result = (ArrayList) request.getAttribute("destnationAddressRecords");
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
			<input type=hidden name="branch" value="<%=values.get("branch")%>" >
			<input type=hidden name="branchname" value="<%=values.get("branchname")%>" >
			<input type=hidden name="AM_GETY_CODE" value="<%=values.get("AM_GETY_CODE")%>" >
			<input type=hidden name="AM_GETY_LEVL" value="<%=values.get("AM_GETY_LEVL")%>" >
			<input type=hidden name="AM_GETY_TYPE" value="<%=values.get("AM_GETY_TYPE")%>" >
			<input type=hidden name="AM_COMT<%=i%>" id="AM_COMT<%=i%>" value="<%=values.get("AM_COMT")%>" />
			<input type=hidden name="AM_MAIL_ID<%=i%>" id="AM_MAIL_ID<%=i%>" value="<%=values.get("AM_MAIL_ID")%>" /><!-- AAP05 -->
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
