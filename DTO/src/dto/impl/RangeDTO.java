package dto.impl;


//import body.Cell;
//import expression.Range;

import java.util.ArrayList;
import java.util.List;

public class RangeDTO {
    final private String rangeID;
    final private List<CellDTO> rangeCells = new ArrayList<>();
    final String from;
    final String to;


//    public RangeDTO(Range range) {
//        this.rangeID = range.getRangeId();
//        this.from = range.getFrom().toString();
//        this.to = range.getTo().toString();
//        List<Cell> cells = range.getRangeCells();
//        for (Cell cell : cells) {
//            //rangeCells.add(new CellDTO(cell));
//            rangeCells.add(DTOCreator.createCellDTO(cell));
//        }
//    }

    public RangeDTO(String rangeID, String from, String to, List<CellDTO> rangeCells) {
        this.rangeID = rangeID;
        this.from = from;
        this.to = to;
        this.rangeCells.addAll(rangeCells);
    }


    public String getRangeFrom() {
        return from;
    }

    public String getRangeTo() {
        return to;
    }

    public List<CellDTO> getRangeCells() {
        return rangeCells;
    }

    public String getRangeId() {
        return rangeID;
    }
}
