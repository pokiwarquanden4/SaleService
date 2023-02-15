package com.example.saleservice.service.FTPClient;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

@Service
public class FTPReaderService {
    private String server = "127.0.0.1";
    private int port = 21;
    private String user = "admin";
    private String pass = "quang01239748392";
    private String remoteFilePath = "/SaleService/src/main/java/com/example/saleservice/constant/PNG/";
    public InputStream Reader(String serial) {
        remoteFilePath += serial + ".png";
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFilePath);
            ftpClient.logout();
            ftpClient.disconnect();
            return inputStream;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String convertToBase64(String serial) throws IOException {
        return FPTConvert.convertToBase64(Reader(serial));
    }
}
