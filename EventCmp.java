import java.util.Comparator;
import java.lang.Double;

class EventCmp implements Comparator<Event> {
    public int compare(Event e1, Event e2) {
        if (Double.compare(e1.getTime(), e2.getTime()) == 0) {
            return e1.getNumber() - e2.getNumber();
        } 
        if (e1.getTime() > e2.getTime()) { 
            return 1; 
        }
        return -1;
    }
}
