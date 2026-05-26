/*
 * To use this notifications include the following in the jsp or html
 * 
 * <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
 * <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
 */
function showToast(messageType, message, duration) {
	const notifDuration = duration != null ? duration : 6000;
	const backgroundStyle = { background: getToastColor(messageType) };
	
    Toastify({
        text: message,
        duration: notifDuration,
        style: backgroundStyle,
        gravity: "top",
        position: "right",
        stopOnFocus: true,
        offset: {
            y: 55
          },
    }).showToast();
}

function getToastColor(messageType) {
    let backgroundColor;
    switch (messageType) {
        case "error":
            backgroundColor = "linear-gradient(to right, #e53935, #e35d5b)";
            break;
        case "warning":
            backgroundColor = "linear-gradient(to right, #ffa000, #ffc107)";
            break;
        case "info":
            backgroundColor = "linear-gradient(to right, #2979ff, #3f51b5)";
            break;
        case "success":
            backgroundColor = "linear-gradient(to right, #00a152, #61c267)";
            break;
        case "default":
            backgroundColor = "linear-gradient(to right, #0D4E94, #004e8e)";
            break;
        default:
            backgroundColor = "linear-gradient(to right, #0D4E94, #004e8e)";
    }

    return backgroundColor;
}