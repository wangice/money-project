SET GLOBAL innodb_file_per_table=1;
SET GLOBAL innodb_file_format=Barracuda;

CREATE TABLE db_usr(
  `id` INT(8) NOT NULL AUTO_INCREMENT,
  `phone` CHAR(13) NOT NULL COMMENT '电话号码',
  `account` VARCHAR(20) DEFAULT NULL COMMENT '用户账号',
  `usr_name` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
  `pwd` VARCHAR(150) DEFAULT NULL COMMENT '密码',
  `role` INT(2) COMMENT '角色',
  `identity` CHAR(18) DEFAULT NULL COMMENT '身份证号码',
  `identity_status` INT(2) COMMENT '身份证状态（0，未认证, 1,正常）',
  `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
  `pic_big` VARCHAR(100) DEFAULT NULL COMMENT '大图片路径',
  `pic_small` VARCHAR(100) DEFAULT NULL COMMENT '小图片路径',
  `create_time` DATETIME  NOT NULL ,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`)
)ENGINE=INNODB DEFAULT CHARSET=utf8 KEY_BLOCK_SIZE=4 ROW_FORMAT=COMPRESSED DATA DIRECTORY = '/opt/ice/mysql/innodb'

-- 临时存放文件
CREATE TABLE db_temp_file(
`id` INT(8) NOT NULL AUTO_INCREMENT,
`path` VARCHAR(100) NOT NULL COMMENT '文件存放路径',
`w` TINYINT(1) COMMENT '写入完成 ? true : false',
`dat` BLOB COMMENT '文件正文',
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8 KEY_BLOCK_SIZE=4 ROW_FORMAT=COMPRESSED


SELECT VERSION();