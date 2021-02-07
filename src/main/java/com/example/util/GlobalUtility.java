package com.example.util;

import com.example.dto.QuizDtls;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@Service("globalUtility")
public class GlobalUtility {
    @Autowired
    private ConfigUtility config;
    String response = null;

    public URL buildURL(QuizDtls quiz, int param) throws Exception {
        URIBuilder makeurl = new URIBuilder();
        try {
            if (param == 4)
                makeurl.setScheme(config.getProperty("endpoint.scheme")).setHost(config.getProperty("endpoint.host")).setPath(config.getProperty("endpoint.path"))
                        .setParameter(config.getProperty("endpoint.param1"), String.valueOf(quiz.getAmount()))
                        .setParameter(config.getProperty("endpoint.param2"), String.valueOf(quiz.getCategory()))
                        .setParameter(config.getProperty("endpoint.param3"), quiz.getDifficulty())
                        .setParameter(config.getProperty("endpoint.param4"), quiz.getType());

            if (param == 1)
                makeurl.setScheme(config.getProperty("endpoint.scheme")).setHost(config.getProperty("endpoint.host")).setPath(config.getProperty("endpoint.path"))
                        .setParameter(config.getProperty("endpoint.param1"), String.valueOf(quiz.getAmount()));

        } catch (Exception ex) {
            throw new Exception();
        }
        return makeurl.build().toURL();
    }

    public String hitOpenAPI(QuizDtls quiz, int param) throws Exception {
        String readLine = null;
        URL getUrl = null;
        try {
            getUrl = buildURL(quiz, param);
            HttpURLConnection httpcon = null;
            httpcon = (HttpURLConnection) getUrl.openConnection();

            httpcon.setRequestMethod("GET");
            int code = httpcon.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
                StringBuffer json = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    json.append(readLine);
                }
                in.close();
                response = json.toString();
            } else {
                System.out.println("Error in getting response");
            }

        } catch (IOException | URISyntaxException e) {
            throw new IOException();
        }
        if (response != null)
            return response;
        else
            throw new IOException();
    }

    public ValidationMsg isValidAmount(int qno) {
        if (qno > 10 || qno < 1) {
            return new ValidationMsg(-1, "Number of questions should be in between 1 to 10", false);
        }
        return new ValidationMsg(1, "Success", true);
    }

    public ValidationMsg isValidCat(int cat) {
        if ((cat == 0) || (cat > 8 && cat < 33)) {
            return new ValidationMsg(1, "Success", true);
        }
        return new ValidationMsg(-1, "Invalid Category", false);
    }

    public ValidationMsg isValidDiffLevel(String diff) {
        if (diff.equalsIgnoreCase("medium") || diff.equalsIgnoreCase("easy") || diff.equalsIgnoreCase("hard")) {
            return new ValidationMsg(1, "Success", true);
        }
        return new ValidationMsg(-1, "Invalid Category", false);
    }

    public ValidationMsg isValidType(String type) {
        if (type.equalsIgnoreCase("boolean") || type.equalsIgnoreCase("multiple")) {
            return new ValidationMsg(1, "Success", true);
        }
        return new ValidationMsg(-1, "Invalid Type", false);
    }
}
