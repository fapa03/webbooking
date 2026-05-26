<%--
Authur		:	Ramachandran.V
Date		:	24-March-2003

FileName	:	JavWebBookingGeneral.jsp
FormBean	:	JavWebBookingGeneralForm.class
ActionBean	:	JavWebBookingGeneralAction.class
OtherClasses	:	none
CSS FileName	:	webbooking.css
--%>


<%@ page info="WebBooking - General" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html:html>

<head>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<title>Informacion General</title>
<script lanaguage="javascript">	
	
	function openLov(str){
		destinationcode = document.forms[0].destinationsitecode.value;
		destinationClientId = document.forms[0].destinationclave.value;
		
		if(str=='branch'){		
			window.open('lov.do?type='+str+'&selectedBranch='+destinationcode,'abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
		}
		
		if(str=='branchaddress'){	
		//alert(document.forms[0].destinationcode.value);
					window.open('lov.do?type='+str+'&sucursal='+ document.forms[0].destinationcode.value ,'abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
		}
		if(str=='client'){
			
			if(destinationcode==''){
				alert('SELECCIONE LA CIUDAD DESTINO');
				return;
			}else
				window.open('lov.do?type='+str+'&destinationsitecode='+destinationcode,'abc','width=400,height=200,top=185,left=200,screenY=185,screenX=200');
		}
		
		if(str=='destinationaddress'){
			
			destinationclave = document.forms[0].destinationclave.value;
			if(destinationcode==''){
				alert('SELECCIONE LA CIUDAD DESTINO');
				return;
			}else if(destinationClientId==''){
				alert('SELECCIONE EL NUMERO DE CLIENTE');
				return;
			}else{
				window.open('lov.do?type='+str+'&destClId='+destinationClientId+'&addresstype=DC&destinationclient='+destinationclave+'&destinationsitecode='+destinationcode,'abc','width=600,height=200,top=200,left=100,screenY=200,screenX=100');
			}
		}
		
		if(str=='fiscaladdress'){
			window.open('lov.do?type='+str,'abc','width=600,height=200,,top=200,left=100,screenY=200,screenX=100');
		}
	}
	
	function callForm(str){
		
		destinationcode = document.forms[0].destinationsitecode.value;
		destinationClientId = document.forms[0].destinationclave.value;
	<%
		String addressCode = (String)session.getAttribute("addresscode");
		String from=(String)request.getParameter("from");		
		if(addressCode!=null && addressCode.length()>0){
	%>		
		addressCode=<%= addressCode %>
	<%
		}else{
	%>
		addressCode = document.forms[0].destinationaddresscode.value;
	<%
		}
	%>
			if(destinationcode==''){
				alert('SELECCIONE LA CIUDAD DESTINO');
				return;
			}else if(destinationClientId==''){
				alert('SELECCIONE EL NUMERO DE CLIENTE');
				return;
			}else if(addressCode==''){
				alert('POR FAVOR SELECCIONE LA DIRECCIÓN DESTINO');
				return;
			}
			else{
				document.forms[0].action="webBookinggeneral.do?from="+str+"&addresscode="+addressCode;
				document.forms[0].submit();
			}
	}
	
	function initScript(){
		borderbranchcheck=document.forms[0].borderbranchcheck.value;
		shiperrmsg=document.forms[0].shiperrmsg.value;
		if(shiperrmsg!=''){
			alert(shiperrmsg);
			return;
		}
		//if(borderbranchcheck!='' && borderbranchcheck=='true'){			
			window.open("BorderBranch.jsp?from=<%= from %>","border","width=400,height=175,resizable=no,menubar=no,status=no,titlebar=no,top=230,left=200,screenY=230,screenX=200");
		//}
	}
	
	function goOut(page){
		if(confirm("DESEA PERDER LA INFORMACION?")){
			document.forms[0].action="webBookinggeneral.do?includeattribute=yes&page="+page;
			document.forms[0].submit();
		}else{
			return;
		}
	}
	
</script>
</head>

<body bgcolor="#FFFFFF" onload="initScript()" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" background="images/bg.gif" oncontextmenu="return false">


<html:form action="webBookinggeneral.do">
<jsp:include page="nocache.jsp" flush="false" />

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
<table width="68%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td>
	
    <!--  New Image  -->  

	<logic:present name="loginForm">
   <html:hidden  name="loginForm" property="userValidate" />

  <logic:equal name="loginForm" property="userValidate" value="validUser">

<img src="images/toplink.gif" width="771" height="22" usemap="#mainMap" border="0">
      <map name="mainMap"> 
<!--  <area shape="rect" coords="67,6,151,18" href="javascript:goOut('mainpage')">
        <area shape="rect" coords="165,6,275,17" href="javascript:goOut('thispage')">
		<area shape="rect" coords="288,6,390,17" href="javascript:goOut('guiacancel')">
		<area shape="rect" coords="404,6,506,17" href="javascript:goOut('shipmenthistory')">
		<area shape="rect" coords="520,6,644,17" href="javascript:goOut('clientreport')">
		<area shape="rect" coords="676,6,764,17" href="login.do?logout=yes" alt="Terminar Sesión" title="Terminar Sesión">
  guiacancel
 -->	
 <!-- added by bala -->
         <area shape="rect" coords="565,5,680,18" href="javascript:goOut('clientreport')">
       
		<area shape="rect" coords="460,5,555,17" href="javascript:goOut('shipmenthistory')">
        
		<area shape="rect" coords="1,4,135,18" href="javascript:goOut('cliententry')">
        
		<area shape="rect" coords="145,5,226,18" href="javascript:goOut('mainpage')">
        
		<area shape="rect" coords="236,5,342,18" href="javascript:goOut('thispage')">
        
		<area shape="rect" coords="352,5,450,18" href="javascript:goOut('guiacancel')">
       
	   <area shape="rect" coords="690,4,772,18" href="login.do?logout=yes" alt="Terminar Sesión" title="Terminar Sesión">
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
		<area shape="rect" coords="676,6,764,17" href="login.do?logout=yes" alt="Terminar Sesión" title="Terminar Sesión">
  
 	
	  
	  </map>
   </logic:notEqual>
</logic:present>
	
    
	</td>
  </tr>
  <tr>
    <td>
      <div align="right"><img src="images/book_generales.gif" width="521" height="25" usemap="#Map2" border="0"> 
        <map name="Map2"> 
          <area shape="rect" coords="450,7,516,18" href="javascript:callForm('bookingservices')">
          <area shape="rect" coords="314,7,441,21" href="javascript:callForm('bookingdetail')">
        </map>
      </div>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
<map name="peMap"> 
  <area shape="rect" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com">
</map>
<table border="0" align=center cellspacing="0" cellpadding="0" width="75%">
<logic:present name="shipmentdestination" scope="request">
	<tr>	
	<td align=center>
	<font style="font-size: 12pt; color: #ff0000; font-weight: bold">
		<bean:write name="shipmentdestination" />
	</font>
	</td>	
	</tr>
</logic:present>
</table>

<br>
<table width=780 border=0  cellspacing=1 cellpadding=0>
  <tr>
    <td>
      <div align="left" style="font-size: 10pt; color: #ff0000; font-weight: bold"> 
      	Estimado Cliente con la finalidad de agilizar el proceso de documentación, le informamos que ya podrá utilizar una nueva pantalla para la elaboración de guías, ubicada al final del menú principal. 
		Aprovechamos para informarle que a partir del 1º de Junio la opción que utiliza actualmente será deshabilitada quedando solo la nueva opción. 
		Para cualquier duda podrá comunicarse al 01 800 821 0208.
      </div>
    </td>
  </tr>
</table>  
<table width=780 border=0  cellspacing=1 cellpadding=0>
  <tr>
    <td>
      <div align="right"> </div>
    </td>
  </tr>
  <tr> 
    <td width="774"><strong>Documentacion</strong></td>
  </tr>
  <tr> 
    <td width="774"> <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CEDEE9">
        <tr> 
          <td width="436"> <table border="0" width="82%" bgcolor="#CEDEE9" cellspacing="0" cellpadding="0">
              <tr> 
                <td width="100%" colspan="2"> <b>Origen</b></td>
              </tr>
              <tr> 
                <td width="14%" align="right"> <font size="3"> 
                  Plaza:<html:text size="5" readonly="true" property="orgioncode" />
                  </font></td>
                <td width="86%"><font size="3"> 
					<html:text size="28" readonly="true" property="orginsite" />                  
                  </font></td>
              </tr>
              <tr> 
	                      <td width="14%"><font size="3"> 
	                        Sucursal:<html:text size="5" readonly="true" property="orginbranchcode" />
	                        </font></td>
	                      <td width="86%"><font size="3"> 
	      					<html:text size="28" readonly="true" property="orgionbranch" />                  
	                        </font></td>
              </tr>
            </table></td>
          <td width="475"> 
			<table border="0" width="98%" bgcolor="#CEDEE9" cellspacing="" cellpadding="0">
              <tr> 
                <td width="100%" colspan="2"><b>Destino</b></td>
              </tr>
              <tr> 
                <td width="14%" align="right"><font size="3">                 
                  Plaza:<html:text size="5" property="destinationsitecode" readonly="true"/>
                  </font></td>
                <td width="40%"><font size="3">
                  <html:text size="28" property="destinationsite" readonly="true" onclick="openLov('branch')"/>
                  </font></td>
                  <td width="45%">
                  <html:button property="destinationbranchbut" value="Plaza" styleClass="button1" onclick="openLov('branch')" />
                  </td>
              </tr>
			
			 <tr> 
                <td width="14%"><font size="3">                 
                  Sucursal:<html:text size="5" property="destinationcode" readonly="true"/>
                  </font></td>
                <td width="40%"><font size="3">
                  <html:text size="28" property="destinationbranch" readonly="true" />
                  </font></td>
                  <td width="45%">
                  <a href="#" onclick="openLov('branchaddress')"> BranchAddress</a>
                  </td>
              </tr></table>
           </td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td width="774"> <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CEDEE9">
        <tr> 
          <td width="438" valign="top"> <table border="0" cellpadding="0" cellspacing="0" width="82%">
              <tr> 
                <td align="right" colspan="3"> <p align="left"><b><u>Cliente Origen</u></b> 
                </td>
              </tr>
              <tr> 
                <td align="right" colspan="3"> <p align="left">&nbsp; </td>
              </tr>
              <tr> 
                <td width="30%" align="right">Clave</td>
                <td width="4%" align="right">&nbsp;</td>
                <td width="66%">
                <font size="3"> 
                  <html:text size="13" property="orgienclave" readonly="true"/>
                  </font></td>
              </tr>
              <tr> 
                <td width="30%" align="right">Nombre</td>
                <td width="4%" align="right">&nbsp;</td>
                <td width="66%"><font size="3"> 
                  <html:text size="40" property="orgiennombre" readonly="true"/>
                  </font></td>
              </tr>
              <tr> 
                <td width="30%" align="right">Origen</td>
                <td width="4%" align="right">&nbsp;</td>
                <td width="66%"> <table border="0" cellpadding="0" cellspacing="0" width="70%">
                    <tr> 
                      <td width="62%"><font size="3"> 
                        <html:text size="25" property="orgien1" readonly="true"/>
                        </font></td>
                      <td width="36%"><font size="3"> 
                        <html:text size="11" property="orgien2" readonly="true"/>
                        </font></td>
                      <td width="2%"></td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="30%" align="right">Colonia</td>
                <td width="4%" align="right">&nbsp;</td>
                <td width="66%"> <table border="0" cellpadding="0" cellspacing="0" width="70%">
                    <tr> 
                      <td width="46%"><font size="3"> 
                        <html:text size="18" property="orgiencolonia1" readonly="true"/>
                        </font></td>
                      <td width="54%"><font size="3"> 
                        <html:text size="18" property="orgiencolonia2" readonly="true"/>
                        </font></td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="30%" align="right">R.F.C</td>
                <td width="4%" align="right">&nbsp;</td>
                <td width="66%"> <table border="0" cellpadding="0" cellspacing="0" width="70%">
                    <tr> 
                      <td width="22%"><font size="3"> 
                        <html:text size="15" property="orgienrfc" readonly="true"/>
                        </font></td>
                      <td width="41%">Telefono</td>
                      <td width="37%"><font size="3"> 
                        <html:text size="12" property="orgientelefono" readonly="true"/>
                        </font></td>
                    </tr>
                  </table></td>
              </tr>
            </table></td>
          <td width="455" valign="top"> <table border="0" cellpadding="0" cellspacing="0" width="82%">
              <tr> 
                <td align="right" colspan="3"> <p align="left"><b><u>Cliente Destino</u></b> 
                </td>
              </tr>
              <tr> 
                <td align="right" colspan="3"> <p align="left">&nbsp; </td>
              </tr>
              <tr> 
                <td width="30%" align="right">Clave</td>
                <td width="4%" align="right">&nbsp;</td>
                <td width="66%">
                <table width=50% border=0>
						<tr>
							<td width=10%>
								<font size="3">                 
									<html:text size="13" property="destinationclave" readonly="true" onclick="openLov('client')"/>
								</font>
							</td>
							<td width="20%" align=left>
								<html:button property="client" styleClass="button1" value="Cliente" onclick="openLov('client')" />
							</td>
						</tr>
					</table>
                <font size="3"> 
                  
                  </font></td>
              </tr>
              <tr> 
                <td width="30%" align="right">Nombre</td>
                <td width="4%" align="right">&nbsp;</td>
                <td width="66%"><font size="3"> 
                  <html:text size="40" property="destinationnombre" readonly="true"/>
                  </font></td>
              </tr>
              <tr> 
                <td width="30%" align="right"> 
					<html:button value="Destino" property="destinobutton" styleClass="button1" onclick="openLov('destinationaddress')" />
                </td>
                <td width="4%" align="right">&nbsp;</td>
                <td width="66%"> <table border="0" cellpadding="0" cellspacing="0" width="70%">
                    <tr> 
                      <td width="62%"><font size="3"> 
                        <html:text size="25" property="destino1" readonly="true" onclick="openLov('destinationaddress')"/>
                        </font></td>
                      <td width="36%"><font size="3"> 
                        <html:text size="11" property="destino2" readonly="true" />
                        </font></td>
                      <td width="2%"></td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="30%" align="right">Colonia</td>
                <td width="4%" align="right">&nbsp;</td>
                <td width="66%"> <table border="0" cellpadding="0" cellspacing="0" width="70%">
                    <tr> 
                      <td width="46%"><font size="3"> 
                        <html:text size="18" property="destinationcolonia1" readonly="true" />
                        </font></td>
                      <td width="54%"><font size="3"> 
                        <html:text size="18" property="destinationcolonia2" readonly="true" />
                        </font></td>
                    </tr>
                  </table></td>
              </tr>
              <tr> 
                <td width="30%" align="right">R.F.C</td>
                <td width="4%" align="right">&nbsp;</td>
                <td width="66%"> <table border="0" cellpadding="0" cellspacing="0" width="70%">
                    <tr> 
                      <td width="22%"><font size="3"> 
                        <html:text size="15" property="destinationrfc" readonly="true" />
                        </font></td>
                      <td width="41%">Telefono</td>
                      <td width="37%"><font size="3"> 
                        <html:text size="12" property="destinationtelefono" readonly="true" />
                        </font></td>
                    </tr>
                  </table></td>
              </tr>
            </table>
            <p>&nbsp;</p></td>
        </tr>
      </table></td>
  </tr>
  
  
  
  
  <tr> 
    <td width="774" valign="top"> 
      <table border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="#CEDEE9">
        <tr> 
          <td width="43%" align="right" colspan="2"> 
            <p align="left"><b><u>Direccion Fiscal</u></b> 
          </td>
          <td width="22%">&nbsp;</td>
          <td width="35%">&nbsp;</td>
        </tr>
        <tr> 
          <td width="100%" align="right" colspan="4"> 
            <p align="left">&nbsp; 
          </td>
        </tr>
        <tr align="left"> 
          <td colspan="4"> 
            <table border="0" cellpadding="0" cellspacing="0" width="82%">
              <tr> 
                <td width="30%" align="right">Calle</td>
                <td width="1%" align="right">&nbsp;</td>
                <td width="66%"> 
                  <table border="0" cellpadding="0" cellspacing="0" width="70%">
                    <tr> 
                      <td width="48%"><font size="3"> 
                        <html:text size="25" property="fiscal1" readonly="true"/>
                        </font></td>
                      <td width="17%"><font size="3"> 
                        <html:text size="7" property="fiscal2" readonly="true" onclick="openLov('fiscaladdress')"/>
                        </font></td>
                      <td width="35%"> 
                        <html:button value="Fiscal" property="ficalbutton" styleClass="button1" onclick="openLov('fiscaladdress')"/>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr> 
                <td width="30%" align="right">Colonia</td>
                <td width="1%" align="right">&nbsp;</td>
                <td width="66%"> 
                  <table border="0" cellpadding="0" cellspacing="0" width="70%">
                    <tr> 
                      <td width="46%"><font size="3"> 
                        <html:text size="18" property="fiscalcolonia1" readonly="true" />
                        </font></td>
                      <td width="54%"><font size="3"> 
                        <html:text size="18" property="fiscalcolonia2" readonly="true" />
                        </font></td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr> 
                <td width="30%" align="right">Telefono</td>
                <td width="1%" align="right">&nbsp;</td>
                <td width="66%"> 
                  <table border="0" cellpadding="0" cellspacing="0" width="70%">
                    <tr> 
                      <td width="22%"><font size="3"> 
                      <html:text size="11" property="fiscaltelefono" readonly="true" />                      
                        </font></td>
                      <td width="41%">&nbsp;</td>
                      <td width="37%"><font size="3"> </font></td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
            <font size="3"> </font> </td>
        </tr>
        <tr> 
          <td width="5%" align="right">&nbsp;</td>
          <td width="89%" colspan="3">&nbsp;</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr> 
    <td height="5" width="774"></td>
  </tr>
</table>
<html:hidden property="destinationaddresscode" />
<html:hidden property="destinationcoloniacode" />
<html:hidden property="citycode" />
<html:hidden property="fiscaladdresscode" />
<html:hidden property="orgionaddresscode" />
<html:hidden property="borderbranchcheck" />
<html:hidden property="pedinumber" />
<html:hidden property="custagent" />
<html:hidden property="ship_seqn" />
<html:hidden property="no_ship" />
<html:hidden property="cur_loc" />
<html:hidden property="cur_dest" />
<html:hidden property="lc_rout" />
<html:hidden property="shiperrmsg" />
<html:hidden property="getycode" />
<html:hidden property="getylevl" />
<html:hidden property="getytype" />
<html:hidden property="destinationzipcode" /><!-- AAP01 -->
</html:form>
</body>
</html:html>