package com.google.codeu.servlets;

import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/** Takes request that contain text and responds with an MP3 file speaking that text. */
@WebServlet("/a11y/tts")
public class TextToSpeech extends HttpServlet {

    private TextToSpeechClient ttsClient;

    @Override
    public void init() {
        ttsClient = TextToSpeechClient.create();
    }

    /** Responds with an MP3 bytestream from the Google Cloud Text-to-Speech API */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String text = Jsoup.clean(request.getParameter("text"), Whitelist.none());

        // Text to Speech API
        SynthesisInput input = SynthesisInput.newBuilder()
                .setText(text)
                .build();

        // TODO

        response.setContentType("audio/mpeg");

        try (
                ServletOutputStream output = response.getOutputStream();
                // Placeholder
                InputStream input2 = getServletContext().getResourceAsStream(responseFromAPI);
        ) {
            byte[] buffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = input2.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }
}
