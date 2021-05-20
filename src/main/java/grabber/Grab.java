package grabber;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * interface for start parser
 */

public interface Grab {

    //method to start parser
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;
}
