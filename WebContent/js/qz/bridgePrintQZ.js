function initBridgePrintQZ(idElement) {
	try {
		var ifrm = document.createElement("iframe");
		//ifrm.setAttribute("src", "/webbooking/printQZTray.jsp?idElement="
		ifrm.setAttribute("src", "printQZTray.jsp?idElement="
				+ idElement);
		ifrm.style.width = "100%";
		ifrm.style.height = "100%";
		ifrm.style.border = "none";
		var zonePrint = document.createElement("div");
		zonePrint.setAttribute("id", "zonePrint");
		zonePrint.appendChild(ifrm);

		var zonePrintQZ = document.getElementById("zonePrintQZ");

		zonePrintQZ.appendChild(zonePrint);
	} catch (x) {
		console.log(x);
		setTimeout(function() {
			$("#zonePrintQZ", parent.document).html("");
		}, 5000);
	}
}