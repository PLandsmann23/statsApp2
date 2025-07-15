function parseEvents(data){
    let table = document.querySelector("#events");
    table.innerHTML = "";
    let periods = data.game.periods +1;

    for(let periodEvents of data.events){
        let period = periodEvents.period;
        let events = periodEvents.events;
        let thead = document.createElement('thead');
        thead.innerHTML = `<thead><tr><th>${getPeriodWithName(period)}</th><th>&nbsp;</th></tr></thead>`
        table.appendChild(thead);
        let body = document.createElement('tbody');

        for(let event of events){
            let tr;

            switch (event.type){
                case "GoalScored":{
                    tr = scoredGoalToRow(event);
                    break;
                }
                case "GoalConceded":{
                    tr = concededGoalToRow(event);
                    break;
                }
                case "Penalty":{
                    tr = penaltyToRow(event);
                    break;
                }
                case "OpponentPenalty":{
                    tr = opponentPenaltyToRow(event);
                    break;
                }
            }

            body.appendChild(tr);
        }

        table.appendChild(body);
    }
}

function parseCredentials(data){

    let teams = document.querySelector("#header-teams");
    let date = document.querySelector("#header-date");
    let score = document.querySelector("#header-score");
    let period = document.querySelector("#header-period");

    let timeStr = data.game.time.toString().substring(0, data.game.time.toString().length-3);
    let periodScoreStr = [];
    let periodGoals = data.periodGoals;

    for(let i = 1; i<=Number(data.game.currentPeriod); i++){
        let period = periodGoals.find(({ period }) => Number(period) === i);
        periodScoreStr.push(`${period.goalsScored}:${period.goalsConceded}`)
    }


    teams.innerText = `${data.game.team.name} X ${data.game.opponent}`;
    date.innerText = `${dateToString(data.game.date)}, ${timeStr}`;
    score.innerText = `${data.goalsScored}:${data.goalsConceded} (${periodScoreStr.join(", ")})`;
    period.innerText = `${getPeriodWithName(data.game.currentPeriod)}`;
}

function parseShots(data){
    let shots = data.shots.find(({ period }) => Number(period) === Number(data.game.currentPeriod));

    document.querySelector("#shots-header").innerText = `Střely na branku v ${getPeriodWithShortName(data.game.currentPeriod)}`

    document.querySelector("#shots").innerText = shots.shots;

}

function parseSaves(data){
    let activeGoalkeeper = findActiveGoalkeeper();
    if(activeGoalkeeper){
        let saves = data.savesRecords.find(({
                                                period,
                                                goalkeeper
                                            }) => Number(period) === Number(data.game.currentPeriod) && goalkeeper.id == activeGoalkeeper.id);

        document.querySelector("#saves-header").innerText = `Zásahy brankáře v ${getPeriodWithShortName(data.game.currentPeriod)}`;
        document.querySelector("#saves-header-name").innerText = `#${saves.goalkeeper.gameNumber} ${saves.goalkeeper.player.name} ${saves.goalkeeper.player.surname}`;

        document.querySelector("#saves").innerText = saves.saves;
        document.querySelector("#saves").dataset.id = saves.goalkeeper.id;
        document.querySelector("#saves-table").style.visibility = "unset";
    } else {
        document.querySelector("#saves-table").style.visibility = "hidden";
    }
}

function parseButtons(data){
    let previousButton = document.querySelector("#previous-period");
    let nextButton = document.querySelector("#next-period");

    let roster = data.roster;

    let goalkeepers = roster.filter(player =>
        player.player?.position === "GK"
    );

    let goalkeeperChangeButton = document.querySelector("#goalkeeper-change-button");

    goalkeeperChangeButton.disabled = goalkeepers.length < 2;

    if(Number(data.game.currentPeriod)=== Number(data.game.periods)){
        nextButton.innerText = "Přejít na prodloužení";
    } else {
        nextButton.innerText = `Další ${getPeriodName(data.game.periods)}`;
    }

    previousButton.innerText = `Předchozí ${getPeriodName(data.game.periods)}`;



    if(Number(data.game.currentPeriod) ===1){
        previousButton.style.visibility = "hidden";
    } else {
        previousButton.style.visibility = "unset";
    }

    if(Number(data.game.currentPeriod) >Number(data.game.periods)){
        nextButton.style.visibility = "hidden";
    } else {
        nextButton.style.visibility = "unset";
    }
}


function parsePlayerStats(data){
    let gkHead = document.querySelector("#goalie-stats-head");
    let gkBody = document.querySelector("#goalie-stats-body");
    let playersBody = document.querySelector("#player-stats-body");
    let periods = loadFromSessionStorage("game").periods;
    let savesHeader = gkHead.querySelector("#stats-saves-header");
    savesHeader.colSpan = periods+2;
    let gkHeadRow = gkHead.querySelector('tr:nth-child(2)');
    gkHeadRow.innerHTML = `<th>Číslo</th><th>Jméno</th>`;

    for(let i = 1; i<=periods;i++){
        gkHeadRow.innerHTML += `<th>${i}</th>`;
    }
        gkHeadRow.innerHTML += `<th>PR</th><th>Celkem</th><th>OG</th><th>%</th>`;


    gkBody.innerHTML = "";
    playersBody.innerHTML = "";

    for(let goalkeeperStats of data.goalkeepers){
        let row = document.createElement('tr');

        let numberCell = document.createElement('td');
        numberCell.innerText = goalkeeperStats.player.gameNumber;
        numberCell.classList.add("stats-player-number");
        row.appendChild(numberCell);

        let nameCell = document.createElement('td');
        nameCell.innerText = `${goalkeeperStats.player.player.name} ${goalkeeperStats.player.player.surname}`;
        nameCell.classList.add("stats-player-name");
        row.appendChild(nameCell);


        for(let i = 1; i<=periods+1;i++){
            let saves = goalkeeperStats.periodSaves.find(({period}) => Number(period) === i);
            let savesCell = document.createElement('td');
            savesCell.innerText = saves?.saves??'';
            row.appendChild(savesCell);
        }

        let totalSavesCell = document.createElement('td');
        totalSavesCell.innerText = goalkeeperStats?.saves??'';
        row.appendChild(totalSavesCell);

        let goalsCell = document.createElement('td');
        goalsCell.innerText = goalkeeperStats?.goals??'';
        row.appendChild(goalsCell);

        let percentageCell = document.createElement('td');
        percentageCell.innerText = goalkeeperStats?.percentage??'---';
        row.appendChild(percentageCell);

        gkBody.appendChild(row);
    }

    for(let playerStats of data.players){
        let row = document.createElement('tr');

        let numberCell = document.createElement('td');
        numberCell.innerText = playerStats.player.gameNumber;
        numberCell.classList.add("stats-player-number");
        row.appendChild(numberCell);

        let nameCell = document.createElement('td');
        nameCell.innerText = `${playerStats.player.player.name} ${playerStats.player.player.surname}`;
        nameCell.classList.add("stats-player-name");
        row.appendChild(nameCell);


        let goalsCell = document.createElement('td');
        goalsCell.innerText = playerStats?.goals??'';
        row.appendChild(goalsCell);
        
        let assistsCell = document.createElement('td');
        assistsCell.innerText = playerStats?.assists??'';
        row.appendChild(assistsCell);  
        
        let pointsCell = document.createElement('td');
        let points = playerStats?.goals+playerStats?.assists;
        pointsCell.innerText = points;
        row.appendChild(pointsCell);

        let plusMinusCell = document.createElement('td');
        let plusMinus = playerStats?.plus-playerStats?.minus;
        plusMinusCell.innerText = plusMinus;
        row.appendChild(plusMinusCell);

        let penaltyMinutesCell = document.createElement('td');
        penaltyMinutesCell.innerText = playerStats?.penaltyMinutes??'';
        row.appendChild(penaltyMinutesCell);

        playersBody.appendChild(row);
    }
}




// === Vložení eventů ===

function scoredGoalToRow(event){
    let tr = document.createElement('tr');
    tr.onclick = () =>openScoredGoalEdit(event.id);
    tr.dataset.id = event.id;

    let timeCell = document.createElement('td');
    timeCell.innerText = secondsToGameTime(event.time);

    let eventTypeCell = document.createElement('td');
    eventTypeCell.innerText = "Vstřelený gól";

    const scorer = `${event.scorer?.gameNumber ?? "--"}`;
    const assists = (event.assists || []).map(r => `${r.gameNumber}`).join(', ');
    const onIce = (event.onIce || []).map(r => `${r.gameNumber}`).join(', ');
    const situation = event.situation ?? "";

    let detailCell = document.createElement('td');
    detailCell.innerHTML = `Střelec: ${scorer} | Asistence: ${assists}<br>Na ledě: ${onIce} | Situace: ${situation}`;


    tr.appendChild(timeCell);
    tr.appendChild(eventTypeCell);
    tr.appendChild(detailCell);

    return tr;
}

function concededGoalToRow(event){
    let tr = document.createElement('tr');
    tr.onclick = ()=> openConcededGoalEdit(event.id);
    tr.dataset.id = event.id;

    let timeCell = document.createElement('td');
    timeCell.innerText = secondsToGameTime(event.time);

    let eventTypeCell = document.createElement('td');
    eventTypeCell.innerText = "Obdržený gól";

    const onIce = (event.onIce || []).map(r => `${r.gameNumber}`).join(', ');
    const situation = event.situation ?? "";

    let detailCell = document.createElement('td');
    detailCell.innerHTML = `Na ledě: ${onIce} | Situace: ${situation}`;


    tr.appendChild(timeCell);
    tr.appendChild(eventTypeCell);
    tr.appendChild(detailCell);

    return tr;
}

function penaltyToRow(event){
    let tr = document.createElement('tr');
    tr.onclick = ()=> openPenaltyEdit(event.id);
    tr.dataset.id = event.id;


    let timeCell = document.createElement('td');
    timeCell.innerText = secondsToGameTime(event.time);

    let eventTypeCell = document.createElement('td');
    eventTypeCell.innerText = "Vyloučení";

    const player = `${event.player?.gameNumber ?? "--"}`;
    const minutes = penaltyMinutesToText(event.minutes);
    const reason = event.reason ?? "---";

    let detailCell = document.createElement('td');
    detailCell.innerHTML = `Hráč: ${player} | Trest: ${minutes}<br>Důvod: ${reason}`;


    tr.appendChild(timeCell);
    tr.appendChild(eventTypeCell);
    tr.appendChild(detailCell);

    return tr;
}

function opponentPenaltyToRow(event){
    let tr = document.createElement('tr');
    tr.onclick = () => openOpponentPenaltyEdit(event.id);
    tr.dataset.id = event.id;


    let timeCell = document.createElement('td');
    timeCell.innerText = secondsToGameTime(event.time);

    let eventTypeCell = document.createElement('td');
    eventTypeCell.innerText = "Vyloučení soupeře";

    const minutes = penaltyMinutesToText(event.minutes);

    let detailCell = document.createElement('td');
    detailCell.innerHTML = `Trest: ${minutes}`;


    tr.appendChild(timeCell);
    tr.appendChild(eventTypeCell);
    tr.appendChild(detailCell);

    return tr;
}

function rosterToButtons (modal){
    let roster = loadFromSessionStorage("roster");
    let rosterContainer = modal.querySelector(".roster");
    rosterContainer.innerHTML = "";

    for (let player of roster){
        let button = document.createElement("button");
        button.dataset.number = player.gameNumber;
        button.dataset.id = player.id;
        button.innerHTML = `${player.gameNumber}<br>${player.player.surname}`;
        button.onclick = ()=> playerToInput(button);
        rosterContainer.appendChild(button);
    }

}

function playerToInput (button){
    let modal = button.closest(".modal");

    let activeInput = modal.querySelector("input.active-input");
    activeInput.value = button.dataset.number;
    activeInput.dispatchEvent(new Event("input", { bubbles: true }));

    findInRoster(activeInput);

    activeInput.classList.remove("active-input");
    let rosterContainer = modal.querySelector(".roster");
    rosterContainer.style.display = "none";

    if(activeInput.id === "goal" && modal.querySelector("#scored-goal-onIce1").value ===""){
        modal.querySelector("#scored-goal-onIce1").value = button.dataset.number;
        findInRoster(modal.querySelector("#scored-goal-onIce1"));
    }

    if(activeInput.id === "assist1" && modal.querySelector("#scored-goal-onIce2").value ===""){
        modal.querySelector("#scored-goal-onIce2").value = button.dataset.number;
        findInRoster(modal.querySelector("#scored-goal-onIce2"));
    }

    if(activeInput.id === "assist2" && modal.querySelector("#scored-goal-onIce3").value ===""){
        modal.querySelector("#scored-goal-onIce3").value = button.dataset.number;
        findInRoster(modal.querySelector("#scored-goal-onIce3"));
    }

    let inputs = modal.querySelectorAll(".player-inputs input[type=number]");
    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i] === activeInput) {
            if (i + 1 < inputs.length) {
                activateInput(inputs[i + 1]);
            }
            break;
        }
    }

}

document.querySelectorAll(".player-inputs input, input.player-inputs").forEach(input =>{
   input.addEventListener("focus", (ev)=>{
       activateInput(input);

   });

});

document.addEventListener("click", (event) => {
    // Najdi aktivní input
    const activeInput = document.querySelector("input.active-input");
    if (!activeInput) return; // Pokud žádný není, nic nedělej

    const modal = activeInput.closest(".modal");
    const rosterContainer = modal.querySelector(".roster");

    // Pokud klik byl uvnitř aktivního inputu nebo uvnitř rosteru, nic nedělej
    if (activeInput.contains(event.target) || rosterContainer.contains(event.target)) {
        return;
    }

    // Jinak deaktivuj input a skryj roster
    activeInput.classList.remove("active-input");
    rosterContainer.style.display = "none";
});

function activateInput(input){
    let modal = input.closest(".modal");
    let rosterContainer = modal.querySelector(".roster");

    modal.querySelectorAll(".player-inputs input, input.player-inputs").forEach(input=>{
        input.classList.remove("active-input");
    });

    input.classList.add("active-input");
    rosterContainer.style.display = "unset";
}