function parseData(data){

    let teams = document.querySelector("#teams-count");
    let games = document.querySelector("#games-count");
    let players = document.querySelector("#players-count");

    teams.innerText = data.noOfTeams;
    games.innerText = data.noOfGames;
    players.innerText = data.noOfPlayers;

    parseGames(data.nextGames);
    parseTeams(data.teams)


}


function parseGames(data){

    const tableBody = document.querySelector("#next-games table tbody");

    tableBody.innerHTML ="";


    data.forEach(game => {
        const row = document.createElement('tr');


        const teamCell = document.createElement('td');
        teamCell.textContent = game.team.name;
        row.appendChild(teamCell);

        const statusCell = document.createElement('td');
        statusCell.textContent = 'X';
        row.appendChild(statusCell);

        const opponentCell = document.createElement('td');
        opponentCell.textContent = game.opponent;
        row.appendChild(opponentCell);

        const metaCell = document.createElement('td');
        const metaTable = document.createElement('table');
        metaTable.classList.add('game-meta');

        const metaRow1 = document.createElement('tr');

        const dateCell = document.createElement('td');
        const dateIcon = document.createElement('img');
        dateIcon.src = '/svg/calendar.svg';
        dateCell.appendChild(dateIcon);
        dateCell.innerHTML += ` ${dateToString(game.date)}`;
        metaRow1.appendChild(dateCell);

        const timeCell = document.createElement('td');
        const timeIcon = document.createElement('img');
        timeIcon.src = '/svg/clock.svg';
        timeCell.appendChild(timeIcon);
        timeCell.innerHTML += ` ${game.time.substring(0,game.time.length-3)}`;
        metaRow1.appendChild(timeCell);

        metaTable.appendChild(metaRow1);

        const metaRow2 = document.createElement('tr');
        const locationCell = document.createElement('td');
        locationCell.colSpan = 2;

        const locationIcon = document.createElement('img');
        locationIcon.src = '/svg/place.svg';
        locationCell.appendChild(locationIcon);
        locationCell.innerHTML += `&nbsp; ${game.venue}`;

        metaRow2.appendChild(locationCell);
        metaTable.appendChild(metaRow2);

        metaCell.appendChild(metaTable);
        row.appendChild(metaCell);

        tableBody.appendChild(row);
    });
}

function parseTeams(teams){
    const tableBody = document.querySelector('#my-teams tbody');

    tableBody.innerHTML ="";


    teams.forEach(teamData => {
        const row = document.createElement('tr');

        row.dataset.id = teamData.id;

        const teamNameCell = document.createElement('td');
        teamNameCell.textContent = teamData.name;
        row.appendChild(teamNameCell);

        const playersCell = document.createElement('td');
        playersCell.textContent = teamData.noOfPlayers;
        row.appendChild(playersCell);

        const actionCell = document.createElement('td');

        const editIcon = document.createElement('img');
        editIcon.src = '/svg/pencil.svg';
        editIcon.onclick = () => openTeamEdit(teamData.id);
        actionCell.appendChild(editIcon);

        const deleteIcon = document.createElement('img');
        deleteIcon.src = '/svg/bin.svg';
        deleteIcon.onclick = () => openTeamDeleteDialog(teamData.id);
        actionCell.appendChild(deleteIcon);

        row.appendChild(actionCell);

        tableBody.appendChild(row);
    });
}

