@echo off
docker pull omridinio20/sheet-cell-server
docker run -p 8080:8080 omridinio20/sheet-cell-server
pause