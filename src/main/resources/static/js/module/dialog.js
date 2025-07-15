function openDialog(dialogId){
    let dialog = document.getElementById(dialogId);

    dialog.style.display= "block";
    setTimeout(() => {
        dialog.style.opacity=1;
    }, 10);
}

function closeDialog(dialog, dialogId){
    if(dialog==null){
        dialog = document.getElementById(dialogId);
    }

    dialog.querySelectorAll("input").forEach(el=>
        el.value = null
    )

    dialog.style.opacity=0;
    setTimeout(() => {

        dialog.style.display= "none";
    }, 200);
}