package com.xlzhang.preview.base;



/**
 * 功能: TODO.<br/>
 * date: 2016年8月9日 上午11:28:24 <br/>
 *
 * @author xlzhang@wisdombud.com
 * @version
 * @since JDK 1.7
 */
public class RollbackException extends RuntimeException {
    private static final long serialVersionUID = 447107166201097344L;

    public RollbackException(String msg) {
        super(msg);
    }

    public RollbackException(String msg, Throwable e) {
        super(msg, e);
    }
}
