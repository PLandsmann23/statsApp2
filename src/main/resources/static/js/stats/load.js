document.addEventListener("DOMContentLoaded", ()=>{loadData();});


async function loadData(){
    const response = await fetch("/api/teams");

    if(response.ok){
        let data = await response.json()

        let select = document.querySelector("#team");

        teamsToSelect(select, data);


    } else {
        await handleFetchError(response);
    }

}


async function loadStats(){
    let team = document.querySelector('#team');

    if(team.value===""){
        return;
    }

    let range = document.querySelector('#range');



    let params = new URLSearchParams([["range", range.value]]);

    const response = await fetch(`/api/stats/${team.value}${params.size>0?'?'+params.toString():''}`);


    if(response.ok){
        let data = await response.json()

        parseTeamStats(data.teamStats);
        parsePlayerStats(data);


    } else {
        await handleFetchError(response);
    }
}