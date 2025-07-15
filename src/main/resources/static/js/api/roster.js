async function newRoster(playerId) {

    const roster = {player: {id: playerId}};

    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/roster`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(roster)
    });

    if (response.ok) {
        showMessage("Hráč přidán na soupisku", "success");
        await loadData();
    } else {

        if(response.status == 409){
            let data = await response.json();
            pasteConflict(data, false);
        } else {
            await handleFetchError(response);
        }

    }
}


function openRosterNumberEdit(button) {
    let row = button.closest('tr');
    let id = row.dataset.id;
    let input = row.querySelector("input");
    let img = button.querySelector("img");
    img.src = '/svg/tick.svg';
    input.disabled = false;
    input.dataset.original = input.value;
    button.onclick = ()=> editRoster(id, button);
}

async function editRoster(id, button) {
    let row = button.closest('tr');
    let input = row.querySelector("input");

    if(input.dataset.original == input.value){
        await loadData();
        return;
    }

    const roster = {gameNumber: input.value};

    const response = await fetch(`${API_URL}roster/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(roster)
    });

    if (response.ok) {
        await loadData();
    } else {
        if(response.status == 409){
            let data = await response.json();
            pasteConflict(data, true);
        } else {
            await handleFetchError(response);
        }
    }
}



function openRosterDeleteDialog(id) {
    const dialog = document.querySelector("#delete-roster");
    dialog.querySelector("[name=delete-roster-id]").value = id;
    openDialog("delete-roster");
}

async function deleteRoster() {
    const dialog = document.querySelector("#delete-roster");
    const id = dialog.querySelector("[name=delete-roster-id]").value;

    const response = await fetch(`${API_URL}roster/${id}`, {
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

async function resolveConflict(){
    let modal = document.querySelector("#number-conflict");

    let newNumber = Number(modal.querySelector("#new-player-number").value);
    let oldNumber = Number(modal.querySelector("#roster-player-number").value);

    let oldId = modal.querySelector("#old-roster-id").value.trim();
    let newRosterId = modal.querySelector("#new-roster-id").value.trim();
    let newPlayerId = modal.querySelector("#new-player-id").value.trim();


    let isEdit = !!newRosterId;

    let oldPlayer = {
        rosterId: oldId,
        newNumber: oldNumber
    }

    let newPlayer;

    if(isEdit){
        newPlayer = {
            rosterId: newRosterId,
            newNumber: newNumber
        };
    } else {
        newPlayer = {
            playerId: newPlayerId,
            newNumber: newNumber
        };
    }

    let rosters = [oldPlayer, newPlayer];

    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/roster/conflict`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(rosters)
    });

    if (response.ok) {
        await loadData();
        closeModal(modal, null);
    } else {
            await handleFetchError(response);
    }


}

function pasteConflict(data, edit){
    let modal = document.querySelector("#number-conflict");
    let button = modal.querySelector("#number-conflict-submit")

    let conflictingNumber = data.conflictingNumber;

    let newPlayer = edit?data.editedRoster:data.newPlayer;
    let conflictingRoster = data.conflictingRoster;
    let newPlayerName = modal.querySelector("#new-player-name");
    let conflictingRosterName = modal.querySelector("#roster-player-name");


    if(edit){
        modal.querySelector("#new-roster-id").value = data.editedRoster.id;
        newPlayerName.innerText = `${newPlayer.player.name} ${newPlayer.player.surname}`;
    } else {
        modal.querySelector("#new-player-id").value = data.newPlayer.id;
        newPlayerName.innerText = `${newPlayer.name} ${newPlayer.surname}`;
    }

    modal.querySelector("#old-roster-id").value = data.conflictingRoster.id;


    let newPlayerNumber = modal.querySelector("#new-player-number");
    let conflictingRosterNumber = modal.querySelector("#roster-player-number");

    conflictingRosterName.innerText = `${conflictingRoster.player.name} ${conflictingRoster.player.surname}`;

    newPlayerNumber.value = conflictingNumber;
    conflictingRosterNumber.value = conflictingNumber;


    button.disabled = true;

    button.onclick = null;
    button.onclick = ()=> resolveConflict(edit);
    openModal("number-conflict");
}


function validateNumberConflict() {
    const modal = document.querySelector("#number-conflict");


    let oldNumber = modal.querySelector("#roster-player-number").value.trim();
    let newNumber = modal.querySelector("#new-player-number").value.trim();

    let oldId = modal.querySelector("#old-roster-id").value.trim();
    let newRosterId = modal.querySelector("#new-roster-id").value.trim();
    let newPlayerId = modal.querySelector("#new-player-id").value.trim();


    let isEdit = !!newRosterId;
    const idsToIgnore = [oldId];
    if (isEdit) idsToIgnore.push(newRosterId);

    let valid = true;

    if (newNumber === "") {
        valid = false;
    } else if (isNumberTakenExcept(idsToIgnore, newNumber)) {
        modal.querySelector("#e-new-player-number").innerText =
            `Hráč s číslem ${newNumber} již na soupisce je`;
        valid = false;
    } else {
        modal.querySelector("#e-new-player-number").innerText = "";
    }

    if (oldNumber === "") {
        valid = false;
    } else if (isNumberTakenExcept(idsToIgnore, oldNumber)) {
        modal.querySelector("#e-roster-player-number").innerText =
            `Hráč s číslem ${oldNumber} již na soupisce je`;
        valid = false;
    } else {
        modal.querySelector("#e-roster-player-number").innerText = "";
    }

    if (oldNumber !== "" && newNumber !== "" && oldNumber === newNumber) {
        modal.querySelector("#e-number-conflict").innerText = "Čísla se shodují";
        valid = false;
    } else {
        modal.querySelector("#e-number-conflict").innerText = "";
    }

    modal.querySelector("#number-conflict-submit").disabled = !valid;
}


function isNumberTakenExcept(idsToIgnore, numberToCheck) {
    const roster = loadFromSessionStorage("roster");

    return roster
        .filter(player => !idsToIgnore.includes(String(player.id)))
        .some(player => String(player.gameNumber) === String(numberToCheck));
}

// Spustit validaci při každém vstupu
document.querySelectorAll("#number-conflict input").forEach(el =>
    el.addEventListener("input", validateNumberConflict)
);

function parseSelectGoalie(data){
    let modal = document.querySelector("#select-active-goalie");

    let goalkeeper1 = modal.querySelector("#goalie1");
    let goalkeeper2 = modal.querySelector("#goalie2");
    let goalkeeper1Label = modal.querySelector("label[for=goalie1]");
    let goalkeeper2Label = modal.querySelector("label[for=goalie2]");

    goalkeeper1.value = data[0].id;
    goalkeeper2.value = data[1].id;

    goalkeeper1Label.innerText = `#${data[0].gameNumber} ${data[0].player.name} ${data[0].player.surname}`;
    goalkeeper2Label.innerText = `#${data[1].gameNumber} ${data[1].player.name} ${data[1].player.surname}`;

    openModal("select-active-goalie");
}

async function selectGoalie() {
    let id = getSelectedRadioValue("goalie").value;

    if(!id){
        return;
    }

    const roster = {activeGk: true};

    const response = await fetch(`${API_URL}roster/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(roster)
    });

    if (response.ok) {
        await loadData();
        closeModal(null, "select-active-goalie");
    } else {
            await handleFetchError(response);

    }
}
