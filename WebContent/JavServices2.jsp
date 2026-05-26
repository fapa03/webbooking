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
<html>
<head>
<link rel="stylesheet" href="webbooking.css">
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
<body marginwidth=0 marginheight=0 topmargin=0 leftmargin=0>
<html:form action="services">
<table border="0" cellspacing="0" cellpadding="0" width="769">
  <tr> 
    <td width="429" height="77"><img src="images/inner_top01.jpg" border="0" usemap="#peMap" width="429" height="79"></td>
    <td width="342" height="77"> 
      <div align="left"><img src="images/inner_top02.jpg" width="341" height="79" border="0"></div>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="3"></td>
  </tr>
</table>
<map name="peMap"> 
    <area shape="rect" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com">
</map>
      <table border="0" width="100%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="2%"></td>
          <td width="11%">
          <td width="87%" align="right"><img src="images/services.jpg" width="498" height="26" border=0 useMap="#Map"></td>
        </tr>
      </table>
<table border="0" width="100%" cellspacing="0" cellpadding="0" bgcolor="#CEDEE9">
  <tr>
    <td width="100%">
      <table border="0" width="98%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="3%"></td>
          <td width="98%" colspan="6">
            <p align="left"><font face="Arial" size="2"><b>&nbsp;&nbsp;Detalle de Embarque</b></font></td>
        </tr>
        <tr>
        
          <td width="3%"></td>
          <td width="7%"><font face="Arial" size="2">Cantidad</font></td>
          <td width="14%"><font face="Arial" size="2">&nbsp; Descripcion</font></td>
          <td width="45%"><font face="Arial" size="2">&nbsp; Contenido</font></td>
          <td width="8%"><font face="Arial" size="2">&nbsp; Peso</font></td>
          <td width="10%"><font face="Arial" size="2">&nbsp;&nbsp; Volumen</font></td>
          <td width="23%">
            <p align="center"><font face="Arial" size="2">Importe&nbsp; </font></td>
        </tr>
        <%        
			ArrayList additionService =(ArrayList)request.getAttribute("SHIPMENT");
			double subTotalImporte = 0.0d;		
			Global global = (Global) session.getAttribute("sGlobal");
			boolean dispAmntFlag = false;

			if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
				dispAmntFlag = true;
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
			boolean isOneRecord = false;
			for(int i=0;i <additionService.size();i ++){
				isOneRecord = true;
				ShipmentRecord shipRecords = (ShipmentRecord)additionService.get(i);
				subTotalImporte = subTotalImporte+Double.parseDouble(shipRecords.getCharges());
        %>        
        <tr>
          <td width="3%"></td>
          <td width="7%"><input type=text style="TEXT-ALIGN: right" name="quantity" size="4" readonly class="textreadonly" value='<%=(shipRecords.getQuantity()==null?"":shipRecords.getQuantity())%>' style="width: 54; height: 18" ></td>
          <td width="14%"><input type=text name="description" size="12" readonly=true class="textreadonly" value='<%=shipRecords.getDescription()==null?"":shipRecords.getDescription()%>' style="width: 105; height: 18"></td>
          <td width="45%"><input type=text name="content" size="30" readonly=true class="textreadonly" value='<%=shipRecords.getContent()==null?"":shipRecords.getContent()%>' style="width: 342; height: 18"></td>
          <td width="8%"><input type=text style="TEXT-ALIGN: right" name="peso" size="4" readonly=true class="textreadonly" value='<%=shipRecords.getPeso()==null?"":shipRecords.getPeso()%>' style="width: 60; height: 18"></td>
          <td width="10%"><input type=text style="TEXT-ALIGN: right" name="volume" size="7" readonly=true class="textreadonly" value='<%=shipRecords.getVolume()==null?"":shipRecords.getVolume()%>' style="width: 72; height: 18"></td>

          
          <%
          
	  		//if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
	  			if(dispAmntFlag)	
			{
		%>
	          <td width="23%">
    		      	<input type=text style="TEXT-ALIGN: right" name="charges" size="8" readonly=true 
          			class="textreadonly" value='<%=shipRecords.getCharges()==null?"":df.format(Double.parseDouble(shipRecords.getCharges()))%>' 
          			style="width: 64; height: 18">
	          </td>
	    <%
			}
			else
			{
		%>
			<td width="23%">
          		<input type=text style="TEXT-ALIGN: right" name="charges" size="8" readonly=true 
          			class="textreadonly" value="**********" 
          			style="width: 64; height: 18">
         	 </td>
		<%
			}
		%>
        </tr>
        <% 
			}                
			int inc = additionService.size();
			while(inc < 6){
        %>
        <tr>
          <td width="3%"></td>
          <td width="7%"><input type=text name="quantity" size="4" readonly class="textreadonly" style="width: 54; height: 18" ></td>
          <td width="14%"><input type=text name="description" size="12" readonly=true class="textreadonly" style="width: 105; height: 18"></td>
          <td width="45%"><input type=text name="content" size="30" readonly=true class="textreadonly" style="width: 343; height: 18"></td>
          <td width="8%"><input type=text name="peso" size="4" readonly=true class="textreadonly" style="width: 58; height: 18"></td>
          <td width="10%"><input type=text name="volume" size="7" readonly=true class="textreadonly" style="width: 72; height: 18"></td>
          <td width="23%"><input type=text name="charges" size="8" readonly=true class="textreadonly" style="width: 64; height: 18"></td>
        </tr>
        <% 
				inc++; 
			}
        %>
        <tr>
          <td width="3%"></td>
          <td width="7%"></td>
          <td width="14%"></td>
          <td width="45%"></td>
          <td width="8%"></td>
          <td width="10%">
          <p align="right"><font face="Arial" size="2">Sub - Total&nbsp; </font></td>          
          
	
			<%
      //    if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
    	  if(dispAmntFlag)	
			{
				if(isOneRecord)
				{
          %>
		       <td width="23%"><input type="text" style="TEXT-ALIGN: right" value="<%= df.format(subTotalImporte) %>" readonly name="chargeSubTotal"  class="textreadonly" style="width: 64; height: 18" size="20" ></td>
          <%
				}
				else
				{
          %>
	          <td width="23%"><input type="text" style="TEXT-ALIGN: right" readonly name="chargeSubTotal"  class="textreadonly" style="width: 64; height: 18" size="20" ></td>
          <%
				}
		  	}
          else
          {
        	  if(isOneRecord)
				{
        %>

		          <td width="23%"><input type="text" style="TEXT-ALIGN: right" value="**********" readonly name="chargeSubTotal"  class="textreadonly" style="width: 64; height: 18" size="20" ></td>
        <%
				}
				else
				{
        %>
	          <td width="23%"><input type="text" style="TEXT-ALIGN: right" value="**********" readonly name="chargeSubTotal"  class="textreadonly" style="width: 64; height: 18" size="20" ></td>
        <%
				}
          }
        %>
<!-- -->
			
			
			
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
          <td width="2%">
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <%
               /*ArrayList addService = (ArrayList)request.getAttribute("additionService");
               int addServInc = addService.size();     
               for (int i=0;i< addServInc; i++){ 
			   AddServRecord addServRec = (AddServRecord)addService.get(i);*/
              %>               
				<tr>
                <!--<td width="75%" colspan="3"><input type="text" readonly class="textreadonly" name="description" size="37" value='<%--addServRec.getDescription()==null?"":addServRec.getDescription()--%>'></td>-->
                <!--<td width="75%" colspan="3"><input type="text" readonly class="textreadonly" name="description" size="37" value='<%--addServRec.getDescription()==null?"":addServRec.getDescription()--%>'></td>-->
                <!--<td width="22%"><input type="text" name="total" readonly class="textreadonly" name="total" size="12" value='<%--addServRec.getTotal()==null?"":addServRec.getTotal()--%>'></td>-->
              </tr>                             
              <%//}              
              //while( addServInc < 6){%>
				<!--<tr>
                <td width="3%"></td>
                <td width="75%" colspan="3"><input type="text" readonly class="textreadonly" name="description" size="37"></td>
                <td width="22%"><input type="text" name="total" readonly class="textreadonly" name="total" size="12"></td>-->
				<% 
				//addServInc ++;
               // }
               %>
              
              <tr>
                <td width="3%"></td>
                <!--<td width="22%"><input type="text" readonly class="textreadonly" name="subTotal" size="12"></td>-->
              </tr>              
            </table>
          </td>
          <td width="98%" valign=top>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td width="1%"></td>
                <td width="85%" colspan="2"><b><font size="2" face="Arial">&nbsp;&nbsp;
                Requisitos de Servicios</font></b></td>
                <td width="14%"></td>
              </tr>
              <tr>
                <td width="1%"></td>
                <td width="85%" colspan="2"><font size="2" face="Arial">Requistos</font></td>
                <td width="14%"></td>
              </tr>
              <% 
				ArrayList addReqs = (ArrayList)request.getAttribute("additionRequirement");	
				int addReqInc = addReqs.size();
				for(int i=0;i < addReqInc; i++) {%>              
              <tr>
                <td width="1%"></td>
                <td width="85%" colspan="2"><input type="text" readonly class="textreadonly" name="T75" size="47" style="width: 635; height: 18"></td>
                <td width="14%"></td>
              </tr>
              <% } 
              while(addReqInc < 6){%>
				<tr>
                <td width="1%"></td>
                <td width="85%" colspan="2"><input type="text" readonly class="textreadonly" name="T75" size="47" style="width: 634; height: 18"></td>
                <td width="14%"></td>
              </tr>              	
             <%
             addReqInc++;
              }
              %>
              <tr>
                <td width="1%"></td>
                <td width="42%">
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
      <table border="0" width="90%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5%"></td>
          <td width="99%" colspan="2"><font face="Arial" size="2">Persona que
            Envia</font></td>
        </tr>
        <tr>
          <td width="2%"></td>          
          <td width="10%"><html:text readonly="true" styleClass="textreadonly" style="width:90;height:18" property="addInfo"/></td>
          <td width="90%"><html:text readonly="true" styleClass="textreadonly" style="width:550;height:18" property="comment"/></td>
        </tr>
        <tr>
          <td width="100%" colspan="3"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<html:hidden property="hidGuiaNumber"/>
<html:hidden property="formaPago"/><!-- AAP01 -->
<html:hidden property="docuType"/><!-- AAP02 -->
<html:hidden property="manifiestoType"/><!-- AAP02 -->
<html:hidden property="preferedAddressCode"/><!-- AAP03 -->
<html:hidden property="preferedAddress"/><!-- AAP03 -->
</html:form>
<map name="Map">
  <area shape="rect" coords="370,6,430,18" href="javascript:webGuiaDetails()">
</map>
</body>
</html>
