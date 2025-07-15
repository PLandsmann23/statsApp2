document.addEventListener("DOMContentLoaded", ()=>{loadData();});


async function loadData(){
    // showSkeleton("#next-games");
    // showSkeleton("#my-teams");
    // showSkeleton("#summary");

    let response =  await fetch(`${API_URL}info`);

    if(response.ok){
      let data = await response.json();

      parseData(data);

    } else {
        await handleFetchError(response);
    }
    
    hideSkeleton("#next-games");
    hideSkeleton("#my-teams");
    hideSkeleton("#summary");
}
