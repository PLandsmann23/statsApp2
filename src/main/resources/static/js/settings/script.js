document.addEventListener("DOMContentLoaded",()=>{
    let settings = loadFromSessionStorage("userSettings");

    let newLength = document.querySelector('#defaultPeriodLength');
    let newPeriods = document.querySelector('#defaultPeriods');

    newLength.value = settings.periodLength;
    newPeriods.value = settings.periods;

});

function validate(){
    let newPass = document.querySelector('#new-password');
    let newPassRepeat = document.querySelector('#new-password-repeat');
    let newPassError = document.querySelector('#e-new-password');
    let newPassRepeatError = document.querySelector('#e-new-password-repeat');
    let submit = document.querySelector("#change-pass-button");

    if(newPass.value.length > 0){
        newPass.classList.remove("error-input");
        newPassError.innerText = "";
    } else {
        newPass.classList.add("error-input");
        newPassError.innerText = "Heslo nesmí být prázdné";
    }

    if(newPass.value === newPassRepeat.value){
        newPassRepeat.classList.remove("error-input");
        newPassRepeatError.innerText = "";
    } else {
        newPassRepeat.classList.add("error-input");
        newPassRepeatError.innerText = "Hesla se neshodují";
    }

    if(newPass.value.length > 0 && newPass.value === newPassRepeat.value){
        submit.disabled = false;

    }   else {
        submit.disabled = true;
    }
}

function validateSettings(){
    let newLength = document.querySelector('#defaultPeriodLength');
    let newPeriods = document.querySelector('#defaultPeriods');
    let newLengthError = document.querySelector('#e-defaultPeriodLength');
    let newPeriodsError = document.querySelector('#e-defaultPeriods');
    let submit = document.querySelector("#change-settings-button");

    if(newLength.value.length > 0){
        newLength.classList.remove("error-input");
        newLengthError.innerText = "";
    } else {
        newLength.classList.add("error-input");
        newLengthError.innerText = "Délka nesmí být prázdná";
    }

    if(newPeriods.value.length > 0){
        newPeriods.classList.remove("error-input");
        newPeriodsError.innerText = "";
    } else {
        newPeriods.classList.add("error-input");
        newPeriodsError.innerText = "Délka nesmí být prázdná";
    }



    if(newLength.value.length > 0 && newPeriods.value.length > 0){
        submit.disabled = false;

    }   else {
        submit.disabled = true;
    }
}