document.addEventListener("DOMContentLoaded", ()=>{loadData();});


async function loadData(){

    let response =  await fetch(`${API_URL}games/${getIdFromUrl()}`);

    if(response.ok){
        let data = await response.json();

        saveToSessionStorage("roster",data.roster);
        saveToSessionStorage("game",data.game);
        parseEvents(data);
        parseCredentials(data);
        parseShots(data);
        parseSaves(data);
        parseButtons(data);
        hideSkeleton(".shots-div");
        hideSkeleton(".saves-div");
        hideSkeleton(".game-header");
        hideSkeleton(".events-box");


    } else {
        if(response.status === 409){
            let data = await response.json();
            parseSelectGoalie(data);
        } else {
            await handleFetchError(response);
        }
    }

}

async function loadPlayerStats(){

    let response =  await fetch(`${API_URL}games/${getIdFromUrl()}/playerStats`);

    if(response.ok){
        let data = await response.json();

        parsePlayerStats(data);
        openModal("player-stats");

    } else {
            await handleFetchError(response);

    }

}