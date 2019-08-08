# Tengine 补充

## 开机启动

```
chkconfig --list
chkconfig --add nginx
chkconfig nginx on
```

## 时间问题

```
service ntpd status

```



## 虚拟目录

       location /www {
            alias  /var/data/www1;
            index  index.html index.htm a.html;
        }
## 自动索引

       location /art {
            alias  /var/data/www1/;
              autoindex on;
       
        }
## 动静分离

```
      location / {
		proxy_pass http://192.168.150.11:803;
    
        }

     
     
     location ~ .*\.(gif|jpg|jpeg|png|bmp|swf|html|htm|css|js)$ {
            root  /var/data/www1/;
	       
        }

```

# SSL



![enter image description here](http://images.gitbook.cn/3312b3d0-1d6b-11e8-a1b7-770e0be79dd9)

SSL 能够帮助系统在客户端和服务器之间建立一条安全通信通道。SSL 安全协议是由 Netscape Communication 公司在 1994 年设计开发，SSL 依赖于加密算法、极难窃听、有较高的安全性，因此 SSL 协议已经成为网络上最常用的安全保密通信协议，该安全协议主要用来提供对用户和服务器的认证；对传送的数据进行加密和隐藏；确保数据在传送中不被改变，即数据的完整性，现已成为该领域中全球化的标准。

### SSL 和 TLS

所有的X.509证书包含以下数据：

1、X.509版本号：指出该证书使用了哪种版本的X.509标准，版本号会影响证书中的一些特定信息。目前的版本是3。

2、证书持有人的[公钥](https://baike.baidu.com/item/公钥)：包括证书持有人的公钥、算法(指明密钥属于哪种密码系统)的标识符和其他相关的密钥参数。

3、证书的序列号：由CA给予每一个证书分配的唯一的数字型编号，当证书被取消时，实际上是将此证书序列号放入由CA签发的CRL（Certificate Revocation List证书作废表，或证书黑名单表）中。这也是序列号唯一的原因。

4、主题信息：证书持有人唯一的标识符(或称DN-distinguished name)这个名字在 Internet上应该是唯一的。DN由许多部分组成，看起来象这样：

CN=Bob Allen, OU=Total Network Security Division

O=Network Associates, Inc.

C=US

这些信息指出该科目的通用名、组织单位、组织和国家或者证书持有人的姓名、服务处所等信息。

5、证书的有效期：证书起始日期和时间以及终止日期和时间；指明证书在这两个时间内有效。

6、认证机构：证书发布者，是签发该证书的实体唯一的CA的X.509名字。使用该证书意味着信任签发证书的实体。(注意：在某些情况下，比如根或顶级CA证书，发布者自己签发证书)

7、发布者的数字签名：这是使用发布者[私钥](https://baike.baidu.com/item/私钥)生成的签名，以确保这个证书在发放之后没有被撰改过。

8、签名算法标识符：用来指定CA签署证书时所使用的签名算法。算法标识符用来指定CA签发证书时所使用的[公开密钥](https://baike.baidu.com/item/公开密钥)算法和HASH算法。



## 抓包工具

### 青花瓷

https://www.charlesproxy.com/latest-release/download.do

## 对称加密与非对称加密

非对称加密算法需要两个密钥：公开密钥（publickey）和私有密钥（privatekey），公开密钥与私有密钥是一对，如果用公开密钥对数据进行加密，只有用对应的私有密钥才能解密；如果用私有密钥对数据进行加密，那么只有用对应的公开密钥才能解密。因为加密和解密使用的是两个不同的密钥，所以这种算法叫作非对称加密算法。

## CA（Certificate Authority）

CA 是负责签发证书、认证证书、管理已颁发证书的机关。它要制定政策和具体步骤来验证、识别用户身份，并对用户证书进行签名，以确保证书持有者的身份和公钥的拥有权。

![img](https://img.alicdn.com/tfs/TB1JNDiVNjaK1RjSZFAXXbdLFXa-796-76.png)

常见的ca厂商

- Symantec
- Comodo
- Godaddy
- GlobalSign
- Digicert
- VeriSign

- GeoTrust
- Thawte
- Network Solutions

 CA 供应商之间的区别主要有：机构品牌、证书加密方式、保险额度、服务与质量、浏览器支持率等

## 证书种类



- DV（Domain Validation）证书只进行域名的验证，一般验证方式是提交申请之后CA会往你在whois信息里面注册的邮箱发送邮件，只需要按照邮件里面的内容进行验证即可。

- OV（Organization Validation）证书在DV证书验证的基础上还需要进行公司的验证，一般他们会通过购买邓白氏等这类信息库来查询域名所属的公司以及这个公司的电话信息，通过拨打这个公司的电话来确认公司是否授权申请OV证书。

- EV证书一般是在OV的基础上还需要公司的金融机构的开户许可证，不过不同CA的做法不一定一样，例如申请人是地方政府机构的时候是没有金融机构的开户证明的，这时候就会需要通过别的方式去鉴别申请人的实体信息。

![83cf43c8886ff637c1eb77a319bb0fc5a327e3e5](https://oss.aliyuncs.com/yqfiles/83cf43c8886ff637c1eb77a319bb0fc5a327e3e5.jpeg)

## 多网站公用同一证书

## OPenSSL 自签名

certmgr.msc

下载 

http://slproweb.com/products/Win32OpenSSL.html

### 生成私钥

genrsa

制台输入 genrsa，会默认生成一个 2048 位的私钥

```
openssl genrsa -des3 -out server.key 1024
```



### 由私钥生成证书

```
openssl req -new -key c:/dev/my.key -out c:/dev/my.csr
openssl req -new -key server.key -out server.csr
```

查看证书

 Common Name，这里输入的域名即为我们要使用 HTTPS 访问的域名

```
req -text -in c:/dev/my.csr -noout
```



生成解密的key 

```
openssl rsa -in server.key -out server.key.unsecure
```



### 签名

工具

https://sourceforge.net/projects/xca/

```
 x509 -req -days 365 -in c:/dev/my.csr -signkey c:/dev/my.key -out c:/dev/sign.crt
 openssl x509 -req -days 365 -in server.csr -signkey server.key.unsecure -out server.crt

```

查看证书

```
x509 -text -in c:\openSSLDemo\fd.crt -noout
```

- Country Name (2 letter code) [XX]:CN           #请求签署人的信息
- State or Province Name (full name) []: #请求签署人的省份名字
- Locality Name (eg, city) [Default City]:# 请求签署人的城市名字
- Organization Name (eg, company) [Default Company Ltd]:#请求签署人的公司名字
- Organizational Unit Name (eg, section) []:#请求签署人的部门名字
- Common Name (eg, your name or your server's hostname) []:#这里一般填写请求人的的服务器域名，