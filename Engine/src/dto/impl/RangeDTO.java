package dto.impl;

import body.Cell;
import expression.Range;

import java.util.ArrayList;
import java.util.List;

public class RangeDTO {
    //private Range range;
    final private String rangeID;
    final private List<CellDTO> rangeCells = new ArrayList<>();
    final String from;
    final String to;


    public RangeDTO(Range range) {
        //this.range = range;
        this.rangeID = range.getRangeId();
        this.from = range.getFrom().toString();
        this.to = range.getTo().toString();
        List<Cell> cells = range.getRangeCells();
        for (Cell cell : cells) {
            rangeCells.add(new CellDTO(cell));
        }
    }

    public String getRangeFrom() {
        //return range.getFrom().toString();
        return from;
    }

    public String getRangeTo() {
        //return range.getTo().toString();
        return to;
    }

    public List<CellDTO> getRangeCells() {
//        List<Cell> cells = (List<Cell>) range.getValue();
//        List<CellDTO> cellDTOs = new ArrayList<>();
//        for (Cell cell : cells) {
//            cellDTOs.add(new CellDTO(cell));
//        }
//        return cellDTOs;
        return rangeCells;
    }

    public String getRangeId() {
        //return range.getRangeId();
        return rangeID;
    }
}
