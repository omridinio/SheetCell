package Mangger;

import dto.impl.Coordinate;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class CoordinateAdapter extends TypeAdapter<Coordinate> {

    @Override
    public void write(JsonWriter out, Coordinate coordinate) throws IOException {
        out.value(coordinate.toString());
    }

    @Override
    public Coordinate read(JsonReader in) throws IOException {
        String coordString = in.nextString();
        return new Coordinate(coordString);
    }
}
