<%@ page language="java" import="java.util.*"%>

<HTML>
<HEAD>
<link rel="stylesheet" href="css/stylev2.css">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<TITLE></TITLE>


<script >
var colflag='false';
var rowid;
var rowidflag='true';
var oldobject;
var rownum;
function initScript(){
	try{
		var obj= document.getElementById('tablerow0');				
		changeColor(obj);
		}catch(JSException){
		}
}
function changeColor(obj){	
	rowid=obj.id;
	if(colflag=='true')
		oldobject.style.backgroundColor='#fff';
	obj.style.backgroundColor='#ccc';
	oldobject=obj;	
	colflag='true';
}
function getRow(){
	
	if(rowidflag=='true'){
		rowindex=rowid.indexOf('w')	
		rownum=rowid.substring(rowindex+1,rowid.length);
		
		window.opener.document.forms[0].preferedAddress.value="";	
		am_addr_styp= (document.getElementById('tablecol1'+rownum));
		u14 = (document.getElementById('tablecol2'+rownum));
		u16=(document.getElementById('tablecol3'+rownum));
		zip=(document.getElementById('tablecol4'+rownum));
		
		window.opener.document.forms[0].addressType.value = am_addr_styp.innerHTML;
		window.opener.document.forms[0].addressLine4.value = u14.innerHTML;
		window.opener.document.forms[0].addressLine6.value = u16.innerHTML;
		window.opener.document.forms[0].zipCode.value = zip.innerHTML;		
		
		window.opener.document.forms[0].addressCode.value=document.forms[0].addressCode[rownum].value;	
		window.opener.document.forms[0].doorNumber.value=document.forms[0].am_drnr[rownum].value;
		window.opener.document.forms[0].streetName.value=document.forms[0].am_strt_name[rownum].value;
		window.opener.document.forms[0].phoneNumber.value=document.forms[0].am_phono1[rownum].value;				
		window.opener.document.forms[0].suitNumber.value=document.forms[0].am_suitno[rownum].value;
		window.opener.document.forms[0].floorNumber.value=document.forms[0].am_floorno[rownum].value;
		window.opener.document.forms[0].addressLine1.value=document.forms[0].u11[rownum].value;
		window.opener.document.forms[0].addressLine2.value=document.forms[0].u12[rownum].value;
		window.opener.document.forms[0].addressLine3.value=document.forms[0].u13[rownum].value;
		window.opener.document.forms[0].addressLine5.value=document.forms[0].u15[rownum].value;
		window.opener.document.forms[0].addressLine7.value=document.forms[0].u17[rownum].value;
		
		drnr=document.forms[0].am_drnr[rownum].value;
		stname=document.forms[0].am_strt_name[rownum].value;
		u15=document.forms[0].u15[rownum].value;
		u13=document.forms[0].u13[rownum].value;
		u12=document.forms[0].u12[rownum].value;
		u11=document.forms[0].u11[rownum].value;
		u17=document.forms[0].u17[rownum].value;
							
		window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+"#"+drnr+" ,"+stname+", "+"\n";     
		window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+u16.innerHTML+",  "+u15+", "+"\n";
		window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+u14.innerHTML+", "+u13+", "+"\n";
		window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+u12+", "+u11+", ";
		
		if(u17!="" || u17!=null)
		  window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+u17;
	    if(u17=="" || u17==null)
		  window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+zip.innerHTML;		  
		  window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}
function getRow1(){
	
	if(rowidflag=='true'){
		rowindex=rowid.indexOf('w')	
		rownum=rowid.substring(rowindex+1,rowid.length);			
		am_addr_styp= (document.getElementById('tablecol1'+rownum));
		u14 = (document.getElementById('tablecol2'+rownum));
		u16=(document.getElementById('tablecol3'+rownum));
		zip=(document.getElementById('tablecol4'+rownum));	

		window.opener.document.forms[0].addressType.value=am_addr_styp.innerHTML;
		window.opener.document.forms[0].addressLine4.value=u14.innerHTML;
		window.opener.document.forms[0].addressLine6.value=u16.innerHTML;
		window.opener.document.forms[0].zipCode.value=zip.innerHTML;			
		
		window.opener.document.forms[0].addressCode.value=document.forms[0].addressCode.value;	
		window.opener.document.forms[0].doorNumber.value=document.forms[0].am_drnr.value;
		window.opener.document.forms[0].streetName.value=document.forms[0].am_strt_name.value;
		window.opener.document.forms[0].phoneNumber.value=document.forms[0].am_phono1.value;				
		window.opener.document.forms[0].suitNumber.value=document.forms[0].am_suitno.value;
		window.opener.document.forms[0].floorNumber.value=document.forms[0].am_floorno.value;
		window.opener.document.forms[0].addressLine1.value=document.forms[0].u11.value;
		window.opener.document.forms[0].addressLine2.value=document.forms[0].u12.value;
		window.opener.document.forms[0].addressLine3.value=document.forms[0].u13.value;
		window.opener.document.forms[0].addressLine5.value=document.forms[0].u15.value;
		window.opener.document.forms[0].addressLine7.value=document.forms[0].u17.value;
		
		drnr=document.forms[0].am_drnr.value;
		stname=document.forms[0].am_strt_name.value;
		u15=document.forms[0].u15.value;
		u13=document.forms[0].u13.value;
		u12=document.forms[0].u12.value;
		u11=document.forms[0].u11.value;
		u17=document.forms[0].u17.value;
		
		window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+drnr+", "+stname+", "+"\n";
		window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+u16.innerHTML+",  "+u15+", "+"\n";
		window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+u14.innerHTML+", "+u13+", "+"\n";
		window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+u12+", "+u11+", ";	
		
		if(u17!="" || u17!=null)
		  window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+u17;
	    if(u17=="" || u17==null)
		  window.opener.document.forms[0].preferedAddress.value=window.opener.document.forms[0].preferedAddress.value+zip.innerHTML;		  			
		  
		  window.close();
	}else{
		alert('NO HAY REGISTROS DISPONIBLES');
	}
}
</script>
</HEAD>
<BODY onLoad="initScript()">
<form name=frm>
<div id="datadiv" style="BACKGROUND-COLOR: #cedee9; HEIGHT: 160pt; OVERFLOW: auto; WIDTH: 100%">
	<table align="left" width="100%" border=1 cellspacing="1" cellpadding="1">
<tr> 
        <td width="25%" align="left" nowrap><b>Tipo de Dirección</b></td>
        <td  width="30%" align="left" nowrap><b>Ciudad</b></td>
        <td  width="25%" align="left" nowrap><b>Colonia</b></td>
        <td  width="25%" align="left" nowrap><b>Código Postal</b></td>
      </tr>
      <% 
		ArrayList result = (ArrayList) request.getAttribute("clientAddressRecords");		
        if(result.size()==0){
	%>
      <script language=javascript>
				rowidflag='false';				
			</script>
      <tr> 
        <td align="center" colspan=2 width="80%"><font size="5" color="red" >No 
          Records</font></td>
      </tr>
      <%
		}else{
		for(int i=0;i < result.size(); i++){
		HashMap values = (HashMap) result.get(i);
		//String temp =(String)values.get("AM_ADDR_CODE");
		//System.out.println("ADDRESS CODE IN LOV "+temp);
	%>
      <tr id="tablerow<%=i%>" name=t1 style="CURSOR: hand" onclick="changeColor(this)"> 
        <td width="25%" height="22" align="left" id="tablecol1<%=i%>"><%=values.get("AM_ADDR_STYP")%></td>
        <td align="left" id="tablecol2<%=i%>" width="20%"><%=values.get("u14")%></td>
        <td align="left" id="tablecol3<%=i%>" width="30%"><%=values.get("u16")%></td>
        <td align="left" id="tablecol4<%=i%>" width="25%"><%=values.get("Zipcode")%></td>
        <input type=hidden name="addressCode" value="<%=values.get("AM_ADDR_CODE")%>" >
        <input type=hidden name="am_strt_name" value="<%=values.get("AM_STRT_NAME")%>" >
        <input type=hidden name="am_drnr" value="<%=values.get("AM_DRNR")%>" >
        <input type=hidden name="am_phono1" value="<%=values.get("AM_PHNO1")%>" >
        <input type=hidden name="am_suitno" value="<%=values.get("AM_SUIT_NO")%>" >
        <input type=hidden name="am_floorno" value="<%=values.get("AM_FLOR_NO")%>" >
        <input type=hidden name="u11" value="<%=values.get("u11")%>" >
        <input type=hidden name="u12" value="<%=values.get("u12")%>" >
        <input type=hidden name="u13" value="<%=values.get("u13")%>" >
        <input type=hidden name="u15" value="<%=values.get("u15")%>" >
        <input type=hidden name="u17" value="<%=values.get("u17")%>" >
      </tr>
      <%
	 		}
		}
		
	  %>
    </table>
</div>
<table width=20% align=center>
<%
    if(result.size()== 1){
%>
<tr>
	<td><input type=button class="button1" value="Aceptar" onClick="getRow1()"></td>
	<td><input type=button class="button1" value="Cancelar" onclick="javascript:window.close()"></td>
</tr>
<%
 }
 else{
 
%>
<tr>
	<td><input type=button class="button1" value="Aceptar" onClick="getRow()"></td>
	<td><input type=button class="button1" value="Cancelar" onclick="javascript:window.close()"></td>
</tr>
<%
}
%>
</form>
</BODY>
