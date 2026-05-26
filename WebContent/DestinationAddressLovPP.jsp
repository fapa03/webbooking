<%@ page language="java" import="java.util.*"%>

<HTML>
<HEAD>
<link rel="stylesheet" media="screen" type="text/css"
	href="webbooking.css">
<TITLE></TITLE>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
</HEAD>
<BODY onload="initScript()" class="backgroundStandard" >
<%
	String destinationRfc = (String)request.getAttribute("destinationrfc");
	destinationRfc = (destinationRfc!=null?destinationRfc:"");
%>
<script>
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
		oldobject.style.backgroundColor='#fff';
	obj.style.backgroundColor='#ccc';
	oldobject=obj;	
	colflag='true';
}

	function getRow() {		
		if(rowidflag=='true'){
			rowindex=rowid.indexOf('w');		
			rownum=rowid.substring(rowindex+1,rowid.length);		
			destinationcolonia2 = (document.getElementById('tablecol3'+rownum));
			destinationcolonia1 = (document.getElementById('tablecol2'+rownum));
			if(document.forms[0].branch[rownum].value=='null') {
				alert('NO SE HA ASIGNADO LA COLONIA A LA SUCURSAL');
			} else {//AAP01
				var branchvalue = document.forms[0].branch[rownum].value.substring(0,3);//AAP01
				if (branchvalue == '70') {//AAP01
					window.opener.document.forms[0].destRefDom.value = document.forms[0].am_comt[rownum].value;//AAP01
					window.opener.document.forms[0].checkRefDir.value = 'Y';//AAP01				
					if (document.forms[0].am_phono1[rownum].value=='') {//AAP01
						window.opener.document.forms[0].checkTelDir.value = 'Y';//AAP01
					}//AAP01
				}//AAP01			
			}
			//Added on 14/07/2010 - For kitsId : 70454 Starts
	 		var addressDefinition = document.forms[0].am_addr_defn_type[rownum].value;
			if (addressDefinition != "Y") {
				alert("DIRECCIÓN ESTRUCTURAL SE REQUIERE PARA PROCEDER DE RESERVA");
				return;
			}
			//Added on 14/07/2010 - For kitsId : 70454 Ends
	 		
			window.opener.document.forms[0].destcolonia1.value = destinationcolonia1.innerHTML;
			window.opener.document.forms[0].destcolonia2.value = destinationcolonia2.innerHTML;		
			window.opener.document.forms[0].dest1.value=document.forms[0].am_strt_name[rownum].value;
			window.opener.document.forms[0].dest2.value=document.forms[0].am_drnr[rownum].value;
			window.opener.document.forms[0].desttelefono.value=document.forms[0].am_phono1[rownum].value;
			window.opener.document.forms[0].destcode.value=document.forms[0].am_addr_code[rownum].value;
			window.opener.document.forms[0].destrfc.value=destinationRfc;
			window.opener.document.forms[0].destBranch.value = document.forms[0].branch[rownum].value;//AAP01
			
			/*se reinician valores de tipo de entrega para que tenga efecto el evento onchange del componente*/
			var obj = window.opener.document.forms[0].deliveryType;
			obj.selectedIndex=-1;
			obj.value = "";
			
			window.opener.document.forms[0].currentTask.value = "zoneextendedverify";
			window.opener.document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
			window.opener.document.forms[0].submit();
			
			window.close();
		}else{
			alert('NO HAY REGISTROS DISPONIBLES');
		}
	}


	function getRow1(){
		
		rowindex=rowid.indexOf('w');		
		rownum=rowid.substring(rowindex+1,rowid.length);	
		destinationcolonia2 = (document.getElementById('tablecol3'+rownum));
		destinationcolonia1 = (document.getElementById('tablecol2'+rownum));
		//alert("getRow1() document.forms[0].branch.value "+document.forms[0].branch.value);
		if(document.forms[0].branch.value=='null') {
			alert('NO SE HA ASIGNADO LA COLONIA A LA SUCURSAL');
		} else {//AAP01
			var branchvalue = document.forms[0].branch.value.substring(3);//AAP01
			//alert('getRow1() paso 1 branchvalue: '+branchvalue);
			if (branchvalue == '70') {//AAP01
				window.opener.document.forms[0].destRefDom.value = document.forms[0].am_comt.value;//AAP01
				window.opener.document.forms[0].checkRefDir.value = 'Y';//AAP01
				
				if (document.forms[0].am_phono1.value=='') {
					window.opener.document.forms[0].checkTelDir.value = 'Y';//AAP01
				}				
			}//AAP01
		}//AAP01
	
		//Added on 14/07/2010 - For kitsId : 70454 Starts
		var addressDefinition = document.forms[0].am_addr_defn_type.value;
		if (addressDefinition != "Y") {
			alert("DIRECCIÓN ESTRUCTURAL SE REQUIERE PARA PROCEDER DE RESERVA");
			return;
		}
		//Added on 14/07/2010 - For kitsId : 70454 Ends
		window.opener.document.forms[0].destcolonia1.value = destinationcolonia1.innerHTML;
		window.opener.document.forms[0].destcolonia2.value = destinationcolonia2.innerHTML;
			
		window.opener.document.forms[0].dest1.value=document.forms[0].am_strt_name.value;
		window.opener.document.forms[0].dest2.value=document.forms[0].am_drnr.value;
		window.opener.document.forms[0].desttelefono.value=document.forms[0].am_phono1.value;
		window.opener.document.forms[0].destcode.value=document.forms[0].am_addr_code.value;
		window.opener.document.forms[0].destrfc.value=destinationRfc;
		window.opener.document.forms[0].dest_am_gety_code.value = document.forms[0].am_gety_code.value;
		window.opener.document.forms[0].destBranch.value = document.forms[0].branch.value;//AAP01
		
		window.opener.document.forms[0].currentTask.value = "zoneextendedverify";
		window.opener.document.forms[0].banCerrar.value = "false";//bandera para validar cuando se cierre la ventana desde "x" de navegador
		window.opener.document.forms[0].submit();

		window.close();
	}

</script>
<form name="frm">
<div id="datadiv"
	style="HEIGHT: 92pt; OVERFLOW: auto; WIDTH: 100%">
<table align="left" width="100%" border="1" cellspacing="1" class="tablaDetallev2"
	cellpadding="1">
	<tr>
		<td align="left" width="15%"><b>Tipo de Dirección</b></td>
		<td align="left" width="20%"><b>Ciudad</b></td>
		<td align="left" width="50%"><b>Colonia</b></td>
		<td align="left" width="25%"><b>Código Postal</b></td>
	</tr>
	<% 
		ArrayList result = (ArrayList) request.getAttribute("destnationAddressRecords");
        if(result.size()==0){
	%>
	<script language="javascript">
				rowidflag='false';				
			</script>
	<tr>
		<td align="center" colspan="2" width="80%"><font size="5"
			color="red">No Records</font></td>
	</tr>
	<%
		}else{
		for(int i=0;i < result.size(); i++){
		HashMap values = (HashMap) result.get(i);
		String branches=(String)values.get("branch");
		if ((branches==null) || (branches=="") || (branches.length()<=0))
		{
			session.setAttribute("colonyerror","error");
		}
		// Added on 27/07/2010 - For kitsId : 70454 Starts
		else
		{
			session.setAttribute("colonyerror","");
		}
		// Added on 27/07/2010 - For kitsId : 70454 Starts
	%>
	<tr id="tablerow<%=i%>" name="t1" style="CURSOR: hand"
		onclick="changeColor(this)">
		<td align="left" id="tablecol1<%=i%>" width="15%"><%=values.get("AM_ADDR_STYP")%></td>
		<td align="left" id="tablecol2<%=i%>" width="20%"><%=values.get("u14")%></td>
		<td align="left" id="tablecol3<%=i%>" width="50%"><%=values.get("u16")%></td>
		<td align="left" id="tablecol4<%=i%>" width="25%"><%=values.get("Zipcode")%></td>
		<input type="hidden" name="am_strt_name"
			value="<%=values.get("AM_STRT_NAME")%>">
		<input type="hidden" name="am_drnr" value="<%=values.get("AM_DRNR")%>">
		<input type="hidden" name="am_phono1"
			value="<%=values.get("AM_PHNO1")%>">
		<input type="hidden" name="am_addr_code"
			value="<%=values.get("AM_ADDR_CODE")%>">
		<input type="hidden" name="destinationcoloniacode"
			value="<%=values.get("c16")%>">
		<input type="hidden" name="citycode" value="<%=values.get("c14")%>">
		<input type="hidden" name="branch" value="<%=values.get("branch")%>">
		<input type="hidden" name="branchname"
			value="<%=values.get("branchname")%>">
		<!-- Added on 14/07/2010 - For kitsId : 70454 Starts -->
		<input type="hidden" name="am_addr_defn_type"
			value="<%=values.get("AM_ADDR_DEFN_TYPE")%>">
		<!-- Added on 14/07/2010 - For kitsId : 70454 Ends -->
		<input type="hidden" name="am_gety_code"
			value="<%=values.get("AM_GETY_CODE")%>">
		<input type="hidden" name="am_comt"
			value="<%=values.get("AM_COMT")%>">
	</tr>
	<%
     		}
		}
     %>
</table>
</div>
<table width="20%" align="center">
	<tr>
		<td>
		<%
			    if(result.size()== 1){
			%> <input type="button" class="button1 buttonMedium" value="Aceptar"
			onClick="getRow1()"> <%
				}else{
			%> <input type="button" class="button1 buttonMedium" value="Aceptar"
			onclick="getRow()"> <%
				}
			%>
		</td>
		<td><input type="button" class="button1 buttonMedium" value="Cancelar"
			onclick="javascript:window.close()"></td>
	</tr>
</table>
</form>
</BODY>
</HTML>