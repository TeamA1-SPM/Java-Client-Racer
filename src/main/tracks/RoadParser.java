package main.tracks;


import main.constants.SpriteName;
import main.game.SpritesLoader;
import main.helper.Car;
import main.helper.Point;
import main.helper.Segment;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.ArrayList;

import static main.constants.ColorMode.*;
import static main.constants.Settings.*;
import static main.constants.SpriteName.*;


public class RoadParser{
    private String jsonPath = "./src/main/tracks/" ; //assign your JSON String here
    private SpritesLoader spritesLoader;
    private ArrayList<Segment> segmentList;
    private final ArrayList<Car> carList = new ArrayList<>();


    public RoadParser(SpritesLoader spritesLoader){
        this.spritesLoader = spritesLoader;
    }

    public ArrayList<Segment> getTrack(int number){
        String fileName;
        switch (number){
            case 1:
                fileName = "track01.json";
                break;
            default:
                fileName = "track01.json";
                break;
        }
        return parse(fileName);
    }

    public ArrayList<Car> getCarList(){
        return carList;
    }

    private ArrayList<Segment> parse(String fileName) {
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

                // set segment curve
                double curve = Double.parseDouble(jsonSegment.get("curve").toString());
                segment.setCurve(curve);

                // set segment roadside sprites
                JSONArray spriteArray = (JSONArray) jsonSegment.get("sprite");
                for (Object o : spriteArray) {
                    JSONObject jsonSprite = (JSONObject) o;
                    SpriteName name = getSpriteName(jsonSprite.get("source").toString());
                    double offset = Double.parseDouble(jsonSprite.get("offset").toString());
                    segment.addRoadsideObj(name, offset, spritesLoader.getSpriteWidth(name));
                }

                // set segment roadside sprites
                JSONArray carsArray = (JSONArray) jsonSegment.get("cars");
                for (Object o : carsArray) {
                    JSONObject jsonSprite = (JSONObject) o;
                    SpriteName name = getSpriteName(jsonSprite.get("sprite").toString());
                    double offset = Double.parseDouble(jsonSprite.get("offset").toString());
                    double position = Double.parseDouble(jsonSprite.get("z").toString());
                    double speed = Double.parseDouble(jsonSprite.get("speed").toString());
                    double width = spritesLoader.getSpriteWidth(name);
                    Car car = new Car(name, offset, position, speed, width);
                    this.carList.add(car);
                }


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

    private SpriteName getSpriteName(String name){
        return switch (name) {
            case "BILLBOARD01" -> BILLBOARD01;
            case "BILLBOARD02" -> BILLBOARD02;
            case "BILLBOARD03" -> BILLBOARD03;
            case "BILLBOARD04" -> BILLBOARD04;
            case "BILLBOARD05" -> BILLBOARD05;
            case "BILLBOARD06" -> BILLBOARD06;
            case "BILLBOARD07" -> BILLBOARD07;
            case "BILLBOARD08" -> BILLBOARD08;
            case "BILLBOARD09" -> BILLBOARD09;
            case "PALM_TREE" -> PALM_TREE;
            case "TREE1" -> TREE1;
            case "TREE2" -> TREE2;
            case "DEAD_TREE1" -> DEAD_TREE1;
            case "DEAD_TREE2" -> DEAD_TREE2;
            case "BUSH1" -> BUSH1;
            case "BUSH2" -> BUSH2;
            case "CACTUS" -> CACTUS;
            case "STUMP" -> STUMP;
            case "BOULDER1" -> BOULDER1;
            case "BOULDER2" -> BOULDER2;
            case "BOULDER3" -> BOULDER3;
            case "COLUMN" -> COLUMN;
            case "SEMI" -> SEMI;
            case "TRUCK" -> TRUCK;
            case "CAR01" -> CAR01;
            case "CAR02" -> CAR02;
            case "CAR03" -> CAR03;
            case "CAR04" -> CAR04;
            default -> PLAYER_STRAIGHT;
        };
    }
}
