package com.smartroute.util;

/**
 * <p>
 * Scheduler C++/Java bridge.
 * </p>
 * Set the system variable <code>java.library.path</code> to path containing the scheduler.so or scheduler.dll on windows, e.g:<br/> 
 * <blockquote><code>-Djava.library.path=D:\tmp\smart-route\Scheduler\windows\x64\Release</code></blockquote>
 * 
 * @author frol
 *
 */
public class SchedulerApi {
    private native String fnscheduler(String arg);

    static final String SCHEDULER_LIB = "scheduler";
    
    public static void main(String[] args)
    {
        System.loadLibrary(SCHEDULER_LIB);
        System.out.println("Library loaded");
        String ret = new SchedulerApi().fnscheduler("Hello, Scheduler!");
        System.out.println("ret = " + ret);
    }

}
