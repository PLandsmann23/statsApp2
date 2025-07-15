function teamsToSelect(select, data){
    select.innerHTML = "";

    let option = document.createElement('option');
    option.innerText = "---";
    option.disabled = true;
    option.selected = true;
    select.appendChild(option);


    for(let team of data){

        let option = document.createElement('option');
        option.innerText = team.name;
        option.value = team.id;

        select.appendChild(option);
    }

}

function parseTeamStats(data) {
    const statsConfig = [
        { key: 'ppPercentage', selectorValue: '#pp-eff .stats-value', selectorCircle: '#pp-eff .stats-circle' },
        { key: 'shPercentage', selectorValue: '#sh-eff .stats-value', selectorCircle: '#sh-eff .stats-circle' },
        { key: 'shotEfficiency', selectorValue: '#shots-eff .stats-value', selectorCircle: '#shots-eff .stats-circle' },
        { key: 'saveEfficiency', selectorValue: '#saves-eff .stats-value', selectorCircle: '#saves-eff .stats-circle' },
    ];

    function setStatValueAndCircle(value, valueElem, circleElem) {
        if (value != null) {
            valueElem.innerText = value.toFixed(2) + ' %';
            circleElem.style.background = `conic-gradient(var(--violet) ${3.6 * value}deg, var(--lgrey) 0deg)`;
        } else {
            valueElem.innerText = '--';
            circleElem.style.background = ''; // případně nějaká default barva
        }
    }

    statsConfig.forEach(({ key, selectorValue, selectorCircle }) => {
        const value = data[key];
        const valueElem = document.querySelector(selectorValue);
        const circleElem = document.querySelector(selectorCircle);
        if (valueElem && circleElem) {
            setStatValueAndCircle(value, valueElem, circleElem);
        }
    });

    const avgScoredElem = document.querySelector('#avg-goals-scored');
    const avgConcededElem = document.querySelector('#avg-goals-conceded');
    const avgScoredLine = document.querySelector('.scored-bar');
    const avgConcededLine = document.querySelector('.conceded-bar');

    if (avgScoredElem) {
        avgScoredElem.innerText = data.avgScored != null ? data.avgScored.toFixed(2) : '--';
    }
    if (avgConcededElem) {
        avgConcededElem.innerText = data.avgConceded != null ? data.avgConceded.toFixed(2) : '--';
    }

    if (
        data.avgScored != null &&
        data.avgConceded != null &&
        (data.avgScored + data.avgConceded) > 0 &&
        avgScoredLine &&
        avgConcededLine
    ) {
        const total = data.avgScored + data.avgConceded;
        avgScoredLine.style.width = (data.avgScored / total * 100) + '%';
        avgConcededLine.style.width = (data.avgConceded / total * 100) + '%';
    }
}



function parsePlayerStats(data){
    document.querySelector(".stats-table").style.display = "block";


    let gkBody = document.querySelector("#goalie-stats-body");
    let playersBody = document.querySelector("#player-stats-body");

    gkBody.innerHTML = "";
    playersBody.innerHTML = "";

    for(let goalkeeperStats of data.goalkeeperStats){
        let row = document.createElement('tr');

        let numberCell = document.createElement('td');
        numberCell.innerText = goalkeeperStats.player.number;
        numberCell.classList.add("stats-player-number");
        row.appendChild(numberCell);

        let nameCell = document.createElement('td');
        nameCell.innerText = `${goalkeeperStats.player.name} ${goalkeeperStats.player.surname}`;
        nameCell.classList.add("stats-player-name");
        row.appendChild(nameCell);

        let gamesPlayedCell = document.createElement('td');
        gamesPlayedCell.textContent = goalkeeperStats.gamesPlayed;
        row.appendChild(gamesPlayedCell);

        let goalsConcededCell = document.createElement('td');
        goalsConcededCell.textContent = goalkeeperStats.goalsConceded;
        row.appendChild(goalsConcededCell);

        let savesCell = document.createElement('td');
        savesCell.textContent = goalkeeperStats.saves;
        row.appendChild(savesCell);


        let avgGoalsCell = document.createElement('td');
        avgGoalsCell.textContent = goalkeeperStats.avgGoals!=null? goalkeeperStats.avgGoals.toFixed(2) : "--";
        row.appendChild(avgGoalsCell);


        let percentageCell = document.createElement('td');
        percentageCell.textContent = goalkeeperStats.savePercentage!=null? (goalkeeperStats.savePercentage).toFixed(2) : "--";
        row.appendChild(percentageCell)

        gkBody.appendChild(row);
    }

    for(let player of data.playerStats){
        let row = document.createElement('tr');


        let numberCell = document.createElement('td');
        numberCell.textContent = player.player.number;
        row.appendChild(numberCell);

        let nameCell = document.createElement('td');
        nameCell.textContent = `${player.player.name} ${player.player.surname}`;
        row.appendChild(nameCell);

        let gpCell = document.createElement('td');
        gpCell.textContent = player.gamesPlayed;
        row.appendChild(gpCell);

        let goalsCell = document.createElement('td');
        goalsCell.textContent = player.goals;
        row.appendChild(goalsCell);

        let assistsCell = document.createElement('td');
        assistsCell.textContent = player.assists;
        row.appendChild(assistsCell);

        let pointsCell = document.createElement('td');
        pointsCell.textContent = player.goals + player.assists;
        row.appendChild(pointsCell);

        let plusMinusCell = document.createElement('td');
        plusMinusCell.textContent = player.plus-player.minus;
        row.appendChild(plusMinusCell);

        let penaltyCell = document.createElement('td');
        penaltyCell.textContent = player.penaltyMinutes?? "0";
        row.appendChild(penaltyCell);

        playersBody.appendChild(row);
    }

}