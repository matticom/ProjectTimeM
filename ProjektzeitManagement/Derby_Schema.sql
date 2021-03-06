CREATE TABLE CUSTOMER (Customer_ID INTEGER NOT NULL, Customer_Name VARCHAR(255), PRIMARY KEY (Customer_ID))
CREATE TABLE EMPLOYEE (Employee_ID INTEGER NOT NULL, Employee_FirstName VARCHAR(255), Employee_LastName VARCHAR(255), PRIMARY KEY (Employee_ID))
CREATE TABLE PROJECT (Project_ID INTEGER NOT NULL, Project_EndDate BIGINT, Project_Name VARCHAR(255), Project_StartDate BIGINT, Customer_ID_FK INTEGER, PRIMARY KEY (Project_ID))
CREATE TABLE WORKINGTIME (WorkingTime_ID INTEGER NOT NULL, WorkingTime_BreakTime_Seconds INTEGER, WorkingTime_EndTime BIGINT, WorkingTime_StartTime BIGINT, Employee_ID_FK INTEGER, Project_ID_FK INTEGER, PRIMARY KEY (WorkingTime_ID))
CREATE TABLE PROJECT_EMPLOYEES (Employee_ID INTEGER NOT NULL, Project_ID INTEGER NOT NULL, PRIMARY KEY (Employee_ID, Project_ID))
ALTER TABLE PROJECT ADD CONSTRAINT FK_PROJECT_Customer_ID_FK FOREIGN KEY (Customer_ID_FK) REFERENCES CUSTOMER (Customer_ID)
ALTER TABLE WORKINGTIME ADD CONSTRAINT FK_WORKINGTIME_Project_ID_FK FOREIGN KEY (Project_ID_FK) REFERENCES PROJECT (Project_ID)
ALTER TABLE WORKINGTIME ADD CONSTRAINT FK_WORKINGTIME_Employee_ID_FK FOREIGN KEY (Employee_ID_FK) REFERENCES EMPLOYEE (Employee_ID)
ALTER TABLE PROJECT_EMPLOYEES ADD CONSTRAINT FK_PROJECT_EMPLOYEES_Project_ID FOREIGN KEY (Project_ID) REFERENCES PROJECT (Project_ID)
ALTER TABLE PROJECT_EMPLOYEES ADD CONSTRAINT FK_PROJECT_EMPLOYEES_Employee_ID FOREIGN KEY (Employee_ID) REFERENCES EMPLOYEE (Employee_ID)
DELETE FROM SEQUENCE WHERE SEQ_NAME = SEQ_GEN
SELECT * FROM SEQUENCE WHERE SEQ_NAME = SEQ_GEN
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values (SEQ_GEN, 0)