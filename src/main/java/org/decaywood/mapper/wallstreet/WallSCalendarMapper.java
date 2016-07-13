package org.decaywood.mapper.wallstreet;

import com.fasterxml.jackson.databind.JsonNode;
import org.decaywood.mapper.AbstractMapper;
import org.decaywood.timeWaitingStrategy.TimeWaitingStrategy;
import org.decaywood.utils.DateParser;
import org.decaywood.utils.RequestParaBuilder;
import org.decaywood.utils.URLMapper;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        for(int i=0; i<dateLists.size()-1; i++) {
            String target = URLMapper.WALLSTREETCN_CALENDAR.toString();
            System.out.println(dateLists.get(i) + dateLists.get(i+1));
            RequestParaBuilder builder = new RequestParaBuilder(target)
                    .addParameter("start", dateLists.get(i))
                    .addParameter("end", dateLists.get(i+1));
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


}
