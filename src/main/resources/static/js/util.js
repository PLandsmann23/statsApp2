async function handleFetchError(response) {
    return response.json().then(err => {
        const message = err.message || "Nastala chyba";
        showMessage(message,"error");
        throw new Error(message);
    });
}


function secondsToGameTime(timeInSeconds){
    let minutes = Math.floor(timeInSeconds /60);
    let seconds = timeInSeconds - (minutes*60);
    if(seconds<10){
        seconds = "0"+seconds;
    }

    if(minutes<10){
        minutes = "0"+minutes;
    }
    return minutes+":"+seconds;
}

function gameTimeToSeconds(input) {
    if (!input) return 0;

    // Odeber všechny nepovolené znaky kromě číslic a dvojtečky
    input = input.trim();

    if (input.includes(":")) {
        // Formát s dvojtečkou: mm:ss nebo mmm:ss
        const [minutes, seconds] = input.split(":").map(Number);
        return (isNaN(minutes) ? 0 : minutes * 60) + (isNaN(seconds) ? 0 : seconds);
    } else {
        // Formát bez dvojtečky: mmss nebo mmmss (např. 945 -> 9:45)
        const digits = input.replace(/\D/g, '');

        if (digits.length <= 2) {
            // Např. "5" nebo "45" = čistě vteřiny
            return parseInt(digits, 10);
        } else {
            // Poslední dvě čísla = sekundy, zbytek minuty
            const seconds = parseInt(digits.slice(-2), 10);
            const minutes = parseInt(digits.slice(0, -2), 10);
            return (isNaN(minutes) ? 0 : minutes * 60) + (isNaN(seconds) ? 0 : seconds);
        }
    }
}

function setSelectValue(selectElement, value) {
    if (!selectElement) return;

    const optionExists = Array.from(selectElement.options).some(opt => opt.value === value);

    if (optionExists) {
        selectElement.value = value;
    }
}

function saveToSessionStorage(key, value){
    sessionStorage.setItem(key,JSON.stringify(value));
}

function loadFromSessionStorage(key){
    const item = sessionStorage.getItem(key);
    return item ? JSON.parse(item) : null;}

function getPeriodName(periods){
    let names = {
        2: "poločas",
        3: "třetina",
        4: "čtvrtina",
        5: "perioda"
    }

    if(periods<5 && periods >1){
        return names[periods];
    } else {
        return names[5];
    }

}

function getPeriodShortName(periods){
    let names = {
        2: "pol.",
        3: "tř.",
        4: "čt.",
        5: "per."
    }

    if(periods<5 && periods >1){
        return names[periods];
    } else {
        return names[5];
    }

}

function getPeriodWithName(currentPeriod) {
    const settings = loadFromSessionStorage("game");
    const basePeriods = settings.periods;

    if (currentPeriod === 0) {
        return "Před zápasem";
    }



    if (currentPeriod % 1 === 0 && currentPeriod <= basePeriods) {
        return currentPeriod + ". " + getPeriodName(basePeriods);
    }

    // index za základními periodami
    let overtimeIndex = basePeriods + 1;

    if (currentPeriod === overtimeIndex) {
        return "Prodloužení";
    }


}


function getPeriodWithShortName(currentPeriod) {
    const settings = loadFromSessionStorage("game");
    const basePeriods = settings.periods;

    if (currentPeriod === 0) {
        return "Před zápasem";
    }



    if (currentPeriod % 1 === 0 && currentPeriod <= basePeriods) {
        return currentPeriod + ". " + getPeriodShortName(basePeriods);
    }

    // index za základními periodami
    let overtimeIndex = basePeriods + 1;

    if (currentPeriod === overtimeIndex) {
        return "PR";
    }


}

function scoreboardToReal(input){
    const regex = /^(?:\d{2}:\d{2}|\d{4})$/;

    let settings = loadFromSessionStorage("game");
    let modal = input.closest(".modal");
    let realTimeInput = modal.querySelector("[name=game-time]");
    let period = modal.querySelector("[name=period]").value;
    period = Number.parseInt(period);
    let endTime;
    realTimeInput.value ="";
    realTimeInput.dataset.seconds ="";

    if(regex.test(input.value)){
        let scoreboardInSeconds = gameTimeToSeconds(input.value);
        input.classList.remove("error-input");

        if(period<=settings.periods){
            endTime = period*settings.periodLength*60;
        } else{
            return;
        }


        realTimeInput.value= (secondsToGameTime(endTime - scoreboardInSeconds));
        realTimeInput.dataset.seconds = endTime - scoreboardInSeconds;
    } else {
        input.classList.add("error-input");
    }
}

function realToScoreboard(input){
    const regex = /^(?:\d{2}:\d{2}|\d{4})$/;

    let settings = loadFromSessionStorage("game");
    let modal = input.closest(".modal");
    let scoreboardTimeInput = modal.querySelector("[name=scoreboard-time]");
    let period = modal.querySelector("[name=period]").value;
    period = Number.parseInt(period);
    let endTime;
    scoreboardTimeInput.value ="";

    if(regex.test(input.value)){
        let realTimeInSeconds = gameTimeToSeconds(input.value);
        input.classList.remove("error-input");

        if(period<=settings.periods){
            endTime = period*settings.periodLength*60;
        }  else{
            return;
        }


        scoreboardTimeInput.value= (secondsToGameTime(endTime - realTimeInSeconds));
        input.dataset.seconds = realTimeInSeconds;
    } else {
        input.classList.add("error-input");
    }
}

function findInRoster(input){
    let modal = input.closest(".modal");


    let playerNo = Number.parseInt(input.value);
    let idInput = modal.querySelector("#"+input.id+"-id");

    if (input.value !== "") {
        try {
            idInput.value = findPlayerId(playerNo);
            input.classList.remove("error-input"); // pokud se najde, odstraníme případnou starou chybu
        } catch (e) {
            idInput.value = ""; // když nenajdeme, vynulujeme ID
            input.classList.add("error-input");

        }
    } else {
        // Pokud je input prázdný → vždy smažeme chybu + vyčistíme ID
        input.classList.remove("error-input");
        idInput.value = "";

    }

}

function findPlayerId(gameNumber){
    let roster = loadFromSessionStorage("roster");
    for(let player of roster){
        if(player.gameNumber === gameNumber){
            return player.id;
        }
    }

    throw new Error("Player not found");
}

function findPlayerNumber(id){
    let roster = loadFromSessionStorage("roster");
    for(let player of roster){
        if(player.id == id){
            return player.gameNumber;
        }
    }

    throw new Error("Player not found");
}


function timeToAttribute(input){
    const regex = /^(?:\d{2}:\d{2}|\d{4})$/;

    if(regex.test(input.value)) {
        let realTimeInSeconds = gameTimeToSeconds(input.value);
        input.classList.remove("error-input");
        input.dataset.seconds = realTimeInSeconds;

    } else {
        input.classList.add("error-input");
        input.dataset.seconds = null;

    }


}

function getPeriodFromRealTime(timeInSeconds){
    let settings = loadFromSessionStorage("game");
    let periods = settings.periods;
    let periodLength = settings.periodLength*60;
    let fullGameLength = periods*periodLength;

    if(timeInSeconds==0){
        return 1;
    }

    if(timeInSeconds<=fullGameLength){
        return Math.ceil(timeInSeconds/periodLength);
    } else {
        return periods+1;
    }
}

function getSelectedRadioValue(name) {
    const radios = document.querySelectorAll(`input[name="${name}"]`);
    for (const radio of radios) {
        if (radio.checked) {
            return radio;
        }
    }
    return null;
}

function dateToString(string){
    let date = new Date(string).toLocaleDateString("cs-CZ", {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });

    return date;
}

function findActiveGoalkeeper() {
    let roster = loadFromSessionStorage("roster");

    for (let rosterPlayer of roster) {
        if (rosterPlayer.activeGk) {
            return rosterPlayer;
        }
    }

    return null;
}

function penaltyMinutesToText(minutes) {
    const mapping = {
        2: "2 min",
        4: "2+2 min",
        12: "2+10 min",
        10: "10 min",
        5: "5 min",
        25: "5 min + OK",
        20: "OK",
        0: "TS"
    };

    return mapping[minutes] ?? `${minutes} min`;
}

function openMenu(){
    if(window.innerWidth < 1440){
        document.querySelector(".menu").style.display = "flex";
    }
}

function closeMenu(){
    if(window.innerWidth < 1440){
        document.querySelector(".menu").style.display = "none";
    }
}

function showSkeleton(containerSelector) {
    const box = document.querySelector(containerSelector);
    if (!box) return;

    box.classList.add("position-relative");

    const skeleton = document.createElement("div");
    skeleton.classList.add("skeleton-overlay");
    skeleton.dataset.skeleton = "true"; // pro pozdější selekci a odstranění

    box.appendChild(skeleton);
}

function hideSkeleton(containerSelector) {
    const box = document.querySelector(containerSelector);
    if (!box) return;

    const skeleton = box.querySelector(".skeleton-overlay");
    if (skeleton) {
        skeleton.style.opacity = "0"; // Spustí fade-out animaci
        setTimeout(() => {
            skeleton.remove();
        }, 500); // Počkej, až skončí transition (0.3s)
    }
}
