/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : suhui

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 10/04/2020 21:38:47
*/

-- 商户表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_merchant
-- ----------------------------
DROP TABLE IF EXISTS `tob_order_merchant`;
CREATE TABLE `tob_order_merchant` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
  `merchant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户名称',
  `country_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户国家码',
  `online_state` int DEFAULT '0' COMMENT '在线状态  0 离线 1在线',
  `merchant_state` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'to_audit' COMMENT '商户状态(待审核(to_audit)、正常接单(normal)、余额不足(lack_balance)、禁止接单(ban_order))',
  `merchant_rate` double DEFAULT '0' COMMENT '费率',
  `can_use_limit` double(11,0) NOT NULL DEFAULT '0' COMMENT '可用额度',
  `used_limit` double(11,0) NOT NULL DEFAULT '0' COMMENT '已用额度',
  `total_limit` double(11,0) NOT NULL DEFAULT '0' COMMENT '总额度=已用额度+可用额度',
  `merchant_strategy` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'one' COMMENT '策略(单一制(one)、多单制(more))',
  `pay_lock_money` double(11,0) NOT NULL DEFAULT '0' COMMENT '支付锁定金额',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `merchant_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
  `ensure_money` double DEFAULT '0' COMMENT '保证金',
  `lease_money` double DEFAULT '0' COMMENT '租赁金余额',
  `ensure_proportion` double DEFAULT '1' COMMENT '保证金比例',
  `merchant_picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '照片',
  `card_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '1' COMMENT '证件类型 1-身份证 2-军官证 3-护照 4-驾照',
  `card_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '证件号码',
  `card_front_picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '证件正面照',
  `card_back_picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '证件反面照',
  `card_hold_picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手持证件照',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;


-- 商户 金额变更表

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_merchant_money_change
-- ----------------------------
DROP TABLE IF EXISTS `tob_order_merchant_money_change`;
CREATE TABLE `tob_order_merchant_money_change` (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `merchant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户ID',
  `merchant_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户名称',
  `merchant_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
  `change_money` double DEFAULT '0' COMMENT '变动金额',
  `change_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '增加、减少',
  `change_class` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '变动种类 保证金、租赁金',
  `flag` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'init' COMMENT '标记 待处理、已处理',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `change_text` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `order_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单ID',
  `order_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单编号',
  `error_text` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '报错异常',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

--订单表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tob_order_main
-- ----------------------------
DROP TABLE IF EXISTS `tob_order_main`;
CREATE TABLE `tob_order_main`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `order_state` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '订单状态(待分配1、待用户支付2、待承兑商收款3、待承兑商兑付4、待用户收款5、已完成6、已作废0)',
  `user_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户姓名',
  `user_contact` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户联系方式',
  `source_currency` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '源币种',
  `target_currency` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标币种',
  `source_currency_money` double NOT NULL DEFAULT 0 COMMENT '源币种金额',
  `target_currency_money` double NOT NULL DEFAULT 0 COMMENT '目标币种兑付金额',
  `exchange_rate` double NOT NULL DEFAULT 0 COMMENT '汇率',
  `user_pay_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'alipay' COMMENT '用户支付方式(支付宝alipay、银行卡bank_card)',
  `user_pay_area_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户支付区域区号',
  `user_pay_account` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户支付账号',
  `user_pay_account_user` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户支付账号真实姓名',
  `user_pay_bank` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户支付账号开户行',
  `user_pay_bank_branch` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户支付账号开户网点',
  `order_text` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

