<%@ page contentType="text/html" import="java.util.*,bean.Global"%>

<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache");		//HTTP 1.0
	response.setDateHeader ("Expires", 0);			//prevents caching at the proxy server
%>

<%	
	String MANIFEST_STATUS = (String)request.getAttribute("MANIFEST_STATUS");
	String NUMBER_OF_GUIAS = (String)request.getAttribute("NUMBER_OF_GUIAS");
	String MANIFEST_AMOUNT = (String)request.getAttribute("MANIFEST_AMOUNT");
	String NUMBER_OF_PACKAGES = (String)request.getAttribute("NUMBER_OF_PACKAGES");
	String PREFERED_COLLECTION_TIME = (String)request.getAttribute("PREFERED_COLLECTION_TIME");	
%>

<html>
<head>
<title>Manifest Details</title>	
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
</head>
<body text=#000000 bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0"  >
<form method="post" name="form1">

<map name="peMap"> 
  <area shape="rect" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com">
</map>

<table border="0" cellspacing="0" cellpadding="0" width="99%" style="WIDTH: 99%">
<tr>
	<td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td>&nbsp;</td>
                <% 
					Global global = (Global)session.getAttribute("sGlobal");
					ArrayList result = (ArrayList) request.getAttribute("defaultaddress");
					String strtDrnr="";
					for(int i=0;i < result.size(); i++){
						HashMap values = (HashMap) result.get(i);
						strtDrnr = values.get("AM_STRT_NAME")+" "+values.get("AM_DRNR");
						String rfc = ((global.rfc!=null && global.rfc.length()>0)?"RFC: "+global.rfc:"");
				%>
                <td> 
                  <div align="center">
					<font face="Arial" size="2">
						<b><%= global.clientName %></b><br>
						<b><%= strtDrnr %></b><br>
						<b><%= values.get("u16") %></b><br>
						<b><%= values.get("u14") %></b><br>
						<b><%=  rfc %></b>
					</font> 
                  </div>
                </td>
                <%
					}
                %>
                <td>&nbsp;</td>
              </tr>
              <tr> 
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
         </table>
	</td>
</tr>

<tr>
	<td width="100%">
		<table border="1" width="100%" cellspacing="0" cellpadding="0" bgcolor="#CEDEE9">
		<tr> 
			<td align="center" colspan="2"><b><font size="2" face="Arial">Detalle del Manifiesto</font></b></td>
		</tr>
		<tr> 
			<td width="100%" colspan="0"> 
				<table border="1" width="100%" cellspacing="0" cellpadding="0">
					<tr> 
						<td width="24%" align="right"><font face="Arial" size="2">
						No. Manifiesto&nbsp; </font></td>
						<td width="14%" class="mrbtd" align="left"><%= session.getAttribute("MANIFEST_NUMBER").toString() %></td>
						<td width="17%" align="right" nowrap><font face="Arial" size="2">
						Status del Manifiesto&nbsp; </font></td>
						<td width="41%" class="mrbtd">&nbsp;<%= MANIFEST_STATUS %></td>
						<td width="4%"></td>
					</tr>
					<tr> 
						<td width="24%" align="right"><font face="Arial" size="2">
						No. de Guías&nbsp; </font></td>
						<td width="14%" class="mrbtd"> 
						<P align=right>&nbsp;&nbsp;<%= NUMBER_OF_GUIAS %></P></td>
						<td width="17%" align="right"><font face="Arial" size="2">
						Importe&nbsp; </font></td>
						<td width="41%" class="mrbtd" align="right">&nbsp; <%= MANIFEST_AMOUNT %> </td>
						<td width="4%"></td>
					</tr>
					<tr> 
						<td width="24%" align="right"><font face="Arial" size="2">
						No. de Paquetes&nbsp;</font></td>
						<td width="14%" class="mrbtd"> 
						<P align=right>&nbsp;&nbsp;<%= NUMBER_OF_PACKAGES %></P></td>
						<td width="17%" align="right" nowrap><font face="Arial" size="2">
						Hora de Recolección&nbsp;</font></td>
						<td width="41%" class="mrbtd">&nbsp;<%= PREFERED_COLLECTION_TIME %></td>
						<td width="4%"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr> 
			<td width="100%" height="42"  colspan="7">&nbsp;</td>
		</tr>
		<tr> 
			<td width="100%" align="center" ><font face="Arial" size="2"><b>Guías en Manifiesto</b></font></td>
		</tr>
		<tr> 
			<td width="100%" colspan="2"> <table border="1" width="100%" cellspacing="0" cellpadding="1">
		<tr> 
			<td width="6%" valign="bottom" align="middle"> <font size="2" face="Arial" align="center">No. 
            Guia</font></td>
            <td width="7%" valign="bottom" align="middle"><font size="2" face="Arial">No. 
            Rastreao</font></td>
	    <td width="18%" valign="bottom" align="middle"><font size="2" face="Arial">Referencia
            </font></td>
            <td width="11%" valign="bottom" align="middle"><font size="2" face="Arial">Sucursal 
            Destino</font></td>
            <td width="20%" valign="bottom" align="middle"><font size="2" face="Arial">Client 
            Destino</font></td>
            <td width="8%" valign="bottom" align="middle"> <font size="2" face="Arial">importe</font></td>
            <td width="6%" valign="bottom" align="middle"> <font size="2" face="Arial"> 
            No.de<br>
            Paquetes</font></td>
             <td width="6%" valign="bottom" align="middle"> <font size="2" face="Arial"> 
	    Weight            
            </font></td>
             <td width="6%" valign="bottom" align="middle"> <font size="2" face="Arial"> 
	               Volume</font></td>
		</tr>
		<% 
		        ArrayList  manifestDetails = (ArrayList)request.getAttribute("printManifestDetails");
				if(manifestDetails!=null && manifestDetails.size()!=0){
				for(int i=0;i<manifestDetails.size();i++){
					HashMap manifestValues=(HashMap)manifestDetails.get(i);
		%>
        <tr> 
			<td class="mrbtd" align="left"><font size="1" face="Arial"> <%=manifestValues.get("GH_FORM_NO")%></font></td>

            <td class="mrbtd" align="left"><font size="1" face="Arial"> <%=manifestValues.get("GH_GUIA_NO")%></font></td>
			<td class="mrbtd" align="left"><font size="1" face="Arial"> <%=manifestValues.get("GR_GUIA_REFR")%></font></td>
            <td class="mrbtd" nowrap><font size="1" face="Arial"> <%= manifestValues.get("DESTBRANCNHNAME") %></font></td>
            <td class="mrbtd"><font size="1" face="Arial"> <%=manifestValues.get("GH_DEST_CLNT_NAME")%></font></td>
            <td class="mrbtd" align="right"><font size="1" face="Arial"> <%=manifestValues.get("GH_GUIA_AMNT")%></font></td>
            <td  class="mrbtd" align="right"><font size="1" face="Arial"> <%=manifestValues.get("GH_NUMB_PACK")%></font></td>
        <td  class="mrbtd" align="right"><font size="1" face="Arial"> <%=manifestValues.get("GH_TOTL_WGHT")%></font></td>
        <td  class="mrbtd" align="right"><font size="1" face="Arial"> <%=manifestValues.get("GH_TOTL_VLUM")%></font></td>
        </tr>
        <%
		    }
		  }
		%>	 
        </table>
     </td>
 </tr>
 
	<tr> 
      <td width="100%" height="21" colspan="2"> 
		<table border="0" width="100%" cellspacing="0" cellpadding="0">
			<tr>
				<td align="center" height="50"> <input type="button" class="button" name="return" value="Regresar" onClick="javascript:history.back()"></td>
			</tr>        
			</table>
		</td>
	</tr>
  </table>	      
  </td>
 </tr>
</table> 
  

<table width="90%" border="0" cellspacing="0" align="center">
  <tr> 
    <td colspan="3" height="60">&nbsp;</td>
  </tr>
  <tr> 
    <td width="47%" height="33">&nbsp;</td>
    <td width="5%" height="33">&nbsp;</td>
    <td width="48%" height="33">&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3" height="4"></td>
  </tr>
  <tr> 
    <td width="47%" bgcolor="#000000" height="1"><img src="images/spacer.gif" width="1" height="1"></td>
    <td width="5%" height="1"></td>
    <td width="48%" bgcolor="#000000" height="1"><img src="images/spacer.gif" width="1" height="1"></td>
  </tr>
  <tr> 
    <td width="47%"> 
      <div align="center"><b><font size="1" face="Arial"><%= global.clientName %></font></b></div>
    </td>
    <td width="5%">&nbsp;</td>
    <td width="55%"> 
      <div align="center"><b><font size="1" face="Arial">IMPULSORA DE TRANSPORTES 
        MEXICANOS SA DE CV</font></b></div>
    </td>
  </tr>
</table>
</form>
</body>
</html>