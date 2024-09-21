package Properties;

import java.util.List;

public class CoordinateExpression {
    private List<Integer> coordinates;

    public CoordinateExpression(Integer... coordinates) {
        for (Integer coordinate : coordinates) {
            this.coordinates.add(coordinate);
        }
    }

    public List<Integer> getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object coordinates){
        if (this == coordinates) return true;
        if (coordinates == null || getClass() != coordinates.getClass()) return false;
        CoordinateExpression that = (CoordinateExpression) coordinates;
        return this.coordinates.equals(that.coordinates);
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }





}


