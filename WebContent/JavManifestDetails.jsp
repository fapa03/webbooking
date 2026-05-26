<%--
Authur		:	KUMARAN
Date		:	

FileName	:	
FormBean	:	
ActionBean	:	
OtherClasses	:	none
CSS FileName	:	webbooking.css
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 03/07/2013
 * Descripción:  Se agregaron validaciones y procesos para guias flete por cobrar.
 * ------------------------------------------------------------------
--%>
<%@ page contentType="text/html" import="java.util.*,bean.*"%>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>
<%	
	String MANIFEST_STATUS = (String)request.getAttribute("MANIFEST_STATUS");
	String NUMBER_OF_GUIAS = (String)request.getAttribute("NUMBER_OF_GUIAS");
	String MANIFEST_AMOUNT = (String)request.getAttribute("MANIFEST_AMOUNT");
	String NUMBER_OF_PACKAGES = (String)request.getAttribute("NUMBER_OF_PACKAGES");
	String PREFERED_COLLECTION_TIME = (String)request.getAttribute("PREFERED_COLLECTION_TIME");
%>
<html:html>
<head>
<script language="javascript">
	function returnBack(){
		//document.forms[0].action="JavShipmentHistoryLog.jsp";//Changed by rama//AAP01
		document.forms[0].operation.value="";//AAP01
		document.forms[0].action = "shipmentHistory.do";//AAP01
		document.forms[0].submit();
	}
	function printManifestDetails() {
		document.forms[0].operation.value="Print";
		document.forms[0].submit();
	}
	function doSubmitNext(){
		//var startIndex = document.forms[0].startIndex.value; 
		//var endIndex = document.forms[0].endIndex.value;
		var pageIndex = document.forms[0].pageindex.value;
		var maxPage = document.forms[0].maxpageindex.value;
		var curPage = document.forms[0].currentpage.value;		
		/*
		if(document.forms[0].emptyrecs.value == "empty"){
			alert("USTED ESTA VIENDO LA ULTIMA PAGINA");
		}
		else */
		if(curPage == maxPage){
			alert("USTED ESTA VIENDO LA ULTIMA PAGINA");
		}
		else{
			//document.forms[0].startIndex.value = parseInt(startIndex) + 10;
			//document.forms[0].endIndex.value = parseInt(endIndex) + 10;
			document.forms[0].pageindex.value = parseInt(pageIndex) + 1;
			document.forms[0].currentpage.value = parseInt(curPage) + 1;			
			document.forms[0].submit();					
		}	
	}
	function doSubmitPrevious(){
		//var startIndex = document.forms[0].startIndex.value; 
		//var endIndex = document.forms[0].endIndex.value;
		var pageIndex = document.forms[0].pageindex.value;
		var maxPage = document.forms[0].maxpageindex.value;
		var curPage = document.forms[0].currentpage.value;		
		if(curPage == 0){
			alert("USTED ESTA VIENDO LA PRIMER PAGINA");
		}
		else{
			//document.forms[0].startindex.value = parseInt(startIndex) - 10;
			//document.forms[0].endindex.value = parseInt(endIndex) - 10;
			document.forms[0].pageindex.value = parseInt(pageIndex) - 1;
			document.forms[0].currentpage.value = parseInt(curPage) - 1;
			document.forms[0].submit();					
		}	
	}
	 function goOut(page) {
	    	if (page == "guiabooking") {
	    		page = "thispage";
	    	}
	    		
	    	if (confirm("DESEA PERDER LA INFORMACION?")) {
 	    		if (page=='shipmenthistory') {//AAP03
	    			document.forms[0].action="shipmentHistory.do";//AAP03
	    		} else {//AAP03
	    			document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page="+page;//AAP03	
	    		}//AAP03
	    		document.forms[0].submit();
	    	} else {
	    		return;
	    	}
		}
</script>
	<TITLE>Manifest Details</TITLE>
	<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
	<link rel=stylesheet media=screen type=text/css href=webbooking.css>
	
	<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
	<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
	<script type="text/javascript" src="js/v2/global.js"></script>
	<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
	<script type="text/javascript" src="barraEspera.js"></script>
	<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
</head>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" class="backgroundStandard"  >
<html:form action="/javManifestDetails"> 


		<table border="0" cellspacing="0" cellpadding="0" class="width100porcent backgroudBluePtx">
			<tr>
				<td  >
					<div class="bodyWidth marginAutoCentro">
				        <img src="images/logos/logoW.png" border="0"  
				        onclick="goOut('mainpage');hideMenu();"       
				          style="max-width: 138px;margin-top: 11px;margin-bottom: 9px;cursor: pointer;"> 
											
							
							<a id="menu-hamburger" class="Animated1" onclick="showMenu()">
							
								<div style="margin-top: 15px;">
									<span id="menuline1" class="Animated05"></span> <span
										id="menuline2" class="Animated05"></span> <span id="menuline3"
										class="Animated05"></span>
								</div>
								
							</a>
					</div>
				</td>
				<td >
			 
				</td>
			</tr>
		</table>

		<div class="MenuOverlay displayNone">
		
	        <a class="btnCloseMenu" onclick="hideMenu()">
	            <div>
	                <span class="lineOne"></span>
	                <span class="lineTwo"></span>
	            </div>
 	        </a>
  
  			<div class="itemsMenu marginAutoCentro bodyWidth">
  			
					<logic:present name="loginForm">
						<html:hidden name="loginForm" property="userValidate" />
						<logic:equal name="loginForm" property="userValidate" value="validUser">
  								
  								<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a> 
								<a onclick="goOut('cliententry');hideMenu();" style="">Registro de cliente destino</a>
								<logic:equal name="loginForm" property="showWeb" value="Y">
									<logic:equal name="loginForm" property="newBooking" value="Y">
  										<a onclick="goOut('guiabooking');hideMenu();" style="">Elaboración de guías</a>
  									</logic:equal>
  								</logic:equal> 
  								<html:hidden name="loginForm" property="showPpg"></html:hidden>
								<logic:equal name="loginForm" property="showPpg" value="Y">
									<logic:equal name="loginForm" property="showPpg" value="Y">
										<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
									</logic:equal>
								</logic:equal>
								<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de guías</a>
								<a onclick="goOut('shipmenthistory');hideMenu();" style="">Historico de envios</a>
								<a onclick="goOut('clientreport');hideMenu();" style="">Información de cliente</a>
								<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
									<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>									
								</logic:equal>
						 
 						</logic:equal>
					</logic:present> 
 					<logic:present name="loginForm">
						<html:hidden name="loginForm" property="userValidate" />
						<logic:notEqual name="loginForm" property="userValidate" value="validUser">

 								<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a> 
								<logic:equal name="loginForm" property="showWeb" value="Y">
									<logic:equal name="loginForm" property="newBooking" value="Y">
  										<a onclick="goOut('guiabooking');hideMenu();" style=""> Elaboración de Guias</a>
  									</logic:equal>
  								</logic:equal>  
  								<html:hidden name="loginForm" property="showPpg"></html:hidden>
								<logic:equal name="loginForm" property="showPpg" value="Y">
									<logic:equal name="loginForm" property="showPpg" value="Y">
										<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
									</logic:equal>
								</logic:equal>
								<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de guías</a> 
								<a onclick="goOut('shipmenthistory');hideMenu();" style="">Historico de envios</a> 
								<a onclick="goOut('clientreport');hideMenu();" style="">Información de cliente</a> 
								<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
									<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>									
								</logic:equal>
 
						</logic:notEqual>
					</logic:present>  			
  			
			
			</div>
 			
		</div>	



 <table border="0" cellspacing="0" cellpadding="0" width="99%" class="marginAutoCentro bodyWidth" >
<tbody>
<tr>
<td>
	<table border="0" width="100%" cellspacing="0" cellpadding="0"  >
	  <tr>
	    <td width="100%">
	      <form method="post"   
            id=form1 name="form1">
	        <table border="0" width="100%" cellspacing="0" cellpadding="0">
	          <tr>
	            <td width="1%"></td>
	            <td width="145%"> <strong class="titleSection fontBold">Detalle de Manifiesto</strong> </td>
	          </tr>
	          <tr>
	            <td width="100%" colspan="2">
	              <table border="0" width="100%" cellspacing="1" cellpadding="0">
	                <tr>
	                  <td width="20%" align="right"><font face="Arial" size="2">Manifiesto&nbsp; </font></td>
	                  <td width="18%" class="mrbtd" align="left"><%= session.getAttribute("MANIFEST_NUMBER").toString() %></td>
	                  <td width="21%" align="right"><font face="Arial" size="2">Estado&nbsp; </font></td>
	                  <td width="30%" class="mrbtd">&nbsp;<%= MANIFEST_STATUS %></td>
	                  <td width="11%"></td>
	                </tr>
	                <tr>
	                  <td width="20%" align="right"><font face="Arial" size="2">Numero de Guías&nbsp; </font></td>
	                  <td width="18%" class="mrbtd">
                        <P align=right>&nbsp;&nbsp;<%= NUMBER_OF_GUIAS %></P></td>
	                  <td width="21%" align="right"><font face="Arial" size="2">Importe&nbsp; </font></td>
	                  <td width="30%" class="mrbtd" align="right">&nbsp;
                 		<%
						  	Global global = (Global) session.getAttribute("sGlobal");
							
	                 		boolean dispAmntFlag = false;
	
	                 		if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
	                 			dispAmntFlag = true;
                 					
                 		
						  	//if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
					  		if(dispAmntFlag)
							{
					  	%>
	        	          	<%= MANIFEST_AMOUNT %> 
            	      	<%
							}
							else
							{
						%>
								<font size="4"><b>******</b></font>
						<%
							}
						%>
        	          </td>
	                  <td width="11%"></td>
	                </tr>
	                <tr>
	                  <td width="20%" align="right"><font face="Arial" size="2">Numero de Paquetes&nbsp;</font></td>
	                  <td width="18%" class="mrbtd">
                        <P align=right>&nbsp;&nbsp;<%= NUMBER_OF_PACKAGES %></P></td>
	                  <td width="21%" align="right"><font face="Arial" size="2">Hora para Recolección&nbsp;</font></td>
	                  <td width="30%" class="mrbtd">&nbsp;<%= PREFERED_COLLECTION_TIME %></td>
	                  <td width="11%"></td>
	                </tr>
	              </table>
	            </td>
	          </tr>
	          <tr>
	            <td width="3%"></td>
	            <td width="97%">&nbsp;</td>
	          </tr>

	          <tr>
	            <td width="3%"></td>
	            <td width="97%"><font face="Arial" size="2"><b>Guías en Manifiesto</b></font></td>
	          </tr>
	          <tr>
	            <td width="100%" colspan="2">
	              <table border="0" width="101%" cellspacing="1" cellpadding="1" class="tablaDetallev2" >
	                <tr>
						<td width="4%"><font size="2" face="Arial">&nbsp;</font></td>
						<td width="9%" valign="bottom" align="middle">
						  <font size="2" face="Arial" align="center">No. Guia</font>
						</td>
						<td width="11%" valign="bottom" align="middle"><font size="2" face="Arial">No. Rastreo</font></td>
						<td width="22%" valign="bottom" align="middle"><font size="2" face="Arial">Sucursal Destino</font></td>
						<td width="22%" valign="bottom" align="middle"><font size="2" face="Arial">Cliente Destino</font></td>
						<td width="8%" valign="bottom" align="middle">
						  <font size="2" face="Arial">Importe</font></td>
						<td width="6%" valign="bottom" align="middle">
						  <font size="2" face="Arial"> No.de<br>Paquetes</font></td>
						<td width="7%"></td>
	                </tr>
	                
	   			    <%if( request.getAttribute("mrbGuiaInfo")!= null &&  ! request.getAttribute("mrbGuiaInfo").equals(""))
					    {
					%>
							<%= request.getAttribute("mrbGuiaInfo") %>
					<%
						}
					%>
		

	              </table>
	            </td>
	          </tr>
	          <tr><td>&nbsp;</td></tr>
	          <tr>	          
	            <td width="100%" colspan="2">
	              <table border="0" width="100%" cellspacing="0" cellpadding="0">
	                <tr>
	                  <td width="27%"></td>
   	                  <td width="7%"><html:button property="previous" value="Anterior" onclick="doSubmitPrevious()" styleClass="button"  /></td>
	                  <td width="8%"><input type="button" class="button" value="Imprimir" onClick="printManifestDetails()" name="printbut"/></td>
	                  <td width="8%"><input type="button" class="button" value="Regresar" onclick="returnBack()" name="returnbut" /> </td>
					  <td width="21%"><html:button property="next" value="Siguiente" styleClass="button" onclick="doSubmitNext()"/></td>

	                  
	                </tr>
	              </table>
	            </td>
	            
	          </tr>
	        </table>
	      
	    </td>
	  </tr>
	</table>

		</td></tr></tbody></table>  
		
		      <html:hidden property="operation"/>
              <html:hidden property="endindex"/>
              <html:hidden property="pageindex"/>
              <html:hidden property="maxpageindex"/>
              <html:hidden property="currentpage"/>      
    
     </html:form>
   </BODY>
</html:html>


