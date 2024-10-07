package dto.impl;

public class AproveRequest {
    private String status;
    private int requestID;

    public AproveRequest(String status, int requestID) {
        this.status = status;
        this.requestID = requestID;
    }

    public String getStatus() {
        return status;
    }

    public int getRequestID() {
        return requestID;
    }
}


