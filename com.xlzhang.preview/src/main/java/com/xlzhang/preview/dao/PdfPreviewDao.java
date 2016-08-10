package com.xlzhang.preview.dao;

import java.util.List;

import com.xlzhang.preview.pojo.PdfPreview;

public interface PdfPreviewDao {

    List<PdfPreview> findByBaseId(String baseId);

    void save(PdfPreview pojo);

}
