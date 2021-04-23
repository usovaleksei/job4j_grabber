import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TriggerTest {
    @Test
    public void testMethod() {
        int rsl = Trigger.test();
        assertThat(rsl, is(2));
    }
}
