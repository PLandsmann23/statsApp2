async function newGoalScored() {
    const modal = document.querySelector("#scored-goal");


    const timeInput = modal.querySelector("#scored-goal-time-real");
    const time = timeInput.dataset.seconds
    const situation = modal.querySelector("#scored-goal-situation").value;
    const scorerId = modal.querySelector("#goal-id").value || null;
    const assistIds = [];
    const a1 = modal.querySelector("#assist1-id").value;
    const a2 = modal.querySelector("#assist2-id").value;
    if (a1) assistIds.push(Number(a1));
    if (a2) assistIds.push(Number(a2));
    const onIceIds = [];
    for (let i = 1; i <= 6; i++) {
        const onIceId = modal.querySelector(`#scored-goal-onIce${i}-id`).value;
        if (onIceId) onIceIds.push(Number(onIceId));
    }

    const goal = {
        type:"GoalScored",
        time: Number(time),
        scorerId: scorerId ? Number(scorerId) : null,
        assistIds: assistIds,
        onIceIds: onIceIds,
        situation: situation
    };

    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/goalScored`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(goal)
    });

    if (response.ok) {
        showMessage("Gól uložen", "success");
        closeModal(null, 'scored-goal');
        await loadData();
    } else {
        await handleFetchError(response);
    }

}

function openScoredGoalNew(){
    let modal = document.querySelector("#scored-goal");

    let period = modal.querySelector("[name=period]");

    period.value = loadFromSessionStorage("game").currentPeriod;

    openModal(modal,null);
}

async function editGoalScored() {
    const modal = document.querySelector("#scored-goal-edit");

    const eventId = modal.querySelector("#scored-goal-id").value;

    const timeInput = modal.querySelector("#scored-goal-time-real-edit");
    const time = timeInput.dataset.seconds
    const situation = modal.querySelector("#scored-goal-situation-edit").value;
    const scorerId = modal.querySelector("#goal-edit-id").value || null;
    const assistIds = [];
    const a1 = modal.querySelector("#assist1-edit-id").value;
    const a2 = modal.querySelector("#assist2-edit-id").value;
    if (a1) assistIds.push(Number(a1));
    if (a2) assistIds.push(Number(a2));
    const onIceIds = [];
    for (let i = 1; i <= 6; i++) {
        const onIceId = modal.querySelector(`#scored-goal-onIce${i}-edit-id`).value;
        if (onIceId) onIceIds.push(Number(onIceId));
    }

    const goal = {
        type:"GoalScored",
        time: Number(time),
        scorerId: scorerId ? Number(scorerId) : null,
        assistIds: assistIds,
        onIceIds: onIceIds,
        situation: situation
    };

        const response = await fetch(`${API_URL}events/${eventId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(goal)
        });

        if (response.ok) {
            showMessage("Gól upraven", "success");
            closeModal(null, 'scored-goal-edit');
            await loadData(); // pokud máš funkci pro reload dat
        } else {
            await handleFetchError(response);
        }

}


async function openScoredGoalEdit(id){
    const response = await fetch(`${API_URL}events/${id}`);

    if (response.ok) {
        const data = await response.json();

        parseScoredGoalToEdit(data);
        openModal("scored-goal-edit");
    } else {
        await handleFetchError(response);
    }
}

function parseScoredGoalToEdit(event) {
    const modal = document.querySelector("#scored-goal-edit");

    const time = modal.querySelector("#scored-goal-time-real-edit");
    time.value = secondsToGameTime(event.time);
    timeToAttribute(time);

    const situation = modal.querySelector("#scored-goal-situation-edit");
    setSelectValue(situation, event.situation);

    const scorer = modal.querySelector("#goal-edit");
    const scorerId = modal.querySelector("#goal-edit-id");
    if (event.scorer) {
        scorer.value = event.scorer.gameNumber;
        scorerId.value = event.scorer.id;
        findInRoster(scorer);
    }

    const assists = event.assists || [];
    const assistInputs = [
        modal.querySelector("#assist1-edit"),
        modal.querySelector("#assist2-edit")
    ];
    const assistIds = [
        modal.querySelector("#assist1-edit-id"),
        modal.querySelector("#assist2-edit-id")
    ];

    for (let i = 0; i < 2; i++) {
        if (assists[i]) {
            assistInputs[i].value = assists[i].gameNumber;
            assistIds[i].value = assists[i].id;
            findInRoster(assistInputs[i]);
        }
    }

    const onIce = event.onIce || [];
    for (let i = 0; i < 6; i++) {
        const numInput = modal.querySelector(`#scored-goal-onIce${i + 1}-edit`);
        const idInput = modal.querySelector(`#scored-goal-onIce${i + 1}-edit-id`);
        if (onIce[i]) {
            numInput.value = onIce[i].gameNumber;
            idInput.value = onIce[i].id;
            findInRoster(numInput);
        }
    }

    const idInput = modal.querySelector("#scored-goal-id");
    if (idInput) {
        idInput.value = event.id;
    }
}

function openScoredGoalDeleteDialog(){
    document.querySelector("input[name=delete-event-id]").value = document.querySelector("#scored-goal-id").value;
    closeModal(null, "scored-goal-edit");
    openDialog("delete-event");

}


