function parsePlayers(data){
    const tableBody = document.querySelector('#not-in-roster tbody');

    tableBody.innerHTML ="";


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

        const rosterAnchor = document.createElement('button');
        rosterAnchor.onclick = () => newRoster(player.id) ;
        rosterAnchor.textContent = 'Přidat';
        actionCell.appendChild(rosterAnchor);

        row.appendChild(actionCell);



        tableBody.appendChild(row);
    });
}

function parseRoster(data) {
    const tableBody = document.querySelector('#roster tbody');
    tableBody.innerHTML = "";

    data.forEach(item => {
        const tr = document.createElement('tr');
        tr.dataset.id = item.id;

        // Sloupec: Číslo (input + edit button)
        const numberCell = document.createElement('td');
        const numberWrapper = document.createElement('div');

        const input = document.createElement('input');
        input.type = 'number';
        input.value = item.gameNumber ?? "";
        input.disabled = true;

        const editBtn = document.createElement('button');
        editBtn.classList.add('edit-roster');
        const editImg = document.createElement('img');
        editImg.src = '/svg/pencil.svg';
        editBtn.appendChild(editImg);
        editBtn.onclick = () => openRosterNumberEdit(editBtn);

        numberWrapper.appendChild(input);
        numberWrapper.appendChild(editBtn);
        numberCell.appendChild(numberWrapper);

        const nameCell = document.createElement('td');
        nameCell.textContent = `${item.player.name} ${item.player.surname}`;

        const positionCell = document.createElement('td');
        positionCell.textContent = typeof item.player.position === 'string' ? item.player.position : "--";

        const lineCell = document.createElement('td');
        const select = document.createElement('select');
        const options = [
            { value: 0, text: '--' },
            { value: 1, text: '1' },
            { value: 2, text: '2' },
            { value: 3, text: '3' },
            { value: 4, text: '4' },
            { value: 5, text: '5' }
        ];

        options.forEach(opt => {
            const option = document.createElement('option');
            option.value = opt.value;
            option.textContent = opt.text;
            select.appendChild(option);
        });

        select.disabled = true;
        setSelectValue(select, JSON.stringify(item.line));
        lineCell.appendChild(select);

        const deleteCell = document.createElement('td');
        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'Odebrat';
        deleteBtn.onclick = () => openRosterDeleteDialog(item.id);
        deleteCell.appendChild(deleteBtn);

        // Poskládat řádek
        tr.appendChild(numberCell);
        tr.appendChild(nameCell);
        tr.appendChild(positionCell);
        tr.appendChild(lineCell);
        tr.appendChild(deleteCell);

        tableBody.appendChild(tr);
    });
}

function parseGame(data){
    let gameInfo = document.querySelector("#game-info");

    gameInfo.innerHTML = `Zápas: ${data.team.name} &nbsp;X&nbsp;  ${data.opponent}`;

}



