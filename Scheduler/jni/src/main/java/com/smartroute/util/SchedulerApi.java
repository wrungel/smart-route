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

    /**
     * The basic routine for generating scheduler-suggestions.
     * Should be called for incoming orders.
     * It trys to find a profitable assignment to maximize the win.
     * The suggestions are returned which have better added value than some threshold in the schedulers configuration
     *
     * @param orderIDs Database-Ids of orders
     * @param maxSuggestionsPerOrder
     * @return xml
     */
    public native String fnTryScheduleFavorable(int[] orderIDs, int maxSuggestionsPerOrder);

    /**
     * This routine is for generating 'best available' scheduler-suggestions.
     * It should be called for orders, whose dacay time comes near.
     * The suggestions are returned which have better added value than some threshold in the schedulers configuration
     *
     * @param orderIDs Database-Ids of orders
     * @param maxSuggestionsPerOrder
     * @return xml
     */
    public native String fnBestAvailable(int[] orderIDs, int maxSuggestionsPerOrder);

    /**
     * Gives a price estimate for an order.
     * To be called before the order is given up, thu its not in hte database yet.
     *
     * @param orderXML
     * @return price estimate in euro-cents
     */
    private native int fnEstimatePrice(String orderXML);

    static final String SCHEDULER_LIB = "smart-route-scheduler";
    
    static {
        System.loadLibrary(SCHEDULER_LIB);
        System.out.println("Library loaded");
    }

    public static void main(String[] args)
    {

        SchedulerApi scheduler = new SchedulerApi();

        String favorableXML = scheduler.fnTryScheduleFavorable(new int[]{1}, 2);
        System.out.println("favorableXML = " + favorableXML);

        String bestAvailableXML = scheduler.fnBestAvailable(new int[]{1}, 2);
        System.out.println("bestAvailable = " + bestAvailableXML);

        int priceEstimate = scheduler.fnEstimatePrice("<order> </order>");
        System.out.println("priceEstimate = " + priceEstimate);
    }

}
