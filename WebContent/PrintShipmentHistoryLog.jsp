<%@ page contentType="text/html" import="java.util.*,bean.JavManifestSent,bean.JavManifestNotSent,bean.Global"%>

<%
	String manifestDate = (String)request.getAttribute("manifestdate");
%>


<html>
<head>
<script language="javascript">

function returnBack(){	
	document.forms[0].action='shipmentHistory.do';
	document.forms[0].returnval.value="true";
	document.forms[0].manifestdate.value='<%= manifestDate %>'
	document.forms[0].submit();
}
</script>
<link rel="stylesheet" href="webbooking.css">
<title>Shipment History Log</title>
</head>
<body topmargin="0" leftmargin="0" marginwidth=0 marginheight=0 bgcolor="#CEDEE9">
<form action="" >
<jsp:include page="nocache.jsp" flush="false" />
  <table border="0" width="100%" cellspacing="0" cellpadding="0" >
    <tr>
      <td width="100%"> 
      <table border="0" width="100%" cellspacing="0" cellpadding="0" height="356" >
		<tr> 
            <td height="16" colspan="2" align="center">
				<font face="Arial" size="2"><b>Consulta de Manifiestos</b></font> 
				<font size="2" face="Arial">&nbsp; 
				</font></td>
			</tr>
		  <tr> 
            <td height="16" align="center">
				<font size="2" face="Arial"><b>Manifiestos</b></font></td>            
				</tr>
          <tr>
            <td width="100%" height="77"> 
			<table width="95%" align="center" border="1" align="left" cellpadding="0" cellspacing="0">
			<tr> 
                  <td width="93" align="center" valign="bottom"><strong><font face="Arial" size="2">Manifiesto
					</font></strong></td>
                  <td width="82" align="center" valign="bottom"> 
                    <p align="center"><strong><font face="Arial" size="2">No. de<br>Guías
					</font></strong></td>
                  <td width="56" align="center" valign="bottom"> 
                    <p align="center"><strong><font face="Arial" size="2"> No. de<br>
                      Paquetes</font></strong></td>
                  <td width="90" align="center" valign="bottom"> 
                    <p align="center"><strong><font face="Arial" size="2">
                      Importe</font></strong></td>
                  <td width="158" align="center" valign="bottom"> 
                    <p align="center"><strong><font face="Arial" size="2">Status del Manifiesto
                    </font></strong></p></td>
                  <td colspan="2" align="center" nowrap valign="bottom"> <p align="left"><strong><font face="Arial" size="2">Hora de Generación</font></strong></p></td>
                </tr>
                <!--------------Start of Multi Record Block ---------------->
                <%
              	Object manifestObject = request.getAttribute("printSentValues");

            	Global global = (Global) session.getAttribute("sGlobal");
            	boolean dispAmntFlag = false;

            	if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
            		dispAmntFlag = true;
              	if(manifestObject != null){
              		ArrayList manifestSent =(ArrayList) manifestObject;
              		int manifestSentSize = manifestSent.size();         	
                     if(manifestSentSize >0)
					 {
              		for(int i=0;i < manifestSentSize; i++){
							JavManifestSent manifestSentRecs = (JavManifestSent) manifestSent.get(i);     	
                %>
                <tr> 
                  <td width="93"  class="td" align="left" height="18" nowrap> 
<p align="right"><%=manifestSentRecs.getManifestNumber().trim()%></td>
                  <td width="82"  class="td" align="right" nowrap height="18"> 
                    <p align="right"><%=manifestSentRecs.getGuiaNumber()%></td>
                  <td width="56"  class="td" align="right" nowrap height="18"><%=manifestSentRecs.getNoOfPackages()%></td>
                  <td width="90"  class="td" align="right" nowrap height="18">
                  	<%
                  	//	if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
                  		if(dispAmntFlag)
                  		{
                  	%>
	                  		<%=manifestSentRecs.getManifestAmount()%>
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
                  <td width="158"  class="td" nowrap height="18">&nbsp;<%=(manifestSentRecs.getManifestStatus()==null?"":manifestSentRecs.getManifestStatus())%></td>
                  <td width="201"  class="td" nowrap height="18"><%=(manifestSentRecs.getPreferredCollectionTime()==null?"":manifestSentRecs.getPreferredCollectionTime())%></td>
                 
                </tr>
                <%
              		}        	
             
			  }else{
              %>
              <tr>
              			<td height="26" colspan=7 align=center>
              				<B><font color=red >No Records Found</font></B>
              			</td>
              		</tr>          
			  
                <% 
				  } 
				  }
				%>
                <!--------------End of Multi Record Block ------------------>
              </table></td>
          </tr>
		  <tr> 
            <td height="16" align="left">&nbsp;</td>           
          </tr>
		  <tr> 
            <td height="16" align="left">&nbsp;</td>           
          </tr>
          <tr> 
            <td height="16" align="center"><font size="2" face="Arial"><b></b></font></td>           
          </tr>
		 
          <tr> 
            <td width="100%" height="77"> 
			<table width="95%" align="center" border="1" align="left" cellpadding="0" cellspacing="0">
				<tr> 
                  <td width="93" align="center" valign="bottom"><strong><font face="Arial" size="2">Manifiesto
					</font></strong></td>
                  <td width="82" align="center" valign="bottom"> 
                    <p align="center"><strong><font face="Arial" size="2">No. de<br>Guías
					</font></strong></td>
                  <td width="56" align="center" valign="bottom"> 
                    <p align="center"><strong><font face="Arial" size="2"> No. de<br>
                      Paquetes</font></strong></td>
                  <td width="90" align="center" valign="bottom"> 
                    <p align="center"><strong><font face="Arial" size="2">
                      Importe</font></strong></td>
                  <td width="158" align="center" valign="bottom"> 
                    <p align="center"><strong><font face="Arial" size="2">Status del Manifiesto
                    </font></strong></p></td>
                  <td colspan="2" align="center" nowrap valign="bottom"> <p align="left"><strong><font face="Arial" size="2">Hora de Generación</font></strong></p></td>
                </tr>
                <!---------------------- start of Second multirecord block --------------->
                <%
              	Object notSentManifestObject = request.getAttribute("printNotsentValues");
              	if(notSentManifestObject != null){
              		ArrayList manifestNotSent = (ArrayList) notSentManifestObject;
						int manifestNotSentSize = manifestNotSent.size();
						if(manifestNotSentSize!=0){
							for(int i=0;i < manifestNotSentSize;i++){											
								JavManifestNotSent manifestNotSentRecs = (JavManifestNotSent)manifestNotSent.get(i);
				%>
                <tr> 
                  <td width="89"  class="td" align="left" nowrap height="19"> 
                    <%= manifestNotSentRecs.getManifestNotSentNumber().trim() %></td>
                  <td width="77"  class="td" align="right" nowrap height="19"> 
                    <p align="right"><%= manifestNotSentRecs.getGuiaNotSentNumber() %>&nbsp;</td>
                  <td width="53"  class="td" align="right" nowrap height="19"><%= manifestNotSentRecs.getNoOfNotSentPackages()%>&nbsp;</td>
                  
                  <td width="86"  class="td" align="right" nowrap height="19">
                  <%
	//                	Global global = (Global) session.getAttribute("sGlobal");
                  		//if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
                  			if(dispAmntFlag)
                  		{
                  	%>
	                  		<%= manifestNotSentRecs.getManifestNotSentAmount()%>&nbsp;
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
                  
                  <td width="148"  class="td" nowrap height="19"><%= (manifestNotSentRecs.getManifestNotSentStatus()==null?"":manifestNotSentRecs.getManifestNotSentStatus())%>&nbsp;</td>
                  <td width="188"  class="td" nowrap height="19"><%= (manifestNotSentRecs.getPreferredCollectionNotSentTime()==null?"":manifestNotSentRecs.getPreferredCollectionNotSentTime()) %>&nbsp;</td>
                  
                </tr>
                <%
              			}
              		}else{
              	%>
              		<tr>
              			<td height="26" colspan=7 align=center>
              				<B><font color=red >No Records Found</font></B>
              			</td>
              		</tr>
              	<%
              		}
				}
              %>
                <!------------------- End of second multi record block ------------------>
              </table></td>
          </tr>
          <tr> 
            <td width="100%" colspan="2" height="29"> 
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
            <tr> 
                  <td align="center"> 
					<input type="button" class="button" value="Regresar" onClick='returnBack()' name="Return"> 
                  </td>
                </tr>
              </table></td>
          </tr>
          <tr> 
            <td width="100%" colspan="2" height="21"></td>
          </tr>
        </table></td>
  </tr>
</table>
<input type=hidden name="returnval">
<input type=hidden name="manifestdate">
</form>
</body>
</html>

