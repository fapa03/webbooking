

<%@page contentType="text/html"%>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="WEB-INF/struts-bean.tld" prefix="bean" %>


<%
	String str = (String)request.getAttribute("sLoginFlag");
	//AAPP//System.out.println("STRING "+str);	
%>


<html:html>
<head>
<title>Paquetexpress - Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<script language="JavaScript">
<!--

function initScript()
{
   document.forms[0].loginName.focus();
}

function submitForm(){
	if(document.forms[0].loginName.value==""){
		alert("CAPTURE UN NOMBRE DE USUARIO");
		document.forms[0].loginName.focus();
		return ;
	}else if(document.forms[0].password.value==""){
		alert("CAPTURE LA CONTRASEÑA");
		document.forms[0].password.focus();
		return ;
	}else
		document.forms[0].submit();
}
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; 
  if(d.images){ 
	if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; 
    for(i=0; i<a.length; i++)
		if (a[i].indexOf("#")!=0){ 
			d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];
		}
	}
}

function MM_findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->

</script>
<link rel="stylesheet" type="text/css" href="css/stylev2.css"/> 
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
</head>

<body  onLoad='initScript()'  bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('images/login_o.gif')">

	<html:form action="login">
		<div class="divLogin"> 

			<div class="divLogo">
				<img src="images/logos/Logo-WB-AZ-1000px.png" />
			</div>
			<table style="width: 420px;" border="0" cellspacing="0" cellpadding="0" align="center">

				<tr>
					<logic:present name="sLoginFlag" scope="request">
						<logic:equal name="sLoginFlag" value="no">
							<td align="center" colspan='3'><font color='red' size='4'><bean:message
										key="login.failed" /></font></td>
						</logic:equal>
						<logic:equal name="sLoginFlag" value="cc">
							<td align="center" colspan='3'><font color='red' size='4'><bean:message
										key="login.centrocosto" /></font></td>
						</logic:equal>
						<logic:equal name="sLoginFlag" value="0001">
							<td align="center" colspan='3'><font color='red' size='4'><bean:message
										key="login.err001" /></font></td>
						</logic:equal>
						<logic:equal name="sLoginFlag" value="002">
							<td align="center" colspan='3'><font color='red' size='4'><bean:message
										key="login.err002" /></font></td>
						</logic:equal>
						<logic:equal name="sLoginFlag" value="logout">
							<td align="center" colspan='3'><font color='red' size='4'><bean:message
										key="login.logout" /></font></td>
						</logic:equal>
					</logic:present>
				</tr>
				<tr>
					<td >
					<img src="images/login/user.png" />
					<html:text property="loginName"
							style="TEXT-TRANSFORM: uppercase;" maxlength="15" /></td>
				</tr>
		 
				<tr>
 
					<td >
						<img src="images/login/psw.png" />
						<html:password property="password"
							maxlength="15"
							onkeydown="if(window.event.keyCode==13 || window.event.keyCode==32) submitForm()" />
					</td>
				</tr>
 				<tr>
					<td><a href="javascript:submitForm()"
						onmouseout="MM_swapImgRestore()"
						onmouseover="MM_swapImage('Image7','','images/login_o.gif',1)">
							Enviar </a></td>
				</tr>
			</table>
			</td>
			</tr>

			</table>
			<html:hidden property="clientId" />
			<html:hidden property="assignedBranch" />
			<html:hidden property="tarifType" />
			<html:hidden property="additionalServiceFlag" />
	</html:form>
	</div>
</body>
</html:html>
