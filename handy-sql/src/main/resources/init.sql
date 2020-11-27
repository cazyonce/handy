create database if not exists handy;

CREATE TABLE `api_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) NOT NULL,
  `method` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL DEFAULT '',
  `describe` varchar(255) NOT NULL DEFAULT '',
  `response_code` int(11) NOT NULL,
  `response_reason` varchar(150) NOT NULL DEFAULT '',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0（禁用）|1（启用）',
  `execute_sql` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `api_header` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_mapping_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` varchar(255) NOT NULL DEFAULT '',
  `header_type` varchar(10) NOT NULL COMMENT 'REUQEST|RESPONSE',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=utf8mb4;