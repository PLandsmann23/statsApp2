async function deleteEvent() {
        let dialog = document.querySelector("#delete-event");
        let id = dialog.querySelector("[name=delete-event-id]").value;


        const response = await fetch(`${API_URL}events/${id}`, {
            headers: { "Content-Type": "application/json" },
            method: "DELETE"
        });

        if (response.ok) {
            showMessage("Událost smazána", "success");

                closeDialog(dialog, null);
                await loadData();

        } else {

        }


}