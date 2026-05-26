<%@page import="java.text.Normalizer, java.util.ArrayList, java.util.LinkedHashMap, beanUtil.GetBrnchOcurre, mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse, paquetexpress.internal.prepaid.conversion.form.ConversionForm"%>
<%@ page info="WebBooking - General"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%String version = "00.00.19";
	ConversionForm cform = (ConversionForm) session.getAttribute("invoiceConversionForm");%>
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<title>Documentar rastreo de prepago de sobre</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<LINK href="Styles.css?v=<%=version%>" type="text/css" rel="StyleSheet">
<script type="text/javascript" src="GuiaConversion.js?v=<%=version%>"></script>
<script type="text/javascript">	
	function focus() {
		if(<%=session.getAttribute("zoneFocus")%> == '1') {
			document.forms[0].destclave.focus();
		}
	}
	
	function descargaFactura(params) {	
		//alert('GenCartaPorte?trackingNoGen='+document.forms[0].trackingNoGen.value);
		link ='GetFacturaGenerada?'+params;
		window.open(link, '_blank');
	}					
</script>
 
<script type="text/javascript">
function goOut(page){	
 	document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page="+page;
	document.forms[0].submit();	 
}
</script>
<link rel="stylesheet" media="screen" type="text/css" href="css/font-awesome.min.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css?v=<%=version%>">
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
<script type="text/javascript" src="js/jquery/1.12.0.js?v=<%=version%>"></script>
<script type="text/javascript" src="barraEspera.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/v2/global.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/ToastNotification.js?v=<%=version%>"></script>

</head>
<body id="body" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onpageshow="init();" onunload="clearGuias();" class="backgroundStandard">
<html:form name="invoiceConversionForm"
	type="paquetexpress.internal.prepaid.conversion.form.ConversionForm"
	action="guiaconversion">
	
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
		
		<div class="MenuOverlay displayNone" style="z-index:1;">
		
	        <a class="btnCloseMenu" onclick="hideMenu()">
	            <div>
	                <span class="lineOne"></span>
	                <span class="lineTwo"></span>
	            </div>
 	        </a>
  
  			<div class="itemsMenu marginAutoCentro bodyWidth">
  			
	  			<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a> 
	  			 
	  			<logic:present name="admin">
	  				<a onclick="changebranch();hideMenu();" style="">Cambiar de Sucursal</a> 				 
			 	</logic:present> 
			 	
			 	<a onclick="Regresar();hideMenu();" style="">Regresar</a>
				<logic:present name="loginForm">
					<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
						<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>
					</logic:equal>
				</logic:present>
								
			</div>
 			
		</div>		
	
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
<!-- 		<table width="100%" border="0" cellspacing="0" cellpadding="0"> -->
<!-- 			<tr> -->
<!-- 				<td> -->
<!-- 				<table width="100%" border="0" cellspacing="0" cellpadding="0"> -->
<!-- 					<tr> -->
<!-- 						<td width="53%"><img src="images/mainTop.jpg" height="74"></td> -->
<!-- 						<td width="47%"> -->
<!-- 						<table width="100%" border="0" cellspacing="0" cellpadding="0"> -->
<!-- 							<tr> -->
<!-- 								<td width="52%"> -->
<!-- 								<div align="right"><strong><font color="#000066" -->
<!-- 									size="2" face="Arial">Nombre&nbsp;&nbsp;</font></strong></div> -->
<!-- 								</td> -->
<!-- 								<td width="2%"><strong><font color="#000066" -->
<!-- 									size="2" face="Arial">:</font></strong></td> -->
<!-- 								<td width="46%"> -->
<!-- 									<div align="left"> -->
<!-- 										<font color="#990000" size="2" face="Verdana"> -->
<%-- 											<logic:present name="sGlobal"> --%>
<%-- 												<bean:write name="sGlobal" property ="origenUserNombre"/>											 --%>
<%-- 											</logic:present> --%>
<!-- 										</font> -->
<!-- 									</div> -->
<!-- 								</td> -->
<!-- 							</tr> -->
<!-- 							<tr> -->
<!-- 								<td> -->
<!-- 								<div align="right"><strong><font color="#000066" -->
<!-- 									size="2" face="Arial">Sucursal&nbsp;</font></strong></div> -->
<!-- 								</td> -->
<!-- 								<td width="2%"><strong><font color="#000066" -->
<!-- 									size="2" face="Arial">:</font></strong></td> -->
<!-- 								<td> -->
<!-- 								<div align="left"><font color="#990000" size="2" -->
<%-- 									face="Verdana"><logic:present name="branchid"> --%>
<%-- 									<bean:write name="branchName"/> --%>
<%-- 								</logic:present></font></div> --%>
<!-- 								</td> -->
<!-- 							</tr> -->
<!-- 							<tr> -->
<!-- 								<td COLSPAN="3"> -->
<%-- 								<p align="right"><logic:present name="admin"> --%>
<!-- 									<INPUT TYPE="BUTTON" VALUE="Cambiar de Sucursal" -->
<!-- 										class="buttondisplay1" ONCLICK="changebranch();"> -->
<%-- 								</logic:present> <!--INPUT TYPE="BUTTON" VALUE="Terminar Sesión" --%>
<!-- 									class="buttondisplay" ONCLICK="logout();"  
<!-- 									TYPE="BUTTON" VALUE=" Menú Principal" class="buttondisplay" -->
<!-- 									onclick="mainpage();"> -->
<!-- 									<INPUT -->
<!-- 									TYPE="BUTTON" VALUE="Regresar" class="buttondisplay" onclick="Regresar();"> -->
<!-- 								</td> -->
<!-- 							</tr> -->
<!-- 						</table> -->
<!-- 						</td> -->
<!-- 					</tr> -->
<!-- 				</table> -->
<!-- 				</td> -->
<!-- 				<td>&nbsp;</td> -->
<!-- 			</tr> -->

<!-- 		</table> -->
		<logic:present name="error">
			<table width="100%" border="1" cellspacing="0" cellpadding="0" ALIGN="CENTER">
				<tr>
					<td class="LabelError" align="center"><bean:write name="error"/></td>
				</tr>
			</table>
		</logic:present>
		<logic:present name="invoiceConversionForm" property="facturaMod">
			<logic:notEqual name="invoiceConversionForm" property="facturaMod" value="">			
			<table width="100%" border="1" cellspacing="0" cellpadding="0" ALIGN="CENTER">				
				<tr>
					<td align="center" class="LabelError">
						Factura Generada<br>												
						<a href="javascript:descargaFactura('<bean:write name="invoiceConversionForm" property="facturaMod"/>');" class="linkFact">
			           		Descargar Factura
			            </a>
					</td>
				</tr>				
			</table>
			</logic:notEqual>
		</logic:present>

		<table align="center" class="bodyWidth marginAutoCentro">
			<tr>
				<td>
				   <h2 class="titleSection fontBold">Documentar Rastreo de Prepago de Sobres</h2>
				</td>
			</tr>
			<tr>
			<td >
		  
			 <div>
				 <strong><font color="#000066"
						size="2" face="Arial">Nombre&nbsp;&nbsp;</font></strong> 
										
				<font color="#990000" size="2" face="Verdana">
					<logic:present name="sGlobal">
						<bean:write name="sGlobal" property ="origenUserNombre"/>											
					</logic:present>
				</font>
			 </div>
			 <div>
			<strong><font color="#000066"
					size="2" face="Arial">Sucursal&nbsp;</font></strong>

			<font color="#990000" size="2" face="Verdana">
			     <logic:present
					name="branchid">
					<bean:write name="branchName" />
				</logic:present>
			</font>
 
	
 					 
			</div>
			<table>
				<tr>
					<td width="100"></td>
					<td width="500">
					<p align="center" class="LabelForm" valign="top">
						Centro de costo:
					 	<html:select property="centrosCosto" styleClass ="textinputmedium" onchange="cambioCentroCosto();"><!-- AAP05 -->
							<html:options property="centrosCostoValue" labelProperty="centrosCostoLabel"/><!-- AAP05 -->									
						</html:select><!-- AAP05 -->
					</p>
					</td>
					<td>
					<p align="center" class="LabelForm" valign="top">Número de
						Rastreo:&nbsp;</p>
					</td>
					<td>
					
					   <div class="inputV2">
						  <div class="group">      
						   
							<html:text property="trackingNo" styleClass="textinput" readonly="true"/>
								
							  <span class="highlight"></span>
						      <span class="bar"></span>
						  </div>
					    </div>						
					
					</td>
					<td><input type="button" VALUE="Ver Rastreo"
						onclick="loaddetail();" class="buttonnormal buttonLarge" colspan="3"
						styleClass="width: 48; height: 22"></td>
				</tr>
			</table>
			<table class="marginAutoCentro bodyWidth">
				<tr>
					<td style="height: 8px;">
						<textarea id="mensaje" class="textareacontenido" cols="46" readonly="readonly" rows="4" onblur="self.focus();" style="background-color: #EEEEEE; border-style: ridge; 
							overflow: hidden; text-align: center; vertical-align: middle; z-index: 2; visibility: hidden; position: absolute; top: 450px; left: 200px; width: 350px; height: 80px">
						</textarea>
					</td>
				</tr> 
			</table>
			<logic:present name="shippingtypes">
				<logic:present name="origin">
					<p align="center" class="LabelForm" valign="top">Cliente: <html:text
						property="clientId" styleClass="textinput" readonly="true" />
					Nombre: <html:text property="clientName" styleClass="textboxbig"
						readonly="true" />
				</logic:present>
				<logic:notPresent name="origin">
					<p align="center" class="LabelForm" valign="top">Cliente: <html:text
						property="clientId" styleClass="textinput" /> Nombre: <html:text
						property="clientName" styleClass="textboxbig" /> <input
						type="button" VALUE="Cliente"
						onclick="openLov('sipwebclientonly');" class="buttonnormal buttonMedium"
						colspan="3" styleClass="width: 48; height: 22">
				</logic:notPresent>

				<table width="" align="center" class="width100porcent " >
					<td width="">
					<fieldset class="SubHead"><legend class="SubHead">
					Datos Fiscales </legend>
					<table width="808" height="58">
						<tr>
							<td width="205" height="25">
							<p align="right" class="LabelForm">Calle:
							</td>
							<td width="150" height="25">
							
							   <div class="inputV2">
								  <div class="group">      
								   
										<html:text property="fiscal1"
										styleClass="textinput" readonly="true" />
										
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>							
							

								</td>
							<td width="104" height="25">
							<p align="right" class="LabelForm">Número:
							</td>
							<td width="179" height="25">
							
							   <div class="inputV2">
								  <div class="group">      
								   
									<html:text property="fiscal2"
										styleClass="textinput" readonly="true" />
										
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>								

								
								</td>
							<td width="164" height="25" class="LabelForm">
							<p align="right">&nbsp;&nbsp;&nbsp;&nbsp;R.F.C:</p>
							</td>

							<td width="247" height="25" class="LabelForm">
							
							   <div class="inputV2">
								  <div class="group">      
								   
										<html:text
										property="fiscalrfc" styleClass="textinput" readonly="true" />
										
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>								
							

								</td>
							<td width="50" height="25" />
						</tr>
						<tr>
							<td width="181" height="25">
							<p align="right" class="LabelForm">Teléfono:
							</td>
							<td width="148" height="25">
							
							
							   <div class="inputV2">
								  <div class="group">      
								   
										 <html:text
											property="fiscaltelefono" styleClass="textinput" readonly="true" />
										
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>	
							   
							 </td>
							<td width="104" height="25">
							<p align="right" class="LabelForm">Ciudad:
							</td>
							<td width="179" height="25">
							
							   <div class="inputV2">
								  <div class="group">      
								   
										<html:text
										property="fiscalcolonia1" styleClass="textinput" readonly="true" />
																		
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>								

								</td>
							<td width="164" height="25">
							<p align="right" class="LabelForm">Colonia:
							</td>

							<td width="247" height="25">
						 
							
							   <div class="inputV2">
								  <div class="group">      
								   
										<html:text property="fiscalcolonia2"
											styleClass="textinput" readonly="true" />
																		
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>								
							

								 
							</td>
							<td width="50" height="25" >
							
							<logic:notPresent name="origin">	
							<INPUT TYPE="BUTTON"
								name="fiscal" VALUE="Dirección Fiscal" class="buttonnormal"
								onclick="openLov('sipwebfiscaladdress');" colspan="3"
								styleClass="width: 55; height: 22">
							</logic:notPresent>
							</td>
								
							<td width="50" height="25" />
							<td width="50" height="25" />
						</tr>
					</table>

					</fieldset>
				</table>
				<table width="" align="center" class="width100porcent " >
					<td width="">
					<fieldset class="SubHead"><legend class="SubHead">
					Datos del Remitente </legend>

					<table width="846" height="58">
						<tr>
							<td width="320" height="25">
							<p align="right" class="LabelForm">Calle:
							</td>
							<td width="148" height="25">
							   
							   <div class="inputV2">
								  <div class="group">      
								   
									   <html:text property="orgien1"
										styleClass="textinput" readonly="true" />
																		
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>	
							 
								</td>
							<td width="140" height="25">
							<p align="right" class="LabelForm">Número:
							</td>
							<td width="170" height="25">
							
							   <div class="inputV2">
								  <div class="group">      
											   
										<html:text property="orgien2"
											styleClass="textinput" readonly="true" />
																													
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>								
							

								</td>
							<td width="240" height="25" class="LabelForm">
							<p align="right">R.F.C:</p>
							</td>
							<td width="120" height="25" class="LabelForm">
							<p align="left">
							
							   <div class="inputV2">
								  <div class="group">      
											   
										<html:text property="orgienrfc"
											styleClass="textinput" readonly="true" />
																													
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>									
							 
							</td>
							<td width="150" height="25" />
						</tr>
						<tr>
							<td width="181" height="25">
							<p align="right" class="LabelForm">Teléfono:
							</td>
							<td width="148" height="25">
							
							   <div class="inputV2">
								  <div class="group">      
											   
										<html:text
										property="orgientelefono" styleClass="textinput" readonly="true" />
																													
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>								
							
							</td>
							<td width="125" height="25">
							<p align="right" class="LabelForm">Ciudad:
							</td>
							<td width="170" height="25">
							
							   <div class="inputV2">
								  <div class="group">      
											   
										<html:text
										property="orgiencolonia1" styleClass="textinput" readonly="true" />
																													
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>								
							 
							</td>
							<td width="171" height="25">
							<p align="right" class="LabelForm">Colonia:
							</td>
							<td width="150" height="25">
							
							   <div class="inputV2">
								  <div class="group">      
											   
										<html:text
										property="orgiencolonia2" styleClass="textinput" readonly="true" />
																													
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>							
							</td>
							<logic:notPresent
								name="origin">
								<td width="50" height="25">
								<p align="right">
							<!-- 
							<INPUT TYPE="BUTTON" name="origin"
								VALUE="Origen" class="buttonorgin buttonMedium"
								onclick="openLov('sipweborginaddress');" colspan="3"
								styleClass="width: 55; height: 22"> 
								 -->
								</td>
							</logic:notPresent> 
						</tr>
					</table>

					</fieldset>
				</table>
				<table class="width100porcent " align="center">
					<td >
					<fieldset class="SubHead"><legend class="SubHead">
					Datos del Destinatario </legend>
					<table border="0" width="806">
						<tr>
							<td width="8%" class="LabelForm" align="right">Plaza
							Destino:</td>
							<td width="11%" align="right">
							
							   <div class="inputV2">
								  <div class="group">      
											   
										<html:text property="destSite"
										styleClass="textsmall" onchange="clearDestData();"/>
																													
									  <span class="highlight"></span>
								      <span class="bar"></span>
								  </div>
							    </div>							
							
							</td>
							<td width="21%" align="right">
							
								   <div class="inputV2">
									  <div class="group">      
												   
											<html:text
											property="destSiteName" styleClass="textinput" onchange="clearDestData();"/>
																														
										  <span class="highlight"></span>
									      <span class="bar"></span>
									  </div>
								    </div>								
							 
								</td>
							<td width="11%"><INPUT TYPE="BUTTON" name="origin"
								VALUE="Plaza" class="buttonnormal buttonMedium" onclick="openLov('branchPP')"
								colspan="3" styleClass="width: 55; height: 22"></td>
							<td width="17%" class="LabelForm" align="right">&nbsp;Cliente:</td>
							<td width="16%">
							
							   <div class="inputV2">
									  <div class="group">      
												   
											<html:text property="destclave"
											styleClass="textsmall" onfocus="zoneverify();" onchange="clearDestData2();"/>
																														
										  <span class="highlight"></span>
									      <span class="bar"></span>
									  </div>
								    </div>	
								    							
							</td>
							<td width="51%"><html:text property="destnombre"
								styleClass="textinputmedium" onfocus="zoneverify();" onchange="clearDestData2();"/></td>
							<td width="79%"><INPUT TYPE="BUTTON" name="origin"
								VALUE="Cliente" class="buttonnormal buttonMedium"
								onclick="openLov('clientwithsite')" colspan="3"
								styleClass="width: 55; height: 22"></td>
							<td width="1%" align="center">
								<html:button property="destinationclientBut" styleClass="button1 buttonMedium"
										value="Registrar" onclick="openLov('cliententry');" />
							</td>
						</tr>
					</table>

					<table width="880" height="58">
						<tr>
							<td width="275" height="25">
							<p align="right" class="LabelForm">Calle:
							</td>
							<td width="148" height="25">
							
							   <div class="inputV2">
									  <div class="group">      
												   
										<html:text property="dest1"
											styleClass="textinput" readonly="true" />
																														
										  <span class="highlight"></span>
									      <span class="bar"></span>
									  </div>
								    </div>	
								    							
							</td>
							<td width="105" height="25">
							<p align="right" class="LabelForm">Número:
							</td>
							<td width="170" height="25">
							
						   			<div class="inputV2">
									  <div class="group">      
												   
											<html:text property="dest2"
										styleClass="textinput" readonly="true" />
																														
										  <span class="highlight"></span>
									      <span class="bar"></span>
									  </div>
								    </div>								
							
							</td>
							<td width="160" height="25" class="LabelForm">
							<p align="right">R.F.C:</p>
							</td>
							<td width="120" height="25" class="LabelForm">
							<p align="right">
							
						   			<div class="inputV2">
									  <div class="group">      
								
							<html:text property="destrfc"
								styleClass="textinput" readonly="true" />
																														
										  <span class="highlight"></span>
									      <span class="bar"></span>
									  </div>
								    </div>								
					
							</td>
							<td width="160" height="25" />
						</tr>

						<tr>
							<td width="275" height="25">
							<p align="right" class="LabelForm">Teléfono:
							</td>
							<td width="148" height="25">
							
						   			<div class="inputV2">
									  <div class="group">      
								
										<html:text
										property="desttelefono" styleClass="textinput" readonly="true" />
																														
										  <span class="highlight"></span>
									      <span class="bar"></span>
									  </div>
								    </div>								
							
							</td>
							<td width="105" height="25">
							<p align="right" class="LabelForm">Ciudad:
							</td>
							<td width="170" height="25">
							
						   			<div class="inputV2">
									  <div class="group">      
								
										<html:text
								property="destcolonia1" styleClass="textinput" readonly="true" />
																														
										  <span class="highlight"></span>
									      <span class="bar"></span>
									  </div>
								    </div>								
							
							</td>
							<td width="160" height="25">
							<p align="right" class="LabelForm">Colonia:
							</td>
							<td width="120" height="25">
							<p align="right">
							
						   			<div class="inputV2">
									  <div class="group">      
								
										<html:text property="destcolonia2"
										styleClass="textinput" readonly="true" />
																														
										  <span class="highlight"></span>
									      <span class="bar"></span>
									  </div>
								    </div>								
							
							
							</td>
							<td width="160" height="25">
							<p align="left"><INPUT TYPE="BUTTON" name="origin"
								VALUE="Dirección" class="buttondir buttonLarge"
								onclick="openLov('destinationaddressPP')" colspan="3"
								styleClass="width: 55; height: 22">
							</td>
						</tr>
					</table>
					<table id="divRefDom" style="visibility: hidden; position: absolute; width:880px; border: 0px; padding: 0px; border-spacing: 0px;"><!-- AAP02 -->					
						<tr>
							<td>
								<p align="right" class="LabelForm">Referencia de Dirección:
							</td>
							<td><html:textarea
								property="destRefDom" styleClass="textArea" cols="70" rows="2"/>
							</td>							
						</tr>
					</table><!-- AAP02 -->
					</fieldset>
				</table>
				<table id="msjeAvi" align="center">
					<tr>
						<td></td>
					</tr>
				</table>				
				<table width="900" align="center">
				<tr>
					<td width="980">
					<fieldset class="SubHead"><legend class="SubHead">
					Detalle del Rastreo </legend>

					<table width="801">
						<tr>
							<td class="LabelForm" align="left" width="8%">Entrega:</td>
							<td align="left" width="15%"><html:select property="deliveryType" multiple="true"
								size="3" styleClass="te	xtinput" onchange="verifyDelvryType()">
								<html:options name="delvryTypeCode" labelName="delvryTypeDesc" />
							</html:select>
							</td>
							<td width="50%" class="LabelForm" align="left">Opciones Ocurre:
								<html:checkbox property="opcOcurre"  onchange="changeDeliveryOcurre('')" disabled="true"/>
								<html:select property="brnchOcurre" disabled="true" onchange="changeSelectedAddr(document.forms[0].brnchOcurre.value)">
									<%if (cform.getOpcOcurre() && cform.getBrnchOcurre().length() > 0){ %>
										<option value="<%=cform.getBrnchOcurre()%>" selected="selected"><%=cform.getBrnchOcurre().contains("|") ? cform.getBrnchOcurre().split("\\|")[4] : "" %></option>
									<%} %>
									<%
									if (cform.getDestcode() != "" && cform.getDeliveryType().equalsIgnoreCase("O")){
										for (BranchDetailDTOResponse sucursal : cform.getFilteredBrnch()) {
											String addr = sucursal.calle + " " + sucursal.numero + " " + sucursal.colonia + " "+ sucursal.ciudad + " C.P. " + sucursal.codigoPostal;
											addr = Normalizer.normalize(addr, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
											String selectData = sucursal.clave.concat("|").concat(sucursal.estado).concat("|").concat(addr).concat("|").
													concat(sucursal.localizaLatitud).concat("%2C").concat(sucursal.localizaLongitud).concat("|").
													concat(sucursal.nombre + " " + sucursal.clave.substring(3, 5));
											if (cform.getOpcOcurre()){
												if (cform.getBrnchOcurre().equalsIgnoreCase("")){
									%>				<option title="<%=addr%>" value="<%=selectData%>"><%=sucursal.nombre + " " + sucursal.clave.substring(3, 5) %></option>
									<%			} else if (cform.getBrnchOcurre().length() > 0 && cform.getBrnchOcurre().contains("|") && !cform.getBrnchOcurre().split("\\|")[4].equalsIgnoreCase(sucursal.nombre + " " + sucursal.clave.substring(3, 5))){
									%>				<option title="<%=addr%>" value="<%=selectData%>"><%=sucursal.nombre + " " + sucursal.clave.substring(3, 5) %></option>					
									<%			}
											}else{break;}
										}
									}
									%>
								</html:select>
								<a id="btnSucursales" hidden="hidden" class="buttonMedium tagButton" title="Ver sucursales" onclick="searchBrnch(document.forms[0].brnchOcurre.value)"><i class="fa fa-map-marker"></i></a>
							</td>
							<td>
							<tr>
							</tr>
							<table>
								<tr>
									<td width="60">&nbsp;</td>
									<td align="right">
										<a id="addr" onclick="brnchOnMap(document.forms[0].brnchOcurre.value)" style="text-decoration: underline; color: #2196F3"></a>
									</td>
								</tr>
							</table>
							
							<table>
								<tr>
									<td>
										<table>
											<tr>
												<td class="LabelForm" align="right">Zona:</td>
												<td width="159">
									   			<div class="inputV2">
												  <div class="group">
													<html:text property="zone" styleClass="textinput" readonly="true" style="width: 70px;"/>
													  <span class="highlight"></span>
												      <span class="bar"></span>
												  </div>
											    </div>
												</td>
												<td class="LabelForm" align="right" style="padding-left: 10px;">Peso<em style="color: gray;">(kg)</em>:</td>
												<td width="159">
										   			<div class="inputV2">
													  <div class="group">
														<html:text property="weight" onblur="updateBrnchOcu()" styleClass="textinput" readonly="true" style="width: 70px;" />
														  <span class="highlight"></span>
													      <span class="bar"></span>
													  </div>
												    </div>
												</td>
												<td  class="LabelForm" align="right" style="padding-left: 10px;">Volumen:</td>
												<td width="159">
										   			<div class="inputV2">
													  <div class="group">
														<html:text property="volume" styleClass="textinput" readonly="true" style="width: 70px;" />
														  <span class="highlight"></span>
													      <span class="bar"></span>
													  </div>
												    </div>
												</td>
												<td class="LabelForm" align="right" style="padding-left: 10px;">Largo<em style="color: gray;">(cm)</em>:</td>
												<td width="159">
										   			<div class="inputV2">
													  <div class="group">
														<html:text property="volL" onkeypress="return isNumber(event, this)" onblur="calcVolWeight();updateBrnchOcu()" styleClass="textinput" style="width: 70px;" />
														  <span class="highlight"></span>
													      <span class="bar"></span>
													  </div>
												    </div>
												</td>
												<td width="100" class="LabelForm" align="right"  style="padding-left: 10px;">Alto<em style="color: gray;">(cm)</em>:</td>
												<td width="159">
										   			<div class="inputV2">
													  <div class="group">
														  <html:text property="volH" onkeypress="return isNumber(event, this)" onblur="calcVolWeight();updateBrnchOcu()" styleClass="textinput" style="width: 70px;" />
														  <span class="highlight"></span>
													      <span class="bar"></span>
													  </div>
												    </div>
												</td>
												<td width="100" class="LabelForm" align="right"  style="padding-left: 10px;">Ancho<em style="color: gray;">(cm)</em>:</td>
												<td width="159">
										   			<div class="inputV2">
													  <div class="group">
														  <html:text property="volW" onkeypress="return isNumber(event, this)" onblur="calcVolWeight();updateBrnchOcu()" styleClass="textinput" style="width: 70px;" />
														  <span class="highlight"></span>
													      <span class="bar"></span>
													  </div>
												    </div>
												</td>
											<tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table>
											<tr>
												<td  width="50" class="LabelForm" align="right">Descripción:</td>
												<td>
													<html:select property="packType" styleClass="textinput">
														<html:options name="packTypeName" labelName="packTypeLabel" />
													</html:select>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
										<td  >
											<table>
												<tr>
													<td width="100" class="LabelForm" align="right">Contenido:</td>
												<td width="350">
													<html:text property="content" styleClass="textinput" style="width: 250" maxlength="50"/>
												</td>
													<td width="20px"><div align="right">Cat. productos:</div></td>
													<td > 
														
														<div class="inputV2" style="width: 250px;">
														  <div class="group">       
															 <html:text styleClass="textcontenido" property="productDescSat" onblur="setBack('true');" onfocus="setBack('false');" onkeydown="openKeyLov('catProducts')" onkeypress="openKeyLov('catProducts')" style="width: 100%;" maxlength="100"/>
																												      
														      <span class="highlight"></span>
														      <span class="bar"></span>
														   </div>
													    </div>
													</td>
													<td>
														<html:button property="lovbutton3" value=". . ."
														styleClass="lov buttonMore" onclick="openLov('catProducts')" />
													</td>
													<td width="100" class="LabelForm">Tipo de Envío:</td>
													<td>
														<html:select property="shippingType" multiple="true" size="2" styleClass="textinput">
															<html:options name="shippingtypes" />
														</html:select>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								<tr>
								    <td width="450" class="LabelForm" align="left">Incluye Zona Ext.:
								    <html:checkbox property="extCheck"	disabled="true" onclick="extChk();"> <!--  -->
									</html:checkbox>
									<html:hidden property="ext"></html:hidden>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td colspan="8" width="801">
								<table>
									<tr>
										<td width="100" class="LabelForm">Observaciones:</td>
										<td>
										
										<div class="inputV2">
										  <div class="group">      
									
											  <html:text styleClass="textinput" size="95" maxlength="255" property="comments" onblur="setBack('true');" onfocus="setBack('false');"/>
																															
											  <span class="highlight"></span>
										      <span class="bar"></span>
										  </div>
									    </div>
										
										</td>
										<td></td>
									</tr>
									<tr>
										<td width="100" class="LabelForm">Referencia:</td>
										<td>
										
										<div class="inputV2">
										  <div class="group">      
									
											  <html:text styleClass="textinput" size="95" maxlength="65" property="reference" onkeypress="return add_Key_li(this.value);"/>
																															
											  <span class="highlight"></span>
										      <span class="bar"></span>
										  </div>
									    </div>										
										
										</td>
										<td>
											<input type="button" onclick="return add_li_submit(document.forms[0].reference.value);" value="Añadir referencia" class="buttonnormal buttonLarge">
										</td>
									</tr>
									<tr>
										<td>
										</td>
										<td >
											<div id="dataRefdiv" class="divReference">
												<dl id="listaDesordenada" style="width: 100%; margin: 0px; padding: 0px;" >																
      											</dl>
											</div>
										</td>
										<td>
										</td>
									</tr>
									<TR class="LabelForm">
										<TD align=right height="25">Número de pedimento:&nbsp;</TD>
										<TD>
											<html:text styleClass="textcontenido" property="pedinumber" onblur="setBack('true');" onfocus="setBack('false');" style="width: 30%;"/>
											Agente Aduanal:&nbsp;
											<html:text styleClass="textcontenido" property="custagent" onblur="setBack('true');" onfocus="setBack('false');" style="width: 30%;"/>
										</TD>
									</TR>
								</table>
							</td>							
						</tr>						
						<tr>
						<logic:present name="status">
											<table>
												<tr class="LabelForm" style="visibility: hidden;">
													<td>Tipo de Pago:</td>
													<td>Efectivo<input type="checkbox" name="cash"
														onclick="callCashMode();" ></td>
													<td>Crédito<input type="checkbox" name="credit"
														onclick="callCreditMode();" checked="yes"></td>
												</tr>
											</table>
										</logic:present>
						</tr>						
						<tr>
							<td colspan="8" width="801" align="center">
							<INPUT
								TYPE="button" onclick="changeGuia();"
								VALUE="Modificar Documentacion" name="modificar"
								class="buttonnormal1 buttoneExtraLarge" /> 
								<INPUT TYPE="button"
								onclick="facturarCambios();" VALUE="Facturar Cambios"
								name="facturar" class="buttonnormal1 buttonLarge" />
							<INPUT
								TYPE="button" name="confirmar" onclick="return convert();"
								VALUE="Confirmar Documentación" class="buttonnormal1 buttoneExtraLarge" /> 
								<INPUT TYPE="BUTTON" name="limpiar" VALUE="Limpiar Datos" class="buttonnormal buttonLarge"
								onclick="resetfields();" /></td>
						</tr>
					</table>
					</fieldset>
					</td>
					</tr>
				</table>
				<table class="bodyWidth marginAutoCentro"><!-- AAP04 -->
					<tr>
						<td style="width: 900px;">
							<div id="divDetMail" style="visibility: visible;">
							<fieldset class="SubHead">
								<legend class="SubHead"> Notificaciones por Correo Electrónico </legend><!-- AAP04 -->
								<table>
									<tr class="LabelForm">
										<td align="right">Notificación de Entrega</td><!-- AAP04 -->
										<td>
											<html:checkbox property="eMailOrigCheck" onclick="cleanEmail(this, document.forms[0].eMailOrigText)"/>
											<html:text property="eMailOrigText" styleClass="textinput" size="95" maxlength="200" readonly="true"/>
										</td>
									</tr>
									<tr class="LabelForm">
										<td align="right">Notificación de Envío</td><!-- AAP04 -->
										<td>
											<html:checkbox property="eMailDestCheck" onclick="cleanEmail(this, document.forms[0].eMailDestText)"/>
											<html:text property="eMailDestText" styleClass="textinput" size="95" maxlength="200" readonly="true"/>
										</td>
									</tr>
								</table>
							</fieldset>
							</div>
						</td>
					</tr>
				</table><!-- AAP04 -->
				</logic:present>
			</td>
		 </tr>
		</table>
	</table>
	


	<html:hidden property="fiscaladdresscode" />
	<html:hidden property="refNo" />
	<html:hidden property="orgioncode" />
	<html:hidden property="destcode" />
	<html:hidden property="currentTask" />
	<html:hidden property="checkOriginAddress" />
	<html:hidden property="oldDestSite" />
	<html:hidden property="oldDestSiteName" />
	<html:hidden property="payMode" />
	<html:hidden property="guiaOld" />
	<html:hidden property="aceptarNuevosCargos" />
	<html:hidden property="nuevoValorExt" />
	<html:hidden property="valorIVA" />
	<html:hidden property="valorRetAmount" />
	<html:hidden property="clientType" />
	<html:hidden property="isExtendedZone" />
	<html:hidden property="guiaHasErrors" />
	<html:hidden property="guiaErrorType" />
	<html:hidden property="dest_am_gety_code" />
	<html:hidden property="horasEntregaOcu" />
	<html:hidden property="horasEntregaEad" />
	<html:hidden property="checkRefDir" /><!-- AAP02 -->
	<html:hidden property="checkTelDir" /><!-- AAP02 -->
	<html:hidden property="reqAcuse" /><!-- AAP02 -->
	<html:hidden property="reqAcuseXT" />
	<html:hidden property="actType" /><!-- AAP02 -->				   
	<html:hidden property="actCheck"/><!-- AAP02 -->
	<html:hidden property="destBranch" /><!-- AAP02 -->
	<html:hidden property="allowNewInvoice" />
	<html:hidden property="clientHasLocalCredit" />
	
	<html:hidden property="idSetSel"/>
	<html:hidden property="zonaKmSel"/>
	<html:hidden property="tarifaSel"/>
	<html:hidden property="pesoKgSel"/>
	<html:hidden property="volumenSel"/>
	<html:hidden property="numRastreoSel"/>
	<html:hidden property="acuseSel"/>
	<html:hidden property="valorDeclaradoSel"/>
	<html:hidden property="EADSel"/>
	<html:hidden property="RADSel"/>
	<html:hidden property="EXTSel"/>
	<html:hidden property="mainSetSel"/>
	<html:hidden property="invcSel"/>
	<html:hidden property="clickedItems"/>
	<html:hidden property="banCerrar"/>
	<html:hidden property="reLoad"/>
	<html:hidden property="facturaMod"/>
	<html:hidden property="filtrarPor"/>
	<html:hidden property="brncVrtl"/>
	<html:hidden property="shipType"/>
	<html:hidden property="neededConfirmationService2D"/><!-- JSA01 -->
	<html:hidden property="confirmationService2D"/><!-- JSA01 -->
	<html:hidden property="shipTypeSEGDescChange"/><!-- JSA01 -->
	<html:hidden property="shipTypeSEGChange"/><!-- JSA01 -->
	<html:hidden property="msjConfirmationService"/><!-- JSA01 -->
	<html:hidden property="alertNOConfirmationService"/><!-- JSA01 -->
	<html:hidden property="volLMAX"/><!-- JSA01 -->
	<html:hidden property="volHMAX"/><!-- JSA01 -->
	<html:hidden property="volWMAX"/><!-- JSA01 -->
	<html:hidden property="listReferences" /><!-- AAP13 -->
	<html:hidden property="flagValidRefrClnt" /><!-- AAP13 -->
	<html:hidden property="factorDividorPesoVol" /> <!--JSA01 VARIABLE PARA REALIZAR CALCULO DEL PESO VOLUMETRICO LADO DEL CLIENTE -->
	<html:hidden property="cantPesoVolDecimales" /><!--JSA01 VARIABLE PARA TRUNCAR EL CALCULO DEL PESO VOLUMETRICO -->
	<html:hidden property="volumeOriginal"/><!-- JSA04 -->	
	<html:hidden property="borderBranch" /><!-- AAP20 -->
	<html:hidden property="locationType" /><!-- AAP20 -->	
	<html:hidden property="volLMin" />
	<html:hidden property="volWMin" />
	<html:hidden property="volHMin" />
	<html:hidden property="wghtMin" />
	<html:hidden property="volMin" />
		
	<!-- Sucursal default addr -->
	<html:hidden property="defaultBrnchAddr" />
	
	<!-- Propiedades para Complemento Carta Porte -->
	<html:hidden property="productIdSat" />
	
	<!-- Propiedades para validación ModRastreo -->
	<html:hidden property="modFormnoFlag" />
	<html:hidden property="extOrgnValue" />
	
	<!-- Validación del número de pedimento -->
	<html:hidden property="borderbranchcheck" />
	<html:hidden property="msgInfo" />

	<html:hidden property="assignedBranch" />
	
	<html:hidden property="destClaveAux" />
	<html:hidden property="destNombreAux" />
</html:form>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
</body>
</html>