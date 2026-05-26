<html:html>

<head>
	<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
	<script type="text/javascript" charset="UTF-8" src="js/qz/printZPL/dependencies/rsvp-3.1.0.min.js"></script>
	<script type="text/javascript" charset="UTF-8" src="js/qz/printZPL/dependencies/sha-256.min.js"></script>
	<script type="text/javascript" charset="UTF-8" src="js/qz/printZPL/qz-tray.js"></script>
	<script type="text/javascript" charset="UTF-8" src="js/qz/printQZ.js"></script>
	
	<link rel="stylesheet" media="screen" type="text/css" href="css/QZTray.css">
	
<script type="text/javascript" src="js/v2/global.js"></script>
<title>Impresion QZ Tray</title>

<script type="text/javascript">
	$(window).load(function() {
// 		var searchParams = new URLSearchParams(window.location.search)
// 		var param = searchParams.get('idElement');
		var url = window.location.search;
		var param1 = url.split('?')[1];
		
		var param = param1.split('=')[1];
		 
		var zpl = $("#" + param, parent.document).val();	

		printZPLZebra(zpl);

	});
</script>

</head>
<body id="body">
	<div style="text-align: center;">

	    <img src="images/printer.png" id="imgPrinter" class="displayNone" style="width: 100px; margin: auto;" />
		<div id="textMsj"></div>

		<div id="rowImpresoras" class="displayNone">

			<div style="text-align: left;">
				<select style="width: 300px;margin: 20px 0 10px 0;height: 30px;" id="selectPrinters"
					onchange="onSelectImpresora(this.value)">
					<option value="-">Seleccione</option>
				</select>


			</div>
			<div style="text-align: left;">
				<input id="buttonPrintSend" type="button" style="display: none;"
					class="buttonPrintSend" value="Imprimir" name="buttonPrint"
					onclick="sendImpresion()" />
			</div>
		</div>

		<div class="success-checkmark" id="iconCheckSuccess"
			style="display: none;">
			<div class="check-icon">
				<span class="icon-line line-tip"></span> <span
					class="icon-line line-long"></span>
				<div class="icon-circle"></div>
				<div class="icon-fix"></div>
			</div>
		</div>

	</div>
</body>
</html:html>