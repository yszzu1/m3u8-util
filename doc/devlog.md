

默认会占用并清空D:/tmp文件夹

单个url地址的文件分片不能超过6位数
一个分片偿试下载3次

运行方式:
java -jar m3u8-util-0.0.1-SNAPSHOT.jar   http://www.baidu.com/aaa.m3u8

输出文件D:/xxx.ts

不支持多url同时下载，第二个任务会删掉第一个任务的文件

支持点播方式， 不支持实时视频


其他格式可以使用chrome的插件 video-downloader