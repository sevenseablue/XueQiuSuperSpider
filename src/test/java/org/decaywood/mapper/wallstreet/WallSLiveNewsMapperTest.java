package org.decaywood.mapper.wallstreet;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by seven on 13/07/16.
 */
public class WallSLiveNewsMapperTest {
    @Test
    public void mapLogic() throws Exception {
        assertEquals(new WallSLiveNewsMapper().mapLogic("").split("\n").length, 60);
    }

}