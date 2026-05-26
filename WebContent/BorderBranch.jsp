<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<link rel="stylesheet" media="screen" type="text/css" href="css/font-awesome.min.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<link rel=stylesheet media=screen type=text/css href=webbooking.css />
<LINK href="Styles.css" type="text/css" rel="StyleSheet">
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon">
</HEAD>
<BODY class="backgroundStandard widthOverflow" style="margin: 0px !important; cursor: default;" onunload="reabrir();" onload="javascript:document.forms[0].pedinumber.focus()">
<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.
	String from =request.getParameter("from");
	String isNewBooking = request.getParameter("isNewBooking")==null?"":request.getParameter("isNewBooking");
%>
<script>
function populate(){
	var band = true;
	if (document.frm.pedinumber.value =="") {
		alert("Capture Número de Pedimento");
		band = false;
	} else {
		if (document.frm.custagent.value == "") {
			alert("Capture Nombre de Agente");
			band = false;
		}
	}
	
	if (band) {
		window.opener.document.forms[0].borderbranchcheck.value="false";
		window.opener.document.forms[0].pedinumber.value=document.frm.pedinumber.value;
		window.opener.document.forms[0].custagent.value=document.frm.custagent.value;
		
		document.forms[0].banCerrar.value='true';
		/*Modificacion para la nueva documentacion.*/
		/* if (document.forms[0].isNewBooking.value != "Y") {
			window.opener.document.forms[0].action="webBookinggeneral.do?from=<\%= from %>";
			window.opener.document.forms[0].submit();
		} */
		
		try{
			window.opener.document.forms[0].accion.value = "validaPedimento";
		}catch(e){
			window.opener.document.forms[0].currentTask.value = "validaPedimento";
		}
	    window.opener.document.forms[0].submit();
		self.close();
	}
}

function closeWin(){
	if (document.forms[0].isNewBooking.value != "Y") {
		window.opener.document.forms[0].borderbranchcheck.value="";
	}
	window.opener.document.forms[0].showCapNumPedi.value = false;
	window.close();
}

function reabrir() {
	if (window.opener.document.forms[0].showCapNumPedi.value == true){
		if (document.forms[0].isNewBooking.value == 'Y') {
			if (document.forms[0].banCerrar.value!='true') {
				window.opener.abrirBorderBranch();
			}
		}	
	}
}
</script>
<form name=frm>
<input type="hidden" name="isNewBooking" id="isNewBooking" value="<%=isNewBooking%>" >
<input type="hidden" name="banCerrar" value="false"/>
<br>
<br>

<TABLE WIDTH=100% ALIGN=center  BORDER=0 CELLSPACING=0 CELLPADDING=0>
	<TR>
		<TD align=right>Numero de pedimento&nbsp;</TD>
		<TD><input type=text name="pedinumber" maxlength="50" style="TEXT-TRANSFORM: uppercase"></TD>
		
	</TR>
	<tr height=2>
		<td colspan=2 >
	</tr>
	<TR>
		<TD align=right>Agente Aduanal&nbsp;</TD>
		<TD><input type=text name="custagent" maxlength="50" style="TEXT-TRANSFORM: uppercase" onkeydown="if(window.event.keyCode==13) { populate() }" ></TD>
	</TR>
	<tr height=10>
	<td colspan=2 >
	</tr>
	<TR>
		<TD align=right><input type=button class="button1"  value="Aceptar" onclick="populate()">&nbsp;</TD>
		<TD align=left><input type=button class="button1"  value="Cancelar" onclick="closeWin()"></TD>
	</TR>
</TABLE>
</form>
</BODY>
</HTML>