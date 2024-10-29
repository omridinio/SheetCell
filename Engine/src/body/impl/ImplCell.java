package body.impl;

import body.Cell;
import dto.impl.Coordinate;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Empty;
import expression.impl.Number;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImplCell implements Cell,Serializable  {

    private String Id;
    private int lastVersionUpdate;
    private String originalValue;
    private Expression expression;
    private EffectiveValue effectiveValue;
    private String username;
    private List<Coordinate> cellsDependsOnThem = new ArrayList<>();
    private List<Coordinate> cellsDependsOnHim = new ArrayList<>();

    public ImplCell(String id) {
        Id = id;
        lastVersionUpdate = 1;
        originalValue = "";
        effectiveValue = new Empty();
    }

    public ImplCell(double num){
        Number number = new Number(num);
        effectiveValue = number;
    }

    @Override
    public Cell copyCell() throws IOException, ClassNotFoundException {
        // Step 1: Serialize the object to a byte array
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(byteOutStream);
        outStream.writeObject(this);
        outStream.flush();

        // Step 2: Deserialize the byte array into a new object
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
        ObjectInputStream inStream = new ObjectInputStream(byteInStream);

        Cell newContoller = (Cell) inStream.readObject();
        return  newContoller;
    }

    @Override
    public String getId() {
        return Id;
    }

    @Override
    public void setId(String id) {
        Id = id;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public int getLastVersionUpdate() {
        return lastVersionUpdate;
    }

    @Override
    public void setLastVersionUpdate(int version) {
        lastVersionUpdate = version;
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setOriginalValue(String original) {
        originalValue = original;
    }

    @Override
    public void setEffectiveValue(EffectiveValue value) {
        this.effectiveValue = value;
    }

    @Override
    public void setExpression(Expression expression) {
        this.expression = expression;
    }
    @Override
    public EffectiveValue getEffectiveValue()throws NumberFormatException {return effectiveValue;}

    @Override
    public void setDependsOnThem(List<Coordinate> dependsOnThem) {
        this.cellsDependsOnThem = dependsOnThem;
    }

    @Override
    public void setDependsOnHim(List<Coordinate> dependsOnHim) {
        this.cellsDependsOnHim = dependsOnHim;
    }

    @Override
    public List<Coordinate> getCellsDependsOnThem() {
        return cellsDependsOnThem;
    }
    @Override
    public List<Coordinate> getCellsDependsOnHim() {
        return cellsDependsOnHim;
    }

    @Override
    public Coordinate getCoordinate() {
        return null;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }




}

