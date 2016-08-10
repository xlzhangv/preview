package com.xlzhang.preview.base;



import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.opensymphony.xwork2.ActionSupport;

public class AbstractCommonAction extends ActionSupport
    implements ServletRequestAware, ServletResponseAware, SessionAware {

    private static final Logger logger = Logger.getLogger(AbstractCommonAction.class);
    private static final long serialVersionUID = 1L;

    // 分页参数
    protected int start;
    protected int limit;
    protected long pageCount;

    protected int pageIndex;
    protected int pageSize;

    protected String sortField;
    protected String sortOrder;
    protected Map<String, Object> session;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    /**
     * final Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter())
     * .setDateFormat("yyyy-MM-dd").create();
     * 发�?�消�?.
     * 
     * @author ghlin
     * @param isSuccess
     * @param data
     * @param msg
     */
    public <T> void sendMsg(final boolean isSuccess, final T data, final String msg, final DateFormat... dateFormat) {
        final Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("success", isSuccess);
        responseMap.put("data", data);
        responseMap.put("message", msg);
        final GsonBuilder gsonBuilder = new GsonBuilder();
        if (dateFormat != null && dateFormat.length > 0) {
            gsonBuilder.setDateFormat(dateFormat[0].getFormat());
        }
        Gson gson = gsonBuilder.create();
        this.sendResponseMsg(gson.toJson(responseMap));
    }

    /**
     * 发�?�成功消�?.
     * 
     * @author ghlin
     * @param data
     * @param msg
     */
    public <T> void sendSuccessMsg(final T data, final String msg, final DateFormat... dateFormat) {
        this.sendMsg(true, data, msg, dateFormat);
    }

    /**
     * 发�?�成功消�?.
     * 
     * @author ghlin
     */
    public void sendSuccessMsg() {
        this.sendMsg(true, "", "", DateFormat.DateTime);
    }

    /**
     * 发�?�失败消�?.
     * 
     * @author ghlin
     * @param data
     * @param errorMsg
     */
    public <T> void sendFailMsg(final T data, final String errorMsg) {
        this.sendMsg(false, data, errorMsg, DateFormat.DateTime);
    }

    /**
     * 发�?�数组消�?.
     * 
     * @author ghlin
     */

    public <E> void sendArrayMsg(final Collection<E> collectObj) {
        this.sendResponseMsg(new Gson().toJson(collectObj));
    }

    /**
     * 发�?�respons消息.
     * 
     * @author ghlin
     * @param jsonMsg
     */
    public void sendResponseMsg(final String jsonMsg) {
        final HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(jsonMsg);
            writer.flush();
        } catch (final IOException e) {
            logger.error("Json convert send to page error!", e);
        } finally {
            if (null != writer) {
                writer.close();
                writer = null;
            }
        }
    }

    protected <T> T fromJsonToBean(final String params, final Class<T> clazz) throws UnsupportedEncodingException {
        return fromJsonToBean(params, clazz, null);
    }

    /**
     * @param params
     * @param clazz
     * @param deserializers
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("rawtypes")
    protected <T> T fromJsonToBean(final String params, final Class<T> clazz,
        final Map<Class, JsonDeserializer> deserializers) throws UnsupportedEncodingException {
        final String json = URLDecoder.decode(URLDecoder.decode(params, "UTF-8"), "UTF-8");
        final GsonBuilder gsonBuilder = new GsonBuilder();
        if (null != deserializers) {
            for (final Map.Entry<Class, JsonDeserializer> deserializer : deserializers.entrySet()) {
                gsonBuilder.registerTypeAdapter(deserializer.getKey(), deserializer.getValue());
            }
        }
        final Gson gson =
            gsonBuilder.setDateFormat(DateFormat.DateTime.getFormat()).create();
        return gson.fromJson(json, clazz);
    }

    /**
     * 记录cookie <br/>
     * 
     * @author zhang
     */
    public void addCookie(final String name, final String value) {

        Cookie passwordCookie = getCookie(name);
        if (null == passwordCookie) {
            passwordCookie = new Cookie(name, value);
            passwordCookie.setMaxAge(60 * 60 * 24);
            passwordCookie.setPath("/");
            ServletActionContext.getResponse().addCookie(passwordCookie);
        } else {
            passwordCookie.setValue(value);
            ServletActionContext.getResponse().addCookie(passwordCookie);
        }
    }

    public static Cookie getCookie(final String cookieName) {
        final Cookie[] cookies = ServletActionContext.getRequest().getCookies();
        if (null == cookies) {
            return null;
        }
        for (final Cookie cookie : cookies) {
            if (StringUtils.isBlank(cookie.getValue())) {
                continue;
            }
            if (!cookieName.equals(cookie.getName())) {
                continue;
            }
            return cookie;
        }
        return null;
    }

    /**
     * 功能: 获取访问IP.<br/>
     * date: 2016�?5�?4�? 下午4:39:19 <br/>
     *
     * @author xlzhang@wisdombud.com
     * @param request
     * @return
     */
    public String analysisRemoteIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }

    /**
     * start.
     * 
     * @return the start
     * @since JDK 1.6
     */
    public int getStart() {
        return this.start;
    }

    /**
     * limit.
     * 
     * @return the limit
     * @since JDK 1.6
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * start.
     * 
     * @param start the start to set
     * @since JDK 1.6
     */
    public void setStart(final int start) {
        this.start = start;
    }

    /**
     * limit.
     * 
     * @param limit the limit to set
     * @since JDK 1.6
     */
    public void setLimit(final int limit) {
        this.limit = limit;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(final long pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(final int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(final String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(final String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(final HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(final HttpServletResponse response) {
        this.response = response;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public void setServletRequest( HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse( HttpServletResponse response) {
        this.response = response;
    }

    public void setSession( Map<String, Object> session) {
        this.session = session;
    }

}
