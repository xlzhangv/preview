/**
 * Date:2015年5月13日 下午2:40:29
 * Copyright (c) 2015, www.wisdombud.com All Rights Reserved.
 */

package com.xlzhang.preview.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlzhang.preview.dao.PdfPreviewDao;
import com.xlzhang.preview.pojo.PdfPreview;
import com.xlzhang.preview.service.PdfPreviewSrv;

/**
 * 功能: TODO.<br/>
 * date: 2015年7月31日 下午4:46:08 <br/>
 *
 * @author lilei@wisdombud.com
 * @version
 * @since JDK 1.7
 */
@Service
public class PdfPreviewSrvImpl implements PdfPreviewSrv {

    @Autowired
    private PdfPreviewDao previewDao;

    @Override
    public List<PdfPreview> findByBaseId(String baseId) {
        // TODO Auto-generated method stub
        return previewDao.findByBaseId(baseId);
    }

    @Override
    public void save(PdfPreview pdfPreview) {
        // TODO Auto-generated method stub
        previewDao.save(pdfPreview);
    }
}
