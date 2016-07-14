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
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author: decaywood
 * @date: 2015/11/27 10:48
 */


/**
 * 当前日期 -> 龙虎榜映射器
 */
public class WallSLiveNewsMapper extends AbstractMapper<String, String> {

    public WallSLiveNewsMapper() throws RemoteException {
        this(null);
    }

    /**
     * @param strategy 超时等待策略（null则设置为默认等待策略）
     */
    public WallSLiveNewsMapper(TimeWaitingStrategy strategy) throws RemoteException {
        super(strategy);
    }

    @Override
    public String mapLogic(String s) throws Exception {
        String target = URLMapper.WALLSTREETCN_LIVENEWS.toString();
        RequestParaBuilder builder = new RequestParaBuilder(target);
        URL url = new URL(builder.build());
        String json = request(url);
        JsonNode node = mapper.readTree(json);
        return processNode(node);

    }

    private String processNode(JsonNode node) {
        StringBuffer sb = new StringBuffer();
        for (JsonNode jsonNode : node.get("results")) {
            String title = jsonNode.get("title").asText();
            String createdAt = jsonNode.get("createdAt").asText();
            System.out.println(title);
            System.out.println(createdAt);
            sb.append(title).append("\n");
        }
        return sb.toString();
    }

    private void writeFiles(int start, int end, String path) throws IOException, InterruptedException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        StringBuffer sb = new StringBuffer();

        for (int i = start; i <= end; i++) {
            System.out.println(i);
            String target = URLMapper.WALLSTREETCN_LIVENEWS.toString();
            //status=published&order=-created_at&page=3&channelId=1&extractImg=1&extractText=1
            RequestParaBuilder builder = new RequestParaBuilder(target)
                    .addParameter("status", "published")
                    .addParameter("order", "-created_at")
                    .addParameter("page", i)
                    .addParameter("channelId", "1")
                    .addParameter("extractImg", "1")
                    .addParameter("extractText", "1");
            URL url = new URL(builder.build());
            String json = request(url);
            String string = url.toString() + "\t" + json + "\n";

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
        int start = 4220;
        int end = 6480;
        String path = "/work/data/wallstreetcn/livenews." + start + "." + end + ".txt";
        WallSLiveNewsMapper wsnm = new WallSLiveNewsMapper();
        wsnm.writeFiles(start, end, path);
    }


}
