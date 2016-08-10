/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50622
Source Host           : 127.0.0.1:3306
Source Database       : exam_questions

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2016-08-09 10:10:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pdf_preview
-- ----------------------------
DROP TABLE IF EXISTS `pdf_preview`;
CREATE TABLE `pdf_preview` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BASE_ID` varchar(64) DEFAULT NULL COMMENT '关联附件ID',
  `IMG_PATH` text COMMENT '生成预览切片路径',
  `SORT_FLAG` int(11) DEFAULT NULL COMMENT '排序字段',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
