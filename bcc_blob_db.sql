SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema bcc_blob_db
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `bcc_blob_db` ;
CREATE SCHEMA IF NOT EXISTS `bcc_blob_db` DEFAULT CHARACTER SET utf8 ;
USE `bcc_blob_db` ;

-- -----------------------------------------------------
-- Table `bcc_blob_db`.`county_bureau`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bcc_blob_db`.`county_bureau` ;

CREATE TABLE IF NOT EXISTS `bcc_blob_db`.`county_bureau` (
  `county_bureau_id` INT NOT NULL AUTO_INCREMENT COMMENT 'bureau- 县政府行政审批部门（局）。不同的 bureau 官员负责不同类型的行政申请审批',
  `name` VARCHAR(45) NOT NULL COMMENT '县行政审批主管局名字',
  PRIMARY KEY (`county_bureau_id`))
ENGINE = InnoDB;

-- ----------------------------
-- Insert some records into bureau table
-- ----------------------------
INSERT INTO `county_bureau` (name) VALUES ('国土局');
INSERT INTO `county_bureau` (name) VALUES ('林业局');
INSERT INTO `county_bureau` (name) VALUES ('人社局');
INSERT INTO `county_bureau` (name) VALUES ('民政局');
INSERT INTO `county_bureau` (name) VALUES ('计生局');
INSERT INTO `county_bureau` (name) VALUES ('党组织局');

-- -----------------------------------------------------
-- Table `bcc_blob_db`.`town_office`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bcc_blob_db`.`town_office` ;

CREATE TABLE IF NOT EXISTS `bcc_blob_db`.`town_office` (
  `town_office_id` INT NOT NULL AUTO_INCREMENT COMMENT 'offcie - 乡级政府行政审批部门（处）记录主键',
  `county_bureau_id` INT NOT NULL,
  `name` VARCHAR(45) NULL COMMENT '行政乡名字',
  PRIMARY KEY (`town_office_id`),
  INDEX `fk_town_office_county_bureau1_idx` (`county_bureau_id` ASC),
  CONSTRAINT `fk_town_office_county_bureau1`
    FOREIGN KEY (`county_bureau_id`)
    REFERENCES `bcc_blob_db`.`county_bureau` (`county_bureau_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- ----------------------------
-- Insert some records into town_office table
-- ----------------------------
INSERT INTO `town_office` (county_bureau_id, name) VALUES ('1', '北七家国土处'); -- 国土局、北七家国土处
INSERT INTO `town_office` (county_bureau_id, name) VALUES ('1', '小汤山国土处'); -- 国土局、小汤山国土处
INSERT INTO `town_office` (county_bureau_id, name) VALUES ('1', '十三陵国土处'); -- 国土局、十三陵国土处
INSERT INTO `town_office` (county_bureau_id, name) VALUES ('2', '北七家林业处'); -- 林业局、北七家林业处
INSERT INTO `town_office` (county_bureau_id, name) VALUES ('2', '小汤山林业处'); -- 林业局、小汤山林业处
INSERT INTO `town_office` (county_bureau_id, name) VALUES ('2', '十三陵林业处'); -- 林业局、十三陵林业处


-- -----------------------------------------------------
-- Table `bcc_blob_db`.`village_office`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bcc_blob_db`.`village_office` ;

CREATE TABLE IF NOT EXISTS `bcc_blob_db`.`village_office` (
  `village_office_id` INT NOT NULL AUTO_INCREMENT COMMENT '村办公室记录主键',
  `town_office_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL COMMENT '行政村名字',
  PRIMARY KEY (`village_office_id`),
  INDEX `fk_village_office_town_office1_idx` (`town_office_id` ASC),
  CONSTRAINT `fk_village_office_town_office1`
    FOREIGN KEY (`town_office_id`)
    REFERENCES `bcc_blob_db`.`town_office` (`town_office_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- ----------------------------
-- Insert some records into village table
-- ----------------------------
INSERT INTO `village_office` (name, town_office_id) VALUES ('郑家庄', '1'); -- 北七家、郑家庄
INSERT INTO `village_office` (name, town_office_id) VALUES ('白各庄', '1'); -- 北七家、白各庄


-- -----------------------------------------------------
-- Table `bcc_blob_db`.`business`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bcc_blob_db`.`business` ;

CREATE TABLE IF NOT EXISTS `bcc_blob_db`.`business` (
  `business_id` INT NOT NULL AUTO_INCREMENT COMMENT '业务种类记录的主键',
  `county_bureau_id` INT NOT NULL,
  `name` VARCHAR(60) NOT NULL COMMENT '行政业务类别名字：国土，林业，人社，民政，计生，党组织。每个业务类别有一个县级主管局和村级业务处',
  `edit_app_url` VARCHAR(45) NOT NULL COMMENT '创建或修改一个新业务申请的页面名称',
  `disp_app_url` VARCHAR(45) NULL COMMENT '显示一个已有业务申请的页面名称',
  PRIMARY KEY (`business_id`),
  INDEX `fk_business_county_bureau1_idx` (`county_bureau_id` ASC),
  CONSTRAINT `fk_business_county_bureau1`
    FOREIGN KEY (`county_bureau_id`)
    REFERENCES `bcc_blob_db`.`county_bureau` (`county_bureau_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- ----------------------------
-- Insert some records into business table
-- ----------------------------
-- 国土局业务记录
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('1', '农民个人占集体土地建房(原地改建、扩建、新建)','edit_guotu','disp_guotu'); 
-- 林业局业务记录
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('2', '林木采伐许可证','edit_caifaxuke','disp__caifaxuke'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('2', '木材运输证','edit_mucai','disp_mucai'); 
-- 人社业务记录
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('3', '城乡居民养老保险参保登记','edit_jumingyanglao','disp_jumingyanglao'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('3', '劳动就业咨询','edit_laodongjiuye','disp_laodongjiuye'); 
-- 民政业务记录
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('4', '城乡特困群众大病救助申请办理','edit_chengxiangtekun','disp_chengxiangtekun'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('4', '农村低保办理','edit_nongcundibao','disp_nongcundibao'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('4', '出具无婚姻登记记录查询证明','edit_wuhunchaxun','disp_wuhunchaxun'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('4', '城市低保申请办理','edit_chengshidibao','disp_chengshidibao'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('4', '发放百岁及以上老人长寿补贴申请办理','edit_changshoubutie','disp_changshoubutie'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('4', '自然灾害生活救助申请办理','edit_zaihaijiuzhu','disp_zaihaijiuzhu'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('4', '出具婚姻登记记录查询证明','edit_yihunchaxun','disp_yihunchaxun'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('4', '孤儿救助办理','edit_guerjiuzhu','disp_guerjiuzhu'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('4', '农村五保申请','edit_wubaoshenqing','disp_wubaoshenqing'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('4', '优抚对象优待金发放办理','edit_youwuduixiang','disp_youwuduixiang'); 
-- 计生业务记录
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('5', '生育证办理','edit_shengyuzheng','disp_shengyuzheng'); 
-- 党组业务记录
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('6', '县内党组织关系转接办理','edit_xianneizhuanjie','disp_xianneizhuanjie'); 
INSERT INTO `business` (county_bureau_id, name,edit_app_url, disp_app_url) VALUES ('6', '县外党组织关系转接办理','edit_xianwaizhuanjie','disp_xianwaizhuanjie'); 


-- -----------------------------------------------------
-- Table `bcc_blob_db`.`app_abstract`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bcc_blob_db`.`app_abstract` ;

CREATE TABLE IF NOT EXISTS `bcc_blob_db`.`app_abstract` (
  `app_abstract_id` INT NOT NULL AUTO_INCREMENT COMMENT '自动生成的申请表主键',
  `business_id` INT NOT NULL COMMENT '连接业务种类 business 的外键',
  `village_agent_id` INT NOT NULL,
  `state` VARCHAR(30) NOT NULL COMMENT '申请目前进展状态: draft, deniedByOffice, deniedByBureau,  deliveredToOffice, deliveredToBureau, finalApproved',
  `village_name` VARCHAR(45) NOT NULL COMMENT '提交申请的村庄名称',
  `last_modify_date` DATE NULL COMMENT '最后一次修订这个申请的日期',
  `last_modifier_name` VARCHAR(45) NULL COMMENT '最后修订申请的人员名字',
  `edit_app_url` VARCHAR(45) NULL COMMENT '创建新业务时从对应 business 记录复制过来的以便于后面的算法处理',
  `disp_app_url` VARCHAR(45) NULL COMMENT '创建新业务时从对应 business 记录复制过来的以便于后面的算法处理',
  PRIMARY KEY (`app_abstract_id`),
  INDEX `fk_application_Business1_idx` (`business_id` ASC),
  INDEX `fk_application_village_agent1_idx` (`village_agent_id` ASC),
  CONSTRAINT `fk_application_Business1`
    FOREIGN KEY (`business_id`)
    REFERENCES `bcc_blob_db`.`business` (`business_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_application_village_agent1`
    FOREIGN KEY (`village_agent_id`)
    REFERENCES `bcc_blob_db`.`village_agent` (`village_agent_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `bcc_blob_db`.`village_agent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bcc_blob_db`.`village_agent` ;

CREATE TABLE IF NOT EXISTS `bcc_blob_db`.`village_agent` (
  `village_agent_id` INT NOT NULL AUTO_INCREMENT,
  `village_office_id` INT NOT NULL,
  `name` VARCHAR(20) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`village_agent_id`),
  INDEX `fk_village_agent_village_office1_idx` (`village_office_id` ASC),
  CONSTRAINT `fk_village_agent_village_office1`
    FOREIGN KEY (`village_office_id`)
    REFERENCES `bcc_blob_db`.`village_office` (`village_office_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- ----------------------------
-- Insert some records into village_agent table
-- ----------------------------
INSERT INTO `village_agent` (village_office_id, name, password) VALUES ('1', '123', '123'); -- 郑家庄代办员
INSERT INTO `village_agent` (village_office_id, name, password) VALUES ('1', '张三', '123'); -- 郑家庄代办员


-- -----------------------------------------------------
-- Table `bcc_blob_db`.`town_officer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bcc_blob_db`.`town_officer` ;

CREATE TABLE IF NOT EXISTS `bcc_blob_db`.`town_officer` (
  `town_officer_id` INT NOT NULL AUTO_INCREMENT,
  `town_office_id` INT NOT NULL,
  `name` VARCHAR(20) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`town_officer_id`),
  INDEX `fk_town_officer_town_office1_idx` (`town_office_id` ASC),
  CONSTRAINT `fk_town_officer_town_office1`
    FOREIGN KEY (`town_office_id`)
    REFERENCES `bcc_blob_db`.`town_office` (`town_office_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- ----------------------------
-- Insert some records into town_officer table
-- ----------------------------
INSERT INTO `town_officer` (town_office_id, name, password) VALUES ('1', '123', '123'); 
INSERT INTO `town_officer` (town_office_id, name, password) VALUES ('1', '李四', '123'); 


-- -----------------------------------------------------
-- Table `bcc_blob_db`.`bureau_clerk`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bcc_blob_db`.`bureau_clerk` ;

CREATE TABLE IF NOT EXISTS `bcc_blob_db`.`bureau_clerk` (
  `bureau_clerk_id` INT NOT NULL AUTO_INCREMENT,
  `county_bureau_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`bureau_clerk_id`),
  INDEX `fk_bureau_clerk_county_bureau1_idx` (`county_bureau_id` ASC),
  CONSTRAINT `fk_bureau_clerk_county_bureau1`
    FOREIGN KEY (`county_bureau_id`)
    REFERENCES `bcc_blob_db`.`county_bureau` (`county_bureau_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- ----------------------------
-- Insert some records into bureau_clerk table
-- ----------------------------
INSERT INTO `bureau_clerk` (county_bureau_id, name, password) VALUES ('1', '123', '123'); 
INSERT INTO `bureau_clerk` (county_bureau_id, name, password) VALUES ('1', '王五', '123'); 


-- -----------------------------------------------------
-- Table `bcc_blob_db`.`guotu_app_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bcc_blob_db`.`guotu_app_detail` ;

CREATE TABLE IF NOT EXISTS `bcc_blob_db`.`guotu_app_detail` (
  `guotu_app_id` INT NOT NULL AUTO_INCREMENT COMMENT '国土局申请记录主键',
  `app_abstract_id` INT NULL,
  `business_name` VARCHAR(80) NULL COMMENT '行政业务名称',
  `applicant_name` VARCHAR(20) NULL COMMENT '申请人名称',
  `age` INT NULL COMMENT '年龄',
  `sex` VARCHAR(6) NULL COMMENT '性别',
  `household` VARCHAR(6) NULL COMMENT '家庭户口',
  `address` VARCHAR(200) NULL COMMENT '住址',
  `phone` VARCHAR(30) NULL COMMENT '联系电话',
  `shen_qing_file` MEDIUMBLOB NULL COMMENT '建房用地申请书',
  `shen_pi_file` MEDIUMBLOB NULL,
  `song_yue_file` MEDIUMBLOB NULL COMMENT '建房用地送阅审批件',
  `shen_cha_file` MEDIUMBLOB NULL COMMENT '建房用地审查意见表',
  `qi_ta_file` MEDIUMBLOB NULL COMMENT '其他所需文件',
  `village_opinion` VARCHAR(800) NULL COMMENT '村委会意见',
  `town_opinion` VARCHAR(800) NULL COMMENT '乡镇审核意见',
  `bureau_opinion` VARCHAR(800) NULL COMMENT '国土局意见',
  PRIMARY KEY (`guotu_app_id`),
  INDEX `fk_guotu_app_detail_app_abstract1_idx` (`app_abstract_id` ASC),
  CONSTRAINT `fk_guotu_app_detail_app_abstract1`
    FOREIGN KEY (`app_abstract_id`)
    REFERENCES `bcc_blob_db`.`app_abstract` (`app_abstract_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
