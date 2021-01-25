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
    public String hitOpenAPI(QuizDtls quiz,int param)
    {
        String readLine = null;
        URL getUrl = null;
        try {

            if(param ==4)
                getUrl = new URL(endpoint+"amount="+quiz.getAmount()+"&category="+quiz.getCategory()+"&difficulty="+quiz.getDifficulty());

            if(param ==1)
                getUrl = new URL(endpoint+"amount="+quiz.getAmount());


            System.out.println(getUrl.getQuery()+" "+getUrl.getFile());

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

    public ValidationMsg isValidAmount(int qno)
    {
        if(qno>10 || qno<1)
        {

            return new ValidationMsg(-1,"Number of questions should be in between 1 to 10",false);
        }
        return new ValidationMsg(1,"Success",true);
    }

    public ValidationMsg isValidCat(int cat)
    {
        if((cat==0) || (cat>8 &&  cat<33))
        {
            return new ValidationMsg(1,"Success",true);
        }
        return new ValidationMsg(-1,"Invalid Category",false);
    }

    public ValidationMsg isValidDiffLevel(String diff)
    {
        if(diff.equalsIgnoreCase("medium")||diff.equalsIgnoreCase("easy")||diff.equalsIgnoreCase("hard"))
        {
            return new ValidationMsg(1,"Success",true);
        }
        return new ValidationMsg(-1,"Invalid Category",false);
    }

    public ValidationMsg isValidType(String type)
    {
        if(type.equalsIgnoreCase("boolean")|| type.equalsIgnoreCase("multiple"))
        {
            return new ValidationMsg(1,"Success",true);
        }
        return new ValidationMsg(-1,"Invalid Type",false);
    }

}
