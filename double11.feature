@double11_promotion
Feature: 雙十一優惠 (Double 11 Promotion)
  As a shopper during Double 11 sale
  I want the system to apply 20% discount for every 10 pieces of the same product
  So that I can get discounts when buying in bulk

  Scenario: 購買 12 件襪子 - 一組享有折扣，剩餘按原價
    Given the Double 11 promotion is active with 20% discount for every 10 pieces of same product
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 12       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1200           | 200      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 12       |

  Scenario: 購買 27 件襪子 - 兩組享有折扣，剩餘按原價
    Given the Double 11 promotion is active with 20% discount for every 10 pieces of same product
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 27       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 2700           | 400      | 2300        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 27       |

  Scenario: 購買 10 件不同商品 - 無折扣因為非同一種商品
    Given the Double 11 promotion is active with 20% discount for every 10 pieces of same product
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 商品A         | 1        | 100       |
      | 商品B         | 1        | 100       |
      | 商品C         | 1        | 100       |
      | 商品D         | 1        | 100       |
      | 商品E         | 1        | 100       |
      | 商品F         | 1        | 100       |
      | 商品G         | 1        | 100       |
      | 商品H         | 1        | 100       |
      | 商品I         | 1        | 100       |
      | 商品J         | 1        | 100       |
    Then the order summary should be:
      | totalAmount |
      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 商品A         | 1        |
      | 商品B         | 1        |
      | 商品C         | 1        |
      | 商品D         | 1        |
      | 商品E         | 1        |
      | 商品F         | 1        |
      | 商品G         | 1        |
      | 商品H         | 1        |
      | 商品I         | 1        |
      | 商品J         | 1        |
      