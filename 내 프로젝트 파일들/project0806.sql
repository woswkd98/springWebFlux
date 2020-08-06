/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.20 : Database - bidding
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bidding` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `bidding`;

/*Table structure for table `bidding` */

DROP TABLE IF EXISTS `bidding`;

CREATE TABLE `bidding` (
  `biddingId` int NOT NULL AUTO_INCREMENT,
  `uploadAt` datetime NOT NULL,
  `price` int NOT NULL,
  `request_requestId` int NOT NULL,
  `user_userId` int NOT NULL,
  PRIMARY KEY (`biddingId`,`request_requestId`,`user_userId`),
  UNIQUE KEY `biddingId_UNIQUE` (`biddingId`),
  KEY `fk_bidding_request1_idx` (`request_requestId`),
  KEY `fk_bidding_user1_idx` (`user_userId`),
  CONSTRAINT `fk_bidding_request1` FOREIGN KEY (`request_requestId`) REFERENCES `request` (`requestId`),
  CONSTRAINT `fk_bidding_user1` FOREIGN KEY (`user_userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `bidding` */

/*Table structure for table `chat` */

DROP TABLE IF EXISTS `chat`;

CREATE TABLE `chat` (
  `idchat` int NOT NULL AUTO_INCREMENT,
  `context` varchar(45) DEFAULT NULL,
  `user_userId` int NOT NULL,
  `roomId` int NOT NULL,
  PRIMARY KEY (`idchat`,`user_userId`),
  KEY `fk_chat_user1_idx` (`user_userId`),
  CONSTRAINT `fk_chat_user1` FOREIGN KEY (`user_userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `chat` */

/*Table structure for table `request` */

DROP TABLE IF EXISTS `request`;

CREATE TABLE `request` (
  `requestId` int NOT NULL AUTO_INCREMENT,
  `category` varchar(20) NOT NULL,
  `context` varchar(100) DEFAULT NULL,
  `uploadAt` varchar(32) NOT NULL,
  `deadLine` bigint NOT NULL,
  `hopeDate` varchar(16) DEFAULT NULL,
  `user_userId` int NOT NULL,
  PRIMARY KEY (`requestId`,`user_userId`),
  UNIQUE KEY `requestId_UNIQUE` (`requestId`),
  KEY `fk_request_user1_idx` (`user_userId`),
  CONSTRAINT `fk_request_user1` FOREIGN KEY (`user_userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `request` */

/*Table structure for table `request_has_tag` */

DROP TABLE IF EXISTS `request_has_tag`;

CREATE TABLE `request_has_tag` (
  `request_requestId` int NOT NULL,
  `tag_tagId` int NOT NULL,
  PRIMARY KEY (`request_requestId`,`tag_tagId`),
  KEY `fk_request_has_tag1_tag1_idx` (`tag_tagId`),
  KEY `fk_request_has_tag1_request1_idx` (`request_requestId`),
  CONSTRAINT `fk_request_has_tag1_request1` FOREIGN KEY (`request_requestId`) REFERENCES `request` (`requestId`),
  CONSTRAINT `fk_request_has_tag1_tag1` FOREIGN KEY (`tag_tagId`) REFERENCES `tag` (`tagId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `request_has_tag` */

/*Table structure for table `review` */

DROP TABLE IF EXISTS `review`;

CREATE TABLE `review` (
  `reviewId` int NOT NULL AUTO_INCREMENT,
  `grade` float DEFAULT '3.5',
  `context` varchar(45) DEFAULT NULL,
  `user_userId` int NOT NULL,
  `seller_user_userId` int NOT NULL,
  PRIMARY KEY (`reviewId`,`user_userId`),
  KEY `fk_review_user1_idx` (`user_userId`),
  CONSTRAINT `fk_review_user1` FOREIGN KEY (`user_userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `review` */

/*Table structure for table `seller` */

DROP TABLE IF EXISTS `seller`;

CREATE TABLE `seller` (
  `sellerId` int NOT NULL AUTO_INCREMENT,
  `portfolio` varchar(100) DEFAULT NULL,
  `imageLink` varchar(45) DEFAULT NULL,
  `imageCount` int DEFAULT NULL,
  `sellerGrade` int DEFAULT '0',
  `reviewerCount` int DEFAULT '0',
  PRIMARY KEY (`sellerId`),
  UNIQUE KEY `sellerId_UNIQUE` (`sellerId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `seller` */

insert  into `seller`(`sellerId`,`portfolio`,`imageLink`,`imageCount`,`sellerGrade`,`reviewerCount`) values 
(1,'sery','saery',1,0,0),
(2,'qawet','qwet',1,0,0),
(3,'qawet','qwet',1,0,0),
(4,'qawet','qwet',1,0,0),
(5,'qawet','qwet',1,0,0);

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `tagId` int NOT NULL AUTO_INCREMENT,
  `context` varchar(20) NOT NULL,
  `bidCount` int DEFAULT '0',
  `requestCount` int DEFAULT '0',
  PRIMARY KEY (`tagId`),
  UNIQUE KEY `tagId_UNIQUE` (`tagId`),
  UNIQUE KEY `context_UNIQUE` (`context`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tag` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `userId` int NOT NULL AUTO_INCREMENT,
  `userPassword` varchar(64) NOT NULL,
  `userEmail` varchar(45) NOT NULL,
  `userName` varchar(45) NOT NULL,
  `isWithdraw` tinyint DEFAULT '1',
  `sellerId` int NOT NULL DEFAULT '-1',
  PRIMARY KEY (`userId`),
  UNIQUE KEY `indexId_UNIQUE` (`userId`),
  UNIQUE KEY `userEmail_UNIQUE` (`userEmail`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`userId`,`userPassword`,`userEmail`,`userName`,`isWithdraw`,`sellerId`) values 
(1,'$2a$10$DgmdY29f.gASzdHfFRMYE.rj50l7ffaKJXSFdlJYZKUDnmgyv9Fle','asaerzyaerydtg','aet',1,5);

/* Procedure structure for procedure `insertSellerThenUpdateUser` */

/*!50003 DROP PROCEDURE IF EXISTS  `insertSellerThenUpdateUser` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`scott`@`localhost` PROCEDURE `insertSellerThenUpdateUser`(
	IN iportfolio VARCHAR(100),
	IN iimageLink VARCHAR(45),
	in iimageCount int,
	IN iUserId INT
    )
BEGIN
DECLARE id INT DEFAULT 0;
	INSERT INTO seller(portfolio,imageLink,imageCount) VALUES (iportfolio,iimageLink,iimageCount);
	
	SET id = LAST_INSERT_ID(); # 가져와서 
	update user set  sellerId = id where userId = iUserId;
	
	END */$$
DELIMITER ;

/* Procedure structure for procedure `insertTag` */

/*!50003 DROP PROCEDURE IF EXISTS  `insertTag` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `insertTag`(
	IN inContext VARCHAR(20),
	IN requestId INT
    )
BEGIN
	DECLARE id INT DEFAULT 0;
	
	SELECT tagId INTO id FROM tag WHERE CONTEXT = inContext; #일단 태그의 내용과 맞는 TAG가 있는지 확인한다
	
	IF id <= 0 THEN
		INSERT INTO tag(CONTEXT) VALUES (inContext); # 만약 없으면 생성시켜서 집어넣고
		SET id = LAST_INSERT_ID(); #인덱스를 가져온다 
	ELSE 
		UPDATE tag SET requestCount = requestCount + 1 WHERE tagId = id; #태그의 내용과 맞는 TAG가 있으면 카운트 증가
	END IF;
	
	INSERT INTO request_has_Tag(request_requestId, tag_tagId) VALUES (requestId, id); #마지막은 다 대 다 관계 저장
END */$$
DELIMITER ;

/* Procedure structure for procedure `insertThenReturnId` */

/*!50003 DROP PROCEDURE IF EXISTS  `insertThenReturnId` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`scott`@`localhost` PROCEDURE `insertThenReturnId`(
	IN icategory VARCHAR(20),
	IN iCONTEXT VARCHAR(100),
	IN iuploadAt DATETIME,
	IN ideadLine Bigint,
	IN ihopeDate DATE,
	IN iuser_indexId INT
    )
BEGIN
	INSERT INTO request (category, CONTEXT, uploadAt, deadLine, hopeDate, user_userId) VALUES (icategory, iCONTEXT, iuploadAt, ideadLine, ihopeDate, iuser_indexId);
	
	SELECT LAST_INSERT_ID(); # 가져와서 
	END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
