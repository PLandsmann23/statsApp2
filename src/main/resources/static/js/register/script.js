const selectors = {
    username: "#username",
    email: "#email",
    password: "#password",
    passwordRepeat: "#password-repeat",
    button: "button#register",
};

const errors = {
    username: "#username-error",
    email: "#email-error",
    password: "#password-error",
    passwordRepeat: "#password-repeat-error"
};


const emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

function showError(id, msg) {
    document.querySelector(errors[id]).innerText = msg;
}

function clearError(id) {
    showError(id, "");
}

function getValue(id) {
    return document.querySelector(selectors[id]).value.trim();
}

function validateField(id) {
    const value = getValue(id);
    switch (id) {
        case "username":
            if (value === "") showError(id, "Uživatelské jméno musí být vyplněno");
            else clearError(id);
            break;
        case "email":
            if (value === "") showError(id, "Email musí být vyplněn");
            else if (!emailRegex.test(value)) showError(id, "Email není ve správném formátu");
            else clearError(id);
            break;
        case "password":
            if (value === "") showError(id, "Heslo musí být vyplněno");
            else if (value.length < 6) showError(id, "Heslo musí mít alespoň 6 znaků");
            else clearError(id);
            break;
        case "passwordRepeat":
            if (value !== getValue("password")) showError(id, "Hesla se musí shodovat");
            else clearError(id);
            break;
    }
}

function checkFormValid() {
    const valid =
        getValue("username") &&
        emailRegex.test(getValue("email")) &&
        getValue("password").length >= 6 &&
        getValue("password") === getValue("passwordRepeat");

    document.querySelector(selectors.button).disabled = !valid;
}

async function handleRegister() {
    const body = {
        username: getValue("username"),
        email: getValue("email"),
        password: getValue("password"),
    };

    try {
        const response = await fetch("api/users/register", {
            headers: { "Content-Type": "application/json" },
            method: "POST",
            body: JSON.stringify(body),
        });

        if (response.ok) {
            window.location.href = "/login?register";
        } else {
            const data = await response.json();
            Object.keys(data).forEach((key) => {
                if (errors[key]) showError(key, data[key]);
            });
        }
    } catch (e) {
        alert("Chyba při registraci: " + e.message);
    }
}

// === EVENTY ===

Object.keys(selectors).forEach((id) => {
    const input = document.querySelector(selectors[id]);
    if (!input) return;

    const eventType = id === "passwordRepeat" || id === "password" ? "input" : "blur";

    input.addEventListener(eventType, () => {
        validateField(id);
        checkFormValid();
    });

    input.addEventListener("input", checkFormValid); // pro live validaci tlačítka
});

document.addEventListener("keypress", (event) => {
    if (event.key === "Enter" && !document.querySelector(selectors.button).disabled) {
        handleRegister();
    }
});
