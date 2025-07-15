function showMessage(message, type) {
    const messageBox = document.querySelector(".message-box");

    messageBox.querySelector("h3").innerHTML = "";
    if (message != null) {
        messageBox.querySelector("h3").innerHTML = message;
    }

    if (type != null) {
        let source = "/svg/";
        switch (type) {
            case "success": source += "check.svg"; break;
            case "error":   source += "warning.svg"; break;
            default:        source += "info.svg";
        }
        messageBox.querySelector("img").setAttribute("src", source);
    }

    // Zobraz a přechodem nastav opacity na 1
    messageBox.style.display = "flex";
    setTimeout(() => {
        messageBox.style.opacity = 1;
    }, 10);

    // Po 3 sekundách začni skrývat (opacity)
    setTimeout(() => {
        messageBox.style.opacity = 0;

        // A po přechodu opacity (např. 500 ms) skryj display
        setTimeout(() => {
            messageBox.style.display = "none";
        }, 500);
    }, 3000);
}
