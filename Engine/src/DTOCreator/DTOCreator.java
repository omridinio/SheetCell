package DTOCreator;

import body.Cell;
import body.Sheet;
import body.impl.ImplCell;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.Coordinate;
import dto.impl.ImplSheetDTO;
import dto.impl.RangeDTO;
import expression.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DTOCreator {

    public static CellDTO createCellDTO(Cell cell) {
        return new CellDTO(cell.getId(), cell.getLastVersionUpdate(), cell.getOriginalValue(), cell.getEffectiveValue().toString(), cell.getUsername(), cell.getCellsDependsOnThem(), cell.getCellsDependsOnHim());
    }

    public static CellDTO createEmptyCellDTO(String id) {
        return createCellDTO(new ImplCell(id));
    }

    public static SheetDTO createSheetDTO(Sheet sheet) {
        Map<Coordinate, CellDTO> activeCellsDto = new HashMap<>();
        Map<Coordinate, Cell> activeCells = sheet.getActiveCell();
        for(Coordinate coord : activeCells.keySet()){
            activeCellsDto.put(coord, createCellDTO(activeCells.get(coord)));
        }
        return new ImplSheetDTO(sheet.getSheetName(), sheet.getVersion(), sheet.getThickness(), sheet.getWidth(), sheet.getRowCount(), sheet.getColumnCount(), activeCellsDto, sheet.getSheetOwner());
    }

    public static RangeDTO createRangeDTO(Range range) {
        List<CellDTO> rangeCellsDto = new ArrayList<>();
        List<Cell> rangeCells = range.getRangeCells();
        for (Cell cell : rangeCells) {
            rangeCellsDto.add(createCellDTO(cell));
        }
        return new RangeDTO(range.getRangeId(), range.getFrom().toString(), range.getTo().toString(), rangeCellsDto);
    }

}
