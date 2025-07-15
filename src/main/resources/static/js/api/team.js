async function newTeam(){
    let modal = document.querySelector("#create-team");
    let name =  modal.querySelector("#team-name").value;

    let team = {
        name: name
    }

    let response = await fetch(`${API_URL}teams`, {
        headers: {"Content-Type": "application/json"},
        method: "POST",
        body: JSON.stringify(team)
    });

    if(response.ok){
        await loadData();
        closeModal(modal, null);

    } else {
        await handleFetchError(response);
    }
}

async function editTeam(){
    let modal = document.querySelector("#edit-team");
    let id = modal.querySelector("#team-id").value;
    let name =  modal.querySelector("#team-name-edit").value;

    let team = {
        name: name
    }

    let response = await fetch(`${API_URL}teams/${id}`, {
        headers: {"Content-Type": "application/json"},
        method: "PUT",
        body: JSON.stringify(team)
    });

    if(response.ok){
        await loadData();
        closeModal(modal, null);

    } else {
        await handleFetchError(response);
    }
}

async function deleteTeam(){
    let dialog = document.querySelector("#delete-team");
    let id = dialog.querySelector("[name=delete-team-id]").value;

    let response = await fetch(`${API_URL}teams/${id}`, {
        headers: {"Content-Type": "application/json"},
        method: "DELETE"
    });

    if(response.ok){
        let data = await response.json();
        showMessage(data.message, "success");
        await loadData();
        closeDialog(dialog, null);

    } else {
        await handleFetchError(response);
    }
}

async function openTeamEdit(id){

    let response =  await fetch(`${API_URL}teams/${id}`);

    if(response.ok){
        let data = await response.json();

        let modal = document.querySelector("#edit-team");

        modal.querySelector("#team-id").value = id;

        modal.querySelector("#team-name-edit").value = data.name;

        openModal("edit-team");

    } else {
        await handleFetchError(response);
    }

}

function openTeamDeleteDialog(id){

        let dialog = document.querySelector("#delete-team");

        dialog.querySelector("[name=delete-team-id]").value = id;


        openDialog("delete-team");


}


document.querySelectorAll("#edit-team input").forEach(
    el=> el.addEventListener("input", validateTeamEdit)
)

document.querySelectorAll("#create-team input").forEach(
    el=> el.addEventListener("input", validateTeamCreate)
)

function validateTeamEdit(){
    let modal = document.querySelector("#edit-team");

    let name = modal.querySelector("#team-name-edit");
    let nameError = modal.querySelector("#e-team-name-edit");

    let button = modal.querySelector("#edit-team-submit");

    if(name.value.length >0){
        button.disabled = false;
        nameError.innerText = "";
    }else {
        button.disabled = true;
        nameError.innerText = "Název týmu musí být vyplněn";
    }
}

function validateTeamCreate(){
    let modal = document.querySelector("#create-team");

    let name = modal.querySelector("#team-name");
    let nameError = modal.querySelector("#e-team-name");

    let button = modal.querySelector("#create-team-submit");

    if(name.value.length >0){
        button.disabled = false;
        nameError.innerText = "";
    }else {
        button.disabled = true;
        nameError.innerText = "Název týmu musí být vyplněn";
    }
}