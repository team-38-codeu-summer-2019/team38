package com.google.codeu.servlets;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import ws.schild.jave.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@WebServlet("/a11y/stt")
public class SpeechToText extends HttpServlet {

    private SpeechClient sttClient;

    @Override
    public void init() {
        try {
            sttClient = SpeechClient.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        FileOutputStream os = new FileOutputStream("test.webm");

        byte[] buffer = new byte[1024];
        int len;

        // read bytes from the input stream and store them in buffer
        InputStream in = request.getInputStream();
        while ((len = in.read(buffer)) != -1) {
            // write bytes from the buffer into output stream
            os.write(buffer, 0, len);
        }

        File source = new File("test.webm");
        File target = new File("test.flac");
        AudioAttributes audioattrs = new AudioAttributes();
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setAudioAttributes(audioattrs);
        audioattrs.setCodec("flac");
        audioattrs.setSamplingRate(new Integer(48000));
        audioattrs.setChannels(new Integer(1));
        attrs.setFormat("flac");
        Encoder instance = new Encoder();
        try {
            instance.encode(new MultimediaObject(source), target, attrs, null);
        } catch (EncoderException e) {
            e.printStackTrace();
        }

        Path path = Paths.get("test.flac");
        byte[] data = Files.readAllBytes(path);
        ByteString audioBytes = ByteString.copyFrom(data);

        // Builds the sync recognize request
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                .setSampleRateHertz(48000)
                .setAudioChannelCount(1)
                .setLanguageCode("en-US")
                .build();
        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(audioBytes)
                .build();

        // Performs speech recognition on the audio file
        RecognizeResponse resp = sttClient.recognize(config, audio);
        List<SpeechRecognitionResult> results = resp.getResultsList();

        StringBuilder sb = new StringBuilder();

        for (SpeechRecognitionResult result : results) {
            // There can be several alternative transcripts for a given
            // chunk of speech. Just use the first (most likely) one here.
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            sb.append(alternative.getTranscript());
            System.out.printf("Transcription: %s%n", alternative.getTranscript());
        }

        response.setContentType("text/plain");

        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.println(sb.toString());
    }
}
