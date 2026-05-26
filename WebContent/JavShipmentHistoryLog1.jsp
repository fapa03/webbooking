<%@ page contentType="text/html" import="java.util.*,bean.*"%>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>


<html:html>
<head>
<script language="javascript">
	// maintain the checked manifest
	<%
		String getCheckedValue = request.getParameter("checkit");
		if(getCheckedValue == null){
			getCheckedValue = "dum";
		}		
	%>
	
	<%
		String returnVal = request.getParameter("returnval");		
		if(returnVal!=null && returnVal.equalsIgnoreCase("true")){
	%>

			var temp =true;
	<%
		}else{
	%>
			var temp =false;
	<%
		}
	%>
	
	
	function printDetails(){
		document.forms[0].operation.value="Print";
		document.forms[0].submit();
	}
	function doCheck(){
		document.forms[0].checkit.value = '<%=getCheckedValue %>';
		var keepChecked = '<%=getCheckedValue %>'
		for(i=0; i< document.forms[0].chooseGuia.length;i++){
			if(document.forms[0].chooseGuia[i].value == keepChecked){
				document.forms[0].chooseGuia[i].checked = true;
			}
		}
		document.forms[0].manifestDate.focus();
	}
	
	// Move to the next page of the Manifest Sent Records.	
	function doSubmitNext(){
		var startIndex = document.forms[0].startIndex.value; 
		var endIndex = document.forms[0].endIndex.value;
		var pageIndex = document.forms[0].pageIndex.value;
		var maxPage = document.forms[0].maxPageIndex.value;
		var curPage = document.forms[0].currentPage.value;		
		
		if(document.forms[0].emptyrecs.value == "empty"){
			alert("USTED ESTA VIENDO LA ULTIMA PAGINA");			
		}
		else if(curPage == maxPage){
			alert("USTED ESTA VIENDO LA ULTIMA PAGINA");
		}
		else{
			document.forms[0].startIndex.value = parseInt(startIndex) + 6;
			document.forms[0].endIndex.value = parseInt(endIndex) + 6;
			document.forms[0].pageIndex.vchooalue = parseInt(pageIndex) + 1;
			document.forms[0].currentPage.value = parseInt(curPage) + 1;
			document.shipmentform.submit();					
		}	
	}
	//maintain the check status
	function checkIt(manifestNumber){
		document.forms[0].checkit.value = manifestNumber;
	}
	
	// Move to the previous page for Manifest Sent Records
	function doSubmitPrevious(){
		var startIndex = document.forms[0].startIndex.value; 
		var endIndex = document.forms[0].endIndex.value;
		var pageIndex = document.forms[0].pageIndex.value;
		var maxPage = document.forms[0].maxPageIndex.value;
		var curPage = document.forms[0].currentPage.value;		
		if(document.forms[0].emptyrecs.value == "empty"){
			alert("USTED ESTA VIENDO LA PRIMER PAGINA");
		}
		else if(curPage == 1){
			alert("USTED ESTA VIENDO LA PRIMER PAGINA");
		}
		else{
			document.forms[0].startIndex.value = parseInt(startIndex) - 6;
			document.forms[0].endIndex.value = parseInt(endIndex) - 6;
			document.forms[0].pageIndex.value = parseInt(pageIndex) - 1;
			document.forms[0].currentPage.value = parseInt(curPage) - 1;
			document.shipmentform.submit();					
		}	
	}	
	// Move to the next page of the Manifest Not sent Records
	function doSubmitNotNext(){
		var startIndex = document.forms[0].startNSIndex.value; 
		var endIndex = document.forms[0].endNSIndex.value;
		var pageIndex = document.forms[0].pageNSIndex.value;
		var maxPage = document.forms[0].maxPageNSIndex.value;
		var curPage = document.forms[0].currentNSPage.value;				
		if(document.forms[0].notSentEmpty.value == "empty"){
			alert("USTED ESTA VIENDO LA ULTIMA PAGINA");			
		}
		else if(curPage == maxPage){
			alert("USTED ESTA VIENDO LA ULTIMA PAGINA");			
		}
		else{
			document.forms[0].startNSIndex.value = parseInt(startIndex) + 6;
			document.forms[0].endNSIndex.value = parseInt(endIndex) + 6;
			document.forms[0].pageNSIndex.value = parseInt(pageIndex) + 1;
			document.forms[0].currentNSPage.value = parseInt(curPage) + 1;
			document.shipmentform.submit();					
		}
	}
	// Move to the previous page of the Manifest not sent records
	function doSubmitNotPrevious(){
		var startIndex = document.forms[0].startNSIndex.value; 
		var endIndex = document.forms[0].endNSIndex.value;
		var pageIndex = document.forms[0].pageNSIndex.value;
		var maxPage = document.forms[0].maxPageNSIndex.value;
		var curPage = document.forms[0].currentNSPage.value;
		if(document.forms[0].notSentEmpty.value == "empty"){
			alert("USTED ESTA VIENDO LA PRIMER PAGINA");
		}else if(curPage == 1){
			alert("USTED ESTA VIENDO LA PRIMER PAGINA");
		}else{
			document.forms[0].startNSIndex.value = parseInt(startIndex) - 6;
			document.forms[0].endNSIndex.value = parseInt(endIndex) - 6;
			document.forms[0].pageNSIndex.value = parseInt(pageIndex) - 1;
			document.forms[0].currentNSPage.value = parseInt(curPage) - 1;
			document.shipmentform.submit();					
		}			
	}
	// Deleting a manifest number
	function doDelete(manifestNumber){
		var del = confirm("REALMENTE DESEA BORRAR EL MANIFIESTO"+manifestNumber+"?");
		if(del){
			document.forms[0].deleteGuiaNumber.value=manifestNumber;
			document.forms[0].submit();
		}
	}
	// Printing function
	function printDetail(){
		//window.print();
	}
	
	// viewing Manifest details
	function saveGuiaNumber(){	
		document.forms[0].operation.value="";
		for(var i=0;i < document.forms[0].chooseGuia.length;i++){			
			if(document.forms[0].chooseGuia[i].checked){
				document.forms[0].genManifestNumber.value= document.forms[0].chooseGuia[i].value;
			}
		}
		if(document.forms[0].genManifestNumber.value == ''){
			alert("FAVOR DE HACER ‘CLICK’ EN EL NUMERO DEL MANIFIESTO QUE QUIERE VER");
		}else{
			var manfiestNo = document.forms[0].checkit.value;
			var confirmDetail = confirm("ESTÁ UD. SEGURO(A) QUE QUIERE VISUALIZAR LOS DETALLES PARA EL MANIFIESTO NO.:"+manfiestNo);
			if(confirmDetail){
				document.shipmentform.submit();
			}else{
				return;
			}
		}
	}
	
	// date validation
	var dtCh = "/";
	var currentYear = new Date;
	var minYear = 1990;	
	var maxYear = currentYear.getFullYear();
	
	function isInteger(s){
		var i ;
		for(i =0; i < s.length;i++){
			var c = s.charAt(i);
			if(((c < "0") || (c > "9"))) return false;
		}
		return true;
	}
	
	function checkCharacters(s, check){
		var i;
		var returnString  = "";
		for(i = 0;i < s.length;i++){
			var c = s.charAt(i);
			if(check.indexOf(c) == -1) 
				returnString += c;
		}
		return returnString;
	}
	function daysInFebruary(year){
		return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
	}
	
	function DaysArray(n){
		for(var i=1;i <=n;i++){
			this[i] = 31;
			if(i == 4 || i == 6 || i == 9 ||i == 11){
				this[i] = 30;
			}
			if(i == 2){
				this[i] = 29;
			}
		}
		return this;
	}
	
	function isDate(dtStr){
		var inMonth = DaysArray(12);
		var pos1 = dtStr.indexOf(dtCh);
		var pos2 = dtStr.indexOf(dtCh,pos1+1);
		var strMonth = dtStr.substring(pos1+1,pos2);
		var strDay = dtStr.substring(0,pos1);
		var strYear = dtStr.substring(pos2+1);
		var strYr = strYear;
		if(strDay.charAt(0) == "0" && strDay.length> 1)
			strDay = strDay.substring(1);
		if(strMonth.charAt(0) == "0" && strMonth.length > 1)
			strMonth = strMonth.substring(1);
		for (var i = 1; i <= 3; i++) {
			if (strYr.charAt(0)=="0" && strYr.length>1) 
				strYr=strYr.substring(1)
		}
		month = parseInt(strMonth);
		day = parseInt(strDay);
		year = parseInt(strYr);
		
		var now = new Date();
		
		var currentDay = now.getDate();
		var currentMonth = now.getMonth();
		var currentYear = now.getFullYear();
		currentMonth = currentMonth+1;
		
		if((day > currentDay) && (month >= currentMonth) && (year == currentYear)){
			alert('NO SE PUEDE MOSTRAR EL HISTORICO DE ENVIOS PARA FUTURAS FECHAS');
			//document.forms[0].manifestDate.select();
			return false;
		}
		
		if(dtStr.length <10){
		  alert ("DATE SHOULD BE IN DD/MM/YYYY  FORMAT");
		  //document.forms[0].manifestDate.select();
		  return false;
		}
		if (pos1==-1 || pos2==-1){
			alert("EL CAMPO DE FECHA ES OBLIGATORIO Y NO PUEDE QUEDAR VACIO")
			//document.forms[0].manifestDate.select();
			return false;
		}		
		
		if (strMonth.length<1 || month<1 || month>12){
			alert("POR FAVOR, CAPTURE VALOR CORRECTO PARA EL MES");
			//document.forms[0].manifestDate.select();
			return false;
		}
		
		if (strDay.length<1 || day<1 || day>31 || (month==2 && 	day>daysInFebruary(year)) || day > inMonth[month]){
			alert("POR FAVOR, CAPTURE UN VALOR CORRECTO PARA EL DIA");
			//document.forms[0].manifestDate.select();
			return false;
		}	
		
		if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
			alert("POR FAVOR DIGITE UN NÚMERO DE 4 DÍGITOS ENTRE "+minYear+" Y "+maxYear+"PARA");
			//document.forms[0].manifestDate.select();
			return false;
		}
		
		if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(checkCharacters(dtStr, dtCh))==false){
			alert("POR FAVOR, CAPTURE VALOR CORRECTO PARA EL MES");
			//document.forms[0].manifestDate.select();
			return false
		}
		return true;
	}
	function validateDate(){
		var dt=document.forms[0].manifestDate;
		//Added by rama
		if(trim(dt.value)==""){
			alert("PLEASE ENTER DATE IN DD/MM/YYYY FORMAT");
			dt.value="";
			dt.focus();
			return false;
		}
		//End		
		if (isDate(dt.value)==false){
			dt.select();//Changed by rama
			return false;
		}
		return true;
	}
	function trim(val){
		len = 0;
	
		try
		{ // check the val is valid string 
			len = val.length;
		}
		catch(exp)
		{
			return "";
		}
		for(i=0; i<val.length && val.substr(i,1)==' '; i++);
		for(j=val.length-1; j>-1 && val.substr(j,1)==' '; j--);
		j++;
		if (i<j) val=val.substring(i, j);
			else val="";

		return val;
	}
	function genManifestDetails(){
		document.forms[0].operation.value="";
		if(validateDate()){
			document.shipmentform.submit();
		}
	}
	function generateManifest(){
		document.forms[0].action="manifestgeneration.do?show=true";
		document.forms[0].submit();
	}
</script>
<link rel="stylesheet" href="webbooking.css">
<title>Shipment History Log</title>
</head>
<body topmargin="0" leftmargin="0" marginwidth=0 marginheight=0 onLoad='doCheck()'>
<html:form action="/shipmentHistory" onsubmit="return validateDate()">
<jsp:include page="include.jsp" flush="false" />
<!-- added by Bala -->

  <!--  New Image  -->  

	<logic:present name="loginForm">
   <html:hidden  name="loginForm" property="userValidate" />

  <logic:equal name="loginForm" property="userValidate" value="validUser">


<table width="68%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="images/toplink.gif" width="771" height="22" usemap="#mainMap" border="0"></td>
  </tr>
</table>
<map name="peMap"> 
  <area shape="rect" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com">
</map>
<map name="mainMap"> 



  <area shape="rect" coords="565,5,680,18" href="JavReportMain.jsp">
       
		<area shape="rect" coords="460,5,555,17" href="shipmentHistory.do"><!-- JavShipmentHistoryLog.jsp -->
        
		<area shape="rect" coords="1,4,135,18" href="clientDestinationEntry.do">

		<area shape="rect" coords="145,5,226,18" href="JavWebBookingMain.jsp">
        
		<area shape="rect" coords="236,5,342,18" href="webBookinggeneral.do?includeattribute=yes">
        
		<area shape="rect" coords="352,5,450,18" href="JavGuiaCancellation.jsp">
       
	   <area shape="rect" coords="690,4,772,18" href="login.do?logout=yes" alt="Terminar Sesión" title="Terminar Sesión">
<!-- Added by Bala -->
</map>
  </logic:equal>
</logic:present>


 <!--  Old  Image  -->  

	<logic:present name="loginForm">
   <html:hidden  name="loginForm" property="userValidate" />
  <logic:notEqual name="loginForm" property="userValidate" value="validUser">
<table width="68%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="images/toplink1.gif" width="771" height="22" usemap="#mainMap" border="0"></td>
  </tr>
</table>
<map name="peMap"> 
  <area shape="rect" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com">
</map>
<map name="mainMap"> 
	<area shape="rect" coords="67,6,151,18" href="JavWebBookingMain.jsp">
	<area shape="rect" coords="165,6,275,17" href="webBookinggeneral.do?includeattribute=yes">
	<area shape="rect" coords="288,6,390,17" href="JavGuiaCancellation.jsp">
	<area shape="rect" coords="404,6,506,17" href="shipmentHistory.do"><!-- JavShipmentHistoryLog.jsp -->
	<area shape="rect" coords="520,6,644,17" href="JavReportMain.jsp">
	<area shape="rect" coords="676,6,764,17" href="login.do?logout=yes" alt="Terminar Sesión" title="Terminar Sesión">
</map>
  </logic:notEqual>
</logic:present>

<!-- Ended by bala -->
<jsp:include page="nocache.jsp" flush="false" />
<table border="0" width="80%" cellspacing="0" cellpadding="0" bgcolor="#CEDEE9">
  <tr>
    <td width="100%">
      <table border="0" width="80%" cellspacing="0" cellpadding="0" height="476">
        <tr>
          <td width="1%" height="21"></td>
          <td width="83%" height="21"><font face="Arial" size="2"><b>Consulta de Envíos</b></font></td>
          <td width="100%" height="21"> <font size="2" face="Arial">&nbsp; </font></td>
        </tr>
        <tr>
          <td width="1%" height="25"></td>
          <td width="72%" height="25">
            <p align="right"><font size="2" face="Arial">*Fecha&nbsp;</font> </td>
          <td width="85%" height="25"><html:text styleClass="text" property="manifestDate" maxlength='10'  style="width: 100; height: 18"/>
          <input type='button' value='Aceptar' style='width:55;height:20;font-size:11' class='button' onclick='genManifestDetails()'>
            <font size="2" face="Arial">DD/MM/YYYY</font> </td>
        </tr>
        <tr>
          <td width="1%" height="21"></td>
          <td width="56%" height="21"><font size="2" face="Arial"><b>Manifiestos Recolectados</b></font></td>
          <td width="70%" height="21"></td>
        </tr>
        <tr>
          <td width="100%" colspan="3" height="186">
            <table border="0" width="776" cellpadding="0" cellspacing="1">
<tr>
                <td width="28"></td>
                <td width="88" align="center" valign="bottom"><font face="Arial" size="2">Manifiesto
                 </font></td>
                <td width="68" align="center" valign="bottom"> 
                  <p align="center"><font face="Arial" size="2">No.de<br>
                  Guías</font></td>
                <td width="50" align="center" valign="bottom"> 
                  <p align="center"><font face="Arial" size="2">No.de<br>
                  Paquetes</font></td>
                <td width="80" align="center" valign="bottom"> 
                  <p align="center"><font face="Arial" size="2">Importe <br>
                  </font></td>
                <td width="235" align="center" valign="bottom"> 
                  <p align="center"><font face="Arial" size="2">Estado del Manifiesto</font></p>
                </td>
                <td width="239" colspan="3" align="center" valign="bottom"> 
                  <p align="left"><font face="Arial" size="2">Hora para Recolección
                  </font></p>
                </td>
              </tr>              
              <input type="hidden" name="emptyrecs">
              <!--------------Start of Multi Record Block ---------------->
              <%
              	Object manifestObject = request.getAttribute("sentValues");
              	if(manifestObject != null){
              		ArrayList manifestSent =(ArrayList) manifestObject;
              		int manifestSentSize = manifestSent.size();              	
              		if(manifestSentSize == 0){%>
              		<script language="javascript">
              			if(!temp)
              				alert('NO HAY REGISTROS PARA NO ENVIAR EL MANIFIESTO');
              			document.forms[0].manifestDate.select();
              			document.forms[0].emptyrecs.value = "empty";
              		</script>              		
              <%		}             
              		for(int i=0;i < manifestSentSize; i++){
							JavManifestSent manifestSentRecs = (JavManifestSent) manifestSent.get(i);     	
              %>
              <tr>
                <td width="28"></td>
                <td width="74"  class="td" align="right" height="18" nowrap>
                  <p align="right"><%=manifestSentRecs.getManifestNumber().trim()%></td>
                <td width="80"  class="td" align="right" nowrap height="18">
                  <p align="right"><%=manifestSentRecs.getGuiaNumber()%></td>
                <td width="50"  class="td" align="right" nowrap height="18"><%=manifestSentRecs.getNoOfPackages()%></td>
                <td width="80"  class="td" align="right" nowrap height="18"><%=manifestSentRecs.getManifestAmount()%></td>
                <td width="248"  class="td" nowrap height="18">&nbsp;<%=(manifestSentRecs.getManifestStatus()==null?"":manifestSentRecs.getManifestStatus())%></td>
                <td width="137"  class="td" nowrap height="18" colspan="2"><%=(manifestSentRecs.getPreferredCollectionTime()==null?"":manifestSentRecs.getPreferredCollectionTime())%></td>
                <td width="49"><input type="hidden" name="emp" value="full">
					<input type="radio" value='<%=manifestSentRecs.getManifestNumber()%>' onClick="checkIt('<%=manifestSentRecs.getManifestNumber()%>')" name="chooseGuia"></td>
              </tr>
              <%
              		}              	
              	int emptyLoop = 6;
              	while(manifestSentSize < emptyLoop){
              %>	
              	 	<tr>
                			<td width="28">&nbsp;</td>
                			<td width="74"  class="td" align="right" height="18" nowrap>
                  			<p align="right">&nbsp;</td>
                			<td width="80"  class="td" align="right" nowrap height="18">
                  			<p align="right">&nbsp;<input type="hidden" name="emp" value="full">
</td>
                			<td width="50"  class="td" align="right" nowrap height="18">&nbsp;</td>
                			<td width="80"  class="td" align="right" nowrap height="18">&nbsp;</td>
                			<td width="248"  class="td" nowrap height="18">&nbsp;</td>
                			<td width="137"  class="td" nowrap height="18" colspan="2">&nbsp;</td>
                			<td width="49"><input type="radio" value='nan' disabled name="R1"></td>
              		</tr>
              <%		
              		 manifestSentSize++;
              	  }
              	}
              	else{
              		int looper = 0;
              		String alertUser = request.getParameter("alerter");
              		if(alertUser != null){%>
              			<script language="javascript">
              				alert("LA CONSULTA NO OBTUVO REGISTROS");
              			</script>
              		<%}
              		while(looper < 6){%>
              		<tr>
                			<td width="28">&nbsp;</td>
                			<td width="74"  class="td" align="right" height="18" nowrap>
                  			<p align="right">&nbsp;
                  			<input type="hidden" name="emp" value="empty">
                  			</td>
                			<td width="80"  class="td" align="right" nowrap height="18">
                  			<p align="right">&nbsp;<input type="hidden" name="alerter" value="alert"> </td>
                			<td width="50"  class="td" align="right" nowrap height="18">&nbsp;</td>
                			<td width="80"  class="td" align="right" nowrap height="18">&nbsp;</td>
                			<td width="248"  class="td" nowrap height="18">&nbsp;</td>
                			<td width="137"  class="td" nowrap height="18" colspan="2">&nbsp;</td>
                			<td width="49"><input type="radio" value='nan' disabled  name="R1"></td>
              		</tr>              		              			
              <%
              	looper++;
		          		} 
              	}
              	
              %>             
              <html:hidden property="startIndex"/>
              <html:hidden property="endIndex"/>
              <html:hidden property="pageIndex"/>
              <html:hidden property="maxPageIndex"/>
              <html:hidden property="currentPage"/>              
              <html:hidden property="genManifestNumber"/>
           
              <!--------------End of Multi Record Block ------------------>
              <tr>
                <td width="28"></td>
                <td width="74"></td>
                <td width="68"></td>
                <td width="50"></td>
                <td width="80"></td>
                <td width="287" align="right">
                </td>
                <td width="60" align="right">
                  <p align="right">
                  
                  <html:button property="previous" value="Anterior" onclick="doSubmitPrevious()" styleClass="button"  style="width: 69; height: 22"/></p>
                </td>
                <td width="43"><html:button property="next" value="Siguiente" styleClass="button" style="width:69; height: 22" onclick="doSubmitNext()"/>
					<script language="javascript">
						if(document.forms[0].emp[0].value=="empty"){
							document.forms[0].previous.disabled = true;
							document.forms[0].next.disabled = true;
						}
						else if(document.forms[0].emptyrecs.value == "empty"){
							document.forms[0].previous.disabled = true;
							document.forms[0].next.disabled = true;
						}

					</script>                
                </td>
                <td width="49"></td>
              </tr>
            </table>             
          </td>
        </tr>
        <tr>
          <td width="4%" height="21"></td>
          <td width="47%" height="21"><font size="2" face="Arial"><b>Manifiestos pendientes de Recolectar</b></font></td>
          <td width="49%" height="21"></td>
        </tr>
        <tr>
          <td width="100%" colspan="3" height="152">
            <table border="0" width="774" cellspacing="1" cellpadding="0" height="90">
<tr>
                <td width="24" height="57"></td>
                <td width="85" height="57" align="center" valign="bottom"><font face="Arial" size="2">Manifiesto
                  </font></td>
                <td width="68" height="57" align="center" valign="bottom"> 
                  <p align="center"><font face="Arial" size="2">No.de<br>
                 Guías</font></td>
                <td width="50" height="57" align="center" valign="bottom"> 
                  <p align="center"><font face="Arial" size="2">No. de<br>
                  Paquetes</font></td>
                <td width="80" height="57" align="center" valign="bottom"> <p align="center"><font face="Arial" size="2">Importe 
                    </font></td>
                <td width="235" height="57" align="center" valign="bottom"> 
                  <p align="center"><font face="Arial" size="2">Estado del Manifiesto</font></p>
                </td>
                <td width="235" height="57" colspan="3" align="center" valign="bottom"> 
                  <p align="left"><font face="Arial" size="2">Hora para Recolección
                  </font></p>
                </td>
              </tr>
              <input type="hidden" name="notSentEmpty">
              <!---------------------- start of Second multirecord block --------------->
              <%
              	Object notSentManifestObject = request.getAttribute("notSentValues");
              	if(notSentManifestObject != null){
              		ArrayList manifestNotSent = (ArrayList) notSentManifestObject;
						int manifestNotSentSize = manifestNotSent.size();
						if(manifestNotSentSize == 0){%>
							<script language='javascript'>
								if(!temp)
									alert("NO HAY REGISTROS PARA ENVIAR EL MANIFIESTO");
								document.forms[0].manifestDate.select();
								document.forms[0].notSentEmpty.value = 'empty';
							</script>
						<%}
						for(int i=0;i < manifestNotSentSize;i++){
							JavManifestNotSent manifestNotSentRecs = (JavManifestNotSent)manifestNotSent.get(i);
              %>
							<tr>
							  <td width="24" height="19"><input type="hidden" name="emp1" value="full"></td>
							  <td width="71"  class="td" align="right" nowrap height="19">
							   <%= manifestNotSentRecs.getManifestNotSentNumber().trim() %></td>
							  <td width="80"  class="td" align="right" nowrap height="19">
							    <p align="right"><%= manifestNotSentRecs.getGuiaNotSentNumber() %>&nbsp;</td>
							  <td width="50"  class="td" align="right" nowrap height="19"><%= manifestNotSentRecs.getNoOfNotSentPackages()%>&nbsp;</td>
							  <td width="80"  class="td" align="right" nowrap height="19"><%= manifestNotSentRecs.getManifestNotSentAmount()%>&nbsp;</td>
							  <td width="260"  class="td" nowrap height="19"><%= (manifestNotSentRecs.getManifestNotSentStatus()==null?"":manifestNotSentRecs.getManifestNotSentStatus())%>&nbsp;</td>
							  <td width="146"  class="td" nowrap height="19"><%= (manifestNotSentRecs.getPreferredCollectionNotSentTime()==null?"":manifestNotSentRecs.getPreferredCollectionNotSentTime()) %>&nbsp;</td>
							  <td width="22" height="19"><input type="radio" value='<%=manifestNotSentRecs.getManifestNotSentNumber()%>' onClick="checkIt('<%=manifestNotSentRecs.getManifestNotSentNumber()%>')" name="chooseGuia"></td>
							  <td width="17" height="19"><a href="javascript:doDelete('<%=manifestNotSentRecs.getManifestNotSentNumber()%>')" ><img src='images/delete.jpg' width="17" border="0" height="17"></a></td>
							</tr>
              <%
						}
              			int showEmptyRecs = 6;
              			while(manifestNotSentSize < showEmptyRecs){
              %>
                <tr>
                <td width="24" height="20"></td>
                <td width="71"  class="td" align="right" nowrap height="20">&nbsp;   
                     
                  </td>
                <td width="80"  class="td" align="right" nowrap height="20">
                  <p align="right">&nbsp;</td>
                <td width="50"  class="td" align="right" nowrap height="20"><input type="hidden" name="emp1" value="full">&nbsp;</td>
                <td width="80"  class="td" align="right" nowrap height="20">&nbsp;&nbsp;</td>
                <td width="260"  class="td" nowrap height="20">&nbsp;</td>
                <td width="146"  class="td" nowrap height="20">&nbsp;</td>
                <td width="22" height="20"><input type="radio" value='nan' disabled  name="chooseGuia"></td>
                <td width="17" height="20"><img src='images/delete.jpg' border="0"></td>
              </tr>              
				<%
							manifestNotSentSize ++;
						}
					}
					else{
						int looper = 0;
						while(looper < 6){
					%>
						<tr>
						  <td width="24" height="20"></td>
						  <td width="71"  class="td" align="right" nowrap height="20">   
						   <input type="hidden" name="emp1" value="empty"> &nbsp;&nbsp; </td>
						  <td width="80"  class="td" align="right" nowrap height="20">
						    <p align="right">&nbsp;&nbsp;</td>
						  <td width="50"  class="td" align="right" nowrap height="20">&nbsp;&nbsp;</td>
						  <td width="80"  class="td" align="right" nowrap height="20">&nbsp;&nbsp;</td>
						  <td width="260"  class="td" nowrap height="20">&nbsp;&nbsp;</td>
						  <td width="146"  class="td" nowrap height="20">&nbsp;</td>
						  <td width="22" height="20"><input type="radio" value='nan' disabled name="chooseGuia"></td>
						  <td width="17" height="20"><img src='images/delete.jpg' border="0"></td>
						</tr>              						
				<%	
							looper++;
						}
					}
				%>
              <!------------------- End of second multi record block ------------------>
              <html:hidden property="startNSIndex"/>
              <html:hidden property="endNSIndex"/>
              <html:hidden property="pageNSIndex"/>
              <html:hidden property="maxPageNSIndex"/>
              <html:hidden property="currentNSPage"/>              
              <html:hidden property="deleteGuiaNumber"/>
            </table>
          </td>
        </tr>
        <tr>
          <td width="100%" colspan="3" height="29">
            <table border="0" width="98%" cellspacing="0" cellpadding="0">
              <tr>
                <td width="31%">
                  <p align="right"></td>
                <td width="14%">
                </td>
                <td width="8%"></td>
                <td width="13%">
                  <p align="left"></td>
                <td width="17%">
                <p align="right">
                <html:button property="notPrevious" value="Anterior" onclick="doSubmitNotPrevious()" styleClass="button"  style="width: 69; height: 22"/>
                &nbsp;&nbsp;
                </td>
                <td width="18%">
                <html:button property="notNext" value="Siguiente" styleClass="button" onclick="doSubmitNotNext()" style="width: 71; height: 22"/>
                </td>
                		<script language="javascript">
						if(document.forms[0].emp1[0].value=="empty"){
							document.forms[0].notPrevious.disabled = true;
							document.forms[0].notNext.disabled = true;
						}
						else if(document.forms[0].notSentEmpty.value == 'empty'){
							document.forms[0].notPrevious.disabled = true;
							document.forms[0].notNext.disabled = true;
						}
					</script>                
              </tr>
              <tr>
                <td width="31%">&nbsp;
                
                </td>
                <td width="14%">&nbsp;
                  </td>
                <td width="8%">&nbsp;</td>
                <td width="13%">&nbsp;
                  </td>
                <td width="35%" colspan="2">&nbsp;
                
                </td>
              </tr>
              <tr>
                <td width="31%">
                </td>
                <td width="14%">
                  <input type="button" class="button" value="Generar" onclick="generateManifest()" name="generatebut">
                </td>
                <td width="8%">
					<html:button property="Detail" styleClass="button" value="Detalle" onclick="saveGuiaNumber()"/>
				</td>
                <td width="13%">
                  <input type="button" class="button" value="Imprimir" onClick='printDetails()' name="Print">
                </td>
                <td width="35%" colspan="2">
                <script language="javascript">
                	var notsentDetails = document.forms[0].notSentEmpty.value;
                	var sentDetails = document.forms[0].emptyrecs.value;
                	
                	if((document.forms[0].emp[0].value == 'empty') && (document.forms[0].emp1[0].value == 'empty')){
                		document.forms[0].Detail.disabled=true;
                	}
                	else if((notsentDetails == 'empty') && (sentDetails == 'empty')){
                		document.forms[0].Detail.disabled=true;
                	}

                </script>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td width="100%" colspan="3" height="21"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<input type='hidden' name='checkit'>
<html:hidden property="operation"/>
</html:form>
</body>
</html:html>
