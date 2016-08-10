/**
 * Date:2015年5月13日 下午2:40:29
 * Copyright (c) 2015, www.wisdombud.com All Rights Reserved.
 */

package com.xlzhang.preview.service;

import java.util.List;

import com.xlzhang.preview.pojo.PdfPreview;

public interface PdfPreviewSrv {

    List<PdfPreview> findByBaseId(String baseId);

    void save(PdfPreview pdfPreview);

}
