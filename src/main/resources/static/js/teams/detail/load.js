document.addEventListener("DOMContentLoaded", ()=>{loadData();});


async function loadData(){

    let response =  await fetch(`${API_URL}teams/${getIdFromUrl()}/detail`);

    if(response.ok){
        let data = await response.json();

        parseGames(data.games);
        parsePlayers(data.players);

        document.querySelector("#team-name").innerText = data.team.name;

        hideSkeleton("#games");
        hideSkeleton(".team-header");

    } else {
        await handleFetchError(response);
    }

}