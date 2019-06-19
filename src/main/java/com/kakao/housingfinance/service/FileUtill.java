package com.kakao.housingfinance.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Future;

@Component
public class FileUtill {
    private List<String> readTextFile(MultipartFile mpf) throws IOException {

        byte[] bytes = mpf.getBytes();
        ByteArrayInputStream inputFilestream = new ByteArrayInputStream(bytes);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputFilestream ));
        String line = "";
        List<String> readLines = new ArrayList<>();

        while ((line = br.readLine()) != null) readLines.add(line);

        br.close();

        return readLines;
    }
    @Async
    public Future<List<String>> getCompanyList(MultipartFile mpf) throws IOException, InterruptedException {
        List<String> readFileLines = readTextFile(mpf);
        String firstLine =  readFileLines.get(0);
        StringTokenizer st = new StringTokenizer(firstLine, ",");
        List<String> readFinanceCompany = new ArrayList<>();

        while(st.hasMoreElements()){
            String tmp = st.nextToken();
            if(tmp.equals("연도") || tmp.equals("월")) continue;
            tmp = tmp.replace("(억원)","");

            readFinanceCompany.add(tmp);
        }
        return  new AsyncResult<>(readFinanceCompany);
    }

    @Async
    public Future<List<String []>> getFinanceData(MultipartFile mpf) throws IOException, InterruptedException {
        List<String> readFileLines = readTextFile(mpf);
        List<String []> financeData = new ArrayList<>();

        for(int i=1;i<readFileLines.size();i++){
            String firstLine =  readFileLines.get(i);
            StringTokenizer st = new StringTokenizer(firstLine, ",");

            String [] arr = new String[st.countTokens()];
            int j = 0;
            while(st.hasMoreElements()){
                arr[j++] = st.nextToken();
            }
            financeData.add(arr);
        }

        return new AsyncResult<>(financeData);
    }





}
