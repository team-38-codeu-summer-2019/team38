async function f() {
    let message = document.getElementById("tts-input").value;

    if (message === "") {
        // Do nothing; consier showing a simple error to the user.
        window.alert("Enter message to be played")
    }

    try {
        let resp = await fetch("/a11y/tts", {
            method: "POST",
            body: message, // The message from the form
            headers: {
                "Content-Type": "text/plain"
            },
        })

        // Our audio is binary data - often called a "blob" - and
        // not text data.
        let audio = await resp.blob()
        window.AudioContext = window.AudioContext || window.webkitAudioContext;
        var audioContext = new AudioContext();

        let elem = document.getElementById("audio-")
        var blobURL = window.URL.createObjectURL(audio);
        elem.src = blobURL;
        elem.play()

    } catch (err) {
        throw new Error(`Unable to call the Text to Speech API: {e}`)
    }
}