package Mangger;

import dto.impl.PermissionRequest;

import java.util.*;

public class RequestPermissonManager {
    private List<PermissionRequest> allRequest = new ArrayList<>();
    private Map<String,Map<Integer, PermissionRequest>> requestPermissonByOwner = new HashMap<>(); //<owner,<requestID,PermissionRequest>>
    private Map<String, Map<Integer, PermissionRequest>> requestPermissionBySheet = new HashMap<>(); //<sheetName,<requestID,PermissionRequest>>

    public synchronized void addRequest(PermissionRequest request){
        String owner = request.getOwner();
        String sheetName = request.getSheetName();
        int requestID = request.getIndex();
        allRequest.add(request);
        if(!requestPermissonByOwner.containsKey(owner)){
            requestPermissonByOwner.put(owner,new HashMap<>());
        }
        requestPermissonByOwner.get(owner).put(requestID,request);
        if(!requestPermissionBySheet.containsKey(sheetName)){
            requestPermissionBySheet.put(sheetName,new HashMap<>());
        }
        requestPermissionBySheet.get(sheetName).put(requestID,request);
    }

    public List<PermissionRequest> getAllRequestByOwner(String owner){
        List<PermissionRequest> res = new ArrayList<>();
        if(requestPermissonByOwner.containsKey(owner)){
            for(PermissionRequest request : requestPermissonByOwner.get(owner).values()){
                if(request.isApproved()){
                    requestPermissonByOwner.get(owner).remove(request.getIndex());
                }
                else{
                    res.add(request);
                }
            }
        }
        return res;
    }

    public List<PermissionRequest> getAllRequestBySheet(String sheetName){
        List<PermissionRequest> res = new ArrayList<>();
        if(requestPermissionBySheet.containsKey(sheetName)){
            res.addAll(requestPermissionBySheet.get(sheetName).values());
        }
        return res;
    }

    public synchronized void aprroveRequest(String owner, int requestID){
        if(requestPermissonByOwner.containsKey(owner) && requestPermissonByOwner.get(owner).containsKey(requestID)){
            PermissionRequest request = requestPermissonByOwner.get(owner).get(requestID);
            request.setApproved(true);
            requestPermissonByOwner.get(owner).remove(requestID);
        }
    }

    public synchronized void aprroveRequest(String owner, PermissionRequest request){
        if(requestPermissonByOwner.containsKey(owner) && requestPermissonByOwner.get(owner).containsKey(request.getIndex())){
            request.setApproved(true);
            requestPermissonByOwner.get(owner).remove(request.getIndex());
        }
    }

    public synchronized boolean isRequestExist(PermissionRequest request){
        return allRequest.contains(request);
    }

    public synchronized PermissionRequest deleteRequest(String owner, int requestId, String newStatus) {
        if(requestPermissonByOwner.containsKey(owner) && requestPermissonByOwner.get(owner).containsKey(requestId)){
            PermissionRequest request = requestPermissonByOwner.get(owner).get(requestId);
            requestPermissonByOwner.get(owner).remove(requestId);
            request.setApproved(true);
            request.setStatus(newStatus);
            return request;
        }
        return null;
    }
}
