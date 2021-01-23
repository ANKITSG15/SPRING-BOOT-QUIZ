package com.util;

import com.example.QuizDtls;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;

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

    public ValidationMsg isValidation(int qno, int cat, String diff, String type)
    {
        int count =4;
        if(qno>10 || qno<1)
        {

           return new ValidationMsg(-1,"Number of questions should be in between 1 to 10");
        }
        else
        {
            count--;
        }

        if((cat>8 && cat <33) || cat<0 || cat>32 )
        {
            return new ValidationMsg(-1,"Invalid Category");
        }
        else
        {
            count--;
        }

        if(diff.equalsIgnoreCase("medium")||diff.equalsIgnoreCase("easy")||diff.equalsIgnoreCase("hard"))
        {
            count--;
        }
        else
        {
            return new ValidationMsg(-1,"Invalid Difficulty Level");
        }
        if(type.equalsIgnoreCase("boolean")|| type.equalsIgnoreCase("multiple"))
        {
            return new ValidationMsg(-1,"Invalid Type");
        }
        else
        {
            count--;
        }

        if(count==0)
        {
            return new ValidationMsg(1,"Success");
        }
        else
        {
            return new ValidationMsg(-1,"Failure");
        }

    }

}
