document.addEventListener("DOMContentLoaded", loadData);

async function loadData(){
    fetch(API_URL +"games/"+getIdFromUrl()+"/roster")
        .then(response => response.json())
        .then((data)=>{
            parsePlayers(data.notInRoster);
            parseRoster(data.roster);
            parseGame(data.game);
            saveToSessionStorage("roster", data.roster);
            hideSkeleton(".header-game");
            hideSkeleton(".roster-grid");
        });
}