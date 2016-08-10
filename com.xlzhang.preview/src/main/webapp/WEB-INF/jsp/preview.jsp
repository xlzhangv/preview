<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge chrome=1">
        <meta name="renderer" content="webkit">
        <link rel="icon" href="favicon.ico" type="image/x-icon">
         <script src="/resources/js/base.js"></script>
        <script src="/resources/3rd-js/jquery/jquery-1.11.3.min.js"></script>
        <title>预览</title>
           <style type="text/css">
               body{ background:url(${contextPath}/resources/images/main_bg.png) #F3F3F4 repeat-y 3px 0px;padding-left:25px;border:0;width:97.5%;height:100%;overflow-y: auto;}
          	   #preview-body{
          	   	 text-align: center;
          	   }
          	   .vertical{
          	   		 margin:10px auto;
          	   		 display: block;
          	   }
          	   .horizontal{
          	   		margin:10px;
          	   }
          </style>
    </head>
    <body>
    	<div style="position: fixed; right:5px;">
    		<button id="vertical">垂直</button>
    		<button id="horizontal">平铺</button>
    		<button id="min">缩小</button>
    		<button id="max">放大</button>
    		<button id="reduction">还原</button>
    	</div>
        <div id="preview-body" class="preview-body">
        	
        
        </div>
        <script>
            var fileId='<%=request.getParameter("fileId")%>';
            var relativePath='<%=request.getParameter("relativePath")%>';
            var fileName='<%=request.getParameter("fileName")%>';
        </script>
        <script src="/resources/js/preview.js"></script>
    </body>
</html>