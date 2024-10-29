package dto.impl;

import java.util.ArrayList;
import java.util.List;

public class RangeDTO {
    final private String rangeID;
    final private List<CellDTO> rangeCells = new ArrayList<>();
    final String from;
    final String to;


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
