package com.kademika.akka;

import akka.actor.UntypedActor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class TestUntypedActor extends UntypedActor {
    private TreeMap<Integer, Double> mapSum = new TreeMap<>();

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof String) {
            String str = (String) obj;
            if(str.equals("end")) {
                writeResult("src/main/resources/Result.txt");
            } else {
                setDataToTree(str);
            }
            return;
        }
        unhandled(obj);
    }

    private void setDataToTree(String data) {
        Integer customID;
        Double customAmount;

        customID = Integer.valueOf(data.split(";")[0]);
        customAmount = Double.valueOf(data.split(";")[1]);
        if (mapSum != null) {
            Double temp = mapSum.get(customID);
            if (temp == null) {
                mapSum.put(customID, customAmount);
            } else {
                temp += customAmount;
                mapSum.put(customID, temp);
            }

        } else {
            mapSum.put(customID, customAmount);
        }

    }

    private void writeResult(String fileName) {
        if (mapSum == null) {
            return;
        }

        StringBuilder data = new StringBuilder();
        String str;
        try (FileOutputStream fos = new FileOutputStream(fileName);
             OutputStreamWriter fileWriter = new OutputStreamWriter(fos,
                     StandardCharsets.UTF_8)) {

            for (Map.Entry<Integer, Double> entry : mapSum.entrySet()) {
                data.append(String.valueOf(entry.getKey()));
                data.append("; ");
                data.append(String.valueOf(entry.getValue()));
                data.append("\r\n");
                fileWriter.write(data.toString());
                data.setLength(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
