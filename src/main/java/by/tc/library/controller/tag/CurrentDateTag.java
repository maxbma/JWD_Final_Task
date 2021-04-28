package by.tc.library.controller.tag;

import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CurrentDateTag extends TagSupport {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final static Logger logger = Logger.getLogger(CurrentDateTag.class);

    public int doStartTag() throws JspException{
        JspWriter out = pageContext.getOut();
        try{
            Timestamp date = new Timestamp(System.currentTimeMillis());
            String currentTime = sdf.format(date);
            out.print(currentTime);
        } catch (Exception e) {
            logger.error(e);
        }
        return SKIP_BODY;
    }
}
