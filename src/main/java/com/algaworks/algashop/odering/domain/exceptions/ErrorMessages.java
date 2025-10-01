package com.algaworks.algashop.odering.domain.exceptions;

public class ErrorMessages {
    public static final String CUSTOMER_EMAIL_INVALID = "";
    public static final String CUSTOMER_BIRTHDATE_INVALID = "";
    public static final String CUSTOMER_FULLANME_INVALID = "";
    public static final String CUSTOMER_DOCUMENT_BLANK = "Document value cannot be blank";
    public static final String CUSTOMER_PHONE_BLANK = "Phone value cannot be blank";
    public static final String CUSTOMER_PHONE_INVALID = "Phone must have at least 8 digits";
    public static final String CUSTOMER_ARCHIVED = "Customer is archived it cannot be changed";

    public static final String EMAIL_BLANK = "Email value cannot be blank";
    public static final String EMAIL_INVALID = "Email value is invalid";
    public static final String ZIPCODE_INVALID = "Invalid zipcode";

    public static final String ORDER_STATUS_CANNOT_BE_CHANGED = "Cannot change order status from %s to %s";
    public static final String ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST = "Order delivery date cannot be in the past";
    public static final String ORDER_CANNOT_BE_PLACED_HAS_NO_ITEMS = "Order with no items cannot be placed";
    public static final String ORDER_CANNOT_BE_PLACED_HAS_NO_BILLING_INFO = "Order with no billing info cannot be placed";
    public static final String ORDER_CANNOT_BE_PLACED_HAS_NO_SHIPPING = "Order with no shipping cost info cannot be placed";
    public static final String ORDER_CANNOT_BE_PLACED_HAS_NO_PAYMENT_METHOD = "Order with no payment method cannot be placed";
    public static final String ORDER_CANNOT_BE_PLACED_HAS_NO_DELIVERY_DATE = "Order with no expected delivery date cannot be placed";
    public static final String ORDER_ITEM_DOES_NOT_EXIST = "Order item {} does not exist for Order {}";
    public static final String ORDER_CANNOT_BE_EDITED = "Order cannot be changed. Order id {}, Order status {}";

    public static final String PRODUCT_OUT_OF_STOCK = "Product {} is out of stock";
}
