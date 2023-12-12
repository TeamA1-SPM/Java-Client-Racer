package main.tracks;


import main.helper.Point;
import main.helper.Segment;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.ArrayList;

import static main.constants.ColorMode.*;
import static main.constants.ColorMode.DARK;
import static main.constants.Settings.*;


public class RoadParser{
    private String jsonPath = "./src/main/tracks/" ; //assign your JSON String here

    private ArrayList<Segment> segmentList;


    public ArrayList<Segment> parse(String fileName) {
        segmentList = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            //Use JSONObject for simple JSON and JSONArray for array of JSON.
            JSONObject data = (JSONObject) parser.parse(new FileReader(jsonPath+fileName));//path to the JSON file.

            JSONArray segmentsArray = (JSONArray) data.get("segments");

            for(int i = 0; i < segmentsArray.size(); i++){
                Segment segment = new Segment(i);

                JSONObject jsonSegment = (JSONObject) segmentsArray.get(i);

                // set p1 world [y,z]
                JSONArray p1 = (JSONArray) jsonSegment.get("p1");
                segment.setP1World(new Point(0,stringToInt(p1.get(0).toString()), stringToInt(p1.get(1).toString())));
                // set p1 world [y,z]
                JSONArray p2 = (JSONArray) jsonSegment.get("p2");
                segment.setP2World(new Point(0,stringToInt(p2.get(0).toString()), stringToInt(p2.get(1).toString())));


                double curve = Double.parseDouble(jsonSegment.get("curve").toString());
                segment.setCurve(curve);



                if (Math.floorDiv(i, RUMBLE_LENGTH) % 2 == 0) {
                    segment.setColorMode(LIGHT);
                } else {
                    segment.setColorMode(DARK);
                }


                segmentList.add(segment);
            }

            addStartFinish();

        } catch (Exception e) {
            System.out.println(e);
        }

        return segmentList;

    }

    private void addStartFinish(){
        int index = (int)Math.floor(PLAYER_Z /SEGMENT_LENGTH) % segmentList.size();

        // Start line
        segmentList.get(index + 2).setColorMode(START);
        segmentList.get(index + 3).setColorMode(START);

        // Finish line
        for(int n = 0; n < RUMBLE_LENGTH; n++){
            segmentList.get(segmentList.size() - 1 - n).setColorMode(FINISH);
        }
    }

    private int stringToInt(String number){
        double result;
        if(number.length() > 10){
            result = Double.parseDouble(number.substring(0,9));
        }else{
            result = Double.parseDouble(number);
        }
        return (int)result;
    }


}
