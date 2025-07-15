function parseTeams(data){
    const tableBody = document.querySelector('.my-teams tbody');

    tableBody.innerHTML ="";


    data.forEach(teamData => {
        const row = document.createElement('tr');

        row.dataset.id = teamData.id;

        const teamNameCell = document.createElement('td');
        teamNameCell.textContent = teamData.name;
        row.appendChild(teamNameCell);

        const playersCell = document.createElement('td');
        playersCell.textContent = teamData.noOfPlayers;
        row.appendChild(playersCell);

        const gamesCell = document.createElement('td');
        gamesCell.textContent = teamData.noOfGames;
        row.appendChild(gamesCell);

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

        const anchorCell = document.createElement('td');

        const teamAnchor = document.createElement('a');
        teamAnchor.href = '/teams/'+teamData.id;
        teamAnchor.textContent = 'Přejít na tým';
        anchorCell.appendChild(teamAnchor);

        row.appendChild(anchorCell);

        tableBody.appendChild(row);
    })
}