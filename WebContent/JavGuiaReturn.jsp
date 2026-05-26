<%--
Authur		:	JOSE ARMANDO
Date		:	05/07/2017

FileName	:	
FormBean	:	
ActionBean	:	
OtherClasses	:	none
CSS FileName	:	webbooking.css
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción:  
 * ------------------------------------------------------------------
--%>
<%@ page language="java" %>
<%@ page import="bean.Global"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html>
<HEAD>
	<TITLE>Guia Return</TITLE>
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
			document.forms[0].guiaNumber.select();
	  <%
		}
	  %>
	  	if(form.destBrncId.value.indexOf('70') > -1 || form.radFlag.value== '0'){
	  		//form.typeRecoleccion[1].disabled = true;	
	  	}else{
	  		form.typeRecoleccion[1].disabled = false;
	  	}
		if (form.guiaNumberReturn.value == '' && form.validado.value == '1') {
			form.submitFrm.disabled = false;
		}else {
			form.submitFrm.disabled = true;
		}
	}
	
	function isNumber(evt,txt) {
	    var charCode = (evt.which) ? evt.which : evt.keyCode
	    //Teclas flecha izq, der, abajo, arriba, Enter, punto y coma
	    if(charCode == 37 || charCode == 38 || charCode == 39 || charCode == 40 || charCode == 13 || charCode == 59){
	    	return true;			
	    }
	    if(charCode == 46){//PUNTO
	        return false;
	    }else if (charCode > 31 && (charCode < 48 || charCode > 57))
	       return false;
	    return true;
	 }
	
	function confirmCncl() {
		form=document.forms[0];		
		if(form.guiaNumber.value == ''){
			alert('CAPTURE EL NUMERO DE GUIA PARA GENERAR LA GUIA DE RETORNO');
			return false;
		}
		if (!emailValidate("emailDest", " en campo CORREO CLIENTE DESTINO ")) {					
			if (emailValidate("eMailDestCheck", "eMailDestText", " en campo CLIENTE DESTINO")) {
				continuar = true;
			}
		}
		if(form.guiaNumber.value != ''){
			if (confirm("ESTÁ UD. SEGURO(A) QUE DESEA GENERAR LA GUÍA DE RETORNO?")) {
				showBarraEspera('mensaje','body','Espere por favor...Generando Guía');
				form.useraction.value = "GenerateGuideRR";
				document.forms[0].submit();
			}				
		}		
	}

	function confirmNotificacion() {
		form=document.forms[0];		
		if(form.guiaNumberReturn.value == ''){
			alert('CAPTURE EL NUMERO DE GUIA PARA OBTENER LA GUIA DE RETORNO');
			return false;
		}
		if (!emailValidate("emailDest", " en campo CORREO CLIENTE DESTINO ")) {					
			if (emailValidate("eMailDestCheck", "eMailDestText", " en campo CLIENTE DESTINO")) {
				continuar = true;
			}
		}
		if(form.guiaNumberReturn.value != ''){
			form.useraction.value = "notificacion_form";
			document.forms[0].submit();			
		}	
	}
	
	/* verifica checkbox de emails para validacion de los mismos.*/
	function emailValidate(txtObj, msj) {
		var continuar = true;
		var continuaCiclo = true;

		if (eval("document.forms[0]." + txtObj).value != "") {
				var at = ";";
				var lat = eval("document.forms[0]." + txtObj).value.indexOf(at);
				var eMail = "";
				var cadena = "";
				if (lat == -1) {
					eMail = eval("document.forms[0]." + txtObj).value;
					continuaCiclo = false;
					if (!echeck(eMail, msj)) {
						eval("document.forms[0]." + txtObj).focus();
						continuar = false;
					}
				} else {
					eMail = eval("document.forms[0]." + txtObj).value
							.substring(0, lat);
					cadena = eval("document.forms[0]." + txtObj).value
							.substring(lat + 1);
				}

				while (continuaCiclo) {
					if (!echeck(eMail, msj)) {
						eval("document.forms[0]." + txtObj).focus();
						continuar = false;
						continuaCiclo = false;
					} else {
						lat = cadena.indexOf(at);
						if (lat == -1) {
							eMail = cadena;
							cadena = "";
						} else {
							eMail = cadena.substring(0, lat);
							cadena = cadena.substring(lat + 1);
						}

						if (eMail == "") {
							continuaCiclo = false;
						}
					}
				}

			}
			return continuar;
		}
	
	/*funcion para validar Emails capturados*/
	function echeck(str, msje) {
		var at = "@";
		var dot = ".";
		var lat = str.indexOf(at);
		var lstr = str.length;
		
		if (str.indexOf(" ") != -1) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +". No debe llevar espacios");
			return false;
		}
		
		if (str.indexOf(at) == -1) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +". Necesita un '@'.");
			return false;
		}
		
		if (str.indexOf(at) == -1 || str.indexOf(at) == 0
				|| str.indexOf(at) == lstr) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +". Necesita un Nombre de Usuario.");
			return false;
		}
		
		if (str.indexOf(dot) == -1 || str.indexOf(dot) == 0
				|| str.indexOf(dot) == lstr) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +". Falta nombre de Cuenta.");
			return false;
		}
		
		if (str.indexOf(at, (lat + 1)) != -1) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +".");
			return false;
		}
		
		if (str.substring(lat - 1, lat) == dot
				|| str.substring(lat + 1, lat + 2) == dot) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +".");
			return false;
		}
		
		if (str.indexOf(dot, (lat + 2)) == -1) {
			alert("Formato Invalido de E-mail '"+str+"'"+ msje +".");
			return false;
		}
		return true;
	}
		function validateGuiaNo() {
			form = document.forms[0];
			form.guiaNumber.value = trim(form.guiaNumber.value.toString()
					.toUpperCase());
			if (form.guiaNumber.value != '') {
				form.useraction.value = "validate_form";
				showBarraEspera('mensaje', 'body', 'Espere por favor...Validando Número de Rastreo');
				form.submit();
			}
		}
		function trim(val) {
			for (var i = 0; i < val.length && val.substr(i, 1) == ' '; i++)
				;
			for (var j = val.length - 1; j > -1 && val.substr(j, 1) == ' '; j--)
				;
			j++;
			if (i < j)
				val = val.substring(i, j);
			else
				val = "";
			return val;
		}

		function goOut(page) {
			if (page == "guiabooking") {
				page = "thispage";
				if (document.forms[0].showWeb.value != 'Y') {
					alert('USUARIO NO AUTORIZADO PARA OPCION ELABORACION DE GUIA DE RETORNO');
					return;
				}
			}
			if (confirm("DESEA PERDER LA INFORMACION?")) {
				if (page == 'shipmenthistory') {//AAP01
					document.forms[0].action = "shipmentHistory.do";//AAP01
				} else {//AAP01
					document.forms[0].action = "webBookinggeneralMain.do?includeattribute=yes&page="+ page;//AAP01	
				}//AAP01
				document.forms[0].submit();
			} else {
				return;
			}
		}
	function pulsar(e) { 
		tecla = (document.all) ? e.keyCode :e.which; 
		if(tecla ==13){
			validateGuiaNo();
		}
		return (tecla!=13); 
	} 
	//-->
	</SCRIPT>

</HEAD>
<BODY id="body" text=#000000 leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload="initScript(); mostrarCapaCentro('mensaje');" class="backgroundStandard">
<html:form action="guiaReturn.do">
	<table border="0" cellspacing="0" cellpadding="0" class="width100porcent backgroudBluePtx">
	  <tr>
	    <td>
	      <div class="bodyWidth marginAutoCentro">
	        <img src="images/logos/logoW.png" border="0" onclick="goOut('mainpage');hideMenu();" style="max-width: 138px;margin-top: 11px;margin-bottom: 9px;cursor: pointer;">
	          <a id="menu-hamburger" class="Animated1" onclick="showMenu()">
	            <div style="margin-top: 15px;">
	              <span id="menuline1" class="Animated05"></span> 
	              <span id="menuline2" class="Animated05"></span> 
	              <span id="menuline3" class="Animated05"></span>
	            </div>
	          </a>
	      </div>
	    </td>
	    <td>
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
  					<logic:equal name="loginForm" property="showPpg" value="Y">
							<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
		  			</logic:equal>
		          	<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de Guía</a>
		          	<a onclick="goOut('shipmenthistory');hideMenu();" style="">Histórico de Envíos</a>
		          	<a onclick="goOut('clientreport');hideMenu();" style="">Información del Cliente</a>
		          	<logic:present name="loginForm">
		          		<logic:equal name="loginForm" property="milliSeconds" value="">		
		          			<!-- si no viene de Customer central, se muestra cerrar sesion -->
		          			<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>
		          		</logic:equal>
		          	</logic:present>
		         </logic:equal>
	      </logic:present> 
	      <logic:present name="loginForm">
	        	<html:hidden name="loginForm" property="userValidate" />
	        	<logic:notEqual name="loginForm" property="userValidate" value="validUser">
	        	<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a> 
	    		<logic:equal name="loginForm" property="showWeb" value="Y">
	    			<logic:equal name="loginForm" property="newBooking" value="Y">
	    				<a onclick="goOut('guiabooking');hideMenu();" style="">Elaboración de guías</a>
  					</logic:equal>
  				</logic:equal> 
  				<html:hidden name="loginForm" property="showPpg"></html:hidden>
				<logic:equal name="loginForm" property="showPpg" value="Y">
						<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
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
		<td style="height: 8px;">
			<textarea id="mensaje" class="textareacontenido" cols="46" readonly="readonly" rows="4" onblur="self.focus();" 
			style="background-color: #EEEEEE; border-style: ridge; overflow: hidden; text-align: center; vertical-align: middle; z-index: 2; visibility: hidden; position: absolute; top: 450px; left: 200px; width: 350px; height: 80px">
			</textarea>
		</td>
	</tr>
	</table>
<jsp:include page="nocache.jsp" flush="false" />
	<table class="bodyWidth marginAutoCentro">
	<tr><td>
	<strong class="titleSection fontBold">Generación de Guía de retorno</strong>
	<BLOCKQUOTE>
	<TABLE cellSpacing=0 cellPadding=0 border=0 >
		<TR>
			<TD width="100" align=right style="width:20%">* No. de Rastreo&nbsp;
			</TD>
			<TD width="200" align=left>
				<div class="inputV2">
					<div class="group">       
						<html:text styleClass="text"  property="guiaNumber" onkeypress="return pulsar(event)" style="width: 200; height: 22" maxlength="15" onchange="validateGuiaNo();"/>
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			</TD>
			<TD width="100" align=right style="width:20%">No. de Rastreo de Retorno&nbsp;
			</TD>
			<TD width="200" align=left>
				<TABLE width="27" cellSpacing=0 cellPadding=0 border=0  style="width: 100%; height: 18" class="tablaDetallev2">
		  		<tr>
		  			<td  width="39" style="width:100%; height: 18;">
			  			<logic:present name="guiaNumberReturn">
							<bean:write name="guiaNumberReturn" scope="request" />
						</logic:present>&nbsp;
			  		</td>
		  		</tr>
		  		</table>
			</TD>
	    </TR>
	</TABLE>
	<fieldset>
	  	<legend>Detalle Origen</legend>
	  		<TABLE cellSpacing=0 cellPadding=0 border=0  style="height: 18; width: 100%;" class="tableRowSeparator">
	  			<TBODY>
	  				<TR>
	  					<TD align=right width="175" style="width:15% ; height: 18">Sitio&nbsp;</TD>
		              	<TD >
		  					<TABLE width="27" cellSpacing=0 cellPadding=0 border=0  style="width: 100%; height: 18" class="tablaDetallev2">
		  						<tr>
			  						<td  width="39" style="width:100%; height: 18;">
			  							<logic:present name="OrgnSiteName">
											<bean:write name="OrgnSiteName" scope="request" />
										</logic:present>&nbsp;
			  						</td>
		  						</tr>
		  					</table>
		              	</TD>
	            		<TD width="175" align=right style="width:15% ;height: 18">Sucursal&nbsp;</TD>
	            		<TD align=left>
							<TABLE width="27" cellSpacing=0 cellPadding=0 border=0  style="width: 100%; height: 18" class="tablaDetallev2">
								<tr>
									<td width="39"  style="width:100%; height: 18;">
										<logic:present name="OrgnBrncName">
						    				<bean:write name="OrgnBrncName" scope="request" />
										</logic:present>&nbsp;
									</td>
								</tr>
							</table>
	            		</TD>
	            	</TR>
	            	<TR>
	            		<TD align=right style="width:17% ; height: 18">Nombre de Cliente&nbsp;</TD>
	            		<TD colspan="3">
							<TABLE width="70" cellSpacing=0 cellPadding=0 border=0  style="width: 100%; height: 18" class="tablaDetallev2">
								<tr>
									<td CLASS=" " width="70"  style="width:100%; height: 18;">
										<logic:present name="OrgnClntName">
											<bean:write name="OrgnClntName" scope="request" />
										</logic:present>&nbsp;
				    				</td>
				    			</tr>
				    		</table>
	            		</TD>
	            	</TR>
				</TBODY>
	  		</TABLE>
	</fieldset>
	<fieldset>
	  	<legend>Detalle Destino</legend>
	  		<TABLE cellSpacing=0 cellPadding=0 border=0  style="height: 18" class="tableRowSeparator">
	  			<TBODY>
	  				<TR>
	  					<TD align=right width="175" style="width:15% ; height: 18">Sitio&nbsp;</TD>
		              	<TD >
		  					<TABLE width="27" cellSpacing=0 cellPadding=0 border=0  style="width: 100%; height: 18" class="tablaDetallev2">
		  						<tr>
			  						<td  width="39" style="width:100%; height: 18;">
			  							<logic:present name="DestSiteName">
											<bean:write name="DestSiteName" scope="request" />
										</logic:present>&nbsp;
			  						</td>
		  						</tr>
		  					</table>
		              	</TD>
	            		<TD width="175" align=right style="width:15% ;height: 18">Sucursal&nbsp;</TD>
	            		<TD align=left>
							<TABLE width="27" cellSpacing=0 cellPadding=0 border=0  style="width: 100%; height: 18" class="tablaDetallev2">
								<tr>
									<td width="39"  style="width:100%; height: 18;">
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
	            		<TD colspan="3">
							<TABLE width="70" cellSpacing=0 cellPadding=0 border=0  style="width: 100%; height: 18" class="tablaDetallev2">
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
		            	<TD colspan="3" align=left style="width:68% ; height: 18">
		               		<TABLE cellSpacing=0 cellPadding=0 border=0 width=445 style="width:475 ; height: 18">
								<TBODY>
									<TR>
							  			<TD>
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
												<tr>
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
						</TD>
					</TR>
				</TBODY>
	  		</TABLE>
	</fieldset>
	<fieldset>
	<BR>
	<legend>Embarque</legend> 
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
	<fieldset>
	<legend>Información adicional para el retorno</legend> 
	  	<TABLE cellSpacing=0 cellPadding=0 border=0  style="height: 18" class="tableRowSeparator">
	  		<TBODY>
				<TR> 
					<td width="100" align=left style="width:20%" class="LabelForm" colspan="2">
					PISO/RECOLECCION:
						<html:select size="1" property="typeRecoleccion">
							<html:option value="1">PISO</html:option>
							<html:option value="0">RECOLECCÍON</html:option>
						</html:select></td>
		        </TR>
				<TR> 
					<td colspan="3" width="100%" style="width:100%" class="LabelForm" colspan="2">CORREO CLIENTE DESTINO (PARA ENVIAR A MÁS DE 1, FAVOR DE SEPARARLOS CON ";"):</td>
				<TR>
					<TD width="200" align=left colspan="3">
						<div class="inputV2">
							<div class="group">       
								<html:text styleClass="text"  property="emailDest" style="width: 100%; height: 22" maxlength="150"/>
								<span class="highlight"></span>
								<span class="bar" ></span>
							</div>
						</div>
					</TD>
		        </TR>
				<TR> 
					<td class="LabelForm"  style="width:200px !important">TELEFONO (CELULAR) CLIENTE DESTINO:</td>
					<TD width="200" align=left>
						<div class="inputV2">
							<div class="group">       
								<html:text styleClass="text" onkeypress="return isNumber(event, this)" property="phoneDest" style="width: 200; height: 22" maxlength="30"/>
								<span class="highlight"></span>
								<span class="bar" ></span>
							</div>
						</div>
					</TD>
		        </TR>
	  		</TBODY>
	  	</TABLE>
	</fieldset>
	</BLOCKQUOTE>
	<center>
	<html:button styleClass="bookbutton buttonLarge"  value="Generar guía retorno" onclick='confirmCncl();' property="submitFrm" />
	<html:button styleClass="bookbutton buttonLarge"  value="Enviar guía retorno" onclick='confirmNotificacion();' property="submitFrmNotification" />			
	<html:hidden property="useraction" />
	<html:hidden property="docuType"/>
	<html:hidden property="destBrncId"/>
	<html:hidden property="guiaNumberReturn"/>
	<html:hidden property="radFlag"/>
	<html:hidden property="orgnClntName"/>
	<html:hidden property="orgnClntClave"/>
	<html:hidden property="destClntName"/>
	<html:hidden property="destBrncName"/>
	<html:hidden property="destSiteName"/>
	<html:hidden property="orgnBrncName"/>
	<html:hidden property="orgnSiteName"/>
	<html:hidden property="destClntName"/>
	<html:hidden property="destClntClave"/>
	<html:hidden property="prepBrncId"/>
	<html:hidden property="issueDate"/>
	<html:hidden property="guiaAmount"/>
	<html:hidden property="mrbServiceInfo"/>
	<html:hidden property="validado"/>
	<input type="hidden" name='showWeb' value='<bean:write name="loginForm" property ="showWeb"/>'/>
	</td>
	</tr>
	</table>
	</html:form>
	</BODY>
</html:html>
