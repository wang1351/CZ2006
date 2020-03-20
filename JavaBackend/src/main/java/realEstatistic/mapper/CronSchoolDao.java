package realEstatistic.mapper;

import net.minidev.json.parser.ParseException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import realEstatistic.model.SCHOOL_TYPE;
import realEstatistic.model.School;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Primary
@Component
@EnableScheduling
@Lazy(value = false)
public class CronSchoolDao implements SchoolDao{

    private static List<School> primaryList = new ArrayList<School>();
    private static List<School> secondaryList = new ArrayList<School>();
    private static List<School> jcList = new ArrayList<School>();
    private static List<School> mixedList = new ArrayList<School>();

    @Override
    public List<School> getAllSecondary() {
        return secondaryList;
    }

    @Override
    public List<School> getAllPrimary() {
        return primaryList;
    }

    @Override
    public List<School> getAllJc() {
        return jcList;
    }

    @Override
    public List<School> getAllMixed() {
        return mixedList;
    }

    @Override
    public List<School> getPrimaryByLocation(float startLat, float endLat, float startLon, float endLon) {
        ArrayList<School> filteredList = new ArrayList<>();
        //TODO fix this duplicate code issue
        for(School s : primaryList){
            float lat = s.getLat();
            float lon = s.getLong_();
            if (lat >= startLat && lat <= endLat && lon >= startLon && lon <= endLon){
                filteredList.add(s);
            }
        }
        return filteredList;
    }

    @Override
    public List<School> getSecondaryByLocation(float startLat, float endLat, float startLon, float endLon) {
        ArrayList<School> filteredList = new ArrayList<>();
        for(School s : secondaryList){
            float lat = s.getLat();
            float lon = s.getLong_();
            if (lat >= startLat && lat <= endLat && lon >= startLon && lon <= endLon){
                filteredList.add(s);
            }
        }
        return filteredList;
    }

    @Override
    public List<School> getJcByLocation(float startLat, float endLat, float startLon, float endLon) {
        ArrayList<School> filteredList = new ArrayList<>();
        for(School s : jcList){
            float lat = s.getLat();
            float lon = s.getLong_();
            if (lat >= startLat && lat <= endLat && lon >= startLon && lon <= endLon){
                filteredList.add(s);
            }
        }
        return filteredList;
    }

    @Override
    public List<School> getMixedByLocation(float startLat, float endLat, float startLon, float endLon) {
        ArrayList<School> filteredList = new ArrayList<>();
        for(School s : mixedList){
            float lat = s.getLat();
            float lon = s.getLong_();
            if (lat >= startLat && lat <= endLat && lon >= startLon && lon <= endLon){
                filteredList.add(s);
            }
        }
        return filteredList;
    }

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }



    @Scheduled(cron = "* 0 0 * * *")
    public static void CronFetch() throws IOException, JSONException, ParseException, InterruptedException {
        System.setProperty("http.agent", "Mozilla/5.0");
        JSONObject json =  readJsonFromUrl("https://data.gov.sg/api/action/datastore_search?resource_id=ede26d32-01af-4228-b1ed-f05c45a1d8ee&limit=10000");
        for(int i = 0; i < json.getJSONObject("result").getJSONArray("records").length(); i++) {
            String schoolName, schoolDescription;
            float lat = 0, long_ = 0;
            SCHOOL_TYPE type = null;
            String postalCode = null;
            JSONObject myjson = (JSONObject) json.getJSONObject("result").getJSONArray("records").get(i);
            postalCode = myjson.getString("postal_code");
            schoolName = myjson.getString("school_name");
            try {
                type = SCHOOL_TYPE.fromString(myjson.getString("mainlevel_code"));
            } catch (IllegalArgumentException e){
                e.printStackTrace();
            }
            schoolDescription = "tel:  " + myjson.getString("telephone_no") + " email:   " + myjson.getString("email_address") + " nearby bus:  " +
                    myjson.getString("bus_desc") + " ethos: " +
                    myjson.getString("philosophy_culture_ethos");
            Thread.sleep(5000);
            JSONObject postalJson = null;
            StringBuilder result = new StringBuilder();
            String line;

            URLConnection urlConnection = new URL("https://geocode.xyz/" + postalCode + "?json=1").openConnection();

            urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0");


            try (InputStream is = new URL("https://geocode.xyz/" + postalCode + "?json=1").openStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                while ((line = br.readLine()) != null) {
                    result.append(line);
                }

            }

            try {
                postalJson = new JSONObject(result.toString());
            } catch (JSONException err) {
                System.out.println(err);
            }

            lat = (float) postalJson.getDouble("latt");
            long_ = (float) postalJson.getDouble("longt");

            UUID newId = UUID.randomUUID();
            School a = new School(newId, schoolName, lat, long_, schoolDescription, type);
            switch (type){
                case PRIMARY:
                    primaryList.add(a);
                    break;
                case SECONDASRY:
                    secondaryList.add(a);
                    break;
                case MIXEDLEVEL:
                    mixedList.add(a);
                    break;
                case JC:
                    jcList.add(a);
                    break;
            }
        }

    }


}




