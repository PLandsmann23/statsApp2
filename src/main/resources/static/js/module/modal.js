
function openModal(modalName){
    let modal = document.getElementById(modalName);
    let currentPeriod = Number(loadFromSessionStorage("game")?.currentPeriod);
    let periods = Number(loadFromSessionStorage("game")?.periods);
    let rosterModals = ["scored-goal", "conceded-goal", "penalty", "scored-goal-edit", "conceded-goal-edit", "penalty-edit"];
    let needRoster = false;

    rosterModals.forEach(modalId => {
            if (modalName === modalId) {
                needRoster = true;
            }
        }
    );

    if(needRoster) rosterToButtons(modal);


    if(modal.id ==="scored-goal" || modal.id ==="conceded-goal" || modal.id ==="penalty" || modal.id ==="opponent-penalty"){
        modal.querySelector("[name=period]").value = currentPeriod;
        if(currentPeriod<=periods){
            modal.querySelector(".scoreboard-time").style.display = "initial";
            // modal.querySelectorAll("input[type=checkbox], input[type=checkbox] + label").forEach(el => el.style.display = "initial");


        } else {
            modal.querySelector(".scoreboard-time").style.display = "none";
            // modal.querySelectorAll("input[type=checkbox], input[type=checkbox] + label").forEach(el => el.style.display = "none");

        }
    }
    modal.style.display= "block";
    setTimeout(() => {
        modal.style.opacity=1;
    }, 10);
}

async function closeModal(modal, modalId){
    if(modal==null){
        modal = document.getElementById(modalId);
    }

    let modalError = modal.querySelector(".modal-error")??null;

    if(modalError!=null) {
        modalError.classList.add("hide");
        modalError.innerText = "";
    }

    for(let select of modal.querySelectorAll("select")){
        if(modal.id === "scored-goal" || modal.id === "scored-goal-edit" )setSelectValue(select,"5/5");
        if(modal.id === "penalty" || modal.id === "penalty-edit" || modal.id === "opponent-penalty" || modal.id === "opponent-penalty-edit") setSelectValue(select,"2");
    }


    for(let input of modal.querySelectorAll("input[type=checkbox], input[type=radio]")){
            input.checked = false;
        }


    for(let input of modal.querySelectorAll("input")){
        input.value = "";
        input.classList.remove("error-input");
    }

    for (let errorSpan of modal.querySelectorAll("span.error-message")){
        errorSpan.innerHTML = "";
    }
    modal.style.opacity=0;
    setTimeout(() => {

        modal.style.display= "none";
    }, 300);

}



document.addEventListener("click", (evt)=>{
    document.querySelectorAll('.modal').forEach(function(modal) {
        if (evt.target === modal) {
            closeModal(modal);
        }
    });
});

window.addEventListener('keydown', function(event) {
    if (event.key === "Escape") {
        document.querySelectorAll('.modal').forEach(function(modal) {
            if (modal.style.display === "block") {
                closeModal(modal);
            }
        });
    }
});