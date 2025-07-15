const API_URL = "/api/";

function getIdFromUrl() {
    const segments = window.location.pathname.split("/").filter(Boolean);

    for (let i = 0; i < segments.length - 1; i++) {
        if (!isNaN(segments[i + 1])) {
            return segments[i + 1];
        }
    }

    return null;
}
