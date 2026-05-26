<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:html locale="true">
<head>
<title>Struts File Upload Example</title>
<html:base/>
</head>
<body bgcolor="white">
<html:form action="/FileUpload" method="post" enctype="multipart/form-data">
<table>
<tr>
<td align="center" colspan="2">
<font size="4">Please Enter the Following Details</font>
</tr>

<tr>
<td align="left" colspan="2">
<font color="red"><html:errors/></font>
</tr>



<tr>
<td align="right">
File Name
</td>
<td align="left">
<html:file property="theFile"/> 
</td>
</tr>


<tr>
<td align="center" colspan="2">
<html:submit>Upload File</html:submit>
</td>
</tr>
</table>


</html:form>
</body>
</html:html>
