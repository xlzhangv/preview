package com.xlzhang.preview.pojo;

// default package
// Generated 2015-8-11 9:19:48 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 功能: TODO.<br/>
 * date: 2015年8月26日 下午7:44:58 <br/>
 *
 * @author xlzhang@wisdombud.com
 * @version
 * @since JDK 1.7
 */
@Entity
@Table(name = "PDF_PREVIEW")
public class PdfPreview implements java.io.Serializable {

    private static final long serialVersionUID = -6996039866973777468L;
    private Long id;
    private String baseId;
    private String imgPath;
    private Integer sortFlag;

    public PdfPreview() {
    }

    public PdfPreview(String baseId, String imgPath, Integer sortFlag) {
        super();
        this.baseId = baseId;
        this.imgPath = imgPath;
        this.sortFlag = sortFlag;
    }

    public PdfPreview(final Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Column(name = "BASE_ID")
    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    @Column(name = "IMG_PATH")
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Column(name = "SORT_FLAG")
    public Integer getSortFlag() {
        return this.sortFlag;
    }

    public void setSortFlag(final Integer sortFlag) {
        this.sortFlag = sortFlag;
    }

}
