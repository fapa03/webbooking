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
 * Descripción:  Se agregaron variable formaPago para mantener el valor seleccionado
 * en el historial de manifiestos. 
 * ------------------------------------------------------------------
--%>
<%@page contentType="text/html" import="java.util.*,bean.ShipmentRecord,bean.AddServRecord,bean.Global" %>
<%@taglib uri="WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html>
<head>
<link rel="stylesheet" href="webbooking.css">

<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />


<title>Guia details</title>
<script lanugage = "javascript">	
	function webGuiaDetails(){
		//var guiaNumber = document.forms[0].hidGuiaNumber.value;
		//document.forms[0].action='webguiadetails.do?hidGuiaNumber='+guiaNumber;
		//document.forms[0].submit();
		history.back();
	}
</script>
</head>
<body marginwidth=0 marginheight=0 topmargin=0 leftmargin=0 class="backgroundStandard">
<html:form action="services">

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

<!-- <table border="0" cellspacing="0" cellpadding="0" width="769" > -->
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

<table border="0" width="100%" cellspacing="0" cellpadding="0">
<!--         <tr> -->
<!--           <td width="2%"></td> -->
<!--           <td width="11%"> -->
<!--           <td width="87%" align="right"><img src="images/services.jpg" width="498" height="26" border=0 useMap="#Map"></td> -->
<!--         </tr> -->
      </table>
<table border="0" width="100%" cellspacing="0" cellpadding="0"  class=" marginAutoCentro bodyWigth2 tableRowSeparator" >
  <tr>
    <td width="100%" align="center">
      <table border="0" width="100%" cellspacing="0" cellpadding="0" class="tableRowSeparatorNo">
        <tr>
          <td width="2%"></td>
          <td width="98%" colspan="6">
            <p align="left"><font face="Arial" size="2"><b>&nbsp;&nbsp;Detalle de Embarque</b></font></td>
        </tr>
        <tr>
          <td width="2%"></td>
          <td width="6%"><font face="Arial" size="2">Cantidad</font></td>
          <td width="14%"><font face="Arial" size="2">&nbsp; Descripcion</font></td>
          <td width="43%"><font face="Arial" size="2">&nbsp; Contenido</font></td>
          <td width="7%"><font face="Arial" size="2">&nbsp; Peso</font></td>
          <td width="11%"><font face="Arial" size="2">&nbsp;&nbsp; Volumen</font></td>
          <td width="19%">
            <p align="left"><font face="Arial" size="2">Importe&nbsp; </font>
          </td>
        </tr>
        <%
		ArrayList additionService =(ArrayList)request.getAttribute("SHIPMENT");
		double subTotalImporte = 0.0d;
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		Global global = (Global) session.getAttribute("sGlobal");
		boolean dispAmntFlag = false;

		if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
			dispAmntFlag = true;
		boolean isOneRecord = false;
        for(int i=0;i <additionService.size();i ++){
			isOneRecord = true;
			ShipmentRecord shipRecords = (ShipmentRecord)additionService.get(i);
			subTotalImporte = subTotalImporte+Double.parseDouble(shipRecords.getCharges());
        %>        
        <tr>
          <td width="2%"></td>
          <td width="6%"><input type=text style="TEXT-ALIGN:  right; WIDTH: 51px"  name="quantity" size=4 readonly class="textreadonly" value='<%=(shipRecords.getQuantity()==null?"":shipRecords.getQuantity())%>' style="width: 41;  " ></td>
          <td width="14%"><input type=text style="WIDTH: 112px" name="description" size="12" readonly=true class="textreadonly" value='<%=shipRecords.getDescription()==null?"":shipRecords.getDescription()%>' style="width: 101;  "></td>
          <td width="43%"><input type=text style="WIDTH: 340px" name="content" size="30" readonly=true class="textreadonly" value='<%=shipRecords.getContent()==null?"":shipRecords.getContent()%>' style="width: 331;  "></td>
          <td width="7%"><input type=text style="TEXT-ALIGN:  right; WIDTH: 58px"  name="peso" size="4" readonly=true class="textreadonly" value='<%=shipRecords.getPeso()==null?"":df.format(Double.parseDouble(shipRecords.getPeso()))%>' style="width: 46;  "></td>
          <td width="11%"><input type=text style="TEXT-ALIGN: right; WIDTH: 87px" name="volume" size="6" readonly=true class="textreadonly" value='<%=shipRecords.getVolume()==null?"":df.format(Double.parseDouble(shipRecords.getVolume()))%>' style="width: 59;  "></td>
		<%
          
	  		//if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
	  			
		if(dispAmntFlag)
			{
		%>
	          <td width="19%">
	          	<input type=text style="  TEXT-ALIGN:  right; WIDTH: 70px"  name="charges" size="8" readonly=true class="textreadonly" value='<%=shipRecords.getCharges()==null?"":df.format(Double.parseDouble(shipRecords.getCharges()))%>' style="width: 70;  ">
	          </td>
	    <%
			}
			else
			{
		%>
			<td width="19%">
	          	<input type=text style="  TEXT-ALIGN:  right; WIDTH: 70px"  name="charges" size="8" readonly=true class="textreadonly" value="**********" style="width: 70;  ">
	          </td>
		<%
			}
		%>
        </tr>
        <% }
        int inc = additionService.size();
        while(inc < 6){%>
        <tr>
          <td width="2%"></td>
          <td width="6%"><input type=text style="  TEXT-ALIGN:  right; WIDTH: 51px" name="quantity" size=4 readonly class="textreadonly" style="width: 41;  " ></td>
          <td width="14%"><input type=text style="  WIDTH: 112px" name="description" size="12" readonly=true class="textreadonly" style="width: 101;  "></td>
          <td width="43%"><input type=text style="  WIDTH: 340px" name="content" size="30" readonly=true class="textreadonly" style="width: 330;  "></td>
          <td width="7%"><input type=text style="  TEXT-ALIGN:  right; WIDTH: 58px" name="peso" size="4" readonly=true class="textreadonly" style="width: 45;  "></td>
          <td width="11%"><input type=text style="  TEXT-ALIGN: right; WIDTH: 87px" name="volume" size="6" readonly=true class="textreadonly" style="width: 59;  "></td>
          <td width="19%"><input type=text style="  TEXT-ALIGN:  right; WIDTH: 70px" name="charges" size="8" readonly=true class="textreadonly" style="width: 70;  "></td>
        </tr>
        <% inc++; 
        }%>
        <tr>
          <td width="2%"></td>
          <td width="6%"></td>
          <td width="14%"></td>
          <td width="43%"></td>
          <td width="7%"></td>
          <td width="11%">
          <p align="right"><font face="Arial" size="2">Sub - Total&nbsp; </font></td>
          <%
	//          if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
		
		if(dispAmntFlag)
			{
				if(isOneRecord)
				{
          %>
		          <td width="19%"><input type="text" style="  TEXT-ALIGN:  right; WIDTH: 70px"  value="<%= df.format(subTotalImporte) %>" readonly name="chargeSubTotal" style="width: 70;  " class="textreadonly" ></td>
          <%
				}
				else
				{
          %>
	          <td width="19%"><input type="text" style="  TEXT-ALIGN:  right; WIDTH: 70px"  readonly name="chargeSubTotal" style="width: 70;  " class="textreadonly" ></td>
          <%
				}
		  	}
          else
          {
        	  if(isOneRecord)
				{
        %>
		          <td width="19%"><input type="text" style="  TEXT-ALIGN:  right; WIDTH: 70px"  value="**********" readonly name="chargeSubTotal" style="width: 70;  " class="textreadonly" ></td>
        <%
				}
				else
				{
        %>
	          <td width="19%"><input type="text" style="  TEXT-ALIGN:  right; WIDTH: 70px" value="**********"  readonly name="chargeSubTotal" style="width: 70;  " class="textreadonly" ></td>
        <%
				}
          }
          %>
          <script language="javascript">					
				/*var total=0;	
				for(i=0;i<6; i++){						
					if(document.forms[0].charges[i].value != ""){
						total=parseInt(total)+parseInt(document.forms[0].charges[i].value);											
					}	
				}				
				document.forms[0].chargeSubTotal.value = total;*/
		 </script>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="100%">
      <table border="0" width="100%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="49%">
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td width="4%" ></td>
                <td width="75%" colspan="3"><font face="Arial" size="2"><b>&nbsp;&nbsp;
                  Servicios Adicionales</b></font></td>
                <td width="29%" ></td>
              </tr>
              <tr>
                <td width="4%" ></td>
                <td width="75%" colspan="3"><font face="Arial" size="2">Descripcion</font></td>
                <td width="29%">
                  <p align="center"><font face="Arial" size="2">Total&nbsp;</font> </td>
              </tr>
              <%
               ArrayList addService = (ArrayList)request.getAttribute("additionService");
               int addServInc = addService.size();
               double subTotal = 0.0d;
               for (int i=0;i< addServInc; i++){
					AddServRecord addServRec = (AddServRecord)addService.get(i);
					subTotal = subTotal +Double.parseDouble(addServRec.getTotal());					
               %>               
				<tr>
                <td width="4%"></td>
                <td width="75%" colspan="3"><input type="text" readonly class="textreadonly" name="description" size="37" value='<%=addServRec.getDescription()==null?"":addServRec.getDescription()%>' style="  WIDTH: 283px"></td>
               <%
//			          if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
			
			if(dispAmntFlag)
					{
        	  %>
                <td width="29%">
                	<input type="text" name="total" style="  TEXT-ALIGN: right; WIDTH: 70px" 
                		readonly class="textreadonly" name="total" size="12" 
                		value='<%=addServRec.getTotal()==null?"":df.format(Double.parseDouble(addServRec.getTotal()))%>'>
                </td>
              <%
					}
					else
					{
				%>
				 <td width="29%">
                	<input type="text" name="total" style="  TEXT-ALIGN: right; WIDTH: 70px" 
                		readonly class="textreadonly" name="total" size="12" 
                		value="**********">
                </td>
				<%
					}
				%>
              </tr>                             
              <%
				}              
              while( addServInc < 6){%>
				<tr>
                <td width="4%"></td>
                <td width="75%" colspan="3"><input type="text" readonly class="textreadonly" name="description" size="37" style="  WIDTH: 283px"></td>
                <td width="29%"><input type="text" name="total" readonly class="textreadonly" name="total" size="12" style="  TEXT-ALIGN: right; WIDTH: 70px"></td>
              </tr> 
				<% 
				addServInc ++;
                }
               %>
              
              <tr>
                <td width="4%"></td>
                <td width="4%">
                  <p align="right"></td>
                <td width="4%"></td>
                <td width="63%">
                  <p align="right"><font face="Arial" size="2">Sub - Total&nbsp;
                  </font></td>
                 <%
		//	          if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
			
				if(dispAmntFlag)
					{
        	  %>
        	  		<td width="29%">
    	            	<input type="text" style="  TEXT-ALIGN: right; WIDTH: 68px" 
                			value="<%= df.format(subTotal) %>" readonly class="textreadonly" name="subTotal" size="12">
	               	</td>
        	  <%
					}
					else
					{
				%>
					<td width="29%">
    	            	<input type="text" style="  TEXT-ALIGN: right; WIDTH: 68px" 
                			value="**********" readonly class="textreadonly" name="subTotal" size="12">
	               	</td>
				<%
					}
				%>
                
               	
              </tr>
              <script language="javascript">					
				/*var tot=0;	
				for(i=0;i<6; i++){						
					if(document.forms[0].total[i].value != ""){												
						tot=parseFloat(tot)+parseFloat(document.forms[0].total[i].value);						
					}	
				}				
				document.forms[0].subTotal.value = tot;*/
			</script>
            </table>
          </td>
          <td width="51%" valign=top>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td width="3%"></td>
                <td width="83%" colspan="2"><b><font size="2" face="Arial">&nbsp;&nbsp;
                 Requisitos de Servicios</font></b></td>
                <td width="14%"></td>
              </tr>
              <tr>
                <td width="3%"></td>
                <td width="83%" colspan="2"><font size="2" face="Arial">Requistos</font></td>
                <td width="14%"></td>
              </tr>
              <% 
				ArrayList addReqs = (ArrayList)request.getAttribute("additionRequirement");	
				int addReqInc = addReqs.size();
              for(int i=0;i < addReqInc; i++) {%>
              <tr>
                <td width="3%"></td>
                <td width="83%" colspan="2"><input type="text" readonly class="textreadonly" name="requirement" size="47" style="width: 320;  " value='<%=addReqs.get(i).toString()%>'></td>
                <td width="14%"></td>
              </tr>
              <% } 
              while(addReqInc < 6){%>
				<tr>
                <td width="3%"></td>
                <td width="83%" colspan="2"><input type="text" readonly class="textreadonly" name="T75" size="47" style="width: 321;  "></td>
                <td width="14%"></td>
              </tr>              	
             <%
             addReqInc++;
              }
              %>
              <tr>
                <td width="3%"></td>
                <td width="40%">
                  <p align="right"></td>
                <td width="43%"></td>
                <td width="14%"></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="100%">
      <table border="0" width="100%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="2%"></td>
          <td width="99%" colspan="2"><font face="Arial" size="2">Persona que
            Envia</font></td>
        </tr>
        <tr>
          <td width="2%"></td>          
          <td width="20%"><html:text readonly="true" styleClass="textreadonly" size="40" property="comment"/></td>
          <td width="79%"><html:text readonly="true" styleClass="textreadonly" size="69" property="addInfo"/></td>
        </tr>
        <tr>
          <td width="100%" colspan="3"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<!-- value cut from here -->
<html:hidden property="hidGuiaNumber"/>
<html:hidden  property="formaPago"/><!-- AAP01 -->
<html:hidden property="docuType"/><!-- AAP02 -->
<html:hidden property="manifiestoType"/><!-- AAP02 -->
<html:hidden property="preferedAddressCode"/><!-- AAP03 -->
<html:hidden property="preferedAddress"/><!-- AAP03 -->
</html:form>
<map name="Map">
  <area shape="rect" coords="370,6,430,18" href="javascript:webGuiaDetails()">
</map>
</body>
</html:html>