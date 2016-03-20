package ee.ttu.foodinter.request;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {
    public static String getData(RequestPackage p) {
        BufferedReader reader = null;
        String uri = p.getUri();

        if(p.getMethod().equals("GET")) {
            uri += "?" + p.getEncodedParams();
        }

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod());
            Log.d("lammas", "got url");

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            Log.d("lammas", "stringbuilderi successful");
            Log.d("lammas", ""+uri);
            String line = "";
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                sb.append(line + "\n");
            }
            //System.out.println(line);

            return sb.toString();
        } catch (Exception e) {
            Log.d("lammas", "http manager exception");
            e.printStackTrace();
            return null;
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}