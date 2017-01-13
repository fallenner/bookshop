/*
SQLyog Ultimate v8.32 
MySQL - 5.5.36 : Database - bookstore
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bookstore` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `bookstore`;

/*Table structure for table `address` */

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `id` varchar(32) NOT NULL,
  `detail` text,
  `acceptusername` varchar(20) DEFAULT NULL,
  `tel` varchar(11) DEFAULT NULL,
  `postcode` varchar(12) DEFAULT NULL,
  `is_default` varchar(2) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `status` tinyint(2) DEFAULT '1',
  `province` varchar(10) DEFAULT NULL,
  `city` varchar(10) DEFAULT NULL,
  `area` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `address` */

insert  into `address`(`id`,`detail`,`acceptusername`,`tel`,`postcode`,`is_default`,`username`,`status`,`province`,`city`,`area`) values ('3fbf352cf810435d861c978a68d7cb04','白云大道1号1','张三1','13027140633','1234561','0','dzq',1,'广东','广州','白云'),('7ba60a09485245cab958d7fa0740e877','白云大道1号','王五','13027140633','1234561','1','dzq123',1,'广东','广州','白云');

/*Table structure for table `book` */

DROP TABLE IF EXISTS `book`;

CREATE TABLE `book` (
  `bid` char(32) NOT NULL,
  `bname` varchar(100) DEFAULT NULL,
  `price` decimal(5,1) DEFAULT NULL,
  `author` varchar(20) DEFAULT NULL,
  `image` varchar(200) DEFAULT NULL,
  `cid` char(32) DEFAULT NULL,
  `sellcount` tinyint(4) DEFAULT '0',
  `blurb` text,
  `recommend` tinyint(2) DEFAULT '0' COMMENT '1代表推荐书籍，0代表普通书籍',
  PRIMARY KEY (`bid`),
  KEY `cid` (`cid`),
  CONSTRAINT `book_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `category` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `book` */

insert  into `book`(`bid`,`bname`,`price`,`author`,`image`,`cid`,`sellcount`,`blurb`,`recommend`) values ('050eb306984d4578a1502b00391abfe4','狗狗心事：它和你想的大不一样','21.3','费奈尔','/drWJrgxC/6Q4N6dqq/iIa8QUf1.jpg','2',0,'传统的驯狗方式——套项圈、惩罚、呵斥等在书中了无踪影。简·费奈尔在这部全球畅销书中，完全颠覆了我们看待狗狗的方式。她深入研究狗狗的隐形语言，并将其提炼成一种简单的指导原则，用来帮助每位主人和他们最好的朋友——狗狗，建立起一种全新的亲密关系。对狗狗的独特理解使她每到一处都能赢得热爱动物人士的尊敬，世界各地的狗狗主人被她在这《狗狗心事:它和你想的大不一样》中展现出的富有创造力的观点深深的打动着。\r\n　　本书向狗狗的主人们详细介绍了如何将费奈尔的方法引入狗狗的日常生活，从根本上改变狗狗的问题：在家中随地大小便，见到陌生人狂吠，出门散步麻烦不断。2周内，狗狗的主人就可以掌握一种让他们与狗狗生活得更美好、更幸福的实用训练体系。\r\n',0),('0ed6718d67444d16a852bfd728908ebc','从你的全世界路过','46.7','张嘉佳','/S0EALq2D/oZlIq0pW/ta8ihbvx.jpg','3',0,'《从你的全世界路过》就像朋友在深夜跟你在叙述，叙述他走过的千山万水。当你辗转失眠时，当你需要安慰时，当你等待列车时，当你赖床慵懒时，当你饭后困顿时，应该都能找到一章合适的。[1] \r\n这算是一本睡前读物。采用讲故事的形式来讲述一个个平凡却引人深思的故事，它与那些纯粹的鸡汤文不同，故事的切合和说理更让人从心理上认同，因为书中的每一段故事似乎都发生在每一位读者身边。\r\n',1),('1dace71b77654509bf9f66094347cda4','如何说孩子才会听 怎么听孩子才肯说','21.6','玛兹丽施','/t6JJqHeS/MCyRWa3K/PvOiHiHF.jpg','2',0,'要是有一本书能让你学会一种语言，说出话来孩子就会听，你会吃惊吗？\r\n　　不必惊奇，《如何说孩子才会听，怎么听孩子才肯说》将帮助你学会一种让孩子听话的神奇语言，这种爱的语言使孩子很容易接受您的要求和忠告。世界上数以千万的父母用自身的亲身实践证明了书中提供的方法非常有效。该书在美国畅销300多万册，并被翻译成30多种文字风靡全球。世界各地的父母和专家热情洋溢地赞誉两位作者，她们在长期的实践中摸索出的这套语言是那样行之有效，的的确确能缓解所有年龄段的孩子与父母的紧张关系，结束父母与孩子的冲突对抗，带来父母和孩子的合作。',0),('315db0713a7341a1beb65706b3759775',' 悲惨世界','19.1','维克多·雨果（法）','/8bJKO27z/Y2SSih7I/T72KwvW0.jpg','3',0,'《悲惨世界》是由法国大作家维克多·雨果在1862年所发表的一部长篇小说，涵盖了拿破仑战争和之后的十几年的时间，是十九世纪最著名的小说之一。故事的主线围绕主人公土伦苦刑犯冉·阿让（Jean Valjean）的个人经历，融进了法国的历史、革命、战争、道德哲学、法律、正义、宗教信仰。多次被改编演绎成影视作品。',0),('33fc2894e0374858916d678fa6291bab','遇见未知的自己','21.8','张德芬','/efEn5m0j/edAaLuh2/NOfakoIY.jpg','3',0,'华语世界第一部影响了数千万人的身心灵成长小说，销量过百万的《遇见未知的自己》，作者张德芬首次对内容进行修订，并精心续写全新结局，分享她最新的心灵成长心得。\r\n　　本书借由我们每天都可能遭遇到的种种事情，帮助我们看到主宰自己人生的模式是如何形成的，又如何在操控我们的身心。并以故事的形式来分享张德芬多年的心灵成长感悟，来帮助我们解除现有的人生模式，帮助我们从思想、情绪和身体的桎梏中解脱出来，从而活出自己想要的人生，找回原本真实、快乐的自己！',0),('57b20e5a08694ff4b52882a31ee7df6c','IT运维之道','33.2','李鹏','/86CEtvdL/AxJxMh64/7NFuSLrO.jpg','1',0,'《IT运维之道》是一本由人民邮电出版社出版的计算机类书籍。本书全面展现IT服务方法、标准、技巧、技术，让读者能纵览IT服务全貌；深入浅出的阐述了IT运维精髓和全方位要素，深刻剖析了IT运维的成功规律；系统概述了从硬件到软件的各类IT技术，让读者具备IT运维软功夫和硬功夫；让读者少走弯路，学会一套成熟、规范、有效的IT运维方法。',1),('748ae59e9d9c4d50835228f121345931','计算机网络（第6版）','33.2','谢希仁','/1wOj63G9/Zu0XbftW/dTEPEa4m.jpg','1',0,'十年经久不衰，55次印刷，销量200万册，不断出新的经典教科书。',0),('b16d95e7933f426684afdc45dc4542c6','平凡的世界','47.9','路遥','/3dJihBep/R4G6mnTE/TmWlF5op.jpg','3',0,'平凡的世界》是中国作家路遥创作的一部百万字的小说。这是一部全景式地表现中国当代城乡社会生活的长篇小说，全书共三部。该书以中国70年代中期到80年代中期十年间为背景，通过复杂的矛盾纠葛，以孙少安和孙少平两兄弟为中心，刻画了当时社会各阶层众多普通人的形象；劳动与爱情、挫折与追求、痛苦与欢乐、日常生活与巨大社会冲突纷繁地交织在一起，深刻地展示了普通人在大时代历史进程中所走过的艰难曲折的道路。',0),('baa84bbfd0ab4bca9cbe6a59231074f2','郑渊洁十二生肖童话（全12册）','126.0','郑渊洁','/eAxLFGur/R1ZPEE20/NZgR2GB8.jpg','4',0,'郑渊洁十二生肖童话系列由《鼠王做寿》《牛王醉酒》《虎王出山》《兔王卖耳》《龙王闹海》《蛇王淘金》《马王登基》《羊王称霸》《猴王变形》《鸡王画虎》《狗王梦游》《猪王照相》共十二本组成。故事中的动物们可谓是“八仙过海，各显神通”。鼠王“掌握了”控制时间的诀窍；牛王脱掉了一身的牛皮癣；虎王离开森林，来到动物园“指点江山”；兔王卖掉了自己的大耳朵；龙王用计惩戒了妄图盗卖古董的坏蛋；蛇王摇身一变成为了人，来到人间淘金；马王不费一兵一卒就能直取他国领土……不过，他们这么做的结果到底如何？更多精彩尽在其中，十二生肖童话系列将给你意想不到的惊喜。',0),('eb53f7e0ea634c7aa701dc168834c4cc','MINECRAFT我的世界','52.1','奥布莱恩（O‘Brien，S.）','/qKQHOeEt/Pi8S0cdA/FfquY52Y.jpg','1',0,'本书共的13章，每章针对一个专题展开，伴随大家从求生存一直到成霸业。\r\n第1章“入手指南”向大家说明如何下载、安装Minecraft以及开启新游戏。第2章“初夜生存”将向大家传授如何平安度过Minecraft中艰苦的初期阶段。第3章“资源采集”将帮助大家掌握步入正轨所需要的技能。第4章“开山采矿”将帮助大家凿出离高效的采矿通道。第5章“战斗学院”将训练大家对付各种怪物。第6章“农业生产”之后，大家就可以过上自给自足、永\r\n不挨饿的好日子。第7章“农场与驯化”全是可爱的小动物，它们在Minecraft中繁衍生息的同时也为我们提供了各种宝贵资源。第8章“创意建造”，从宏伟的外观建筑到华丽的内部装潢都没问题。第9章“红石与铁路交通”介绍用红石能源自动控制门、自动分发矿车、建造车站、经停站等。第10章“附魔、锻造与酿造”介绍了各种有趣的玩法。第11章“村庄与其他建筑”主要讲解与大家息息相关的NPC村民们。第12章“一战到底：下界与末地”将告诉大家如何快速找到要塞、得到物品以及如何备战大Boss末影龙。第13章“Mod与多人游戏”，将向大家展示一个完全自主定制的游戏。\r\n事不宜迟，快来感受Minecraft的魅力吧。\r\n',0),('f4d59f5d4bac4d3989c8df3e319dd453','绽放自我——歪歪兔生命教育童话','63.7','吉葡乐','/yLkqKzuP/6nB7MxT2/9aoSX7er.jpg','4',0,'这套童话具有天马行空的想象力，选材广泛，时空转换炫目灵动，故事或情节曲折，感情充沛，或幽默风趣，令人捧腹。该书系统地归纳了生命教育的10个主题，在引人入胜的故事中，让读者直面生命，感知生命中的痛苦、尊严、梦想、自由等一些本质的内涵，启发孩子思考生命的意义，呵护心灵的成长，学会珍爱和提升自我，学会积极的生存、健康的生活与独立的发展，实现自我生命的最大价值。既是生命教育的启蒙读物，也引导孩子由此开启独立思考的大门。7-12岁适读，全10册。',1);

/*Table structure for table `cart` */

DROP TABLE IF EXISTS `cart`;

CREATE TABLE `cart` (
  `id` varchar(32) NOT NULL,
  `bid` varchar(32) DEFAULT NULL,
  `count` tinyint(4) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `total_price` varchar(20) DEFAULT NULL,
  `uid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `cart` */

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `cid` char(32) NOT NULL,
  `cname` varchar(100) NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `category` */

insert  into `category`(`cid`,`cname`) values ('1','科技'),('2','生活'),('3','文学'),('4','童话');

/*Table structure for table `orderitem` */

DROP TABLE IF EXISTS `orderitem`;

CREATE TABLE `orderitem` (
  `iid` char(32) NOT NULL,
  `count` tinyint(4) DEFAULT NULL,
  `subtotal` varchar(20) DEFAULT NULL,
  `oid` char(32) DEFAULT NULL,
  `bid` char(32) DEFAULT NULL,
  PRIMARY KEY (`iid`),
  KEY `oid` (`oid`),
  KEY `bid` (`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `orderitem` */

insert  into `orderitem`(`iid`,`count`,`subtotal`,`oid`,`bid`) values ('1a41f93db2684824975a6bc61f1b259c',3,'140.10','2483f2211cf64e76b08b47fb6f6ffdcb','0ed6718d67444d16a852bfd728908ebc'),('5852d3005def416ca4cfcae6ced24e5a',2,'43.20','2483f2211cf64e76b08b47fb6f6ffdcb','1dace71b77654509bf9f66094347cda4'),('b5c5b97d2685419d85e180650debdaf0',3,'140.10','e92b3334c83c46288b43b86a096ab5af','0ed6718d67444d16a852bfd728908ebc');

/*Table structure for table `orders` */

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `oid` char(32) NOT NULL,
  `total` varchar(20) DEFAULT NULL,
  `state` varchar(4) DEFAULT NULL,
  `uid` char(32) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`oid`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `orders` */

insert  into `orders`(`oid`,`total`,`state`,`uid`,`address`,`date`) values ('2483f2211cf64e76b08b47fb6f6ffdcb','183.30','2','dzq123','7ba60a09485245cab958d7fa0740e877','2016-04-29 12:23:37'),('e92b3334c83c46288b43b86a096ab5af','140.10','4','dzq','3fbf352cf810435d861c978a68d7cb04','2016-05-04 14:22:29');

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` varchar(32) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  `state` tinyint(2) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role` */

insert  into `role`(`id`,`name`,`state`) values ('9689a6a006d111e6b83d00ff6c94','admin',0);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `uid` char(32) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `code` char(64) NOT NULL,
  `state` tinyint(1) DEFAULT '0',
  `truename` varchar(20) DEFAULT NULL,
  `tel` varchar(15) DEFAULT NULL,
  `salt` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`uid`,`username`,`password`,`email`,`code`,`state`,`truename`,`tel`,`salt`) values ('1b6feb0fdbc2406fb1a354bdc0c85137','admin','896524f7c8bb7a0767f529af88d8d68427d70691','371958725@qq.com','db950b44a22d4559badf60cf2caf6e41838c9b7add5e48f38fae5518109842ce',1,'admin',NULL,'699d425279da0907'),('5fcb78610a36486fa4c933b0d5408789','dzq','896524f7c8bb7a0767f529af88d8d68427d70691','371958725@qq.com','27b5f3f4d39f4135ae519bfe7729aff7723ef9bfd1cf46fe9ca09c067228dab9',1,'李四',NULL,'699d425279da0907');

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `roleid` varchar(32) DEFAULT NULL,
  `userid` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_role` */

insert  into `user_role`(`roleid`,`userid`) values ('9689a6a006d111e6b83d00ff6c94','1b6feb0fdbc2406fb1a354bdc0c85137');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
