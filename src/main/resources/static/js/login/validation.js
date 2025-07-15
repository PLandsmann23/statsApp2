document.querySelectorAll("input").forEach(el =>
    el.addEventListener("input", () => {
        let button = document.querySelector("button");
        let username = document.querySelector("#username");
        let password = document.querySelector("#password");

        button.disabled = (username.value === "" || password.value === "");
    })
);