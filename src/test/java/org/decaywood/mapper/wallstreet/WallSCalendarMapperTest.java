package org.decaywood.mapper.wallstreet;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by seven on 13/07/16.
 */
public class WallSCalendarMapperTest {
    @Test
    public void mapLogic() throws Exception {
        List<String> strings = Arrays.asList("2016-07-01", "2016-07-13");
        assertEquals(new WallSCalendarMapper().mapLogic(strings).split("\n").length, 228);
    }

}