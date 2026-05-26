<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ page import="java.util.ArrayList, java.util.HashMap" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
		<link rel="stylesheet" media="screen" type="text/css" href="css/font-awesome.min.css">
		<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
		<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
		<link rel=stylesheet media=screen type=text/css href=webbooking.css />
		<script type="text/javascript" src="js/v2/global.js"></script>
		<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
		
		<title>Catalogo de Regimen Fiscal</title>
	</head>
	<body onload="initScript()" class="backgroundStandard">
	<script type="text/javascript">
			var colflag='false';
			var rowid;
			var rowidflag='true';
			var oldobject;
			var rownum;
			var descArray = new Array();
			<% 
				String action = request.getParameter("action");
				String index = request.getParameter("index");
				
				int intIndex=-1;
				if(index!=null) {
					intIndex=Integer.parseInt(index);
				}
			%>
			function initScript(){
				try{
					var obj= document.getElementById('tablerow0');		
					changeColor(obj);
					}catch(JSException){
					}
			}
			function changeColor(obj) {
				rowid=obj.id;
				if(colflag=='true') {
					oldobject.style.backgroundColor='#fff';
				}
				obj.style.backgroundColor='#D0D2C5';
				oldobject=obj;	
				colflag='true';
			}
			function getRowInit() {
				var tabla = document.getElementById('tablaDetalle');
				var reng = tabla.rows;
				if (reng.length==2) {
					getRow();
				}
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
					window.opener.document.forms[0].regimenFiscalDes.value = obj1.innerHTML.trim() + " - " + obj2.innerHTML.trim();
					window.opener.document.forms[0].regimenFiscalId.value = obj1.innerHTML.trim();
					
					window.opener.document.forms[0].usoCfdiDes.value = "";
					window.opener.document.forms[0].usoCfdiId.value = "";
					
					window.close();
				}else{
					alert('NO HAY REGISTROS DISPONIBLES');
				}
			}
			function getRowSelect() {
				getRow();
			}
			function srchLdProd(){
				var input, filter, table, tr, td, i, txtValue;
				input = document.getElementById("regimen");
				filter = input.value;
				table = document.getElementById("regimFiscalTable");
				tr = table.getElementsByTagName("tr");
				var compareTo; // 0 = Id		1 = Descripción   
				compareTo = Number.isInteger(filter * 1) ? 0 : 1;
				if (compareTo == 1){
					filter = filter.toUpperCase();
				}
				// Loop through all table rows, and hide those who don't match the search query
				for (i = 1; i < tr.length; i++) {
				  td = tr[i].getElementsByTagName("td")[compareTo];
				  if (td) {
				    txtValue = td.textContent || td.innerText;
				    if (txtValue.toUpperCase().indexOf(filter) > -1) {
				      tr[i].style.display = "";
				    } else {
				      tr[i].style.display = "none";
				    }
				  }
				}
			}
			function openKeyLov(str) {
				if(window.event.keyCode==13 && str == 'regimFiscal'){
					if (document.forms[0].regimenFiscalDes.value == '') {
						alert("CAPTURE UN VALOR PARA 'CAT. REGIMEN FISCAL'");
						return;
					}else{
						var productSearched = document.getElementById("regimenFiscalDes").value;
						if (productSearched.length < 3){
							alert("CAPTURE 3 CARACTERES O MÁS");
							return;
						}
					}
					try {
						var clntDestId = window.opener.document.forms[0].destinationclave.value;
					} catch (e) {
						try {
							var clntDestId = window.opener.document.forms[0].destclave.value;
						} catch (e) {
							// TODO: handle exception
						}
					}
				}
			}
			function srchProd(str){
				if(str == 'regimFiscal'){
					if (document.forms[0].regimenFiscalDes.value == '') {
						alert("CAPTURE UN VALOR PARA 'CAT. REGIMEN FISCAL'");
						return;
					}else{
						var productSearched = document.getElementById("regimenFiscalDes").value;
						if (productSearched.length < 3){
							alert("CAPTURE 3 CARACTERES O MÁS");
							return;
						}
					}
					try {
						var clntDestId = window.opener.document.forms[0].destinationclave.value;
					} catch (e) {
						try {
							var clntDestId = window.opener.document.forms[0].destclave.value;
						} catch (e) {
							// TODO: handle exception
						}
					}
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
		ArrayList result = (ArrayList) request.getAttribute("regimFiscalRecords");
		int count=0;
	%>
	<div>
		<label><b>Seleccione un Regimen</b></label>
		<i style="float: right;"class="fa fa-search" aria-hidden="true" onclick="srchLdProd()"></i>
		<input style="float: right;" type="text" id="regimen" onkeyup="srchLdProd()" placeholder="Buscar en la lista...">
	</div>
		<div id="datadiv" style="OVERFLOW: auto; WIDTH: 100%;HEIGHT: 92pt">
	<table id="regimFiscalTable" align="left" class="tablaDetallev2" width="100%" border=1 cellspacing="1" cellpadding="1">
		<tr>
			<td align="left" width="15%"><b>Código</b></td>
			<td align="left"  width="30%"><b>Descripción</b></td>            
       </tr>
       <%
		if(result==null || result.size()==0){
	%>
			<script type="text/javascript">
				rowidflag='false';
			</script>
			<tr>
				<td align="center" colspan=4 width="80%"><font size="5" color="red" >No Records</font></td>
			</tr>
	<%
		}else{
			int i = 0;
			//for(int i=0;i < result.size(); i++){
				 //ArrayList values = result.get(i);
	%>	
				<logic:iterate id="regimFiscal" name="regimFiscalRecords">
			       	<tr id="tablerow<%=i%>" onclick="changeColor(this)">
			       		<logic:iterate id="regimData" name="regimFiscal" indexId="j">
			       			<logic:equal value="0" name="j">
			       				<td align="left" id="tablecol1<%=i%>">
			       					<bean:write name="regimData"/>
			       				</td>
			       			</logic:equal>
			       		</logic:iterate>
			       		<logic:iterate id="regimData" name="regimFiscal" indexId="k">
			       			<logic:equal value="2" name="k">
			       				<logic:notEqual value="" name="regimData">
			       					<td align="left" id="tablecol2<%=i%>">
			       						<bean:write name="regimData"/>
			       					</td>
			       				</logic:notEqual>
			       			</logic:equal>
			       			<logic:equal value="4" name="k">
			       				<logic:notEqual value="" name="regimData">
			       					<td align="left" id="tablecol2<%=i%>">
			       						<bean:write name="regimData"/>
			       					</td>
			       				</logic:notEqual>
			       			</logic:equal>
			       		</logic:iterate>
			       	</tr>
			       	<%i++; %>
		       </logic:iterate>           
	<%
     	//	}
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
	</body>
</html>