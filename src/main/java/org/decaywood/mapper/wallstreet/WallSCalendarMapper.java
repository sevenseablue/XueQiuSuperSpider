package org.decaywood.mapper.wallstreet;

import com.fasterxml.jackson.databind.JsonNode;
import org.decaywood.mapper.AbstractMapper;
import org.decaywood.timeWaitingStrategy.TimeWaitingStrategy;
import org.decaywood.utils.DateParser;
import org.decaywood.utils.RequestParaBuilder;
import org.decaywood.utils.URLMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author: decaywood
 * @date: 2015/11/27 10:48
 */


/**
 * 当前日期 -> 龙虎榜映射器
 */
public class WallSCalendarMapper extends AbstractMapper<List<String>, String> {

    public WallSCalendarMapper() throws RemoteException {
        this(null);
    }

    /**
     * @param strategy 超时等待策略（null则设置为默认等待策略）
     */
    public WallSCalendarMapper(TimeWaitingStrategy strategy) throws RemoteException {
        super(strategy);
    }

    @Override
    public String mapLogic(List<String> dates) throws Exception {
        StringBuffer sb = new StringBuffer();
        List<String> dateLists = DateParser.intervalDays(dates.get(0), dates.get(1));
        System.out.println(dateLists);
        for (int i = 0; i < dateLists.size() - 1; i++) {
            String target = URLMapper.WALLSTREETCN_CALENDAR.toString();
            System.out.println(dateLists.get(i) + dateLists.get(i + 1));
            RequestParaBuilder builder = new RequestParaBuilder(target)
                    .addParameter("start", dateLists.get(i))
                    .addParameter("end", dateLists.get(i + 1));
            System.out.println(builder.build());
            URL url = new URL(builder.build());
            String json = request(url);
            JsonNode node = mapper.readTree(json);
            sb.append(processNode(node));
        }
        return sb.toString();

    }

    private String processNode(JsonNode node) {
        StringBuffer sb = new StringBuffer();
        for (JsonNode jsonNode : node.get("results")) {
            String title = jsonNode.get("title").asText();
            String createdAt = jsonNode.get("id").asText();
//            System.out.println(title);
//            System.out.println(createdAt);
            sb.append(title).append("\n");
        }
        return sb.toString();
    }

    private void writeFiles(String start, String end, String path) throws IOException, InterruptedException {
        List<String> dateLists = DateParser.intervalDays(start, end);
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < dateLists.size() - 1; i++) {
            System.out.println(dateLists.get(i));
            String target = URLMapper.WALLSTREETCN_CALENDAR.toString();
            RequestParaBuilder builder = new RequestParaBuilder(target)
                    .addParameter("start", dateLists.get(i))
                    .addParameter("end", dateLists.get(i + 1));
            URL url = new URL(builder.build());
            String json = request(url);
            String string = dateLists.get(i) + "\t" + dateLists.get(i + 1) + "\t" + url.toString() + "\t" + json + "\n";

            sb.append(string);
            if (i % 30 == 0) {
                bw.write(sb.toString());
                sb.delete(0, sb.length());
                bw.flush();
            }
            Thread.sleep(1100);
        }
        bw.write(sb.toString());
        bw.flush();
        sb.delete(0, sb.length());
        System.out.println("out");
        bw.close();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        String start = "2014-11-17";
        String end = "2014-11-22";
        String path = "/work/data/wallstreetcn/calendar." + start + "." + end + ".txt";
//        Files.write(Paths.get(path), )
        WallSCalendarMapper wscm = new WallSCalendarMapper();
        wscm.writeFiles(start, end, path);
    }
}
