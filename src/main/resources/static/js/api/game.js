function openGameCreate() {
    const modal = document.querySelector("#create-game");
    const settings = loadFromSessionStorage("userSettings");

    if (!settings) {
        showMessage("Nepodařilo se načíst uživatelské nastavení.", "error");
        return;
    }

    modal.querySelector("#game-periods").value = settings.periods ?? 3;
    modal.querySelector("#game-period-length").value = settings.periodLength ?? 20;

    openModal("create-game");
}

async function newGame() {
    const modal = document.querySelector("#create-game");

    const opponent = modal.querySelector("#game-opponent").value.trim();
    const date = modal.querySelector("#game-date").value;
    const time = modal.querySelector("#game-time").value;
    const venue = modal.querySelector("#game-venue").value.trim();
    const periodLength = Number(modal.querySelector("#game-period-length").value);
    const periods = Number(modal.querySelector("#game-periods").value);

    const game = { opponent, date, time, venue, periodLength, periods };

    const response = await fetch(`${API_URL}teams/${getIdFromUrl()}/games`, {
        headers: { "Content-Type": "application/json" },
        method: "POST",
        body: JSON.stringify(game)
    });

    if (response.ok) {
        await loadData();
        closeModal(modal, null);
    } else {
        await handleFetchError(response);
    }
}

async function openGameEdit(id) {
    const response = await fetch(`${API_URL}games/${id}`);

    if (response.ok) {
        const data = await response.json();
        const game = data.game || data;

        const modal = document.querySelector("#edit-game");

        modal.querySelector("#game-id").value = id;
        modal.querySelector("#game-opponent-edit").value = game.opponent || "";
        modal.querySelector("#game-date-edit").value = game.date || "";
        modal.querySelector("#game-time-edit").value = game.time || "";
        modal.querySelector("#game-venue-edit").value = game.venue || "";
        modal.querySelector("#game-period-length-edit").value = game.periodLength ?? 20;
        modal.querySelector("#game-periods-edit").value = game.periods ?? 3;

        openModal("edit-game");
    } else {
        await handleFetchError(response);
    }
}

async function editGame() {
    const modal = document.querySelector("#edit-game");
    const id = modal.querySelector("#game-id").value;

    const opponent = modal.querySelector("#game-opponent-edit").value.trim();
    const date = modal.querySelector("#game-date-edit").value;
    const time = modal.querySelector("#game-time-edit").value;
    const venue = modal.querySelector("#game-venue-edit").value.trim();
    const periodLength = Number(modal.querySelector("#game-period-length-edit").value);
    const periods = Number(modal.querySelector("#game-periods-edit").value);

    const game = { opponent, date, time, venue, periodLength, periods };

    const response = await fetch(`${API_URL}games/${id}`, {
        headers: { "Content-Type": "application/json" },
        method: "PUT",
        body: JSON.stringify(game)
    });

    if (response.ok) {
        await loadData();
        closeModal(modal, null);
    } else {
        await handleFetchError(response);
    }
}

function openGameDeleteDialog(id) {
    const dialog = document.querySelector("#delete-game");
    dialog.querySelector("[name=delete-game-id]").value = id;
    openDialog("delete-game");
}

async function deleteGame() {
    const dialog = document.querySelector("#delete-game");
    const id = dialog.querySelector("[name=delete-game-id]").value;

    const response = await fetch(`${API_URL}games/${id}`, {
        headers: { "Content-Type": "application/json" },
        method: "DELETE"
    });

    if (response.ok) {
        const data = await response.json();
        showMessage(data.message, "success");
        await loadData();
        closeDialog(dialog, null);
    } else {
        await handleFetchError(response);
    }
}

function validateGameCreate() {
    const modal = document.querySelector("#create-game");
    const opponent = modal.querySelector("#game-opponent");
    const error = modal.querySelector("#e-game-opponent");
    const button = modal.querySelector("#create-game-submit");

    const valid = opponent.value.trim().length > 0;
    button.disabled = !valid;
    error.innerText = valid ? "" : "Soupeř musí být vyplněn";
}

function validateGameEdit() {
    const modal = document.querySelector("#edit-game");
    const opponent = modal.querySelector("#game-opponent-edit");
    const error = modal.querySelector("#e-game-opponent-edit");
    const button = modal.querySelector("#edit-game-submit");

    const valid = opponent.value.trim().length > 0;
    button.disabled = !valid;
    error.innerText = valid ? "" : "Soupeř musí být vyplněn";
}

document.querySelectorAll("#edit-game input").forEach(
    el => el.addEventListener("input", validateGameEdit)
);

document.querySelectorAll("#create-game input").forEach(
    el => el.addEventListener("input", validateGameCreate)
);


async function addPeriod(){
    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/addPeriod`, {
        headers: { "Content-Type": "application/json" },
        method: "POST",
    });

    if (response.ok) {
        await loadData();
    } else {
        await handleFetchError(response);
    }

}

async function removePeriod(){
    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/removePeriod`, {
        headers: { "Content-Type": "application/json" },
        method: "POST",
    });

    if (response.ok) {
        await loadData();
    } else {
        await handleFetchError(response);
    }

}

async function changeGoalkeeper(){
    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/changeGoalkeeper`, {
        headers: { "Content-Type": "application/json" },
        method: "POST",
    });

    if (response.ok) {
        let data = await response.json();
        await loadData();
        showMessage(data.message, "success");
    } else {
        await handleFetchError(response);
    }

}