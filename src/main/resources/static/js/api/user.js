async function changePassword(){

    let oldPasswordValue = document.querySelector("#old-password").value;
    let newPasswordValue = document.querySelector("#new-password").value;

    const response = await fetch("/api/users/changePassword", {headers: {"Content-Type": "application/json"},
        method: "POST",
        body: JSON.stringify({
            oldPassword: oldPasswordValue,
            newPassword: newPasswordValue
        })
    });

    if (response.ok) {
        showMessage("Heslo bylo změněno", "success");
    } else {
        await handleFetchError(response);
    }

}


async function changeSettings(){

    let newLength = document.querySelector('#defaultPeriodLength');
    let newPeriods = document.querySelector('#defaultPeriods');


    let body = {
        defaultPeriodLength: newLength.value,
        defaultPeriods: newPeriods.value
    }

        const response = await fetch("/api/users/settings", {headers: {"Content-Type": "application/json"},
            method: "POST",
            body: JSON.stringify(body)
        });

        if (response.ok) {
            showMessage("Nastavení bylo změněno", "success");
            saveToSessionStorage("userSettings", {periodLength: newLength.value, periods: newPeriods.value});
        } else {
            await handleFetchError(response);
        }

}