<%--
Authur		:	Ramachandran.V
Date		:	25-March-2003

FileName	:	JavWebBookingServicesAditional.jsp
FormBean	:	JavWebBookingServicesAditionalForm.class
ActionBean	:	JavWebBookingServicesAditionalAction.class
OtherClasses	:	none
CSS FileName	:	webbooking.css
--%>

<%@ page info="WebBooking - ServicesAditional" %>
<%-- Global added by palanivel 

--%>
<%@ page import="java.util.*,bean.*,logger.*" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>

<head>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<title>Services</title>
<link rel="stylesheet" href="webbooking.css" type="text/css">
</head>

<body marginwidth="0" marginheight="0" topmargin=0 leftmargin=0 background="images/bg.gif" oncontextmenu="return false" onload="//init();">
<script language=javascript>
<%
	Global global = (Global)session.getAttribute("sGlobal");
	String shown=request.getParameter("shown");
	JavWebBookingServicesForm serForm = (JavWebBookingServicesForm) session.getAttribute("webBookingservices");//AAP01
	
	String zonaExtendida = serForm.getZonaExtendida();//AAP01
	//AAPP//System.out.println("SHOWN IN ADITIONAL JSP "+shown);
	if(shown!=null && shown.equals("true")){
%>

	var shown='true';
<%
	}else if(shown!=null && shown.equals("false")){
%>
	var shown='false';
<%
	}
%>
function callServices(){	
	document.forms[0].actionval.value="calculate";
	document.forms[0].shown.value=shown;
	document.frm.action="JavWebBookingServices.jsp";
	document.frm.submit();
}
/*Funcion para realizar validacion de zona extendida //AAP01*/
function init(){
	//alert('zona Extendidaaas ==>'+document.forms[0].zonaExtendida.value);
	if (document.forms[0].zonaExtendida.value.substring(0,1) =='Y'){
		document.getElementById('flash2').style.visibility='visible';
		setInterval('flash2.filters.alpha.opacity=(flash2.filters.alpha.opacity+5)%100',100);
	}	
}
</script>
<form name=frm>
<jsp:include page="nocache.jsp" flush="false" />
<table border="0" cellspacing="0" cellpadding="0" width="769">
  <tr> 
    <td width="429" height="77"><IMG border=0 height=79 src="images/inner_top01.jpg" useMap=#peMap width=429></td>
    <td width="342" height="77"> 
      <div align="left"><IMG height=79 src="images/inner_top02.jpg" width=341></div>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="3"></td>
  </tr>
</table>
<map name="peMap"> 
  <area shape="RECT" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com">
</map>
<br>

<table width="780" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="1%">&nbsp;</td>
    <td width="77%"><b>Servicios Adicionales </b></td>
    <td width="22%">&nbsp;</td>
  </tr>  
  <tr> 
    <td width="1%">&nbsp;</td>
    <td width="77%">
      <table width="780" border="0" cellspacing="0" cellpadding="0" bgcolor="#cedee9">
        <tr> 
          <td height="160">
            <table bgcolor="#cedee9" align=center border="0" cellpadding="0" cellspacing="0" width="97%">
              <tr> 
                <td width="71%">&nbsp;</td>
              </tr>
              <tr> 
                <td width="71%"> 
                  <table bgcolor="#cedee9" align=center border="0" cellpadding="0" cellspacing="1" width="97%">
                    <tr> 
                      <td width="34%" height="23">&nbsp;Descripcion</td>
                      <td width="21%" height="23"> 
                        <p align="right">Sub-total</p>
                      </td>
                      <td width="17%" height="23"> 
                        <p align="right">Descuento</p>
                      </td>
                      <td width="28%" height="23"> 
                        <p align="right">Total</p>
                      </td>
                    </tr>
                    
		<%
		ArrayList servicesList = (ArrayList)session.getAttribute("calculatedservicelist");
		/*Available Services Object..
		1.shipE	2.shipG	3.rad	4.ead	5.ack	6.cod	7.codr	8.inv 9.Add 
		*/
		for(int i=0;i<servicesList.size();i++){
			Hashtable servicesTable = (Hashtable)servicesList.get(i);
			
			
			if(servicesTable.get("newservice")!=null){
			ArrayList listOfservices=new ArrayList();
			listOfservices=(ArrayList)servicesTable.get("newservice");
			for(int index=0;index<listOfservices.size();index++)
			{
				Hashtable serv_new=new Hashtable();
				AccessLog.Log(""+global.clientId+"INSIDE RAD");
				serv_new=(Hashtable)listOfservices.get(index);
			    Set set_new=serv_new.keySet();
			    Iterator new_iterator=set_new.iterator();
			    for(;new_iterator.hasNext();)
			    {
			    String key=(String)new_iterator.next();
			    Services servicesTemp=(Services)serv_new.get(key);
			    %>
			    <tr> 
		<td width="34%" class="td" height="21"><%= servicesTemp.descriptionAditional %></td>
		<td width="21%" class="td" height="21" align="right">$<%= servicesTemp.GS_SUB_TOTL %></td>
		<td width="17%" class="td" height="21" align="right">$<%= servicesTemp.GS_DISC %></td>
		<td width="28%" class="td" height="21" align="right">$<%= servicesTemp.TOTAL %></td>
             	</tr>
			    <%
			      }
								
			}
							
						
			}
			
			
        
			else if((Services)servicesTable.get("rad")!=null){
					Services servicesTemp = (Services)servicesTable.get("rad");
		%>		
			<tr> 
				<td width="34%" class="td" height="21"><%= servicesTemp.descriptionAditional %></td>
				<td width="21%" class="td" height="21" align="right">$<%= servicesTemp.GS_SUB_TOTL %></td>
				<td width="17%" class="td" height="21" align="right">$<%= servicesTemp.GS_DISC %></td>
				<td width="28%" class="td" height="21" align="right">$<%= servicesTemp.TOTAL %></td>
             </tr>
        <%
			}else if((Services)servicesTable.get("ead")!=null){
				Services servicesTemp = (Services)servicesTable.get("ead");				
		%>
			<tr> 
				<td width="34%" class="td" height="21"><%= servicesTemp.descriptionAditional %></td>
				<td width="21%" class="td" height="21" align="right">$<%= servicesTemp.GS_SUB_TOTL %></td>
				<td width="17%" class="td" height="21" align="right">$<%= servicesTemp.GS_DISC %></td>
				<td width="28%" class="td" height="21" align="right">$<%= servicesTemp.TOTAL %></td>
             </tr>
	   <%
			}else if((Services)servicesTable.get("ack")!=null) {
				Services servicesTemp = (Services)servicesTable.get("ack");				
		%>
			<tr> 
				<td width="34%" class="td" height="21"><%= servicesTemp.descriptionAditional %></td>
				<td width="21%" class="td" height="21" align="right">$<%= servicesTemp.GS_SUB_TOTL %></td>
				<td width="17%" class="td" height="21" align="right">$<%= servicesTemp.GS_DISC %></td>
				<td width="28%" class="td" height="21" align="right">$<%= servicesTemp.TOTAL %></td>
             </tr>
		<%
			}else if((Services)servicesTable.get("cod")!=null){
	
			
		      
              Services servicesTemp = (Services)servicesTable.get("cod");
			  if (global.tarifType != null && global.tarifType.equalsIgnoreCase("C"))
			  {
				     if(((String)session.getAttribute("valorCod")) != null 
						 && ((String)session.getAttribute("valorCod")).trim().length()>0 )

					  {		
					     
							
		%>
			<tr>
								<td width="34%" class="td" height="21"><%= servicesTemp.descriptionAditional %></td>
								<td width="21%" class="td" height="21" align="right">$<%= servicesTemp.GS_SUB_TOTL %></td>
								<td width="17%" class="td" height="21" align="right">$<%= servicesTemp.GS_DISC %></td>
								<td width="28%" class="td" height="21" align="right">$<%= servicesTemp.TOTAL %></td>
							 </tr>
        <%
	}
							
			 }else
			  { 
				 %>
			       
						       <tr> 
								<td width="34%" class="td" height="21"><%= servicesTemp.descriptionAditional %></td>
								<td width="21%" class="td" height="21" align="right">$<%= servicesTemp.GS_SUB_TOTL %></td>
								<td width="17%" class="td" height="21" align="right">$<%= servicesTemp.GS_DISC %></td>
								<td width="28%" class="td" height="21" align="right">$<%= servicesTemp.TOTAL %></td>
							 </tr>
					 <% }
				}else if((Services)servicesTable.get("codr")!=null){
 
								 
			 		Services servicesTemp = (Services)servicesTable.get("codr");				
		%>
			<tr> 
				<td width="34%" class="td" height="21"><%= servicesTemp.descriptionAditional %></td>
				<td width="21%" class="td" height="21" align="right">$<%= servicesTemp.GS_SUB_TOTL %></td>
				<td width="17%" class="td" height="21" align="right">$<%= servicesTemp.GS_DISC %></td>
				<td width="28%" class="td" height="21" align="right">$<%= servicesTemp.TOTAL %></td>
             </tr>
        <%
	}else if((Services)servicesTable.get("inv")!=null){
	%>
	<logic:notPresent name="NOINSURANCE">
	<%
				Services servicesTemp = (Services)servicesTable.get("inv");
		%>
			<tr> 
				<td width="34%" class="td" height="21"><%= servicesTemp.descriptionAditional %></td>
				<td width="21%" class="td" height="21" align="right">$<%= servicesTemp.GS_SUB_TOTL %></td>
				<td width="17%" class="td" height="21" align="right">$<%= servicesTemp.GS_DISC %></td>
				<td width="28%" class="td" height="21" align="right">$<%= servicesTemp.TOTAL %></td>
             </tr></logic:notPresent>
        <%
        
			}
		
			 //Code added by palanivel to display Additional service Values
				
				if(global.tarifType != null && global.tarifType.equalsIgnoreCase("C"))
			    {
					 
					  if(session.getAttribute("aditionalServicesDetail") != null)
					 {
					     ArrayList additionalServicesArray = null ;
					     additionalServicesArray =  (ArrayList) session.getAttribute("aditionalServicesDetail");
					     					
						 int arraySize =  additionalServicesArray. size();
						 for(int j=0;j<arraySize;j++)
						{
					
							AdditionalService serviceRecordsRecs = (AdditionalService)additionalServicesArray.get(j);
							if((Services)servicesTable.get(serviceRecordsRecs.service)!=null)
							{
							Services servicesTemp = (Services)servicesTable.get(serviceRecordsRecs.service);
						%>
						<tr> 
							<td width="34%" class="td" height="21"><%= servicesTemp.descriptionAditional %></td>
							<td width="21%" class="td" height="21" align="right">$<%= servicesTemp.GS_SUB_TOTL %></td>
							<td width="17%" class="td" height="21" align="right">$<%= servicesTemp.GS_DISC %></td>
							<td width="28%" class="td" height="21" align="right">$<%= servicesTemp.TOTAL %></td>
						 </tr>
						<%
							}
						}
					 }
				}

			  }
		%>
			</table>
                </td>
              </tr>              
            </table>
          </td>
        </tr>
        <tr class="textMsgZExt"><!-- AAP01 -->
        	<td align="center" id="flash2" style="filter:alpha(opacity=100);visibility:hidden;" >Entrega a domicilio, es Zona Plus</td>
        </tr><!-- AAP01 -->
        <tr>
        	<td align="center"><html:button property="return" value="Regresar" styleClass="button" onclick="callServices()" /></td>	
        </tr>
      </table>
    </td>
    <td width="22%">&nbsp;</td>
  </tr>
  <tr> 
    <td width="1%">&nbsp;</td>
    <td width="77%">&nbsp;</td>
    <td width="22%">&nbsp;</td>
  </tr>
</table>
<input type=hidden name=actionval >
<input type=hidden name=shown >
<html:hidden property="zonaExtendida" value= "<%=zonaExtendida%>"/>
</form>
</body>
</html:html>
