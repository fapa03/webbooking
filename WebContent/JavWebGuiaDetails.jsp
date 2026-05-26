<%--

/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creación: 
 * Compañía: KUMARAN.
 * Descripción del programa: menu principal
 * FileName	:	
 * FormBean	:	
 * ActionBean	:	
 * OtherClasses	:	none
 * CSS FileName	:	webbooking.css
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 01/07/2013
 * Descripción:  se agregó variable formaPago para mantener el valor del tipo de pago seleccionado.
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción:  	
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción:  
 * ------------------------------------------------------------------
 */
--%>
<%@page contentType="text/html"%>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page  import="bean.Global"%>
<% //AAPP//System.out.println("New WebGuia Details..");%>
<html:html>
<head>
<link rel="stylesheet" href="webbooking.css">


<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />

<title>Guia details</title>


<script Language"JavaScript">
function submitForm(){
	document.forms[0].submit();
}

function getBack(){

	var flag=document.forms[0].hidFromFlag.value;
	if(flag!=null && flag=="MG"){
	   document.forms[0].action="manifestgeneration.do";
	   submitForm();
	}
	if(flag!=null && flag=="MD"){
	   document.forms[0].action="javManifestDetails.do";
	   submitForm();
	}
	  
}
function callServices(){
	var guiaNumber = document.forms[0].guiaNumber.value;
	document.forms[0].action="services.do?hidGuiaNumber="+guiaNumber;
	document.forms[0].submit();
}

</script>
</head>
<body marginwidth=0 marginheight=0 topmargin=0 leftmargin=0 class="backgroundStandard">





<html:form action="webguiadetails"> 

<table border="0" cellspacing="0" cellpadding="0" class="width100porcent backgroudBluePtx">
			<tr>
				<td  >
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
<!--   								<a onclick="goOut('mainpage');hideMenu();"> Menú Principal </a> -->
<!--   								<a onclick="goOut('cliententry');hideMenu();">Registro de Cliente Destino</a> -->
<!--   								<a onclick="goOut('guiabooking');hideMenu();"> Elaboración de Guias</a> -->
<!--   								<a onclick="goOut('guiacancel');hideMenu();"> Cancelación de guia </a> -->
<!--   								<a onclick="goOut('shipmenthistory');hideMenu();"> Histórico de Envíos </a> -->
<!-- 								<a onclick="goOut('clientreport');hideMenu();">Información del Cliente  </a>				 -->
<!-- 								<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a> -->
								
								<a onclick="webGuiaDetails();hideMenu();"> Generales </a>
  								<a onclick="callServices();hideMenu();">Services</a>		
  													
								
 						</logic:equal>
					</logic:present> 
<%--  					<logic:present name="clientDestinationEntryForm" scope="request"> --%>
<%-- 						<logic:notEqual name="clientDestinationEntryForm" property="errorMessages" value=""> --%>
<!-- 							<tr> -->
<%-- 			 					<td style="width: 500px;color: red;"><bean:write name="clientDestinationEntryForm" property="errorMessages"/></td> --%>
<!-- 							</tr>	 -->
<%-- 						</logic:notEqual>	   --%>
<%-- 					</logic:present>		 --%>
  			
			
			</div>
 			
		</div>	
<!-- <table border="0" cellspacing="0" cellpadding="0" width="769"> -->
<!--   <tr>  -->
<!--     <td width="429" height="77"><img src="images/inner_top01.jpg" border="0" usemap="#peMap" width="429" height="79"></td> -->
<!--     <td width="342" height="77">  -->
<!--       <div align="left"><img src="images/inner_top02.jpg" width="341" height="79" border="0"></div> -->
<!--     </td> -->
<!--   </tr> -->
<!--   <tr>  -->
<!--     <td colspan="2" height="3"></td> -->
<!--   </tr> -->
<!-- </table> -->
<!-- <map name="peMap">  -->
<!--     <area shape="rect" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com"> -->
<!-- </map> -->
<table width="770" cellspacing="0" cellpadding="0">
<!--   <tr>  -->
<!--     <td>&nbsp;</td> -->
<!--     <td>&nbsp;</td> -->
<!--     <td align="right"><img src="images/generales.jpg" width="498" height="26" usemap="#Map" border="0"></td> -->
<!--   </tr> -->
</table>
<table width="769" cellpadding="0" cellspacing="1" class=" marginAutoCentro bodyWidth">
  <tr> 
    <td height="21"> <table border="0" cellspacing="0" cellpadding="0" width="769" class="tableRowSeparator" >
        <tr> 
          <td width="1" height="21">&nbsp;</td>
          <td align="right" height="21" width="90"> <p align="right"><b>Guia</b> 
          </td>
          <td  height="21" width="2">&nbsp;</td>
          <td  height="21" colspan="7">&nbsp; </td>
        </tr>
        <tr> 
          <td width="1" height="25">&nbsp;</td>
          <td height="25" width="90"> <div align="right"><font face="Arial" size="2">No.Rastreo</font></div></td>
          <td  height="25" width="2">&nbsp;</td>
          <td  height="25" width="130">
          		<div class="inputV2" >
					<div class="group">       
						 <html:text property="guiaNumber" readonly="true"/>      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
          	
          	
           </td>
          <td  align="right" height="25" width="80"><font face="Arial" size="2">No.Guia&nbsp;</font></td>
          <td width="2" >&nbsp;</td>
          <td width="120" >
          		<div class="inputV2" >
					<div class="group">       
						<html:text property="formNumber" size="12" readonly="true"/>      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
          
          
          </td>
          <td width="94" > <p align="right"><font face="Arial" size="2">Status 
              Guia&nbsp;</font> </td>
          <td width="2" >&nbsp;</td>
			<td width="108" >
				<div class="inputV2" >
					<div class="group">       
						<html:text property="statusDesc" size="35" readonly="true"/>
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
			
			</td>
        </tr>
        <tr> 
          <td width="1">&nbsp;</td>
          <td align="right" width="90"><font face="Arial" size="2">FechaEmision</font></td>
          <td width="2" height="23">&nbsp;</td>
          <td width="130" height="23">
          		<div class="inputV2" >
					<div class="group">       
						<html:text property="issueDate" readonly="true"/>
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
          	
          </td>
          <td width="80" align="right" height="23"><font face="Arial" size="2">Importe&nbsp;</font></td>
          <td width="2" height="23">&nbsp;</td>
          
          <%
          	Global global = (Global) session.getAttribute("sGlobal");
	        boolean dispAmntFlag = false;
	
    	    if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
        	  	dispAmntFlag = true;
    	    
	  		//if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
	  			
			if(dispAmntFlag)	
			{
          %>
				<td width="130" height="23">
					<div class="inputV2" >
					<div class="group">       
						<html:text property="guiaAmount" style="text-align:right"  size="12" readonly="true"/> 
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
					</div>
					
				</td>
          <%
			}
			else
			{
		%>
				<td width="130" height="23">
					<div class="inputV2" >
					<div class="group">       
						<html:text property="guiaAmount" style="text-align:right"  size="12" readonly="true" value="**********" />
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
					</div>
					
				</td>
		<%
			}
		%>
		
          <td width="124" height="23" nowrap> <p align="right"><font face="Arial" size="2">Valor 
              Declarado&nbsp;</font> </td>
          <td width="2" height="23">&nbsp;</td>
          <td width="208" height="23">
          	<div class="inputV2" >
				<div class="group">       
					<html:text property="totalValue" size="35" style="text-align:right"  readonly="true"/>
					<span class="highlight"></span>
					<span class="bar" ></span>
				</div>
			</div>
          </td>
        </tr>
        <tr> 
          <td width="1" height="25">&nbsp;</td>
          <td align="right" width="90"><font face="Arial" size="2">Valor COD</font></td>
          <td width="2" >&nbsp;</td>
          <td width="130" >
          <div class="inputV2" >
				<div class="group">       
					<html:text property="codeValue" style="text-align:right" readonly="true" />
					<span class="highlight"></span>
					<span class="bar" ></span>
				</div>
			</div>
          
        
          
          </td>
          <td colspan="6" >&nbsp;</td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td> <table width="100%" cellspacing="0" cellpadding="0" >
        <tr> 
          <td> <table border="0" cellspacing="0" cellpadding="0" width="100%" class="tableRowSeparator">
              <tr> 
                <td width="1"></td>
                <td width="77" > <p align="right"><font face="Arial" size="2"><b>Origen</b></font> 
                </td>
                <td width="10" >&nbsp;</td>
                <td width="178"></td>
                <td colspan="3"></td>
              </tr>
              <tr> 
                <td width="1"></td>
                <td align="right" width="77"><font face="Arial" size="2">Sucursal</font></td>
                <td align="right" width="10">&nbsp;</td>
                <td colspan="4">
                <div class="inputV2" style="width:258px;">
				<div class="group">       
					<html:text  property="orginBranchName" style="width:258px;" readonly="true"/> 
					<span class="highlight"></span>
					<span class="bar" ></span>
				</div>
				</div>
                </td>
              </tr>
              <tr> 
                <td width="1"></td>
                <td align="right" width="77"><font face="Arial" size="2">Cliente</font></td>
                <td align="right" width="10">&nbsp;</td>
                <td colspan="4"> <table width="100%" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td width="21%">
                    <div class="inputV2" >
						<div class="group">       
							<html:text property="orginClientId" size="8" readonly="true" />
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
					</div>
                      
                      
                      </td>
                      <td width="79%">
                      <div class="inputV2" style="width:185px;">
						<div class="group">       
							<html:text property="orginClientName" size="24"  readonly="true"  />
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                     </td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="1"></td>
                <td align="right" width="77"><font face="Arial" size="2">Direccion</font></td>
                <td align="right" width="10"></td>
                <td colspan="4"><font face="Arial" size="2"></font> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td width="37%">
                     	<div class="inputV2" >
						<div class="group">       
							<html:text property="orginClientAddress" size="23"  readonly="true"/>
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      
                                            
                      </td>
                      <td width="63%">
                      	<div class="inputV2" style="width:80px;">
						<div class="group">       
							<html:text property="orginDoorNumber" size="9" readonly="true" />
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      
                      </td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="1"></td>
                <td width="77" align="right"><font face="Arial" size="2">Colonia</font></td>
                <td width="10" align="right">&nbsp;</td>
                <td colspan="4"><font face="Arial" size="2"></font>
                  <table width="100%" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td width="27%"><font face="Arial" size="2">
                      	<div class="inputV2" >
						<div class="group">       
							<html:text property="orginColony" size="16" readonly="true"/> 
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      
                        </font></td>
                      <td width="73%">
                      	<div class="inputV2" style="width:129px;" >
						<div class="group">       
							<html:text  property="orginCity" size="16"  readonly="true" />
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      </td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="1"></td>
                <td align="right" width="77"><font face="Arial" size="2">R.F.C</font></td>
                <td align="right" width="10">&nbsp;</td>
                <td colspan="4"><font face="Arial" size="2"></font> <table width="100%" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td width="30%">
                      	<div class="inputV2" >
						<div class="group">       
							<html:text property="orginRfc" size="16"  readonly="true"/>
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      
                      </td>
                      <td width="70%">
                      	<div class="inputV2" style="width:129px;" >
						<div class="group">       
							<html:text property="orginPhoneNumber" size="16" readonly="true"/> 
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      </td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="1"></td>
                <td  align="right" width="77"> </td>
                <td  align="right" width="10"></td>
                <td width="178"></td>
                <td width="3"></td>
                <td width="14"></td>
                <td width="124"></td>
              </tr>
            </table></td>
          <td> <table cellspacing="0" cellpadding="0" width="100%" class="tableRowSeparator" style="margin-left:64px;">
              <tr> 
                <td width="9" height="13"></td>
                <td width="58" height="13" > <p align="right"><font face="Arial" size="2"><b>Destino</b></font> 
                </td>
                <td width="9" height="13" >&nbsp;</td>
                <td width="98" height="13"></td>
                <td colspan="3" height="13"></td>
              </tr>
              <tr> 
                <td width="9"></td>
                <td align="right" width="58"><font face="Arial" size="2">Sucursal</font></td>
                <td align="right" width="9">&nbsp;</td>
                <td colspan="4">
                	<div class="inputV2" style="width:270px;" >
						<div class="group">       
							 <html:text property="destBranchName" style="width:266px;" readonly="true"/> 
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
					</div>
                <font face="Arial" size="2"> 
                  </font></td>
              </tr>
              <tr> 
                <td width="9"></td>
                <td align="right" width="58"><font face="Arial" size="2">Cliente</font></td>
                <td align="right" width="9">&nbsp;</td>
                <td colspan="4"> <table width="100%" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td width="14%">
                      	<div class="inputV2"  >
						<div class="group">       
							 <html:text  property="destClientId" size="9" readonly="true"/>
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      
                      </td>
                      <td width="width:86%;">
                      	<div class="inputV2" style="width:184px;">
						<div class="group">       
							 <html:text property="destClientName" size="24"  readonly="true" />
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      </td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="9"></td>
                <td align="right" width="58"><font face="Arial" size="2">Direccion</font></td>
                <td align="right" width="9">&nbsp;</td>
                <td colspan="4"><font face="Arial" size="2"></font> <table width="86%" border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td width="37%">
                      	<div class="inputV2" >
						<div class="group">       
							 <html:text property="destClientAddress" size="24" readonly="true"/>
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      </td>
                      <td width="63%">
                      	<div class="inputV2" style="width:80px;" >
						<div class="group">       
							 <html:text property="destDoorNumber" size="9"  readonly="true"/>
							<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      
                      </td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="9"></td>
                <td width="58" align="right"><font face="Arial" size="2">Colonia</font></td>
                <td width="9" align="right">&nbsp;</td>
                <td colspan="4"><font face="Arial" size="2"></font> <table width="100%" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td width="27%">
                      	<div class="inputV2" >
						<div class="group">       
							 <html:text property="destColony" size="17"  readonly="true"/> 
                        	<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      
                     </td>
                      <td width="73%">
                      	<div class="inputV2" style="width:130px;" >
						<div class="group">       
							 <html:text property="destCity" size="16"  readonly="true" /> 
                        	<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      
                     </td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="9"></td>
                <td align="right" width="58"><font face="Arial" size="2">Phone</font></td>
                <td align="right" width="9">&nbsp;</td>
                <td colspan="4"><font face="Arial" size="2"></font> <table width="100%" cellspacing="0" cellpadding="0"   >
                    <tr> 
                      <td width="30%" height="20">
                      	<div class="inputV2" >
						<div class="group">       
							 <html:text property="destPhoneNumber" size="35"  readonly="true"/> 
                        	<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      
                     </td>
                      <td width="70%"><font face="Arial" size="2">&nbsp;</font></td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="9"></td>
                <td  align="right" width="58"> </td>
                <td  align="right" width="9"></td>
                <td width="98"></td>
                <td width="5"></td>
                <td width="18"></td>
                <td width="180"></td>
              </tr>
            </table></td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td> <table cellspacing="0" cellpadding="0" >
        <tr> 
          <td> <table border="0"  class="tableRowSeparator" >
              <tr> 
                <td width="1"> <p align="right"><font face="Arial" size="2"><b>Entrega</b></font> 
                </td>
                <td >&nbsp;</td>
                <td ></td>
                <td ></td>
              </tr>
              <tr> 
                <td width="101" align="right">Tipo</td>
                
                <td > 
                
                <div class="inputV2" >
					<div class="group">       
						  <html:text  property="deleiveryType" size="35"  readonly="true"/> 
                       	<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
                
               
                </td>
              </tr>
              <tr> 
                <td width="101" align="right">Recibio</td>
               
                <td >
                <div class="inputV2" >
					<div class="group">       
						<html:text property="delivererName" size="35" readonly="true"/>
                       	<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
                <font face="Arial" size="2">  
                  </font></td>
              </tr>
              <tr> 
                <td width="101" align="right">Fecha Entrega</td>
                
                <td >
                <div class="inputV2" >
					<div class="group">       
						<html:text property ="deliveryDate" size="35"  readonly="true"/>
                       	<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
				</div>
                <font face="Arial" size="2">  
                  </font></td>
              </tr>
              <tr> 
                <td width="101"></td>
                <td width="10"></td>
                <td width="85"></td>
                <td width="187"></td>
              </tr>
            </table></td>
          <td> <table border="0" cellspacing="0" cellpadding="0" width="100%" class="tableRowSeparator" style="margin-left:20px;">
              <tr> 
                <td width="1" height="17"></td>
                <td width="88" height="17" > <p align="right"><font face="Arial" size="2"><b>Fiscals</b></font> 
                </td>
                <td width="10" height="17" >&nbsp;</td>
                <td width="171" height="17"></td>
                <td  height="17"></td>
              </tr>
              <tr> 
                <td width="1"></td>
                <td align="right" width="88"><font face="Arial" size="2">Direccion</font></td>
                <td align="right" width="10">&nbsp;</td>
                <td ><font face="Arial" size="2"></font> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td width="37%">
                      	<div class="inputV2" >
						<div class="group">       
							<html:text property="fiscalAddress" size="23"  readonly="true"/>
                       		<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                     </td>
                      <td width="63%">
                      	<div class="inputV2" >
						<div class="group">       
							<html:text property="fiscalDoorNumber" size="9"  readonly="true"/>
                       		<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      </td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="1"></td>
                <td width="88" align="right"><font face="Arial" size="2">Colonia</font></td>
                <td width="10" align="right">&nbsp;</td>
                <td ><font face="Arial" size="2"></font> <table width="100%" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td width="27%">
                     	<div class="inputV2" >
						<div class="group">       
							<html:text property="fiscalColony" size="16" readonly="true"/>
                       		<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      
                      <font face="Arial" size="2">  
                        </font></td>
                      <td width="73%">
                      	<div class="inputV2" >
						<div class="group">       
							<html:text property="fiscalStreet" size="16" readonly="true"/>
                       		<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                      <font face="Arial" size="2">  
                        </font></td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="1"></td>
                <td align="right" width="88"><font face="Arial" size="2">Ciudad</font></td>
                <td align="right" width="10">&nbsp;</td>
                <td colspan="0"><font face="Arial" size="2"></font> <table width="100%" cellspacing="0" cellpadding="0" >
                    <tr> 
                      <td>
                      	<div class="inputV2" >
						<div class="group">       
							<html:text property="fiscalPhoneNumber" style="width:258px;" readonly="true"/>
                       		<span class="highlight"></span>
							<span class="bar" ></span>
						</div>
						</div>
                        
                      </td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="1"></td>
                <td  align="right" width="88"> </td>
                <td  align="right" width="10"></td>
                <td width="171"></td>
                <td width="2"></td>
                <td width="13"></td>
                <td width="132"></td>
              </tr>
            </table></td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td > <div align="center"> 
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td> <div align="center"> 
                <input type="button"  class="button buttonMedium"  onClick="getBack()" value="Regresar" name="B1">
              </div></td>
          </tr>
        </table>
      </div></td>
  </tr>
</table>
<html:hidden property="hidFromFlag"/>
<html:hidden property="status"/> 
<html:hidden property="destBranchId"/> 
<html:hidden property="orginBranchId"/>
<html:hidden property="deliverer"/>
<html:hidden property="formaPago"/><!-- AAP01 -->
<html:hidden property="docuType"/><!-- AAP02 --> 
<html:hidden property="manifiestoType"/><!-- AAP02 -->
<html:hidden property="preferedAddressCode"/><!-- AAP03 -->
<html:hidden property="preferedAddress"/><!-- AAP03 -->
</html:form> 
<map name="Map">
  <area shape="rect" coords="442,6,492,18" href="javascript:callServices()">
</map>
</body>
</html:html>
