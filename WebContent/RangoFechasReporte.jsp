<%@ page language="java" %>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html" %>

<html:html>
<link rel="stylesheet" href="webbooking.css">
<head>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<script src="js/jquery.min.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/font-awesome.min.css">

<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Rango de Fechas</title>
</head>
<body  bgcolor="#CEDEE9" onload="javascript:document.forms[0].startDate.focus()" class="backgroundStandard">
<script type="text/javascript">
	function submitReport(){
		if(chkDate1()){
			if(chkDate2()) {
				document.forms[0].action="rangoFechasReporte.do";
				document.forms[0].submit();
			}		
		}
	}
	
	function chkDate1(){
		try {
		
			var dd=0, mm=0, yy=0, hh=0, mi=0;		
			var sMonDays=new Array;	
			var fromDate=null;	
	
			dateObj=document.forms[0].startDate.value;
			
			if(trim(dateObj)=="") {
				alert("Por Favor Capture Fecha Inicial");
				document.forms[0].startDate.focus();
				return false;
			}
			
			if(dateObj != "" || dateObj!=null) {
			   fromDate=dateObj.split("/");		  
			}
			
			if((dateObj.indexOf("/")==-1)) {
				alert("FECHA INVALIDA, DEBE SER EN EL SIGUIENTE FORMATO: DD/MM/YYYY");
				document.forms[0].startDate.select();
				return false;
			}
			//below code commented by sundar on 07/10/2003
	       /*	dd = parseInt(fromDate[0]);		
			mm = parseInt(fromDate[1]);
			yy = parseInt(fromDate[2]);*/
			//code added by sundar on 07/10/2003	
			dd = parseInt(eval(fromDate[0]));		
			mm = parseInt(eval(fromDate[1]));
			yy = parseInt(eval(fromDate[2]));
			
			//alert("dd.."+dd+"   mm..."+mm+"  yy..."+yy);
			if (isNaN(dd) || isNaN(mm) || isNaN(yy) || isNaN(hh) || isNaN(mi) ||
				(yy>99 && yy<1000) || (mm<1 || mm>12) || (dd<1 || dd>31) ||
				(hh<0 || hh>23) || (mi<0 || mi>59) ){
				
				alert('FAVOR DE CAPTURAR LA FECHA CORRECTA EN EL FORMATO DD/MM/YYYY');
				document.forms[0].startDate.select();
				//dtObj.select();
				//dtObj.focus();
				return false;
			}
			sMonDays[0] =31;
			sMonDays[1] =29;
			sMonDays[2] =31;
			sMonDays[3] =30;
			sMonDays[4] =31;
			sMonDays[5] =30;
			sMonDays[6] =31;
			sMonDays[7] =31;
			sMonDays[8] =30;
			sMonDays[9] =31;
			sMonDays[10]=30;
			sMonDays[11]=31;
	
			yy = yy>50 && yy<100?(1900+yy):(yy<50?(2000+yy):yy);
	
			if (!((yy%4==0 && yy%100!=0) || yy%400==0)){ //not a leap year
				sMonDays[1]=28; 
			}
	
			if (dd<1 || dd>sMonDays[mm-1]){
				alert('EL DIA DEBE SER ENTRE 1 Y EL ULTIMO DEL MES');
				document.forms[0].startDate.select();
				//dtObj.select();
				//dtObj.focus();
				return false;
			}			
		} catch(exp) {
				//alert(exp.Error);
		}	
		return true;
	}
		
	function chkDate2(){
		try {		
			var dd=0, mm=0, yy=0, hh=0, mi=0;		
			var sMonDays=new Array;	
			var fromDate=null;	
	
			dateObj=document.forms[0].endDate.value;
			
			if(trim(dateObj)=="") {
				alert("PLEASE ENTER TO DATE");
				document.forms[0].endDate.focus();
				return false;
			}
			
			if(dateObj != "" || dateObj!=null) {
			   fromDate=dateObj.split("/");		  
			}
			
			if((dateObj.indexOf("/")==-1)) {
				alert("FECHA INVALIDA, DEBE SER EN EL SIGUIENTE FORMATO: DD/MM/YYYY");
				document.forms[0].endDate.select();
				return false;
			}
			//below code commented by sundar on 07/10/2003
	       /*	dd = parseInt(fromDate[0]);		
			mm = parseInt(fromDate[1]);
			yy = parseInt(fromDate[2]);*/
			//code added by sundar on 07/10/2003	
			dd = parseInt(eval(fromDate[0]));		
			mm = parseInt(eval(fromDate[1]));
			yy = parseInt(eval(fromDate[2])); 
			
			//alert("dd.."+dd+"   mm..."+mm+"  yy..."+yy);
			
			if (isNaN(dd) || isNaN(mm) || isNaN(yy) || isNaN(hh) || isNaN(mi) ||
				(yy>99 && yy<1000) || (mm<1 || mm>12) || (dd<1 || dd>31) ||
				(hh<0 || hh>23) || (mi<0 || mi>59) ){
				
				alert('FAVOR DE CAPTURAR LA FECHA CORRECTA EN EL FORMATO DD/MM/YYYY');
				//dtObj.select();
				//dtObj.focus();
				document.forms[0].endDate.select();
				return false;
			}
			sMonDays[0] =31;
			sMonDays[1] =29;
			sMonDays[2] =31;
			sMonDays[3] =30;
			sMonDays[4] =31;
			sMonDays[5] =30;
			sMonDays[6] =31;
			sMonDays[7] =31;
			sMonDays[8] =30;
			sMonDays[9] =31;
			sMonDays[10]=30;
			sMonDays[11]=31;
	
			yy = yy>50 && yy<100?(1900+yy):(yy<50?(2000+yy):yy);
	
			if (!((yy%4==0 && yy%100!=0) || yy%400==0)){ //not a leap year
				sMonDays[1]=28; 
			}
	
			if (dd<1 || dd>sMonDays[mm-1]){
				alert('EL DIA DEBE SER ENTRE 1 Y EL ULTIMO DEL MES');
				//dtObj.select();
				//dtObj.focus();
				document.forms[0].endDate.select();
				return false;
			}			
		} catch(exp){
			//alert(exp.Error);
		}
		return true;
   	}

	function trim(val){
		len = 0;
		try { // check the val is valid string 
			len = val.length;
		} catch(exp) {
			return "";
		}
		for(var i=0; i<val.length && val.substr(i,1)==' '; i++);
		for(var j=val.length-1; j>-1 && val.substr(j,1)==' '; j--);
		j++;
		if (i<j) 
			val=val.substring(i, j);
		else 
			val="";
		return val;
	}

</script>
<html:form action="/rangoFechasReporte.do">
<table border="1" class="tablaDetallev2"  width="100%" cellspacing="0" cellpadding="0">
	<caption>Informe de Servicios Facturados</caption>
	<tr>
		<td width="25%" align="right">Fecha inicial:</td>
		<td width="75%">
			<html:text  property="startDate"/>(dd/mm/yyyy)
		</td>
	 </tr>
	 <tr>
		<td width="25%" align="right">Fecha final:</td>
		<td width="75%">
			<html:text property="endDate"/>(dd/mm/yyyy)
		</td>
	 </tr>
	 <tr>
	   	<td colspan="2" align="center" width="23%">
	   		<a class="fa fa-download buttonMedium tagButton" onclick="submitReport()" >Descargar</a>
<%-- 			<html:button styleClass="button buttonLarge" property="but" value="Ver Reporte" onclick="submitReport()" /> --%>
		</td>
	</tr>
</table>
</html:form>
</body>
</html:html>
