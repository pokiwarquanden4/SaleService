package com.example.saleservice.service.FTPClient;

import java.io.IOException;
import java.io.InputStream;

import com.example.saleservice.config.ConfigUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FTPReaderService {
    @Autowired
    private ConfigUtils configUtils;
    private String server = "127.0.0.1";
    private int port = 21;
    private String user = "admin";
    private String pass = "quang01239748392";
    private String remoteFilePath ;
    private FTPClient ftpClient = new FTPClient();
    private InputStream inputStream;
    public InputStream Reader(String serial) {
        remoteFilePath = configUtils.getConfig("12345");
        System.out.println(remoteFilePath);
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            inputStream = ftpClient.retrieveFileStream(remoteFilePath + serial + ".png");
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
