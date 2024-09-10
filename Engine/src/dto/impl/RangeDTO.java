package dto.impl;

import body.Cell;
import expression.Range;

import java.util.ArrayList;
import java.util.List;

public class RangeDTO {
    private Range range;

    public RangeDTO(Range range) {
        this.range = range;
    }

    public String getRangeFrom() {
        return range.getFrom().toString();
    }

    public String getRangeTo() {
        return range.getTo().toString();
    }

    public List<CellDTO> getRangeCells() {
        List<Cell> cells = (List<Cell>) range.getValue();
        List<CellDTO> cellDTOs = new ArrayList<>();
        for (Cell cell : cells) {
            cellDTOs.add(new CellDTO(cell));
        }
        return cellDTOs;
    }

    public String getRangeId() {
        return range.getRangeId();
    }
}
