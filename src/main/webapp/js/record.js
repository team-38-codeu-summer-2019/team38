var record = document.getElementById('record-stt');
var stop = document.getElementById('stop-stt');
var submit = document.getElementById('submit-stt');

if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    console.log('getUserMedia supported.');
    var blob = new Blob();
    navigator.mediaDevices.getUserMedia (
        // constraints - only audio needed for this app
        {
            audio: true
        })

    // Success callback
        .then(function(stream) {
            var mediaRecorder = new MediaRecorder(stream);
            record.onclick = function() {
                mediaRecorder.start();
                console.log(mediaRecorder.state);
                console.log("recorder started");
                record.style.background = "red";
                record.style.color = "black";
            }

            var chunks = [];
            mediaRecorder.ondataavailable = function(e) {
                chunks.push(e.data);
            }

            stop.onclick = function() {
                mediaRecorder.stop();
                console.log(mediaRecorder.state);
                console.log("recorder stopped");
                record.style.background = "";
                record.style.color = "";
            }

            mediaRecorder.onstop = function(e) {
                console.log("recorder stopped");

                var audio = document.getElementById('audio-stt');

                audio.setAttribute('controls', '');

                blob = new Blob(chunks, { 'type' : 'audio/webm; codecs=opus' });
                chunks = [];
                var audioURL = window.URL.createObjectURL(blob);
                audio.src = audioURL;
            }
        })

        // Error callback
        .catch(function(err) {
                console.log('The following getUserMedia error occured: ' + err);
            }
        );

    submit.onclick = async function(){
        var oReq = new XMLHttpRequest();
        oReq.open("POST", "/a11y/stt", true);
        oReq.onload = function (oEvent) {
            // Uploaded.
        };
        oReq.send(blob);
        oReq.onreadystatechange = function () {
            if(oReq.readyState === 4 && oReq.status === 200) {
                var result = document.getElementById('output-stt');
                result.value = oReq.response;
            }
        };
    }
} else {
    console.log('getUserMedia not supported on your browser!');
}