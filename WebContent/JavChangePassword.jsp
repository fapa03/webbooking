<%@ page contentType="text/html" %>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="WEB-INF/struts-bean.tld" prefix="bean" %>
<html:html>
<head>

<script>
function initScript(){
	clearForm(false);
    checkErrors();
}

function clearForm(forceClear){
	if (document.forms[0].action.value == "" || forceClear){
		document.forms[0].oldPassword.focus();
		document.forms[0].oldPassword.value="";
		document.forms[0].newPassword.value="";
		document.forms[0].confirmPassword.value="";
		document.forms[0].valPassReq.value = "";
		document.forms[0].sPwdUpdated.value = "";
	}
}

function checkErrors(){
	if (document.forms[0].valPassReq.value == "false"){
		   Lobibox.notify('error', {
			    size: 'mini',
			    icon:'fa fa-times-circle',
			    msg: 'La contraseña no cumple con los requerimientos de seguridad. Favor de intentar nuevamente',
			    sound: 'sound5',
			    delay: false
			});
		   document.forms[0].valPassReq.value = "";
	 } else if (document.forms[0].sPwdUpdated.value == "no"){
		 Lobibox.notify('error', {
			    size: 'mini',
			    icon:'fa fa-times-circle',
			    msg: 'La contraseña NO ha sido cambiada',
			    sound: 'sound5',
			    delay: false
			});
	}
	clearForm(true);
}
function checkPassword(){
	var oldPass = document.forms[0].oldPassword.value;
	var newPass = document.forms[0].newPassword.value;
	var conPass = document.forms[0].confirmPassword.value;
	
	if(oldPass=="" || oldPass==null){	
		alert("CAPTURE LA CONTRASEÑA ACTUAL");
		document.forms[0].oldPassword.focus();
		return false;
	}else if(newPass=="" || newPass==null){	
		alert("CAPTURE LA NUEVA CONTRASEÑA");
		document.forms[0].newPassword.focus();
		return false;
	}else if(conPass=="" || conPass==null){	
		alert("CONFIRME LA NUEVA CONTRASEÑA");
		document.forms[0].confirmPassword.focus();
		return false;
	}else if(newPass != conPass){
		alert("LA NUEVA CONTRASEÑA NO CORRESPONDE CON LA CONFIRMACION");		
		return false;
	}else if(oldPass==newPass){
		alert("LA NUEVA CONTRASEÑA NO DEBE SER LA MISMA QUE LA ANTERIOR");
		document.forms[0].newPassword.select();
		return false;
	}else if(trim(newPass).length==0){
		alert('NO DEBE CONTENER ESPACIOS ENTRE LOS CARACTERES');
		return false;
	}else{
		document.forms[0].action.value = "modifyPassword";
	    document.forms[0].submit();	   
	}
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
	
	var n2sp = 0;

	return val;
}


function regresapantalla(valor)
{
	window.history.go(-1);
}

</script>
<title>Cambiar contraseña</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="css/change-password.css">
<link rel="stylesheet" href="css/lobibox.css"/>
<link rel="stylesheet" href="css/font-awesome.min.css"/>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
<script src="js/jquery.min.js"></script>
<style type="text/css">
input[type="button"] {
    font-size: 12px;
    margin: 0 !important;
    padding: 0 !important;
     box-shadow: none;
}
.inputV2 input {
    padding: 5px;
    font-size: 12px;
    margin: 0;
   
}
.inputV2 {
    margin-bottom: 20px;
}
.inputV2 label{
	display: block;
	width: 100%;
	text-align: center;
}

</style>

</head>
<body onLoad="initScript()" class="backgroundStandard" style="height: 100%;width: 100%;" >

<h1>
<jsp:include page="include.jsp" flush="false" /> 
</h1>

<section class="backgroudBluePtx" style="width: 300px;margin: auto;padding: 40px 20px 15px 20px;background: rgba(112,112,112 , 0.1);border-radius: 10px;">

	<html:form action="changepassword">
			 
			 
			  <div class="inputV2">
				  <div class="group">       
				      <html:password property="oldPassword" maxlength="15" />
				      <span class="highlight"></span>
				      <span class="bar"></span>
				      <label for="oldPassword">Contraseña anterior</label>
				  </div>
			    </div>		
	         
			  <div class="inputV2">
				  <div class="group">       
				      <html:password property="newPassword"  maxlength="15"/>
				      <span class="highlight"></span>
				      <span class="bar"></span>
				      <label for="newPassword">Nueva contraseña </label>
				  </div>
			    </div>		          
	          	
	  		   <div class="inputV2">
				  <div class="group">       
				      <html:password property="confirmPassword"  maxlength="15"/>
				      <span class="highlight"></span>
				      <span class="bar"></span>
				      <label for="confirmPassword">Confirme nueva contraseña</label>
				  </div>
			    </div>
			    <b>Requerimientos de contraseña:</b>          
	          	<ul>
			      <li>Mínimo 10 caracteres</li>
			      <li>Al menos un número</li>
			      <li>Al menos 1 letra minúscula</li>
			      <li>Al menos 1 letra mayúscula</li>
			      <li>Al menos 1 carácter especial (ej: #, $, %)</li>
			      <li>No se permiten espacios</li>
			    </ul>
				<div align="center">
			        <html:button onclick="regresapantalla();" value="Regresar" styleClass="button buttonMedium" property="regresar"/>
			        <html:button onclick="checkPassword()" value="Cambiar" styleClass="button buttonMedium" property="Change Password"/>
			        <html:hidden property="sPwdUpdated" />
			        <html:hidden property="valPassReq" />
			        <html:hidden property="action" />
			    </div>
	</html:form>
</section>
<script type="text/javascript" src="js/lobibox.min.js"></script>
<script src="js/notifications.min.js"></script>
</body>
</html:html>
