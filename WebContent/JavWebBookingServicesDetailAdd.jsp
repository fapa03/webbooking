<%--
Authur		:	Ramachandran.V
Date		:	25-March-2003

FileName	:	JavWebBookingServicesDetailAdd.jsp
FormBean	:	JavWebBookingServicesDetailAddForm.class
ActionBean	:	JavWebBookingServicesDetailAddAction.class
OtherClasses	:	none
CSS FileName	:	webbooking.css


--%>
<%@ page info="WebBooking - Services" import="java.util.*,bean.ShipmentServiceDetail"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html:html>
<head>
<title></title>
<LINK media=screen href="webbooking.css" type=text/css rel=stylesheet>
</head>


<%
    // code added by sundar on 23/09/2003
	//AAPP//System.out.println("Inside javaweb booking service details");
    String serviceid=(String)session.getAttribute("serviceid");   
     
    String referid=(String)session.getAttribute("referserviceid");   

	String servicecode=(String)session.getAttribute("servicecode");
	String action = (String)request.getAttribute("action");
	String actionfirst = (String)request.getParameter("actionfirst");
	String arrayindex=(String)request.getAttribute("arrayindex");
	arrayindex=(arrayindex!=null?arrayindex:"-1");
	String hitCount = (String)session.getAttribute("sHitCount");
	if((action!=null && action.equals("add")) || 
		(actionfirst!=null && actionfirst.equals("add"))){
%>
      <!-- code added by sundar on 23/09/2003 -->
	<body onLoad="disableAdding('<%=serviceid%>','<%=referid%>','<%=servicecode%>')" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" background="images/bg.gif" oncontextmenu="return false" >
<%
 
	}else{
%>
	<body onload="javascript:document.forms[0].cantidad.focus()" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" background="images/bg.gif" oncontextmenu="return false">
<%
	}
%>

<script language="javascript" src="common.js">
</script>

<script language="javascript">
<%
	if(hitCount!=null){
%>
var hitCount = <%= hitCount%>
<%
	
	}else{
%>
var hitCount = 0;
<%
	}
%>

function goOut(page){
	if(confirm("DESEA PERDER LA INFORMACION?")){
		document.forms[0].action="webBookinggeneral.do?includeattribute=yes&page="+page;
		document.forms[0].target='_parent';
		document.forms[0].submit();
	}else{
		document.forms[0].cantidad.focus();
		return;
	}
}

function disableAdding(ser,ref,sercode){
  // code added by sundar on 23/09/2003 
  
   document.forms[0].ss_srvc_id.value=ser;
   document.forms[0].ss_refr_srvc_id.value=ref;
   document.forms[0].descripcioncode.value=sercode;
	//alert("HitCount onloading : "+hitCount);
	if(hitCount!=6)
		document.forms[0].cantidad.focus();
	if(hitCount==6){
		document.forms[0].addbutton.disabled=true;		
		document.forms[0].lovbutton1.disabled=true;
		document.forms[0].lovbutton2.disabled=true;
		document.forms[0].cantidad.disabled=true;
		document.forms[0].descripcion.disabled=true;
		document.forms[0].contenido.disabled=true;
		document.forms[0].tarifa.disabled=true;
		document.forms[0].peso.disabled=true;
		document.forms[0].volumen.disabled=true;
	}
}
function openLov(str){
	action=document.forms[0].actionfordup.value;
	if(str=='description'){
		//alert("action in add "+action);
		selDesc = document.forms[0].descripcion.value;
		window.open('lov.do?type='+str+'&selecteddescription='+selDesc,'abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
	}else if(str=='tariff'){
		//alert("action in add "+action);
		if(document.forms[0].descripcion.value==''){
			alert("CAPTURE UN VALOR PARA 'DESCRIPCION'");
			return;
		}
		serviceId = document.forms[0].ss_srvc_id.value;
		window.open('lov.do?type='+str+'&serviceid='+serviceId+'&action='+action+'&index=<%= arrayindex %>','abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
	}
}
function callGeneral(){
	if(document.forms[0].editmode.value=='yes'){
		alert("HAGA LOS CAMBIOS  ANTES DE NAVEGAR");		
		return;
	}
	deletehitcount = document.forms[0].deletehitcount.value;
	document.forms[0].action='webBookinggeneral.do?to=bookinggeneral&hitcount='+hitCount+'&delcount='+deletehitcount;
	document.forms[0].target='_parent';
	document.forms[0].submit();
}
function resetValues(){
	document.forms[0].cantidad.value="";
	document.forms[0].descripcion.value="";
	document.forms[0].contenido.value="";
	document.forms[0].tarifa.value="";	
	document.forms[0].peso.value="";
	document.forms[0].volumen.value="";
}

function submitForm(obj){

	objName = obj.name;	
	//alert(objName);
	cantidad = trim(document.forms[0].cantidad.value);
	descripcion=trim(document.forms[0].descripcion.value);
	contenido=trim(document.forms[0].contenido.value);
	tarifa=trim(document.forms[0].tarifa.value);
	peso=trim(document.forms[0].peso.value);
	volumen=trim(document.forms[0].volumen.value);
	refServiceId=document.forms[0].ss_refr_srvc_id.value;
	serviceId=document.forms[0].ss_srvc_id.value;
	deletehitcount = document.forms[0].deletehitcount.value;
	
	if(cantidad==''){
		alert("CAPTURE UN VALOR PARA 'CANTIDAD'");
		document.forms[0].cantidad.focus();
		return;
	}else if(descripcion==''){
		alert("CAPTURE UN VALOR PARA 'DESCRIPCION'");
		document.forms[0].lovbutton1.focus();
		return;
	}else if(contenido==''){
		alert("CAPTURE UN VALOR PARA 'CONTENIDO'");
		document.forms[0].contenido.focus();
		return;
	}else if(tarifa==''){
		alert("CAPTURE UN VALOR PARA 'TARIFA'");
		document.forms[0].lovbutton2.focus();
		return;
	}
	else if(peso==''){
		alert("CAPTURE UN VALOR PARA #1.");
		document.forms[0].peso.value="";
		document.forms[0].peso.focus();
		return;
	}else if(volumen==''){
		alert("CAPTURE UN VALOR PARA #1.");
		document.forms[0].volumen.value="";
		document.forms[0].volumen.focus();
		return;
	}		
	
	if(isNaN(peso)){
		alert("CAPTURE SOLO NUMEROS EN EL CAMPO 'PESO'");
		document.forms[0].peso.select();
		return;
	}else if(isNaN(volumen)){
		alert("CAPTURE SOLO NUMEROS EN EL CAMPO 'VOLUMEN'");
		document.forms[0].volumen.select();
		return;
	}
		
	if(tarifa=='T7'){
		if(parseFloat(peso)<61 && parseFloat(volumen)<0.51) {
			alert("AMBOS PESO Y VOLUMEN NO DEBEN DE SER MENORES QUE #1 Y #2 RESPECTIVELY");
			return;
		}
	}
	/*VALIDACION PARA PESO MAXIMO*///AAP04
	if (parseFloat(peso)>parseFloat(document.forms[0].pesoMax.value))	{
		alert("PESO NO DEBE EXCEDER DE "+document.forms[0].pesoMax.value + " KG");
		document.forms[0].peso.select();
		return;
	}
	/*if(peso!='0'){
		var number = toNumber(peso);
		document.forms[0].peso.value=number;
	}
	if(volumen!='0'){
		var number = toNumber(volumen);
		document.forms[0].volumen.value=number;
	}*/
	
	if(objName=='addbutton'){		
		//alert("REFERENCE SERVICE ID in jsp "+refServiceId);
		//alert("SERVICE ID in jsp "+serviceId);
		document.forms[0].action='webBookingservicesdetailadd.do?action=add&referenceserviceid='+refServiceId+'&serviceid='+serviceId;
		document.forms[0].target='editdeleteframe';
		document.forms[0].submit();
		resetValues();
		
		if(deletehitcount!=''){
			hitCount=hitCount-deletehitcount;
			++hitCount;
			document.forms[0].deletehitcount.value="";
		}else{
			++hitCount;
		}			
		//alert(hitCount);			
			
		if(hitCount==6){
			obj.disabled=true;
			document.forms[0].lovbutton1.disabled=true;
			document.forms[0].lovbutton2.disabled=true;
			document.forms[0].cantidad.disabled=true;
			document.forms[0].descripcion.disabled=true;
			document.forms[0].contenido.disabled=true;
			document.forms[0].tarifa.disabled=true;
			document.forms[0].peso.disabled=true;
			document.forms[0].volumen.disabled=true;
		}
			
	}else if(objName=='editbutton'){
			//alert('hitcount in edit '+hitCount);
			document.forms[0].action='webBookingservicesdetailadd.do?action=edit&arrayindex=<%=arrayindex%>&hitcount='+hitCount;
			document.forms[0].target='editdeleteframe';
			document.forms[0].submit();
			resetValues();
			document.forms[0].editbutton.name="addbutton";
			document.forms[0].editbutton.value="Agregar";			
			document.forms[0].editmode.value='';//For Edit mode
			
			if(hitCount==6){
				obj.disabled=true;
				document.forms[0].lovbutton1.disabled=true;
				document.forms[0].lovbutton2.disabled=true;
				document.forms[0].cantidad.disabled=true;
				document.forms[0].descripcion.disabled=true;
				document.forms[0].contenido.disabled=true;
				document.forms[0].tarifa.disabled=true;
				document.forms[0].peso.disabled=true;
				document.forms[0].volumen.disabled=true;				
			}
			document.forms[0].actionfordup.value='add';//For Duplication checking
	}
}

function callServices(){
	
	if(document.forms[0].editmode.value=='yes'){
		alert("HAGA LOS CAMBIOS  ANTES DE NAVEGAR");
		return;
	}
	//alert('calling services');
	//alert("hitcount :"+hitCount);
	deletehitcount = document.forms[0].deletehitcount.value;
	document.forms[0].action='webBookingservices.do?hitcount='+hitCount+'&delcount='+deletehitcount;
	document.forms[0].target='_parent';
	document.forms[0].submit();
}

//added by Murugesh on 29/05/06
function logoutLocation()
{
	window.parent.location.replace('\login.do?logout=yes');
	
}

</script>
<html:form action="webBookingservicesdetailadd.do">
<table width="75%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td><TABLE cellSpacing=0 cellPadding=0 width=769 border=0>
        <TBODY>
          <TR> 
            <TD width=429 height=77><IMG height=70 
      src="images/inner_top01.jpg" width=429 
      useMap=#peMapMap border=0></TD>
            <TD width=342 height=77> <DIV align=left><IMG height=70 
      src="images/inner_top02.jpg" 
    width=341></DIV></TD>
          </TR>
          <TR> 
            <TD colSpan=2 height=3></TD>
          </TR>
        </TBODY>
      </TABLE></td>
  </tr>
  <tr> 
    <td><TABLE cellSpacing=0 cellPadding=0 width="77%" border=0>
        <TBODY>
          <TR> 
            <TD>

	    <!--  New Image  -->  

	<logic:present name="loginForm">
   <html:hidden  name="loginForm" property="userValidate" />

  <logic:equal name="loginForm" property="userValidate" value="validUser">

<img src="images/toplink.gif" width="771" height="22" usemap="#mainMap" border="0">
      <map name="mainMap"> 

 <!-- added by bala -->
         <area shape="rect" coords="565,5,680,18" href="javascript:goOut('clientreport')">
       
		<area shape="rect" coords="460,5,555,17" href="javascript:goOut('shipmenthistory')">
        
		<area shape="rect" coords="1,4,135,18" href="javascript:goOut('cliententry')">
        
		<area shape="rect" coords="145,5,226,18" href="javascript:goOut('mainpage')">
        
		<area shape="rect" coords="236,5,342,18" href="javascript:goOut('thispage')">
        
		<area shape="rect" coords="352,5,450,18" href="javascript:goOut('guiacancel')">
       
	   <area shape="rect" coords="690,4,772,18" href="javascript:logoutLocation()" alt="Terminar Sesión" title="Terminar Sesión">
 <!-- ended  by bala -->
	  
	  </map>
   </logic:equal>
</logic:present>
	

 <!--  Old Image  -->  

	<logic:present name="loginForm">
   <html:hidden  name="loginForm" property="userValidate" />

  <logic:notEqual name="loginForm" property="userValidate" value="validUser">

<img src="images/toplink1.gif" width="771" height="22" usemap="#mainMap" border="0">
      <map name="mainMap"> 
  <area shape="rect" coords="67,6,151,18" href="javascript:goOut('mainpage')">
        <area shape="rect" coords="165,6,275,17" href="javascript:goOut('thispage')">
		<area shape="rect" coords="288,6,390,17" href="javascript:goOut('guiacancel')">
		<area shape="rect" coords="404,6,506,17" href="javascript:goOut('shipmenthistory')">
		<area shape="rect" coords="520,6,644,17" href="javascript:goOut('clientreport')">
		<area shape="rect" coords="676,6,764,17" href="javascript:logoutLocation()" alt="Terminar Sesión" title="Terminar Sesión">
  
 	
	  
	  </map>
   </logic:notEqual>
</logic:present>

	</TD>
          </TR>
          <TR> 
            <TD> 
     <DIV align=right>
		<IMG height=25 src="images/book_details_de.gif" width=521 useMap=#Map border=0> 
		<MAP name=Map>
		  <AREA shape=RECT coords=453,7,509,19 href="javascript:callServices()" >
		  <AREA shape=RECT coords=244,7,300,20 href="javascript:callGeneral()">
		</MAP>
     </DIV></TD>
          </TR>
        </TBODY>
      </TABLE></td>
  </tr>
</table>
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
<TABLE cellSpacing=0 cellPadding=0 width=780 bgColor=#cedee9 border=0>
  <TBODY> 
  <TR> 
    <TD width="73%" bgColor=#ffffff> 
      <div align="left"><B><font size="3" face="Times New Roman, Times, serif" color="#000000">Captura de Detalle</font></B></div>
    </TD>
  <TR> 
    <TD width="73%" bgColor=#ffffff height=5></TD>
  <TR> 
    <TD width="73%" bgcolor="#FFFFFF"> 
      <table width="99%" border="0" cellpadding="0" cellspacing="0" bgcolor="#cedee9">
        <tr> 
          <td width="7%"> 
            <div align="right">Cantidad</div>
          </td>
          <td width="0%">&nbsp;</td>
          <td width="15%">       
			<html:text styleClass="text" property="cantidad" onblur="isNumber(this)"/>
          </td>
          <td width="17%"> 
            <div align="right">Descripcion</div>
          </td>
          <td width="0%">&nbsp;</td>
          <td width="26%"> 
            <table width="100%" cellspacing="0" cellpadding="0">
              <tr> 
                <td width="44%"> 
                <html:text styleClass="textdescrip" property="descripcion" readonly="true" onclick="openLov('description')"/>
                
                </td>
                <td width="3%">&nbsp;</td>
                <td width="53%"> 
                  <html:button property="lovbutton1" value=". . ." styleClass="lov" onclick="openLov('description')"/>
                </td>
              </tr>
            </table>
          </td>
          <td width="9%"> 
            <div align="right">Contenido </div>
          </td>
          <td width="0%">&nbsp;</td>
          <td width="26%"> 
          <html:text styleClass="textcontenido" property="contenido" />
          </td>
        </tr>
        <tr> 
          <td width="7%"> 
            <div align="right">Tarifa</div>
          </td>
          <td width="0%">&nbsp;</td>
          <td width="15%"> 
            <table width="100%" cellspacing="0" cellpadding="0" id="req">
              <tr> 
                <td width="44%"> 
                <html:text styleClass="text" property="tarifa" readonly="true" onclick="openLov('tariff')"/>
                </td>
                <td width="3%">&nbsp;</td>
                <td width="53%"> 
                  <html:button property="lovbutton2" value=". . ." styleClass="lov" onclick="openLov('tariff')" />
                </td>
              </tr>
            </table>
          </td>
          <td width="17%"> 
            <div align="right">Peso</div>
          </td>
          <td width="0%">&nbsp;</td>
          <td width="26%"> 
          <html:text styleClass="text" property="peso" />
          </td>
          <td width="9%"> 
            <div align="right">Volumen </div>
          </td>
          <td width="0%">&nbsp;</td>
          <td width="26%">
          <html:text styleClass="text" property="volumen" />
          </td>
        </tr>
        <tr bgcolor="#FFFFFF"> 
          <td colspan="9" height="5"></td>
        </tr>
      </table>
    </TD>
  <TR> 
    <TD width="73%" bgColor=#ffffff> 
      <table width="100%" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="40%"> 
            <div align="right"> </div>
          </td>
          <td width="7%"> 
            <div align="center">
            <%
				if(action!=null && action.equals("edit")){
            %>
				<html:button property="editbutton" value="Change" styleClass="button1" onclick="submitForm(this)" />
			<%
				}else{
			%>
              <html:button property="addbutton" value="Agregar" styleClass="button1" onclick="submitForm(this)" />
             <%
				}
             %>
            </div>
          </td>
          <td width="46%">&nbsp; </td>
        </tr>
      </table>
    </TD>
  </TR>
  </TBODY> 
</TABLE>
<input type="hidden" name="deletehitcount">
<input type="hidden" name="actionfordup" >
<input type="hidden" name="editmode">
<html:hidden property="ss_srvc_id" />
<html:hidden property="ss_refr_srvc_id" />
<html:hidden property="descripcioncode" />
<html:hidden property="importe" />
<html:hidden property="specialTariff" />
<html:hidden property="pesoMax" /><!-- AAP01 -->
</html:form>

<%
	if(action!=null && action.equals("add") || actionfirst!=null && actionfirst.equals("add")){	
%>
<script language=javascript>
		document.forms[0].actionfordup.value='add';
</script>
<%
	}else{
%>
<script language=javascript>
		document.forms[0].actionfordup.value='edit';
</script>
<%
	}
%>

<script>
<%
	if(action!=null && action.equals("edit")){
%>

	document.forms[0].editmode.value='yes';
<%
	}else{
%>
	document.forms[0].editmode.value='';
<%
	}
%>
</script>
</body>
</html:html>