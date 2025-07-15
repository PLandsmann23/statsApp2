async function editSaves(addRemove){
    let currentPeriod = loadFromSessionStorage("game").currentPeriod;
    let savesCount = document.querySelector("#saves");

    if(Number(savesCount.innerText)+addRemove<0){
        return;
    }

    let saves = {
        goalkeeper: savesCount.dataset.id,
        saves: Number(savesCount.innerText)+addRemove,
    }

    const response = await fetch(`${API_URL}games/${getIdFromUrl()}/saves/${currentPeriod}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(saves)
    });

    if (response.ok) {
        let data = await response.json();

        savesCount.dataset.id = data.goalkeeper.id;
        savesCount.innerText = data.saves;


    } else {
        await handleFetchError(response);

    }
}