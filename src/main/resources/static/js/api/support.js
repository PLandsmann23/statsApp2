async function sendSupportMessage(){
    let subject = document.querySelector('#subject');
    let message = document.querySelector('#message');



        const response = await fetch("/api/support/send", {headers: {"Content-Type": "application/json"},
            method: "POST",
            body: JSON.stringify({
                subject: subject.value,
                message: message.value
            })
        });

        if (response.ok) {
            showMessage("Zpráva byla odeslána", "success");
            subject.value = "";
            message.value = "";
        } else {
            await handleFetchError(response);
        }

}