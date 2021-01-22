package com.util;

import com.example.QuizDtls;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@Service("globalUtility")
public class GlobalUtility {

    String endpoint = "https://opentdb.com/api.php?";
    String response = null;
    public String hitOpenAPI(QuizDtls quiz)
    {
        String readLine = null;
        try {

            URL getUrl = new URL(endpoint+"amount="+quiz.getAmount()+"&category="+quiz.getCategory()+"&difficulty="+quiz.getDifficulty());
            System.out.println(endpoint+"amount="+quiz.getAmount()+"&category="+quiz.getCategory()+"&difficulty="+quiz.getDifficulty());

            HttpURLConnection httpcon = null;
            try {
                httpcon = (HttpURLConnection)getUrl.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpcon.setRequestMethod("GET");
            int code = httpcon.getResponseCode();

            System.out.println("Code-"+code);

            if(code == HttpURLConnection.HTTP_OK)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
                StringBuffer json = new StringBuffer();
                while((readLine = in.readLine())!= null)
                {
                    json.append(readLine);
                }
                in.close();
                response = json.toString();
                System.out.println("Response"+json);
            }
            else
            {
                System.out.println("Error in getting response");
            }

        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}
