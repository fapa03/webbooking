<%--
Authur		:	Murugesapandian.T
Date		:	12-May-2006

FileName	:	ClientDestinationEntry.jsp
FormBean	:	ClientDestinationEntry.class
ActionBean	:	ClientDestinationEntry.class
OtherClasses	:	none
CSS FileName	:	webbooking.css
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 02/07/2013
 * Descripción:  Se agregaron validaciones para rfc y tipo de cliente	
 * ------------------------------------------------------------------
--%>
<%@ page info="WebBooking - General" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.ArrayList"%>
<%
	String version = "00.00.44";
%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel=stylesheet media=screen type=text/css href="./webbooking.css?v=<%=version%>">
<script type="text/javascript" src="barraEspera.js?v=<%=version%>"></script>
<script type="text/javascript" src="common.js?v=<%=version%>"></script>
<script type="text/javascript" src="ClientDestinationEntry.js?v=<%=version%>"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css?v=<%=version%>">
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
<script type="text/javascript" src="js/jquery/1.12.0.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/v2/global.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/ToastNotification.js?v=<%=version%>"></script>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />

<title>Registro de cliente destino</title>
</head>
<body id="body"  onpageshow="onLoad();mostrarCapaCentro('mensaje');" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="backgroundStandard" onkeydown="noBack(event);">
<html:form action="clientDestinationEntry.do">
<jsp:include page="nocache.jsp" flush="false" />

		
		<table border="0" cellspacing="0" cellpadding="0" class="width100porcent backgroudBluePtx">
			<tr>
				<td  >
					<logic:equal name="clientDestinationEntryForm" property="fastClntCap" value="false">
					<div class="bodyWidth marginAutoCentro">
						<img src="images/logos/logoW.png" border="0" 
						onclick="goOut('mainpage');hideMenu();" 
							style="max-width: 138px;margin-top: 11px;margin-bottom: 9px;cursor:pointer;"> 
						
							<a id="menu-hamburger" class="Animated1" onclick="showMenu()">
								<div style="margin-top: 15px;">
									<span id="menuline1" class="Animated05"></span> <span
										id="menuline2" class="Animated05"></span> <span id="menuline3"
										class="Animated05"></span>
								</div>
							</a>
							
						
					</div>
					</logic:equal>
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
							
  								<a onclick="goOut('mainpage');hideMenu();"> Menú Principal </a>
  								<a onclick="goOut('cliententry');hideMenu();">Registro de Cliente Destino</a>
  								<logic:equal name="loginForm" property="showWeb" value="Y">
									<logic:equal name="loginForm" property="newBooking" value="Y">
  										<a onclick="goOut('guiabooking');hideMenu();"> Elaboración de Guias</a>
  									</logic:equal>
  								</logic:equal>
  								<html:hidden name="loginForm" property="showPpg"></html:hidden>
								  <logic:equal name="loginForm" property="showPpg" value="Y">
									<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
								  </logic:equal>
  								<a onclick="goOut('guiacancel');hideMenu();"> Cancelación de guia </a>
  								<a onclick="goOut('shipmenthistory');hideMenu();"> Histórico de Envíos </a>
								<a onclick="goOut('clientreport');hideMenu();">Información del Cliente  </a>
								<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->				
									<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>
								</logic:equal>
 						</logic:equal>
					</logic:present> 
			</div>
		</div>
		<table>
			<tr>
				<td style="height: 8px;"><textarea id="mensaje"
						class="textareacontenido" cols="46" readonly="readonly" rows="4"
						onblur="self.focus();"
						style="background-color: #EEEEEE; border-style: ridge; overflow: hidden; text-align: center; vertical-align: middle; z-index: 2; visibility: hidden; position: absolute; top: 450px; left: 200px; width: 350px; height: 80px">
					</textarea></td>
			</tr>
		</table>		

<table width="68%" border="0" cellspacing="0" cellpadding="0" class="marginAutoCentro bodyWidth">
	<logic:present name="clientDestinationEntryForm" scope="request">
		<logic:notEqual name="clientDestinationEntryForm" property="errorMessages" value="">
			<tr>
			 	<td class="fontBold " style="width: 500px;color: red;"><bean:write name="clientDestinationEntryForm" property="errorMessages" filter="false"/></td>
			</tr>	
		</logic:notEqual>	  
	</logic:present>
</table>

<table border="0" cellspacing=1 cellpadding=0 class="marginAutoCentro bodyWidth">
<tr>
<td style="border:none;">
<table border="0" cellspacing=0 cellpadding=0>
	<tr>
		<td align="left"  style="height: 45px !important;" ><strong class="titleSection fontBold">Registro de Cliente Destino</strong></td>
	</tr>
</table>
</td>
</tr>
</table>
<table cellspacing=1 cellpadding=0 class="marginAutoCentro bodyWidth">
<tr>
<td style="border:none;">
<table border="0" cellspacing=0 cellpadding=0 class="tableRowSeparator">


		<tr>
              <td align="right" style = "width:141" >CodigoCliente &nbsp; </td>
              <td width=15 align="right">
              	
              	<div class="inputV2">
					<div class="group">       
						<html:text property = "codigoCliente" style = "TEXT-TRANSFORM: uppercase;width:150" onkeypress="loadDetailKey();" onblur="setBack('true');" onfocus="setBack('false');"/>
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
              	
              </td>
                <!-- Added by Kavitha on 11May2011 for Cliente cust_clnt_id and EMAIL-->
              <td style="display: flex;clear: both;">
              	<div style="float:left; margin-left: 5px; ">
              	<INPUT TYPE="BUTTON" class=""  VALUE="Ver Detalle" style="FONT-WEIGHT:bolder;COLOR:lightyellow;FONT-FAMILY:Arial;FONT-SIZE:12px;" onclick="loaddetail()">
              	</div>
              	
               	<div style="float: left; margin-left:  5px; padding-top: 6px;">
               	Codigo Int &nbsp;
               	</div>
               	<div class="inputV2" style = "width:254; float: right;"  >
					<div class="group">       
						<html:text property = "codigoInt" style = "TEXT-TRANSFORM: uppercase;width:254" maxlength="15" onkeypress="loadDetailKey();" onblur="setBack('true');" onfocus="setBack('false');"/>
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
             </td >          
        </tr>					
		<tr>
			 <td align="right">Nombre  &nbsp; </td>
			 <td colspan = 3 align="left">
			 	<div class="inputV2" style = "width:566" align="right" >
					<div class="group">       
						<html:text property = "nombre" style = "TEXT-TRANSFORM: uppercase;width:566" maxlength="80" onkeypress="openLovNombreKey('regClientDest');" onblur="setBack('true');" onfocus="setBack('false');"/>
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			 </td>
			 <td align="right" style="margin-right: 4px;">
				&nbsp;
			 	<html:button property="nombreBut" styleClass="button1 buttonMore"  value="..." style = "width:20"  onclick="openLov('regClientDest')" />
			 </td>
		</tr>
		
		<tr>
			<td align="right" >Telefono  &nbsp; </td>
			<td >
				<div class="inputV2" style = "width:150;"  >
					<div class="group" >       
						<html:text property = "telefono" style = "TEXT-TRANSFORM: uppercase;width:150" maxlength="20" onblur="setBack('true');" onfocus="setBack('false');"/>
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			
			
			</td>
		    <!-- Added by Kavitha on 11May2011 for Cliente cust_clnt_id and EMAIL-->
		    
			<td  align="left" >
			
				<div style = " float: left; padding-top: 6px;" >
				  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; E-mail &nbsp;&nbsp;
				</div>
				<div class="inputV2" style = "width:319; float: left;"  >
					<div class="group" >       
						<html:text property = "email" style = "width:319" maxlength="80" onblur="setBack('true');emailValidate(this);" onfocus="setBack('false');"/>
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			
			
			</td>
		</tr>
		<tr>
			<td align="right" >Celular  &nbsp; </td>
			<td >
				<div class="inputV2" style = "width:150;"  >
					<div class="group" >       
						<html:text property = "celular" style = "TEXT-TRANSFORM: uppercase;width:150" onkeypress="return isNumberKey(event);" maxlength="10" onblur="isNumber22(this);setBack('true');" onfocus="setBack('false');"/>
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			</td>    
		    
			<td  align="left" >			
				
				<div class="inputV2" style = "width:319; float: left;"  >
					
				</div>
			
			
			</td>
		</tr>
		<tr>
			
			<td align="right">Tipo de Cliente  &nbsp; </td>
			<td>
				<html:select onchange="validRfcSat();" property="tipoCliente"><!-- AAP01 -->					
					<html:option value="C">COMPAÑIA</html:option><!-- AAP01 -->
					<html:option value="I" >INDIVIDUO</html:option><!-- AAP01 -->
				</html:select><!-- AAP01 -->
			</td>
			<td align="left">
				<div style="float: left; padding-top: 6px;">
				 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; RFC &nbsp;&nbsp; </div>
				
				<div style="float: left;">
					<div class="inputV2" style = "width:41; float: left; padding-right: 10;"  >
						<div class="group" >       
							<html:text onchange="validRfcSat();" property = "rfc1" size="4" maxlength="4" style="TEXT-TRANSFORM: uppercase;" onblur="setBack('true');" onfocus="setBack('false');"/>
																						      
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
					</div>
					
				
					<div class="inputV2" style = "width:53; float: left; padding-right: 10;"  >
						<div class="group" >       
							<html:text onchange="validRfcSat();" property = "rfc2" size="6" maxlength="6" style="TEXT-TRANSFORM: uppercase;" onkeypress="return isNumberKey(event);" onblur="setBack('true');" onfocus="setBack('false');"/>
																						      
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
					</div>
					
				
					<div class="inputV2" style = "width:36; float: left; padding-right: 10;"  >
						<div class="group" >       
							<html:text  onchange="validRfcSat();" property = "rfc3" size="3" maxlength="3" style="TEXT-TRANSFORM: uppercase;" onblur="setBack('true');" onfocus="setBack('false');"/>
																						      
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
					</div>
					<table>
						<tr>
							<td>
								<input hidden="hidden" type="BUTTON" class="" id="btnDatosFiscales" value="Datos Fiscales" style="FONT-WEIGHT:bolder;COLOR:lightyellow;FONT-FAMILY:Arial;FONT-SIZE:12px;" onclick="openLov('datosFiscalesForm');setBack('true');">
							</td>
							<td>
								
							</td>
							<input id="validData" type="checkbox" value="validData" disabled="disabled">Datos Fiscales Validados
							
						</tr>
					</table>
					
				</div>
			
			</td>
		</tr>
		<tr hidden="hidden">
			 <td hidden="hidden" align="right">Regimen Fiscal &nbsp; </td>
			 <td colspan = 3 align="left">
			 	<div class="inputV2" style = "width:566" align="right" >
					<div class="group">       
						<html:text readonly="true" property = "regimenFiscalDes" style = "display: none; TEXT-TRANSFORM: uppercase;width:566" maxlength="80" onblur="openLov('regimFiscal');setBack('true');" onfocus="setBack('false');"/>
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			 </td>
			 <logic:present name="clientDestinationEntryForm" scope="request">
				<logic:equal name="clientDestinationEntryForm" property="validRFC" value="Y">
					<logic:equal name="clientDestinationEntryForm" property="genericRFC" value="N">
						<td align="right" style="margin-right: 4px;">
							&nbsp;
				 		</td>
					</logic:equal>
				</logic:equal>	  
			</logic:present>
			 
		</tr>
		<tr hidden="hidden">
			 <td hidden="hidden" align="right">Uso de CFDI &nbsp; </td>
			 <td colspan = 3 align="left">
			 	<div class="inputV2" style = "width:566" align="right" >
					<div class="group">       
						<html:text readonly="true" property = "usoCfdiDes" style = "display: none; TEXT-TRANSFORM: uppercase;width:566" maxlength="80" onblur="openLov('usoCfdi');setBack('true');" onfocus="setBack('false');"/>
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			 </td>
			 <logic:present name="clientDestinationEntryForm" scope="request">
				<logic:equal name="clientDestinationEntryForm" property="validRFC" value="Y">
					<logic:equal name="clientDestinationEntryForm" property="genericRFC" value="N">
						<td align="right" style="margin-right: 4px;">
							&nbsp;
				 		</td>
					</logic:equal>
				</logic:equal>	  
			</logic:present>
		</tr>
</table>
</td>
</tr>
</table>

<table border="0" cellspacing=1 cellpadding=0  class="marginAutoCentro bodyWidth  " >
<tr>
<td style="border:none;">
<table border="0" cellspacing=0 cellpadding=0  class="tableRowSeparator">
		<tr>
			<div>
				<div>
					<td align="right" style = "width:141"><span style = "width:120">Calle  &nbsp; </td>
			  		<td width=15 >
			  		<div class="inputV2" style = "width:150; float: left; padding-right: 10;"  >
						<div class="group" >       
							<html:text property = "calle" style = "TEXT-TRANSFORM: uppercase;width:150" maxlength="80" onblur="setBack('true');" onfocus="setBack('false');"/>
																						      
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
					</div>
			  		
			  		</td>
				</div>
				<div>
					<td nowrap align="right">Numero  &nbsp; </td>
					
			  		<td>
			  		<div class="inputV2" style = "width:320; float: left; padding-right: 10;"  >
						<div class="group" >       
							<html:text property = "numero" style = "TEXT-TRANSFORM: uppercase;width:320" maxlength="15" onblur="setBack('true');" onfocus="setBack('false');"/>
																						      
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
					</div>
			  		
			  		</td>
				</div>
			</div>
			  
			  
		</tr>

		<tr>
			  <td align="right">
				  <logic:present name="clientDestinationEntryForm">
					   <bean:write name="clientDestinationEntryForm" property="codigoPostalLabel" />  &nbsp;	 		  		 
				   </logic:present>
			  </td>
			  <td colspan = 1>
			  		<div class="inputV2" style = "width:142; float: left; padding-right: 10;"  >
					<div class="group" >       
						<html:text property = "codigoPostal" style = "TEXT-TRANSFORM: uppercase;width:142" onkeypress="return isNumberKey(event);" onkeyup="valZipCodeData('postal')" maxlength="5" onblur="setBack('true');" onfocus="setBack('false');"/>																      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
					</div>
					
				   	<html:button property="codigoPostalBut" styleClass="button1 buttonMore"  value="..."  disabled = "false" style = "width:20" onclick="openLov('postal')" />
	          </td>
	          <td align="right">
				   <logic:present name="clientDestinationEntryForm">
					   <bean:write name="clientDestinationEntryForm" property="coloniaLabel" />  &nbsp; 
					</logic:present>
			  </td>
			  <td>
			  	<div class="inputV2" style = "width:320; float: left; padding-right: 10;"  >
					<div class="group" >       
						<html:text property = "colonia" style = "TEXT-TRANSFORM: uppercase;width:320" maxlength="60" readonly="true"/>																      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			  	
			  </td>
		</tr>
		<tr>
			  <td align="right">
				   <logic:present name="clientDestinationEntryForm">
					  <bean:write name="clientDestinationEntryForm" property="ciudadLabel" />  &nbsp;
	             	</logic:present>
			  </td>
			  <td>
			  	<div class="inputV2" style = "width:152; float: left; padding-right: 10;"  >
					<div class="group" >       
						<html:text property = "ciudad" style = "width:152"  onclick="" maxlength="60" readonly="true"/>																					      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			  	
			  </td>
			  <td align="right">				  
				   Sucursal &nbsp;
			  </td>
			  <td colspan = 1>
			  		<div class="inputV2" style = "width:320; float: left; padding-right: 10;"  >
					<div class="group" >       
						<html:text property = "assignedToBranch" style = "width:50"  readonly = "true" onclick=""/>
						<html:text property = "assignedToBranchRef" style = "TEXT-TRANSFORM: uppercase;width:265" maxlength="15" readonly="true" />																      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
					</div>					
	          </td>
		</tr>
		<tr>
			  <td align="right">
				 <logic:present name="clientDestinationEntryForm">
						<bean:write name="clientDestinationEntryForm" property="estadoLabel" />  &nbsp;
				   </logic:present>
			  </td>
			  <td>
			  	<div class="inputV2" style = "width:152; float: left; padding-right: 10;"  >
					<div class="group" >       
						<html:text property = "estado" style = "TEXT-TRANSFORM: uppercase;width:152" onfocus="getPostalDetails()"  maxlength="60" readonly = "true"/>																      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			  
			  
			  </td>
			  <td align="right">
			  <logic:present name="clientDestinationEntryForm">
		    <bean:write name="clientDestinationEntryForm" property="zonaLabel"/>  &nbsp;
			   </logic:present>
			  </td>
			  <td colspan = 1>
			  	<div class="inputV2" style = "width:320; float: left; padding-right: 10;"  >
					<div class="group" >       
						<html:text property = "zona" style = "TEXT-TRANSFORM: uppercase;width:320" maxlength="60" readonly = "true"/>																      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			  	
			  </td>
		</tr>
		<tr>
			  <td align="right">
			  <logic:present name="clientDestinationEntryForm">
			 <bean:write name="clientDestinationEntryForm" property="municipioLabel" />  &nbsp;
			   </logic:present>
			  </td>
			  <td>
			  	<div class="inputV2" style = "width:152; float: left; padding-right: 10;"  >
					<div class="group" >       
						<html:text property = "municipio" style = "TEXT-TRANSFORM: uppercase;width:152" maxlength="60" readonly = "true"/>																      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			  	</td>
			  <td align="right">
			   <logic:present name="clientDestinationEntryForm">
		       <bean:write name="clientDestinationEntryForm" property="paisLabel" />  &nbsp;
			   </logic:present>
			  </td>
			  <td colspan = 1>
			  <div class="inputV2" style = "width:320; float: left; padding-right: 10;"  >
					<div class="group" >       
						<html:text property = "pais" style = "TEXT-TRANSFORM: uppercase;width:320" maxlength="60" readonly = "true"/>																      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			  
			  </td>
		</tr>
		<tr>
			<td align="right">
				<logic:present name="clientDestinationEntryForm">
					<bean:write name="clientDestinationEntryForm" property="delegacionSecto" />  &nbsp;
			 	</logic:present>
			</td>
			<td></td>
			<td align="right">EAD</td>
			<td><html:checkbox property ="ead" onclick="eadVerify();" /></td>
		</tr>		
</table>
</td>
</tr>
</table>
<table border="0" cellspacing=1 cellpadding=0  class="marginAutoCentro ">
<tr>
<td style="border:none;">
<table border="0" cellspacing=0 cellpadding=0   class="marginAutoCentro bodyWidth">
		<tr>
			<!-- Modified By Sam [08-06-2006] , because of no functionality when checking ead -->			
			<td align="right" >
			 	<html:button property="regresar" style="FONT-WEIGHT:bolder;COLOR:lightyellow;FONT-FAMILY:Arial;FONT-SIZE:12px;BACKGROUND-COLOR:#003366 ;width:80" value="Regresar" onclick="saveAndBack('main_page')"/>			 
			</td>
			<td align="center" width="20%">
			 	<html:button property="guardar" style="FONT-WEIGHT:bolder;COLOR:lightyellow;FONT-FAMILY:Arial;FONT-SIZE:12px;BACKGROUND-COLOR:#003366 ;width:80" value="Insertar" onclick="saveAndBack('saveAndBack')"/>			 
			</td>
			<td align="left">
				<input type="button" value="Limpiar" style="FONT-WEIGHT:bolder;COLOR:lightyellow;FONT-FAMILY:Arial;FONT-SIZE:12px;BACKGROUND-COLOR:#003366 ;width:80" onClick="formreset();">
			</td>
		</tr>
</table>
</td>
</tr>
</table>
<html:hidden property="mode"/>

	<!-- postal values  -->
<html:hidden  property="level"/>
<html:hidden  property="code"/>
<html:hidden  property="type"/>
<html:hidden  property="refNo"/>

<!-- Generate Sequence Number -->
<html:hidden property="codigoClienteHid"/>
<html:hidden property="codigoIntHid"/>

<!-- Generate Error number values -->
<html:hidden property="mesgType"/>
<html:hidden property="mesgText"/>

<html:hidden property="eadCheck" />

<html:hidden property="geLevl"/>
<html:hidden property="geType"/>
<html:hidden property="geCode"/>

<!-- Added By Sam [08-06-2006] , because of sucursal value is same as assignedvalue --->
<html:hidden property="sucursal"/>
<!--  Eco Added by Sam-->

<input type="hidden" name = "pDetailsC"/>

<html:hidden property="bloqueaCodCliente"/>
<html:hidden property = "errorMessages"/>

<!-- CFDI 4-0 -->
<html:hidden property = "regimenFiscalId"/>
<html:hidden property = "usoCfdiId"/>
<html:hidden property = "genericRFC"/>
<html:hidden property = "validRFC"/>
<!-- Captura Datos Fiscales -->
<html:hidden property = "datosFiscales" />
<html:hidden property = "datosFiscalesString" />
<html:hidden property = "datosFiscalesTmpString" />
<html:hidden property = "tieneCredito" />
<html:hidden property = "validFiscal" />
<html:hidden property = "currRFC" />
<html:hidden property = "currValFlag" />
<html:hidden property = "openFiscalForm" value="N"/>
<html:hidden property = "fastClntCap"/>
</html:form>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
</body>
</html:html>

