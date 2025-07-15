async function newGoalConceded() {
    const modal = document.querySelector("#conceded-goal");
    const time = modal.querySelector("#conceded-goal-time-real").dataset.seconds;
    const situation = modal.querySelector("#conceded-goal-situation").value;
    const emptyNet = modal.querySelector("#empty-net").checked;
    const onIceIds = [];
    for (let i = 1; i <= 6; i++) {
        const id = modal.querySelector(`#conceded-goal-onIce${i}-id`).value;
        if (id) onIceIds.push(Number(id));
    }

    let activeGk = findActiveGoalkeeper();

    const goal = {
        type: "GoalConceded",
        time: Number(time),
        onIceIds: onIceIds,
        situation: situation,
        inGoalId: !emptyNet && activeGk ? activeGk.id : null
    };

    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/goalConceded`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(goal)
    });

    if (response.ok) {
        showMessage("Gól vytvořen", "success");
        closeModal(null, 'conceded-goal');
        await loadData();
    } else {
        await handleFetchError(response);
    }
}

async function editConcededGoal() {
    const modal = document.querySelector("#conceded-goal-edit");
    const eventId = modal.querySelector("#conceded-goal-id").value;
    const time = modal.querySelector("#conceded-goal-time-real-edit").dataset.seconds;
    const situation = modal.querySelector("#conceded-goal-situation-edit").value;
    const emptyNet = modal.querySelector("#empty-net-edit").checked;
    const onIceIds = [];
    for (let i = 1; i <= 6; i++) {
        const id = modal.querySelector(`#conceded-goal-onIce${i}-edit-id`).value;
        if (id) onIceIds.push(Number(id));
    }

    let activeGk = findActiveGoalkeeper();

    const goal = {
        type: "GoalConceded",
        time: Number(time),
        onIceIds: onIceIds,
        situation: situation,
        inGoalId: !emptyNet && activeGk ? activeGk.id : null
    };

    const response = await fetch(`${API_URL}events/${eventId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(goal)
    });

    if (response.ok) {
        showMessage("Gól upraven", "success");
        closeModal(null, 'conceded-goal-edit');
        await loadData();
    } else {
        await handleFetchError(response);
    }
}

async function openConcededGoalEdit(id) {
    const response = await fetch(`${API_URL}events/${id}`);
    if (response.ok) {
        const data = await response.json();
        parseConcededGoalToEdit(data);
        openModal("conceded-goal-edit");
    } else {
        await handleFetchError(response);
    }
}

function parseConcededGoalToEdit(event) {
    const modal = document.querySelector("#conceded-goal-edit");
    const time = modal.querySelector("#conceded-goal-time-real-edit");
    time.value = secondsToGameTime(event.time);
    timeToAttribute(time);
    const situation = modal.querySelector("#conceded-goal-situation-edit");
    setSelectValue(situation, event.situation);
    const idInput = modal.querySelector("#conceded-goal-id");
    if (idInput) idInput.value = event.id;
    const onIce = event.onIce || [];
    for (let i = 0; i < 6; i++) {
        const numInput = modal.querySelector(`#conceded-goal-onIce${i + 1}-edit`);
        const idInput = modal.querySelector(`#conceded-goal-onIce${i + 1}-edit-id`);
        if (onIce[i]) {
            numInput.value = onIce[i].gameNumber;
            idInput.value = onIce[i].id;
            findInRoster(numInput);
        } else {
            numInput.value = "";
            idInput.value = "";
        }
    }
}

function openConcededGoalDeleteDialog(){
    document.querySelector("input[name=delete-event-id]").value = document.querySelector("#conceded-goal-id").value;
    closeModal(null, "conceded-goal-edit");
    openDialog("delete-event");

}