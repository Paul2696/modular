-- MySQL Script generated by MySQL Workbench
-- Sun 25 Aug 2019 05:08:35 PM CDT
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`userType`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`userType` (
  `idUserType` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idUserType`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `type` INT NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idUser`, `type`),
  INDEX `fk_user_userType_idx` (`type` ASC)  ,
  CONSTRAINT `fk_user_userType`
    FOREIGN KEY (`type`)
    REFERENCES `mydb`.`userType` (`idUserType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`course` (
  `idCourse` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `start` DATETIME NOT NULL,
  `end` DATETIME NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idCourse`, `idUser`),
  INDEX `fk_course_user1_idx` (`idUser` ASC)  ,
  CONSTRAINT `fk_course_user1`
    FOREIGN KEY (`idUser`)
    REFERENCES `mydb`.`user` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`notification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`notification` (
  `idNotification` INT NOT NULL AUTO_INCREMENT,
  `message` VARCHAR(200) NOT NULL,
  `idCourse` INT NOT NULL,
  `seen` TINYINT NOT NULL COMMENT '\n',
  `resource` LONGBLOB NULL,
  `date` DATETIME NOT NULL,
  PRIMARY KEY (`idNotification`, `idCourse`),
  INDEX `fk_notification_course1_idx` (`idCourse` ASC)  ,
  CONSTRAINT `fk_notification_course1`
    FOREIGN KEY (`idCourse`)
    REFERENCES `mydb`.`course` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`homework`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`homework` (
  `idHomework` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `idCourse` INT NOT NULL,
  `end` DATETIME NOT NULL,
  `description` VARCHAR(500) NULL,
  `resource` LONGBLOB NULL,
  PRIMARY KEY (`idHomework`, `idCourse`),
  INDEX `fk_homework_course1_idx` (`idCourse` ASC)  ,
  CONSTRAINT `fk_homework_course1`
    FOREIGN KEY (`idCourse`)
    REFERENCES `mydb`.`course` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`userHomework`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`userHomework` (
  `idHomework` INT NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idHomework`, `idUser`),
  INDEX `fk_homework_has_user_user1_idx` (`idUser` ASC)  ,
  INDEX `fk_homework_has_user_homework1_idx` (`idHomework` ASC)  ,
  CONSTRAINT `fk_homework_has_user_homework1`
    FOREIGN KEY (`idHomework`)
    REFERENCES `mydb`.`homework` (`idHomework`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_homework_has_user_user1`
    FOREIGN KEY (`idUser`)
    REFERENCES `mydb`.`user` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`chat` (
  `idUser` INT NOT NULL,
  `idUser1` INT NOT NULL,
  `message` VARCHAR(150) NOT NULL,
  `date` DATETIME NOT NULL,
  `idChat` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idChat`),
  INDEX `fk_user_has_user_user2_idx` (`idUser1` ASC)  ,
  INDEX `fk_user_has_user_user1_idx` (`idUser` ASC)  ,
  CONSTRAINT `fk_user_has_user_user1`
    FOREIGN KEY (`idUser`)
    REFERENCES `mydb`.`user` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_user_user2`
    FOREIGN KEY (`idUser1`)
    REFERENCES `mydb`.`user` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`homeworkResponse`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`homeworkResponse` (
  `idHomework` INT NOT NULL,
  `idUser` INT NOT NULL,
  `grade` INT NULL,
  `response` LONGBLOB NULL,
  `textResponse` VARCHAR(500) NULL,
  `sended` DATETIME NOT NULL,
  `idHomeworkResponse` INT NOT NULL AUTO_INCREMENT,
  `fileExtension` VARCHAR(45) NULL,
  PRIMARY KEY (`idHomeworkResponse`),
  INDEX `fk_homework_has_user_user2_idx` (`idUser` ASC)  ,
  INDEX `fk_homework_has_user_homework2_idx` (`idHomework` ASC)  ,
  CONSTRAINT `fk_homework_has_user_homework2`
    FOREIGN KEY (`idHomework`)
    REFERENCES `mydb`.`homework` (`idHomework`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_homework_has_user_user2`
    FOREIGN KEY (`idUser`)
    REFERENCES `mydb`.`user` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`userCourse`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`userCourse` (
  `idUser` INT NOT NULL,
  `idCourse` INT NOT NULL,
  PRIMARY KEY (`idUser`, `idCourse`),
  INDEX `fk_user_has_course_course1_idx` (`idCourse` ASC)  ,
  INDEX `fk_user_has_course_user1_idx` (`idUser` ASC)  ,
  CONSTRAINT `fk_user_has_course_user1`
    FOREIGN KEY (`idUser`)
    REFERENCES `mydb`.`user` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_course_course1`
    FOREIGN KEY (`idCourse`)
    REFERENCES `mydb`.`course` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

USE mydb;
INSERT INTO userType (name) VALUES ('teacher');
INSERT INTO userType (name) VALUES ('student');
INSERT INTO userType (name) VALUES ('admin');
