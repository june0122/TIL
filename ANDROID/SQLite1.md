# 안드로이드 데이터베이스 프로그래밍

## 1. 데이터베이스

### 앱의 실행 여부와 관계없이 유지되어야 하는 데이터 -> 영구적 보관이 가능한 저장소에 저장해야 삭제 방지 가능

> 이를 위한 방법들

- SharedPreferences를 사용하여 앱의 설정 정보와 같은 간단한 데이터를 보관
- 파일 입출력 API를 통해 기기의 저장소에 데이터를 직접 읽고 쓰기
- SQL을 사용하여 로컬 데이터베이스에 데이터를 관리하는 방법
- 인터넷을 통해 연결된 서버와 데이터를 주고 받기

> 가장 범용적이고 보편적인 방법은 데이터베이스를 사용하여 데이터를 관리하는 방법

- SharedPrefrences의 key-value 저장 형식의 한계점 극복
- 파일 입출력 방법의 저장 형식 설계에 따른 복잡성 해소
- 서버 통신 방법에서 요구되는 서버 구축 비용 절감

### 1.1 데이터베이스 관리 시스템 (Database Management System, DBMS)

데이터베이스를 만들고 데이터를 저장하거나 관리할 수 있도록 만들어주는 소프트웨어

- Oracle
- **Mysql** : 다른 프레임워크에 비해 성능과 용량 측면에서 존재하는 안드로이드에서 기본적으로 사용
- MS-SQL
- SQLite

<br>

## 2. SQLite

- 서버 단위에서 대용량 데이터를 처리하기 위한 용도보다는, 단일 응용 프로그램에서 비교적 적은 용량의 데이터를 처리하는데 적합한 DSMS.
  - 즉, 안드로이드 앱과 같은 소규모 프로그램에서 데이터베이스 관리가 필요한 경우 사용하기 적합
  
- 별도의 복잡한 시스템, 서비스, 프로그램이 요구되지 않으며, SQLite를 위한 API 함수를 호출하는 것만으로 DB 관련 기능을 사용 가능
  - DB API를 통해 관리되는 모든 데이터는 하나의 파일에 저장되므로 SQLite의 DB 백업 작업은 하나의 DB 저장 파일을 복사하는 것으로 끝난다.

<br>

## 3. 관계형 데이터베이스 (Realtional Database)

안드로이드에서 기본적으로 제공하는 SQLite는 관계형 데이터베이스 관리시스템 <sup>RDBMS</sup>의 한 종류이다.

> 관계형 데이터베이스 이론의 용어들

- 관계(relation) = 테이블(table)
- 속성(attribute) = 컬럼(column) = 필드(field)
- 튜플(tuple) = 로우(row)

### 3.1 스키마 (Schema)

- 데이터베이스의 테이블 구조 및 형식, 관계 등의 정보를 형식 언어 <sup>formal language</sup>로 기술한 것
- 데이터베이스의 설계도와 같은 역할을 수행하며 다른 시스템에 똑같은 데이터 베이스를 만들 수 있다.

### 3.2 SQL (Structured Query Language)

- SQL(Structured Query Language)은 관계형 데이터베이스 관리 시스템에서 데이터를 관리하기 위해 사용되는 표준 프로그래밍 언어(Language)이다.
  - C와 Java와 같이 프로그램을 개발하기 위한 목적이 아닌, 데이터베이스를 관리하기 위해 사용되는 특수 목적 언어

- SQL은 **대화식**으로 동작, 하나의 기능을 수행하면 실행 결과를 확인한 다음 다른 기능을 실행하는 형태로 동작
  - 예로, 테이블에 데이터를 추가하고 조회하는 경우, INSERT 명령으로 시작되는 SQL 문장을 실행하고 그 결과가 리턴되면, 그 다음 데이터 조회를 위한 SELECT 명령을 실행할 수 있다.

> ### SQL에 정의된 세 종류의 명령어 : 데이터 정의 언어, 데이터 조작 언어, 데이터 제어 언어

1. 데이터 정의 언어 (DDL, Data Definition Language)
   - 데이터의 구조를 정의
   - 테이블, 인덱스, 트리거 등의 개체를 만들고 관리하는데 사용되는 명령들이 데이터 정의 언어에 속한다.

        |  DML   | 설명                                   |
        |:------:|:-------------------------------------|
        | CREATE | 테이블, 인덱스 등의 데이터베이스 개체 생성             |
        | ALTER  | CREATE로 생성된 테이블, 인덱스 등의 데이터베이스 개체 수정 |
        |  DROP  | CREATE 명령으로 생성된 데이터베이스 개체 삭제         |

        ```sql
        /* CREATE 명령으로 FRUITSHOP_T 테이블 생성. */
        CREATE TABLE FRUITSHOP_T (NAME TEXT NOT NULL, COUNT INTEGER) ;

        /* ALTER 명령으로 FRUITSHOP_T 테이블에 새로운 컬럼(COLUMN) 추가. */
        ALTER TABLE FRUITSHOP_T ADD SIZE INTEGER ;

        /* DROP 명령으로 FRUITSHOP_T 테이블 삭제. */
        DROP TABLE FRUITSHOP_T ;
        ```

2. 데이터 조작 언어 (DML, Data Manipulation Language)

    - 데이터 정의 언어로 생성된 개체에 데이터를 추가하거나 수정, 삭제, 조회 등의 기능을 수행할 때 사용

        |  DML   | 설명                     |
        |:------:|:-----------------------|
        | INSERT | 테이블에 하나 이상의 데이터 추가     |
        | UPDATE | 테이블에 저장된 하나 이상의 데이터 수정 |
        | DELETE | 테이블의 데이터 삭제            |
        | SELECT | 테이블에 저장된 데이터 조회        |

        ```sql
        /* INSERT 문을 사용하여 FRUITSHOP_T 테이블에 데이터 추가. */
        INSERT INTO FRUITSHOP_T (NAME, COUNT) VALUES ('apple', 2) ;

        /* UPDATE 문으로 FRUITSHOP_T 테이블 데이터 수정. */
        UPDATE FRUITSHOP_T SET COUNT=3 WHERE NAME='apple' ;

        /* DELETE 명령으로 FRUITSHOP_T 테이블의 데이터 삭제. */
        DELETE FROM FRUITSHOP_T WHERE NAME='apple' ;

        /* SELECT 명령으로 FRUITSHOP_T 테이블의 데이터 조회 */
        SELECT * FROM FRUITSHOP_T ;
        ```

3. 데이터 제어 언어 (DCL, Data Control Language)

    - 데이터를 다루는데 있어 권한을 설정하거나, 데이터의 무결성 처리 등을 수행하는데 사용되는 언어

        |   DML    | 설명                                 |
        |:--------:|:-----------------------------------|
        |  GRANT   | 데이터베이스 개체(테이블, 인덱스 등)에 대한 사용 권한 설정 |
        |  BEGIN   | 트랜잭션 시작                            |
        |  COMMIT  | 트랜잭션 내의 실행 결과 적용                   |
        | ROLLBACK | 트랜잭션의 실행 취소                        |

        ```sql
        /* GRANT 명령을 사용하여 FRUITSHOP_T 테이블 권한 설정. */
        GRANT ALL ON FRUITSHOP_T TO PUBLIC ;

        /* COMMIT 멸영으로 트랜잭션 실행 결과 적용. */
        BEGIN TRANSACTION ;
        INSERT INTO FRUITSHOP_T (NAME, COUNT) VALUES ('banana', 3) ;
        INSERT INTO FRUITSHOP_T (NAME, COUNT) VALUES ('orange', 5) ;
        COMMIT TRANSACTION ;

        /* ROLLBACK 명령으로 트랜잭션 취소. */
        BEGIN TRANSACTION ;
        DELETE FROM FRUITSHOP_T ;
        ROLLBACK TRANSACTION ;
        ```
