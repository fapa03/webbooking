<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html:html>
<head>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<script src="js/jquery.min.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/font-awesome.min.css">

<script>

function chkDate(){
	try{
	
		var dd=0, mm=0, yy=0, hh=0, mi=0;		
		var sMonDays=new Array;	
		var fromDate=null;	

		dateObj=document.forms[0].startDate.value;
		
		if(trim(dateObj)==""){
			alert("PLEASE ENTER DATE");
			document.forms[0].startDate.focus();
			return false;
		}
		
		if(dateObj != "" || dateObj!=null)
		{
		   fromDate=dateObj.split("/");
		  
		}
		
		if((dateObj.indexOf("/")==-1)){
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
			dtObj.select();
			dtObj.focus();
			return false;
		}
  
		
		}catch(exp){
			//alert(exp.Error);
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
</script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>Reporte</title>
<link rel="stylesheet" href="webbooking.css" >
</head>
<body bgcolor="#CEDEE9" onload="javascript:document.forms[0].startDate.focus()"  class="backgroundStandard">
	<html:form action="manifestreport">
	
			
	
	<strong class="titleReport">Reporte por fecha</strong>
<table width="100%" class="tablaDetallev2"  border="1" cellspacing="1" cellpadding="1">
<tr>
  			  
    <td width="25%"> 
		<div align="right"><font color="#004040">Inicial</font></div>
	</td>
    		
			    <td width="75%" align=""> 
			    	
			    	<div class="inputV2" style = "width:100; float: left;"  >
								<div class="group">       
									<html:text property = "startDate" style = "TEXT-TRANSFORM: uppercase;width:100" maxlength="80" onkeypress="loadDetailKey();"/>
																								      
									<span class="highlight"></span>
									<span class="bar" ></span>
								</div>
								
					</div>
					<div style="max-width:100; float:left; padding-top: 6px;">
								(dd/mm/yyyy)
					</div>
			  </td>
  			</tr>
			<tr>
				
	    		<td align="center" colspan="2">
	    			<a class="fa fa-download buttonMedium tagButton" onclick="if (chkDate()) { document.forms[0].action='manifestreport.do'; document.forms[0].submit() }" >Descargar</a>
	    		
	<%-- 			<html:button styleClass="button buttonLarge"  property="reportViewer" value="Mirar reporte"    /> --%>
				</td>
				
			</tr>
		</table>			
	</html:form>
</body>
</html:html>
