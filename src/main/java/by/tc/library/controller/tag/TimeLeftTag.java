package by.tc.library.controller.tag;

import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeLeftTag extends TagSupport {

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final static Logger logger = Logger.getLogger(CurrentDateTag.class);
    private final static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    private String returnDate;

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public int doStartTag() throws JspException{
        JspWriter out = pageContext.getOut();
        try{
            out.print(timeLeft(returnDate));
        } catch (Exception e) {
            logger.error(e);
        }
        return SKIP_BODY;
    }

    public String timeLeft(String returnDate) throws ParseException {
        Date dateToReturn = sdf.parse(returnDate);
        if(dateToReturn.getTime() - System.currentTimeMillis() < 0){
            return "—";
        }
        long timeLeft = dateToReturn.getTime() - System.currentTimeMillis();
        final long millisecInMinute = 1000*60;
        final long millisecInHour = millisecInMinute*60;
        final long millisecInDay = millisecInHour*24;
        if(timeLeft < millisecInHour){
            return  timeLeft/millisecInMinute + "m";
        } else if(timeLeft < millisecInDay){
            long hours = timeLeft/millisecInHour;
            long minutes = (timeLeft - hours*millisecInHour)/millisecInMinute;
            return hours + "h " + minutes + "m";
        } else{
            long days = timeLeft/millisecInDay;
            long hours = (timeLeft - days*millisecInDay)/millisecInHour;
            long minutes = (timeLeft - days*millisecInDay - hours*millisecInHour)/millisecInMinute;
            return days + "d " + hours + "h " + minutes + "m";
        }
    }


}
