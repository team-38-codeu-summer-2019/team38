let message = document.getElementById("message-form");

if (message === "") {
    // Do nothing; consier showing a simple error to the user.
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

    // TODO e.g. <audio id = "audio-11">
    //           <audio id = "something" src = "">
    let elem = document.getElementById("audio-")

} catch (err) {
    throw new Error(`Unable to call the Text to Speech API: {e}`)
}