
<head>
<SCRIPT language="javascript">
function showmodel()
{
var val=window.open('dialog.jsp','toolbar=no,titlebar=no','width=430,height=200,top=210,left=350,screenY=185,screenX=200');

}
function fun()

{

if(document.all.txt_selection.value.length > 0)
{
window.document.forms[0].submit();
}
}
</SCRIPT>
</HEAD>


<BODY onload="return showmodel();" onfocus="fun();">
<form name="model" action="manifestgeneration.do" method="post">
<input type="hidden" name="txt_selection" > 
</form>
</body>