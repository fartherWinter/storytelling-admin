# ERP系统数据库脚本

## 文件说明

本目录包含ERP系统所需的数据库脚本文件：

- `erp_tables.sql`: 包含系统所有表结构的创建脚本，定义了产品、供应商、客户、销售订单等核心业务实体的数据表结构。
- `erp_init_data.sql`: 包含系统初始化数据，为系统提供基础的测试数据。
- `erp_workflow_tables.sql`: 包含工作流审核相关的表结构创建脚本，定义了流程定义、流程实例、流程任务等工作流相关的数据表结构。

## 表结构说明

系统主要包含以下数据表：

1. **erp_product**: 产品信息表，存储产品的基本信息。
2. **erp_supplier**: 供应商信息表，存储供应商的基本信息。
3. **erp_customer**: 客户信息表，存储客户的基本信息。
4. **erp_sales_order**: 销售订单表，存储销售订单的基本信息。
5. **erp_sales_order_detail**: 销售订单明细表，存储销售订单的产品明细。
6. **erp_product_inventory**: 产品库存表，存储产品的库存信息。
7. **erp_supplier_evaluation**: 供应商评估记录表，存储对供应商的评估记录。
8. **erp_product_category**: 产品类别表，存储产品的分类信息。

### 工作流相关表

9. **erp_workflow_task**: 流程审核任务表，存储流程任务的基本信息。
10. **erp_workflow_instance**: 流程实例表，存储流程实例的基本信息。
11. **erp_workflow_definition**: 流程定义表，存储流程定义的基本信息。
12. **erp_workflow_history**: 流程历史记录表，存储流程操作的历史记录。

## 使用方法

1. 首先执行 `erp_tables.sql` 创建表结构
2. 然后执行 `erp_workflow_tables.sql` 创建工作流相关表结构
3. 最后执行 `erp_init_data.sql` 初始化基础数据

```sql
source /path/to/erp_tables.sql
source /path/to/erp_workflow_tables.sql
source /path/to/erp_init_data.sql
```

## 表关系说明

- 销售订单明细表(erp_sales_order_detail)与销售订单表(erp_sales_order)是一对多关系
- 销售订单明细表(erp_sales_order_detail)与产品表(erp_product)是多对一关系
- 产品库存表(erp_product_inventory)与产品表(erp_product)是一对一关系
- 供应商评估记录表(erp_supplier_evaluation)与供应商表(erp_supplier)是多对一关系

## 注意事项

- 执行脚本前请确保已创建对应的数据库
- 脚本中包含外键约束，请确保按照正确的顺序执行
- 初始数据仅用于测试，生产环境请根据实际情况调整
- 工作流相关表与Flowable引擎的表是分开的，Flowable引擎会自动创建自己的表

## 工作流使用说明

系统集成了Flowable工作流引擎，用于实现业务流程的审批。目前已实现的流程包括：

1. **销售订单审批流程**：销售订单创建后需要经过部门经理、财务审批，金额大于10000元的订单还需要总经理审批。

工作流相关的API接口位于`/workflow`路径下，可以通过这些接口进行流程的部署、启动、查询和处理等操作。