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
 * Descripción:  Se agregaron validaciones para link HISTORICO DE ENVIOS
 * ------------------------------------------------------------------
--%>
<%@ page language="java" %>
<%@ page import="bean.Global"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html>
<HEAD>
	<TITLE>Guia Cancellation</TITLE>
	<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
	<link rel=stylesheet media=screen type=text/css href=webbooking.css>
	<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
	<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
	<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
	
	<script type="text/javascript" src="barraEspera.js"></script>
	<script type="text/javascript" src="js/v2/global.js"></script>
	<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
	<SCRIPT type="text/javascript">
	<!--

	var form;
	function initScript(){
			 form=document.forms[0];
	  <%
		String errMsgString = (String)request.getAttribute("errorMessageText");

	  	String isGuiaAmount =  null;
	  	if(request.getAttribute("GuiaAmount")!=null)
	  		isGuiaAmount = request.getAttribute("GuiaAmount").toString();

		if( errMsgString != null && errMsgString.length()>0){
	  %>
			alert("<%= errMsgString.trim() %>");
			
			//Added by rama			
			document.forms[0].formnumber.value="";
			document.forms[0].guiaNumber.value="";
			document.forms[0].formnumber.select();
	  <%
		}
	  %>
	}
	
	function confirmCncl() {
		form=document.forms[0];		
		if((form.formnumber.value =='')  && (form.guiaNumber.value == '')){
			alert('CAPTURE CUALQUIER NUMERO DE GUIA O NUMERO DE FORMA PARA BORRAR');
			return false;
		}
		if( form.formnumber.value != '' && form.guiaNumber.value != ''){
			if (confirm("ESTÁ UD. SEGURO(A) QUE DESEA BORRAR LA GUÍA?")) {
				showBarraEspera('mensaje','body','Espere por favor...Cancelando Guía');
				return true;
			} else {
				return false;
			}				
		}else {
			return false;
		}		
	}
	
	function validateFrmNo(){
		form=document.forms[0];
		form.formnumber.value = trim(form.formnumber.value.toString().toUpperCase());
		form.guiaNumber.value = "";
		if( form.formnumber.value != '') {
			form.useraction.value="validate_form";
			showBarraEspera('mensaje','body','Espere por favor...Validando Guía');
			form.submit();
		}
		
	}
	function validateGuiaNo(){
		form=document.forms[0];
		form.guiaNumber.value = trim(form.guiaNumber.value.toString().toUpperCase());
		form.formnumber.value = "";
		if( form.guiaNumber.value != '') {
			form.useraction.value="validate_form";
			showBarraEspera('mensaje','body','Espere por favor...Validando Número de Rastreo');
			form.submit();
		}
	}
	function  trim(val){
		for(var i=0; i<val.length && val.substr(i,1)==' '; i++);
			for(var j=val.length-1; j>-1 && val.substr(j,1)==' '; j--);
				j++;
			if (i<j) val=val.substring(i, j);
				else val="";		
		return val;
	}
	
	
	function goOut(page){
		if(page=="guiabooking") {
			page="thispage";
			if (document.forms[0].showWeb.value != 'Y') {
				alert('USUARIO NO AUTORIZADO PARA OPCION ELABORACION DE GUIA');
				return;
			}
		}
		if(confirm("DESEA PERDER LA INFORMACION?")){
			if (page=='shipmenthistory') {//AAP01
				document.forms[0].action="shipmentHistory.do";//AAP01
			} else {//AAP01
				document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page="+page;//AAP01	
			}//AAP01
			document.forms[0].submit();
		}else{
			return;
		}
	}
	
	//-->
	
</SCRIPT>

</HEAD>
<BODY id="body" text=#000000 leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload="initScript(); mostrarCapaCentro('mensaje');" class="backgroundStandard">
<html:form action="guiaCancellation.do">
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
          <a onclick="goOut('mainpage');hideMenu();" style="">Menú Principal</a>
          <a onclick="goOut('cliententry');hideMenu();" style="">Registro de Cliente Destino</a>
          <logic:equal name="loginForm" property="showWeb" value="Y">
          	<logic:equal name="loginForm" property="newBooking" value="Y">
  				<a onclick="goOut('guiabooking');hideMenu();" style="">Elaboración de guías</a>
  			</logic:equal>
  		  </logic:equal>
  		  <html:hidden name="loginForm" property="showPpg"></html:hidden>
		  <logic:equal name="loginForm" property="showPpg" value="Y">
				<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
		  </logic:equal>
          <a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de Guía</a>
          <a onclick="goOut('shipmenthistory');hideMenu();" style="">Histórico de Envíos</a>
          <a onclick="goOut('clientreport');hideMenu();" style="">Información del Cliente</a>
          <logic:present name="loginForm">
          	<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
          		<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>
          	</logic:equal>
          </logic:present>
         
        </logic:equal>
      </logic:present> 
      <logic:present name="loginForm">
        <html:hidden name="loginForm" property="userValidate" />
        <logic:notEqual name="loginForm" property="userValidate" value="validUser">

        <a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a> 
    	<logic:equal name="loginForm" property="showPpg" value="Y">
			<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
		</logic:equal>
  		<html:hidden name="loginForm" property="showPpg"></html:hidden>
  		<logic:equal name="loginForm" property="showPpg" value="Y">
			<div class="menu_sitio">
				<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
			</div>
		</logic:equal>
    	<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de guías</a> 
    	<a onclick="goOut('shipmenthistory');hideMenu();" style="">Historico de envios</a> 
    	<a onclick="goOut('clientreport');hideMenu();" style="">Información de cliente</a> 
    	<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>
        </logic:notEqual>
      </logic:present>  			
    
  </div>
  
</div>
		<table>
			<tr>
				<td style="height: 8px;"><textarea id="mensaje" class="textareacontenido" cols="46" readonly="readonly" rows="4" onblur="self.focus();"
						style="background-color: #EEEEEE; border-style: ridge; overflow: hidden; text-align: center; vertical-align: middle; z-index: 2; visibility: hidden; position: absolute; top: 450px; left: 200px; width: 350px; height: 80px">
					</textarea></td>
			</tr>
		</table>

<jsp:include page="nocache.jsp" flush="false" />
<table class="bodyWidth marginAutoCentro">
<tr><td>
<strong class="titleSection fontBold">Cancelación de Guía</strong>
<BLOCKQUOTE>     
  <TABLE cellSpacing=0 cellPadding=0 border=0 >
    <TR>
            <TD width="20"  style="width:20%" align=right >
				<span style="width:100%">
					* No. Guía&nbsp;
				</span>
			</TD>
            <TD width="320" style="width:30%">
				<TABLE width="320" style="width:30%" cellSpacing=0 cellPadding=0 border=0>
				  <TR>
				    <TD width="20">
						<span class=" " style="width: 31; height: 16">
						<bean:write name="sSiteId" scope="session" />
					</TD>
						<TD width="200">
							<div class="inputV2">
								<div class="group">       
									<html:text  styleClass="text"   size="17" maxlength="15" property="formnumber" style="width: 200; height: 22" onchange="validateFrmNo();" />
									<span class="highlight"></span>
									<span class="bar" ></span>
								</div>
							</div>
						</TD>
				  </TR>
				</TABLE>
            </TD>
            <TD width="100" align=right style="width:20%">
				* No. de Rastreo&nbsp;
            </TD>
            <TD width="200" align=left>
				<div class="inputV2">
					<div class="group">       
						<html:text styleClass="text"  property="guiaNumber" style="width: 200; height: 22" maxlength="15" onchange="validateGuiaNo();"/>
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			</TD>
     </TR>
  </TABLE>         
        <fieldset>
        <legend>Detalles Destino</legend>
         <TABLE cellSpacing=0 cellPadding=0 border=0  style="height: 18" class="tableRowSeparator">
          <TBODY>
	            <TR>
	              <TD align=right width="175" style="width:15% ; height: 18">Sito&nbsp;</TD>
	              <TD >
	  				<TABLE width="27" cellSpacing=0 cellPadding=0 border=0  style="width: 329; height: 18" class="tablaDetallev2">
	  				<tr>
	  				<td CLASS=" " width="39"  style="width:100%; height: 18;">
	  					<logic:present name="DestSiteName">
						<bean:write name="DestSiteName" scope="request" />
					</logic:present>&nbsp;
	  				</td>
	  				</tr>
	  				</table>
	              </TD>
	              </TR>
          <TR>
            <TD align=right width="175" style="width:15% ; height: 18">Sucursal&nbsp;</TD>
            <TD >
				<TABLE width="27" cellSpacing=0 cellPadding=0 border=0  style="width: 329; height: 18" class="tablaDetallev2">
				<tr>
				<td CLASS=" " width="39"  style="width:100%; height: 18;">
					<logic:present name="DestBrncName">
					    <bean:write name="DestBrncName" scope="request" />
					</logic:present>&nbsp;
				</td>
				</tr>
				</table>
            </TD>
            </TR>
          <TR>
            <TD align=right style="width:17% ; height: 18">Nombre de Cliente&nbsp;</TD>
            <TD >
				<TABLE width="70" cellSpacing=0 cellPadding=0 border=0  style="width: 475; height: 18" class="tablaDetallev2">
				<tr>
				<td CLASS=" " width="70"  style="width:100%; height: 18;">
					<logic:present name="DestClntName">
						<bean:write name="DestClntName" scope="request" />
					</logic:present>&nbsp;
			    </td>
			    </tr>
			    </table>
            </TD>
            
            </TR>

          <TR>
            <TD align=right style="width:7% ; height: 18">Fecha de Emisión&nbsp;</TD>
            <TD align=left style="width:68% ; height: 18">
               <TABLE cellSpacing=0 cellPadding=0 border=0 width=445 style="width:475 ; height: 18">
					<TBODY>
					<TR>
					  <TD >
						 <TABLE width="23" cellSpacing=0 cellPadding=0 border=0  style="width:180;" class="tablaDetallev2">
							<tr>
							<TD CLASS=" " width="23" style="width:180; height: 18;">
					  			<logic:present name="IssueDate">
								    <bean:write name="IssueDate" scope="request" />
								</logic:present>&nbsp;
					  		</TD>
					  		</TR>
					  	  </TABLE>
					  </TD>
					  <TD>&nbsp;&nbsp;&nbsp;&nbsp;</TD>
					  <TD align=right style="width:31% ; height: 18">Importe</TD>
					  <TD>&nbsp;</TD>
					  <TD style="width:165 ;">
					  
						<TABLE width="21" cellSpacing=0 cellPadding=0 border=0  style="width:148 ; height: 18" class="tablaDetallev2">
							<tr >
							<%
								Global global = (Global) session.getAttribute("sGlobal");
								boolean dispAmntFlag = false;
								if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
								dispAmntFlag = true;
								if(dispAmntFlag||(isGuiaAmount==null))	
								{
							%>
									<TD CLASS=" " width="19"  align="right" style="width:100%; height: 18;">&nbsp;
										<logic:present name="GuiaAmount">
											<bean:write name="GuiaAmount" scope="request" />
										</logic:present>
									</TD>
							<%
								}
								else 
								{
							%>
										<TD CLASS=" " width="19"  align="right" style="width:100%; height: 18;">
											<font size="4"><b>**********</b></font>
										</TD>
							<%
								}
							%>
					  		</TR>
					  	  </TABLE>
					  </TD>
					 </TR>
					</TBODY>
				</TABLE>
		</TD></TR></TBODY>
		</TABLE>
			</fieldset>
			
			<fieldset>
		<BR><legend>Embarque</legend> 

    
          
        <TABLE cellSpacing=1 cellPadding=1 style="width:100%" class="tablaDetallev2 tableRowSeparator">
          <TBODY>
            <TR class="tablaDetallev2Header"> 
              <Td width=35>Cantidad</Td>
              <Td width=250 >Descripción</Td>
              <Td>Contenido</Td>
            </TR>
            <%if( request.getAttribute("mrbServiceInfo")!= null &&  ! request.getAttribute("mrbServiceInfo").equals(""))
			 {
		   %>
            <%= request.getAttribute("mrbServiceInfo") %> 
            <%
			}else{
		   %>
 <TR class="tablaDetallev2Body"> 
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
 </TR>
 <TR> 
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
 </TR>
 <TR> 
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
 </TR>
 <TR> 
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
 </TR>
 <TR> 
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
 </TR>
 <TR> 
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
   <TD>&nbsp;</TD>
 </TR>
            <%
			}
		%>
          </TBODY>
        </TABLE>
        </fieldset>     
     </BLOCKQUOTE>
      <center>
		<html:submit styleClass="bookbutton buttonLarge"  value="Confirmar Cancelación" onclick='confirmCncl();' property="submitFrm" />		
		<html:hidden value="Cancellation" property="useraction" />
		<html:hidden property="docuType"/>
		<input type="hidden" name='showWeb' value='<bean:write name="loginForm" property ="showWeb"/>'/>
		</td></tr></table>   
     </html:form>
   </BODY>
</html:html>
