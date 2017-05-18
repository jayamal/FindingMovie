package utils;

import java.util.UUID;

/**
 * Created by jayamalk on 10/3/2016.
 */
public class AnalyticsUtil {

    public static final String TRACK_ID = "UA-85128701-1";
    private static String cid = UUID.randomUUID().toString();

    public enum EventType{
        OPEN_APP(0, "General", "OpenApp"),
        SEARCH_START(1, "General", "SearchStart"),
        SEARCH_END(2, "General", "SearchEnd"),
        OPEN(3, "General", "Open"),
        SAVE(4, "General", "Save");
        private int type;
        private String eventCat;
        private String eventLbl;

        EventType(int type, String eventCat, String eventLbl) {
            this.type = type;
            this.eventCat = eventCat;
            this.eventLbl = eventLbl;
        }

        public int getType() {
            return type;
        }

        public String getEventCat() {
            return eventCat;
        }

        public String getEventLbl() {
            return eventLbl;
        }
    }

    public static void notifyEvent(final EventType eventType){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String eventValue = "1";
                    String trackId = TRACK_ID;
                    String eventCat = eventType.getEventCat();
                    String eventLbl = eventType.getEventLbl();
                    String url = "https://www.google-analytics.com/collect?payload_data&v=1&t=event&tid=" + TRACK_ID + "&cid=" + cid + "&ec=" + eventCat + "&ea=" + eventLbl + "&el=" + eventLbl + "&ev=1";
                    RestUtils.makeRestCall(url);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.run();
    }
}
