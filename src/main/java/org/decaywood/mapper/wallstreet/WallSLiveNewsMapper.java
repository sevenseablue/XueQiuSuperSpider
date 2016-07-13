package org.decaywood.mapper.wallstreet;

import com.fasterxml.jackson.databind.JsonNode;
import org.decaywood.mapper.AbstractMapper;
import org.decaywood.timeWaitingStrategy.TimeWaitingStrategy;
import org.decaywood.utils.RequestParaBuilder;
import org.decaywood.utils.URLMapper;

import java.net.URL;
import java.rmi.RemoteException;

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

//        String dateParam = DateParser.getTimePrefix(date, false);

        String target = URLMapper.WALLSTREETCN_LIVENEWS.toString();
        RequestParaBuilder builder = new RequestParaBuilder(target);
//                .addParameter("date", dateParam);
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


}
