document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);

    if (params.has("logout")) {
        showMessage("Uživatel byl odhlášen", "success");
    }

    if (params.has("register")) {
        showMessage("Registrace byla úspěšná, nyní se můžete přihlásit", "success");
    }

    if (params.has("error")) {
        const errorType = params.get("error");
        if (errorType === "un") {
            document.querySelector("#username-error").innerText = "Uživatelské jméno nebo email nejsou správné";
        }
        if (errorType === "p") {
            document.querySelector("#password-error").innerText = "Heslo není správné";
        }
        if (errorType === "ne") {
            document.querySelector("#username-error").innerText = "Účet nebyl ověřen, nejprve účet ověřte pomocí emailu";
        }
    }
});