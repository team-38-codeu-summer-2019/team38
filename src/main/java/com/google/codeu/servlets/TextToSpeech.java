package com.google.codeu.servlets;

import com.google.cloud.texttospeech.v1.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.stream.Collectors;

/** Takes request that contain text and responds with an MP3 file speaking that text. */
@WebServlet("/a11y/tts")
public class TextToSpeech extends HttpServlet {

    private TextToSpeechClient ttsClient;

    @Override
    public void init() {
        try {
            ttsClient = TextToSpeechClient.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Responds with an MP3 bytestream from the Google Cloud Text-to-Speech API */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String text = Jsoup.clean(request.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator())), Whitelist.none());

        // Text to Speech API
        SynthesisInput input = SynthesisInput.newBuilder()
                .setText(text)
                .build();

        // Build the voice request, select the language code ("en-US") and the ssml voice gender
        VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                .setLanguageCode("en-US")
                // Try experimenting with the different voices
                .setSsmlGender(SsmlVoiceGender.FEMALE)
                .build();

        // Select the type of audio file you want returned
        AudioConfig audioConfig = AudioConfig.newBuilder()
                .setAudioEncoding(AudioEncoding.MP3)
                .build();

        // Perform the text-to-speech request on the text input with the selected voice parameters and
        // audio file type
        SynthesizeSpeechResponse respFromAPI = ttsClient.synthesizeSpeech(input, voice, audioConfig);

        // Get the audio contents from the response
        String filePath = getServletContext().getRealPath("/WEB-INF") + "/out.mp3";
        OutputStream out = new FileOutputStream(filePath);
        out.write(respFromAPI.getAudioContent().toByteArray());

        response.setContentType("audio/mpeg");

        try (
                ServletOutputStream output = response.getOutputStream();
                InputStream input2 = getServletContext().getResourceAsStream("/WEB-INF/out.mp3")
        ) {
            byte[] buffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = input2.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }
}
