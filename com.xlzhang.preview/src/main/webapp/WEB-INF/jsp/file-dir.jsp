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
        <title>文件夹</title>
           <style type="text/css">
               body{ background:url(${contextPath}/resources/images/main_bg.png) #F3F3F4 repeat-y 3px 0px;padding-left:25px;border:0;width:97.5%;height:100%;overflow-y: auto;}
          	   ul li{
          	   	list-style: none;
          	   }
          </style>
    </head>
    <body>
    	<a id="back_parent" href='javascript:void(0)' >返回上一级</a>
    	<ul id="dir">
    	
    	
    	</ul>
        <script type="text/javascript">
        	function moveDirPath(path){
        	    $.ajax({
            	    url:'/online-preview/file-dir.html',
            	    data:{
            			path : path
            	    },
            	    dataType:'json',
            	    type:'post',
            	    success:function(result){
            			if(result.parent_dir){
            			    $('#back_parent').data('dir',result.parent_dir);
            			}
            		    $('#dir').empty();
            			var dir="<li class='child-dir' data-dir='{file_path}'><a href='javascript:void(0)'>{filename}</a></li>";
            			var file="<li class='child-file' data-dir='{file_path}' data-filename='{filename}'><a href='javascript:void(0)'>{filename}</a></li>";
            			$.each(result.file_list,function(i,f){
            			    if(f.is_dir){
            				  $('#dir').append(dir.format(f));
            			    }else{
            				  $('#dir').append(file.format(f));
            			    }
            			   
            			});
            	    }
            	});
        	}
        	$(function(){
        	    moveDirPath();
        	    $('#back_parent').click(function(){
        			var dir=$(this).data('dir');
        			if(dir){
        			    moveDirPath(dir); 
        			}
        			
        	    });
        	    $('#dir').delegate('.child-dir','click',function(){
        			var filename=$(this).data('dir');
        			moveDirPath(filename);
        	    });
        	    $('#dir').delegate('.child-file','click',function(){
        			var filename = fileId =$(this).data('filename');
        			var relativePath=$(this).data('dir');
        			var params="fileId="+filename+"&fileName="+filename+"&relativePath="+relativePath;
					window.open('/online-preview/pdf-preview.html?'+params);
			    });
        	    
        	    
        	    
        	})
        	
        </script>
    </body>
</html>