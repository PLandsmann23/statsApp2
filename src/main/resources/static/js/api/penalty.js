async function newPenalty() {
    const modal = document.querySelector("#penalty");
    const time = modal.querySelector("#penalty-time-real").dataset.seconds;
    const playerId = modal.querySelector("#penalty-player-id").value || null;
    const length = modal.querySelector("#penalty-length").value;
    const reason = modal.querySelector("#reason").value;
    const coincidental = modal.querySelector("#coincidental").checked;

    const penalty = {
        type: "Penalty",
        time: Number(time),
        playerId: playerId ? Number(playerId) : null,
        minutes: Number(length),
        reason: reason,
        coincidental: coincidental
    };

    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/penalty`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(penalty)
    });

    if (response.ok) {
        showMessage("Trest vytvořen", "success");
        closeModal(null, 'penalty');
        await loadData();
    } else {
        await handleFetchError(response);
    }
}

async function editPenalty() {
    const modal = document.querySelector("#penalty-edit");
    const eventId = modal.querySelector("#penalty-id").value;
    const time = modal.querySelector("#penalty-time-real-edit").dataset.seconds;
    const playerId = modal.querySelector("#penalty-player-edit-id").value || null;
    const length = modal.querySelector("#penalty-length-edit").value;
    const reason = modal.querySelector("#reason-edit").value;
    const coincidental = modal.querySelector("#coincidental-edit").checked;

    const penalty = {
        type: "Penalty",
        time: Number(time),
        playerId: playerId ? Number(playerId) : null,
        minutes: Number(length),
        reason: reason,
        coincidental: coincidental
    };

    const response = await fetch(`${API_URL}events/${eventId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(penalty)
    });

    if (response.ok) {
        showMessage("Trest upraven", "success");
        closeModal(null, 'penalty-edit');
        await loadData();
    } else {
        await handleFetchError(response);
    }
}

async function openPenaltyEdit(id) {
    const response = await fetch(`${API_URL}events/${id}`);
    if (response.ok) {
        const data = await response.json();
        parsePenaltyToEdit(data);
        openModal("penalty-edit");
    } else {
        await handleFetchError(response);
    }
}

function parsePenaltyToEdit(event) {
    const modal = document.querySelector("#penalty-edit");
    const time = modal.querySelector("#penalty-time-real-edit");
    time.value = secondsToGameTime(event.time);
    timeToAttribute(time);
    const playerInput = modal.querySelector("#penalty-player-edit");
    const playerIdInput = modal.querySelector("#penalty-player-edit-id");
    if (event.player) {
        playerInput.value = event.player.gameNumber;
        playerIdInput.value = event.player.id;
        findInRoster(playerInput);
    } else {
        playerInput.value = "";
        playerIdInput.value = "";
    }
    const length = modal.querySelector("#penalty-length-edit");
    setSelectValue(length, event.minutes.toString());
    const reason = modal.querySelector("#reason-edit");
    reason.value = event.reason || "";
    const coincidental = modal.querySelector("#coincidental-edit");
    coincidental.checked = event.coincidental || false;
    const idInput = modal.querySelector("#penalty-id");
    if (idInput) idInput.value = event.id;
}

function openPenaltyDeleteDialog(){
    document.querySelector("input[name=delete-event-id]").value = document.querySelector("#penalty-id").value;
    closeModal(null, "penalty-edit");
    openDialog("delete-event");

}
