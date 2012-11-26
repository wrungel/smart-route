package com.smartroute.util;

import java.util.Vector;

/**
 * <p>
 * Scheduler C++/Java bridge.
 * </p>
 * 
 * Set the system variable <code>java.library.path</code> to path containing dynamic libraries:
 * <ul>
 * <li>smart-route-linux.so or smart-route-win32.dll on windows</li>
 * <li>mysqlcppconn.so (.dll)</li>
 * <li>boost_date_time.so (.dll)</li>
 * <li>libmysql.so (.dll)</li>
 * </ul>
 * Example:
 * <blockquote><code>-Djava.library.path=E:\BIN\mysql-connector-c++-noinstall-1.1.1-win32\lib;E:\BIN\boost_1_52_0_libs\lib32;E:\BIN\mysql-5.5.28-win32\lib;C:\Users\maxx\.m2\repository\com\smartroute\smart-route-win32\0.0.1-SNAPSHOT"</code></blockquote>
 * 
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

    static final String SCHEDULER_LIB = "smart-route-win32-0.0.1-SNAPSHOT";
    
    static {
    	try {
    		java.lang.reflect.Field LIBRARIES = null;
			LIBRARIES = ClassLoader.class.getDeclaredField("loadedLibraryNames");
			@SuppressWarnings("unchecked")
			final Vector<String> libraries = (Vector<String>) LIBRARIES.get(SchedulerApi.class.getClassLoader());
			for (String s: libraries) {
				System.out.println("ALREADY LOADED: " + s);
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

    	//System.out.println(System.getProperty("java.library.path"));
    	System.loadLibrary("libmysql");
    	System.load("mysqlcppconn");
    	System.load("boost_date_time-vc100-mt-1_52");
        System.load(SCHEDULER_LIB);
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
