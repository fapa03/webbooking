<%--
Authur		:	Ramachandran.V
Date		:	25-March-2003

FileName	:	JavWebBookingServicesDetailEdit1.jsp
FormBean	:	JavWebBookingServicesDetailEditForm1.class
ActionBean	:	JavWebBookingServicesDetailEditAction1.class
OtherClasses	:	none
CSS FileName	:	webbooking.css

--%>
<%@ page info="WebBooking - Services" import="bean.*" %>


<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%!
		public String replaceNull(String original){
			if(original==null || original.length()==0 )
				original="&nbsp;";
				
			return original;
		}
%>

<html:html>
<head>
<title></title>
<LINK media=screen href="webbooking.css" type=text/css rel=stylesheet>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" background="images/bg.gif" oncontextmenu="return false">
<script language="javascript">
<%
	String hitCount = (String)request.getAttribute("hitcount");
	if(hitCount!=null) 
		session.setAttribute("sHitCount",hitCount);
	
	String sesHitCount = (String)session.getAttribute("sHitCount");
	//System.out.println("################# HITCOUNT IN EDIT JSP ############### "+hitCount);
	
	if(sesHitCount!=null ){
		
%>
var hitCount = <%= sesHitCount%>
<%
	}else{
%>
var hitCount = 0;
<%
	}
%>
function editRow(){	
	checkstatus=false;
	for(i=0;i<document.forms[0].radioselect.length;i++){
		checkedValue = document.forms[0].radioselect[i].checked;
		if(checkedValue)
			checkstatus=true;
		else
			continue;
	}
	if(!checkstatus){
		alert("FAVOR DE SELECCIONAR UN REGISTRO A  EDITAR");
		return;
	}
	
	document.forms[0].action='webBookingservicesdetailedit1.do?action=edit&hitcount='+hitCount;
	document.forms[0].target='addframe';
	document.forms[0].editbutton.disabled=true;
	document.forms[0].deletebutton.disabled=true;
	document.forms[0].submit();
}
function deleteRow(){
	checkstatus=false;
	for(i=0;i<document.forms[0].radioselect.length;i++){
		checkedValue = document.forms[0].radioselect[i].checked;
		if(checkedValue)
			checkstatus=true;
		else
			continue;
	}
	if(!checkstatus){
		alert("FAVOR DE SELECCIONAR UN REGISTRO A BORRAR");	
		return;
	}
	if(confirm("DESEA BORRAR EL REGISTRO SELECCIONADO?")){
		document.forms[0].action='webBookingservicesdetailedit1.do?action=delete&hitcount='+hitCount;
		document.forms[0].target='editdeleteframe';
		document.forms[0].submit();
		var deleteCount  = parent.frames[0].document.forms[0].deletehitcount.value;
		if(deleteCount==""){
			parent.frames[0].document.forms[0].deletehitcount.value=1;
		}else{
			deleteCount = parseInt(deleteCount);
			deleteCount = deleteCount+1;
			parent.frames[0].document.forms[0].deletehitcount.value=deleteCount;
			//alert('processed del count :'+deleteCount);
		}
		//alert('hitcount in delete '+hitCount);		
		if(hitCount==6){
			parent.frames[0].document.forms[0].lovbutton1.disabled=false;
			parent.frames[0].document.forms[0].cantidad.disabled=false;
			parent.frames[0].document.forms[0].descripcion.disabled=false;
			parent.frames[0].document.forms[0].contenido.disabled=false;		
			parent.frames[0].document.forms[0].peso.disabled=false;
			parent.frames[0].document.forms[0].volumen.disabled=false;
			<%
				String clicked = (String)session.getAttribute("editclicked");
				//System.out.println("CLICKED VALUE IN EDIT1 JSP  "+clicked);
				if(clicked!=null && clicked.equalsIgnoreCase("yes")){
			%>
				parent.frames[0].document.forms[0].editbutton.disabled=false;
			<%
				}else{
			%>
				parent.frames[0].document.forms[0].addbutton.disabled=false;
			<%
				}
			%>
		}
	}else{
		return;
	}
}
</script>

<html:form action="webBookingservicesdetailedit1.do">
<table border="0" align=center cellspacing="0" cellpadding="0" width="75%">
<logic:present name="zeroexist" scope="request">
	<tr>	
	<td align=center>
	<font style="font-size: 12pt; color: #ff0000; font-weight: bold">
		<bean:write name="zeroexist" />
	</font>
	</td>	
	</tr>
</logic:present>
</table>
<TABLE cellSpacing=0 cellPadding=0 width=778 bgColor=#cedee9 border=0>
  <TBODY> 
  <TR> 
    <TD width="73%" bgColor=#ffffff><B><font size="3" face="Times New Roman, Times, serif">Detalles 
      de Embarque </font></B></TD>
  <TR> 
    <TD width="73%" bgColor=#ffffff height=5></TD>
  <TR> 
    <TD width="73%"> 
        <TABLE cellSpacing=1 cellPadding=0 width=762 bgColor=#cedee9 
            border=0>
          <TBODY> 
          <TR> 
            <CENTER>
              <TD width="11%">Cantidad</TD>
              <TD width="16%">Descripcion</TD>
              <TD width="39%">Contenido</TD>
            </CENTER>
            <TD width="7%"> 
              <P align=right>Peso </P>
            </TD>
            <TD width="7%"> 
              <P align=right>Volumen </P>
            </TD>
            <TD width="16%"> 
              <P align=right>Importe </P>
            </TD>
            <TD width="2%">&nbsp;</TD>
          </TR>
          <CENTER>
            
			<%
				java.util.ArrayList servicesDetailArray = (java.util.ArrayList)session.getAttribute("servicesDetailDefault");
				int size=0;
				Global global = (Global) session.getAttribute("sGlobal");
				boolean dispAmntFlag = false;
				if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
					dispAmntFlag = true;
				
				if(servicesDetailArray!=null){
				size = servicesDetailArray.size();
				if(size==6){
					for(int i=0;i<6;i++){
						ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
			%>
			<TR> 
				<TD width="11%" height=20 class="td"><%= replaceNull(ssd.cantidad)%></TD>          
				<TD width="16%" height=20 class="td"><%= replaceNull(ssd.descripcion)%></TD>
				<TD width="39%" height=20 class="td"><%= replaceNull(ssd.contenido)%> </TD>
				<TD width="7%" height=20 align=right class="td"><%= replaceNull(ssd.peso)%> </TD>
				<TD width="7%" height=20 align=right class="td"><%= replaceNull(ssd.volumen)%> </TD>
				<%
//				if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
				if(dispAmntFlag)
				{
				%>
				<TD width="16%" height=20 align=right class="td">$<%= replaceNull(ssd.importe)%></TD>
				<%
				}
				else
				{
				%>
					<TD width="16%" height=20 class="td" align=right><font size="4"><b>**********</b></font></TD>							
				<%
				} 
				%>
				<TD width="2%" height=20>
					<input type=radio name="radioselect" value="<%=i%>">
				</TD>
			<input type=hidden name="descripcioncode<%=i%>" value="<%= ssd.descripcionCode %>">
				
            </TR>
            <%
					}
				}else{
					for(int i=0;i<size;i++){
						ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
			%>
			<TR> 
				<TD width="11%" height=20 class="td"><%= replaceNull(ssd.cantidad)%></TD>          
				<TD width="16%" height=20 class="td"><%= replaceNull(ssd.descripcion)%></TD>
				<TD width="39%" height=20 class="td"><%= replaceNull(ssd.contenido)%> </TD>
				<TD width="7%" height=20 align=right class="td"><%= replaceNull(ssd.peso)%> </TD>
				<TD width="7%" height=20 align=right class="td"><%= replaceNull(ssd.volumen)%> </TD>
				<%
				//if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
				if(dispAmntFlag)
				{
				%>
				<TD width="16%" height=20 align=right class="td">$<%= replaceNull(ssd.importe)%></TD>
				<%
				}
				else
				{
				%>
					<TD width="16%" height=20 class="td" align=right><font size="4"><b>**********</b></font></TD>							
				<%
				} 
				%>
				<TD width="2%" height=20>
					<input type=radio name="radioselect" value="<%=i%>">
				</TD>
				<input type=hidden name="descripcioncode<%=i%>" value="<%= ssd.descripcionCode %>">
            </TR>
			<%
					}
					for(int i=size;i<6;i++){
			%>
			<TR> 
              <TD width="11%" height=20 class="td">&nbsp; </TD>
              <TD width="16%" height=20 class="td">&nbsp; </TD>
              <TD width="39%" height=20 class="td">&nbsp; </TD>
              <TD width="7%" height=20 class="td">&nbsp; </TD>
              <TD width="7%" height=20 class="td">&nbsp; </TD>
              <TD width="16%" height=20 class="td">&nbsp; </TD>
              <TD width="2%" height=20> 
				<input type=radio name="radioselect" disabled="true" >				
              </TD>
            </TR>
			<%
					}
				}
			}else{ //For first invocation
				for(int i=1;i<=6;i++){
            %>			
			<TR> 
              <TD width="11%" height=20 class="td">&nbsp; </TD>
              <TD width="16%" height=20 class="td">&nbsp; </TD>
              <TD width="39%" height=20 class="td">&nbsp; </TD>
              <TD width="7%" height=20 class="td">&nbsp; </TD>
              <TD width="7%" height=20 class="td">&nbsp; </TD>
              <TD width="16%" height=20 class="td">&nbsp; </TD>
              <TD width="2%" height=20> 
                <input type=radio name="radioselect" disabled="true" >
              </TD>
            </TR>
            
            <%
				}
            }
            %>
          </CENTER>
          </TBODY> 
        </TABLE>
    </TD>
  <TR> 
    <TD width="73%" bgColor=#ffffff> 
      <table width="100%" cellspacing="0" cellpadding="0">
        <tr bgcolor="#cedee9"> 
          <td width="37%" bgcolor="#cedee9">&nbsp;</td>          
          <%
			if(size==0){
          %>
          <td width="14%"> 
            <div align="center"> 
              <html:button property="editbutton" value="Editar" styleClass="button1" disabled="true" />
            </div>
          </td>
          <td width="49%"> 
            <html:button property="editbutton" value="Borrar" styleClass="button1" disabled="true" />
          </td>
          <%
			}else{
		  %>
		  <td width="14%"> 
            <div align="center"> 
              <html:button property="editbutton" value="Editar" styleClass="button1" onclick="editRow()"/>
            </div>
          </td>
          <td width="49%"> 
            <html:button property="deletebutton" value="Borrar" styleClass="button1" onclick="deleteRow()"/>
          </td>
		  <%
			}
          %>
        </tr>
      </table>
    </TD>
  </TR>
  </TBODY> 
</TABLE>
</html:form>
</body>
</html:html>