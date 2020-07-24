-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema project1
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema project1
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `project1` DEFAULT CHARACTER SET utf8 ;
USE `project1` ;

-- -----------------------------------------------------
-- Table `project1`.`seller`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project1`.`seller` ;

CREATE TABLE IF NOT EXISTS `project1`.`seller` (
  `sellerId` INT NOT NULL AUTO_INCREMENT,
  `portfolio` VARCHAR(100) NULL,
  `imageLink` VARCHAR(45) NULL,
  `imageCount` INT NULL,
  `sellerGrade` INT NULL,
  PRIMARY KEY (`sellerId`),
  UNIQUE INDEX `sellerId_UNIQUE` (`sellerId` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project1`.`user` ;

CREATE TABLE IF NOT EXISTS `project1`.`user` (
  `indexId` INT NOT NULL,
  `userId` VARCHAR(45) NOT NULL,
  `userPassword` VARCHAR(45) NOT NULL,
  `userEmail` VARCHAR(45) NOT NULL,
  `userName` VARCHAR(45) NOT NULL,
  `usercol` VARCHAR(45) NULL,
  `seller_sellerId` INT NOT NULL,
  PRIMARY KEY (`indexId`, `seller_sellerId`),
  UNIQUE INDEX `indexId_UNIQUE` (`indexId` ASC) VISIBLE,
  UNIQUE INDEX `userId_UNIQUE` (`userId` ASC) VISIBLE,
  INDEX `fk_user_seller1_idx` (`seller_sellerId` ASC) VISIBLE,
  CONSTRAINT `fk_user_seller1`
    FOREIGN KEY (`seller_sellerId`)
    REFERENCES `project1`.`seller` (`sellerId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`request`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project1`.`request` ;

CREATE TABLE IF NOT EXISTS `project1`.`request` (
  `requestId` INT NOT NULL AUTO_INCREMENT,
  `category` VARCHAR(45) NOT NULL,
  `uploadAt` VARCHAR(20) NOT NULL,
  `deadLine` VARCHAR(20) NOT NULL,
  `hopeDate` VARCHAR(20) NULL,
  `tags` VARCHAR(45) NULL,
  PRIMARY KEY (`requestId`),
  UNIQUE INDEX `requestId_UNIQUE` (`requestId` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project1`.`tag` ;

CREATE TABLE IF NOT EXISTS `project1`.`tag` (
  `tagId` INT NOT NULL AUTO_INCREMENT,
  `context` VARCHAR(20) NOT NULL,
  `bidCount` INT NULL DEFAULT 0,
  `clickCount` INT NULL DEFAULT 0,
  PRIMARY KEY (`tagId`),
  UNIQUE INDEX `tagId_UNIQUE` (`tagId` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`user_has_tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project1`.`user_has_tag` ;

CREATE TABLE IF NOT EXISTS `project1`.`user_has_tag` (
  `tag_tagId` INT NOT NULL,
  `seller_sellerId` INT NOT NULL,
  PRIMARY KEY (`tag_tagId`, `seller_sellerId`),
  INDEX `fk_user_has_tag_tag1_idx` (`tag_tagId` ASC) VISIBLE,
  INDEX `fk_user_has_tag_seller1_idx` (`seller_sellerId` ASC) VISIBLE,
  CONSTRAINT `fk_user_has_tag_tag1`
    FOREIGN KEY (`tag_tagId`)
    REFERENCES `project1`.`tag` (`tagId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_tag_seller1`
    FOREIGN KEY (`seller_sellerId`)
    REFERENCES `project1`.`seller` (`sellerId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`bidding`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project1`.`bidding` ;

CREATE TABLE IF NOT EXISTS `project1`.`bidding` (
  `biddingId` INT NOT NULL,
  `uploadAt` VARCHAR(20) NOT NULL,
  `price` INT NOT NULL,
  `request_requestId` INT NOT NULL,
  `seller_sellerId` INT NOT NULL,
  PRIMARY KEY (`biddingId`, `request_requestId`, `seller_sellerId`),
  UNIQUE INDEX `biddingId_UNIQUE` (`biddingId` ASC) VISIBLE,
  INDEX `fk_bidding_request1_idx` (`request_requestId` ASC) VISIBLE,
  INDEX `fk_bidding_seller1_idx` (`seller_sellerId` ASC) VISIBLE,
  CONSTRAINT `fk_bidding_request1`
    FOREIGN KEY (`request_requestId`)
    REFERENCES `project1`.`request` (`requestId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bidding_seller1`
    FOREIGN KEY (`seller_sellerId`)
    REFERENCES `project1`.`seller` (`sellerId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project1`.`review`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project1`.`review` ;

CREATE TABLE IF NOT EXISTS `project1`.`review` (
  `reviewId` INT NOT NULL AUTO_INCREMENT,
  `grade` FLOAT NULL DEFAULT 3.5,
  `context` VARCHAR(45) NULL,
  `seller_sellerId` INT NOT NULL,
  PRIMARY KEY (`reviewId`, `seller_sellerId`),
  UNIQUE INDEX `reviewId_UNIQUE` (`reviewId` ASC) VISIBLE,
  INDEX `fk_review_seller1_idx` (`seller_sellerId` ASC) VISIBLE,
  CONSTRAINT `fk_review_seller1`
    FOREIGN KEY (`seller_sellerId`)
    REFERENCES `project1`.`seller` (`sellerId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
