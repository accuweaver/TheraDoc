SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';


-- -----------------------------------------------------
-- Table `Patients`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Patients` (
  `patient_id` DECIMAL(10,0) NOT NULL ,
  `MRN` DECIMAL(10,0) NOT NULL ,
  `first_name` TEXT NULL DEFAULT NULL ,
  `last_name` TEXT NULL DEFAULT NULL ,
  `date_of_birth` DATETIME NULL DEFAULT NULL ,
  `sex` CHAR(1) NULL DEFAULT NULL ,
  `address` TEXT NULL DEFAULT NULL ,
  `ssn` DECIMAL(18,0) NULL DEFAULT NULL ,
  PRIMARY KEY (`patient_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `UQ_Patients_MRN` ON `Patients` (`MRN` ASC) ;


-- -----------------------------------------------------
-- Table `Encounters`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Encounters` (
  `encounter_id` DECIMAL(10,0) NOT NULL ,
  `MRN` DECIMAL(10,0) NOT NULL ,
  `location_name` TEXT NULL DEFAULT NULL ,
  `admit_date` DATE NULL DEFAULT NULL ,
  `discharge_date` DATE NULL DEFAULT NULL ,
  `Patients_patient_id` DECIMAL(10,0) NOT NULL ,
  PRIMARY KEY (`encounter_id`) ,
  CONSTRAINT `fk_Encounters_Patients1`
    FOREIGN KEY (`MRN` )
    REFERENCES `Patients` (`MRN` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `FK_Encounters_MRN` ON `Encounters` (`MRN` ASC) ;


-- -----------------------------------------------------
-- Table `message_errors`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `message_errors` (
  `message_id` DECIMAL(10,0) NOT NULL ,
  `message_date_time` DATETIME NULL DEFAULT NULL ,
  `message` TEXT NULL DEFAULT NULL ,
  `message_control_id` TEXT NULL DEFAULT NULL ,
  `process_status` CHAR(1) NULL COMMENT 'Status of processing: pending, in-process, completed, errors' ,
  `process_error` TEXT NULL ,
  `process_time` DATETIME NULL ,
  `message_queue_message_id` DECIMAL(10,0) NOT NULL COMMENT 'Reference to the original message ID from the message queue.\n' ,
  PRIMARY KEY (`message_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `message_queue`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `message_queue` (
  `message_id` DECIMAL(10,0) NOT NULL ,
  `message_date_time` DATETIME NULL DEFAULT NULL ,
  `message` TEXT NULL DEFAULT NULL ,
  `message_control_id` TEXT NULL DEFAULT NULL ,
  `process_status` CHAR(1) NULL COMMENT 'Status of processing: pending, in-process, completed, errors' ,
  `process_error` TEXT NULL ,
  `process_time` DATETIME NULL ,
PRIMARY KEY (`message_id`),
  CONSTRAINT `fk_message_queue_message_errors1`
    FOREIGN KEY (`message_id` )
    REFERENCES `message_errors` (`message_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `message_completed`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `message_completed` (
  `message_id` DECIMAL(10,0) NOT NULL ,
  `message_date_time` DATETIME NULL DEFAULT NULL ,
  `message` TEXT NULL DEFAULT NULL ,
  `message_control_id` TEXT NULL DEFAULT NULL ,
  `process_status` CHAR(1) NOT NULL ,
  `process_error` TEXT NULL ,
  `process_time` DATETIME NOT NULL ,
  `message_queue_message_id` DECIMAL(10,0) NOT NULL ,
  `encounter_id` DECIMAL(10,0) NULL ,
  `patient_id` DECIMAL(10,0) NULL ,
    PRIMARY KEY (`message_id`),
  CONSTRAINT `message_queue_message_id`
    FOREIGN KEY (`message_id` )
    REFERENCES `message_queue` (`message_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `encounter_id`
    FOREIGN KEY (`encounter_id` )
    REFERENCES `Encounters` (`encounter_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `patient_id`
    FOREIGN KEY (`patient_id` )
    REFERENCES `Patients` (`patient_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


CREATE INDEX `encounter_id_idx` ON `message_completed` (`encounter_id` ASC) ;

CREATE INDEX `patient_id_idx` ON `message_completed` (`patient_id` ASC) ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
