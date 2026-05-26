<HTML>
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY >
<%
	String printString = (String)session.getAttribute("printstring");
	//AAPP//System.out.println("PRINT STRING "+ printString);
	session.removeAttribute("printstring");
%>
<script>
function closeWin(){
	window.opener.document.forms[0].borderbranchcheck.value="";
	window.close();
}
</script>
<form name=frm>
<%= printString %>
</form>
</BODY>
</HTML>