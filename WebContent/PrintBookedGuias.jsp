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
<%@page contentType="text/html" import="bean.JavManifestRecord,bean.Global"%>

<%@page  import="java.util.*"%>
<% Global global =(Global)session.getAttribute("sGlobal"); 
	
	boolean dispAmntFlag = false;
	
	if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
		dispAmntFlag = true;
	
	String formaPago = "";//AAP01	
	
	if ( request.getParameter("formaPago") != null && request.getParameter("formaPago").length()>0) {//AAP01
		formaPago = request.getParameter("formaPago");//AAP01
	}//AAP01
	
	String docuType = "";//AAP02
	if ( request.getParameter("docuType") != null && request.getParameter("docuType").length()>0) {//AAP02
		docuType = request.getParameter("docuType");//AAP02
	}//AAP02
	
	String manifiestoType = "";//AAP02
	if ( request.getParameter("manifiestoType") != null && request.getParameter("manifiestoType").length()>0) {//AAP02
		manifiestoType = request.getParameter("manifiestoType");//AAP02
	}//AAP02
	
	String preferedAddress = "";//AAP03
	if ( request.getParameter("preferedAddress") != null && request.getParameter("preferedAddress").length()>0) {//AAP03
		preferedAddress = request.getParameter("preferedAddress");//AAP03
	}//AAP03
	String preferedAddressCode = "";//AAP03
	if ( request.getParameter("preferedAddressCode") != null && request.getParameter("preferedAddressCode").length()>0) {//AAP03
		preferedAddressCode = request.getParameter("preferedAddressCode");//AAP03
	}//AAP03
	
%>

<html>
<head>
<link rel="stylesheet" href="webbooking.css">

<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />


<title>Guias Documentadas</title>
<script language="JavaScript" type="text/JavaScript"> 
	function callManifestGeneration(){
		document.forms[0].submit();
	}
</script>

</head>
<body   topmargin="0" leftmargin="0" marginwidth=0 marginheight=0>
<form  action="manifestgeneration.do"> 
  <table border="0" width="100%" cellspacing="0" cellpadding="0"  height="181">
    <tr> 
      <td width="100%" height="132" colspan="5" align=:center">
<p align ="center"> <strong class="titleSection fontBold">Guias Documentadas</strong></p>
				
        <table width="99%" height="93" border="1" cellpadding="0" cellspacing="0" class="tablaDetallev2" >
          <tr class="tablaDetallev2Header" > 
           
            <td width="102" height="41" valign="bottom" align="left"><strong><font face="Arial" size="2">No 
              Guia</font></strong></td>
            <td width="91" height="41" valign="bottom" align="left"><strong><font face="Arial" size="2">No. 
              Rastreo</font></strong></td>
            <td width="118" height="41" valign="bottom" align="left"><strong><font face="Arial" size="2">Sucursal 
              Destino</font></strong></td>
            <td width="264" height="41" valign="bottom" align="left"><strong><font face="Arial" size="2">Cliente 
              Destino</font></strong></td>
            <td width="95" height="41" valign="bottom" align="right"> 
              <p align="right"><strong><font face="Arial" size="2">Importe</font></strong></td>
            <td width="78" height="41" valign="bottom" align="right"> 
              <p align="center"><strong><font face="Arial" size="2">Total<br>
                No.de<br>
                Paquetes</font></strong></td>
          
          </tr>
          <%     
          			
					ArrayList manifestDetails =(ArrayList)request.getAttribute("sBookedGuiasDetails");					
					int lenmanifestDetails = manifestDetails.size();
					
					for(int i=0;i<lenmanifestDetails; i++){ 
						JavManifestRecord manifestRecords = (JavManifestRecord)manifestDetails.get(i);													
				 %>
          <tr class="tablaDetallev2Body"> 
            
            <td width="102" height="17" nowrap class="td" align="left">&nbsp;<%=manifestRecords.getOrigBranch()%><%=manifestRecords.getFormNumber()%> 
            </td>
            <td width="91" height="17" nowrap class="td" >&nbsp;<%=manifestRecords.getGuiaNumber()%> 
            </td>
            <td width="118" height="17" nowrap class="td" >&nbsp; <%= manifestRecords.getDestBranch()%> 
            </td>
            <td width="264" height="17" nowrap class="td" >&nbsp;<%= manifestRecords.getDestClientName()%> 
            </td>
            <td width="95" height="17" nowrap class="td" align="right">
            		<%
						//if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
						if(dispAmntFlag)
						{
				  	%>
            				<%= manifestRecords.getGuiaAmount()%> 
            		<%
						}
						else
						{
					%>
							<font size="2"><b>******</b></font>
					<%
						}
					%>
            </td>
            <td width="78" height="17" nowrap class="td" align="right">&nbsp;	
              <%= manifestRecords.getNumPack()%> </td>
          </tr>
          <%
              	}				
                %>
          <tr> 
            <td height="2" colspan="5"> </td>
          </tr>
        </table></td>
    </tr>
    <tr>
      <td colspan="5" align="center" ><input style="margin-top: 6px;" class="button buttonMedium" type="button" name="Regresar"  value="Return" onClick="callManifestGeneration()" ></td>
    </tr>
  </table>
  <input type="hidden" name="formaPago" value="<%=formaPago%>"/><!-- AAP01 -->
  <input type="hidden" name="docuType" value="<%=docuType%>"/><!-- AAP02 -->  
  <input type="hidden" name="manifiestoType" value="<%=manifiestoType%>"/><!-- AAP02 -->
  <input type="hidden" name="preferedAddressCode" value="<%=preferedAddressCode%>"/><!-- AAP03 -->
  <input type="hidden" name="preferedAddress" value="<%=preferedAddress%>"/><!-- AAP03 -->
</form>
</body>
</html>



