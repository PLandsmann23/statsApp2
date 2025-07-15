async function newOpponentPenalty() {
    const modal = document.querySelector("#opponent-penalty");
    const time = modal.querySelector("#opponent-penalty-time-real").dataset.seconds;
    const length = modal.querySelector("#opponent-penalty-length").value;
    const coincidental = modal.querySelector("#opponent-coincidental").checked;

    const penalty = {
        type: "OpponentPenalty",
        time: Number(time),
        minutes: Number(length),
        coincidental: coincidental
    };

    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/opponentPenalty`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(penalty)
    });

    if (response.ok) {
        showMessage("Trest soupeře upraven", "success");
        closeModal(null, 'opponent-penalty');
        await loadData();
    } else {
        await handleFetchError(response);
    }
}

async function editOpponentPenalty() {
    const modal = document.querySelector("#opponent-penalty-edit");
    const eventId = modal.querySelector("#opponent-penalty-id").value;
    const time = modal.querySelector("#opponent-penalty-time-real-edit").dataset.seconds;
    const length = modal.querySelector("#opponent-penalty-length-edit").value;
    const coincidental = modal.querySelector("#opponent-coincidental-edit").checked;

    const penalty = {
        type: "OpponentPenalty",
        time: Number(time),
        minutes: Number(length),
        coincidental: coincidental
    };

    const response = await fetch(`${API_URL}events/${eventId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(penalty)
    });

    if (response.ok) {
        showMessage("Trest soupeře upraven", "success");
        closeModal(null, 'opponent-penalty-edit');
        await loadData();
    } else {
        await handleFetchError(response);
    }
}

async function openOpponentPenaltyEdit(id) {
    const response = await fetch(`${API_URL}events/${id}`);
    if (response.ok) {
        const data = await response.json();
        parseOpponentPenaltyToEdit(data);
        openModal("opponent-penalty-edit");
    } else {
        await handleFetchError(response);
    }
}

function parseOpponentPenaltyToEdit(event) {
    const modal = document.querySelector("#opponent-penalty-edit");
    const time = modal.querySelector("#opponent-penalty-time-real-edit");
    time.value = secondsToGameTime(event.time);
    timeToAttribute(time);
    const length = modal.querySelector("#opponent-penalty-length-edit");
    setSelectValue(length, event.minutes.toString());
    const coincidental = modal.querySelector("#opponent-coincidental-edit");
    coincidental.checked = event.coincidental || false;
    const idInput = modal.querySelector("#opponent-penalty-id");
    if (idInput) idInput.value = event.id;
}


function openOpponentPenaltyDeleteDialog(){
    document.querySelector("input[name=delete-event-id]").value = document.querySelector("#opponent-penalty-id").value;
    closeModal(null, "opponent-penalty-edit");
    openDialog("delete-event");

}