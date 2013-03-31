/** Create the Encounters table **/
CREATE TABLE Encounters
  (
    encounter_id NUMBER NOT NULL ,
    MRN          NUMBER NOT NULL ,
    location_name CLOB DEFAULT NULL ,
    admit_date          DATE DEFAULT NULL ,
    discharge_date      DATE DEFAULT NULL ,
    Patients_patient_id NUMBER NOT NULL
  ) ;
CREATE UNIQUE INDEX Encounters_MRN_id_IDX ON Encounters
  (
    MRN ASC , encounter_id ASC
  )
  ;
  CREATE INDEX Encounters_MRN_IDX ON Encounters
    ( MRN ASC
    ) ;
  ALTER TABLE Encounters ADD CONSTRAINT Encounters_PK PRIMARY KEY
  (
    encounter_id
  )
  ;

/** Create the Patients Table **/
CREATE TABLE Patients
  (
    patient_id NUMBER NOT NULL ,
    MRN        NUMBER NOT NULL ,
    first_name CLOB DEFAULT NULL ,
    last_name CLOB DEFAULT NULL ,
    date_of_birth CLOB DEFAULT NULL ,
    sex CHAR DEFAULT NULL ,
    address CLOB DEFAULT NULL ,
    ssn CLOB DEFAULT NULL
  ) ;

CREATE UNIQUE INDEX Patients_MRN_IDX ON Patients
  (
    MRN ASC
  )
  ;
  ALTER TABLE Patients ADD CONSTRAINT Patients_PK PRIMARY KEY
  (
    patient_id
  )
  ;

/** Messages Completed (already processed successfully) **/
CREATE TABLE message_completed
  (
    message_id        NUMBER NOT NULL ,
    message_date_time DATE DEFAULT NULL ,
    MESSAGE CLOB DEFAULT NULL ,
    message_control_id CLOB DEFAULT NULL ,
    process_status CHAR NOT NULL ,
    process_error CLOB ,
    process_time             DATE NOT NULL ,
    message_queue_message_id NUMBER ,
    Encounters_encounter_id  NUMBER ,
    Patients_patient_id      NUMBER
  ) ;
CREATE INDEX mc_mq_id_IDX ON message_completed
  (
    message_queue_message_id ASC
  ) ;
CREATE INDEX mc_Encounters_id_IDX ON message_completed
  (
    Encounters_encounter_id ASC
  ) ;
ALTER TABLE message_completed ADD CONSTRAINT mc_PK PRIMARY KEY
(
  message_id
)
;

/** Messages that failed processing for some reason **/
CREATE TABLE message_failed
  (
    message_id        NUMBER NOT NULL ,
    message_date_time DATE DEFAULT NULL ,
    MESSAGE CLOB DEFAULT NULL ,
    message_control_id CLOB DEFAULT NULL ,
    process_status CHAR NOT NULL ,
    process_error CLOB ,
    process_time             DATE NOT NULL ,
    message_queue_message_id NUMBER ,
    Encounters_encounter_id  NUMBER ,
    Patients_patient_id      NUMBER
  ) ;
CREATE INDEX mf_mq_id_IDX ON message_failed
  ( message_queue_message_id ASC
  ) ;
CREATE INDEX mf_Encounters_id_IDX ON message_failed
  (
    Encounters_encounter_id ASC
  ) ;
ALTER TABLE message_failed ADD CONSTRAINT mf_PK PRIMARY KEY
(
  message_id
)
;

/** Message queue - server inserts stuff here for later processing **/
CREATE TABLE message_queue
  (
    message_id        NUMBER NOT NULL ,
    message_date_time DATE DEFAULT NULL ,
    MESSAGE CLOB DEFAULT NULL ,
    message_control_id CLOB DEFAULT NULL ,
    process_status CHAR NOT NULL ,
    process_error CLOB ,
    process_time DATE NOT NULL
  ) ;
ALTER TABLE message_queue ADD CONSTRAINT mq_PK PRIMARY KEY
(
  message_id
)
;

/** 
    Foreign key constraints
*/
ALTER TABLE Encounters ADD CONSTRAINT Encounters_Patient_FK FOREIGN KEY ( Patients_patient_id ) REFERENCES Patients ( patient_id ) NOT DEFERRABLE ;

ALTER TABLE message_completed ADD CONSTRAINT mc_Patients_FK FOREIGN KEY ( Patients_patient_id ) REFERENCES Patients ( patient_id ) NOT DEFERRABLE ;

ALTER TABLE message_completed ADD CONSTRAINT mc_mq_FK FOREIGN KEY ( message_queue_message_id ) REFERENCES message_queue ( message_id ) NOT DEFERRABLE ;

ALTER TABLE message_failed ADD CONSTRAINT mf_Patients_FK FOREIGN KEY ( Patients_patient_id ) REFERENCES Patients ( patient_id ) NOT DEFERRABLE ;

ALTER TABLE message_failed ADD CONSTRAINT mf_mq_FK FOREIGN KEY ( message_queue_message_id ) REFERENCES message_queue ( message_id ) NOT DEFERRABLE ;

