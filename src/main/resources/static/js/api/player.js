async function newPlayer() {
    const modal = document.querySelector("#create-player");

    const name = modal.querySelector("#player-name").value.trim();
    const surname = modal.querySelector("#player-surname").value.trim();
    const number = Number(modal.querySelector("#player-number").value);
    const position = modal.querySelector("#player-position").value;

    const player = { name, surname, number, position: {code: position} };

    const response = await fetch(`${API_URL}teams/${getIdFromUrl()}/players`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(player)
    });

    if (response.ok) {
        await loadData();
        closeModal(modal, null);
    } else {
        await handleFetchError(response);
    }
}


async function openPlayerEdit(id) {
    const response = await fetch(`${API_URL}players/${id}`);

    if (response.ok) {
        const player = await response.json();
        const modal = document.querySelector("#edit-player");

        modal.querySelector("#player-id").value = player.id;
        modal.querySelector("#player-name-edit").value = player.name;
        modal.querySelector("#player-surname-edit").value = player.surname;
        modal.querySelector("#player-number-edit").value = player.number;
        setSelectValue(modal.querySelector("#player-position-edit"),player.position);


        openModal("edit-player");
    } else {
        await handleFetchError(response);
    }
}

async function editPlayer() {
    const modal = document.querySelector("#edit-player");
    const id = modal.querySelector("#player-id").value;

    const name = modal.querySelector("#player-name-edit").value.trim();
    const surname = modal.querySelector("#player-surname-edit").value.trim();
    const number = Number(modal.querySelector("#player-number-edit").value);
    const position = modal.querySelector("#player-position-edit").value;

    const player = { name, surname, number, position: {code: position} };

    const response = await fetch(`${API_URL}players/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(player)
    });

    if (response.ok) {
        await loadData();
        closeModal(modal, null);
    } else {
        await handleFetchError(response);
    }
}


function openPlayerDeleteDialog(id) {
    const dialog = document.querySelector("#delete-player");
    dialog.querySelector("[name=delete-player-id]").value = id;
    openDialog("delete-player");
}

async function deletePlayer() {
    const dialog = document.querySelector("#delete-player");
    const id = dialog.querySelector("[name=delete-player-id]").value;

    const response = await fetch(`${API_URL}players/${id}`, {
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


function validatePlayerCreate() {
    const modal = document.querySelector("#create-player");
    const name = modal.querySelector("#player-name").value.trim();
    const surname = modal.querySelector("#player-surname").value.trim();

    let valid = true;

    if (!name) {
        modal.querySelector("#e-player-name").innerText = "Jméno musí být vyplněno";
        valid = false;
    } else {
        modal.querySelector("#e-player-name").innerText = "";
    }

    if (!surname) {
        modal.querySelector("#e-player-surname").innerText = "Příjmení musí být vyplněno";
        valid = false;
    } else {
        modal.querySelector("#e-player-surname").innerText = "";
    }


    modal.querySelector("#create-player-submit").disabled = !valid;
}

function validatePlayerEdit() {
    const modal = document.querySelector("#edit-player");
    const name = modal.querySelector("#player-name-edit").value.trim();
    const surname = modal.querySelector("#player-surname-edit").value.trim();

    let valid = true;

    if (!name) {
        modal.querySelector("#e-player-name-edit").innerText = "Jméno musí být vyplněno";
        valid = false;
    } else {
        modal.querySelector("#e-player-name-edit").innerText = "";
    }

    if (!surname) {
        modal.querySelector("#e-player-surname-edit").innerText = "Příjmení musí být vyplněno";
        valid = false;
    } else {
        modal.querySelector("#e-player-surname-edit").innerText = "";
    }



    modal.querySelector("#edit-player-submit").disabled = !valid;
}


document.querySelectorAll("#create-player input, #create-player select").forEach(el =>
    el.addEventListener("input", validatePlayerCreate)
);

document.querySelectorAll("#edit-player input, #edit-player select").forEach(el =>
    el.addEventListener("input", validatePlayerEdit)
);