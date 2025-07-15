$(function () {
    $("#tabs").tabs({
        activate: function (event, ui) {
            $('.tab-content').removeClass('active');
            $(ui.newPanel).addClass('active');
        }
    });

    $('.tab-content').first().addClass('active');
});

function parseGames(data) {
    const tableBody = document.querySelector('#games tbody');
    tableBody.innerHTML = "";

    data.forEach(game => {
        const row = document.createElement('tr');
        row.dataset.id = game.game.id;

        const opponentCell = document.createElement('td');
        opponentCell.textContent = game.game.opponent || "---";
        row.appendChild(opponentCell);

        const dateCell = document.createElement('td');
        dateCell.textContent = game.game.date ? dateToString(game.game.date) : "---";
        row.appendChild(dateCell);

        const timeCell = document.createElement('td');
        timeCell.textContent = game.game.time ? game.game.time.slice(0, -3) : "---";
        row.appendChild(timeCell);

        const venueCell = document.createElement('td');
        venueCell.textContent = game.game.venue || "---";
        row.appendChild(venueCell);

        const scoreCell = document.createElement('td');
        scoreCell.textContent = `${game.goalsScored ?? 0}:${game.goalsConceded ?? 0}`;
        row.appendChild(scoreCell);

        const actionCell = document.createElement('td');
        const editIcon = document.createElement('img');
        editIcon.src = '/svg/pencil.svg';
        editIcon.onclick = () => openGameEdit(game.game.id);
        actionCell.appendChild(editIcon);

        const deleteIcon = document.createElement('img');
        deleteIcon.src = '/svg/bin.svg';
        deleteIcon.onclick = () => openGameDeleteDialog(game.game.id);
        actionCell.appendChild(deleteIcon);

        row.appendChild(actionCell);

        const anchorCell = document.createElement('td');
        const teamAnchor = document.createElement('a');
        teamAnchor.href = '/games/' + game.game.id;
        teamAnchor.textContent = 'Přejít na zápas';
        anchorCell.appendChild(teamAnchor);
        row.appendChild(anchorCell);

        tableBody.appendChild(row);
    });
}

function parsePlayers(data) {
    const tableBody = document.querySelector('#players tbody');
    tableBody.innerHTML = "";

    data.forEach(player => {
        const row = document.createElement('tr');
        row.dataset.id = player.id;

        const numberCell = document.createElement('td');
        numberCell.textContent = player.number ?? "--";
        row.appendChild(numberCell);

        const nameCell = document.createElement('td');
        nameCell.textContent = `${player.name} ${player.surname}`;
        row.appendChild(nameCell);

        const positionCell = document.createElement('td');
        positionCell.textContent = typeof player.position === 'string' ? player.position : "--";
        row.appendChild(positionCell);

        const actionCell = document.createElement('td');
        const editIcon = document.createElement('img');
        editIcon.src = '/svg/pencil.svg';
        editIcon.onclick = () => openPlayerEdit(player.id);
        actionCell.appendChild(editIcon);

        const deleteIcon = document.createElement('img');
        deleteIcon.src = '/svg/bin.svg';
        deleteIcon.onclick = () => openPlayerDeleteDialog(player.id);
        actionCell.appendChild(deleteIcon);

        row.appendChild(actionCell);

        tableBody.appendChild(row);
    });
}
