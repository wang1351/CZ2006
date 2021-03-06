package realEstatistic.mapper;

import net.minidev.json.parser.ParseException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import realEstatistic.model.FACILITY_TYPE;
import realEstatistic.model.SCHOOL_TYPE;
import realEstatistic.model.School;
import realEstatistic.config.CronTime;
import realEstatistic.util.PostalConverter;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * This class implements the CronSchoolDao entity, which implements the interface SchoolDao and is specifically designed to refresh School information periodically.
 */
@Primary
@Component
@EnableScheduling
@Lazy(value = false)
public class CronSchoolDao implements SchoolDao{

    private static final List<School> primaryList = new ArrayList<>();
    private static final List<School> secondaryList = new ArrayList<>();
    private static final List<School> jcList = new ArrayList<>();
    private static final List<School> mixedList = new ArrayList<>();

    /**
     * This method is to get all secondary school in Singapore
     * @return a List of School objects
     */
    @Override
    public List<School> getAllSecondary() {
        return secondaryList;
    }

    /**
     * This method is to get all primary school in Singapore
     * @return a List of School objects
     */
    @Override
    public List<School> getAllPrimary() {
        return primaryList;
    }

    /**
     * This method is to get all JC in Singapore
     * @return a List of School objects
     */
    @Override
    public List<School> getAllJc() {
        return jcList;
    }

    /**
     * This method is to get all schools of mixed level in Singapore
     * @return a List of School objects
     */
    @Override
    public List<School> getAllMixed() {
        return mixedList;
    }

    /**
     * This method is to get all primary school that lays within the given region
     * @param startLat the starting latitude
     * @param endLat the ending latitude
     * @param startLon the starting longitude
     * @param endLon the ending longitude
     * @return a List of School objects
     */
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

    /**
     * This method is to get all secondary school that lays within the given region
     * @param startLat the starting latitude
     * @param endLat the ending latitude
     * @param startLon the starting longitude
     * @param endLon the ending longitude
     * @return a List of School objects
     */
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

    /**
     * This method is to get all jc that lays within the given region
     * @param startLat the starting latitude
     * @param endLat the ending latitude
     * @param startLon the starting longitude
     * @param endLon the ending longitude
     * @return a List of School objects
     */
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

    /**
     * This method is to get all schools of mixed levels that lays within the given region
     * @param startLat the starting latitude
     * @param endLat the ending latitude
     * @param startLon the starting longitude
     * @param endLon the ending longitude
     * @return a List of School objects
     */
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

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            is.close();
        }
    }

    /**
     * This method is set to be a cron method and is used to fetch school data from Gov Data.
     * @throws IOException Thrown when the data sent from Gov Data is not in Json format
     * @throws JSONException Thrown when the data sent from Gov Data lacks essential information
     */
    @Scheduled(cron = CronTime.fetchTime)
    public static void cronFetch() throws IOException, JSONException {
        System.setProperty("http.agent", "Mozilla/5.0");
        JSONObject json =  readJsonFromUrl("https://data.gov.sg/api/action/datastore_search?resource_id=ede26d32-01af-4228-b1ed-f05c45a1d8ee&limit=10000");
        primaryList.clear();
        secondaryList.clear();
        jcList.clear();
        mixedList.clear();
        for(int i = 0; i < json.getJSONObject("result").getJSONArray("records").length(); i++) {
            String schoolName, schoolDescription;
            float lat = 0;
            float long_ = 0;
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

            if(postalCode.length()==5){
                postalCode = "0" + postalCode;
            }
            lat = Float.parseFloat(PostalConverter.convertPostal(postalCode).split("\t")[0]);
            long_ = Float.parseFloat(PostalConverter.convertPostal(postalCode).split("\t")[1]);

            UUID newId = UUID.randomUUID();
            School s = new School(newId, FACILITY_TYPE.fromShool(type), schoolName, schoolDescription, lat, long_, type);
            switch (type){
                case PRIMARY:
                    primaryList.add(s);
                    break;
                case SECONDASRY:
                    secondaryList.add(s);
                    break;
                case MIXEDLEVEL:
                    mixedList.add(s);
                    break;
                case JC:
                    jcList.add(s);
                    break;
                default:
                    break;
            }
        }}


}




