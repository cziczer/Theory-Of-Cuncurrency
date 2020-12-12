package com.company;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Histogram {
    String separator = ",";
    Map<String, Integer> histogram = new HashMap<>();

    public synchronized void Add(String key) {
        if (histogram.get(key) == null) {
            histogram.put(key, 1);
        } else {
            var counter = histogram.get(key);
            histogram.put(key, counter + 1);
        }
    }

    public synchronized void SafeRecords(int M, int producersConsumersAmount,
                                         boolean fairness, boolean probabilityBiased) {
        List<RowRecord> records = histogram
                .entrySet()
                .stream()
                .map(e -> new RowRecord(M, Integer.parseInt(e.getKey().split("-")[1]),
                        e.getKey().split("-")[0], producersConsumersAmount, fairness,
                        probabilityBiased, e.getValue()))
                .sorted()
                .collect(Collectors.toList());
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("result1.csv",true), StandardCharsets.UTF_8));
            for (RowRecord record : records) {
                StringBuffer sb = new StringBuffer();
                sb.append(record.bufferSize);
                sb.append(separator);
                sb.append(record.producerConsumer);
                sb.append(separator);
                sb.append(record.size);
                sb.append(separator);
                sb.append(record.ProdConsConfig);
                sb.append(separator);
                sb.append(record.fairness);
                sb.append(separator);
                sb.append(record.isRandom);
                sb.append(separator);
                sb.append(record.counter);
                bw.write(sb.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CreateHeader() throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("result1.csv",true), StandardCharsets.UTF_8));
        StringBuffer oneLine = new StringBuffer();
        oneLine.append("buff size");
        oneLine.append(separator);
        oneLine.append("Producer/Consumer");
        oneLine.append(separator);
        oneLine.append("size");
        oneLine.append(separator);
        oneLine.append("prod cons config");
        oneLine.append(separator);
        oneLine.append("fairness");
        oneLine.append(separator);
        oneLine.append("is randomized");
        oneLine.append(separator);
        oneLine.append("counter");
        bw.write(oneLine.toString());
        bw.newLine();
        bw.flush();
        bw.close();
    }
}
