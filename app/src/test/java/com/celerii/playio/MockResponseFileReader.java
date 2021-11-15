package com.celerii.playio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MockResponseFileReader {
    String content;

    public MockResponseFileReader(String path) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(path));
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();
        String str;
        while((str = bufferedReader.readLine())!= null) {
            stringBuilder.append(str);
        }
        this.content = stringBuilder.toString();
        reader.close();
    }
}
