# FastDFS

## 介绍

技术论坛： http://bbs.chinaunix.net/forum-240-1.html

FAQ：http://bbs.chinaunix.net/thread-1920470-1-1.html

资源地址： https://sourceforge.net/projects/fastdfs/

源码资源： https://github.com/happyfish100

 FastDFS是一个开源的轻量级分布式文件系统，它对文件进行管理，功能包括：文件存储、文件同步、文件访问（文件上传、文件下载）等，解决了大容量存储和负载均衡的问题。特别适合以文件为载体的在线服务，如相册网站、视频网站等等。

FastDFS为互联网量身定制，充分考虑了冗余备份、负载均衡、线性扩容等机制，并注重高可用、高性能等指标，使用FastDFS很容易搭建一套高性能的文件服务器集群提供文件上传、下载等服务。

FastDFS服务端有两个角色：跟踪器（tracker）和存储节点（storage）。跟踪器主要做调度工作，在访问上起负载均衡的作用。

存储节点存储文件，完成文件管理的所有功能：就是这样的存储、同步和提供存取接口，FastDFS同时对文件的metadata进行管理。所谓文件的meta data就是文件的相关属性，以键值对（key value）方式表示，如：width=1024，其中的key为width，value为1024。文件metadata是文件属性列表，可以包含多个键值对。

跟踪器和存储节点都可以由一台或多台服务器构成。跟踪器和存储节点中的服务器均可以随时增加或下线而不会影响线上服务。其中跟踪器中的所有服务器都是对等的，可以根据服务器的压力情况随时增加或减少。

为了支持大容量，存储节点（服务器）采用了分卷（或分组）的组织方式。存储系统由一个或多个卷组成，卷与卷之间的文件是相互独立的，所有卷的文件容量累加就是整个存储系统中的文件容量。一个卷可以由一台或多台存储服务器组成，一个卷下的存储服务器中的文件都是相同的，卷中的多台存储服务器起到了冗余备份和负载均衡的作用。

在卷中增加服务器时，同步已有的文件由系统自动完成，同步完成后，系统自动将新增服务器切换到线上提供服务。

当存储空间不足或即将耗尽时，可以动态添加卷。只需要增加一台或多台服务器，并将它们配置为一个新的卷，这样就扩大了存储系统的容量。

FastDFS中的文件标识分为两个部分：卷名和文件名，二者缺一不可。



### 谁在用？

有人在生产环境中使用FastDFS吗？
答案是肯定的。据我所知，截止2012年底至少有25家公司在使用FastDFS，其中有好几家是做网盘的公司。
其中存储量最大的一家，集群中存储group数有400个，存储服务器超过800台，存储容量达到6PB，文件数超过1亿，Group持续增长中。。。
以下是使用FastDFS的用户列表：
  某大型网盘（因对方要求对公司名保密，就不提供名字了。有400个group，存储容量达到了6PB，文件数超过1亿）  
  UC （http://www.uc.cn/，存储容量超过10TB）
  支付宝（http://www.alipay.com/）
  京东商城（http://www.360buy.com/）
  淘淘搜（http://www.taotaosou.com/）
  飞信（http://feixin.1008**/）
  赶集网（http://www.ganji.com/）
  淘米网（http://www.61.com/）
  迅雷（http://www.xunlei.com/）
  蚂蜂窝（http://www.mafengwo.cn/）
  丫丫网（http://www.iyaya.com/）
  虹网（[http://3g.ahong.com](http://3g.ahong.com/)）
  5173（http://www.5173.com/）
  华夏原创网（http://www.yuanchuang.com/）
  华师京城教育云平台（http://www.hsjdy.com.cn/）
  视友网（http://www.cuctv.com/）
  搜道网（http://www.sodao.com/）
  58同城（http://www.58.com/）
  商务联盟网（http://www.biz72.com/）
  中青网（http://www.youth.cn/）
  缤丽网 （http://www.binliy.com/）
  飞视云视频（http://www.freeovp.com/）
  梦芭莎（http://www.moonbasa.com/）
  活动帮（[www.eventsboom.com](http://www.eventsboom.com/)）
  51CTO（http://www.51cto.com/）
  搜房网（http://www.soufun.com/）



### 架构图

- racker Server：跟踪服务器，主要做调度工作，在访问上起负载均衡的作用。记录storage server的状态，是连接Client和Storage server的枢纽。

-  Storage Server：存储服务器，文件和meta data都保存到存储服务器上

- group：组，也称为卷。同组内服务器上的文件是完全相同的

- 文件标识：包括两部分：组名和文件名（包含路径）

-  meta data：文件相关属性，键值对（Key Value Pair）方式，如：width=1024,heigth=768



![arch](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image002.jpg)

### 上传流程

![upload](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image001.jpg)

- client询问tracker上传到的storage，不需要附加参数；

- tracker返回一台可用的storage；

- client直接和storage通讯完成文件上传

### 下载流程

![download](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image001.jpg)

- client询问tracker下载文件的storage，参数为文件标识（组名和文件名）；

- tracker返回一台可用的storage；

- client直接和storage通讯完成文件下载。

###  FastDFS和其他文件存储的简单对比

| **指标**               | **FastDFS** | **NFS** | **集中存储设备**   **如NetApp****、NAS** |
| ---------------------- | ----------- | ------- | ---------------------------------------- |
| 线性扩容性             | 高          | 差      | 差                                       |
| 文件高并发访问性能     | 高          | 差      | 一般                                     |
| 文件访问方式           | 专有API     | POSIX   | 支持POSIX                                |
| 硬件成本               | 较低        | 中等    | 高                                       |
| 相同内容文件只保存一份 | 支持        | 不支持  | 不支持                                   |



apache httpd 1.x



| **指标**                  | **FastDFS**                                                  | **mogileFS**                                                 |
| ------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 系统简洁性                | 简洁   只有两个角色：tracker和storage                        | 一般   有三个角色：tracker、storage和存储文件信息的mysql db  |
| 系统性能                  | 很高（没有使用数据库，文件同步直接点对点，不经过tracker中转） | 高（使用mysql来存储文件索引等信息，文件同步通过tracker调度和中转） |
| 系统稳定性                | 高（C语言开发，可以支持高并发和高负载）                      | 一般（Perl语言开发，高并发和高负载支持一般）                 |
| 软RAID方式                | 分组（组内冗余），灵活性较大                                 | 动态冗余，灵活性一般                                         |
| 通信协议                  | 专有协议   下载文件支持HTTP                                  | HTTP                                                         |
| 技术文档                  | 较详细                                                       | 较少                                                         |
| 文件附加属性（meta data） | 支持                                                         | 不支持                                                       |
| 相同内容文件只保存一份    | 支持                                                         | 不支持                                                       |
| 下载文件时支持文件偏移量  | 支持                                                         | 不支持                                                       |

 

## 安装

## JavaApi

https://github.com/tobato/FastDFS_Client

RAID

配置

```
fdfs:
  so-timeout: 1500
  connect-timeout: 600
  tracker-list:
  - 192.168.150.13:22122
```



### 上传文件



```
  // 元数据
  Set<MetaData> metaDataSet = new HashSet<MetaData>();
        metaDataSet.add(new MetaData("Author", "yimingge"));
        metaDataSet.add(new MetaData("CreateDate", "2016-01-05"));
        
        
  	try {
			StorePath uploadFile = null;
			uploadFile = fc.uploadFile(filename.getInputStream(), filename.getSize(), getFileExtName(filename.getOriginalFilename()), metaDataSet);
	
			account.setPassword(password);
			account.setLocation(uploadFile.getPath());
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
```

获取文件后缀

```
	private String getFileExtName(String name) {
		// TODO Auto-generated method stub
		return (name.substring(name.lastIndexOf(".")+1));
	}
	
```

或

```
FilenameUtils.getExtension
```

**返回结果带group**

uploadFile.getFullPath() ： group1/M00/00/00/wKiWDV0u7ZKALKtNAAADP9sEx2w432.sql

**不带group**

uploadFile.getPath() ： M00/00/00/wKiWDV0u7ZKALKtNAAADP9sEx2w432.sql



### 缩略图

配置

```
  thumb-image:
    width: 150
    height: 150
```



```
uploadFile  = fc.uploadImageAndCrtThumbImage(filename.getInputStream(), filename.getSize(), FilenameUtils.getExtension(filename.getOriginalFilename()), metaDataSet);
		
```

![1563360427239](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1563360427239.png)

### 下载文件

	@RequestMapping("/down")
		@ResponseBody
		public ResponseEntity<byte[]> down(HttpServletResponse resp) {
			
			DownloadByteArray cb = new DownloadByteArray();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "aaa.xx");
			byte[] bs = fc.downloadFile("group1", "M00/00/00/wKiWDV0vAb-AcOaYABf1Yhcsfws9181.xx", cb);
			
		return new ResponseEntity<>(bs,headers,HttpStatus.OK);
		}
