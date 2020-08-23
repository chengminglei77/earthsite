<html >
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0,viewport-fit=cover">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta name="format-detection" content="telephone=no">
        
		<style>
			@font-face{
				 font-family: 'TimesNewRoman'; 
				 src: url('http://lovexiancity.xpu.edu.cn/Times_New_Roman.ttf');
			}
			@font-face{
				 font-family: 'TimesNewRomanBold'; 
				 src: url('http://lovexiancity.xpu.edu.cn/Times_New_Roman_Bold.ttf');
			}
			
			* {
				font-family:TimesNewRoman;
				margin: 0;
				padding: 0;
				-webkit-tap-highlight-color: transparent;
				outline: none;
			}
			
			
			body {
				background-color:white;
				
			}
			
			img{
				width: 100%;
				object-fit:cover;
				padding:10px 0;
			}
			
			.article-title {
				font-size: 30px;
				font-weight: 600;
				line-height: 42px;
				text-align: left;
				width: 100%;
				font-family: TimesNewRomanBold;
			}
			.article_title_highlighter {
				width: 20px;
				height: 4px;
			}
			
			.publish_time {
				color: #737373;
				font-size: 12px;
				margin-bottom: 3px;
				overflow: hidden;
				height: 20px;
				line-height: 20px;
				width: calc(100vw - 40px);
			}
			
			p span {
				line-height: 1.5;
				word-wrap: break-word;
				white-space: normal;
				word-break:break-all;
			}
			
			.container {
				padding: 24px;
				padding-top: 10px;
				overflow: auto;
				position: relative;
				background-size: 100% 100%;
			}
			
			p {
				margin-bottom:10px;
				font-size:18px;
			}
			
			strong {
				font-size:20px;
			}
			
			html, body {
				
				-webkit-overflow-scrolling: touch;
				max-height: 100vh;
				background-color: #fff;
				overflow: auto;
				z-index: 1;
			}
			video {
				display:block;
				width:100%;
				-moz-border-radius: 0.5em;
				-webkit-border-radius: 0.5em;
				border-radius:0.5em;
				padding-top:5px;
				padding-bottom:5px;
			}
			
			
		</style>
    </head>
    <body >
	
		<div style="width: 100%; height: 100%;">
			<div class="container">
				<div>
				   <p>
					<span style="font-size: 24px;"><strong class="article-title"><span style="color: rgb(43, 43, 43);">${articleTitle}</span></strong></span>
				   </p>
				</div>
				<div class="article_title_highlighter" style="background-color: rgb(38, 38, 38);"></div>
				<div id="copyright" style="width: 100%; height: auto; overflow: unset; margin-top: 12px; transition: height 0.2s ease 0s;">
					<div class="publish_time">
						  PublishTime:${pubtime}
					</div>
				</div>
				
				
				<div>
					${content}
				</div>
			</div>
			
		</div>
        
    </body>
</html>
