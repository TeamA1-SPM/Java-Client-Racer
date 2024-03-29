package main.tracks;

import main.constants.SpriteName;
import main.game.SpritesLoader;
import main.gamehelper.Car;
import main.gamehelper.Point;
import main.gamehelper.Segment;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static main.constants.ColorMode.*;
import static main.constants.Settings.*;
import static main.constants.SpriteName.*;

/*
 * Converts a json track file into a segment ArrayList
 * uses simple json library
 */

public class RoadParser{
    private final SpritesLoader spritesLoader;
    private ArrayList<Segment> segmentList;
    private final ArrayList<Car> carList = new ArrayList<>();

    public RoadParser(SpritesLoader spritesLoader){
        this.spritesLoader = spritesLoader;
    }

    public ArrayList<Segment> getTrack(int number){
        String fileName = switch (number) {
            case 2 -> "track02.json";
            case 3 -> "track03.json";
            case 4 -> "track04.json";
            case 5 -> "track05.json";
            case 6 -> "track06.json";
            case 7 -> "track07.json";
            case 8 -> "track08.json";
            case 9 -> "track09.json";
            case 10 -> "track10.json";
            default -> "track01.json";
        };
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

            FileReader reader;

            try{
                reader = new FileReader("./tracks/" + fileName);
            }catch (FileNotFoundException e){
                reader = new FileReader("./src/main/tracks/" + fileName);
            }

            JSONArray data = (JSONArray) parser.parse(reader);//path to the JSON file.

            for(int i = 0; i < data.size(); i++){
                Segment segment = new Segment(i);

                JSONObject jsonSegment = (JSONObject) data.get(i);

                // set p1 world [y,z]
                JSONArray p1 = (JSONArray) jsonSegment.get("p1");
                String p1WorldY = p1.get(0).toString();
                String p1WorldZ = p1.get(1).toString();
                segment.setP1World(new Point(0,stringToInt(p1WorldY), stringToInt(p1WorldZ)));

                // set p2 world [y,z]
                JSONArray p2 = (JSONArray) jsonSegment.get("p2");
                String p2WorldY = p2.get(0).toString();
                String p2WorldZ = p2.get(1).toString();
                segment.setP2World(new Point(0,stringToInt(p2WorldY), stringToInt(p2WorldZ)));

                // set segment curve
                double curve = Double.parseDouble(jsonSegment.get("curve").toString());
                segment.setCurve(curve);

                // set segment roadside sprites
                JSONArray spriteArray = (JSONArray) jsonSegment.get("roadside");
                for (Object o : spriteArray) {
                    JSONObject jsonSprite = (JSONObject) o;
                    SpriteName name = getSpriteName(jsonSprite.get("sprite").toString());
                    double offset = Double.parseDouble(jsonSprite.get("offset").toString());
                    segment.addRoadsideObj(name, offset, spritesLoader.getSpriteWidth(name));
                }

                // set segment roadside sprites
                JSONArray carsArray = (JSONArray) jsonSegment.get("cars");
                for (Object o : carsArray) {
                    JSONObject jsonSprite = (JSONObject) o;
                    SpriteName name = getSpriteName(jsonSprite.get("sprite").toString());
                    double offset = Double.parseDouble(jsonSprite.get("offset").toString());
                    double position = Double.parseDouble(jsonSprite.get("position").toString());
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

    // add start and finish line to the track
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

    // string to int converter
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
