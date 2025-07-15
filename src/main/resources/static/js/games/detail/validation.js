function validateScoredGoal(edit){
    let editNew = edit? "-edit":"";
    let modal = document.querySelector(`#scored-goal${editNew}`);
    let errorSpan = modal.querySelector(".modal-error");
    let button = modal.querySelector(`.save`);
    let time = modal.querySelector(`#scored-goal-time-real${editNew}`);
    let scorer = modal.querySelector(`#goal${editNew}-id`);

    let timeCond = time.dataset.seconds != null && time.dataset.seconds !=="";
    let scorerCond = scorer.value != null && scorer.value !=="";
    let inputErrorCond = !Array.from(modal.querySelectorAll("input"))
        .some(el => el.classList.contains("error-input"));
    let assist1Error = modal.querySelector(`#assist1${editNew}`).classList.contains("error-input");
    let assist2Error = modal.querySelector(`#assist2${editNew}`).classList.contains("error-input");
    let onIceError = Array.from(modal.querySelectorAll(`#scored-goal-onIce1${editNew},#scored-goal-onIce2${editNew},#scored-goal-onIce3${editNew},#scored-goal-onIce4${editNew},#scored-goal-onIce5${editNew},#scored-goal-onIce6${editNew}`))
        .some(el => el.classList.contains("error-input"));


    button.disabled = !(timeCond && inputErrorCond && scorerCond);

    let messages = [];
    if (!timeCond) messages.push("čas není vyplněn");
    if (!scorerCond) messages.push("střelec není vybrán");
    if (assist1Error) messages.push("asistent 1 nebyl nalezen");
    if (assist2Error) messages.push("asistent 2 nebyl nalezen");
    if (onIceError) messages.push("hráč na ledě nebyl nalezen");

    if (messages.length > 0) {
        let finalMessage = messages
            .map((msg, i) => i === 0 ? msg.charAt(0).toUpperCase() + msg.slice(1) : msg)
            .join(", ");
        errorSpan.innerText = finalMessage;
        errorSpan.classList.remove("hide");
    } else {
        errorSpan.innerText = "";
        errorSpan.classList.add("hide");

    }

}


document.querySelectorAll("#scored-goal input").forEach(
    el=>{
        el.addEventListener("input", ()=> validateScoredGoal(false));
    }
)
document.querySelectorAll("#scored-goal-edit input").forEach(
    el=>{
        el.addEventListener("input", ()=> validateScoredGoal(true));
    }
)

function validateConcededGoal(edit){
    let editNew = edit? "-edit":"";
    let modal = document.querySelector(`#conceded-goal${editNew}`);
    let errorSpan = modal.querySelector(".modal-error");
    let button = modal.querySelector(`.save`);
    let time = modal.querySelector(`#conceded-goal-time-real${editNew}`);

    let timeCond = time.dataset.seconds != null && time.dataset.seconds !=="";
    let inputErrorCond = !Array.from(modal.querySelectorAll("input"))
        .some(el => el.classList.contains("error-input"));
    let onIceError = Array.from(modal.querySelectorAll(`#conceded-goal-onIce1${editNew},#conceded-goal-onIce2${editNew},#conceded-goal-onIce3${editNew},#conceded-goal-onIce4${editNew},#conceded-goal-onIce5${editNew},#conceded-goal-onIce6${editNew}`))
        .some(el => el.classList.contains("error-input"));


    button.disabled = !(timeCond && inputErrorCond);

    let messages = [];
    if (!timeCond) messages.push("čas není vyplněn");
    if (onIceError) messages.push("hráč na ledě nebyl nalezen");

    if (messages.length > 0) {
        let finalMessage = messages
            .map((msg, i) => i === 0 ? msg.charAt(0).toUpperCase() + msg.slice(1) : msg)
            .join(", ");
        errorSpan.innerText = finalMessage;
        errorSpan.classList.remove("hide");
    } else {
        errorSpan.innerText = "";
        errorSpan.classList.add("hide");

    }

}


document.querySelectorAll("#conceded-goal input").forEach(
    el=>{
        el.addEventListener("input", ()=> validateConcededGoal(false));
    }
)
document.querySelectorAll("#conceded-goal-edit input").forEach(
    el=>{
        el.addEventListener("input", ()=> validateConcededGoal(true));
    }
)


function validatePenalty(edit){
    let editNew = edit? "-edit":"";
    let modal = document.querySelector(`#penalty${editNew}`);
    let errorSpan = modal.querySelector(".modal-error");
    let button = modal.querySelector(`.save`);
    let time = modal.querySelector(`#penalty-time-real${editNew}`);

    let timeCond = time.dataset.seconds != null && time.dataset.seconds !=="";
    let inputErrorCond = !Array.from(modal.querySelectorAll("input"))
        .some(el => el.classList.contains("error-input"));
    let playerError = modal.querySelector(`#penalty-player${editNew}`).classList.contains("error-input");


    button.disabled = !(timeCond && inputErrorCond);

    let messages = [];
    if (!timeCond) messages.push("čas není vyplněn");
    if (playerError) messages.push("hráč nebyl nalezen");

    if (messages.length > 0) {
        let finalMessage = messages
            .map((msg, i) => i === 0 ? msg.charAt(0).toUpperCase() + msg.slice(1) : msg)
            .join(", ");
        errorSpan.innerText = finalMessage;
        errorSpan.classList.remove("hide");
    } else {
        errorSpan.innerText = "";
        errorSpan.classList.add("hide");

    }

}


document.querySelectorAll("#penalty input").forEach(
    el=>{
        el.addEventListener("input", ()=> validatePenalty(false));
    }
)
document.querySelectorAll("#penalty-edit input").forEach(
    el=>{
        el.addEventListener("input", ()=> validatePenalty(true));
    }
)


function validateOpponentPenalty(edit){
    let editNew = edit? "-edit":"";
    let modal = document.querySelector(`#opponent-penalty${editNew}`);
    let errorSpan = modal.querySelector(".modal-error");
    let button = modal.querySelector(`.save`);
    let time = modal.querySelector(`#opponent-penalty-time-real${editNew}`);

    let timeCond = time.dataset.seconds != null && time.dataset.seconds !=="";
    let inputErrorCond = !Array.from(modal.querySelectorAll("input"))
        .some(el => el.classList.contains("error-input"));


    button.disabled = !(timeCond && inputErrorCond);

    let messages = [];
    if (!timeCond) messages.push("čas není vyplněn");

    if (messages.length > 0) {
        let finalMessage = messages
            .map((msg, i) => i === 0 ? msg.charAt(0).toUpperCase() + msg.slice(1) : msg)
            .join(", ");
        errorSpan.innerText = finalMessage;
        errorSpan.classList.remove("hide");
    } else {
        errorSpan.innerText = "";
        errorSpan.classList.add("hide");

    }

}


document.querySelectorAll("#opponent-penalty input").forEach(
    el=>{
        el.addEventListener("input", ()=> validateOpponentPenalty(false));
    }
)
document.querySelectorAll("#opponent-penalty-edit input").forEach(
    el=>{
        el.addEventListener("input", ()=> validateOpponentPenalty(true));
    }
)