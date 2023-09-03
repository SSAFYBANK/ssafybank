package com.ssafy.ssafybank.global.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.ssafybank.domain.exchangeRate.entity.ExchangeRate;
import com.ssafy.ssafybank.domain.exchangeRate.repository.ExchangeRateRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateUtils {

    private static HttpURLConnection connection;
    private static BigDecimal defaultExchangeRate = BigDecimal.valueOf(1300);

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Scheduled(cron = "0 0 9 ? * MON-FRI") // 초 분 시 일 월 요일
    public List<JSONObject> getExchangeRates() {
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

        String authKey = "RNH2Dx2huEfUOvrZPC0wt2eqh8Wouf1z";
        String searchDate = "20230901";
        String dataType = "AP01";
        List<JSONObject> exchangeRatesList = new ArrayList<>();

        try {
            // Request URL
            URL url = new URL(
                    "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + authKey
                            + "&searchdate=" + searchDate + "&data=" + dataType);
            connection = (HttpURLConnection) url.openConnection();

            // Request 초기 세팅
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            // API 호출
            // 실패했을 경우 Connection Close
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else { // 성공했을 경우 환율 정보 추출
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    JSONTokener tokener = new JSONTokener(line);
                    JSONArray exchangeRateInfoList = new JSONArray(tokener);

                    // 모든 통화에 대한 환율 정보 조회
                    for (int i = 0; i < exchangeRateInfoList.length(); i++) {
                        JSONObject exchangeRateInfo = exchangeRateInfoList.getJSONObject(i);

                        ExchangeRate exchangeRate = new ExchangeRate();
                        exchangeRate.setExchangeCountry(exchangeRateInfo.getString("cur_nm"));
                        exchangeRate.setExchangeDate(LocalDate.now());
                        exchangeRate.setBuyExchange(exchangeRateInfo.getString("ttb"));
                        exchangeRate.setSellExchange(exchangeRateInfo.getString("tts"));
                        exchangeRate.setExchangeCode(exchangeRateInfo.getString("cur_unit"));
                        exchangeRateRepository.save(exchangeRate);

                        // 리스트에 환율 정보 추가
                        exchangeRatesList.add(exchangeRateInfo);
                    }
                }
                reader.close();
            }
            System.out.println("환율" + responseContent.toString());

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        if (exchangeRatesList.isEmpty()) {
            // 만약 환율 정보가 없는 경우 기본 환율 정보 반환
            JSONObject defaultRate = new JSONObject();
            try {
                defaultRate.put("cur_unit", "KRW");
                defaultRate.put("ttb", "0");
                defaultRate.put("tts", "0");
                defaultRate.put("cur_nm", "한국 원");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            exchangeRatesList.add(defaultRate);
        }

        return exchangeRatesList;
    }
}
