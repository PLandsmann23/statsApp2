async function editShots(addRemove){
    let currentPeriod = loadFromSessionStorage("game").currentPeriod;
    let shotsCount = document.querySelector("#shots");

    if(Number(shotsCount.innerText)+addRemove<0){
        return;
    }

    let shots = {
        shots: Number(shotsCount.innerText)+addRemove,
    }

    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/shots/${currentPeriod}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(shots)
    });

    if (response.ok) {
        let data = await response.json();
        shotsCount.innerText = data.shots;


    } else {
        await handleFetchError(response);

    }
}