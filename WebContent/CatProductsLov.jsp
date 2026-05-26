<%@page import="mx.com.paquetexpress.general.model.dto.SysSspDesMstrDTO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ page import="java.util.ArrayList, java.util.HashMap" %>

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
		
		<title>Catalogo de Productos</title>
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
					window.opener.document.forms[0].productDescSat.value = obj2.innerHTML + " - " + obj1.innerHTML;
					window.opener.document.forms[0].productIdSat.value = obj2.innerHTML;
					try {
						window.opener.document.forms[0].tarifa.focus();
					} catch (e) {
						try {
							window.opener.document.forms[0].comments.focus();
						} catch (e) {
							// TODO: handle exception
						}
					}
					
					window.close();
				}else{
					alert('NO HAY REGISTROS DISPONIBLES');
				}
			}
			function getRowSelect() {
				getRow();
			}
			function srchLdProd(){
				var input, filter, table, tr, td, i, txtValue, sim, simPqtx, simValue, simPqtxValue;
				input = document.getElementById("product");
				filter = input.value;
				table = document.getElementById("products");
				tr = table.getElementsByTagName("tr");
				var compareTo; // 0 = Id		1 = Descripción   
				compareTo = Number.isInteger(filter * 1) ? 0 : 1;
				if (compareTo == 1){
					filter = filter.toUpperCase();
				}
				// Loop through all table rows, and hide those who don't match the search query
				for (i = 1; i < tr.length; i++) {
				  td = tr[i].getElementsByTagName("td")[compareTo];
				  sim = tr[i].getElementsByTagName("td")[2];
				  simPqtx = tr[i].getElementsByTagName("td")[3];
				  if (td) {
				    txtValue = td.textContent || td.innerText;
				    simValue = sim.textContent || sim.innerText;
				    simPqtxValue = simPqtx.textContent || simPqtx.innerText;
				    if (txtValue.toUpperCase().indexOf(filter) > -1 ||
				    		simValue.toUpperCase().indexOf(filter) > -1 ||
				    		simPqtxValue.toUpperCase().indexOf(filter) > -1) {
				      tr[i].style.display = "";
				    } else {
				      tr[i].style.display = "none";
				    }
				  }
				}
			}
			function openKeyLov(str) {
				if(window.event.keyCode==13 && str == 'catProducts'){
					if (document.forms[0].productToSearch.value == '') {
						alert("CAPTURE UN VALOR PARA 'CAT. PRODUCTOS'");
						return;
					}else{
						var productSearched = document.getElementById("productToSearch").value;
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
					try{
						if (window.opener.document.forms[0].formaPago.value == 'TO_PAY'){
							window.open('lov.do?type='+str+'&contenido='+document.getElementById("productToSearch").value.toUpperCase()+'&clntDestId='+clntDestId,'_self','width=950,height=250,top=185,left=0,screenY=100,screenX=100');	
						}else{
							window.open('lov.do?type='+str+'&contenido='+document.getElementById("productToSearch").value.toUpperCase(),'_self','width=950,height=250,top=185,left=0,screenY=100,screenX=100');
						}	
					}catch(e){
						window.open('lov.do?type='+str+'&contenido='+document.getElementById("productToSearch").value.toUpperCase(),'_self','width=950,height=250,top=185,left=0,screenY=100,screenX=100');
					}
				}
			}
			function srchProd(str){
				if(str == 'catProducts'){
					if (document.forms[0].productToSearch.value == '') {
						alert("CAPTURE UN VALOR PARA 'CAT. PRODUCTOS'");
						return;
					}else{
						var productSearched = document.getElementById("productToSearch").value;
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
					try{
						if (window.opener.document.forms[0].formaPago.value == 'TO_PAY'){
							window.open('lov.do?type='+str+'&contenido='+document.getElementById("productToSearch").value.toUpperCase()+'&clntDestId='+clntDestId,'_self','width=950,height=250,top=185,left=0,screenY=100,screenX=100');	
						}else{
							window.open('lov.do?type='+str+'&contenido='+document.getElementById("productToSearch").value.toUpperCase(),'_self','width=950,height=250,top=185,left=0,screenY=100,screenX=100');
						}	
					}catch(e){
						window.open('lov.do?type='+str+'&contenido='+document.getElementById("productToSearch").value.toUpperCase(),'_self','width=950,height=250,top=185,left=0,screenY=100,screenX=100');
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
		@SuppressWarnings("unchecked")
		ArrayList<SysSspDesMstrDTO> result = (ArrayList<SysSspDesMstrDTO>) request.getAttribute("catProductsRecords");
		int count=0;
	%>
	<div>
		<label><b>Nombre / Código</b></label><br>
		<input type="text" id="productToSearch" onkeypress="openKeyLov('catProducts')">
		<input type=button class="button1 buttonMedium" value="Buscar" onclick="srchProd('catProducts')">
		<br>
		<hr>
	</div>
	<div>
		<label><b>Seleccione producto</b></label>
		<i style="float: right;"class="fa fa-search" aria-hidden="true" onclick="srchLdProd()"></i>
		<input style="float: right;" type="text" id="product" onkeyup="srchLdProd()" placeholder="Buscar en la lista...">
	</div>
		<div id="datadiv" style="OVERFLOW: auto; WIDTH: 100%;HEIGHT: 92pt">
	<table id="products" align="left" class="tablaDetallev2" width="100%" border=1 cellspacing="1" cellpadding="1">
		<tr>
			<td align="left" width="15%"><b>Código</b></td>
			<td align="left"  width="30%"><b>Descripción</b></td>
			<td align="left"  width="30%"><b>Términos similares SAT</b></td>
			<td align="left"  width="30%"><b>Términos similares PQTX</b></td>               
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
			for(int i=0;i < result.size(); i++){
				SysSspDesMstrDTO values = (SysSspDesMstrDTO) result.get(i);
	%>	
		<tr id="tablerow<%=i%>" name=t1 style="CURSOR: hand" onclick="changeColor(this)">
				<td align="left" id="tablecol2<%=i%>" width="20%"><%=values.getCode() == null ? "&nbsp" : values.getCode()%></td>
				<td align="left" id="tablecol1<%=i%>" width="30%"><%=values.getDescr() == null ? "&nbsp" : values.getDescr()%></td>
				<td align="left" id="tablecol3<%=i%>" width="30%"><%=values.getSimilar() == null ? "&nbsp" : values.getSimilar()%></td>
				<td align="left" id="tablecol4<%=i%>" width="30%"><%=values.getSimilarPqtx() == null ? "&nbsp" : values.getSimilarPqtx()%></td>
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
	</body>
</html>