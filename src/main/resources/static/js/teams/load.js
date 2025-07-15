document.addEventListener("DOMContentLoaded", ()=>{loadData();});


async function loadData(){

    let response =  await fetch(`${API_URL}teams`);

    if(response.ok){
        let data = await response.json();

        parseTeams(data);

        hideSkeleton(".my-teams");

    } else {
        await handleFetchError(response);
    }

}